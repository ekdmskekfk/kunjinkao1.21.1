/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.mixin;

import cyou.rimuru.shootingstardemo.mc1211.client.magic.h;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={MouseHandler.class})
abstract class MouseHandlerMixin {
    MouseHandlerMixin() {
    }

    @Inject(method={"onPress"}, at={@At(value="HEAD")}, cancellable=true)
    private void shootingStarDemo$whiteSpaceClick(long l2, int n2, int n3, int n4, CallbackInfo callbackInfo) {
        Minecraft minecraft = Minecraft.getInstance();
        if (n3 == 0 && l2 == minecraft.getWindow().getWindow()) {
            h.a(n2);
        }
        if (n3 != 1 || l2 != minecraft.getWindow().getWindow() || minecraft.screen != null || minecraft.getOverlay() != null || !h.a()) {
            return;
        }
        if (!minecraft.mouseHandler.isMouseGrabbed()) {
            minecraft.mouseHandler.grabMouse();
        }
        h.a(n2, n4);
        callbackInfo.cancel();
    }

    @Inject(method={"onScroll"}, at={@At(value="HEAD")}, cancellable=true)
    private void shootingStarDemo$whiteSpaceScroll(long l2, double d2, double d3, CallbackInfo callbackInfo) {
        Minecraft minecraft = Minecraft.getInstance();
        if (l2 == minecraft.getWindow().getWindow() && minecraft.screen == null && h.a()) {
            h.a(d3);
            callbackInfo.cancel();
        }
    }
}

