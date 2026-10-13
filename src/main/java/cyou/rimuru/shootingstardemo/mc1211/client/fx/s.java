/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.fx;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.u;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;

final class s {
    private static final int[] a = new int[]{10, 15, 20, 30, 40, 60};
    private static final float b = 7.0f;
    private static final int c1 = 0xF000F0;

    private s() {
    }

    static int a(float f2, float f3) {
        int n2 = u.w.length;
        float f4 = f2 - (float)Math.floor(f2 / (float)n2) * (float)n2;
        int n3 = (int)f4;
        float f5 = f4 - (float)n3;
        f5 = f5 * f5 * (3.0f - 2.0f * f5);
        int n4 = u.w[n3 % n2];
        int n5 = u.w[(n3 + 1) % n2];
        float f6 = s.a(n4 >> 16) + (s.a(n5 >> 16) - s.a(n4 >> 16)) * f5;
        float f7 = s.a(n4 >> 8) + (s.a(n5 >> 8) - s.a(n4 >> 8)) * f5;
        float f8 = s.a(n4) + (s.a(n5) - s.a(n4)) * f5;
        f6 += (1.0f - f6) * f3;
        f7 += (1.0f - f7) * f3;
        f8 += (1.0f - f8) * f3;
        return 0xFF000000 | s.a(f6) << 16 | s.a(f7) << 8 | s.a(f8);
    }

    static c a(float f2, float f3, float f4, float f7) {
        int n2 = Mth.clamp((int)((int)(f7 * 255.0f)), (int)0, (int)255) << 24;
        return (f5, f6) -> n2 | s.a(f2 + f5 * f3 + f6 * 0.35f, f4) & 0xFFFFFF;
    }

    private static float a(int n2) {
        float f2 = (float)(n2 & 0xFF) / 255.0f;
        return f2 * f2;
    }

    private static int a(float f2) {
        return Mth.clamp((int)((int)(Math.sqrt(Mth.clamp((float)f2, (float)0.0f, (float)1.0f)) * 255.0 + 0.5)), (int)0, (int)255);
    }

    private static float a(GuiGraphics guiGraphics) {
        Matrix4f matrix4f = guiGraphics.pose().last().pose();
        return (float)Math.sqrt(Math.abs(matrix4f.m00() * matrix4f.m11() - matrix4f.m01() * matrix4f.m10())) * (float)Minecraft.getInstance().getWindow().getGuiScale();
    }

    private static int b(float f2) {
        int n2 = 0;
        for (int i2 = 1; i2 < a.length; ++i2) {
            if (!(Math.abs((float)a[i2] / 10.0f - f2) < Math.abs((float)a[n2] / 10.0f - f2))) continue;
            n2 = i2;
        }
        return n2;
    }

    private static b a(a a2, int n2, String string, float f2) {
        Font font = Minecraft.getInstance().font;
        Style style = a2.e[n2];
        int[] nArray = string.codePoints().toArray();
        Component[] componentArray = new Component[nArray.length];
        float[] fArray = new float[nArray.length];
        float f3 = 0.0f;
        for (int i2 = 0; i2 < nArray.length; ++i2) {
            componentArray[i2] = Component.literal((String)Character.toString(nArray[i2])).withStyle(style);
            fArray[i2] = f3;
            f3 += font.getSplitter().stringWidth((FormattedText)componentArray[i2]) + (i2 < nArray.length - 1 ? f2 : 0.0f);
        }
        return new b(componentArray, fArray, f3);
    }

    static float a(GuiGraphics guiGraphics, a a2, String string, float f2, float f3) {
        return s.a((a)a2, (int)s.b((float)(s.a((GuiGraphics)guiGraphics) * f2)), (String)string, (float)f3).c * f2;
    }

