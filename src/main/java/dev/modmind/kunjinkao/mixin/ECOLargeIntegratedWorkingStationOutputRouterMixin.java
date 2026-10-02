package dev.modmind.kunjinkao.mixin;

import appeng.api.config.Actionable;
import appeng.api.stacks.AEKey;
import cn.dancingsnow.neoecoae.api.me.output.ECOCraftingOutputRouter;
import org.spongepowered.asm.mixin.Mixin;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * 给 neoecoae 的「大型集成工作站」补上 ECOCraftingOutputRouter 接口。
 *
 * <h2>为什么要这个补丁</h2>
 * 那台机器的产物投递（{@code ECOLargeIntegratedWorkingStationBlockEntity#deliverBatchOutputs}）
 * 要求 {@code this instanceof ECOCraftingOutputRouter} 成立：
 *
 * <pre>
 *   if (batch.craftingJobId != null &amp;&amp; !jobTerminated &amp;&amp; this instanceof ECOCraftingOutputRouter) {
 *       router = (ECOCraftingOutputRouter) this;
 *   } else if (batch.craftingJobId != null &amp;&amp; !jobTerminated) {
 *       return false;                        // ← 直接失败，界面显示「暂停：产物输出受阻」
 *   }
 * </pre>
 *
 * 但那台机器与它的父类都没有实现这个接口 —— 真正实现它的是被 mixin 加工过的 AE2
 * CraftingService（见 neoecoae 自带的 CraftingServiceMixin）。
 * 于是只要这批产物带着尚未结束的 craftingJobId，投递就必然返回 false，
 * 而且与 ME 网络、合成 CPU 有没有空间完全无关 —— 表现为「产物输出受阻」且无从下手。
 *
 * <h2>本补丁做什么</h2>
 * 把接口织到那台机器上，实现体转交给网格的合成服务 ——
 * 也就是作者当时想写、却写成了 {@code this} 的那个对象。
 *
 * <h2>验证记录</h2>
 * 在实例 {@code 1.21.1mod测试} 中实测确认：
 * <pre>
 *   [mixin/]: Mixing ECOLargeIntegratedWorkingStationOutputRouterMixin from kunjinkao.mixins.json
 *             into cn.dancingsnow.neoecoae.blocks.entity.ECOLargeIntegratedWorkingStationBlockEntity
 *   诊断输出: instanceof Router = true（此前恒为 false）
 * </pre>
 * 织入后机器不再停在「产物输出受阻」，可正常返回产物。
 *
 * <h2>为什么用 targets 字符串 + 反射</h2>
 * 目标类的继承链上有 lowdraglib2 的接口（ISyncPersistRPCBlockEntity），
 * 编译期带上它会凭空多一个依赖。用 {@code @Mixin(targets=...)} 的字符串形式后，
 * 目标类在编译期不会被加载，网格则通过反射获取 —— 只需要 AE2 与 neoecoae 两个 jar。
 *
 * <h2>撤除条件</h2>
 * neoecoae 作者修好这个判定后，删掉本类与 kunjinkao.mixins.json 里的这一项即可。
 * mixin 配置标了 {@code required=false}，没装 neoecoae 时整份配置静默跳过。
 */
@Mixin(targets = "cn.dancingsnow.neoecoae.blocks.entity.ECOLargeIntegratedWorkingStationBlockEntity")
public abstract class ECOLargeIntegratedWorkingStationOutputRouterMixin implements ECOCraftingOutputRouter {

    /** 反射查找一次就够。 */
    private static Method getMainNodeMethod;
    private static Method getGridMethod;
    private static Method getCraftingServiceMethod;

    @Override
    public long neoecoae$insertIntoCpuForJob(UUID jobId, AEKey key, long amount, Actionable mode) {
        Object grid = resolveGrid();
        if (grid == null) {
            return 0L;
        }
        try {
            if (getCraftingServiceMethod == null) {
                getCraftingServiceMethod = grid.getClass().getMethod("getCraftingService");
            }
            Object crafting = getCraftingServiceMethod.invoke(grid);
            // 网格的合成服务（被 neoecoae 的 CraftingServiceMixin 加工过）才是真正的 Router。
            if (crafting instanceof ECOCraftingOutputRouter router) {
                return router.neoecoae$insertIntoCpuForJob(jobId, key, amount, mode);
            }
        } catch (ReflectiveOperationException ignored) {
            // 取不到就当作放不下；上层会维持"输出受阻"，不会抛出。
        }
        return 0L;
    }

    /** 取本机所在网格；任一步失败返回 null。 */
    private Object resolveGrid() {
        try {
            if (getMainNodeMethod == null) {
                getMainNodeMethod = this.getClass().getMethod("getMainNode");
            }
            Object node = getMainNodeMethod.invoke(this);
            if (node == null) {
                return null;
            }
            if (getGridMethod == null) {
                getGridMethod = node.getClass().getMethod("getGrid");
            }
            return getGridMethod.invoke(node);
        } catch (ReflectiveOperationException ignored) {
            return null;
        }
    }
}