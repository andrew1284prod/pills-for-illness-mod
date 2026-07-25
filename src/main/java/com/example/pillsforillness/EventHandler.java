package com.example.pillsforillness;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.init.Blocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;

import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import java.util.Random;

@Mod.EventBusSubscriber(modid = PillsForIllnessMod.MODID)
public class EventHandler {
    public static final DamageSource VIRUS_DAMAGE = new DamageSource("virus").setDamageBypassesArmor().setDamageAllowedInCreativeMode();
    public static final DamageSource OVERDOSE_DAMAGE = new DamageSource("overdose").setDamageBypassesArmor().setDamageAllowedInCreativeMode();
    private static final Random RAND = new Random();

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.PlaceEvent event) {
        if (event.getWorld().isRemote) return;
        Block placedBlock = event.getPlacedBlock().getBlock();
        if (placedBlock == Blocks.IRON_ORE) {
            PlayerPlacedBlocksData data = PlayerPlacedBlocksData.get(event.getWorld());
            data.addPlacedBlock(event.getPos());
        }
    }

    @SubscribeEvent
    public static void onBlockHarvest(BlockEvent.HarvestDropsEvent event) {
        if (event.getWorld().isRemote) return;

        Block stateBlock = event.getState().getBlock();
        World world = event.getWorld();
        BlockPos pos = event.getPos();

        // 1. Логика для Камня (Stone)
        if (stateBlock == Blocks.STONE && event.getState().getValue(BlockStone.VARIANT) == BlockStone.EnumType.STONE) {
            double randVal = RAND.nextDouble();
            double c2 = ModConfig.dropSettings.neutralizerChance2;
            double c1 = ModConfig.dropSettings.neutralizerChance1;

            if (c2 > 0 && randVal < c2) {
                if (ModConfig.dropSettings.cancelCobblestoneDrop) {
                    event.getDrops().clear();
                }
                event.getDrops().add(new ItemStack(ModItems.NEUTRALIZER, 2));
            } else if (c1 > 0 && randVal < (c2 + c1)) {
                if (ModConfig.dropSettings.cancelCobblestoneDrop) {
                    event.getDrops().clear();
                }
                event.getDrops().add(new ItemStack(ModItems.NEUTRALIZER, 1));
            }
        }

        // 2. Логика для Железной Руды (Iron Ore)
        if (stateBlock == Blocks.IRON_ORE) {
            PlayerPlacedBlocksData data = PlayerPlacedBlocksData.get(world);
            if (data.isPlacedByPlayer(pos)) {
                data.removePlacedBlock(pos); // Блок сломан, удаляем из базы
            } else {
                // Блок сгенерирован натурально
                double chance = ModConfig.dropSettings.medicinePowderChance;
                if (chance > 0 && RAND.nextDouble() < chance) {
                    event.getDrops().add(new ItemStack(ModItems.MEDICINE_POWDER, 1));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.START || event.player.world.isRemote) return;

        if (ModConfig.permadeathSettings.enablePermadeath && event.player instanceof EntityPlayerMP) {
            EntityPlayerMP mp = (EntityPlayerMP) event.player;
            if (mp.isSpectator()) return;

            NBTTagCompound data = mp.getEntityData();
            NBTTagCompound persistent = data.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
            if (persistent.getBoolean("pfi_perma_dead")) {
                mp.setGameType(GameType.SPECTATOR);
                return;
            }

            IPlayerTimer timer = mp.getCapability(CapabilityPlayerTimer.TIMER_CAP, null);
            if (timer != null && timer.isPermaDead()) {
                mp.setGameType(GameType.SPECTATOR);
                return;
            }
        }

        IPlayerTimer timer = event.player.getCapability(CapabilityPlayerTimer.TIMER_CAP, null);
        if (timer != null) {
            timer.tick(event.player.world, event.player);
        }
    }

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof EntityPlayer) || event.getEntity().world.isRemote) return;
        EntityPlayer player = (EntityPlayer) event.getEntity();
        IPlayerTimer timer = player.getCapability(CapabilityPlayerTimer.TIMER_CAP, null);

        boolean shouldPerma = false;

        if (timer != null) {
            if (event.getSource() != VIRUS_DAMAGE && event.getSource() != OVERDOSE_DAMAGE) {
                if (ModConfig.virusSettings.resetVirusOnDeath) {
                    timer.setTimer(0);
                }
            }

            if (ModConfig.permadeathSettings.enablePermadeath) {
                if (event.getSource() == OVERDOSE_DAMAGE || event.getSource() == VIRUS_DAMAGE) {
                    timer.setPermaDead(true);
                    shouldPerma = true;
                } else if (ModConfig.overdoseSettings.enableOverdose) {
                    PotionEffect activeOverdose = player.getActivePotionEffect(ModPotions.OVERDOSE);
                    if (activeOverdose != null) {
                        int level = activeOverdose.getAmplifier();
                        double[] chances = ModConfig.overdoseSettings.overdoseDeathChances;

                        double chance = (chances != null && chances.length > level) ? chances[level] : 0.0;
                        if (RAND.nextDouble() < chance) {
                            timer.setPermaDead(true);
                            shouldPerma = true;
                        }
                    }
                }
            }
        }

        if (shouldPerma && ModConfig.permadeathSettings.enablePermadeath) {
            NBTTagCompound data = player.getEntityData();
            NBTTagCompound persistent = data.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
            persistent.setBoolean("pfi_perma_dead", true);
            data.setTag(EntityPlayer.PERSISTED_NBT_TAG, persistent);
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerRespawnEvent event) {
        if (event.player.world.isRemote || !ModConfig.permadeathSettings.enablePermadeath) return;
        EntityPlayer player = event.player;

        NBTTagCompound data = player.getEntityData();
        NBTTagCompound persistent = data.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);

        if (persistent.getBoolean("pfi_perma_dead") && player instanceof EntityPlayerMP) {
            ((EntityPlayerMP) player).setGameType(GameType.SPECTATOR);
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(net.minecraftforge.event.entity.player.PlayerEvent.Clone event) {
        EntityPlayer original = event.getOriginal();
        EntityPlayer clone = event.getEntityPlayer();

        IPlayerTimer oldTimer = original.getCapability(CapabilityPlayerTimer.TIMER_CAP, null);
        IPlayerTimer newTimer = clone.getCapability(CapabilityPlayerTimer.TIMER_CAP, null);

        if (oldTimer != null && newTimer != null) {
            newTimer.setTimer(oldTimer.getTimer());
            newTimer.setLastPillTime(oldTimer.getLastPillTime());
            newTimer.setPermaDead(oldTimer.isPermaDead());
        }
    }
}
