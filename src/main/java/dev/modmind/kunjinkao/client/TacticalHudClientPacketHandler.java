package dev.modmind.kunjinkao.client;

import dev.modmind.kunjinkao.client.function.HudFunctionManager;
import dev.modmind.kunjinkao.client.hud.HudScreen;
import dev.modmind.kunjinkao.client.hud.HudEntityScreen;
import dev.modmind.kunjinkao.client.hud.HudExclusionScreen;
import dev.modmind.kunjinkao.network.ExcludedPlayerData;
import dev.modmind.kunjinkao.network.HudEntityAction;
import dev.modmind.kunjinkao.network.HudEntityData;

import java.util.List;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

/** S2C 战术 HUD 状态处理器。该类只会在客户端收到回包时调用。 */
public final class TacticalHudClientPacketHandler {

    private TacticalHudClientPacketHandler() {
    }

    public static void apply(boolean enabled, boolean authorized) {
        if (!authorized) {
            ClientHudState.reset();
            HudFunctionManager.deactivateAll();
            closeHudScreen();
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.displayClientMessage(
                        Component.translatable("message.kunjinkao.hud_not_authorized"), true);
            }
            return;
        }

        if (enabled) {
            ClientHudState.enable();
            HudFunctionManager.activateDefault();
            Minecraft.getInstance().setScreen(new HudScreen());
        } else {
            ClientHudState.disable();
            HudFunctionManager.deactivateAll();
            closeHudScreen();
        }
    }

    public static void applyNightVision(boolean enabled, boolean authorized) {
        HudFunctionManager.setNightVisionEnabled(enabled && authorized);
        if (Minecraft.getInstance().player == null) {
            return;
        }
        if (!authorized) {
            Minecraft.getInstance().player.displayClientMessage(
                    Component.translatable("message.kunjinkao.hud_not_authorized"), true);
            return;
        }
        Minecraft.getInstance().player.displayClientMessage(Component.translatable(
                enabled ? "message.kunjinkao.hud_night_vision_enabled" : "message.kunjinkao.hud_night_vision_disabled"), true);
    }

    public static void applyTrueInvisibility(boolean enabled, boolean authorized) {
        HudFunctionManager.setTrueInvisibilityEnabled(enabled && authorized);
        if (Minecraft.getInstance().player == null) {
            return;
        }
        TacticalHudInvisibilityVisualState.setTrueInvisible(Minecraft.getInstance().player.getUUID(), enabled && authorized);
        if (!authorized) {
            Minecraft.getInstance().player.displayClientMessage(
                    Component.translatable("message.kunjinkao.hud_not_authorized"), true);
            return;
        }
        Minecraft.getInstance().player.displayClientMessage(Component.translatable(
                enabled ? "message.kunjinkao.hud_true_invisibility_enabled"
                        : "message.kunjinkao.hud_true_invisibility_disabled"), true);
    }

    public static void applyMagnet(boolean enabled, boolean authorized) {
        HudFunctionManager.setMagnetEnabled(enabled && authorized);
        if (Minecraft.getInstance().player == null) {
            return;
        }
        if (!authorized) {
            Minecraft.getInstance().player.displayClientMessage(
                    Component.translatable("message.kunjinkao.hud_not_authorized"), true);
            return;
        }
        Minecraft.getInstance().player.displayClientMessage(Component.translatable(
                enabled ? "message.kunjinkao.hud_magnet_enabled" : "message.kunjinkao.hud_magnet_disabled"), true);
    }

    public static void openEntityList(List<HudEntityData> entities, boolean authorized) {
        if (!authorized) {
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.displayClientMessage(
                        Component.translatable("message.kunjinkao.hud_not_authorized"), true);
            }
            return;
        }
        Minecraft.getInstance().setScreen(new HudEntityScreen(entities));
    }

    public static void applyEntityAction(HudEntityAction action, UUID entityUuid, boolean success, boolean authorized) {
        Minecraft minecraft = Minecraft.getInstance();
        if (!authorized) {
            if (minecraft.player != null) {
                minecraft.player.displayClientMessage(Component.translatable("message.kunjinkao.hud_not_authorized"), true);
            }
            return;
        }
        if (minecraft.screen instanceof HudEntityScreen screen) {
            screen.applyActionResult(action, entityUuid, success);
        }
        if (minecraft.player != null) {
            minecraft.player.displayClientMessage(Component.translatable(success
                    ? (action == HudEntityAction.KILL ? "message.kunjinkao.entity_killed" : "message.kunjinkao.entity_teleported")
                    : "message.kunjinkao.entity_action_failed"), true);
        }
    }

    /**
     * 排除列表：既用于首次打开，也用于解除之后的名单刷新。
     * 列表内容始终以服务端回传为准。
     */
    public static void applyExcludedList(List<ExcludedPlayerData> players, boolean authorized) {
        if (!authorized) {
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.displayClientMessage(
                        Component.translatable("message.kunjinkao.hud_not_authorized"), true);
            }
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.screen instanceof HudExclusionScreen screen) {
            screen.replacePlayers(players);
        } else {
            minecraft.setScreen(new HudExclusionScreen(players));
        }
    }

    private static void closeHudScreen() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.screen instanceof HudScreen || minecraft.screen instanceof HudEntityScreen
                || minecraft.screen instanceof HudExclusionScreen) {
            minecraft.setScreen(null);
        }
    }
}
