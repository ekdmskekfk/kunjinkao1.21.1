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
 * 建筑手杖的放置预览：把这次将要铺到哪几格，用线框画出来。
 * <p>
 * 挂在 {@link RenderHighlightEvent.Block} 上 —— 这是原版"你正看着某个方块"时每帧都会触发的事件，
 * 目标方块、朝向、相机、buffer 全都是现成的，不必自己去算射线或挑渲染时机。
 * 这个做法取自无用之物的 StretcherHighlight。
 * <p>
 * 位置用的是与服务端铺设相同的那条链：同一起步点、同一个
 * {@link PlacementCoreHandler#extensionDirection} 方向判定、同样"每格靠前一格"的延伸方式，
 * 并且复用服务端 tryPlaceAt 的两个前置判定（目标可替换、依托面实心），
 * 因此预览与实际结果一一对应 —— 放不下的格子不会被画出来。
 */
@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = KunJinKaoEntry.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public final class PlacementPreviewRenderer {

    /** 生存下单次最多放置 32 个（与 PlacementCoreHandler.MAX_PLACE_SURVIVAL 一致）。 */
    private static final int MAX_PREVIEW_SURVIVAL = 32;
    /** 创造模式下的安全上限。 */
    private static final int MAX_PREVIEW_CREATIVE = 1024;
    /** 线框略微外扩，避免与方块表面 z-fighting。 */
    private static final double INFLATE = 0.004D;
    /**
     * 线框颜色：说明里写的是"会以黑框提示你将要连续放置方块的位置"，所以用近黑色。
     * 全黑在暗处会看不见，取一点点灰。
     */
    private static final float LINE_R = 0.08F;
    private static final float LINE_G = 0.08F;
    private static final float LINE_B = 0.08F;
    private static final float LINE_A = 0.85F;

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
        List<BlockPos> targets = previewTargets(player, target);
        if (targets.isEmpty()) {
            return;
        }
        Vec3 camera = event.getCamera().getPosition();
        PoseStack pose = event.getPoseStack();
        var buffer = event.getMultiBufferSource().getBuffer(RenderType.lines());
        for (BlockPos pos : targets) {
            pose.pushPose();
            pose.translate(pos.getX() - camera.x, pos.getY() - camera.y, pos.getZ() - camera.z);
            LevelRenderer.renderLineBox(pose, buffer, new AABB(BlockPos.ZERO).inflate(INFLATE),
                    LINE_R, LINE_G, LINE_B, LINE_A);
            pose.popPose();
        }
    }

    /** 按服务端那条链算出这次会铺到哪几格（只含确实放得下的）。 */
    private static List<BlockPos> previewTargets(Player player, BlockHitResult hit) {
        List<BlockPos> targets = new ArrayList<>();
        ItemStack sword = player.getMainHandItem();
        if (!(sword.getItem() instanceof KunJinKaoSwordItem)
                || !KunJinKaoSwordItem.isConstructionWandEnabled(sword)) {
            return targets;
        }
        // 材料同样取自背包（说明：消耗背包中的方块），不是副手 ——
        // 之前预览为空就是因为这里查的是副手。
        ItemStack material = PlacementCoreHandler.findMaterial(player);
        if (!(material.getItem() instanceof BlockItem)) {
            return targets;
        }
        var level = Minecraft.getInstance().level;
        if (level == null) {
            return targets;
        }
        Direction face = hit.getDirection();
        BlockPos clicked = hit.getBlockPos();
        BlockPos first = clicked.relative(face);
        Direction extend = PlacementCoreHandler.extensionDirection(player, face);
        int cap = player.isCreative() ? MAX_PREVIEW_CREATIVE : MAX_PREVIEW_SURVIVAL;
        int limit = Math.min(material.getCount(), cap);
        for (int i = 0; i < limit; i++) {
            BlockPos target = first.relative(extend, i);
            // 与服务端 tryPlaceAt 的前置判定、顺序都保持一致：目标可替换、依托面实心。
            if (!level.getBlockState(target).canBeReplaced()) {
                break;
            }
            BlockPos support = i == 0 ? clicked : first.relative(extend, i - 1);
            Direction supportFace = i == 0 ? face : extend;
            if (!level.getBlockState(support).isFaceSturdy(level, support, supportFace)) {
                break;
            }
            targets.add(target);
        }
        return targets;
    }
}