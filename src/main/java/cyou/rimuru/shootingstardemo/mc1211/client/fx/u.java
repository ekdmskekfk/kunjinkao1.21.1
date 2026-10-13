/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.fx;

import cyou.rimuru.shootingstardemo.mc1211.client.cinematic.k;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.a;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.b;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.d;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.e;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.i;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.j;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.n;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.q;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.s;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.t;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.v;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.w;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.x;
import cyou.rimuru.shootingstardemo.mc1211.client.render.f;
import cyou.rimuru.shootingstardemo.mc1211.registry.g;
import cyou.rimuru.shootingstardemo.mc1211.star.l;
import cyou.rimuru.shootingstardemo.mc1211.star.m;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

public final class u
extends n {
    static final int a = 7;
    static final int b = 9;
    static final int c = 29;
    static final int d = 438;
    static final int e = 462;
    static final int f = 562;
    static final int g = 592;
    static final int h = 8;
    static final int i = 290;
    static final int j = 398;
    static final int k = 426;
    static final int l = 8;
    private static final int[] D = new int[]{33, 35};
    private static final float E = 0.083333336f;
    private static final int[] F = new int[]{30, 35, 34, 30, 27, 32};
    private static final int[] G = new int[]{33, 27, 35, 31};
    private static final int[] H = new int[]{35, 33, 27, 34, 30, 29, 32, 31, 28, 35};
    private static final int[] I = new int[]{0, 55, 148, 207, 288, 333, 353, 397, 423, 456, 470, 491, 508, 527, 554, 586};
    private static final int J = 3;
    private static final double K = 1400.0;
    private static final double L = 2800.0;
    private static final StringBuilder M = new StringBuilder();
    static final int[] w = new int[]{0xFF222C, 16744464, 16769566, 2681940, 1869055, 5517567, 12205311};
    final double x;
    final int y;
    final int z;
    final int A;
    private final cyou.rimuru.shootingstardemo.mc1211.client.cinematic.i N;
    public final boolean B;
    private j O;
    private final int P;
    private final int Q;
    private final int R;
    private final int S;
    private final int T;
    private final int[] U = new int[64];
    private final int[] V = cyou.rimuru.shootingstardemo.mc1211.star.m.j();
    private final int[] W = new int[64];
    private final boolean[] X = new boolean[64];
    private final float[][] Y = new float[64][];
    private final int[][] Z = new int[64][];
    private boolean aa;
    private final cyou.rimuru.shootingstardemo.mc1211.client.cinematic.l ab;
    private final List<int[]>[] ac = new List[64];
    int C;
    private final Long2IntOpenHashMap ad = new Long2IntOpenHashMap();
    private int ae;

    public int b() {
        return this.C;
    }

    u(cyou.rimuru.shootingstardemo.mc1211.net.e e2) {
        super(e2);
        Minecraft minecraft = Minecraft.getInstance();
        this.x = cyou.rimuru.shootingstardemo.mc1211.star.m.a(cyou.rimuru.shootingstardemo.mc1211.star.l.d.j().a(e2.n()));
        this.y = cyou.rimuru.shootingstardemo.mc1211.star.m.b(this.x);
        this.z = this.y / 8;
        this.A = cyou.rimuru.shootingstardemo.mc1211.star.m.c(this.x);
        this.N = super.e();
        this.B = minecraft.player != null && minecraft.player.getId() == e2.c();
        this.P = cyou.rimuru.shootingstardemo.mc1211.star.m.a(this.p.x, this.y);
        this.Q = cyou.rimuru.shootingstardemo.mc1211.star.m.a(this.p.z, this.y);
        this.R = Mth.floor((double)this.p.y);
        this.T = minecraft.level != null ? minecraft.level.getMinBuildHeight() : -64;
        int n2 = minecraft.level != null ? minecraft.level.getMaxBuildHeight() - 1 : 319;
        this.S = cyou.rimuru.shootingstardemo.mc1211.star.m.a(this.R, this.x, this.T);
        this.ae = Math.min(this.S + this.A + 150, n2 - 30);
        Arrays.fill(this.W, this.R);
        this.ad.defaultReturnValue(Integer.MAX_VALUE);
        for (int i2 = 0; i2 < this.U.length; ++i2) {
            this.U[i2] = cyou.rimuru.shootingstardemo.mc1211.star.m.a(this.S, i2 % 8, i2 / 8, this.x, this.T);
        }
        this.ab = new cyou.rimuru.shootingstardemo.mc1211.client.cinematic.l(this);
    }

    public static void a(l l2, cyou.rimuru.shootingstardemo.mc1211.net.e e2) {
        u u2 = new u(e2);
        cyou.rimuru.shootingstardemo.mc1211.client.fx.x.a("tess_", u2.u);
        M.setLength(0);
        cyou.rimuru.shootingstardemo.mc1211.client.fx.e.a(u2);
        if (u2.B && cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.c()) {
            cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.a(cyou.rimuru.shootingstardemo.mc1211.client.cinematic.k.a(u2.e(), u2).a(u2.u));
        } else if (!u2.B) {
            cyou.rimuru.shootingstardemo.mc1211.client.fx.a.a(l2, false, u2.l());
        }
    }

    @Override
    protected int d() {
        return 1262;
    }

    @Override
    public double a() {
        return Math.max(3000.0, (double)this.y + 1000.0);
    }

    public double c() {
        return this.x;
    }

    @Override
    public cyou.rimuru.shootingstardemo.mc1211.client.cinematic.i e() {
        return this.N;
    }

    boolean f() {
        return cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.a(this.N) > 0.5f;
    }

    float a(float f2) {
        return cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.a(this.N, f2);
    }

    public Vec3 g() {
        return this.p;
    }

    public Vec3 i() {
        return new Vec3((double)this.P + (double)this.y * 0.5, (double)this.R, (double)this.Q + (double)this.y * 0.5);
    }

    public Vec3 s() {
        return new Vec3((double)this.P + (double)this.y * 0.5 + 0.5, (double)this.ae + 0.5, (double)this.Q + (double)this.y * 0.5 + 0.5);
    }

    public Vec3 a(int n2) {
        double d2 = (double)(this.P + n2 % 8 * this.z) + (double)this.z * 0.5;
        double d3 = (double)(this.Q + n2 / 8 * this.z) + (double)this.z * 0.5;
        float[] fArray = this.Y[n2];
        return fArray == null ? new Vec3(d2, (double)this.W[n2], d3) : new Vec3(d2, (double)fArray[0] + (double)fArray[1] * 0.5, d3);
    }

    public int t() {
        return this.S;
    }

    public int c(int n2) {
        return this.U[n2];
    }

    public int a(double d2, double d3) {
        int n2 = Mth.floor((double)((d2 - (double)this.P) / (double)this.z));
        int n3 = Mth.floor((double)((d3 - (double)this.Q) / (double)this.z));
        return n2 < 0 || n3 < 0 || n2 >= 8 || n3 >= 8 ? this.S : this.U[cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(n2, n3)];
    }

    public int d(int n2) {
        return this.R - this.U[cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(3 - n2, 3)];
    }

    public static int a(int n2, int n3) {
        return n3 * 8 + n2;
    }

    private void a(SoundEvent soundEvent, double d2) {
        float f2 = (float)Mth.clamp((double)((2800.0 - d2) / 1400.0), (double)0.0, (double)1.0);
        M.append(M.isEmpty() ? "" : " ").append(this.v).append(':').append(soundEvent.getLocation().getPath()).append(String.format(Locale.ROOT, "@%.2f", Float.valueOf(f2)));
        if (f2 < 0.01f) {
            if (this.O != null) {
                this.O.a(3);
                this.O = null;
            }
            return;
        }
        boolean bl = this.O != null && !this.O.isStopped() && Minecraft.getInstance().getSoundManager().isActive((SoundInstance)this.O);
        j j2 = new j(this.u, soundEvent.getLocation(), f2, bl ? 3 : 0, SoundInstance.Attenuation.NONE, 0.0, 0.0, 0.0, true);
        if (this.O != null) {
            this.O.a(3);
        }
        Minecraft.getInstance().getSoundManager().play((SoundInstance)j2);
        this.O = j2;
    }

    public static String u() {
        return M.toString();
    }

    @Override
    protected void h() {
        if (this.O != null) {
            Minecraft.getInstance().getSoundManager().stop((SoundInstance)this.O);
            this.O = null;
        }
    }

    Vec3 a(Vec3 vec3) {
        double d2 = Mth.clamp((double)vec3.x, (double)this.P, (double)(this.P + this.y));
        double d3 = Mth.clamp((double)vec3.z, (double)this.Q, (double)(this.Q + this.y));
        return new Vec3(d2, Mth.clamp((double)vec3.y, (double)this.R, (double)this.ae), d3);
    }

    private void a(ClientLevel clientLevel, int n2) {
        for (int i2 = 0; i2 < this.W.length; ++i2) {
            if (n2 >= this.V[i2] - 30) continue;
            int n3 = this.P + i2 % 8 * this.z;
            int n4 = this.Q + i2 / 8 * this.z;
            int n5 = Integer.MIN_VALUE;
            int n6 = Math.max(4, this.z / 16);
            for (int i3 = n3; i3 < n3 + this.z; i3 += n6) {
                for (int i4 = n4; i4 < n4 + this.z; i4 += n6) {
                    int n7;
                    int n8 = clientLevel.getHeight(Heightmap.Types.MOTION_BLOCKING, i3, i4);
                    if (n8 >= (n7 = this.ad.get(cyou.rimuru.shootingstardemo.mc1211.client.fx.u.c(i3, i4)))) {
                        n8 = this.a(clientLevel, i3, i4, n7);
                    }
                    if (n8 <= clientLevel.getMinBuildHeight() + 1) continue;
                    n5 = Math.max(n5, n8 + 1);
                }
            }
            if (n5 == Integer.MIN_VALUE) continue;
            this.W[i2] = Math.max(n5, this.S);
            this.X[i2] = true;
        }
    }

    private int[] b(int n2, int n3) {
        int n4 = n2 % 8;
        int n5 = n2 / 8;
        int n6 = Math.min(this.W[n2] - this.S, n3 - this.S);
        if (n6 <= 1 || !this.X[n2]) {
            return null;
        }
        int n7 = cyou.rimuru.shootingstardemo.mc1211.star.m.a(this.q, n4, n5);
        if (n7 == 3 && n6 > this.z + (int)Math.round(16.0 * this.x)) {
            n7 = 2;
        }
        int n8 = n7 == 3 ? this.z : n6;
        int[] nArray = cyou.rimuru.shootingstardemo.mc1211.star.m.a(this.q, n4, n5, this.x, this.S, n8, n3);
        return new int[]{nArray[0], nArray[1], n7 == 3 ? Math.min(n6, this.z) : this.z, n8};
    }

    private List<int[]> a(int n2, int[] nArray, int n3) {
        int n4 = n2 % 8;
        int n5 = n2 / 8;
        return cyou.rimuru.shootingstardemo.mc1211.star.m.a(this.q, n4, n5, this.z, nArray[3], nArray[1], this.P + n4 * this.z, this.Q + n5 * this.z, this.S + this.A, n3);
    }

    public void a(Consumer<double[]> consumer) {
        double d2;
        ClientLevel clientLevel = Minecraft.getInstance().level;
        if (clientLevel == null || !this.aa) {
            return;
        }
        int n2 = clientLevel.getMaxBuildHeight() - 1;
        for (int i2 = 0; i2 < 64; ++i2) {
            Object object;
            Object object2;
            d2 = this.P + i2 % 8 * this.z;
            double d3 = this.Q + i2 / 8 * this.z;
            float[] fArray = this.Y[i2];
            if (fArray != null) {
                consumer.accept(new double[]{d2, fArray[0], d3, d2 + (double)this.z, fArray[0] + fArray[1], d3 + (double)this.z, this.V[i2]});
                if (this.ac[i2] == null) continue;
                java.util.Iterator<int[]> iter = this.ac[i2].iterator();
                while (iter.hasNext()) {
                    int[] cell = iter.next();
                    consumer.accept(cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(cell, this.V[i2]));
                }
                continue;
            }
            if (this.V[i2] < this.v) continue;
            int[] coords = this.Z[i2];
            if (coords == null) continue;
            consumer.accept(new double[]{d2, coords[0], d3, d2 + (double)this.z, coords[0] + coords[1], d3 + (double)this.z, this.V[i2]});
            java.util.Iterator<int[]> iter2 = this.a(i2, coords, n2).iterator();
            while (iter2.hasNext()) {
                int[] nArray = iter2.next();
                consumer.accept(cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(nArray, this.V[i2]));
            }
        }
        Vec3 vec3 = this.s();
        d2 = (double)Math.round(13.0 * cyou.rimuru.shootingstardemo.mc1211.star.m.e(this.x)) + 1.0;
        consumer.accept(new double[]{vec3.x - d2, vec3.y - d2, vec3.z - d2, vec3.x + d2, vec3.y + d2, vec3.z + d2, 562.0});
    }

    private static double[] a(int[] nArray, int n2) {
        int n3 = 16;
        return new double[]{nArray[3], nArray[4], nArray[5], nArray[3] + n3, nArray[4] + n3, nArray[5] + n3, n2};
    }

    private void c(ClientLevel clientLevel) {
        for (int i2 = 0; i2 < this.Z.length; ++i2) {
            this.Z[i2] = this.b(i2, clientLevel.getMaxBuildHeight() - 1);
        }
        this.aa = true;
    }

    public boolean v() {
        return this.aa;
    }

    public double w() {
        if (!this.aa) {
            return Double.NaN;
        }
        double d2 = Double.POSITIVE_INFINITY;
        for (int i2 = 0; i2 < this.Z.length; ++i2) {
            float[] fArray = this.Y[i2];
            double d3 = fArray != null ? (double)fArray[0] : (this.Z[i2] != null ? (double)this.Z[i2][0] : Double.POSITIVE_INFINITY);
            d2 = Math.min(d2, d3);
        }
        return Double.isInfinite(d2) ? Double.NaN : d2;
    }

    public boolean e(int n2) {
        return this.Y[n2] != null;
    }

    public int x() {
        return this.z;
    }

    public int f(int n2) {
        return this.V[n2];
    }

    public int b(double d2, double d3) {
        int n2 = Mth.floor((double)((d2 - (double)this.P) / (double)this.z));
        int n3 = Mth.floor((double)((d3 - (double)this.Q) / (double)this.z));
        return n2 < 0 || n3 < 0 || n2 >= 8 || n3 >= 8 ? -1 : cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(n2, n3);
    }

    public cyou.rimuru.shootingstardemo.mc1211.client.cinematic.l y() {
        return this.ab;
    }

    private void b(ClientLevel clientLevel, int n2) {
        int[] nArray = this.b(n2, clientLevel.getMaxBuildHeight() - 1);
        if (nArray == null) {
            return;
        }
        this.Y[n2] = new float[]{nArray[0], nArray[1], nArray[2]};
        List<int[]> list = this.a(n2, nArray, clientLevel.getMaxBuildHeight() - 1);
        if (!list.isEmpty()) {
            this.ac[n2] = list;
            this.C += list.size();
            for (int[] nArray2 : list) {
                for (int i2 = nArray2[3]; i2 < nArray2[3] + 16; ++i2) {
                    for (int i3 = nArray2[5]; i3 < nArray2[5] + 16; ++i3) {
                        long l2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.u.c(i2, i3);
                        this.ad.put(l2, Math.min(this.ad.get(l2), nArray2[4]));
                    }
                }
            }
        }
    }

    private int a(ClientLevel clientLevel, int n2, int n3, int n4) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        for (int i2 = n4 - 1; i2 > this.S; --i2) {
            if (clientLevel.getBlockState((BlockPos)mutableBlockPos.set(n2, i2, n3)).isAir()) continue;
            return i2;
        }
        return this.S - 1;
    }

    private static long c(int n2, int n3) {
        return (long)n2 << 32 | (long)n3 & 0xFFFFFFFFL;
    }

    @Override
    protected void a(Minecraft minecraft, ClientLevel clientLevel, int n2) {
        int n3;
        double d2;
        double d3;
        int n4;
        if (n2 % 10 == 0 || n2 == 461) {
            this.a(clientLevel, n2);
        }
        if (n2 == 461) {
            this.c(clientLevel);
        }
        Vec3 vec3 = this.a(cyou.rimuru.shootingstardemo.mc1211.client.fx.e.e());
        if (n2 == 0 && this.B) {
            cyou.rimuru.shootingstardemo.mc1211.client.fx.q.ab.values().forEach(cyou.rimuru.shootingstardemo.mc1211.client.render.a::a);
        }
        for (n4 = 0; n4 < I.length; ++n4) {
            if (n2 != I[n4]) continue;
            this.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.aa[n4], this.f() ? 0.0 : cyou.rimuru.shootingstardemo.mc1211.client.fx.e.e().distanceTo(vec3));
        }
        if (n2 == 9) {
            for (n4 = 0; n4 < 280; ++n4) {
                double d4 = this.t.nextDouble() * (double)this.y;
                int n5 = n4 % 4;
                d3 = n5 == 0 ? (double)this.P + d4 : (n5 == 1 ? (double)(this.P + this.y) : (n5 == 2 ? (double)this.P + d4 : (double)this.P));
                d2 = n5 == 0 ? (double)this.Q : (n5 == 1 ? (double)this.Q + d4 : (n5 == 2 ? (double)(this.Q + this.y) : (double)this.Q + d4));
                cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(clientLevel, (ParticleOptions)cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(w[(int)((d3 - (double)this.P + d2 - (double)this.Q) * 7.0 / (double)(2 * this.y + 1))], 1.4f), this.a(clientLevel, d3, d2).add(0.0, 0.5, 0.0), 0.0, 0.2 + this.t.nextDouble() * 0.6, 0.0);
            }
        }
        if (n2 == 426) {
            cyou.rimuru.shootingstardemo.mc1211.client.fx.e.a(this.i(), 1.6f, 1600.0);
            Vec3 vec32 = this.i();
            for (int i2 = 0; i2 < 500; ++i2) {
                double d6 = this.t.nextDouble() * Math.PI * 2.0;
                d3 = 2.0 + this.t.nextDouble() * 5.0;
                d2 = (double)this.y * 0.5 * this.t.nextDouble();
                Vec3 vec33 = this.a(clientLevel, vec32.x + Math.cos(d6) * d2, vec32.z + Math.sin(d6) * d2);
                cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(clientLevel, (ParticleOptions)cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(i2 % 5 == 0 ? 0xFFFFFF : w[i2 % 7], 1.6f + this.t.nextFloat() * 2.0f), vec33.add(0.0, 0.5, 0.0), Math.cos(d6) * d3, 0.2 + this.t.nextDouble() * 0.8, Math.sin(d6) * d3);
            }
        }
        if (n2 >= 438 && n2 < 462) {
            this.c(clientLevel, n2);
        }
        if (n2 == 462) {
            cyou.rimuru.shootingstardemo.mc1211.client.fx.e.a(this.i(), 1.4f, 1200.0);
        }
        for (n3 = 0; n3 < 4; ++n3) {
            if (n2 != 474 + n3 * 20) continue;
            cyou.rimuru.shootingstardemo.mc1211.client.fx.e.a(this.i(), 1.0f + 0.25f * (float)n3, 1200.0);
        }
        for (n3 = 0; n3 < this.V.length; ++n3) {
            if (n2 == this.V[n3]) {
                this.b(clientLevel, n3);
                if (this.Y[n3] != null) {
                    this.e(clientLevel, n3);
                }
                if (this.ac[n3] != null) {
                    this.f(clientLevel, n3);
                }
            }
            if (this.ac[n3] != null && n2 > this.V[n3] && n2 < this.V[n3] + 40 && n2 % 3 == 0) {
                this.g(clientLevel, n3);
            }
            if (this.Y[n3] == null || n2 <= this.V[n3] || n2 >= this.V[n3] + 120 || n2 % 2 != 0) continue;
            this.a(clientLevel, n3, n2 - this.V[n3]);
        }
        if (n2 >= 462 && n2 < 622) {
            this.d(clientLevel, n2);
        }
        if (n2 == 562) {
            cyou.rimuru.shootingstardemo.mc1211.client.fx.e.a(this.s(), 1.9f, 1600.0);
            Vec3 vec34 = this.s();
            for (int i3 = 0; i3 < 700; ++i3) {
                Vec3 vec35 = new Vec3(this.d(1.0), this.d(1.0), this.d(1.0)).normalize();
                double d7 = 1.5 + this.t.nextDouble() * 4.0;
                cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(clientLevel, (ParticleOptions)cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(i3 % 5 == 0 ? 0xFFFFFF : w[i3 % 7], 2.0f + this.t.nextFloat() * 2.5f), vec34.add(vec35.scale(this.t.nextDouble() * 6.0)), vec35.x * d7, vec35.y * d7, vec35.z * d7);
            }
        }
        if (n2 > 562 && n2 < 1162 && n2 % 3 == 0) {
            Vec3 vec36 = this.s();
            cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(clientLevel, (ParticleOptions)cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(w[this.t.nextInt(7)], 0.8f), vec36.add(this.d(9.0), this.d(9.0), this.d(9.0)), this.d(0.02), this.d(0.02), this.d(0.02));
        }
    }

    private Vec3 a(ClientLevel clientLevel, double d2, double d3) {
        int n2 = clientLevel.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mth.floor((double)d2), Mth.floor((double)d3));
        return new Vec3(d2, n2 <= clientLevel.getMinBuildHeight() ? (double)this.R : (double)n2, d3);
    }

    private void c(ClientLevel clientLevel, int n2) {
        double d2 = (double)(n2 - 438) / 20.0 * 215.0 * this.x;
        for (int i2 = 0; i2 < 40; ++i2) {
            boolean bl = this.t.nextBoolean();
            int n3 = this.t.nextInt(9);
            double d3 = d2 * (this.t.nextBoolean() ? 1.0 : -1.0);
            double d4 = (double)(n3 * this.z) - (double)this.y * 0.5;
            double d5 = Math.signum(d3) * Math.max(Math.abs(d3) - Math.abs(d4), 0.0);
            double d6 = (double)this.P + (double)this.y * 0.5 + (bl ? d5 : d4);
            double d7 = (double)this.Q + (double)this.y * 0.5 + (bl ? d4 : d5);
            if (d6 < (double)this.P || d7 < (double)this.Q || d6 > (double)(this.P + this.y) || d7 > (double)(this.Q + this.y) || Math.abs(d5) < 1.0) continue;
            Vec3 vec3 = this.a(clientLevel, d6, d7);
            int n4 = (int)((d6 - (double)this.P + d7 - (double)this.Q) * 7.0 / (double)(2 * this.y + 1));
            cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(clientLevel, (ParticleOptions)cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(i2 % 3 == 0 ? 0xFFFFFF : w[Mth.clamp((int)n4, (int)0, (int)6)], 1.2f + this.t.nextFloat()), vec3.add(this.d(0.6), 0.3, this.d(0.6)), this.d(0.3), 0.3 + this.t.nextDouble() * 0.9, this.d(0.3));
        }
    }

    private void d(ClientLevel clientLevel, int n2) {
        int n3 = n2 < 474 ? 30 : 60;
        for (int i2 = 0; i2 < n3; ++i2) {
            double d2 = (double)this.P + this.t.nextDouble() * (double)this.y;
            double d3 = (double)this.Q + this.t.nextDouble() * (double)this.y;
            double d4 = (double)this.S + this.t.nextDouble() * 60.0;
            int n4 = Mth.clamp((int)((int)((d2 - (double)this.P + d3 - (double)this.Q) * 7.0 / (double)(2 * this.y + 1))), (int)0, (int)6);
            cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(clientLevel, (ParticleOptions)cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(i2 % 6 == 0 ? 0xFFFFFF : w[n4], 0.9f + this.t.nextFloat() * 1.2f), new Vec3(d2, d4, d3), this.d(0.02), 0.4 + this.t.nextDouble() * 1.2, this.d(0.02));
        }
    }

    private void e(ClientLevel clientLevel, int n2) {
        double d2;
        double d3;
        int n3;
        float[] fArray = this.Y[n2];
        double d4 = this.P + n2 % 8 * this.z;
        double d5 = this.Q + n2 / 8 * this.z;
        int n4 = Mth.clamp((int)((n2 % 8 + n2 / 8) * 7 / 15), (int)0, (int)6);
        for (n3 = 0; n3 < 90; ++n3) {
            d3 = d4 + this.t.nextDouble() * (double)this.z;
            d2 = d5 + this.t.nextDouble() * (double)this.z;
            cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(clientLevel, (ParticleOptions)cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(n3 % 4 == 0 ? 0xFFFFFF : w[n4], 1.6f + this.t.nextFloat() * 1.6f), new Vec3(d3, (double)this.U[n2] + 1.0 + this.t.nextDouble() * 4.0, d2), this.d(0.2), 1.0 + this.t.nextDouble() * 3.0, this.d(0.2));
        }
        for (n3 = 0; n3 < 120; ++n3) {
            d3 = d4 + this.t.nextDouble() * (double)this.z;
            d2 = d5 + this.t.nextDouble() * (double)this.z;
            double d6 = (double)this.U[n2] + this.t.nextDouble() * (double)Math.max(this.S - this.U[n2] + this.A, 1);
            cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(clientLevel, (ParticleOptions)new BlockParticleOption(ParticleTypes.BLOCK, this.b(d6)), new Vec3(d3, d6, d2), this.d(0.4), -0.4 - this.t.nextDouble() * 1.2, this.d(0.4));
        }
        for (n3 = 0; n3 < 60; ++n3) {
            d3 = d4 + this.t.nextDouble() * (double)this.z;
            BlockState blockState = clientLevel.getBlockState(BlockPos.containing((double)d3, (double)((double)fArray[0] + 0.5), (double)(d2 = d5 + this.t.nextDouble() * (double)this.z)));
            if (blockState.isAir()) continue;
            cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(clientLevel, (ParticleOptions)new BlockParticleOption(ParticleTypes.BLOCK, blockState), new Vec3(d3, (double)fArray[0] - 0.2, d2), this.d(0.1), -0.2, this.d(0.1));
        }
    }

    private void f(ClientLevel clientLevel, int n2) {
        int n3 = 16;
        Vec3 vec3 = this.a(n2).add(0.0, (double)this.Y[n2][1] * 0.5, 0.0);
        cyou.rimuru.shootingstardemo.mc1211.client.fx.e.a(vec3, 0.6f, 900.0);
        for (int[] nArray : this.ac[n2]) {
            int n4;
            int n5 = Math.floorMod(nArray[7] + n2 % 8 + n2 / 8, 7);
            for (n4 = 0; n4 < 18; ++n4) {
                double d2 = this.t.nextDouble() * (double)n3;
                int n6 = this.t.nextInt(12);
                double d3 = (n6 & 1) == 0 ? 0.0 : (double)n3;
                double d4 = (n6 & 2) == 0 ? 0.0 : (double)n3;
                Vec3 vec32 = switch (n6 / 4) {
                    case 0 -> new Vec3((double)nArray[3] + d2, (double)nArray[4] + d3, (double)nArray[5] + d4);
                    case 1 -> new Vec3((double)nArray[3] + d3, (double)nArray[4] + d2, (double)nArray[5] + d4);
                    default -> new Vec3((double)nArray[3] + d3, (double)nArray[4] + d4, (double)nArray[5] + d2);
                };
                cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(clientLevel, (ParticleOptions)cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(n4 % 3 == 0 ? 0xFFFFFF : w[n5], 1.4f + this.t.nextFloat() * 1.6f), vec32, this.d(0.15), this.d(0.15), this.d(0.15));
            }
            for (n4 = 0; n4 < 10; ++n4) {
                Vec3 vec33 = new Vec3((double)nArray[3] + this.t.nextDouble() * (double)n3, (double)nArray[4] - 0.2, (double)nArray[5] + this.t.nextDouble() * (double)n3);
                cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(clientLevel, (ParticleOptions)new BlockParticleOption(ParticleTypes.BLOCK, this.b((double)nArray[4] - 40.0)), vec33, this.d(0.05), -0.3, this.d(0.05));
            }
        }
    }

    private void g(ClientLevel clientLevel, int n2) {
        int n3 = 16;
        for (int[] nArray : this.ac[n2]) {
            if (this.t.nextInt(3) != 0) continue;
            int n4 = this.t.nextInt(7);
            for (int i2 = 0; i2 < 4; ++i2) {
                Vec3 vec3 = new Vec3((double)nArray[3] + (this.t.nextBoolean() ? 0.0 : (double)n3), (double)nArray[4] + this.t.nextDouble() * (double)n3, (double)nArray[5] + (this.t.nextBoolean() ? 0.0 : (double)n3));
                cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(clientLevel, (ParticleOptions)cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(w[n4], 2.2f), vec3, 0.0, 0.0, 0.0);
            }
        }
    }

    private BlockState b(double d2) {
        return (d2 > (double)(this.R - 5) ? Blocks.DIRT : (d2 > 0.0 ? (this.t.nextInt(5) == 0 ? Blocks.ANDESITE : Blocks.STONE) : (this.t.nextInt(6) == 0 ? Blocks.TUFF : Blocks.DEEPSLATE))).defaultBlockState();
    }

    private void a(ClientLevel clientLevel, int n2, int n3) {
        float[] fArray = this.Y[n2];
        double d2 = this.P + n2 % 8 * this.z;
        double d3 = this.Q + n2 / 8 * this.z;
        int n4 = Math.max(1, 8 - n3 / 16);
        for (int i2 = 0; i2 < n4; ++i2) {
            double d4;
            double d5 = d2 + this.t.nextDouble() * (double)this.z;
            BlockState blockState = clientLevel.getBlockState(BlockPos.containing((double)d5, (double)((double)fArray[0] + 0.5), (double)(d4 = d3 + this.t.nextDouble() * (double)this.z)));
            if (blockState.isAir()) continue;
            cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(clientLevel, (ParticleOptions)new BlockParticleOption(ParticleTypes.FALLING_DUST, blockState), new Vec3(d5, (double)fArray[0] - 0.1, d4), 0.0, -0.05, 0.0);
        }
    }

    @Override
    public void a(d d2, List<cyou.rimuru.shootingstardemo.mc1211.client.render.e> list) {
        float f2;
        float f3 = this.c(d2);
        float f4 = this.a(d2.b());
        float f5 = cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.a(this.N);
        boolean bl = !Float.isNaN(f4) && f5 > 0.001f;
        float f6 = f2 = bl ? cyou.rimuru.shootingstardemo.mc1211.client.fx.w.h(f4) * f5 : 0.0f;
        if (f2 < 0.999f) {
            this.b(d2, list, f3);
            this.a(d2, list, f3);
        }
        if (f2 > 0.001f) {
            float f7 = this.r;
            list.add(new cyou.rimuru.shootingstardemo.mc1211.client.render.e("tess_space", j2 -> {
                cyou.rimuru.shootingstardemo.mc1211.client.fx.q.ab.forEach(j2::a);
                cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(f4, f2, f7, cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(j2));
            }));
        }
    }

    private static BiConsumer<String, float[]> a(cyou.rimuru.shootingstardemo.mc1211.client.render.j j2) {
        return (string, fArray) -> {
            switch (((float[])fArray).length) {
                case 1: {
                    j2.b((String)string, fArray[0]);
                    break;
                }
                case 2: {
                    j2.a((String)string, fArray[0], fArray[1]);
                    break;
                }
                case 3: {
                    j2.a((String)string, fArray[0], fArray[1], fArray[2]);
                    break;
                }
                case 4: {
                    j2.a((String)string, fArray[0], fArray[1], fArray[2], fArray[3]);
                    break;
                }
                default: {
                    j2.a((String)string, (float[])fArray);
                }
            }
        };
    }

    static float b(float f2) {
        f2 = Mth.clamp((float)f2, (float)0.0f, (float)1.0f);
        return f2 * f2 * (3.0f - 2.0f * f2);
    }

    static double[] e(float f2) {
        double d2 = (double)f2 / 20.0;
        double d3 = 1.0 - (double)cyou.rimuru.shootingstardemo.mc1211.client.fx.u.b((f2 - 562.0f) / 20.0f);
        return new double[]{d2 * 0.9 * d3, d2 * 0.55 * d3, d2 * 0.3 * d3, d2 * 0.18 * d3};
    }

    double f(float f2) {
        return Mth.lerp((double)cyou.rimuru.shootingstardemo.mc1211.client.fx.u.b((f2 - 562.0f) / 24.0f), (double)30.0, (double)8.125) * cyou.rimuru.shootingstardemo.mc1211.star.m.e(this.x);
    }

    private void a(d d2, List<cyou.rimuru.shootingstardemo.mc1211.client.render.e> list, float f2) {
        if (f2 < 280.0f || f2 > 466.0f) {
            return;
        }
        double[] dArray = cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(f2, this.x);
        Vec3 vec3 = this.i();
        Vec3 vec32 = d2.a(new Vec3(vec3.x, (double)this.R + dArray[0], vec3.z));
        Vec3 vec33 = d2.a(new Vec3(vec3.x, (double)this.R + dArray[3], vec3.z));
        float f3 = (float)dArray[1];
        float f4 = (float)dArray[2];
        float f5 = (float)dArray[4];
        float f6 = cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(f2, 284.0f, 312.0f, 436.0f, 466.0f);
        float f7 = cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(f2, 280.0f, 284.0f, 286.0f, 298.0f);
        float f8 = f2 / 20.0f;
        float[] fArray = new float[192];
        cyou.rimuru.shootingstardemo.mc1211.client.fx.t.a(f2, fArray);
        list.add(new cyou.rimuru.shootingstardemo.mc1211.client.render.e("tess_sky", j2 -> {
            j2.a("MilkyTop", cyou.rimuru.shootingstardemo.mc1211.client.fx.q.ab.get("MilkyTop"));
            j2.a("S0", f8, f6, f4, f7);
            j2.a("MouthC", (float)vec3.x, (float)vec3.y, (float)vec3.z, f5);
            j2.a("EngC", (float)vec32.x, (float)vec32.y, (float)vec32.z, f3);
            j2.a("EngX", 1.0f, 0.0f, 0.0f);
            j2.a("EngY", 0.0f, 1.0f, 0.0f);
            j2.a("EngZ", 0.0f, 0.0f, 1.0f);
            j2.a("KeyDir", -0.42f, 0.82f, 0.39f);
            j2.a("KeyCol", 2.2f, 2.09f, 1.94f);
            j2.a("Eng", fArray);
        }));
    }

    private void b(d d2, List<cyou.rimuru.shootingstardemo.mc1211.client.render.e> list, float f2) {
        Object object;
        if (f2 < 8.0f || f2 > 1262.0f) {
            return;
        }
        Vec3 vec3 = this.s();
        double[] dArray = cyou.rimuru.shootingstardemo.mc1211.client.fx.u.e(f2);
        double d3 = this.f(f2);
        float f3 = cyou.rimuru.shootingstardemo.mc1211.client.fx.u.b((f2 - 426.0f) / 6.0f);
        float[] fArray = new float[64];
        for (int i2 = 0; i2 < 16; ++i2) {
            double[] cellPos = cyou.rimuru.shootingstardemo.mc1211.client.fx.w.a(i2, dArray);
            Vec3 vec32 = d2.a(vec3.add(cellPos[0] * d3 * (double)f3, cellPos[1] * d3 * (double)f3, cellPos[2] * d3 * (double)f3));
            fArray[i2 * 4] = (float)vec32.x;
            fArray[i2 * 4 + 1] = (float)vec32.y;
            fArray[i2 * 4 + 2] = (float)vec32.z;
            fArray[i2 * 4 + 3] = (float)cellPos[3];
        }
        float[] fArray2 = new float[256];
        Vec3 origin = d2.c();
        for (int i3 = 0; i3 < 64; ++i3) {
            float[] fArray3 = this.Y[i3];
            boolean bl = fArray3 != null && f2 >= (float)this.V[i3];
            fArray2[i3 * 4] = bl ? (f2 - (float)this.V[i3]) / 20.0f : -1.0f;
            fArray2[i3 * 4 + 1] = bl ? (float)((double)fArray3[0] - origin.y) : 0.0f;
            fArray2[i3 * 4 + 2] = bl ? fArray3[1] : 0.0f;
            fArray2[i3 * 4 + 3] = bl ? fArray3[2] : (float)this.z;
        }
        Vec3 vec33 = d2.a(new Vec3((double)this.P, (double)this.R, (double)this.Q));
        float f4 = cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(f2, 9.0f, 10.5f, 962.0f, 1162.0f);
        float f5 = cyou.rimuru.shootingstardemo.mc1211.client.fx.u.b((f2 - 426.0f) / 8.0f);
        float f6 = cyou.rimuru.shootingstardemo.mc1211.client.fx.u.b((f2 - 426.0f + 1.0f) / 2.0f);
        float f7 = f2 >= 426.0f ? (f2 - 426.0f) / 20.0f : -1.0f;
        float f8 = f2 >= 438.0f ? (f2 - 438.0f) / 20.0f : -1.0f;
        float f9 = f2 >= 462.0f ? (f2 - 462.0f) / 20.0f : -1.0f;
        float f10 = f2 >= 562.0f ? (f2 - 562.0f) / 20.0f : -1.0f;
        float f11 = cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(f2, 428.0f, 448.0f, 562.0f, 582.0f);
        float f12 = cyou.rimuru.shootingstardemo.mc1211.client.fx.u.b((f2 - 562.0f) / 24.0f);
        float f13 = f2 < 562.0f ? 1.0f : Mth.lerp((float)cyou.rimuru.shootingstardemo.mc1211.client.fx.u.b((f2 - 562.0f) / 100.0f), (float)1.0f, (float)0.4f) * (1.0f - cyou.rimuru.shootingstardemo.mc1211.client.fx.u.b((f2 - 562.0f - 300.0f) / 300.0f));
        float f14 = (float)((double)this.S - origin.y);
        float f15 = (float)(vec3.y - origin.y);
        float f16 = f2 / 20.0f;
        float f17 = this.r;
        float f18 = (float)cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a((double)f2);
        float f19 = cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(f2, 422.0f, 452.0f, 722.0f, 882.0f);
        float f20 = (float)this.x;
        float f21 = Math.round(20.0 * this.x);
        float f22 = (float)((double)(this.T + 4) - origin.y);
        list.add(new cyou.rimuru.shootingstardemo.mc1211.client.render.e("tess_world", j2 -> {
            j2.a("Pts", fArray);
            j2.a("Cubes", fArray2);
            j2.a("Grid", vec33);
            j2.a("P0", f16, f17, f4, f5);
            j2.a("P1", f8, f9, f10, f18);
            j2.a("P2", f14, f11, f12, f13);
            j2.a("P3", f15, this.y, f19, f20);
            j2.a("P4", f6, f7, f21, f22);
        }));
    }

    static double a(double d2) {
        if (d2 < 29.0) {
            return 0.0;
        }
        double d3 = 13.05;
        double d4 = Math.min(d2 - 29.0, 261.0) / 20.0;
        double d5 = d4 / d3;
        return 0.6 * d4 + 3.4 * d3 * d5 * d5 * d5 / 3.0;
    }

    @Override
    public void a(d d2, f f2) {
        boolean bl;
        float f3 = this.c(d2);
        float f4 = this.a(d2.b());
        float f5 = cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.a(this.N);
        boolean bl2 = bl = !Float.isNaN(f4) && f5 > 0.001f;
        if (bl) {
            this.b(f2, f4, f5);
        }
        if (bl && cyou.rimuru.shootingstardemo.mc1211.client.fx.w.h(f4) > 0.5f) {
            return;
        }
        Vec3 vec3 = d2.c();
        float f6 = bl ? 1.0f : (float)Mth.clamp((double)(1.0 - vec3.distanceTo(this.a(vec3)) / 1200.0), (double)0.2, (double)1.0);
        float f7 = (f3 - 9.0f) / 20.0f;
        if (f7 >= 0.0f && f7 < 1.0f) {
            f2.a(0xE8F8FF, (float)Math.exp((double)(-f7) * 6.0) * 0.2f * f6);
            f2.b = Math.max(f2.b, (float)Math.exp((double)(-f7) * 4.0) * 0.35f * f6);
        }
        float f8 = cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(f3, 280.0f, 286.0f, 292.0f, 312.0f);
        f2.b = Math.max(f2.b, f8 * 0.7f * f6);
        f2.a = Math.max(f2.a, f8 * 0.25f * f6);
        float f9 = cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(f3, 288.0f, 308.0f, 394.0f, 398.0f);
        f2.a = Math.max(f2.a, f9 * 0.12f * f6);
        float f10 = Mth.clamp((float)((f3 - 398.0f) / 28.0f), (float)0.0f, (float)1.0f);
        if (f3 >= 398.0f && f3 < 426.0f) {
            f2.d = Math.max(f2.d, f10 * f10 * 0.8f * f6);
            f2.b = Math.max(f2.b, f10 * 1.2f * f6);
            f2.a = Math.max(f2.a, (0.2f + f10 * f10 * 0.9f) * f6);
        }
        cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(f2, cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(f3, 423.0f, 424.0f, 425.0f, 426.0f) * 0.6f, 0.0f);
        float f11 = (f3 - 426.0f) / 20.0f;
        if (f11 >= 0.0f && f11 < 2.5f) {
            if (f11 < 0.05f) {
                f2.a(0xFFFFFF, 5.0f * f6);
            } else if (f11 - 0.05f < (float)D.length * 0.083333336f) {
                cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(f2, f11 - 0.05f, this.i().add(0.0, 40.0, 0.0), f6, 0.083333336f, D);
                f2.y = f2.y > 0.0f ? Math.min(f2.y, 12.0f) : 12.0f;
            }
            f2.a(w[(int)(f11 * 9.0f) % 7], (float)Math.exp((double)(-f11) * 5.0) * 0.7f * f6);
            f2.a = Math.max(f2.a, cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(f11, 0.0f, 0.02f, 0.6f, 2.0f) * 1.5f * f6);
            f2.m = Math.max(f2.m, 0.8f * cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(f11, 0.0f, 0.1f, 0.8f, 2.5f));
        }
        float f12 = cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(f3, 438.0f, 442.0f, 460.0f, 462.0f);
        f2.a = Math.max(f2.a, f12 * (0.2f + 0.4f * (f3 - 438.0f) / 24.0f) * f6);
        f2.b = Math.max(f2.b, f12 * 0.5f * f6);
        f2.d = Math.max(f2.d, f12 * f12 * 0.25f * f6);
        cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(f2, cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(f3, 457.0f, 459.0f, 461.0f, 462.0f) * 0.6f, 0.0f);
        cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(f2, (f3 - 462.0f) / 20.0f, F, 0, this.i().add(0.0, 10.0, 0.0), f6, 1.0f);
        for (int i2 = 0; i2 < 4; ++i2) {
            cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(f2, (f3 - 474.0f - (float)(i2 * 20)) / 20.0f, G, i2, this.i().add(0.0, 60.0, 0.0), f6, 0.7f * (0.9f + 0.1f * (float)i2));
        }
        float f13 = cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(f3, 474.0f, 480.0f, 552.0f, 562.0f);
        f2.a = Math.max(f2.a, f13 * 0.2f * f6);
        f2.m = Math.max(f2.m, f13 * 0.35f);
        cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(f2, cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(f3, 554.0f, 557.0f, 561.0f, 562.0f) * 0.7f, 0.0f);
        float f14 = (f3 - 562.0f) / 20.0f;
        if (f14 >= 0.0f && f14 < 8.0f) {
            int n2;
            if (f14 < 0.06f) {
                f2.a(0xFFFFFF, 6.5f);
            } else if (f14 - 0.06f < (float)H.length * 0.083333336f) {
                cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(f2, f14 - 0.06f, this.s(), 1.0f, 0.083333336f, H);
                n2 = (int)((f14 - 0.06f) / 0.083333336f);
                f2.y = f2.y > 0.0f ? Math.min(f2.y, 12.0f) : 12.0f;
                f2.d = Math.max(f2.d, n2 % 2 == 0 ? 1.0f : 0.3f);
                f2.b = Math.max(f2.b, n2 % 2 == 0 ? 0.5f : 1.8f);
            }
            n2 = w[(int)(f14 * 5.0f) % 7];
            f2.a(n2, (float)Math.exp((double)(-f14) * 2.5) * 0.9f);
            f2.a = Math.max(f2.a, cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(f14, 0.0f, 0.02f, 1.0f, 1.8f) * 1.9f);
            f2.m = Math.max(f2.m, 1.1f * cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(f14, 0.0f, 0.1f, 3.0f, 8.0f));
            f2.n = Math.max(f2.n, 1.0f + 1.2f * cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(f14, 0.0f, 0.1f, 2.0f, 6.0f));
            f2.v = Math.max(f2.v, 0.2f * cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(f14, 0.5f, 1.0f, 3.0f, 6.0f));
        }
    }

    private static void a(f f2, float f3, int[] nArray, int n2, Vec3 vec3, float f4, float f5) {
        if (f3 < 0.0f || f3 >= 2.5f) {
            return;
        }
        if (f3 < 0.05f) {
            f2.a(0xFFFFFF, 4.5f * f4 * f5);
        } else if (f3 - 0.05f < (float)nArray.length * 0.083333336f) {
            int n3;
            int[] nArray2 = new int[nArray.length];
            for (n3 = 0; n3 < nArray2.length; ++n3) {
                nArray2[n3] = nArray[(n3 + n2) % nArray.length];
            }
            cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(f2, f3 - 0.05f, vec3, f4, 0.083333336f, nArray2);
            n3 = (int)((f3 - 0.05f) / 0.083333336f);
            f2.y = f2.y > 0.0f ? Math.min(f2.y, 12.0f) : 12.0f;
            f2.d = Math.max(f2.d, (n3 % 2 == 0 ? 0.9f : 0.25f) * f4);
        }
        f2.a(w[(int)(f3 * 9.0f + (float)(n2 * 2)) % 7], (float)Math.exp((double)(-f3) * 5.0) * 0.8f * f4);
        f2.a = Math.max(f2.a, cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(f3, 0.0f, 0.02f, 0.5f, 2.0f) * 1.2f * f4 * f5);
        f2.b = Math.max(f2.b, cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(f3, 0.0f, 0.02f, 0.4f, 1.5f) * 1.2f * f4);
        f2.m = Math.max(f2.m, 0.75f * cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(f3, 0.0f, 0.1f, 1.0f, 2.5f));
    }

    private void b(f f2, float f3, float f4) {
        float f5 = cyou.rimuru.shootingstardemo.mc1211.client.fx.w.h(f3) * f4;
        f2.x = Math.max(f2.x, f5);
        if (f5 > 0.001f) {
            f2.m = Math.max(f2.m, 0.6f);
        }
        float f6 = cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(f3, 62.0f, 72.0f, 108.0f, 118.0f);
        f2.d = Math.max(f2.d, f6 * 0.25f);
        float f7 = cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(f3, 118.0f, 121.0f, 124.0f, 130.0f);
        f2.b = Math.max(f2.b, f7 * 1.6f);
        f2.a = Math.max(f2.a, f7 * 0.25f);
        f2.a(15788287, f7 * 0.15f);
        float f8 = cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(f3, 150.0f, 154.0f, 160.0f, 180.0f);
        f2.a = Math.max(f2.a, f8 * 0.2f);
        float f9 = cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(f3, 210.0f, 230.0f, 256.0f, 262.0f);
        f2.d = Math.max(f2.d, f9 * 0.18f);
        f2.a = Math.max(f2.a, f9 * 0.15f);
        float f10 = cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(f3, 248.0f, 249.0f, 250.0f, 256.0f);
        f2.b = Math.max(f2.b, f10 * 1.2f);
        f2.a(0xFFFFFF, f10 * 0.3f);
        float f11 = cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(f3, 262.0f, 270.0f, 288.0f, 290.0f);
        f2.d = Math.max(f2.d, f11 * 0.7f);
        f2.a = Math.max(f2.a, f11 * 0.35f);
        float f12 = cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(f3, 288.0f, 290.0f, 291.0f, 296.0f);
        f2.b = Math.max(f2.b, f12 * 0.8f);
        float f13 = Mth.clamp((float)((f3 - 336.0f) / 22.0f), (float)0.0f, (float)1.0f);
        if (f3 >= 336.0f && f3 < 358.0f) {
            f2.d = Math.max(f2.d, f13 * f13 * 0.6f);
        }
        float f14 = cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(f3, 357.0f, 358.0f, 358.6f, 362.0f);
        f2.a(0xFFFFFF, f14 * 1.2f);
    }

    @Override
    b c(float f2) {
        float f3 = (float)this.v + f2;
        float[][] fArrayArray = new float[][]{{0.0f, -0.35f}, {4.0f, -1.4f}, {9.0f, -1.4f}, {16.0f, -2.7f}, {40.0f, -2.7f}, {60.0f, -1.3f}, {622.0f, -1.3f}, {652.0f, -0.35f}};
        float f4 = fArrayArray[fArrayArray.length - 1][1];
        for (int i2 = 0; i2 < fArrayArray.length - 1; ++i2) {
            if (!(f3 >= fArrayArray[i2][0]) || !(f3 < fArrayArray[i2 + 1][0])) continue;
            f4 = Mth.lerp((float)cyou.rimuru.shootingstardemo.mc1211.client.fx.u.b((f3 - fArrayArray[i2][0]) / (fArrayArray[i2 + 1][0] - fArrayArray[i2][0])), (float)fArrayArray[i2][1], (float)fArrayArray[i2 + 1][1]);
            break;
        }
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.b.a(f4, -0.12f, 0.0f).a(cyou.rimuru.shootingstardemo.mc1211.client.fx.u.a(f3, 0.0f, 3.0f, 648.0f, 656.0f));
    }

    @Override
    boolean b(Vec3 vec3) {
        return (this.v >= 457 && this.v < 462 || this.v >= 554 && this.v < 562) && vec3.distanceTo(this.a(vec3)) < 1400.0;
    }

    @Override
    public void a(GuiGraphics guiGraphics, d d2) {
        String string;
        String string2;
        float f2 = this.c(d2);
        Minecraft minecraft = d2.a();
        if (minecraft.player == null || f2 < 9.0f || f2 >= 602.0f) {
            return;
        }
        Vec3 vec3 = minecraft.player.position();
        if (vec3.distanceTo(this.a(vec3)) > 300.0) {
            return;
        }
        float f3 = (float)cyou.rimuru.shootingstardemo.mc1211.client.fx.i.c(guiGraphics) / 2.0f;
        s.c c2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.s.a(f2 * 0.03f, 1.6f, 0.2f, 1.0f);
        s.c c3 = cyou.rimuru.shootingstardemo.mc1211.client.fx.s.c.a(-1326254598);
        float f4 = Mth.clamp((float)((f2 - 9.0f) / 8.0f), (float)0.0f, (float)1.0f) * (1.0f - Mth.clamp((float)((f2 - 562.0f - 30.0f) / 10.0f), (float)0.0f, (float)1.0f));
        if (f2 < 462.0f) {
            string2 = "DIMENSIONAL SHEAR INBOUND";
            string = String.format(Locale.ROOT, "T\u2212%05.2f   64 CELLS", Float.valueOf((462.0f - f2) / 20.0f / this.u));
        } else {
            int n2 = 0;
            for (int i2 = 0; i2 < this.V.length; ++i2) {
                n2 += f2 >= (float)this.V[i2] ? 1 : 0;
            }
            string2 = f2 < 474.0f ? "SEVERED" : (f2 < 562.0f ? "ASCENSION" : "FOLDED");
            string = f2 < 562.0f ? String.format(Locale.ROOT, "%02d / %02d CELLS", n2, 64) : "W-AXIS LOCKED";
        }
        float f5 = cyou.rimuru.shootingstardemo.mc1211.client.fx.s.a(guiGraphics, cyou.rimuru.shootingstardemo.mc1211.client.fx.s.a.b, string2, 1.1f, 2.6f);
        cyou.rimuru.shootingstardemo.mc1211.client.fx.s.a(guiGraphics, f3, 40.0f, f5 * 0.5f + 70.0f, 24.0f, f4 * 0.45f);
        cyou.rimuru.shootingstardemo.mc1211.client.fx.s.a(guiGraphics, cyou.rimuru.shootingstardemo.mc1211.client.fx.s.a.b, string2, f3, 34.0f, 0.5f, 1.1f, 2.6f, c2, f4, 1.0f);
        cyou.rimuru.shootingstardemo.mc1211.client.fx.s.a(guiGraphics, cyou.rimuru.shootingstardemo.mc1211.client.fx.s.a.c, string, f3, 50.0f, 0.5f, 0.85f, 0.4f, c3, f4, 1.0f);
    }

    public void b(GuiGraphics guiGraphics, d d2) {
        float f2 = this.a(d2.b());
        if (Float.isNaN(f2)) {
            return;
        }
        cyou.rimuru.shootingstardemo.mc1211.client.fx.v.a(guiGraphics, d2, this, f2, cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.a(this.N));
    }
}

