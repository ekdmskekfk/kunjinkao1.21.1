package dev.modmind.kunjinkao.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.network.TimeAccelStatusPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * 世界空间里的加速悬浮提示。
 * <ul>
 *   <li><b>方块</b>：六个面各贴一条。背面那几条由方块本身遮住（用 NORMAL 而非 SEE_THROUGH），
 *       所以任何角度看过去，朝着你的那几个面都各有一条 —— 正面与顶面可以同时看到。</li>
 *   <li><b>生物</b>：头顶一条。</li>
 *   <li><b>时间加速</b>：太阳方向一条（没有具体位置）。</li>
 * </ul>
 * 朝向用原版铭牌那套：{@code mulPose(cameraOrientation())} + {@code scale(0.025, -0.025, 0.025)}。
 * 已核实 {@code EntityRenderDispatcher.render} 只做 translate、没有任何旋转，所以这一步不能省 ——
 * 少了自己转向相机这一步，文字会朝背面，看起来就是镜像或干脆看不见。
 */
@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = KunJinKaoEntry.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public final class TimeAccelOverlayRenderer {

    /** 原版铭牌的缩放：Y 取负把字翻正，X 保持正数。 */
    private static final float TAG_SCALE = 0.025F;
    /** 方块六个面。 */
    private static final Direction[] BLOCK_FACES = {
            Direction.UP, Direction.DOWN, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};
    /** 提示离方块表面多远。 */
    private static final double FACE_OFFSET = 0.55D;
    /** 生物头顶再抬多少。 */
    private static final double ENTITY_HEIGHT = 0.6D;
    /** 太阳方向的提示离相机多远（只是方向，取多大都不影响观感）。 */
    private static final double SUN_DISTANCE = 100.0D;
    /** 文字颜色。 */
    private static final int COLOR_TEXT = 0xFF9BE9FF;
    /** 单个物品提示的最大绘制条数，防止异常数据把这一帧撑爆。 */
    private static final int MAX_LABELS_PER_ENTRY = 6;

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
        List<TimeAccelStatusPayload.Entry> entries = TimeAccelClientState.entries();
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
            List<Vec3> points = labelPositions(minecraft, event, entry, camera);
            if (points.isEmpty()) {
                continue;
            }
            Component text = Component.translatable(entry.kind() == TimeAccelStatusPayload.KIND_TIME
                            ? "hud.kunjinkao.time_accel_time_tag"
                            : "hud.kunjinkao.time_accel_tag",
                    entry.multiplier(), formatRemaining(remaining));

            for (Vec3 at : points) {
                pose.pushPose();
                pose.translate(at.x - camera.x, at.y - camera.y, at.z - camera.z);
                // 转向相机：这一步不能省，理由见类注释。
                pose.mulPose(event.getCamera().rotation());
                pose.scale(TAG_SCALE, -TAG_SCALE, TAG_SCALE);
                font.drawInBatch(text, -font.width(text) / 2.0F, 0.0F, COLOR_TEXT, false,
                        pose.last().pose(), buffer, Font.DisplayMode.NORMAL, background, 0xF000F0);
                pose.popPose();
            }
            drewAnything = true;
        }
        if (drewAnything) {
            // 世界渲染阶段没有别人替我们 flush。
            buffer.endBatch();
        }
    }

    /** 该条加速记录要画在哪些位置；空列表表示这一帧画不了（例如生物已消失）。 */
    private static List<Vec3> labelPositions(Minecraft minecraft, RenderLevelStageEvent event,
                                             TimeAccelStatusPayload.Entry entry, Vec3 camera) {
        if (entry.kind() == TimeAccelStatusPayload.KIND_BLOCK) {
            return blockFacePoints(entry.pos());
        }
        if (entry.kind() == TimeAccelStatusPayload.KIND_ENTITY) {
            Entity entity = minecraft.level.getEntity(entry.entityId());
            if (entity == null) {
                return List.of();
            }
            return List.of(new Vec3(entity.getX(),
                    entity.getY() + entity.getBbHeight() + ENTITY_HEIGHT, entity.getZ()));
        }
        return List.of(sunPosition(minecraft, event, camera));
    }

    /**
     * 方块六个面外侧各一个点。
     * <p>
     * 六条都画，但渲染用 NORMAL（走深度测试），背面那几条会被方块自身遮住 ——
     * 于是任何角度都只看得到朝着你的那几个面，正面与顶面可以同时出现。
     */
    private static List<Vec3> blockFacePoints(BlockPos pos) {
        List<Vec3> points = new ArrayList<>(MAX_LABELS_PER_ENTRY);
        double cx = pos.getX() + 0.5D;
        double cy = pos.getY() + 0.5D;
        double cz = pos.getZ() + 0.5D;
        for (Direction face : BLOCK_FACES) {
            points.add(new Vec3(cx + face.getStepX() * FACE_OFFSET,
                    cy + face.getStepY() * FACE_OFFSET,
                    cz + face.getStepZ() * FACE_OFFSET));
        }
        return points;
    }

    /**
     * 太阳方向上的一个点。
     * <p>
     * 方向取自原版天空：{@code Axis.XP.rotationDegrees(getTimeOfDay * 360)} 再绕 Y 转 -90，
     * 化简后是 {@code (-sin(2πt), cos(2πt), 0)} —— 太阳沿 X 轴东升西落。
     * 太阳落到地平线以下时改画到正上方，免得夜里提示跑到地底下。
     */
    private static Vec3 sunPosition(Minecraft minecraft, RenderLevelStageEvent event, Vec3 camera) {
        double angle = minecraft.level.getTimeOfDay(
                event.getPartialTick().getGameTimeDeltaPartialTick(false)) * Math.PI * 2.0D;
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