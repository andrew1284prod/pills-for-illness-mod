package com.example.pillsforillness;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.*;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = PillsForIllnessMod.MODID, name = "pills-for-illness")
@Config.LangKey("config.pills_for_illness.title")
public class ModConfig {

    public enum SystemMode {
        ALWAYS_ON,
        ASK
    }

    @Name("general settings")
    @LangKey("config.pills_for_illness.general")
    public static GeneralCategory generalSettings = new GeneralCategory();

    @Name("virus settings")
    @LangKey("config.pills_for_illness.virus")
    public static VirusCategory virusSettings = new VirusCategory();

    @Name("overdose settings")
    @LangKey("config.pills_for_illness.overdose")
    public static OverdoseCategory overdoseSettings = new OverdoseCategory();

    @Name("pill settings")
    @LangKey("config.pills_for_illness.pill")
    public static PillCategory pillSettings = new PillCategory();

    @Name("monitor settings")
    @LangKey("config.pills_for_illness.monitor")
    public static MonitorCategory monitorSettings = new MonitorCategory();

    @Name("permadeath settings")
    @LangKey("config.pills_for_illness.permadeath")
    public static PermadeathCategory permadeathSettings = new PermadeathCategory();

    @Name("drop settings")
    @LangKey("config.pills_for_illness.drop")
    public static DropCategory dropSettings = new DropCategory();

    public static class GeneralCategory {
        @Comment({"Режим работы системы при создании мира.", "ALWAYS_ON - Всегда включена, кнопка скрыта.", "ASK - Показывать тумблер в меню создания мира."})
        @LangKey("config.pills_for_illness.general.systemMode")
        public SystemMode systemMode = SystemMode.ASK;
    }

    public static class VirusCategory {
        @LangKey("config.pills_for_illness.virus.resetOnDeath")
        public boolean resetVirusOnDeath = false;

        @LangKey("config.pills_for_illness.virus.deathDamage")
        public float virusDeathDamage = 1000.0F;

        @LangKey("config.pills_for_illness.virus.thresholds")
        public int[] virusStageThresholds = {44000, 56000, 72000, 86000, 96000, 100000};
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
        public int pillHealAmount = 24000;

        @RangeInt(min = 0, max = 240000)
        @LangKey("config.pills_for_illness.pill.bufferedHealAmount")
        public int bufferedPillHealAmount = 10000;

        @RangeInt(min = 0, max = 72000)
        @LangKey("config.pills_for_illness.pill.pillCooldown")
        public int pillCooldown = 60;

        @RangeInt(min = 0, max = 72000)
        @LangKey("config.pills_for_illness.pill.bufferedPillCooldown")
        public int bufferedPillCooldown = 0;
    }

    public static class MonitorCategory {
        @LangKey("config.pills_for_illness.monitor.enable")
        public boolean enableMedicalMonitor = true;

        @RangeInt(min = 0, max = 72000)
        @LangKey("config.pills_for_illness.monitor.cooldown")
        public int monitorCooldown = 200;

        @LangKey("config.pills_for_illness.monitor.dropDamage")
        public float monitorDropDamage = 3.0F;

        @LangKey("config.pills_for_illness.monitor.useDamage")
        public float monitorUseDamage = 1.0F;
    }

    public static class PermadeathCategory {
        @LangKey("config.pills_for_illness.permadeath.enable")
        public boolean enablePermadeath = true;
    }

    public static class DropCategory {
        @RangeDouble(min = 0.0, max = 1.0)
        @LangKey("config.pills_for_illness.drop.neutralizerChance1")
        public double neutralizerChance1 = 0.02;

        @RangeDouble(min = 0.0, max = 1.0)
        @LangKey("config.pills_for_illness.drop.neutralizerChance2")
        public double neutralizerChance2 = 0.005;

        @LangKey("config.pills_for_illness.drop.cancelCobblestone")
        public boolean cancelCobblestoneDrop = true;

        @RangeDouble(min = 0.0, max = 1.0)
        @LangKey("config.pills_for_illness.drop.powderChance")
        public double medicinePowderChance = 0.20;

        @LangKey("config.pills_for_illness.drop.guaranteeCarrotDrop")
        public boolean guaranteeCarrotDrop = true;
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
