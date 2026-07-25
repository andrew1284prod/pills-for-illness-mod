package com.example.pillsforillness;

import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {
    public void preInit() {
        ModPotions.init();
        CapabilityManager.INSTANCE.register(IPlayerTimer.class, new PlayerTimerStorage(), PlayerTimer::new);
        // CapabilityHandler.register(); -- УДАЛЕНО, так как класс использует @EventBusSubscriber
    }

    public void init() {
        // EventHandler.register(); -- УДАЛЕНО, так как класс использует @EventBusSubscriber
    }
}
