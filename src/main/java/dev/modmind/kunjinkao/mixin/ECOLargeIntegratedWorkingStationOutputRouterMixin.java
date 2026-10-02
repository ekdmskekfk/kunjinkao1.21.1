package dev.modmind.kunjinkao.mixin;

import appeng.api.config.Actionable;
import appeng.api.stacks.AEKey;
import cn.dancingsnow.neoecoae.api.me.output.ECOCraftingOutputRouter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * 给 neoecoae 的「大型集成工作站」补上 ECOCraftingOutputRouter 接口，并附带诊断日志。
 *
 * <h2>问题背景</h2>
 * 那台机器的产物投递（deliverBatchOutputs）要求 {@code this instanceof ECOCraftingOutputRouter}
 * 成立，否则在"合成任务尚未结束"时直接 return false —— 显示「暂停：产物输出受阻」。
 * 但那台机器与它的父类都没有实现这个接口，真正实现它的是被 mixin 加工过的 AE2
 * CraftingService（见 neoecoae 自带的 CraftingServiceMixin）。
 *
 * <h2>本补丁</h2>
 * 把接口织到那台机器上，实现体转发给网格的合成服务。
 *
 * <h2>诊断日志（定位仍在受阻的原因）</h2>
 * 已在实例日志中确认本 mixin 确实织入（Mixing ... into ...），但现象依旧，
 * 说明还有第二个原因。因此这里打印两类信息：
 * <ul>
 *   <li>每次 {@code setPauseReason} 被调用时，打印原因名与调用栈前几帧 —— 直接指出是哪条路径在设暂停；</li>
 *   <li>每次转发 {@code insertIntoCpuForJob} 时，打印 jobId、物品、请求量与实际接受的量。</li>
 * </ul>
 * 定位完成后删掉这两处日志即可（或整个补丁）。
 */
@Mixin(targets = "cn.dancingsnow.neoecoae.blocks.entity.ECOLargeIntegratedWorkingStationBlockEntity")
public abstract class ECOLargeIntegratedWorkingStationOutputRouterMixin implements ECOCraftingOutputRouter {

    private static final org.slf4j.Logger KJK_LOGGER =
            org.slf4j.LoggerFactory.getLogger("kunjinkao/neoecoae-patch");

    /** 反射查找一次就够。 */
    private static Method getMainNodeMethod;
    private static Method getGridMethod;
    private static Method getCraftingServiceMethod;

    @Override
    public long neoecoae$insertIntoCpuForJob(UUID jobId, AEKey key, long amount, Actionable mode) {
        Object grid = resolveGrid();
        if (grid == null) {
            KJK_LOGGER.info("[KJK-PATCH] insertIntoCpuForJob({}, {}, {}) -> 网格为 null", jobId, key, amount);
            return 0L;
        }
        try {
            if (getCraftingServiceMethod == null) {
                getCraftingServiceMethod = grid.getClass().getMethod("getCraftingService");
            }
            Object crafting = getCraftingServiceMethod.invoke(grid);
            if (crafting instanceof ECOCraftingOutputRouter router) {
                long accepted = router.neoecoae$insertIntoCpuForJob(jobId, key, amount, mode);
                KJK_LOGGER.info("[KJK-PATCH] 转发给合成服务: job={} key={} 请求={} 接受={}",
                        jobId, key, amount, accepted);
                return accepted;
            }
            KJK_LOGGER.info("[KJK-PATCH] 合成服务 {} 不是 ECOCraftingOutputRouter",
                    crafting == null ? "null" : crafting.getClass().getName());
        } catch (ReflectiveOperationException e) {
            KJK_LOGGER.info("[KJK-PATCH] 取合成服务失败: {}", e.toString());
        }
        return 0L;
    }

    /**
     * 诊断：谁在设暂停原因、设成了什么。
     * 参数类型用 Enum（目标方法收的是私有内嵌枚举 PauseReason，编译期引不到，
     * 但 Enum 是它的父类型，Mixin 按描述符匹配、按可赋值性校验）。
     */
    @Inject(method = "setPauseReason", at = @At("HEAD"))
    private void kunjinkao$logPauseReason(Enum<?> reason, CallbackInfo ci) {
        StackTraceElement[] trace = new Throwable().getStackTrace();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < trace.length && i < 6; i++) {
            StackTraceElement el = trace[i];
            if (el.getClassName().contains("kunjinkao")) {
                continue;
            }
            sb.append("\n    at ").append(el);
        }
        KJK_LOGGER.info("[KJK-PATCH] setPauseReason -> {}{}", reason, sb);
    }

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