/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.cinematic;

import cyou.rimuru.shootingstardemo.mc1211.client.cinematic.a;
import cyou.rimuru.shootingstardemo.mc1211.client.cinematic.c;
import cyou.rimuru.shootingstardemo.mc1211.client.cinematic.g;
import cyou.rimuru.shootingstardemo.mc1211.client.cinematic.i;
import cyou.rimuru.shootingstardemo.mc1211.client.cinematic.j;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.k;
import cyou.rimuru.shootingstardemo.mc1211.star.l;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;

public final class f {
    private f() {
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

    private static Vec3 b(i i2, float f2, Vec3 vec3) {
        double d2;
        Vec3 vec32 = i2.a(f2);
        Vec3 vec33 = new Vec3(vec3.x - vec32.x, 0.0, vec3.z - vec32.z);
        double d3 = vec33.length();
        Vec3 vec34 = d3 <= (d2 = Math.max(96.0, (double)Minecraft.getInstance().options.getEffectiveRenderDistance() * 16.0 - 26.0)) ? vec3 : new Vec3(vec32.x + vec33.x / d3 * d2, vec3.y, vec32.z + vec33.z / d3 * d2);
        return j.a(vec34, 2.5);
    }

    private static a a(Vec3 vec3, Vec3 vec32, float f2, float f3) {
        return new a(j.a(vec3, vec32, 160.0), vec32, f2, f3);
    }

    private static Vec3 a(k k2) {
        Vec3 vec3 = Vec3.ZERO;
        for (int i2 = 0; i2 < 7; ++i2) {
            vec3 = vec3.add(k2.a(i2));
        }
        return vec3.scale(0.14285714285714285);
    }

    public static c a(i i3, k k2) {
        int n2;
        double d2 = k2.b();
        l l2 = l.c;
        ArrayList<g> arrayList = new ArrayList<g>();
        int n3 = 58;
        int n4 = 272;
        arrayList.add(g.a(0, 8, (i i2, float f2, float f3) -> {
            Vec3 vec3 = i2.a(f2);
            Vec3 vec32 = i2.a(vec3, 0.36, 1.38, 0.62);
            return new a(i2.a(vec3, f.a(f3, 1.35, 1.2), f.a(f3, 1.7, 1.6), f.a(f3, 2.1, 1.85)), vec32, 40.0f, 0.0f);
        }));
        arrayList.add(g.a(8, 24, (i i2, float f2, float f3) -> {
            Vec3 vec3 = i2.a(f2);
            Vec3 vec32 = i2.a(vec3, 4.0, f.a(f3, 28.0, 44.0), -f.a(f3, 20.0, 28.0));
            return new a(vec32, i2.a().add(0.0, -12.0, 0.0), (float)f.a(f3, 74.0, 80.0), 0.0f);
        }));
        arrayList.add(g.a(24, n3 - 6, 4, (i2, f2, f3) -> {
            Vec3 vec3 = i2.a(f2);
            Vec3 vec32 = i2.a(vec3, 1.2, 1.9, -1.6);
            Vec3 vec33 = f.a(k2).add(0.0, cyou.rimuru.shootingstardemo.mc1211.star.c.b(d2) * f.a(f3, 0.6, 1.0), 0.0);
            return new a(vec32, vec33, (float)f.a(f3, 84.0, 76.0), (float)f.a(f3, -4.0, 6.0));
        }));
        arrayList.add(g.a(n3 - 6, n3 + 12, 3, (i2, f2, f3) -> {
            Vec3 vec3 = f.a(k2);
            Vec3 vec32 = f.b(i2, f2, vec3.add(0.0, 20.0 + 420.0 * (double)f3 * (double)f3, 0.0));
            return new a(vec32, vec32.add(0.0, 100.0, 12.0), 76.0f, 0.0f);
        }));
        arrayList.add(g.a(n3 + 12, n4, (i i2, float f2, float f3) -> {
            Vec3 vec3 = f.b(i2, f2, f.a(k2).add(0.0, 460.0, 0.0));
            return new a(vec3, vec3.add(0.0, 40.0, 10.0), 70.0f, 0.0f);
        }));
        for (n2 = 0; n2 < 7; ++n2) {
            int n5 = cyou.rimuru.shootingstardemo.mc1211.star.i.c(n2);
            int n6 = n2 == 0 ? n4 : cyou.rimuru.shootingstardemo.mc1211.star.i.c(n2 - 1) + 6;
            int n7 = n2;
            arrayList.add(g.a(n6, n5, (i i2, float f2, float f3) -> {
                Vec3 vec3 = k2.a(n7);
                Vec3 vec32 = f.a(i2, f2, vec3);
                double d3 = cyou.rimuru.shootingstardemo.mc1211.star.c.a(n7, d2);
                Vec3 vec33 = f.b(i2, f2, vec3.add(vec32.scale(d3 + 48.0)).add(f.a(vec32).scale(40.0)).add(0.0, 3.0, 0.0));
                double d4 = cyou.rimuru.shootingstardemo.mc1211.star.c.b(d2) * Math.pow(Math.max(1.0 - (double)f3, 0.0), 0.75);
                return f.a(vec33, vec3.add(0.0, Math.max(d4 * 0.85, 14.0 * d2), 0.0), (float)f.a(f3, 72.0, 66.0), (float)f.a(f3, 2.0, -2.0));
            }));
            int n8 = n2 < 6 ? n5 + 6 : n5 + 10;
            arrayList.add(g.a(n5, n8, (i i2, float f2, float f3) -> {
                Vec3 vec3 = k2.a(n7);
                Vec3 vec32 = f.a(i2, f2, vec3);
                double d3 = cyou.rimuru.shootingstardemo.mc1211.star.c.a(n7, d2);
                Vec3 vec33 = f.b(i2, f2, vec3.add(vec32.scale(d3 * 1.25 + 30.0)).add(f.a(vec32).scale(-60.0)).add(0.0, 30.0 + d3 * 0.35, 0.0));
                return f.a(vec33, vec3.add(0.0, 6.0 * d2, 0.0), (float)f.a(f3, 76.0, 80.0), (float)f.a(f3, -3.0, 0.0));
            }));
        }
        n2 = cyou.rimuru.shootingstardemo.mc1211.star.i.c(6) + 10;
        arrayList.add(g.a(n2, 440, 4, (i2, f2, f3) -> {
            Vec3 vec3 = f.a(k2);
            Vec3 vec32 = f.a(i2, f2, vec3);
            Vec3 vec33 = vec3.add(vec32.scale(f.a(f3, 260.0, 220.0))).add(0.0, f.a(f3, 150.0, 180.0), 0.0);
            return f.a(f.b(i2, f2, i.a(vec33, vec3, f.a(f3, -14.0, 14.0))), vec3.add(0.0, -10.0 * d2, 0.0), 78.0f, 0.0f);
        }));
        arrayList.add(g.a(440, 476, (i i2, float f2, float f3) -> {
            Vec3 vec3 = f.a(k2);
            Vec3 vec32 = f.a(i2, f2, vec3);
            Vec3 vec33 = f.b(i2, f2, vec3.add(vec32.scale(f.a(f3, 300.0, 260.0))).add(f.a(vec32).scale(80.0)).add(0.0, f.a(f3, 110.0, 140.0), 0.0));
            return f.a(vec33, vec3.add(0.0, f.a(f3, 150.0, 260.0) * d2, 0.0), 88.0f, (float)f.a(f3, -3.0, 3.0));
        }));
        arrayList.add(g.a(476, 526, 8, (i2, f2, f3) -> {
            Vec3 vec3 = k2.a(4);
            Vec3 vec32 = f.a(i2, f2, vec3);
            double d3 = cyou.rimuru.shootingstardemo.mc1211.star.c.a(4, d2);
            Vec3 vec33 = f.b(i2, f2, vec3.add(vec32.scale(d3 * f.a(f3, 0.8, 0.6))).add(f.a(vec32).scale(f.a(f3, 30.0, 50.0))).add(0.0, f.a(f3, 24.0, 48.0), 0.0));
            return f.a(vec33, vec3.add(0.0, f.a(f3 * f3, 10.0, 220.0) * d2, 0.0), (float)f.a(f3, 70.0, 80.0), 0.0f);
        }));
        arrayList.add(g.a(526, 556, 10, (i2, f2, f3) -> {
            Vec3 vec3 = i2.a(f2);
            Vec3 vec32 = f.a(i2, f2, f.a(k2)).scale(-1.0);
            Vec3 vec33 = vec3.add(vec32.scale(-f.a(f3, 3.2, 5.0))).add(f.a(vec32).scale(f.a(f3, 1.4, 2.2))).add(0.0, f.a(f3, 1.9, 2.8), 0.0);
            return new a(vec33, f.a(k2).add(0.0, 60.0 * d2, 0.0), (float)f.a(f3, 64.0, 72.0), 0.0f);
        }));
        return new c(i3, l2.f, l2.g, l2.h, 0, arrayList).a().c().a(2);
    }
}

