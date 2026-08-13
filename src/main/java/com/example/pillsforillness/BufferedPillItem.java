package com.example.pillsforillness;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class BufferedPillItem extends ItemFood {
    public BufferedPillItem() {
        super(0, 0, false);
        setAlwaysEdible();
        setCreativeTab(ModCreativeTabs.TAB_PILLS);
    }

    @Override
    protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
        if (!worldIn.isRemote) {
            IPlayerTimer timer = player.getCapability(CapabilityPlayerTimer.TIMER_CAP, null);
            if (timer != null) {
                timer.onBufferedPillTaken(worldIn, player);
            }
        }

        if (ModConfig.pillSettings.bufferedPillCooldown > 0) {
            player.getCooldownTracker().setCooldown(this, ModConfig.pillSettings.bufferedPillCooldown);
        }
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
