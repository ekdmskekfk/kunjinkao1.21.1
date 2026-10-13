/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.mixin;

import cyou.rimuru.shootingstardemo.mc1211.client.magic.h;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.KeyboardInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={KeyboardInput.class})
abstract class KeyboardInputMixin
extends Input {
    KeyboardInputMixin() {
    }

    @Inject(method={"tick"}, at={@At(value="TAIL")})
    private void shootingStarDemo$holdInWhiteSpace(boolean bl, float f2, CallbackInfo callbackInfo) {
        if (h.a()) {
            this.up = false;
            this.down = false;
            this.left = false;
            this.right = false;
            this.jumping = false;
            this.shiftKeyDown = false;
            this.leftImpulse = 0.0f;
            this.forwardImpulse = 0.0f;
        }
    }
}

