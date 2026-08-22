package dev.modmind.kunjinkao.tactical.common;

import java.util.UUID;

/** Small, bounded DTO sent to the tactical HUD; entity NBT is never exposed. */
public record EntityRowData(
        UUID entityUuid,
        int runtimeEntityId,
        String dimensionId,
        String entityTypeKey,
        String displayName,
        double x,
        double y,
        double z) {
}
