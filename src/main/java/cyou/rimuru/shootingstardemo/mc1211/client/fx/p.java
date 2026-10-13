/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.fx;

import com.mojang.blaze3d.vertex.PoseStack;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.d;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.e;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.i;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.o;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.q;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

final class p {
    private static final int a = 16724808;
    private static final int b = 16766684;
    private static final int c = 8381183;

    private p() {
    }

    static void a(GuiGraphics guiGraphics, d d2, o o2, float f2, float f3) {
        Minecraft minecraft = d2.a();
        Font font = minecraft.font;
        int n2 = i.c(guiGraphics);
        int n3 = i.d(guiGraphics);
        float f4 = Mth.clamp((float)(f3 * 1.6f - 0.3f), (float)0.0f, (float)1.0f);
        if (f4 <= 0.01f) {
            return;
        }
        int n4 = (int)(f4 * 255.0f);
        boolean bl = (int)(f2 * 3.0f) % 11 == 0;
        int n5 = n4 << 24 | 0xFF3348;
        int n6 = (int)((float)n4 * 0.55f) << 24 | 0xFFD6DC;
        int n7 = 12;
        int n8 = 22;
        p.a(guiGraphics, n7, n7, n8, 1, 1, n5);
        p.a(guiGraphics, n2 - n7, n7, n8, -1, 1, n5);
        p.a(guiGraphics, n7, n3 - n7, n8, 1, -1, n5);
        p.a(guiGraphics, n2 - n7, n3 - n7, n8, -1, -1, n5);
        p.a(guiGraphics, font, "UPLINK // SS-01 > THE SHOOTING STAR", n7 + 6, n7 + 6, bl ? n6 : n5, 1.0f);
        p.a(guiGraphics, font, String.format(Locale.ROOT, "T+%05.2f", Float.valueOf(f2 / 20.0f / o2.j())), n7 + 6, n7 + 17, n6, 1.0f);
        float f5 = Math.max(0.0f, (352.0f - f2) / 20.0f / o2.j());
        String string = f2 < 352.0f ? String.format(Locale.ROOT, "IMPACT T-%05.2f", Float.valueOf(f5)) : "IMPACT CONFIRMED";
        p.b(guiGraphics, font, string, n2 - n7 - 6, n7 + 6, n5, 1.0f);
        Vec3 vec3 = o2.c();
        p.b(guiGraphics, font, String.format(Locale.ROOT, "LOCK %d / %d / %d", Mth.floor((double)vec3.x), Mth.floor((double)vec3.y), Mth.floor((double)vec3.z)), n2 - n7 - 6, n3 - n7 - 14, n6, 1.0f);
        if (f2 < 62.0f) {
            float[] fArray;
            if (f2 >= 9.0f && (fArray = e.a(vec3.add(0.0, 0.5, 0.0), guiGraphics)) != null) {
                float f6 = Mth.clamp((float)((f2 - 9.0f) / 8.0f), (float)0.0f, (float)1.0f);
                int n9 = (int)Mth.lerp((float)f6, (float)60.0f, (float)9.0f);
                p.a(guiGraphics, (int)fArray[0], (int)fArray[1], n9, n5);
                p.a(guiGraphics, font, "TARGET ACQUIRED", (int)fArray[0] + n9 + 4, (int)fArray[1] - 9, n5, 1.0f);
                p.a(guiGraphics, font, "STRIKE RADIUS " + Math.round(o2.j) + " \u00b7 FULL DEPTH", (int)fArray[0] + n9 + 4, (int)fArray[1] + 1, n6, 1.0f);
            }
            return;
        }
        if (f2 < 238.0f || f2 >= 318.0f && f2 < 342.0f) {
            p.a(guiGraphics, font, f2, n2, n3, n4, n5, n6);
        }
        if (q.e(f2)) {
            p.b(guiGraphics, font, f2, n2, n3, n4, n5, n6);
        }
        if (f2 >= 342.0f && f2 < 352.0f) {
            float[] fArray = e.a(o2.c().add(0.0, 0.5, 0.0), guiGraphics);
            if (fArray != null) {
                p.a(guiGraphics, (int)fArray[0], (int)fArray[1], 9, n5);
            }
            boolean bl2 = (int)(f2 / 2.0f) % 2 == 0;
            p.d(guiGraphics, font, "INBOUND", n2 / 2, 30, bl2 ? n5 : n6, 1.5f);
        }
        if (f2 >= 356.0f && f2 < 412.0f) {
            int n10 = (int)((float)n4 * Mth.clamp((float)((412.0f - f2) / 12.0f), (float)0.0f, (float)1.0f));
            p.d(guiGraphics, font, "IMPACT CONFIRMED", n2 / 2, n3 / 2 + 50, n10 << 24 | 0xFF3348, 2.0f);
        }
        if (f2 >= 494.0f) {
            int n11 = (int)((float)n4 * Mth.clamp((float)((f2 - 486.0f - 8.0f) / 10.0f), (float)0.0f, (float)1.0f));
            p.c(guiGraphics, font, "SHAFT \u00d8400 \u00b7 WORLD TOP TO VOID", n2 / 2, n3 - n7 - 30, n11 << 24 | 0xFFD6DC, 1.25f);
        }
    }

