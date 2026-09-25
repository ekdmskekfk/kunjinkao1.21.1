package dev.modmind.kunjinkao.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.network.TimeAccelStatusPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

/**
 * 在被加速的方块上方画悬浮提示：加速倍率 + 倒计时。
 * <p>
 * 画在 {@code AFTER_ENTITIES}：这一阶段的姿态栈已是相机坐标系，
 * 直接用「世界坐标 - 相机坐标」就能把字放到方块上（与原版名字标签同一套换算）。
 * 文字用 SEE_THROUGH，被方块挡住时依然看得见 —— 提示的作用正是让你找到加速场。
 */
@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = KunJinKaoEntry.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public final class TimeAccelOverlayRenderer {

    /** 名字标签的标准缩放：0.025 对应约 1/40，正是原版铭牌的大小。 */
    private static final float TAG_SCALE = 0.025F;
    /** 悬浮高度：方块顶上再抬一点，避免贴脸。 */
    private static final double HEIGHT_ABOVE_BLOCK = 1.35D;

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

        for (TimeAccelStatusPayload.Entry entry : entries) {
            long remaining = TimeAccelClientState.remainingMillis(entry);
            if (entry.remainingMillis() >= 0L && remaining <= 0L) {
                continue;
            }
            Component text = Component.translatable("hud.kunjinkao.time_accel_tag",
                    entry.multiplier(), formatRemaining(remaining));

            pose.pushPose();
            pose.translate(entry.pos().getX() + 0.5D - camera.x,
                    entry.pos().getY() + HEIGHT_ABOVE_BLOCK - camera.y,
                    entry.pos().getZ() + 0.5D - camera.z);
            // 与相机同向 + 负缩放 = 永远正对玩家的广告牌文字（原版铭牌就是这么做的）。
            pose.mulPose(event.getCamera().rotation());
            pose.scale(-TAG_SCALE, -TAG_SCALE, TAG_SCALE);
            float halfWidth = -font.width(text) / 2.0F;
            int background = ((int) (minecraft.options.getBackgroundOpacity(0.25F) * 255.0F)) << 24;
            font.drawInBatch(text, halfWidth, 0.0F, 0xFF9BE9FF, false,
                    pose.last().pose(), buffer, Font.DisplayMode.SEE_THROUGH, background, 0xF000F0);
            pose.popPose();
        }
        // 世界渲染阶段没有别人替我们 flush。
        buffer.endBatch();
    }

    /** 倒计时文本：无限显示 ∞，否则显示一位小数的秒。 */
    private static String formatRemaining(long remainingMillis) {
        if (remainingMillis < 0L) {
            return "∞";
        }
        return String.format(java.util.Locale.ROOT, "%.1fs", remainingMillis / 1000.0D);
    }
}