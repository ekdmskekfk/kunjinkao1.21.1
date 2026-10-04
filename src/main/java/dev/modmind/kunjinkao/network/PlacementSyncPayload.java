package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.world.SwordPlacementRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * S2C：把整张「放置记录」表推给客户端，供菜单第二页显示。
 * <p>
 * 整表推送而不是增量：这张表天然很小（玩家手动立起来的场地，几十条量级），
 * 增量同步要额外维护版本号与重连补发，不划算。
 * <p>
 * 线格式：{@code writeVarInt(count)}，随后每条
 * {@code writeVarInt(kind) | writeResourceLocation(dimension) | writeBlockPos(pos) | writeVarInt(radius) | writeUUID(owner)}。
 */
public record PlacementSyncPayload(List<Entry> entries) implements CustomPacketPayload {

    /** 客户端展示用的一条记录。owner 也带上，界面可以标出"这是你自己的场"。 */
    public record Entry(int kind, ResourceLocation dimension, BlockPos pos, int radius, int multiplier, UUID owner) {
    }

    /** 客户端收到的最后一份表。菜单第二页直接读它，不再自己发请求。 */
    private static volatile List<Entry> clientCache = List.of();

    public static List<Entry> clientEntries() {
        return clientCache;
    }

    public static final Type<PlacementSyncPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "placement_sync"));

    public static final StreamCodec<FriendlyByteBuf, PlacementSyncPayload> STREAM_CODEC =
            StreamCodec.of(
                    (buf, p) -> {
                        buf.writeVarInt(p.entries.size());
                        for (Entry entry : p.entries) {
                            buf.writeVarInt(entry.kind());
                            buf.writeResourceLocation(entry.dimension());
                            buf.writeBlockPos(entry.pos());
                            buf.writeVarInt(entry.radius());
                            buf.writeVarInt(entry.multiplier());
                            buf.writeUUID(entry.owner());
                        }
                    },
                    buf -> {
                        int count = buf.readVarInt();
                        List<Entry> list = new ArrayList<>(count);
                        for (int i = 0; i < count; i++) {
                            list.add(new Entry(buf.readVarInt(), buf.readResourceLocation(),
                                    buf.readBlockPos(), buf.readVarInt(), buf.readVarInt(), buf.readUUID()));
                        }
                        return new PlacementSyncPayload(list);
                    });

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    /** 服务端：把当前表推给一个人。 */
    public static void sendTo(Player player) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) {
            return;
        }
        PacketDistributor.sendToPlayer(serverPlayer, of(server));
    }

    /** 服务端：把当前表推给所有人（有人立起/收回场地时调）。 */
    public static void broadcast(MinecraftServer server) {
        PacketDistributor.sendToAllPlayers(of(server));
    }

    private static PlacementSyncPayload of(MinecraftServer server) {
        List<Entry> list = new ArrayList<>();
        for (SwordPlacementRegistry.Entry entry : SwordPlacementRegistry.get(server).entries()) {
            list.add(new Entry(entry.kind(), entry.dimension().location(), entry.pos(),
                    entry.radius(), entry.multiplier(), entry.owner()));
        }
        return new PlacementSyncPayload(list);
    }

    public static void handle(PlacementSyncPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            clientCache = List.copyOf(payload.entries());
            net.minecraft.client.Minecraft.getInstance().execute(() -> {
                // 菜单开着就立刻重画，免得看到的是上一次的表。
                if (net.minecraft.client.Minecraft.getInstance().screen
                        instanceof dev.modmind.kunjinkao.client.gui.SwordOptionsScreen screen) {
                    screen.onPlacementSync();
                }
            });
        });
    }
}