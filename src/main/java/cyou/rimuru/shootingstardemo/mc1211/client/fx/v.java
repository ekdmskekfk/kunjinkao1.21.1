/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.fx;

import cyou.rimuru.shootingstardemo.mc1211.client.fx.d;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.e;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.i;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.q;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.s;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.t;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.u;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.w;
import cyou.rimuru.shootingstardemo.mc1211.star.m;
import java.util.Locale;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;

final class v {
    private static final int a = 15922682;
    private static final float b = 26.0f;
    private static final String[] c = new String[]{"BETELGEUSE", "ANTARES", "ALDEBARAN", "ARCTURUS", "POLARIS", "SIRIUS A", "VEGA", "RIGEL"};
    private static final String[] d = new String[]{"M1 IA", "M1.5 IAB", "K5 III", "K1.5 III", "F7 IB", "A1 V", "A0 V", "B8 IA"};
    private static final int[] e = new int[]{16738874, 16742464, 16751189, 16756848, 16773338, 14477055, 13819647, 11847935};
    private static final float f = 152.0f;
    private static final float g = 190.0f;
    private static final float h = 298.0f;
    private static final float i = 3.0f;
    private static final float[] j = new float[]{9.0f, 52.0f, 118.0f, 134.0f, 210.0f, 248.0f, 262.0f, 290.0f, 336.0f, 358.0f, 398.0f, 426.0f, 438.0f, 462.0f, 474.0f, 562.0f};
    private static final String[] k = new String[]{"TARGET MARKED", "LEAVING EARTH", "THE BRANE", "THE BULK", "THE FOLD", "THE THROAT", "TRANSIT", "ON STATION", "DESCENT", "OVERHEAD", "W-FOLD", "CAGE LOCKED", "SHEAR LINES", "SEVERED", "ASCENSION", "FOLDED"};

    private v() {
    }

    private static s.c a(float f2) {
        return s.c.a(Mth.clamp((int)((int)(f2 * 255.0f)), (int)0, (int)255) << 24 | 0xF2F5FA);
    }

    private static float b(float f2) {
        f2 = Mth.clamp((float)f2, (float)0.0f, (float)1.0f);
        return f2 * f2 * (3.0f - 2.0f * f2);
    }

    private static float a(float f2, float f3, float f4, float f5, float f6) {
        return v.b((f2 - f3) / (f4 - f3)) * (1.0f - v.b((f2 - f5) / (f6 - f5)));
    }

    static void a(GuiGraphics guiGraphics, d d2, u u2, float f2, float f3) {
        int n2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.i.c(guiGraphics);
        int n3 = cyou.rimuru.shootingstardemo.mc1211.client.fx.i.d(guiGraphics);
        float f4 = Mth.clamp((float)(f3 * 1.6f - 0.3f), (float)0.0f, (float)1.0f);
        if (f4 <= 0.01f) {
            return;
        }
        float f5 = v.a(f2, 146.0f, 152.0f, 208.0f, 216.0f);
        float f6 = 0.0f;
        if (f2 >= 290.0f && f2 < 358.0f) {
            f6 = v.a(w.a(f2), f2, n2, n3) * v.b((f2 - 298.0f + 4.0f) / 6.0f);
        }
        v.a(guiGraphics, u2, f2, n2, n3, f4 * (1.0f - Math.max(f5, f6)));
        if (f2 < 44.0f || f2 >= 358.0f) {
            v.b(guiGraphics, u2, f2, n2, n3, f4);
            return;
        }
        v.a(guiGraphics, f2, n2, n3, f4, f5, f6);
    }

    private static void a(GuiGraphics guiGraphics, u u2, float f2, int n2, int n3, float f3) {
        if (f3 <= 0.01f) {
            return;
        }
        s.c c2 = s.a(f2 * 0.03f, 1.6f, 0.22f, 1.0f);
        s.a(guiGraphics, s.a.b, "SS-05", 26.0f, 32.0f, 0.0f, 0.8f, 2.4f, v.a(0.75f), f3, 1.0f);
        float f4 = 9.0f;
        s.e.a(guiGraphics, 26.0f + 11.78f * f4 / 2.0f, 38.0f, f4, 0.0f, c2, f3, 1.0f);
        s.a(guiGraphics, 26.0f, 26.0f + 11.78f * f4, 53.0f, 0.75f, c2, f3 * 0.7f);
        s.a(guiGraphics, s.a.c, String.format(Locale.ROOT, "T+%05.2f", Float.valueOf(f2 / 20.0f / u2.u)), 26.0f, 67.0f, 0.0f, 0.85f, 0.4f, v.a(0.8f), f3, 1.0f);
        String string = v.d(f2);
        if (string == null) {
            return;
        }
        float f5 = v.b((f2 - v.e(f2)) / 8.0f);
        s.a(guiGraphics, s.a.b, string, (float)n2 - 26.0f, 32.0f, 1.0f, 0.9f, 2.2f, v.a(0.95f), f3 * f5, 1.0f);
        String string2 = v.a(u2, f2);
        if (string2 != null) {
            s.a(guiGraphics, s.a.c, string2, (float)n2 - 26.0f, 47.0f, 1.0f, 0.85f, 0.4f, v.a(0.8f), f3 * f5, 1.0f);
        }
    }

