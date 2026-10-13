/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.mixin;

import cyou.rimuru.shootingstardemo.mc1211.client.magic.h;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value={MouseHandler.class})
abstract class MouseTurnMixin {
    @Unique
    private double shootingStarDemo$pitch = Double.NaN;

    MouseTurnMixin() {
    }

    @ModifyArg(method={"turnPlayer"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"), index=0)
    private double shootingStarDemo$whiteSpaceYaw(double d2, double d3) {
        if (!h.a()) {
            this.shootingStarDemo$pitch = Double.NaN;
            return d2;
        }
        double[] dArray = h.a(d2, d3);
        this.shootingStarDemo$pitch = dArray[1];
        return dArray[0];
    }

    @ModifyArg(method={"turnPlayer"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"), index=1)
    private double shootingStarDemo$whiteSpacePitch(double d2, double d3) {
        double d4 = this.shootingStarDemo$pitch;
        this.shootingStarDemo$pitch = Double.NaN;
        return Double.isNaN(d4) ? d3 : d4;
    }
}

