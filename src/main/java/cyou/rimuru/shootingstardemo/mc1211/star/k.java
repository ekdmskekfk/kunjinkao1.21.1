/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.star;

import cyou.rimuru.shootingstardemo.mc1211.magic.a;
import cyou.rimuru.shootingstardemo.mc1211.magic.f;
import cyou.rimuru.shootingstardemo.mc1211.registry.g;
import cyou.rimuru.shootingstardemo.mc1211.spell.b;
import cyou.rimuru.shootingstardemo.mc1211.spell.c;
import cyou.rimuru.shootingstardemo.mc1211.star.d;
import cyou.rimuru.shootingstardemo.mc1211.star.e;
import cyou.rimuru.shootingstardemo.mc1211.star.i;
import cyou.rimuru.shootingstardemo.mc1211.star.j;
import cyou.rimuru.shootingstardemo.mc1211.star.l;
import cyou.rimuru.shootingstardemo.mc1211.star.m;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public final class k {
    private static final double a = 25.0;
    private static final int b = 16724808;

    private k() {
    }

    public static boolean a(ServerPlayer serverPlayer, l l2, f f2) {
        if (c.a(l2, serverPlayer.getUUID())) {
            cyou.rimuru.shootingstardemo.mc1211.magic.a.a(serverPlayer, l2.g + " is already on its way");
            return false;
        }
        long l3 = serverPlayer.getRandom().nextLong();
        switch (l2) {
            case b: {
                c.a(new e(serverPlayer, c.a(serverPlayer, 420.0, 260.0), l3, f2));
                break;
            }
            case c: {
                c.a(new i(serverPlayer, c.a(serverPlayer, 320.0, 400.0), l3, f2));
                break;
            }
            case d: {
                c.a(new m(serverPlayer, c.a(serverPlayer, 320.0, 200.0), l3, f2));
                break;
            }
            default: {
                c.a(new j(serverPlayer, c.a(serverPlayer, 420.0, 260.0), l3, f2));
            }
        }
        return true;
    }

    static boolean a(ServerPlayer serverPlayer) {
        return serverPlayer.isCreative() || serverPlayer.isSpectator();
    }

    static boolean a(Entity entity, ServerPlayer serverPlayer) {
        if (entity instanceof Player) {
            Player player = (Player)entity;
            return !k.a(player) && player.isAlive();
        }
        for (Entity entity2 : entity.getIndirectPassengers()) {
            Player player;
            if (!(entity2 instanceof Player) || !k.a(player = (Player)entity2)) continue;
            return false;
        }
        return !entity.isRemoved();
    }

    private static boolean a(Player player) {
        return player.isCreative() || player.isSpectator();
    }

    static void a(ServerLevel serverLevel, Entity entity, ServerPlayer serverPlayer, float f2) {
        d.a(serverLevel, entity, entity == serverPlayer ? null : serverPlayer, f2);
    }

    static BlockPos a(ServerPlayer serverPlayer, Vec3 vec3, double d2, int n2) {
        double d3 = d2 + 3.0;
        if (!k.a(serverPlayer) || Math.hypot(serverPlayer.getX() - vec3.x, serverPlayer.getZ() - vec3.z) >= d3) {
            return null;
        }
        Vec3 vec32 = serverPlayer.position();
        Vec3 vec33 = cyou.rimuru.shootingstardemo.mc1211.spell.b.a(serverPlayer, vec3, d3, d3 + 25.0, n2);
        if (vec33 != null) {
            k.a(serverPlayer, vec32, vec33);
            return null;
        }
        return BlockPos.containing((double)serverPlayer.getX(), (double)(serverPlayer.getY() - 1.0), (double)serverPlayer.getZ());
    }

    private static void a(ServerPlayer serverPlayer, Vec3 vec3, Vec3 vec32) {
        ServerLevel serverLevel = serverPlayer.serverLevel();
        DustParticleOptions dustParticleOptions = cyou.rimuru.shootingstardemo.mc1211.spell.d.a(16724808, 0.8f);
        DustParticleOptions dustParticleOptions2 = cyou.rimuru.shootingstardemo.mc1211.spell.d.a(16766684, 0.6f);
        cyou.rimuru.shootingstardemo.mc1211.spell.d.a(serverLevel, (ParticleOptions)dustParticleOptions, vec3.x, vec3.y + 1.0, vec3.z, 60, 0.3, 0.8, 0.3, 0.02);
        cyou.rimuru.shootingstardemo.mc1211.spell.d.a(serverLevel, (ParticleOptions)dustParticleOptions2, vec3.x, vec3.y + 1.0, vec3.z, 20, 0.2, 0.8, 0.2, 0.01);
        for (Vec3 vec33 : new Vec3[]{vec3, vec32}) {
            serverLevel.playSound(null, vec33.x, vec33.y, vec33.z, g.m, SoundSource.PLAYERS, 1.2f, 1.0f);
        }
        serverPlayer.connection.send((Packet)new ClientboundSetTitlesAnimationPacket(2, 30, 12));
        serverPlayer.connection.send((Packet)new ClientboundSetSubtitleTextPacket((Component)Component.literal((String)"EVAC // CLEAR OF THE STRIKE ZONE").withStyle(style -> style.withColor(16724808))));
        serverPlayer.connection.send((Packet)new ClientboundSetTitleTextPacket((Component)Component.empty()));
    }
}

