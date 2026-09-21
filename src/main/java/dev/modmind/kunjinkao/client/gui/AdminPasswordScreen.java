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
    /** 提交按钮：等待服务端回包期间禁用，防止反复提交。 */
    private Button submitButton;
    /** 是否已提交、正在等待服务端结果；等待期间忽略再次提交（包括回车键路径）。 */
    private boolean awaitingResult;

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
        submitButton = addRenderableWidget(Button.builder(Component.literal("验证"), button -> submit())
                .bounds(x + 70, y + 72, 80, 20).build());
        setInitialFocus(passwordBox);
    }

    /**
     * 1.21.1 的 Screen.render 第一步就会调用 renderBackground，随后才画控件。
     * 因此面板与标题必须画在这里（即 super.renderBackground 之后），才能得到
     * "背景只画一遍 → 面板 → 控件"的正确顺序：画到 super.render 之后会被 0xE0 的面板
     * 盖住输入框与按钮，画到 super.render 之前又会被背景纹理压暗（原 bug）。
     */
    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // 不再手动调用 renderBackground：背景由 super.render 内部负责，只保留这一次绘制
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        int panelWidth = 220;
        int panelHeight = 105;
        int x = (width - panelWidth) / 2;
        int y = (height - panelHeight) / 2;
        graphics.fill(x, y, x + panelWidth, y + panelHeight, 0xE0122030);
        graphics.fill(x, y, x + panelWidth, y + 1, 0xFF57CFFF);
        graphics.fill(x, y + panelHeight - 1, x + panelWidth, y + panelHeight, 0xFF57CFFF);
        graphics.drawCenteredString(font, title, width / 2, y + 16, 0xFFE9FBFF);
        graphics.drawString(font, "密码", x + 20, y + 30, 0xFF8FEAFF);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // 背景与控件都由 super.render 负责；密码面板见上面的 renderBackground
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

    /**
     * 提交后立即清空输入框并禁用按钮：明文密码不再停留在屏幕上，
     * 等待回包期间也无法反复提交（服务端某些分支不回包时尤其重要）。
     */
    private void submit() {
        if (passwordBox == null || awaitingResult) {
            return;
        }
        NetworkHandler.sendToServer(new SubmitAdminPasswordPayload(passwordBox.getValue()));
        passwordBox.setValue("");
        awaitingResult = true;
        if (submitButton != null) {
            submitButton.active = false;
        }
    }

    /** 收到服务端结果（成功或失败）后恢复提交能力；成功时界面本身会被关闭。 */
    public void onResult() {
        awaitingResult = false;
        if (submitButton != null) {
            submitButton.active = true;
        }
        if (passwordBox != null) {
            passwordBox.setValue("");
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}