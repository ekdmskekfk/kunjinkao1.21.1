package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public record ManageHudEntityPayload(HudEntityAction action, UUID entityUuid) implements CustomPacketPayload {

    public static final Type<ManageHudEntityPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "manage_hud_entity"));

    public static final StreamCodec<FriendlyByteBuf, ManageHudEntityPayload> STREAM_CODEC =
            StreamCodec.of((buf, p) -> { buf.writeEnum(p.action); buf.writeUUID(p.entityUuid); },
                    buf -> new ManageHudEntityPayload(NetworkHandler.readEnumSafe(buf, HudEntityAction.class), buf.readUUID()));

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(ManageHudEntityPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            dev.modmind.kunjinkao.event.KunJinKaoColdDataEffectsHandler.handleManageEntity(
                    net.neoforged.neoforge.server.ServerLifecycleHooks.getCurrentServer(), context.player(),
                    payload.action, payload.entityUuid);
        });
    }
}