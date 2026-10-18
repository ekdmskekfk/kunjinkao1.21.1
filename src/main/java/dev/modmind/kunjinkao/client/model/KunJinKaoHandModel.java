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
 * 君君的模型：一只**半握着、手背拱起**的手，指尖收在下面贴地，腕部断口在后面。
 * <p>
 * 姿态照实拍视频定的：不是"手指外露地抓地"，而是放松的半握 —— 手背是一条圆拱，
 * 手指弯在掌下、指尖（带指甲）落在地面上，拇指在侧面朝前。整体像一座低矮的拱丘。
 * <p>
 * <b>蠕动 = 尺蠖式的一拱一拱</b>（照视频的动作）：
 * 一个周期里"手背拱起（整体抬高、重心前移）→ 往前送 → 落下"，用 {@code limbSwing}
 * 当周期时钟 —— 它随实际移动速度推进，所以拱动的节奏和真实位移天然同步；
 * 站着的时候只剩呼吸般的微小起伏。所有动作都是模型动画，实体那层用最普通的步行寻路。
 * <p>
 * <b>贴图约定：</b>皮肤部件共用 {@code texOffs(0,0)} 的 32x32 皮肤区（底色 + 受光 +
 * 血管 + 腕部小痣 + 断口切面），指甲共用 {@code texOffs(32,0)} 的 16x16 白甲区。
 */
public class KunJinKaoHandModel extends EntityModel<KunJinKaoHandEntity> {

