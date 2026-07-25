package com.example.pillsforillness;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.init.MobEffects;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionOverdose extends Potion {
    private static final ResourceLocation ICON = new ResourceLocation(PillsForIllnessMod.MODID, "textures/gui/overdose.png");

    public PotionOverdose() {
        super(true, 0xCC0000);
        setPotionName("effect.overdose");
        setRegistryName("overdose");
    }

    @Override
    public boolean hasStatusIcon() {
        return false;
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true; // Работать каждый тик
    }

    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
        int duration = 20; // на 1 секунду каждую секунду обновления
        if (amplifier >= 0) { // Уровень I
            entity.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, duration, 0, true, false));
        }
        if (amplifier >= 1) { // Уровень II
            entity.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, duration, 1, true, false));
        }
        if (amplifier >= 2) { // Уровень III
            entity.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, duration, 0, true, false));
            entity.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, duration, 2, true, false));
            entity.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, duration, 1, true, false));
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
