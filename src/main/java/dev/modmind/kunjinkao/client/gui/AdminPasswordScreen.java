package dev.modmind.kunjinkao.client.gui;

import com.mojang.blaze3d.platform.InputConstants;
import dev.modmind.kunjinkao.client.KunJinKaoKeyBindings;
import dev.modmind.kunjinkao.network.NetworkHandler;
import dev.modmind.kunjinkao.network.SubmitAdminPasswordPayload;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

/** 】键打开的管理员密码验证窗口。密码是否正确只由服务器决定。 */
public final class AdminPasswordScreen extends Screen {
    private EditBox passwordBox;

    public AdminPasswordScreen() {
        super(Component.literal("管理员验证"));
    }

    @Override
    protected void init() {
        int panelWidth = 220;
        int panelHeight = 105;
        int x = (width - panelWidth) / 2;
        int y = (height - panelHeight) / 2;
        passwordBox = new EditBox(font, x + 20, y + 42, panelWidth - 40, 20, Component.literal("密码"));
        passwordBox.setMaxLength(64);
        passwordBox.setHint(Component.literal("输入管理员密码"));
        addRenderableWidget(passwordBox);
        addRenderableWidget(Button.builder(Component.literal("验证"), button -> submit())
                .bounds(x + 70, y + 72, 80, 20).build());
        setInitialFocus(passwordBox);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics, mouseX, mouseY, partialTick);
        int panelWidth = 220;
        int panelHeight = 105;
        int x = (width - panelWidth) / 2;
        int y = (height - panelHeight) / 2;
        graphics.fill(x, y, x + panelWidth, y + panelHeight, 0xE0122030);
        graphics.fill(x, y, x + panelWidth, y + 1, 0xFF57CFFF);
        graphics.fill(x, y + panelHeight - 1, x + panelWidth, y + panelHeight, 0xFF57CFFF);
        graphics.drawCenteredString(font, title, width / 2, y + 16, 0xFFE9FBFF);
        graphics.drawString(font, "密码", x + 20, y + 30, 0xFF8FEAFF);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
            submit();
            return true;
        }
        if (KunJinKaoKeyBindings.OPEN_ADMIN_PASSWORD.getKey().equals(InputConstants.getKey(keyCode, scanCode))) {
            onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void submit() {
        if (passwordBox == null) {
            return;
        }
        NetworkHandler.sendToServer(new SubmitAdminPasswordPayload(passwordBox.getValue()));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}