    static float a(GuiGraphics guiGraphics, a a2, String string, float f2, float f3, float f4, float f5, float f6, c c2, float f7, float f8) {
        if (f7 <= 0.004f || string.isEmpty()) {
            return 0.0f;
        }
        b b2 = s.a(a2, s.b(s.a(guiGraphics) * f5), string, f6);
        float f9 = b2.c * f5;
        Matrix4f matrix4f = new Matrix4f((Matrix4fc)guiGraphics.pose().last().pose()).translate(f2 - f9 * f4, f3 - 7.0f * f5, 0.0f).scale(f5, f5, 1.0f);
        float f10 = 7.0f - a2.d;
        guiGraphics.flush();
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        if (f8 > 0.0f) {
            c c3 = cyou.rimuru.shootingstardemo.mc1211.client.fx.s.c.a(-16777216);
            s.a((MultiBufferSource)bufferSource, matrix4f, b2, c3, 0.9f / f5, f10, a2.d, f7 * Math.min(f8, 1.0f) * 0.28f);
            if (f8 > 1.0f) {
                s.a((MultiBufferSource)bufferSource, matrix4f, b2, c3, 2.4f / f5, f10, a2.d, f7 * (f8 - 1.0f) * 0.16f);
            }
        }
        s.a((MultiBufferSource)bufferSource, matrix4f, b2, c2, f10, a2.d, f7);
        bufferSource.endBatch();
        return f9;
    }

    private static void a(MultiBufferSource multiBufferSource, Matrix4f matrix4f, b b2, c c2, float f2, float f3, float f4, float f5) {
        for (int i2 = 0; i2 < 8; ++i2) {
            double d2 = (double)i2 * Math.PI / 4.0;
            s.a(multiBufferSource, new Matrix4f((Matrix4fc)matrix4f).translate((float)Math.cos(d2) * f2, (float)Math.sin(d2) * f2, 0.0f), b2, c2, f3, f4, f5);
        }
    }

    private static void a(MultiBufferSource multiBufferSource, Matrix4f matrix4f, b b2, c c2, float f2, float f3, float f4) {
        Font font = Minecraft.getInstance().font;
        MultiBufferSource multiBufferSource2 = renderType -> new d(multiBufferSource.getBuffer(renderType), c2, b2.c, f2, f3, f4);
        for (int i2 = 0; i2 < b2.a.length; ++i2) {
            font.drawInBatch(b2.a[i2].getVisualOrderText(), b2.b[i2], 0.0f, -1, false, matrix4f, multiBufferSource2, Font.DisplayMode.NORMAL, 0, 0xF000F0);
        }
    }

    private static BufferBuilder a(GuiGraphics guiGraphics, ResourceLocation resourceLocation) {
        guiGraphics.flush();
        Minecraft.getInstance().getTextureManager().getTexture(resourceLocation).setFilter(true, false);
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture((int)0, (ResourceLocation)resourceLocation);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        return Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
    }

    private static BufferBuilder b(GuiGraphics guiGraphics) {
        guiGraphics.flush();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        return Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
    }

    private static void a(BufferBuilder bufferBuilder, Matrix4f matrix4f, float f2, float f3, float f4, float f5, int n2) {
        bufferBuilder.addVertex(matrix4f, f2, f3, 0.0f).setUv(f4, f5).setColor(n2 >> 16 & 0xFF, n2 >> 8 & 0xFF, n2 & 0xFF, n2 >>> 24);
    }

    private static void a(BufferBuilder bufferBuilder, Matrix4f matrix4f, float f2, float f3, int n2) {
        bufferBuilder.addVertex(matrix4f, f2, f3, 0.0f).setColor(n2 >> 16 & 0xFF, n2 >> 8 & 0xFF, n2 & 0xFF, n2 >>> 24);
    }

    private static void a(BufferBuilder bufferBuilder) {
        MeshData meshData = bufferBuilder.build();
        if (meshData != null) {
            BufferUploader.drawWithShader((MeshData)meshData);
        }
        RenderSystem.enableDepthTest();
    }

    static void a(GuiGraphics guiGraphics, float f2, float f3, float f4, float f5, c c2, float f6) {
        int n2 = Math.max(1, Math.min(32, (int)(Math.abs(f3 - f2) / 12.0f)));
        BufferBuilder bufferBuilder = s.b(guiGraphics);
        Matrix4f matrix4f = guiGraphics.pose().last().pose();
        for (int i2 = 0; i2 < n2; ++i2) {
            float f7 = (float)i2 / (float)n2;
            float f8 = (float)(i2 + 1) / (float)n2;
            int n3 = s.a(c2.at(f7, 0.5f), f6);
            int n4 = s.a(c2.at(f8, 0.5f), f6);
            float f9 = f2 + (f3 - f2) * f7;
            float f10 = f2 + (f3 - f2) * f8;
            s.a(bufferBuilder, matrix4f, f9, f4, n3);
            s.a(bufferBuilder, matrix4f, f9, f4 + f5, n3);
            s.a(bufferBuilder, matrix4f, f10, f4 + f5, n4);
            s.a(bufferBuilder, matrix4f, f10, f4, n4);
        }
        s.a(bufferBuilder);
    }

