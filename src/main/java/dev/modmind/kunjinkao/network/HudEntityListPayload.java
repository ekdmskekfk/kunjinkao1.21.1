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

public record HudEntityListPayload(boolean authorized, List<HudEntityData> entities) implements CustomPacketPayload {

    public static final Type<HudEntityListPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "hud_entity_list"));

    /** 编解码共用的条数上限：编码端先截断，解码端复用同一常量校验，避免两侧上限不对称。 */
    public static final int MAX_ENTRIES = 16384;

    /** 字符串字段的字符数上限（与 buf.writeUtf / readUtf 的限制保持一致）。 */
    private static final int MAX_STRING_LENGTH = 128;

    public static final StreamCodec<FriendlyByteBuf, HudEntityListPayload> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public void encode(FriendlyByteBuf buf, HudEntityListPayload p) {
                    buf.writeBoolean(p.authorized);
                    // 编码前先截到 MAX_ENTRIES：否则实体总数超过上限时，客户端解码会抛
                    // IllegalArgumentException 导致整个实体列表都打不开（编码端不能只依赖解码端报错）。
                    int count = Math.min(p.entities.size(), MAX_ENTRIES);
                    buf.writeVarInt(count);
                    for (int i = 0; i < count; i++) {
                        HudEntityData e = p.entities.get(i);
                        buf.writeUUID(e.uuid());
                        buf.writeVarInt(e.entityId());
                        // writeUtf 第二个参数是字符数上限，超长会直接抛 EncoderException 把包卡住；
                        // 维度 id / 翻译键 / 显示名都可能超长（自定义实体尤其明显），先截断再写。
                        buf.writeUtf(truncate(e.dimensionId(), MAX_STRING_LENGTH), MAX_STRING_LENGTH);
                        buf.writeUtf(truncate(e.typeTranslationKey(), MAX_STRING_LENGTH), MAX_STRING_LENGTH);
                        buf.writeUtf(truncate(e.displayName(), MAX_STRING_LENGTH), MAX_STRING_LENGTH);
                        buf.writeDouble(e.x());
                        buf.writeDouble(e.y());
                        buf.writeDouble(e.z());
                    }
                }

                @Override
                public HudEntityListPayload decode(FriendlyByteBuf buf) {
                    boolean authorized = buf.readBoolean();
                    int count = buf.readVarInt();
                    if (count < 0 || count > MAX_ENTRIES) throw new IllegalArgumentException("Invalid HUD entity list size: " + count);
                    List<HudEntityData> list = new ArrayList<>(count);
                    for (int i = 0; i < count; i++) {
                        UUID uuid = buf.readUUID();
                        int id = buf.readVarInt();
                        String dim = buf.readUtf(MAX_STRING_LENGTH);
                        String type = buf.readUtf(MAX_STRING_LENGTH);
                        String name = buf.readUtf(MAX_STRING_LENGTH);
                        list.add(new HudEntityData(uuid, id, dim, type, name, buf.readDouble(), buf.readDouble(), buf.readDouble()));
                    }
                    return new HudEntityListPayload(authorized, list);
                }
            };

    /** 超长字符串先截断到上限：writeUtf 超限会抛 EncoderException，整个包都发不出去。 */
    private static String truncate(String value, int maxLength) {
        return value != null && value.length() > maxLength ? value.substring(0, maxLength) : value;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(HudEntityListPayload payload, IPayloadContext context) {
        ClientHooks.handleClientPayload(payload, context);
    }
}
