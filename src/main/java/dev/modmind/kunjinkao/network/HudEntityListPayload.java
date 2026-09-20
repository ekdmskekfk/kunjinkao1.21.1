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

    public static final StreamCodec<FriendlyByteBuf, HudEntityListPayload> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public void encode(FriendlyByteBuf buf, HudEntityListPayload p) {
                    buf.writeBoolean(p.authorized);
                    buf.writeVarInt(p.entities.size());
                    for (HudEntityData e : p.entities) {
                        buf.writeUUID(e.uuid());
                        buf.writeVarInt(e.entityId());
                        buf.writeUtf(e.dimensionId(), 128);
                        buf.writeUtf(e.typeTranslationKey(), 128);
                        buf.writeUtf(e.displayName(), 128);
                        buf.writeDouble(e.x());
                        buf.writeDouble(e.y());
                        buf.writeDouble(e.z());
                    }
                }

                @Override
                public HudEntityListPayload decode(FriendlyByteBuf buf) {
                    boolean authorized = buf.readBoolean();
                    int count = buf.readVarInt();
                    if (count < 0 || count > 16384) throw new IllegalArgumentException("Invalid HUD entity list size: " + count);
                    List<HudEntityData> list = new ArrayList<>(count);
                    for (int i = 0; i < count; i++) {
                        UUID uuid = buf.readUUID();
                        int id = buf.readVarInt();
                        String dim = buf.readUtf(128);
                        String type = buf.readUtf(128);
                        String name = buf.readUtf(128);
                        list.add(new HudEntityData(uuid, id, dim, type, name, buf.readDouble(), buf.readDouble(), buf.readDouble()));
                    }
                    return new HudEntityListPayload(authorized, list);
                }
            };

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(HudEntityListPayload payload, IPayloadContext context) {
        ClientHooks.handleClientPayload(payload, context);
    }
}
