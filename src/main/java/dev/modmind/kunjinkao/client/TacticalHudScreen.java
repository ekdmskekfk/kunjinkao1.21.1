package dev.modmind.kunjinkao.client;

import com.mojang.blaze3d.platform.InputConstants;
import dev.modmind.kunjinkao.network.NetworkHandler;
import dev.modmind.kunjinkao.network.TacticalHudPayloads;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/** Non-pausing, transparent, interactive entry screen for authorized administrators. */
public final class TacticalHudScreen extends Screen {

    public TacticalHudScreen() {
        super(Component.translatable("screen.kunjinkao.tactical_hud"));
    }

    @Override
    protected void init() {
        int panelX = width / 2 - 110;
        int panelY = height / 2 - 54;
        addRenderableWidget(Button.builder(Component.translatable("button.kunjinkao.tactical_night_vision"),
                        button -> NetworkHandler.sendToServer(new TacticalHudPayloads.ToggleNightVisionPayload()))
                .bounds(panelX + 14, panelY + 34, 192, 20).build());
        addRenderableWidget(Button.builder(Component.translatable("button.kunjinkao.tactical_entities"),
                        button -> NetworkHandler.sendToServer(new TacticalHudPayloads.RequestEntityListPayload()))
                .bounds(panelX + 14, panelY + 60, 192, 20).build());
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        int panelX = width / 2 - 110;
        int panelY = height / 2 - 54;
        graphics.fill(panelX, panelY, panelX + 220, panelY + 108, 0xB0101820);
        graphics.fill(panelX, panelY, panelX + 220, panelY + 2, 0xD048D8FF);
        graphics.drawCenteredString(font, title, width / 2, panelY + 12, 0xE8F8FF);
        graphics.drawCenteredString(font, Component.translatable("screen.kunjinkao.tactical_hint"), width / 2, panelY + 92, 0x9FB8C8);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (TacticalHudKeyMappings.TOGGLE_HUD != null
                && TacticalHudKeyMappings.TOGGLE_HUD.isActiveAndMatches(InputConstants.getKey(keyCode, scanCode))) {
            closeHud();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void onClose() {
        closeHud();
    }

    public static void closeHud() {
        if (TacticalHudClientState.isEnabled()) NetworkHandler.sendToServer(new TacticalHudPayloads.ToggleHudPayload(false));
        Minecraft.getInstance().setScreen(null);
    }
}
