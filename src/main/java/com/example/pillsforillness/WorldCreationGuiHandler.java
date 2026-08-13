package com.example.pillsforillness;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = PillsForIllnessMod.MODID, value = Side.CLIENT)
public class WorldCreationGuiHandler {
    private static final int BUTTON_ID = 99281;
    public static boolean pendingSystemEnabled = true;

    @SubscribeEvent
    public static void onInitGui(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.getGui() instanceof GuiCreateWorld) {
            if (ModConfig.generalSettings.systemMode == ModConfig.SystemMode.ASK) {
                GuiCreateWorld gui = (GuiCreateWorld) event.getGui();

                int buttonWidth = 300;
                int buttonHeight = 20;
                int buttonX = gui.width / 2 - (buttonWidth / 2);
                int buttonY = gui.height - 52;

                GuiButton toggleBtn = new GuiButton(
                    BUTTON_ID,
                    buttonX,
                    buttonY,
                    buttonWidth,
                    buttonHeight,
                    getButtonText()
                );

                event.getButtonList().add(toggleBtn);
            }
        }
    }

    @SubscribeEvent
    public static void onActionPerformed(GuiScreenEvent.ActionPerformedEvent.Post event) {
        if (event.getGui() instanceof GuiCreateWorld && event.getButton().id == BUTTON_ID) {
            pendingSystemEnabled = !pendingSystemEnabled;
            event.getButton().displayString = getButtonText();
        }
    }

    private static String getButtonText() {
        String status = pendingSystemEnabled
            ? I18n.format("gui.pills_for_illness.enabled")
            : I18n.format("gui.pills_for_illness.disabled");
        return I18n.format("gui.pills_for_illness.toggle_button", status);
    }
}
