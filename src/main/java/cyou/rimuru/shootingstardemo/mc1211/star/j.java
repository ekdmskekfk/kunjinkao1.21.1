/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.star;

import cyou.rimuru.shootingstardemo.mc1211.magic.f;
import cyou.rimuru.shootingstardemo.mc1211.spell.d;
import cyou.rimuru.shootingstardemo.mc1211.star.a;
import cyou.rimuru.shootingstardemo.mc1211.star.k;
import cyou.rimuru.shootingstardemo.mc1211.star.l;
import java.util.Comparator;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class j
extends cyou.rimuru.shootingstardemo.mc1211.spell.a {
    public static final double k = 200.0;
    public static final int l = 5;
    public static final int m = 7;
    public static final int n = 9;
    public static final int o = 31;
    public static final int p = 314;
    public static final int q = 352;
    public static final int r = 462;
    public static final int s = 486;
    public static final int t = 526;
    public static final double u = 260.0;
    public static final double v = 420.0;
    private static final TicketType<ChunkPos> x = TicketType.create((String)"shooting_star_demo:shooting_star", Comparator.comparingLong(ChunkPos::toLong));
    public final double w;
    private final int y;
    private final ChunkPos z;
    private BlockPos A;
    private boolean B = true;
    private a C;

    public j(ServerPlayer serverPlayer, Vec3 vec3, long l2, f f2) {
        super(cyou.rimuru.shootingstardemo.mc1211.star.l.a, serverPlayer, serverPlayer.position(), vec3, l2, false, f2);
        this.w = f2.b();
        this.y = Math.min((int)Math.ceil((this.w + 16.0) / 16.0), 32);
        this.z = new ChunkPos(BlockPos.containing((Position)vec3));
        this.b.getChunkSource().addRegionTicket(x, this.z, this.y, this.z);
    }

    public static void i() {
    }

    public static double a(double d2, double d3) {
        if (d2 < 352.0) {
            return 0.0;
        }
        if (d2 < 462.0) {
            return d3;
        }
        double d4 = Mth.clamp((double)((d2 - 462.0) / 24.0), (double)0.0, (double)1.0);
        return d3 * Math.pow(1.0 - d4, 1.6);
    }

    @Override
    public double e() {
        return this.w + 400.0;
    }

    @Override
    protected void a(int n2) {
        if (this.i != null && n2 % 10 == 0) {
            cyou.rimuru.shootingstardemo.mc1211.spell.d.a(this.i);
        }
        if (n2 == 31 && this.i != null) {
            this.A = cyou.rimuru.shootingstardemo.mc1211.star.k.a(this.i, this.e, this.w, this.b(495));
        }
        if (n2 == 352) {
            this.C = new a(this.b, this.e, this.w, this.A).a(new a.h(){

                @Override
                public void a(ChunkPos chunkPos) {
                    j.this.b.getChunkSource().addRegionTicket(x, chunkPos, 0, chunkPos);
                }

                @Override
                public void b(ChunkPos chunkPos) {
                    j.this.b.getChunkSource().removeRegionTicket(x, chunkPos, 0, chunkPos);
                }
            });
        }
        if (this.C != null && !this.C.i()) {
            this.C.j();
        }
        if (n2 >= 352 && n2 < 486 && (n2 - 352) % 2 == 0) {
            this.a(cyou.rimuru.shootingstardemo.mc1211.star.j.a(n2, this.w), n2 == 352 || (n2 - 352) % 10 == 0);
        }
        if (this.B && n2 > 352 && (this.C == null || this.C.i())) {
            this.j();
        }
    }

    private void a(double d2, boolean bl) {
        if (d2 <= 0.5) {
            return;
        }
        AABB aABB = new AABB(this.e.x - d2, (double)(this.b.getMinBuildHeight() - 64), this.e.z - d2, this.e.x + d2, (double)(this.b.getMaxBuildHeight() - 1 + 64), this.e.z + d2);
        double d3 = d2 * d2;
        List<Entity> list = this.b.getEntities((Entity)null, aABB, entity -> Mth.square((double)(entity.getX() - this.e.x)) + Mth.square((double)(entity.getZ() - this.e.z)) <= d3);
        for (Entity entity2 : list) {
            if (!bl && !(entity2 instanceof LivingEntity) || !cyou.rimuru.shootingstardemo.mc1211.star.k.a(entity2, this.i)) continue;
            cyou.rimuru.shootingstardemo.mc1211.star.k.a(this.b, entity2, this.i, this.h.c());
        }
    }

    private void j() {
        if (this.B) {
            this.B = false;
            this.b.getChunkSource().removeRegionTicket(x, this.z, this.y, this.z);
        }
    }

    @Override
    protected void f() {
        cyou.rimuru.shootingstardemo.mc1211.star.a.a(this.C);
        this.j();
    }
}

