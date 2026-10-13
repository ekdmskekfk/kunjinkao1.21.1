/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.e;
import cyou.rimuru.shootingstardemo.mc1211.client.magic.h;
import cyou.rimuru.shootingstardemo.mc1211.client.render.k;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={GameRenderer.class})
abstract class GameRendererMixin {
    GameRendererMixin() {
    }

    @Inject(method={"bobHurt"}, at={@At(value="HEAD")})
    private void shootingStarDemo$shake(PoseStack poseStack, float f2, CallbackInfo callbackInfo) {
        e.a(poseStack, f2);
    }

    @Inject(method={"getFov"}, at={@At(value="RETURN")}, cancellable=true)
    private void shootingStarDemo$cinematicFov(Camera camera, float f2, boolean bl, CallbackInfoReturnable<Double> callbackInfoReturnable) {
        if (bl && d.g()) {
            callbackInfoReturnable.setReturnValue((double)d.a(((Double)callbackInfoReturnable.getReturnValue()).floatValue()));
        }
    }

    @Inject(method={"renderItemInHand"}, at={@At(value="HEAD")}, cancellable=true)
    private void shootingStarDemo$hideHand(Camera camera, float f2, Matrix4f matrix4f, CallbackInfo callbackInfo) {
        if (d.g() || h.c()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"render"}, at={@At(value="RETURN")})
    private void shootingStarDemo$frameMark(DeltaTracker deltaTracker, boolean bl, CallbackInfo callbackInfo) {
        k.a();
    }
}