    private static int a(int n2, float f2) {
        return Mth.clamp((int)((int)((float)(n2 >>> 24) * f2)), (int)0, (int)255) << 24 | n2 & 0xFFFFFF;
    }

    static void a(GuiGraphics guiGraphics, float f2, float f3, float f4, float f5, float f6, int n2) {
        for (int i2 = 0; i2 < 4; ++i2) {
            float f7 = (i2 & 1) == 0 ? -1.0f : 1.0f;
            float f8 = (i2 & 2) == 0 ? -1.0f : 1.0f;
            float f9 = f2 + f7 * f4;
            float f10 = f3 + f8 * f4;
            s.a(guiGraphics, f9 - (f7 > 0.0f ? f5 : 0.0f), f10 - (f8 > 0.0f ? f6 : 0.0f), f5, f6, n2);
            s.a(guiGraphics, f9 - (f7 > 0.0f ? f6 : 0.0f), f10 - (f8 > 0.0f ? f5 : 0.0f), f6, f5, n2);
        }
    }

    static void a(GuiGraphics guiGraphics, float f2, float f3, float f4, float f5, float f6) {
        int n2 = 24;
        int n3 = 6;
        BufferBuilder bufferBuilder = s.b(guiGraphics);
        Matrix4f matrix4f = guiGraphics.pose().last().pose();
        for (int i2 = 0; i2 < n2; ++i2) {
            float f7 = 2.0f * (float)i2 / (float)n2 - 1.0f;
            float f8 = 2.0f * (float)(i2 + 1) / (float)n2 - 1.0f;
            for (int i3 = 0; i3 < n3; ++i3) {
                float f9 = 2.0f * (float)i3 / (float)n3 - 1.0f;
                float f10 = 2.0f * (float)(i3 + 1) / (float)n3 - 1.0f;
                s.a(bufferBuilder, matrix4f, f2 + f7 * f4, f3 + f9 * f5, s.a(f7, f9, f6));
                s.a(bufferBuilder, matrix4f, f2 + f7 * f4, f3 + f10 * f5, s.a(f7, f10, f6));
                s.a(bufferBuilder, matrix4f, f2 + f8 * f4, f3 + f10 * f5, s.a(f8, f10, f6));
                s.a(bufferBuilder, matrix4f, f2 + f8 * f4, f3 + f9 * f5, s.a(f8, f9, f6));
            }
        }
        s.a(bufferBuilder);
    }

    private static int a(float f2, float f3, float f4) {
        float f5 = 1.0f - f2 * f2;
        float f6 = 1.0f - f3 * f3;
        return Mth.clamp((int)((int)(f4 * f5 * (2.0f - f5) * f6 * (2.0f - f6) * 255.0f)), (int)0, (int)255) << 24;
    }

    static void a(GuiGraphics guiGraphics, float f2, float f3, float f4, float f5, int n2) {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(f2, f3, 0.0f);
        poseStack.scale(f4 / 8.0f, f5 / 8.0f, 1.0f);
        guiGraphics.fill(0, 0, 8, 8, n2);
        poseStack.popPose();
    }

    static interface c {
        public int at(float var1, float var2);

        public static c a(int n2) {
            return (f2, f3) -> n2;
        }
    }

    static enum a {
        a("display", 16.0f),
        b("label", 9.5f),
        c("mono", 9.5f);

        private final Style[] e = new Style[s.a.length];
        final float d;

        a(String name, float size) {
            for (int i2 = 0; i2 < s.a.length; ++i2) {
                this.e[i2] = Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath("shooting_star_demo", "tess_" + name + "_" + s.a[i2]));
            }
            this.d = size * 0.7f;
        }
    }

    private static final class b
{
        private final Component[] a;
        private final float[] b;
        private final float c;