    private static int c(float f2) {
        int n2 = -1;
        for (int i2 = 0; i2 < j.length; ++i2) {
            if (!(f2 >= j[i2])) continue;
            n2 = i2;
        }
        if (n2 == 0 && f2 >= 40.0f) {
            return -1;
        }
        return n2;
    }

    private static String d(float f2) {
        int n2 = v.c(f2);
        return n2 < 0 ? null : k[n2];
    }

    private static float e(float f2) {
        int n2 = v.c(f2);
        return n2 < 0 ? f2 : j[n2];
    }

    private static String a(u u2, float f2) {
        if (f2 >= 52.0f && f2 < 118.0f) {
            return v.a(w.k(f2));
        }
        if (f2 >= 210.0f && f2 < 262.0f) {
            double[] dArray = w.c(f2);
            return String.format(Locale.ROOT, "R %.1f MLY   DEPTH %.1f MLY", dArray[0], dArray[2]);
        }
        if (f2 >= 358.0f && f2 < 398.0f) {
            double[] dArray = w.a(f2, u2.x);
            return String.format(Locale.ROOT, "ALT %,.0f KM   SPAN %,.0f KM", dArray[0] / 1000.0, dArray[1] * 2.0 / 1000.0);
        }
        if (f2 >= 398.0f && f2 < 426.0f) {
            return String.format(Locale.ROOT, "ALT %,.0f M", w.a(f2, u2.x)[0]);
        }
        if (f2 >= 474.0f && f2 < 562.0f) {
            int n2 = 0;
            for (int n3 : m.j()) {
                n2 += f2 >= (float)n3 ? 1 : 0;
            }
            return String.format(Locale.ROOT, "%02d / 64 CELLS", n2);
        }
        if (f2 >= 562.0f) {
            return String.format(Locale.ROOT, "%d \u00d7 %d BLOCKS", u2.y, u2.y);
        }
        if (f2 < 462.0f && (f2 < 40.0f || f2 >= 358.0f)) {
            return String.format(Locale.ROOT, "SHEAR T\u2212%05.2f", Float.valueOf((462.0f - f2) / 20.0f / u2.u));
        }
        return null;
    }

    static String a(double d2) {
        if (d2 < 0.0) {
            return "RANGE \u2014";
        }
        double d3 = d2 / 1.496E8;
        double d4 = d2 / 9.4607E12;
        if (d3 < 2.0) {
            return String.format(Locale.ROOT, "RANGE %,.0f KM", d2);
        }
        if (d4 < 0.5) {
            return String.format(Locale.ROOT, "RANGE %,.1f AU", d3);
        }
        if (d4 < 1000.0) {
            return String.format(Locale.ROOT, "RANGE %,.1f LY", d4);
        }
        if (d4 < 1000000.0) {
            return String.format(Locale.ROOT, "RANGE %,.1f KLY", d4 / 1000.0);
        }
        return String.format(Locale.ROOT, "RANGE %,.1f MLY", d4 / 1000000.0);
    }

    private static float[] a(w.a a2, double d2, double d3, double d4, int n2, int n3) {
        q.c c2 = new q.c(d2, d3, d4);
        double d5 = c2.c(a2.c());
        if (d5 <= 1.0E-18) {
            return null;
        }
        double d6 = Math.tan(Math.toRadians(a2.f()) * 0.5);
        double d7 = c2.c(a2.e()) / (d5 * d6);
        double d8 = c2.c(a2.d()) / (d5 * d6);
        if (Math.abs(d7) > 1.9 || Math.abs(d8) > 1.05) {
            return null;
        }
        return new float[]{(float)((double)n2 / 2.0 + d7 * (double)n3 / 2.0), (float)((double)n3 / 2.0 - d8 * (double)n3 / 2.0), (float)((double)n3 / 2.0 / (d5 * d6))};
    }

