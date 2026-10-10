package dev.modmind.kunjinkao.client.qol;

import com.mojang.blaze3d.platform.InputConstants;
import dev.modmind.kunjinkao.network.NetworkHandler;
import dev.modmind.kunjinkao.network.QolSettingPayload;
import dev.modmind.kunjinkao.qol.QolSetting;
import net.minecraft.client.Minecraft;

/**
 * 面板里绑定的开关快捷键：按一下就等于在面板上点了那一行的开关。
 * <p>
 * <b>只做开关类。</b> 两个数值项（村庄英雄等级、交互距离）都不是高频操作，
 * 打开面板滚一下滚轮就够，为它们各占一个全局按键不划算；执行键要等 M3 的 127 选区挖掘
 * 才有意义，现在做等于凭空造一个没人知道该绑什么的键。所以 {@link QolSetting.Kind#NUMBER}
 * 哪怕被手改配置文件绑上了键，这里也一律忽略。
 * <p>
 * 与 {@code KunJinKaoKeyBindings} 里那些键不同，这些绑定不进原版按键设置界面：
 * 它们属于面板的一部分，在面板里点格子改，改完立刻生效。
 */
public final class QolKeybinds {

    /**
     * 上一 tick 每个设定项的按下状态，用来做边沿检测。
     * <p>
     * 没有它的话，按住一个键会每 tick 翻转一次（20 次/秒），玩家看到的是开关在乱闪。
     * 按设定项 id 当下标而不是枚举名查表：id 就是线格式里那个稳定序号，加项也不会错位。
     */
    private static final boolean[] WAS_DOWN = new boolean[QolSetting.COUNT];

    private QolKeybinds() {
    }

    /**
     * 每个客户端 tick 调一次。
     * <p>
     * 触发条件刻意收得很紧：没开任何界面、人在游戏里、服务端已经授权。
     * 发出去的是普通的状态改动包 —— 客户端只负责说"我按了"，值合不合法、有没有权限，
     * 服务端会照 {@link QolSettingPayload} 那套再判一遍。
     */
    public static void tick(Minecraft minecraft) {
        long window = minecraft.getWindow().getWindow();
        boolean inGame = minecraft.screen == null && minecraft.player != null;
        for (QolSetting setting : QolSetting.values()) {
            int id = setting.id();
            int code = QolPanelConfig.toggleKey(setting);
            boolean down = code != 0
                    && setting.kind() == QolSetting.Kind.TOGGLE
                    && InputConstants.isKeyDown(window, code);
            boolean edge = down && !WAS_DOWN[id];
            // 先记状态、再判其它条件：这样"开着界面按住键、关掉界面时手还没松"不会补触发一次。
            WAS_DOWN[id] = down;
            if (!edge || !inGame) {
                continue;
            }
            if (!QolClientState.authorized(setting)) {
                // 没授权时这一行是只读的，发过去也会被服务端丢掉，不如一个包都不发。
                continue;
            }
            NetworkHandler.sendToServer(new QolSettingPayload(id, QolClientState.isOn(setting) ? 0 : 1));
        }
    }
}