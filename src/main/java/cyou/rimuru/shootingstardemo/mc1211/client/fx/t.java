/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.fx;

import cyou.rimuru.shootingstardemo.mc1211.client.fx.w;

public final class t {
    public static final int a = 48;
    public static final double b = 2.0;
    public static final float c = 130.0f;
    public static final float d = 150.0f;
    public static final float e = 210.0f;
    public static final float f = 220.0f;
    public static final float g = 262.0f;
    public static final float h = 278.0f;
    public static final float i = 290.0f;
    public static final float j = 358.0f;
    public static final float k = 398.0f;
    public static final float l = 408.0f;
    public static final float m = 426.0f;
    static final double n = 3.2;
    static final double o = 1.1;
    public static final double p = 0.234375;
    public static final double q = 0.16;
    static final double r = 0.05;
    static final double s = 0.105;
    static final double t = Math.toRadians(1.0);
    static final double u = 0.012;
    static final double v = 1.5707963267948966;
    static final double w = 1.5707963267948966 - t * 85.0;
    static final double x = 1.5707963267948966;
    static final double y = 0.018234702756410794;
    public static final String[] z = new String[]{"BETELGEUSE", "ANTARES", "ALDEBARAN", "ARCTURUS", "POLARIS", "SIRIUS A", "VEGA", "RIGEL"};
    public static final String[] A = new String[]{"M1 Ia", "M1.5 Iab", "K5 III", "K1.5 III", "F7 Ib", "A1 V", "A0 V", "B8 Ia"};
    public static final int[] B = new int[]{3500, 3600, 3900, 4300, 6000, 9900, 9600, 12100};
    static final double[] C = new double[]{0.62, 0.55, 0.3, 0.24, 0.17, 0.06, 0.075, 0.42};
    public static final int[] D = new int[]{3, 4, 0, 7, 1, 2, 5, 6};

    private t() {
    }

    static double a(double d2) {
        return Math.max(0.0, Math.min(1.0, d2));
    }

    static double b(double d2) {
        d2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(d2);
        return d2 * d2 * (3.0 - 2.0 * d2);
    }

    static double a(float f2, double d2, double d3) {
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.t.b(((double)f2 - d2) / (d3 - d2));
    }

    static double a(float f2, double d2, double d3, double d4, double d5) {
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(f2, d2, d3) * (1.0 - cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(f2, d4, d5));
    }

    static double a(double d2, double d3, double d4) {
        if (d2 <= d3) {
            return 0.0;
        }
        double d5 = d4 - d3;
        if (d2 >= d4) {
            return d5 * 0.5 + (d2 - d4);
        }
        double d6 = (d2 - d3) / d5;
        return d5 * (d6 * d6 * d6 - 0.5 * d6 * d6 * d6 * d6);
    }

    static double b(float f2, double d2, double d3) {
        return (double)f2 < d2 ? 0.0 : Math.exp(-((double)f2 - d2) / d3);
    }

    static double a(double d2, double d3, double d4, double d5, double d6) {
        d2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(d2);
        double d7 = d4 * d6;
        double d8 = d2 * d2;
        double d9 = d8 * d2;
        return d3 + (d9 - 2.0 * d8 + d2) * d7 + (-2.0 * d9 + 3.0 * d8) * (d5 - d3);
    }

    public static double a(float f2) {
        if (f2 < 210.0f) {
            return w + t * (double)(f2 - 130.0f);
        }
        if (f2 < 220.0f) {
            return cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a((double)((f2 - 210.0f) / 10.0f), w + t * 80.0, t, 1.5707963267948966, 10.0);
        }
        double d2 = f2 < 290.0f ? -0.035 * Math.exp((double)(-(f2 - 220.0f)) / 3.0) * Math.sin((double)(f2 - 220.0f) * 0.95) : 0.0;
        double d3 = 1.5707963267948966 + 0.012 * cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a((double)f2, 290.0, 302.0) + 0.006234702756410793 * cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a((double)f2, 352.0, 362.0);
        if (f2 < 398.0f) {
            return d3 + d2;
        }
        double d4 = 1.5707963267948966 + 0.012 * cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(398.0, 290.0, 302.0) + 0.006234702756410793 * cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(398.0, 352.0, 362.0);
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a((double)((f2 - 398.0f) / 10.0f), d4, 0.018234702756410794, Math.PI, 10.0);
    }

