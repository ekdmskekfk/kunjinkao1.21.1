/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.mixin;

import cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.a;
import cyou.rimuru.shootingstardemo.mc1211.client.magic.e;
import cyou.rimuru.shootingstardemo.mc1211.client.magic.h;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Gui.class})
abstract class GuiMixin {
    GuiMixin() {
    }

    @Inject(method={"render"}, at={@At(value="HEAD")})
    private void shootingStarDemo$whiteSpaceMirror(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo callbackInfo) {
        h.f();
    }

    @Inject(method={"render"}, at={@At(value="HEAD")}, cancellable=true)
    private void shootingStarDemo$film(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo callbackInfo) {
        if (d.h()) {
            float f2 = deltaTracker.getGameTimeDeltaPartialTick(true);
            cyou.rimuru.shootingstardemo.mc1211.client.fx.e.a(guiGraphics, f2);
            d.a(guiGraphics, f2);
            callbackInfo.cancel();
        }
    }

    @Inject(method={"render"}, at={@At(value="TAIL")})
    private void shootingStarDemo$hud(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo callbackInfo) {
        float f2 = deltaTracker.getGameTimeDeltaPartialTick(true);
        cyou.rimuru.shootingstardemo.mc1211.client.fx.e.b(guiGraphics, f2);
        a.a(guiGraphics, f2);
        e.a(guiGraphics, f2);
        d.a(guiGraphics, f2);
    }

    @Inject(method={"render"}, at={@At(value="TAIL")})
    private void shootingStarDemo$whiteSpace(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo callbackInfo) {
        h.a(guiGraphics, deltaTracker.getGameTimeDeltaPartialTick(true));
    }
}