        private b(Component[] letters, float[] xs, float width) {
            this.a = letters;
            this.b = xs;
            this.c = width;
        }

        @Override
        public final String toString() {
            return cyou.rimuru.shootingstardemo.mc1211.RecordBridge.toString(this);
        }

        @Override
        public final int hashCode() {
            return (int)cyou.rimuru.shootingstardemo.mc1211.RecordBridge.hashCode(this);
        }

        @Override
        public final boolean equals(Object object) {
            return (boolean)cyou.rimuru.shootingstardemo.mc1211.RecordBridge.equals(this, object);
        }

        public Component[] a() {
            return this.a;
        }

        public float[] b() {
            return this.b;
        }

        public float c() {
            return this.c;
        }
    }

    private static final class d
    implements VertexConsumer {
        private final VertexConsumer a;
        private final c b;
        private final float c;
        private final float d;
        private final float e;
        private final float f;
        private float g;
        private float h;

        d(VertexConsumer vertexConsumer, c c2, float f2, float f3, float f4, float f5) {
            this.a = vertexConsumer;
            this.b = c2;
            this.c = f2;
            this.d = f3;
            this.e = f4;
            this.f = f5;
        }

        public VertexConsumer addVertex(Matrix4f matrix4f, float f2, float f3, float f4) {
            this.g = f2;
            this.h = f3;
            this.a.addVertex(matrix4f, f2, f3, f4);
            return this;
        }

        public VertexConsumer addVertex(float f2, float f3, float f4) {
            this.g = f2;
            this.h = f3;
            this.a.addVertex(f2, f3, f4);
            return this;
        }

        public VertexConsumer setColor(int n2, int n3, int n4, int n5) {
            float f2 = Mth.clamp((float)(this.g / Math.max(this.c, 0.001f)), (float)0.0f, (float)1.0f);
            float f3 = Mth.clamp((float)((this.h - this.d) / this.e), (float)0.0f, (float)1.0f);
            int n6 = this.b.at(f2, f3);
            int n7 = Mth.clamp((int)((int)((float)((n6 >>> 24) * n5) / 255.0f * this.f)), (int)0, (int)255);
            this.a.setColor(n6 >> 16 & 0xFF, n6 >> 8 & 0xFF, n6 & 0xFF, n7);
            return this;
        }

        public VertexConsumer setUv(float f2, float f3) {
            this.a.setUv(f2, f3);
            return this;
        }

        public VertexConsumer setUv1(int n2, int n3) {
            this.a.setUv1(n2, n3);
            return this;
        }

        public VertexConsumer setUv2(int n2, int n3) {
            this.a.setUv2(n2, n3);
            return this;
        }

        public VertexConsumer setNormal(float f2, float f3, float f4) {
            this.a.setNormal(f2, f3, f4);
            return this;
        }
    }

    static final class e {
        static final float a = 11.78f;
        static final float b = 0.2f;
        static final float c = 0.7f;
        static final float d = 0.6f;
        private static final int[] e = new int[]{16, 24, 40, 60, 90, 135, 200};
        private static final int f = 24;

        private e() {
        }

        static void a(GuiGraphics guiGraphics, float f2, float f3, float f4, float f5, c c2, float f6, float f7) {
            if (f6 <= 0.004f) {
                return;
            }
            float f8 = f4 * s.a(guiGraphics);
            int n2 = 0;
            for (int i2 = 1; i2 < e.length; ++i2) {
                if (!(Math.abs((float)e[i2] - f8) < Math.abs((float)e[n2] - f8))) continue;
                n2 = i2;
            }
            ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath((String)"shooting_star_demo", (String)("textures/gui/tess_title_" + e[n2] + ".png"));
            float f9 = f2 - 6.0899997f * f4;
            float f10 = f3 - 0.2f * f4;
            float f11 = 12.179999f * f4;
            float f12 = 1.4f * f4;
            float f13 = 0.64285713f;
            BufferBuilder bufferBuilder = s.a(guiGraphics, resourceLocation);
            Matrix4f matrix4f = guiGraphics.pose().last().pose();
            if (f7 > 0.0f) {
                c c3 = cyou.rimuru.shootingstardemo.mc1211.client.fx.s.c.a(-16777216);
                for (int i3 = 0; i3 < 8; ++i3) {
                    double d2 = (double)i3 * Math.PI / 4.0;
                    float f14 = (float)Math.cos(d2) * 1.1f;
                    float f15 = (float)Math.sin(d2) * 1.1f;
                    cyou.rimuru.shootingstardemo.mc1211.client.fx.s.e.a(bufferBuilder, matrix4f, f9 + f14, f10 + f15, f11, f12, f13, f5, c3, f6 * f7 * 0.2f);
                }
            }
            cyou.rimuru.shootingstardemo.mc1211.client.fx.s.e.a(bufferBuilder, matrix4f, f9, f10, f11, f12, f13, f5, c2, f6);
            s.a(bufferBuilder);
        }

        private static void a(BufferBuilder bufferBuilder, Matrix4f matrix4f, float f2, float f3, float f4, float f5, float f6, float f7, c c2, float f8) {
            cyou.rimuru.shootingstardemo.mc1211.client.fx.s.e.a(bufferBuilder, matrix4f, f2 + f7 * 0.5f, f3, f4, f5, 0.0f, f6, 0.2f, c2, f8);
            cyou.rimuru.shootingstardemo.mc1211.client.fx.s.e.a(bufferBuilder, matrix4f, f2 - f7 * 0.5f, f3, f4, f5, f6, 1.0f, 0.2f, c2, f8);
        }

        private static void a(BufferBuilder bufferBuilder, Matrix4f matrix4f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, c c2, float f9) {
            float f10 = f8 / (11.78f + 2.0f * f8);
            for (int i2 = 0; i2 < 24; ++i2) {
                float f11 = (float)i2 / 24.0f;
                float f12 = (float)(i2 + 1) / 24.0f;
                float f13 = (f11 - f10) / (1.0f - 2.0f * f10);
                float f14 = (f12 - f10) / (1.0f - 2.0f * f10);
                s.a(bufferBuilder, matrix4f, f2 + f4 * f11, f3 + f5 * f6, f11, f6, cyou.rimuru.shootingstardemo.mc1211.client.fx.s.e.a(c2, f13, f6, f9));
                s.a(bufferBuilder, matrix4f, f2 + f4 * f11, f3 + f5 * f7, f11, f7, cyou.rimuru.shootingstardemo.mc1211.client.fx.s.e.a(c2, f13, f7, f9));
                s.a(bufferBuilder, matrix4f, f2 + f4 * f12, f3 + f5 * f7, f12, f7, cyou.rimuru.shootingstardemo.mc1211.client.fx.s.e.a(c2, f14, f7, f9));
                s.a(bufferBuilder, matrix4f, f2 + f4 * f12, f3 + f5 * f6, f12, f6, cyou.rimuru.shootingstardemo.mc1211.client.fx.s.e.a(c2, f14, f6, f9));
            }
        }

        private static int a(c c2, float f2, float f3, float f4) {
            int n2 = c2.at(Mth.clamp((float)f2, (float)0.0f, (float)1.0f), Mth.clamp((float)f3, (float)0.0f, (float)1.0f));
            return Mth.clamp((int)((int)((float)(n2 >>> 24) * f4)), (int)0, (int)255) << 24 | n2 & 0xFFFFFF;
        }

        static void a(GuiGraphics guiGraphics, float f4, float f5, float f6, c c2, float f7, float f8) {
            float f9 = f5 + f6 * 0.5f;
            if (f7 > 0.004f) {
                s.a(guiGraphics, f4, f9, 7.29f * f6, f6 * 1.5f, f7);
            }
            if (f8 <= 0.004f) {
                return;
            }
            BufferBuilder bufferBuilder = s.a(guiGraphics, ResourceLocation.fromNamespaceAndPath((String)"shooting_star_demo", (String)"textures/gui/tess_title_glow.png"));
            float f10 = f4 - 6.49f * f6;
            float f11 = 12.98f * f6;
            float f12 = f5 - 0.6f * f6;
            float f13 = 2.2f * f6;
            cyou.rimuru.shootingstardemo.mc1211.client.fx.s.e.a(bufferBuilder, guiGraphics.pose().last().pose(), f10, f12, f11, f13, 0.0f, 1.0f, 0.6f, (f2, f3) -> c2.at(f2, 0.5f), f8);
            s.a(bufferBuilder);
        }
    }
}

