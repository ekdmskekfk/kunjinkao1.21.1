package dev.modmind.kunjinkao.clientbridge;

import dev.modmind.kunjinkao.block.entity.AcceleratorBlockEntity;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.function.Consumer;

/**
 * Common-side gateway for client-only behavior.
 *
 * <p>The callbacks are installed exclusively by the physical-client event
 * subscriber. This class deliberately has no dependency on the mod's
 * {@code client} package or on {@code net.minecraft.client}.</p>
 */
public final class ClientHooks {
    @FunctionalInterface
    public interface PayloadHandler {
        void handle(CustomPacketPayload payload, IPayloadContext context);
    }

    private static volatile PayloadHandler payloadHandler;
    private static volatile Consumer<AcceleratorBlockEntity> acceleratorScreenOpener;

    private ClientHooks() {
    }

    public static void registerClientHandlers(PayloadHandler handler,
                                              Consumer<AcceleratorBlockEntity> screenOpener) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            payloadHandler = handler;
            acceleratorScreenOpener = screenOpener;
        }
    }

    public static void handleClientPayload(CustomPacketPayload payload, IPayloadContext context) {
        PayloadHandler handler = payloadHandler;
        if (FMLEnvironment.dist == Dist.CLIENT && handler != null) {
            handler.handle(payload, context);
        }
    }

    public static void openAcceleratorScreen(AcceleratorBlockEntity accelerator) {
        Consumer<AcceleratorBlockEntity> opener = acceleratorScreenOpener;
        if (FMLEnvironment.dist == Dist.CLIENT && opener != null) {
            opener.accept(accelerator);
        }
    }
}
