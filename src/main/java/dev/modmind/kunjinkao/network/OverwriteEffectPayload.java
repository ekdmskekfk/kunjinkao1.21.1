package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.client.KunJinKaoClientOverwriteEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class OverwriteEffectPayload implements CustomPacketPayload {

    public static final int PHASE_START = 0;
    public static final int PHASE_PROGRESS = 1;
    public static final int PHASE_END = 2;
    public static final int PHASE_CANCEL = 3;
    public static final int PHASE_DECISION = 4;

    public static final CustomPacketPayload.Type<OverwriteEffectPayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "overwrite_effect"));

    public static final StreamCodec<FriendlyByteBuf, OverwriteEffectPayload> STREAM_CODEC =
            StreamCodec.of(OverwriteEffectPayload::write, OverwriteEffectPayload::read);

    private final int entityId;
    private final int remainingTicks;
    private final int phase;
    private final int phaseDetail;
    private final boolean hasPosition;
    private final int posX;
    private final int posY;
    private final int posZ;
    private final String terminalText;
    private final int terminalLine;

    public OverwriteEffectPayload(int entityId, int remainingTicks, int phase) {
        this(entityId, remainingTicks, phase, 0, false, 0, 0, 0, "", 0);
    }

    public OverwriteEffectPayload(int entityId, int remainingTicks, int phase, int phaseDetail, boolean hasPosition, int posX, int posY, int posZ) {
        this(entityId, remainingTicks, phase, phaseDetail, hasPosition, posX, posY, posZ, "", 0);
    }

    public OverwriteEffectPayload(int entityId, int remainingTicks, int phase, int phaseDetail, boolean hasPosition, int posX, int posY, int posZ, String terminalText, int terminalLine) {
        this.entityId = entityId;
        this.remainingTicks = remainingTicks;
        this.phase = phase;
        this.phaseDetail = phaseDetail;
        this.hasPosition = hasPosition;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.terminalText = terminalText;
        this.terminalLine = terminalLine;
    }

    public static void write(FriendlyByteBuf buffer, OverwriteEffectPayload message) {
        buffer.writeInt(message.entityId);
        buffer.writeInt(message.remainingTicks);
        buffer.writeInt(message.phase);
        buffer.writeInt(message.phaseDetail);
        buffer.writeBoolean(message.hasPosition);
        if (message.hasPosition) {
            buffer.writeInt(message.posX);
            buffer.writeInt(message.posY);
            buffer.writeInt(message.posZ);
        }
        buffer.writeUtf(message.terminalText);
        buffer.writeInt(message.terminalLine);
    }

    public static OverwriteEffectPayload read(FriendlyByteBuf buffer) {
        int entityId = buffer.readInt();
        int remainingTicks = buffer.readInt();
        int phase = buffer.readInt();
        int phaseDetail = buffer.readInt();
        boolean hasPosition = buffer.readBoolean();
        int posX = 0, posY = 0, posZ = 0;
        if (hasPosition) {
            posX = buffer.readInt();
            posY = buffer.readInt();
            posZ = buffer.readInt();
        }
        String terminalText = buffer.readUtf();
        int terminalLine = buffer.readInt();
        return new OverwriteEffectPayload(entityId, remainingTicks, phase, phaseDetail, hasPosition, posX, posY, posZ, terminalText, terminalLine);
    }

    public int getPhaseDetail() {
        return phaseDetail;
    }

    public BlockPos getPosition() {
        return hasPosition ? new BlockPos(posX, posY, posZ) : null;
    }

    public String getTerminalText() {
        return terminalText;
    }

    public int getTerminalLine() {
        return terminalLine;
    }

    public static void handle(OverwriteEffectPayload message, IPayloadContext context) {
        if (!context.flow().isClientbound()) return;
        context.enqueueWork(() -> {
            switch (message.phase) {
                case PHASE_START -> KunJinKaoClientOverwriteEffects.start(message.entityId, message.remainingTicks, message.getPhaseDetail());
                case PHASE_PROGRESS -> KunJinKaoClientOverwriteEffects.update(message.entityId, message.remainingTicks);
                case PHASE_END -> KunJinKaoClientOverwriteEffects.endFlash(message.entityId, message.getPosition());
                case PHASE_CANCEL -> KunJinKaoClientOverwriteEffects.cancel(message.entityId);
                case PHASE_DECISION -> KunJinKaoClientOverwriteEffects.startDecision(message.entityId, message.remainingTicks);
                default -> {
                }
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
