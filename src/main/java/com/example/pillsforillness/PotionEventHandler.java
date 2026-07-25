package com.example.pillsforillness;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = PillsForIllnessMod.MODID)
public class PotionEventHandler {
    @SubscribeEvent
    public static void onPotionRemove(PotionEvent.PotionRemoveEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        if (entity == null) return;
        Potion potion = event.getPotion();
        if (potion == ModPotions.VIRUS && !ModPotions.allowVirusRemoval) {
            event.setCanceled(true);
        }
        if (potion == ModPotions.OVERDOSE && !ModPotions.allowOverdoseRemoval) {
            event.setCanceled(true);
        }
    }
}
