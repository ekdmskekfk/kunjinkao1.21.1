/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.fx;

import cyou.rimuru.shootingstardemo.mc1211.client.fx.a;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.b;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.c;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.d;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.e;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.g;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.h;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.i;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.n;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.p;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.r;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.x;
import cyou.rimuru.shootingstardemo.mc1211.client.render.j;
import cyou.rimuru.shootingstardemo.mc1211.star.l;
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

public final class f
extends n {
    static final int a = 7;
    static final int b = 9;
    static final int c = 290;
    static final int d = 322;
    static final int e = 29;
    static final int f = 22;
    static final int g = 372;
    private static final int w = 16734751;
    private static final int x = 16752736;
    public final double h;
    public final float i;
    private final double y;
    private final cyou.rimuru.shootingstardemo.mc1211.client.cinematic.i z;
    public final boolean j;
    private final List<SoundInstance> A = new ArrayList<SoundInstance>();
    private boolean B;
    private int C = -1;
    static final int k = 19;
    private static final float D = 0.083333336f;
    private static final int[] E = new int[]{17, 20, 15, 18, 19, 20, 16, 0, 18, 17, 20, 6, 15, 19};
    private static final int[] F = new int[]{20, 16, 18, 20, 15, 19, 17, 20, 1, 18, 15};
    static final int l = 5;

    f(cyou.rimuru.shootingstardemo.mc1211.net.e e2) {
        super(e2);
        Minecraft minecraft = Minecraft.getInstance();
        this.h = cyou.rimuru.shootingstardemo.mc1211.star.e.a(cyou.rimuru.shootingstardemo.mc1211.star.l.b.j().a(e2.n()));
        this.y = cyou.rimuru.shootingstardemo.mc1211.star.e.b(this.h);
        this.i = (float)(400.0 * this.h);
        this.z = super.e();
        this.j = minecraft.player != null && minecraft.player.getId() == e2.c();
    }

    @Override
    public double a() {
        return Math.max(super.a(), (double)this.i * 2.5 + 200.0);
    }

    double b() {
        return 6.5 * this.y;
    }

    double c() {
        return 88.0 * this.h;
    }

    double a(double d2) {
        return cyou.rimuru.shootingstardemo.mc1211.star.e.a(d2, (double)this.i);
    }

    public static void a(l l2, cyou.rimuru.shootingstardemo.mc1211.net.e e2) {
        f f2 = new f(e2);
        cyou.rimuru.shootingstardemo.mc1211.client.fx.x.a("gungnir_", f2.u);
        cyou.rimuru.shootingstardemo.mc1211.client.fx.e.a(f2);
        if (f2.j && cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.c()) {
            cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.a(cyou.rimuru.shootingstardemo.mc1211.client.cinematic.e.a(f2.e(), f2).a(f2.u));
        } else if (!f2.j) {
            cyou.rimuru.shootingstardemo.mc1211.client.fx.a.a(l2, false, f2.l());
        }
    }

    @Override
    protected int d() {
        return 1222;
    }

    @Override
    public cyou.rimuru.shootingstardemo.mc1211.client.cinematic.i e() {
        return this.z;
    }

    boolean f() {
        return cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.a(this.z) > 0.5f;
    }

    float a(float f2) {
        return cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.a(this.z, f2);
    }

    public Vec3 g() {
        return this.p;
    }

    Vec3 a(Vec3 vec3) {
        Vec3 vec32 = new Vec3(vec3.x - this.p.x, 0.0, vec3.z - this.p.z);
        double d2 = vec32.length();
        if (d2 < 0.001) {
            return this.p;
        }
        return this.p.add(vec32.scale(Math.min(d2, (double)this.i) / d2)).add(0.0, Math.min(Math.max(vec3.y - this.p.y, 0.0), 80.0), 0.0);
    }

    double b(double d2) {
        double d3 = (double)this.i * 1.3;
        if (d2 <= d3) {
            return cyou.rimuru.shootingstardemo.mc1211.star.e.b(d2, this.i);
        }
        return cyou.rimuru.shootingstardemo.mc1211.star.e.b(d3, this.i) + (d2 - d3) / 17.0;
    }

    private SoundInstance a(SoundEvent soundEvent, Vec3 vec3, float f2, boolean bl) {
        r r2;
        if (bl) {
            r2 = new r(this.u, soundEvent.getLocation(), SoundSource.PLAYERS, Math.min(1.0f, f2), 1.0f, SoundInstance.createUnseededRandom(), false, 0, SoundInstance.Attenuation.NONE, 0.0, 0.0, 0.0, true);
            this.A.add((SoundInstance)r2);
        } else {
            r2 = new r(this.u, soundEvent.getLocation(), SoundSource.PLAYERS, f2 * 16.0f, 1.0f, SoundInstance.createUnseededRandom(), false, 0, SoundInstance.Attenuation.LINEAR, vec3.x, vec3.y, vec3.z, false);
        }
        Minecraft.getInstance().getSoundManager().play((SoundInstance)r2);
        return r2;
    }

    private void a(SoundEvent soundEvent, float f2) {
        this.a(soundEvent, f2, 1.0f);
    }

    private void a(SoundEvent soundEvent, float f2, float f3) {
        r r2 = new r(this.u, soundEvent.getLocation(), SoundSource.PLAYERS, Math.min(1.0f, f2), f3, SoundInstance.createUnseededRandom(), false, 0, SoundInstance.Attenuation.NONE, 0.0, 0.0, 0.0, true);
        Minecraft.getInstance().getSoundManager().play((SoundInstance)r2);
    }

    private void s() {
        for (SoundInstance soundInstance : this.A) {
            Minecraft.getInstance().getSoundManager().stop(soundInstance);
        }
        this.A.clear();
    }

    @Override
    protected void h() {
        this.s();
    }

    private Vec3 a(ClientLevel clientLevel, double d2, double d3) {
        int n2 = clientLevel.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mth.floor((double)d2), Mth.floor((double)d3));
        return new Vec3(d2, n2 <= clientLevel.getMinBuildHeight() ? this.p.y : (double)n2, d3);
    }

    @Override
    protected void a(Minecraft minecraft, ClientLevel clientLevel, int n2) {
        double d2;
        int n3;
        boolean bl;
        boolean bl2 = this.f();
        this.B |= bl2;
        if (this.B && !bl2 && !this.A.isEmpty() && n2 < 290) {
            this.s();
        }
        Vec3 vec3 = this.b(clientLevel);
        Vec3 vec32 = cyou.rimuru.shootingstardemo.mc1211.client.fx.e.e();
        Vec3 vec33 = this.a(vec32);
        double d3 = Math.hypot(vec32.x - this.p.x, vec32.z - this.p.z);
        boolean bl3 = bl = d3 < 2000.0;
        if (n2 == 0) {
            if (this.j) {
                cyou.rimuru.shootingstardemo.mc1211.client.fx.h.U.values().forEach(cyou.rimuru.shootingstardemo.mc1211.client.render.a::a);
            }
            this.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.b, vec3, 0.8f, bl2);
        }
        if (n2 == 7) {
            this.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.c, vec3, 0.9f, bl2);
        }
        if (n2 == 9) {
            this.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.r, bl2 ? this.p : vec33, 1.0f, bl2);
            for (n3 = 0; n3 < 50; ++n3) {
                cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(clientLevel, (ParticleOptions)cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(n3 % 3 == 0 ? 16765616 : 16734751, 1.0f), this.p.add(0.0, 0.2, 0.0), this.d(0.3), 0.1 + this.t.nextDouble() * 0.5, this.d(0.3));
            }
        }
        if (n2 == 29 && !bl2) {
            this.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.s, vec33, 1.0f, false);
        }
        if (bl2) {
            if (n2 == 44) {
                this.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.t, this.p, 1.0f, true);
            }
            if (n2 == 124) {
                this.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.u, this.p, 1.0f, true);
            }
            if (n2 == 146) {
                this.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.v, this.p, 1.0f, true);
            }
            if (n2 == 168) {
                this.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.w, this.p, 1.0f, true);
            }
            if (n2 == 247) {
                this.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.x, this.p, 1.0f, true);
            }
            if (n2 == 256) {
                this.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.y, this.p, 1.0f, true);
            }
        }
        if (n2 >= 9 && n2 < 322) {
            for (n3 = 0; n3 < 3; ++n3) {
                cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(clientLevel, (ParticleOptions)cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(n3 == 0 ? 16769736 : 16734751, 0.6f + this.t.nextFloat() * 0.5f), this.p.add(this.d(0.25), 0.1, this.d(0.25)), this.d(0.08), 0.05 + this.t.nextDouble() * 0.25, this.d(0.08));
            }
        }
        if (n2 == 290) {
            this.s();
            if (bl2 || bl) {
                this.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.z, bl2 ? 1.0f : (float)Mth.clamp((double)(1.2 - d3 / 2000.0), (double)0.3, (double)1.0));
            }
        }
        if (n2 == 320) {
            cyou.rimuru.shootingstardemo.mc1211.client.fx.c.a(this.p, clientLevel.getGameTime(), 2L, (double)this.i * 1.6, this.r);
        }
        if (n2 > 282 && n2 < 322) {
            float f2 = (float)(n2 - 322 + 40) / 40.0f;
            for (int i2 = 0; i2 < (int)(26.0f * f2 * f2); ++i2) {
                double d4 = this.t.nextDouble() * Math.PI * 2.0;
                double d5 = Math.sqrt(this.t.nextDouble()) * (double)this.i;
                Vec3 vec34 = this.a(clientLevel, this.p.x + Math.cos(d4) * d5, this.p.z + Math.sin(d4) * d5);
                cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(clientLevel, (ParticleOptions)cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(12101792, 1.4f), vec34.add(0.0, 0.4, 0.0), 0.0, 0.2 + this.t.nextDouble() * 0.4, 0.0);
            }
        }
        if (n2 == 322) {
            if (bl2 || bl) {
                this.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.A, bl2 ? 1.0f : 0.8f);
            }
            for (int i3 = 0; i3 < 300; ++i3) {
                double d6 = this.t.nextDouble() * Math.PI * 2.0;
                double d7 = 1.0 + this.t.nextDouble() * 4.0;
                double d8 = 0.5 + this.t.nextDouble() * 2.0;
                cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(clientLevel, (ParticleOptions)cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(i3 % 3 == 0 ? 0xFFF0E0 : (i3 % 3 == 1 ? 16752736 : 16734751), 2.0f + this.t.nextFloat() * 2.0f), this.p.add(this.d(3.0), this.t.nextDouble() * 8.0, this.d(3.0)), Math.cos(d6) * d8, d7, Math.sin(d6) * d8);
            }
        }
        if (n2 >= 322 && n2 < 378) {
            this.a(clientLevel, n2);
        }
        double d9 = d2 = bl2 ? 341.0 : this.b(d3);
        if (this.C < 0 && n2 >= 322 && (double)n2 >= d2 && d3 < 4000.0) {
            this.C = n2;
            float f3 = (float)Mth.clamp((double)(1.3 - d3 / 1600.0), (double)0.35, (double)1.0);
            this.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.B, bl2 ? 1.0f : f3);
            cyou.rimuru.shootingstardemo.mc1211.client.fx.e.a(vec32, 2.4f * f3, 4000.0);
        }
        if (this.C >= 0 && n2 == this.C + 2) {
            this.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.C, bl2 ? 1.0f : (float)Mth.clamp((double)(1.2 - d3 / 900.0), (double)0.2, (double)1.0));
        }
        if (n2 == 372) {
            Vec3 vec35 = new Vec3(this.p.x, Math.min(Math.max(vec32.y, this.p.y), this.p.y + 300.0), this.p.z);
            this.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.D, bl2 ? this.p : vec35, bl2 ? 1.0f : 0.9f, bl2);
        }
        if (n2 > 322 && n2 < 1122) {
            this.b(clientLevel, n2);
        }
    }

    private void a(ClientLevel clientLevel, int n2) {
        double d2 = this.a((double)n2);
        if (d2 > (double)this.i * 1.4) {
            return;
        }
        Vec3 vec3 = cyou.rimuru.shootingstardemo.mc1211.client.fx.e.e();
        double d3 = Math.atan2(vec3.z - this.p.z, vec3.x - this.p.x);
        int n3 = n2 == 322 ? 60 : 90;
        for (int i2 = 0; i2 < n3; ++i2) {
            double d4 = i2 % 3 == 0 ? this.t.nextDouble() * Math.PI * 2.0 : d3 + this.d(0.5);
            double d5 = d2 * (0.96 + this.t.nextDouble() * 0.06);
            Vec3 vec32 = this.a(clientLevel, this.p.x + Math.cos(d4) * d5, this.p.z + Math.sin(d4) * d5);
            BlockState blockState = clientLevel.getBlockState(BlockPos.containing((double)vec32.x, (double)(vec32.y - 0.5), (double)vec32.z));
            double d6 = 0.8 + this.t.nextDouble() * 1.4;
            if (!blockState.isAir() && i2 % 2 == 0) {
                cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(clientLevel, (ParticleOptions)new BlockParticleOption(ParticleTypes.BLOCK, blockState), vec32.add(0.0, 0.4, 0.0), Math.cos(d4) * d6, 0.3 + this.t.nextDouble() * 0.9, Math.sin(d4) * d6);
            }
            cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(clientLevel, (ParticleOptions)cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(this.t.nextFloat() < 0.25f ? 14207160 : 9076340, 2.2f + this.t.nextFloat() * 1.6f), vec32.add(0.0, this.t.nextDouble() * 4.0, 0.0), Math.cos(d4) * d6 * 0.7, 0.05 + this.t.nextDouble() * 0.25, Math.sin(d4) * d6 * 0.7);
        }
    }

    private void b(ClientLevel clientLevel, int n2) {
        Vec3 vec3;
        double d2;
        double d3;
        int n3;
        float f2 = 1.0f - (float)(n2 - 322) / 800.0f;
        if (n2 < 442) {
            for (n3 = 0; n3 < (int)(60.0f * f2); ++n3) {
                d3 = this.t.nextDouble() * Math.PI * 2.0;
                d2 = Math.sqrt(this.t.nextDouble()) * (double)this.i * 0.6;
                vec3 = new Vec3(this.p.x + Math.cos(d3) * d2, this.p.y + 40.0 + this.t.nextDouble() * 120.0, this.p.z + Math.sin(d3) * d2);
                cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(clientLevel, (ParticleOptions)cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(this.t.nextBoolean() ? 16734751 : 16752736, 0.6f + this.t.nextFloat() * 0.5f), vec3, this.d(0.05), -0.2 - this.t.nextDouble() * 0.3, this.d(0.05));
            }
        }
        for (n3 = 0; n3 < (int)(10.0f * f2) + 1; ++n3) {
            d3 = this.t.nextDouble() * Math.PI * 2.0;
            d2 = this.b() + 2.0 + this.t.nextDouble() * Math.max(1.0, this.c() - this.b() - 2.0);
            vec3 = this.a(clientLevel, this.p.x + Math.cos(d3) * d2, this.p.z + Math.sin(d3) * d2);
            cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(clientLevel, (ParticleOptions)cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(5918796, 2.6f + this.t.nextFloat() * 1.4f), vec3.add(0.0, 0.5, 0.0), this.d(0.02), 0.08 + this.t.nextDouble() * 0.12, this.d(0.02));
        }
        if (n2 % 3 == 0) {
            double d4 = this.p.y + this.t.nextDouble() * 260.0;
            double d5 = (double)this.t.nextInt(4) * Math.PI * 0.5 + 0.7853981633974483;
            double d6 = this.b() - 0.2;
            Vec3 vec32 = new Vec3((double)Mth.floor((double)this.p.x) + 0.5 + Math.cos(d5) * d6, d4, (double)Mth.floor((double)this.p.z) + 0.5 + Math.sin(d5) * d6);
            for (int i2 = 0; i2 < 6; ++i2) {
                cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(clientLevel, (ParticleOptions)cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(i2 % 2 == 0 ? 16752736 : 16734751, 0.5f), vec32, Math.cos(d5) * 0.15 + this.d(0.05), this.d(0.05), Math.sin(d5) * 0.15 + this.d(0.05));
            }
        }
    }

    @Override
    public void a(d d2, List<cyou.rimuru.shootingstardemo.mc1211.client.render.e> list) {
        float f2;
        float f3 = this.c(d2);
        float f4 = this.a(d2.b());
        float f5 = cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.a(this.z);
        boolean bl = !Float.isNaN(f4) && f5 > 0.001f;
        float f6 = f2 = bl ? cyou.rimuru.shootingstardemo.mc1211.client.fx.h.m(f4) * f5 : 0.0f;
        if (f2 < 0.999f) {
            this.a(d2, list, f3);
        }
        if (f2 > 0.001f) {
            float f7 = this.r;
            list.add(new cyou.rimuru.shootingstardemo.mc1211.client.render.e("gungnir_space", j2 -> {
                cyou.rimuru.shootingstardemo.mc1211.client.fx.h.U.forEach(j2::a);
                cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(f4, f2, f7, cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(j2));
            }));
            if (cyou.rimuru.shootingstardemo.mc1211.client.fx.h.n(f4)) {
                list.add(new cyou.rimuru.shootingstardemo.mc1211.client.render.e("gungnir_mech", j2 -> cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(f4, f7, cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(j2))));
            }
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

    private void a(d d2, List<cyou.rimuru.shootingstardemo.mc1211.client.render.e> list, float f2) {
        if (f2 < 8.0f || f2 > 1222.0f) {
            return;
        }
        ClientLevel clientLevel = d2.a().level;
        float f3 = cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(f2, 9.0f, 10.5f, 300.0f, 318.0f);
        float f4 = f2 >= 300.0f && f2 < 322.0f ? (f2 - 300.0f) / 22.0f : 0.0f;
        float f5 = f2 >= 322.0f ? (f2 - 322.0f) / 20.0f : -1.0f;
        float f6 = (float)this.a((double)f2);
        float f7 = cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(f2, 323.0f, 328.0f, 522.0f, 642.0f);
        float f8 = cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(f2, 9.0f, 21.0f, 292.0f, 300.0f);
        float f9 = (float)cyou.rimuru.shootingstardemo.mc1211.client.fx.f.c((double)f2);
        float f10 = f2 < 322.0f ? 0.0f : (float)Math.exp((double)(-(f2 - 322.0f)) / 700.0) * cyou.rimuru.shootingstardemo.mc1211.client.fx.f.b((f2 - 322.0f) / 3.0f);
        float f11 = f2 < 325.0f ? 0.0f : cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(f2, 325.0f, 352.0f, 1142.0f, 1222.0f);
        float f12 = f2 < 322.0f ? 0.0f : cyou.rimuru.shootingstardemo.mc1211.client.fx.f.b((f2 - 322.0f) / 4.0f) * (float)Math.exp((double)(-(f2 - 322.0f)) / 160.0);
        float f13 = cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(f2, 324.0f, 342.0f, 822.0f, 1122.0f);
        float f14 = f2 < 322.0f ? 0.0f : cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(f2, 322.0f, 322.5f, 1142.0f, 1222.0f);
        float f15 = f2 < 322.0f ? 0.0f : cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(f2, 322.0f, 322.5f, 392.0f, 432.0f);
        float f16 = f2 < 322.0f ? 0.0f : cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(f2, 322.4f, 323.6f, 338.0f, 352.0f);
        Vec3 vec3 = d2.a(this.p);
        double d3 = d2.c().y;
        float f17 = (float)((double)(clientLevel == null ? 320 : clientLevel.getMaxBuildHeight() - 1) - d3);
        int n2 = clientLevel == null ? -64 : clientLevel.getMinBuildHeight();
        float f18 = (float)((double)cyou.rimuru.shootingstardemo.mc1211.star.e.a(n2, this.p.y) - d3);
        float f19 = (float)((double)(n2 + 2) - d3);
        float f20 = f2 / 20.0f;
        float f21 = this.r;
        list.add(new cyou.rimuru.shootingstardemo.mc1211.client.render.e("gungnir_world", j2 -> {
            j2.a("Target", vec3);
            j2.a("P0", f20, f21, f3, f4);
            j2.a("P1", f5, f6, this.i, f7);
            j2.a("P2", f8, f9, (float)vec3.y, f10);
            j2.a("P3", f11, f12, f18, f13);
            j2.a("P4", f17, f19, (float)this.b(), 1.0f);
            j2.a("P5", f14, f15, f16, (float)this.h);
        }));
    }

    static double c(double d2) {
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
    public void a(d d2, cyou.rimuru.shootingstardemo.mc1211.client.render.f f2) {
        float f3;
        float f4;
        float f5;
        float f6;
        boolean bl;
        float f7 = this.c(d2);
        float f8 = this.a(d2.b());
        float f9 = cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.a(this.z);
        boolean bl2 = bl = !Float.isNaN(f8) && f9 > 0.001f;
        if (bl) {
            this.b(f2, f8, f9);
        }
        if (bl && cyou.rimuru.shootingstardemo.mc1211.client.fx.h.m(f8) > 0.5f) {
            return;
        }
        double d3 = d2.c().distanceTo(this.a(d2.c()));
        float f10 = f6 = bl ? 1.0f : (float)Mth.clamp((double)(1.0 - d3 / 1200.0), (double)0.2, (double)1.0);
        if (f7 > 300.0f && f7 < 322.0f) {
            f5 = cyou.rimuru.shootingstardemo.mc1211.client.fx.f.b((f7 - 322.0f + 22.0f) / 22.0f);
            f2.b(655872, f5 * 0.55f * f6);
            f2.b = Math.max(f2.b, f5 * f5 * 0.35f * f6);
            f2.d = Math.max(f2.d, f5 * f5 * f5 * 0.25f * f6);
        }
        if ((f5 = (f7 - 322.0f) / 20.0f) >= 0.0f && f5 < 6.0f) {
            f4 = f5 - 0.05f;
            if (f4 >= 0.0f && f4 < (float)E.length * 0.083333336f) {
                cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(f2, f4, this.p.add(0.0, 10.0, 0.0), f6, 0.083333336f, E);
                int n2 = (int)(f4 / 0.083333336f);
                f3 = f4 / 0.083333336f - (float)n2;
                float f11 = f2.y = f2.y > 0.0f ? Math.min(f2.y, 12.0f) : 12.0f;
                if (n2 % 2 == 0) {
                    f2.d = Math.max(f2.d, (1.0f - f3) * 0.9f * f6);
                } else {
                    f2.b = Math.max(f2.b, (1.0f - f3) * 1.4f * f6);
                }
            }
            f2.d = Math.max(f2.d, cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(f5, 0.0f, 0.02f, 0.4f, 1.4f) * 0.7f * f6);
            f2.b = Math.max(f2.b, cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(f5, 0.0f, 0.02f, 0.5f, 2.0f) * 1.0f * f6);
            f2.m = Math.max(f2.m, 1.2f * cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(f5, 0.0f, 0.1f, 2.0f, 5.0f));
            f2.n = Math.max(f2.n, 1.0f + 1.2f * cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(f5, 0.0f, 0.1f, 1.0f, 3.0f));
            if (this.C < 0 && f5 > (float)E.length * 0.083333336f) {
                cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(f2, cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(f5, 0.9f, 1.1f, 6.0f, 6.0f) * 0.6f * f6, 0.0f);
            }
        }
        if (this.C >= 0 && (f4 = (f7 - (float)this.C) / 20.0f) >= 0.0f && f4 < 8.0f) {
            float f12;
            float f13 = f12 = bl ? 1.0f : f6;
            if (f4 < (float)F.length * 0.083333336f) {
                cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(f2, f4, this.p.add(0.0, 10.0, 0.0), f12, 0.083333336f, F);
                f2.y = f2.y > 0.0f ? Math.min(f2.y, 12.0f) : 12.0f;
                int n3 = (int)(f4 / 0.083333336f);
                f2.d = Math.max(f2.d, n3 % 2 == 0 ? 1.0f * f12 : 0.3f * f12);
            }
            f2.a = Math.max(f2.a, cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(f4, 0.0f, 0.02f, 0.8f, 3.5f) * 2.4f * f12);
            f2.b = Math.max(f2.b, cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(f4, 0.0f, 0.02f, 0.3f, 1.6f) * 1.3f * f12);
            f2.d = Math.max(f2.d, cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(f4, 0.0f, 0.02f, 0.2f, 0.9f) * 0.6f * f12);
            f3 = cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(f4, 0.2f, 1.0f, 4.0f, 8.0f);
            f2.b(2759184, f3 * 0.45f * f12);
            f2.v = Math.max(f2.v, 0.25f * f3 * f12);
        }
        if (f5 >= 0.0f) {
            f4 = cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(f5, 0.5f, 3.0f, 20.0f, 40.0f);
            f2.m = Math.max(f2.m, 0.8f * f4);
        }
    }

    private void b(cyou.rimuru.shootingstardemo.mc1211.client.render.f f2, float f3, float f4) {
        float f5;
        float f62;
        float f7 = cyou.rimuru.shootingstardemo.mc1211.client.fx.h.m(f3) * f4;
        f2.x = Math.max(f2.x, f7);
        if (f7 > 0.001f) {
            f2.m = Math.max(f2.m, 0.55f);
        }
        for (float f6 : new float[]{84.0f, 110.0f}) {
            float f8 = cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(f3, f6 - 1.0f, f6, f6 + 1.0f, f6 + 5.0f);
            f2.b = Math.max(f2.b, f8 * 0.9f);
            f2.a(16763296, f8 * 0.22f);
        }
        float f9 = cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(f3, 124.0f, 128.0f, 140.0f, 146.0f);
        f2.b = Math.max(f2.b, f9 * 0.2f);
        float f10 = cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(f3, 141.0f, 145.0f, 146.0f, 147.0f);
        f2.d = Math.max(f2.d, f10 * 0.5f);
        float f11 = f3 - 160.0f;
        if (f11 >= 0.0f && f11 < 12.0f) {
            f2.a = Math.max(f2.a, (float)Math.exp(-f11 * 0.5f) * 0.9f);
            f2.b = Math.max(f2.b, (float)Math.exp(-f11 * 0.4f) * 0.7f);
            f2.a(16770760, (float)Math.exp(-f11 * 1.2f) * 0.6f);
        }
        if (f3 >= 168.0f && f3 < 252.0f) {
            boolean bl;
            f62 = (f3 - 168.0f) / 84.0f;
            f2.a = Math.max(f2.a, 0.05f + 0.35f * f62 * f62);
            f2.b = Math.max(f2.b, 0.1f + 0.6f * f62 * f62);
            boolean bl2 = bl = f3 < 202.0f || f3 >= 226.0f;
            if (bl) {
                f2.d = Math.max(f2.d, 0.55f * f62 * f62);
            }
            for (int i2 = 1; i2 < 7; ++i2) {
                f5 = (float)cyou.rimuru.shootingstardemo.mc1211.client.fx.h.a(i2);
                float f12 = cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(f3, f5 - 0.5f, f5, f5 + 0.5f, f5 + 3.0f);
                f2.a = Math.max(f2.a, f12 * (0.3f + 0.1f * (float)i2));
                f2.b = Math.max(f2.b, f12 * 0.8f);
                f2.a(16767152, f12 * 0.15f * (float)i2 / 6.0f);
            }
        }
        if ((f62 = f3 - 252.0f) >= 0.0f && f62 < 10.0f) {
            f2.a(0xFFFFFF, f62 < 1.0f ? 3.6f : 3.6f * (float)Math.exp(-(f62 - 1.0f) * 1.4f));
            f2.a = Math.max(f2.a, (float)Math.exp(-f62 * 0.45f) * 1.3f);
            f2.b = Math.max(f2.b, (float)Math.exp(-f62 * 0.35f) * 1.4f);
            f2.d = Math.max(f2.d, (float)Math.exp(-f62 * 0.5f) * 0.8f);
        }
        if (f3 >= 256.0f && f3 < 290.0f) {
            f2.d = Math.max(f2.d, 0.3f);
            float f13 = cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(f3, 262.0f, 263.0f, 264.0f, 265.0f);
            f2.b = Math.max(f2.b, f13 * 0.9f);
            f2.d = Math.max(f2.d, f13 * 0.5f);
            float f14 = cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(f3, 266.0f, 268.0f, 275.0f, 277.0f);
            f2.a = Math.max(f2.a, f14 * 0.4f);
            f5 = cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(f3, 284.0f, 285.0f, 285.5f, 287.0f);
            f2.a = Math.max(f2.a, f5 * 0.5f);
            f2.b = Math.max(f2.b, f5 * 0.6f);
        }
        if (f3 >= 288.0f && f3 < 300.0f) {
            float f15 = cyou.rimuru.shootingstardemo.mc1211.client.fx.f.b((f3 - 290.0f + 2.0f) / 6.0f);
            f2.a = Math.max(f2.a, f15 * 0.8f);
            f2.b = Math.max(f2.b, f15 * 0.8f);
            f2.b(4854784, f15 * 0.4f);
            f2.v = Math.max(f2.v, f15 * 0.3f);
            f2.m = Math.max(f2.m, 0.9f * f15);
        }
        float f16 = cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(f3, 300.0f, 300.2f, 300.6f, 303.0f);
        f2.a(0xFFFFFF, f16 * 1.4f);
    }

    @Override
    b c(float f2) {
        float f3 = this.d(f2);
        float[][] fArrayArray = new float[][]{{0.0f, -0.35f}, {4.0f, -1.4f}, {9.0f, -1.4f}, {16.0f, -2.6f}, {60.0f, -2.6f}, {80.0f, -1.3f}, {402.0f, -1.3f}, {432.0f, -0.35f}};
        float f4 = fArrayArray[fArrayArray.length - 1][1];
        for (int i2 = 0; i2 < fArrayArray.length - 1; ++i2) {
            if (!(f3 >= fArrayArray[i2][0]) || !(f3 < fArrayArray[i2 + 1][0])) continue;
            f4 = Mth.lerp((float)cyou.rimuru.shootingstardemo.mc1211.client.fx.f.b((f3 - fArrayArray[i2][0]) / (fArrayArray[i2 + 1][0] - fArrayArray[i2][0])), (float)fArrayArray[i2][1], (float)fArrayArray[i2 + 1][1]);
            break;
        }
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.b.a(f4, -0.12f, 0.0f).a(cyou.rimuru.shootingstardemo.mc1211.client.fx.f.a(f3, 0.0f, 3.0f, 428.0f, 436.0f));
    }

    @Override
    boolean b(Vec3 vec3) {
        return this.v >= 290 && this.C < 0 && this.v < 522 && Math.hypot(vec3.x - this.p.x, vec3.z - this.p.z) < 2000.0;
    }

    @Override
    public void a(GuiGraphics guiGraphics, d d2) {
        float f2 = this.c(d2);
        Minecraft minecraft = d2.a();
        if (minecraft.player == null || f2 < 9.0f || f2 >= 322.0f) {
            return;
        }
        Vec3 vec3 = minecraft.player.position();
        double d3 = Math.hypot(vec3.x - this.p.x, vec3.z - this.p.z);
        if (d3 > (double)this.i + 150.0) {
            return;
        }
        Font font = minecraft.font;
        int n2 = 0;
        int n3 = cyou.rimuru.shootingstardemo.mc1211.client.fx.i.c(guiGraphics);
        if (f2 >= 290.0f) {
            boolean bl = (int)(f2 / 2.0f) % 2 == 0;
            cyou.rimuru.shootingstardemo.mc1211.client.fx.p.d(guiGraphics, font, "BRACE", n3 / 2, 26 + n2, (bl ? 255 : 120) << 24 | 0xFF5A1F, 1.5f);
            return;
        }
        float f3 = (float)cyou.rimuru.shootingstardemo.mc1211.client.fx.f.c((double)f2) % 1.0f;
        int n4 = (int)(255.0f * (0.55f + 0.45f * (1.0f - f3)));
        String string = d3 < (double)this.i ? "INSIDE THE STRIKE ZONE" : "KINETIC STRIKE INBOUND";
        String string2 = String.format(Locale.ROOT, "T-%05.2f", Float.valueOf(Math.max(0.0f, (322.0f - f2) / 20.0f / this.u)));
        cyou.rimuru.shootingstardemo.mc1211.client.fx.p.d(guiGraphics, font, string, n3 / 2, 26 + n2, n4 << 24 | 0xFF5A1F, 1.5f);
        cyou.rimuru.shootingstardemo.mc1211.client.fx.p.c(guiGraphics, font, string2, n3 / 2, 44 + n2, -1, 1.25f);
    }

    public void b(GuiGraphics guiGraphics, d d2) {
        float f2 = this.a(d2.b());
        if (Float.isNaN(f2)) {
            return;
        }
        cyou.rimuru.shootingstardemo.mc1211.client.fx.g.a(guiGraphics, d2, this, f2, cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.a(this.z));
    }

    int i() {
        return this.C;
    }
}

