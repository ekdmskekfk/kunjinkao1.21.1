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

    /** 编解码共用的条数上限：编码端先截断，解码端复用同一常量校验，避免两侧上限不对称。 */
    public static final int MAX_ENTRIES = 4096;

    /** 玩家名的字符数上限（与 buf.writeUtf / readUtf 的限制保持一致）。 */
    private static final int MAX_STRING_LENGTH = 64;

    public static final Type<ExcludedPlayersPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "excluded_players"));

    public static final StreamCodec<FriendlyByteBuf, ExcludedPlayersPayload> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public void encode(FriendlyByteBuf buf, ExcludedPlayersPayload p) {
                    buf.writeBoolean(p.authorized);
                    // 编码前先截到 MAX_ENTRIES：名单过长时解码端会抛异常，客户端连排除列表都刷新不出来。
                    int count = Math.min(p.players.size(), MAX_ENTRIES);
                    buf.writeVarInt(count);
                    for (int i = 0; i < count; i++) {
                        ExcludedPlayerData entry = p.players.get(i);
                        buf.writeUUID(entry.uuid());
                        // 名字超过上限时 writeUtf 会抛 EncoderException，先截断再写。
                        buf.writeUtf(truncate(entry.name(), MAX_STRING_LENGTH), MAX_STRING_LENGTH);
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
                        list.add(new ExcludedPlayerData(uuid, buf.readUtf(MAX_STRING_LENGTH)));
                    }
                    return new ExcludedPlayersPayload(authorized, list);
                }
            };

    /** 超长字符串先截断到上限：writeUtf 超限会抛 EncoderException，整个包都发不出去。 */
    private static String truncate(String value, int maxLength) {
        return value != null && value.length() > maxLength ? value.substring(0, maxLength) : value;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(ExcludedPlayersPayload payload, IPayloadContext context) {
        ClientHooks.handleClientPayload(payload, context);
    }
}