package dev.modmind.kunjinkao.tactical.common;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

public final class EntityQueryService {

    public static final int MAX_ENTITIES = 200;

    private EntityQueryService() {
    }

    public static List<EntityRowData> getLoadedEntities(MinecraftServer server) {
        List<EntityRowData> result = new ArrayList<>();
        for (ServerLevel level : server.getAllLevels()) {
            for (Entity entity : level.getAllEntities()) {
                result.add(new EntityRowData(
                        entity.getUUID(),
                        entity.getId(),
                        level.dimension().location().toString(),
                        BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString(),
                        entity.getDisplayName().getString(),
                        entity.getX(), entity.getY(), entity.getZ()));
            }
        }
        result.sort(Comparator.comparing(EntityRowData::dimensionId)
                .thenComparing(EntityRowData::displayName)
                .thenComparing(EntityRowData::entityUuid));
        return result.size() > MAX_ENTITIES ? List.copyOf(result.subList(0, MAX_ENTITIES)) : List.copyOf(result);
    }

    public static Entity findLoadedEntity(MinecraftServer server, UUID entityUuid) {
        for (ServerLevel level : server.getAllLevels()) {
            Entity entity = level.getEntity(entityUuid);
            if (entity != null && !entity.isRemoved()) {
                return entity;
            }
        }
        return null;
    }
}
