package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.event.KunJinKaoColdDataEffectsHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * 合并后的剑设置 C2S 包：把原来 9 个"形状相同"的包（5 个 (hand, int) + 4 个 (hand, boolean)）
 * 收敛到这一条线格式，由 {@link #settingId()} 判别具体设置项。
 * <p>
 * 线格式：{@code writeEnum(hand) | writeVarInt(settingId) | writeVarInt(value)}。
 * 用 VarInt 而不是定长 int：id 与 value 都是小整数，合并后包体更小；开关类设置统一编码为 0/1
 * （{@link #value()} != 0），使 9 个设置项共用同一条解码路径。
 * <p>
 * 合并的动机是消掉 9 个逐字节相同的样板类，只是为了少维护、少注册；它不改变设置的语义与权限。
 * 授权闸门刻意没有搬进这里的 handle：仍然调用 {@link KunJinKaoColdDataEffectsHandler} 原来的
 * handleXxx 方法，由它们内部的 withSword(isAuthorized) 统一校验，
 * 保证"改剑设置"依旧只对通过密码验证的玩家开放，合并后不会绕过它。
 */
public record SwordSettingPayload(InteractionHand hand, int settingId, int value) implements CustomPacketPayload {

    // ===== settingId 常量表 =====
    // 这些数值是线格式的一部分（会被写进包里），只能往后追加，绝对不能改动已有取值：
    // 一旦改动，新旧客户端/服务端会把同一个 id 解释成不同的设置项，改到别的设置上。
    public static final int MINING_SPEED = 0;
    public static final int ATTACK_DAMAGE_LIMIT = 1;
    public static final int ORE_DROP_MULTIPLIER = 2;
    public static final int LOOTING_MODE = 3;
    public static final int AREA_CLEAR_TARGET_MODE = 4;
    public static final int BLUE_SCREEN_ATTACK = 5;
    public static final int UNBREAKABLE_BLOCK_BREAKING = 6;
    public static final int ULTIMATE_DEATH = 7;
    public static final int QUIT_STRIKE = 8;

    public static final Type<SwordSettingPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "sword_setting"));

    // 读手沿用 NetworkHandler.readEnumSafe（越界序号回落 values()[0]），与原来 9 个包的读法一致。
    public static final StreamCodec<FriendlyByteBuf, SwordSettingPayload> STREAM_CODEC =
            StreamCodec.of((buf, p) -> { buf.writeEnum(p.hand); buf.writeVarInt(p.settingId); buf.writeVarInt(p.value); },
                    buf -> new SwordSettingPayload(NetworkHandler.readEnumSafe(buf, InteractionHand.class),
                            buf.readVarInt(), buf.readVarInt()));

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(SwordSettingPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            InteractionHand hand = payload.hand();
            int value = payload.value();
            switch (payload.settingId()) {
                case MINING_SPEED -> KunJinKaoColdDataEffectsHandler.handleSetSwordMiningSpeed(context.player(), hand, value);
                case ATTACK_DAMAGE_LIMIT -> KunJinKaoColdDataEffectsHandler.handleSetSwordAttackDamageLimit(context.player(), hand, value);
                case ORE_DROP_MULTIPLIER -> KunJinKaoColdDataEffectsHandler.handleSetSwordOreDropMultiplier(context.player(), hand, value);
                case LOOTING_MODE -> KunJinKaoColdDataEffectsHandler.handleSetSwordLootingMode(context.player(), hand, value);
                case AREA_CLEAR_TARGET_MODE -> KunJinKaoColdDataEffectsHandler.handleSetAreaClearTargetMode(context.player(), hand, value);
                // 以下是开关类：线格式统一是 0/1，这里还原成原来的 boolean 再交给原方法。
                case BLUE_SCREEN_ATTACK -> KunJinKaoColdDataEffectsHandler.handleToggleBlueScreenAttack(context.player(), hand, value != 0);
                case UNBREAKABLE_BLOCK_BREAKING -> KunJinKaoColdDataEffectsHandler.handleToggleUnbreakableBlockBreaking(context.player(), hand, value != 0);
                case ULTIMATE_DEATH -> KunJinKaoColdDataEffectsHandler.handleToggleUltimateDeath(context.player(), hand, value != 0);
                case QUIT_STRIKE -> KunJinKaoColdDataEffectsHandler.handleToggleQuitStrike(context.player(), hand, value != 0);
                default -> {
                    // 未知 settingId：与原来"收到自己认不出的包"一样，什么都不做。
                }
            }
        });
    }
}