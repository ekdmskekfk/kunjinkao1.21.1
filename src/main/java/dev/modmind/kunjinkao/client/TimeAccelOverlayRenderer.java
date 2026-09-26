package dev.modmind.kunjinkao.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.network.TimeAccelStatusPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

/**
 * 世界空间里的加速悬浮提示：方块、生物、以及整体时间加速（画在太阳方向）。
 * <p>
 * 姿态换算按原版铭牌那一套，但有两处必须和原版区分开，写错就会完全看不见：
 * <ol>
 *   <li><b>不要再叠相机旋转</b>。{@code AFTER_ENTITIES} 拿到的姿态栈已经是"含相机旋转的视图空间"，
 *       视图空间里相机看向 -Z，所以字体所在的 +Z 面天然就朝向观察者。
 *       再 mulPose(camera.rotation()) 会把文字转到侧面甚至背面，结果就是一片空白。</li>
 *   <li><b>X 不能取负</b>。缩放是 {@code (0.025, -0.025, 0.025)}：Y 取负是为了把字翻正，
 *       X 取负会让整行字变成镜像。</li>
 * </ol>
 */
@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = KunJinKaoEntry.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public final class TimeAccelOverlayRenderer {

    /** 原版铭牌的缩放。Y 取负翻正字，X 保持正数否则整行镜像。 */
    private static final float TAG_SCALE = 0.025F;
    /** 方块上方的高度。 */
    private static final double BLOCK_HEIGHT = 1.6D;
    /** 生物头顶再抬多少。 */
    private static final double ENTITY_HEIGHT = 0.6D;
    /** 太阳方向的提示离相机多远（只是方向，取多大都不影响观感）。 */
    private static final double SUN_DISTANCE = 100.0D;
    /** 文字颜色（青色，与模组主题一致）。 */
    private static final int COLOR_TEXT = 0xFF9BE9FF;

    private TimeAccelOverlayRenderer() {
    }

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null || minecraft.player == null) {
            return;
        }
        var entries = TimeAccelClientState.entries();
        if (entries.isEmpty()) {
            return;
        }
        Vec3 camera = event.getCamera().getPosition();
        PoseStack pose = event.getPoseStack();
        MultiBufferSource.BufferSource buffer = minecraft.renderBuffers().bufferSource();
        Font font = minecraft.font;
        int background = ((int) (minecraft.options.getBackgroundOpacity(0.25F) * 255.0F)) << 24;

        boolean drewAnything = false;
        for (TimeAccelStatusPayload.Entry entry : entries) {
            long remaining = TimeAccelClientState.remainingMillis(entry);
            if (entry.remainingMillis() >= 0L && remaining <= 0L) {
                continue;
            }
            Vec3 at = resolvePosition(minecraft, event, entry, camera);
            if (at == null) {
                continue;
            }
            Component text = Component.translatable(entry.kind() == TimeAccelStatusPayload.KIND_TIME
                            ? "hud.kunjinkao.time_accel_time_tag"
                            : "hud.kunjinkao.time_accel_tag",
                    entry.multiplier(), formatRemaining(remaining));

            pose.pushPose();
            pose.translate(at.x - camera.x, at.y - camera.y, at.z - camera.z);
            // 视图空间里字体的 +Z 已朝向观察者，所以这里只缩放、不旋转。
            pose.scale(TAG_SCALE, -TAG_SCALE, TAG_SCALE);
            font.drawInBatch(text, -font.width(text) / 2.0F, 0.0F, COLOR_TEXT, false,
                    pose.last().pose(), buffer, Font.DisplayMode.SEE_THROUGH, background, 0xF000F0);
            pose.popPose();
            drewAnything = true;
        }
        if (drewAnything) {
            // 世界渲染阶段没有别人替我们 flush。
            buffer.endBatch();
        }
    }

    /** 三种加速对象各自的提示位置；取不到（生物已消失等）返回 null。 */
    private static Vec3 resolvePosition(Minecraft minecraft, RenderLevelStageEvent event,
                                        TimeAccelStatusPayload.Entry entry, Vec3 camera) {
        if (entry.kind() == TimeAccelStatusPayload.KIND_BLOCK) {
            return new Vec3(entry.pos().getX() + 0.5D,
                    entry.pos().getY() + BLOCK_HEIGHT,
                    entry.pos().getZ() + 0.5D);
        }
        if (entry.kind() == TimeAccelStatusPayload.KIND_ENTITY) {
            Entity entity = minecraft.level.getEntity(entry.entityId());
            if (entity == null) {
                return null;
            }
            return new Vec3(entity.getX(), entity.getY() + entity.getBbHeight() + ENTITY_HEIGHT, entity.getZ());
        }
        return sunPosition(minecraft, event, camera);
    }

    /**
     * 太阳方向上的一个点。
     * <p>
     * 方向取自原版天空的算法：{@code Axis.XP.rotationDegrees(getTimeOfDay * 360)} 再绕 Y 转 -90，
     * 化简后就是 {@code (-sin(2πt), cos(2πt), 0)} —— 即太阳沿 X 轴东升西落。
     * 太阳落到地平线以下时改画到正上方，否则夜里提示会跑到地底下去。
     */
    private static Vec3 sunPosition(Minecraft minecraft, RenderLevelStageEvent event, Vec3 camera) {
        double angle = minecraft.level.getTimeOfDay(event.getPartialTick().getGameTimeDeltaPartialTick(false)) * Math.PI * 2.0D;
        double x = -Math.sin(angle);
        double y = Math.cos(angle);
        if (y <= 0.05D) {
            x = 0.0D;
            y = 1.0D;
        }
        return new Vec3(camera.x + x * SUN_DISTANCE, camera.y + y * SUN_DISTANCE, camera.z);
    }

    /** 倒计时文本：无限显示 ∞，否则显示一位小数的秒。 */
    private static String formatRemaining(long remainingMillis) {
        if (remainingMillis < 0L) {
            return "∞";
        }
        return String.format(java.util.Locale.ROOT, "%.1fs", remainingMillis / 1000.0D);
    }
}