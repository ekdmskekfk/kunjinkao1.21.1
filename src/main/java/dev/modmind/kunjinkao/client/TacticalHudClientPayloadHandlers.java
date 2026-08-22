package dev.modmind.kunjinkao.client;

import dev.modmind.kunjinkao.network.TacticalHudPayloads;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

/** S2C handlers; this class is only reached through client-bound payload dispatch. */
public final class TacticalHudClientPayloadHandlers {

    private TacticalHudClientPayloadHandlers() {
    }

    public static void handleHudState(TacticalHudPayloads.HudStatePayload payload) {
        TacticalHudClientState.receiveServerState(payload.enabled(), payload.authorized());
    }

    public static void handleEntityList(TacticalHudPayloads.EntityListPayload payload) {
        if (TacticalHudClientState.isAuthorized() && TacticalHudClientState.isEnabled()) {
            Minecraft.getInstance().setScreen(new EntityManagerScreen(payload.entities()));
        }
    }

    public static void handleActionResult(TacticalHudPayloads.EntityActionResultPayload payload) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null) minecraft.player.displayClientMessage(Component.literal(payload.message()), true);
    }
}
