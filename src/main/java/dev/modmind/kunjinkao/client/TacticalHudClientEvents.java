package dev.modmind.kunjinkao.client;

import dev.modmind.kunjinkao.network.NetworkHandler;
import dev.modmind.kunjinkao.network.TacticalHudClientPayloadBridge;
import dev.modmind.kunjinkao.network.TacticalHudPayloads;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.client.event.ClientTickEvent;

/** Physical-client event listener. It never opens a screen before server authorization arrives. */
public final class TacticalHudClientEvents {

    private TacticalHudClientEvents() {
    }

    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.level == null) {
            TacticalHudClientState.reset();
            return;
        }
        TacticalHudClientState.tick();
        for (CustomPacketPayload payload : TacticalHudClientPayloadBridge.drain()) {
            if (payload instanceof TacticalHudPayloads.HudStatePayload state) {
                TacticalHudClientPayloadHandlers.handleHudState(state);
            } else if (payload instanceof TacticalHudPayloads.EntityListPayload entities) {
                TacticalHudClientPayloadHandlers.handleEntityList(entities);
            } else if (payload instanceof TacticalHudPayloads.EntityActionResultPayload result) {
                TacticalHudClientPayloadHandlers.handleActionResult(result);
            }
        }
        if (TacticalHudKeyMappings.TOGGLE_HUD == null) return;
        while (TacticalHudKeyMappings.TOGGLE_HUD.consumeClick()) {
            NetworkHandler.sendToServer(new TacticalHudPayloads.ToggleHudPayload(!TacticalHudClientState.isEnabled()));
        }
    }
}
