package dev.modmind.kunjinkao.client.gui;

import com.mojang.blaze3d.platform.InputConstants;
import dev.modmind.kunjinkao.client.KunJinKaoKeyBindings;
import dev.modmind.kunjinkao.network.NetworkHandler;
import dev.modmind.kunjinkao.network.SubmitAdminPasswordPayload;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

/**
 * 】键打开的管理员密码验证窗口。密码是否正确只由服务器决定。
 *
 * <p>外观走 {@link KunJinKaoScreen} 的统一壳：深色面板、青色双层描边、四角括号、
 * 扫描线、带乱码记号的标题栏。这里只负责内容区（标签 / 输入框 / 按钮 / 提示）。</p>
 */
public final class AdminPasswordScreen extends KunJinKaoScreen {

    private static final int PANEL_WIDTH = 260;
    private static final int PANEL_HEIGHT = 132;

    private EditBox passwordBox;
    /** 提交按钮：等待服务端回包期间禁用，防止反复提交。 */
    private Button submitButton;
    /** 是否已提交、正在等待服务端结果；等待期间忽略再次提交（包括回车键路径）。 */
    private boolean awaitingResult;
    /** 服务端迟迟不回包时的等待 tick 数（100 tick = 5 秒）。 */
    private static final int RESULT_TIMEOUT_TICKS = 100;
    private int awaitingTicks;
    /** 超时提示；null 表示当前没有提示。 */
    private Component hint;

    public AdminPasswordScreen() {
        super(Component.literal("管理员验证"));
    }

    @Override
    protected int panelWidth() {
        return PANEL_WIDTH;
    }

    @Override
    protected int panelHeight() {
        return PANEL_HEIGHT;
    }

    @Override
    protected void initPanel() {
        int x = contentX();
        int y = contentY();
        int w = contentWidth();

        passwordBox = new EditBox(font, x, y + 14, w, 20, Component.literal("密码"));
        passwordBox.setMaxLength(64);
        passwordBox.setHint(Component.literal("输入管理员密码"));
        addRenderableWidget(passwordBox);

        submitButton = addRenderableWidget(Button.builder(Component.literal("验证"), button -> submit())
                .bounds(x + (w - 80) / 2, y + 44, 80, 20).build());
        setInitialFocus(passwordBox);
    }

    @Override
    protected void renderPanelBody(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        int x = contentX();
        int y = contentY();
        int w = contentWidth();

        graphics.drawString(font, "密码", x, y, KunJinKaoUi.TEXT_LABEL, false);
        // 输入框外面套一圈焦点环，让"现在该在这里输入"一眼可见
        KunJinKaoUi.focusRing(graphics, x, y + 14, w, 20);
        if (hint != null) {
            graphics.drawCenteredString(font, hint, panelX() + panelW() / 2,
                    contentBottom() - font.lineHeight, KunJinKaoUi.TEXT_WARN);
        }
    }

    /**
     * 超时兜底：服务端在某些分支或卡顿时可能不回包，此时提交按钮会永久禁用、回车也无效，
     * 界面就变成了死路。等待超过 {@link #RESULT_TIMEOUT_TICKS} 后重新放开提交并给出提示。
     */
    @Override
    public void tick() {
        super.tick();
        if (awaitingResult && ++awaitingTicks > RESULT_TIMEOUT_TICKS) {
            onResult();
            hint = Component.translatable("message.kunjinkao.admin_password_timeout");
        }
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
        awaitingTicks = 0;
        hint = null;
        if (submitButton != null) {
            submitButton.active = false;
        }
    }

    /**
     * 收到服务端结果（成功或失败）后恢复提交能力；成功时界面本身会被关闭。
     * 也是 {@link #tick()} 里的超时恢复路径 —— 因此它不再是零调用方法。
     */
    public void onResult() {
        awaitingResult = false;
        awaitingTicks = 0;
        if (submitButton != null) {
            submitButton.active = true;
        }
        if (passwordBox != null) {
            passwordBox.setValue("");
        }
    }
}
