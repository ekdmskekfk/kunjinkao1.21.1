package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.clientbridge.ClientHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record OverwriteEffectPayload(int entityId, int remainingTicks, int phase, int phaseDetail,
                                     boolean hasPosition, int posX, int posY, int posZ,
                                     String terminalText, int terminalLine) implements CustomPacketPayload {

    public static final Type<OverwriteEffectPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "overwrite_effect"));

    public static final int PHASE_START = 0;
    public static final int PHASE_PROGRESS = 1;
    public static final int PHASE_END = 2;
    public static final int PHASE_CANCEL = 3;
    public static final int PHASE_DECISION = 4;

    public static final StreamCodec<FriendlyByteBuf, OverwriteEffectPayload> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public void encode(FriendlyByteBuf buf, OverwriteEffectPayload p) {
                    buf.writeInt(p.entityId);
                    buf.writeInt(p.remainingTicks);
                    buf.writeInt(p.phase);
                    buf.writeInt(p.phaseDetail);
                    buf.writeBoolean(p.hasPosition);
                    if (p.hasPosition) {
                        buf.writeInt(p.posX);
                        buf.writeInt(p.posY);
                        buf.writeInt(p.posZ);
                    }
                    buf.writeUtf(p.terminalText);
                    buf.writeInt(p.terminalLine);
                }

                @Override
                public OverwriteEffectPayload decode(FriendlyByteBuf buf) {
                    int eid = buf.readInt();
                    int ticks = buf.readInt();
                    int phase = buf.readInt();
                    int detail = buf.readInt();
                    boolean hasPos = buf.readBoolean();
                    int x = 0, y = 0, z = 0;
                    if (hasPos) {
                        x = buf.readInt();
                        y = buf.readInt();
                        z = buf.readInt();
                    }
                    String text = buf.readUtf();
                    int line = buf.readInt();
                    return new OverwriteEffectPayload(eid, ticks, phase, detail, hasPos, x, y, z, text, line);
                }
            };

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public BlockPos position() {
        return hasPosition ? new BlockPos(posX, posY, posZ) : null;
    }

    public static OverwriteEffectPayload start(int entityId, int ticks, int theme) {
        return new OverwriteEffectPayload(entityId, ticks, PHASE_START, theme, false, 0, 0, 0, "", 0);
    }

    public static OverwriteEffectPayload progress(int entityId, int ticks) {
        return new OverwriteEffectPayload(entityId, ticks, PHASE_PROGRESS, 0, false, 0, 0, 0, "", 0);
    }

    public static OverwriteEffectPayload end(int entityId, BlockPos pos) {
        return new OverwriteEffectPayload(entityId, 0, PHASE_END, 0, pos != null,
                pos != null ? pos.getX() : 0, pos != null ? pos.getY() : 0, pos != null ? pos.getZ() : 0, "", 0);
    }

    public static OverwriteEffectPayload cancel(int entityId) {
        return new OverwriteEffectPayload(entityId, 0, PHASE_CANCEL, 0, false, 0, 0, 0, "", 0);
    }

    public static OverwriteEffectPayload decision(int entityId, int ticks) {
        return new OverwriteEffectPayload(entityId, ticks, PHASE_DECISION, 0, false, 0, 0, 0, "", 0);
    }

    public static void handle(OverwriteEffectPayload payload, IPayloadContext context) {
        ClientHooks.handleClientPayload(payload, context);
    }
}