    static double b(float f2) {
        return 1.0 - cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(f2, 200.0, 214.0) + cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(f2, 290.0, 306.0) * (1.0 - cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(f2, 392.0, 404.0));
    }

    public static double c(float f2) {
        double d2 = 1.0 - 0.55 * cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(f2, 262.0, 272.0, 286.0, 298.0);
        return d2 * (1.0 - cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(f2, 404.0, 420.0));
    }

    public static double d(float f2) {
        return 1.0 - cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(f2, 406.0, 421.0);
    }

    static double[] e(float f2) {
        double d2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.t.b(f2);
        return new double[]{cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(f2), 0.22 * Math.sin((double)f2 * 0.031 + 1.3) * d2, 0.16 * Math.sin((double)f2 * 0.019 + 0.4) * d2};
    }

    static double[] a(double d2, double d3, double d4, double d5, double[] dArray, double d6) {
        double d7 = Math.cos(dArray[0]);
        double d8 = Math.sin(dArray[0]);
        double d9 = d2 * d7 - d5 * d8;
        double d10 = d2 * d8 + d5 * d7;
        d7 = Math.cos(dArray[1]);
        d8 = Math.sin(dArray[1]);
        double d11 = d3 * d7 - d10 * d8;
        double d12 = d3 * d8 + d10 * d7;
        d7 = Math.cos(dArray[2]);
        d8 = Math.sin(dArray[2]);
        double d13 = d4 * d7 - d12 * d8;
        double d14 = d4 * d8 + d12 * d7;
        double d15 = 3.2 / (3.2 - d14 * d6);
        double d16 = 3.2 / (3.2 - d6);
        double d17 = d15 / d16;
        return new double[]{d9 * d17, d11 * d17, d13 * d17, d17};
    }

    public static double[] a(int n2, float f2) {
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a((n2 & 1) == 0 ? -1.0 : 1.0, (n2 & 2) == 0 ? -1.0 : 1.0, (n2 & 4) == 0 ? -1.0 : 1.0, (n2 & 8) == 0 ? -1.0 : 1.0, cyou.rimuru.shootingstardemo.mc1211.client.fx.t.e(f2), cyou.rimuru.shootingstardemo.mc1211.client.fx.t.c(f2));
    }

    public static int a(int n2) {
        for (int i2 = 0; i2 < 8; ++i2) {
            if (D[i2] != n2) continue;
            return i2;
        }
        return 0;
    }

    public static double[] c(double d2) {
        double[] dArray = cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(new double[]{1.0, 0.15, 0.0}, new double[]{1.0, 0.3, 0.05}, cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a((d2 - 3500.0) / 800.0));
        dArray = cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(dArray, new double[]{1.0, 0.8, 0.6}, cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a((d2 - 4300.0) / 1700.0));
        dArray = cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(dArray, new double[]{0.55, 0.68, 1.0}, cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a((d2 - 6000.0) / 3700.0));
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(dArray, new double[]{0.35, 0.5, 1.0}, cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a((d2 - 9700.0) / 2400.0));
    }

    static double[] a(double[] dArray, double[] dArray2, double d2) {
        return new double[]{dArray[0] + (dArray2[0] - dArray[0]) * d2, dArray[1] + (dArray2[1] - dArray[1]) * d2, dArray[2] + (dArray2[2] - dArray[2]) * d2};
    }

    static double[] b(int n2) {
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.t.c((double)B[D[n2]]);
    }

    static double c(int n2) {
        int n3 = D[n2];
        return (double)n3 * 1000.0 + (double)(B[n3] / 100) + C[n3];
    }

