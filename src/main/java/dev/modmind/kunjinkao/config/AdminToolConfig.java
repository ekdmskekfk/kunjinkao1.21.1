package dev.modmind.kunjinkao.config;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.UUID;

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
        if (submittedPassword == null || submittedPassword.length() > 64) {
            return false;
        }
        String configured = configuredPassword();
        // 未配置密码（配置缺失或留空）时一律拒绝，避免空密码被用来提权。
        if (configured.isEmpty()) {
            return false;
        }
        byte[] expected = configured.getBytes(StandardCharsets.UTF_8);
        byte[] submitted = submittedPassword.getBytes(StandardCharsets.UTF_8);
        if (!MessageDigest.isEqual(expected, submitted)) {
            return false;
        }
        PasswordAdminSavedData.get(player.server).authorize(player.getUUID());
        return true;
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
