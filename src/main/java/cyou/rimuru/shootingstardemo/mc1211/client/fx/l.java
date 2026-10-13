/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.fx;

import cyou.rimuru.shootingstardemo.mc1211.client.fx.d;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.e;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.i;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.k;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.m;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.p;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.q;
import cyou.rimuru.shootingstardemo.mc1211.star.c;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

final class l {
    private static final int a = 12101887;
    private static final int b = 15262463;
    private static final int c = 8381183;
    private static final int d = 0xFFFFFF;

    private l() {
    }

    static void a(GuiGraphics guiGraphics, d d2, k k2, float f2, float f3) {
        Minecraft minecraft = d2.a();
        Font font = minecraft.font;
        int n2 = i.c(guiGraphics);
        int n3 = i.d(guiGraphics);
        float f4 = Mth.clamp((float)(f3 * 1.6f - 0.3f), (float)0.0f, (float)1.0f);
        if (f4 <= 0.01f) {
            return;
        }
        int n4 = (int)(f4 * 255.0f);
        boolean bl = (int)(f2 * 3.0f) % 13 == 0;
        int n5 = n4 << 24 | 0xB8A8FF;
        int n6 = (int)((float)n4 * 0.55f) << 24 | 0xE8E2FF;
        int n7 = n4 << 24 | 0xFFFFFF;
        int n8 = 12;
        p.a(guiGraphics, n8, n8, 22, 1, 1, n5);
        p.a(guiGraphics, n2 - n8, n8, 22, -1, 1, n5);
        p.a(guiGraphics, n8, n3 - n8, 22, 1, -1, n5);
        p.a(guiGraphics, n2 - n8, n3 - n8, 22, -1, -1, n5);
        p.a(guiGraphics, font, "UPLINK // SS-04 > SEVEN STARS", n8 + 6, n8 + 6, bl ? n6 : n5, 1.0f);
        p.a(guiGraphics, font, String.format(Locale.ROOT, "T+%05.2f", Float.valueOf(f2 / 20.0f)), n8 + 6, n8 + 17, n6, 1.0f);
        int n9 = 0;
        for (int i2 = 0; i2 < 7; ++i2) {
            n9 += f2 >= (float)cyou.rimuru.shootingstardemo.mc1211.star.i.c(i2) ? 1 : 0;
        }
        String string = f2 < 290.0f ? String.format(Locale.ROOT, "FIRST IMPACT T-%05.2f", Float.valueOf((290.0f - f2) / 20.0f / k2.j())) : (n9 < 7 ? String.format(Locale.ROOT, "IMPACTS %d / 7", n9) : (f2 < 440.0f ? "LINKING" : "URSA MAJOR BURNED"));
        p.b(guiGraphics, font, string, n2 - n8 - 6, n8 + 6, n5, 1.0f);
        Vec3 vec3 = k2.f();
        p.b(guiGraphics, font, String.format(Locale.ROOT, "FIGURE %d / %d / %d", Mth.floor((double)vec3.x), Mth.floor((double)vec3.y), Mth.floor((double)vec3.z)), n2 - n8 - 6, n3 - n8 - 14, n6, 1.0f);
        if (f2 < 62.0f || f2 >= 272.0f) {
            l.a(guiGraphics, font, k2, f2, n2, n3, n4, n5, n6, n7);
            return;
        }
        l.b(guiGraphics, font, k2, f2, n2, n3, n4, n5, n6, n7);
    }

