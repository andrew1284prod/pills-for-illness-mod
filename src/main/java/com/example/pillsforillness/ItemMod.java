package com.example.pillsforillness;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemMod extends Item {
    public ItemMod() {
        super();
        setCreativeTab(ModCreativeTabs.TAB_PILLS);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        String key = getUnlocalizedName(stack) + ".tooltip";
        if (I18n.hasKey(key)) {
            tooltip.add(TextFormatting.ITALIC + I18n.format(key));
        }
    }
}