    private static void a(GuiGraphics guiGraphics, float f2, int n2, int n3, float f3, float f4, float f5) {
        w.a a2 = w.a(f2);
        float f6 = f3 * (1.0f - Math.max(f4, f5));
        if (f6 > 0.01f) {
            for (w.b b2 : w.j(f2)) {
                float[] fArray = v.a(a2, b2.c().d(), b2.c().e(), b2.c().f(), n2, n3);
                if (fArray == null) continue;
                s.a(guiGraphics, fArray[0], fArray[1], 9.0f, 4.0f, 1.0f, (int)(f6 * 0.9f * 255.0f) << 24 | 0xF2F5FA);
                s.a(guiGraphics, s.a.b, b2.a(), fArray[0] + 16.0f, fArray[1] - 1.0f, 0.0f, 0.8f, 1.8f, v.a(0.95f), f6, 1.0f);
                s.a(guiGraphics, s.a.c, b2.b(), fArray[0] + 16.0f, fArray[1] + 10.0f, 0.0f, 0.78f, 0.3f, v.a(0.8f), f6, 1.0f);
            }
        }
        if (f4 > 0.01f) {
            v.a(guiGraphics, f2, n2, n3, f3 * f4);
        }
        if (f5 > 0.01f) {
            v.a(guiGraphics, a2, f2, n2, n3, f3 * f5);
        }
    }

    private static void a(GuiGraphics guiGraphics, float f2, int n2, int n3, float f3) {
        float f4 = f3 * v.a(f2, 152.0f, 160.0f, 182.0f, 190.0f);
        if (f4 <= 0.01f) {
            return;
        }
        float f5 = 46.0f;
        float f6 = (float)n2 / 2.0f;
        float f7 = (float)n3 / 2.0f - f5 / 2.0f;
        s.c c2 = s.a(f2 * 0.025f, 2.4f, 0.16f, 1.0f);
        float f8 = v.b((f2 - 152.0f) / 16.0f);
        s.e.a(guiGraphics, f6, f7, f5, c2, f4 * 0.6f, f4 * (0.35f + 0.25f * f8));
        s.e.a(guiGraphics, f6, f7, f5, f5 * 0.55f * (1.0f - f8), c2, f4, 0.9f);
    }

    private static void a(GuiGraphics guiGraphics, w.a a2, float f2, int n2, int n3, float f3) {
        double[] dArray = w.a(f2, a2, 0.0, 0.0, 0.0);
        float[] fArray = dArray == null ? null : v.a(a2, dArray[0], dArray[1], dArray[2], n2, n3);
        for (int i2 = 0; i2 < 8; ++i2) {
            float[] fArray2;
            double[] dArray2;
            float f4 = (f2 - 298.0f - (float)i2 * 3.0f) / 7.0f;
            if (f4 <= 0.0f || (dArray2 = t.d(t.a(i2), f2))[3] <= 0.0 || dArray2[12] > 0.6) continue;
            double[] dArray3 = w.a(f2, a2, dArray2[0], dArray2[1], dArray2[2]);
            float[] fArray3 = fArray2 = dArray3 == null ? null : v.a(a2, dArray3[0], dArray3[1], dArray3[2], n2, n3);
            if (fArray2 == null) continue;
            float f5 = Math.max(7.0f, (float)(dArray2[3] * t.C[i2] * dArray3[3] * (double)fArray2[2]) * 1.3f + 5.0f);
            float f6 = v.b(f4);
            float f7 = Mth.lerp((float)f6, (float)(f5 + 46.0f), (float)f5);
            float f8 = f3 * Mth.clamp((float)(f4 * 2.0f), (float)0.0f, (float)1.0f);
            int n4 = v.a(0xFFFFFF, e[i2], f6);
            s.a(guiGraphics, fArray2[0], fArray2[1], f7, Mth.clamp((float)(f7 * 0.45f), (float)3.0f, (float)8.0f), 1.0f, (int)(f8 * 255.0f) << 24 | n4);
            float f9 = f3 * v.b((f4 - 0.7f) / 0.6f);
            if (!(f9 > 0.01f)) continue;
            boolean bl = fArray == null || fArray2[0] >= fArray[0];
            float f10 = bl ? fArray2[0] + f7 + 7.0f : fArray2[0] - f7 - 7.0f;
            float f11 = bl ? 0.0f : 1.0f;
            s.a(guiGraphics, s.a.b, c[i2], f10, fArray2[1] - 1.0f, f11, 0.8f, 1.8f, s.c.a(0xFF000000 | v.a(15922682, e[i2], 0.35f)), f9, 1.0f);
            s.a(guiGraphics, s.a.c, d[i2], f10, fArray2[1] + 10.0f, f11, 0.78f, 0.3f, v.a(0.8f), f9, 1.0f);
        }
    }

