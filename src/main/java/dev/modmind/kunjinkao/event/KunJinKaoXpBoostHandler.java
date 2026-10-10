package dev.modmind.kunjinkao.event;

import dev.modmind.kunjinkao.config.AdminToolConfig;
import dev.modmind.kunjinkao.qol.QolSetting;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;

/**
 * 经验飞涨（功能 72）：击杀获得的经验乘一个服务器级倍率。
 * <p>
 * <b>只覆盖"击杀"这一路。</b> 挖矿与烧炼的经验走的是完全不同的路径
 * （`Block.popExperience` / 熔炉自己的结算），不在 `LivingExperienceDropEvent` 上，
 * 本版没有接 —— 与其在三个地方各写一遍、还得逐个处理时运与熔炉经验的口径，
 * 不如先把参考实现覆盖的那一路做对。
 * <p>
 * 倍率存在 {@code AdminToolConfig [qol] xpBoostMultiplier}，1 = 原版，
 * 由 OP 在功能面板里调或直接编辑 toml。
 */
public final class KunJinKaoXpBoostHandler {

    /**
     * 单次掉落经验的上限。
     * <p>
     * 原版一次掉落远到不了这个量级，留它是为了挡住"服主把倍率手改成很大 + 某个模组给了巨额基础经验"
     * 时 {@code int} 溢出成负数 —— 那会让玩家掉经验而不是得经验。
     */
    private static final int MAX_DROPPED_EXPERIENCE = 1_000_000;

    @SubscribeEvent
    public void onExperienceDrop(LivingExperienceDropEvent event) {
        int multiplier = AdminToolConfig.qolServerValue(QolSetting.XP_BOOST);
        if (multiplier <= 1) {
            return;
        }
        // 只认"玩家击杀"：getAttackingPlayer 为空的是自然死亡、环境伤害、假玩家机器。
        // 后两者不该吃这个倍率 —— 机器击杀是 119 机器共鸣单独一项（M3），
        // 在这里顺手一起放大等于把两项功能悄悄合并了。
        if (event.getAttackingPlayer() == null) {
            return;
        }
        int original = event.getOriginalExperience();
        if (original <= 0) {
            return;
        }
        long scaled = (long) original * multiplier;
        event.setDroppedExperience((int) Math.min(MAX_DROPPED_EXPERIENCE, scaled));
    }
}