    private static void a(GuiGraphics guiGraphics, Font font, float f2, int n2, int n3, int n4, int n5, int n6) {
        q.a a2 = q.a(f2);
        if (q.f(f2) > 0.3f) {
            return;
        }
        for (q.b b2 : q.i(f2)) {
            double[] dArray = a2.a(b2.c());
            if (dArray == null || Math.abs(dArray[0]) > 1.9 || Math.abs(dArray[1]) > 1.05) continue;
            int n7 = (int)((double)n2 / 2.0 + dArray[0] * (double)n3 / 2.0);
            int n8 = (int)((double)n3 / 2.0 - dArray[1] * (double)n3 / 2.0);
            double d2 = b2.d() / Math.max(dArray[2], 1.0E-30);
            int n9 = (int)Mth.clamp((double)(d2 / Math.tan(Math.toRadians(a2.g()) * 0.5) * (double)n3 / 2.0 + 6.0), (double)7.0, (double)160.0);
            boolean bl = b2.a().equals("TARGET") || b2.a().equals("SOL") && f2 > 170.0f;
            p.a(guiGraphics, n7, n8, n9, bl ? n5 : n4 << 24 | 0x7FE2FF);
            p.a(guiGraphics, font, b2.a(), n7 + n9 + 4, n8 - 9, bl ? n5 : n4 << 24 | 0x7FE2FF, 1.0f);
            p.a(guiGraphics, font, b2.b(), n7 + n9 + 4, n8 + 1, n6, 1.0f);
        }
        double d3 = q.j(f2);
        int n10 = (int)Math.floor(Math.log10(Math.max(d3, 1.0)));
        p.a(guiGraphics, font, String.format(Locale.ROOT, "SCALE 10^%d KM", n10), 18, n3 - 40, n5, 1.5f);
        String string = d3 > 9.4607E12 ? String.format(Locale.ROOT, "RANGE %,.0f LY", d3 / 9.4607E12) : String.format(Locale.ROOT, "RANGE %,.0f KM", d3);
        p.a(guiGraphics, font, string, 18, n3 - 24, n6, 1.0f);
        float f3 = (float)(Math.log10(Math.max(d3, 1.0)) - (double)n10);
        guiGraphics.fill(18, n3 - 46, 138, n3 - 45, n6);
        guiGraphics.fill(18, n3 - 48, 18 + (int)(120.0f * f3), n3 - 45, n5);
        if (f2 >= 314.0f) {
            p.d(guiGraphics, font, "RIDING THE BEAM", n2 / 2, 30, n5, 1.5f);
        }
    }

