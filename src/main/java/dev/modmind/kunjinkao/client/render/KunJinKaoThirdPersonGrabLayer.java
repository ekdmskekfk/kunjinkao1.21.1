package dev.modmind.kunjinkao.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.client.KunJinKaoClientSwordVisuals;
import dev.modmind.kunjinkao.client.RemoteSwordDrawVisualState;
import dev.modmind.kunjinkao.client.TacticalHudInvisibilityVisualState;
import dev.modmind.kunjinkao.KunJinKaoSwordItem;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.HumanoidArm;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

/** 第三人称拔剑编译后半段的真实玩家手臂取剑动画。 */
@EventBusSubscriber(modid = KunJinKaoEntry.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public final class KunJinKaoThirdPersonGrabLayer
        extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public KunJinKaoThirdPersonGrabLayer(
            RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent) {
        super(parent);
    }

    public static void addToPlayerRenderers(EntityRenderersEvent.AddLayers event) {
        addLayer(event.getSkin(net.minecraft.client.resources.PlayerSkin.Model.WIDE));
        addLayer(event.getSkin(net.minecraft.client.resources.PlayerSkin.Model.SLIM));
    }

    private static void addLayer(PlayerRenderer renderer) {
        if (renderer != null) {
            renderer.addLayer(new KunJinKaoThirdPersonGrabLayer(renderer));
        }
    }

    @SubscribeEvent
    public static void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
        if (!(event.getEntity() instanceof AbstractClientPlayer player)) {
            return;
        }
        if (!isReachActive(player)
                || TacticalHudInvisibilityVisualState.isTrueInvisible(player.getUUID())) {
            return;
        }
        setAnimatedArmVisible(event.getRenderer().getModel(),
                getCompileArm(player), false);
    }

    @SubscribeEvent
    public static void onRenderPlayerPost(RenderPlayerEvent.Post event) {
        if (!(event.getEntity() instanceof AbstractClientPlayer player)) {
            return;
        }
        if (!isReachActive(player)) {
            return;
        }
        setAnimatedArmVisible(event.getRenderer().getModel(),
                getCompileArm(player), true);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                       AbstractClientPlayer player, float limbSwing, float limbSwingAmount,
                       float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!isReachActive(player)) {
            return;
        }

        PlayerModel<AbstractClientPlayer> model = getParentModel();
        HumanoidArm side = getCompileArm(player);
        ModelPart arm = side == HumanoidArm.RIGHT ? model.rightArm : model.leftArm;
        ModelPart sleeve = side == HumanoidArm.RIGHT ? model.rightSleeve : model.leftSleeve;
        var armPose = arm.storePose();
        var sleevePose = sleeve.storePose();
        float progress = getReachProgress(player);
        float inward = side == HumanoidArm.RIGHT ? -0.28F : 0.28F;

        renderRemoteCompileSword(poseStack, buffer, packedLight, player, side);

        arm.visible = true;
        sleeve.visible = true;
        // 从当前走路/待机姿势平滑抬起，并伸向角色正前方的编译剑。
        arm.xRot = Mth.lerp(progress, arm.xRot, -1.38F);
        arm.yRot = Mth.lerp(progress, arm.yRot, inward);
        arm.zRot = Mth.lerp(progress, arm.zRot, 0.0F);
        sleeve.copyFrom(arm);

        VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(player.getSkin().texture()));
        arm.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY);
        sleeve.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY);

        arm.loadPose(armPose);
        sleeve.loadPose(sleevePose);
        // 后续层不应再次画默认手臂；RenderPlayerEvent.Post 会恢复共享模型状态。
        arm.visible = false;
        sleeve.visible = false;
    }

    private static void setAnimatedArmVisible(PlayerModel<AbstractClientPlayer> model,
                                              HumanoidArm side, boolean visible) {
        if (side == HumanoidArm.RIGHT) {
            model.rightArm.visible = visible;
            model.rightSleeve.visible = visible;
        } else {
            model.leftArm.visible = visible;
            model.leftSleeve.visible = visible;
        }
    }

    private static boolean isReachActive(AbstractClientPlayer player) {
        Minecraft minecraft = Minecraft.getInstance();
        return player == minecraft.player
                ? KunJinKaoClientSwordVisuals.isThirdPersonReachActive(player)
                : RemoteSwordDrawVisualState.isReachActive(player.getUUID());
    }

    private static float getReachProgress(AbstractClientPlayer player) {
        return player == Minecraft.getInstance().player
                ? KunJinKaoClientSwordVisuals.getCompileReachProgress()
                : RemoteSwordDrawVisualState.getReachProgress(player.getUUID());
    }

    private static HumanoidArm getCompileArm(AbstractClientPlayer player) {
        if (player == Minecraft.getInstance().player) {
            return KunJinKaoClientSwordVisuals.getCompileArm(player);
        }
        InteractionHand hand = RemoteSwordDrawVisualState.getHand(player.getUUID());
        if (hand == InteractionHand.MAIN_HAND) {
            return player.getMainArm();
        }
        return player.getMainArm() == HumanoidArm.RIGHT ? HumanoidArm.LEFT : HumanoidArm.RIGHT;
    }

    private void renderRemoteCompileSword(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                                          AbstractClientPlayer player, HumanoidArm side) {
        if (player == Minecraft.getInstance().player
                || !RemoteSwordDrawVisualState.isCompiling(player.getUUID())) {
            return;
        }

        InteractionHand hand = RemoteSwordDrawVisualState.getHand(player.getUUID());
        ItemStack sword = player.getItemInHand(hand);
        if (!(sword.getItem() instanceof KunJinKaoSwordItem) || KunJinKaoSwordItem.isDisguised(sword)) {
            return;
        }

        float progress = RemoteSwordDrawVisualState.getCompileProgress(player.getUUID());
        float sideOffset = side == HumanoidArm.RIGHT ? -0.16F : 0.16F;
        ItemStack visualStack = sword.copy();
        CompoundTag modelTag = visualStack.get(DataComponents.CUSTOM_DATA) == null
                ? new CompoundTag() : visualStack.get(DataComponents.CUSTOM_DATA).copyTag();
        modelTag.putBoolean("KunJinKaoCompileHudModel", true);
        visualStack.set(DataComponents.CUSTOM_DATA, CustomData.of(modelTag));

        poseStack.pushPose();
        getParentModel().body.translateAndRotate(poseStack);
        // 剑在角色胸前略偏持剑侧，尺寸小于正常第三人称剑，避免遮住头部。
        poseStack.translate(sideOffset, -0.36F, -0.56F);
        poseStack.mulPose(Axis.YP.rotationDegrees(side == HumanoidArm.RIGHT ? 22.0F : -22.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(8.0F));
        float scale = 0.28F + progress * 0.40F;
        poseStack.scale(scale, scale, scale);
        Minecraft.getInstance().getItemRenderer().renderStatic(visualStack, ItemDisplayContext.FIXED,
                packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, player.level(), player.getId());
        poseStack.popPose();
    }
}
