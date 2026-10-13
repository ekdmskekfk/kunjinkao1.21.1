/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.fx;

import cyou.rimuru.shootingstardemo.mc1211.client.fx.q;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.t;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public final class w {
    public static final float a = 9.0f;
    public static final float b = 36.0f;
    public static final float c = 40.0f;
    public static final float d = 52.0f;
    static final float e = 66.0f;
    static final float f = 78.0f;
    static final float g = 96.0f;
    public static final float h = 118.0f;
    public static final float i = 130.0f;
    public static final float j = 150.0f;
    public static final float k = 210.0f;
    public static final float l = 248.0f;
    public static final float m = 262.0f;
    public static final float n = 278.0f;
    public static final float o = 288.0f;
    public static final float p = 290.0f;
    public static final float q = 336.0f;
    public static final float r = 358.0f;
    public static final float s = 398.0f;
    public static final float t = 426.0f;
    static final double u = 9.4607E18;
    public static final double v = 6371000.0;
    static final double w = 2.2;
    static final double x = 3.2;
    static final q.c y = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.O;
    static final q.c z = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w.a(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.D.a(0.3)).b();
    static final q.c A = cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(z, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.H, 55.0);
    static final q.c B = A.b(y.a(A.c(y))).b();
    static final q.c C = B.d(y);
    static final q.c D = cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(A, y, 35.0);
    public static final double E = 10.0;
    static final double F = 8.0;
    static final double G = 26.0;
    static final double H = 8.0;
    static final double I = 9.0;
    static final double J = 34.0;
    public static final q.c K = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w.a(7.5);
    static final double L = 2.2;
    static final double M = 0.24444444444444446;
    static final double N = 3.2;
    static final double O = 1.6;
    static final q.c P = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.D;
    static final q.c Q = P.d(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w);
    static final q.c R = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.D.a(5.4).a(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.x.a(2.4)).b();
    static final double S = 3.4;
    static final double T = 0.5;
    static final double[] U = new double[]{40.0, 52.0, 58.0, 62.0, 66.0, 72.0, 78.0, 82.0, 88.0, 94.0, 100.0, 108.0, 114.0, 120.0, 126.0, 130.0};
    static final double[] V = new double[]{cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(12.742), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(127.42), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(6371.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(38226.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(509680.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(5.983914828E8), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(2.9919574139999996E10), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(4.73035E15), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(7.56856E17), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(1.702926E18), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(7.56856E18), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(3.78428E19), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(8.51463E19), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(1.513712E20), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(2.1286575E20), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(2.365175E20)};
    static final double[] W = new double[]{126.0, 130.0, 140.0, 150.0, 160.0, 175.0, 190.0, 205.0, 220.0, 235.0, 250.0, 262.0, 272.0, 280.0, 286.0, 290.0};
    static final q.c[] X;
    static final q.c[] Y;
    static final double[] Z;
    static final double[] aa;
    static final double ab = 27.0;
    static final double[] ac;
    static final q.c[] ad;
    static final q.c[] ae;
    static final double[] af;
    static final double[] ag;

    private w() {
    }

    private static q.c a(q.c c2, q.c c3, double d2) {
        q.c c4 = c2.b(c3.a(c2.c(c3))).b();
        double d3 = Math.toRadians(d2);
        return c3.a(Math.sin(d3)).a(c4.a(Math.cos(d3))).b();
    }

    static q.c a(double d2, double d3, double d4) {
        return B.a(d2).a(y.a(d3)).a(C.a(d4));
    }

    public static double[] a(int n2, double[] dArray) {
        double d2 = (n2 & 1) == 0 ? -1.0 : 1.0;
        double d3 = (n2 & 2) == 0 ? -1.0 : 1.0;
        double d4 = (n2 & 4) == 0 ? -1.0 : 1.0;
        double d5 = (n2 & 8) == 0 ? -1.0 : 1.0;
        double d6 = Math.cos(dArray[0]);
        double d7 = Math.sin(dArray[0]);
        double d8 = d2 * d6 - d5 * d7;
        double d9 = d2 * d7 + d5 * d6;
        d6 = Math.cos(dArray[1]);
        d7 = Math.sin(dArray[1]);
        double d10 = d3 * d6 - d9 * d7;
        double d11 = d3 * d7 + d9 * d6;
        d6 = Math.cos(dArray[2]);
        d7 = Math.sin(dArray[2]);
        double d12 = d4 * d6 - d11 * d7;
        double d13 = d4 * d7 + d11 * d6;
        double d14 = 3.2 / (3.2 - d13);
        double d15 = d8 * d14 * 2.2 * 0.5;
        double d16 = d10 * d14 * 2.2 * 0.5;
        double d17 = d12 * d14 * 2.2 * 0.5;
        d6 = Math.cos(dArray[3]);
        d7 = Math.sin(dArray[3]);
        return new double[]{d15 * d6 - d17 * d7, d16, d15 * d7 + d17 * d6, d13};
    }

    static a a(int n2, q.c c2, q.c c3, q.c c4, double d2) {
        q.c c5 = c3.b(c2).b();
        q.c c6 = c5.d(c4).b();
        return new a(n2, c2, c5, c6.d(c5), c6, d2);
    }

    static double a(float f2, double d2, double d3) {
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.q.b(((double)f2 - d2) / (d3 - d2));
    }

    static q.c a(double d2, double[] dArray, q.c[] cArray) {
        int n2 = dArray.length;
        if (d2 <= dArray[0]) {
            return cArray[0];
        }
        if (d2 >= dArray[n2 - 1]) {
            return cArray[n2 - 1];
        }
        int n3 = 0;
        while (d2 > dArray[n3 + 1]) {
            ++n3;
        }
        double d3 = dArray[n3 + 1] - dArray[n3];
        double d4 = (d2 - dArray[n3]) / d3;
        q.c c2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(n3, dArray, cArray).a(d3);
        q.c c3 = cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(n3 + 1, dArray, cArray).a(d3);
        double d5 = d4 * d4;
        double d6 = d5 * d4;
        return cArray[n3].a(2.0 * d6 - 3.0 * d5 + 1.0).a(c2.a(d6 - 2.0 * d5 + d4)).a(cArray[n3 + 1].a(-2.0 * d6 + 3.0 * d5)).a(c3.a(d6 - d5));
    }

    private static q.c a(int n2, double[] dArray, q.c[] cArray) {
        int n3 = dArray.length;
        if (n2 == 0) {
            return cArray[1].b(cArray[0]).a(1.0 / (dArray[1] - dArray[0]));
        }
        if (n2 == n3 - 1) {
            return cArray[n3 - 1].b(cArray[n3 - 2]).a(1.0 / (dArray[n3 - 1] - dArray[n3 - 2]));
        }
        return cArray[n2 + 1].b(cArray[n2 - 1]).a(1.0 / (dArray[n2 + 1] - dArray[n2 - 1]));
    }

    public static a a(float f2) {
        if (f2 < 130.0f) {
            return cyou.rimuru.shootingstardemo.mc1211.client.fx.w.b(f2);
        }
        if (f2 < 290.0f) {
            double d2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(f2, 266.0, 282.0);
            q.c c2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(y, B.a(-1.0).a(C).b(), d2);
            double d3 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(58.0, 54.0, cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(f2, 130.0, 170.0)), 66.0, cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(f2, 200.0, 230.0)) + 8.0 * d2;
            return cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(3, cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a((double)f2, W, X), cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a((double)f2, W, Y), c2, d3);
        }
        if (f2 < 336.0f) {
            q.c c3 = cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(y, B.a(-1.0).a(C).b(), 1.0));
            q.c c4 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(c3, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w, cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(f2, 292.0, 306.0));
            double d4 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(74.0, 56.0, cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(f2, 290.0, 310.0)), 70.0, cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(f2, 310.0, 336.0));
            return cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(0, cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a((double)f2, ac, ad), cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a((double)f2, ac, ae), c4, d4);
        }
        double d5 = Math.pow(10.0, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a((double)f2, af, ag));
        q.c c5 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w.a(2.6).a(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w, cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(f2, 336.0, 346.0));
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(0, cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(d5), c5, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(70.0, 74.0, cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(f2, 336.0, 358.0)));
    }

    static q.c a(double d2) {
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w.a(1.0 + d2).a(R.a(3.4 * Math.pow(d2 / 0.5, 1.3)));
    }

    static a b(float f2) {
        double d2 = Math.pow(10.0, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a((double)f2, U, V));
        double d3 = Math.log10(d2);
        q.c c2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(z, A, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.b((d3 - 5.5) / 4.0)), D, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.b((d3 - 12.5) / 5.0));
        double d4 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.b((d3 - 8.5) / 1.5);
        double d5 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.b((d3 - 17.0) / 1.5);
        double d6 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.b((d3 - 16.5) / 1.5);
        q.c c3 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.x.a(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.H, d4).a(y, d5).b();
        double d7 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(62.0, 58.0, cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(f2, 52.0, 78.0));
        if (f2 < 66.0f) {
            double d8 = d2 / 6371.0;
            double d9 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.b((Math.log10(d8) + 1.3) / 1.9);
            return cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(0, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w.a(c2.a(d8)), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w.a(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.aq, d9), c3, d7);
        }
        q.c c4 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.G.a(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w.a(4.258750455597227E-5));
        if (f2 < 78.0f) {
            q.c c5 = c4.a(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.aq, d4);
            return cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(1, c5.a(c2.a(d2 / 1.495978707E8)), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.G.a(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.aq, d4), c3, d7);
        }
        q.c c6 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.Q.a(c4.a(1.5812558341348946E-8));
        q.c c7 = c6.a(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.Q, d4).a(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.aq, d5);
        q.c c8 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.Q.a(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.aq, d6);
        if (f2 < 96.0f) {
            return cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(2, c7.a(c2.a(d2 / 9.4607E15)), c8, c3, d7);
        }
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(3, c7.a(0.001).a(c2.a(d2 / 9.4607E18)), c8.a(0.001), c3, d7);
    }

    public static double[] c(float f2) {
        double d2 = 9.0 * cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(f2, 210.0, 250.0);
        double d3 = Math.max(34.0 * cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(f2, 206.0, 246.0), d2 + 0.001);
        double d4 = 2.0 * Math.sqrt(Math.max(d2 * (d3 - d2), 0.0));
        return new double[]{d2, d3, d4, cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(f2, 248.0, 258.0)};
    }

    static q.c d(float f2) {
        return y.a(-cyou.rimuru.shootingstardemo.mc1211.client.fx.w.c(f2)[2]);
    }

    static q.c a(q.c c2, float f2) {
        q.c c3 = c2.b(cyou.rimuru.shootingstardemo.mc1211.client.fx.w.d(f2));
        return K.a(P.a(c3.c(B)).a(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w.a(c3.c(y))).a(Q.a(c3.c(C))).a(0.24444444444444446));
    }

    static q.c a(q.c c2) {
        return P.a(c2.c(B)).a(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w.a(c2.c(y))).a(Q.a(c2.c(C)));
    }

    static q.c b(q.c c2, float f2) {
        q.c c3 = c2.b(K).a(4.090909090909091);
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.w.d(f2).a(B.a(c3.c(P)).a(y.a(c3.c(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w))).a(C.a(c3.c(Q))));
    }

    static double[] a(float f2, float f3) {
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(f2, f3, 1.0);
    }

    static double[] a(float f2, float f3, double d2) {
        int n2 = Z.length;
        double d3 = (aa[n2 - 2] - aa[n2 - 1]) / (Z[n2 - 1] - Z[n2 - 2]) * 0.24444444444444446;
        double d4 = K.a() - 1.0;
        double d5 = Math.max(0.0, Math.min(1.0, (double)(f2 - f3) / 27.0));
        double d6 = d5 * d5;
        double d7 = d6 * d5;
        double d8 = d4 - ((d7 - 2.0 * d6 + d5) * d3 * 27.0 + (-2.0 * d7 + 3.0 * d6) * (d4 - 3.2));
        double d9 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(cyou.rimuru.shootingstardemo.mc1211.client.fx.w.e(278.0f) * 0.24444444444444446, 1.6, cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(f2, (double)f3, (double)(f3 + 30.0f)));
        if (f2 >= 398.0f) {
            double d10 = Math.max(0.0, Math.min(1.0, (double)((f2 - 398.0f) / 28.0f)));
            d10 *= d10;
            double d11 = 128.0 * d2 / 6371000.0;
            d8 = Math.pow(10.0, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(Math.log10(3.2), Math.log10(d11), d10));
            d9 = Math.pow(10.0, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(Math.log10(1.6), Math.log10(d11), d10));
        }
        return new double[]{d8, d9};
    }

    static double e(float f2) {
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(8.0, 4.0, cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(f2, 250.0, 266.0));
    }

    static double[] a(float f2, a a2) {
        if (f2 < 130.0f || f2 >= 358.0f) {
            return null;
        }
        if (a2.a() == 3) {
            double d2;
            q.c c2;
            if (f2 < 278.0f) {
                c2 = y.a(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a((double)f2, Z, aa));
                d2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.w.e(f2);
            } else {
                double[] dArray = cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(f2, 278.0f);
                c2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.w.b(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w.a(1.0 + dArray[0]), f2);
                d2 = dArray[1] / 0.24444444444444446;
            }
            return new double[]{c2.d(), c2.e(), c2.f(), d2};
        }
        if (a2.a() == 0 && f2 >= 278.0f) {
            double[] dArray = cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(f2, 278.0f);
            q.c c3 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w.a(1.0 + dArray[0]);
            return new double[]{c3.d(), c3.e(), c3.f(), dArray[1]};
        }
        return null;
    }

    public static double f(float f2) {
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.w.b(f2, 278.0f);
    }

    static double b(float f2, float f3) {
        return 2.2 * cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(f2, (double)(f3 - 4.0f), (double)(f3 + 6.0f)) * (1.0 - cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(f2, (double)(f3 + 22.0f), (double)(f3 + 50.0f)));
    }

    public static double[] g(float f2) {
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(f2, 1.0);
    }

    public static double[] a(float f2, double d2) {
        double[] dArray = cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(f2, 288.0f, d2);
        boolean bl = f2 >= 286.0f && f2 < 426.0f;
        return new double[]{dArray[0] * 6371000.0, dArray[1] * 6371000.0, bl ? 1.0 : 0.0, (K.a() - 1.0) * 6371000.0, cyou.rimuru.shootingstardemo.mc1211.client.fx.w.b(f2, 288.0f) * 6371000.0};
    }

    public static float h(float f2) {
        if (f2 < 40.0f || f2 >= 358.0f) {
            return 0.0f;
        }
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a((f2 - 40.0f) / 12.0f, 0.0f, 1.0f);
    }

    static float i(float f2) {
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(f2, 204.0f, 214.0f, 258.0f, 268.0f);
    }

    public static void a(float f2, float f3, float f4, BiConsumer<String, float[]> biConsumer) {
        Object object;
        q.c c2;
        q.c c3;
        q.c c4;
        q.c c5;
        q.c c6;
        a a2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(f2);
        double d2 = Math.tan(Math.toRadians(a2.f()) * 0.5);
        biConsumer.accept("CamFwd", a2.c().c());
        biConsumer.accept("CamUp", a2.d().c());
        biConsumer.accept("CamRight", a2.e().c());
        switch (a2.a()) {
            case 0: {
                c6 = a2.b();
                c5 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.G.a(c6.a(4.258750455597227E-5));
                c4 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.Q.a(c5.a(1.5812558341348946E-8));
                break;
            }
            case 1: {
                c5 = a2.b();
                c6 = c5.b(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.G).a(23481.065876628472);
                c4 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.Q.a(c5.a(1.5812558341348946E-8));
                break;
            }
            case 2: {
                c4 = a2.b();
                c5 = c4.b(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.Q).a(6.3240873387645096E7);
                c6 = c5.b(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.G).a(23481.065876628472);
                break;
            }
            default: {
                c4 = a2.b().a(1000.0);
                c5 = c4.b(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.Q).a(6.3240873387645096E7);
                c6 = c5.b(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.G).a(23481.065876628472);
            }
        }
        q.c c7 = c4.a(0.001);
        biConsumer.accept("EarthRel", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.aq.b(c6).c());
        biConsumer.accept("MoonRel", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.E.b(c6).c());
        biConsumer.accept("EarthX", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.B[0].c());
        biConsumer.accept("EarthY", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.B[1].c());
        biConsumer.accept("EarthZ", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.B[2].c());
        biConsumer.accept("SunDir", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.C.c());
        biConsumer.accept("SiteN", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w.c());
        biConsumer.accept("SunRelS", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.aq.b(c5).c());
        float[] fArray = new float[cyou.rimuru.shootingstardemo.mc1211.client.fx.q.N.length * 4];
        for (int i2 = 0; i2 < cyou.rimuru.shootingstardemo.mc1211.client.fx.q.N.length; ++i2) {
            c3 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.N[i2].b(c5);
            fArray[i2 * 4] = (float)c3.d();
            fArray[i2 * 4 + 1] = (float)c3.e();
            fArray[i2 * 4 + 2] = (float)c3.f();
            fArray[i2 * 4 + 3] = (float)((Double)cyou.rimuru.shootingstardemo.mc1211.client.fx.q.M[i2][2] / 1.495978707E8);
        }
        biConsumer.accept("Planet", fArray);
        biConsumer.accept("Ecliptic", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.H.c());
        biConsumer.accept("SaturnPole", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.K.c());
        biConsumer.accept("GalaxyN", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.O.c());
        biConsumer.accept("ToCentre", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.P.c());
        biConsumer.accept("CentreRel", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.aq.b(c4).c());
        biConsumer.accept("SunRelG", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.Q.b(c4).c());
        biConsumer.accept("FloorRel", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.aq.b(c7).c());
        biConsumer.accept("BulkX", B.c());
        biConsumer.accept("BulkZ", C.c());
        biConsumer.accept("MapX", P.c());
        biConsumer.accept("MapY", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w.c());
        biConsumer.accept("MapZ", Q.c());
        biConsumer.accept("MouthE", K.c());
        double[] dArray = cyou.rimuru.shootingstardemo.mc1211.client.fx.w.c(f2);
        c3 = a2.a() == 0 ? K.b(c6) : cyou.rimuru.shootingstardemo.mc1211.client.fx.w.d(f2).b(c7);
        biConsumer.accept("MouthRel", c3.c());
        double[] dArray2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(f2, a2);
        q.c c8 = a2.a() == 0 ? P : B;
        q.c c9 = a2.a() == 0 ? cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w : y;
        q.c c10 = c2 = a2.a() == 0 ? Q : C;
        if (dArray2 != null) {
            object = new q.c(dArray2[0], dArray2[1], dArray2[2]).b(a2.a() == 0 ? c6 : c7);
            biConsumer.accept("EngC", new float[]{(float)((q.c)object).d(), (float)((q.c)object).e(), (float)((q.c)object).f(), (float)dArray2[3]});
        } else {
            biConsumer.accept("EngC", new float[]{0.0f, 0.0f, 0.0f, 1.0f});
        }
        biConsumer.accept("EngX", c8.c());
        biConsumer.accept("EngY", c9.c());
        biConsumer.accept("EngZ", c2.c());
        object = new float[192];
        cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(f2, (float[])object);
        biConsumer.accept("Eng", (float[])object);
        float f5 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.m((f2 - 102.0f) / 14.0f);
        float f6 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(f2, 326.0f, 340.0f, 368.0f, 378.0f);
        float f7 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(f2, 268.0f, 276.0f, 292.0f, 298.0f);
        float f8 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(f2, 62.0f, 66.0f, 102.0f, 118.0f);
        biConsumer.accept("T0", new float[]{(float)d2, f2 / 20.0f, f4, f3});
        biConsumer.accept("T1", new float[]{a2.a(), a2.a() <= 1 ? 1.0f : 0.0f, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a((f2 - 40.0f) / 12.0f, 0.0f, 1.0f), 0.2727f});
        biConsumer.accept("T2", new float[]{0.004650467f, f5, 10.0f, f6});
        biConsumer.accept("T3", new float[]{(float)dArray[0], (float)dArray[1], (float)dArray[2], (float)dArray[3]});
        biConsumer.accept("T4", new float[]{0.24444444f, (float)cyou.rimuru.shootingstardemo.mc1211.client.fx.w.f(f2), dArray2 != null ? 1.0f : 0.0f, cyou.rimuru.shootingstardemo.mc1211.client.fx.w.i(f2)});
        biConsumer.accept("T5", new float[]{f7, f8, 8.0f, f2 >= 130.0f ? 1.0f : 0.0f});
        biConsumer.accept("T6", new float[]{cyou.rimuru.shootingstardemo.mc1211.client.fx.q.m((f2 - 118.0f - 2.0f) / 14.0f), 0.0f, 0.0f, 0.0f});
    }

    public static List<b> j(float f2) {
        a a2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(f2);
        ArrayList<b> arrayList = new ArrayList<b>();
        switch (a2.a()) {
            case 1: {
                arrayList.add(new b("SOL", "G2V", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.aq.b(a2.b())));
                arrayList.add(new b("SOL-3", "TARGET", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.G.b(a2.b())));
                break;
            }
            case 2: {
                arrayList.add(new b("SOL", "26 KLY FROM THE CORE", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.Q.b(a2.b())));
                arrayList.add(new b("MILKY WAY", "100 KLY ACROSS \u00b7 400 BN STARS", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.aq.b(a2.b())));
                break;
            }
            case 3: {
                if (f2 < 118.0f) {
                    arrayList.add(new b("MILKY WAY", "SOL-3 INSIDE", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.aq.b(a2.b())));
                    break;
                }
                if (!(f2 >= 210.0f) || !(f2 < 262.0f)) break;
                arrayList.add(new b("SOL-3", f2 < 248.0f ? "BEHIND THE FOLD" : "THROAT OPEN", cyou.rimuru.shootingstardemo.mc1211.client.fx.w.d(f2).b(a2.b())));
                break;
            }
        }
        return arrayList;
    }

    static double[] a(float f2, a a2, double d2, double d3, double d4) {
        double[] dArray = cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(f2, a2);
        if (dArray == null) {
            return null;
        }
        q.c c2 = a2.a() == 0 ? P : B;
        q.c c3 = a2.a() == 0 ? cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w : y;
        q.c c4 = a2.a() == 0 ? Q : C;
        q.c c5 = new q.c(dArray[0], dArray[1], dArray[2]).a(c2.a(d2 * dArray[3])).a(c3.a(d3 * dArray[3])).a(c4.a(d4 * dArray[3])).b(a2.b());
        return new double[]{c5.d(), c5.e(), c5.f(), dArray[3]};
    }

    public static double k(float f2) {
        a a2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(f2);
        return switch (a2.a()) {
            case 0 -> a2.b().a() * 6371.0;
            case 1 -> a2.b().b(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.G).a() * 1.495978707E8;
            case 2 -> a2.b().b(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.Q).a() * 9.4607E15;
            default -> f2 < 118.0f ? a2.b().b(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.Q.a(0.001)).a() * 9.4607E18 : -1.0;
        };
    }

    static {
        Z = new double[]{130.0, 212.0, 236.0, 252.0, 262.0, 270.0, 278.0};
        aa = new double[]{26.0, 26.0, 15.0, 10.0, 2.0, -18.0, -30.0};
        ac = new double[]{286.0, 290.0, 298.0, 308.0, 320.0, 336.0};
        af = new double[]{336.0, 342.0, 347.0, 351.0, 355.0, 358.0};
        ag = new double[]{cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.5), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.2), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.05), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.008), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(8.0E-4), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(1.0E-4)};
        double d2 = Math.toRadians(35.0);
        double d3 = Math.pow(10.0, V[14]) / 9.4607E18;
        double d4 = Math.pow(10.0, V[15]) / 9.4607E18;
        X = new q.c[]{cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(d3 * Math.cos(d2), d3 * Math.sin(d2), 0.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(d4 * Math.cos(d2), d4 * Math.sin(d2), 0.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(26.0, 18.0, 2.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(26.5, 21.0, 5.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(24.5, 23.0, 10.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(18.0, 24.5, 16.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(8.0, 26.5, 20.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(-3.0, 30.0, 20.5), cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(-18.0, 40.0, 26.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(-30.0, 48.0, 32.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(-32.0, 50.0, 33.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(-20.0, 38.0, 22.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(-6.0, 16.0, 7.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(-1.5, 0.0, 1.5), cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(-0.3, -16.0, 0.3), cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(0.0, -30.0, 0.0)};
        Y = new q.c[]{cyou.rimuru.shootingstardemo.mc1211.client.fx.q.aq, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.aq, cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(0.0, 1.0, 0.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(0.0, 16.0, 0.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(0.0, 24.0, 0.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(0.0, 26.0, 0.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(0.0, 26.0, 0.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(0.0, 24.0, 0.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(0.0, 6.0, 0.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(0.0, -6.0, 0.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(0.0, -10.0, 0.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(0.0, -8.0, 0.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(0.0, -22.0, 0.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(0.0, -40.0, 0.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(0.0, -60.0, 0.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(0.0, -70.0, 0.0)};
        ad = new q.c[]{cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(X[14], 286.0f), K, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w.a(6.4).a(R.a(3.2)), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w.a(4.4).a(R.a(5.2)), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w.a(2.4).a(R.a(4.6)), cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(0.5)};
        ae = new q.c[]{cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(Y[14], 286.0f), cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(Y[15], 290.0f), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w.a(4.4), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w.a(3.8), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w.a(3.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w.a(2.6)};
    }

    public static final class a
{
        private final int a;
        private final q.c b;
        private final q.c c;
        private final q.c d;
        private final q.c e;
        private final double f;

        public a(int frame, q.c pos, q.c fwd, q.c up, q.c right, double fov) {
            this.a = frame;
            this.b = pos;
            this.c = fwd;
            this.d = up;
            this.e = right;
            this.f = fov;
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

