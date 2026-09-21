package dev.modmind.kunjinkao.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.modmind.kunjinkao.client.AdminEyeVisualState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import org.joml.Matrix4f;

/**
 * 绑定在 PlayerModel.head 上的第三人称眼部终端。只渲染本地玩家，因而无需网络同步。
 */
public final class EyeHudLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    private static final BlockState[] COMMAND_BLOCK_STATES = {
            Blocks.COMMAND_BLOCK.defaultBlockState(),
            Blocks.REPEATING_COMMAND_BLOCK.defaultBlockState(),
            Blocks.CHAIN_COMMAND_BLOCK.defaultBlockState()
    };
    private static final BlockState WHITE_SUPPORT_STATE = Blocks.WHITE_CONCRETE.defaultBlockState();
    // Minecraft 模型的正 X 是角色自身左侧；正面看角色时它在画面右边。
    private static final float LOCAL_LEFT_EYE_X = 0.2475F;
    private static final float BLOCK_CENTER_Y = -0.20F;
    private static final float BLOCK_CENTER_Z = -0.28F;
    private static final float BLOCK_SCALE = 0.16F;
    private static final float SCREEN_CENTER_X = 0.105F;
    private static final float SCREEN_CENTER_Y = -0.1425F;
    private static final float SCREEN_Z = -0.365F;
    // PlayerModel 坐标每个皮肤像素为 1/16 方块；正 Y 在头部局部坐标中向下。
    private static final float EYE_Y = -0.29F + (1.0F / 16.0F);
    private static final float FRONT_OF_FACE_Z = -0.258F;

    public EyeHudLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent) {
        super(parent);
    }

    public static void addToPlayerRenderers(EntityRenderersEvent.AddLayers event) {
        addLayer(event.getSkin(net.minecraft.client.resources.PlayerSkin.Model.WIDE));
        addLayer(event.getSkin(net.minecraft.client.resources.PlayerSkin.Model.SLIM));
    }

    private static void addLayer(PlayerRenderer renderer) {
        if (renderer != null) {
            renderer.addLayer(new EyeHudLayer(renderer));
        }
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, AbstractClientPlayer player,
                       float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks,
                       float netHeadYaw, float headPitch) {
        if (!AdminEyeVisualState.isEnabled(player.getUUID())) {
            return;
        }

        poseStack.pushPose();
        // ModelPart 会在此处应用头部的平移和旋转；位置不依赖世界绝对坐标。
        getParentModel().head.translateAndRotate(poseStack);
        // Player-model X is mirrored from the viewer's naming convention.
        // Positive X places the display on the player's actual left eye.
        renderFloatingScreenAndBraces(poseStack, buffer);
        renderWhiteSupportBars(poseStack, buffer);

        poseStack.pushPose();
        poseStack.translate(LOCAL_LEFT_EYE_X + BLOCK_SCALE / 2.0F,
                BLOCK_CENTER_Y + BLOCK_SCALE / 2.0F, BLOCK_CENTER_Z + BLOCK_SCALE / 2.0F);

        // Render the real vanilla block model instead of stretching a block
        // texture onto a flat quad. This preserves the command block's 3D form.
        poseStack.scale(BLOCK_SCALE, BLOCK_SCALE, BLOCK_SCALE);
        poseStack.translate(-0.5F, -0.5F, -0.5F);
        int frame = Math.floorMod(((int) ageInTicks) / 8, COMMAND_BLOCK_STATES.length);
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(COMMAND_BLOCK_STATES[frame], poseStack, buffer,
                LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
        poseStack.popPose();
    }

    private static void renderFloatingScreenAndBraces(PoseStack poseStack, MultiBufferSource buffer) {
        // lightning() 是加法混合（SRC_ALPHA, ONE）且输出到 weather framebuffer，alpha 不生效；
        // 改用 debugQuads()：QUADS + 半透明混合 + 不剔除且无纹理。
        // 两者的顶点格式同为 POSITION_COLOR，因此下面的顶点与颜色写法无需改动。
        VertexConsumer consumer = buffer.getBuffer(RenderType.debugQuads());
        Matrix4f matrix = poseStack.last().pose();

        // 淡蓝色半透明悬浮屏，双面绘制，围绕玩家时两侧均可见。
        float halfWidth = 0.075F;
        float halfHeight = 0.050F;
        colorQuad(consumer, matrix, SCREEN_CENTER_X - halfWidth, SCREEN_CENTER_Y - halfHeight, SCREEN_Z,
                SCREEN_CENTER_X + halfWidth, SCREEN_CENTER_Y + halfHeight, 125, 222, 255, 88, false);
        colorQuad(consumer, matrix, SCREEN_CENTER_X - halfWidth, SCREEN_CENTER_Y - halfHeight, SCREEN_Z + 0.001F,
                SCREEN_CENTER_X + halfWidth, SCREEN_CENTER_Y + halfHeight, 125, 222, 255, 88, true);

        // 两根白色支架连接屏幕和命令方块的内侧。
    }

    /** 使用原版白色混凝土的立体方杆，保证支架在所有渲染设置下均清晰可见。 */
    private static void renderWhiteSupportBars(PoseStack poseStack, MultiBufferSource buffer) {
        // 两组弯曲支架：从屏幕右缘折弯，最后插入命令方块的前面。
        renderSupportRod(poseStack, buffer, 0.205F, -0.1725F, -0.375F, 0.100F, 0.0F);
        renderSupportRod(poseStack, buffer, 0.205F, -0.1025F, -0.375F, 0.100F, 0.0F);
    }

    private static void renderCurvedSupport(PoseStack poseStack, MultiBufferSource buffer, boolean upper) {
        if (upper) {
            renderSupportRod(poseStack, buffer, 0.205F, -0.2775F, -0.415F, 0.050F, -66.0F);
            renderSupportRod(poseStack, buffer, 0.235F, -0.320F, -0.415F, 0.057F, -45.0F);
            renderSupportRod(poseStack, buffer, 0.2825F, -0.340F, -0.415F, 0.060F, 0.0F);
            return;
        }
        renderSupportRod(poseStack, buffer, 0.2075F, -0.195F, -0.415F, 0.056F, -63.0F);
        renderSupportRod(poseStack, buffer, 0.2375F, -0.240F, -0.415F, 0.053F, -49.0F);
        renderSupportRod(poseStack, buffer, 0.2825F, -0.260F, -0.415F, 0.060F, 0.0F);
    }

    private static void renderSupportRod(PoseStack poseStack, MultiBufferSource buffer,
                                         float centerX, float centerY, float centerZ,
                                         float length, float angleDegrees) {
        poseStack.pushPose();
        poseStack.translate(centerX, centerY, centerZ);
        poseStack.mulPose(Axis.ZP.rotationDegrees(angleDegrees));
        poseStack.scale(length, 0.032F, 0.060F);
        poseStack.translate(-0.5F, -0.5F, -0.5F);
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(WHITE_SUPPORT_STATE, poseStack, buffer,
                LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }

    private static void brace(VertexConsumer consumer, Matrix4f matrix,
                              float x1, float y1, float z1, float x2, float y2, float z2) {
        float thickness = 0.026F;
        consumer.addVertex(matrix, x1 - thickness, y1, z1).setColor(244, 251, 255, 235);
        consumer.addVertex(matrix, x1 + thickness, y1, z1).setColor(244, 251, 255, 235);
        consumer.addVertex(matrix, x2 + thickness, y2, z2).setColor(244, 251, 255, 235);
        consumer.addVertex(matrix, x2 - thickness, y2, z2).setColor(244, 251, 255, 235);
    }

    private static void colorQuad(VertexConsumer consumer, Matrix4f matrix,
                                  float minX, float minY, float z, float maxX, float maxY,
                                  int red, int green, int blue, int alpha, boolean reversed) {
        if (reversed) {
            consumer.addVertex(matrix, minX, minY, z).setColor(red, green, blue, alpha);
            consumer.addVertex(matrix, minX, maxY, z).setColor(red, green, blue, alpha);
            consumer.addVertex(matrix, maxX, maxY, z).setColor(red, green, blue, alpha);
            consumer.addVertex(matrix, maxX, minY, z).setColor(red, green, blue, alpha);
            return;
        }
        consumer.addVertex(matrix, minX, minY, z).setColor(red, green, blue, alpha);
        consumer.addVertex(matrix, maxX, minY, z).setColor(red, green, blue, alpha);
        consumer.addVertex(matrix, maxX, maxY, z).setColor(red, green, blue, alpha);
        consumer.addVertex(matrix, minX, maxY, z).setColor(red, green, blue, alpha);
    }
}
