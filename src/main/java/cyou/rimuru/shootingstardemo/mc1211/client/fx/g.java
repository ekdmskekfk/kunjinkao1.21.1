/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.fx;

import cyou.rimuru.shootingstardemo.mc1211.client.fx.d;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.e;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.f;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.h;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.i;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.p;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.q;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

final class g {
    private static final int a = 16734751;
    private static final int b = 16769740;
    private static final int c = 8381183;
    private static final int d = 0xFFFFFF;
    private static final String[] e = new String[]{"MAGNETOSPHERE TAP", "COIL BANK A", "COIL BANK B", "COIL BANK C", "COIL BANK D", "BREECH", "TRACK SYNC"};

    private g() {
    }

    static void a(GuiGraphics guiGraphics, d d2, f f2, float f3, float f4) {
        Minecraft minecraft = d2.a();
        Font font = minecraft.font;
        int n2 = i.c(guiGraphics);
        int n3 = i.d(guiGraphics);
        float f5 = Mth.clamp((float)(f4 * 1.6f - 0.3f), (float)0.0f, (float)1.0f);
        if (f5 <= 0.01f) {
            return;
        }
        if (f3 >= 292.0f && f3 < 362.0f) {
            return;
        }
        int n4 = (int)(f5 * 255.0f);
        boolean bl = (int)(f3 * 3.0f) % 13 == 0;
        int n5 = n4 << 24 | 0xFF5A1F;
        int n6 = (int)((float)n4 * 0.55f) << 24 | 0xFFE2CC;
        int n7 = n4 << 24 | 0xFFFFFF;
        int n8 = 12;
        int n9 = 22;
        p.a(guiGraphics, n8, n8, n9, 1, 1, n5);
        p.a(guiGraphics, n2 - n8, n8, n9, -1, 1, n5);
        p.a(guiGraphics, n8, n3 - n8, n9, 1, -1, n5);
        p.a(guiGraphics, n2 - n8, n3 - n8, n9, -1, -1, n5);
        p.a(guiGraphics, font, "UPLINK // SS-03 > GUNGNIR", n8 + 6, n8 + 6, bl ? n6 : n5, 1.0f);
        p.a(guiGraphics, font, String.format(Locale.ROOT, "T+%05.2f", Float.valueOf(f3 / 20.0f)), n8 + 6, n8 + 17, n6, 1.0f);
        String string = f3 < 322.0f ? String.format(Locale.ROOT, "IMPACT T-%05.2f", Float.valueOf(Math.max(0.0f, (322.0f - f3) / 20.0f / f2.j()))) : "IMPACT CONFIRMED";
        p.b(guiGraphics, font, string, n2 - n8 - 6, n8 + 6, n5, 1.0f);
        Vec3 vec3 = f2.g();
        p.b(guiGraphics, font, String.format(Locale.ROOT, "TARGET %d / %d / %d", Mth.floor((double)vec3.x), Mth.floor((double)vec3.y), Mth.floor((double)vec3.z)), n2 - n8 - 6, n3 - n8 - 14, n6, 1.0f);
        if (f3 < 54.0f) {
            float[] fArray;
            if (f3 >= 9.0f && (fArray = cyou.rimuru.shootingstardemo.mc1211.client.fx.e.a(vec3.add(0.0, 0.5, 0.0), guiGraphics)) != null) {
                float f6 = Mth.clamp((float)((f3 - 9.0f) / 8.0f), (float)0.0f, (float)1.0f);
                int n10 = (int)Mth.lerp((float)f6, (float)60.0f, (float)9.0f);
                p.a(guiGraphics, (int)fArray[0], (int)fArray[1], n10, n5);
                p.a(guiGraphics, font, "KINETIC LOCK", (int)fArray[0] + n10 + 4, (int)fArray[1] - 9, n5, 1.0f);
                p.a(guiGraphics, font, String.format(Locale.ROOT, "ZONE \u00d8%d \u00b7 PLANE TO GROUND", (int)(f2.i * 2.0f)), (int)fArray[0] + n10 + 4, (int)fArray[1] + 1, n6, 1.0f);
            }
            return;
        }
        if (f3 < 110.0f) {
            g.b(guiGraphics, font, f3, n2, n3, n4, n5, n6);
            return;
        }
        if (f3 < 146.0f) {
            g.a(guiGraphics, font, f3, n2, n3, n4, n5, n6, n7);
            return;
        }
        if (f3 < 168.0f) {
            g.b(guiGraphics, font, f3, n2, n3, n4, n5, n6, n7);
            return;
        }
        if (f3 < 252.0f) {
            g.c(guiGraphics, font, f3, n2, n3, n4, n5, n6, n7);
            return;
        }
        if (f3 < 262.0f) {
            boolean bl2 = (int)(f3 / 1.5f) % 2 == 0;
            p.d(guiGraphics, font, "RELEASE", n2 / 2, n3 / 2 - 70, bl2 ? n7 : n5, 3.0f);
            return;
        }
        if (f3 < 292.0f) {
            g.d(guiGraphics, font, f3, n2, n3, n4, n5, n6, n7);
            return;
        }
        if (f3 >= 362.0f) {
            int n11 = (int)((float)n4 * Mth.clamp((float)((f3 - 322.0f - 40.0f) / 10.0f), (float)0.0f, (float)1.0f));
            p.d(guiGraphics, font, "IMPACT CONFIRMED", n2 / 2, n3 - n8 - 52, n11 << 24 | 0xFF5A1F, 2.0f);
            p.c(guiGraphics, font, String.format(Locale.ROOT, "ZONE \u00d8%d PLANED \u00b7 SPIRE STANDING \u00b7 %d M", (int)(f2.i * 2.0f), 384), n2 / 2, n3 - n8 - 28, n11 << 24 | 0xFFE2CC, 1.25f);
        }
    }

