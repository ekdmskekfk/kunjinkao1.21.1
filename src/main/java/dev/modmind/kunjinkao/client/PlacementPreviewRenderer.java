package dev.modmind.kunjinkao.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.KunJinKaoSwordItem;
import dev.modmind.kunjinkao.world.PlacementCoreHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * 建筑手杖的放置预览：在被点击面的平面上，把这次将要铺到哪几格用<b>彩色流动框</b>画出来。
 * <p>
 * 挂在 {@link RenderHighlightEvent.Block} 上 —— 原版"你正看着某个方块"时每帧都会触发，
 * 目标、朝向、相机、buffer 全是现成的（做法取自无用之物的 StretcherHighlight）。
 * <p>
 * 位置用的是与服务端铺设完全相同的那条链：同一起步点、同一个面平面的两个切向轴、
 * 同样一圈圈向外扩的顺序，并且复用服务端 tryPlaceAt 的前置判定
 * （目标可替换、身后那格是实心），因此预览与实际结果一一对应。
 * <p>
 * 颜色按时间旋转色相，并叠加每格的序号，于是整片框看起来是彩色的、而且是"流动"的。
 */
@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = KunJinKaoEntry.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public final class PlacementPreviewRenderer {

    /** 与 PlacementCoreHandler 一致的上限。 */
    private static final int MAX_PREVIEW_SURVIVAL = 32;
    private static final int MAX_PREVIEW_CREATIVE = 1024;
    /** 线框略微外扩，避免与方块表面 z-fighting。 */
    private static final double INFLATE = 0.004D;
    /** 色相每秒转多少圈。 */
    private static final float HUE_SPEED = 0.35F;
    /** 相邻两格之间的色相差，用来做出"流动"的感觉。 */
    private static final float HUE_PER_BLOCK = 0.045F;
    /** 饱和度与明度：偏亮，保证在暗处也看得见。 */
    private static final float SATURATION = 0.85F;
    private static final float VALUE = 1.0F;

    private PlacementPreviewRenderer() {
    }

    @SubscribeEvent
    public static void onHighlight(RenderHighlightEvent.Block event) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null || minecraft.level == null || minecraft.screen != null) {
            return;
        }
        BlockHitResult target = event.getTarget();
        if (target.getType() != HitResult.Type.BLOCK) {
            return;
        }
        List<BlockPos> targets = previewTargets(minecraft.level, player, target);
        if (targets.isEmpty()) {
            return;
        }
        Vec3 camera = event.getCamera().getPosition();
        PoseStack pose = event.getPoseStack();
        var buffer = event.getMultiBufferSource().getBuffer(RenderType.lines());
        float baseHue = (float) ((System.currentTimeMillis() % 100000L) / 1000.0D) * HUE_SPEED;
        for (int index = 0; index < targets.size(); index++) {
            BlockPos pos = targets.get(index);
            // 色相 = 时间 + 序号：整片同时旋转，且相邻格子有色差，看上去就是彩色流动。
            float[] rgb = hsvToRgb((baseHue + index * HUE_PER_BLOCK) % 1.0F, SATURATION, VALUE);
            pose.pushPose();
            pose.translate(pos.getX() - camera.x, pos.getY() - camera.y, pos.getZ() - camera.z);
            LevelRenderer.renderLineBox(pose, buffer, new AABB(BlockPos.ZERO).inflate(INFLATE),
                    rgb[0], rgb[1], rgb[2], 1.0F);
            pose.popPose();
        }
    }

    /** HSV -> RGB，色相 0..1。 */
    private static float[] hsvToRgb(float hue, float saturation, float value) {
        float h = (hue - (float) Math.floor(hue)) * 6.0F;
        int sector = (int) h;
        float f = h - sector;
        float p = value * (1.0F - saturation);
        float q = value * (1.0F - saturation * f);
        float t = value * (1.0F - saturation * (1.0F - f));
        return switch (sector % 6) {
            case 0 -> new float[]{value, t, p};
            case 1 -> new float[]{q, value, p};
            case 2 -> new float[]{p, value, t};
            case 3 -> new float[]{p, q, value};
            case 4 -> new float[]{t, p, value};
            default -> new float[]{value, p, q};
        };
    }

    /** 按服务端那条链算出这次会铺到哪几格（只含确实放得下的）。 */
    private static List<BlockPos> previewTargets(Level level, Player player, BlockHitResult hit) {
        List<BlockPos> targets = new ArrayList<>();
        ItemStack sword = player.getMainHandItem();
        if (!(sword.getItem() instanceof KunJinKaoSwordItem)
                || !KunJinKaoSwordItem.isConstructionWandEnabled(sword)) {
            return targets;
        }
        Direction face = hit.getDirection();
        BlockPos clicked = hit.getBlockPos();
        var origin = level.getBlockState(clicked);
        if (origin.isAir()) {
            return targets;
        }
        // 与服务端同一条约束：材料必须是"点击的那一种方块"，不是就一格都不画。
        ItemStack material = PlacementCoreHandler.findMaterial(player, origin.getBlock().asItem());
        if (!(material.getItem() instanceof BlockItem)) {
            return targets;
        }
        BlockPos first = clicked.relative(face);
        Direction.Axis normalAxis = face.getAxis();
        List<Direction> tangents = new ArrayList<>(2);
        for (Direction.Axis axis : Direction.Axis.values()) {
            if (axis != normalAxis) {
                tangents.add(Direction.fromAxisAndDirection(axis, Direction.AxisDirection.POSITIVE));
            }
        }
        Direction ta = tangents.get(0);
        Direction tb = tangents.get(1);
        int cap = player.isCreative() ? MAX_PREVIEW_CREATIVE : MAX_PREVIEW_SURVIVAL;
        int limit = Math.min(material.getCount(), cap);
        // 与服务端同样的"一圈圈往外扩"，判定顺序也一致。
        int maxRing = 32;
        for (int r = 0; r <= maxRing && targets.size() < limit; r++) {
            for (int i = -r; i <= r && targets.size() < limit; i++) {
                for (int j = -r; j <= r && targets.size() < limit; j++) {
                    if (Math.max(Math.abs(i), Math.abs(j)) != r) {
                        continue;
                    }
                    BlockPos target = first.relative(ta, i).relative(tb, j);
                    if (!level.getBlockState(target).canBeReplaced()) {
                        continue;
                    }
                    // 与服务端一致：依托必须是同种方块（而不是仅仅"实心"），
                    // 于是预览只会画在同种材质的面内，不会跨到旁边的泥土或空气上。
                    BlockPos support = target.relative(face.getOpposite());
                    if (level.getBlockState(support).getBlock() != origin.getBlock()) {
                        continue;
                    }
                    targets.add(target);
                }
            }
        }
        return targets;
    }
}