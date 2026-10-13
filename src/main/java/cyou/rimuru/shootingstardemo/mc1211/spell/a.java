/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.spell;

import cyou.rimuru.shootingstardemo.mc1211.magic.e;
import cyou.rimuru.shootingstardemo.mc1211.magic.f;
import cyou.rimuru.shootingstardemo.mc1211.spell.d;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

public abstract class a {
    public final cyou.rimuru.shootingstardemo.mc1211.magic.d a;
    public final ServerLevel b;
    public final UUID c;
    public final Vec3 d;
    public final Vec3 e;
    public final long f;
    public final boolean g;
    public final f h;
    protected ServerPlayer i;
    protected int j;
    private double k;

    protected a(cyou.rimuru.shootingstardemo.mc1211.magic.d d2, ServerPlayer serverPlayer, Vec3 vec3, Vec3 vec32, long l2, boolean bl) {
        this(d2, serverPlayer, vec3, vec32, l2, bl, cyou.rimuru.shootingstardemo.mc1211.magic.f.a(d2));
    }

    protected a(cyou.rimuru.shootingstardemo.mc1211.magic.d d2, ServerPlayer serverPlayer, Vec3 vec3, Vec3 vec32, long l2, boolean bl, f f2) {
        this.a = d2;
        this.h = f2;
        this.b = serverPlayer.serverLevel();
        this.c = serverPlayer.getUUID();
        this.i = serverPlayer;
        this.d = vec3;
        this.e = vec32;
        this.f = l2;
        this.g = bl;
    }

    final boolean a() {
        this.i = this.b.getServer().getPlayerList().getPlayer(this.c);
        if (this.i != null && this.i.level() != this.b) {
            this.i = null;
        }
        int n2 = (int)Math.floor(this.k + 1.0E-6);
        while (this.j <= n2 && !this.b()) {
            this.a(this.j);
            ++this.j;
        }
        this.k += (double)this.h.d();
        return this.b();
    }

    protected boolean b() {
        return this.j > this.a.f();
    }

    protected abstract void a(int var1);

    protected int[] c() {
        return new int[0];
    }

    public cyou.rimuru.shootingstardemo.mc1211.net.e d() {
        return new cyou.rimuru.shootingstardemo.mc1211.net.e(cyou.rimuru.shootingstardemo.mc1211.magic.e.a(this.a), this.i == null ? -1 : this.i.getId(), this.d.x, this.d.y, this.d.z, this.e.x, this.e.y, this.e.z, this.i == null ? 0.0f : this.i.getYRot(), this.f, this.c(), (this.g ? 1 : 0) | (this.g && cyou.rimuru.shootingstardemo.mc1211.spell.d.a(this.b) ? 4 : 0), this.h.b(), this.h.d());
    }

    public double e() {
        return 512.0;
    }

    protected void f() {
    }

    public int g() {
        return this.j;
    }

    protected int b(int n2) {
        return Math.max(1, Math.round((float)n2 / this.h.d()));
    }

    protected Vec3 h() {
        return this.i != null && this.i.isAlive() ? this.i.position() : this.d;
    }

    protected static double a(double d2) {
        d2 = Math.max(0.0, Math.min(1.0, d2));
        return 1.0 - Math.pow(1.0 - d2, 3.0);
    }
}

