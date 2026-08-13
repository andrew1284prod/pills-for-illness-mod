package com.example.pillsforillness;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = PillsForIllnessMod.MODID)
public class ModItems {

    public static Item WHITE_PART;
    public static Item ORANGE_PART;
    public static Item MEDICINE_POWDER;
    public static Item PILL;
    public static Item BUFFERED_PILL;
    public static Item NEUTRALIZER;
    public static Item MEDICAL_MONITOR;

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        WHITE_PART = new ItemMod()
            .setUnlocalizedName("whitepart")
            .setRegistryName(PillsForIllnessMod.MODID, "whitepart");

        ORANGE_PART = new ItemMod()
            .setUnlocalizedName("orangepart")
            .setRegistryName(PillsForIllnessMod.MODID, "orangepart");

        MEDICINE_POWDER = new ItemMod()
            .setUnlocalizedName("medicinepowder")
            .setRegistryName(PillsForIllnessMod.MODID, "medicinepowder");

        PILL = new PillItem()
            .setUnlocalizedName("pill")
            .setRegistryName(PillsForIllnessMod.MODID, "pill");

        BUFFERED_PILL = new BufferedPillItem()
            .setUnlocalizedName("bufferedpill")
            .setRegistryName(PillsForIllnessMod.MODID, "bufferedpill");

        NEUTRALIZER = new ItemMod()
            .setUnlocalizedName("neutralizer")
            .setRegistryName(PillsForIllnessMod.MODID, "neutralizer");

        MEDICAL_MONITOR = new MedicalMonitorItem()
            .setUnlocalizedName("medicalmonitor")
            .setRegistryName(PillsForIllnessMod.MODID, "medicalmonitor");

        event.getRegistry().registerAll(WHITE_PART, ORANGE_PART, MEDICINE_POWDER, PILL, BUFFERED_PILL, NEUTRALIZER, MEDICAL_MONITOR);
    }
}
