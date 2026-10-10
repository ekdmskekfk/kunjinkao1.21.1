package dev.modmind.kunjinkao.client.qol;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import net.neoforged.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 功能面板的界面状态：选中哪个分类、滚到哪、缩放多少。存在
 * {@code config/kunjinkao-qol-panel.json}。
 * <p>
 * <b>只存"看起来怎么样"，不存"功能开没开"。</b> 功能状态是服务端权威
 * （{@link QolClientState} 只是它的一份镜像，退出即清），而界面状态纯属本地偏好 ——
 * 放进存档或服务端都会变成"我在 A 服调的分类，去 B 服还是它"这种莫名其妙的串台。
 * <p>
 * 读写全程不抛异常：配置文件坏了就退回默认值。这份文件只影响画得好不好看，
 * 为它让玩家进不去游戏是不划算的。
 */
public final class QolPanelConfig {

    private static final Logger LOGGER = LogManager.getLogger("KunJinKao");
    private static final String FILE_NAME = "kunjinkao-qol-panel.json";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /** 面板打开时的本地偏好。字段名就是 JSON 里的键名，改名会导致旧文件读不出来（回落默认值，无害）。 */
    public static final class Data {
        /** 上次选中的分类（枚举名）。找不到对应分类时回落到第一个有内容的分类。 */
        public String selectedCategory = "";
        /** 列表滚动位置（像素）。负数会被夹回 0。 */
        public int scrollY;
        /** 手动缩放偏移，-50 ~ 100，对应 50% ~ 200%。 */
        public int guiScaleOffset;
        /** 是否跟着分辨率自适应缩放；关掉则只用手动偏移。 */
        public boolean adaptive = true;
    }

    private static Data data;
    private static boolean loaded;

    private QolPanelConfig() {
    }

    /**
     * 取当前偏好对象。
     * <p>
     * 首次访问时才读盘（懒加载）：这个类只被面板用到，不该在游戏启动阶段就去碰文件系统。
     */
    public static Data get() {
        if (!loaded) {
            data = load();
            loaded = true;
        }
        return data;
    }

    /** 写回磁盘。面板关闭时调一次即可，不需要每帧存。 */
    public static void save() {
        if (!loaded) {
            return;
        }
        Path path = path();
        try {
            Files.createDirectories(path.getParent());
            Files.writeString(path, GSON.toJson(data), StandardCharsets.UTF_8);
        } catch (IOException failure) {
            // 存不下就算了：下次打开面板只是回到默认的分类与滚动位置，不影响任何功能。
            LOGGER.warn("[Kunjinkao] 功能面板界面状态写入失败：{}", failure.toString());
        }
    }

    private static Data load() {
        Path path = path();
        if (!Files.isRegularFile(path)) {
            return new Data();
        }
        try {
            String json = Files.readString(path, StandardCharsets.UTF_8);
            Data parsed = GSON.fromJson(json, Data.class);
            return sanitize(parsed);
        } catch (IOException | JsonSyntaxException broken) {
            LOGGER.warn("[Kunjinkao] 功能面板界面状态读取失败，改用默认值：{}", broken.toString());
            return new Data();
        }
    }

    /**
     * 把读到的值夹回合法区间。
     * <p>
     * 手改过这个文件、或者它来自一个以后版本的面板时，值可能越界；
     * 这里统一收口，免得一个 {@code scrollY: -99999} 让列表画到屏幕外、看起来像面板坏了。
     */
    private static Data sanitize(Data parsed) {
        Data fixed = parsed == null ? new Data() : parsed;
        fixed.scrollY = Math.max(0, fixed.scrollY);
        fixed.guiScaleOffset = Math.max(-50, Math.min(100, fixed.guiScaleOffset));
        if (fixed.selectedCategory == null) {
            fixed.selectedCategory = "";
        }
        return fixed;
    }

    private static Path path() {
        return FMLPaths.CONFIGDIR.get().resolve(FILE_NAME);
    }
}