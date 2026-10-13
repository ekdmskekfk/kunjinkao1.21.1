/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.cinematic;

import cyou.rimuru.shootingstardemo.mc1211.client.cinematic.a;
import cyou.rimuru.shootingstardemo.mc1211.client.cinematic.c;
import cyou.rimuru.shootingstardemo.mc1211.client.cinematic.g;
import cyou.rimuru.shootingstardemo.mc1211.client.cinematic.i;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.o;
import cyou.rimuru.shootingstardemo.mc1211.star.l;
import java.util.ArrayList;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public final class h {
    private h() {
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

    public static c a(i i3, o o2) {
        l l2 = l.a;
        double d2 = o2.j;
        double d3 = Mth.clamp((double)(d2 / 200.0), (double)0.3, (double)1.0);
        ArrayList<g> arrayList = new ArrayList<g>();
        int n2 = 72;
        int n3 = 352;
        arrayList.add(g.a(0, 8, (i2, f2, f3) -> {
            Vec3 vec3 = i2.a(f2);
            Vec3 vec32 = i2.a(vec3, 0.36, 1.38, 0.62);
            return new a(i2.a(vec3, h.a(f3, 1.35, 1.2), h.a(f3, 1.7, 1.6), h.a(f3, 2.1, 1.85)), vec32, 40.0f, 0.0f);
        }));
        arrayList.add(g.a(8, 30, (i2, f2, f3) -> {
            Vec3 vec3 = i2.a(f2);
            Vec3 vec32 = i2.a().add(0.0, h.a(f3, 30.0, 4.0), 0.0);
            return new a(i2.a(vec3, 1.1, 2.1, -3.2), vec32, (float)h.a(f3, 58.0, 30.0), 0.0f);
        }));
        arrayList.add(g.a(30, 46, (i2, f2, f3) -> {
            Vec3 vec3 = i2.a();
            Vec3 vec32 = h.a(i2, f2);
            Vec3 vec33 = vec3.add(vec32.scale(h.a(f3, 7.5, 6.0))).add(h.a(vec32).scale(2.8)).add(0.0, h.a(f3, 1.0, 1.4), 0.0);
            return new a(vec33, vec3.add(0.0, h.a(f3 * f3, 0.3, 24.0), 0.0), (float)h.a(f3, 64.0, 70.0), (float)h.a(f3, 0.0, 4.0));
        }));
        arrayList.add(g.a(46, n2, 3, (i2, f2, f3) -> {
            Vec3 vec3 = i2.a();
            Vec3 vec32 = h.a(i2, f2);
            double d2_l57 = 6.0 + 470.0 * (double)f3 * (double)f3;
            return new a(vec3.add(vec32.scale(4.0 + d2_l57 * 0.04)).add(0.0, d2_l57, 0.0), vec3, (float)h.a(f3, 70.0, 64.0), 0.0f);
        }));
        arrayList.add(g.a(n2, 342, (i2, f2, f3) -> {
            Vec3 vec3 = i2.a().add(0.0, 480.0, 0.0);
            return new a(vec3, vec3.add(h.a(i2, f2).scale(10.0)).add(0.0, 40.0, 0.0), 70.0f, 0.0f);
        }));
        arrayList.add(g.a(342, n3, (i2, f2, f3) -> {
            Vec3 vec3 = i2.a();
            Vec3 vec32 = h.a(i2, f2);
            Vec3 vec33 = vec3.add(vec32.scale(h.a(f3, 150.0, 135.0))).add(h.a(vec32).scale(22.0)).add(0.0, 2.5, 0.0);
            return new a(vec33, vec3.add(0.0, h.a((float)Math.sqrt(f3), 330.0, 30.0), 0.0), (float)h.a(f3, 62.0, 56.0), (float)h.a(f3, 2.0, -2.0));
        }));
        arrayList.add(g.a(n3, n3 + 22, (i2, f2, f3) -> {
            Vec3 vec3 = i2.a();
            Vec3 vec32 = h.a(i2, f2);
            Vec3 vec33 = vec3.add(vec32.scale(d2 + h.a(f3, 150.0, 140.0) * d3)).add(h.a(vec32).scale(40.0 * d3)).add(0.0, h.a(f3, 150.0, 138.0) * d3, 0.0);
            return new a(vec33, vec3.add(0.0, h.a(f3, 70.0, 90.0) * d3, 0.0), 84.0f, (float)h.a(f3, -3.0, 0.0));
        }));
        arrayList.add(g.a(n3 + 22, n3 + 70, 6, (i2, f2, f3) -> {
            Vec3 vec3 = i2.a();
            Vec3 vec32 = h.a(i2, f2);
            Vec3 vec33 = vec3.add(vec32.scale(d2 + 175.0 * d3)).add(0.0, h.a(f3, 95.0, 175.0) * d3, 0.0);
            return new a(i.a(vec33, vec3, h.a(f3, -18.0, 16.0)), vec3.add(0.0, h.a(f3, 130.0, 230.0) * d3, 0.0), (float)h.a(f3, 76.0, 80.0), 0.0f);
        }));
        arrayList.add(g.a(n3 + 70, 462, (i2, f2, f3) -> {
            Vec3 vec3 = i2.a();
            Vec3 vec32 = h.a(i2, f2);
            Vec3 vec33 = vec3.add(vec32.scale(d2 + 16.0 * d3));
            Vec3 vec34 = vec33.add(h.a(vec32).scale(h.a(f3, -24.0, 24.0) * d3)).add(0.0, 4.0, 0.0);
            Vec3 vec35 = vec3.add(vec32.scale(d2 - 30.0 * d3)).add(h.a(vec32).scale(h.a(f3, 30.0, -30.0) * d3)).add(0.0, h.a(f3, 30.0, 90.0) * d3, 0.0);
            return new a(vec34, vec35, 74.0f, (float)h.a(f3, 6.0, -6.0));
        }));
        arrayList.add(g.a(462, 494, (i2, f2, f3) -> {
            Vec3 vec3 = i2.a();
            Vec3 vec32 = h.a(i2, f2);
            Vec3 vec33 = vec3.add(vec32.scale(d2 + h.a(f3, 90.0, 70.0) * d3)).add(h.a(vec32).scale(30.0 * d3)).add(0.0, h.a(f3, 300.0, 270.0) * d3, 0.0);
            return new a(vec33, vec3.add(0.0, h.a(f3, 40.0, 0.0) * d3, 0.0), 72.0f, (float)h.a(f3, 0.0, 6.0));
        }));
        arrayList.add(g.a(494, 530, 8, (i2, f2, f3) -> {
            Vec3 vec3 = i2.a();
            Vec3 vec32 = h.a(i2, f2);
            Vec3 vec33 = vec3.add(vec32.scale(d2 + h.a(f3, 60.0, 30.0) * d3)).add(h.a(vec32).scale(h.a(f3, 20.0, -10.0) * d3)).add(0.0, h.a(f3, 150.0, 60.0) * d3, 0.0);
            return new a(vec33, vec3.add(vec32.scale(h.a(f3, 40.0, 0.0) * d3)).add(0.0, h.a(f3, -40.0, -110.0) * d3, 0.0), (float)h.a(f3, 78.0, 72.0), 0.0f);
        }));
        arrayList.add(g.a(530, 564, 10, (i2, f2, f3) -> {
            Vec3 vec3 = i2.a(f2);
            Vec3 vec32 = h.a(i2, f2);
            Vec3 vec33 = vec3.add(vec32.scale(h.a(f3, 3.2, 5.0))).add(h.a(vec32).scale(h.a(f3, 1.4, 2.2))).add(0.0, h.a(f3, 1.9, 2.8), 0.0);
            return new a(vec33, i2.a().add(0.0, -70.0, 0.0), (float)h.a(f3, 64.0, 72.0), 0.0f);
        }));
        return new c(i3, l2.f, l2.g, l2.h, 0, arrayList).a().c().a(2);
    }
}

