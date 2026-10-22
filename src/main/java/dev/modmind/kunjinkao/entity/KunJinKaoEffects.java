package dev.modmind.kunjinkao.entity;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/** 状态效果注册表。目前只有「骚臭」一个。 */
public final class KunJinKaoEffects {

    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, KunJinKaoEntry.MOD_ID);

    public static final DeferredHolder<MobEffect, StenchEffect> STENCH =
            EFFECTS.register("stench", StenchEffect::new);

    private KunJinKaoEffects() {
    }

    public static void register(IEventBus modEventBus) {
        EFFECTS.register(modEventBus);
    }
}
