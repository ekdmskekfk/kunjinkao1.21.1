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
 * 君君的模型：一只攥紧的拳头，手背朝上、指峰隆起、拇指锁扣、指尖贴地。
 * <p>
 * <b>坐标系备忘（这次栽过跟头，写下来防止再栽）：</b>
 * MC 实体模型的坐标是 <b>Y 翻转</b>的 —— {@code LivingEntityRenderer#render} 里做了
 * {@code scale(-1,-1,1)} + {@code translate(0,-1.501,0)}，于是：
 * <ul>
 *   <li><b>y_model = 24 是地面</b>（原版人形的脚底就在 24），越往上 y 越小；</li>
 *   <li><b>+Y_model 指向地下</b>，所以"往下蜷"是 <b>正的 xRot</b>（(0,0,-1) 绕 X 转正角 → y_model 增大 = 朝地）；</li>
 *   <li>Z 不翻转，模型 -Z 是脸的朝向（原版四足动物的头在 -Z）。</li>
 * </ul>
 * 之前那版"又高又反"就是这两条没落实：部件放在 y=0~6（悬空 1.1~1.5 格）、
 * 手指用负角蜷（结果翘向天）。
 * <p>
 * 姿态按描述落实：四指从指根内卷蜷进掌下、指峰隆起一截、拇指横向锁扣（侧面半包）、
 * 指甲从指缝两侧露出顺指尖斜指、手背视角只见手背与骨节。
 * <p>
 * 蠕动 = 尺蠖式一拱一拱：以腕部为支点，拳面绕它拱起（抬高 + 前送）再落下；
 * {@code limbSwing} 当周期时钟，拱动节奏与真实位移同步。
 */
public class KunJinKaoHandModel extends EntityModel<KunJinKaoHandEntity> {

    public static final ModelLayerLocation LAYER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "hand"), "main");

    private static final int SKIN_U = 0;
    private static final int SKIN_V = 0;
    private static final int NAIL_U = 32;
    private static final int NAIL_V = 0;

    /** 模型坐标里的"地面"：见类注释，Y 翻转、24 = 脚底。 */
    private static final float GROUND = 24.0F;

    private final ModelPart root;
    private final ModelPart back;
    private final ModelPart[] fingerCurl = new ModelPart[4];
    private final ModelPart[] nails = new ModelPart[4];
    private final ModelPart thumb;

    public KunJinKaoHandModel(ModelPart root) {
        this.root = root.getChild("root");
        this.back = this.root.getChild("back");
        for (int i = 0; i < 4; i++) {
            this.fingerCurl[i] = this.back.getChild("finger" + i);
            this.nails[i] = this.fingerCurl[i].getChild("nail" + i);
        }
        this.thumb = this.back.getChild("thumb");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot().addOrReplaceChild("root",
                CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        // 腕部断口：贴地（y 21..24），在 +Z（身后），端面有暗色切面（贴图）。
        root.addOrReplaceChild("wrist", CubeListBuilder.create()
                        .texOffs(SKIN_U, SKIN_V)
                        .addBox(-3.1F, -1.5F, -2.6F, 6.2F, 3.0F, 4.6F),
                PartPose.offset(0.0F, GROUND - 1.5F, 2.8F));

        // 拳体：支点放在腕部上前沿（绕它转 = 从腕部拱起，腕不悬空）。
        // 拳体底面正好在 GROUND。
        PartDefinition back = root.addOrReplaceChild("back", CubeListBuilder.create()
                        .texOffs(SKIN_U, SKIN_V)
                        .addBox(-4.3F, -1.7F, -6.6F, 8.6F, 4.2F, 6.6F),
                PartPose.offset(0.0F, GROUND - 2.5F, 1.2F));

        // 指峰：叠在拳体上沿（y 更小 = 世界更高），比拳面高出一截。
        back.addOrReplaceChild("knuckle", CubeListBuilder.create()
                        .texOffs(SKIN_U, SKIN_V)
                        .addBox(-3.7F, -0.85F, -2.0F, 7.4F, 1.7F, 4.0F),
                PartPose.offset(0.0F, GROUND - 5.9F, -3.8F));

        // 四指内卷：从拳体前缘向下蜷（正角 = 朝地），指尖贴地。
        // 手背视角看不到它们；只有前缘与侧面露出一点弧度。
        float[] centerX = {-3.05F, -1.05F, 0.95F, 2.95F};
        for (int i = 0; i < 4; i++) {
            PartDefinition curl = back.addOrReplaceChild("finger" + i,
                    CubeListBuilder.create()
                            .texOffs(SKIN_U, SKIN_V)
                            .addBox(-0.85F, -0.7F, -1.3F, 1.7F, 1.4F, 2.6F),
                    PartPose.offsetAndRotation(centerX[i], GROUND - 3.2F, -5.2F, 1.2F, 0.0F, 0.0F));
            // 指甲：比手指宽（1.9 > 1.7），从两侧露出来；顺着指尖方向斜指（正角 = 朝地）。
            curl.addOrReplaceChild("nail" + i,
                    CubeListBuilder.create()
                            .texOffs(NAIL_U, NAIL_V)
                            .addBox(-0.95F, -0.22F, 0.1F, 1.9F, 0.44F, 1.7F),
                    PartPose.offsetAndRotation(0.0F, 0.0F, 0.9F, 0.55F, 0.0F, 0.0F));
        }

        // 拇指锁扣：横跨拳面前方（绕 Y 转约 69°，让它"横"过来），侧面看微微张开呈半包；
        // 甲面朝外上，从侧面与上方都能看到。
        PartDefinition thumb = back.addOrReplaceChild("thumb",
                CubeListBuilder.create()
                        .texOffs(SKIN_U, SKIN_V)
                        .addBox(-1.0F, -0.85F, -1.5F, 2.0F, 1.7F, 3.4F),
                PartPose.offsetAndRotation(-1.4F, GROUND - 3.1F, -4.9F, -0.15F, 1.15F, 0.0F));
        thumb.addOrReplaceChild("thumb_nail",
                CubeListBuilder.create()
                        .texOffs(NAIL_U, NAIL_V)
                        .addBox(-1.0F, -0.2F, -0.3F, 2.0F, 0.4F, 1.5F),
                PartPose.offset(0.0F, 0.75F, 1.6F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(KunJinKaoHandEntity entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {
        float amplitude = entity.squirmAmplitude(limbSwingAmount);
        // 尺蠖周期：limbSwing 随移动推进 → 拱动节奏与真实位移同步；idle 提供站立时的慢波。
        float cycle = limbSwing * 0.75F + ageInTicks * 0.03F;
        float hump = Mth.sin(cycle);
        float lift = Math.max(0.0F, hump);

        // 拱起 + 前送：注意 Y 翻转 —— "往上"是 y 减小。
        this.root.y = -lift * 0.5F * amplitude;
        this.root.z = hump * 0.55F * amplitude;
        this.root.xRot = 0.0F;
        this.root.yRot = netHeadYaw * (float) (Math.PI / 180.0D) * 0.3F;

        // 拱起：拳面绕腕部支点向上抬（抬起前端 = 让 -Z 端的 y_model 减小 = 负角）。
        this.back.xRot = -lift * 0.22F * amplitude;

        // 四指：拱起（推地）时稍微伸直（正角减小），落下时蜷紧（正角加大）勾住地面。
        for (int i = 0; i < 4; i++) {
            float phase = i * 0.6F;
            float push = Mth.sin(cycle + phase) * 0.16F * amplitude;
            this.fingerCurl[i].xRot = 1.2F - push;
            this.nails[i].xRot = 0.55F - push * 0.5F;
        }

        // 拇指：锁住不参与蠕动，只给一点随周期的沉浮。
        this.thumb.xRot = -0.15F + hump * 0.05F * amplitude;
    }

    @Override
    public void renderToBuffer(com.mojang.blaze3d.vertex.PoseStack poseStack,
                               com.mojang.blaze3d.vertex.VertexConsumer buffer,
                               int packedLight, int packedOverlay, int color) {
        this.root.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}