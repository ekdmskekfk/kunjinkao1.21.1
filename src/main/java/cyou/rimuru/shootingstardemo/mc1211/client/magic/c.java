/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.magic;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import cyou.rimuru.shootingstardemo.mc1211.client.magic.j;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix3x2f;
import org.joml.Matrix4f;

public final class c {
    public static final int a = 0xFFFFFF;
    public static final int b = 0xFBFBFC;
    public static final int c = 15659251;
    public static final int d = 13225686;
    public static final int e = 3883080;
    public static final int f = 9080985;
    public static final int g = 11909826;
    public static final int h = 16037439;
    public static final int i = 16774364;
    public static final int j = 1712688;
    public static final int k = 3055195;
    public static final int l = 14239816;
    public static final int m = 0xF2F8FF;
    public static final int n = 0;
    public static final int o = 1;
    public static final int p = 2;
    public static final int q = 3;
    public static final int r = 0;
    public static final int s = 1;
    public static final int t = 2;
    public static final int u = 3;
    public static final int v = 4;
    public static final int w = 1;
    public static final int x = 2;
    public static final int y = 64;
    public static final int z = 4;
    public static ShaderInstance A;
    public static final ResourceLocation B;
    private static final int[] F;
    private static final ResourceLocation[] G;
    private static final ResourceLocation[] H;
    private static final ResourceLocation[] I;
    private static int J;
    private static final ResourceLocation K;
    private static final int L = 0;
    private static final int M = 1;
    private static final int N = 2;
    private static final int O = 3;
    private static final int P = 4;
    private static final int Q = 5;
    private static final int R = 6;
    private static final int S = 7;
    public static final int C = 1;
    public static final int D = 2;
    public static final int E = 4;

    private c() {
    }

    public static void a() {
    }

    static ResourceLocation a(String string) {
        return ResourceLocation.fromNamespaceAndPath((String)"shooting_star_demo", (String)string);
    }

    private static ResourceLocation[] f(String string) {
        ResourceLocation[] resourceLocationArray = new ResourceLocation[F.length];
        for (int i2 = 0; i2 < F.length; ++i2) {
            resourceLocationArray[i2] = cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a("white_space_" + string + "_" + F[i2]);
        }
        return resourceLocationArray;
    }

    public static void a(float f2) {
        J = cyou.rimuru.shootingstardemo.mc1211.client.magic.c.b(f2);
    }

    private static int b(float f2) {
        int n2 = 0;
        for (int i2 = 1; i2 < F.length; ++i2) {
            if (!(Math.abs((float)F[i2] / 10.0f - f2) < Math.abs((float)F[n2] / 10.0f - f2))) continue;
            n2 = i2;
        }
        return n2;
    }

    public static MutableComponent a(Component component, float f2) {
        int n2 = cyou.rimuru.shootingstardemo.mc1211.client.magic.c.b(f2);
        return component.copy().withStyle(style -> style.withFont(H[n2]));
    }

    public static MutableComponent a(Component component) {
        return component.copy().withStyle(style -> style.withFont(G[J]));
    }

    public static MutableComponent b(String string) {
        return cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a((Component)Component.literal((String)string));
    }

    public static MutableComponent b(Component component) {
        return component.copy().withStyle(style -> style.withFont(H[J]));
    }

    public static MutableComponent c(String string) {
        return cyou.rimuru.shootingstardemo.mc1211.client.magic.c.b((Component)Component.literal((String)string));
    }

    public static MutableComponent d(String string) {
        return Component.literal((String)string).withStyle(style -> style.withFont(I[J]));
    }

