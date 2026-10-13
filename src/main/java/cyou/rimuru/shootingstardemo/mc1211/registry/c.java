/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;

public final class c {
    public static final ResourceKey<DamageType> a = c.a("shooting_star");
    public static final ResourceKey<DamageType> b = c.a("shooting_star_hit");

    private c() {
    }

    private static ResourceKey<DamageType> a(String string) {
        return ResourceKey.create((ResourceKey)Registries.DAMAGE_TYPE, (ResourceLocation)ResourceLocation.fromNamespaceAndPath((String)"shooting_star_demo", (String)string));
    }

    public static DamageSource a(ServerLevel serverLevel, ResourceKey<DamageType> resourceKey, Entity entity) {
        return new DamageSource((Holder)serverLevel.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(resourceKey), entity, entity);
    }
}