    private static void a(GuiGraphics guiGraphics, Font font, k k2, float f2, int n2, int n3, int n4, int n5, int n6, int n7) {
        int n8;
        if (f2 >= 9.0f && f2 < 290.0f) {
            int n9;
            for (n9 = 0; n9 < 7; ++n9) {
                float f3 = k.b(f2, n9);
                Vec3 vec3 = k2.a(n9).add(0.0, cyou.rimuru.shootingstardemo.mc1211.star.c.b(k2.b()), 0.0);
                float[] fArray = e.a(f3 > 0.01f ? vec3 : k2.a(n9).add(0.0, 1.0, 0.0), guiGraphics);
                if (fArray == null) continue;
                int n10 = (int)Mth.lerp((float)f3, (float)24.0f, (float)8.0f);
                int n11 = f3 > 0.99f ? n5 : n6;
                p.a(guiGraphics, (int)fArray[0], (int)fArray[1], n10, n11);
                if (f3 > 0.01f) {
                    float f4 = 24 + n9 * 4;
                    float f5 = n9 < 6 ? 4.0f : 14.0f;
                    int n12 = (int)((float)n4 * l.a(f2, f4, f4 + 1.0f, f4 + f5, f4 + f5 + 2.0f));
                    if (n12 <= 4) continue;
                    p.a(guiGraphics, font, String.format(Locale.ROOT, "%s %s \u00b7 %.0f LY", cyou.rimuru.shootingstardemo.mc1211.star.c.b[n9], cyou.rimuru.shootingstardemo.mc1211.star.c.c[n9], cyou.rimuru.shootingstardemo.mc1211.star.c.e[n9]), (int)fArray[0] + n10 + 4, (int)fArray[1] - 4, n12 << 24 | 0xB8A8FF, 1.0f);
                    continue;
                }
                p.a(guiGraphics, font, "NODE " + (n9 + 1), (int)fArray[0] + n10 + 4, (int)fArray[1] - 4, n11, 1.0f);
            }
            if (f2 >= 24.0f) {
                n9 = 0;
                for (int i2 = 0; i2 < 7; ++i2) {
                    n9 += k.b(f2, i2) > 0.99f ? 1 : 0;
                }
                p.d(guiGraphics, font, n9 < 7 ? String.format(Locale.ROOT, "STELLAR LOCK %d / 7", n9) : "URSA MAJOR \u00b7 ARRAY LINKED", n2 / 2, 30, n5, 1.5f);
            }
            return;
        }
        for (n8 = 0; n8 < 7; ++n8) {
            float f6 = f2 - (float)cyou.rimuru.shootingstardemo.mc1211.star.i.c(n8);
            if (!(f6 >= 0.0f) || !(f6 < 14.0f)) continue;
            int n13 = (int)((float)n4 * Mth.clamp((float)(1.0f - (f6 - 10.0f) / 4.0f), (float)0.0f, (float)1.0f));
            int n14 = f6 < 5.0f ? (int)(Math.sin((double)f2 * 91.0) * 5.0) : 0;
            p.c(guiGraphics, font, "IMPACT " + (n8 + 1) + " \u00b7 " + cyou.rimuru.shootingstardemo.mc1211.star.c.b[n8], n2 / 2 + n14, n3 / 2 - 80, n13 << 24 | 0xB8A8FF, 3.0f);
            p.c(guiGraphics, font, String.format(Locale.ROOT, "%s URSAE MAJORIS \u00b7 %.0f LY \u00b7 CRATER \u00d8%d", cyou.rimuru.shootingstardemo.mc1211.star.c.c[n8], cyou.rimuru.shootingstardemo.mc1211.star.c.e[n8], (int)(cyou.rimuru.shootingstardemo.mc1211.star.c.a(n8, k2.b()) * 2.0)), n2 / 2, n3 / 2 - 46, n13 << 24 | 0xE8E2FF, 1.25f);
        }
        if (f2 >= 290.0f && f2 < 398.0f) {
            for (n8 = 0; n8 < 7; ++n8) {
                boolean bl = f2 >= (float)cyou.rimuru.shootingstardemo.mc1211.star.i.c(n8);
                guiGraphics.fill(n2 / 2 - 70 + n8 * 20, n3 - 40, n2 / 2 - 58 + n8 * 20, n3 - 36, bl ? n5 : n6);
            }
        }
        if (f2 >= 398.0f && f2 < 440.0f) {
            n8 = Mth.clamp((int)((int)((f2 - 398.0f) / 5.0f) + 1), (int)0, (int)7);
            p.d(guiGraphics, font, String.format(Locale.ROOT, "LINK %d / 7", n8), n2 / 2, 30, n7, 2.0f);
        }
        if (f2 >= 470.0f) {
            n8 = (int)((float)n4 * Mth.clamp((float)((f2 - 440.0f - 30.0f) / 10.0f), (float)0.0f, (float)1.0f));
            p.d(guiGraphics, font, "CONSTELLATION BURNED", n2 / 2, n3 - 64, n8 << 24 | 0xB8A8FF, 2.0f);
            p.c(guiGraphics, font, String.format(Locale.ROOT, "URSA MAJOR \u00b7 7 STARS \u00b7 7 LINES \u00b7 %.0f BLOCKS", cyou.rimuru.shootingstardemo.mc1211.star.c.a(k2.b())), n2 / 2, n3 - 40, n8 << 24 | 0xE8E2FF, 1.25f);
        }
    }

    private static int[] a(m.a a2, q.c c2, int n2, int n3) {
        double d2 = c2.c(a2.c());
        if (d2 <= 1.0E-18) {
            return null;
        }
        double d3 = Math.tan(Math.toRadians(a2.f()) * 0.5);
        double d4 = c2.c(a2.e()) / (d2 * d3);
        double d5 = c2.c(a2.d()) / (d2 * d3);
        if (Math.abs(d4) > 1.9 || Math.abs(d5) > 1.05) {
            return null;
        }
        return new int[]{(int)((double)n2 / 2.0 + d4 * (double)n3 / 2.0), (int)((double)n3 / 2.0 - d5 * (double)n3 / 2.0)};
    }

