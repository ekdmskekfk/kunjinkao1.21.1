package dev.modmind.kunjinkao.client.render;

import dev.modmind.kunjinkao.client.InjectionClientState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

/**
 * 屏幕正中的注入进度条。
 *
 * <p>刻意画在<b>准星正下方一点</b>而不是绝对正中：绝对正中会盖住准星，
 * 注入时玩家通常还在盯着手里那件物品，看不见准星会很难受。</p>
 *
 * <p>只读 {@link InjectionClientState}，不碰任何网络与服务端状态。</p>
 */
public final class InjectionProgressOverlay {

    private static final int BAR_WIDTH = 160;
    private static final int BAR_HEIGHT = 8;

    /** 进度条底槽（半透明黑）。 */
    private static final int COLOR_TRACK = 0xC0101010;
    /** 进度条本体（青蓝）。 */
    private static final int COLOR_FILL = 0xFF3AA6FF;
    /** 进度条高光（更亮的青）。 */
    private static final int COLOR_HIGHLIGHT = 0xFF9BD8FF;
    /** 外框。 */
    private static final int COLOR_BORDER = 0xFF6FD3FF;

    private static final Component TITLE = Component.translatable("hud.kunjinkao.injection_title");
    private static final Component DONE = Component.translatable("hud.kunjinkao.injection_done");

    private InjectionProgressOverlay() {
    }

    public static void render(GuiGraphics graphics, int screenWidth, int screenHeight) {
        if (InjectionClientState.isActive()) {
            renderActive(graphics, screenWidth, screenHeight);
        } else if (InjectionClientState.isFlashVisible()) {
            renderDoneFlash(graphics, screenWidth, screenHeight);
        }
    }

    private static void renderActive(GuiGraphics graphics, int screenWidth, int screenHeight) {
        Font font = Minecraft.getInstance().font;
        int centerX = screenWidth / 2;
        int x = centerX - BAR_WIDTH / 2;
        int y = screenHeight / 2 + 24;

        // 标题（带百分比）
        float progress = InjectionClientState.progress();
        int percent = (int) (progress * 100.0F);
        Component title = Component.empty().append(TITLE)
                .append(Component.literal(" " + percent + "%"));
        graphics.drawCenteredString(font, title, centerX, y - 12, 0xFF9BD8FF);

        // 外框 + 底槽
        graphics.fill(x - 1, y - 1, x + BAR_WIDTH + 1, y + BAR_HEIGHT + 1, COLOR_BORDER);
        graphics.fill(x, y, x + BAR_WIDTH, y + BAR_HEIGHT, COLOR_TRACK);

        // 进度：至少留 1px，避免 0% 时看起来像没开始
        int filled = Math.max(0, Math.min(BAR_WIDTH, (int) (progress * BAR_WIDTH)));
        if (filled > 0) {
            graphics.fill(x, y, x + filled, y + BAR_HEIGHT, COLOR_FILL);
            // 顶部 2px 高光，让进度条看起来不是纯色块
            graphics.fill(x, y, x + filled, y + 2, COLOR_HIGHLIGHT);
        }
    }

    private static void renderDoneFlash(GuiGraphics graphics, int screenWidth, int screenHeight) {
        Font font = Minecraft.getInstance().font;
        int centerX = screenWidth / 2;
        int y = screenHeight / 2 + 24;
        float alpha = InjectionClientState.flashAlpha();
        int color = ((int) (alpha * 255.0F) << 24) | 0x55FF55;
        graphics.drawCenteredString(font, DONE, centerX, y, color);
    }
}