    static double b(int n2, float f2) {
        int n3 = D[n2];
        double d2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(f2, 133.0 + (double)n3 * 2.0, 137.0 + (double)n3 * 2.0);
        double d3 = 1.4 * cyou.rimuru.shootingstardemo.mc1211.client.fx.t.b(f2, 133.5 + (double)n3 * 2.0, 2.5) * (f2 < 150.0f ? 1.0 : 0.0) + 1.8 * cyou.rimuru.shootingstardemo.mc1211.client.fx.t.b(f2, 221.0 + (double)n3 * 2.2, 3.0) + 1.2 * cyou.rimuru.shootingstardemo.mc1211.client.fx.t.b(f2, 292.0 + (double)n3 * 0.8, 2.5) + 0.8 * cyou.rimuru.shootingstardemo.mc1211.client.fx.t.b(f2, 236.0 + (double)n3 * 1.2, 2.0) + 0.8 * cyou.rimuru.shootingstardemo.mc1211.client.fx.t.b(f2, 248.0 + (double)n3 * 1.0, 2.0);
        return d2 * (1.0 + d3) * (1.0 - cyou.rimuru.shootingstardemo.mc1211.client.fx.t.c(n2, f2));
    }

    static double c(int n2, float f2) {
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(f2, 400.0 + (double)n2 * 1.1, 408.0 + (double)n2 * 1.1);
    }

    public static double[] d(int n2, float f2) {
        int n3 = n2 >> 1;
        double d2 = (n2 & 1) == 0 ? -1.0 : 1.0;
        double[] dArray = cyou.rimuru.shootingstardemo.mc1211.client.fx.t.e(f2);
        double d3 = cyou.rimuru.shootingstardemo.mc1211.client.fx.t.c(f2);
        double[] dArray2 = new double[4];
        dArray2[n3] = d2;
        double[] dArray3 = cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(dArray2[0], dArray2[1], dArray2[2], dArray2[3], dArray, d3);
        int n4 = n3 == 3 ? 1 : 3;
        int n5 = -1;
        int n6 = -1;
        for (int i2 = 0; i2 < 4; ++i2) {
            if (i2 == n3 || i2 == n4) continue;
            if (n5 < 0) {
                n5 = i2;
                continue;
            }
            n6 = i2;
        }
        double[] dArray4 = cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(dArray2, n5, dArray, d3);
        double[] dArray5 = cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(dArray2, n6, dArray, d3);
        double d4 = cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(dArray4);
        double d5 = cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(dArray5);
        double[] dArray6 = cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(dArray4, 1.0 / Math.max(d4, 1.0E-9));
        double[] dArray7 = cyou.rimuru.shootingstardemo.mc1211.client.fx.t.c(dArray5, cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(dArray6, cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(dArray5, dArray6)));
        dArray7 = cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(dArray7, 1.0 / Math.max(cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(dArray7), 1.0E-9));
        double[] dArray8 = cyou.rimuru.shootingstardemo.mc1211.client.fx.t.d(dArray6, dArray7);
        double d6 = (double)(n2 % 3 - 1) * 0.28 + 0.12;
        double[] dArray9 = cyou.rimuru.shootingstardemo.mc1211.client.fx.t.b(cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(dArray7, Math.cos(d6)), cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(dArray8, Math.sin(d6)));
        double d7 = 0.56 * Math.min(d4, d5);
        double d8 = cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(dArray3);
        double d9 = cyou.rimuru.shootingstardemo.mc1211.client.fx.t.b((d8 - 0.14) / 0.24);
        double d10 = cyou.rimuru.shootingstardemo.mc1211.client.fx.t.c(n2, f2);
        double d11 = 1.0 - d10;
        double d12 = d7 * (0.15 + 0.85 * d9) * d11 * Math.min(1.0, 0.25 + cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(f2, 132.0 + (double)D[n2] * 2.0, 138.0 + (double)D[n2] * 2.0) * 0.75);
        double d13 = 1.0 - cyou.rimuru.shootingstardemo.mc1211.client.fx.t.b(d10 * 1.2);
        double d14 = cyou.rimuru.shootingstardemo.mc1211.client.fx.t.b(n2, f2) * d9;
        return new double[]{dArray3[0] * d13, dArray3[1] * d13, dArray3[2] * d13, d9 > 0.001 ? d12 : 0.0, dArray6[0], dArray6[1], dArray6[2], cyou.rimuru.shootingstardemo.mc1211.client.fx.t.c(n2), dArray9[0], dArray9[1], dArray9[2], d14, 1.0 - d9};
    }

