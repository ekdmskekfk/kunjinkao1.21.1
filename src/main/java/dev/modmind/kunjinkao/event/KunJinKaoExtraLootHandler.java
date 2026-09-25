package dev.modmind.kunjinkao.event;

import dev.modmind.kunjinkao.KunJinKaoSwordItem;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;

import java.util.Map;

/**
 * 用管理员剑击杀生物时的额外掉落：斩首与刷怪蛋。
 * <p>
 * 挂在 {@link LivingDropsEvent} 上而不是 {@code LivingDeathEvent}，因为前者直接给出
 * <b>本次掉落的可变列表</b>：往里面加条目就等于"这只生物多掉了东西"，
 * 原版掉落、抢夺加成、经验与进度全都不受影响，也不需要自己生成实体。
 * <p>
 * 头颅只覆盖原版里有对应头颅的生物；没头颅的生物（比如牛）自然什么都不掉，
 * 而不是硬塞一个不存在的物品。刷怪蛋走 {@link SpawnEggItem#byId}，
 * 所以只有真正注册了刷怪蛋的生物才会掉 —— 模组生物也一样，有就掉、没有就不掉。
 */
public final class KunJinKaoExtraLootHandler {

    /** 有对应头颅的原版生物。变种（尸壳/溺尸/流浪者）沿用同一颗头。 */
    private static final Map<EntityType<?>, Item> HEADS = Map.ofEntries(
            Map.entry(EntityType.ZOMBIE, Items.ZOMBIE_HEAD),
            Map.entry(EntityType.HUSK, Items.ZOMBIE_HEAD),
            Map.entry(EntityType.DROWNED, Items.ZOMBIE_HEAD),
            Map.entry(EntityType.ZOMBIFIED_PIGLIN, Items.ZOMBIE_HEAD),
            Map.entry(EntityType.SKELETON, Items.SKELETON_SKULL),
            Map.entry(EntityType.STRAY, Items.SKELETON_SKULL),
            Map.entry(EntityType.BOGGED, Items.SKELETON_SKULL),
            Map.entry(EntityType.CREEPER, Items.CREEPER_HEAD),
            Map.entry(EntityType.WITHER_SKELETON, Items.WITHER_SKELETON_SKULL),
            Map.entry(EntityType.PIGLIN, Items.PIGLIN_HEAD),
            Map.entry(EntityType.PIGLIN_BRUTE, Items.PIGLIN_HEAD),
            Map.entry(EntityType.ENDER_DRAGON, Items.DRAGON_HEAD));

    @SubscribeEvent
    public void onLivingDrops(LivingDropsEvent event) {
        if (!(event.getEntity() instanceof LivingEntity victim) || victim.level().isClientSide()) {
            return;
        }
        Player killer = resolveKiller(event.getSource());
        if (killer == null) {
            return;
        }
        ItemStack sword = killer.getMainHandItem();
        if (!(sword.getItem() instanceof KunJinKaoSwordItem)) {
            return;
        }

        if (KunJinKaoSwordItem.isBeheadingEnabled(sword)) {
            Item head = HEADS.get(victim.getType());
            if (head != null) {
                addDrop(victim, event, new ItemStack(head));
            }
        }
        if (KunJinKaoSwordItem.isSpawnEggDropEnabled(sword)) {
            SpawnEggItem egg = SpawnEggItem.byId(victim.getType());
            if (egg != null) {
                addDrop(victim, event, new ItemStack(egg));
            }
        }
    }

    /**
     * 找出该为这一刀负责的玩家。
     * <p>
     * 先看 {@code getEntity()}（伤害的归属者），再看 {@code getDirectEntity()}
     * —— 箭矢、三叉戟这类投掷物的直接命中者是弹射物本身，归属者才是玩家。
     */
    private static Player resolveKiller(DamageSource source) {
        if (source.getEntity() instanceof Player player) {
            return player;
        }
        return source.getDirectEntity() instanceof Player player ? player : null;
    }

    private static void addDrop(LivingEntity victim, LivingDropsEvent event, ItemStack stack) {
        Level level = victim.level();
        ItemEntity item = new ItemEntity(level, victim.getX(),
                victim.getY() + victim.getEyeHeight() * 0.5D, victim.getZ(), stack);
        item.setDefaultPickUpDelay();
        event.getDrops().add(item);
    }
}