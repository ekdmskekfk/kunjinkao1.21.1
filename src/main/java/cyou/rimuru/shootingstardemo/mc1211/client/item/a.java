/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.item;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.n;
import cyou.rimuru.shootingstardemo.mc1211.registry.e;
import cyou.rimuru.shootingstardemo.mc1211.star.l;
import java.io.IOException;
import java.io.UncheckedIOException;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public final class a {
    public static final ResourceLocation a = ResourceLocation.fromNamespaceAndPath((String)"shooting_star_demo", (String)"item/stellar_remote_icon");
    private static final ModelResourceLocation d = ModelResourceLocation.standalone((ResourceLocation)a);
    private static final ResourceLocation e = ResourceLocation.fromNamespaceAndPath((String)"shooting_star_demo", (String)"textures/item/stellar_remote.png");
    private static final RenderType f = RenderType.entityCutoutNoCull((ResourceLocation)e);
    private static final RenderType g = RenderType.entityTranslucent((ResourceLocation)e);
    private static final RenderType h = RenderType.entityTranslucentEmissive((ResourceLocation)e);
    static ShaderInstance b;
    static ShaderInstance c;
    private static final float i = 128.0f;
    private static final float[] j;
    private static final float[] k;
    private static final float[] l;
    private static final float[] m;
    private static final float[] n;
    private static final float[] o;
    private static final float[] p;
    private static final float[] q;
    private static final float[] r;
    private static final float[] s;
    private static final float[] t;
    private static final float[] u;
    private static final float[] v;
    private static final float[] w;
    private static final int x = 0xF000F0;
    private static final int y = 0;
    private static final int z = 1;
    private static final int A = 2;
    private static final float B = 1.75f;
    private static final float C = 0.8f;
    private static final float D = 0.2f;
    private static final float E = 9.2f;
    private static final float F = 5.15f;
    private static final float G = 1.12f;
    private static long H;
    private static long I;

    public static void a(IEventBus iEventBus) {
        final a a2 = new a();
        iEventBus.addListener((net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent registerClientExtensionsEvent) -> registerClientExtensionsEvent.registerItem(new IClientItemExtensions(){
            private BlockEntityWithoutLevelRenderer b;

            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.b == null) {
                    Minecraft minecraft = Minecraft.getInstance();
                    this.b = new BlockEntityWithoutLevelRenderer(minecraft.getBlockEntityRenderDispatcher(), minecraft.getEntityModels()) {
                        public void renderByItem(ItemStack itemStack, ItemDisplayContext itemDisplayContext, PoseStack poseStack, MultiBufferSource multiBufferSource, int n2, int n3) {
                            a2.a(itemStack, itemDisplayContext, poseStack, multiBufferSource, n2, n3);
                        }
                    };
                }
                return this.b;
            }
        }, new Item[]{cyou.rimuru.shootingstardemo.mc1211.registry.e.a}));
        iEventBus.addListener((net.neoforged.neoforge.client.event.ModelEvent.RegisterAdditional registerAdditional) -> registerAdditional.register(d));
        iEventBus.addListener((net.neoforged.neoforge.client.event.RegisterShadersEvent registerShadersEvent) -> {
            try {
                registerShadersEvent.registerShader(new ShaderInstance(registerShadersEvent.getResourceProvider(), ResourceLocation.fromNamespaceAndPath((String)"shooting_star_demo", (String)"armillary_glow"), DefaultVertexFormat.POSITION_TEX_COLOR), shaderInstance -> {
                    b = shaderInstance;
                });
                registerShadersEvent.registerShader(new ShaderInstance(registerShadersEvent.getResourceProvider(), ResourceLocation.fromNamespaceAndPath((String)"shooting_star_demo", (String)"remote_screen"), DefaultVertexFormat.POSITION_TEX_COLOR), shaderInstance -> {
                    c = shaderInstance;
                });
            }
            catch (IOException iOException) {
                throw new UncheckedIOException(iOException);
            }
        });
    }

    public void a(ItemStack itemStack, ItemDisplayContext itemDisplayContext, PoseStack poseStack, MultiBufferSource multiBufferSource, int n2, int n3) {
        Minecraft minecraft = Minecraft.getInstance();
        if (itemDisplayContext == ItemDisplayContext.GUI) {
            poseStack.pushPose();
            poseStack.translate(0.5f, 0.5f, 0.5f);
            minecraft.getItemRenderer().render(itemStack, itemDisplayContext, false, poseStack, multiBufferSource, n2, n3, minecraft.getModelManager().getModel(d));
            poseStack.popPose();
            return;
        }
        float f2 = cyou.rimuru.shootingstardemo.mc1211.client.item.a.a();
        float f3 = (float)(Util.getMillis() % 3600000L) / 1000.0f;
        float f4 = f2 < 0.0f ? 0.0f : (1.0f - Mth.clamp((float)((f2 - 486.0f - 10.0f) / 10.0f), (float)0.0f, (float)1.0f)) * cyou.rimuru.shootingstardemo.mc1211.client.item.a.a(Mth.clamp((float)(f2 / 5.0f), (float)0.0f, (float)1.0f));
        float f5 = f2 >= 7.0f && f2 < 12.0f ? 1.0f : 0.0f;
        boolean bl = itemDisplayContext.firstPerson();
        poseStack.pushPose();
        poseStack.translate(0.5f, 0.0f, 0.5f);
        poseStack.scale(0.0625f, 0.0625f, 0.0625f);
        this.a(poseStack.last(), multiBufferSource.getBuffer(f), n2, n3);
        this.a(poseStack.last(), multiBufferSource.getBuffer(h), n3, f5, f2);
        this.a(poseStack.last(), multiBufferSource.getBuffer(cyou.rimuru.shootingstardemo.mc1211.client.item.a.RenderTypes.b), f3, f2);
        poseStack.pushPose();
        poseStack.translate(0.0f, 5.15f, 1.12f);
        poseStack.mulPose(Axis.XP.rotationDegrees(-118.0f * f4));
        poseStack.translate(0.0f, -5.15f, -1.12f);
        this.b(poseStack.last(), multiBufferSource.getBuffer(g), n2, n3);
        poseStack.popPose();
        this.a(poseStack.last(), multiBufferSource.getBuffer(cyou.rimuru.shootingstardemo.mc1211.client.item.a.RenderTypes.a), f3, f2, f4, bl);
        poseStack.popPose();
    }

    private static float a() {
        float f2;
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) {
            return -1.0f;
        }
        n n2 = null;
        int n3 = 352;
        for (l l2 : cyou.rimuru.shootingstardemo.mc1211.star.l.values()) {
            n n4 = cyou.rimuru.shootingstardemo.mc1211.client.fx.e.a(l2);
            if (n4 == null || n4.m() != minecraft.player.getId() || n2 != null && n4.p() >= n2.p()) continue;
            n2 = n4;
            n3 = switch (l2) {
                case cyou.rimuru.shootingstardemo.mc1211.star.l.b -> 322;
                case cyou.rimuru.shootingstardemo.mc1211.star.l.c -> 290;
                case cyou.rimuru.shootingstardemo.mc1211.star.l.d -> 462;
                default -> 352;
            };
        }
        if (n2 == null) {
            return -1.0f;
        }
        long l3 = Util.getMillis();
        if ((long)n2.p() != H) {
            H = n2.p();
            I = l3;
        }
        float f3 = (f2 = (float)n2.p() + Mth.clamp((float)((float)(l3 - I) / (50.0f / n2.j())), (float)0.0f, (float)Math.max(1.0f, n2.j()))) < 9.0f ? f2 : (f2 < (float)n3 ? 9.0f + (f2 - 9.0f) * 343.0f / (float)(n3 - 9) : 352.0f + (f2 - (float)n3));
        return f3 > 526.0f ? -1.0f : f3;
    }

    private static float a(float f2) {
        return 1.0f - (1.0f - f2) * (1.0f - f2) * (1.0f - f2);
    }

    private void a(PoseStack.Pose pose, VertexConsumer vertexConsumer, int n2, int n3) {
        float f2;
        float f3;
        int n4;
        this.a(pose, vertexConsumer, n2, n3, 1.75f, 0.8f, 0.2f, 0.0f, 9.2f, j, k, l, m);
        this.a(pose, vertexConsumer, n2, n3, 1.5f, 0.62f, 0.18f, 9.2f, 9.599999f, w, w, w, m);
        this.a(pose, vertexConsumer, n2, n3, 1.55f, 0.66f, 0.18f, -0.3f, 0.0f, v, v, v, v);
        this.a(pose, vertexConsumer, n2, n3, 1.73f, 0.7f, -0.52f, 1.88f, 5.5f, 0.52f, n, n, n, v);
        this.a(pose, vertexConsumer, n2, n3, -1.88f, 0.7f, -0.52f, -1.73f, 5.5f, 0.52f, n, n, n, v);
        this.a(pose, vertexConsumer, n2, n3, -1.4f, 5.95f, 0.78000003f, 1.4f, 8.75f, 0.93f, o, v, v, v);
        this.a(pose, vertexConsumer, n2, n3, -1.46f, 8.72f, 0.78000003f, 1.46f, 8.92f, 1.1600001f, w, w, w, w);
        this.a(pose, vertexConsumer, n2, n3, -1.08f, 2.75f, 0.78000003f, 1.08f, 5.15f, 1.12f, p, v, v, v);
        this.a(pose, vertexConsumer, n2, n3, -1.16f, 5.09f, 1.04f, 1.16f, 5.29f, 1.24f, u, u, u, u);
        for (n4 = 0; n4 < 6; ++n4) {
            f3 = (float)(n4 % 3 - 1) * 0.98f;
            f2 = n4 < 3 ? 1.3f : 0.5f;
            float[] fArray = new float[]{(float)n4 * 8.0f, 74.0f, 8.0f, 8.0f};
            this.a(pose, vertexConsumer, n2, n3, f3 - 0.4f, f2, 0.78000003f, f3 + 0.4f, f2 + 0.58f, 0.94f, fArray, v, v, v);
        }
        for (n4 = 0; n4 < 4; ++n4) {
            f3 = (n4 % 2 == 0 ? -1.0f : 1.0f) * 1.4f;
            f2 = n4 < 2 ? 0.25f : 9.0f;
            this.a(pose, vertexConsumer, n2, n3, f3 - 0.08f, f2 - 0.08f, 0.79f, f3 + 0.08f, f2 + 0.08f, 0.84000003f, u, u, u, u);
        }
        this.a(pose, vertexConsumer, n2, n3, -0.85f, 3.2f, -0.96000004f, 0.85f, 8.2f, -0.78000003f, t, t, v, v);
        this.a(pose, vertexConsumer, n2, n3, -0.85f, 8.2f, -0.96000004f, 0.85f, 8.45f, -0.7f, v, v, v, v);
        this.a(pose, vertexConsumer, n2, n3, -1.46f, 9.599999f, -0.26f, -0.94f, 10.3f, 0.26f, v, v, v, v);
        this.a(pose, vertexConsumer, n2, n3, -1.34f, 10.3f, -0.14f, -1.06f, 12.0f, 0.14f, s, s, s, v);
        this.a(pose, vertexConsumer, n2, n3, -1.28f, 12.0f, -0.08f, -1.12f, 13.6f, 0.08f, u, u, u, u);
        this.a(pose, vertexConsumer, n2, n3, -1.36f, 13.6f, -0.16f, -1.04f, 13.92f, 0.16f, v, v, v, v);
        this.a(pose, vertexConsumer, n2, n3, -0.34f, -0.75f, -0.08f, -0.2f, -0.3f, 0.08f, u, u, u, u);
        this.a(pose, vertexConsumer, n2, n3, 0.2f, -0.75f, -0.08f, 0.34f, -0.3f, 0.08f, u, u, u, u);
        this.a(pose, vertexConsumer, n2, n3, -0.34f, -0.89f, -0.08f, 0.34f, -0.75f, 0.08f, u, u, u, u);
    }

    private void a(PoseStack.Pose pose, VertexConsumer vertexConsumer, int n2, float f2, float f3) {
        float f4 = 0.46f - 0.32f * f2;
        int n3 = f3 >= 0.0f && f3 < 486.0f ? -1 : -3092272;
        float f5 = 3.95f;
        this.a(pose, vertexConsumer, 0xF000F0, n2, -0.74f, f5 - 0.5f, 1.12f, 0.74f, f5 + 0.5f, 1.12f + f4, q, q, q, q, n3);
        this.a(pose, vertexConsumer, 0xF000F0, n2, -0.52f, f5 - 0.72f, 1.12f, 0.52f, f5 + 0.72f, 1.12f + f4, q, q, q, q, n3);
        this.a(pose, vertexConsumer, 0xF000F0, n2, -0.6f, f5 - 0.6f, 1.12f, 0.6f, f5 + 0.6f, 1.12f + f4 + 0.06f, q, q, q, q, n3);
    }

    private void b(PoseStack.Pose pose, VertexConsumer vertexConsumer, int n2, int n3) {
        float f2 = 1.12f;
        float f3 = 1.84f;
        float f4 = 2.7f;
        this.a(pose, vertexConsumer, n2, n3, -1.14f, f4, f3 - 0.12f, 1.14f, 5.15f, f3, r, r, r, r);
        this.a(pose, vertexConsumer, n2, n3, -1.14f, f4, f2, -1.0f, 5.15f, f3 - 0.12f, r, r, r, r);
        this.a(pose, vertexConsumer, n2, n3, 1.0f, f4, f2, 1.14f, 5.15f, f3 - 0.12f, r, r, r, r);
        this.a(pose, vertexConsumer, n2, n3, -1.0f, f4, f2, 1.0f, f4 + 0.14f, f3 - 0.12f, r, r, r, r);
    }

    private void a(PoseStack.Pose pose, VertexConsumer vertexConsumer, float f2, float f3) {
        boolean bl = f3 >= 0.0f && f3 < 486.0f;
        float f4 = bl ? Mth.clamp((float)((f3 - 9.0f) / 12.0f), (float)0.0f, (float)1.0f) : 0.0f;
        f4 = f4 * f4 * (3.0f - 2.0f * f4);
        float f5 = bl && f3 >= 9.0f ? (float)(cyou.rimuru.shootingstardemo.mc1211.client.item.a.a((double)f3) % 1.0) : 0.0f;
        float f6 = bl && f3 >= 352.0f ? Math.max(0.0f, 1.0f - (f3 - 352.0f) / 8.0f) : 0.0f;
        float f7 = bl ? Mth.clamp((float)((f3 - 9.0f) / 343.0f), (float)0.0f, (float)1.0f) : 0.0f;
        float f8 = (float)Math.round(f7 * 20.0f) * 4.0f;
        float f9 = f2 * 0.5f % 1.0f;
        float f10 = -1.12f;
        float f11 = 1.12f;
        float f12 = 6.18f;
        float f13 = 8.52f;
        float f14 = 0.935f;
        vertexConsumer.addVertex(pose.pose(), f10, f12, f14).setUv(f8 - 1.0f, -1.0f).setColor(f9, f4, f5, f6);
        vertexConsumer.addVertex(pose.pose(), f11, f12, f14).setUv(f8 + 1.0f, -1.0f).setColor(f9, f4, f5, f6);
        vertexConsumer.addVertex(pose.pose(), f11, f13, f14).setUv(f8 + 1.0f, 1.0f).setColor(f9, f4, f5, f6);
        vertexConsumer.addVertex(pose.pose(), f10, f13, f14).setUv(f8 - 1.0f, 1.0f).setColor(f9, f4, f5, f6);
    }

    private void a(PoseStack.Pose pose, VertexConsumer vertexConsumer, int n2, int n3, float f2, float f3, float f4, float f5, float f6, float[] fArray, float[] fArray2, float[] fArray3, float[] fArray4) {
        int[][] nArrayArray;
        float[] fArray5;
        float[][] fArrayArray = new float[][]{{f2 - f4, f3}, {-f2 + f4, f3}, {-f2, f3 - f4}, {-f2, -f3 + f4}, {-f2 + f4, -f3}, {f2 - f4, -f3}, {f2, -f3 + f4}, {f2, f3 - f4}};
        for (int i2 = 0; i2 < 8; ++i2) {
            float[] object = fArrayArray[i2];
            float[] fArray6 = fArrayArray[(i2 + 1) % 8];
            float f7 = fArray6[1] - object[1];
            float f8 = object[0] - fArray6[0];
            float fArray8 = (float)Math.sqrt(f7 * f7 + f8 * f8);
            f7 /= fArray8;
            float[] fArray7 = (f8 /= fArray8) > 0.9f ? fArray : (f8 < -0.9f ? fArray2 : fArray3);
            if (Math.abs(f7) < 0.9f && Math.abs(f8) < 0.9f) {
                fArray7 = new float[]{fArray3[0], fArray3[1], 1.0f, fArray3[3]};
            }
            this.a(pose, vertexConsumer, n2, n3, -1, (Vector3fc)new Vector3f(f7, 0.0f, f8), fArray7, fArray6[0], f5, fArray6[1], object[0], f5, object[1], object[0], f6, object[1], fArray6[0], f6, fArray6[1]);
        }
        for (int[] nArray : nArrayArray = new int[][]{{0, 1, 2, 7}, {7, 2, 3, 6}, {6, 3, 4, 5}}) {
            float[] fArray8 = fArrayArray[nArray[0]];
            fArray5 = fArrayArray[nArray[1]];
            float[] fArray9 = fArrayArray[nArray[2]];
            float[] fArray10 = fArrayArray[nArray[3]];
            this.a(pose, vertexConsumer, n2, n3, -1, (Vector3fc)new Vector3f(0.0f, 1.0f, 0.0f), fArray4, fArray8[0], f6, fArray8[1], fArray10[0], f6, fArray10[1], fArray9[0], f6, fArray9[1], fArray5[0], f6, fArray5[1]);
            this.a(pose, vertexConsumer, n2, n3, -1, (Vector3fc)new Vector3f(0.0f, -1.0f, 0.0f), fArray4, fArray5[0], f5, fArray5[1], fArray9[0], f5, fArray9[1], fArray10[0], f5, fArray10[1], fArray8[0], f5, fArray8[1]);
        }
    }

    private void a(PoseStack.Pose pose, VertexConsumer vertexConsumer, int n2, int n3, float f2, float f3, float f4, float f5, float f6, float f7, float[] fArray, float[] fArray2, float[] fArray3, float[] fArray4) {
        this.a(pose, vertexConsumer, n2, n3, f2, f3, f4, f5, f6, f7, fArray, fArray2, fArray3, fArray4, -1);
    }

    private void a(PoseStack.Pose pose, VertexConsumer vertexConsumer, int n2, int n3, float f2, float f3, float f4, float f5, float f6, float f7, float[] fArray, float[] fArray2, float[] fArray3, float[] fArray4, int n4) {
        this.a(pose, vertexConsumer, n2, n3, n4, (Vector3fc)new Vector3f(0.0f, 0.0f, 1.0f), fArray, f2, f3, f7, f5, f3, f7, f5, f6, f7, f2, f6, f7);
        this.a(pose, vertexConsumer, n2, n3, n4, (Vector3fc)new Vector3f(0.0f, 0.0f, -1.0f), fArray2, f5, f3, f4, f2, f3, f4, f2, f6, f4, f5, f6, f4);
        this.a(pose, vertexConsumer, n2, n3, n4, (Vector3fc)new Vector3f(1.0f, 0.0f, 0.0f), fArray3, f5, f3, f7, f5, f3, f4, f5, f6, f4, f5, f6, f7);
        this.a(pose, vertexConsumer, n2, n3, n4, (Vector3fc)new Vector3f(-1.0f, 0.0f, 0.0f), fArray3, f2, f3, f4, f2, f3, f7, f2, f6, f7, f2, f6, f4);
        this.a(pose, vertexConsumer, n2, n3, n4, (Vector3fc)new Vector3f(0.0f, 1.0f, 0.0f), fArray4, f2, f6, f7, f5, f6, f7, f5, f6, f4, f2, f6, f4);
        this.a(pose, vertexConsumer, n2, n3, n4, (Vector3fc)new Vector3f(0.0f, -1.0f, 0.0f), fArray4, f2, f3, f4, f5, f3, f4, f5, f3, f7, f2, f3, f7);
    }

    private void a(PoseStack.Pose pose, VertexConsumer vertexConsumer, int n2, int n3, int n4, Vector3fc vector3fc, float[] fArray, float ... fArray2) {
        float[][] fArrayArray = new float[][]{{0.0f, 1.0f}, {1.0f, 1.0f}, {1.0f, 0.0f}, {0.0f, 0.0f}};
        for (int i2 = 0; i2 < 4; ++i2) {
            vertexConsumer.addVertex(pose.pose(), fArray2[i2 * 3], fArray2[i2 * 3 + 1], fArray2[i2 * 3 + 2]).setColor(n4).setUv((fArray[0] + fArray[2] * fArrayArray[i2][0]) / 128.0f, (fArray[1] + fArray[3] * fArrayArray[i2][1]) / 128.0f).setOverlay(n3).setLight(n2).setNormal(pose, vector3fc.x(), vector3fc.y(), vector3fc.z());
        }
    }

    private void a(PoseStack.Pose pose, VertexConsumer vertexConsumer, float f2, float f3, float f4, boolean bl) {
        Matrix4f matrix4f = new Matrix4f((Matrix4fc)RenderSystem.getModelViewMatrix()).mul((Matrix4fc)pose.pose());
        Matrix3f matrix3f = new Matrix3f((Matrix4fc)matrix4f).invert();
        Vector3f vector3f = matrix3f.transform(new Vector3f(1.0f, 0.0f, 0.0f)).normalize();
        Vector3f vector3f2 = matrix3f.transform(new Vector3f(0.0f, 1.0f, 0.0f)).normalize();
        boolean bl2 = f3 >= 0.0f && f3 < 486.0f;
        Vector3f vector3f3 = new Vector3f(0.0f, 7.35f, 1.0f);
        Vector3f vector3f4 = new Vector3f(1.0f, 0.0f, 0.0f);
        Vector3f vector3f5 = new Vector3f(0.0f, 1.0f, 0.0f);
        float f5 = bl2 && f3 >= 9.0f ? 1.0f : 0.35f;
        float f6 = bl2 && f3 >= 352.0f ? Math.max(0.0f, 1.0f - (f3 - 352.0f) / 8.0f) : 0.0f;
        cyou.rimuru.shootingstardemo.mc1211.client.item.a.a(pose, vertexConsumer, (Vector3fc)vector3f3, (Vector3fc)vector3f4, (Vector3fc)vector3f5, 1.9f, 1.7f, 0, 1.0f, 0.12f, 0.16f, 0.22f * f5 + 0.9f * f6);
        Vector3f vector3f6 = new Vector3f(0.0f, 3.95f, 1.6700001f);
        float f7 = 0.75f + 0.25f * Mth.sin((float)(f2 * (bl2 ? 9.0f : 2.0f)));
        cyou.rimuru.shootingstardemo.mc1211.client.item.a.a(pose, vertexConsumer, (Vector3fc)vector3f, (Vector3fc)vector3f2, (Vector3fc)vector3f6, 1.4f + 1.2f * f4, 0, 1.0f, 0.1f, 0.14f, (0.12f + 0.4f * f4) * f7);
        float f8 = bl2 ? (Mth.sin((float)(f2 * 18.0f)) > 0.0f ? 1.0f : 0.25f) : (f2 % 1.4f < 0.12f ? 1.0f : 0.15f);
        Vector3f vector3f7 = new Vector3f(-1.2f, 13.76f, 0.0f);
        cyou.rimuru.shootingstardemo.mc1211.client.item.a.a(pose, vertexConsumer, (Vector3fc)vector3f, (Vector3fc)vector3f2, (Vector3fc)vector3f7, 0.9f + 0.6f * f8, 0, 1.0f, 0.12f, 0.15f, 0.8f * f8);
        cyou.rimuru.shootingstardemo.mc1211.client.item.a.a(pose, vertexConsumer, (Vector3fc)vector3f, (Vector3fc)vector3f2, (Vector3fc)vector3f7, 0.45f, 2, 1.0f, 0.8f, 0.8f, 0.9f * f8);
        float[][] fArrayArray = new float[][]{{0.3f, 1.0f, 0.45f}, {1.0f, 0.12f, 0.15f}, {1.0f, 0.65f, 0.15f}};
        for (int i2 = 0; i2 < 3; ++i2) {
            Vector3f vector3f8 = new Vector3f(-0.5f + (float)i2 * 0.5f, 5.68f, 0.83f);
            float f9 = i2 == 0 ? 1.0f : (i2 == 1 ? (bl2 ? 1.0f : 0.12f) : (bl2 ? (Mth.sin((float)(f2 * 11.0f)) > 0.0f ? 1.0f : 0.2f) : (f2 % 2.0f < 0.1f ? 1.0f : 0.1f)));
            cyou.rimuru.shootingstardemo.mc1211.client.item.a.a(pose, vertexConsumer, (Vector3fc)vector3f8, (Vector3fc)vector3f4, (Vector3fc)vector3f5, 0.24f, 0.24f, 0, fArrayArray[i2][0], fArrayArray[i2][1], fArrayArray[i2][2], 0.9f * f9);
        }
    }

    private static double a(double d2) {
        int n2 = 29;
        if (d2 < (double)n2) {
            return (d2 - 9.0) / 20.0 * 0.6;
        }
        double d3 = (double)(352 - n2) / 20.0;
        double d4 = Math.min(d2 - (double)n2, (double)(352 - n2)) / 20.0;
        double d5 = d4 / d3;
        return 0.6 * d4 + 3.4 * d3 * d5 * d5 * d5 / 3.0;
    }

    private static void a(PoseStack.Pose pose, VertexConsumer vertexConsumer, Vector3fc vector3fc, Vector3fc vector3fc2, Vector3fc vector3fc3, float f2, int n2, float f3, float f4, float f5, float f6) {
        cyou.rimuru.shootingstardemo.mc1211.client.item.a.a(pose, vertexConsumer, vector3fc3, vector3fc, vector3fc2, f2, f2, n2, f3, f4, f5, f6);
    }

    private static void a(PoseStack.Pose pose, VertexConsumer vertexConsumer, Vector3fc vector3fc, Vector3fc vector3fc2, Vector3fc vector3fc3, float f2, float f3, int n2, float f4, float f5, float f6, float f7) {
        if (f7 <= 0.004f) {
            return;
        }
        float f8 = (float)n2 * 4.0f;
        cyou.rimuru.shootingstardemo.mc1211.client.item.a.a(pose, vertexConsumer, vector3fc, vector3fc2, vector3fc3, -f2, -f3, f8 - 1.0f, -1.0f, f4, f5, f6, f7);
        cyou.rimuru.shootingstardemo.mc1211.client.item.a.a(pose, vertexConsumer, vector3fc, vector3fc2, vector3fc3, f2, -f3, f8 + 1.0f, -1.0f, f4, f5, f6, f7);
        cyou.rimuru.shootingstardemo.mc1211.client.item.a.a(pose, vertexConsumer, vector3fc, vector3fc2, vector3fc3, f2, f3, f8 + 1.0f, 1.0f, f4, f5, f6, f7);
        cyou.rimuru.shootingstardemo.mc1211.client.item.a.a(pose, vertexConsumer, vector3fc, vector3fc2, vector3fc3, -f2, f3, f8 - 1.0f, 1.0f, f4, f5, f6, f7);
    }

    private static void a(PoseStack.Pose pose, VertexConsumer vertexConsumer, Vector3fc vector3fc, Vector3fc vector3fc2, Vector3fc vector3fc3, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        vertexConsumer.addVertex(pose.pose(), vector3fc.x() + vector3fc2.x() * f2 + vector3fc3.x() * f3, vector3fc.y() + vector3fc2.y() * f2 + vector3fc3.y() * f3, vector3fc.z() + vector3fc2.z() * f2 + vector3fc3.z() * f3).setUv(f4, f5).setColor(f6, f7, f8, Math.min(1.0f, f9));
    }

    static {
        j = new float[]{0.0f, 0.0f, 28.0f, 74.0f};
        k = new float[]{28.0f, 0.0f, 28.0f, 74.0f};
        l = new float[]{56.0f, 0.0f, 13.0f, 74.0f};
        m = new float[]{69.0f, 0.0f, 28.0f, 13.0f};
        n = new float[]{97.0f, 0.0f, 10.0f, 40.0f};
        o = new float[]{69.0f, 13.0f, 24.0f, 24.0f};
        p = new float[]{107.0f, 0.0f, 20.0f, 20.0f};
        q = new float[]{107.0f, 20.0f, 16.0f, 16.0f};
        r = new float[]{69.0f, 37.0f, 24.0f, 24.0f};
        s = new float[]{93.0f, 40.0f, 6.0f, 40.0f};
        t = new float[]{48.0f, 74.0f, 18.0f, 34.0f};
        u = new float[]{66.0f, 74.0f, 8.0f, 8.0f};
        v = new float[]{74.0f, 74.0f, 8.0f, 8.0f};
        w = new float[]{82.0f, 74.0f, 24.0f, 6.0f};
        H = Long.MIN_VALUE;
    }

    private static final class RenderTypes
    extends RenderType {
        static final RenderType a = cyou.rimuru.shootingstardemo.mc1211.client.item.a.RenderTypes.create((String)"shooting_star_demo_remote_glow", (VertexFormat)DefaultVertexFormat.POSITION_TEX_COLOR, (VertexFormat.Mode)VertexFormat.Mode.QUADS, (int)256, (boolean)false, (boolean)true, (RenderType.CompositeState)RenderType.CompositeState.builder().setShaderState(new RenderStateShard.ShaderStateShard(() -> cyou.rimuru.shootingstardemo.mc1211.client.item.a.b)).setTransparencyState(LIGHTNING_TRANSPARENCY).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).createCompositeState(false));
        static final RenderType b = cyou.rimuru.shootingstardemo.mc1211.client.item.a.RenderTypes.create((String)"shooting_star_demo_remote_screen", (VertexFormat)DefaultVertexFormat.POSITION_TEX_COLOR, (VertexFormat.Mode)VertexFormat.Mode.QUADS, (int)256, (boolean)false, (boolean)true, (RenderType.CompositeState)RenderType.CompositeState.builder().setShaderState(new RenderStateShard.ShaderStateShard(() -> c)).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setCullState(NO_CULL).createCompositeState(false));

        private RenderTypes() {
            super("", DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS, 0, false, false, () -> {}, () -> {});
        }
    }
}