    static double[] a(double[] dArray, int n2, double[] dArray2, double d2) {
        double[] dArray3 = (double[])dArray.clone();
        int n3 = n2;
        dArray3[n3] = dArray3[n3] + 1.0;
        double[] dArray4 = cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(dArray3[0], dArray3[1], dArray3[2], dArray3[3], dArray2, d2);
        int n4 = n2;
        dArray3[n4] = dArray3[n4] - 2.0;
        double[] dArray5 = cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(dArray3[0], dArray3[1], dArray3[2], dArray3[3], dArray2, d2);
        return new double[]{(dArray4[0] - dArray5[0]) * 0.5, (dArray4[1] - dArray5[1]) * 0.5, (dArray4[2] - dArray5[2]) * 0.5};
    }

    public static double f(float f2) {
        double[] dArray = new double[]{220.0, 236.0, 247.0};
        double[] dArray2 = new double[]{14.0, 9.0, 8.0};
        for (int i2 = 0; i2 < 3; ++i2) {
            double d2 = ((double)f2 - dArray[i2]) / dArray2[i2];
            if (!(d2 >= 0.0) || !(d2 <= 1.35)) continue;
            return 2.0 - d2 * 4.0;
        }
        return Double.NaN;
    }

    public static double g(float f2) {
        double d2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.t.f(f2);
        if (Double.isNaN(d2)) {
            return 0.0;
        }
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a((2.0 - d2) / 0.4) * cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a((d2 + 3.4) / 0.6);
    }

    public static double h(float f2) {
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.t.b(f2, 220.0, 3.5) + 0.6 * cyou.rimuru.shootingstardemo.mc1211.client.fx.t.b(f2, 408.0, 3.0);
    }

    static double i(float f2) {
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(f2, 215.5, 217.0, 220.0, 223.0) + 0.7 * cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(f2, 245.5, 246.5, 248.0, 250.0) + 0.9 * cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(f2, 274.0, 276.0, 279.0, 282.0) + 0.6 * cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(f2, 396.5, 398.0, 400.0, 402.0);
    }

    static double j(float f2) {
        return 0.25 + 0.9 * cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(f2, 214.0, 220.0, 256.0, 264.0) + 0.5 * cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(f2, 262.0, 266.0, 286.0, 296.0) + 2.0 * cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(f2, 398.0, 412.0);
    }

    static double k(float f2) {
        double d2 = 0.25 * (double)f2;
        d2 += 0.9 * (cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a((double)f2, 214.0, 220.0) - cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a((double)f2, 256.0, 264.0));
        d2 += 0.5 * (cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a((double)f2, 262.0, 266.0) - cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a((double)f2, 286.0, 296.0));
        return (d2 += 2.0 * cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a((double)f2, 398.0, 412.0)) / 20.0;
    }

    static double[] l(float f2) {
        double d2 = (double)f2 / 20.0;
        return new double[]{d2 * 0.9, d2 * 0.55, d2 * 0.3, d2 * 0.18};
    }

    static double m(float f2) {
        return 0.062 + 0.172375 * cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(f2, 408.0, 424.0);
    }

    public static double[] e(int n2, float f2) {
        double[] dArray = cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(n2, cyou.rimuru.shootingstardemo.mc1211.client.fx.t.l(f2));
        double d2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.t.m(f2);
        return new double[]{dArray[0] * d2, dArray[1] * d2 + 0.16 * cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(f2, 408.0, 424.0), dArray[2] * d2, dArray[3]};
    }

