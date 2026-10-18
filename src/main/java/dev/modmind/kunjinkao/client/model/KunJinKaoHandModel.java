package dev.modmind.kunjinkao.client.model;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.entity.KunJinKaoHandEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/**
 * 锟斤拷之手的模型：一只趴在地上的手，手指朝前（+Z）。
 * <p>
 * <b>蠕动是这样做的：</b>四根手指各带一个相位差，按 {@code sin(摆动相位 + 相位差)} 做波浪式
 * 抬落与左右轻摆 —— 就像肉虫子的体节波。掌与腕整体再叠一层缓慢的起伏与侧倾，
 * 让"趴在地上往前挪"这件事在视觉上成立。所有动作都是模型动画，
 * 实体那层（{@code KunJinKaoHandEntity}）用的是最普通的步行寻路。
 * <p>
 * <b>贴图约定：</b>全部"皮肤"部件共用 {@code texOffs(0,0)} 起的一块 32x32 皮肤区
 * （底色 + 血管 + 腕部小痣都画在这一块里），四片指甲共用 {@code texOffs(32,0)} 的 16x16 白甲区。
 * 部件各自取样区域不同、但都在各自色块内，所以不需要为每个部件单独画 UV。
 */
public class KunJinKaoHandModel extends EntityModel<KunJinKaoHandEntity> {

