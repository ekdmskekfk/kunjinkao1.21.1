/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.fx;

import cyou.rimuru.shootingstardemo.mc1211.client.fx.q;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public final class h {
    public static final float a = 9.0f;
    public static final float b = 31.0f;
    public static final float c = 44.0f;
    public static final float d = 50.0f;
    public static final float e = 62.0f;
    public static final float f = 84.0f;
    public static final float g = 110.0f;
    public static final float h = 124.0f;
    public static final float i = 146.0f;
    public static final float j = 160.0f;
    public static final float k = 168.0f;
    public static final float l = 252.0f;
    public static final float m = 256.0f;
    public static final float n = 290.0f;
    public static final float o = 300.0f;
    public static final float p = 322.0f;
    static final float q = 202.0f;
    static final float r = 226.0f;
    static final float s = 262.0f;
    static final float t = 265.0f;
    static final float u = 266.0f;
    static final float v = 277.0f;
    static final float w = 282.0f;
    public static final int x = 7;
    static final double y = 69911.0;
    static final double z = 4.6732617030490934E-4;
    static final double A = 1.22;
    static final double B = 0.0042;
    static final double C = 0.8;
    static final double D = 360.0;
    static final q.c E = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.N[4];
    static final q.c F = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.H.b();
    static final q.c G = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.aq.b(E).b();
    static final q.c H = cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a();
    static final q.c I = H.a(-1.0);
    static final q.c J = F.d(H).b();
    static final double K = -1.5707963267948966;
    static final q.c L = G.a(0.5).a(F.a(0.34)).a(I.a(-0.66)).a(J.a(0.28)).b();
    static final q.c M = G.a(0.75).a(F.a(0.45)).a(I.a(-0.3)).a(J.a(-0.35)).b();
    static final q.c N = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w.a(60.2).a(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.D.a(0.95));
    static final double O = 300.0;
    static final q.c P = E.a(cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(-1.5707963267948966).a(4.6732617030490934E-4));
    static final q.c Q = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.G.a(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w.a(0.012818838871347653));
    static final q.c R = P.a(J.a(Q.b(P).a() * 0.34));
    static final q.c S = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.G.a(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w.a(Q.b(P).a() * 0.3));
    static final q.c T = F.a(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.D.a(0.6)).b();
    public static final Map<String, String> U = new LinkedHashMap<String, String>();
    static final double[] V;
    static final double[] W;
    static final double[] X;
    static final double[] Y;
    static final q.c[] Z;
    static final double[] aa;
    static final double[] ab;
    static final q.c ac;
    static final double ad;
    static final double[] ae;
    static final double[] af;
    static final double[] ag;
    static final double[] ah;
    static final q.c ai;
    static final q.c aj;

    private h() {
    }

    static q.c a() {
        q.c c2 = G.b(F.a(G.c(F))).b();
        q.c c3 = F.d(c2).b();
        q.c c4 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.G.b(E).b();
        double d2 = Math.toRadians(40.0);
        for (double d3 : new double[]{1.0, -1.0}) {
            q.c c5 = c2.a(Math.cos(d2)).a(c3.a(Math.sin(d2) * d3)).b();
            if (!(F.d(c5).c(c4) > 0.0)) continue;
            return c5;
        }
        return c2;
    }

    static q.c a(q.c c2) {
        return c2.b(F.a(c2.c(F))).b();
    }

    static q.c a(double d2) {
        return J.a(Math.cos(d2) * 1.22).a(I.a(Math.sin(d2) * 1.22));
    }

    static q.c b(double d2) {
        return J.a(-Math.sin(d2)).a(I.a(Math.cos(d2)));
    }

    static double c(double d2) {
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.q.c((d2 - 168.0) / 84.0);
    }

    public static double d(double d2) {
        return 7.0 * Math.pow(cyou.rimuru.shootingstardemo.mc1211.client.fx.h.c(d2), 2.2);
    }

    public static double a(int n2) {
        return 168.0 + 84.0 * Math.pow((double)n2 / 7.0, 0.45454545454545453);
    }

    static double e(double d2) {
        return -1.5707963267948966 + cyou.rimuru.shootingstardemo.mc1211.client.fx.h.d(d2) * Math.PI * 2.0;
    }

    static double f(double d2) {
        double d3 = cyou.rimuru.shootingstardemo.mc1211.client.fx.h.c(d2);
        if (d3 <= 0.0 || d3 >= 1.0) {
            return 0.0;
        }
        return 96.76105373056564 * Math.pow(d3, 1.2) / 84.0;
    }

    public static double g(double d2) {
        if (d2 < 168.0) {
            return 0.0;
        }
        double d3 = d2 < 252.0 ? cyou.rimuru.shootingstardemo.mc1211.client.fx.h.f(d2) * 1.22 * 69911.0 * 20.0 : cyou.rimuru.shootingstardemo.mc1211.client.fx.h.f(251.99) * 1.22 * 69911.0 * 20.0;
        double d4 = 1.0 - Math.exp(-d3 / 1100000.0);
        if (d2 >= 256.0) {
            d4 = Math.max(d4, 0.93 + 0.05 * cyou.rimuru.shootingstardemo.mc1211.client.fx.q.b((d2 - 256.0) / 20.0));
        }
        return Math.min(d4, 0.9987);
    }

    static double h(double d2) {
        if (d2 < 168.0) {
            return 0.0;
        }
        double d3 = cyou.rimuru.shootingstardemo.mc1211.client.fx.h.c(d2);
        return 288.0 * d3 * d3 * d3;
    }

    static double i(double d2) {
        double d3 = cyou.rimuru.shootingstardemo.mc1211.client.fx.h.c(d2);
        if (d3 <= 0.0 || d3 >= 1.0) {
            return 0.0;
        }
        return 864.0 * d3 * d3 / 84.0;
    }

    static double j(double d2) {
        if (d2 >= 160.0) {
            return 0.0;
        }
        double d3 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.c((d2 - 146.0) / 14.0);
        return -1.7 * (1.0 - d3 * d3);
    }

    static float a(float f2) {
        if (f2 < 168.0f) {
            return 0.22f * cyou.rimuru.shootingstardemo.mc1211.client.fx.q.m((f2 - 146.0f) / 6.0f);
        }
        if (f2 < 252.0f) {
            return (float)(0.85 * Math.pow(cyou.rimuru.shootingstardemo.mc1211.client.fx.h.c((double)f2), 1.6));
        }
        if (f2 < 290.0f) {
            float f3 = (float)Math.exp((double)(-(f2 - 252.0f)) / 14.0);
            return 0.3f + 0.8f * f3;
        }
        return 0.3f + 0.9f * cyou.rimuru.shootingstardemo.mc1211.client.fx.q.m((f2 - 290.0f + 2.0f) / 6.0f);
    }

    static float b(float f2) {
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.q.m((f2 - 124.0f - 2.0f) / 16.0f);
    }

    static float c(float f2) {
        float f3 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.m((f2 - 124.0f - 14.0f) / 8.0f);
        float f4 = (float)(cyou.rimuru.shootingstardemo.mc1211.client.fx.h.d((double)f2) / 7.0);
        float f5 = f2 < 252.0f ? 1.0f : (float)Math.exp((double)(-(f2 - 252.0f)) / 6.0);
        return (0.35f * f3 + 0.65f * f4) * f5;
    }

    static float d(float f2) {
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(f2, 124.0f, 134.0f, 254.0f, 264.0f) * (0.25f + 0.75f * (float)(cyou.rimuru.shootingstardemo.mc1211.client.fx.h.d((double)f2) / 7.0));
    }

    public static float e(float f2) {
        return f2 >= 252.0f ? (f2 - 252.0f) / 20.0f : -1.0f;
    }

    static q.c k(double d2) {
        double d3 = 1.0 - d2;
        return P.a(d3 * d3 * d3).a(R.a(3.0 * d3 * d3 * d2)).a(S.a(3.0 * d3 * d2 * d2)).a(Q.a(d2 * d2 * d2));
    }

    static q.c l(double d2) {
        double d3 = 1.0 - d2;
        return R.b(P).a(3.0 * d3 * d3).a(S.b(R).a(6.0 * d3 * d2)).a(Q.b(S).a(3.0 * d2 * d2)).b();
    }

    static double m(double d2) {
        return Math.pow(10.0, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(d2, X, Y));
    }

    static q.c a(q.c[] cArray, q.c c2) {
        return cArray[0].a(c2.d()).a(cArray[1].a(c2.e())).a(cArray[2].a(c2.f()));
    }

    static q.c b(q.c[] cArray, q.c c2) {
        return new q.c(c2.c(cArray[0]), c2.c(cArray[1]), c2.c(cArray[2]));
    }

    static a a(int n2, int n3, q.c c2, q.c[] cArray, double d2, q.c c3, q.c c4, q.c c5, double d3) {
        q.c c6 = c4.b(c3).b();
        q.c c7 = c6.d(c5).b();
        q.c c8 = c7.d(c6);
        q.c c9 = c2.a(cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(cArray, c3).a(d2));
        return new a(n2, c9, cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(cArray, c6), cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(cArray, c8), cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(cArray, c7), d3, n3, c2, cArray, d2, c3, c6, c8, c7);
    }

    static a a(int n2, q.c c2, q.c c3, q.c c4, double d2) {
        q.c c5 = c3.b(c2).b();
        q.c c6 = c5.d(c4).b();
        return new a(n2, c2, c5, c6.d(c5), c6, d2, 0, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.aq, Z, 1.0, c2, c5, c6.d(c5), c6);
    }

    static q.c[] n(double d2) {
        q.c c2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.h.b(d2);
        q.c c3 = cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(d2).b();
        return new q.c[]{c3.d(c2).b(), c3, c2};
    }

    static q.c[] b(q.c c2) {
        q.c c3 = T.b(c2.a(T.c(c2)));
        if (c3.a() < 0.001) {
            c3 = F.b(c2.a(F.c(c2)));
        }
        q.c c4 = c3.b();
        return new q.c[]{c4.d(c2).b(), c4, c2};
    }

    static double a(float f2, double d2, double d3) {
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.q.b(((double)f2 - d2) / (d3 - d2));
    }

    public static a f(float f2) {
        if (f2 < 84.0f) {
            double d2 = Math.pow(10.0, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a((double)f2, aa, ab));
            q.c c2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w.a(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w.a(d2)).a(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.D.a(d2 * 0.3));
            double d3 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.b((Math.log10(d2) + 1.3) / 1.9);
            return cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(0, c2, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w.a(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.aq, d3), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.x, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(62.0, 54.0, cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(f2, 62.0, 84.0)));
        }
        if (f2 < 110.0f) {
            return cyou.rimuru.shootingstardemo.mc1211.client.fx.h.g(f2);
        }
        if (f2 < 146.0f) {
            return cyou.rimuru.shootingstardemo.mc1211.client.fx.h.h(f2);
        }
        if (f2 < 168.0f) {
            return cyou.rimuru.shootingstardemo.mc1211.client.fx.h.i(f2);
        }
        if (f2 < 202.0f) {
            return cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(f2, false);
        }
        if (f2 < 226.0f) {
            return cyou.rimuru.shootingstardemo.mc1211.client.fx.h.j(f2);
        }
        if (f2 < 252.0f) {
            return cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(f2, true);
        }
        if (f2 < 256.0f) {
            return cyou.rimuru.shootingstardemo.mc1211.client.fx.h.k(f2);
        }
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.h.l(f2);
    }

    static a g(float f2) {
        double d2 = Math.pow(10.0, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a((double)f2, ae, af));
        q.c c2 = ac.b(E).b();
        q.c c3 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(c2, L, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.b(((double)f2 - 92.0) / 16.0));
        q.c c4 = E.a(c3.a(d2));
        q.c c5 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.G.b(c4).b();
        q.c c6 = E.b(c4).b();
        q.c c7 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(c5, c6, cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(f2, 85.0, 93.0));
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(1, c4, c4.a(c7), F.a(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.x, 0.3).b(), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(56.0, 46.0, cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(f2, 86.0, 110.0)));
    }

    static a h(float f2) {
        q.c c2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(-1.5707963267948966);
        if (f2 < 141.0f) {
            double d2 = Math.pow(10.0, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a((double)f2, ag, ah));
            q.c c3 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(L, L.a(J.a(0.35)).b(), cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(f2, 110.0, 141.0));
            q.c c4 = c3.a(d2);
            q.c c5 = c2.a(0.32 * cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(f2, 118.0, 141.0));
            return cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(2, 1, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.aq, Z, 1.0, c4, c5, F, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(44.0, 52.0, cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(f2, 110.0, 141.0)));
        }
        a a2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.h.h(140.99f);
        double d3 = Math.pow(cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(f2, 141.0, 146.0), 2.2);
        q.c c6 = c2.a(cyou.rimuru.shootingstardemo.mc1211.client.fx.h.b(-1.5707963267948966).a(-0.004)).a(c2.b().a(0.0035)).a(F.a(0.0012));
        q.c c7 = a2.k().a(c6, d3);
        q.c c8 = a2.k().a(a2.l()).a(c2, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.b(cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(f2, 141.0, 144.0)));
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(2, 1, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.aq, Z, 1.0, c7, c8, F.a(c2.b(), d3).b(), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(52.0, 64.0, d3));
    }

    static a i(float f2) {
        double d2;
        q.c c2;
        q.c c3;
        q.c[] cArray = cyou.rimuru.shootingstardemo.mc1211.client.fx.h.n(-1.5707963267948966);
        q.c c4 = cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(-1.5707963267948966);
        double d3 = 1.4303900673713722E-5;
        double d4 = cyou.rimuru.shootingstardemo.mc1211.client.fx.h.j((double)f2) + 0.5;
        if (f2 < 160.0f) {
            double d5 = cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(f2, 146.0, 160.0);
            c3 = new q.c(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.052, 0.046, d5), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.03, 0.024, d5), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.64, 0.6, d5));
            c2 = new q.c(0.0, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(-0.02, -0.012, d5), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(-0.5, -0.2, d5));
            d2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(50.0, 60.0, d5);
        } else {
            double d6 = cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(f2, 160.0, 168.0);
            double d7 = 1.0 - Math.pow(1.0 - d6, 3.0);
            c3 = new q.c(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.046, 0.13, d7), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.024, 0.06, d7), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.6, 0.95, d7));
            c2 = new q.c(0.0, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(-0.012, -0.15, d7), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(-0.2, -0.2, d7));
            d2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(58.0, 60.0, d7);
        }
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(2, 2, c4, cArray, d3, c3, c2, new q.c(0.0, 1.0, 0.0), d2);
    }

    static a a(float f2, boolean bl) {
        double d2;
        q.c c2;
        q.c c3;
        double d3 = cyou.rimuru.shootingstardemo.mc1211.client.fx.h.e((double)f2);
        q.c[] cArray = cyou.rimuru.shootingstardemo.mc1211.client.fx.h.n(d3);
        q.c c4 = cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(d3);
        double d4 = 1.4303900673713722E-5;
        if (!bl) {
            double d5 = cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(f2, 168.0, 202.0);
            c3 = new q.c(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.1, 0.085, d5), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.045, 0.032, d5), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(-0.42, -0.3, d5));
            c2 = new q.c(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.0, 0.01, d5), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(-0.25, -0.4, d5), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.6, 1.2, d5));
            d2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(58.0, 68.0, d5);
        } else {
            double d6 = cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(f2, 226.0, 252.0);
            c3 = new q.c(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.07, 0.05, d6), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.03, 0.022, d6), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(-0.24, -0.16, d6));
            c2 = new q.c(0.0, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(-0.45, -0.6, d6), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(1.2, 2.0, d6));
            d2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(70.0, 94.0, d6 * d6);
        }
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(2, 2, c4, cArray, d4, c3, c2, new q.c(0.0, 1.0, 0.0), d2);
    }

    static a j(float f2) {
        double d2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(f2, 202.0, 226.0);
        q.c c2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(M, M.a(J.a(0.3)).b(), d2);
        q.c c3 = c2.a(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(7.4, 6.6, d2));
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(2, 1, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.aq, Z, 1.0, c3, F.a(-0.05), F, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(46.0, 44.0, d2));
    }

    static a k(float f2) {
        q.c c2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(-1.5707963267948966);
        double d2 = Math.exp((double)(-(f2 - 252.0f)) * 0.9) * (1.0 - Math.exp((double)(-(f2 - 252.0f)) * 5.0));
        q.c c3 = c2.a(J.a(-0.16 - d2 * 0.02)).a(F.a(0.035)).a(I.a(-0.075));
        q.c c4 = c2.a(J.a(0.9));
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(2, 1, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.aq, Z, 1.0, c3, c4, F, 58.0 + d2 * 8.0);
    }

    static a l(float f2) {
        double d2;
        q.c c2;
        q.c c3;
        double d3;
        int n2;
        q.c c4;
        q.c c5;
        if (f2 < 282.0f) {
            double d4 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a((double)f2, V, W);
            c5 = cyou.rimuru.shootingstardemo.mc1211.client.fx.h.k(d4);
            c4 = cyou.rimuru.shootingstardemo.mc1211.client.fx.h.l(d4);
            n2 = 1;
            d3 = 6.6845871222684464E-9;
        } else {
            c5 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w.a(1.0 + cyou.rimuru.shootingstardemo.mc1211.client.fx.h.m((double)f2));
            c4 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w.a(-1.0);
            n2 = 0;
            d3 = 1.569612305760477E-4;
        }
        q.c[] cArray = cyou.rimuru.shootingstardemo.mc1211.client.fx.h.b(c4);
        if (f2 < 262.0f) {
            double d5 = cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(f2, 256.0, 262.0);
            c3 = new q.c(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.42, 0.3, d5), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.16, 0.12, d5), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(1.25, 1.45, d5));
            q.c c6 = cyou.rimuru.shootingstardemo.mc1211.client.fx.h.b(cArray, E.b(c5).a(1.495978707E8)).b();
            q.c c7 = new q.c(0.0, 0.0, -0.25).b(c3).b();
            c2 = c3.a(c7.a(c6, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.45, 0.2, d5)).b());
            d2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(58.0, 46.0, d5);
        } else if (f2 < 265.0f) {
            double d6 = cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(f2, 262.0, 265.0);
            double d7 = Math.atan2(0.3, 1.45) + (Math.PI - Math.atan2(0.3, 1.45)) * d6;
            double d8 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(1.48, 1.3, d6);
            c3 = new q.c(Math.sin(d7) * d8 * (1.0 - 0.96 * d6) + 0.05 * d6, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.12, 0.1, d6), Math.cos(d7) * d8);
            c2 = new q.c(0.0, 0.0, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(-0.2, 0.8, d6));
            d2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(44.0, 58.0, d6);
        } else if (f2 < 286.0f) {
            double d9 = cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(f2, 265.0, 286.0);
            c3 = new q.c(0.05, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.1, 0.07, d9), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(-1.3, -0.95, d9));
            c2 = new q.c(0.0, 0.0, 0.8);
            d2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(58.0, 64.0, d9);
        } else {
            double d10 = cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(f2, 286.0, 300.0);
            c3 = new q.c(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.05, 0.26, d10), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.07, -0.12, d10), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(-0.95, -0.7, d10));
            c2 = c3.a(new q.c(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.0, -0.18, d10), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.0, 1.05, d10), 1.0).b());
            d2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(64.0, 74.0, d10);
        }
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(n2, 3, c5, cArray, d3, c3, c2, new q.c(0.0, 1.0, 0.0), d2);
    }

    public static float m(float f2) {
        if (f2 < 50.0f || f2 >= 300.0f) {
            return 0.0f;
        }
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a((f2 - 50.0f) / 12.0f, 0.0f, 1.0f);
    }

    public static boolean n(float f2) {
        return f2 >= 110.0f && f2 < 300.0f;
    }

    static float o(float f2) {
        return Math.max(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(f2, 86.0f, 92.0f, 104.0f, 110.0f), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(f2, 257.0f, 262.0f, 288.0f, 292.0f));
    }

    static float p(float f2) {
        return 0.82f * cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(f2, 256.0f, 261.0f, 282.0f, 286.0f);
    }

    static float q(float f2) {
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(f2, 266.0f, 268.0f, 275.0f, 277.0f);
    }

    static float r(float f2) {
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.q.m((f2 - 290.0f + 1.0f) / 4.0f);
    }

    public static void a(float f2, float f3, float f4, BiConsumer<String, float[]> biConsumer) {
        q.c c2;
        q.c c3;
        a a2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.h.f(f2);
        double d2 = Math.tan(Math.toRadians(a2.f) * 0.5);
        biConsumer.accept("CamFwd", a2.c.c());
        biConsumer.accept("CamUp", a2.d.c());
        biConsumer.accept("CamRight", a2.e.c());
        switch (a2.a) {
            case 0: {
                c3 = a2.b;
                c2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.G.a(c3.a(4.258750455597227E-5));
                break;
            }
            case 1: {
                c2 = a2.b;
                c3 = c2.b(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.G).a(23481.065876628472);
                break;
            }
            default: {
                c2 = E.a(a2.b.a(4.6732617030490934E-4));
                c3 = c2.b(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.G).a(23481.065876628472);
            }
        }
        q.c c4 = a2.a == 2 ? a2.b : c2.b(E).a(2139.833083491868);
        biConsumer.accept("EarthRel", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.aq.b(c3).c());
        biConsumer.accept("MoonRel", N.b(c3).c());
        biConsumer.accept("EarthX", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.B[0].c());
        biConsumer.accept("EarthY", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.B[1].c());
        biConsumer.accept("EarthZ", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.B[2].c());
        biConsumer.accept("SunDir", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.C.c());
        biConsumer.accept("SiteN", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w.c());
        biConsumer.accept("SunRelS", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.aq.b(c2).c());
        float[] fArray = new float[cyou.rimuru.shootingstardemo.mc1211.client.fx.q.N.length * 4];
        for (int i2 = 0; i2 < cyou.rimuru.shootingstardemo.mc1211.client.fx.q.N.length; ++i2) {
            q.c c5 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.N[i2].b(c2);
            fArray[i2 * 4] = (float)c5.d();
            fArray[i2 * 4 + 1] = (float)c5.e();
            fArray[i2 * 4 + 2] = (float)c5.f();
            fArray[i2 * 4 + 3] = (float)((Double)cyou.rimuru.shootingstardemo.mc1211.client.fx.q.M[i2][2] / 1.495978707E8);
        }
        biConsumer.accept("Planet", fArray);
        biConsumer.accept("Ecliptic", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.H.c());
        biConsumer.accept("SaturnPole", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.K.c());
        biConsumer.accept("GalaxyN", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.O.c());
        biConsumer.accept("ToCentre", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.P.c());
        biConsumer.accept("JupRel", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.aq.b(c4).c());
        biConsumer.accept("JupPole", F.c());
        biConsumer.accept("JupX", ai.c());
        biConsumer.accept("JupZ", aj.c());
        biConsumer.accept("SunJ", G.c());
        double d3 = f2 < 168.0f ? -1.5707963267948966 : (f2 < 252.0f ? cyou.rimuru.shootingstardemo.mc1211.client.fx.h.e((double)f2) : -1.5707963267948966);
        biConsumer.accept("NeedleJ", cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(d3).b().c());
        biConsumer.accept("BreechJ", cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(-1.5707963267948966).b().c());
        q.c c6 = f2 < 110.0f ? E.b(ac).b() : (a2.g == 3 ? cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(a2.i, new q.c(0.0, 0.0, 1.0)) : a2.c);
        biConsumer.accept("Heading", c6.c());
        float f5 = a2.a == 0 ? 1.0f : (a2.a == 1 ? 1.0f : 0.0f);
        float f6 = 1.0f;
        float f7 = a2.g == 2 ? (float)Math.min(cyou.rimuru.shootingstardemo.mc1211.client.fx.h.f((double)f2) * 0.5, 0.35) : 0.0f;
        biConsumer.accept("G0", new float[]{(float)d2, f2 / 20.0f, f4, f3});
        biConsumer.accept("G1", new float[]{f5, f6, a2.a == 2 || c4.a() < 400.0 ? 1.0f : 0.0f, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a((f2 - 50.0f) / 12.0f, 0.0f, 1.0f)});
        biConsumer.accept("G2", new float[]{0.2727f, 0.004650467f, cyou.rimuru.shootingstardemo.mc1211.client.fx.h.o(f2), cyou.rimuru.shootingstardemo.mc1211.client.fx.h.p(f2)});
        biConsumer.accept("G3", new float[]{cyou.rimuru.shootingstardemo.mc1211.client.fx.h.e(f2), f7, cyou.rimuru.shootingstardemo.mc1211.client.fx.h.d(f2), 0.0f});
        biConsumer.accept("G4", new float[]{cyou.rimuru.shootingstardemo.mc1211.client.fx.h.c(f2), a2.a == 0 ? cyou.rimuru.shootingstardemo.mc1211.client.fx.h.r(f2) : 0.0f, 1.22f, a2.a});
        biConsumer.accept("G5", new float[]{cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(f2, 50.0f, 62.0f, 78.0f, 84.0f), 0.0042f, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(f2, 282.0f, 285.0f, 300.0f, 301.0f), f2 >= 282.0f ? (float)cyou.rimuru.shootingstardemo.mc1211.client.fx.h.m((double)f2) : -1.0f});
    }

    public static void a(float f2, float f3, BiConsumer<String, float[]> biConsumer) {
        float[] fArray;
        q.c c2;
        double d2;
        q.c c3;
        float f4;
        q.c c4;
        double d3;
        q.c c5;
        a a2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.h.f(f2);
        double d4 = Math.tan(Math.toRadians(a2.f) * 0.5);
        biConsumer.accept("CamPos", a2.k.c());
        biConsumer.accept("CamFwd", a2.l.c());
        biConsumer.accept("CamUp", a2.m.c());
        biConsumer.accept("CamRight", a2.n.c());
        q.c[] cArray = a2.i;
        q.c c6 = E;
        q.c c7 = cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(a2, a2.h);
        if (a2.g == 1) {
            c5 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.aq;
            d3 = 1.0;
        } else {
            double d5 = cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(a2);
            q.c c8 = c6.b(c7).a(1.495978707E8);
            if (a2.a == 2) {
                c8 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.aq.b(a2.h).a(69911.0);
            }
            c5 = cyou.rimuru.shootingstardemo.mc1211.client.fx.h.b(cArray, c8).a(1.0 / d5);
            d3 = 69911.0 / d5;
        }
        q.c c9 = c4 = a2.a == 0 ? cyou.rimuru.shootingstardemo.mc1211.client.fx.q.C : cyou.rimuru.shootingstardemo.mc1211.client.fx.q.aq.b(c7).b();
        if (a2.a == 2) {
            c4 = G;
        }
        q.c c10 = cyou.rimuru.shootingstardemo.mc1211.client.fx.h.b(cArray, c4);
        float f5 = f4 = a2.g == 2 ? cyou.rimuru.shootingstardemo.mc1211.client.fx.h.c(a2.h) : 1.0f;
        if (a2.a == 0) {
            c3 = cyou.rimuru.shootingstardemo.mc1211.client.fx.h.b(cArray, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.aq.b(a2.h)).a(6371.0);
            d2 = 6371.0;
        } else {
            c2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.G.b(c7).a(1.495978707E8);
            c3 = a2.g == 1 ? cyou.rimuru.shootingstardemo.mc1211.client.fx.h.b(cArray, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.G.b(c7).a(2139.833083491868)) : cyou.rimuru.shootingstardemo.mc1211.client.fx.h.b(cArray, c2).a(1.0 / cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(a2));
            d2 = a2.g == 1 ? 0.09113015119223013 : 6371.0 / cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(a2);
        }
        biConsumer.accept("KeyDir", c10.b().c());
        biConsumer.accept("KeyCol", new float[]{1.0f, 0.95f, 0.86f, 3.2f * f4 * (a2.a == 0 ? 1.0f : 0.55f)});
        c2 = a2.a == 0 ? c3.b() : (c5.a() > 1.0E-9 ? c5.b() : new q.c(0.0, -1.0, 0.0));
        if (a2.a == 0) {
            float[] fArray2 = new float[4];
            fArray2[0] = 0.45f;
            fArray2[1] = 0.62f;
            fArray2[2] = 1.0f;
            fArray = fArray2;
            fArray2[3] = 0.5f;
        } else {
            float[] fArray3 = new float[4];
            fArray3[0] = 0.95f;
            fArray3[1] = 0.68f;
            fArray3[2] = 0.44f;
            fArray = fArray3;
            fArray3[3] = a2.g == 3 && f2 >= 262.0f ? 0.08f : 0.55f * (0.3f + 0.7f * f4);
        }
        float[] fArray4 = fArray;
        biConsumer.accept("FillDir", c2.c());
        biConsumer.accept("FillCol", fArray4);
        biConsumer.accept("JupC", new float[]{(float)c5.d(), (float)c5.e(), (float)c5.f(), (float)d3});
        biConsumer.accept("EarthC", new float[]{(float)c3.d(), (float)c3.e(), (float)c3.f(), (float)d2});
        biConsumer.accept("RingU", J.c());
        biConsumer.accept("RingV", I.c());
        biConsumer.accept("RingN", F.c());
        double d6 = cyou.rimuru.shootingstardemo.mc1211.client.fx.h.h((double)f2);
        double d7 = d6 % 0.8;
        double d8 = Math.floor(d6 / 0.8) % 4096.0;
        float f6 = (float)cyou.rimuru.shootingstardemo.mc1211.client.fx.h.d((double)f2);
        double d9 = cyou.rimuru.shootingstardemo.mc1211.client.fx.h.e((double)f2);
        double d10 = Math.min(cyou.rimuru.shootingstardemo.mc1211.client.fx.h.f((double)f2) * 6.0 + 0.05, 5.026548245743669);
        float f7 = f2 >= 160.0f ? (f2 - 160.0f) / 20.0f : -1.0f;
        float f8 = (float)(cyou.rimuru.shootingstardemo.mc1211.client.fx.h.i((double)f2) * 0.5);
        biConsumer.accept("N0", new float[]{(float)d4, f2 / 20.0f, f3, a2.g});
        biConsumer.accept("N1", new float[]{1.22f, 0.0042f, -1.5707964f, (float)(d9 % (Math.PI * 2))});
        biConsumer.accept("N2", new float[]{cyou.rimuru.shootingstardemo.mc1211.client.fx.h.b(f2), cyou.rimuru.shootingstardemo.mc1211.client.fx.h.c(f2), cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(f2), f6});
        biConsumer.accept("N3", new float[]{cyou.rimuru.shootingstardemo.mc1211.client.fx.h.e(f2), f7, (float)d10, f4});
        biConsumer.accept("N4", new float[]{(float)d7, (float)d8, f8, (float)(a2.g == 2 && f2 < 168.0f ? cyou.rimuru.shootingstardemo.mc1211.client.fx.h.j((double)f2) : 0.0)});
        biConsumer.accept("N5", new float[]{cyou.rimuru.shootingstardemo.mc1211.client.fx.h.q(f2), (float)((double)(f2 - 266.0f) * 40.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.h.r(f2), f2 >= 290.0f ? (f2 - 290.0f) / 20.0f : -1.0f});
        biConsumer.accept("N6", new float[]{0.8f, f2 >= 168.0f && f2 < 252.0f ? 1.0f : 0.0f, f2 >= 146.0f && f2 < 162.0f ? 1.0f : 0.0f, cyou.rimuru.shootingstardemo.mc1211.client.fx.h.p(f2)});
    }

    static q.c a(a a2, q.c c2) {
        return switch (a2.a) {
            case 0 -> cyou.rimuru.shootingstardemo.mc1211.client.fx.q.G.a(c2.a(4.258750455597227E-5));
            case 1 -> c2;
            default -> E.a(c2.a(4.6732617030490934E-4));
        };
    }

    static double a(a a2) {
        return switch (a2.a) {
            case 0 -> a2.j * 6371.0;
            case 1 -> a2.j * 1.495978707E8;
            default -> a2.j * 69911.0;
        };
    }

    static float c(q.c c2) {
        double d2 = c2.c(G);
        if (d2 > 0.0) {
            return 1.0f;
        }
        double d3 = c2.b(G.a(d2)).a();
        return (float)cyou.rimuru.shootingstardemo.mc1211.client.fx.q.b((d3 - 0.97) / 0.08);
    }

    public static List<b> s(float f2) {
        a a2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.h.f(f2);
        ArrayList<b> arrayList = new ArrayList<b>();
        if (a2.a == 0 && f2 < 84.0f) {
            if (f2 < 72.0f) {
                arrayList.add(new b("TARGET", "KINETIC LOCK", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w.b(a2.b)));
            }
            if (f2 >= 70.0f) {
                arrayList.add(new b("EARTH", "6,371 KM", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.aq.b(a2.b)));
                arrayList.add(new b("MOON", "384,400 KM", N.b(a2.b)));
            }
        }
        if (a2.a == 1 && f2 < 110.0f) {
            arrayList.add(new b("SOL", "G2V", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.aq.b(a2.b)));
            arrayList.add(new b("EARTH", "SOL-3 \u00b7 TARGET", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.G.b(a2.b)));
            arrayList.add(new b("JUPITER", "5.2 AU \u00b7 SS-03", E.b(a2.b)));
        }
        if (a2.g == 1 && f2 < 143.0f) {
            arrayList.add(new b("JUPITER", "71,492 KM \u00b7 318 M\u2295", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.aq.b(a2.b)));
            if (f2 >= 120.0f) {
                arrayList.add(new b("BREECH", "SS-03 MASS DRIVER", cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(-1.5707963267948966).b(a2.b)));
            }
        }
        if (a2.g == 3 && f2 < 282.0f && f2 >= 265.0f) {
            arrayList.add(new b("EARTH", "TARGET", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.G.b(cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(a2, a2.b))));
        }
        if (a2.g == 3 && f2 >= 282.0f && f2 < 286.0f) {
            arrayList.add(new b("MOON", "PASS 12,100 KM", N.b(a2.b)));
        }
        return arrayList;
    }

    public static double t(float f2) {
        a a2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.h.f(f2);
        q.c c2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(a2, a2.b);
        if (a2.a == 0) {
            return a2.b.b(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w).a() * 6371.0;
        }
        return c2.b(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.G.a(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w.a(4.258750455597227E-5))).a() * 1.495978707E8;
    }

    static {
        U.putAll(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.ab);
        U.remove("MilkyTop");
        U.put("Jupiter", "shooting_star/jupiter.jpg");
        V = new double[]{256.0, 258.0, 262.0, 268.0, 276.0, 282.0};
        W = new double[]{0.0, 2.0E-5, 0.004, 0.2, 0.93, 1.0};
        X = new double[]{282.0, 283.5, 284.5, 286.0, 287.5, 289.0, 290.0, 295.0, 300.0};
        Y = new double[]{cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(300.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(110.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(68.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(54.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(10.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.6), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.0175), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.011), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.0075)};
        Z = new q.c[]{new q.c(1.0, 0.0, 0.0), new q.c(0.0, 1.0, 0.0), new q.c(0.0, 0.0, 1.0)};
        aa = new double[]{50.0, 62.0, 68.0, 74.0, 80.0, 84.0};
        ab = new double[]{cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.0016), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.012), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.4), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(2.4), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(16.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(60.0)};
        ac = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.G.a(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w.a(61.0).a(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.D.a(18.0)).a(4.258750455597227E-5));
        ad = ac.b(E).a();
        ae = new double[]{84.0, 88.0, 94.0, 100.0, 106.0, 110.0};
        af = new double[]{cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(ad), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(ad * 0.96), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(2.5), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.4), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.02), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.008411871065488368)};
        ag = new double[]{110.0, 116.0, 124.0, 136.0, 141.0};
        ah = new double[]{cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(14.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(6.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(3.9), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(3.3), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(3.1)};
        ai = J;
        aj = I.a(-1.0);
    }

    public static final class a
{
        private final int a;
        private final q.c b;
        private final q.c c;
        private final q.c d;
        private final q.c e;
        private final double f;
        private final int g;
        private final q.c h;
        private final q.c[] i;
        private final double j;
        private final q.c k;
        private final q.c l;
        private final q.c m;
        private final q.c n;

        public a(int frame, q.c pos, q.c fwd, q.c up, q.c right, double fov, int mode, q.c origin, q.c[] basis, double unit, q.c camL, q.c fwdL, q.c upL, q.c rightL) {
            this.a = frame;
            this.b = pos;
            this.c = fwd;
            this.d = up;
            this.e = right;
            this.f = fov;
            this.g = mode;
            this.h = origin;
            this.i = basis;
            this.j = unit;
            this.k = camL;
            this.l = fwdL;
            this.m = upL;
            this.n = rightL;
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

        public int a() {
            return this.a;
        }

        public q.c b() {
            return this.b;
        }

        public q.c c() {
            return this.c;
        }

        public q.c d() {
            return this.d;
        }

        public q.c e() {
            return this.e;
        }

        public double f() {
            return this.f;
        }

        public int g() {
            return this.g;
        }

        public q.c h() {
            return this.h;
        }

        public q.c[] i() {
            return this.i;
        }

        public double j() {
            return this.j;
        }

        public q.c k() {
            return this.k;
        }

        public q.c l() {
            return this.l;
        }

        public q.c m() {
            return this.m;
        }

        public q.c n() {
            return this.n;
        }
    }

    public static final class b
{
        private final String a;
        private final String b;
        private final q.c c;

        public b(String name, String detail, q.c rel) {
            this.a = name;
            this.b = detail;
            this.c = rel;
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

        public String a() {
            return this.a;
        }

        public String b() {
            return this.b;
        }

        public q.c c() {
            return this.c;
        }
    }
}

