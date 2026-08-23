package dev.modmind.kunjinkao.network;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.tactical.common.EntityAction;
import dev.modmind.kunjinkao.tactical.common.EntityRowData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/** Payload definitions only. Server and client behavior lives in side-specific handlers. */
public final class TacticalHudPayloads {

    private static final int MAX_STRING_LENGTH = 192;

    private TacticalHudPayloads() {
    }

    public record ToggleHudPayload(boolean requestedEnabled) implements CustomPacketPayload {
        public static final Type<ToggleHudPayload> TYPE = TacticalHudPayloads.type("tactical_toggle_hud");
        public static final StreamCodec<FriendlyByteBuf, ToggleHudPayload> STREAM_CODEC =
                StreamCodec.of((buf, payload) -> buf.writeBoolean(payload.requestedEnabled), buf -> new ToggleHudPayload(buf.readBoolean()));

        public static void handle(ToggleHudPayload payload, IPayloadContext context) { TacticalHudServerHandlers.handleToggleHud(payload, context); }
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    public record HudStatePayload(boolean enabled, boolean authorized) implements CustomPacketPayload {
        public static final Type<HudStatePayload> TYPE = TacticalHudPayloads.type("tactical_hud_state");
        public static final StreamCodec<FriendlyByteBuf, HudStatePayload> STREAM_CODEC =
                StreamCodec.of((buf, payload) -> { buf.writeBoolean(payload.enabled); buf.writeBoolean(payload.authorized); },
                        buf -> new HudStatePayload(buf.readBoolean(), buf.readBoolean()));

        public static void handle(HudStatePayload payload, IPayloadContext context) {
            if (context.flow().isClientbound()) context.enqueueWork(() -> TacticalHudClientPayloadBridge.enqueue(payload));
        }
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    public record ToggleNightVisionPayload() implements CustomPacketPayload {
        public static final Type<ToggleNightVisionPayload> TYPE = TacticalHudPayloads.type("tactical_toggle_night_vision");
        public static final StreamCodec<FriendlyByteBuf, ToggleNightVisionPayload> STREAM_CODEC =
                StreamCodec.of((buf, payload) -> { }, buf -> new ToggleNightVisionPayload());

        public static void handle(ToggleNightVisionPayload payload, IPayloadContext context) { TacticalHudServerHandlers.handleToggleNightVision(context); }
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    public record ToggleInvisibilityPayload() implements CustomPacketPayload {
        public static final Type<ToggleInvisibilityPayload> TYPE = TacticalHudPayloads.type("tactical_toggle_invisibility");
        public static final StreamCodec<FriendlyByteBuf, ToggleInvisibilityPayload> STREAM_CODEC =
                StreamCodec.of((buf, payload) -> { }, buf -> new ToggleInvisibilityPayload());

        public static void handle(ToggleInvisibilityPayload payload, IPayloadContext context) { TacticalHudServerHandlers.handleToggleInvisibility(context); }
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    public record InvisibilityStatePayload(UUID playerUuid, boolean invisible) implements CustomPacketPayload {
        public static final Type<InvisibilityStatePayload> TYPE = TacticalHudPayloads.type("tactical_invisibility_state");
        public static final StreamCodec<FriendlyByteBuf, InvisibilityStatePayload> STREAM_CODEC =
                StreamCodec.of((buf, payload) -> { buf.writeUUID(payload.playerUuid); buf.writeBoolean(payload.invisible); },
                        buf -> new InvisibilityStatePayload(buf.readUUID(), buf.readBoolean()));

        public static void handle(InvisibilityStatePayload payload, IPayloadContext context) {
            if (context.flow().isClientbound()) context.enqueueWork(() -> TacticalHudClientPayloadBridge.enqueue(payload));
        }
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    public record RequestEntityListPayload() implements CustomPacketPayload {
        public static final Type<RequestEntityListPayload> TYPE = TacticalHudPayloads.type("tactical_request_entities");
        public static final StreamCodec<FriendlyByteBuf, RequestEntityListPayload> STREAM_CODEC =
                StreamCodec.of((buf, payload) -> { }, buf -> new RequestEntityListPayload());

        public static void handle(RequestEntityListPayload payload, IPayloadContext context) { TacticalHudServerHandlers.handleEntityListRequest(context); }
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    public record EntityListPayload(List<EntityRowData> entities) implements CustomPacketPayload {
        public static final Type<EntityListPayload> TYPE = TacticalHudPayloads.type("tactical_entity_list");
        public static final StreamCodec<FriendlyByteBuf, EntityListPayload> STREAM_CODEC = StreamCodec.of(EntityListPayload::write, EntityListPayload::read);

        private static void write(FriendlyByteBuf buf, EntityListPayload payload) {
            int count = Math.min(payload.entities.size(), 200);
            buf.writeVarInt(count);
            for (int index = 0; index < count; index++) {
                EntityRowData row = payload.entities.get(index);
                buf.writeUUID(row.entityUuid());
                buf.writeVarInt(row.runtimeEntityId());
                buf.writeUtf(row.dimensionId(), MAX_STRING_LENGTH);
                buf.writeUtf(row.entityTypeKey(), MAX_STRING_LENGTH);
                buf.writeUtf(row.displayName(), MAX_STRING_LENGTH);
                buf.writeDouble(row.x()); buf.writeDouble(row.y()); buf.writeDouble(row.z());
            }
        }

        private static EntityListPayload read(FriendlyByteBuf buf) {
            int count = buf.readVarInt();
            if (count < 0 || count > 200) throw new IllegalArgumentException("Invalid tactical entity count: " + count);
            List<EntityRowData> rows = new ArrayList<>(count);
            for (int index = 0; index < count; index++) {
                rows.add(new EntityRowData(buf.readUUID(), buf.readVarInt(), buf.readUtf(MAX_STRING_LENGTH),
                        buf.readUtf(MAX_STRING_LENGTH), buf.readUtf(MAX_STRING_LENGTH), buf.readDouble(), buf.readDouble(), buf.readDouble()));
            }
            return new EntityListPayload(List.copyOf(rows));
        }

        public static void handle(EntityListPayload payload, IPayloadContext context) {
            if (context.flow().isClientbound()) context.enqueueWork(() -> TacticalHudClientPayloadBridge.enqueue(payload));
        }
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    public record ManageEntityPayload(UUID entityUuid, EntityAction action) implements CustomPacketPayload {
        public static final Type<ManageEntityPayload> TYPE = TacticalHudPayloads.type("tactical_manage_entity");
        public static final StreamCodec<FriendlyByteBuf, ManageEntityPayload> STREAM_CODEC =
                StreamCodec.of((buf, payload) -> { buf.writeUUID(payload.entityUuid); buf.writeEnum(payload.action); },
                        buf -> new ManageEntityPayload(buf.readUUID(), buf.readEnum(EntityAction.class)));

        public static void handle(ManageEntityPayload payload, IPayloadContext context) { TacticalHudServerHandlers.handleManageEntity(payload, context); }
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    public record EntityActionResultPayload(UUID subjectUuid, String action, boolean success, String message) implements CustomPacketPayload {
        public static final Type<EntityActionResultPayload> TYPE = TacticalHudPayloads.type("tactical_action_result");
        public static final StreamCodec<FriendlyByteBuf, EntityActionResultPayload> STREAM_CODEC =
                StreamCodec.of((buf, payload) -> { buf.writeUUID(payload.subjectUuid); buf.writeUtf(payload.action, MAX_STRING_LENGTH); buf.writeBoolean(payload.success); buf.writeUtf(payload.message, MAX_STRING_LENGTH); },
                        buf -> new EntityActionResultPayload(buf.readUUID(), buf.readUtf(MAX_STRING_LENGTH), buf.readBoolean(), buf.readUtf(MAX_STRING_LENGTH)));

        public static void handle(EntityActionResultPayload payload, IPayloadContext context) {
            if (context.flow().isClientbound()) context.enqueueWork(() -> TacticalHudClientPayloadBridge.enqueue(payload));
        }
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    private static <T extends CustomPacketPayload> CustomPacketPayload.Type<T> type(String path) {
        return new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, path));
    }
}
