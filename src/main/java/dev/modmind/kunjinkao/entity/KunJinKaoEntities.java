package dev.modmind.kunjinkao.entity;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.SwordRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * 本模组自己的实体注册。
 * <p>
 * 目前只有一个：{@code kunjinkao:hand} —— 一只贴地蠕动的手（见 {@link KunJinKaoHandEntity}）。
 * <p>
 * <b>自然生成</b>：由 {@link #onSpawnPlacements} 提供落点规则（站在动物可生成的方块上、
 * 亮度足够），并由 {@code data/kunjinkao/neoforge/biome_modifier/add_hand_spawns.json}
 * 注入到全部主世界生物群系。分类保持 CREATURE，所以它按"动物"那一套规则生成
 * （区块生成时随群落一起产生），也不受"离玩家太远就清除"的影响。
 */
public final class KunJinKaoEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, KunJinKaoEntry.MOD_ID);

    /**
     * 君君（原名"锟斤拷之手"）。
     * <p>
     * 碰撞箱刻意压得很扁（0.7 × 0.45）：它是一只贴地爬的手，不是站着的人形，
     * 判定框高一点就会显得"浮在地上"。眼睛高度取 0.3，让它看向玩家时模型抬头幅度合理。
     * <p>
     * 分类用 CREATURE 不用 MONSTER：它现在是**被打的受害者**（不攻击、被打就跑），
     * 而且 CREATURE 类默认不按"离玩家太远"自动清除 —— 宠物语义。
     */
    public static final DeferredHolder<EntityType<?>, EntityType<KunJinKaoHandEntity>> HAND =
            ENTITY_TYPES.register("hand", () -> EntityType.Builder
                    .of(KunJinKaoHandEntity::new, MobCategory.CREATURE)
                    .sized(0.7F, 0.45F)
                    .eyeHeight(0.3F)
                    .clientTrackingRange(8)
                    .build(KunJinKaoEntry.MOD_ID + ":hand"));

    /**
     * 刷怪蛋。
     * <p>
     * 主色/副色对着照片取：肤色 {@code 0xE9C9B8}、指甲那种偏冷的白 {@code 0xF6F1E7}。
     * 放在 {@link SwordRegistry#ITEMS} 这一份物品注册表里，是为了让它和剑、加速方块、
     * 世界门一起出现在同一个创造栏里，而不是再开一个注册表。
     */
    public static final DeferredItem<Item> HAND_SPAWN_EGG = SwordRegistry.ITEMS.register(
            "hand_spawn_egg",
            () -> new SpawnEggItem(HAND.get(), 0xE9C9B8, 0xF6F1E7, new Item.Properties()));

    private KunJinKaoEntities() {
    }

    public static void register(IEventBus modEventBus) {
        ENTITY_TYPES.register(modEventBus);
        // 属性挂在 mod 总线上：EntityAttributeCreationEvent 是注册期事件，
        // 晚于它再 put 就不会生效了（实体一生成就会因为拿不到属性表而崩）。
        modEventBus.addListener(KunJinKaoEntities::onAttributes);
        // 自然生成的落点规则。生物群系一侧由 data/kunjinkao/neoforge/biome_modifier 的
        // add_hand_spawns.json 注入，两者缺一不可：没有 placement 就不会被选中，
        // 没有 biome modifier 则生物群系的刷怪表里根本没有这个类型。
        modEventBus.addListener(KunJinKaoEntities::onSpawnPlacements);
    }

    /**
     * 君君的落点规则：站在"动物可生成"的方块上，且光照足够亮。
     *
     * <p>照搬原版动物的判据（草/泥土类方块 + 亮度 > 8）：它是 CREATURE 分类，
     * 用动物那一套才不会在洞里、水底、岩浆边上冒出来。
     * 用 {@code MOTION_BLOCKING_NO_LEAVES} 而不是 {@code WORLD_SURFACE}：
     * 后者会把树叶也算成地面，导致它生成在树冠上。</p>
     */
    private static void onSpawnPlacements(net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent event) {
        event.register(HAND.get(),
                net.minecraft.world.entity.SpawnPlacementTypes.ON_GROUND,
                net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (type, level, reason, pos, random) ->
                        level.getBlockState(pos.below()).is(net.minecraft.tags.BlockTags.ANIMALS_SPAWNABLE_ON)
                                && level.getRawBrightness(pos, 0) > 8,
                net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }

    private static void onAttributes(EntityAttributeCreationEvent event) {
        event.put(HAND.get(), KunJinKaoHandEntity.createAttributes().build());
    }
}