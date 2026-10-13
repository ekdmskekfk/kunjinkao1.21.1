/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.spell;

import cyou.rimuru.shootingstardemo.mc1211.ShootingStarDemo;
import cyou.rimuru.shootingstardemo.mc1211.net.e;
import cyou.rimuru.shootingstardemo.mc1211.spell.a;
import cyou.rimuru.shootingstardemo.mc1211.spell.b;
import cyou.rimuru.shootingstardemo.mc1211.spell.d;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public final class c {
    private static final List<a> a = new ArrayList<a>();
    private static final List<a> b = new ArrayList<a>();
    private static boolean c;

    private c() {
    }

    public static void a(MinecraftServer minecraftServer) {
        c = true;
        Iterator<a> iterator = a.iterator();
        while (iterator.hasNext()) {
            boolean bl;
            a a2 = iterator.next();
            try {
                bl = a2.a();
            }
            catch (RuntimeException runtimeException) {
                ShootingStarDemo.LOGGER.c("Spell {} failed", a2.a.a(), runtimeException);
                bl = true;
            }
            if (!bl) continue;
            a2.f();
            iterator.remove();
        }
        c = false;
        a.addAll(b);
        b.clear();
        cyou.rimuru.shootingstardemo.mc1211.spell.b.a(minecraftServer);
        cyou.rimuru.shootingstardemo.mc1211.star.a.c();
    }

    public static void a() {
        a.forEach(cyou.rimuru.shootingstardemo.mc1211.spell.a::f);
        a.clear();
        b.clear();
        cyou.rimuru.shootingstardemo.mc1211.spell.b.a();
        cyou.rimuru.shootingstardemo.mc1211.star.a.e();
        cyou.rimuru.shootingstardemo.mc1211.star.b.a();
    }

    public static void a(a a2) {
        if (c) {
            b.add(a2);
        } else {
            a.add(a2);
        }
        d.a(a2.b, a2.e, a2.e(), a2.d());
    }

    public static void b(a a2) {
        if (c) {
            b.add(a2);
        } else {
            a.add(a2);
        }
        e e2 = a2.d();
        for (ServerPlayer serverPlayer : a2.b.getServer().getPlayerList().getPlayers()) {
            cyou.rimuru.shootingstardemo.mc1211.net.d.a(serverPlayer, e2);
        }
    }

    public static boolean a(cyou.rimuru.shootingstardemo.mc1211.magic.d d2) {
        for (List<a> list : List.of(a, b)) {
            for (a a2 : list) {
                if (a2.a != d2) continue;
                return true;
            }
        }
        return false;
    }

    public static boolean a(cyou.rimuru.shootingstardemo.mc1211.magic.d d2, UUID uUID) {
        for (List<a> list : List.of(a, b)) {
            for (a a2 : list) {
                if (a2.a != d2 || !a2.c.equals(uUID)) continue;
                return true;
            }
        }
        return false;
    }

    public static Vec3 a(ServerPlayer serverPlayer, double d2, double d3) {
        Vec3 vec3;
        Vec3 vec32 = serverPlayer.getEyePosition();
        Vec3 vec33 = serverPlayer.getLookAngle();
        HitResult hitResult = serverPlayer.pick(d2, 1.0f, false);
        Vec3 vec34 = hitResult.getType() == HitResult.Type.MISS ? vec32.add(vec33.scale(d2)) : hitResult.getLocation();
        LivingEntity livingEntity = cyou.rimuru.shootingstardemo.mc1211.spell.c.a(serverPlayer, d2);
        if (livingEntity != null) {
            vec3 = livingEntity.position();
        } else if (hitResult.getType() != HitResult.Type.MISS) {
            vec3 = vec34.subtract(vec33.scale(0.05));
        } else if (vec33.y < 0.0) {
            vec3 = vec34;
        } else {
            Vec3 vec35 = new Vec3(vec33.x, 0.0, vec33.z);
            vec35 = vec35.lengthSqr() < 1.0E-4 ? new Vec3(0.0, 0.0, 1.0) : vec35.normalize();
            vec3 = vec32.add(vec35.scale(d3)).with(Direction.Axis.Y, (double)(serverPlayer.level().getMaxBuildHeight() - 1));
        }
        return new Vec3(vec3.x, (double)cyou.rimuru.shootingstardemo.mc1211.spell.c.a(serverPlayer.serverLevel(), vec3), vec3.z);
    }

    public static LivingEntity a(ServerPlayer serverPlayer, double d2) {
        LivingEntity livingEntity;
        Entity entity2;
        Vec3 vec3 = serverPlayer.getEyePosition();
        HitResult hitResult = serverPlayer.pick(d2, 1.0f, false);
        Vec3 vec32 = hitResult.getType() == HitResult.Type.MISS ? vec3.add(serverPlayer.getLookAngle().scale(d2)) : hitResult.getLocation();
        EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult((Entity)serverPlayer, (Vec3)vec3, (Vec3)vec32, (AABB)serverPlayer.getBoundingBox().expandTowards(vec32.subtract(vec3)).inflate(1.0), entity -> d.a(entity, serverPlayer), (double)vec3.distanceToSqr(vec32));
        return entityHitResult != null && (entity2 = entityHitResult.getEntity()) instanceof LivingEntity ? (livingEntity = (LivingEntity)entity2) : null;
    }

    private static int a(ServerLevel serverLevel, Vec3 vec3) {
        int n2;
        int n3 = Mth.floor((double)vec3.x);
        int n4 = Mth.floor((double)vec3.z);
        LevelChunk levelChunk = serverLevel.getChunk(SectionPos.blockToSectionCoord((int)n3), SectionPos.blockToSectionCoord((int)n4));
        Heightmap.Types types = Heightmap.Types.MOTION_BLOCKING_NO_LEAVES;
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        for (n2 = Math.min(Mth.floor((double)vec3.y), levelChunk.getHeight(types, n3 & 0xF, n4 & 0xF) + 1); n2 > serverLevel.getMinBuildHeight() && !types.isOpaque().test(levelChunk.getBlockState((BlockPos)mutableBlockPos.set(n3, n2 - 1, n4))); --n2) {
        }
        return n2;
    }
}