    public static String e(String string) {
        String string2 = string.toUpperCase(Locale.ROOT);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i2 = 0; i2 < string2.length(); ++i2) {
            char c2 = string2.charAt(i2);
            stringBuilder.append(c2 == ' ' ? "  " : String.valueOf(c2));
            if (i2 >= string2.length() - 1) continue;
            stringBuilder.append(' ');
        }
        return stringBuilder.toString();
    }

    public static int a(float f2, int n2) {
        return Mth.clamp((int)Math.round(f2 * 255.0f), (int)0, (int)255) << 24 | n2 & 0xFFFFFF;
    }

    public static int a(int n2, int n3, float f2) {
        f2 = Mth.clamp((float)f2, (float)0.0f, (float)1.0f);
        int n4 = Math.round(Mth.lerp((float)f2, (float)(n2 >> 16 & 0xFF), (float)(n3 >> 16 & 0xFF)));
        int n5 = Math.round(Mth.lerp((float)f2, (float)(n2 >> 8 & 0xFF), (float)(n3 >> 8 & 0xFF)));
        int n6 = Math.round(Mth.lerp((float)f2, (float)(n2 & 0xFF), (float)(n3 & 0xFF)));
        return n4 << 16 | n5 << 8 | n6;
    }

    public static Matrix4f a(Matrix3x2f matrix3x2f) {
        return new Matrix4f(matrix3x2f.m00, matrix3x2f.m01, 0.0f, 0.0f, matrix3x2f.m10, matrix3x2f.m11, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, matrix3x2f.m20, matrix3x2f.m21, 0.0f, 1.0f);
    }

    public static void a(GuiGraphics guiGraphics) {
        guiGraphics.flush();
        RenderSystem.clear((int)256, (boolean)Minecraft.ON_OSX);
    }

    public static void a(GuiGraphics guiGraphics, int n2, int n3, int n4, int n5, int n6, float f2, int n7, boolean bl) {
        int n8 = cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(n6);
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, 0, n7 & 3 | (bl ? 4 : 0), n2, n3, n4, n5, n8 >> 8, n8, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.c(f2));
    }

    public static void b(GuiGraphics guiGraphics, int n2, int n3, int n4, int n5, int n6, float f2, int n7, boolean bl) {
        int n8 = bl ? 16 : 8;
        int n9 = cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(n6);
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, 1, n7 & 3 | (bl ? 4 : 0), n2 - n8, n3 - n8, n4 + 2 * n8, n5 + 2 * n8, n9 >> 8, n9, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.c(f2));
    }

    public static void a(GuiGraphics guiGraphics, int n2, int n3, int n4, int n5, float f2, int n6) {
        int n7 = cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(n5);
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, 2, n6, n2, n3, n4, n4, n7 >> 8, n7, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.c(f2));
    }

    public static void a(GuiGraphics guiGraphics, int n2, int n3, int n4, int n5, float f2, float f3) {
        if (f3 <= 0.0f) {
            return;
        }
        int n6 = cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(n5);
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, 3, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.d(f3), n2, n3, n4, n4, n6 >> 8, n6, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.c(f2));
    }

    public static void a(GuiGraphics guiGraphics, int n2, int n3, float f2, float f3) {
        int n4 = Math.round(Mth.clamp((float)f2, (float)0.0f, (float)1.0f) * 65535.0f);
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, 4, 0, 0, 0, n2, n3, n4 >> 8, n4, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.c(f3));
    }

    public static void a(GuiGraphics guiGraphics, int n2, float f2, int n3) {
        int n4 = cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(n2);
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, 5, n3, 0, 0, 64, 64, n4 >> 8, n4, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.c(f2));
    }

    public static void a(GuiGraphics guiGraphics, float f2, float f3, float f4, float f5, float f6, boolean bl) {
        int n2 = Mth.clamp((int)Math.round(f2 * 2047.0f), (int)0, (int)2047);
        int n3 = Mth.clamp((int)Math.round(f3 * 2047.0f), (int)0, (int)2047);
        float f7 = Mth.positiveModulo((float)(f4 / ((float)Math.PI * 2) + 0.5f), (float)1.0f);
        int n4 = Mth.clamp((int)Math.round(f7 * 255.0f), (int)0, (int)255);
        int n5 = Mth.clamp((int)Math.round((f5 - 0.8f) / 0.5f * 255.0f), (int)0, (int)255);
        int n6 = cyou.rimuru.shootingstardemo.mc1211.client.magic.c.c(f6);
        if (n6 <= 0) {
            return;
        }
        int n7 = (0x80 | (4 | (bl ? 1 : 0)) << 4 | 5) << 24 | n4 << 16 | n5 << 8 | n6;
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, cyou.rimuru.shootingstardemo.mc1211.client.magic.j.a, 0, 0, 64, 64, (float)n2 + 0.25f, (float)n3 + 0.25f, n7);
    }

    public static void a(GuiGraphics guiGraphics, int n2, int n3, int n4, float f2, float f3) {
        if (f3 <= 0.0f) {
            return;
        }
        int n5 = cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(n4);
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, 6, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.d(f3), n2 - 12, n3 - 12, 24, 24, n5 >> 8, n5, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.c(f2));
    }

    public static void b(GuiGraphics guiGraphics, int n2, int n3, int n4, int n5, float f2, float f3) {
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, n2, n3, n4, n5, f2, f3, 0);
    }

    public static void a(GuiGraphics guiGraphics, int n2, int n3, int n4, int n5, float f2, float f3, int n6) {
        if (f3 <= 0.0f || f2 <= 0.0f) {
            return;
        }
        int n7 = cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(0xFFFFFF, n5, f3));
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, 7, n6, n2, n3 - 3, n4, 6, n7 >> 8, n7, cyou.rimuru.shootingstardemo.mc1211.client.magic.c.c(f2));
    }

    private static int a(int n2) {
        return (n2 >> 19 & 0x1F) << 11 | (n2 >> 10 & 0x3F) << 5 | n2 >> 3 & 0x1F;
    }

    private static int c(float f2) {
        return Mth.clamp((int)Math.round(f2 * 255.0f), (int)0, (int)255);
    }

    private static int d(float f2) {
        return Mth.clamp((int)(Math.round(f2 * 8.0f) - 1), (int)0, (int)7);
    }

    private static void a(GuiGraphics guiGraphics, int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9, int n10) {
        if (n6 <= 0 || n7 <= 0 || n2 != 4 && n2 != 3 && n2 != 6 && n2 != 7 && n10 <= 0) {
            return;
        }
        int n11 = (0x80 | (n3 & 7) << 4 | n2) << 24 | (n8 & 0xFF) << 16 | (n9 & 0xFF) << 8 | n10 & 0xFF;
        cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a(guiGraphics, K, n4, n5, n6, n7, (float)n6 + 0.25f, (float)n7 + 0.25f, n11);
    }

    private static void a(GuiGraphics guiGraphics, ResourceLocation resourceLocation, int n2, int n3, int n4, int n5, float f2, float f3, int n6) {
        if (A == null) {
            return;
        }
        guiGraphics.flush();
        Matrix4f matrix4f = guiGraphics.pose().last().pose();
        RenderSystem.setShader(() -> A);
        RenderSystem.setShaderTexture((int)0, (ResourceLocation)resourceLocation);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        int n7 = n6 >>> 24;
        int n8 = n6 >> 16 & 0xFF;
        int n9 = n6 >> 8 & 0xFF;
        int n10 = n6 & 0xFF;
        BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        bufferBuilder.addVertex(matrix4f, (float)n2, (float)n3, 0.0f).setUv(f2, f3).setColor(n8, n9, n10, n7);
        bufferBuilder.addVertex(matrix4f, (float)n2, (float)(n3 + n5), 0.0f).setUv(f2, f3 + 0.5f).setColor(n8, n9, n10, n7);
        bufferBuilder.addVertex(matrix4f, (float)(n2 + n4), (float)(n3 + n5), 0.0f).setUv(f2 + 0.5f, f3 + 0.5f).setColor(n8, n9, n10, n7);
        bufferBuilder.addVertex(matrix4f, (float)(n2 + n4), (float)n3, 0.0f).setUv(f2 + 0.5f, f3).setColor(n8, n9, n10, n7);
        BufferUploader.drawWithShader((MeshData)bufferBuilder.buildOrThrow());
        RenderSystem.enableDepthTest();
    }

    static {
        B = cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a("white_space");
        F = new int[]{10, 15, 20, 25, 30, 40, 50};
        G = cyou.rimuru.shootingstardemo.mc1211.client.magic.c.f("text");
        H = cyou.rimuru.shootingstardemo.mc1211.client.magic.c.f("caption");
        I = cyou.rimuru.shootingstardemo.mc1211.client.magic.c.f("title");
        J = 2;
        K = cyou.rimuru.shootingstardemo.mc1211.client.magic.c.a("textures/gui/white_space_noise.png");
    }
}

