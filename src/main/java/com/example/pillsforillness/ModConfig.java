package com.example.pillsforillness;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.*;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = PillsForIllnessMod.MODID, name = "pills-for-illness")
@Config.LangKey("config.pills_for_illness.title")
public class ModConfig {

    @Name("virus settings")
    @LangKey("config.pills_for_illness.virus")
    public static VirusCategory virusSettings = new VirusCategory();

    @Name("overdose settings")
    @LangKey("config.pills_for_illness.overdose")
    public static OverdoseCategory overdoseSettings = new OverdoseCategory();

    @Name("pill settings")
    @LangKey("config.pills_for_illness.pill")
    public static PillCategory pillSettings = new PillCategory();

    @Name("permadeath settings")
    @LangKey("config.pills_for_illness.permadeath")
    public static PermadeathCategory permadeathSettings = new PermadeathCategory();

    @Name("drop settings")
    @LangKey("config.pills_for_illness.drop")
    public static DropCategory dropSettings = new DropCategory();

    public static class VirusCategory {
        @LangKey("config.pills_for_illness.virus.resetOnDeath")
        public boolean resetVirusOnDeath = false;

        @LangKey("config.pills_for_illness.virus.deathDamage")
        public float virusDeathDamage = 1000.0F;

        @LangKey("config.pills_for_illness.virus.thresholds")
        public int[] virusStageThresholds = {36000, 48000, 60000, 72000, 84000, 85200};
    }

    public static class OverdoseCategory {
        @LangKey("config.pills_for_illness.overdose.enable")
        public boolean enableOverdose = true;

        @RangeInt(min = 1, max = 10)
        @LangKey("config.pills_for_illness.overdose.maxLevel")
        public int maxOverdoseLevel = 3;

        @LangKey("config.pills_for_illness.overdose.deathDamage")
        public float overdoseDeathDamage = 1000.0F;

        @LangKey("config.pills_for_illness.overdose.durations")
        public int[] overdoseDurations = {1200, 2400, 3600};

        @LangKey("config.pills_for_illness.overdose.deathChances")
        public double[] overdoseDeathChances = {0.2, 0.4, 0.6};
    }

    public static class PillCategory {
        @RangeInt(min = 0, max = 240000)
        @LangKey("config.pills_for_illness.pill.healAmount")
        public int pillHealAmount = 18000;

        @RangeInt(min = 0, max = 240000)
        @LangKey("config.pills_for_illness.pill.bufferedHealAmount")
        public int bufferedPillHealAmount = 10000;
    }

    public static class PermadeathCategory {
        @LangKey("config.pills_for_illness.permadeath.enable")
        public boolean enablePermadeath = true;
    }

    public static class DropCategory {
        @RangeDouble(min = 0.0, max = 1.0)
        @LangKey("config.pills_for_illness.drop.neutralizerChance1")
        public double neutralizerChance1 = 0.05; // 5%

        @RangeDouble(min = 0.0, max = 1.0)
        @LangKey("config.pills_for_illness.drop.neutralizerChance2")
        public double neutralizerChance2 = 0.02; // 2%

        @LangKey("config.pills_for_illness.drop.cancelCobblestone")
        public boolean cancelCobblestoneDrop = true;

        @RangeDouble(min = 0.0, max = 1.0)
        @LangKey("config.pills_for_illness.drop.powderChance")
        public double medicinePowderChance = 0.50; // 50%
    }

    @Mod.EventBusSubscriber(modid = PillsForIllnessMod.MODID)
    public static class ConfigSyncHandler {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(PillsForIllnessMod.MODID)) {
                net.minecraftforge.common.config.ConfigManager.sync(PillsForIllnessMod.MODID, Config.Type.INSTANCE);
            }
        }
    }
}
