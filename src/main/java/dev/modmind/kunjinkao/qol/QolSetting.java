package dev.modmind.kunjinkao.qol;

import java.util.Locale;

/**
 * 功能面板的设定项总表：一项一行，服务端与客户端共用同一份定义。
 * <p>
 * 放在这里而不是各写一份，是因为"这一项叫什么、取值范围多少、谁能改"必须两边一致 ——
 * 客户端拿它画数值条，服务端拿它校验客户端发来的值。两边各维护一份的话，
 * 改了服务端忘了改客户端，表现就是滑条能拖到越界值然后被服务端静默丢弃。
 * <p>
 * <b>本类不得触碰任何 {@code net.minecraft.client} 类型</b>：服务端也要加载它。
 */
public enum QolSetting {

    /** 80 星瞳夜视：常驻夜视，短时长高频续期。与战术 HUD 的夜视开关是同一份状态。 */
    NIGHT_VISION(0, 80, Category.SURVIVAL, Scope.PERSONAL, Kind.TOGGLE, 0, 1, 0),
    /** 83 凌空采掘：空中（可选水下）挖掘不再吃原版的处境惩罚。 */
    FLY_MINING(1, 83, Category.CONVENIENCE, Scope.PERSONAL, Kind.TOGGLE, 0, 1, 0),
    /** 85 鲛人之息：水下氧气永远满。 */
    WATER_BREATH(2, 85, Category.SURVIVAL, Scope.PERSONAL, Kind.TOGGLE, 0, 1, 0),
    /** 88 万民敬仰：常驻村庄英雄，等级可调，上限由服务器配置压。 */
    VILLAGE_HERO(3, 88, Category.INCOME, Scope.PERSONAL, Kind.NUMBER, 0, 255, 0),
    /** 89 长臂善舞：方块交互与实体交互距离各 +N 格，上限由服务器配置压。 */
    REACH(4, 89, Category.CONVENIENCE, Scope.PERSONAL, Kind.NUMBER, 0, 64, 0);

    /**
     * 面板顶部的分类标签。
     * <p>
     * 顺序就是标签条上的显示顺序（照开发计划 5.2 节）。以后加项时直接挂到已有分类上；
     * <b>没有项的类别不会显示</b>，所以 M1 阶段"机器与工程""服务器级"两个标签是空的、不出现，
     * 等 M3 的功能进来会自己冒出来，不用改面板代码。
     */
    public enum Category {
        SURVIVAL("survival"),
        INCOME("income"),
        CONVENIENCE("convenience"),
        ENGINEERING("engineering"),
        SERVER("server");

        private final String key;

        Category(String key) {
            this.key = key;
        }

        public String translationKey() {
            return "screen.kunjinkao.qol.category." + key;
        }
    }

    /** 作用域：个人级改动写进玩家存档，服务器级改动写回 toml 且只有 OP 能动。 */
    public enum Scope {
        PERSONAL,
        SERVER
    }

    /** 交互形态：开关只有 0/1，数值项是一个区间。 */
    public enum Kind {
        TOGGLE,
        NUMBER
    }

    /** 设定项总数，用于遍历与数组定长。 */
    public static final int COUNT = values().length;

    private final int id;
    private final int code;
    private final Category category;
    private final Scope scope;
    private final Kind kind;
    private final int min;
    private final int hardMax;
    private final int defaultValue;

    QolSetting(int id, int code, Category category, Scope scope, Kind kind,
               int min, int hardMax, int defaultValue) {
        this.id = id;
        this.code = code;
        this.category = category;
        this.scope = scope;
        this.kind = kind;
        this.min = min;
        this.hardMax = hardMax;
        this.defaultValue = defaultValue;
    }

    /** 线格式里的判别 id。只能往后追加，不能改已有取值。 */
    public int id() {
        return id;
    }

    /**
     * 原始开发计划里的功能序号（80 / 83 / 85 / 88 / 89 …）。
     * <p>
     * 只用于面板上的序号徽章与搜索：玩家和文档都用这个号说话（"89 那个"），
     * 而线格式用的是 {@link #id()}。两个号故意分开 —— 序号是给人看的，动了不影响兼容；
     * 线格式 id 动了会让新旧端对不上。
     */
    public int code() {
        return code;
    }

    public Category category() {
        return category;
    }

    public Scope scope() {
        return scope;
    }

    public Kind kind() {
        return kind;
    }

    public int min() {
        return min;
    }

    /**
     * 本项自身的硬上限。真正生效的上限还要再被服务器配置压一次
     * （见 {@link QolSettings#maxOf}）—— 这里只是"再怎么配也不能超"的那条线。
     */
    public int hardMax() {
        return hardMax;
    }

    public int defaultValue() {
        return defaultValue;
    }

    /** 名称的翻译键。按枚举名生成，加一项就自动多一个键，不用回来改这里。 */
    public String translationKey() {
        return "screen.kunjinkao.qol." + name().toLowerCase(Locale.ROOT);
    }

    /** 本类别下有没有项 —— 面板用它决定要不要画这个标签。 */
    public static boolean hasAnyIn(Category category) {
        for (QolSetting setting : values()) {
            if (setting.category == category) {
                return true;
            }
        }
        return false;
    }

    /** 按线格式 id 取项；未知 id 返回 null（调用方必须当成"什么都不做"处理）。 */
    public static QolSetting byId(int id) {
        for (QolSetting setting : values()) {
            if (setting.id == id) {
                return setting;
            }
        }
        return null;
    }

    /** 把任意输入夹到本项的合法区间（按硬上限，不含服务器配置）。 */
    public int clamp(int value) {
        return Math.max(min, Math.min(hardMax, value));
    }
}