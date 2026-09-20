package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

/** 管理员在 HUD 排除列表里解除某个玩家的排除。 */
public record ManageExcludedPlayerPayload(UUID playerUuid) implements CustomPacketPayload {

    public static final Type<ManageExcludedPlayerPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "manage_excluded_player"));

    public static final StreamCodec<FriendlyByteBuf, ManageExcludedPlayerPayload> STREAM_CODEC =
            StreamCodec.of((buf, p) -> buf.writeUUID(p.playerUuid),
                    buf -> new ManageExcludedPlayerPayload(buf.readUUID()));

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(ManageExcludedPlayerPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> dev.modmind.kunjinkao.event.KunJinKaoColdDataEffectsHandler.handlePardonExcludedPlayer(
                net.neoforged.neoforge.server.ServerLifecycleHooks.getCurrentServer(), context.player(), payload.playerUuid));
    }
}