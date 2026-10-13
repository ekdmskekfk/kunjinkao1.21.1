/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.mixin;

import cyou.rimuru.shootingstardemo.mc1211.client.fx.b;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.e;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={PlayerModel.class})
abstract class PlayerModelPoseMixin {
    PlayerModelPoseMixin() {
    }

    @Inject(method={"setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V"}, at={@At(value="TAIL")})
    private void shootingStarDemo$castPose(LivingEntity livingEntity, float f2, float f3, float f4, float f5, float f6, CallbackInfo callbackInfo) {
        b b2 = e.a(livingEntity.getId(), Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true));
        if (b2 == null || b2.g() <= 0.0f) {
            return;
        }
        HumanoidModel humanoidModel = (HumanoidModel)(Object)this;
        float f7 = b2.g();
        PlayerModelPoseMixin.apply(humanoidModel.rightArm, b2.a(), b2.b(), b2.c(), f7);
        PlayerModelPoseMixin.apply(humanoidModel.leftArm, b2.d(), b2.e(), b2.f(), f7);
    }

    private static void apply(ModelPart modelPart, float f2, float f3, float f4, float f5) {
        if (Float.isNaN(f2)) {
            return;
        }
        modelPart.xRot = Mth.lerp((float)f5, (float)modelPart.xRot, (float)f2);
        modelPart.yRot = Mth.lerp((float)f5, (float)modelPart.yRot, (float)f3);
        modelPart.zRot = Mth.lerp((float)f5, (float)modelPart.zRot, (float)f4);
    }
}