    public static final ModelLayerLocation LAYER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "hand"), "main");

    /** 皮肤贴图区原点。 */
    private static final int SKIN_U = 0;
    private static final int SKIN_V = 0;
    /** 指甲贴图区原点。 */
    private static final int NAIL_U = 32;
    private static final int NAIL_V = 0;

    private final ModelPart root;
    private final ModelPart wrist;
    private final ModelPart palm;
    private final ModelPart[] fingerProximal = new ModelPart[4];
    private final ModelPart[] fingerDistal = new ModelPart[4];
    private final ModelPart[] nails = new ModelPart[4];
    private final ModelPart thumb;

    public KunJinKaoHandModel(ModelPart root) {
        this.root = root.getChild("root");
        this.wrist = this.root.getChild("wrist");
        this.palm = this.root.getChild("palm");
        for (int i = 0; i < 4; i++) {
            ModelPart proximal = this.palm.getChild("finger" + i);
            this.fingerProximal[i] = proximal;
            this.fingerDistal[i] = proximal.getChild("finger_tip" + i);
            this.nails[i] = this.palm.getChild("nail" + i);
        }
        this.thumb = this.palm.getChild("thumb");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot().addOrReplaceChild("root",
                CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        // 腕部断口：比掌略窄、带一圈切面（视觉上是"从手臂上断下来的"）。
        root.addOrReplaceChild("wrist", CubeListBuilder.create()
                        .texOffs(SKIN_U, SKIN_V)
                        .addBox(-2.6F, -1.4F, -4.0F, 5.2F, 2.8F, 4.0F),
                PartPose.offset(0.0F, 2.0F, -3.0F));

        // 掌：宽 8、厚 2.6、长 6，指根在前缘（+Z 侧）。
        PartDefinition palm = root.addOrReplaceChild("palm", CubeListBuilder.create()
                        .texOffs(SKIN_U, SKIN_V)
                        .addBox(-4.0F, -1.3F, -3.0F, 8.0F, 2.6F, 6.0F),
                PartPose.offset(0.0F, 2.0F, 0.0F));

        // 四指：每根两节（近节挂在掌上，远节挂在近节上，转动时跟着弯）。
        // 指间距 2.1，指宽 1.6，留出细缝让"并拢"读得出来。
        float[] centerX = {-3.15F, -1.05F, 1.05F, 3.15F};
        for (int i = 0; i < 4; i++) {
            float x = centerX[i];
            PartDefinition proximal = palm.addOrReplaceChild("finger" + i,
                    CubeListBuilder.create()
                            .texOffs(SKIN_U, SKIN_V)
                            .addBox(-0.8F, -0.8F, 0.0F, 1.6F, 1.6F, 3.2F),
                    PartPose.offset(x, 0.9F, 3.0F));
            proximal.addOrReplaceChild("finger_tip" + i,
                    CubeListBuilder.create()
                            .texOffs(SKIN_U, SKIN_V)
                            .addBox(-0.7F, -0.7F, 0.0F, 1.4F, 1.4F, 2.6F),
                    PartPose.offset(0.0F, 0.0F, 3.2F));
            // 指甲：一片薄而长的白甲，压在指尖上方、往前伸出约 1.4 —— 照片里指甲明显比指尖长。
            palm.addOrReplaceChild("nail" + i,
                    CubeListBuilder.create()
                            .texOffs(NAIL_U, NAIL_V)
                            .addBox(-0.65F, -0.25F, 0.0F, 1.3F, 0.5F, 2.4F),
                    PartPose.offsetAndRotation(x, 0.35F, 6.6F, -0.12F, 0.0F, 0.0F));
        }

        // 拇指：贴在掌的侧面，向外斜一点，也有两节。
        PartDefinition thumb = palm.addOrReplaceChild("thumb",
                CubeListBuilder.create()
                        .texOffs(SKIN_U, SKIN_V)
                        .addBox(-0.85F, -0.85F, -1.4F, 1.7F, 1.7F, 3.4F),
                PartPose.offsetAndRotation(-4.6F, 0.7F, -1.2F, 0.0F, 0.55F, 0.15F));
        thumb.addOrReplaceChild("thumb_tip",
                CubeListBuilder.create()
                        .texOffs(SKIN_U, SKIN_V)
                        .addBox(-0.75F, -0.75F, 0.0F, 1.5F, 1.5F, 2.4F),
                PartPose.offset(0.0F, 0.0F, 2.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(KunJinKaoHandEntity entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {
        // 蠕动的两个来源：limbSwing（移动节奏，站着时是 0）与 ageInTicks（idle 时的慢波）。
        // 振幅取 squirmAmplitude：追击时蠕动明显加强，站立时只剩轻微起伏。
        float amplitude = entity.squirmAmplitude(limbSwingAmount);
        float wave = limbSwing * 0.9F;
        float idle = ageInTicks * 0.045F;

        // 整体：缓慢起伏 + 轻微侧倾 + 朝向目标转头。
        this.root.y = 0.4F + Mth.sin(ageInTicks * 0.12F) * 0.35F;
        this.root.zRot = Mth.sin(ageInTicks * 0.09F) * 0.05F * amplitude;
        this.root.yRot = netHeadYaw * (float) (Math.PI / 180.0D) * 0.35F;

        // 腕部：与掌反相的轻抬，做出"从头到尾的体节波"。
        this.wrist.xRot = Mth.sin(wave + idle + 1.6F) * 0.12F * amplitude;

        // 手指：相位差波浪。攻击时（attackTime > 0）整排手指先张开再合拢 —— "拍"的那一下。
        float attack = entity.getAttackAnim(0.0F);
        for (int i = 0; i < 4; i++) {
            float phase = i * 0.85F;
            float lift = Mth.sin(wave + idle + phase) * 0.28F * amplitude;
            this.fingerProximal[i].xRot = -0.10F + lift - attack * 0.55F;
            this.fingerProximal[i].yRot = Mth.sin(wave * 0.6F + idle + phase) * 0.10F * amplitude
                    + attack * (i < 2 ? -0.22F : 0.22F);
            this.fingerDistal[i].xRot = -0.22F + Mth.sin(wave + idle + phase + 0.7F) * 0.30F * amplitude
                    + attack * 0.65F;
            // 指甲跟着指尖走：抬起时指甲翘起来，露出"又长又白"的那截。
            this.nails[i].xRot = -0.12F + lift * 0.8F + attack * 0.35F;
        }

        // 拇指：与手指反相的慢摆，撑地感。
        this.thumb.yRot = 0.55F + Mth.sin(wave + idle + 2.2F) * 0.14F * amplitude;
        this.thumb.xRot = Mth.sin(wave * 0.8F + idle) * 0.08F * amplitude;
    }

    @Override
    public void renderToBuffer(com.mojang.blaze3d.vertex.PoseStack poseStack,
                               com.mojang.blaze3d.vertex.VertexConsumer buffer,
                               int packedLight, int packedOverlay, int color) {
        this.root.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}