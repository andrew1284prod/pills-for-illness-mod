package com.example.pillsforillness;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.init.MobEffects;

public class PlayerTimer implements IPlayerTimer {
    private long timer = 0;
    private long lastPillTakenWorldTime = 0;
    private boolean permaDead = false;

    private boolean[] msgSent = new boolean[6];

    @Override public long getTimer() { return timer; }
    @Override public void setTimer(long ticks) { this.timer = ticks; }
    @Override public long getLastPillTime() { return lastPillTakenWorldTime; }
    @Override public void setLastPillTime(long time) { this.lastPillTakenWorldTime = time; }
    @Override public boolean isPermaDead() { return permaDead; }
    @Override public void setPermaDead(boolean dead) { this.permaDead = dead; }

    @Override
    public void tick(World world, EntityPlayer player) {
        if (world.isRemote || !PfiWorldData.get(world).isSystemEnabled()) return;
        timer++;

        PotionEffect virusEffect = player.getActivePotionEffect(ModPotions.VIRUS);
        int currentLevel = (virusEffect != null) ? virusEffect.getAmplifier() : -1;

        int[] thresholds = ModConfig.virusSettings.virusStageThresholds;
        int targetLevel = -1;

        if (thresholds != null && thresholds.length >= 6) {
            for (int i = 5; i >= 0; i--) {
                if (timer >= thresholds[i]) {
                    targetLevel = i;
                    break;
                }
            }
        }

        if (targetLevel != currentLevel && targetLevel >= 0) {
            player.addPotionEffect(new PotionEffect(ModPotions.VIRUS, Integer.MAX_VALUE, targetLevel, true, false));
        }

        if (thresholds != null && thresholds.length >= 6) {
            String[] localizationKeys = {
                "msg.pills.tremor", "msg.pills.hard_to_move", "msg.pills.too_heavy",
                "msg.pills.loud_heart", "msg.pills.dying", "msg.pills.too_late"
            };
            for (int i = 0; i < 6; i++) {
                if (timer >= thresholds[i] && !msgSent[i]) {
                    msgSent[i] = true;
                    player.sendMessage(new TextComponentTranslation(localizationKeys[i]));
                }
            }
        }

        int deathTime = (thresholds != null && thresholds.length >= 6) ? thresholds[5] + 1200 : 86400;
        if (timer >= deathTime) {
            player.attackEntityFrom(EventHandler.VIRUS_DAMAGE, ModConfig.virusSettings.virusDeathDamage);
        }
    }

    @Override
    public void onPillTaken(World world, EntityPlayer player) {
        if (!PfiWorldData.get(world).isSystemEnabled()) return;
        long worldTime = world.getTotalWorldTime();

        ModPotions.allowVirusRemoval = true;
        player.removePotionEffect(ModPotions.VIRUS);
        ModPotions.allowVirusRemoval = false;

        player.removePotionEffect(MobEffects.MINING_FATIGUE);
        player.removePotionEffect(MobEffects.SLOWNESS);
        player.removePotionEffect(MobEffects.WEAKNESS);
        player.removePotionEffect(MobEffects.POISON);
        player.removePotionEffect(MobEffects.NAUSEA);
        player.removePotionEffect(MobEffects.BLINDNESS);

        timer = Math.max(0, timer - ModConfig.pillSettings.pillHealAmount);

        int[] thresholds = ModConfig.virusSettings.virusStageThresholds;
        if (thresholds != null && thresholds.length >= 6) {
            for (int i = 0; i < 6; i++) {
                if (timer < thresholds[i]) msgSent[i] = false;
            }
        }

        if (ModConfig.overdoseSettings.enableOverdose) {
            PotionEffect existingOverdose = player.getActivePotionEffect(ModPotions.OVERDOSE);

            if (existingOverdose != null) {
                int currentAmplifier = existingOverdose.getAmplifier();
                int nextLevel = currentAmplifier + 1;

                if (nextLevel >= ModConfig.overdoseSettings.maxOverdoseLevel) {
                    player.attackEntityFrom(EventHandler.OVERDOSE_DAMAGE, ModConfig.overdoseSettings.overdoseDeathDamage);
                    if (ModConfig.permadeathSettings.enablePermadeath) {
                        setPermaDead(true);
                    }
                    return;
                }
                applyOverdoseEffect(player, nextLevel);
            } else {
                applyOverdoseEffect(player, 0);
            }
        }

        lastPillTakenWorldTime = worldTime;
    }

    @Override
    public void onBufferedPillTaken(World world, EntityPlayer player) {
        if (!PfiWorldData.get(world).isSystemEnabled()) return;
        long worldTime = world.getTotalWorldTime();

        ModPotions.allowVirusRemoval = true;
        player.removePotionEffect(ModPotions.VIRUS);
        ModPotions.allowVirusRemoval = false;

        player.removePotionEffect(MobEffects.MINING_FATIGUE);
        player.removePotionEffect(MobEffects.SLOWNESS);
        player.removePotionEffect(MobEffects.WEAKNESS);
        player.removePotionEffect(MobEffects.POISON);
        player.removePotionEffect(MobEffects.NAUSEA);
        player.removePotionEffect(MobEffects.BLINDNESS);

        timer = Math.max(0, timer - ModConfig.pillSettings.bufferedPillHealAmount);

        int[] thresholds = ModConfig.virusSettings.virusStageThresholds;
        if (thresholds != null && thresholds.length >= 6) {
            for (int i = 0; i < 6; i++) {
                if (timer < thresholds[i]) msgSent[i] = false;
            }
        }

        lastPillTakenWorldTime = worldTime;
    }

    private void applyOverdoseEffect(EntityPlayer player, int level) {
        int[] durations = ModConfig.overdoseSettings.overdoseDurations;
        int duration = (durations != null && durations.length > level) ? durations[level] : 1200;
        player.addPotionEffect(new PotionEffect(ModPotions.OVERDOSE, duration, level));
    }
}
