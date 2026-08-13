package com.example.pillsforillness;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class MedicalMonitorItem extends Item {
    public MedicalMonitorItem() {
        super();
        setMaxStackSize(1);
        setCreativeTab(ModCreativeTabs.TAB_PILLS);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);

        if (!ModConfig.monitorSettings.enableMedicalMonitor) {
            return new ActionResult<>(EnumActionResult.FAIL, stack);
        }

        if (!worldIn.isRemote) {
            float damage = ModConfig.monitorSettings.monitorUseDamage;
            if (damage > 0.0F) {
                playerIn.attackEntityFrom(DamageSource.MAGIC, damage);
            }

            IPlayerTimer timer = playerIn.getCapability(CapabilityPlayerTimer.TIMER_CAP, null);
            if (timer != null) {
                long currentTicks = timer.getTimer();
                int[] thresholds = ModConfig.virusSettings.virusStageThresholds;

                int maxTicks = 85200;
                if (thresholds != null && thresholds.length > 0) {
                    maxTicks = thresholds[thresholds.length - 1];
                }

                double percentage = maxTicks > 0 ? Math.min(100.0, Math.max(0.0, ((double) currentTicks / maxTicks) * 100.0)) : 0.0;
                String formattedPercentage = String.format("%.1f", percentage);

                playerIn.sendMessage(new TextComponentTranslation("msg.pills.monitor_status", formattedPercentage));
            }
        }

        if (ModConfig.monitorSettings.monitorCooldown > 0) {
            playerIn.getCooldownTracker().setCooldown(this, ModConfig.monitorSettings.monitorCooldown);
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
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
