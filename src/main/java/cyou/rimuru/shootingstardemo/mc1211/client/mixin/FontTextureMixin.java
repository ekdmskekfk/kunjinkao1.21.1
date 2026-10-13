/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.mixin;

import cyou.rimuru.shootingstardemo.mc1211.client.magic.i;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={TextureManager.class})
abstract class FontTextureMixin {
    FontTextureMixin() {
    }

    @Inject(method={"register(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/client/renderer/texture/AbstractTexture;)V"}, at={@At(value="HEAD")})
    private void shootingStarDemo$noteWhiteSpaceFont(ResourceLocation resourceLocation, AbstractTexture abstractTexture, CallbackInfo callbackInfo) {
        i.a(resourceLocation, abstractTexture);
    }
}

