/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.cinematic;

import cyou.rimuru.shootingstardemo.mc1211.ShootingStarDemo;
import cyou.rimuru.shootingstardemo.mc1211.client.cinematic.a;
import cyou.rimuru.shootingstardemo.mc1211.client.cinematic.b;
import cyou.rimuru.shootingstardemo.mc1211.client.cinematic.c;
import cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d;
import cyou.rimuru.shootingstardemo.mc1211.client.cinematic.g;
import cyou.rimuru.shootingstardemo.mc1211.client.cinematic.i;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.u;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;

public final class l
implements b {
    private static final boolean a = System.getenv("AINZ_CAMERA_LOG") != null;
    private static final int b = 4;
    private static final double c = 2.5;
    private static final double d = 3.0;
    private static final double e = 28.0;
    private static final double f = 3.0;
    private static final double g = 0.06;
    private static final double h = 0.3;
    private static final int i = -1;
    private static final int j = 0;
    private static final int k = 1;
    private final u l;
    private float[] m;
    private int n;
    private int o;
    private int p;
    private List<double[]> q;
    private int r = -1;
    private int s = 0;
    private Vec3 t = Vec3.ZERO;
    private Vec3 u = Vec3.ZERO;
    private float v = Float.NaN;
    private Vec3 w;
    private Vec3 x;
    private float y = Float.NaN;
    private float z;
    private float A;
    private float B;
    private int C;
    private int D;
    private int E;
    private int F;
    private float G;
    private float H;
    private float I;
    private float J;
    private float K;
    private final StringBuilder L = new StringBuilder();
    private static String M = "";

    public l(u u2) {
        this.l = u2;
    }

    public static String a() {
        return M;
    }

    private static double b() {
        return Math.max(96.0, (double)Minecraft.getInstance().options.getEffectiveRenderDistance() * 16.0 - 26.0);
    }

    public Vec3 a(i i2, float f2, Vec3 vec3) {
        Vec3 vec32 = i2.c(f2);
        if (this.m == null) {
            this.a(vec32);
        }
        double d2 = cyou.rimuru.shootingstardemo.mc1211.client.cinematic.l.b();
        double d3 = 0.8 * d2;
        Vec3 vec33 = new Vec3(vec3.x - vec32.x, 0.0, vec3.z - vec32.z);
        double d4 = vec33.length();
        Vec3 vec34 = vec3;
        if (d4 > d3) {
            double d5 = d3 + (d2 - d3) * (1.0 - Math.exp(-(d4 - d3) / (d2 - d3)));
            vec34 = new Vec3(vec32.x + vec33.x / d4 * d5, vec3.y, vec32.z + vec33.z / d4 * d5);
        }
        float f3 = cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.a(i2, f2);
        double d6 = this.a(vec34.x, vec34.z, Float.isNaN(f3) ? 0.0f : f3);
        return Double.isNaN(d6) || vec34.y >= d6 + 2.5 ? vec34 : new Vec3(vec34.x, d6 + 2.5, vec34.z);
    }

    private void a(Vec3 vec3) {
        int n2;
        int n3;
        int n4;
        int n5;
        ClientLevel clientLevel = Minecraft.getInstance().level;
        int n6 = (int)Math.ceil((cyou.rimuru.shootingstardemo.mc1211.client.cinematic.l.b() + 48.0) / 4.0);
        this.p = n6 * 2 + 1;
        this.n = Mth.floor((double)vec3.x) - n6 * 4;
        this.o = Mth.floor((double)vec3.z) - n6 * 4;
        float[] fArray = new float[this.p * this.p];
        Arrays.fill(fArray, Float.NaN);
        if (clientLevel != null) {
            for (n5 = 0; n5 < this.p; ++n5) {
                for (n4 = 0; n4 < this.p; ++n4) {
                    int n7 = this.n + n5 * 4;
                    n3 = this.o + n4 * 4;
                    if (!clientLevel.hasChunk(n7 >> 4, n3 >> 4)) continue;
                    n2 = clientLevel.getHeight(Heightmap.Types.MOTION_BLOCKING, n7, n3);
                    fArray[n5 * this.p + n4] = n2 <= clientLevel.getMinBuildHeight() + 1 ? Float.NaN : (float)n2;
                }
            }
        }
        this.m = new float[fArray.length];
        for (n5 = 0; n5 < this.p; ++n5) {
            for (n4 = 0; n4 < this.p; ++n4) {
                float f2 = Float.NaN;
                for (n3 = Math.max(0, n5 - 1); n3 <= Math.min(this.p - 1, n5 + 1); ++n3) {
                    for (n2 = Math.max(0, n4 - 1); n2 <= Math.min(this.p - 1, n4 + 1); ++n2) {
                        float f3 = fArray[n3 * this.p + n2];
                        if (Float.isNaN(f3) || !Float.isNaN(f2) && !(f3 > f2)) continue;
                        f2 = f3;
                    }
                }
                this.m[n5 * this.p + n4] = f2;
            }
        }
    }

    private double a(double d2, double d3) {
        double d4 = (d2 - (double)this.n) / 4.0;
        double d5 = (d3 - (double)this.o) / 4.0;
        int n2 = Mth.floor((double)d4);
        int n3 = Mth.floor((double)d5);
        if (n2 < 0 || n3 < 0 || n2 >= this.p - 1 || n3 >= this.p - 1) {
            return Double.NaN;
        }
        double d6 = d4 - (double)n2;
        double d7 = d5 - (double)n3;
        double d8 = this.m[n2 * this.p + n3];
        double d9 = this.m[(n2 + 1) * this.p + n3];
        double d10 = this.m[n2 * this.p + n3 + 1];
        double d11 = this.m[(n2 + 1) * this.p + n3 + 1];
        if (Double.isNaN(d8) || Double.isNaN(d9) || Double.isNaN(d10) || Double.isNaN(d11)) {
            double d12 = Double.NaN;
            for (double d13 : new double[]{d8, d9, d10, d11}) {
                d12 = Double.isNaN(d12) || d13 > d12 ? (Double.isNaN(d13) ? d12 : d13) : d12;
            }
            return d12;
        }
        return Mth.lerp((double)d7, (double)Mth.lerp((double)d6, (double)d8, (double)d9), (double)Mth.lerp((double)d6, (double)d10, (double)d11));
    }

    private double a(double d2, double d3, float f2) {
        double d4 = this.a(d2, d3);
        int n2 = this.l.b(d2, d3);
        if (n2 < 0 || f2 < (float)this.l.f(n2)) {
            return d4;
        }
        double d5 = (double)this.l.c(n2) + 1.0;
        if (Double.isNaN(d4)) {
            return d5;
        }
        return Mth.lerp((double)cyou.rimuru.shootingstardemo.mc1211.client.cinematic.l.a((f2 - (float)this.l.f(n2)) / 20.0f), (double)d4, (double)Math.min(d4, d5));
    }

    @Override
    public a a(c c2, a a2, float f2, float f3) {
        Object object;
        Object object2;
        Object object3;
        boolean bl;
        int n2 = c2.d(f2);
        g g2 = c2.e.get(n2);
        boolean bl2 = bl = n2 != this.r && g2.c() <= 0;
        if (this.q == null && this.l.v()) {
            List<double[]> path = new ArrayList<>();
            this.l.a(path::add);
            this.q = path;
            this.r = -1;
        }
        if (n2 != this.r) {
            this.c();
            this.r = n2;
            this.s = this.a(c2, g2, f3);
        }
        Vec3 camera = a2.c();
        Vec3 adjusted = camera;
        if (this.s != 0 && this.q != null) {
            double d2 = this.s == -1 ? cyou.rimuru.shootingstardemo.mc1211.client.cinematic.l.a(camera.y, this.a(camera.x, camera.z, g2.b()), 3.0) : cyou.rimuru.shootingstardemo.mc1211.client.cinematic.l.b(camera.y, this.b(camera.x, camera.z, g2.b()), 3.0);
            adjusted = new Vec3(camera.x, d2, camera.z);
        }
        Vec3 vec3 = this.a(adjusted, f2);
        Vec3 vec32 = vec3.subtract(camera);
        float f4 = Float.isNaN(this.v) ? 0.0f : (f2 - this.v) / 20.0f;
        this.v = f2;
        if (bl || f4 < 0.0f || f4 > 0.5f) {
            this.t = vec32;
            this.u = Vec3.ZERO;
        } else if (f4 > 0.0f) {
            boolean bl3 = vec32.lengthSqr() > this.t.lengthSqr() + 0.25;
            double[] xs = cyou.rimuru.shootingstardemo.mc1211.client.cinematic.l.a(this.t.x, this.u.x, vec32.x, bl3 ? 0.06 : 0.3, f4);
            double[] ys = cyou.rimuru.shootingstardemo.mc1211.client.cinematic.l.a(this.t.y, this.u.y, vec32.y, bl3 ? 0.06 : 0.3, f4);
            double[] zs = cyou.rimuru.shootingstardemo.mc1211.client.cinematic.l.a(this.t.z, this.u.z, vec32.z, bl3 ? 0.06 : 0.3, f4);
            this.t = new Vec3(xs[0], ys[0], zs[0]);
            this.u = new Vec3(xs[1], ys[1], zs[1]);
        }
        Vec3 vec33 = camera.add(this.t);
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null && cyou.rimuru.shootingstardemo.mc1211.client.cinematic.l.a(level, vec33)) {
            Vec3 probe = vec33;
            for (int i2 = 1; i2 <= 96 && cyou.rimuru.shootingstardemo.mc1211.client.cinematic.l.a(level, probe); ++i2) {
                probe = vec33.add(0.0, (double)i2, 0.0);
            }
            this.t = this.t.add(probe.subtract(vec33));
            this.u = Vec3.ZERO;
            vec33 = probe;
            ++this.F;
        }
        this.a(level, vec33, f2, bl || f2 - (float)g2.a() < (float)Math.max(2, g2.c()));
        return new a(vec33, a2.d(), a2.e(), a2.f());
    }

    private int a(c c2, g g2, float f2) {
        int n2;
        if (this.q == null || this.q.isEmpty()) {
            return 0;
        }
        double d2 = 0.0;
        double d3 = 0.0;
        boolean bl = false;
        for (n2 = 0; n2 <= 16; ++n2) {
            Vec3 vec3 = g2.d().pose(c2.d, f2, (float)n2 / 16.0f).c();
            for (double[] dArray : this.q) {
                if (!(dArray[6] - 10.0 <= (double)g2.b()) || !cyou.rimuru.shootingstardemo.mc1211.client.cinematic.l.a(dArray, vec3, 3.0)) continue;
                bl = true;
                break;
            }
            d2 += Math.max(0.0, vec3.y - this.a(vec3.x, vec3.z, g2.b()));
            d3 += Math.max(0.0, this.b(vec3.x, vec3.z, g2.b()) - vec3.y);
        }
        int n3 = !bl ? 0 : (n2 = d2 <= d3 ? -1 : 1);
        if (a || n2 != 0) {
            this.L.append(String.format(Locale.ROOT, " %d%s", this.r, n2 == -1 ? "u" : (n2 == 1 ? "o" : "")));
        }
        return n2;
    }

    private double a(double d2, double d3, int n2) {
        return this.a(d2, d3, n2, true);
    }

    private double b(double d2, double d3, int n2) {
        return this.a(d2, d3, n2, false);
    }

    private double a(double d2, double d3, int n2, boolean bl) {
        double d4 = Double.NaN;
        ArrayList<Double> arrayList = new ArrayList<Double>();
        for (double[] dArray : this.q) {
            double d5;
            double d6;
            double d7;
            if (dArray[6] - 10.0 > (double)n2 || (d7 = Math.sqrt((d6 = Math.max(0.0, Math.max(dArray[0] - d2, d2 - dArray[3]))) * d6 + (d5 = Math.max(0.0, Math.max(dArray[2] - d3, d3 - dArray[5]))) * d5)) >= 28.0) continue;
            double d8 = 12.0 * (d7 / 6.0) * (d7 / 6.0) / Math.max(1.0E-4, 1.0 - d7 / 28.0 * (d7 / 28.0));
            double d9 = bl ? dArray[1] - 3.0 - 2.0 + d8 : -(dArray[4] + 3.0 + 2.0 - d8);
            arrayList.add(d9);
            d4 = Double.isNaN(d4) ? d9 : Math.min(d4, d9);
        }
        if (Double.isNaN(d4)) {
            return bl ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
        }
        double d10 = 0.0;
        Iterator iterator = arrayList.iterator();
        while (iterator.hasNext()) {
            double d11 = (Double)iterator.next();
            d10 += Math.exp(-(d11 - d4) / 3.0);
        }
        double d12 = d4 - 3.0 * Math.log(d10);
        return bl ? d12 : -d12;
    }

    private Vec3 a(Vec3 vec3, float f2) {
        ArrayList<double[]> arrayList = new ArrayList<double[]>();
        this.l.a((double[] dArray) -> {
            if (dArray[6] - 10.0 <= (double)f2) {
                arrayList.add(dArray);
            }
        });
        Vec3 vec32 = vec3;
        for (int i2 = 0; i2 < 4; ++i2) {
            boolean bl = false;
            for (double[] dArray2 : arrayList) {
                if (!cyou.rimuru.shootingstardemo.mc1211.client.cinematic.l.a(dArray2, vec32, 3.0)) continue;
                double d2 = (vec32.y - (dArray2[1] - 3.0)) * (this.s == 1 ? 1.6 : 1.0);
                double d3 = (dArray2[4] + 3.0 - vec32.y) * (this.s == -1 ? 1.6 : 1.0);
                double d4 = (vec32.x - (dArray2[0] - 3.0)) * 1.25;
                double d5 = (dArray2[3] + 3.0 - vec32.x) * 1.25;
                double d6 = (vec32.z - (dArray2[2] - 3.0)) * 1.25;
                double d7 = (dArray2[5] + 3.0 - vec32.z) * 1.25;
                double d8 = Math.min(Math.min(d2, d3), Math.min(Math.min(d4, d5), Math.min(d6, d7)));
                vec32 = d8 == d2 ? new Vec3(vec32.x, dArray2[1] - 3.0 - 0.01, vec32.z) : (d8 == d3 ? new Vec3(vec32.x, dArray2[4] + 3.0 + 0.01, vec32.z) : (d8 == d4 ? new Vec3(dArray2[0] - 3.0 - 0.01, vec32.y, vec32.z) : (d8 == d5 ? new Vec3(dArray2[3] + 3.0 + 0.01, vec32.y, vec32.z) : (d8 == d6 ? new Vec3(vec32.x, vec32.y, dArray2[2] - 3.0 - 0.01) : new Vec3(vec32.x, vec32.y, dArray2[5] + 3.0 + 0.01)))));
                bl = true;
            }
            if (!bl) break;
        }
        return vec32;
    }

    private static boolean a(double[] dArray, Vec3 vec3, double d2) {
        return vec3.x > dArray[0] - d2 && vec3.x < dArray[3] + d2 && vec3.y > dArray[1] - d2 && vec3.y < dArray[4] + d2 && vec3.z > dArray[2] - d2 && vec3.z < dArray[5] + d2;
    }

    private static boolean a(ClientLevel clientLevel, Vec3 vec3) {
        BlockPos blockPos = BlockPos.containing((Position)vec3);
        return blockPos.getY() < clientLevel.getMinBuildHeight() || !clientLevel.getBlockState(blockPos).getVisualShape((BlockGetter)clientLevel, blockPos, CollisionContext.empty()).isEmpty();
    }

    private static double[] a(double d2, double d3, double d4, double d5, double d6) {
        double d7 = 2.0 / d5;
        double d8 = d7 * d6;
        double d9 = 1.0 / (1.0 + d8 + 0.48 * d8 * d8 + 0.235 * d8 * d8 * d8);
        double d10 = d2 - d4;
        double d11 = (d3 + d7 * d10) * d6;
        double d12 = d4 + (d10 + d11) * d9;
        return new double[]{d12, (d3 - d7 * d11) * d9};
    }

    private static double a(double d2, double d3, double d4) {
        if (Double.isInfinite(d3)) {
            return d3 > 0.0 ? d2 : d3;
        }
        double d5 = Math.min(d2, d3);
        return d5 - d4 * Math.log1p(Math.exp(-Math.abs(d2 - d3) / d4));
    }

    private static double b(double d2, double d3, double d4) {
        return -cyou.rimuru.shootingstardemo.mc1211.client.cinematic.l.a(-d2, -d3, d4);
    }

    private static float a(float f2) {
        f2 = Mth.clamp((float)f2, (float)0.0f, (float)1.0f);
        return f2 * f2 * (3.0f - 2.0f * f2);
    }

    private void a(ClientLevel clientLevel, Vec3 vec3, float f2, boolean bl) {
        float f3;
        ++this.D;
        if (clientLevel != null && cyou.rimuru.shootingstardemo.mc1211.client.cinematic.l.a(clientLevel, vec3)) {
            ++this.E;
            ++this.C;
        }
        this.B = Math.max(this.B, (float)this.t.length());
        this.K = Math.max(this.K, (float)this.t.length());
        float f4 = f3 = Float.isNaN(this.y) ? 0.0f : f2 - this.y;
        if (this.w != null && f3 > 0.001f && f3 < 2.0f && !bl) {
            Vec3 vec32 = vec3.subtract(this.w).scale(1.0 / (double)f3);
            float f5 = (float)vec32.length();
            if (f5 > this.G) {
                this.G = f5;
                this.H = f2;
            }
            this.z = Math.max(this.z, f5);
            if (this.x != null) {
                float f6 = (float)vec32.subtract(this.x).length() / f3;
                if (f6 > this.I) {
                    this.I = f6;
                    this.J = f2;
                }
                this.A = Math.max(this.A, f6);
            }
            this.x = vec32;
        } else if (bl) {
            this.x = null;
        }
        this.w = vec3;
        this.y = f2;
        M = String.format(Locale.ROOT, "camera frames %d, in solid %d, rescued %d, max step %.1f/t at %.0f, max accel %.1f/t2 at %.0f, max push %.1f, layers%s", this.D, this.E, this.F, Float.valueOf(this.G), Float.valueOf(this.H), Float.valueOf(this.I), Float.valueOf(this.J), Float.valueOf(this.K), this.L);
    }

    private void c() {
        if (a && this.r >= 0) {
            ShootingStarDemo.LOGGER.a(String.format(Locale.ROOT, "[tess camera] shot %d: max step %.2f/t, max accel %.2f/t2, max push %.2f, in solid %d, layer %d", this.r, Float.valueOf(this.z), Float.valueOf(this.A), Float.valueOf(this.B), this.C, this.s), new Object[0]);
            ShootingStarDemo.LOGGER.a("[tess camera] " + M, new Object[0]);
        }
        this.z = 0.0f;
        this.A = 0.0f;
        this.B = 0.0f;
        this.C = 0;
    }
}

