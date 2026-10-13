/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.mixin;

import cyou.rimuru.shootingstardemo.mc1211.client.fx.e;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.sounds.SoundSource;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={SoundEngine.class})
abstract class SoundEngineMixin {
    SoundEngineMixin() {
    }

    @Inject(method={"getVolume"}, at={@At(value="HEAD")}, cancellable=true)
    private void shootingStarDemo$hush(@Nullable SoundSource soundSource, CallbackInfoReturnable<Float> callbackInfoReturnable) {
        if (e.b() && soundSource != null && soundSource != SoundSource.PLAYERS && soundSource != SoundSource.MASTER) {
            callbackInfoReturnable.setReturnValue(1.0E-4f);
        }
    }
}

