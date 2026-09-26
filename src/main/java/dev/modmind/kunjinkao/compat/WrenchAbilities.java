package dev.modmind.kunjinkao.compat;

import net.neoforged.neoforge.common.ItemAbility;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 把"扳手类"的 {@link ItemAbility} 认出来。
 * <p>
 * 光把剑写进 {@code c:tools/wrench} 标签，只能让模组"认得出这是一把扳手"；
 * 而模组内部决定走哪条分支的，往往是它自己的 ItemAbility ——
 * 例如 Mekanism（反编译确认）：
 * <pre>
 *   if (stack.canPerformAction(MekanismItemAbilities.WRENCH_DISMANTLE)) { ... }
 *   if (stack.is(MekanismTags.Items.CONFIGURATORS)) { ... }   // = #c:tools/wrench
 * </pre>
 * 它自家的配置器两条都满足，只靠标签的外来扳手只满足后一条，
 * 于是可能走进与自家扳手不同的分支（实测现象：拆卸后的机器丢失 NBT）。
 * 这里把已知的扳手能力收集起来，供 {@code canPerformAction} 判定，
 * 让剑与模组自家的扳手走完全相同的路径。
 * <p>
 * 全部按类名反射获取，不引入对任何模组的编译期依赖。
 */
public final class WrenchAbilities {

    /** 已知的"扳手能力"宿主类：类名 -> 其中的静态 ItemAbility 字段。 */
    private static final String[][] SOURCES = {
            {"mekanism.api.MekanismItemAbilities", "WRENCH_"},
    };

    private static boolean resolved;
    private static Set<ItemAbility> abilities = Set.of();

    private WrenchAbilities() {
    }

    private static synchronized void resolve() {
        if (resolved) {
            return;
        }
        resolved = true;
        Set<ItemAbility> found = new HashSet<>();
        for (String[] source : SOURCES) {
            try {
                Class<?> holder = Class.forName(source[0]);
                for (Field field : holder.getFields()) {
                    if (field.getType() == ItemAbility.class && field.getName().startsWith(source[1])) {
                        Object value = field.get(null);
                        if (value instanceof ItemAbility ability) {
                            found.add(ability);
                        }
                    }
                }
            } catch (ReflectiveOperationException | LinkageError e) {
                // 没装这个模组：跳过即可，不是错误。
            }
        }
        abilities = Collections.unmodifiableSet(found);
    }

    /** 这个能力是否属于已知的扳手能力集合。 */
    public static boolean isWrenchAbility(ItemAbility ability) {
        if (ability == null) {
            return false;
        }
        resolve();
        return abilities.contains(ability);
    }
}