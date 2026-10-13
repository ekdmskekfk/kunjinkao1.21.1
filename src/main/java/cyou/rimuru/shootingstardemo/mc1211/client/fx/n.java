/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.fx;

import cyou.rimuru.shootingstardemo.mc1211.client.cinematic.i;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.b;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.d;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.e;
import cyou.rimuru.shootingstardemo.mc1211.client.render.f;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public abstract class n {
    protected final cyou.rimuru.shootingstardemo.mc1211.magic.d m;
    protected final cyou.rimuru.shootingstardemo.mc1211.net.e n;
    protected final Vec3 o;
    protected final Vec3 p;
    protected final long q;
    protected final float r;
    protected final boolean s;
    protected final RandomSource t;
    protected final float u;
    protected int v;
    private double a;
    private i b;

    protected n(cyou.rimuru.shootingstardemo.mc1211.net.e e2) {
        this.m = cyou.rimuru.shootingstardemo.mc1211.magic.e.a(e2.b());
        this.n = e2;
        this.o = new Vec3(e2.d(), e2.e(), e2.f());
        this.p = new Vec3(e2.g(), e2.h(), e2.i());
        this.q = e2.k();
        this.r = (float)((double)(this.q & 0xFFFFL) / 65535.0);
        this.s = (e2.m() & 1) != 0;
        this.t = RandomSource.create((long)this.q);
        this.u = cyou.rimuru.shootingstardemo.mc1211.magic.f.c.a(e2.o());
    }

    final void a(Minecraft minecraft, ClientLevel clientLevel) {
        int n2 = (int)Math.floor(this.a + 1.0E-6);
        while (this.v <= n2) {
            this.a(minecraft, clientLevel, this.v);
            ++this.v;
        }
        this.a += (double)this.u;
    }

    public float d(float f2) {
        return this.v == 0 ? f2 * this.u : (float)(this.a - (double)this.u + 1.0 + (double)(f2 * this.u));
    }

    public float j() {
        return this.u;
    }

    protected abstract void a(Minecraft var1, ClientLevel var2, int var3);

    public boolean k() {
        return this.v > this.d();
    }

    protected int d() {
        return this.m.f();
    }

    protected void h() {
    }

    public Vec3 l() {
        return this.p;
    }

    public double a() {
        return 600.0;
    }

    public void a(d d2, List<cyou.rimuru.shootingstardemo.mc1211.client.render.e> list) {
    }

    public void a(d d2, f f2) {
    }

    public void a(GuiGraphics guiGraphics, d d2) {
    }

    public int m() {
        return this.n.c();
    }

    public cyou.rimuru.shootingstardemo.mc1211.magic.d n() {
        return this.m;
    }

    public long o() {
        return this.q;
    }

    public int p() {
        return this.v;
    }

    public boolean q() {
        return this.s;
    }

    public i e() {
        if (this.b == null) {
            double d2 = Math.toRadians(this.n.j());
            final Vec3 vec3 = new Vec3(-Math.sin(d2), 0.0, Math.cos(d2));
            this.b = new i(){
                final /* synthetic */ n b;
                {
                    this.b = n.this;
                }

                @Override
                public Vec3 a(float f2) {
                    ClientLevel clientLevel = Minecraft.getInstance().level;
                    Entity entity = clientLevel == null ? null : this.b.a(clientLevel);
                    return entity != null ? entity.getPosition(f2) : this.b.o;
                }

                @Override
                public Vec3 a() {
                    return this.b.r();
                }

                @Override
                public Vec3 b() {
                    return vec3;
                }
            };
        }
        return this.b;
    }

    protected Vec3 r() {
        return this.p;
    }

    b c(float f2) {
        return null;
    }

    boolean b(Vec3 vec3) {
        return false;
    }

    protected Entity a(ClientLevel clientLevel) {
        return clientLevel.getEntity(this.n.c());
    }

    protected Vec3 a(d d2) {
        Entity entity = d2.a().level == null ? null : this.a(d2.a().level);
        return entity != null ? entity.getPosition(d2.b()) : this.o;
    }

    protected Vec3 b(ClientLevel clientLevel) {
        Entity entity = this.a(clientLevel);
        return entity != null ? entity.position() : this.o;
    }

    protected float b(d d2) {
        return this.d(d2.b()) / 20.0f;
    }

    protected float c(d d2) {
        return this.d(d2.b());
    }

    protected static float a(float f2, float f3, float f4, float f5, float f6) {
        return (float)(Mth.clamp((double)((double)(f2 - f3) / Math.max((double)(f4 - f3), 0.001)), (double)0.0, (double)1.0) * (1.0 - Mth.clamp((double)((double)(f2 - f5) / Math.max((double)(f6 - f5), 0.001)), (double)0.0, (double)1.0)));
    }

    protected static float a(d d2, Vec3 vec3, double d3) {
        return (float)Mth.clamp((double)(1.0 - d2.c().distanceTo(vec3) / d3), (double)0.0, (double)1.0);
    }

    protected static void a(f f2, float f3, Vec3 vec3, int n2, int n3, float f4) {
        if (f3 < 0.0f || f3 > 0.22f || f4 <= 0.0f) {
            return;
        }
        int n4 = (int)(f3 / 0.045f);
        if (n4 == 2) {
            return;
        }
        float[] fArray = e.a(vec3);
        f2.a(f4, n4 % 2 == 0 ? n2 : n3, fArray[0], fArray[1]);
    }

    protected static void a(f f2, float f3, Vec3 vec3, float f4, float f5, int ... nArray) {
        if (f3 < 0.0f || f4 <= 0.0f) {
            return;
        }
        int n2 = (int)(f3 / f5);
        if (n2 >= nArray.length || nArray[n2] < 0) {
            return;
        }
        float[] fArray = e.a(vec3);
        f2.a(f4, nArray[n2], fArray[0], fArray[1]);
    }

    protected static void a(f f2, float f3, float f4) {
        if (f3 <= 0.001f) {
            return;
        }
        f2.c = Math.max(f2.c, f3);
        f2.u = Math.max(f2.u, 0.3f * f3);
        f2.w = Math.max(f2.w, f3);
        f2.v = Math.max(f2.v, 0.35f * f3);
        if (f3 > 0.5f && f4 > 0.0f) {
            f2.y = f2.y > 0.0f ? Math.min(f2.y, f4) : f4;
        }
    }

    protected double d(double d2) {
        return this.t.nextGaussian() * d2;
    }

    protected static void a(ClientLevel clientLevel, SoundEvent soundEvent, Vec3 vec3, float f2, float f3) {
        clientLevel.playLocalSound(vec3.x, vec3.y, vec3.z, soundEvent, SoundSource.PLAYERS, f2, f3, false);
    }

    protected static void a(ClientLevel clientLevel, Holder<SoundEvent> holder, Vec3 vec3, float f2, float f3) {
        cyou.rimuru.shootingstardemo.mc1211.client.fx.n.a(clientLevel, (SoundEvent)holder.value(), vec3, f2, f3);
    }

    protected static void a(ClientLevel clientLevel, ParticleOptions particleOptions, Vec3 vec3, double d2, double d3, double d4) {
        clientLevel.addParticle(particleOptions, true, vec3.x, vec3.y, vec3.z, d2, d3, d4);
    }

    protected static DustParticleOptions a(int n2, float f2) {
        return new DustParticleOptions(new Vector3f((float)(n2 >> 16 & 0xFF) / 255.0f, (float)(n2 >> 8 & 0xFF) / 255.0f, (float)(n2 & 0xFF) / 255.0f), f2);
    }

    protected void a(ClientLevel clientLevel, ParticleOptions particleOptions, Vec3 vec3, double d2, int n2, double d3, double d4) {
        for (int i2 = 0; i2 < n2; ++i2) {
            double d5 = this.t.nextDouble() * Math.PI * 2.0;
            double d6 = Math.cos(d5);
            double d7 = Math.sin(d5);
            cyou.rimuru.shootingstardemo.mc1211.client.fx.n.a(clientLevel, particleOptions, vec3.add(d6 * d2, 0.0, d7 * d2), d6 * d3, d4, d7 * d3);
        }
    }

    protected static float[] b(int n2) {
        return new float[n2 * 4];
    }
}

