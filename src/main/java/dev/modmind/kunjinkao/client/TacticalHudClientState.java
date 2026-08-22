package dev.modmind.kunjinkao.client;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

/** Client display state. Authorization and enabled state are set only by S2C packets. */
public final class TacticalHudClientState {

    private static boolean authorized;
    private static boolean enabled;
    private static float animationProgress;

    private TacticalHudClientState() {
    }

    public static boolean isAuthorized() { return authorized; }
    public static boolean isEnabled() { return enabled; }
    public static float animationProgress() { return animationProgress; }

    public static void receiveServerState(boolean serverEnabled, boolean serverAuthorized) {
        authorized = serverAuthorized;
        enabled = serverAuthorized && serverEnabled;
        Minecraft minecraft = Minecraft.getInstance();
        if (!authorized) {
            if (minecraft.player != null) {
                minecraft.player.displayClientMessage(Component.translatable("message.kunjinkao.tactical_denied"), true);
            }
            if (minecraft.screen instanceof TacticalHudScreen || minecraft.screen instanceof EntityManagerScreen) minecraft.setScreen(null);
        } else if (enabled && !(minecraft.screen instanceof TacticalHudScreen) && !(minecraft.screen instanceof EntityManagerScreen)) {
            minecraft.setScreen(new TacticalHudScreen());
        } else if (!enabled && (minecraft.screen instanceof TacticalHudScreen || minecraft.screen instanceof EntityManagerScreen)) {
            minecraft.setScreen(null);
        }
    }

    public static void tick() {
        float target = enabled ? 1.0F : 0.0F;
        animationProgress += (target - animationProgress) * 0.25F;
    }

    public static void reset() {
        authorized = false;
        enabled = false;
        animationProgress = 0.0F;
    }
}
