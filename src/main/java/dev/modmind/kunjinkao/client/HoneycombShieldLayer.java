package dev.modmind.kunjinkao.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.Mth;

/** Renders every impact as seven small cyan hexagon outlines on the player body. */
public final class HoneycombShieldLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    private static final float CELL_RADIUS = 0.10F;
    private static final float MODEL_SURFACE_OFFSET = -0.62F;
    public HoneycombShieldLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent) { super(parent); }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, AbstractClientPlayer player,
            float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        for (ShieldHitVisualState.ActiveShieldHit hit : ShieldHitVisualState.getActive(player.getUUID(), partialTick)) {
            poseStack.pushPose();
            getParentModel().body.translateAndRotate(poseStack);
            float bodyYaw = Mth.rotLerp(partialTick, player.yBodyRotO, player.yBodyRot);
            float viewYaw = Mth.rotLerp(partialTick, player.yRotO, player.getYRot());
            poseStack.mulPose(Axis.YP.rotationDegrees(-hit.relativeImpactYaw() - (viewYaw - bodyYaw)));
            poseStack.translate(0.0F, -0.34F + hit.impactHeight() * 0.45F, MODEL_SURFACE_OFFSET);
            drawHoneycomb(poseStack, bufferSource.getBuffer(RenderType.lines()), hit.alpha());
            poseStack.popPose();
        }
    }

    private static void drawHoneycomb(PoseStack poseStack, VertexConsumer consumer, float alpha) {
        drawHexagon(poseStack, consumer, 0.0F, 0.0F, alpha);
        float spacing = CELL_RADIUS * 1.72F;
        for (int index = 0; index < 6; index++) {
            float angle = (float)(Math.PI / 3.0D * index);
            drawHexagon(poseStack, consumer, Mth.cos(angle) * spacing, Mth.sin(angle) * spacing, alpha);
        }
    }

    private static void drawHexagon(PoseStack poseStack, VertexConsumer consumer, float centerX, float centerY, float alpha) {
        for (int index = 0; index < 6; index++) {
            float first = (float)(Math.PI / 3.0D * index + Math.PI / 6.0D);
            float second = (float)(Math.PI / 3.0D * ((index + 1) % 6) + Math.PI / 6.0D);
            vertex(poseStack, consumer, centerX + Mth.cos(first) * CELL_RADIUS, centerY + Mth.sin(first) * CELL_RADIUS, alpha);
            vertex(poseStack, consumer, centerX + Mth.cos(second) * CELL_RADIUS, centerY + Mth.sin(second) * CELL_RADIUS, alpha);
        }
    }

    private static void vertex(PoseStack poseStack, VertexConsumer consumer, float x, float y, float alpha) {
        consumer.addVertex(poseStack.last(), x, y, 0.0F).setColor(0.26F, 0.86F, 1.0F, alpha).setNormal(poseStack.last(), 0.0F, 0.0F, 1.0F);
    }
}
