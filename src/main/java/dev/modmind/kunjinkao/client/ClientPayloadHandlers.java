package dev.modmind.kunjinkao.client;

import dev.modmind.kunjinkao.client.gui.AdminPasswordScreen;
import dev.modmind.kunjinkao.network.AdminEyeStatePayload;
import dev.modmind.kunjinkao.network.AdminPasswordResultPayload;
import dev.modmind.kunjinkao.network.ExcludedPlayersPayload;
import dev.modmind.kunjinkao.network.HudEntityActionResultPayload;
import dev.modmind.kunjinkao.network.HudEntityListPayload;
import dev.modmind.kunjinkao.network.HudShieldHitPayload;
// 4 个 HUD 状态回执包已合并为 HudStatePayload，导入一起收敛。
import dev.modmind.kunjinkao.network.HudStatePayload;
import dev.modmind.kunjinkao.network.HudTrueInvisibilityVisualPayload;
import dev.modmind.kunjinkao.network.OverwriteEffectPayload;
import dev.modmind.kunjinkao.network.SwordDrawAnimationPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/** Handles S2C payloads after client-only dispatch. */
@OnlyIn(Dist.CLIENT)
public final class ClientPayloadHandlers {
    private ClientPayloadHandlers() {
    }

    public static void handle(CustomPacketPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (payload instanceof OverwriteEffectPayload effect) {
                switch (effect.phase()) {
                    case OverwriteEffectPayload.PHASE_START -> KunJinKaoClientOverwriteEffects.start(effect.entityId(), effect.remainingTicks(), effect.phaseDetail());
                    case OverwriteEffectPayload.PHASE_PROGRESS -> KunJinKaoClientOverwriteEffects.update(effect.entityId(), effect.remainingTicks());
                    case OverwriteEffectPayload.PHASE_END -> KunJinKaoClientOverwriteEffects.endFlash(effect.entityId(), effect.position());
                    case OverwriteEffectPayload.PHASE_CANCEL -> KunJinKaoClientOverwriteEffects.cancel(effect.entityId());
                    case OverwriteEffectPayload.PHASE_DECISION -> KunJinKaoClientOverwriteEffects.startDecision(effect.entityId(), effect.remainingTicks());
                    default -> {
                    }
                }
            } else if (payload instanceof HudStatePayload state) {
                // 合并后的 HUD 状态包：按 stateId 分派到原来那四个方法，applyXxx 的逻辑一行未改。
                switch (state.stateId()) {
                    case HudStatePayload.TACTICAL_HUD -> TacticalHudClientPacketHandler.apply(state.enabled(), state.authorized());
                    case HudStatePayload.NIGHT_VISION -> TacticalHudClientPacketHandler.applyNightVision(state.enabled(), state.authorized());
                    case HudStatePayload.TRUE_INVISIBILITY -> TacticalHudClientPacketHandler.applyTrueInvisibility(state.enabled(), state.authorized());
                    case HudStatePayload.MAGNET -> TacticalHudClientPacketHandler.applyMagnet(state.enabled(), state.authorized());
                    default -> {
                        // 未知 stateId：与原来"收到认不出的包"一样，静默忽略。
                    }
                }
            } else if (payload instanceof HudEntityListPayload list) {
                TacticalHudClientPacketHandler.openEntityList(list.entities(), list.authorized());
            } else if (payload instanceof HudEntityActionResultPayload result) {
                TacticalHudClientPacketHandler.applyEntityAction(result.action(), result.entityUuid(), result.success(), result.authorized());
            } else if (payload instanceof HudTrueInvisibilityVisualPayload state) {
                TacticalHudInvisibilityVisualState.setTrueInvisible(state.playerUuid(), state.enabled());
            } else if (payload instanceof HudShieldHitPayload hit) {
                ShieldHitVisualState.trigger(hit.playerUuid(), hit.relativeImpactYaw(), hit.impactHeight());
            } else if (payload instanceof AdminEyeStatePayload eye) {
                AdminEyeVisualState.setEnabled(eye.playerUuid(), eye.enabled());
            } else if (payload instanceof SwordDrawAnimationPayload animation) {
                RemoteSwordDrawVisualState.trigger(animation.playerUuid(), animation.hand());
            } else if (payload instanceof ExcludedPlayersPayload list) {
                TacticalHudClientPacketHandler.applyExcludedList(list.players(), list.authorized());
            } else if (payload instanceof AdminPasswordResultPayload result) {
                handleAdminPasswordResult(result.authorized());
            }
        });
    }

    private static void handleAdminPasswordResult(boolean authorized) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.screen instanceof AdminPasswordScreen) {
            minecraft.setScreen(null);
        }
        if (minecraft.player != null) {
            minecraft.player.displayClientMessage(Component.literal(authorized
                    ? "§a管理员密码验证成功。"
                    : "§c密码错误，已关闭验证窗口。"), true);
        }
    }
}
