/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.mixin;

import com.mojang.blaze3d.audio.SoundBuffer;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.x;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import net.minecraft.Util;
import net.minecraft.client.sounds.SoundBufferLibrary;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={SoundBufferLibrary.class})
abstract class SoundBufferLibraryMixin {
    @Shadow
    @Final
    private ResourceProvider resourceManager;
    @Shadow
    @Final
    private Map<ResourceLocation, CompletableFuture<SoundBuffer>> cache;

    SoundBufferLibraryMixin() {
    }

    @Inject(method={"<init>"}, at={@At(value="TAIL")})
    private void shootingStarDemo$remember(ResourceProvider resourceProvider, CallbackInfo callbackInfo) {
        x.a = (SoundBufferLibrary)(Object)this;
    }

    @Inject(method={"getCompleteBuffer"}, at={@At(value="HEAD")}, cancellable=true)
    private void shootingStarDemo$stretched(ResourceLocation resourceLocation2, CallbackInfoReturnable<CompletableFuture<SoundBuffer>> callbackInfoReturnable) {
        if (x.a(resourceLocation2)) {
            callbackInfoReturnable.setReturnValue(this.cache.computeIfAbsent(resourceLocation2, resourceLocation -> CompletableFuture.supplyAsync(() -> x.a(this.resourceManager, resourceLocation), Util.nonCriticalIoPool())));
        }
    }
}

