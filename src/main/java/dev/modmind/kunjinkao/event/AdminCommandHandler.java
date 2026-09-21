package dev.modmind.kunjinkao.event;

import dev.modmind.kunjinkao.config.PasswordAdminSavedData;
import java.util.UUID;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

/**
 * 管理员身份的运维命令。
 *
 * <p>密码机制只负责"授予"，而授权一旦写进存档就无法通过改密码回收，因此需要一条
 * 只对 OP（权限等级 4）开放的命令来查看/授予/撤销管理员身份。这也是本模组第一处
 * 使用原版权限系统的地方：面向服务器的管理操作走 OP，面向玩法的权限仍走密码。</p>
 */
public final class AdminCommandHandler {
    private static final String ROOT = "kunjinkao-admin";
    private static final int OP_PERMISSION_LEVEL = 4;

    private AdminCommandHandler() {
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal(ROOT)
                .requires(source -> source.hasPermission(OP_PERMISSION_LEVEL))
                .then(Commands.literal("list")
                        .executes(context -> listAdmins(context.getSource())))
                .then(Commands.literal("grant")
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes(context -> grant(context.getSource(),
                                        EntityArgument.getPlayer(context, "player")))))
                .then(Commands.literal("revoke")
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes(context -> revoke(context.getSource(),
                                        EntityArgument.getPlayer(context, "player"))))));
    }

    private static int listAdmins(CommandSourceStack source) {
        PasswordAdminSavedData data = data(source);
        if (data == null) {
            return 0;
        }
        var uuids = data.snapshot();
        if (uuids.isEmpty()) {
            source.sendSuccess(() -> Component.literal("[Kunjinkao] 当前没有任何管理员授权"), false);
            return 0;
        }
        source.sendSuccess(() -> Component.literal("[Kunjinkao] 管理员共 " + uuids.size() + " 人："), false);
        for (UUID uuid : uuids) {
            ServerPlayer online = source.getServer() == null
                    ? null : source.getServer().getPlayerList().getPlayer(uuid);
            String label = online == null
                    ? uuid.toString() : online.getName().getString() + " (" + uuid + ")";
            source.sendSuccess(() -> Component.literal("  - " + label), false);
        }
        return uuids.size();
    }

    private static int grant(CommandSourceStack source, ServerPlayer target) {
        PasswordAdminSavedData data = data(source);
        if (data == null) {
            return 0;
        }
        data.authorize(target.getUUID());
        source.sendSuccess(() -> Component.literal(
                "[Kunjinkao] 已授予 " + target.getName().getString() + " 管理员身份"), true);
        return 1;
    }

    private static int revoke(CommandSourceStack source, ServerPlayer target) {
        PasswordAdminSavedData data = data(source);
        if (data == null) {
            return 0;
        }
        boolean removed = data.revoke(target.getUUID());
        if (removed) {
            source.sendSuccess(() -> Component.literal(
                    "[Kunjinkao] 已撤销 " + target.getName().getString() + " 的管理员身份"), true);
            return 1;
        }
        source.sendSuccess(() -> Component.literal(
                "[Kunjinkao] " + target.getName().getString() + " 本来就不在管理员名单里"), false);
        return 0;
    }

    private static PasswordAdminSavedData data(CommandSourceStack source) {
        var server = source.getServer() != null ? source.getServer() : ServerLifecycleHooks.getCurrentServer();
        if (server == null) {
            source.sendFailure(Component.literal("[Kunjinkao] 服务器未就绪"));
            return null;
        }
        return PasswordAdminSavedData.get(server);
    }
}