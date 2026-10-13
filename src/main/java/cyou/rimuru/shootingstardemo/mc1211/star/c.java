/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.star;

public final class c {
    public static final int a = 7;
    public static final String[] b = new String[]{"DUBHE", "MERAK", "PHECDA", "MEGREZ", "ALIOTH", "MIZAR", "ALKAID"};
    public static final String[] c = new String[]{"\u03b1", "\u03b2", "\u03b3", "\u03b4", "\u03b5", "\u03b6", "\u03b7"};
    public static final int d = 3;
    public static final double[] e = new double[]{123.0, 79.0, 83.0, 81.0, 83.0, 83.0, 104.0};
    public static final double[] f = new double[]{1.79, 2.37, 2.44, 3.31, 1.77, 2.23, 1.86};
    static final double[] g = new double[]{11.0621, 11.0307, 11.8972, 12.2571, 12.9005, 13.3988, 13.7923};
    static final double[] h = new double[]{61.7508, 56.3825, 53.6947, 57.0325, 55.9598, 54.9254, 49.3133};
    public static final int[][] i = new int[][]{{0, 1}, {1, 2}, {2, 3}, {3, 0}, {3, 4}, {4, 5}, {5, 6}};
    public static final double j = 4.0;
    public static final double k = 1440.0;
    public static final double l = 3600.0;
    private static final double[][] m = cyou.rimuru.shootingstardemo.mc1211.star.c.a();

    private c() {
    }

    private static double[][] a() {
        double d2;
        double d3 = 0.0;
        double d4 = 0.0;
        for (int i2 = 0; i2 < 7; ++i2) {
            d3 += g[i2] / 7.0;
            d4 += h[i2] / 7.0;
        }
        double d5 = Math.toRadians(d3 * 15.0);
        double d6 = Math.toRadians(d4);
        double[][] dArray = new double[7][2];
        for (int i3 = 0; i3 < 7; ++i3) {
            double d7 = Math.toRadians(g[i3] * 15.0);
            double d8 = Math.toRadians(h[i3]);
            double d9 = Math.sin(d6) * Math.sin(d8) + Math.cos(d6) * Math.cos(d8) * Math.cos(d7 - d5);
            double d10 = Math.cos(d8) * Math.sin(d7 - d5) / d9;
            double d11 = (Math.cos(d6) * Math.sin(d8) - Math.sin(d6) * Math.cos(d8) * Math.cos(d7 - d5)) / d9;
            dArray[i3][0] = -d10;
            dArray[i3][1] = -d11;
        }
        double d12 = dArray[6][0] - dArray[0][0];
        double d13 = dArray[6][1] - dArray[0][1];
        double d14 = -Math.atan2(d13, d12);
        double d15 = Math.cos(d14);
        double d16 = Math.sin(d14);
        double d17 = 1.0E9;
        double d18 = -1.0E9;
        for (double[] dArray2 : dArray) {
            d2 = dArray2[0] * d15 - dArray2[1] * d16;
            double d19 = dArray2[0] * d16 + dArray2[1] * d15;
            dArray2[0] = d2;
            dArray2[1] = d19;
            d17 = Math.min(d17, d2);
            d18 = Math.max(d18, d2);
        }
        double d20 = d18 - d17;
        double d21 = dArray[3][0];
        d2 = dArray[3][1];
        for (double[] dArray3 : dArray) {
            dArray3[0] = (dArray3[0] - d21) / d20;
            dArray3[1] = (dArray3[1] - d2) / d20;
        }
        return dArray;
    }

    public static double a(double d2, double d3, double d4, double d5) {
        double d6 = d2 - d4;
        double d7 = d3 - d5;
        if (d6 * d6 + d7 * d7 < 1.0) {
            return 0.0;
        }
        return Math.atan2(d7, d6) + 1.5707963267948966;
    }

    public static double[] a(int n2, double d2, double d3) {
        double d4 = Math.cos(d2);
        double d5 = Math.sin(d2);
        double d6 = m[n2][0] * cyou.rimuru.shootingstardemo.mc1211.star.c.a(d3);
        double d7 = m[n2][1] * cyou.rimuru.shootingstardemo.mc1211.star.c.a(d3);
        return new double[]{d6 * d4 - d7 * d5, d6 * d5 + d7 * d4};
    }

    public static double a(double d2) {
        return 360.0 * d2;
    }

    public static double b(double d2) {
        return 900.0 * d2;
    }

    public static double a(int n2, double d2) {
        return (16.0 + (3.4 - f[n2]) * 12.0) * d2;
    }

    public static double b(int n2, double d2) {
        return cyou.rimuru.shootingstardemo.mc1211.star.c.a(n2, d2) * 0.45;
    }

    public static double[] c(int n2, double d2) {
        double[] dArray = cyou.rimuru.shootingstardemo.mc1211.star.c.a(n2, d2, 4.0);
        double d3 = Math.sqrt(dArray[0] * dArray[0] + 1.296E7 + dArray[1] * dArray[1]);
        return new double[]{dArray[0] / d3, 3600.0 / d3, dArray[1] / d3};
    }
}

