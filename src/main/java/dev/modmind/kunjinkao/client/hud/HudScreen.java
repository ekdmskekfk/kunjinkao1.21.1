package dev.modmind.kunjinkao.client.hud;

import dev.modmind.kunjinkao.client.function.HudFunction;
import dev.modmind.kunjinkao.client.function.HudFunctionManager;
import dev.modmind.kunjinkao.client.KunJinKaoKeyBindings;
import dev.modmind.kunjinkao.network.NetworkHandler;
import dev.modmind.kunjinkao.network.ToggleTacticalHudPayload;
import dev.modmind.kunjinkao.network.ToggleHudNightVisionPayload;
import dev.modmind.kunjinkao.network.ToggleHudTrueInvisibilityPayload;
import dev.modmind.kunjinkao.network.RequestHudEntityListPayload;
import dev.modmind.kunjinkao.network.RequestExcludedPlayersPayload;
import dev.modmind.kunjinkao.network.ToggleHudMagnetPayload;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * 不暂停游戏的透明交互层。Screen 会释放鼠标，使战术功能栏可点击。
 */
public final class HudScreen extends Screen {

    public HudScreen() {
        super(Component.translatable("screen.kunjinkao.tactical_hud"));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // 不调用 renderBackground：保持正常游戏画面可见。
        HudOverlayRenderer.renderInteractive(graphics, width, height, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        HudFunction function = HudOverlayRenderer.getFunctionAt(width, height, mouseX, mouseY);
        if (function != null && button == 0) {
            HudFunctionManager.activate(function);
            if (function == HudFunction.NIGHT_VISION) {
                NetworkHandler.sendToServer(new ToggleHudNightVisionPayload());
            } else if (function == HudFunction.TRUE_INVISIBILITY) {
                NetworkHandler.sendToServer(new ToggleHudTrueInvisibilityPayload());
            } else if (function == HudFunction.ENTITY_INFO) {
                NetworkHandler.sendToServer(new RequestHudEntityListPayload());
            } else if (function == HudFunction.MAGNET) {
                NetworkHandler.sendToServer(new ToggleHudMagnetPayload());
            } else if (function == HudFunction.EXCLUSION_LIST) {
                NetworkHandler.sendToServer(new RequestExcludedPlayersPayload());
            }
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (handleToggleKeyPressed(keyCode, scanCode)) {
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    /**
     * Screen 会先消费键盘输入，因此在 HUD Screen 内显式处理可自定义的切换按键。
     */
    public static boolean handleToggleKeyPressed(int keyCode, int scanCode) {
        // Screen 打开时 IN_GAME 按键上下文可能不激活，因此直接比对用户当前的可配置绑定。
        if (!KunJinKaoKeyBindings.TOGGLE_TACTICAL_HUD.getKey().equals(InputConstants.getKey(keyCode, scanCode))) {
            return false;
        }
        // 仍由服务端白名单链路确认关闭，客户端不直接伪造 HUD 状态。
        NetworkHandler.sendToServer(new ToggleTacticalHudPayload(false));
        return true;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
