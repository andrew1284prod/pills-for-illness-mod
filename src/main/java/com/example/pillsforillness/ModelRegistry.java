package com.example.pillsforillness;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = PillsForIllnessMod.MODID, value = Side.CLIENT)
public class ModelRegistry {
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        registerModel(ModItems.WHITE_PART);
        registerModel(ModItems.ORANGE_PART);
        registerModel(ModItems.MEDICINE_POWDER);
        registerModel(ModItems.PILL);
        registerModel(ModItems.BUFFERED_PILL);
        registerModel(ModItems.NEUTRALIZER);
    }

    private static void registerModel(Item item) {
        if (item == null) return;
        ModelLoader.setCustomModelResourceLocation(
            item,
            0,
            new ModelResourceLocation(item.getRegistryName(), "inventory")
        );
    }
}