    private static int[] a(h.a a2, q.c c2, int n2, int n3) {
        double d2 = c2.c(a2.c());
        if (d2 <= 1.0E-12) {
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

    private static void a(GuiGraphics guiGraphics, Font font, float f2, int n2, int n3, int n4, int n5, int n6) {
        h.a a2 = h.f(f2);
        for (h.b b2 : h.s(f2)) {
            int[] nArray = g.a(a2, b2.c(), n2, n3);
            if (nArray == null) continue;
            boolean bl = b2.a().equals("TARGET") || b2.a().equals("BREECH") || b2.b().contains("SS-03");
            int n7 = bl ? n5 : n4 << 24 | 0x7FE2FF;
            p.a(guiGraphics, nArray[0], nArray[1], 7, n7);
            p.a(guiGraphics, font, b2.a(), nArray[0] + 11, nArray[1] - 9, n7, 1.0f);
            p.a(guiGraphics, font, b2.b(), nArray[0] + 11, nArray[1] + 1, n6, 1.0f);
        }
    }

    private static void b(GuiGraphics guiGraphics, Font font, float f2, int n2, int n3, int n4, int n5, int n6) {
        g.a(guiGraphics, font, f2, n2, n3, n4, n5, n6);
        double d2 = h.t(f2);
        int n7 = (int)Math.floor(Math.log10(Math.max(d2, 1.0)));
        p.a(guiGraphics, font, String.format(Locale.ROOT, "SCALE 10^%d KM", n7), 18, n3 - 40, n5, 1.5f);
        p.a(guiGraphics, font, String.format(Locale.ROOT, "RANGE %,.0f KM", d2), 18, n3 - 24, n6, 1.0f);
        float f3 = (float)(Math.log10(Math.max(d2, 1.0)) - (double)n7);
        guiGraphics.fill(18, n3 - 46, 138, n3 - 45, n6);
        guiGraphics.fill(18, n3 - 48, 18 + (int)(120.0f * f3), n3 - 45, n5);
        if (f2 >= 84.0f && f2 < 100.0f) {
            boolean bl = (int)(f2 / 2.0f) % 2 == 0;
            p.d(guiGraphics, font, "RELAY \u00b7 JUPITER 5.2 AU", n2 / 2, 30, bl ? n5 : n6, 1.5f);
        }
    }

    private static void a(GuiGraphics guiGraphics, Font font, float f2, int n2, int n3, int n4, int n5, int n6, int n7) {
        int n8;
        g.a(guiGraphics, font, f2, n2, n3, n4, n5, n6);
        float f3 = g.a(f2, 113.0f, 116.0f, 130.0f, 136.0f);
        if (f3 > 0.01f) {
            int n9 = (int)((float)n4 * f3);
            n8 = f2 < 119.0f ? (int)(Math.sin((double)f2 * 91.0) * 6.0) : 0;
            p.c(guiGraphics, font, "THE SHOOTING STAR", n2 / 2 + n8, n3 / 2 - 96, n9 << 24 | 0xFF5A1F, 4.0f);
            p.c(guiGraphics, font, "SS-03 GUNGNIR \u00b7 MASS DRIVER \u00b7 JUPITER \u00b7 RING 1.22 RJ \u00b7 536,000 KM ROUND", n2 / 2, n3 / 2 - 54, n9 << 24 | 0xFFE2CC, 1.25f);
        }
        if (f2 >= 124.0f) {
            float f4 = h.b(f2);
            n8 = (int)(f4 * 670000.0f);
            p.d(guiGraphics, font, f4 < 0.999f ? "ACCELERATOR WAKING" : "ACCELERATOR ONLINE", n2 / 2, 30, f4 < 0.999f && (int)(f2 / 2.0f) % 2 == 0 ? n6 : n5, 1.5f);
            p.c(guiGraphics, font, String.format(Locale.ROOT, "COILS %,d / 670,000", n8), n2 / 2, n3 - 60, n7, 1.25f);
            int n10 = n2 - 170;
            int n11 = n3 / 2 - 50;
            p.a(guiGraphics, font, "SYSTEMS", n10, n11 - 14, n6, 1.0f);
            for (int i2 = 0; i2 < e.length; ++i2) {
                boolean bl = f4 >= ((float)i2 + 0.5f) / (float)e.length;
                p.a(guiGraphics, font, e[i2], n10, n11 + i2 * 11, bl ? n4 << 24 | 0x7FE2FF : n6, 1.0f);
                p.b(guiGraphics, font, bl ? "ONLINE" : "\u00b7 \u00b7 \u00b7", n10 + 150, n11 + i2 * 11, bl ? n4 << 24 | 0x7FE2FF : n6, 1.0f);
            }
        }
    }

    private static void b(GuiGraphics guiGraphics, Font font, float f2, int n2, int n3, int n4, int n5, int n6, int n7) {
        int n8 = 24;
        int n9 = n3 / 2 - 40;
        p.a(guiGraphics, font, "PAYLOAD", n8, n9, n6, 1.0f);
        p.a(guiGraphics, font, "GUNGNIR", n8, n9 + 12, n5, 2.0f);
        p.a(guiGraphics, font, "LENGTH 1,020 M \u00b7 \u00d8 36 M", n8, n9 + 34, n7, 1.0f);
        p.a(guiGraphics, font, "MASS 9.1E10 KG \u00b7 W-OS LATTICE", n8, n9 + 45, n7, 1.0f);
        p.a(guiGraphics, font, "CORE: INERT \u00b7 NO WARHEAD", n8, n9 + 56, n6, 1.0f);
        boolean bl = f2 >= 160.0f;
        boolean bl2 = (int)(f2 / 2.0f) % 2 == 0;
        p.d(guiGraphics, font, bl ? "BREECH LOCKED" : "LOADING", n2 / 2, 30, bl || bl2 ? n5 : n6, 1.5f);
    }

    private static void c(GuiGraphics guiGraphics, Font font, float f2, int n2, int n3, int n4, int n5, int n6, int n7) {
        double d2 = h.d((double)f2);
        int n8 = Math.min(7, (int)Math.floor(d2) + 1);
        double d3 = d2 - Math.floor(d2);
        boolean bl = d3 < 0.12 && d2 >= 1.0;
        p.d(guiGraphics, font, String.format(Locale.ROOT, "LAP %d / %d", n8, 7), n2 / 2, 30, bl ? n7 : n5, 2.0f);
        double d4 = h.g((double)f2);
        p.c(guiGraphics, font, String.format(Locale.ROOT, "VELOCITY %.4f c", d4), n2 / 2, n3 - 64, n7, 1.5f);
        int n9 = 220;
        guiGraphics.fill(n2 / 2 - n9 / 2, n3 - 44, n2 / 2 + n9 / 2, n3 - 43, n6);
        guiGraphics.fill(n2 / 2 - n9 / 2, n3 - 46, n2 / 2 - n9 / 2 + (int)((double)n9 * d4), n3 - 43, n5);
        for (int i2 = 1; i2 <= 7; ++i2) {
            int n10 = n2 / 2 - 60 + (i2 - 1) * 20;
            boolean bl2 = d2 >= (double)i2;
            guiGraphics.fill(n10, n3 - 34, n10 + 12, n3 - 30, bl2 ? n5 : n6);
        }
    }

    private static void d(GuiGraphics guiGraphics, Font font, float f2, int n2, int n3, int n4, int n5, int n6, int n7) {
        g.a(guiGraphics, font, f2, n2, n3, n4, n5, n6);
        double d2 = h.t(f2);
        p.c(guiGraphics, font, String.format(Locale.ROOT, "RANGE %,.0f KM", d2), n2 / 2, n3 - 64, n7, 1.5f);
        p.c(guiGraphics, font, String.format(Locale.ROOT, "VELOCITY %.4f c", h.g((double)f2)), n2 / 2, n3 - 44, n6, 1.0f);
        if (f2 >= 264.0f && f2 < 277.0f) {
            boolean bl = (int)(f2 / 1.5f) % 2 == 0;
            p.d(guiGraphics, font, "DEBRIS FIELD \u00b7 MAIN BELT", n2 / 2, 30, bl ? n5 : n6, 1.5f);
        } else if (f2 >= 282.0f) {
            p.d(guiGraphics, font, "TERMINAL \u00b7 SOL-3", n2 / 2, 30, n5, 1.5f);
        }
    }

    private static float a(float f2, float f3, float f4, float f5, float f6) {
        return Mth.clamp((float)((f2 - f3) / (f4 - f3)), (float)0.0f, (float)1.0f) * (1.0f - Mth.clamp((float)((f2 - f5) / (f6 - f5)), (float)0.0f, (float)1.0f));
    }
}

