/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.star;

import cyou.rimuru.shootingstardemo.mc1211.magic.f;
import cyou.rimuru.shootingstardemo.mc1211.registry.b;
import cyou.rimuru.shootingstardemo.mc1211.spell.d;
import cyou.rimuru.shootingstardemo.mc1211.star.a;
import cyou.rimuru.shootingstardemo.mc1211.star.c;
import cyou.rimuru.shootingstardemo.mc1211.star.k;
import cyou.rimuru.shootingstardemo.mc1211.star.l;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class i
extends cyou.rimuru.shootingstardemo.mc1211.spell.a {
    public static final int k = 5;
    public static final int l = 7;
    public static final int m = 9;
    public static final int n = 24;
    public static final int o = 4;
    public static final int p = 290;
    public static final int q = 16;
    public static final int r = 398;
    public static final int s = 5;
    public static final int t = 8;
    public static final int u = 440;
    public static final int v = 560;
    public static final double w = 5.5;
    public static final double x = 12.0;
    public static final double y = 400.0;
    public static final double z = 320.0;
    private static final int A = 24;
    private static final double B = 2.0;
    private static final int C = 12;
    private static final TicketType<ChunkPos> D = TicketType.create((String)"shooting_star_demo:seven_stars", Comparator.comparingLong(ChunkPos::toLong));
    private static final int E = 4;
    private static final int F = 2;
    private final List<int[]> G = new ArrayList<int[]>();
    private final double H;
    private final double I;
    private final double J;
    private final double K;
    private final double L;
    private final double[][] M = new double[7][];
    private final int[] N = new int[7];
    private final a[] O = new a[7];
    private final a[] P = new a[cyou.rimuru.shootingstardemo.mc1211.star.c.i.length];
    private boolean Q = true;

    public i(ServerPlayer serverPlayer, Vec3 vec3, long l2, f f2) {
        super(cyou.rimuru.shootingstardemo.mc1211.star.l.c, serverPlayer, serverPlayer.position(), vec3, l2, false, f2);
        int n2;
        this.H = f2.b();
        this.I = 5.5 * this.H;
        this.J = 12.0 * this.H;
        this.K = 2.0 * this.H;
        this.L = cyou.rimuru.shootingstardemo.mc1211.star.c.a(serverPlayer.getX(), serverPlayer.getZ(), vec3.x, vec3.z);
        for (n2 = 0; n2 < 7; ++n2) {
            double[] dArray = cyou.rimuru.shootingstardemo.mc1211.star.c.a(n2, this.L, this.H);
            this.M[n2] = new double[]{vec3.x + dArray[0], vec3.z + dArray[1]};
        }
        for (n2 = 0; n2 < 7; ++n2) {
            this.G.add(new int[]{Mth.floor((double)this.M[n2][0]) >> 4, Mth.floor((double)this.M[n2][1]) >> 4, Math.min((int)Math.ceil((cyou.rimuru.shootingstardemo.mc1211.star.c.a(n2, this.H) * 1.1 + this.K) / 16.0), 12)});
        }
        for (int[] nArray : cyou.rimuru.shootingstardemo.mc1211.star.c.i) {
            double[] dArray = this.M[nArray[0]];
            double[] dArray2 = this.M[nArray[1]];
            int n3 = Math.max(1, (int)Math.ceil(Math.hypot(dArray2[0] - dArray[0], dArray2[1] - dArray[1]) / 64.0));
            for (int i2 = 0; i2 <= n3; ++i2) {
                double d2 = (double)i2 / (double)n3;
                this.G.add(new int[]{Mth.floor((double)Mth.lerp((double)d2, (double)dArray[0], (double)dArray2[0])) >> 4, Mth.floor((double)Mth.lerp((double)d2, (double)dArray[1], (double)dArray2[1])) >> 4, 2});
            }
        }
        Iterator<int[]> iterator = this.G.iterator();
        while (iterator.hasNext()) {
            int[] nArray = iterator.next();
            ChunkPos chunkPos = new ChunkPos(nArray[0], nArray[1]);
            this.b.getChunkSource().addRegionTicket(D, chunkPos, nArray[2], chunkPos);
        }
    }

    public static void i() {
    }

    @Override
    public double e() {
        return cyou.rimuru.shootingstardemo.mc1211.star.c.a(this.H) + 400.0;
    }

    private a.h j() {
        return new a.h(){

            @Override
            public void a(ChunkPos chunkPos) {
                i.this.b.getChunkSource().addRegionTicket(D, chunkPos, 0, chunkPos);
            }

            @Override
            public void b(ChunkPos chunkPos) {
                i.this.b.getChunkSource().removeRegionTicket(D, chunkPos, 0, chunkPos);
            }
        };
    }

    public static int c(int n2) {
        return 290 + n2 * 16;
    }

    public static int d(int n2) {
        return 398 + n2 * 5;
    }

    private double a(int n2, int n3) {
        long l2 = this.f ^ (long)n2 * -7046029254386353131L ^ (long)n3 * -4417276706812531889L;
        l2 = (l2 ^ l2 >>> 31) * -4658895280553007687L;
        l2 ^= l2 >>> 29;
        return (double)(l2 >>> 11) / 9.007199254740992E15;
    }

    private int a(int n2, int n3, int n4) {
        double d2;
        double d3 = cyou.rimuru.shootingstardemo.mc1211.star.c.a(n2, this.H);
        double d4 = Math.hypot((double)n3 + 0.5 - this.M[n2][0], (double)n4 + 0.5 - this.M[n2][1]);
        if (d4 > (d2 = d3 * (1.0 + 0.07 * Math.sin(Math.atan2((double)n4 - this.M[n2][1], (double)n3 - this.M[n2][0]) * 5.0 + (double)n2)))) {
            return Integer.MIN_VALUE;
        }
        double d5 = d4 / d2;
        double d6 = (double)this.N[n2] - cyou.rimuru.shootingstardemo.mc1211.star.c.b(n2, this.H) * (1.0 - d5 * d5) * (1.0 - 0.3 * d5 * d5);
        return Math.max(Mth.floor((double)d6), this.b.getMinBuildHeight());
    }

    private BlockState a(int n2, int n3, int n4, int n5, BlockState blockState) {
        if (!blockState.getFluidState().isEmpty() || blockState.is(cyou.rimuru.shootingstardemo.mc1211.registry.b.c) || blockState.is(cyou.rimuru.shootingstardemo.mc1211.registry.b.d)) {
            return null;
        }
        double d2 = Math.hypot((double)n3 + 0.5 - this.M[n2][0], (double)n5 + 0.5 - this.M[n2][1]) / cyou.rimuru.shootingstardemo.mc1211.star.c.a(n2, this.H) + (this.a(n3, n5) - 0.5) * 0.18;
        double d3 = this.a(n5 * 3 + 7, n3 * 5 - 1);
        if (d2 < 0.25) {
            return d3 < 0.5 ? Blocks.CRYING_OBSIDIAN.defaultBlockState() : (d3 < 0.8 ? Blocks.OBSIDIAN.defaultBlockState() : Blocks.AMETHYST_BLOCK.defaultBlockState());
        }
        if (d2 < 0.7) {
            return d3 < 0.35 ? Blocks.AMETHYST_BLOCK.defaultBlockState() : (d3 < 0.55 ? Blocks.CALCITE.defaultBlockState() : (d3 < 0.8 ? Blocks.BLACKSTONE.defaultBlockState() : Blocks.SMOOTH_BASALT.defaultBlockState()));
        }
        return d3 < 0.3 ? Blocks.CALCITE.defaultBlockState() : (d3 < 0.55 ? Blocks.TUFF.defaultBlockState() : (d3 < 0.8 ? Blocks.COBBLED_DEEPSLATE.defaultBlockState() : null));
    }

    private double[] a(int n2, double d2, double d3) {
        double[] dArray = this.M[cyou.rimuru.shootingstardemo.mc1211.star.c.i[n2][0]];
        double[] dArray2 = this.M[cyou.rimuru.shootingstardemo.mc1211.star.c.i[n2][1]];
        double d4 = dArray2[0] - dArray[0];
        double d5 = dArray2[1] - dArray[1];
        double d6 = Mth.clamp((double)(((d2 - dArray[0]) * d4 + (d3 - dArray[1]) * d5) / (d4 * d4 + d5 * d5)), (double)0.0, (double)1.0);
        return new double[]{Math.hypot(d2 - dArray[0] - d4 * d6, d3 - dArray[1] - d5 * d6), d6};
    }

    private int b(int n2, int n3, int n4) {
        double[] dArray = this.a(n2, (double)n3 + 0.5, (double)n4 + 0.5);
        if (dArray[0] > this.I) {
            return Integer.MIN_VALUE;
        }
        double d2 = Mth.lerp((double)dArray[1], (double)this.N[cyou.rimuru.shootingstardemo.mc1211.star.c.i[n2][0]], (double)this.N[cyou.rimuru.shootingstardemo.mc1211.star.c.i[n2][1]]);
        double d3 = dArray[0] / this.I;
        return Math.max(Mth.floor((double)(d2 - this.J * (1.0 - d3 * d3 * d3 * d3))), this.b.getMinBuildHeight());
    }

    private BlockState b(int n2, int n3, int n4, int n5, BlockState blockState) {
        if (!blockState.getFluidState().isEmpty() || blockState.is(cyou.rimuru.shootingstardemo.mc1211.registry.b.c)) {
            return null;
        }
        double d2 = this.a(n2, (double)n3 + 0.5, (double)n5 + 0.5)[0];
        if (d2 < Math.max(this.H, 0.6)) {
            return cyou.rimuru.shootingstardemo.mc1211.registry.b.d.defaultBlockState();
        }
        double d3 = this.a(n3 * 11 + n2, n5 * 7 - n2);
        return d3 < 0.3 ? Blocks.BLACKSTONE.defaultBlockState() : (d3 < 0.5 ? Blocks.AMETHYST_BLOCK.defaultBlockState() : (d3 < 0.75 ? Blocks.SMOOTH_BASALT.defaultBlockState() : Blocks.CALCITE.defaultBlockState()));
    }

    private Map<BlockPos, BlockState> e(int n2) {
        HashMap<BlockPos, BlockState> hashMap = new HashMap<BlockPos, BlockState>();
        BlockState blockState = cyou.rimuru.shootingstardemo.mc1211.registry.b.c.defaultBlockState();
        int n3 = Mth.floor((double)this.M[n2][0]);
        int n4 = Mth.floor((double)this.M[n2][1]);
        int n5 = Mth.floor((double)((double)this.N[n2] - cyou.rimuru.shootingstardemo.mc1211.star.c.b(n2, this.H)));
        n5 = Math.max(n5, this.b.getMinBuildHeight());
        double d2 = cyou.rimuru.shootingstardemo.mc1211.star.c.a(n2, this.H) / (36.0 * this.H);
        int n6 = 5 + (int)(this.a(n2, 17) * 3.0);
        for (int i2 = -1; i2 < n6; ++i2) {
            double d3 = i2 < 0 ? 0.0 : ((double)i2 + this.a(n2 * 13 + i2, 5) * 0.6) / (double)n6 * Math.PI * 2.0;
            double d4 = i2 < 0 ? 0.0 : 0.35 + this.a(i2, n2 * 7) * 0.5;
            double d5 = (i2 < 0 ? 16.0 : 7.0 + this.a(n2 + i2 * 3, 9) * 7.0) * (0.6 + 0.6 * d2) * this.H;
            double d6 = (i2 < 0 ? 2.2 : 1.2) * this.H;
            for (double d7 = 0.0; d7 < d5; d7 += 1.0) {
                double d8 = d6 * (1.0 - d7 / d5 * 0.85);
                double d9 = (double)n3 + 0.5 + Math.cos(d3) * Math.sin(d4) * d7;
                double d10 = (double)n5 + Math.cos(d4) * d7;
                double d11 = (double)n4 + 0.5 + Math.sin(d3) * Math.sin(d4) * d7;
                int n7 = (int)Math.ceil(d8);
                for (int i3 = -n7; i3 <= n7; ++i3) {
                    for (int i4 = -n7; i4 <= n7; ++i4) {
                        for (int i5 = -n7; i5 <= n7; ++i5) {
                            if (!((double)(i3 * i3 + i4 * i4 + i5 * i5) <= d8 * d8)) continue;
                            hashMap.put(BlockPos.containing((double)(d9 + (double)i3), (double)(d10 + (double)i4), (double)(d11 + (double)i5)), blockState);
                        }
                    }
                }
            }
        }
        return hashMap;
    }

    @Override
    protected void a(int n2) {
        int n6;
        if (this.i != null && n2 % 10 == 0) {
            cyou.rimuru.shootingstardemo.mc1211.spell.d.a(this.i);
        }
        for (n6 = 0; n6 < 7; ++n6) {
            if (n2 == cyou.rimuru.shootingstardemo.mc1211.star.i.c(n6)) {
                this.f(n6);
            }
            if (this.O[n6] == null || this.O[n6].i()) continue;
            this.O[n6].j();
        }
        for (n6 = 0; n6 < this.P.length; ++n6) {
            int n7;
            int n8;
            if (n2 == cyou.rimuru.shootingstardemo.mc1211.star.i.d(n6)) {
                n8 = cyou.rimuru.shootingstardemo.mc1211.star.c.i[n6][0];
                n7 = cyou.rimuru.shootingstardemo.mc1211.star.c.i[n6][1];
                double d2 = (this.M[n8][0] + this.M[n7][0]) * 0.5;
                double d3 = (this.M[n8][1] + this.M[n7][1]) * 0.5;
                double d4 = Math.hypot(this.M[n7][0] - this.M[n8][0], this.M[n7][1] - this.M[n8][1]) * 0.5;
                int n9 = n6;
                this.P[n6] = cyou.rimuru.shootingstardemo.mc1211.star.a.a(this.b, new Vec3(d2, this.e.y, d3), d4 + this.I + this.K, (n3, n4) -> this.b(n9, n3, n4), (n3, n4, n5, blockState) -> this.b(n9, n3, n4, n5, blockState), null).a(24).a(this.j()).a().a("Seven Stars line");
            }
            if (this.P[n6] != null && !this.P[n6].i()) {
                n8 = n2 - cyou.rimuru.shootingstardemo.mc1211.star.i.d(n6);
                n7 = cyou.rimuru.shootingstardemo.mc1211.star.c.i[n6][0];
                int n10 = cyou.rimuru.shootingstardemo.mc1211.star.c.i[n6][1];
                double d5 = Math.hypot(this.M[n10][0] - this.M[n7][0], this.M[n10][1] - this.M[n7][1]) * 0.5;
                this.P[n6].a(n8 >= 8 ? Double.MAX_VALUE : (d5 + this.I) * (double)(n8 + 1) / 8.0);
            }
            if (n2 < cyou.rimuru.shootingstardemo.mc1211.star.i.d(n6) || n2 > cyou.rimuru.shootingstardemo.mc1211.star.i.d(n6) + 8 + 2 || (n2 - cyou.rimuru.shootingstardemo.mc1211.star.i.d(n6)) % 2 != 0) continue;
            this.g(n6);
        }
        if (this.Q && n2 > 440 && this.k()) {
            this.l();
        }
    }

    private boolean k() {
        for (a a2 : this.O) {
            if (a2 != null && a2.i()) continue;
            return false;
        }
        for (a a2 : this.P) {
            if (a2 != null && a2.i()) continue;
            return false;
        }
        return true;
    }

    private void f(int n2) {
        int n6 = Mth.floor((double)this.M[n2][0]);
        int n7 = Mth.floor((double)this.M[n2][1]);
        this.b.getChunk(n6 >> 4, n7 >> 4);
        this.N[n2] = this.b.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, n6, n7) - 1;
        double d2 = cyou.rimuru.shootingstardemo.mc1211.star.c.a(n2, this.H);
        int n8 = n2;
        this.O[n2] = cyou.rimuru.shootingstardemo.mc1211.star.a.a(this.b, new Vec3(this.M[n2][0], (double)this.N[n2], this.M[n2][1]), d2 * 1.1 + this.K, (n3, n4) -> this.a(n8, n3, n4), (n3, n4, n5, blockState) -> this.a(n8, n3, n4, n5, blockState), null).a(24).a(this.j()).a().a("Seven Stars crater");
        this.O[n2].j();
        this.O[n2].j();
        cyou.rimuru.shootingstardemo.mc1211.star.a.a(this.b, this.e(n2));
        this.a(this.M[n2][0], this.M[n2][1], d2 + 4.0 * this.K, (double)this.N[n2] - cyou.rimuru.shootingstardemo.mc1211.star.c.b(n2, this.H) - 4.0 * this.K, true);
    }

    private void g(int n2) {
        int n3 = cyou.rimuru.shootingstardemo.mc1211.star.c.i[n2][0];
        int n4 = cyou.rimuru.shootingstardemo.mc1211.star.c.i[n2][1];
        double d2 = Math.min(this.M[n3][0], this.M[n4][0]) - this.I - this.K;
        double d3 = Math.max(this.M[n3][0], this.M[n4][0]) + this.I + this.K;
        double d4 = Math.min(this.M[n3][1], this.M[n4][1]) - this.I - this.K;
        double d5 = Math.max(this.M[n3][1], this.M[n4][1]) + this.I + this.K;
        AABB aABB = new AABB(d2, (double)(this.b.getMinBuildHeight() - 16), d4, d3, (double)(this.b.getMaxBuildHeight() - 1 + 16), d5);
        List<Entity> list = this.b.getEntities((Entity)null, aABB, entity -> this.a(n2, entity.getX(), entity.getZ())[0] <= this.I + this.K * 0.75);
        for (Entity entity2 : list) {
            this.a(entity2, true);
        }
    }

    private void a(double d2, double d3, double d4, double d5, boolean bl) {
        AABB aABB = new AABB(d2 - d4, d5, d3 - d4, d2 + d4, (double)(this.b.getMaxBuildHeight() - 1 + 64), d3 + d4);
        for (Entity entity2 : this.b.getEntities((Entity)null, aABB, entity -> Math.hypot(entity.getX() - d2, entity.getZ() - d3) <= d4)) {
            this.a(entity2, bl);
        }
    }

    private void a(Entity entity, boolean bl) {
        if ((bl || entity instanceof LivingEntity) && cyou.rimuru.shootingstardemo.mc1211.star.k.a(entity, this.i)) {
            cyou.rimuru.shootingstardemo.mc1211.star.k.a(this.b, entity, this.i, this.h.c());
        }
    }

    private void l() {
        if (this.Q) {
            this.Q = false;
            for (int[] nArray : this.G) {
                ChunkPos chunkPos = new ChunkPos(nArray[0], nArray[1]);
                this.b.getChunkSource().removeRegionTicket(D, chunkPos, nArray[2], chunkPos);
            }
        }
    }

    @Override
    protected void f() {
        for (a a2 : this.O) {
            cyou.rimuru.shootingstardemo.mc1211.star.a.a(a2);
        }
        for (a a2 : this.P) {
            cyou.rimuru.shootingstardemo.mc1211.star.a.a(a2);
        }
        this.l();
    }
}

