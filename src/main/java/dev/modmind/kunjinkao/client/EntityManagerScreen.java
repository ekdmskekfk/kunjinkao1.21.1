package dev.modmind.kunjinkao.client;

import com.mojang.blaze3d.platform.InputConstants;
import java.util.List;
import java.util.UUID;

import dev.modmind.kunjinkao.network.NetworkHandler;
import dev.modmind.kunjinkao.network.TacticalHudPayloads;
import dev.modmind.kunjinkao.tactical.common.EntityAction;
import dev.modmind.kunjinkao.tactical.common.EntityRowData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/** Bounded scrollable entity list. Only entity UUIDs, never client-supplied positions, are sent back. */
public final class EntityManagerScreen extends Screen {

    private static final int VISIBLE_ROWS = 9;
    private final List<EntityRowData> entities;
    private int scroll;
    private UUID selectedEntity;
    private Button terminateButton;
    private Button teleportButton;

    public EntityManagerScreen(List<EntityRowData> entities) {
        super(Component.translatable("screen.kunjinkao.entity_manager"));
        this.entities = List.copyOf(entities);
    }

    @Override
    protected void init() {
        int panelX = width / 2 - 190;
        int panelY = height / 2 - 112;
        terminateButton = addRenderableWidget(Button.builder(Component.translatable("button.kunjinkao.tactical_terminate"),
                        button -> manageSelected(EntityAction.TERMINATE)).bounds(panelX + 12, panelY + 190, 174, 20).build());
        teleportButton = addRenderableWidget(Button.builder(Component.translatable("button.kunjinkao.tactical_teleport"),
                        button -> manageSelected(EntityAction.TELEPORT_TO)).bounds(panelX + 194, panelY + 190, 174, 20).build());
        refreshButtonState();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        int panelX = width / 2 - 190;
        int panelY = height / 2 - 112;
        graphics.fill(panelX, panelY, panelX + 380, panelY + 218, 0xC0101820);
        graphics.fill(panelX, panelY, panelX + 380, panelY + 2, 0xD048D8FF);
        graphics.drawCenteredString(font, title, width / 2, panelY + 10, 0xE8F8FF);
        graphics.drawString(font, Component.translatable("screen.kunjinkao.entity_count", entities.size()), panelX + 12, panelY + 26, 0xAFC8D8);
        int first = Math.min(scroll, Math.max(0, entities.size() - VISIBLE_ROWS));
        for (int row = 0; row < VISIBLE_ROWS && first + row < entities.size(); row++) {
            EntityRowData entity = entities.get(first + row);
            int y = panelY + 42 + row * 16;
            boolean selected = entity.entityUuid().equals(selectedEntity);
            graphics.fill(panelX + 10, y - 2, panelX + 370, y + 13, selected ? 0xA0348FC0 : 0x60304050);
            String text = "%s | %s | %s | %.1f, %.1f, %.1f".formatted(entity.displayName(), entity.entityTypeKey(), entity.dimensionId(), entity.x(), entity.y(), entity.z());
            graphics.drawString(font, text, panelX + 14, y, selected ? 0xFFFFFF : 0xC0D4DE);
        }
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int panelX = width / 2 - 190;
        int panelY = height / 2 - 112;
        if (button == 0 && mouseX >= panelX + 10 && mouseX <= panelX + 370 && mouseY >= panelY + 40 && mouseY < panelY + 184) {
            int row = (int) ((mouseY - (panelY + 40)) / 16);
            int index = scroll + row;
            if (index >= 0 && index < entities.size()) {
                selectedEntity = entities.get(index).entityUuid();
                refreshButtonState();
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (verticalAmount != 0.0D) {
            scroll = Math.max(0, Math.min(Math.max(0, entities.size() - VISIBLE_ROWS), scroll - (int) Math.signum(verticalAmount)));
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (TacticalHudKeyMappings.TOGGLE_HUD != null
                && TacticalHudKeyMappings.TOGGLE_HUD.isActiveAndMatches(InputConstants.getKey(keyCode, scanCode))) {
            TacticalHudScreen.closeHud();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().setScreen(new TacticalHudScreen());
    }

    private void manageSelected(EntityAction action) {
        if (selectedEntity != null) NetworkHandler.sendToServer(new TacticalHudPayloads.ManageEntityPayload(selectedEntity, action));
    }

    private void refreshButtonState() {
        boolean enabled = selectedEntity != null;
        if (terminateButton != null) terminateButton.active = enabled;
        if (teleportButton != null) teleportButton.active = enabled;
    }
}
