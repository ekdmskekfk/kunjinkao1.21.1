package dev.modmind.kunjinkao.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.KunJinKaoSwordItem;
import dev.modmind.kunjinkao.world.PlacementCoreHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * 建筑手杖的放置预览：把这次将要铺到哪几格，用线框画出来。
 * <p>
 * 位置是用<b>与服务端完全相同的那条链</b>算的：同一套起步点、同一个
 * {@link PlacementCoreHandler#extensionDirection} 方向判定、同样"每格靠前一格"的延伸方式。
 * 两边各自算一遍，不发网络包 —— 预览因此是零延迟的，也不会被伪造。
 * <p>
 * 只能做到"看起来对"：客户端无法预知服务端最终会不会因为权限、
 * 保护插件、方块自身规则而拒绝某一格，所以它画的是"按规则应该能放的位置"。
 */
@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = KunJinKaoEntry.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public final class PlacementPreviewRenderer {

    /** 预览最多画多少格。副手方块数通常远小于它，设上限只是为了别把帧率拖垮。 */
    private static final int MAX_PREVIEW = 128;
    /** 线框颜色（青色，与模组主题一致），renderVoxelShape 要的是分量而不是打包整数。 */
    private static final float LINE_R = 0.31F;
    private static final float LINE_G = 0.85F;
    private static final float LINE_B = 0.91F;
    private static final float LINE_A = 0.85F;

    private PlacementPreviewRenderer() {
    }

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        // 放在半透明方块之后：线框会正确地被前面的方块遮住，又不会被自己盖掉。
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null || minecraft.level == null || minecraft.screen != null) {
            return;
        }
        List<BlockPos> targets = previewTargets(minecraft, player);
        if (targets.isEmpty()) {
            return;
        }
        Vec3 camera = event.getCamera().getPosition();
        PoseStack pose = event.getPoseStack();
        VertexConsumer buffer = minecraft.renderBuffers().bufferSource().getBuffer(RenderType.lines());
        for (BlockPos pos : targets) {
            pose.pushPose();
            pose.translate(pos.getX() - camera.x, pos.getY() - camera.y, pos.getZ() - camera.z);
            // 要放的格子通常是空气，空气没有形状，所以直接画整格线框。
            // 最后一个 boolean 是"是否带法线"：线框渲染类型用不上，传 false。
            LevelRenderer.renderVoxelShape(pose, buffer, Shapes.block(), 0.0D, 0.0D, 0.0D,
                    LINE_R, LINE_G, LINE_B, LINE_A, false);
            pose.popPose();
        }
        minecraft.renderBuffers().bufferSource().endBatch(RenderType.lines());
    }

    /** 按服务端那条链算出这次会铺到哪几格。 */
    private static List<BlockPos> previewTargets(Minecraft minecraft, Player player) {
        List<BlockPos> targets = new ArrayList<>();
        ItemStack sword = player.getMainHandItem();
        if (!(sword.getItem() instanceof KunJinKaoSwordItem)
                || !KunJinKaoSwordItem.isConstructionWandEnabled(sword)) {
            return targets;
        }
        ItemStack material = player.getOffhandItem();
        if (!(material.getItem() instanceof BlockItem)) {
            return targets;
        }
        if (!(minecraft.hitResult instanceof BlockHitResult hit)
                || minecraft.hitResult.getType() != HitResult.Type.BLOCK) {
            return targets;
        }
        Direction face = hit.getDirection();
        BlockPos first = hit.getBlockPos().relative(face);
        Direction extend = PlacementCoreHandler.extensionDirection(player, face);
        // 能放几格取决于副手有多少方块，最多画 MAX_PREVIEW 格。
        int limit = Math.min(material.getCount(), MAX_PREVIEW);
        BlockPos clicked = hit.getBlockPos();
        for (int i = 0; i < limit; i++) {
            BlockPos target = first.relative(extend, i);
            // 只画"确实放得下"的格子：这里复用服务端 tryPlaceAt 的两个前置判定，
            // 顺序也保持一致 —— 目标可替换、依托面是实心的。任何一条不成立就到此为止。
            // 这样预览与实际结果一一对应，而不是画出一批放不下的位置。
            if (!minecraft.level.getBlockState(target).canBeReplaced()) {
                break;
            }
            BlockPos support = i == 0 ? clicked : first.relative(extend, i - 1);
            Direction supportFace = i == 0 ? face : extend;
            if (!minecraft.level.getBlockState(support).isFaceSturdy(
                    minecraft.level, support, supportFace)) {
                break;
            }
            targets.add(target);
        }
        return targets;
    }
}