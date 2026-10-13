/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.fx;

import cyou.rimuru.shootingstardemo.mc1211.client.cinematic.h;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.a;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.b;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.d;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.e;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.i;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.j;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.n;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.p;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.q;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.r;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.x;
import cyou.rimuru.shootingstardemo.mc1211.client.render.f;
import cyou.rimuru.shootingstardemo.mc1211.registry.g;
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

public final class o
extends n {
    static final int a = 5;
    static final int b = 7;
    static final int c = 9;
    static final int d = 314;
    static final int e = 352;
    static final int f = 462;
    static final int g = 486;
    static final int h = 29;
    private static final int l = 16724808;
    private static final String[] w = new String[]{"MilkyTop"};
    static final int i = 462;
    private final cyou.rimuru.shootingstardemo.mc1211.client.cinematic.i x = super.e();
    public final float j;
    public final boolean k;
    private final List<SoundInstance> y = new ArrayList<SoundInstance>();
    private j z;
    private static final int A = 3;
    private boolean B;

    o(cyou.rimuru.shootingstardemo.mc1211.net.e e2) {
        super(e2);
        this.j = cyou.rimuru.shootingstardemo.mc1211.star.l.a.j().a(e2.n());
        Minecraft minecraft = Minecraft.getInstance();
        this.k = minecraft.player != null && minecraft.player.getId() == e2.c();
    }

    public static void a(l l2, cyou.rimuru.shootingstardemo.mc1211.net.e e2) {
        o o2 = new o(e2);
        cyou.rimuru.shootingstardemo.mc1211.client.fx.x.a("star_", o2.u);
        cyou.rimuru.shootingstardemo.mc1211.client.fx.e.a(o2);
        if (o2.k && cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.c()) {
            cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.a(cyou.rimuru.shootingstardemo.mc1211.client.cinematic.h.a(o2.e(), o2).a(o2.u));
        } else if (!o2.k) {
            cyou.rimuru.shootingstardemo.mc1211.client.fx.a.a(l2, false, o2.l());
        }
    }

    @Override
    protected int d() {
        return 1386;
    }

    @Override
    public cyou.rimuru.shootingstardemo.mc1211.client.cinematic.i e() {
        return this.x;
    }

    @Override
    public double a() {
        return Math.max(super.a(), (double)this.j + 400.0);
    }

    Vec3 a(Vec3 vec3) {
        Vec3 vec32 = new Vec3(vec3.x - this.p.x, 0.0, vec3.z - this.p.z);
        double d2 = vec32.length();
        if (d2 < 0.001) {
            return this.p;
        }
        return this.p.add(vec32.scale(Math.min(d2, (double)this.j) / d2)).add(0.0, Math.min(Math.max(vec3.y - this.p.y, 0.0), 80.0), 0.0);
    }

    boolean b() {
        return cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.a(this.x) > 0.5f;
    }

    float a(float f2) {
        return cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.a(this.x, f2);
    }

    private SoundInstance a(SoundEvent soundEvent, Vec3 vec3, float f2, boolean bl) {
        r r2;
        if (bl) {
            r2 = new r(this.u, soundEvent.getLocation(), SoundSource.PLAYERS, Math.min(1.0f, f2), 1.0f, SoundInstance.createUnseededRandom(), false, 0, SoundInstance.Attenuation.NONE, 0.0, 0.0, 0.0, true);
            this.y.add((SoundInstance)r2);
        } else {
            r2 = new r(this.u, soundEvent.getLocation(), SoundSource.PLAYERS, f2 * 16.0f, 1.0f, SoundInstance.createUnseededRandom(), false, 0, SoundInstance.Attenuation.LINEAR, vec3.x, vec3.y, vec3.z, false);
        }
        Minecraft.getInstance().getSoundManager().play((SoundInstance)r2);
        return r2;
    }

    private void a(SoundEvent soundEvent, Vec3 vec3, boolean bl) {
        this.a(soundEvent, vec3, bl, bl);
    }

    private void a(SoundEvent soundEvent, Vec3 vec3, boolean bl, boolean bl2) {
        boolean bl3;
        boolean bl4 = bl3 = this.z != null && !this.z.isStopped() && Minecraft.getInstance().getSoundManager().isActive((SoundInstance)this.z);
        j j2 = bl ? new j(this.u, soundEvent.getLocation(), 1.0f, bl3 ? 3 : 0, SoundInstance.Attenuation.NONE, 0.0, 0.0, 0.0, true) : new j(this.u, soundEvent.getLocation(), 1.0f, bl3 ? 3 : 0, SoundInstance.Attenuation.LINEAR, vec3.x, vec3.y, vec3.z, false);
        if (this.z != null) {
            this.z.a(3);
        }
        if (bl2) {
            this.y.add((SoundInstance)j2);
        }
        Minecraft.getInstance().getSoundManager().play((SoundInstance)j2);
        this.z = j2;
    }

    private void f() {
        if (this.z != null && this.y.contains((Object)this.z)) {
            this.z = null;
        }
        for (SoundInstance soundInstance : this.y) {
            Minecraft.getInstance().getSoundManager().stop(soundInstance);
        }
        this.y.clear();
    }

    @Override
    protected void h() {
        this.f();
    }

    private Vec3 a(ClientLevel clientLevel, double d2, double d3) {
        int n2 = clientLevel.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mth.floor((double)d2), Mth.floor((double)d3));
        return new Vec3(d2, n2 <= clientLevel.getMinBuildHeight() ? this.p.y : (double)n2, d3);
    }

    @Override
    protected void a(Minecraft minecraft, ClientLevel clientLevel, int n2) {
        double d2;
        int n3;
        int n4;
        boolean bl = this.b();
        this.B |= bl;
        if (this.B && !bl && !this.y.isEmpty() && n2 < 352) {
            this.f();
        }
        Vec3 vec3 = this.b(clientLevel);
        Vec3 vec32 = this.a(cyou.rimuru.shootingstardemo.mc1211.client.fx.e.e());
        if (n2 == 0) {
            if (this.k) {
                cyou.rimuru.shootingstardemo.mc1211.client.fx.q.ab.values().forEach(cyou.rimuru.shootingstardemo.mc1211.client.render.a::a);
            }
            this.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.n, vec3, bl);
        }
        if (n2 == 7) {
            this.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.o, vec3, bl);
        }
        if (n2 == 9) {
            this.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.d, bl ? this.p : vec32, bl);
            for (n4 = 0; n4 < 40; ++n4) {
                cyou.rimuru.shootingstardemo.mc1211.client.fx.o.a(clientLevel, (ParticleOptions)cyou.rimuru.shootingstardemo.mc1211.client.fx.o.a(n4 % 3 == 0 ? 16765142 : 16724808, 1.0f), this.p.add(0.0, 0.2, 0.0), this.d(0.25), 0.1 + this.t.nextDouble() * 0.4, this.d(0.25));
            }
        }
        if (n2 == 29 && !bl) {
            this.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.e, vec32, 1.0f, false);
        }
        if (bl) {
            if (n2 == 44) {
                this.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.f, this.p, true);
            }
            if (n2 == 238) {
                this.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.g, this.p, true);
            }
            if (n2 == 314) {
                this.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.h, this.p, true);
            }
        }
        if (n2 >= 9 && n2 < 352) {
            for (n4 = 0; n4 < 3; ++n4) {
                cyou.rimuru.shootingstardemo.mc1211.client.fx.o.a(clientLevel, (ParticleOptions)cyou.rimuru.shootingstardemo.mc1211.client.fx.o.a(n4 == 0 ? 16769252 : 16724808, 0.6f + this.t.nextFloat() * 0.5f), this.p.add(this.d(0.2), 0.1, this.d(0.2)), this.d(0.08), 0.05 + this.t.nextDouble() * 0.2, this.d(0.08));
            }
            if (n2 > 312) {
                float f2 = (float)(n2 - 352 + 40) / 40.0f;
                for (n3 = 0; n3 < (int)(30.0f * f2 * f2); ++n3) {
                    d2 = this.t.nextDouble() * Math.PI * 2.0;
                    double d3 = Math.sqrt(this.t.nextDouble()) * (double)this.j;
                    Vec3 vec33 = this.a(clientLevel, this.p.x + Math.cos(d2) * d3, this.p.z + Math.sin(d2) * d3);
                    cyou.rimuru.shootingstardemo.mc1211.client.fx.o.a(clientLevel, (ParticleOptions)cyou.rimuru.shootingstardemo.mc1211.client.fx.o.a(16724808, 1.2f), vec33.add(0.0, 0.5, 0.0), 0.0, 0.3 + this.t.nextDouble() * 0.6, 0.0);
                }
                if (n2 % 4 == 0) {
                    cyou.rimuru.shootingstardemo.mc1211.client.fx.e.a(vec32, 0.05f + 0.15f * f2 * f2, 400.0);
                }
            }
        }
        if (n2 == 352) {
            this.f();
            this.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.i, vec32, 1.0f, bl);
            this.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.j, vec32, bl);
            cyou.rimuru.shootingstardemo.mc1211.client.fx.e.a(vec32, 1.0f, 700.0);
        }
        if (n2 >= 352 && n2 < 486) {
            this.a(clientLevel, n2);
        }
        if (n2 == 462) {
            this.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.p, vec32, bl, false);
        }
        if (n2 == 490) {
            this.a(cyou.rimuru.shootingstardemo.mc1211.registry.g.q, vec32, bl, false);
        }
        if (n2 >= 486 && n2 < 1286) {
            float f3 = 1.0f - (float)(n2 - 486) / 800.0f;
            for (n3 = 0; n3 < (int)(24.0f * f3) + 1; ++n3) {
                boolean bl2;
                d2 = this.t.nextDouble() * Math.PI * 2.0;
                Vec3 vec34 = this.a(clientLevel, this.p.x + Math.cos(d2) * ((double)this.j + 1.5), this.p.z + Math.sin(d2) * ((double)this.j + 1.5));
                boolean bl3 = bl2 = this.t.nextFloat() < 0.4f;
                cyou.rimuru.shootingstardemo.mc1211.client.fx.o.a(clientLevel, (ParticleOptions)cyou.rimuru.shootingstardemo.mc1211.client.fx.o.a(bl2 ? (this.t.nextBoolean() ? 16747050 : 16756810) : 10130068, bl2 ? 0.7f : 1.6f), vec34.add(-Math.cos(d2) * 2.0, -this.t.nextDouble() * 20.0, -Math.sin(d2) * 2.0), this.d(0.03), bl2 ? 0.08 + this.t.nextDouble() * 0.2 : 0.15 + this.t.nextDouble() * 0.15, this.d(0.03));
            }
        }
    }

    private void a(ClientLevel clientLevel, int n2) {
        Vec3 vec3;
        double d2;
        double d3;
        int n3;
        double d4 = cyou.rimuru.shootingstardemo.mc1211.star.j.a(n2, this.j);
        int n4 = n2 - 352;
        int n5 = n4 == 0 ? 500 : 50;
        for (n3 = 0; n3 < n5; ++n3) {
            d3 = this.t.nextDouble() * Math.PI * 2.0;
            d2 = d4 * (0.97 + this.t.nextDouble() * 0.06);
            vec3 = this.a(clientLevel, this.p.x + Math.cos(d3) * d2, this.p.z + Math.sin(d3) * d2);
            BlockState blockState = clientLevel.getBlockState(BlockPos.containing((double)vec3.x, (double)(vec3.y - 0.5), (double)vec3.z));
            if (!blockState.isAir() && n3 % 2 == 0) {
                cyou.rimuru.shootingstardemo.mc1211.client.fx.o.a(clientLevel, (ParticleOptions)new BlockParticleOption(ParticleTypes.BLOCK, blockState), vec3.add(0.0, 0.3, 0.0), -Math.cos(d3) * 0.3, 0.6 + this.t.nextDouble() * 1.4, -Math.sin(d3) * 0.3);
            }
            cyou.rimuru.shootingstardemo.mc1211.client.fx.o.a(clientLevel, (ParticleOptions)cyou.rimuru.shootingstardemo.mc1211.client.fx.o.a(this.t.nextFloat() < 0.3f ? 16766684 : 16724808, 1.4f + this.t.nextFloat()), vec3.add(0.0, this.t.nextDouble() * 12.0, 0.0), Math.cos(d3) * 0.2, 0.4 + this.t.nextDouble() * 0.8, Math.sin(d3) * 0.2);
        }
        if (n4 == 0) {
            for (n3 = 0; n3 < 400; ++n3) {
                d3 = this.t.nextDouble() * Math.PI * 2.0;
                d2 = (double)this.j + this.t.nextDouble() * 12.0;
                vec3 = this.a(clientLevel, this.p.x + Math.cos(d3) * d2, this.p.z + Math.sin(d3) * d2);
                cyou.rimuru.shootingstardemo.mc1211.client.fx.o.a(clientLevel, (ParticleOptions)cyou.rimuru.shootingstardemo.mc1211.client.fx.o.a(12101796, 2.4f), vec3.add(0.0, 1.0, 0.0), Math.cos(d3) * 1.6, 0.2 + this.t.nextDouble() * 0.3, Math.sin(d3) * 1.6);
            }
        }
        if (n2 % 5 == 0) {
            cyou.rimuru.shootingstardemo.mc1211.client.fx.e.a(this.a(cyou.rimuru.shootingstardemo.mc1211.client.fx.e.e()), 0.18f, 500.0);
        }
    }

    @Override
    public void a(d d2, List<cyou.rimuru.shootingstardemo.mc1211.client.render.e> list) {
        boolean bl;
        float f2 = this.c(d2);
        float f3 = this.a(d2.b());
        float f4 = cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.a(this.x);
        boolean bl2 = !Float.isNaN(f3) && f4 > 0.001f;
        float f5 = bl2 ? cyou.rimuru.shootingstardemo.mc1211.client.fx.q.d(f3) * f4 : 0.0f;
        boolean bl3 = bl = bl2 && cyou.rimuru.shootingstardemo.mc1211.client.fx.q.e(f3);
        if (!bl && f5 < 0.999f) {
            this.a(d2, list, f2);
        }
        if (bl) {
            float f6 = this.r;
            list.add(new cyou.rimuru.shootingstardemo.mc1211.client.render.e("star_gun", j2 -> {
                for (String string : w) {
                    j2.a(string, cyou.rimuru.shootingstardemo.mc1211.client.fx.q.ab.get(string));
                }
                cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(f3, f6, cyou.rimuru.shootingstardemo.mc1211.client.fx.o.a(j2));
            }));
            list.add(new cyou.rimuru.shootingstardemo.mc1211.client.render.e("star_rays", j2 -> cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(f3, cyou.rimuru.shootingstardemo.mc1211.client.fx.o.a(j2))));
        } else if (f5 > 0.001f) {
            float f7 = this.r;
            list.add(new cyou.rimuru.shootingstardemo.mc1211.client.render.e("star_space", j2 -> {
                cyou.rimuru.shootingstardemo.mc1211.client.fx.q.ab.forEach(j2::a);
                cyou.rimuru.shootingstardemo.mc1211.client.fx.q.a(f3, f5, f7, cyou.rimuru.shootingstardemo.mc1211.client.fx.o.a(j2));
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

    private void a(d d2, List<cyou.rimuru.shootingstardemo.mc1211.client.render.e> list, float f2) {
        if (f2 < 8.0f || f2 > 1386.0f) {
            return;
        }
        ClientLevel clientLevel = d2.a().level;
        float f3 = cyou.rimuru.shootingstardemo.mc1211.client.fx.o.a(f2, 9.0f, 10.5f, 352.0f, 353.0f);
        float f4 = f2 < 352.0f ? cyou.rimuru.shootingstardemo.mc1211.client.fx.o.b((f2 - 308.0f) / 44.0f) : 0.0f;
        float f5 = cyou.rimuru.shootingstardemo.mc1211.client.fx.o.a(f2, 15.0f, 39.0f, 351.0f, 353.0f);
        float f6 = cyou.rimuru.shootingstardemo.mc1211.client.fx.o.a(f2, 352.0f, 352.5f, 485.0f, 487.0f);
        float f7 = f2 >= 352.0f ? (f2 - 352.0f) / 20.0f : -1.0f;
        float f8 = Mth.clamp((float)((f2 - 462.0f) / 24.0f), (float)0.0f, (float)1.0f);
        float f9 = f2 < 474.0f ? 0.0f : cyou.rimuru.shootingstardemo.mc1211.client.fx.o.b((f2 - 486.0f + 12.0f) / 12.0f) * (float)Math.exp(-Math.max(0.0f, f2 - 486.0f) / 420.0f);
        float f10 = cyou.rimuru.shootingstardemo.mc1211.client.fx.o.a(f2, 9.0f, 21.0f, 332.0f, 352.0f);
        float f11 = (float)cyou.rimuru.shootingstardemo.mc1211.client.fx.o.a((double)f2);
        float f12 = cyou.rimuru.shootingstardemo.mc1211.client.fx.o.a(f2, 470.0f, 486.0f, 1346.0f, 1386.0f);
        float f13 = (float)cyou.rimuru.shootingstardemo.mc1211.star.j.a(f2, this.j);
        Vec3 vec3 = d2.a(this.p);
        float f14 = (float)((double)(clientLevel == null ? -64 : clientLevel.getMinBuildHeight()) - d2.c().y);
        float f15 = f2 / 20.0f;
        float f16 = this.r;
        list.add(new cyou.rimuru.shootingstardemo.mc1211.client.render.e("star_beam", j2 -> {
            j2.a("Target", vec3);
            j2.a("P0", f13, f15, f16, f3);
            j2.a("P1", f3, f5, f4, f6);
            j2.a("P2", f7, f8, f9, this.j);
            j2.a("P3", f10, f11, f14, (float)vec3.y);
            j2.a("P4", f12, 0.0f, 0.0f, 0.0f);
        }));
    }

    static double a(double d2) {
        if (d2 < 29.0) {
            return 0.0;
        }
        double d3 = 16.15;
        double d4 = Math.min(d2 - 29.0, 323.0) / 20.0;
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
        boolean bl;
        boolean bl2;
        float f4 = this.c(d2);
        float f5 = this.a(d2.b());
        float f6 = cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.a(this.x);
        boolean bl3 = bl2 = !Float.isNaN(f5) && f6 > 0.001f;
        if (bl2) {
            this.b(f2, f5, f6);
        }
        boolean bl4 = bl = bl2 && (cyou.rimuru.shootingstardemo.mc1211.client.fx.q.d(f5) > 0.5f || cyou.rimuru.shootingstardemo.mc1211.client.fx.q.e(f5));
        if (bl) {
            return;
        }
        double d3 = d2.c().distanceTo(this.a(d2.c()));
        float f7 = (float)Mth.clamp((double)(1.0 - d3 / 900.0), (double)0.15, (double)1.0);
        if (f4 > 308.0f && f4 < 352.0f) {
            f3 = cyou.rimuru.shootingstardemo.mc1211.client.fx.o.b((f4 - 352.0f + 44.0f) / 44.0f);
            f2.b(3801094, f3 * 0.4f * f7);
            f2.a = Math.max(f2.a, f3 * f3 * 0.35f * f7);
            f2.b = Math.max(f2.b, f3 * f3 * 0.3f * f7);
        }
        if ((f3 = (f4 - 352.0f) / 20.0f) >= 0.0f && f4 < 506.0f) {
            cyou.rimuru.shootingstardemo.mc1211.client.fx.o.a(f2, f3, this.p.add(0.0, 40.0, 0.0), f7, 0.05f, 7, 7, 8, 7, 8, 8);
            f2.a = Math.max(f2.a, cyou.rimuru.shootingstardemo.mc1211.client.fx.o.a(f3, 0.0f, 0.05f, 3.5f, 6.0f) * 0.9f * f7);
            f2.b = Math.max(f2.b, cyou.rimuru.shootingstardemo.mc1211.client.fx.o.a(f3, 0.0f, 0.05f, 0.6f, 3.0f) * f7);
            f2.d = Math.max(f2.d, cyou.rimuru.shootingstardemo.mc1211.client.fx.o.a(f3, 0.0f, 0.03f, 0.35f, 1.4f) * 0.55f * f7);
            float f8 = cyou.rimuru.shootingstardemo.mc1211.client.fx.o.a(f4, 352.0f, 358.0f, 482.0f, 490.0f);
            f2.m = Math.max(f2.m, 1.4f * f8);
            f2.n = Math.max(f2.n, 1.0f + 1.2f * f8);
            f2.b(4849672, f8 * 0.35f * f7);
            f2.a = Math.max(f2.a, f8 * 0.22f * f7);
            f2.b = Math.max(f2.b, f8 * 0.12f * f7);
            float f9 = cyou.rimuru.shootingstardemo.mc1211.client.fx.o.a(f4, 483.0f, 486.0f, 488.0f, 502.0f);
            f2.a(16736368, f9 * 1.2f * f7);
            f2.a = Math.max(f2.a, f9 * 0.5f * f7);
        }
    }

    private void b(f f2, float f3, float f4) {
        float f5;
        float f6 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.d(f3) * f4;
        boolean bl = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.e(f3);
        f2.x = Math.max(f2.x, bl ? f4 : f6);
        float f7 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.f(f3);
        if (f6 > 0.001f || bl) {
            f2.m = Math.max(f2.m, 0.5f * (1.0f - f7 * 0.85f));
        }
        for (float f8 : new float[]{126.0f, 170.0f, 204.0f, 214.0f}) {
            f5 = cyou.rimuru.shootingstardemo.mc1211.client.fx.o.a(f3, f8 - 1.0f, f8, f8 + 1.0f, f8 + 5.0f);
            f2.b = Math.max(f2.b, f5 * 0.9f);
            f2.a(10479871, f5 * 0.18f);
        }
        if (bl && f3 < 314.0f) {
            float f9 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.k(f3);
            float f10 = cyou.rimuru.shootingstardemo.mc1211.client.fx.q.l(f3);
            f2.b = Math.max(f2.b, f9 * f9 * 0.35f + f10 * 0.5f);
            f2.a = Math.max(f2.a, f9 * f9 * 0.12f * (1.0f - f10));
            f2.b(0, f10 * 0.6f);
        }
        if (f3 >= 314.0f && f3 < 320.0f) {
            float f11 = f3 - 314.0f;
            f2.a(0xFFFFFF, f11 < 1.0f ? 3.2f : 3.2f * (float)Math.exp(-(f11 - 1.0f) * 1.6f));
            f2.a = Math.max(f2.a, (float)Math.exp(-f11 * 0.5f) * 1.0f);
            f2.b = Math.max(f2.b, (float)Math.exp(-f11 * 0.4f) * 1.2f);
            f2.d = Math.max(f2.d, (float)Math.exp(-f11 * 0.6f) * 0.7f);
        }
        if (f3 >= 318.0f && f3 < 342.0f) {
            f2.d = Math.max(f2.d, 0.5f * (1.0f - f7));
            f2.a = Math.max(f2.a, 0.06f);
            f2.b = Math.max(f2.b, 0.3f * (1.0f - f7));
        }
        for (float f8 : new float[]{331.0f, 342.0f}) {
            f5 = cyou.rimuru.shootingstardemo.mc1211.client.fx.o.a(f3, f8 - 0.5f, f8, f8 + 1.0f, f8 + 3.0f);
            f2.a = Math.max(f2.a, f5 * 0.7f);
            f2.b = Math.max(f2.b, f5 * 1.1f);
        }
        if (f3 >= 341.5f && f3 < 352.0f) {
            float f12 = cyou.rimuru.shootingstardemo.mc1211.client.fx.o.b((f3 - 342.0f) / 10.0f);
            f2.b(2752516, 0.2f + 0.35f * f12);
        }
    }

    @Override
    b c(float f2) {
        float f3 = this.d(f2);
        float[][] fArrayArray = new float[][]{{0.0f, -0.35f}, {4.0f, -1.4f}, {9.0f, -1.4f}, {16.0f, -2.55f}, {60.0f, -2.55f}, {80.0f, -1.25f}, {496.0f, -1.25f}, {516.0f, -0.35f}};
        float f4 = fArrayArray[fArrayArray.length - 1][1];
        for (int i2 = 0; i2 < fArrayArray.length - 1; ++i2) {
            if (!(f3 >= fArrayArray[i2][0]) || !(f3 < fArrayArray[i2 + 1][0])) continue;
            f4 = Mth.lerp((float)cyou.rimuru.shootingstardemo.mc1211.client.fx.o.b((f3 - fArrayArray[i2][0]) / (fArrayArray[i2 + 1][0] - fArrayArray[i2][0])), (float)fArrayArray[i2][1], (float)fArrayArray[i2 + 1][1]);
            break;
        }
        return cyou.rimuru.shootingstardemo.mc1211.client.fx.b.a(f4, -0.12f, 0.0f).a(cyou.rimuru.shootingstardemo.mc1211.client.fx.o.a(f3, 0.0f, 3.0f, 512.0f, 520.0f));
    }

    @Override
    boolean b(Vec3 vec3) {
        return this.v > 352 && this.v < 462 && vec3.distanceTo(this.a(vec3)) < 1600.0;
    }

    @Override
    public void a(GuiGraphics guiGraphics, d d2) {
        float f2 = this.c(d2);
        Minecraft minecraft = d2.a();
        if (minecraft.player == null || f2 < 9.0f || f2 >= 352.0f) {
            return;
        }
        Vec3 vec3 = minecraft.player.position();
        double d3 = Math.hypot(vec3.x - this.p.x, vec3.z - this.p.z);
        if (d3 > (double)this.j + 120.0) {
            return;
        }
        Font font = minecraft.font;
        int n2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.i.c(guiGraphics);
        float f3 = (float)cyou.rimuru.shootingstardemo.mc1211.client.fx.o.a((double)f2) % 1.0f;
        int n3 = (int)(255.0f * (0.55f + 0.45f * (1.0f - f3)));
        String string = d3 < (double)this.j ? "INSIDE THE STRIKE ZONE" : "ORBITAL STRIKE INBOUND";
        String string2 = String.format(Locale.ROOT, "T-%05.2f", Float.valueOf(Math.max(0.0f, (352.0f - f2) / 20.0f / this.u)));
        cyou.rimuru.shootingstardemo.mc1211.client.fx.p.d(guiGraphics, font, string, n2 / 2, 26, n3 << 24 | 0xFF3348, 1.5f);
        cyou.rimuru.shootingstardemo.mc1211.client.fx.p.c(guiGraphics, font, string2, n2 / 2, 44, -1, 1.25f);
    }

    public void b(GuiGraphics guiGraphics, d d2) {
        float f2 = this.a(d2.b());
        if (Float.isNaN(f2)) {
            return;
        }
        cyou.rimuru.shootingstardemo.mc1211.client.fx.p.a(guiGraphics, d2, this, f2, cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.a(this.x));
    }

    Vec3 c() {
        return this.p;
    }
}

