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
import org.joml.Quaternionf;

import java.util.ArrayList;
import java.util.List;

/**
 * 世界空间里的加速悬浮提示。
 * <ul>
 *   <li><b>方块</b>：文字<b>平贴</b>在朝向相机的那些面上（立方体从任意角度看是 1~3 个面），
 *       所以正面和顶面可以各有一条而互不遮挡 —— 与参考实现（无用之物）的观感一致。</li>
 *   <li><b>生物</b>：头顶一条广告牌。</li>
 *   <li><b>时间加速</b>：太阳方向一条（没有具体位置）。</li>
 * </ul>
 * <p>
 * 姿态换算（这里踩过三次坑，结论都是实机验证出来的，别照直觉改）：
 * <ul>
 *   <li>这个姿态是<b>世界朝向</b>，不含相机旋转：依据是实机现象 —— 一旦按“把它共轭回世界”去写，
 *       文字就会歪、并且跟着视角转，可见它本来就在世界里。</li>
 *   <li>缩放取 {@code (+, -, +)}：Y 取负把字翻正，X 保持正数，两个轴都取负会让整行字水平镜像。</li>
 *   <li>贴面文字直接按面旋转即可；只有广告牌需要 {@code mulPose(camera.rotation())} 主动转向相机。</li>
 * </ul>
 */
