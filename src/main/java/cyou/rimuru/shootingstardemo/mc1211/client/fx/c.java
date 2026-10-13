/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.fx;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public final class c {
    private static final long a = 3600L;
    private static final long b = 1800L;
    private static final double c = 0.03;
    private static long d = -1000L;
    private static final List<a> e = new ArrayList<a>();

    private c() {
    }

    public static void a(Vec3 vec3, long l2, long l3, double d2, float f2) {
        e.add(new a(vec3.x, vec3.z, l2, l3, d2, f2));
        while (e.size() > 8) {
            e.remove(0);
        }
    }

    public static void a() {
        e.clear();
    }

    public static boolean a(long l2) {
        boolean bl;
        if (e.isEmpty()) {
            return false;
        }
        int n2 = e.size();
        e.removeIf(a2 -> l2 - a2.c > 5400L || l2 < a2.c - 200L);
        long l3 = Long.MAX_VALUE;
        for (a a3 : e) {
            long l4 = l2 - a3.c;
            l3 = Math.min(l3, l4 < a3.d + 80L ? 1L : (l4 > 3600L ? 10L : Long.MAX_VALUE));
        }
        boolean bl2 = bl = e.size() != n2 || l3 != Long.MAX_VALUE && (l2 - d >= l3 || l2 < d);
        if (bl) {
            d = l2;
        }
        return bl;
    }

    public static boolean b() {
        return !e.isEmpty();
    }

    public static boolean a(double d2, double d3, double d4) {
        for (a a2 : e) {
            double d5 = d4 - (double)a2.c;
            if (d5 < 0.0) continue;
            double d6 = d5 > 3600.0 ? 1.0 - Mth.clamp((double)((d5 - 3600.0) / 1800.0), (double)0.0, (double)1.0) : 1.0;
            double d7 = d2 + d5 * 0.03 - a2.a;
            double d8 = d3 - a2.b;
            double d9 = Math.sqrt(d7 * d7 + d8 * d8);
            double d10 = 30.0 * Mth.clamp((double)(d5 / 3.0), (double)0.0, (double)1.0);
            double d11 = d5 > (double)a2.d ? a2.e * Math.pow(Mth.clamp((double)((d5 - (double)a2.d) / 70.0), (double)0.0, (double)1.0), 0.55) : 0.0;
            double d12 = Math.max(d10, d11) * d6;
            double d13 = Math.atan2(d8, d7);
            if (!(d9 < (d12 += (cyou.rimuru.shootingstardemo.mc1211.client.fx.c.a(Math.floor(d13 * 9.0) + (double)a2.f * 7.0) - 0.5) * 18.0 * Mth.clamp((double)(d12 / 80.0), (double)0.0, (double)1.0)))) continue;
            return true;
        }
        return false;
    }

    private static double a(double d2) {
        double d3 = Math.sin(d2 * 127.1 + 311.7) * 43758.5453;
        return d3 - Math.floor(d3);
    }

    private static final class a
{
        private final double a;
        private final double b;
        private final long c;
        private final long d;
        private final double e;
        private final float f;

        private a(double x2, double z, long born, long ring, double radius, float seed) {
            this.a = x2;
            this.b = z;
            this.c = born;
            this.d = ring;
            this.e = radius;
            this.f = seed;
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

        public double a() {
            return this.a;
        }

        public double b() {
            return this.b;
        }

        public long c() {
            return this.c;
        }

        public long d() {
            return this.d;
        }

        public double e() {
            return this.e;
        }

        public float f() {
            return this.f;
        }
    }
}

