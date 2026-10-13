/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.cinematic;

import cyou.rimuru.shootingstardemo.mc1211.client.cinematic.a;
import cyou.rimuru.shootingstardemo.mc1211.client.cinematic.c;
import cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d;
import cyou.rimuru.shootingstardemo.mc1211.client.cinematic.g;
import cyou.rimuru.shootingstardemo.mc1211.client.cinematic.i;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.u;
import cyou.rimuru.shootingstardemo.mc1211.star.l;
import java.util.ArrayList;
import net.minecraft.world.phys.Vec3;

public final class k {
    private k() {
    }

    private static double a(float f2, double d2, double d3) {
        return d2 + (d3 - d2) * (double)f2;
    }

    private static Vec3 a(i i2, float f2, Vec3 vec3) {
        Vec3 vec32 = i2.a(f2).subtract(vec3);
        Vec3 vec33 = new Vec3(vec32.x, 0.0, vec32.z);
        return vec33.lengthSqr() < 1.0 ? i2.b().scale(-1.0) : vec33.normalize();
    }

    private static Vec3 a(Vec3 vec3) {
        return new Vec3(-vec3.z, 0.0, vec3.x);
    }

    private static float a(float f2) {
        return f2 < 0.5f ? (float)Math.cbrt(f2 / 4.0f) : 1.0f - (float)Math.cbrt(2.0f * (1.0f - f2)) / 2.0f;
    }

    private static Vec3 a(Vec3 vec3, Vec3 vec32, Vec3 vec33, double d2) {
        double d3 = 1.0 - d2;
        return vec3.scale(d3 * d3).add(vec32.scale(2.0 * d3 * d2)).add(vec33.scale(d2 * d2));
    }

    private static float b(float f2) {
        f2 = Math.max(0.0f, Math.min(1.0f, f2));
        return f2 * f2 * (3.0f - 2.0f * f2);
    }

    private static int a(int n2) {
        return 474 + n2 * 20;
    }

