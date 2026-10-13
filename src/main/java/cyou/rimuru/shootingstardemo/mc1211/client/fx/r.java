/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.fx;

import cyou.rimuru.shootingstardemo.mc1211.client.fx.x;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;

public class r
extends SimpleSoundInstance {
    private final float a;

    public r(float f2, ResourceLocation resourceLocation, SoundSource soundSource, float f3, float f4, RandomSource randomSource, boolean bl, int n2, SoundInstance.Attenuation attenuation, double d2, double d3, double d4, boolean bl2) {
        super(resourceLocation, soundSource, f3, f4, randomSource, bl, n2, attenuation, d2, d3, d4, bl2);
        this.a = f2;
    }

    public WeighedSoundEvents resolve(SoundManager soundManager) {
        WeighedSoundEvents weighedSoundEvents = super.resolve(soundManager);
        if (cyou.rimuru.shootingstardemo.mc1211.client.fx.x.a(this.a) && this.sound != null && this.sound != SoundManager.EMPTY_SOUND && this.sound != SoundManager.INTENTIONALLY_EMPTY_SOUND && !this.sound.shouldStream()) {
            this.sound = new Sound(cyou.rimuru.shootingstardemo.mc1211.client.fx.x.a(this.sound.getLocation(), this.a), this.sound.getVolume(), this.sound.getPitch(), this.sound.getWeight(), this.sound.getType(), false, false, this.sound.getAttenuationDistance());
        }
        return weighedSoundEvents;
    }
}

