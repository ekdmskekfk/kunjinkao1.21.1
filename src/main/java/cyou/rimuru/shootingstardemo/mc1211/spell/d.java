/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.spell;

import cyou.rimuru.shootingstardemo.mc1211.registry.c;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public final class d {
    public static final double a = 512.0;

    private d() {
    }

    public static boolean a(Entity entity, ServerPlayer serverPlayer) {
        LivingEntity livingEntity;
        if (!(entity instanceof LivingEntity) || !(livingEntity = (LivingEntity)entity).isAlive() || entity instanceof ArmorStand) {
            return false;
        }
        if (entity.isSpectator()) {
            return false;
        }
        if (serverPlayer != null) {
            OwnableEntity ownableEntity;
            if (entity == serverPlayer || entity.isPassengerOfSameVehicle((Entity)serverPlayer)) {
                return false;
            }
            if (entity instanceof OwnableEntity && (ownableEntity = (OwnableEntity)entity).getOwnerUUID() != null && serverPlayer.getUUID().equals(ownableEntity.getOwnerUUID())) {
                return false;
            }
        }
        return true;
    }

    public static boolean a(LivingEntity livingEntity) {
        return livingEntity instanceof EnderDragon || livingEntity instanceof WitherBoss || livingEntity instanceof ElderGuardian || livingEntity instanceof Warden;
    }

    public static void a(ServerLevel serverLevel, LivingEntity livingEntity, ServerPlayer serverPlayer, ResourceKey<DamageType> resourceKey, float f2) {
        livingEntity.hurt(c.a(serverLevel, resourceKey, (Entity)serverPlayer), f2);
    }

    public static List<LivingEntity> a(ServerLevel serverLevel, Vec3 vec3, double d2, ServerPlayer serverPlayer) {
        AABB aABB = AABB.ofSize((Vec3)vec3, (double)(d2 * 2.0), (double)(d2 * 2.0), (double)(d2 * 2.0));
        double d3 = d2 * d2;
        ArrayList<LivingEntity> arrayList = new ArrayList<LivingEntity>();
        for (LivingEntity livingEntity2 : serverLevel.getEntitiesOfClass(LivingEntity.class, aABB, livingEntity -> d.a((Entity)livingEntity, serverPlayer))) {
            if (!(livingEntity2.position().distanceToSqr(vec3) <= d3)) continue;
            arrayList.add(livingEntity2);
        }
        return arrayList;
    }

    public static void a(ServerPlayer serverPlayer) {
        serverPlayer.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 40, 4, true, false, true));
        serverPlayer.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 40, 0, true, false, true));
    }

    public static boolean a(ServerLevel serverLevel) {
        return serverLevel.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
    }

    public static int a(ServerLevel serverLevel, int n2, int n3) {
        return serverLevel.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, n2, n3);
    }

    public static DustParticleOptions a(int n2, float f2) {
        return new DustParticleOptions(new Vector3f((float)(n2 >> 16 & 0xFF) / 255.0f, (float)(n2 >> 8 & 0xFF) / 255.0f, (float)(n2 & 0xFF) / 255.0f), f2);
    }

    public static void a(ServerLevel serverLevel, ParticleOptions particleOptions, double d2, double d3, double d4, int n2, double d5, double d6, double d7, double d8) {
        for (ServerPlayer serverPlayer : serverLevel.players()) {
            serverLevel.sendParticles(serverPlayer, particleOptions, true, d2, d3, d4, n2, d5, d6, d7, d8);
        }
    }

    public static void a(ServerLevel serverLevel, Vec3 vec3, CustomPacketPayload customPacketPayload) {
        d.a(serverLevel, vec3, 512.0, customPacketPayload);
    }

    public static void a(ServerLevel serverLevel, Vec3 vec3, double d2, CustomPacketPayload customPacketPayload) {
        double d3 = Math.max(d2, 512.0);
        for (ServerPlayer serverPlayer : serverLevel.players()) {
            if (serverPlayer.distanceToSqr(vec3) > d3 * d3) continue;
            cyou.rimuru.shootingstardemo.mc1211.net.d.a(serverPlayer, customPacketPayload);
        }
    }
}

