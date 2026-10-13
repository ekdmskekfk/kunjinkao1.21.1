/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.fx;

import cyou.rimuru.shootingstardemo.mc1211.client.fx.a;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.b;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.d;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.e;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.i;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.l;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.m;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.n;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.p;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.q;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.r;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.x;
import cyou.rimuru.shootingstardemo.mc1211.client.render.f;
import cyou.rimuru.shootingstardemo.mc1211.client.render.j;
import cyou.rimuru.shootingstardemo.mc1211.registry.g;
import cyou.rimuru.shootingstardemo.mc1211.star.c;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

public final class k
extends n {
    static final int a = 7;
    static final int b = 9;
    static final int c = 290;
    static final int d = 440;
    static final int e = 29;
    static final int f = 470;
    static final int g = 16;
    static final int h = 6;
    private static final int j = 12101887;
    private static final int k = 14998783;
    private static final float l = 0.083333336f;
    private static final int[] w = new int[]{23, 21, 26, 24, 22, 25};
    private static final int[] x = new int[]{23, 21, 26, 24, 25, 22, 23, 1, 21, 26, 24, 6, 25, 22, 21, 26};
    private final cyou.rimuru.shootingstardemo.mc1211.client.cinematic.i y = super.e();
    private final double z;
    public final boolean i;
    private final List<SoundInstance> A = new ArrayList<SoundInstance>();
    private final double B;
    private final Vec3[] C = new Vec3[7];
    private final boolean[] D = new boolean[7];
    private boolean E;

    k(cyou.rimuru.shootingstardemo.mc1211.net.e e2) {
        super(e2);
        this.z = cyou.rimuru.shootingstardemo.mc1211.star.l.c.j().a(e2.n());
        Minecraft minecraft = Minecraft.getInstance();
        this.i = minecraft.player != null && minecraft.player.getId() == e2.c();
        this.B = cyou.rimuru.shootingstardemo.mc1211.star.c.a(this.o.x, this.o.z, this.p.x, this.p.z);
        for (int i2 = 0; i2 < 7; ++i2) {
            double[] dArray = cyou.rimuru.shootingstardemo.mc1211.star.c.a(i2, this.B, this.z);
            this.C[i2] = new Vec3(this.p.x + dArray[0], this.p.y, this.p.z + dArray[1]);
        }
    }

    public static void a(cyou.rimuru.shootingstardemo.mc1211.star.l l2, cyou.rimuru.shootingstardemo.mc1211.net.e e2) {
        k k2 = new k(e2);
        cyou.rimuru.shootingstardemo.mc1211.client.fx.x.a("stars_", k2.u);
        cyou.rimuru.shootingstardemo.mc1211.client.fx.e.a(k2);
        if (k2.i && cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.c()) {
            cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.a(cyou.rimuru.shootingstardemo.mc1211.client.cinematic.f.a(k2.e(), k2).a(k2.u));
        } else if (!k2.i) {
            cyou.rimuru.shootingstardemo.mc1211.client.fx.a.a(l2, false, k2.l());
        }
    }

    @Override
    protected int d() {
        return 1340;
    }

    @Override
    public cyou.rimuru.shootingstardemo.mc1211.client.cinematic.i e() {
        return this.y;
    }

    @Override
    public double a() {
        return 600.0 * Math.max(this.z, 1.0);
    }

    public double b() {
        return this.z;
    }

    boolean c() {
        return cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.a(this.y) > 0.5f;
    }

    float a(float f2) {
        return cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.a(this.y, f2);
    }

    public Vec3 f() {
        return this.p;
    }

    public double g() {
        return this.B;
    }

    public Vec3 a(int n2) {
        return this.C[n2];
    }

    Vec3 a(Vec3 vec3) {
        Vec3 vec32 = this.C[0];
        for (Vec3 vec33 : this.C) {
            if (!(vec33.distanceToSqr(vec3) < vec32.distanceToSqr(vec3))) continue;
            vec32 = vec33;
        }
        return vec32;
    }

    private SoundInstance a(SoundEvent soundEvent, Vec3 vec3, float f2, float f3, boolean bl) {
        r r2;
        if (bl) {
            r2 = new r(this.u, soundEvent.getLocation(), SoundSource.PLAYERS, Math.min(1.0f, f2), f3, SoundInstance.createUnseededRandom(), false, 0, SoundInstance.Attenuation.NONE, 0.0, 0.0, 0.0, true);
            this.A.add((SoundInstance)r2);
        } else {
            r2 = new r(this.u, soundEvent.getLocation(), SoundSource.PLAYERS, f2 * 16.0f, f3, SoundInstance.createUnseededRandom(), false, 0, SoundInstance.Attenuation.LINEAR, vec3.x, vec3.y, vec3.z, false);
        }
        Minecraft.getInstance().getSoundManager().play((SoundInstance)r2);
        return r2;
    }

    private void i() {
        for (SoundInstance soundInstance : this.A) {
            Minecraft.getInstance().getSoundManager().stop(soundInstance);
        }
        this.A.clear();
    }

    @Override
    protected void h() {
        this.i();
    }

    private Vec3 a(ClientLevel clientLevel, double d2, double d3) {
        int n2 = clientLevel.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mth.floor((double)d2), Mth.floor((double)d3));
        return new Vec3(d2, n2 <= clientLevel.getMinBuildHeight() ? this.p.y : (double)n2, d3);
    }

    @Override
    protected void a(Minecraft minecraft, ClientLevel clientLevel, int n2) {
        int n3;
        int n4;
        boolean bl;
        Vec3 vec3;
        boolean bl2 = this.c();
        this.E |= bl2;
        if (this.E && !bl2 && !this.A.isEmpty() && (float)n2 < 272.0f) {
            this.i();
        }
        for (int i2 = 0; i2 < 7; ++i2) {
            if (!this.D[i2] && n2 <= cyou.rimuru.shootingstardemo.mc1211.star.i.c(i2)) {
                vec3 = this.a(clientLevel, this.C[i2].x, this.C[i2].z);
                if (vec3.y > (double)(clientLevel.getMinBuildHeight() + 1)) {
                    this.C[i2] = vec3;
                }
            }
            if (n2 != cyou.rimuru.shootingstardemo.mc1211.star.i.c(i2)) continue;
            this.D[i2] = true;
        }
        Vec3 vec32 = this.b(clientLevel);
        vec3 = this.a(cyou.rimuru.shootingstardemo.mc1211.client.fx.e.e());
        boolean bl3 = bl = cyou.rimuru.shootingstardemo.mc1211.client.fx.e.e().distanceTo(vec3) < 1400.0 * this.z;
        if (n2 == 0) {
            if (this.i) {
                cyou.rimuru.shootingstardemo.mc1211.client.fx.q.ab.values().forEach(cyou.rimuru.shootingstardemo.mc1211.client.render.a::a);
            }
            this.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.b, vec32, 0.8f, 1.0f, bl2);
        }
        if (n2 == 7) {
            this.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.c, vec32, 0.9f, 1.0f, bl2);
        }
        if (n2 == 9) {
            this.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.E, bl2 ? this.p : vec3, 1.0f, 1.0f, bl2);
            Vec3[] vec3Array = this.C;
            n4 = vec3Array.length;
            for (int i3 = 0; i3 < n4; ++i3) {
                Vec3 vec33 = vec3Array[i3];
                for (int i4 = 0; i4 < 20; ++i4) {
                    cyou.rimuru.shootingstardemo.mc1211.client.fx.k.a(clientLevel, (ParticleOptions)cyou.rimuru.shootingstardemo.mc1211.client.fx.k.a(i4 % 3 == 0 ? 14998783 : 12101887, 1.0f), vec33.add(this.d(1.0), 0.3, this.d(1.0)), 0.0, 0.1 + this.t.nextDouble() * 0.4, 0.0);
                }
            }
        }
        if (n2 == 24) {
            this.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.G, bl2 ? this.p : vec3, 1.0f, 1.0f, bl2);
        }
        if (n2 == 29 && !bl2) {
            this.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.F, vec3, 1.0f, 1.0f, false);
        }
        if (bl2) {
            if (n2 == 56) {
                this.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.H, this.p, 1.0f, 1.0f, true);
            }
            if (n2 == 130) {
                this.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.I, this.p, 1.0f, 1.0f, true);
            }
            if (n2 == 208) {
                this.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.J, this.p, 1.0f, 1.0f, true);
            }
            if (n2 == 236) {
                this.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.K, this.p, 1.0f, 1.0f, true);
            }
        }
        for (n3 = 0; n3 < 7; ++n3) {
            n4 = cyou.rimuru.shootingstardemo.mc1211.star.i.c(n3);
            float f2 = 1.15f - (float)n3 * 0.05f;
            if (n2 == n4 - 18) {
                this.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.L, bl2 ? this.p : this.C[n3], bl2 ? 0.6f : 0.6f * (float)Math.max(this.z, 1.0), f2, bl2);
            }
            if (n2 == n4) {
                this.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.M, bl2 ? this.p : this.C[n3], bl2 ? 1.0f : (float)Math.max(this.z, 1.0), f2, bl2);
                cyou.rimuru.shootingstardemo.mc1211.client.fx.e.a(this.C[n3], 1.1f, 900.0 * this.z);
                this.a(clientLevel, n3);
            }
            if (n2 <= n4 || n2 >= n4 + 40) continue;
            this.a(clientLevel, n3, n2 - n4);
        }
        if (n2 == 398) {
            this.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.N, bl2 ? this.p : vec3, 1.0f, 1.0f, bl2);
        }
        if (n2 >= 398 && n2 < 440) {
            this.b(clientLevel, n2);
        }
        if (n2 == 434) {
            this.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.O, bl2 ? this.p : vec3, 1.0f, 1.0f, bl2 || bl);
        }
        if (n2 == 440) {
            cyou.rimuru.shootingstardemo.mc1211.client.fx.e.a(this.p, 1.8f, 1600.0 * this.z);
            for (n3 = 0; n3 < 7; ++n3) {
                for (n4 = 0; n4 < 120; ++n4) {
                    double d2 = this.t.nextDouble() * Math.PI * 2.0;
                    double d3 = 1.0 + this.t.nextDouble() * 5.0;
                    cyou.rimuru.shootingstardemo.mc1211.client.fx.k.a(clientLevel, (ParticleOptions)cyou.rimuru.shootingstardemo.mc1211.client.fx.k.a(n4 % 2 == 0 ? 14998783 : 12101887, 2.0f + this.t.nextFloat() * 2.0f), this.C[n3].add(this.d(2.0 * this.z), this.t.nextDouble() * 10.0 * this.z, this.d(2.0 * this.z)), Math.cos(d2) * 1.4, d3, Math.sin(d2) * 1.4);
                }
            }
        }
        if (n2 == 470) {
            this.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.P, bl2 ? this.p : vec3, 0.9f, 1.0f, bl2);
        }
        if (n2 > 440 && n2 < 1240 && n2 % 3 == 0) {
            for (n3 = 0; n3 < 7; ++n3) {
                Vec3 vec34 = this.C[n3].add(0.0, -cyou.rimuru.shootingstardemo.mc1211.star.c.b(n3, this.z) + 4.0 * this.z, 0.0);
                cyou.rimuru.shootingstardemo.mc1211.client.fx.k.a(clientLevel, (ParticleOptions)cyou.rimuru.shootingstardemo.mc1211.client.fx.k.a(this.t.nextBoolean() ? 12101887 : 14998783, 0.7f), vec34.add(this.d(3.0 * this.z), this.t.nextDouble() * 12.0 * this.z, this.d(3.0 * this.z)), this.d(0.03), 0.06 + this.t.nextDouble() * 0.1, this.d(0.03));
            }
        }
    }

    private void a(ClientLevel clientLevel, int n2) {
        Vec3 vec3 = this.C[n2];
        double d2 = cyou.rimuru.shootingstardemo.mc1211.star.c.a(n2, this.z);
        for (int i2 = 0; i2 < 360; ++i2) {
            double d3 = this.t.nextDouble() * Math.PI * 2.0;
            double d4 = 0.8 + this.t.nextDouble() * 3.5;
            double d5 = 0.4 + this.t.nextDouble() * 2.2;
            Vec3 vec32 = vec3.add(Math.cos(d3) * d2 * 0.4 * this.t.nextDouble(), 1.0 + this.t.nextDouble() * 4.0, Math.sin(d3) * d2 * 0.4 * this.t.nextDouble());
            BlockState blockState = clientLevel.getBlockState(BlockPos.containing((double)(vec32.x + Math.cos(d3) * d2), (double)(vec3.y - 1.0), (double)(vec32.z + Math.sin(d3) * d2)));
            if (i2 % 3 == 0 && !blockState.isAir()) {
                cyou.rimuru.shootingstardemo.mc1211.client.fx.k.a(clientLevel, (ParticleOptions)new BlockParticleOption(ParticleTypes.BLOCK, blockState), vec32, Math.cos(d3) * d5, d4, Math.sin(d3) * d5);
                continue;
            }
            int n3 = i2 % 4 == 0 ? 0xFFFFFF : (i2 % 4 == 1 ? 14998783 : (i2 % 4 == 2 ? 12101887 : 6969994));
            cyou.rimuru.shootingstardemo.mc1211.client.fx.k.a(clientLevel, (ParticleOptions)cyou.rimuru.shootingstardemo.mc1211.client.fx.k.a(n3, 1.6f + this.t.nextFloat() * 2.4f), vec32, Math.cos(d3) * d5 * 0.7, d4 * 0.8, Math.sin(d3) * d5 * 0.7);
        }
    }

    private void a(ClientLevel clientLevel, int n2, int n3) {
        float f2 = 1.0f - (float)n3 / 40.0f;
        Vec3 vec3 = this.C[n2];
        double d2 = cyou.rimuru.shootingstardemo.mc1211.star.c.a(n2, this.z);
        for (int i2 = 0; i2 < (int)(24.0f * f2); ++i2) {
            double d3 = this.t.nextDouble() * Math.PI * 2.0;
            double d4 = d2 * (0.9 + this.t.nextDouble() * 0.4) + (double)n3 * 3.0 * this.z;
            Vec3 vec32 = this.a(clientLevel, vec3.x + Math.cos(d3) * d4, vec3.z + Math.sin(d3) * d4);
            cyou.rimuru.shootingstardemo.mc1211.client.fx.k.a(clientLevel, (ParticleOptions)cyou.rimuru.shootingstardemo.mc1211.client.fx.k.a(this.t.nextBoolean() ? 11049160 : 5918832, 2.4f), vec32.add(0.0, 0.5, 0.0), Math.cos(d3) * 0.6, 0.1 + this.t.nextDouble() * 0.25, Math.sin(d3) * 0.6);
        }
    }

    private void b(ClientLevel clientLevel, int n2) {
        float f2 = (float)(n2 - 398) / 20.0f;
        for (int i2 = 0; i2 < cyou.rimuru.shootingstardemo.mc1211.star.c.i.length; ++i2) {
            float f3 = f2 - (float)i2 * 0.25f;
            if (f3 < 0.0f || f3 > 0.5f) continue;
            Vec3 vec3 = this.C[cyou.rimuru.shootingstardemo.mc1211.star.c.i[i2][0]];
            Vec3 vec32 = this.C[cyou.rimuru.shootingstardemo.mc1211.star.c.i[i2][1]];
            Vec3 vec33 = vec3.lerp(vec32, (double)Math.min(f3 / 0.4f, 1.0f));
            Vec3 vec34 = this.a(clientLevel, vec33.x, vec33.z);
            for (int i3 = 0; i3 < 24; ++i3) {
                cyou.rimuru.shootingstardemo.mc1211.client.fx.k.a(clientLevel, (ParticleOptions)cyou.rimuru.shootingstardemo.mc1211.client.fx.k.a(i3 % 2 == 0 ? 0xFFFFFF : 14998783, 1.0f + this.t.nextFloat()), vec34.add(this.d(2.0 * this.z), 0.5, this.d(2.0 * this.z)), this.d(0.4), 0.3 + this.t.nextDouble() * 1.2, this.d(0.4));
            }
        }
    }

    @Override
    public void a(d d2, List<cyou.rimuru.shootingstardemo.mc1211.client.render.e> list) {
        float f2;
        float f3 = this.c(d2);
        float f4 = this.a(d2.b());
        float f5 = cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.a(this.y);
        boolean bl = !Float.isNaN(f4) && f5 > 0.001f;
        float f6 = f2 = bl ? cyou.rimuru.shootingstardemo.mc1211.client.fx.m.a(f4) * f5 : 0.0f;
        if (f2 < 0.999f) {
            this.a(d2, list, f3);
        }
        if (f2 > 0.001f) {
            float f7 = this.r;
            double d3 = this.B;
            list.add(new cyou.rimuru.shootingstardemo.mc1211.client.render.e("stars_space", j2 -> {
                cyou.rimuru.shootingstardemo.mc1211.client.fx.q.ab.forEach(j2::a);
                cyou.rimuru.shootingstardemo.mc1211.client.fx.m.a(f4, f2, f7, d3, cyou.rimuru.shootingstardemo.mc1211.client.fx.k.a(j2));
            }));
        }
    }

    private static BiConsumer<String, float[]> a(j j2) {
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

    static float a(float f2, int n2) {
        int n3 = cyou.rimuru.shootingstardemo.mc1211.star.i.c(n2);
        return f2 >= (float)n3 ? 0.0f : Mth.clamp((float)((f2 - (float)(n3 - 16)) / 16.0f), (float)0.0f, (float)1.0f);
    }

    static float b(float f2, int n2) {
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.k.b((f2 - (float)(24 + n2 * 4)) / 4.0f);
    }

    private void a(d d2, List<cyou.rimuru.shootingstardemo.mc1211.client.render.e> list, float f2) {
        if (f2 < 8.0f || f2 > 1340.0f) {
            return;
        }
        float[] fArray = new float[64];
        for (int i2 = 0; i2 < 7; ++i2) {
            Vec3 vec3 = d2.a(this.C[i2]);
            int n2 = cyou.rimuru.shootingstardemo.mc1211.star.i.c(i2);
            fArray[i2 * 4] = (float)vec3.x;
            fArray[i2 * 4 + 1] = (float)vec3.y;
            fArray[i2 * 4 + 2] = (float)vec3.z;
            fArray[i2 * 4 + 3] = f2 >= (float)n2 ? (f2 - (float)n2) / 20.0f : -1.0f;
            fArray[(7 + i2) * 4] = (float)cyou.rimuru.shootingstardemo.mc1211.star.c.a(i2, this.z);
            fArray[(7 + i2) * 4 + 1] = (float)cyou.rimuru.shootingstardemo.mc1211.star.c.b(i2, this.z);
            fArray[(7 + i2) * 4 + 2] = cyou.rimuru.shootingstardemo.mc1211.client.fx.k.a(f2, i2);
            fArray[(7 + i2) * 4 + 3] = cyou.rimuru.shootingstardemo.mc1211.client.fx.k.b(f2, i2) * (f2 > 1040.0f ? 0.0f : 1.0f);
        }
        float f3 = cyou.rimuru.shootingstardemo.mc1211.client.fx.k.a(f2, 9.0f, 10.5f, 260.0f, 290.0f);
        float f4 = cyou.rimuru.shootingstardemo.mc1211.client.fx.k.a(f2, 18.0f, 38.0f, 740.0f, 940.0f);
        float f5 = f2 >= 398.0f ? (f2 - 398.0f) / 20.0f : -1.0f;
        float f6 = f2 >= 440.0f ? (f2 - 440.0f) / 20.0f : -1.0f;
        float f7 = cyou.rimuru.shootingstardemo.mc1211.client.fx.k.b((f2 - 9.0f) / 14.0f);
        float f8 = f2 < 440.0f ? 0.0f : cyou.rimuru.shootingstardemo.mc1211.client.fx.k.a(f2, 460.0f, 500.0f, 740.0f, 940.0f);
        Vec3 vec3 = d2.a(this.p);
        float f9 = f2 / 20.0f;
        float f10 = this.r;
        list.add(new cyou.rimuru.shootingstardemo.mc1211.client.render.e("stars_world", j2 -> {
            j2.a("Pts", fArray);
            j2.a("Target", vec3);
            j2.a("P0", f9, f10, f3, f4);
            j2.a("P1", f5, f6, (float)cyou.rimuru.shootingstardemo.mc1211.star.c.b(this.z), 0.0f);
            j2.a("P2", f7, (float)cyou.rimuru.shootingstardemo.mc1211.client.fx.k.a((double)f2), (float)vec3.y, (float)this.z);
            j2.a("P3", 0.0f, 0.0f, f8, 0.0f);
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

    static float b(float f2) {
        f2 = Mth.clamp((float)f2, (float)0.0f, (float)1.0f);
        return f2 * f2 * (3.0f - 2.0f * f2);
    }

    @Override
    public void a(d d2, f f2) {
        float f3;
        float f4;
        boolean bl;
        float f5 = this.c(d2);
        float f6 = this.a(d2.b());
        float f7 = cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.a(this.y);
        boolean bl2 = bl = !Float.isNaN(f6) && f7 > 0.001f;
        if (bl) {
            this.b(f2, f6, f7);
        }
        if (bl && cyou.rimuru.shootingstardemo.mc1211.client.fx.m.a(f6) > 0.5f) {
            return;
        }
        Vec3 vec3 = d2.c();
        float f8 = cyou.rimuru.shootingstardemo.mc1211.client.fx.k.a(f5, 24.0f, 44.0f, 640.0f, 840.0f);
        f2.b(656928, f8 * 0.3f);
        for (int i2 = 0; i2 < 7; ++i2) {
            float f9;
            f4 = bl ? 1.0f : (float)Mth.clamp((double)(1.0 - vec3.distanceTo(this.C[i2]) / (1200.0 * this.z)), (double)0.2, (double)1.0);
            f3 = cyou.rimuru.shootingstardemo.mc1211.client.fx.k.a(f5, i2);
            if (f3 > 0.0f) {
                f2.b = Math.max(f2.b, f3 * f3 * 0.5f * f4);
                f2.d = Math.max(f2.d, f3 * f3 * f3 * 0.35f * f4);
            }
            if (!((f9 = (f5 - (float)cyou.rimuru.shootingstardemo.mc1211.star.i.c(i2)) / 20.0f) >= 0.0f) || !(f9 < 2.5f)) continue;
            if (f9 < 0.05f) {
                f2.a(0xFFFFFF, 4.5f * f4);
            } else if (f9 - 0.05f < (float)w.length * 0.083333336f) {
                int n2;
                int[] nArray = new int[w.length];
                for (n2 = 0; n2 < nArray.length; ++n2) {
                    nArray[n2] = w[(n2 + i2) % w.length];
                }
                cyou.rimuru.shootingstardemo.mc1211.client.fx.k.a(f2, f9 - 0.05f, this.C[i2].add(0.0, 8.0, 0.0), f4, 0.083333336f, nArray);
                n2 = (int)((f9 - 0.05f) / 0.083333336f);
                f2.y = f2.y > 0.0f ? Math.min(f2.y, 12.0f) : 12.0f;
                f2.d = Math.max(f2.d, (n2 % 2 == 0 ? 0.9f : 0.25f) * f4);
            }
            f2.a(14207231, (float)Math.exp((double)(-f9) * 5.0) * 1.0f * f4);
            f2.a = Math.max(f2.a, cyou.rimuru.shootingstardemo.mc1211.client.fx.k.a(f9, 0.0f, 0.02f, 0.5f, 2.0f) * 1.1f * f4);
            f2.b = Math.max(f2.b, cyou.rimuru.shootingstardemo.mc1211.client.fx.k.a(f9, 0.0f, 0.02f, 0.4f, 1.5f) * 1.1f * f4);
            f2.m = Math.max(f2.m, 1.1f * cyou.rimuru.shootingstardemo.mc1211.client.fx.k.a(f9, 0.0f, 0.1f, 1.0f, 2.5f));
        }
        float f10 = cyou.rimuru.shootingstardemo.mc1211.client.fx.k.a(f5, 398.0f, 402.0f, 436.0f, 440.0f);
        f2.a = Math.max(f2.a, f10 * 0.25f);
        f2.m = Math.max(f2.m, f10 * 0.55f);
        f4 = cyou.rimuru.shootingstardemo.mc1211.client.fx.k.a(f5, 432.0f, 435.0f, 439.0f, 440.0f);
        cyou.rimuru.shootingstardemo.mc1211.client.fx.k.a(f2, f4 * 0.6f, 0.0f);
        f3 = (f5 - 440.0f) / 20.0f;
        if (f3 >= 0.0f && f3 < 8.0f) {
            if (f3 < 0.06f) {
                f2.a(0xFFFFFF, 6.0f);
            } else if (f3 - 0.06f < (float)x.length * 0.083333336f) {
                cyou.rimuru.shootingstardemo.mc1211.client.fx.k.a(f2, f3 - 0.06f, this.p.add(0.0, 20.0, 0.0), 1.0f, 0.083333336f, x);
                int n3 = (int)((f3 - 0.06f) / 0.083333336f);
                f2.y = f2.y > 0.0f ? Math.min(f2.y, 12.0f) : 12.0f;
                f2.d = Math.max(f2.d, n3 % 2 == 0 ? 1.0f : 0.3f);
                f2.b = Math.max(f2.b, n3 % 2 == 0 ? 0.4f : 1.6f);
            }
            f2.a(13154559, (float)Math.exp((double)(-f3) * 2.5) * 1.4f);
            f2.a = Math.max(f2.a, cyou.rimuru.shootingstardemo.mc1211.client.fx.k.a(f3, 0.0f, 0.02f, 1.0f, 4.0f) * 1.8f);
            f2.m = Math.max(f2.m, 1.5f * cyou.rimuru.shootingstardemo.mc1211.client.fx.k.a(f3, 0.0f, 0.1f, 3.0f, 8.0f));
            f2.n = Math.max(f2.n, 1.0f + 1.2f * cyou.rimuru.shootingstardemo.mc1211.client.fx.k.a(f3, 0.0f, 0.1f, 2.0f, 6.0f));
            f2.v = Math.max(f2.v, 0.2f * cyou.rimuru.shootingstardemo.mc1211.client.fx.k.a(f3, 0.5f, 1.0f, 3.0f, 6.0f));
        }
    }

    private void b(f f2, float f3, float f4) {
        float f5;
        float f6 = cyou.rimuru.shootingstardemo.mc1211.client.fx.m.a(f3) * f4;
        f2.x = Math.max(f2.x, f6);
        if (f6 > 0.001f) {
            f2.m = Math.max(f2.m, 0.6f);
        }
        for (float f7 : new float[]{96.0f, 130.0f}) {
            f5 = cyou.rimuru.shootingstardemo.mc1211.client.fx.k.a(f3, f7 - 1.0f, f7, f7 + 1.0f, f7 + 5.0f);
            f2.b = Math.max(f2.b, f5 * 0.9f);
            f2.a(14208255, f5 * 0.25f);
        }
        for (int i2 = 0; i2 < 7; ++i2) {
            float f7;
            float f8 = 150.0f + (float)i2 * 6.0f;
            float f9 = cyou.rimuru.shootingstardemo.mc1211.client.fx.k.a(f3, f8, f8 + 0.5f, f8 + 1.0f, f8 + 4.0f);
            f2.a = Math.max(f2.a, f9 * 0.35f);
            f2.b = Math.max(f2.b, f9 * 0.6f);
            f7 = 208.0f + (float)i2 * 4.0f;
            f5 = cyou.rimuru.shootingstardemo.mc1211.client.fx.k.a(f3, f7, f7 + 0.3f, f7 + 0.6f, f7 + 3.0f);
            f2.a(15788799, f5 * (0.6f + 0.2f * (float)i2));
            f2.a = Math.max(f2.a, f5 * (0.4f + 0.1f * (float)i2));
            f2.d = Math.max(f2.d, f5 * 0.5f);
        }
        float f10 = cyou.rimuru.shootingstardemo.mc1211.client.fx.k.a(f3, 236.0f, 238.0f, 270.0f, 272.0f);
        f2.d = Math.max(f2.d, f10 * 0.4f);
        float f11 = cyou.rimuru.shootingstardemo.mc1211.client.fx.k.a(f3, 272.0f, 272.2f, 272.6f, 275.0f);
        f2.a(0xFFFFFF, f11 * 1.2f);
    }

    @Override
    b c(float f2) {
        float f3 = this.d(f2);
        float[][] fArrayArray = new float[][]{{0.0f, -0.35f}, {4.0f, -1.4f}, {9.0f, -1.4f}, {16.0f, -2.7f}, {60.0f, -2.7f}, {80.0f, -1.3f}, {500.0f, -1.3f}, {530.0f, -0.35f}};
        float f4 = fArrayArray[fArrayArray.length - 1][1];
        for (int i2 = 0; i2 < fArrayArray.length - 1; ++i2) {
            if (!(f3 >= fArrayArray[i2][0]) || !(f3 < fArrayArray[i2 + 1][0])) continue;
            f4 = Mth.lerp((float)cyou.rimuru.shootingstardemo.mc1211.client.fx.k.b((f3 - fArrayArray[i2][0]) / (fArrayArray[i2 + 1][0] - fArrayArray[i2][0])), (float)fArrayArray[i2][1], (float)fArrayArray[i2 + 1][1]);
            break;
        }
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.b.a(f4, -0.12f, 0.0f).a(cyou.rimuru.shootingstardemo.mc1211.client.fx.k.a(f3, 0.0f, 3.0f, 526.0f, 534.0f));
    }

    @Override
    boolean b(Vec3 vec3) {
        return this.v >= 434 && this.v < 440 && vec3.distanceTo(this.a(vec3)) < 1400.0 * this.z;
    }

    @Override
    public void a(GuiGraphics guiGraphics, d d2) {
        float f2 = this.c(d2);
        Minecraft minecraft = d2.a();
        if (minecraft.player == null || f2 < 9.0f || f2 >= 480.0f) {
            return;
        }
        Vec3 vec3 = minecraft.player.position();
        if (vec3.distanceTo(this.a(vec3)) > 300.0 * this.z) {
            return;
        }
        Font font = minecraft.font;
        int n2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.i.c(guiGraphics);
        if (f2 < 290.0f) {
            float f3 = (float)cyou.rimuru.shootingstardemo.mc1211.client.fx.k.a((double)f2) % 1.0f;
            int n3 = (int)(255.0f * (0.55f + 0.45f * (1.0f - f3)));
            cyou.rimuru.shootingstardemo.mc1211.client.fx.p.d(guiGraphics, font, "STELLAR STRIKE INBOUND \u00b7 7 NODES", n2 / 2, 26, n3 << 24 | 0xB8A8FF, 1.5f);
            cyou.rimuru.shootingstardemo.mc1211.client.fx.p.c(guiGraphics, font, String.format(Locale.ROOT, "T-%05.2f", Float.valueOf((290.0f - f2) / 20.0f / this.u)), n2 / 2, 44, -1, 1.25f);
            return;
        }
        int n4 = 0;
        for (int i2 = 0; i2 < 7; ++i2) {
            n4 += f2 >= (float)cyou.rimuru.shootingstardemo.mc1211.star.i.c(i2) ? 1 : 0;
        }
        String string = n4 < 7 ? String.format(Locale.ROOT, "STAR %d / 7 \u00b7 %s", n4, cyou.rimuru.shootingstardemo.mc1211.star.c.b[Math.max(n4 - 1, 0)]) : (f2 < 440.0f ? "LINKING" : "CONSTELLATION BURNED");
        cyou.rimuru.shootingstardemo.mc1211.client.fx.p.d(guiGraphics, font, string, n2 / 2, 26, -4675329, 1.5f);
    }

    public void b(GuiGraphics guiGraphics, d d2) {
        float f2 = this.a(d2.b());
        if (Float.isNaN(f2)) {
            return;
        }
        cyou.rimuru.shootingstardemo.mc1211.client.fx.l.a(guiGraphics, d2, this, f2, cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.a(this.y));
    }
}

