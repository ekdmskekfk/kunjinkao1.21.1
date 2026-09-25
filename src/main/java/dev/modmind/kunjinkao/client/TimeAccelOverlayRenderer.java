package dev.modmind.kunjinkao.client;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.network.TimeAccelStatusPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;
import org.joml.Vector4f;

/**
 * 被加速方块上方的悬浮提示：加速倍率 + 倒计时。
 * <p>
 * 分两步，刻意不走"在世界里直接画字"那条路：
 * <ol>
 *   <li>在 {@code AFTER_ENTITIES} 抓下当时的模型视图矩阵与投影矩阵 ——
 *       此刻的姿态栈就是渲染世界用的那一个（已含相机旋转），投影也是透视投影；</li>
 *   <li>在 GUI 图层里用这两个矩阵把方块坐标投影成屏幕坐标，再交给
 *       {@link GuiGraphics#drawString} 画出来。</li>
 * </ol>
 * 这样做是因为 GUI 图层这条渲染路径在本模组里已被实测验证可用（编译动画就是这么画的），
 * 而"世界空间里摆广告牌文字"需要自己复刻原版铭牌那一套姿态换算，容易在旋转上叠错一层，
 * 排查成本远高于收益。
 */
@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = KunJinKaoEntry.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public final class TimeAccelOverlayRenderer {

    /** 悬浮高度：方块顶上再抬一点，避免贴脸。 */
    private static final double HEIGHT_ABOVE_BLOCK = 1.6D;
    /** 提示文字的颜色（青色，与模组主题一致）。 */
    private static final int COLOR_TEXT = 0xFF9BE9FF;
    /** 屏幕边框留白：投影结果超出画面就直接跳过，免得画出诡异的横线。 */
    private static final int SCREEN_MARGIN = 32;

    /** 本帧抓到的矩阵与相机位置，供同帧的 GUI 图层使用。 */
    private static final Matrix4f FRAME_MODEL_VIEW = new Matrix4f();
    private static final Matrix4f FRAME_PROJECTION = new Matrix4f();
    private static double frameCameraX;
    private static double frameCameraY;
    private static double frameCameraZ;
    private static boolean frameCaptured;

    private TimeAccelOverlayRenderer() {
    }

    /** 第一步：在世界渲染阶段抓矩阵。 */
    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null || minecraft.player == null) {
            frameCaptured = false;
            return;
        }
        FRAME_MODEL_VIEW.set(event.getPoseStack().last().pose());
        FRAME_PROJECTION.set(RenderSystem.getProjectionMatrix());
        Vec3 camera = event.getCamera().getPosition();
        frameCameraX = camera.x;
        frameCameraY = camera.y;
        frameCameraZ = camera.z;
        frameCaptured = true;
    }

    /** 第二步：在 GUI 图层里把世界坐标投影成屏幕坐标并画字。 */
    public static void renderLabels(GuiGraphics graphics, int screenWidth, int screenHeight) {
        if (!frameCaptured) {
            return;
        }
        var entries = TimeAccelClientState.entries();
        if (entries.isEmpty()) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return;
        }
        Font font = minecraft.font;

        for (TimeAccelStatusPayload.Entry entry : entries) {
            long remaining = TimeAccelClientState.remainingMillis(entry);
            if (entry.remainingMillis() >= 0L && remaining <= 0L) {
                continue;
            }
            float[] screen = projectToScreen(entry.pos().getX() + 0.5D,
                    entry.pos().getY() + HEIGHT_ABOVE_BLOCK,
                    entry.pos().getZ() + 0.5D, screenWidth, screenHeight);
            if (screen == null) {
                continue;
            }
            Component text = Component.translatable("hud.kunjinkao.time_accel_tag",
                    entry.multiplier(), formatRemaining(remaining));
            int width = font.width(text);
            graphics.drawString(font, text, (int) screen[0] - width / 2, (int) screen[1],
                    COLOR_TEXT, true);
        }
    }

    /**
     * 把世界坐标投影到屏幕坐标。
     *
     * @return {x, y}（GUI 像素）；在相机背后或跑出画面外时返回 null
     */
    private static float[] projectToScreen(double worldX, double worldY, double worldZ,
                                           int screenWidth, int screenHeight) {
        Vector4f point = new Vector4f(
                (float) (worldX - frameCameraX),
                (float) (worldY - frameCameraY),
                (float) (worldZ - frameCameraZ),
                1.0F);
        // 先到视图空间（含相机旋转），再到裁剪空间。
        point.mul(FRAME_MODEL_VIEW);
        point.mul(FRAME_PROJECTION);
        if (point.w() <= 0.001F) {
            // w <= 0 表示这个点在相机背后，做透视除法会得到镜像的假坐标。
            return null;
        }
        float ndcX = point.x() / point.w();
        float ndcY = point.y() / point.w();
        float screenX = (ndcX * 0.5F + 0.5F) * screenWidth;
        float screenY = (1.0F - (ndcY * 0.5F + 0.5F)) * screenHeight;
        if (screenX < -SCREEN_MARGIN || screenX > screenWidth + SCREEN_MARGIN
                || screenY < -SCREEN_MARGIN || screenY > screenHeight + SCREEN_MARGIN) {
            return null;
        }
        return new float[]{screenX, screenY};
    }

    /** 倒计时文本：无限显示 ∞，否则显示一位小数的秒。 */
    private static String formatRemaining(long remainingMillis) {
        if (remainingMillis < 0L) {
            return "∞";
        }
        return String.format(java.util.Locale.ROOT, "%.1fs", remainingMillis / 1000.0D);
    }
}