package dev.modmind.kunjinkao.entity;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * 骚臭：被君君的尿液喷中后获得，持续 5 分钟。
 *
 * <p>三个效果叠在一起：</p>
 * <ol>
 *   <li><b>所有生物远离</b>：每 10 tick 把附近 16 格内的生物朝反方向推一次寻路，
 *       并清掉它们"以你为目标"的仇恨 —— 不改别人 goal 表，所以不会和别的模组打架；</li>
 *   <li><b>60 秒后臭死</b>：按效果剩余时长反推已经过了多久（不额外存 NBT，
 *       重新被喷到时计时自然重置），到点用自定义伤害类型击杀，
 *       死亡信息是「太臭了！你被臭死了。」；</li>
 *   <li><b>黄色粒子</b>：每 5 tick 在身上冒一圈土黄色粒子，让"这个人很臭"看得见。</li>
 * </ol>
 */
public class StenchEffect extends MobEffect {

    /** 效果总时长：5 分钟。 */
    public static final int DURATION_TICKS = 20 * 60 * 5;
    /** 从"被喷到"到"臭死"的时长：60 秒。 */
    public static final int DEATH_AFTER_TICKS = 20 * 60;

    /** 自定义伤害类型（data/kunjinkao/damage_type/stench.json）。 */
    public static final ResourceKey<DamageType> DAMAGE_TYPE = ResourceKey.create(
            Registries.DAMAGE_TYPE,
            ResourceLocation.fromNamespaceAndPath("kunjinkao", "stench"));

    public StenchEffect() {
        // 土黄色，和尿液粒子同一个色系。
        super(MobEffectCategory.HARMFUL, 0xE8C63A);
    }

    /** 每个 tick 都要跑：死亡计时是按 tick 累加的。 */
    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide()) {
            return true;
        }
        MobEffectInstance instance = entity.getEffect(KunJinKaoEffects.STENCH);
        if (instance == null) {
            return true;
        }
        // 剩余时长反推已经过的 tick 数：被刷新时 duration 回到满值，计时自动重来。
        int elapsed = DURATION_TICKS - instance.getDuration();

        if (elapsed >= DEATH_AFTER_TICKS) {
            // 先摘效果再打伤害：否则 hurt 里再次触发本方法会无限递归。
            entity.removeEffect(KunJinKaoEffects.STENCH);
            entity.hurt(stenchSource(entity), Float.MAX_VALUE);
            return true;
        }
        if (elapsed % 10 == 0) {
            scareNearbyMobs(entity);
        }
        if (elapsed % 5 == 0) {
            spawnStenchParticles(entity);
        }
        return true;
    }

    // ===== 臭死用的伤害来源 =====

    /** 构造「臭死」伤害来源；数据包缺失时回落到普通伤害，不至于崩。 */
    public static DamageSource stenchSource(LivingEntity entity) {
        var registry = entity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
        Holder<DamageType> holder = registry.getHolder(DAMAGE_TYPE)
                .map(h -> (Holder<DamageType>) h)
                .orElseGet(() -> registry.getHolderOrThrow(net.minecraft.world.damagesource.DamageTypes.GENERIC));
        return new DamageSource(holder, null, null);
    }

    // ===== 让生物远离 =====

    private static void scareNearbyMobs(LivingEntity stinky) {
        AABB area = stinky.getBoundingBox().inflate(16.0D);
        for (Mob mob : stinky.level().getEntitiesOfClass(Mob.class, area, m -> m != stinky && m.isAlive())) {
            if (mob.hasEffect(KunJinKaoEffects.STENCH)) {
                // 一样臭的家伙之间不用互相躲。
                continue;
            }
            if (mob.getTarget() == stinky) {
                mob.setTarget(null);
            }
            Vec3 away = mob.position().subtract(stinky.position());
            if (away.lengthSqr() < 1.0E-4D) {
                away = new Vec3(1.0D, 0.0D, 0.0D);
            }
            away = away.normalize().scale(8.0D);
            mob.getNavigation().moveTo(mob.getX() + away.x, mob.getY(), mob.getZ() + away.z, 1.4D);
        }
    }

    // ===== 粒子 =====

    private static void spawnStenchParticles(LivingEntity entity) {
        if (!(entity.level() instanceof ServerLevel serverLevel)) {
            return;
        }
        serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.FALLING_HONEY,
                entity.getX(), entity.getY() + entity.getBbHeight() * 0.6D, entity.getZ(),
                3, 0.35D, 0.25D, 0.35D, 0.0D);
    }
}
