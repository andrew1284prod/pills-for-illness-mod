package com.example.pillsforillness;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = PillsForIllnessMod.MODID, name = "Pills for Illness", version = "1.0")
public class PillsForIllnessMod {
    public static final String MODID = "pills_for_illness";

    @Mod.Instance
    public static PillsForIllnessMod instance;

    @SidedProxy(clientSide = "com.example.pillsforillness.ClientProxy", serverSide = "com.example.pillsforillness.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandPfi());
    }
}
