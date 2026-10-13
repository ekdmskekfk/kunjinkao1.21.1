/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.star;

import cyou.rimuru.shootingstardemo.mc1211.magic.f;
import cyou.rimuru.shootingstardemo.mc1211.registry.b;
import cyou.rimuru.shootingstardemo.mc1211.registry.c;
import cyou.rimuru.shootingstardemo.mc1211.spell.d;
import cyou.rimuru.shootingstardemo.mc1211.star.a;
import cyou.rimuru.shootingstardemo.mc1211.star.k;
import cyou.rimuru.shootingstardemo.mc1211.star.l;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class e
extends cyou.rimuru.shootingstardemo.mc1211.spell.a {
    public static final float k = 4.0f;
    public static final double l = 400.0;
    public static final int m = 18;
    public static final double n = 88.0;
    public static final int o = 14;
    public static final double p = 7.0;
    public static final double q = 2.5;
    public static final double r = 36.0;
    public static final double s = 6.5;
    public static final double t = 20.0;
    public static final int u = 60;
    private static final int F = 36;
    private static final int G = 3;
    public static final int v = 5;
    public static final int w = 7;
    public static final int x = 9;
    public static final int y = 31;
    public static final int z = 290;
    public static final int A = 322;
    public static final int B = 36;
    public static final int C = 480;
    public static final double D = 260.0;
    public static final double E = 420.0;
    private static final TicketType<ChunkPos> H = TicketType.create((String)"shooting_star_demo:gungnir", Comparator.comparingLong(ChunkPos::toLong));
    private final ChunkPos I;
    private final int J;
    private final int K;
    private final double[] L = new double[3];
    private boolean M = true;
    private cyou.rimuru.shootingstardemo.mc1211.star.a N;
    private final double O;
    private final double P;
    private double Q;
    private double R;
    private double S;
    private double T;
    private double U;
    private double V;
    private double W;
    private double X;
    private int Y;
    private int Z;
    private final ThreadLocal<a> aa = ThreadLocal.withInitial(a::new);
    private final Thread ab = Thread.currentThread();

    public e(ServerPlayer serverPlayer, Vec3 vec3, long l2, f f2) {
        super(cyou.rimuru.shootingstardemo.mc1211.star.l.b, serverPlayer, serverPlayer.position(), vec3, l2, false, f2);
        this.O = cyou.rimuru.shootingstardemo.mc1211.star.e.a(f2.b());
        this.P = cyou.rimuru.shootingstardemo.mc1211.star.e.b(this.O);
        this.I = new ChunkPos(BlockPos.containing((Position)vec3));
        this.K = Mth.floor((double)vec3.y) - 1;
        this.J = cyou.rimuru.shootingstardemo.mc1211.star.e.a(this.b.getMinBuildHeight(), vec3.y);
        this.k();
        for (int i2 = 0; i2 < this.L.length; ++i2) {
            this.L[i2] = (double)Math.floorMod(l2 >> i2 * 11, 6283L) / 1000.0;
        }
        this.b.getChunkSource().addRegionTicket(H, this.I, this.Z, this.I);
    }

    private void k() {
        this.Q = 400.0 * this.O;
        this.R = 88.0 * this.O;
        this.S = Math.max(this.J - (this.b.getMinBuildHeight() + 2), 0);
        this.T = 0.0;
        this.U = 7.0 * Math.max(this.O, 0.5);
        this.V = 36.0 * Math.min(this.O, 1.0);
        this.W = 6.5 * this.P;
        this.X = 20.0 * this.P;
        this.Y = (int)Math.round(60.0 * this.P);
        this.Z = Math.min((int)Math.ceil((this.Q + this.V + 16.0) / 16.0), 32);
    }

    public static int a(int n2, double d2) {
        return Math.min(n2 + 18, Mth.floor((double)d2) - 2);
    }

    public static double a(float f2) {
        return f2 / 4.0f;
    }

    public static double b(double d2) {
        return Math.min(1.0, Math.sqrt(d2));
    }

    @Override
    public double e() {
        return this.Q * 2.5 + 200.0;
    }

    public double i() {
        return this.Q;
    }

    public static void j() {
    }

    public static double c(double d2) {
        return cyou.rimuru.shootingstardemo.mc1211.star.e.a(d2, 400.0);
    }

    public static double a(double d2, double d3) {
        if (d2 <= 322.0) {
            return 0.0;
        }
        return d3 * Math.pow((d2 - 322.0) / 36.0, 0.6);
    }

    public static double d(double d2) {
        return cyou.rimuru.shootingstardemo.mc1211.star.e.b(d2, 400.0);
    }

    public static double b(double d2, double d3) {
        return 322.0 + 36.0 * Math.pow(Math.max(d2, 0.0) / d3, 1.6666666666666667);
    }

    private double e(double d2) {
        return this.Q + this.O * (6.0 * Math.sin(3.0 * d2 + this.L[0]) + 4.0 * Math.sin(7.0 * d2 + this.L[1]) + 2.4 * Math.sin(13.0 * d2 + this.L[2]));
    }

    private boolean b(int n2, int n3) {
        double d2;
        double d3 = (double)n2 + 0.5 - ((double)Mth.floor((double)this.e.x) + 0.5);
        return cyou.rimuru.shootingstardemo.mc1211.star.e.c(d3, d2 = (double)n3 + 0.5 - ((double)Mth.floor((double)this.e.z) + 0.5)) <= this.W;
    }

    private static double c(double d2, double d3) {
        double d4 = Math.abs(d2);
        double d5 = Math.abs(d3);
        return Math.max(Math.max(d4, d5), (d4 + d5) * 0.7071);
    }

    private int c(int n2, int n3) {
        return this.a(n2, n3, null, 0);
    }

    public int a(int n2, int n3) {
        return this.c(n2, n3);
    }

    private int a(int n2, int n3, double[] dArray, int n4) {
        double d2;
        double d3;
        if (dArray != null) {
            dArray[n4] = Double.NaN;
        }
        if (this.b(n2, n3)) {
            return Integer.MIN_VALUE;
        }
        double d4 = (double)n2 + 0.5 - this.e.x;
        double d5 = (double)n3 + 0.5 - this.e.z;
        double d6 = Math.sqrt(d4 * d4 + d5 * d5);
        if (d6 > (d3 = this.e(Math.atan2(d5, d4)))) {
            return Integer.MIN_VALUE;
        }
        double d7 = this.J;
        double d8 = Math.max(d3 - this.V, this.R + 1.0);
        double d9 = Math.min(d6 / d8, 1.0);
        d7 -= this.T * (1.0 - Math.pow(d9, 1.6));
        double d10 = this.a(d4, d5, d6);
        if (dArray != null) {
            dArray[n4] = d10;
        }
        d7 -= d10 * this.U;
        if (d6 < this.R) {
            d2 = d6 / this.R;
            d7 -= this.S * (1.0 - d2 * d2) * (1.0 - d2 * d2 * 0.35);
        }
        if ((d2 = d6 - (d3 - this.V)) > 0.0) {
            d7 += d2 * d2 / this.V * 1.1;
        }
        return Math.max(Mth.floor((double)d7), this.b.getMinBuildHeight() + 2);
    }

    private boolean a(int[] nArray, int n2, int[] nArray2) {
        a a2 = this.aa.get();
        if (a2.b.length < n2 * 256) {
            a2.b = new double[n2 * 256];
        }
        a2.a.clear();
        for (int i2 = 0; i2 < n2; ++i2) {
            a2.a.put(ChunkPos.asLong((int)nArray[i2 * 2], (int)nArray[i2 * 2 + 1]), i2);
            int n3 = nArray[i2 * 2] << 4;
            int n4 = nArray[i2 * 2 + 1] << 4;
            for (int i3 = 0; i3 < 16; ++i3) {
                for (int i4 = 0; i4 < 16; ++i4) {
                    int n5 = i2 * 256 + i3 * 16 + i4;
                    nArray2[n5] = this.a(n3 + i3, n4 + i4, a2.b, n5);
                }
            }
        }
        return true;
    }

    private double a(double d2, double d3, double d4) {
        if (d4 < this.R * 0.8 || d4 > this.Q * 0.92) {
            return 0.0;
        }
        double d5 = Math.atan2(d3, d2);
        double d6 = 0.0;
        for (int i2 = 0; i2 < 14; ++i2) {
            double d7;
            double d8 = (double)i2 / 14.0 * Math.PI * 2.0 + this.L[i2 % this.L.length] * 0.3;
            double d9 = Math.sin(d4 / this.O * 0.03 + (double)i2 * 1.7) * 0.05 + Math.sin(d4 / this.O * 0.011 + (double)i2) * 0.04;
            double d10 = Math.abs(Math.IEEEremainder(d5 - d8 - d9, Math.PI * 2)) * d4;
            if (!(d10 < (d7 = 3.2 * Math.max(this.O, 0.5) * (1.0 - d4 / this.Q * 0.7)))) continue;
            d6 = Math.max(d6, 1.0 - d10 / d7);
        }
        return d6;
    }

    private BlockState a(int n2, int n3, int n4, BlockState blockState) {
        double d2;
        if (!blockState.getFluidState().isEmpty() || blockState.is(cyou.rimuru.shootingstardemo.mc1211.registry.b.hull) || blockState.is(cyou.rimuru.shootingstardemo.mc1211.registry.b.b)) {
            return null;
        }
        double d3 = Math.hypot((double)n2 + 0.5 - this.e.x, (double)n4 + 0.5 - this.e.z);
        a a2 = this.aa.get();
        int n5 = a2.a.getOrDefault(ChunkPos.asLong((int)(n2 >> 4), (int)(n4 >> 4)), -1);
        double d4 = d2 = n5 >= 0 ? a2.b[n5 * 256 + (n2 & 0xF) * 16 + (n4 & 0xF)] : Double.NaN;
        if (Double.isNaN(d2)) {
            return this.a(n2, n4, d3, this.a((double)n2 + 0.5 - this.e.x, (double)n4 + 0.5 - this.e.z, d3));
        }
        BlockState blockState2 = this.a(n2, n4, d3, d2);
        if (cyou.rimuru.shootingstardemo.mc1211.star.a.d && n5 == 0 && this.N != null && Thread.currentThread() == this.ab) {
            this.N.a(blockState2 != this.a(n2, n4, d3, this.a((double)n2 + 0.5 - this.e.x, (double)n4 + 0.5 - this.e.z, d3)));
        }
        return blockState2;
    }

    private BlockState a(int n2, int n3, double d2, double d3) {
        if (d3 > 0.35) {
            return d3 > 0.7 ? Blocks.MAGMA_BLOCK.defaultBlockState() : (this.d(n2 * 3, n3 * 5) < 0.5 ? Blocks.BLACKSTONE.defaultBlockState() : Blocks.MAGMA_BLOCK.defaultBlockState());
        }
        double d4 = this.d(n2, n3);
        double d5 = (this.d(n3 * 7 + 3, n2 * 13 - 5) - 0.5) * 24.0 * this.O;
        double d6 = (d2 + d5) * (200.0 / this.Q);
        if (d6 < 10.0) {
            return d4 < 0.5 ? Blocks.MAGMA_BLOCK.defaultBlockState() : (d4 < 0.8 ? Blocks.OBSIDIAN.defaultBlockState() : Blocks.BLACKSTONE.defaultBlockState());
        }
        if (d6 < this.R * 200.0 / this.Q) {
            return d4 < 0.34 ? Blocks.BLACKSTONE.defaultBlockState() : (d4 < 0.58 ? Blocks.BASALT.defaultBlockState() : (d4 < 0.7 ? Blocks.MAGMA_BLOCK.defaultBlockState() : (d4 < 0.8 ? Blocks.OBSIDIAN.defaultBlockState() : Blocks.COBBLED_DEEPSLATE.defaultBlockState())));
        }
        if (d6 < 100.0) {
            return d4 < 0.3 ? Blocks.COARSE_DIRT.defaultBlockState() : (d4 < 0.45 ? Blocks.BLACKSTONE.defaultBlockState() : (d4 < 0.65 ? Blocks.GRAVEL.defaultBlockState() : (d4 < 0.73 ? Blocks.BASALT.defaultBlockState() : (d4 < 0.86 ? Blocks.TUFF.defaultBlockState() : (d4 < 0.88 ? Blocks.MAGMA_BLOCK.defaultBlockState() : Blocks.COBBLED_DEEPSLATE.defaultBlockState())))));
        }
        if (d6 < 170.0) {
            return d4 < 0.35 ? Blocks.COARSE_DIRT.defaultBlockState() : (d4 < 0.5 ? Blocks.GRAVEL.defaultBlockState() : (d4 < 0.7 ? Blocks.DIRT.defaultBlockState() : null));
        }
        return d4 < 0.4 ? Blocks.COARSE_DIRT.defaultBlockState() : null;
    }

    private double d(int n2, int n3) {
        long l2 = this.f ^ (long)n2 * -7046029254386353131L ^ (long)n3 * -4417276706812531889L;
        l2 = (l2 ^ l2 >>> 31) * -4658895280553007687L;
        l2 ^= l2 >>> 29;
        return (double)(l2 >>> 11) / 9.007199254740992E15;
    }

    private Map<BlockPos, BlockState> l() {
        BlockState blockState = cyou.rimuru.shootingstardemo.mc1211.registry.b.hull.defaultBlockState();
        BlockState blockState2 = cyou.rimuru.shootingstardemo.mc1211.registry.b.b.defaultBlockState();
        HashMap<BlockPos, BlockState> hashMap = new HashMap<BlockPos, BlockState>();
        int n2 = Mth.floor((double)this.e.x);
        int n3 = Mth.floor((double)this.e.z);
        int n4 = this.b.getMinBuildHeight();
        int n5 = this.b.getMaxBuildHeight() - 1;
        int n6 = Mth.ceil((double)this.X) + 1;
        for (int i2 = n4; i2 <= n5; ++i2) {
            int n7 = n5 - i2;
            boolean bl = Math.floorMod(i2 - this.K, 24) == 0 || Math.floorMod(i2 - this.K, 24) == 1 && i2 > this.K;
            for (int i3 = -n6; i3 <= n6; ++i3) {
                for (int i4 = -n6; i4 <= n6; ++i4) {
                    double d2 = this.c(i3, i4);
                    BlockPos blockPos = new BlockPos(n2 + i3, i2, n3 + i4);
                    if (d2 <= this.W) {
                        boolean bl2 = d2 > this.W - 1.0;
                        boolean bl3 = bl2 && Math.abs(Math.abs(i3) - Math.abs(i4)) <= 0 && i3 != 0;
                        boolean bl4 = n7 == 0 && d2 <= this.W * 0.5;
                        hashMap.put(blockPos, bl2 && (bl3 || bl) || bl4 ? blockState2 : blockState);
                        continue;
                    }
                    if (n7 >= this.Y || i3 != 0 && i4 != 0) continue;
                    double d3 = 1.0 - (double)n7 / (double)this.Y;
                    double d4 = this.W + (this.X - this.W) * Math.pow(d3, 0.7);
                    double d5 = Math.abs(i3) + Math.abs(i4);
                    if (!(d5 <= d4)) continue;
                    hashMap.put(blockPos, d5 > d4 - 1.0 || n7 == 0 ? blockState2 : blockState);
                }
            }
        }
        return hashMap;
    }

    @Override
    protected void a(int n2) {
        if (n2 == 322) {
            this.N = cyou.rimuru.shootingstardemo.mc1211.star.a.a(this.b, this.e, this.Q + 12.0, this::c, this::a, null).a(this::a).a(36).a("Gungnir").a(new cyou.rimuru.shootingstardemo.mc1211.star.a.h(){

                @Override
                public void a(ChunkPos chunkPos) {
                    e.this.b.getChunkSource().addRegionTicket(H, chunkPos, 0, chunkPos);
                }

                @Override
                public void b(ChunkPos chunkPos) {
                    e.this.b.getChunkSource().removeRegionTicket(H, chunkPos, 0, chunkPos);
                }
            });
            this.N.a(this.X + 2.0);
            cyou.rimuru.shootingstardemo.mc1211.star.a.a(this.b, this.l());
        }
        double d2 = cyou.rimuru.shootingstardemo.mc1211.star.e.a((double)n2, this.Q);
        if (this.N != null && !this.N.i()) {
            this.N.b(Math.max(0.0, Math.min(cyou.rimuru.shootingstardemo.mc1211.star.e.a((double)(n2 + 3), this.Q) - 10.0 * this.O, this.Q + 12.0)));
            this.N.a(Math.max(0.0, Math.min(d2 - 10.0 * this.O, this.Q + 12.0)));
        }
        if (n2 >= 322 && n2 <= 370 && (n2 - 322) % 2 == 0) {
            this.a(Math.min(d2, this.Q + 4.0 * this.O), n2 == 322 || (n2 - 322) % 10 == 0);
        }
        if (n2 > 322 && d2 > this.Q && cyou.rimuru.shootingstardemo.mc1211.star.e.a((double)(n2 - 1), this.Q) < this.Q * 2.5) {
            this.d(Math.max(cyou.rimuru.shootingstardemo.mc1211.star.e.a((double)(n2 - 1), this.Q), this.Q), Math.min(d2, this.Q * 2.5));
        }
        if (this.M && n2 > 358 && this.N != null && this.N.i()) {
            this.m();
        }
    }

    private void a(double d2, boolean bl) {
        if (d2 <= 0.5) {
            return;
        }
        AABB aABB = new AABB(this.e.x - d2, (double)(this.b.getMinBuildHeight() - 64), this.e.z - d2, this.e.x + d2, (double)(this.b.getMaxBuildHeight() - 1 + 64), this.e.z + d2);
        List<Entity> list = this.b.getEntities((Entity)null, aABB, entity -> Math.hypot(entity.getX() - this.e.x, entity.getZ() - this.e.z) <= d2 + (double)entity.getBbWidth() * 0.5 && entity.getY() > (double)this.J - this.S - 8.0);
        for (Entity entity2 : list) {
            if (!bl && !(entity2 instanceof LivingEntity) || !cyou.rimuru.shootingstardemo.mc1211.star.k.a(entity2, this.i)) continue;
            cyou.rimuru.shootingstardemo.mc1211.star.k.a(this.b, entity2, this.i, this.h.c());
        }
    }

    private void d(double d2, double d3) {
        AABB aABB = new AABB(this.e.x - d3, (double)this.b.getMinBuildHeight(), this.e.z - d3, this.e.x + d3, (double)(this.b.getMaxBuildHeight() - 1), this.e.z + d3);
        for (Entity entity2 : this.b.getEntities((Entity)null, aABB, entity -> {
            double d4 = Math.hypot(entity.getX() - this.e.x, entity.getZ() - this.e.z);
            return d4 > d2 && d4 <= d3;
        })) {
            LivingEntity livingEntity;
            Player player;
            if (entity2 instanceof Player && ((player = (Player)entity2).isCreative() || player.isSpectator())) continue;
            double d4 = Math.hypot(entity2.getX() - this.e.x, entity2.getZ() - this.e.z);
            double d5 = 1.0 - (d4 - this.Q) / (this.Q * 1.5);
            Vec3 vec3 = new Vec3(entity2.getX() - this.e.x, 0.0, entity2.getZ() - this.e.z).normalize();
            entity2.setDeltaMovement(entity2.getDeltaMovement().add(vec3.scale(1.2 + 2.8 * d5)).add(0.0, 0.6 + 1.2 * d5, 0.0));
            entity2.hurtMarked = true;
            if (!(entity2 instanceof LivingEntity) || (livingEntity = (LivingEntity)entity2) != this.i && !cyou.rimuru.shootingstardemo.mc1211.spell.d.a((Entity)livingEntity, this.i)) continue;
            float f2 = (float)(4.0 + 26.0 * d5 * d5);
            cyou.rimuru.shootingstardemo.mc1211.spell.d.a(this.b, livingEntity, livingEntity == this.i ? null : this.i, this.h.a() ? cyou.rimuru.shootingstardemo.mc1211.registry.c.a : cyou.rimuru.shootingstardemo.mc1211.registry.c.b, Math.min(f2, this.h.c()));
        }
    }

    private void m() {
        if (this.M) {
            this.M = false;
            this.b.getChunkSource().removeRegionTicket(H, this.I, this.Z, this.I);
        }
    }

    @Override
    protected void f() {
        cyou.rimuru.shootingstardemo.mc1211.star.a.a(this.N);
        this.m();
    }

    private static final class a {
        final Long2IntOpenHashMap a = new Long2IntOpenHashMap();
        double[] b = new double[0];

        private a() {
        }
    }
}