    public static final ModelLayerLocation LAYER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "hand"), "main");

    private static final int SKIN_U = 0;
    private static final int SKIN_V = 0;
    private static final int NAIL_U = 32;
    private static final int NAIL_V = 0;

    private final ModelPart root;
    private final ModelPart back;
    private final ModelPart wrist;
    private final ModelPart[] fingerCurl = new ModelPart[4];
    private final ModelPart[] nails = new ModelPart[4];
    private final ModelPart thumb;

    public KunJinKaoHandModel(ModelPart root) {
        this.root = root.getChild("root");
        this.back = this.root.getChild("back");
        this.wrist = this.root.getChild("wrist");
        for (int i = 0; i < 4; i++) {
            this.fingerCurl[i] = this.back.getChild("finger" + i);
            this.nails[i] = this.back.getChild("nail" + i);
        }
        this.thumb = this.back.getChild("thumb");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot().addOrReplaceChild("root",
                CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        // 腕部断口：贴地、比拱起的背低，端面带一圈切面。
        root.addOrReplaceChild("wrist", CubeListBuilder.create()
                        .texOffs(SKIN_U, SKIN_V)
                        .addBox(-3.0F, -1.6F, -3.6F, 6.0F, 3.2F, 4.6F),
                PartPose.offset(0.0F, 2.4F, -5.2F));

        // 手背：拱起的主体积。下层大块 + 上层小块叠出"圆拱"的轮廓。
        PartDefinition back = root.addOrReplaceChild("back", CubeListBuilder.create()
                        .texOffs(SKIN_U, SKIN_V)
                        .addBox(-4.2F, -2.4F, -4.0F, 8.4F, 3.6F, 8.0F),
                PartPose.offset(0.0F, 3.0F, 0.0F));
        back.addOrReplaceChild("knuckle", CubeListBuilder.create()
                        .texOffs(SKIN_U, SKIN_V)
                        .addBox(-3.2F, -1.2F, -2.6F, 6.4F, 1.6F, 4.4F),
                PartPose.offset(0.0F, -2.4F, 1.0F));

        // 四指：收在掌下。近节从手背前缘向下弯（约 -70°），指尖朝下贴地。
        // 只露一点弧度，远端被指甲盖住 —— 与视频里"看不到手指、只看到贴地的指尖"一致。
        float[] centerX = {-3.0F, -1.0F, 1.0F, 3.0F};
        for (int i = 0; i < 4; i++) {
            float x = centerX[i];
            PartDefinition curl = back.addOrReplaceChild("finger" + i,
                    CubeListBuilder.create()
                            .texOffs(SKIN_U, SKIN_V)
                            .addBox(-0.8F, -0.7F, -0.6F, 1.6F, 1.4F, 3.0F),
                    PartPose.offsetAndRotation(x, 0.2F, 3.6F, -1.15F, 0.0F, 0.0F));
            curl.addOrReplaceChild("finger_tip" + i,
                    CubeListBuilder.create()
                            .texOffs(SKIN_U, SKIN_V)
                            .addBox(-0.7F, -0.6F, -0.4F, 1.4F, 1.2F, 2.4F),
                    PartPose.offsetAndRotation(0.0F, 0.0F, 2.4F, 0.85F, 0.0F, 0.0F));
            // 指甲：贴在朝地的那一面上，从侧面与正面都能看到那条白。
            back.addOrReplaceChild("nail" + i,
                    CubeListBuilder.create()
                            .texOffs(NAIL_U, NAIL_V)
                            .addBox(-0.6F, 0.0F, -0.3F, 1.2F, 0.4F, 2.2F),
                    PartPose.offsetAndRotation(x, 0.55F, 5.2F, 0.55F, 0.0F, 0.0F));
        }

        // 拇指：在侧面朝前斜伸，也是弯着的，尖端带甲 —— 视频第 2 秒露出来的就是它。
        PartDefinition thumb = back.addOrReplaceChild("thumb",
                CubeListBuilder.create()
                        .texOffs(SKIN_U, SKIN_V)
                        .addBox(-0.85F, -0.85F, -1.2F, 1.7F, 1.7F, 3.6F),
                PartPose.offsetAndRotation(-4.9F, -0.6F, 1.4F, -0.5F, 0.35F, 0.1F));
        thumb.addOrReplaceChild("thumb_tip",
                CubeListBuilder.create()
                        .texOffs(SKIN_U, SKIN_V)
                        .addBox(-0.75F, -0.75F, 0.0F, 1.5F, 1.5F, 2.2F),
                PartPose.offset(0.0F, 0.0F, 2.2F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(KunJinKaoHandEntity entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {
        float amplitude = entity.squirmAmplitude(limbSwingAmount);
        // 尺蠖周期：limbSwing 随移动推进（所以拱动节奏与真实位移同步），idle 提供站立时的慢波。
        float cycle = limbSwing * 0.75F + ageInTicks * 0.03F;
        float hump = Mth.sin(cycle);

        // 拱起：背抬起来（只抬"正拱"的那半周期），同时整只手往前送一点 —— 一拱一拱。
        this.root.y = 0.35F + Math.max(0.0F, hump) * 0.9F * amplitude;
        this.root.z = hump * 0.7F * amplitude;
        this.root.xRot = -hump * 0.07F * amplitude;
        // 转向：朝目标/行走方向带一点，让它看起来是"这一坨自己在转弯"。
        this.root.yRot = netHeadYaw * (float) (Math.PI / 180.0D) * 0.3F;

        // 手背与腕部反相：背起来时腕部落下，形成"从头到尾"的波。
        this.back.xRot = -Math.max(0.0F, hump) * 0.16F * amplitude;
        this.wrist.xRot = Math.max(0.0F, hump) * 0.10F * amplitude;

        // 指尖贴着地：拱起时手指稍微伸直（把身体往前推），落下时弯回去勾住地面。
        for (int i = 0; i < 4; i++) {
            float phase = i * 0.7F;
            float push = Mth.sin(cycle + phase) * 0.18F * amplitude;
            this.fingerCurl[i].xRot = -1.15F + push;
            this.nails[i].xRot = 0.55F + push * 0.6F;
        }

        // 拇指：慢半拍地搭在侧面。
        this.thumb.xRot = -0.5F + Mth.sin(cycle + 2.0F) * 0.10F * amplitude;
    }

    @Override
    public void renderToBuffer(com.mojang.blaze3d.vertex.PoseStack poseStack,
                               com.mojang.blaze3d.vertex.VertexConsumer buffer,
                               int packedLight, int packedOverlay, int color) {
        this.root.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}