package dev.modmind.kunjinkao.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 管理员身份由服务器端配置的密码授予：玩家在游戏内验证通过后，
 * 其 UUID 被记入当前存档的 {@link PasswordAdminSavedData}，之后所有管理员功能都读这份存档数据。
 * <p>
 * 密码存放在 {@code config/kunjinkao-admin.toml}，不再硬编码在代码里。
 * 该文件只存在于实例/服务端本地，不会随模组 jar 分发。
 */
public final class AdminToolConfig {

    /**
     * 默认密码，仅用于让既有存档开箱即用。
     * 服务器管理员应当立即在配置文件中改掉它——默认值是公开的。
     */
    public static final String DEFAULT_ADMIN_PASSWORD = "118329";

    public static final ModConfigSpec COMMON_SPEC;
    private static final ModConfigSpec.ConfigValue<String> ADMIN_PASSWORD;

    private static final Logger LOGGER = LogManager.getLogger("KunJinKao");

    /** 同一玩家两次尝试之间的最小间隔（毫秒），阻断脚本化高速爆破。 */
    private static final long MIN_ATTEMPT_INTERVAL_MILLIS = 2000L;
    /** 滑动窗口内允许的失败次数，超过则本窗口内直接拒绝。 */
    private static final int MAX_FAILURES_PER_WINDOW = 5;
    private static final long FAILURE_WINDOW_MILLIS = 300_000L;

    /** 进程内的尝试记录：重启即清空，够用且不写盘。 */
    private static final Map<UUID, Attempts> ATTEMPTS = new ConcurrentHashMap<>();
    /** 默认密码告警每个服务器会话只打一次。 */
    private static volatile boolean warnedAboutDefaultPassword = false;

    /** 单个玩家的密码尝试状态。 */
    private static final class Attempts {
        private long lastAttempt;
        private long windowStart;
        private int failures;
    }

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        builder.push("admin");
        ADMIN_PASSWORD = builder
                .comment(" 管理员密码。玩家在游戏内通过该密码验证后即可使用战术 HUD 与管理员剑。",
                        " 默认值是公开的，请务必改成你自己的密码。",
                        " 留空表示关闭密码验证，任何人都无法通过密码取得管理员权限。")
                .define("password", DEFAULT_ADMIN_PASSWORD);
        builder.pop();
        COMMON_SPEC = builder.build();
    }

    private AdminToolConfig() {
    }

    public static boolean isAuthorized(UUID playerUuid) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        return server != null && PasswordAdminSavedData.get(server).isAuthorized(playerUuid);
    }

    /** 只应由服务端网络包处理器调用；绝不信任客户端的“已验证”状态。 */
    public static boolean authorize(ServerPlayer player, String submittedPassword) {
        UUID uuid = player.getUUID();
        long now = System.currentTimeMillis();
        Attempts attempts = ATTEMPTS.computeIfAbsent(uuid, key -> new Attempts());
        synchronized (attempts) {
            // 频率限制：默认密码只有 6 位数字，不设限的话单个连接就能穷举整个空间。
            if (now - attempts.lastAttempt < MIN_ATTEMPT_INTERVAL_MILLIS) {
                return false;
            }
            attempts.lastAttempt = now;
            if (now - attempts.windowStart > FAILURE_WINDOW_MILLIS) {
                attempts.windowStart = now;
                attempts.failures = 0;
            }
            if (attempts.failures >= MAX_FAILURES_PER_WINDOW) {
                LOGGER.warn("[Kunjinkao] 玩家 {} 的密码尝试过于频繁，本窗口内已拒绝", player.getName().getString());
                return false;
            }
            if (!verify(submittedPassword)) {
                attempts.failures++;
                LOGGER.warn("[Kunjinkao] 玩家 {} 的管理员密码验证失败（本窗口第 {} 次）",
                        player.getName().getString(), attempts.failures);
                return false;
            }
            attempts.failures = 0;
            PasswordAdminSavedData.get(player.server).authorize(uuid);
            LOGGER.info("[Kunjinkao] 玩家 {} 通过密码验证，已获得管理员权限", player.getName().getString());
            return true;
        }
    }

    /** 纯密码比对，不改变任何状态（限流通过后才调用）。 */
    private static boolean verify(String submittedPassword) {
        if (submittedPassword == null || submittedPassword.length() > 64) {
            return false;
        }
        String configured = configuredPassword();
        // 未配置密码（配置缺失或留空）时一律拒绝，避免空密码被用来提权。
        if (configured.isEmpty()) {
            return false;
        }
        warnIfDefaultPassword(configured);
        byte[] expected = configured.getBytes(StandardCharsets.UTF_8);
        byte[] submitted = submittedPassword.getBytes(StandardCharsets.UTF_8);
        return MessageDigest.isEqual(expected, submitted);
    }

    /**
     * 默认密码是公开写在源码里的：服务器没改它，等于对外开放管理员权限，至少在日志里吼一声。
     * 每个服务器会话只提醒一次，且不打印密码本身。
     */
    private static void warnIfDefaultPassword(String configured) {
        if (warnedAboutDefaultPassword || !DEFAULT_ADMIN_PASSWORD.equals(configured)) {
            return;
        }
        warnedAboutDefaultPassword = true;
        LOGGER.warn("[Kunjinkao] 管理员密码仍是源码中的默认值（已公开），任何玩家都能凭它取得管理员权限；"
                + "请修改 config/kunjinkao-admin.toml 的 admin.password");
    }

    /** 配置未加载时返回空串，使授权失败而不是回退到默认密码。 */
    private static String configuredPassword() {
        try {
            String value = ADMIN_PASSWORD.get();
            return value == null ? "" : value;
        } catch (IllegalStateException notLoadedYet) {
            return "";
        }
    }
}
