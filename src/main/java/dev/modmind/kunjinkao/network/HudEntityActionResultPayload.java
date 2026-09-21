package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.clientbridge.ClientHooks;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public record HudEntityActionResultPayload(HudEntityAction action, UUID entityUuid, boolean success, boolean authorized) implements CustomPacketPayload {

    public static final Type<HudEntityActionResultPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "hud_entity_action_result"));

    public static final StreamCodec<FriendlyByteBuf, HudEntityActionResultPayload> STREAM_CODEC =
            StreamCodec.of((buf, p) -> { buf.writeEnum(p.action); buf.writeUUID(p.entityUuid); buf.writeBoolean(p.success); buf.writeBoolean(p.authorized); },
                    buf -> new HudEntityActionResultPayload(NetworkHandler.readEnumSafe(buf, HudEntityAction.class), buf.readUUID(), buf.readBoolean(), buf.readBoolean()));

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(HudEntityActionResultPayload payload, IPayloadContext context) {
        ClientHooks.handleClientPayload(payload, context);
    }
}
