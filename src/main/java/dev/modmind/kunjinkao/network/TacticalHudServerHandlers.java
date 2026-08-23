package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.tactical.common.AuthService;
import dev.modmind.kunjinkao.tactical.common.EntityAction;
import dev.modmind.kunjinkao.tactical.common.EntityQueryService;
import dev.modmind.kunjinkao.tactical.common.TacticalHudInvisibilityService;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/** Every C2S request is authorized again here; client UI is never trusted. */
public final class TacticalHudServerHandlers {

    private static final String NIGHT_VISION_MARKER = KunJinKaoEntry.MOD_ID + ":hud_night_vision";

    private TacticalHudServerHandlers() {
    }

    public static void handleToggleHud(TacticalHudPayloads.ToggleHudPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> withPlayer(context, player -> {
            boolean authorized = AuthService.isAuthorized(player.getUUID());
            NetworkHandler.sendToPlayer(player, new TacticalHudPayloads.HudStatePayload(authorized && payload.requestedEnabled(), authorized));
        }));
    }

    public static void handleToggleNightVision(IPayloadContext context) {
        context.enqueueWork(() -> withAuthorizedPlayer(context, player -> {
            if (player.getPersistentData().getBoolean(NIGHT_VISION_MARKER)) {
                player.removeEffect(MobEffects.NIGHT_VISION);
                player.getPersistentData().remove(NIGHT_VISION_MARKER);
                sendResult(player, player.getUUID(), "night_vision", true, "Night vision disabled");
            } else if (player.hasEffect(MobEffects.NIGHT_VISION)) {
                sendResult(player, player.getUUID(), "night_vision", false, "Night vision is controlled by another source");
            } else {
                player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 36_000, 0, false, false, false));
                player.getPersistentData().putBoolean(NIGHT_VISION_MARKER, true);
                sendResult(player, player.getUUID(), "night_vision", true, "Night vision enabled");
            }
        }));
    }

    public static void handleEntityListRequest(IPayloadContext context) {
        context.enqueueWork(() -> withAuthorizedPlayer(context, player -> NetworkHandler.sendToPlayer(player,
                new TacticalHudPayloads.EntityListPayload(EntityQueryService.getLoadedEntities(player.server)))));
    }

    public static void handleToggleInvisibility(IPayloadContext context) {
        context.enqueueWork(() -> withAuthorizedPlayer(context, player -> {
            TacticalHudInvisibilityService.ToggleResult result = TacticalHudInvisibilityService.toggle(player);
            if (result.success()) broadcastInvisibilityState(player, TacticalHudInvisibilityService.isActive(player));
            sendResult(player, player.getUUID(), "invisibility", result.success(), result.message());
        }));
    }

    public static void handleManageEntity(TacticalHudPayloads.ManageEntityPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> withAuthorizedPlayer(context, player -> {
            Entity target = EntityQueryService.findLoadedEntity(player.server, payload.entityUuid());
            if (target == null) {
                sendResult(player, payload.entityUuid(), payload.action().name(), false, "Entity is no longer loaded");
                return;
            }
            if (payload.action() == EntityAction.TERMINATE) {
                if (target instanceof ServerPlayer) {
                    sendResult(player, payload.entityUuid(), "terminate", false, "Players cannot be terminated from this HUD");
                } else {
                    target.discard();
                    sendResult(player, payload.entityUuid(), "terminate", true, "Entity removed");
                }
                return;
            }
            if (target.level() instanceof ServerLevel targetLevel) {
                player.teleportTo(targetLevel, target.getX(), target.getY(), target.getZ(), target.getYRot(), target.getXRot());
                sendResult(player, payload.entityUuid(), "teleport_to", true, "Teleported to entity");
            } else {
                sendResult(player, payload.entityUuid(), "teleport_to", false, "Entity is not in a server level");
            }
        }));
    }

    private static void withPlayer(IPayloadContext context, java.util.function.Consumer<ServerPlayer> action) {
        if (context.player() instanceof ServerPlayer player) action.accept(player);
    }

    private static void withAuthorizedPlayer(IPayloadContext context, java.util.function.Consumer<ServerPlayer> action) {
        withPlayer(context, player -> { if (AuthService.isAuthorized(player.getUUID())) action.accept(player); });
    }

    private static void sendResult(ServerPlayer player, java.util.UUID subjectUuid, String action, boolean success, String message) {
        NetworkHandler.sendToPlayer(player, new TacticalHudPayloads.EntityActionResultPayload(subjectUuid, action, success, message));
    }

    public static void broadcastInvisibilityState(ServerPlayer player, boolean invisible) {
        TacticalHudPayloads.InvisibilityStatePayload payload = new TacticalHudPayloads.InvisibilityStatePayload(player.getUUID(), invisible);
        for (ServerPlayer recipient : player.server.getPlayerList().getPlayers()) {
            NetworkHandler.sendToPlayer(recipient, payload);
        }
    }
}
