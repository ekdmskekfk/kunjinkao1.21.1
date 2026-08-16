package dev.modmind.kunjinkao.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.modmind.kunjinkao.KunJinKaoEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class KunJinKaoEntityRenderer extends EntityRenderer<Player> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "textures/entity/target.png");

    public KunJinKaoEntityRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public ResourceLocation getTextureLocation(Player pEntity) {
        return TEXTURE;
    }

    @Override
    public void render(Player pEntity, float pEntityYaw, float pPartialTicks,
                       PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight) {
        // 目标头顶渲染逻辑
        pPoseStack.pushPose();
        pPoseStack.translate(0.0D, (double) pEntity.getEyeHeight() + 0.5D, 0.0D);
        pPoseStack.scale(0.03125F, 0.03125F, 0.03125F);
        pPoseStack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
        pPoseStack.scale(-1.0F, -1.0F, 1.0F);
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBufferSource, pPackedLight);
        pPoseStack.popPose();
    }
}