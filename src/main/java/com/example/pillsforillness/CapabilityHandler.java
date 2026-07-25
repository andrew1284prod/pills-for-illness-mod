package com.example.pillsforillness;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class CapabilityHandler {
    public static final ResourceLocation TIMER_KEY = new ResourceLocation(PillsForIllnessMod.MODID, "timer");

    public static void register() {
        // статический класс, события ловятся автоматически
    }

    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityPlayer) {
            event.addCapability(TIMER_KEY, new CapabilityPlayerTimer.Provider());
        }
    }
}
