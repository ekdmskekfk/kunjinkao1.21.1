package dev.modmind.kunjinkao.event;

import dev.modmind.kunjinkao.tactical.common.SwordPresence;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

/** Refreshes the sword's quiet saturation effect only when its remaining time is low. */
public final class SwordSaturationHandler {
    private static final int DURATION_TICKS = 300;
    private static final int REFRESH_AT_OR_BELOW_TICKS = 20;

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player) || player.isSpectator()) return;
        if (!SwordPresence.hasRealSwordInInventory(player)) return;
        MobEffectInstance saturation = player.getEffect(MobEffects.SATURATION);
        if (saturation == null || saturation.getDuration() <= REFRESH_AT_OR_BELOW_TICKS) {
            player.addEffect(new MobEffectInstance(MobEffects.SATURATION, DURATION_TICKS, 0, false, false, false));
        }
    }
}
