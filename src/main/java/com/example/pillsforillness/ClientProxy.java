package com.example.pillsforillness;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit() {
        super.preInit();
        ModPotions.initClient(); // <-- ОБЯЗАТЕЛЬНО!
        // Если есть регистрация моделей предметов – здесь
    }
}
