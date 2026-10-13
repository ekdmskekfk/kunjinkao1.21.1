/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.spell;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class b {
    public static final double a = 48.0;
    private static final int b = 8;
    private static final double[] c = new double[]{0.0, 20.0, -20.0, 40.0, -40.0, 60.0, -60.0};
    private static final double d = 0.45;
    private static final int e = 100;
    private static final int f = 20;
    private static final String g = "shooting_star_demo.weightless";
    private static final Map<UUID, a> h = new HashMap<UUID, a>();

    private b() {
    }

    public static Vec3 a(ServerPlayer serverPlayer, Vec3 vec3, double d2, double d3, int n2) {
        if (d2 <= 0.0 || Math.hypot(serverPlayer.getX() - vec3.x, serverPlayer.getZ() - vec3.z) >= d2) {
            return null;
        }
        Vec3 vec32 = cyou.rimuru.shootingstardemo.mc1211.spell.b.a(serverPlayer, vec3, d3);
        if (vec32 == null) {
            return null;
        }
        ServerLevel serverLevel = serverPlayer.serverLevel();
        double d4 = vec3.x - vec32.x;
        double d5 = vec3.z - vec32.z;
        double d6 = vec3.y - (vec32.y + (double)serverPlayer.getEyeHeight());
        float f2 = (float)(Mth.atan2((double)d5, (double)d4) * 57.2957763671875) - 90.0f;
        float f3 = (float)(-Mth.atan2((double)d6, (double)Math.sqrt(d4 * d4 + d5 * d5)) * 57.2957763671875);
        if (serverPlayer.isPassenger()) {
            serverPlayer.stopRiding();
        }
        if (serverPlayer.isFallFlying()) {
            serverPlayer.stopFallFlying();
        }
        serverPlayer.teleportTo(serverLevel, vec32.x, vec32.y, vec32.z, Set.of(), f2, f3);
        serverPlayer.resetFallDistance();
        if (serverPlayer.getAbilities().mayfly) {
            serverPlayer.getAbilities().flying = true;
            serverPlayer.onUpdateAbilities();
        }
        cyou.rimuru.shootingstardemo.mc1211.spell.b.a(serverPlayer, n2);
        return vec32;
    }

    public static void a(MinecraftServer minecraftServer) {
        if (h.isEmpty()) {
            return;
        }
        int n2 = minecraftServer.getTickCount();
        Iterator<Map.Entry<UUID, a>> iterator = h.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, a> entry = iterator.next();
            a a2 = entry.getValue();
            ServerPlayer serverPlayer = minecraftServer.getPlayerList().getPlayer(entry.getKey());
            if (serverPlayer == null) {
                iterator.remove();
                continue;
            }
            if (n2 < a2.b() && serverPlayer.isAlive() && (!serverPlayer.onGround() || n2 - a2.a() <= 20)) continue;
            cyou.rimuru.shootingstardemo.mc1211.spell.b.b(serverPlayer);
            iterator.remove();
        }
    }

    public static void a() {
        h.clear();
    }

    public static void a(ServerPlayer serverPlayer) {
        if (serverPlayer.getTags().contains(g)) {
            cyou.rimuru.shootingstardemo.mc1211.spell.b.b(serverPlayer);
        }
    }

    private static Vec3 a(ServerPlayer serverPlayer, Vec3 vec3, double d2) {
        Vec3 vec32 = cyou.rimuru.shootingstardemo.mc1211.spell.b.a(serverPlayer, vec3);
        for (double d3 : c) {
            double d4;
            double d5 = Math.toRadians(d3);
            double d6 = vec3.x + (vec32.x * Math.cos(d5) - vec32.z * Math.sin(d5)) * d2;
            Vec3 vec33 = cyou.rimuru.shootingstardemo.mc1211.spell.b.a(serverPlayer, d6, d4 = vec3.z + (vec32.x * Math.sin(d5) + vec32.z * Math.cos(d5)) * d2);
            if (vec33 == null) continue;
            return vec33;
        }
        return null;
    }

    private static Vec3 a(ServerPlayer serverPlayer, Vec3 vec3) {
        Vec3 vec32 = new Vec3(serverPlayer.getX() - vec3.x, 0.0, serverPlayer.getZ() - vec3.z);
        if (vec32.lengthSqr() >= 1.0) {
            return vec32.normalize();
        }
        double d2 = Math.toRadians(serverPlayer.getYRot());
        return new Vec3(Math.sin(d2), 0.0, -Math.cos(d2));
    }

    private static Vec3 a(ServerPlayer serverPlayer, double d2, double d3) {
        int n2;
        int n3;
        ServerLevel serverLevel = serverPlayer.serverLevel();
        int n4 = cyou.rimuru.shootingstardemo.mc1211.spell.b.a(serverLevel, n3 = Mth.floor((double)d2), n2 = Mth.floor((double)d3));
        if (n4 <= serverLevel.getMinBuildHeight() || serverLevel.getFluidState(new BlockPos(n3, n4 - 1, n2)).is(FluidTags.LAVA)) {
            return null;
        }
        int n5 = n4;
        for (int i2 = -8; i2 <= 8; i2 += 8) {
            for (int i3 = -8; i3 <= 8; i3 += 8) {
                n5 = Math.max(n5, cyou.rimuru.shootingstardemo.mc1211.spell.b.a(serverLevel, n3 + i2, n2 + i3));
            }
        }
        EntityDimensions entityDimensions = serverPlayer.getType().getDimensions();
        double d4 = (float)(serverLevel.getMinBuildHeight() + serverLevel.dimensionType().logicalHeight()) - entityDimensions.height();
        for (double d5 = Math.min((double)n5 + 48.0, d4); d5 <= d4; d5 += 1.0) {
            AABB aABB = entityDimensions.makeBoundingBox(d2, d5, d3);
            if (!serverLevel.noCollision((Entity)serverPlayer, aABB) || serverLevel.containsAnyLiquid(aABB)) continue;
            return new Vec3(d2, d5, d3);
        }
        return null;
    }

    private static int a(ServerLevel serverLevel, int n2, int n3) {
        LevelChunk levelChunk = serverLevel.getChunk(SectionPos.blockToSectionCoord((int)n2), SectionPos.blockToSectionCoord((int)n3));
        return levelChunk.getHeight(Heightmap.Types.MOTION_BLOCKING, n2 & 0xF, n3 & 0xF) + 1;
    }

    private static void a(ServerPlayer serverPlayer, int n2) {
        serverPlayer.setNoGravity(true);
        serverPlayer.addTag(g);
        int n3 = serverPlayer.serverLevel().getServer().getTickCount();
        h.merge(serverPlayer.getUUID(), new a(n3, n3 + n2), (a2, a3) -> new a(a3.a(), Math.max(a2.b(), a3.b())));
        cyou.rimuru.shootingstardemo.mc1211.spell.b.b(serverPlayer, n2);
    }

    private static void b(ServerPlayer serverPlayer) {
        if (serverPlayer.removeTag(g)) {
            serverPlayer.setNoGravity(false);
        }
        serverPlayer.resetFallDistance();
        if (!serverPlayer.onGround()) {
            cyou.rimuru.shootingstardemo.mc1211.spell.b.b(serverPlayer, 0);
        }
    }

    private static void b(ServerPlayer serverPlayer, int n2) {
        if (serverPlayer.getAbilities().invulnerable) {
            return;
        }
        double d2 = serverPlayer.getY() - (double)cyou.rimuru.shootingstardemo.mc1211.spell.b.a(serverPlayer.serverLevel(), Mth.floor((double)serverPlayer.getX()), Mth.floor((double)serverPlayer.getZ()));
        int n3 = n2 + (int)Math.ceil(Math.max(0.0, d2) / 0.45) + 100;
        serverPlayer.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, n3, 0, true, false, true));
    }

    private static final class a
{
        private final int a;
        private final int b;

        private a(int since, int until) {
            this.a = since;
            this.b = until;
        }

        @Override
        public final String toString() {
            return cyou.rimuru.shootingstardemo.mc1211.RecordBridge.toString(this);
        }

        @Override
        public final int hashCode() {
            return (int)cyou.rimuru.shootingstardemo.mc1211.RecordBridge.hashCode(this);
        }

        @Override
        public final boolean equals(Object object) {
            return (boolean)cyou.rimuru.shootingstardemo.mc1211.RecordBridge.equals(this, object);
        }

        public int a() {
            return this.a;
        }

        public int b() {
            return this.b;
        }
    }
}

