/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import cyou.rimuru.shootingstardemo.mc1211.client.magic.h;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={KeyboardHandler.class})
abstract class KeyboardHandlerMixin {
    KeyboardHandlerMixin() {
    }

    @Inject(method={"keyPress"}, at={@At(value="HEAD")}, cancellable=true)
    private void shootingStarDemo$whiteSpace(long l2, int n2, int n3, int n4, int n5, CallbackInfo callbackInfo) {
        Minecraft minecraft = Minecraft.getInstance();
        if (n4 == 0 || l2 != minecraft.getWindow().getWindow() || minecraft.screen != null || !h.a()) {
            return;
        }
        if (n2 >= 291 && n2 <= 314 || minecraft.options.keyScreenshot.matches(n2, n3) || minecraft.options.keyFullscreen.matches(n2, n3) || InputConstants.isKeyDown((long)l2, (int)292)) {
            return;
        }
        if (n4 == 1) {
            h.a(n2, n3, n5);
        }
        callbackInfo.cancel();
    }
}

