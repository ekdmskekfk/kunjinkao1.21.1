package dev.modmind.kunjinkao.mixin;

import appeng.api.config.Actionable;
import appeng.api.stacks.AEKey;
import cn.dancingsnow.neoecoae.api.me.output.ECOCraftingOutputRouter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import java.lang.reflect.Method;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
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
 *       return false;                        // ← 直接失败，显示「暂停：产物输出受阻」
 *   }
 * </pre>
 *
 * 但那台机器与它的父类都没有实现这个接口 —— 真正实现它的是被 mixin 加工过的 AE2
 * CraftingService（见 neoecoae 自带的 CraftingServiceMixin）。
 * 于是只要这批产物带着尚未结束的 craftingJobId，投递就必然返回 false，
 * 而且与 ME 网络、合成 CPU 有没有空间完全无关。
 *
 * <h2>本补丁做什么</h2>
 * 把接口织到那台机器上，实现体转交给网格的合成服务 ——
 * 也就是作者当时想写、却写成了 {@code this} 的那个对象。
 * 已在实际实例的日志中确认织入成功：
 * <pre>
 *   Mixing ECOLargeIntegratedWorkingStationOutputRouterMixin from kunjinkao.mixins.json
 *     into cn.dancingsnow.neoecoae.blocks.entity.ECOLargeIntegratedWorkingStationBlockEntity
 * </pre>
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

    /**
     * 诊断日志。
     * <p>
     * 刻意【不】使用 @Inject：那要求与目标方法逐字一致的方法描述符，而 setPauseReason
     * 收的是私有内嵌枚举 PauseReason —— 用 Enum 或 Object 代替都会让 Mixin 抛
     * InvalidInjectionException，且失败的是【整个混入类】，连
     * implements ECOCraftingOutputRouter 一起失效（0.3.4 就是这么坏掉的）。
     * 这两处日志写在补丁自己的方法体内，不可能影响织入。
     */
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
            KJK_LOGGER.info("[KJK-PATCH] insertIntoCpuForJob(job={}, key={}, amount={}) -> 网格为 null",
                    jobId, key, amount);
            return 0L;
        }
        try {
            if (getCraftingServiceMethod == null) {
                getCraftingServiceMethod = grid.getClass().getMethod("getCraftingService");
            }
            Object crafting = getCraftingServiceMethod.invoke(grid);
            // 网格的合成服务（被 neoecoae 的 CraftingServiceMixin 加工过）才是真正的 Router。
            if (crafting instanceof ECOCraftingOutputRouter router) {
                long accepted = router.neoecoae$insertIntoCpuForJob(jobId, key, amount, mode);
                KJK_LOGGER.info("[KJK-PATCH] 已转发: job={} key={} 请求={} 接受={}",
                        jobId, key, amount, accepted);
                return accepted;
            }
            KJK_LOGGER.info("[KJK-PATCH] 合成服务 {} 不是 ECOCraftingOutputRouter",
                    crafting == null ? "null" : crafting.getClass().getName());
        } catch (ReflectiveOperationException e) {
            KJK_LOGGER.info("[KJK-PATCH] 取合成服务失败: {}", e.toString());
            // 取不到就当作放不下；上层会维持"输出受阻"，不会抛出。
        }
        return 0L;
    }

    /**
     * 诊断：暂停原因文本被取用时，打印当前原因。
     * <p>
     * 这个方法【没有参数】，描述符天然精确，是唯一能安全注入的入口
     * （setPauseReason 与 deliverBatchOutputs 的参数都是私有内嵌类型，
     * 用 Enum/Object 代替会抛 InvalidInjectionException 并让整个混入类失效）。
     * 它被调用就证明机器确实处在暂停状态、且界面正在显示原因。
     */
    @Inject(method = "getPauseReasonText", at = @At("HEAD"))
    private void kunjinkao$logCurrentPauseReason(CallbackInfoReturnable<net.minecraft.network.chat.Component> cir) {
        Object reason = kunjinkao$currentPauseReason();
        KJK_LOGGER.info("[KJK-PATCH] 当前暂停原因 = {}", reason);
    }

    /**
     * 诊断：把未消耗的输入退回网络这一条路径。
     * <p>
     * 参数 KeyCounter 是 AE2 的公开类型，因此可以精确注入 ——
     * 这正是 deliverBatchOutputs 之外最可能设置「产物输出受阻」的地方。
     */
    @Inject(method = "recoverCounterToNetwork", at = @At("HEAD"))
    private void kunjinkao$logRecoverHead(appeng.api.stacks.KeyCounter counter, CallbackInfoReturnable<Boolean> cir) {
        KJK_LOGGER.info("[KJK-PATCH] recoverCounterToNetwork 被调用，counter 数量={}", counter.size());
    }

    @Inject(method = "recoverCounterToNetwork", at = @At("RETURN"))
    private void kunjinkao$logRecoverResult(appeng.api.stacks.KeyCounter counter, CallbackInfoReturnable<Boolean> cir) {
        KJK_LOGGER.info("[KJK-PATCH] recoverCounterToNetwork 返回 = {}", cir.getReturnValue());
    }

    /** 反射读 pauseReasonId（私有字段），仅在诊断时用。 */
    private Object kunjinkao$currentPauseReason() {
        try {
            java.lang.reflect.Field f = this.getClass().getDeclaredField("pauseReasonId");
            f.setAccessible(true);
            return f.get(this);
        } catch (ReflectiveOperationException e) {
            return "读取失败: " + e;
        }
    }

    /**
     * 诊断：暂停原因发生变化时打印，并附调用栈。
     * <p>
     * 「网格为 null」的假设已被日志证伪（那条诊断一次都没打印），
     * 因此改为直接盯住 pauseReasonId 的变化 —— 它是 private int，用反射读；
     * 只在变化时打印，所以不会刷屏。
     */
    @Inject(method = "tickPendingBatch", at = @At("HEAD"))
    private void kunjinkao$watchPauseReason(CallbackInfoReturnable<appeng.api.networking.ticking.TickRateModulation> cir) {
        int now = kunjinkao$readPauseReasonId();
        if (now != kunjinkao$lastPauseReasonId) {
            kunjinkao$lastPauseReasonId = now;
            Object grid = resolveGrid();
            KJK_LOGGER.info("[KJK-PATCH] 暂停原因变化 -> {}（网格{}null, instanceof Router={}, 待输出非空={}）{}",
                    now, grid == null ? "==" : "!=", this instanceof ECOCraftingOutputRouter,
                    kunjinkao$firstBatchHasPendingOutput(), kunjinkao$shortTrace());
        }
    }

    /**
     * 诊断：returnStoredInputs 是另一个会设置 OUTPUT_BLOCKED 的公开方法。
     * 它被调用即说明机器在"把已存的输入还回去"。
     */
    @Inject(method = "returnStoredInputs", at = @At("HEAD"))
    private void kunjinkao$logReturnStoredInputs(CallbackInfo ci) {
        KJK_LOGGER.info("[KJK-PATCH] returnStoredInputs 被调用（它会设置 OUTPUT_BLOCKED）{}",
                kunjinkao$shortTrace());
    }

    private int kunjinkao$lastPauseReasonId = -1;

    private int kunjinkao$readPauseReasonId() {
        try {
            java.lang.reflect.Field f = this.getClass().getDeclaredField("pauseReasonId");
            f.setAccessible(true);
            return f.getInt(this);
        } catch (ReflectiveOperationException e) {
            return -1;
        }
    }
    /**
     * 反射读 pendingBatches.peekFirst().pendingOutput 是否非空。
     * 这几个字段都是私有的，编译期够不着。
     */
    private boolean kunjinkao$firstBatchHasPendingOutput() {
        try {
            java.lang.reflect.Field batches = this.getClass().getDeclaredField("pendingBatches");
            batches.setAccessible(true);
            Object deque = batches.get(this);
            if (!(deque instanceof java.util.Deque<?> d) || d.isEmpty()) {
                return false;
            }
            Object batch = d.peekFirst();
            java.lang.reflect.Field out = batch.getClass().getDeclaredField("pendingOutput");
            out.setAccessible(true);
            Object counter = out.get(batch);
            return counter != null && !(Boolean) counter.getClass().getMethod("isEmpty").invoke(counter);
        } catch (ReflectiveOperationException e) {
            return false;
        }
    }


    /** 去掉本模组自身的帧，避免把补丁的调用栈也打出来。 */
    private static String kunjinkao$shortTrace() {
        StringBuilder sb = new StringBuilder();
        StackTraceElement[] trace = new Throwable().getStackTrace();
        int shown = 0;
        for (StackTraceElement el : trace) {
            if (el.getClassName().contains("kunjinkao")) {
                continue;
            }
            sb.append("\n    at ").append(el);
            if (++shown >= 6) {
                break;
            }
        }
        return sb.toString();
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