    public static void a(float f2, float[] fArray) {
        double d2;
        double[] dArray = cyou.rimuru.shootingstardemo.mc1211.client.fx.t.e(f2);
        double d3 = cyou.rimuru.shootingstardemo.mc1211.client.fx.t.c(f2);
        double d4 = cyou.rimuru.shootingstardemo.mc1211.client.fx.t.d(f2);
        double d5 = 0.0;
        for (int i2 = 0; i2 < 16; ++i2) {
            double[] dArray2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a((i2 & 1) == 0 ? -1.0 : 1.0, (i2 & 2) == 0 ? -1.0 : 1.0, (i2 & 4) == 0 ? -1.0 : 1.0, (i2 & 8) == 0 ? -1.0 : 1.0, dArray, d3);
            fArray[i2 * 4] = (float)dArray2[0];
            fArray[i2 * 4 + 1] = (float)dArray2[1];
            fArray[i2 * 4 + 2] = (float)dArray2[2];
            fArray[i2 * 4 + 3] = (float)dArray2[3];
            d5 = Math.max(d5, Math.sqrt(dArray2[0] * dArray2[0] + dArray2[1] * dArray2[1] + dArray2[2] * dArray2[2]) + 0.105 * dArray2[3] * d4 * 2.15 + 0.01);
        }
        double d6 = 0.0;
        double d7 = 0.0;
        double d8 = 0.0;
        double d9 = 0.0;
        for (int i3 = 0; i3 < 8; ++i3) {
            double[] dArray3 = cyou.rimuru.shootingstardemo.mc1211.client.fx.t.d(i3, f2);
            int n2 = (16 + 3 * i3) * 4;
            fArray[n2] = (float)dArray3[0];
            fArray[n2 + 1] = (float)dArray3[1];
            fArray[n2 + 2] = (float)dArray3[2];
            fArray[n2 + 3] = (float)dArray3[3];
            fArray[n2 + 4] = (float)dArray3[4];
            fArray[n2 + 5] = (float)dArray3[5];
            fArray[n2 + 6] = (float)dArray3[6];
            fArray[n2 + 7] = (float)dArray3[7];
            fArray[n2 + 8] = (float)dArray3[8];
            fArray[n2 + 9] = (float)dArray3[9];
            fArray[n2 + 10] = (float)dArray3[10];
            fArray[n2 + 11] = (float)dArray3[11];
            d2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.t.b(i3, f2) * dArray3[12] + cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(f2, 133.0 + (double)D[i3] * 2.0, 137.0 + (double)D[i3] * 2.0) * cyou.rimuru.shootingstardemo.mc1211.client.fx.t.c(i3, f2) * 1.6;
            double[] dArray4 = cyou.rimuru.shootingstardemo.mc1211.client.fx.t.b(i3);
            d6 += dArray4[0] * d2;
            d7 += dArray4[1] * d2;
            d8 += dArray4[2] * d2;
            d9 += d2;
        }
        double[] dArray5 = cyou.rimuru.shootingstardemo.mc1211.client.fx.t.l(f2);
        fArray[160] = (float)dArray5[0];
        fArray[161] = (float)dArray5[1];
        fArray[162] = (float)dArray5[2];
        fArray[163] = (float)dArray5[3];
        double d10 = cyou.rimuru.shootingstardemo.mc1211.client.fx.t.m(f2);
        d2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(f2, 131.0, 150.0);
        fArray[164] = (float)(0.16 * cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(f2, 408.0, 424.0));
        fArray[165] = (float)d10;
        fArray[166] = (float)(0.058 * (1.0 - cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(f2, 414.0, 425.0)) * (0.6 + 0.4 * cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(f2, 130.0, 138.0)));
        fArray[167] = (float)((0.35 + 0.65 * d2) * (1.0 + 1.5 * cyou.rimuru.shootingstardemo.mc1211.client.fx.t.h(f2) + 0.6 * cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(f2, 400.0, 420.0)));
        d5 = Math.max(d5, d10 * 2.9 + 0.16);
        d5 = Math.min(d5, 2.0);
        fArray[168] = f2 / 20.0f;
        fArray[169] = (float)d4;
        fArray[170] = (float)d3;
        fArray[171] = (float)d5;
        double d11 = cyou.rimuru.shootingstardemo.mc1211.client.fx.t.f(f2);
        fArray[172] = Double.isNaN(d11) ? 9.0f : (float)d11;
        fArray[173] = (float)cyou.rimuru.shootingstardemo.mc1211.client.fx.t.g(f2);
        fArray[174] = (float)cyou.rimuru.shootingstardemo.mc1211.client.fx.t.h(f2);
        fArray[175] = (float)cyou.rimuru.shootingstardemo.mc1211.client.fx.t.i(f2);
        fArray[176] = (float)cyou.rimuru.shootingstardemo.mc1211.client.fx.t.k(f2);
        fArray[177] = f2 / 20.0f * 0.035f;
        fArray[178] = (float)(0.55 + 0.45 * d2);
        fArray[179] = (float)cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(f2, 404.0, 418.0);
        double d12 = Math.max(d9, 1.0E-6);
        fArray[180] = (float)(d6 / d12);
        fArray[181] = (float)(d7 / d12);
        fArray[182] = (float)(d8 / d12);
        fArray[183] = (float)d9;
        fArray[184] = (float)d2;
        fArray[185] = (float)(cyou.rimuru.shootingstardemo.mc1211.client.fx.t.b(f2, 277.0, 3.0) * ((double)f2 > 277.0 ? 1.0 : 0.0));
        fArray[186] = (float)cyou.rimuru.shootingstardemo.mc1211.client.fx.t.b(f2, 291.0, 5.0);
        fArray[187] = (float)cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(f2, 398.0, 426.0);
        fArray[188] = (float)cyou.rimuru.shootingstardemo.mc1211.client.fx.t.j(f2);
        fArray[189] = 0.0f;
        fArray[190] = 0.0f;
        fArray[191] = 0.0f;
    }

