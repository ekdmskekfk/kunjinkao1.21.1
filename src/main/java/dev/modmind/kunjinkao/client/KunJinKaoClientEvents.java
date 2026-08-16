package dev.modmind.kunjinkao.client;

import dev.modmind.kunjinkao.KunJinKaoSwordItem;
import dev.modmind.kunjinkao.KunJinKaoTheme;
import dev.modmind.kunjinkao.network.NetworkHandler;
import dev.modmind.kunjinkao.network.ToggleDisguisePayload;
import dev.modmind.kunjinkao.network.ToggleOverwritePayload;
import dev.modmind.kunjinkao.network.ToggleThemePayload;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomModelData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KunJinKaoClientEvents {

    private static final Logger LOGGER = LoggerFactory.getLogger(KunJinKaoClientEvents.class);

    private static boolean playerTickLogged = false;

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof Player player && player.level().isClientSide()) {
            if (!playerTickLogged) {
                playerTickLogged = true;
                LOGGER.info("[KunJinKao] Client player tick handler active");
            }
            KunJinKaoClientOverwriteEffects.update();
            KunJinKaoKeyBindings.tick();
        }
    }

    public static void onOverwriteToggle() {
        Player player = Minecraft.getInstance().player;
        InteractionHand hand = findSwordHand(player);
        LOGGER.info("[KunJinKao] O key pressed, hand={}", hand);
        if (hand != null) {
            NetworkHandler.sendToServer(new ToggleOverwritePayload(hand));
        }
    }

    public static void onDisguiseToggle() {
        Player player = Minecraft.getInstance().player;
        InteractionHand hand = findSwordHand(player);
        LOGGER.info("[KunJinKao] G key pressed, hand={}", hand);
        if (hand != null) {
            NetworkHandler.sendToServer(new ToggleDisguisePayload(hand));
        }
    }

    public static void onThemeToggle() {
        Player player = Minecraft.getInstance().player;
        InteractionHand hand = findSwordHand(player);
        LOGGER.info("[KunJinKao] T key pressed, hand={}", hand);
        if (hand != null) {
            ItemStack stack = player.getItemInHand(hand);
            int next = KunJinKaoSwordItem.getTheme(stack) + 1;
            NetworkHandler.sendToServer(new ToggleThemePayload(hand, Math.floorMod(next, KunJinKaoTheme.COUNT)));
        }
    }

    private static InteractionHand findSwordHand(Player player) {
        if (player == null) {
            return null;
        }
        ItemStack main = player.getMainHandItem();
        if (!main.isEmpty() && main.getItem() instanceof KunJinKaoSwordItem) {
            return InteractionHand.MAIN_HAND;
        }
        ItemStack off = player.getOffhandItem();
        if (!off.isEmpty() && off.getItem() instanceof KunJinKaoSwordItem) {
            return InteractionHand.OFF_HAND;
        }
        return null;
    }

    public static void onPlayerConnect(Player player) {
        KunJinKaoClientOverwriteEffects.cancel();
    }

    public static void onPlayerDisconnect(Player player) {
        KunJinKaoClientOverwriteEffects.cancel();
    }

    public static void register() {
        NeoForge.EVENT_BUS.register(new KunJinKaoClientEvents());
    }
}
