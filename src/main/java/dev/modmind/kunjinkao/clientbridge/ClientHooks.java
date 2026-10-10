package dev.modmind.kunjinkao.clientbridge;

import dev.modmind.kunjinkao.block.entity.AcceleratorBlockEntity;
import dev.modmind.kunjinkao.qol.QolSetting;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.function.Consumer;
import java.util.function.Predicate;

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
    /**
     * 客户端本地镜像的"这一项开着吗"查询。
     * <p>
     * 存在的理由是给 mixin 用：{@code Entity#fireImmune} 这类目标在服务端与客户端都会被调到，
     * 而 mixin 类本身是 common 的，<b>不能直接引用 {@code client} 包里的类</b> ——
     * 否则专用服务器一加载这个 mixin 就会因为找不到 {@code net.minecraft.client} 而炸。
     * 所以照 payloadHandler 的老办法，由客户端装配、通过这个口子回调。
     */
    private static volatile Predicate<QolSetting> qolStateQuery;

    private ClientHooks() {
    }

    public static void registerClientHandlers(PayloadHandler handler,
                                              Consumer<AcceleratorBlockEntity> screenOpener) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            payloadHandler = handler;
            acceleratorScreenOpener = screenOpener;
        }
    }

    public static void registerQolStateQuery(Predicate<QolSetting> query) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            qolStateQuery = query;
        }
    }

    /**
     * 客户端镜像里这一项是否开着。
     * <p>
     * 专用服务器上恒为 false —— 那边不该走这个入口，服务端有自己的存档可查。
     * 客户端的值只是服务端推来的副本（{@code QolStatePayload}），不是权威判定：
     * 它决定的是"火焰动画要不要画"这类纯表现问题。
     */
    public static boolean isQolEnabledOnClient(QolSetting setting) {
        Predicate<QolSetting> query = qolStateQuery;
        return FMLEnvironment.dist == Dist.CLIENT && query != null && query.test(setting);
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