    private static float a(w.a a2, float f2, int n2, int n3) {
        float f3 = Float.MAX_VALUE;
        float f4 = Float.MAX_VALUE;
        float f5 = -3.4028235E38f;
        float f6 = -3.4028235E38f;
        double d2 = Math.tan(Math.toRadians(a2.f()) * 0.5);
        for (int i2 = 0; i2 < 16; ++i2) {
            double[] dArray = t.a(i2, f2);
            double[] dArray2 = w.a(f2, a2, dArray[0], dArray[1], dArray[2]);
            if (dArray2 == null) {
                return 0.0f;
            }
            q.c c2 = new q.c(dArray2[0], dArray2[1], dArray2[2]);
            double d3 = c2.c(a2.c());
            if (d3 <= 1.0E-18) {
                return 0.0f;
            }
            float f7 = (float)((double)n2 / 2.0 + c2.c(a2.e()) / (d3 * d2) * (double)n3 / 2.0);
            float f8 = (float)((double)n3 / 2.0 - c2.c(a2.d()) / (d3 * d2) * (double)n3 / 2.0);
            f3 = Math.min(f3, f7);
            f4 = Math.min(f4, f8);
            f5 = Math.max(f5, f7);
            f6 = Math.max(f6, f8);
        }
        float f9 = Math.max((f5 - f3) * (f6 - f4), 1.0f);
        float f10 = Math.max(0.0f, Math.min(f5, (float)n2) - Math.max(f3, 0.0f)) * Math.max(0.0f, Math.min(f6, (float)n3) - Math.max(f4, 0.0f));
        return v.b((f10 / f9 - 0.55f) / 0.1f) * v.b(((f6 - f4) / (float)n3 - 0.2f) / 0.08f);
    }

    private static int a(int n2, int n3, float f2) {
        int n4 = (int)Mth.lerp((float)f2, (float)(n2 >> 16 & 0xFF), (float)(n3 >> 16 & 0xFF));
        int n5 = (int)Mth.lerp((float)f2, (float)(n2 >> 8 & 0xFF), (float)(n3 >> 8 & 0xFF));
        int n6 = (int)Mth.lerp((float)f2, (float)(n2 & 0xFF), (float)(n3 & 0xFF));
        return n4 << 16 | n5 << 8 | n6;
    }

