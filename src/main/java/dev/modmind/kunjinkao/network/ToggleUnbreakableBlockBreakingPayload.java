package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ToggleUnbreakableBlockBreakingPayload(InteractionHand hand, boolean enabled) implements CustomPacketPayload {

    public static final Type<ToggleUnbreakableBlockBreakingPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "toggle_unbreakable_block_breaking"));

    public static final StreamCodec<FriendlyByteBuf, ToggleUnbreakableBlockBreakingPayload> STREAM_CODEC =
            StreamCodec.of((buf, p) -> { buf.writeEnum(p.hand); buf.writeBoolean(p.enabled); },
                    buf -> new ToggleUnbreakableBlockBreakingPayload(NetworkHandler.readEnumSafe(buf, InteractionHand.class), buf.readBoolean()));

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(ToggleUnbreakableBlockBreakingPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> dev.modmind.kunjinkao.event.KunJinKaoColdDataEffectsHandler.handleToggleUnbreakableBlockBreaking(
                context.player(), payload.hand, payload.enabled));
    }
}