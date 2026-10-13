/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.fx;

import cyou.rimuru.shootingstardemo.mc1211.client.fx.q;
import cyou.rimuru.shootingstardemo.mc1211.star.c;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;

public final class m {
    public static final float a = 9.0f;
    public static final float b = 24.0f;
    public static final float c = 4.0f;
    public static final float d = 56.0f;
    public static final float e = 58.0f;
    public static final float f = 70.0f;
    static final float g = 84.0f;
    public static final float h = 96.0f;
    public static final float i = 130.0f;
    public static final float j = 150.0f;
    public static final float k = 6.0f;
    static final float l = 192.0f;
    public static final float m = 192.0f;
    public static final float n = 208.0f;
    public static final float o = 4.0f;
    public static final float p = 236.0f;
    public static final float q = 272.0f;
    public static final float r = 290.0f;
    static final double s = 1.5812501680078302E-5;
    static final double t = 9.4607E12;
    static final double u = 7.353578487849736E-8;
    static final double[] v = new double[]{30.0, 3.0, 3.0, 1.4, 4.1, 2.4, 3.4};
    static final float[][] w = new float[][]{{1.0f, 0.72f, 0.42f}, {0.92f, 0.95f, 1.0f}, {0.93f, 0.95f, 1.0f}, {0.96f, 0.96f, 1.0f}, {0.92f, 0.94f, 1.0f}, {0.94f, 0.95f, 1.0f}, {0.78f, 0.86f, 1.0f}};
    static final q.c x = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.G.a(1.5812501680078302E-5);
    static final double[] y = new double[]{58.0, 70.0, 78.0, 84.0, 90.0, 96.0};
    static final double[] z = new double[]{cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.002), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.02), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.4), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(2.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(12.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(60.0)};
    static final double[] A = new double[]{96.0, 100.0, 108.0, 118.0, 126.0, 130.0};
    static final double[] B = new double[]{1.0, 0.999, 0.8, 0.45, 0.2, 0.12};

    private m() {
    }

    static q.c a(int n2, double d2) {
        double[] dArray = cyou.rimuru.shootingstardemo.mc1211.star.c.c(n2, d2);
        return new q.c(dArray[0], dArray[1], dArray[2]).a(cyou.rimuru.shootingstardemo.mc1211.star.c.e[n2]);
    }

    static q.c a(double d2) {
        q.c c2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.aq;
        for (int i2 = 0; i2 < 7; ++i2) {
            c2 = c2.a(cyou.rimuru.shootingstardemo.mc1211.client.fx.m.a(i2, d2));
        }
        return c2.a(0.14285714285714285);
    }

    static double a(int n2) {
        return Math.max(v[n2], 2.0) * 7.353578487849736E-8 * 6.0;
    }

    public static float a(float f2) {
        if (f2 < 58.0f || f2 >= 272.0f) {
            return 0.0f;
        }
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a((f2 - 58.0f) / 12.0f, 0.0f, 1.0f);
    }

    static float a(float f2, int n2) {
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.q.m((f2 - (150.0f + (float)n2 * 6.0f)) / 5.0f);
    }

    static float b(float f2, int n2) {
        float f3 = 208.0f + (float)n2 * 4.0f;
        return f2 >= f3 ? (f2 - f3) / 20.0f : -1.0f;
    }

    static double c(float f2, int n2) {
        float f3 = 208.0f + (float)n2 * 4.0f;
        if (f2 < f3) {
            return 0.0;
        }
        double[] dArray = new double[]{f3, (double)f3 + 2.0, 236.0, 256.0, 272.0};
        double[] dArray2 = new double[]{cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(cyou.rimuru.shootingstardemo.mc1211.star.c.e[n2]), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(cyou.rimuru.shootingstardemo.mc1211.star.c.e[n2] * 0.97), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(20.0), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.02), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(1.0E-6)};
        return 1.0 - Math.pow(10.0, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a((double)f2, dArray, dArray2)) / cyou.rimuru.shootingstardemo.mc1211.star.c.e[n2];
    }

    static q.c a(float f2, int n2, double d2) {
        q.c c2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.m.a(n2, d2);
        return x.a(c2.b(x).a(1.0 - cyou.rimuru.shootingstardemo.mc1211.client.fx.m.c(f2, n2)));
    }

    static a a(int n2, q.c c2, q.c c3, q.c c4, double d2) {
        q.c c5 = c3.b(c2).b();
        q.c c6 = c5.d(c4).b();
        return new a(n2, c2, c5, c6.d(c5), c6, d2);
    }

    static double a(float f2, double d2, double d3) {
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.q.b(((double)f2 - d2) / (d3 - d2));
    }

    public static a a(float f2, double d2) {
        q.c c2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.m.a(d2);
        if (f2 < 96.0f) {
            double d3 = Math.pow(10.0, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a((double)f2, y, z));
            q.c c3 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w.a(1.0 + d3).a(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.D.a(d3 * 0.25));
            q.c c4 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w.a(10.0).b(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.x.a(1.5)).b();
            q.c c5 = c3.a(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(c4, c2.b(), cyou.rimuru.shootingstardemo.mc1211.client.fx.m.a(f2, 70.0, 96.0)));
            return cyou.rimuru.shootingstardemo.mc1211.client.fx.m.a(0, c3, c5, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.x, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(62.0, 56.0, cyou.rimuru.shootingstardemo.mc1211.client.fx.m.a(f2, 84.0, 96.0)));
        }
        if (f2 < 130.0f) {
            double d4 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a((double)f2, A, B);
            q.c c6 = x.a(c2.b(x).a(1.0 - d4));
            return cyou.rimuru.shootingstardemo.mc1211.client.fx.m.a(2, c6, c2, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.x, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(56.0, 70.0, cyou.rimuru.shootingstardemo.mc1211.client.fx.m.a(f2, 96.0, 130.0)));
        }
        q.c c7 = c2.a(-1.0).b();
        q.c c8 = c7.d(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w).b();
        if (f2 < 150.0f || f2 >= 208.0f && f2 < 236.0f) {
            double d5 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(-0.35, 0.35, cyou.rimuru.shootingstardemo.mc1211.client.fx.m.a(f2, 130.0, 236.0));
            q.c c9 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(c7.a(-1.0), c8, 0.55 + d5 * 0.3).a(95.0).a(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w.a(22.0));
            q.c c10 = c2.a(c9);
            return cyou.rimuru.shootingstardemo.mc1211.client.fx.m.a(2, c10, c2.a(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.aq, 0.12), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w, 64.0);
        }
        if (f2 < 192.0f) {
            int n2 = Math.min((int)((f2 - 150.0f) / 6.0f), 6);
            double d6 = (f2 - 150.0f - (float)n2 * 6.0f) / 6.0f;
            q.c c11 = cyou.rimuru.shootingstardemo.mc1211.client.fx.m.a(n2, d2);
            q.c c12 = c11.a(-1.0).b();
            q.c c13 = c12.d(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w).b();
            double d7 = cyou.rimuru.shootingstardemo.mc1211.client.fx.m.a(n2);
            q.c c14 = c11.a(c13.a(d7 * cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(3.2, 2.6, d6))).a(c12.a(d7 * 1.6)).a(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w.a(d7 * cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.9, 0.6, d6)));
            return cyou.rimuru.shootingstardemo.mc1211.client.fx.m.a(2, c14, c11.a(c12.a(d7 * 0.2)), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w, 58.0);
        }
        if (f2 < 208.0f) {
            double d8 = cyou.rimuru.shootingstardemo.mc1211.client.fx.m.a(f2, 192.0, 208.0);
            q.c c15 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w.a(1.0 + cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.8, 0.05, d8)).a(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.D.a(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(0.4, 0.02, d8)));
            q.c c16 = c15.a(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w.a(10.0).a(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.D.a(-1.0)));
            return cyou.rimuru.shootingstardemo.mc1211.client.fx.m.a(0, c15, c16, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.x, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(70.0, 62.0, d8));
        }
        q.c c17 = cyou.rimuru.shootingstardemo.mc1211.client.fx.m.a(f2, 3, d2);
        q.c c18 = x.b(cyou.rimuru.shootingstardemo.mc1211.client.fx.m.a(3, d2)).b();
        q.c c19 = c18.d(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w).b();
        double d9 = Math.max(c17.b(x).a() * 0.02, 1.0E-9);
        q.c c20 = c17.b(c18.a(d9)).a(c19.a(d9 * 0.25)).a(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w.a(d9 * 0.12));
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.m.a(2, c20, c20.a(c18), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(62.0, 74.0, cyou.rimuru.shootingstardemo.mc1211.client.fx.m.a(f2, 236.0, 272.0)));
    }

    public static void a(float f2, float f3, float f4, double d2, BiConsumer<String, float[]> biConsumer) {
        Object object;
        q.c c2;
        q.c c3;
        q.c c4;
        a a2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.m.a(f2, d2);
        double d3 = Math.tan(Math.toRadians(a2.f()) * 0.5);
        biConsumer.accept("CamFwd", a2.c().c());
        biConsumer.accept("CamUp", a2.d().c());
        biConsumer.accept("CamRight", a2.e().c());
        switch (a2.a()) {
            case 0: {
                c4 = a2.b();
                c3 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.G.a(c4.a(4.258750455597227E-5));
                c2 = c3.a(1.5812501680078302E-5);
                break;
            }
            case 1: {
                c3 = a2.b();
                c4 = c3.b(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.G).a(23481.065876628472);
                c2 = c3.a(1.5812501680078302E-5);
                break;
            }
            default: {
                c2 = a2.b();
                c3 = c2.a(63241.100000000006);
                c4 = c3.b(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.G).a(23481.065876628472);
            }
        }
        biConsumer.accept("EarthRel", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.aq.b(c4).c());
        biConsumer.accept("MoonRel", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.E.b(c4).c());
        biConsumer.accept("EarthX", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.B[0].c());
        biConsumer.accept("EarthY", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.B[1].c());
        biConsumer.accept("EarthZ", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.B[2].c());
        biConsumer.accept("SunDir", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.C.c());
        biConsumer.accept("SiteN", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.w.c());
        biConsumer.accept("SunRelS", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.aq.b(c3).c());
        float[] fArray = new float[cyou.rimuru.shootingstardemo.mc1211.client.fx.q.N.length * 4];
        for (int i2 = 0; i2 < cyou.rimuru.shootingstardemo.mc1211.client.fx.q.N.length; ++i2) {
            object = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.N[i2].b(c3);
            fArray[i2 * 4] = (float)((q.c)object).d();
            fArray[i2 * 4 + 1] = (float)((q.c)object).e();
            fArray[i2 * 4 + 2] = (float)((q.c)object).f();
            fArray[i2 * 4 + 3] = (float)((Double)cyou.rimuru.shootingstardemo.mc1211.client.fx.q.M[i2][2] / 1.495978707E8);
        }
        biConsumer.accept("Planet", fArray);
        biConsumer.accept("Ecliptic", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.H.c());
        biConsumer.accept("SaturnPole", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.K.c());
        biConsumer.accept("GalaxyN", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.O.c());
        biConsumer.accept("ToCentre", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.P.c());
        float[] fArray2 = new float[28];
        object = new float[28];
        float[] fArray3 = new float[28];
        for (int i3 = 0; i3 < 7; ++i3) {
            q.c c5 = cyou.rimuru.shootingstardemo.mc1211.client.fx.m.a(i3, d2).b(c2);
            fArray2[i3 * 4] = (float)c5.d();
            fArray2[i3 * 4 + 1] = (float)c5.e();
            fArray2[i3 * 4 + 2] = (float)c5.f();
            fArray2[i3 * 4 + 3] = (float)(v[i3] * 7.353578487849736E-8);
            ((float[])object)[i3 * 4] = w[i3][0];
            ((float[])object)[i3 * 4 + 1] = w[i3][1];
            ((float[])object)[i3 * 4 + 2] = w[i3][2];
            ((float[])object)[i3 * 4 + 3] = cyou.rimuru.shootingstardemo.mc1211.client.fx.m.a(f2, i3);
            q.c c6 = cyou.rimuru.shootingstardemo.mc1211.client.fx.m.a(f2, i3, d2);
            q.c c7 = c6.b(c2);
            fArray3[i3 * 4] = (float)c7.d();
            fArray3[i3 * 4 + 1] = (float)c7.e();
            fArray3[i3 * 4 + 2] = (float)c7.f();
            fArray3[i3 * 4 + 3] = cyou.rimuru.shootingstardemo.mc1211.client.fx.m.b(f2, i3);
        }
        biConsumer.accept("Stars", fArray2);
        biConsumer.accept("Tints", (float[])object);
        biConsumer.accept("Shots", fArray3);
        q.c c8 = a2.a() == 2 && f2 < 130.0f ? cyou.rimuru.shootingstardemo.mc1211.client.fx.m.a(d2).b() : (f2 >= 208.0f ? cyou.rimuru.shootingstardemo.mc1211.client.fx.m.a(d2).a(-1.0).b() : a2.c());
        biConsumer.accept("Heading", c8.c());
        float f5 = Math.max(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(f2, 84.0f, 100.0f, 124.0f, 130.0f), cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(f2, 234.0f, 238.0f, 270.0f, 273.0f));
        float f6 = a2.a() == 2 ? 0.0f : 1.0f;
        float f7 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(f2, 66.0f, 72.0f, 88.0f, 96.0f);
        float f8 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(f2, 192.0f, 196.0f, 208.0f, 209.0f);
        biConsumer.accept("S0", new float[]{(float)d3, f2 / 20.0f, f4, f3});
        biConsumer.accept("S1", new float[]{f6, f5, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a((f2 - 58.0f) / 12.0f, 0.0f, 1.0f), 0.2727f});
        biConsumer.accept("S2", new float[]{0.004650467f, f7, f8, a2.a()});
        biConsumer.accept("S3", new float[]{3.9531255E-6f, 1.5812502E-5f, 0.0f, 0.0f});
    }

    public static List<b> b(float f2, double d2) {
        boolean bl;
        a a2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.m.a(f2, d2);
        ArrayList<b> arrayList = new ArrayList<b>();
        q.c c2 = switch (a2.a()) {
            case 0 -> cyou.rimuru.shootingstardemo.mc1211.client.fx.q.G.a(a2.b().a(4.258750455597227E-5)).a(1.5812501680078302E-5);
            case 1 -> a2.b().a(1.5812501680078302E-5);
            default -> a2.b();
        };
        boolean bl2 = bl = f2 >= 96.0f && f2 < 135.0f;
        if (bl) {
            for (int i2 = 0; i2 < 7; ++i2) {
                arrayList.add(new b(cyou.rimuru.shootingstardemo.mc1211.star.c.b[i2], String.format(Locale.ROOT, "%s UMa \u00b7 %.0f LY", cyou.rimuru.shootingstardemo.mc1211.star.c.c[i2], cyou.rimuru.shootingstardemo.mc1211.star.c.e[i2]), cyou.rimuru.shootingstardemo.mc1211.client.fx.m.a(i2, d2).b(c2)));
            }
        }
        if (a2.a() == 2 && bl && f2 >= 130.0f) {
            arrayList.add(new b("SOL", "TARGET \u00b7 SOL-3", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.aq.b(c2)));
        }
        return arrayList;
    }

    public static double c(float f2, double d2) {
        a a2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.m.a(f2, d2);
        return switch (a2.a()) {
            case 0 -> a2.b().a() * 6371.0;
            case 1 -> a2.b().b(cyou.rimuru.shootingstardemo.mc1211.client.fx.q.G).a() * 1.495978707E8;
            default -> a2.b().a() * 9.4607E12;
        };
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

