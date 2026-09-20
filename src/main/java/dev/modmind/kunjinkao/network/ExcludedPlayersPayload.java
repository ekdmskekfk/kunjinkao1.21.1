package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.clientbridge.ClientHooks;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record ExcludedPlayersPayload(boolean authorized, List<ExcludedPlayerData> players) implements CustomPacketPayload {

    private static final int MAX_ENTRIES = 4096;

    public static final Type<ExcludedPlayersPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "excluded_players"));

    public static final StreamCodec<FriendlyByteBuf, ExcludedPlayersPayload> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public void encode(FriendlyByteBuf buf, ExcludedPlayersPayload p) {
                    buf.writeBoolean(p.authorized);
                    buf.writeVarInt(p.players.size());
                    for (ExcludedPlayerData entry : p.players) {
                        buf.writeUUID(entry.uuid());
                        buf.writeUtf(entry.name(), 64);
                    }
                }

                @Override
                public ExcludedPlayersPayload decode(FriendlyByteBuf buf) {
                    boolean authorized = buf.readBoolean();
                    int count = buf.readVarInt();
                    if (count < 0 || count > MAX_ENTRIES) {
                        throw new IllegalArgumentException("Invalid excluded player list size: " + count);
                    }
                    List<ExcludedPlayerData> list = new ArrayList<>(count);
                    for (int i = 0; i < count; i++) {
                        UUID uuid = buf.readUUID();
                        list.add(new ExcludedPlayerData(uuid, buf.readUtf(64)));
                    }
                    return new ExcludedPlayersPayload(authorized, list);
                }
            };

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(ExcludedPlayersPayload payload, IPayloadContext context) {
        ClientHooks.handleClientPayload(payload, context);
    }
}