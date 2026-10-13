/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.mixin;

import cyou.rimuru.shootingstardemo.mc1211.client.magic.i;
import net.minecraft.client.renderer.texture.AbstractTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value={AbstractTexture.class})
abstract class GlyphFilterMixin {
    GlyphFilterMixin() {
    }

    @ModifyVariable(method={"setFilter"}, at=@At(value="HEAD"), argsOnly=true, ordinal=0)
    private boolean shootingStarDemo$smoothWhiteSpace(boolean bl) {
        return bl || i.a((AbstractTexture)(Object)this);
    }
}

