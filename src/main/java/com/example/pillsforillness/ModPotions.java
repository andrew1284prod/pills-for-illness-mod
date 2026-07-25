package com.example.pillsforillness;

import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class ModPotions {
    public static Potion OVERDOSE;
    public static Potion VIRUS;

    public static boolean allowVirusRemoval = false;
    public static boolean allowOverdoseRemoval = false;

    public static void init() {
        OVERDOSE = new PotionOverdose();
        VIRUS = new PotionVirus();
    }

    @SideOnly(Side.CLIENT)
    public static void initClient() {
        // Клиентская инициализация – здесь можно загрузить что-то, если нужно
        // Но для иконок достаточно, чтобы классы Potion были загружены на клиенте
    }

    @SubscribeEvent
    public static void registerPotions(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(OVERDOSE);
        event.getRegistry().register(VIRUS);
    }
}
