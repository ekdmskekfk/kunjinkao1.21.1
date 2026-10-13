/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.fx;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public final class q {
    public static final float a = 58.0f;
    public static final float b = 72.0f;
    public static final float c = 126.0f;
    public static final float d = 170.0f;
    public static final float e = 204.0f;
    public static final float f = 214.0f;
    public static final float g = 238.0f;
    public static final float h = 290.0f;
    public static final float i = 301.0f;
    public static final float j = 314.0f;
    public static final float k = 318.0f;
    public static final float l = 324.0f;
    public static final float m = 327.0f;
    public static final float n = 331.0f;
    public static final float o = 333.0f;
    public static final float p = 342.0f;
    public static final float q = 352.0f;
    static final double r = 6371.0;
    static final double s = 1.495978707E8;
    static final double t = 9.4607E15;
    static final double u = 4.258750455597227E-5;
    static final double v = 1.5812558341348946E-8;
    public static final c w = new c(0.0, 1.0, 0.0);
    static final c x = new c(0.0, 0.0, -1.0);
    static final c y = new c(1.0, 0.0, 0.0);
    static final double z = 36.1;
    static final double A = -112.1;
    static final c[] B = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.b();
    static final c C = w.a(Math.sin(Math.toRadians(38.0))).a(y.a(-0.64).a(x.a(-0.77)).b().a(Math.cos(Math.toRadians(38.0)))).b();
    static final c D = y.a(0.8).a(x.a(0.5)).b();
    static final c E = w.a(-0.75).a(y.a(-0.4).a(x.a(0.53))).b().a(60.3);
    static final double F = 0.2727;
    static final c G = C.a(-1.0);
    static final c H = G.d(w).b();
    static final double I = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.c();
    static final c J = G.a(w.a(I)).a(y.a(-0.001));
    static final c K = w.a(0.42).a(x.a(0.9)).b();
    static final double L = 0.004650467260962158;
    static final Object[][] M = new Object[][]{{"MERCURY", 0.387, 2440.0, 118.0}, {"VENUS", 0.723, 6052.0, -64.0}, {"EARTH", 1.0, 6371.0, 0.0}, {"MARS", 1.524, 3390.0, 152.0}, {"JUPITER", 5.203, 69911.0, -118.0}, {"SATURN", 9.537, 58232.0, 0.0}, {"URANUS", 19.19, 25362.0, 76.0}, {"NEPTUNE", 30.07, 24622.0, -150.0}};
    static final c[] N = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.d();
    static final c O = w.a(Math.sin(Math.toRadians(42.0))).a(y.a(0.35).a(x.a(0.94)).b().a(Math.cos(Math.toRadians(42.0)))).b();
    static final c P = y.a(-0.9).a(x.a(0.3)).b(O.a(O.c(y.a(-0.9).a(x.a(0.3))))).b();
    static final c Q = P.a(-26.4);
    static final double R = 58.0;
    static final c S = Q.a(w.a(58.0));
    static final c T = w;
    static final c U = O.b(w.a(O.c(w))).b();
    static final c V = U.d(T);
    static final c W = new c(0.37, -0.74, 0.56).b();
    static final c X = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(W);
    static final c Y = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.b(S.a(-1.0).b());
    static final c Z = S.a(X.a(45.0));
    public static final String[] aa = new String[]{"EarthDay", "EarthAux", "MoonColor", "MilkyWay", "MilkyTop", "Planets", "Rings"};
    public static final Map<String, String> ab = new LinkedHashMap<String, String>();
    static final double[] ac;
    static final double[] ad;
    static final double[] ae;
    static final double[] af;
    static final double[] ag;
    static final double[] ah;
    static final double[] ai;
    static final double[] aj;
    static final double[] ak;
    static final double[] al;
    static final double[] am;
    static final double[] an;
    static final double[] ao;
    static final double[] ap;
    static final c aq;

    private q() {
    }

    static a a(int n2, c c2, c c3, c c4, double d2) {
        c c5;
        c c6;
        c c7;
        switch (n2) {
            case 0: {
                c7 = c2;
                c6 = G.a(c7.a(4.258750455597227E-5));
                c5 = Q.a(c6.a(1.5812558341348946E-8));
                break;
            }
            case 1: {
                c6 = c2;
                c7 = c6.b(G).a(23481.065876628472);
                c5 = Q.a(c6.a(1.5812558341348946E-8));
                break;
            }
            default: {
                c5 = c2;
                c6 = c5.b(Q).a(6.3240873387645096E7);
                c7 = c6.b(G).a(23481.065876628472);
            }
        }
        c c8 = c3.b(c2).b();
        c c9 = c8.d(c4).b();
        return new a(c7, c6, c5, c8, c9.d(c8), c9, d2, n2);
    }

    public static a a(float f2) {
        if (f2 >= 314.0f) {
            return cyou.rimuru.shootingstardemo.mc1211.client.fx.q.b(f2);
        }
        if (f2 < 126.0f) {
            double d2 = Math.pow(10.0, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a((double)f2, ac, ad));
            c c2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a().a(w.a(d2)).a(D.a(d2 * 0.35));
            double d3 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.b((Math.log10(d2) + 1.3) / 1.9);
            c c3 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a().a(aq, d3);
            return cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0, c2, c3, x.a(w, 0.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(62.0, 56.0, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.b((double)(f2 - 72.0f) / 50.0)));
        }
        if (f2 < 170.0f) {
            double d4 = Math.pow(10.0, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a((double)f2, ae, af));
            double d5 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.b(((double)f2 - 150.0) / 20.0);
            double d6 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.b((double)(f2 - 126.0f) / 12.0);
            c c4 = D.a(0.002101693349837231 * (1.0 - d6)).a(y.a(6.0E-4 * d6)).a(1.0 - d5);
            c c5 = G.a(w.a(d4)).a(c4).a(H.a(d4 * 0.7 * d5));
            c c6 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(G.b(c5).b(), c5.a(-1.0).b(), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.b(((double)f2 - 128.0) / 14.0));
            double d7 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.b(((double)f2 - 139.0) / 3.0) * (1.0 - cyou.rimuru.shootingstardemo.mc1211.client.fx.q.b(((double)f2 - 146.0) / 5.0));
            c c7 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(c6, J.b(c5).b(), d7);
            c c8 = x.a(H, d5).b();
            return cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(1, c5, c5.a(c7), c8, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(56.0, 60.0, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.b(((double)f2 - 140.0) / 30.0)) + 8.0 * d7);
        }
        if (f2 < 204.0f) {
            a a2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(169.999f);
            c c9 = a2.c.b(Q).b();
            double[] dArray = (double[])ah.clone();
            dArray[0] = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(a2.c.b(Q).a());
            double d8 = Math.pow(10.0, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a((double)f2, ag, dArray));
            double d9 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.b((double)(f2 - 170.0f) / 30.0);
            c c10 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(c9, Z.b(), d9);
            double d10 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.b((Math.log10(d8) - 0.3) / 1.4);
            c c11 = Q.a(aq, d10);
            c c12 = c11.a(c10.a(d8));
            return cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(2, c12, c11, a2.e.a(O, d10).b(), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(60.0, 58.0, d9));
        }
        if (f2 < 214.0f) {
            double d11 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.b((double)((f2 - 204.0f) / 10.0f));
            c c13 = Z.a(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(Z.a(-1.0).b(), S.b(Z).b(), d11 * 0.85));
            return cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(2, Z, c13, O, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(58.0, 50.0, d11));
        }
        double[] dArray = (double[])aj.clone();
        dArray[0] = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(Z.b(S).a());
        double d12 = Math.pow(10.0, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a((double)f2, ai, dArray));
        double d13 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.b((double)((f2 - 214.0f) / 24.0f));
        c c14 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(Z.a(-1.0).b(), S.b(Z).b(), 0.85);
        c c15 = S.a(X.a(d12));
        c c16 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(X.a(-1.0), S.a(-1.0).b(), 0.35);
        c c17 = c15.a(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(c14, c16, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.b((double)(f2 - 214.0f) / 10.0)));
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(2, c15, c17, O.a(U, d13).b(), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(50.0, 40.0, d13));
    }

    static a b(float f2) {
        c c2 = y.a(0.06).a(x.a(0.035));
        double d2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(64.0, 80.0, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.b((double)(f2 - 318.0f) / 14.0));
        if (f2 < 324.0f) {
            double d3 = Math.pow(10.0, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a((double)f2, ak, al));
            c c3 = Q.a(w.a(d3)).a(c2.a(d3));
            return cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(2, c3, Q, x, d2);
        }
        if (f2 < 327.0f) {
            double d4 = Math.pow(10.0, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a((double)f2, am, an));
            c c4 = G.a(w.a(d4)).a(c2.a(d4));
            return cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(1, c4, G, x, d2);
        }
        double d5 = Math.pow(10.0, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a((double)f2, ao, ap));
        c c5 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a().a(w.a(d5)).a(c2.a(d5));
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0, c5, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(), x, d2);
    }

    public static void a(float f2, float f3, float f4, BiConsumer<String, float[]> biConsumer) {
        a a2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(f2);
        double d2 = Math.tan(Math.toRadians(a2.g) * 0.5);
        boolean bl = f2 >= 314.0f;
        biConsumer.accept("CamFwd", a2.d.c());
        biConsumer.accept("CamUp", a2.e.c());
        biConsumer.accept("CamRight", a2.f.c());
        biConsumer.accept("EarthRel", aq.b(a2.a).c());
        biConsumer.accept("MoonRel", E.b(a2.a).c());
        biConsumer.accept("SiteRel", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a().b(a2.a).c());
        biConsumer.accept("ThreadRel", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a().b(a2.a).c());
        biConsumer.accept("EarthX", B[0].c());
        biConsumer.accept("EarthY", B[1].c());
        biConsumer.accept("EarthZ", B[2].c());
        biConsumer.accept("SunDir", C.c());
        biConsumer.accept("SunRelS", aq.b(a2.b).c());
        float[] fArray = new float[N.length * 4];
        for (int i2 = 0; i2 < N.length; ++i2) {
            c c2 = N[i2].b(a2.b);
            fArray[i2 * 4] = (float)c2.a;
            fArray[i2 * 4 + 1] = (float)c2.b;
            fArray[i2 * 4 + 2] = (float)c2.c;
            fArray[i2 * 4 + 3] = (float)((Double)M[i2][2] / 1.495978707E8);
        }
        biConsumer.accept("Planet", fArray);
        biConsumer.accept("Ecliptic", H.c());
        biConsumer.accept("SaturnPole", K.c());
        biConsumer.accept("CentreRel", aq.b(a2.c).c());
        biConsumer.accept("SunRelG", Q.b(a2.c).c());
        biConsumer.accept("GalaxyN", O.c());
        biConsumer.accept("ToCentre", P.c());
        biConsumer.accept("GunRel", S.b(a2.c).c());
        double d3 = a2.h == 2 ? 58.0 : 1.0E9;
        float f5 = 1.0f - cyou.rimuru.shootingstardemo.mc1211.client.fx.q.m((f2 - (bl ? 360.0f : 128.0f)) / 8.0f);
        if (bl) {
            f5 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.m((f2 - 327.0f + 1.0f) / 3.0f);
        }
        float f6 = bl ? 0.0f : cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(f2, 136.0f, 152.0f, 176.0f, 184.0f);
        float f7 = bl ? cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(f2, 314.0f, 316.0f, 321.0f, 325.0f) : cyou.rimuru.shootingstardemo.mc1211.client.fx.q.m((f2 - 180.0f) / 10.0f);
        double d4 = Math.max(a2.c.b(Q).a(), 1.0E-12);
        float f8 = (float)cyou.rimuru.shootingstardemo.mc1211.client.fx.q.b((cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(d4) - cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(4.0)) / (cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(28.0) - cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(4.0)));
        float f9 = bl ? 1.0f : cyou.rimuru.shootingstardemo.mc1211.client.fx.q.m((f2 - 214.0f) / 6.0f);
        float f10 = bl ? 0.0f : cyou.rimuru.shootingstardemo.mc1211.client.fx.q.m((f2 - 190.0f) / 8.0f);
        biConsumer.accept("S0", new float[]{(float)d2, f2 / 20.0f, f4, f3});
        biConsumer.accept("S1", new float[]{f5, f6, f7, f8});
        biConsumer.accept("S2", new float[]{f9, bl ? 1.0f : 0.0f, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.c(f2), 0.2727f});
        biConsumer.accept("S3", new float[]{0.004650467f, f10, bl ? cyou.rimuru.shootingstardemo.mc1211.client.fx.q.h(f2) : 0.0f, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.g(f2)});
        biConsumer.accept("S4", new float[]{(float)Math.min(d3, 1.0E9), bl ? (f2 - 314.0f) / 20.0f : -1.0f, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.f(f2), a2.h});
        biConsumer.accept("S5", new float[]{cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a((f2 - 331.0f) / 11.0f, 0.0f, 1.0f), 0.0f, 0.0f, 0.0f});
    }

    public static float c(float f2) {
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a((f2 - 58.0f) / 14.0f, 0.0f, 1.0f);
    }

    public static float d(float f2) {
        if (f2 < 58.0f || f2 >= 342.0f || f2 >= 238.0f && f2 < 318.0f) {
            return 0.0f;
        }
        return f2 >= 318.0f ? 1.0f : cyou.rimuru.shootingstardemo.mc1211.client.fx.q.c(f2);
    }

    public static boolean e(float f2) {
        return f2 >= 238.0f && f2 < 318.0f;
    }

    public static float f(float f2) {
        if (f2 < 331.0f || f2 >= 342.0f) {
            return 0.0f;
        }
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.q.m((f2 - 331.0f) / 2.0f);
    }

    static float g(float f2) {
        return f2 >= 314.0f ? 0.0f : 1.0f;
    }

    static float h(float f2) {
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.q.m((f2 - 327.0f - 2.0f) / 5.0f);
    }

    public static List<b> i(float f2) {
        a a2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(f2);
        ArrayList<b> arrayList = new ArrayList<b>();
        if (f2 < 314.0f) {
            if (f2 < 132.0f) {
                arrayList.add(new b("TARGET", "LASER LOCK", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a().b(a2.a), 0.0));
            }
            if (f2 >= 100.0f && f2 < 130.0f) {
                arrayList.add(new b("EARTH", "6,371 KM", aq.b(a2.a), 1.0));
                arrayList.add(new b("MOON", "384,400 KM", E.b(a2.a), 0.2727));
            }
            if (f2 >= 138.0f && f2 < 184.0f) {
                arrayList.add(new b("SATURN", "9.5 AU", N[5].b(a2.b), (Double)M[5][2] / 1.495978707E8));
            }
            if (f2 >= 150.0f && f2 < 184.0f) {
                arrayList.add(new b("SOL", "G2V", aq.b(a2.b), 0.004650467260962158));
                arrayList.add(new b("EARTH", "1 AU", N[2].b(a2.b), 0.0));
                arrayList.add(new b("JUPITER", "5.2 AU", N[4].b(a2.b), 0.0));
                arrayList.add(new b("NEPTUNE", "30 AU", N[7].b(a2.b), 0.0));
            }
            if (f2 >= 190.0f && f2 < 214.0f) {
                arrayList.add(new b("SOL", "26,400 LY TO CORE", Q.b(a2.c), 0.0));
                arrayList.add(new b("MILKY WAY", "100,000 LY", aq.b(a2.c), 50.0));
            }
            if (f2 >= 198.0f) {
                arrayList.add(new b("SS-01", "RAILGUN \u00b7 58,000 LY", S.b(a2.c), 0.0));
            }
        }
        return arrayList;
    }

    public static double j(float f2) {
        a a2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(f2);
        if (a2.h == 2) {
            return a2.c.b(Q).a() * 9.4607E15;
        }
        return a2.a.b(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a()).a() * 6371.0;
    }

    public static c[] a(float f2, double[] dArray) {
        double d2;
        double var7_3;
        c c2;
        c c3;
        c c4 = new c(0.0, 1.0, 0.0);
        if (f2 < 256.0f) {
            var7_3 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.b((double)(f2 - 238.0f) / 18.0);
            double d3 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.d(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.c((double)(f2 - 238.0f) / 18.0));
            c c5 = new c(1.0, 0.0, 0.0).a(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.0, 6.0, var7_3));
            c3 = W.a(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(120.0, 34.0, d3)).a(c5);
            c c6 = new c(0.0, 0.0, -2.5).b(c3).b();
            c2 = c3.a(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(c6, Y, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.22, 0.16, var7_3)));
            d2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(40.0, 46.0, var7_3);
        } else if (f2 < 278.0f) {
            var7_3 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.b((double)(f2 - 256.0f) / 22.0);
            c3 = new c(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(-3.4, -3.0, var7_3), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(1.6, 1.1, var7_3), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(4.0, -9.0, var7_3));
            c2 = c3.a(new c(0.42, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(-0.34, -0.2, var7_3), -1.0));
            d2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(58.0, 52.0, var7_3);
        } else if (f2 < 290.0f) {
            var7_3 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.b((double)((f2 - 278.0f) / 12.0f));
            double d4 = Math.toRadians(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(-38.0, 10.0, var7_3));
            c3 = new c(Math.sin(d4) * 14.0, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(5.5, 3.6, var7_3), 9.0 + Math.cos(d4) * 14.0);
            c2 = new c(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(-1.5, -0.4, var7_3), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(-0.5, 0.1, var7_3), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(4.0, 2.0, var7_3));
            d2 = 54.0;
        } else if (f2 < 301.0f) {
            var7_3 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.b((double)((f2 - 290.0f) / 11.0f));
            c3 = new c(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(2.9, 2.4, var7_3), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(1.9, 1.5, var7_3), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(-18.2, -16.9, var7_3));
            c2 = new c(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.3, 0.0, var7_3), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.2, 0.1, var7_3), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(-2.0, -6.0, var7_3));
            d2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(46.0, 40.0, var7_3);
        } else {
            var7_3 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.b((double)((f2 - 301.0f) / 13.0f));
            double d5 = f2 >= 314.0f ? Math.exp((double)(-(f2 - 314.0f)) * 0.6) * (1.0 - Math.exp((double)(-(f2 - 314.0f)) * 6.0)) * 1.6 : 0.0;
            c c7 = new c(0.0, 0.0, -18.0);
            c c8 = new c(-0.88, -0.22, 0.42).b().a(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(9.0, 6.6, var7_3) + d5);
            c3 = c7.a(c8).a(new c(0.0, 0.0, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(-0.8, 0.0, var7_3)));
            c2 = c7.a(new c(0.0, 0.0, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.6, 0.2, var7_3)));
            d2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(34.0, 27.0, var7_3) + d5 * 5.0;
        }
        dArray[0] = d2;
        c c9 = c2.b(c3).b();
        c c10 = c9.d(c4).b();
        return new c[]{c3, c9, c10.d(c9), c10};
    }

    public static float k(float f2) {
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a((f2 - 290.0f - 1.0f) / 19.0f, 0.0f, 1.0f);
    }

    public static float l(float f2) {
        return f2 >= 314.0f ? 0.0f : cyou.rimuru.shootingstardemo.mc1211.client.fx.q.m((f2 - 310.0f) / 2.5f);
    }

    public static void a(float f2, float f3, BiConsumer<String, float[]> biConsumer) {
        double[] dArray = new double[1];
        c[] cArray = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(f2, dArray);
        biConsumer.accept("CamPos", cArray[0].c());
        biConsumer.accept("CamFwd", cArray[1].c());
        biConsumer.accept("CamUp", cArray[2].c());
        biConsumer.accept("CamRight", cArray[3].c());
        biConsumer.accept("GunX", V.c());
        biConsumer.accept("GunY", U.c());
        biConsumer.accept("GunZ", T.c());
        biConsumer.accept("CentreRel", aq.b(S).c());
        biConsumer.accept("SunRelG", Q.b(S).c());
        biConsumer.accept("GalaxyN", O.c());
        biConsumer.accept("ToCentre", P.c());
        float f4 = f2 >= 314.0f ? (f2 - 314.0f) / 20.0f : -1.0f;
        float f5 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.m((f2 - 238.0f) / 10.0f);
        float f6 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.m((f2 - 250.0f) / 20.0f);
        biConsumer.accept("S0", new float[]{(float)Math.tan(Math.toRadians(dArray[0]) * 0.5), f2 / 20.0f, f3, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.l(f2)});
        biConsumer.accept("S1", new float[]{cyou.rimuru.shootingstardemo.mc1211.client.fx.q.k(f2), f4, f5, f6});
    }

    public static void a(float f2, BiConsumer<String, float[]> biConsumer) {
        double[] dArray = new double[1];
        c[] cArray = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(f2, dArray);
        biConsumer.accept("CamPos", cArray[0].c());
        biConsumer.accept("CamFwd", cArray[1].c());
        biConsumer.accept("CamUp", cArray[2].c());
        biConsumer.accept("CamRight", cArray[3].c());
        biConsumer.accept("KeyDir", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.b(aq.b(S).b()).c());
        biConsumer.accept("Muzzle", new float[]{0.0f, 0.0f, -18.0f});
        float f3 = f2 >= 314.0f ? (f2 - 314.0f) / 20.0f : -1.0f;
        float f4 = f3 < 0.0f ? 0.0f : (float)Math.exp((double)(-f3) * 9.0);
        float f5 = 1.0f - 0.85f * cyou.rimuru.shootingstardemo.mc1211.client.fx.q.l(f2);
        biConsumer.accept("R0", new float[]{(float)Math.tan(Math.toRadians(dArray[0]) * 0.5), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.m((f2 - 238.0f) / 10.0f) * 0.9f * f5, f4, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.k(f2) * f5});
    }

    static c a(c c2) {
        return V.a(c2.a).a(U.a(c2.b)).a(T.a(c2.c));
    }

    static c b(c c2) {
        return new c(c2.c(V), c2.c(U), c2.c(T));
    }

    static c a() {
        return w;
    }

    private static c[] b() {
        double d2 = Math.toRadians(36.1);
        double d3 = Math.toRadians(-112.1);
        c c2 = new c(Math.cos(d2) * Math.sin(d3), Math.sin(d2), Math.cos(d2) * Math.cos(d3));
        c c3 = new c(-Math.sin(d2) * Math.sin(d3), Math.cos(d2), -Math.sin(d2) * Math.cos(d3));
        c c4 = c3.d(c2).b();
        c c5 = w.a(c2.a).a(x.a(c3.a)).a(y.a(c4.a));
        c c6 = w.a(c2.b).a(x.a(c3.b)).a(y.a(c4.b));
        c c7 = w.a(c2.c).a(x.a(c3.c)).a(y.a(c4.c));
        return new c[]{c5, c6, c7};
    }

    private static double c() {
        double d2 = G.c(w);
        double d3 = G.c(G) - 90.95436900000001;
        return -d2 + Math.sqrt(d2 * d2 - d3);
    }

    private static c[] d() {
        c c2 = G.b();
        c c3 = H.d(c2).b();
        c[] cArray = new c[M.length];
        for (int i2 = 0; i2 < M.length; ++i2) {
            if (i2 == 2) {
                cArray[i2] = G;
                continue;
            }
            if (i2 == 5) {
                cArray[i2] = J;
                continue;
            }
            double d2 = (Double)M[i2][1];
            double d3 = Math.toRadians((Double)M[i2][3]);
            cArray[i2] = c2.a(Math.cos(d3) * d2).a(c3.a(Math.sin(d3) * d2));
        }
        return cArray;
    }

    static double a(double d2) {
        return Math.log10(d2);
    }

    static double a(double d2, double d3, double d4) {
        return d2 + (d3 - d2) * d4;
    }

    static double b(double d2) {
        d2 = Math.max(0.0, Math.min(1.0, d2));
        return d2 * d2 * (3.0 - 2.0 * d2);
    }

    static float m(float f2) {
        return (float)cyou.rimuru.shootingstardemo.mc1211.client.fx.q.b((double)f2);
    }

    static double c(double d2) {
        return Math.max(0.0, Math.min(1.0, d2));
    }

    static double d(double d2) {
        return 1.0 - Math.pow(1.0 - d2, 3.0);
    }

    static float a(float f2, float f3, float f4) {
        return Math.max(f3, Math.min(f4, f2));
    }

    static float a(float f2, float f3, float f4, float f5, float f6) {
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a((f2 - f3) / Math.max(f4 - f3, 0.001f), 0.0f, 1.0f) * (1.0f - cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a((f2 - f5) / Math.max(f6 - f5, 0.001f), 0.0f, 1.0f));
    }

    static c a(c c2, c c3, double d2) {
        double d3 = Math.max(-1.0, Math.min(1.0, c2.c(c3)));
        double d4 = Math.acos(d3) * d2;
        c c4 = c3.b(c2.a(d3));
        if (c4.a() < 1.0E-9) {
            return c2;
        }
        return c2.a(Math.cos(d4)).a(c4.b().a(Math.sin(d4)));
    }

    static double a(double d2, double[] dArray, double[] dArray2) {
        int n2;
        int n3 = dArray.length;
        if (d2 <= dArray[0]) {
            return dArray2[0];
        }
        if (d2 >= dArray[n3 - 1]) {
            return dArray2[n3 - 1];
        }
        int n4 = 0;
        while (d2 > dArray[n4 + 1]) {
            ++n4;
        }
        double[] dArray3 = new double[n3];
        double[] dArray4 = new double[n3 - 1];
        for (n2 = 0; n2 < n3 - 1; ++n2) {
            dArray4[n2] = (dArray2[n2 + 1] - dArray2[n2]) / (dArray[n2 + 1] - dArray[n2]);
        }
        dArray3[0] = dArray4[0];
        dArray3[n3 - 1] = dArray4[n3 - 2];
        for (n2 = 1; n2 < n3 - 1; ++n2) {
            if (dArray4[n2 - 1] * dArray4[n2] <= 0.0) {
                dArray3[n2] = 0.0;
                continue;
            }
            double d3 = 2.0 * (dArray[n2 + 1] - dArray[n2]) + (dArray[n2] - dArray[n2 - 1]);
            double d4 = dArray[n2 + 1] - dArray[n2] + 2.0 * (dArray[n2] - dArray[n2 - 1]);
            dArray3[n2] = (d3 + d4) / (d3 / dArray4[n2 - 1] + d4 / dArray4[n2]);
        }
        double d5 = dArray[n4 + 1] - dArray[n4];
        double d6 = (d2 - dArray[n4]) / d5;
        double d7 = d6 * d6;
        double d8 = d7 * d6;
        return (2.0 * d8 - 3.0 * d7 + 1.0) * dArray2[n4] + (d8 - 2.0 * d7 + d6) * d5 * dArray3[n4] + (-2.0 * d8 + 3.0 * d7) * dArray2[n4 + 1] + (d8 - d7) * d5 * dArray3[n4 + 1];
    }

    static {
        ab.put("EarthDay", "moonfall/earth_day.jpg");
        ab.put("EarthAux", "moonfall/earth_lights.jpg+moonfall/earth_clouds.jpg+moonfall/earth_height.jpg");
        ab.put("MoonColor", "moonfall/moon_color.jpg");
        ab.put("MilkyWay", "moonfall/milky_way.jpg");
        ab.put("MilkyTop", "shooting_star/milky_way_top.jpg");
        ab.put("Planets", "shooting_star/planets.jpg");
        ab.put("Rings", "shooting_star/saturn_ring.jpg");
        ac = new double[]{58.0, 72.0, 90.0, 100.0, 108.0, 118.0, 126.0};
        ad = new double[]{cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.0016), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.008), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.6), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(2.4), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(4.2), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(40.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(141.0)};
        ae = new double[]{126.0, 136.0, 139.0, 146.0, 150.0, 158.0, 170.0};
        af = new double[]{cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.006047425646948062), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(9.9), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(I - 0.0015), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(I + 0.006), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(10.6), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(38.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(88.0)};
        ag = new double[]{170.0, 176.0, 181.0, 191.0, 197.0, 204.0};
        ah = new double[]{cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(1.73E-6), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(3.0E-4), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.15), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(10.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(50.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(Z.a())};
        ai = new double[]{214.0, 220.0, 228.0, 234.0, 238.0};
        aj = new double[]{cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(35.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(14.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.8), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.004), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(2.0E-6)};
        ak = new double[]{314.0, 318.0, 320.0, 322.0, 324.0};
        al = new double[]{cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(58.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(57.6), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(18.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.3), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(2.0E-6)};
        am = new double[]{324.0, 325.5, 326.0, 327.0};
        an = new double[]{cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(126.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(10.5), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(9.9), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.0042)};
        ao = new double[]{327.0, 331.0, 342.0};
        ap = new double[]{cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(98.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(1.4), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.08)};
        aq = new c(0.0, 0.0, 0.0);
    }

    public static final class c
{
        private final double a;
        private final double b;
        private final double c;

        public c(double x2, double y, double z) {
            this.a = x2;
            this.b = y;
            this.c = z;
        }

        public c a(c c2) {
            return new c(this.a + c2.a, this.b + c2.b, this.c + c2.c);
        }

        public c b(c c2) {
            return new c(this.a - c2.a, this.b - c2.b, this.c - c2.c);
        }

        public c a(double d2) {
            return new c(this.a * d2, this.b * d2, this.c * d2);
        }

        public double c(c c2) {
            return this.a * c2.a + this.b * c2.b + this.c * c2.c;
        }

        public c d(c c2) {
            return new c(this.b * c2.c - this.c * c2.b, this.c * c2.a - this.a * c2.c, this.a * c2.b - this.b * c2.a);
        }

        public double a() {
            return Math.sqrt(this.c(this));
        }

        public c b() {
            double d2 = this.a();
            return d2 < 1.0E-300 ? this : this.a(1.0 / d2);
        }

        public c a(c c2, double d2) {
            return this.a(c2.b(this).a(d2));
        }

        float[] c() {
            return new float[]{(float)this.a, (float)this.b, (float)this.c};
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

        public double d() {
            return this.a;
        }

        public double e() {
            return this.b;
        }

        public double f() {
            return this.c;
        }
    }

    public static final class a
{
        private final c a;
        private final c b;
        private final c c;
        private final c d;
        private final c e;
        private final c f;
        private final double g;
        private final int h;

        public a(c e2, c s2, c g2, c fwd, c up, c right, double fov, int frame) {
            this.a = e2;
            this.b = s2;
            this.c = g2;
            this.d = fwd;
            this.e = up;
            this.f = right;
            this.g = fov;
            this.h = frame;
        }

        public double[] a(c c2) {
            double d2 = c2.c(this.d);
            if (d2 <= 1.0E-12) {
                return null;
            }
            double d3 = Math.tan(Math.toRadians(this.g) * 0.5);
            return new double[]{c2.c(this.f) / (d2 * d3), c2.c(this.e) / (d2 * d3), d2};
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

        public c a() {
            return this.a;
        }

        public c b() {
            return this.b;
        }

        public c c() {
            return this.c;
        }

        public c d() {
            return this.d;
        }

        public c e() {
            return this.e;
        }

        public c f() {
            return this.f;
        }

        public double g() {
            return this.g;
        }

        public int h() {
            return this.h;
        }
    }

    public static final class b
{
        private final String a;
        private final String b;
        private final c c;
        private final double d;

        public b(String name, String detail, c rel, double radius) {
            this.a = name;
            this.b = detail;
            this.c = rel;
            this.d = radius;
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

        public c c() {
            return this.c;
        }

        public double d() {
            return this.d;
        }
    }
}

