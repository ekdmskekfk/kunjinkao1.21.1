/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.cinematic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public final class j {
    private j() {
    }

    public static Vec3 a(Vec3 vec3, double d2) {
        ClientLevel clientLevel = Minecraft.getInstance().level;
        if (clientLevel == null) {
            return vec3;
        }
        int n2 = clientLevel.getHeight(Heightmap.Types.MOTION_BLOCKING, Mth.floor((double)vec3.x), Mth.floor((double)vec3.z));
        if (n2 <= clientLevel.getMinBuildHeight() + 1) {
            return vec3;
        }
        return vec3.y < (double)n2 + d2 ? new Vec3(vec3.x, (double)n2 + d2, vec3.z) : vec3;
    }

    public static Vec3 a(Vec3 vec3, Vec3 vec32, double d2) {
        Entity entity;
        ClientLevel clientLevel = Minecraft.getInstance().level;
        if (clientLevel == null) {
            return vec3;
        }
        Object object = entity = Minecraft.getInstance().getCameraEntity() != null ? Minecraft.getInstance().getCameraEntity() : Minecraft.getInstance().player;
        if (entity == null) {
            return vec3;
        }
        for (double d3 = 0.0; d3 <= d2; d3 += 6.0) {
            Vec3 vec33 = vec3.add(0.0, d3, 0.0);
            BlockHitResult blockHitResult = clientLevel.clip(new ClipContext(vec33, vec32, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, entity));
            if (blockHitResult.getType() == HitResult.Type.BLOCK && !(blockHitResult.getLocation().distanceToSqr(vec32) < 36.0)) continue;
            return vec33;
        }
        return vec3;
    }
}

