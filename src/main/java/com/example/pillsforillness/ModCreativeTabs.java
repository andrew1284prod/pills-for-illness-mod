package com.example.pillsforillness;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class ModCreativeTabs {
    public static final CreativeTabs TAB_PILLS = new CreativeTabs(PillsForIllnessMod.MODID) {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(ModItems.MEDICAL_MONITOR);
        }
    };
}