    public static c a(i i3, u u2) {
        l l2 = l.d;
        ArrayList<g> arrayList = new ArrayList<g>();
        int n2 = 40;
        int n3 = 358;
        double d2 = Math.max(u2.c(), 0.5);
        arrayList.add(g.a(0, 8, (i i2, float f2, float f3) -> {
            Vec3 vec3 = i2.a(f2);
            Vec3 vec32 = i2.a(vec3, 0.36, 1.38, 0.62);
            return new a(i2.a(vec3, k.a(f3, 1.35, 1.2), k.a(f3, 1.7, 1.6), k.a(f3, 2.1, 1.85)), vec32, 40.0f, 0.0f);
        }));
        arrayList.add(g.a(8, n2 - 6, (i i2, float f2, float f3) -> {
            Vec3 vec3 = i2.a(f2);
            Vec3 vec32 = i2.a(vec3, 2.0, k.a(f3, 14.0, 26.0), -k.a(f3, 10.0, 18.0));
            return new a(vec32, u2.i().add(0.0, k.a(f3, 0.0, 30.0), 0.0), (float)k.a(f3, 78.0, 84.0), 0.0f);
        }));
        arrayList.add(g.a(n2 - 6, n2 + 12, 3, (i2, f2, f3) -> {
            Vec3 vec3 = u2.i();
            Vec3 vec32 = k.a(i2, f2, vec3);
            Vec3 vec33 = u2.y().a(i2, f2, vec3.add(vec32.scale(60.0 * (1.0 - (double)f3))).add(0.0, 40.0 + 420.0 * (double)f3 * (double)f3, 0.0));
            return new a(vec33, vec3.add(vec32.scale(-20.0)), (float)k.a(f3, 76.0, 84.0), 0.0f);
        }));
        arrayList.add(g.a(n2 + 12, n3, (i i2, float f2, float f3) -> {
            Vec3 vec3 = u2.y().a(i2, f2, u2.i().add(0.0, 460.0, 0.0));
            return new a(vec3, vec3.add(0.0, 40.0, 10.0), 70.0f, 0.0f);
        }));
        arrayList.add(g.a(n3, 438, 2, (i2, f2, f3) -> {
            Vec3 vec3 = u2.i();
            Vec3 vec32 = k.a(i2, f2, vec3);
            float f4 = d.a(i2, f2);
            float f5 = Float.isNaN(f4) ? (float)n3 + f3 * (float)(438 - n3) : f4;
            double d3 = f5 < 398.0f ? k.a(k.b((f5 - (float)n3) / 30.0f), 4.0, 56.0) : k.a(k.b((f5 - 398.0f) / 34.0f), 56.0, 20.0);
            double d4 = k.a(k.b((f5 - 398.0f) / 30.0f), 0.0, 1.0);
            Vec3 vec33 = u2.y().a(i2, f2, vec3.add(vec32.scale((175.0 + 70.0 * d4) * d2)).add(k.a(vec32).scale((20.0 + 30.0 * d4) * d2)).add(0.0, 6.0 + 30.0 * d4, 0.0));
            Vec3 vec34 = vec32.scale(-1.0);
            double d5 = Math.toRadians(d3);
            Vec3 vec35 = vec33.add(vec34.scale(Math.cos(d5) * 100.0)).add(0.0, Math.sin(d5) * 100.0, 0.0);
            return new a(vec33, vec35, (float)k.a(f3, 84.0, 88.0), 0.0f);
        }));
        arrayList.add(g.a(438, 462, (i i2, float f2, float f3) -> {
            Vec3 vec3 = u2.i();
            Vec3 vec32 = k.a(i2, f2, vec3);
            Vec3 vec33 = u2.y().a(i2, f2, vec3.add(vec32.scale(k.a(f3, 200.0, 180.0) * d2)).add(k.a(vec32).scale(-50.0 * d2)).add(0.0, k.a(f3, 120.0, 105.0), 0.0));
            return new a(vec33, vec3.add(0.0, 10.0, 0.0), 80.0f, 0.0f);
        }));
        arrayList.add(g.a(462, k.a(0), (i i2, float f2, float f3) -> {
            Vec3 vec3 = u2.i();
            Vec3 vec32 = k.a(i2, f2, vec3);
            Vec3 vec33 = vec3.add(vec32.scale(128.0 * u2.c() + 16.0));
            Vec3 vec34 = u2.y().a(i2, f2, vec33.add(k.a(vec32).scale(k.a(f3, 150.0, 140.0))).add(0.0, 6.0, 0.0));
            return new a(vec34, vec33.add(k.a(vec32).scale(-60.0 * d2)).add(0.0, k.a(f3, 10.0, 30.0), 0.0), 78.0f, (float)k.a(f3, 3.0, -2.0));
        }));
        arrayList.add(g.a(k.a(0), k.a(1), (i i2, float f2, float f3) -> {
            Vec3 vec3 = u2.i();
            Vec3 vec32 = k.a(i2, f2, vec3);
            Vec3 vec33 = u2.y().a(i2, f2, vec3.add(vec32.scale(k.a(f3, 150.0, 140.0) * d2)).add(k.a(vec32).scale(24.0 * d2)).add(0.0, 14.0, 0.0));
            return new a(vec33, vec3.add(0.0, k.a(f3, 90.0, 140.0), 0.0), 82.0f, (float)k.a(f3, -2.0, 2.0));
        }));
        arrayList.add(g.a(k.a(1), k.a(2), 2, (i2, f2, f3) -> {
            Vec3 vec3 = u2.i();
            Vec3 vec32 = k.a(i2, f2, vec3);
            Vec3 vec33 = vec3.add(vec32.scale(210.0 * d2)).add(0.0, k.a(f3, 70.0, 110.0), 0.0);
            return new a(u2.y().a(i2, f2, i.a(vec33, vec3, k.a(f3, -24.0, 12.0))), vec3.add(0.0, 90.0, 0.0), 80.0f, 0.0f);
        }));
        arrayList.add(g.a(k.a(2), k.a(3), (i i2, float f2, float f3) -> {
            Vec3 vec3 = u2.i();
            Vec3 vec32 = k.a(i2, f2, vec3);
            Vec3 vec33 = vec3.add(vec32.scale(40.0 * d2)).add(k.a(vec32).scale(-18.0 * d2));
            Vec3 vec34 = new Vec3(vec33.x, (double)u2.a(vec33.x, vec33.z) + 2.5, vec33.z);
            Vec3 vec35 = u2.y().a(i2, f2, vec34);
            Vec3 vec36 = vec35.add(vec32.scale(-k.a(f3, 30.0, 20.0))).add(0.0, 200.0, 0.0);
            return new a(vec35, vec36, 90.0f, (float)k.a(f3, 0.0, 14.0));
        }));
        arrayList.add(g.a(k.a(3), 556, (i i2, float f2, float f3) -> {
            Vec3 vec3 = u2.i();
            Vec3 vec32 = k.a(i2, f2, vec3);
            Vec3 vec33 = u2.y().a(i2, f2, vec3.add(vec32.scale(k.a(f3, 230.0, 215.0) * d2)).add(k.a(vec32).scale(60.0 * d2)).add(0.0, k.a(f3, 150.0, 175.0), 0.0));
            return new a(vec33, vec3.add(0.0, 80.0, 0.0), 86.0f, 0.0f);
        }));
        int n4 = 556;
        int n5 = 92;
        arrayList.add(g.a(n4, n4 + n5, (i i2, float f2, float f3) -> {
            Vec3 vec3 = u2.s();
            Vec3 vec32 = u2.i();
            Vec3 vec33 = k.a(i2, f2, vec3);
            Vec3 vec34 = k.a(vec33);
            float f4 = k.a(f3) * (float)n5;
            float f5 = k.b(f4 / 30.0f);
            float f6 = c.g(Math.max(0.0f, Math.min(1.0f, (f4 - 30.0f) / ((float)n5 - 30.0f))));
            Vec3 vec35 = vec3.add(vec33.scale(k.a(f5, 70.0, 84.0) * d2)).add(vec34.scale(26.0 * d2)).add(0.0, k.a(f5, -14.0, -6.0), 0.0);
            Vec3 vec36 = vec32.add(vec33.scale(200.0 * d2)).add(vec34.scale(70.0 * d2)).add(0.0, 0.3 * (vec3.y - vec32.y), 0.0);
            Vec3 vec37 = new Vec3(k.a(0.35f, vec35.x, vec36.x), k.a(0.8f, vec35.y, vec36.y), k.a(0.35f, vec35.z, vec36.z));
            Vec3 vec38 = u2.y().a(i2, f2, k.a(vec35, vec37, vec36, (double)f6));
            Vec3 vec39 = vec3.add(0.0, -0.18 * (vec3.y - vec32.y) * (double)f6, 0.0);
            return new a(vec38, vec39, (float)k.a(f6, k.a(f5, 64.0, 74.0), 84.0), (float)k.a(f6, k.a(f5, -6.0, 3.0), 0.0));
        }));
        arrayList.add(g.a(n4 + n5, 678, 10, (i2, f2, f3) -> {
            Vec3 vec3 = i2.a(f2);
            Vec3 vec32 = k.a(i2, f2, u2.i()).scale(-1.0);
            Vec3 vec33 = vec3.add(vec32.scale(-k.a(f3, 3.2, 5.0))).add(k.a(vec32).scale(k.a(f3, 1.4, 2.2))).add(0.0, k.a(f3, 1.9, 2.8), 0.0);
            int n2_l148 = u2.b(vec3.x, vec3.z);
            if (n2_l148 >= 0 && i2.a((float)f2).y < u2.i().y - 2.0) {
                Vec3 vec34 = u2.a(n2_l148);
                double d2_l148 = (double)u2.x() * 0.5 - 1.0;
                vec33 = new Vec3(Math.max(vec34.x - d2_l148, Math.min(vec34.x + d2_l148, vec33.x)), vec33.y, Math.max(vec34.z - d2_l148, Math.min(vec34.z + d2_l148, vec33.z)));
            }
            return new a(u2.y().a(i2, f2, vec33), u2.s(), (float)k.a(f3, 64.0, 72.0), 0.0f);
        }));
        return new c(i3, l2.f, l2.g, l2.h, 0, arrayList).a().c().a(2).a(u2.y());
    }
}

