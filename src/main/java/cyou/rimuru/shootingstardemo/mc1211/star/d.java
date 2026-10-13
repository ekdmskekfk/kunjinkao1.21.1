/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.star;

import cyou.rimuru.shootingstardemo.mc1211.registry.c;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityInLevelCallback;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.entity.PartEntity;

public final class d {
    private static final int a = 16;
    private static final int b = 400;
    private static final double c = 16.0;
    private static final float d = 1.0E30f;
    private static final Map<UUID, a> e = new HashMap<UUID, a>();
    private static final List<b> f = new ArrayList<b>();
    private static final MethodHandle g;
    private static final MethodHandle h;

    private d() {
    }

    public static void a(ServerLevel serverLevel, Entity entity, ServerPlayer serverPlayer, float f2) {
        if (f2 == Float.POSITIVE_INFINITY) {
            cyou.rimuru.shootingstardemo.mc1211.star.d.a(serverLevel, entity, serverPlayer);
            return;
        }
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)entity;
            livingEntity.hurt(cyou.rimuru.shootingstardemo.mc1211.registry.c.a(serverLevel, cyou.rimuru.shootingstardemo.mc1211.registry.c.b, serverPlayer == null || serverPlayer.isRemoved() ? null : serverPlayer), f2);
        } else {
            entity.discard();
        }
    }

    public static void a(ServerLevel serverLevel, Entity entity, ServerPlayer serverPlayer) {
        if (entity instanceof Player) {
            Player player = (Player)entity;
            cyou.rimuru.shootingstardemo.mc1211.star.d.a(serverLevel, player, serverPlayer);
            return;
        }
        Entity entity2 = cyou.rimuru.shootingstardemo.mc1211.star.d.b(entity);
        Level level = entity2.level();
        if (!(level instanceof ServerLevel)) {
            return;
        }
        ServerLevel serverLevel2 = (ServerLevel)level;
        int n2 = serverLevel2.getServer().getTickCount() + 400;
        cyou.rimuru.shootingstardemo.mc1211.star.d.a(serverLevel2, entity2, n2);
        AABB aABB = entity2.getBoundingBox().inflate(16.0);
        for (b b2 : f) {
            if (b2.a != serverLevel2 || !b2.b.intersects(aABB)) continue;
            b2.b = b2.b.minmax(aABB);
            b2.c = n2;
            return;
        }
        f.add(new b(serverLevel2, aABB, n2));
    }

    private static Entity b(Entity entity) {
        Entity entity2;
        if (entity instanceof PartEntity) {
            PartEntity partEntity = (PartEntity)entity;
            entity2 = partEntity.getParent();
        } else {
            entity2 = entity;
        }
        return entity2;
    }

    private static void a(ServerLevel serverLevel, Entity entity, int n2) {
        for (Entity entity2 : List.copyOf(entity.getPassengers())) {
            if (entity2 instanceof Player) {
                entity2.stopRiding();
                continue;
            }
            cyou.rimuru.shootingstardemo.mc1211.star.d.a(serverLevel, cyou.rimuru.shootingstardemo.mc1211.star.d.b(entity2), n2);
        }
        entity.stopRiding();
        if (entity instanceof EnderDragon) {
            EnderDragon enderDragon = (EnderDragon)entity;
            net.minecraft.world.level.dimension.end.EndDragonFight endDragonFight = serverLevel.getDragonFight();
            if (endDragonFight != null) {
                endDragonFight.setDragonKilled(enderDragon);
            }
        }
        IntArrayList intArrayList = new IntArrayList();
        intArrayList.add(entity.getId());
        if (entity.isMultipartEntity() && entity.getParts() != null) {
            for (PartEntity<?> partEntity : entity.getParts()) {
                intArrayList.add(partEntity.getId());
            }
        }
        cyou.rimuru.shootingstardemo.mc1211.star.d.a(entity);
        ClientboundRemoveEntitiesPacket clientboundRemoveEntitiesPacket = new ClientboundRemoveEntitiesPacket((IntList)intArrayList);
        cyou.rimuru.shootingstardemo.mc1211.net.c c2 = new cyou.rimuru.shootingstardemo.mc1211.net.c(intArrayList.toIntArray());
        for (ServerPlayer serverPlayer : serverLevel.players()) {
            serverPlayer.connection.send((Packet)clientboundRemoveEntitiesPacket);
            cyou.rimuru.shootingstardemo.mc1211.net.d.a(serverPlayer, c2);
        }
        e.merge(entity.getUUID(), new a(serverLevel, n2), (a2, a3) -> a2.b >= a3.b ? a2 : a3);
    }

    public static void a(Entity entity) {
        try {
            g.invoke(entity, Entity.RemovalReason.KILLED);
            ((EntityInLevelCallback)h.invoke(entity)).onRemove(Entity.RemovalReason.KILLED);
        }
        catch (Throwable throwable) {
            throw new IllegalStateException("could not take " + String.valueOf(entity) + " out of the world", throwable);
        }
    }

    private static void a(ServerLevel serverLevel, Player player, ServerPlayer serverPlayer) {
        DamageSource damageSource = cyou.rimuru.shootingstardemo.mc1211.registry.c.a(serverLevel, cyou.rimuru.shootingstardemo.mc1211.registry.c.a, serverPlayer == null || serverPlayer.isRemoved() ? null : serverPlayer);
        for (int i2 = 0; i2 < 16 && player.isAlive(); ++i2) {
            player.invulnerableTime = 0;
            player.hurt(damageSource, 1.0E30f);
            if (!player.isAlive()) continue;
            player.setHealth(0.0f);
            player.die(damageSource);
        }
    }

    public static void a(MinecraftServer minecraftServer) {
        Entity object;
        Object object2;
        Map.Entry<UUID, a> object3;
        if (e.isEmpty() && f.isEmpty()) {
            return;
        }
        int n2 = minecraftServer.getTickCount();
        ArrayList<Object> arrayList = new ArrayList<Object>();
        ArrayList<Integer> arrayList2 = new ArrayList<Integer>();
        Iterator<Map.Entry<UUID, a>> iterator = e.entrySet().iterator();
        while (iterator.hasNext()) {
            object3 = iterator.next();
            object2 = (a)object3.getValue();
            if (n2 > ((a)object2).b) {
                iterator.remove();
                continue;
            }
            object = ((a)object2).a.getEntity((UUID)object3.getKey());
            if (object == null) continue;
            arrayList.add(object);
            arrayList2.add(((a)object2).b);
        }
        Iterator<b> iterator2 = f.iterator();
        while (iterator2.hasNext()) {
            object2 = (b)iterator2.next();
            if (n2 > ((b)object2).c) {
                iterator2.remove();
                continue;
            }
            for (Entity entity2 : ((b)object2).a.getEntities((Entity)null, ((b)object2).b, cyou.rimuru.shootingstardemo.mc1211.star.d::c)) {
                arrayList.add(entity2);
                arrayList2.add(((b)object2).c);
            }
        }
        for (int i2 = 0; i2 < arrayList.size(); ++i2) {
            ServerLevel serverLevel;
            object = cyou.rimuru.shootingstardemo.mc1211.star.d.b((Entity)arrayList.get(i2));
            Level level = object.level();
            if (!(level instanceof ServerLevel) || (serverLevel = (ServerLevel)level).getEntity(object.getUUID()) != object) continue;
            cyou.rimuru.shootingstardemo.mc1211.star.d.a(serverLevel, object, (Integer)arrayList2.get(i2));
        }
    }

    private static boolean c(Entity entity) {
        Entity entity2;
        if (entity.tickCount > 1 || entity instanceof Player) {
            return false;
        }
        if (entity instanceof ItemEntity) {
            ItemEntity itemEntity = (ItemEntity)entity;
            entity2 = itemEntity.getOwner();
        } else if (entity instanceof Projectile) {
            Projectile projectile = (Projectile)entity;
            entity2 = projectile.getOwner();
        } else if (entity instanceof OwnableEntity) {
            OwnableEntity ownableEntity = (OwnableEntity)entity;
            entity2 = ownableEntity.getOwner();
        } else {
            entity2 = null;
        }
        Entity entity3 = entity2;
        return !(entity3 instanceof Player);
    }

    static {
        Field field = null;
        Field field2 = null;
        for (Field field3 : Entity.class.getDeclaredFields()) {
            if (Modifier.isStatic(field3.getModifiers())) continue;
            if (field3.getType() == Entity.RemovalReason.class) {
                field = field3;
                continue;
            }
            if (field3.getType() != EntityInLevelCallback.class) continue;
            field2 = field3;
        }
        if (field == null || field2 == null) {
            throw new IllegalStateException("Entity's removal fields not found");
        }
        try {
            field.setAccessible(true);
            field2.setAccessible(true);
            g = MethodHandles.lookup().unreflectSetter(field);
            h = MethodHandles.lookup().unreflectGetter(field2);
        }
        catch (IllegalAccessException illegalAccessException) {
            throw new IllegalStateException(illegalAccessException);
        }
    }

    private static final class b {
        final ServerLevel a;
        AABB b;
        int c;

        b(ServerLevel serverLevel, AABB aABB, int n2) {
            this.a = serverLevel;
            this.b = aABB;
            this.c = n2;
        }
    }

    private static final class a
{
        private final ServerLevel a;
        private final int b;

        private a(ServerLevel level, int until) {
            this.a = level;
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

        public ServerLevel a() {
            return this.a;
        }

        public int b() {
            return this.b;
        }
    }
}

