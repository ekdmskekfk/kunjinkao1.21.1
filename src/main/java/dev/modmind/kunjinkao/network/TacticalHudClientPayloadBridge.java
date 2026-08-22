package dev.modmind.kunjinkao.network;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/** Common-side S2C handoff. The physical client drains this queue from its tick listener. */
public final class TacticalHudClientPayloadBridge {

    private static final ConcurrentLinkedQueue<CustomPacketPayload> PENDING = new ConcurrentLinkedQueue<>();

    private TacticalHudClientPayloadBridge() {
    }

    public static void enqueue(CustomPacketPayload payload) {
        PENDING.add(payload);
    }

    public static List<CustomPacketPayload> drain() {
        List<CustomPacketPayload> payloads = new ArrayList<>();
        for (CustomPacketPayload payload; (payload = PENDING.poll()) != null;) {
            payloads.add(payload);
        }
        return payloads;
    }
}
