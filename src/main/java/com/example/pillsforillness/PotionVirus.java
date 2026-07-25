package com.example.pillsforillness;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionVirus extends Potion {
    private static final ResourceLocation ICON = new ResourceLocation(PillsForIllnessMod.MODID, "textures/gui/virus.png");

    public PotionVirus() {
        super(true, 0x00FF00);
        setPotionName("effect.virus");
        setRegistryName("virus");
        // Без setIconIndex
    }

    @Override
    public boolean hasStatusIcon() {
        return false;
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }

    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
        int duration = 20;
        if (amplifier >= 0) {
            entity.addPotionEffect(new PotionEffect(net.minecraft.init.MobEffects.MINING_FATIGUE, duration, 0, true, false));
        }
        if (amplifier >= 1) {
            entity.addPotionEffect(new PotionEffect(net.minecraft.init.MobEffects.SLOWNESS, duration, 0, true, false));
        }
        if (amplifier >= 2) {
            entity.addPotionEffect(new PotionEffect(net.minecraft.init.MobEffects.WEAKNESS, duration, 0, true, false));
        }
        if (amplifier >= 3) {
            entity.addPotionEffect(new PotionEffect(net.minecraft.init.MobEffects.MINING_FATIGUE, duration, 1, true, false));
            entity.addPotionEffect(new PotionEffect(net.minecraft.init.MobEffects.SLOWNESS, duration, 1, true, false));
            entity.addPotionEffect(new PotionEffect(net.minecraft.init.MobEffects.WEAKNESS, duration, 1, true, false));
        }
        if (amplifier >= 4) {
            entity.addPotionEffect(new PotionEffect(net.minecraft.init.MobEffects.MINING_FATIGUE, duration, 2, true, false));
            entity.addPotionEffect(new PotionEffect(net.minecraft.init.MobEffects.SLOWNESS, duration, 2, true, false));
            entity.addPotionEffect(new PotionEffect(net.minecraft.init.MobEffects.WEAKNESS, duration, 2, true, false));
            entity.addPotionEffect(new PotionEffect(net.minecraft.init.MobEffects.POISON, duration, 0, true, false));
        }
        if (amplifier >= 5) {
            entity.addPotionEffect(new PotionEffect(net.minecraft.init.MobEffects.NAUSEA, duration, 3, true, false));
            entity.addPotionEffect(new PotionEffect(net.minecraft.init.MobEffects.BLINDNESS, duration, 3, true, false));
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderHUDEffect(PotionEffect effect, Gui gui, int x, int y, float z, float alpha) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(ICON);
        GlStateManager.enableBlend();
        Gui.drawModalRectWithCustomSizedTexture(x + 3, y + 3, 0, 0, 18, 18, 16, 16);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderInventoryEffect(PotionEffect effect, Gui gui, int x, int y, float z) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(ICON);
        GlStateManager.enableBlend();
        Gui.drawModalRectWithCustomSizedTexture(x + 6, y + 7, 0, 0, 18, 18, 16, 16);
    }
}
