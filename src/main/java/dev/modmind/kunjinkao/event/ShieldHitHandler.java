package dev.modmind.kunjinkao.event;

import dev.modmind.kunjinkao.network.ShieldHitPayload;
import dev.modmind.kunjinkao.tactical.common.SwordPresence;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

/** Computes and broadcasts a shield impact before sword protection can cancel the damage. */
public final class ShieldHitHandler {
    private ShieldHitHandler() { }

    public static void onMobAttack(ServerPlayer player, DamageSource damageSource) {
        if (!(damageSource.getEntity() instanceof Mob attacker) || !SwordPresence.hasRealSwordInInventory(player)) return;
        Entity impactSource = damageSource.getDirectEntity();
        if (impactSource == null) impactSource = attacker;
        Vec3 direction = impactSource.position().subtract(player.position());
        if (direction.x * direction.x + direction.z * direction.z < 1.0E-6D) {
            Vec3 forward = player.getLookAngle();
            direction = new Vec3(forward.x, 0.0D, forward.z);
        }
        float impactYaw = (float)Math.toDegrees(Math.atan2(direction.z, direction.x));
        float relativeImpactYaw = Mth.wrapDegrees(impactYaw - (90.0F - player.getYRot()));
        float impactHeight = Mth.clamp((float)(impactSource.getY() + impactSource.getBbHeight() * 0.5D - player.getY()), 0.20F, 1.35F);
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(player, new ShieldHitPayload(player.getUUID(), relativeImpactYaw, impactHeight));
    }
}
