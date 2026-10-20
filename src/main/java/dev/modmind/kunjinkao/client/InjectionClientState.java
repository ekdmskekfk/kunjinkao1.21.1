package dev.modmind.kunjinkao.client;

import net.minecraft.world.InteractionHand;

/**
 * 客户端侧的注入进度镜像。
 *
 * <p>时间基准用 {@link System#currentTimeMillis()} 而不是 tick 计数：注入要的是
 * "看起来匀速的 3 秒"，而客户端 tick 在卡顿时会掉、在暂停时不动。用墙钟时间插值，
 * 表现最稳；服务端仍然按 tick 计时并最终决定成败，两边不一致时以服务端的
 * {@code ABORT}/{@code DONE} 为准 —— 这个类只负责画。</p>
 */
public final class InjectionClientState {

    /** 完成后的成功提示保留多久（毫秒）。 */
    private static final long DONE_FLASH_MS = 900L;

    private static boolean active;
    private static long startMs;
    private static long durationMs;
    private static InteractionHand hand = InteractionHand.MAIN_HAND;

    private static boolean flashVisible;
    private static long flashStartMs;

    private InjectionClientState() {
    }

    public static void start(int durationTicks, InteractionHand hand) {
        active = true;
        startMs = System.currentTimeMillis();
        durationMs = Math.max(1, durationTicks) * 50L;
        InjectionClientState.hand = hand;
    }

    public static void abort() {
        active = false;
    }

    public static void complete() {
        active = false;
        flashVisible = true;
        flashStartMs = System.currentTimeMillis();
    }

    public static boolean isActive() {
        return active;
    }

    public static InteractionHand hand() {
        return hand;
    }

    /** 0..1，按墙钟时间插值并夹紧。 */
    public static float progress() {
        if (!active) {
            return 0.0F;
        }
        long elapsed = System.currentTimeMillis() - startMs;
        float raw = durationMs <= 0L ? 1.0F : (float) elapsed / (float) durationMs;
        return Math.max(0.0F, Math.min(1.0F, raw));
    }

    /** 完成提示是否还在显示期内。 */
    public static boolean isFlashVisible() {
        if (!flashVisible) {
            return false;
        }
        return System.currentTimeMillis() - flashStartMs < DONE_FLASH_MS;
    }

    /** 完成提示的淡出系数（1 → 0）。 */
    public static float flashAlpha() {
        if (!flashVisible) {
            return 0.0F;
        }
        long elapsed = System.currentTimeMillis() - flashStartMs;
        float raw = 1.0F - (float) elapsed / (float) DONE_FLASH_MS;
        return Math.max(0.0F, Math.min(1.0F, raw));
    }

    /** 每 client tick 调一次：进度条被墙钟判定走完了但服务端还没回包时，先自己收掉。 */
    public static void tick() {
        if (active && progress() >= 1.0F) {
            // 不在这里置 active=false：留一点余量等服务端的 DONE/ABORT。
            // 服务端按 tick 计时，正常情况会先或同时到达。
        }
        if (flashVisible && !isFlashVisible()) {
            flashVisible = false;
        }
    }

    public static void reset() {
        active = false;
        flashVisible = false;
    }
}
