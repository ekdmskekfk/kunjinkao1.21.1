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
import net.minecraft.world.level.block.state.BlockState;
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
 * 建筑手杖 / 破坏核心的预览：把这次会作用的格子用彩色流动框画出来。
 * <p>
 * 挂在 {@link RenderHighlightEvent.Block} 上 —— 原版"你正看着某个方块"时每帧都会触发，
 * 目标、朝向、相机、buffer 全是现成的（做法取自无用之物的 StretcherHighlight）。
 * <p>
 * 两种模式互斥，且<b>破坏核心优先</b>：开着破坏核心时就只画破坏的那一片，
 * 不会再叠一层建筑手杖的放置预览。
 * <ul>
 *   <li>放置：被点击面的平面上、由同种材料铺开的一片（与 PlacementCoreHandler 同一条链）；</li>
 *   <li>破坏：被点击那一格所在面上、连成一片的同种方块。</li>
 * </ul>
 * 预览最多只画 {@link #MAX_PREVIEW} 格 —— 实际能放/能破坏的上限是 1024，
 * 但预览没必要画那么多，画多了既看不清也拖帧率。
 */
@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = KunJinKaoEntry.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public final class PlacementPreviewRenderer {

    /** 预览最多画多少格。 */
    private static final int MAX_PREVIEW = 32;
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
        ItemStack sword = player.getMainHandItem();
        if (!(sword.getItem() instanceof KunJinKaoSwordItem)) {
            return;
        }
        List<BlockPos> targets;
        if (KunJinKaoSwordItem.isDestructionCoreEnabled(sword)) {
            // 破坏核心优先：此时不要再叠一层建筑手杖的预览。
            targets = destructionTargets(minecraft.level, target);
        } else if (KunJinKaoSwordItem.isConstructionWandEnabled(sword)) {
            targets = placementTargets(minecraft.level, player, target);
        } else {
            return;
        }
        if (targets.isEmpty()) {
            return;
        }
        Vec3 camera = event.getCamera().getPosition();
        PoseStack pose = event.getPoseStack();
        var buffer = event.getMultiBufferSource().getBuffer(RenderType.lines());
        float baseHue = (float) ((System.currentTimeMillis() % 100000L) / 1000.0D) * HUE_SPEED;
        for (int index = 0; index < targets.size(); index++) {
            BlockPos pos = targets.get(index);
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

    /** 该面的两个切向轴：把法线轴排除掉，剩下两个就是。 */
    private static Direction[] tangents(Direction face) {
        Direction.Axis normalAxis = face.getAxis();
        List<Direction> list = new ArrayList<>(2);
        for (Direction.Axis axis : Direction.Axis.values()) {
            if (axis != normalAxis) {
                list.add(Direction.fromAxisAndDirection(axis, Direction.AxisDirection.POSITIVE));
            }
        }
        return new Direction[]{list.get(0), list.get(1)};
    }

    /** 放置预览：面平面上由同种材料铺开的一片，与服务端那条链一致。 */
    private static List<BlockPos> placementTargets(Level level, Player player, BlockHitResult hit) {
        List<BlockPos> targets = new ArrayList<>();
        Direction face = hit.getDirection();
        BlockPos clicked = hit.getBlockPos();
        BlockState origin = level.getBlockState(clicked);
        if (origin.isAir()) {
            return targets;
        }
        ItemStack offhand = player.getOffhandItem();
        boolean override = offhand.getItem() instanceof BlockItem
                && offhand.getItem() != origin.getBlock().asItem();
        ItemStack material = override
                ? offhand
                : PlacementCoreHandler.findMaterial(player, origin.getBlock().asItem());
        if (!(material.getItem() instanceof BlockItem)) {
            return targets;
        }
        // 与服务端同一条洪泛：从点击面的外侧起，在该面平面内一格一格扩散，
        // 只在"身后那一格与点击方块同类"的位置继续。
        List<Direction> planeDirs = PlacementCoreHandler.planeDirections(face);
        BlockPos first = clicked.relative(face);
        java.util.Set<BlockPos> seen = new java.util.HashSet<>();
        java.util.ArrayDeque<BlockPos> queue = new java.util.ArrayDeque<>();
        seen.add(first);
        queue.add(first);
        while (!queue.isEmpty() && targets.size() < MAX_PREVIEW) {
            BlockPos pos = queue.poll();
            BlockPos support = pos.relative(face.getOpposite());
            if (!override && level.getBlockState(support).getBlock() != origin.getBlock()) {
                continue;
            }
            if (level.getBlockState(pos).canBeReplaced()) {
                targets.add(pos);
            }
            for (Direction dir : planeDirs) {
                BlockPos next = pos.relative(dir);
                if (seen.add(next)) {
                    queue.add(next);
                }
            }
        }
        return targets;
    }

    /** 破坏预览：被点击那一格所在面上、连成一片的同种方块。 */
    private static List<BlockPos> destructionTargets(Level level, BlockHitResult hit) {
        List<BlockPos> targets = new ArrayList<>();
        BlockPos clicked = hit.getBlockPos();
        BlockState origin = level.getBlockState(clicked);
        if (origin.isAir()) {
            return targets;
        }
        // 与服务端 destroyFrom 同一条洪泛、同一张 8 方向表（含对角线）。
        java.util.List<Direction> dirs = PlacementCoreHandler.planeDirections(hit.getDirection());
        java.util.Set<BlockPos> seen = new java.util.HashSet<>();
        java.util.ArrayDeque<BlockPos> queue = new java.util.ArrayDeque<>();
        seen.add(clicked);
        queue.add(clicked);
        while (!queue.isEmpty() && targets.size() < MAX_PREVIEW) {
            BlockPos pos = queue.poll();
            BlockState state = level.getBlockState(pos);
            if (!state.isAir() && state.getBlock() == origin.getBlock()
                    && state.getDestroySpeed(level, pos) >= 0.0F && !state.hasBlockEntity()) {
                targets.add(pos);
            }
            for (Direction dir : dirs) {
                BlockPos next = pos.relative(dir);
                if (!seen.add(next)) {
                    continue;
                }
                BlockState nextState = level.getBlockState(next);
                if (!nextState.isAir() && nextState.getBlock() == origin.getBlock()) {
                    queue.add(next);
                }
            }
        }
        return targets;
    }


}