    public static double n(float f2) {
        double[] dArray = cyou.rimuru.shootingstardemo.mc1211.client.fx.t.e(f2);
        double d2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.t.c(f2);
        double d3 = cyou.rimuru.shootingstardemo.mc1211.client.fx.t.d(f2);
        double d4 = 0.0;
        for (int i2 = 0; i2 < 16; ++i2) {
            double[] dArray2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a((i2 & 1) == 0 ? -1.0 : 1.0, (i2 & 2) == 0 ? -1.0 : 1.0, (i2 & 4) == 0 ? -1.0 : 1.0, (i2 & 8) == 0 ? -1.0 : 1.0, dArray, d2);
            d4 = Math.max(d4, Math.sqrt(dArray2[0] * dArray2[0] + dArray2[1] * dArray2[1] + dArray2[2] * dArray2[2]) + 0.105 * dArray2[3] * d3 * 2.15 + 0.01);
        }
        d4 = Math.max(d4, cyou.rimuru.shootingstardemo.mc1211.client.fx.t.m(f2) * 2.9 + 0.16);
        return Math.min(d4, 2.0);
    }

    static double a(double[] dArray, double[] dArray2) {
        return dArray[0] * dArray2[0] + dArray[1] * dArray2[1] + dArray[2] * dArray2[2];
    }

    static double a(double[] dArray) {
        return Math.sqrt(cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(dArray, dArray));
    }

    static double[] a(double[] dArray, double d2) {
        return new double[]{dArray[0] * d2, dArray[1] * d2, dArray[2] * d2};
    }

    static double[] b(double[] dArray, double[] dArray2) {
        return new double[]{dArray[0] + dArray2[0], dArray[1] + dArray2[1], dArray[2] + dArray2[2]};
    }

    static double[] c(double[] dArray, double[] dArray2) {
        return new double[]{dArray[0] - dArray2[0], dArray[1] - dArray2[1], dArray[2] - dArray2[2]};
    }

    static double[] d(double[] dArray, double[] dArray2) {
        return new double[]{dArray[1] * dArray2[2] - dArray[2] * dArray2[1], dArray[2] * dArray2[0] - dArray[0] * dArray2[2], dArray[0] * dArray2[1] - dArray[1] * dArray2[0]};
    }
}

