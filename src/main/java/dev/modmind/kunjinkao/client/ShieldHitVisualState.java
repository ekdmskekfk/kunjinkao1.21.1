package dev.modmind.kunjinkao.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/** Physical-client state for overlapping, short-lived honeycomb shield impacts. */
public final class ShieldHitVisualState {
    public static final int MAX_SHIELDS_PER_PLAYER = 12;
    public static final int SHIELD_LIFETIME_TICKS = 20;
    private static final Map<UUID, List<ShieldHit>> HITS = new HashMap<>();
    private static long clientTick;
    private ShieldHitVisualState() { }

    public static void tick() {
        clientTick++;
        HITS.values().forEach(ShieldHitVisualState::removeExpired);
        HITS.entrySet().removeIf(entry -> entry.getValue().isEmpty());
    }

    public static void reset() {
        HITS.clear();
        clientTick = 0L;
    }

    public static void add(UUID playerUuid, float relativeImpactYaw, float impactHeight) {
        List<ShieldHit> hits = HITS.computeIfAbsent(playerUuid, ignored -> new ArrayList<>());
        removeExpired(hits);
        hits.add(new ShieldHit(relativeImpactYaw, impactHeight, clientTick));
        while (hits.size() > MAX_SHIELDS_PER_PLAYER) hits.removeFirst();
    }

    public static List<ActiveShieldHit> getActive(UUID playerUuid, float partialTick) {
        List<ShieldHit> hits = HITS.get(playerUuid);
        if (hits == null || hits.isEmpty()) return List.of();
        List<ActiveShieldHit> active = new ArrayList<>(hits.size());
        for (ShieldHit hit : hits) {
            float alpha = 1.0F - ((clientTick - hit.createdClientTick + partialTick) / SHIELD_LIFETIME_TICKS);
            if (alpha > 0.0F) active.add(new ActiveShieldHit(hit.relativeImpactYaw, hit.impactHeight, alpha));
        }
        return active;
    }

    private static void removeExpired(List<ShieldHit> hits) { hits.removeIf(hit -> clientTick - hit.createdClientTick >= SHIELD_LIFETIME_TICKS); }
    private record ShieldHit(float relativeImpactYaw, float impactHeight, long createdClientTick) { }
    public record ActiveShieldHit(float relativeImpactYaw, float impactHeight, float alpha) { }
}
