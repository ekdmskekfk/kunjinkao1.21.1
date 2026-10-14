/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.net;

import cyou.rimuru.shootingstardemo.mc1211.net.a;
import cyou.rimuru.shootingstardemo.mc1211.net.b;
import cyou.rimuru.shootingstardemo.mc1211.net.c;
import cyou.rimuru.shootingstardemo.mc1211.net.e;
import java.util.function.Consumer;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class d {
    private static final String d = "2";
    public static Consumer<e> a = e2 -> {};
    public static Consumer<b> b = b2 -> {};
    public static Consumer<c> c = c2 -> {};

    private d() {
    }

    public static void a(RegisterPayloadHandlersEvent registerPayloadHandlersEvent) {
        PayloadRegistrar payloadRegistrar = registerPayloadHandlersEvent.registrar(d);
        payloadRegistrar.playToServer(cyou.rimuru.shootingstardemo.mc1211.net.a.a, cyou.rimuru.shootingstardemo.mc1211.net.a.b, (a2, iPayloadContext) -> {
            Player player = iPayloadContext.player();
            if (player instanceof ServerPlayer) {
                ServerPlayer serverPlayer = (ServerPlayer)player;
                cyou.rimuru.shootingstardemo.mc1211.magic.a.a(serverPlayer, a2.a(), a2.b(), a2.c(), a2.d());
            }
        });
        payloadRegistrar.playToClient(e.e, e.f, (e2, iPayloadContext) -> a.accept((e)e2));
        payloadRegistrar.playToClient(cyou.rimuru.shootingstardemo.mc1211.net.b.a, cyou.rimuru.shootingstardemo.mc1211.net.b.b, (b2, iPayloadContext) -> b.accept((b)b2));
        payloadRegistrar.playToClient(cyou.rimuru.shootingstardemo.mc1211.net.c.a, cyou.rimuru.shootingstardemo.mc1211.net.c.b, (c2, iPayloadContext) -> c.accept((c)c2));
    }

    public static void a(ServerPlayer serverPlayer, CustomPacketPayload customPacketPayload) {
        PacketDistributor.sendToPlayer((ServerPlayer)serverPlayer, (CustomPacketPayload)customPacketPayload, (CustomPacketPayload[])new CustomPacketPayload[0]);
    }

    public static void a(CustomPacketPayload customPacketPayload) {
        PacketDistributor.sendToServer((CustomPacketPayload)customPacketPayload, (CustomPacketPayload[])new CustomPacketPayload[0]);
    }
}