    private static void b(GuiGraphics guiGraphics, u u2, float f2, int n2, int n3, float f3) {
        if (f2 >= 9.0f && f2 < 40.0f) {
            float[] fArray = cyou.rimuru.shootingstardemo.mc1211.client.fx.e.a(u2.i(), guiGraphics);
            if (fArray != null) {
                float f4 = v.b((f2 - 9.0f) / 8.0f);
                float f5 = Mth.lerp((float)f4, (float)60.0f, (float)12.0f);
                s.a(guiGraphics, fArray[0], fArray[1], f5, 6.0f, 1.0f, (int)(f3 * 255.0f) << 24 | 0xF2F5FA);
                float f6 = f3 * v.b((f2 - 9.0f - 6.0f) / 6.0f);
                s.a(guiGraphics, s.a.b, "TARGET", fArray[0] + f5 + 8.0f, fArray[1] - 1.0f, 0.0f, 0.85f, 2.0f, v.a(0.95f), f6, 1.0f);
                s.a(guiGraphics, s.a.c, String.format(Locale.ROOT, "%d \u00d7 %d   8 \u00d7 8 CELLS", u2.y, u2.y), fArray[0] + f5 + 8.0f, fArray[1] + 10.0f, 0.0f, 0.8f, 0.3f, v.a(0.8f), f6, 1.0f);
            }
            return;
        }
        float f7 = (float)n2 / 2.0f;
        float f8 = (float)n3 / 2.0f - 64.0f;
        float f9 = f2 - 462.0f;
        if (f9 >= 0.0f && f9 < 16.0f) {
            v.a(guiGraphics, "SEVERED", "64 CELLS RELEASED", f7, f8, f2, f9, 16.0f, f3);
        }
        for (int i2 = 0; i2 < 4; ++i2) {
            float f10 = f2 - (float)(474 + i2 * 20);
            if (!(f10 >= 0.0f) || !(f10 < 16.0f)) continue;
            int n4 = 8 * i2 + 4;
            String string = String.format(Locale.ROOT, "RING %d / 4   %d CELLS   LIFT %d\u2013%d   FLOOR \u2212%d", i2 + 1, n4, Math.round(90.0 * u2.x), Math.round(250.0 * u2.x), u2.d(i2));
            v.a(guiGraphics, "ASCENSION", string, f7, f8, f2, f10, 16.0f, f3);
            if (u2.C <= 0) continue;
            s.a(guiGraphics, s.a.c, String.format(Locale.ROOT, "CEILING EXCEEDED   W-WRAP %d SECTIONS", u2.C), f7, f8 + 38.0f, 0.5f, 0.75f, 0.3f, s.a(f2 * 0.05f, 1.2f, 0.3f, 1.0f), f3 * v.a(f10, 2.0f, 6.0f, 12.0f, 16.0f), 0.8f);
        }
        if (f2 >= 474.0f && f2 < 582.0f) {
            v.b(guiGraphics, f2, n2, n3, f3 * (1.0f - v.b((f2 - 562.0f) / 20.0f)));
        }
        if (f2 >= 592.0f) {
            v.a(guiGraphics, "FOLDED", String.format(Locale.ROOT, "64 CELLS   %d BLOCKS   W-AXIS LOCKED", u2.y), f7, (float)n3 - 92.0f, f2, f2 - 562.0f - 30.0f, 1.0E9f, f3);
        }
    }

    private static void a(GuiGraphics guiGraphics, String string, String string2, float f2, float f3, float f4, float f5, float f6, float f7) {
        float f8 = v.a(f5, 0.0f, 6.0f, f6 - 5.0f, f6) * f7;
        if (f8 <= 0.01f) {
            return;
        }
        float f9 = v.b(f5 / 10.0f);
        s.c c2 = s.a(f4 * 0.03f, 2.2f, 0.72f, 1.0f);
        float f10 = Mth.lerp((float)f9, (float)9.0f, (float)5.5f);
        float f11 = s.a(guiGraphics, s.a.a, string, 2.2f, f10);
        s.a(guiGraphics, f2, f3 + 4.0f, f11 * 0.5f + 160.0f, 52.0f, f8 * 0.4f);
        float f12 = s.a(guiGraphics, s.a.a, string, f2, f3, 0.5f, 2.2f, f10, c2, f8, 2.0f);
        float f13 = f12 * 0.5f * v.b((f5 - 2.0f) / 10.0f);
        s.a(guiGraphics, f2 - f13, f2 + f13, f3 + 9.0f, 0.75f, c2, f8 * 0.8f);
        s.a(guiGraphics, s.a.c, string2, f2, f3 + 24.0f, 0.5f, 0.85f, 0.4f, v.a(0.85f), f8 * v.b((f5 - 4.0f) / 6.0f), 1.0f);
    }

    private static void b(GuiGraphics guiGraphics, float f2, int n2, int n3, float f3) {
        if (f3 <= 0.01f) {
            return;
        }
        int[] nArray = m.j();
        float f4 = 5.0f;
        float f5 = 2.0f;
        float f6 = 8.0f * f4 + 7.0f * f5;
        float f7 = (float)n2 / 2.0f - f6 / 2.0f;
        float f8 = (float)n3 - 26.0f - f6;
        for (int i2 = 0; i2 < 64; ++i2) {
            int n4 = i2 % 8;
            int n5 = i2 / 8;
            float f9 = v.b((f2 - (float)nArray[i2]) / 6.0f);
            int n6 = s.a(f2 * 0.03f + (float)(n4 + n5) * 2.2f / 14.0f, 0.2f) & 0xFFFFFF;
            int n7 = f9 > 0.0f ? (int)(f3 * (0.2f + 0.75f * f9) * 255.0f) << 24 | v.a(15922682, n6, f9) : (int)(f3 * 0.2f * 255.0f) << 24 | 0xF2F5FA;
            s.a(guiGraphics, f7 + (float)n4 * (f4 + f5), f8 + (float)n5 * (f4 + f5), f4, f4, n7);
        }
    }
}

