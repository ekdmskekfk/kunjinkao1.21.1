/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.mixin;

import cyou.rimuru.shootingstardemo.mc1211.client.cinematic.a;
import cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Camera.class})
abstract class CameraMixin {
    @Shadow
    private boolean detached;
    @Shadow
    private Vec3 position;
    @Shadow
    private float xRot;
    @Shadow
    private float yRot;

    CameraMixin() {
    }

    @Shadow
    protected abstract void setPosition(Vec3 var1);

    @Shadow
    protected abstract void setRotation(float var1, float var2);

    @Inject(method={"setup"}, at={@At(value="TAIL")})
    private void shootingStarDemo$cinematic(BlockGetter blockGetter, Entity entity, boolean bl, boolean bl2, float f2, CallbackInfo callbackInfo) {
        a a2 = d.a(this.position, this.yRot, this.xRot, f2);
        if (a2 == null) {
            return;
        }
        if (a2.c().distanceToSqr(entity.getEyePosition(f2)) > 0.8) {
            this.detached = true;
        }
        this.setRotation(a2.a(), a2.b());
        this.setPosition(a2.c());
    }
}

