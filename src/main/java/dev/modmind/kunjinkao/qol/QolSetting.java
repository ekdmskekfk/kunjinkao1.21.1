package dev.modmind.kunjinkao.qol;

import java.util.Locale;

/**
 * 功能面板的设定项总表：一项一行，服务端与客户端共用同一份定义。
 * <p>
 * 放在这里而不是各写一份，是因为"这一项叫什么、取值范围多少、谁能改"必须两边一致 ——
 * 客户端拿它画数值条，服务端拿它校验客户端发来的值。
 * <p>
 * <b>本类不得触碰任何 {@code net.minecraft.client} 类型</b>：服务端也要加载它。
 */
public enum QolSetting {

    /** 80 星瞳夜视：常驻夜视，短时长高频续期。与战术 HUD 的夜视开关是同一份状态。 */
    NIGHT_VISION(0, 80, Category.SURVIVAL, Scope.PERSONAL, Kind.TOGGLE, 0, 1, 0, ""),
    /** 83 凌空采掘：空中（可选水下）挖掘不再吃原版的处境惩罚。 */
    FLY_MINING(1, 83, Category.CONVENIENCE, Scope.PERSONAL, Kind.TOGGLE, 0, 1, 0, ""),
    /** 85 鲛人之息：水下氧气永远满。 */
    WATER_BREATH(2, 85, Category.SURVIVAL, Scope.PERSONAL, Kind.TOGGLE, 0, 1, 0, ""),
    /** 88 万民敬仰：常驻村庄英雄，等级可调，上限由服务器配置压。 */
    VILLAGE_HERO(3, 88, Category.INCOME, Scope.PERSONAL, Kind.NUMBER, 0, 255, 0, ""),
    /** 89 长臂善舞：方块交互与实体交互距离各 +N 格，上限由服务器配置压。 */
    REACH(4, 89, Category.CONVENIENCE, Scope.PERSONAL, Kind.NUMBER, 0, 64, 0, ""),

    /** 72 经验飞涨：击杀获得的经验乘这个倍率。1 = 原版。 */
    XP_BOOST(5, 72, Category.INCOME, Scope.SERVER, Kind.NUMBER, 1, 101, 1, "xpBoostMultiplier"),
    /** 77 不朽铭文：铁砧里两个相同物品合出【无法破坏】。 */
    ANVIL_UNBREAKABLE(6, 77, Category.INCOME, Scope.SERVER, Kind.TOGGLE, 0, 1, 0, "anvilUnbreakableEnabled"),
    /** 77 的成本：上面那个开关生效时，铁砧收多少级经验。 */
    ANVIL_XP_COST(7, 77, Category.INCOME, Scope.SERVER, Kind.NUMBER, 1, 100, 10, "anvilXpCost"),

    /** 84 烈焰不侵：免疫火焰与岩浆伤害、不着火。 */
    FIRE_IMMUNE(8, 84, Category.SURVIVAL, Scope.PERSONAL, Kind.TOGGLE, 0, 1, 0, ""),
    /** 94 无限交易：交易次数不减少、永不售罄。 */
    UNLIMITED_TRADES(9, 94, Category.INCOME, Scope.PERSONAL, Kind.TOGGLE, 0, 1, 0, ""),
    /** 95 村民大师：交易一次该村民直接满级并刷新交易表。 */
    VILLAGER_MASTER(10, 95, Category.INCOME, Scope.PERSONAL, Kind.TOGGLE, 0, 1, 0, ""),
    /**
     * 70 万载不磨：物品不再掉耐久。
     * <p>
     * 开发计划原本把这一项的开关放在<b>剑的设置界面</b>。这里放进了面板：
     * 计划的横切目标就是"一个入口看全 15 项"，而 70 要的效果是"<b>玩家</b>的物品不掉耐久"，
     * 不是"手上那把剑的能力" —— 按持剑判定会让玩家一换主手就时灵时不灵。
     * 83 凌空采掘在 M1 已经按同样的理由放进了面板，两项保持一致。
     */
    DURABILITY_PROTECTION(11, 70, Category.CONVENIENCE, Scope.PERSONAL, Kind.TOGGLE, 0, 1, 0, "");

    /**
     * 面板顶部的分类标签。
     * <p>
     * 顺序就是标签条上的显示顺序（照开发计划 5.2 节）。{@link #SERVER} 比较特殊：
     * 它不是"另一批项"，而是把各分类里 {@code scope == SERVER} 的行<b>再集中列一遍</b>，
     * 方便管理员一屏定位到所有会写进 toml 的开关（见 {@link #inCategory}）。
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
    private final String configKey;

    QolSetting(int id, int code, Category category, Scope scope, Kind kind,
               int min, int hardMax, int defaultValue, String configKey) {
        this.id = id;
        this.code = code;
        this.category = category;
        this.scope = scope;
        this.kind = kind;
        this.min = min;
        this.hardMax = hardMax;
        this.defaultValue = defaultValue;
        this.configKey = configKey;
    }

    /** 线格式里的判别 id。只能往后追加，不能改已有取值。 */
    public int id() {
        return id;
    }

    /**
     * 原始开发计划里的功能序号（72 / 77 / 80 / 84 …）。
     * <p>
     * 只用于面板上的序号徽章与搜索。与线格式用的 {@link #id()} 故意分开：
     * 序号是给人看的，动了不影响兼容；线格式 id 动了会让新旧端对不上。
     * <b>两项允许共用同一个序号</b>：77 是"开关 + 成本"两行，它们本来就是同一个功能。
     */
    public int code() {
        return code;
    }

    /** 主要归属分类。服务器级项同时还会出现在 {@link Category#SERVER} 那个汇总标签下。 */
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

    /**
     * 服务器级项在 {@code kunjinkao-admin.toml} 的 {@code [qol]} 里的键名；个人级项为空串。
     * <p>
     * 写死在这里而不是按枚举名推导：toml 键是服主会看到、会写进启动脚本的东西，
     * 改个枚举名就悄悄换掉配置键，服主的旧配置文件会一声不响地失效。
     */
    public String configKey() {
        return configKey;
    }

    /** 名称的翻译键。按枚举名生成，加一项就自动多一个键，不用回来改这里。 */
    public String translationKey() {
        return "screen.kunjinkao.qol." + name().toLowerCase(Locale.ROOT);
    }

    /**
     * 本项是否属于某个分类标签。
     * <p>
     * {@link Category#SERVER} 是汇总视图：只要 {@code scope == SERVER} 就算属于它，
     * 不管它自己挂在哪个功能分类下。于是"收益"标签里能看到 72 倍率，
     * 管理员切到"服务器级"还能再看到同一行 —— 这是有意的重复，不是漏改。
     */
    public static boolean inCategory(QolSetting setting, Category category) {
        if (category == Category.SERVER) {
            return setting.scope == Scope.SERVER;
        }
        return setting.category == category;
    }

    /** 本类别下有没有项 —— 面板用它决定要不要画这个标签。 */
    public static boolean hasAnyIn(Category category) {
        for (QolSetting setting : values()) {
            if (inCategory(setting, category)) {
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
}