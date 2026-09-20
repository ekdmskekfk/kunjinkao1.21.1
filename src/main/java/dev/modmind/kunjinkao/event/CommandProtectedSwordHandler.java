package dev.modmind.kunjinkao.event;

import dev.modmind.kunjinkao.KunJinKaoSwordItem;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.CommandEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.bus.api.SubscribeEvent;

/**
 * Preserves administrator swords across server-command execution.
 *
 * <p>Vanilla's {@code /clear}, {@code /item} and {@code /data} commands edit
 * player inventories directly, so an item class cannot veto the removal.  We
 * therefore take a server-side copy immediately before each Brigadier command
 * runs and restore any missing swords at the end of that server tick.</p>
 */
public final class CommandProtectedSwordHandler {
    private static final Map<ServerPlayer, List<ItemStack>> PENDING_RESTORES = new IdentityHashMap<>();

    private CommandProtectedSwordHandler() {
    }

    @SubscribeEvent
    public static void beforeCommand(CommandEvent event) {
        MinecraftServer server = event.getParseResults().getContext().getSource().getServer();
        if (server == null) {
            return;
        }

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            // Several commands may run in one tick. Keep the first, pre-command
            // snapshot so a preceding /clear cannot erase the backup itself.
            PENDING_RESTORES.computeIfAbsent(player, CommandProtectedSwordHandler::copySwords);
        }
        PENDING_RESTORES.entrySet().removeIf(entry -> entry.getValue().isEmpty());
    }

    @SubscribeEvent
    public static void afterCommands(ServerTickEvent.Post event) {
        if (PENDING_RESTORES.isEmpty()) {
            return;
        }

        for (Map.Entry<ServerPlayer, List<ItemStack>> entry : PENDING_RESTORES.entrySet()) {
            ServerPlayer player = entry.getKey();
            if (!player.isRemoved()) {
                restoreMissingSwords(player, entry.getValue());
            }
        }
        PENDING_RESTORES.clear();
    }

    private static List<ItemStack> copySwords(ServerPlayer player) {
        List<ItemStack> copies = new ArrayList<>();
        copySwords(player.getInventory().items, copies);
        copySwords(player.getInventory().armor, copies);
        copySwords(player.getInventory().offhand, copies);
        return copies;
    }

    private static void copySwords(Iterable<ItemStack> source, List<ItemStack> copies) {
        for (ItemStack stack : source) {
            if (stack.getItem() instanceof KunJinKaoSwordItem) {
                copies.add(stack.copy());
            }
        }
    }

    private static void restoreMissingSwords(ServerPlayer player, List<ItemStack> originals) {
        int missing = originals.size() - countSwords(player);
        for (int index = 0; index < missing; index++) {
            ItemStack restored = originals.get(index).copy();
            if (!player.getInventory().add(restored)) {
                player.drop(restored, false);
            }
        }
    }

    private static int countSwords(ServerPlayer player) {
        int count = 0;
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() instanceof KunJinKaoSwordItem) {
                count++;
            }
        }
        for (ItemStack stack : player.getInventory().armor) {
            if (stack.getItem() instanceof KunJinKaoSwordItem) {
                count++;
            }
        }
        for (ItemStack stack : player.getInventory().offhand) {
            if (stack.getItem() instanceof KunJinKaoSwordItem) {
                count++;
            }
        }
        return count;
    }
}
