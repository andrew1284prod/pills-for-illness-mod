package com.example.pillsforillness;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Random;

@Mod.EventBusSubscriber(modid = PillsForIllnessMod.MODID)
public class EventHandler {
    public static final DamageSource VIRUS_DAMAGE = new DamageSource("virus").setDamageBypassesArmor().setDamageAllowedInCreativeMode();
    public static final DamageSource OVERDOSE_DAMAGE = new DamageSource("overdose").setDamageBypassesArmor().setDamageAllowedInCreativeMode();
    private static final Random RAND = new Random();

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event) {
        World world = event.getWorld();
        if (world.isRemote || world.provider.getDimension() != 0) return;

        PfiWorldData data = PfiWorldData.get(world);
        if (!data.isInitialized()) {
            if (ModConfig.generalSettings.systemMode == ModConfig.SystemMode.ALWAYS_ON) {
                data.setSystemEnabled(true);
            } else {
                data.setSystemEnabled(WorldCreationGuiHandler.pendingSystemEnabled);
            }
            data.setInitialized(true);
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerLoggedInEvent event) {
        World world = event.player.world;
        if (world.isRemote || !PfiWorldData.get(world).isSystemEnabled()) return;

        EntityPlayer player = event.player;
        NBTTagCompound entityData = player.getEntityData();
        NBTTagCompound persistent;

        if (entityData.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
            persistent = entityData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
        } else {
            persistent = new NBTTagCompound();
            entityData.setTag(EntityPlayer.PERSISTED_NBT_TAG, persistent);
        }

        if (!persistent.getBoolean("pfi_first_join_chest")) {
            BlockPos chestPos = new BlockPos(player.posX, player.posY, player.posZ).down();
            world.setBlockState(chestPos, Blocks.CHEST.getDefaultState(), 3);

            TileEntity te = world.getTileEntity(chestPos);
            if (te instanceof TileEntityChest) {
                TileEntityChest chest = (TileEntityChest) te;
                chest.setInventorySlotContents(0, new ItemStack(ModItems.PILL, 1));
                chest.setInventorySlotContents(1, new ItemStack(ModItems.PILL, 1));
                chest.setInventorySlotContents(2, new ItemStack(ModItems.PILL, 1));
            }

            persistent.setBoolean("pfi_first_join_chest", true);
        }
    }

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.PlaceEvent event) {
        if (event.getWorld().isRemote || !PfiWorldData.get(event.getWorld()).isSystemEnabled()) return;
        Block placedBlock = event.getPlacedBlock().getBlock();
        if (placedBlock == Blocks.IRON_ORE) {
            PlayerPlacedBlocksData data = PlayerPlacedBlocksData.get(event.getWorld());
            data.addPlacedBlock(event.getPos());
        }
    }

    @SubscribeEvent
    public static void onBlockHarvest(BlockEvent.HarvestDropsEvent event) {
        World world = event.getWorld();
        if (world.isRemote || !PfiWorldData.get(world).isSystemEnabled()) return;

        Block stateBlock = event.getState().getBlock();
        BlockPos pos = event.getPos();

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

        if (stateBlock == Blocks.IRON_ORE) {
            PlayerPlacedBlocksData data = PlayerPlacedBlocksData.get(world);
            if (data.isPlacedByPlayer(pos)) {
                data.removePlacedBlock(pos);
            } else {
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
        if (!PfiWorldData.get(event.player.world).isSystemEnabled()) return;

        EntityPlayer player = event.player;

        if (ModConfig.permadeathSettings.enablePermadeath && player instanceof EntityPlayerMP) {
            EntityPlayerMP mp = (EntityPlayerMP) player;
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

        IPlayerTimer timer = player.getCapability(CapabilityPlayerTimer.TIMER_CAP, null);
        if (timer != null) {
            timer.tick(player.world, player);
        }

        NBTTagCompound entityData = player.getEntityData();
        NBTTagCompound persistent;

        if (entityData.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
            persistent = entityData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
        } else {
            persistent = new NBTTagCompound();
            entityData.setTag(EntityPlayer.PERSISTED_NBT_TAG, persistent);
        }

        if (!persistent.getBoolean("pfi_had_carrot")) {
            boolean hasCarrot = false;
            for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                ItemStack stack = player.inventory.getStackInSlot(i);
                if (!stack.isEmpty() && stack.getItem() == Items.CARROT) {
                    hasCarrot = true;
                    break;
                }
            }
            if (hasCarrot) {
                persistent.setBoolean("pfi_had_carrot", true);
            }
        }

        if (ModConfig.monitorSettings.enableMedicalMonitor && !persistent.getBoolean("pfi_monitor_dropped")) {
            if (!persistent.hasKey("pfi_start_x")) {
                persistent.setDouble("pfi_start_x", player.posX);
                persistent.setDouble("pfi_start_y", player.posY);
                persistent.setDouble("pfi_start_z", player.posZ);
            } else {
                double startX = persistent.getDouble("pfi_start_x");
                double startY = persistent.getDouble("pfi_start_y");
                double startZ = persistent.getDouble("pfi_start_z");

                double distSq = player.getDistanceSq(startX, startY, startZ);
                if (distSq >= 25.0) {
                    persistent.setBoolean("pfi_monitor_dropped", true);
                    float damage = ModConfig.monitorSettings.monitorDropDamage;
                    if (damage > 0.0F) {
                        player.attackEntityFrom(DamageSource.GENERIC, damage);
                    }
                    player.dropItem(new ItemStack(ModItems.MEDICAL_MONITOR, 1), true);
                    player.sendMessage(new TextComponentTranslation("msg.pills.monitor_dropped"));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onZombieDrops(LivingDropsEvent event) {
        World world = event.getEntityLiving().world;
        if (world.isRemote || !PfiWorldData.get(world).isSystemEnabled()) return;
        if (!ModConfig.dropSettings.guaranteeCarrotDrop) return;

        if (event.getEntityLiving() instanceof EntityZombie) {
            EntityZombie zombie = (EntityZombie) event.getEntityLiving();
            if (event.getSource().getTrueSource() instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
                NBTTagCompound entityData = player.getEntityData();
                NBTTagCompound persistent = entityData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);

                if (!persistent.getBoolean("pfi_had_carrot")) {
                    boolean hasCarrotInDrops = false;
                    for (EntityItem drop : event.getDrops()) {
                        if (drop.getItem().getItem() == Items.CARROT) {
                            hasCarrotInDrops = true;
                            break;
                        }
                    }
                    if (!hasCarrotInDrops) {
                        event.getDrops().add(new EntityItem(
                            world,
                            zombie.posX,
                            zombie.posY,
                            zombie.posZ,
                            new ItemStack(Items.CARROT, 1)
                        ));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof EntityPlayer) || event.getEntity().world.isRemote) return;
        World world = event.getEntity().world;
        if (!PfiWorldData.get(world).isSystemEnabled()) return;

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
        if (!PfiWorldData.get(event.player.world).isSystemEnabled()) return;

        EntityPlayer player = event.player;
        NBTTagCompound data = player.getEntityData();
        NBTTagCompound persistent = data.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);

        if (persistent.getBoolean("pfi_perma_dead") && player instanceof EntityPlayerMP) {
            ((EntityPlayerMP) player).setGameType(GameType.SPECTATOR);
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        EntityPlayer original = event.getOriginal();
        EntityPlayer clone = event.getEntityPlayer();

        NBTTagCompound origData = original.getEntityData();
        if (origData.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
            clone.getEntityData().setTag(EntityPlayer.PERSISTED_NBT_TAG, origData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).copy());
        }

        IPlayerTimer oldTimer = original.getCapability(CapabilityPlayerTimer.TIMER_CAP, null);
        IPlayerTimer newTimer = clone.getCapability(CapabilityPlayerTimer.TIMER_CAP, null);

        if (oldTimer != null && newTimer != null) {
            newTimer.setTimer(oldTimer.getTimer());
            newTimer.setLastPillTime(oldTimer.getLastPillTime());
            newTimer.setPermaDead(oldTimer.isPermaDead());
        }
    }
}