    private static void b(GuiGraphics guiGraphics, Font font, float f2, int n2, int n3, int n4, int n5, int n6) {
        int n7;
        float f3 = p.a(f2, 250.0f, 253.0f, 276.0f, 284.0f);
        if (f3 > 0.01f) {
            int n8 = (int)((float)n4 * f3);
            n7 = f2 < 256.0f ? (int)(Math.sin((double)f2 * 91.0) * 6.0) : 0;
            p.c(guiGraphics, font, "THE SHOOTING STAR", n2 / 2 + n7, n3 / 2 - 70, n8 << 24 | 0xFF3348, 4.0f);
            p.c(guiGraphics, font, "SS-01 GALACTIC RAILGUN \u00b7 BEYOND THE RIM \u00b7 58,000 LY FROM HOME", n2 / 2, n3 / 2 - 28, n8 << 24 | 0xFFD6DC, 1.25f);
        }
        float f4 = q.k(f2);
        if (f2 >= 290.0f && f2 < 314.0f) {
            n7 = Math.min(240, n2 / 3);
            int n9 = n2 / 2 - n7 / 2;
            int n10 = n3 - 50;
            guiGraphics.fill(n9, n10, n9 + n7, n10 + 1, n6);
            guiGraphics.fill(n9, n10 + 1, n9 + (int)((float)n7 * f4), n10 + 3, n5);
            p.a(guiGraphics, font, "CHARGE", n9, n10 - 11, n6, 1.0f);
            p.b(guiGraphics, font, String.format(Locale.ROOT, "%3d%%", Math.round(f4 * 100.0f)), n9 + n7, n10 - 11, n5, 1.0f);
            boolean bl = (int)(f2 / 3.0f) % 2 == 0;
            p.c(guiGraphics, font, "TARGET SOL-3 \u00b7 LOCKED", n2 / 2, n3 - 72, bl ? n5 : n6, 1.0f);
        }
    }

    private static float a(float f2, float f3, float f4, float f5, float f6) {
        return Mth.clamp((float)((f2 - f3) / (f4 - f3)), (float)0.0f, (float)1.0f) * (1.0f - Mth.clamp((float)((f2 - f5) / (f6 - f5)), (float)0.0f, (float)1.0f));
    }

    static void a(GuiGraphics guiGraphics, int n2, int n3, int n4, int n5, int n6, int n7) {
        guiGraphics.fill(Math.min(n2, n2 + n5 * n4), n3, Math.max(n2, n2 + n5 * n4), n3 + n6, n7);
        guiGraphics.fill(n2, Math.min(n3, n3 + n6 * n4), n2 + n5, Math.max(n3, n3 + n6 * n4), n7);
    }

    static void a(GuiGraphics guiGraphics, int n2, int n3, int n4, int n5) {
        int n6 = Math.max(3, n4 / 3);
        p.a(guiGraphics, n2 - n4, n3 - n4, n6, 1, 1, n5);
        p.a(guiGraphics, n2 + n4, n3 - n4, n6, -1, 1, n5);
        p.a(guiGraphics, n2 - n4, n3 + n4, n6, 1, -1, n5);
        p.a(guiGraphics, n2 + n4, n3 + n4, n6, -1, -1, n5);
    }

    static void a(GuiGraphics guiGraphics, Font font, String string, int n2, int n3, int n4, float f2) {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate((float)n2, (float)n3, 0.0f);
        poseStack.scale(f2, f2, 1.0f);
        guiGraphics.drawString(font, string, 0, 0, n4, false);
        poseStack.popPose();
    }

    static void b(GuiGraphics guiGraphics, Font font, String string, int n2, int n3, int n4, float f2) {
        p.a(guiGraphics, font, string, n2 - (int)((float)font.width(string) * f2), n3, n4, f2);
    }

    static void c(GuiGraphics guiGraphics, Font font, String string, int n2, int n3, int n4, float f2) {
        p.a(guiGraphics, font, string, n2 - (int)((float)font.width(string) * f2 / 2.0f), n3, n4, f2);
    }

    static void d(GuiGraphics guiGraphics, Font font, String string, int n2, int n3, int n4, float f2) {
        p.c(guiGraphics, font, "[ " + string + " ]", n2, n3, n4, f2);
    }
}

