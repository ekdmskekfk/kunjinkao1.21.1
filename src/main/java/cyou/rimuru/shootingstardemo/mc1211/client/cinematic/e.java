/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.cinematic;

import cyou.rimuru.shootingstardemo.mc1211.client.cinematic.a;
import cyou.rimuru.shootingstardemo.mc1211.client.cinematic.c;
import cyou.rimuru.shootingstardemo.mc1211.client.cinematic.g;
import cyou.rimuru.shootingstardemo.mc1211.client.cinematic.i;
import cyou.rimuru.shootingstardemo.mc1211.client.cinematic.j;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.f;
import cyou.rimuru.shootingstardemo.mc1211.star.l;
import java.util.ArrayList;
import net.minecraft.world.phys.Vec3;

public final class e {
    private e() {
    }

    private static double a(float f2, double d2, double d3) {
        return d2 + (d3 - d2) * (double)f2;
    }

    private static Vec3 a(i i2, float f2) {
        Vec3 vec3 = i2.a(f2).subtract(i2.a());
        Vec3 vec32 = new Vec3(vec3.x, 0.0, vec3.z);
        return vec32.lengthSqr() < 1.0 ? i2.b().scale(-1.0) : vec32.normalize();
    }

    private static Vec3 a(Vec3 vec3) {
        return new Vec3(-vec3.z, 0.0, vec3.x);
    }

