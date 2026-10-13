/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.fx;

import cyou.rimuru.shootingstardemo.mc1211.client.fx.r;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;

public final class j
extends r
implements TickableSoundInstance {
    private final float a;
    private float b;
    private float c;
    private boolean d;

    public j(float f2, ResourceLocation resourceLocation, float f3, int n2, SoundInstance.Attenuation attenuation, double d2, double d3, double d4, boolean bl) {
        this(f2, resourceLocation, f3, 1.0f, n2, attenuation, d2, d3, d4, bl);
    }

    public j(float f2, ResourceLocation resourceLocation, float f3, float f4, int n2, SoundInstance.Attenuation attenuation, double d2, double d3, double d4, boolean bl) {
        super(f2, resourceLocation, SoundSource.PLAYERS, n2 > 0 ? 0.0f : f3, f4, SoundInstance.createUnseededRandom(), false, 0, attenuation, d2, d3, d4, bl);
        this.a = f3;
        this.b = f3;
        this.c = n2 > 0 ? f3 / (float)n2 : 0.0f;
    }

    public void a(int n2) {
        this.b = 0.0f;
        this.c = Math.max(this.volume, 0.001f) / (float)Math.max(n2, 1);
    }

    public void tick() {
        float f2 = this.volume = this.volume < this.b ? Math.min(this.b, this.volume + this.c) : Math.max(this.b, this.volume - this.c);
        if (this.b == 0.0f && this.volume <= 0.0f) {
            this.d = true;
        }
    }

    public boolean isStopped() {
        return this.d;
    }

    public boolean canStartSilent() {
        return this.a > 0.0f;
    }
}

