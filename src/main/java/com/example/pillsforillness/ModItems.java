package com.example.pillsforillness;

import net.minecraft.creativetab.CreativeTabs;
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

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        WHITE_PART = new Item()
            .setUnlocalizedName("whitepart")
            .setRegistryName(PillsForIllnessMod.MODID, "whitepart")
            .setCreativeTab(CreativeTabs.MISC);

        ORANGE_PART = new Item()
            .setUnlocalizedName("orangepart")
            .setRegistryName(PillsForIllnessMod.MODID, "orangepart")
            .setCreativeTab(CreativeTabs.MISC);

        MEDICINE_POWDER = new Item()
            .setUnlocalizedName("medicinepowder")
            .setRegistryName(PillsForIllnessMod.MODID, "medicinepowder")
            .setCreativeTab(CreativeTabs.MISC);

        PILL = new PillItem()
            .setUnlocalizedName("pill")
            .setRegistryName(PillsForIllnessMod.MODID, "pill")
            .setCreativeTab(CreativeTabs.MISC);

        BUFFERED_PILL = new BufferedPillItem()
            .setUnlocalizedName("bufferedpill")
            .setRegistryName(PillsForIllnessMod.MODID, "bufferedpill")
            .setCreativeTab(CreativeTabs.MISC);

        NEUTRALIZER = new Item()
            .setUnlocalizedName("neutralizer")
            .setRegistryName(PillsForIllnessMod.MODID, "neutralizer")
            .setCreativeTab(CreativeTabs.MISC);

        event.getRegistry().registerAll(WHITE_PART, ORANGE_PART, MEDICINE_POWDER, PILL, BUFFERED_PILL, NEUTRALIZER);
    }
}
