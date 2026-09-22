package dev.modmind.kunjinkao.network;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.block.entity.AcceleratorBlockEntity;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * 加速方块的过滤配置（整份状态一次性同步）。
 * <p>
 * 为什么整份发而不是"改哪一格发哪一格"：过滤状态很小（9 个槽 + 2 个开关），
 * 整份发送天然幂等、不会出现"客户端与服务端某一格不一致"的中间态，也省掉一套增量协议。
 * <p>
 * 线格式：{@code writeBlockPos | bool whitelist | bool matchNbt | 9 × OPTIONAL_STREAM_CODEC(ItemStack)}。
 * 槽位数固定为 {@link AcceleratorBlockEntity#FILTER_SLOTS}，所以不需要长度前缀；
 * 空槽写成 EMPTY（用 OPTIONAL 编解码，因为普通 ItemStack 编解码不允许空栈）。
 * <p>
 * 服务端不信任客户端内容：{@link AcceleratorBlockEntity#applyFilter} 只接受方块物品、数量归 1。
 */
public record AcceleratorFilterPayload(BlockPos pos, List<ItemStack> filter, boolean whitelist, boolean matchNbt)
        implements CustomPacketPayload {

    public static final Type<AcceleratorFilterPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(KunJinKaoEntry.MOD_ID, "accelerator_filter"));

    public static final StreamCodec<RegistryFriendlyByteBuf, AcceleratorFilterPayload> STREAM_CODEC = StreamCodec.of(
            (buf, payload) -> {
                buf.writeBlockPos(payload.pos);
                buf.writeBoolean(payload.whitelist);
                buf.writeBoolean(payload.matchNbt);
                for (int slot = 0; slot < AcceleratorBlockEntity.FILTER_SLOTS; slot++) {
                    ItemStack entry = slot < payload.filter.size() ? payload.filter.get(slot) : ItemStack.EMPTY;
                    ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, entry);
                }
            },
            buf -> {
                BlockPos pos = buf.readBlockPos();
                boolean whitelist = buf.readBoolean();
                boolean matchNbt = buf.readBoolean();
                List<ItemStack> filter = new ArrayList<>(AcceleratorBlockEntity.FILTER_SLOTS);
                for (int slot = 0; slot < AcceleratorBlockEntity.FILTER_SLOTS; slot++) {
                    filter.add(ItemStack.OPTIONAL_STREAM_CODEC.decode(buf));
                }
                return new AcceleratorFilterPayload(pos, filter, whitelist, matchNbt);
            });

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(AcceleratorFilterPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer player)) {
                return;
            }
            // 与倍率/范围同一个闸门：授权 + 交互距离
            if (!AcceleratorBlockEntity.mayEdit(player, payload.pos)) {
                return;
            }
            Level level = player.level();
            if (level == null || !level.hasChunkAt(payload.pos)) {
                return;
            }
            if (!(level.getBlockEntity(payload.pos) instanceof AcceleratorBlockEntity accelerator)) {
                return;
            }
            if (!accelerator.applyFilter(payload.filter, payload.whitelist, payload.matchNbt)) {
                return;
            }
            accelerator.setChanged();
            BlockState state = level.getBlockState(payload.pos);
            // 复用方块更新把方块实体数据（含过滤列表）同步回客户端，GUI 据此刷新
            level.sendBlockUpdated(payload.pos, state, state, 2);
        });
    }
}