package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetSwordAttackDamageLimitPayload(InteractionHand hand, int limit) implements CustomPacketPayload {

    public static final Type<SetSwordAttackDamageLimitPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "set_sword_attack_damage_limit"));

    public static final StreamCodec<FriendlyByteBuf, SetSwordAttackDamageLimitPayload> STREAM_CODEC =
            StreamCodec.of((buf, p) -> { buf.writeEnum(p.hand); buf.writeInt(p.limit); },
                    buf -> new SetSwordAttackDamageLimitPayload(NetworkHandler.readEnumSafe(buf, InteractionHand.class), buf.readInt()));

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(SetSwordAttackDamageLimitPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> dev.modmind.kunjinkao.event.KunJinKaoColdDataEffectsHandler.handleSetSwordAttackDamageLimit(
                context.player(), payload.hand, payload.limit));
    }
}