    private static void b(GuiGraphics guiGraphics, Font font, k k2, float f2, int n2, int n3, int n4, int n5, int n6, int n7) {
        int n8;
        int n9;
        int[] nArray;
        m.a a2 = m.a(f2, k2.g());
        float f3 = l.a(f2, 132.0f, 135.0f, 148.0f, 152.0f);
        float f4 = 1.0f - f3;
        for (m.b b2 : f4 > 0.02f ? m.b(f2, k2.g()) : List.<m.b>of()) {
            nArray = l.a(a2, b2.c(), n2, n3);
            if (nArray == null) continue;
            n9 = l.a(b2.a().equals("SOL") ? n4 << 24 | 0x7FE2FF : n5, f4);
            p.a(guiGraphics, nArray[0], nArray[1], 7, n9);
            p.a(guiGraphics, font, b2.a(), nArray[0] + 11, nArray[1] - 9, n9, 1.0f);
            p.a(guiGraphics, font, b2.b(), nArray[0] + 11, nArray[1] + 1, l.a(n6, f4), 1.0f);
        }
        double d2 = m.c(f2, k2.g());
        String string = d2 > 9.4607E12 ? String.format(Locale.ROOT, "RANGE %,.1f LY", d2 / 9.4607E12) : String.format(Locale.ROOT, "RANGE %,.0f KM", d2);
        p.a(guiGraphics, font, string, 18, n3 - 24, n6, 1.0f);
        if (f2 >= 96.0f && f2 < 130.0f) {
            n9 = (int)(f2 / 2.0f) % 2 == 0 ? 1 : 0;
            p.d(guiGraphics, font, "PARALLAX \u00b7 THE FIGURE ONLY HOLDS FROM SOL", n2 / 2, 30, n9 != 0 ? n5 : n6, 1.5f);
        }
        if (f3 > 0.01f) {
            n9 = (int)((float)n4 * f3);
            n8 = f2 < 138.0f ? (int)(Math.sin((double)f2 * 91.0) * 6.0) : 0;
            p.c(guiGraphics, font, "THE SHOOTING STAR", n2 / 2 + n8, n3 / 2 - 96, n9 << 24 | 0xB8A8FF, 4.0f);
            p.c(guiGraphics, font, "SS-04 SEVEN STARS \u00b7 STELLAR ARRAY \u00b7 URSA MAJOR \u00b7 79-123 LY", n2 / 2, n3 / 2 - 54, n9 << 24 | 0xE8E2FF, 1.25f);
        }
        if (f2 >= 150.0f && f2 < 192.0f || f2 >= 208.0f && f2 < 236.0f) {
            n9 = n2 - 170;
            n8 = n3 / 2 - 50;
            p.a(guiGraphics, font, "ARRAYS", n9, n8 - 14, n6, 1.0f);
            for (int i2 = 0; i2 < 7; ++i2) {
                boolean bl;
                boolean bl2 = m.a(f2, i2) > 0.5f;
                boolean bl3 = bl = m.b(f2, i2) >= 0.0f;
                int n10 = bl ? n7 : (bl2 ? n4 << 24 | 0x7FE2FF : n6);
                p.a(guiGraphics, font, cyou.rimuru.shootingstardemo.mc1211.star.c.b[i2], n9, n8 + i2 * 11, n10, 1.0f);
                p.b(guiGraphics, font, bl ? "FIRED" : (bl2 ? "ONLINE" : "\u00b7 \u00b7 \u00b7"), n9 + 150, n8 + i2 * 11, n10, 1.0f);
            }
        }
        if (f2 >= 208.0f && f2 < 236.0f) {
            n9 = Mth.clamp((int)((int)((f2 - 208.0f) / 4.0f) + 1), (int)1, (int)7);
            p.d(guiGraphics, font, String.format(Locale.ROOT, "FIRE %d / 7", n9), n2 / 2, 30, n7, 2.0f);
        }
        if (f2 >= 192.0f && f2 < 208.0f) {
            p.d(guiGraphics, font, "URSA MAJOR \u00b7 7 ARMED \u00b7 SOL-3", n2 / 2, 30, n5, 1.5f);
        }
    }

    private static int a(int n2, float f2) {
        return (int)((float)(n2 >>> 24) * f2) << 24 | n2 & 0xFFFFFF;
    }

    private static float a(float f2, float f3, float f4, float f5, float f6) {
        return Mth.clamp((float)((f2 - f3) / (f4 - f3)), (float)0.0f, (float)1.0f) * (1.0f - Mth.clamp((float)((f2 - f5) / (f6 - f5)), (float)0.0f, (float)1.0f));
    }
}

