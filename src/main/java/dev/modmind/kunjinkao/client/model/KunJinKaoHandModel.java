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
 * 君君的模型：一只**攥紧的拳头**，手背朝上、指峰高高隆起、拇指横向锁扣、指尖贴地。
 * <p>
 * 姿态按描述逐条落实：
 * <ul>
 *   <li><b>四指内卷</b>：四根手指从指根向内大弯（约 -70°），蜷在掌下 —— 从手背视角
 *       只看到光洁的手背与隆起的骨节，看不到手指；</li>
 *   <li><b>指峰隆起</b>：手背前缘另起一条"指节脊"，比拳面高一截；</li>
 *   <li><b>皮肤褶皱</b>：指节处的横向深褶与肉窝画在贴图里（皮肤区的那几条深线）；</li>
 *   <li><b>拇指锁扣</b>：横向跨过拳面前方，压在蜷起的食指/中指侧面 —— 从侧面看微微张开、
 *       呈半包，指甲朝外；</li>
 *   <li><b>指甲朝向</b>：手背视角完全看不见（它们朝掌心），侧面能看到 —— 指甲比手指宽、
 *       从两侧露出来，顺着指尖方向斜指，甲缘整齐（贴图里描了边）。</li>
 * </ul>
 * <p>
 * <b>方向：</b>MC 实体模型的脸朝 <b>-Z</b>（原版四足动物的头都在 -Z），所以手指朝 -Z、
 * 腕部断口在 +Z —— 之前"方向反了"就是手指放到了 +Z，整只手倒着爬。
 * <b>贴地：</b>拳体底面 y=0（0 格离地），起伏只抬"拱起的那半周期"且腕部保持落地。
 * <p>
 * <b>蠕动 = 尺蠖式一拱一拱</b>：以腕部为支点，手背绕它拱起（整体抬高 + 往前送一点）再落下；
 * 用 {@code limbSwing} 当周期时钟，拱动节奏与真实移动速度同步。全是模型动画，实体层用普通寻路。
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

        // 腕部断口：贴地、在 +Z（身后），端面有暗色切面（贴图）。
        root.addOrReplaceChild("wrist", CubeListBuilder.create()
                        .texOffs(SKIN_U, SKIN_V)
                        .addBox(-3.1F, -1.5F, -2.6F, 6.2F, 3.0F, 4.6F),
                PartPose.offset(0.0F, 1.5F, 4.2F));

        // 拳体：支点放在腕部的上前沿 —— 这样绕它转（back.xRot）就是"从腕部拱起"，腕不悬空。
        PartDefinition back = root.addOrReplaceChild("back", CubeListBuilder.create()
                        .texOffs(SKIN_U, SKIN_V)
                        .addBox(-4.3F, -1.7F, -6.6F, 8.6F, 4.2F, 6.6F),
                PartPose.offset(0.0F, 1.7F, 1.2F));

        // 指峰：叠在拳体前上方，比拳面高出一截 —— 四指内卷顶出来的骨节。
        back.addOrReplaceChild("knuckle", CubeListBuilder.create()
                        .texOffs(SKIN_U, SKIN_V)
                        .addBox(-3.7F, -0.85F, -2.0F, 7.4F, 1.7F, 4.0F),
                PartPose.offset(0.0F, 3.35F, -3.8F));

        // 四指内卷：藏在拳体前缘之下，转角 -1.2（约 70°）让它们"卷进掌心"。
        // 手背视角看不到它们；只有前缘与侧面露出一点弧度。
        float[] centerX = {-3.05F, -1.05F, 0.95F, 2.95F};
        for (int i = 0; i < 4; i++) {
            PartDefinition curl = back.addOrReplaceChild("finger" + i,
                    CubeListBuilder.create()
                            .texOffs(SKIN_U, SKIN_V)
                            .addBox(-0.85F, -0.7F, -1.3F, 1.7F, 1.4F, 2.6F),
                    PartPose.offsetAndRotation(centerX[i], -0.2F, -5.2F, -1.2F, 0.0F, 0.0F));
            // 指甲：比手指宽（1.9 > 1.7），从两侧都露出来 —— 顺着指尖方向斜指，
            // 手背视角朝掌心看不见，侧面看是一条整齐的白。
            curl.addOrReplaceChild("nail" + i,
                    CubeListBuilder.create()
                            .texOffs(NAIL_U, NAIL_V)
                            .addBox(-0.95F, -0.22F, 0.1F, 1.9F, 0.44F, 1.7F),
                    PartPose.offsetAndRotation(0.0F, 0.0F, 0.9F, 0.55F, 0.0F, 0.0F));
        }

        // 拇指锁扣：横向跨在拳面前方，压住蜷起的食指/中指那一侧；
        // 侧面看微微张开（半包）。甲面朝上前方，从侧面与上方都能看到。
        PartDefinition thumb = back.addOrReplaceChild("thumb",
                CubeListBuilder.create()
                        .texOffs(SKIN_U, SKIN_V)
                        .addBox(-1.0F, -0.85F, -1.5F, 2.0F, 1.7F, 3.4F),
                PartPose.offsetAndRotation(-1.2F, -0.5F, -5.4F, -0.15F, 0.42F, 0.0F));
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
        // 尺蠖周期：limbSwing 随移动推进 → 拱动节奏与真实位移同步；idle 提供站立的慢波。
        float cycle = limbSwing * 0.75F + ageInTicks * 0.03F;
        float hump = Mth.sin(cycle);
        float lift = Math.max(0.0F, hump);

        // 拱起 + 前送：背绕腕部支点抬，整只手往前送半格模型的距离，再落下。
        // 没有基线高度 —— 拳底始终贴地（上一版"离地太高"就是这里给了常量抬高）。
        this.root.y = lift * 0.5F * amplitude;
        this.root.z = hump * 0.55F * amplitude;
        this.root.xRot = 0.0F;
        this.root.yRot = netHeadYaw * (float) (Math.PI / 180.0D) * 0.3F;

        // 拱起时手腕下压（掌屈），拳面正对前方 —— 描述里的第 3 条。
        this.back.xRot = -lift * 0.22F * amplitude;

        // 四指：拱起（推地）时稍微伸直，落下时蜷紧勾住地面。
        for (int i = 0; i < 4; i++) {
            float phase = i * 0.6F;
            float push = Mth.sin(cycle + phase) * 0.16F * amplitude;
            this.fingerCurl[i].xRot = -1.2F + push;
            this.nails[i].xRot = 0.55F + push * 0.5F;
        }

        // 拇指：锁住不参与蠕动，只给一点点随周期的沉浮。
        this.thumb.xRot = -0.15F + hump * 0.05F * amplitude;
    }

    @Override
    public void renderToBuffer(com.mojang.blaze3d.vertex.PoseStack poseStack,
                               com.mojang.blaze3d.vertex.VertexConsumer buffer,
                               int packedLight, int packedOverlay, int color) {
        this.root.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}