    public static c a(i i3, f f4) {
        l l2 = l.b;
        ArrayList<g> arrayList = new ArrayList<g>();
        int n2 = 62;
        int n3 = 300;
        int n4 = 322;
        int n5 = n4 + 19;
        double d2 = Math.max(f4.h, 0.3);
        double d3 = cyou.rimuru.shootingstardemo.mc1211.star.e.a((double)(n4 + 19), (double)f4.i);
        arrayList.add(g.a(0, 8, (i2, f2, f3) -> {
            Vec3 vec3 = i2.a(f2);
            Vec3 vec32 = i2.a(vec3, 0.36, 1.38, 0.62);
            return new a(i2.a(vec3, e.a(f3, 1.35, 1.2), e.a(f3, 1.7, 1.6), e.a(f3, 2.1, 1.85)), vec32, 40.0f, 0.0f);
        }));
        arrayList.add(g.a(8, 28, (i2, f2, f3) -> {
            Vec3 vec3 = i2.a(f2);
            Vec3 vec32 = i2.a().add(0.0, e.a(f3, 24.0, 3.0), 0.0);
            return new a(i2.a(vec3, 1.1, 2.2, -3.3), vec32, (float)e.a(f3, 60.0, 34.0), 0.0f);
        }));
        arrayList.add(g.a(28, 44, (i2, f2, f3) -> {
            Vec3 vec3 = i2.a();
            Vec3 vec32 = e.a(i2, f2);
            Vec3 vec33 = vec3.add(vec32.scale(e.a(f3, 8.0, 6.5))).add(e.a(vec32).scale(3.0)).add(0.0, e.a(f3, 1.0, 1.3), 0.0);
            return new a(j.a(vec33, 2.5), vec3.add(0.0, e.a(f3 * f3, 2.0, 160.0), 0.0), (float)e.a(f3, 62.0, 72.0), (float)e.a(f3, 0.0, -3.0));
        }));
        arrayList.add(g.a(44, n2 + 4, 3, (i2, f2, f3) -> {
            Vec3 vec3 = i2.a();
            Vec3 vec32 = e.a(i2, f2);
            double d2_l59 = 6.0 + 480.0 * (double)f3 * (double)f3;
            return new a(vec3.add(vec32.scale(4.0 + d2_l59 * 0.04)).add(0.0, d2_l59, 0.0), vec3, (float)e.a(f3, 72.0, 64.0), 0.0f);
        }));
        arrayList.add(g.a(n2 + 4, n3, (i2, f2, f3) -> {
            Vec3 vec3 = i2.a().add(0.0, 480.0, 0.0);
            return new a(vec3, vec3.add(e.a(i2, f2).scale(10.0)).add(0.0, 40.0, 0.0), 70.0f, 0.0f);
        }));
        arrayList.add(g.a(n3, n4, (i2, f2, f3) -> {
            Vec3 vec3 = i2.a();
            Vec3 vec32 = e.a(i2, f2);
            float f4_l69 = f3 * f3;
            double d3_l69 = 20.0 + 9000.0 * Math.pow(Math.max(1.0 - (double)f3, 0.0), 0.75);
            Vec3 vec33 = vec3.add(vec32.scale(e.a(f3, 160.0, 150.0) * d2)).add(e.a(vec32).scale(20.0 * d2)).add(0.0, 2.5, 0.0);
            return new a(j.a(vec33, 2.5), vec3.add(0.0, Math.min(d3_l69, 520.0) * e.a(f4_l69, 0.95, 0.8), 0.0), (float)e.a(f3, 64.0, 58.0), (float)e.a(f3, 2.0, -1.0));
        }));
        arrayList.add(g.a(n4, n4 + 7, (i2, f2, f3) -> {
            Vec3 vec3 = i2.a();
            Vec3 vec32 = e.a(i2, f2);
            Vec3 vec33 = vec3.add(vec32.scale(d3)).add(e.a(vec32).scale(-50.0 * d2)).add(0.0, e.a(f3, 390.0, 370.0) * d2, 0.0);
            return new a(j.a(vec33, 2.5), vec3.add(vec32.scale(50.0 * d2)).add(0.0, 10.0, 0.0), (float)e.a(f3, 74.0, 78.0), (float)e.a(f3, -4.0, -6.0));
        }));
        arrayList.add(g.a(n4 + 7, n4 + 13, (i2, f2, f3) -> {
            Vec3 vec3 = i2.a();
            Vec3 vec32 = e.a(i2, f2);
            Vec3 vec33 = vec3.add(vec32.scale(d3 + 18.0 * d2)).add(e.a(vec32).scale(46.0 * d2)).add(0.0, 3.0, 0.0);
            return new a(j.a(vec33, 2.5), vec3.add(0.0, e.a(f3, 240.0, 300.0), 0.0), (float)e.a(f3, 76.0, 80.0), (float)e.a(f3, 5.0, 3.0));
        }));
        arrayList.add(g.a(n4 + 13, n5, (i2, f2, f3) -> {
            Vec3 vec3 = i2.a();
            Vec3 vec32 = e.a(i2, f2);
            Vec3 vec33 = vec3.add(vec32.scale(d3)).add(e.a(vec32).scale(12.0 * d2)).add(0.0, 5.0, 0.0);
            return new a(j.a(vec33, 2.5), vec3.add(0.0, e.a(f3, 30.0, 20.0), 0.0), (float)e.a(f3, 66.0, 72.0), 0.0f);
        }));
        arrayList.add(g.a(n5, n5 + 40, 2, (i2, f2, f3) -> {
            Vec3 vec3 = i2.a();
            Vec3 vec32 = e.a(i2, f2);
            Vec3 vec33 = vec3.add(vec32.scale(d3 - 4.0 * (double)f3 * d2)).add(e.a(vec32).scale(e.a(f3, 12.0, 20.0) * d2)).add(0.0, e.a(f3 * f3, 5.0, 48.0) * d2, 0.0);
            return new a(j.a(vec33, 2.5), vec3.add(0.0, e.a(f3, 20.0, 70.0), 0.0), (float)e.a(f3, 72.0, 66.0), (float)e.a(f3, -2.0, 0.0));
        }));
        arrayList.add(g.a(n5 + 40, n5 + 100, 8, (i2, f2, f3) -> {
            Vec3 vec3 = i2.a();
            Vec3 vec32 = e.a(i2, f2);
            Vec3 vec33 = vec3.add(vec32.scale(e.a(f3, 200.0, 105.0) * d2)).add(e.a(vec32).scale(e.a(f3, -40.0, -22.0) * d2)).add(0.0, e.a(f3, 14.0, 34.0), 0.0);
            return new a(j.a(vec33, 2.5), vec3.add(0.0, e.a(f3 * f3, 40.0, 300.0), 0.0), (float)e.a(f3, 62.0, 76.0), 0.0f);
        }));
        arrayList.add(g.a(n5 + 100, n5 + 134, 10, (i2, f2, f3) -> {
            Vec3 vec3 = i2.a(f2);
            Vec3 vec32 = e.a(i2, f2);
            Vec3 vec33 = vec3.add(vec32.scale(e.a(f3, 3.2, 5.0))).add(e.a(vec32).scale(e.a(f3, 1.4, 2.2))).add(0.0, e.a(f3, 1.9, 2.8), 0.0);
            return new a(j.a(vec33, 2.5), i2.a().add(0.0, 90.0, 0.0), (float)e.a(f3, 64.0, 72.0), 0.0f);
        }));
        return new c(i3, l2.f, l2.g, l2.h, 0, arrayList).a().c().a(2);
    }
}