@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = KunJinKaoEntry.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public final class TimeAccelOverlayRenderer {

    /** 原版铭牌的缩放：Y 取负把字翻正，X 保持正数。 */
    private static final float TAG_SCALE = 0.025F;
    /** 方块六个面。 */
    private static final Direction[] BLOCK_FACES = Direction.values();
    /** 提示离方块表面多远。贴面文字要略微离开表面，否则会和方块表面打架。 */
    private static final double FACE_OFFSET = 0.53D;
    /**
     * 贴面文字允许占用的最大宽度（方块面宽 1.0，留点边距）。
     * <p>
     * 不设这个上限的话，"加速 ×1024 ∞" 这类长文本会比一个面还宽，
     * 于是溢出到相邻面上，看起来像是被切开的两段（实机截图确认过）。
     */
    private static final float FACE_MAX_WIDTH = 0.92F;
    /** 生物头顶再抬多少。 */
    private static final double ENTITY_HEIGHT = 0.6D;
    /** 太阳方向的提示离相机多远（只是方向，取多大都不影响观感）。 */
    private static final double SUN_DISTANCE = 100.0D;
    /** 文字颜色。 */
    private static final int COLOR_TEXT = 0xFF9BE9FF;

    /** 一条提示的落点与朝向：face 非空表示"平贴在该面上"，为空表示广告牌。 */
    private record Placement(Vec3 at, Direction face) {
    }

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
            List<Placement> placements = placements(minecraft, event, entry, camera);
            if (placements.isEmpty()) {
                continue;
            }
            Component text = Component.translatable(entry.kind() == TimeAccelStatusPayload.KIND_TIME
                            ? "hud.kunjinkao.time_accel_time_tag"
                            : "hud.kunjinkao.time_accel_tag",
                    entry.multiplier(), formatRemaining(remaining));

            for (Placement placement : placements) {
                pose.pushPose();
                pose.translate(placement.at().x - camera.x,
                        placement.at().y - camera.y,
                        placement.at().z - camera.z);
                float scale = TAG_SCALE;
                if (placement.face() != null) {
                    // 这个姿态本身就是世界朝向（实机现象已证伪它含相机旋转：之前多此一举地
                    // 共轭掉相机旋转，文字就会歪、并且跟着视角转，可见它本来就在世界里）。
                    // 所以贴面只需从世界朝向按面旋转。
                    pose.mulPose(faceOrientation(placement.face()));
                    // 长文本要比一个方块面还宽，必须缩到装得下，
                    // 否则会溢出到相邻面上、看起来像被切开的两段。
                    float textWidth = Math.max(1.0F, font.width(text));
                    scale = Math.min(TAG_SCALE, FACE_MAX_WIDTH / textWidth);
                } else {
                    // 广告牌要自己转向相机；贴面文字则是固定在世界里的。
                    pose.mulPose(event.getCamera().rotation());
                }
                // Y 取负把字翻正；X 保持正数，两个轴都取负会让整行字镜像。
                pose.scale(scale, -scale, scale);
                font.drawInBatch(text, -font.width(text) / 2.0F, -font.lineHeight / 2.0F, COLOR_TEXT,
                        false, pose.last().pose(), buffer, Font.DisplayMode.SEE_THROUGH, background, 0xF000F0);
                pose.popPose();
            }
            drewAnything = true;
        }
        if (drewAnything) {
            // 世界渲染阶段没有别人替我们 flush。
            buffer.endBatch();
        }
    }

    /** 该条加速记录要画在哪些位置、什么朝向；空列表表示这一帧画不了。 */
    private static List<Placement> placements(Minecraft minecraft, RenderLevelStageEvent event,
                                              TimeAccelStatusPayload.Entry entry, Vec3 camera) {
        if (entry.kind() == TimeAccelStatusPayload.KIND_BLOCK) {
            return blockFacePlacements(entry.pos(), camera);
        }
        if (entry.kind() == TimeAccelStatusPayload.KIND_ENTITY) {
            Entity entity = minecraft.level.getEntity(entry.entityId());
            if (entity == null) {
                return List.of();
            }
            return List.of(new Placement(new Vec3(entity.getX(),
                    entity.getY() + entity.getBbHeight() + ENTITY_HEIGHT, entity.getZ()), null));
        }
        return List.of(new Placement(sunPosition(minecraft, event, camera), null));
    }

    /**
     * 方块：只取法线朝向相机的那些面，每个面一条平贴文字。
     * <p>
     * 背面的面不提交渲染 —— 这里用的是 SEE_THROUGH（不做深度遮挡），
     * 靠深度测试挡背面是行不通的：改成 NORMAL 会让整条提示都看不见（实机验证过）。
     * 剔除之后，从任意角度看正好剩下 1~3 个面，正面与顶面可以同时出现且互不遮挡。
     */
    private static List<Placement> blockFacePlacements(BlockPos pos, Vec3 camera) {
        double cx = pos.getX() + 0.5D;
        double cy = pos.getY() + 0.5D;
        double cz = pos.getZ() + 0.5D;
        double dx = camera.x - cx;
        double dy = camera.y - cy;
        double dz = camera.z - cz;
        List<Placement> placements = new ArrayList<>(3);
        for (Direction face : BLOCK_FACES) {
            if (face.getStepX() * dx + face.getStepY() * dy + face.getStepZ() * dz <= 0.0D) {
                continue;
            }
            placements.add(new Placement(new Vec3(cx + face.getStepX() * FACE_OFFSET,
                    cy + face.getStepY() * FACE_OFFSET,
                    cz + face.getStepZ() * FACE_OFFSET), face));
        }
        return placements;
    }

    /**
     * 让文字平贴在该面上：字面法线指向该面外侧，字的"上"沿着该面自身的方向。
     * <p>
     * 字体默认躺在 XY 平面、法线朝 +Z，所以从 +Z（SOUTH）出发按面旋转即可。
     */
    private static Quaternionf faceOrientation(Direction face) {
        return switch (face) {
            case SOUTH -> new Quaternionf();
            case NORTH -> new Quaternionf().rotateY((float) Math.PI);
            case EAST -> new Quaternionf().rotateY((float) (Math.PI / 2.0D));
            case WEST -> new Quaternionf().rotateY((float) (-Math.PI / 2.0D));
            case UP -> new Quaternionf().rotateX((float) (-Math.PI / 2.0D));
            case DOWN -> new Quaternionf().rotateX((float) (Math.PI / 2.0D));
        };
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