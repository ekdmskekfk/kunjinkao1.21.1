package dev.modmind.kunjinkao.client.gui;

import dev.modmind.kunjinkao.KunJinKaoSwordItem;
import dev.modmind.kunjinkao.client.KunJinKaoKeyBindings;
import dev.modmind.kunjinkao.network.NetworkHandler;
// 9 个剑设置包已合并为 SwordSettingPayload，导入与发包点一起收敛。
import dev.modmind.kunjinkao.network.SwordSettingPayload;
import dev.modmind.kunjinkao.world.SwordToolHandler;
import dev.modmind.kunjinkao.world.SwordTimeAcceleration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntConsumer;

/**
 * 管理员剑设置界面（. 键）。
 * <p>
 * 布局为「分区标题 + 带边框方框 + 框内双列按钮」：每个分区一个标题和一只方框，
 * 框内按两列摆该分区的选项，按钮文字形如「名称：值」。配色沿用本模组战术 HUD 的
 * 青色主题（深蓝面板 0xE0122030 / 青色描边 0xFF57CFFF / 浅青文字），控件仍用原版
 * 按钮与滑条，和改动前保持一致。
 * <p>
 * 蓝屏打击只是物品上的一个开关标记，绝不会在本机启动任何程序。
 */
public final class SwordOptionsScreen extends Screen {

    // ===== 布局常量（单位是 GUI 像素，与缩放无关）=====
    private static final int SIDE_PAD = 6;
    private static final int TITLE_H = 20;
    private static final int HEADER_H = 14;
    private static final int BOX_PAD = 5;
    private static final int BTN_H = 18;
    private static final int BTN_GAP = 3;
    private static final int ROW_H = BTN_H + BTN_GAP;
    private static final int SECTION_MAX_W = 190;
    private static final int SECTION_GAP = 8;

    // ===== 配色（与原面板一致）=====
    private static final int COLOR_PANEL = 0xE0122030;
    private static final int COLOR_BORDER = 0xFF57CFFF;
    private static final int COLOR_TITLE = 0xFFE9FBFF;
    private static final int COLOR_SECTION = 0xFF8FEAFF;

    private final InteractionHand hand;

    // ===== 9 个控件 =====
    private Button blueScreenToggle;
    private Button areaClearModeButton;
    private AttackDamageSlider attackDamageSlider;
    private Button unbreakableBlockToggle;
    private MiningSpeedSlider miningSpeedSlider;
    private OreDropMultiplierSlider oreDropMultiplierSlider;
    private Button lootingModeButton;
    private Button ultimateDeathToggle;
    private Button quitStrikeToggle;
    private Button constructionWandToggle;
    private Button angelCoreToggle;
    private Button destructionCoreToggle;
    private Button placementUndoToggle;
    private Button brushToggle;
    private Button toolModeButton;
    private Button lightningRodToggle;
    private Button timedAccelToggle;
    private Button infiniteAccelToggle;

    // ===== init() 算出的面板与分区几何，renderBackground 复用 =====
    private int panelX;
    private int panelY;
    private int panelW;
    private int panelH;
    private final List<int[]> sectionBoxes = new ArrayList<>();
    private final List<Component> sectionTitles = new ArrayList<>();
    private final List<int[]> sectionTitlePos = new ArrayList<>();

    // ===== 滑条发包节流 =====
    // 三个滑条原来每变化一格就发一个包，拖一次最坏约 1000 个包（掉落倍数 1..1000）。
    // 规则：同一 settingId 距上次真正发送 < 100ms，且数值相对上次发送没有跨过一"档"时跳过；
    // 被跳过的数值记在 pending 里，render 每帧检查（窗口一过就补发）并在关屏时强制补发一次，
    // 保证玩家最终选定的值一定到达服务端，不会因为节流丢设置。
    // 开关/循环按钮不走节流：它们是离散操作，连点两下必须发两次。
    private static final int SETTING_COUNT = 18;
    private static final long SEND_INTERVAL_MS = 100L;
    private static final int TIER_COUNT = 20;
    private final long[] lastSendMillis = new long[SETTING_COUNT];
    private final int[] lastSentValue = new int[SETTING_COUNT];
    private final boolean[] hasPending = new boolean[SETTING_COUNT];
    private final int[] pendingValue = new int[SETTING_COUNT];

    public SwordOptionsScreen(InteractionHand hand) {
        super(Component.translatable("screen.kunjinkao.settings_options"));
        this.hand = hand;
    }

    /** 「名称：值」形式的按钮文字。 */
    private static Component label(String nameKey, Component value) {
        return Component.translatable(nameKey).append(Component.literal("：")).append(value);
    }

    @Override
    protected void init() {
        sectionBoxes.clear();
        sectionTitles.clear();
        sectionTitlePos.clear();

        // 按可用宽度自适应：两列分区挤不下时分区自动变窄，按钮同步收窄。
        int avail = Math.max(300, width - 12);
        int sectionW = Math.min(SECTION_MAX_W, (avail - SIDE_PAD * 3 - SECTION_GAP) / 2);
        int btnW = Math.max(56, (sectionW - BOX_PAD * 2 - BTN_GAP) / 2);

        // 左列：战斗（2 行）/ 挖掘与战利品（2 行）/ 工具（2 行）
        // 右列：处决（1 行）/ 放置（2 行）/ 加速（1 行）
        // 左右各三块；为容纳新增的「加速」分区，把原「挖掘」与「战利品」合并成一块 ——
        // 两者都是资源类设置，合并后行数不变，正好腾出一个分区的位置。
        int leftBoxH = boxHeight(2);
        int leftBoxH2 = boxHeight(2);
        int leftBoxH3 = boxHeight(2);
        int rightBoxH = boxHeight(1);
        int rightBoxH2 = boxHeight(2);
        int rightBoxH3 = boxHeight(1);

        panelW = SIDE_PAD * 3 + sectionW * 2;
        int leftColH = HEADER_H + leftBoxH + SECTION_GAP + HEADER_H + leftBoxH2
                + SECTION_GAP + HEADER_H + leftBoxH3;
        int rightColH = HEADER_H + rightBoxH + SECTION_GAP + HEADER_H + rightBoxH2
                + SECTION_GAP + HEADER_H + rightBoxH3;
        panelH = TITLE_H + Math.max(leftColH, rightColH) + SIDE_PAD;

        panelX = (width - panelW) / 2;
        panelY = (height - panelH) / 2;

        int leftX = panelX + SIDE_PAD;
        int rightX = leftX + sectionW + SECTION_GAP;

        // ---------- 左列 · 战斗 ----------
        int contentTop = addSection(leftX, panelY + TITLE_H, sectionW, "screen.kunjinkao.section_combat", 2);
        blueScreenToggle = addButton(leftX + BOX_PAD, contentTop, btnW, this::toggleBlueScreenAttack);
        areaClearModeButton = addButton(leftX + BOX_PAD + btnW + BTN_GAP, contentTop, btnW,
                this::cycleAreaClearTargetMode);
        Player player = Minecraft.getInstance().player;
        int attackDamage = player == null ? KunJinKaoSwordItem.DEFAULT_ATTACK_DAMAGE_LIMIT
                : KunJinKaoSwordItem.getAttackDamageLimit(player.getItemInHand(hand));
        attackDamageSlider = addRenderableWidget(new AttackDamageSlider(
                leftX + BOX_PAD, contentTop + ROW_H, sectionW - BOX_PAD * 2, BTN_H,
                attackDamage, this::setAttackDamageLimit));

        // ---------- 左列 · 挖掘与战利品（合并）----------
        // 第 0 行：可破坏不可破坏方块 | 抢夺模式；第 1 行：挖掘速度 | 矿石掉落倍数。
        // 两个滑条各占一格，文字依然是「名称：值」，收窄后仍放得下。
        int miningTop = contentTop + ROW_H + BTN_H + BOX_PAD + SECTION_GAP + HEADER_H;
        contentTop = addSection(leftX, miningTop - HEADER_H, sectionW, "screen.kunjinkao.section_mining_loot", 2);
        unbreakableBlockToggle = addButton(leftX + BOX_PAD, contentTop, btnW,
                this::toggleUnbreakableBlockBreaking);
        lootingModeButton = addButton(leftX + BOX_PAD + btnW + BTN_GAP, contentTop, btnW,
                this::cycleLootingMode);
        int miningSpeed = player == null ? KunJinKaoSwordItem.DEFAULT_MINING_SPEED
                : KunJinKaoSwordItem.getMiningSpeed(player.getItemInHand(hand));
        miningSpeedSlider = addRenderableWidget(new MiningSpeedSlider(
                leftX + BOX_PAD, contentTop + ROW_H, btnW, BTN_H,
                miningSpeed, this::setMiningSpeed));
        int oreDropMultiplier = player == null ? KunJinKaoSwordItem.DEFAULT_ORE_DROP_MULTIPLIER
                : KunJinKaoSwordItem.getOreDropMultiplier(player.getItemInHand(hand));
        oreDropMultiplierSlider = addRenderableWidget(new OreDropMultiplierSlider(
                leftX + BOX_PAD + btnW + BTN_GAP, contentTop + ROW_H, btnW, BTN_H,
                oreDropMultiplier, this::setOreDropMultiplier));

        // ---------- 左列 · 工具 ----------
        // 刷子 / 锄头铲子 / 避雷针，行为见 SwordToolHandler。
        int toolsTitleY = contentTop + ROW_H + BTN_H + BOX_PAD + SECTION_GAP;
        contentTop = addSection(leftX, toolsTitleY, sectionW, "screen.kunjinkao.section_tools", 2);
        brushToggle = addButton(leftX + BOX_PAD, contentTop, btnW, this::toggleBrush);
        toolModeButton = addButton(leftX + BOX_PAD + btnW + BTN_GAP, contentTop, btnW, this::cycleToolMode);
        lightningRodToggle = addButton(leftX + BOX_PAD, contentTop + ROW_H, sectionW - BOX_PAD * 2,
                this::toggleLightningRod);

        // ---------- 右列 · 处决 ----------
        contentTop = addSection(rightX, panelY + TITLE_H, sectionW, "screen.kunjinkao.section_execution", 1);
        ultimateDeathToggle = addButton(rightX + BOX_PAD, contentTop, btnW, this::toggleUltimateDeath);
        quitStrikeToggle = addButton(rightX + BOX_PAD + btnW + BTN_GAP, contentTop, btnW,
                this::toggleQuitStrike);

        // ---------- 右列 · 放置 ----------
        // 三种核心对应 Construction Wand（建筑手杖）的三个核心，行为见 PlacementCoreHandler。
        int placeTitleY = contentTop + BTN_H + BOX_PAD + SECTION_GAP;
        contentTop = addSection(rightX, placeTitleY, sectionW, "screen.kunjinkao.section_placement", 2);
        constructionWandToggle = addButton(rightX + BOX_PAD, contentTop, btnW, this::toggleConstructionWand);
        angelCoreToggle = addButton(rightX + BOX_PAD + btnW + BTN_GAP, contentTop, btnW, this::toggleAngelCore);
        destructionCoreToggle = addButton(rightX + BOX_PAD, contentTop + ROW_H, btnW,
                this::toggleDestructionCore);
        placementUndoToggle = addButton(rightX + BOX_PAD + btnW + BTN_GAP, contentTop + ROW_H, btnW,
                this::togglePlacementUndo);

        // ---------- 右列 · 加速 ----------
        // shift+右键 立加速场：限时（30 秒）或无限，行为见 SwordTimeAcceleration。
        int accelTitleY = contentTop + ROW_H + BTN_H + BOX_PAD + SECTION_GAP;
        contentTop = addSection(rightX, accelTitleY, sectionW, "screen.kunjinkao.section_acceleration", 1);
        timedAccelToggle = addButton(rightX + BOX_PAD, contentTop, btnW, this::toggleTimedAcceleration);
        infiniteAccelToggle = addButton(rightX + BOX_PAD + btnW + BTN_GAP, contentTop, btnW,
                this::toggleInfiniteAcceleration);

        refreshLabels();
    }

    private static int boxHeight(int rows) {
        return BOX_PAD * 2 + rows * ROW_H - BTN_GAP;
    }

    /**
     * 记录一个「分区标题 + 方框」，返回框内容区第一行按钮的 y。
     * 实际绘制在 {@link #renderBackground} 里做，保证顺序是「背景 → 面板 → 控件」。
     */
    private int addSection(int x, int titleY, int sectionW, String titleKey, int rows) {
        int boxTop = titleY + HEADER_H;
        sectionTitles.add(Component.translatable(titleKey));
        sectionTitlePos.add(new int[]{x, titleY});
        sectionBoxes.add(new int[]{x, boxTop, sectionW, boxHeight(rows)});
        return boxTop + BOX_PAD;
    }

    private Button addButton(int x, int y, int w, Runnable action) {
        return addRenderableWidget(Button.builder(Component.empty(), b -> action.run())
                .bounds(x, y, w, BTN_H)
                .build());
    }

    /**
     * 1.21.1 的 Screen.render 第一步就会调用 renderBackground，随后才画控件。
     * 面板、分区标题与分区方框必须画在这里（即 super.renderBackground 之后），
     * 才能得到「背景只画一遍 → 面板与分区 → 控件」的正确顺序。
     */
    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);

        graphics.fill(panelX, panelY, panelX + panelW, panelY + panelH, COLOR_PANEL);
        drawBorder(graphics, panelX, panelY, panelW, panelH, COLOR_BORDER);
        graphics.drawString(font, title, panelX + 8, panelY + 9, COLOR_TITLE, false);

        for (int i = 0; i < sectionBoxes.size(); i++) {
            int[] box = sectionBoxes.get(i);
            int[] pos = sectionTitlePos.get(i);
            graphics.drawString(font, sectionTitles.get(i), pos[0], pos[1], COLOR_SECTION, false);
            drawBorder(graphics, box[0], box[1], box[2], box[3], COLOR_BORDER);
        }
    }

    private static void drawBorder(GuiGraphics graphics, int x, int y, int w, int h, int color) {
        graphics.fill(x, y, x + w, y + 1, color);
        graphics.fill(x, y + h - 1, x + w, y + h, color);
        graphics.fill(x, y, x + 1, y + h, color);
        graphics.fill(x + w - 1, y, x + w, y + h, color);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // 背景与控件都由 super.render 负责；面板与分区见上面的 renderBackground。
        // 每帧检查一次节流窗口：窗口一过就补发滑条最后的值（相当于拖动结束后的收尾发送）。
        flushPendingSettings(false);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    /**
     * 关屏时强制补发一次被节流跳过的滑条值，避免玩家拖完立刻 ESC 导致最后一次设置丢失。
     */
    @Override
    public void onClose() {
        flushPendingSettings(true);
        super.onClose();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (KunJinKaoKeyBindings.OPEN_SWORD_OPTIONS.getKey().equals(
                com.mojang.blaze3d.platform.InputConstants.getKey(keyCode, scanCode))) {
            onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    // ===== 以下是各选项的操作，逻辑与改动前完全一致 =====

    private void toggleBlueScreenAttack() {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            onClose();
            return;
        }
        ItemStack stack = player.getItemInHand(hand);
        if (!(stack.getItem() instanceof KunJinKaoSwordItem)) {
            onClose();
            return;
        }
        boolean enabled = !KunJinKaoSwordItem.isBlueScreenAttackEnabled(stack);
        // Client-side update is only for immediate button feedback. The server
        // independently validates the held item and remains authoritative.
        KunJinKaoSwordItem.setBlueScreenAttackEnabled(stack, enabled);
        NetworkHandler.sendToServer(new SwordSettingPayload(hand, SwordSettingPayload.BLUE_SCREEN_ATTACK,
                enabled ? 1 : 0));
        refreshLabels();
    }

    /** 把所有按钮文字刷新成「名称：值」。 */
    private void refreshLabels() {
        if (blueScreenToggle == null) {
            return;
        }
        Player player = Minecraft.getInstance().player;
        boolean enabled = player != null
                && KunJinKaoSwordItem.isBlueScreenAttackEnabled(player.getItemInHand(hand));
        blueScreenToggle.setMessage(label("screen.kunjinkao.blue_screen_attack",
                Component.translatable(enabled ? "screen.kunjinkao.switch_on" : "screen.kunjinkao.switch_off")));
        boolean canBreak = player != null
                && KunJinKaoSwordItem.canBreakUnbreakableBlocks(player.getItemInHand(hand));
        if (unbreakableBlockToggle != null) {
            unbreakableBlockToggle.setMessage(label("screen.kunjinkao.break_unbreakable_blocks",
                    Component.translatable(canBreak
                            ? "screen.kunjinkao.switch_on" : "screen.kunjinkao.switch_off")));
        }
        if (ultimateDeathToggle != null) {
            boolean ultimateDeath = player != null
                    && KunJinKaoSwordItem.isUltimateDeathEnabled(player.getItemInHand(hand));
            ultimateDeathToggle.setMessage(label("screen.kunjinkao.ultimate_death",
                    Component.translatable(ultimateDeath
                            ? "screen.kunjinkao.switch_on" : "screen.kunjinkao.switch_off")));
        }
        if (quitStrikeToggle != null) {
            boolean quitStrike = player != null
                    && KunJinKaoSwordItem.isQuitStrikeEnabled(player.getItemInHand(hand));
            quitStrikeToggle.setMessage(label("screen.kunjinkao.quit_strike",
                    Component.translatable(quitStrike
                            ? "screen.kunjinkao.switch_on" : "screen.kunjinkao.switch_off")));
        }
        if (constructionWandToggle != null) {
            boolean wand = player != null
                    && KunJinKaoSwordItem.isConstructionWandEnabled(player.getItemInHand(hand));
            constructionWandToggle.setMessage(label("screen.kunjinkao.construction_wand",
                    Component.translatable(wand ? "screen.kunjinkao.switch_on" : "screen.kunjinkao.switch_off")));
        }
        if (angelCoreToggle != null) {
            boolean angel = player != null
                    && KunJinKaoSwordItem.isAngelCoreEnabled(player.getItemInHand(hand));
            angelCoreToggle.setMessage(label("screen.kunjinkao.angel_core",
                    Component.translatable(angel ? "screen.kunjinkao.switch_on" : "screen.kunjinkao.switch_off")));
        }
        if (destructionCoreToggle != null) {
            boolean destruction = player != null
                    && KunJinKaoSwordItem.isDestructionCoreEnabled(player.getItemInHand(hand));
            destructionCoreToggle.setMessage(label("screen.kunjinkao.destruction_core",
                    Component.translatable(destruction
                            ? "screen.kunjinkao.switch_on" : "screen.kunjinkao.switch_off")));
        }
        if (placementUndoToggle != null) {
            boolean undo = player != null
                    && KunJinKaoSwordItem.isPlacementUndoEnabled(player.getItemInHand(hand));
            placementUndoToggle.setMessage(label("screen.kunjinkao.placement_undo",
                    Component.translatable(undo ? "screen.kunjinkao.switch_on" : "screen.kunjinkao.switch_off")));
        }
        if (brushToggle != null) {
            boolean brush = player != null && KunJinKaoSwordItem.isBrushEnabled(player.getItemInHand(hand));
            brushToggle.setMessage(label("screen.kunjinkao.brush",
                    Component.translatable(brush
                            ? "screen.kunjinkao.switch_on" : "screen.kunjinkao.switch_off")));
        }
        if (toolModeButton != null) {
            int mode = player == null ? SwordToolHandler.TOOL_MODE_OFF
                    : KunJinKaoSwordItem.getToolMode(player.getItemInHand(hand));
            String modeKey = switch (mode) {
                case SwordToolHandler.TOOL_MODE_HOE -> "screen.kunjinkao.tool_mode_hoe";
                case SwordToolHandler.TOOL_MODE_SHOVEL -> "screen.kunjinkao.tool_mode_shovel";
                default -> "screen.kunjinkao.tool_mode_off";
            };
            toolModeButton.setMessage(label("screen.kunjinkao.tool_mode", Component.translatable(modeKey)));
        }
        if (lightningRodToggle != null) {
            boolean rod = player != null
                    && KunJinKaoSwordItem.isLightningRodEnabled(player.getItemInHand(hand));
            lightningRodToggle.setMessage(label("screen.kunjinkao.lightning_rod",
                    Component.translatable(rod
                            ? "screen.kunjinkao.switch_on" : "screen.kunjinkao.switch_off")));
        }
        if (timedAccelToggle != null) {
            int accelMode = player == null ? SwordTimeAcceleration.MODE_OFF
                    : KunJinKaoSwordItem.getTimeAccelMode(player.getItemInHand(hand));
            timedAccelToggle.setMessage(label("screen.kunjinkao.timed_acceleration",
                    Component.translatable(accelMode == SwordTimeAcceleration.MODE_TIMED
                            ? "screen.kunjinkao.switch_on" : "screen.kunjinkao.switch_off")));
        }
        if (infiniteAccelToggle != null) {
            int accelMode = player == null ? SwordTimeAcceleration.MODE_OFF
                    : KunJinKaoSwordItem.getTimeAccelMode(player.getItemInHand(hand));
            infiniteAccelToggle.setMessage(label("screen.kunjinkao.infinite_acceleration",
                    Component.translatable(accelMode == SwordTimeAcceleration.MODE_INFINITE
                            ? "screen.kunjinkao.switch_on" : "screen.kunjinkao.switch_off")));
        }
        if (areaClearModeButton != null) {            KunJinKaoSwordItem.AreaClearTargetMode mode = player == null
                    ? KunJinKaoSwordItem.AreaClearTargetMode.HOSTILE
                    : KunJinKaoSwordItem.getAreaClearTargetMode(player.getItemInHand(hand));
            areaClearModeButton.setMessage(label("screen.kunjinkao.area_clear_targets",
                    Component.translatable(mode.translationKey())));
        }
        if (lootingModeButton != null) {
            int mode = player == null ? 0 : KunJinKaoSwordItem.getLootingMode(player.getItemInHand(hand));
            String modeText = switch (mode) {
                case 1 -> "抢夺 25 级";
                case 2 -> "抢夺 50 级";
                default -> "无抢夺";
            };
            lootingModeButton.setMessage(label("screen.kunjinkao.looting_mode", Component.literal(modeText)));
        }
    }

    private void toggleUnbreakableBlockBreaking() {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            onClose();
            return;
        }
        ItemStack stack = player.getItemInHand(hand);
        if (!(stack.getItem() instanceof KunJinKaoSwordItem)) {
            onClose();
            return;
        }
        boolean enabled = !KunJinKaoSwordItem.canBreakUnbreakableBlocks(stack);
        KunJinKaoSwordItem.setBreakUnbreakableBlocks(stack, enabled);
        NetworkHandler.sendToServer(new SwordSettingPayload(hand, SwordSettingPayload.UNBREAKABLE_BLOCK_BREAKING,
                enabled ? 1 : 0));
        refreshLabels();
    }

    private void toggleUltimateDeath() {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            onClose();
            return;
        }
        ItemStack stack = player.getItemInHand(hand);
        if (!(stack.getItem() instanceof KunJinKaoSwordItem)) {
            onClose();
            return;
        }
        boolean enabled = !KunJinKaoSwordItem.isUltimateDeathEnabled(stack);
        KunJinKaoSwordItem.setUltimateDeathEnabled(stack, enabled);
        NetworkHandler.sendToServer(new SwordSettingPayload(hand, SwordSettingPayload.ULTIMATE_DEATH,
                enabled ? 1 : 0));
        refreshLabels();
    }

    private void toggleQuitStrike() {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            onClose();
            return;
        }
        ItemStack stack = player.getItemInHand(hand);
        if (!(stack.getItem() instanceof KunJinKaoSwordItem)) {
            onClose();
            return;
        }
        boolean enabled = !KunJinKaoSwordItem.isQuitStrikeEnabled(stack);
        KunJinKaoSwordItem.setQuitStrikeEnabled(stack, enabled);
        NetworkHandler.sendToServer(new SwordSettingPayload(hand, SwordSettingPayload.QUIT_STRIKE,
                enabled ? 1 : 0));
        refreshLabels();
    }

    // ===== 放置类核心（对应 Construction Wand 的三种核心）=====

    /** 建筑手杖：右键方块面时沿该面延伸放置一排方块。 */
    private void toggleConstructionWand() {
        Player player = Minecraft.getInstance().player;
        ItemStack stack = player == null ? ItemStack.EMPTY : player.getItemInHand(hand);
        if (!(stack.getItem() instanceof KunJinKaoSwordItem)) {
            onClose();
            return;
        }
        boolean enabled = !KunJinKaoSwordItem.isConstructionWandEnabled(stack);
        KunJinKaoSwordItem.setConstructionWandEnabled(stack, enabled);
        NetworkHandler.sendToServer(new SwordSettingPayload(hand, SwordSettingPayload.CONSTRUCTION_WAND,
                enabled ? 1 : 0));
        refreshLabels();
    }

    /** 天使核心：把方块放到所视方块的背面，或对空在半空放置。 */
    private void toggleAngelCore() {
        Player player = Minecraft.getInstance().player;
        ItemStack stack = player == null ? ItemStack.EMPTY : player.getItemInHand(hand);
        if (!(stack.getItem() instanceof KunJinKaoSwordItem)) {
            onClose();
            return;
        }
        boolean enabled = !KunJinKaoSwordItem.isAngelCoreEnabled(stack);
        KunJinKaoSwordItem.setAngelCoreEnabled(stack, enabled);
        NetworkHandler.sendToServer(new SwordSettingPayload(hand, SwordSettingPayload.ANGEL_CORE,
                enabled ? 1 : 0));
        refreshLabels();
    }

    /** 破坏核心：挖掉一格时连带清除面向那一侧的整排方块。 */
    private void toggleDestructionCore() {
        Player player = Minecraft.getInstance().player;
        ItemStack stack = player == null ? ItemStack.EMPTY : player.getItemInHand(hand);
        if (!(stack.getItem() instanceof KunJinKaoSwordItem)) {
            onClose();
            return;
        }
        boolean enabled = !KunJinKaoSwordItem.isDestructionCoreEnabled(stack);
        KunJinKaoSwordItem.setDestructionCoreEnabled(stack, enabled);
        NetworkHandler.sendToServer(new SwordSettingPayload(hand, SwordSettingPayload.DESTRUCTION_CORE,
                enabled ? 1 : 0));
        refreshLabels();
    }

    /**
     * 撤销开关：开启后按 K 键可撤销最近 10 步内的放置/破坏。
     * 这里只改物品标记与同步给服务端，撤销本身由服务端的历史栈执行。
     */
    private void togglePlacementUndo() {
        Player player = Minecraft.getInstance().player;
        ItemStack stack = player == null ? ItemStack.EMPTY : player.getItemInHand(hand);
        if (!(stack.getItem() instanceof KunJinKaoSwordItem)) {
            onClose();
            return;
        }
        boolean enabled = !KunJinKaoSwordItem.isPlacementUndoEnabled(stack);
        KunJinKaoSwordItem.setPlacementUndoEnabled(stack, enabled);
        NetworkHandler.sendToServer(new SwordSettingPayload(hand, SwordSettingPayload.PLACEMENT_UNDO,
                enabled ? 1 : 0));
        refreshLabels();
    }

    // ===== 工具类行为（刷子 / 锄头铲子 / 避雷针）=====

    /** 刷子：长按右键可疑的沙/砾石即可刷取。 */
    private void toggleBrush() {
        Player player = Minecraft.getInstance().player;
        ItemStack stack = player == null ? ItemStack.EMPTY : player.getItemInHand(hand);
        if (!(stack.getItem() instanceof KunJinKaoSwordItem)) {
            onClose();
            return;
        }
        boolean enabled = !KunJinKaoSwordItem.isBrushEnabled(stack);
        KunJinKaoSwordItem.setBrushEnabled(stack, enabled);
        NetworkHandler.sendToServer(new SwordSettingPayload(hand, SwordSettingPayload.BRUSH,
                enabled ? 1 : 0));
        refreshLabels();
    }

    /** 锄头 / 铲子循环：关 → 锄头（→耕地）→ 铲子（→草径）→ 关。 */
    private void cycleToolMode() {
        Player player = Minecraft.getInstance().player;
        ItemStack stack = player == null ? ItemStack.EMPTY : player.getItemInHand(hand);
        if (!(stack.getItem() instanceof KunJinKaoSwordItem)) {
            onClose();
            return;
        }
        int next = (KunJinKaoSwordItem.getToolMode(stack) + 1) % SwordToolHandler.TOOL_MODE_COUNT;
        KunJinKaoSwordItem.setToolMode(stack, next);
        NetworkHandler.sendToServer(new SwordSettingPayload(hand, SwordSettingPayload.TOOL_MODE, next));
        refreshLabels();
    }

    /** 避雷针：右键避雷针召唤闪电。 */
    private void toggleLightningRod() {
        Player player = Minecraft.getInstance().player;
        ItemStack stack = player == null ? ItemStack.EMPTY : player.getItemInHand(hand);
        if (!(stack.getItem() instanceof KunJinKaoSwordItem)) {
            onClose();
            return;
        }
        boolean enabled = !KunJinKaoSwordItem.isLightningRodEnabled(stack);
        KunJinKaoSwordItem.setLightningRodEnabled(stack, enabled);
        NetworkHandler.sendToServer(new SwordSettingPayload(hand, SwordSettingPayload.LIGHTNING_ROD,
                enabled ? 1 : 0));
        refreshLabels();
    }

    // ===== 时间加速（shift+右键 立加速场）=====

    /**
     * 写入加速模式。
     * <p>
     * 模式是<b>单值</b>而不是两个独立开关 —— 限时与无限本来就互斥，
     * 用一个三值字段（关/限时/无限）表达，就天然不会出现两个都亮着的情况。
     */
    private void applyTimeAccelMode(int mode) {
        Player player = Minecraft.getInstance().player;
        ItemStack stack = player == null ? ItemStack.EMPTY : player.getItemInHand(hand);
        if (!(stack.getItem() instanceof KunJinKaoSwordItem)) {
            onClose();
            return;
        }
        KunJinKaoSwordItem.setTimeAccelMode(stack, mode);
        NetworkHandler.sendToServer(new SwordSettingPayload(hand, SwordSettingPayload.TIME_ACCEL_MODE, mode));
        refreshLabels();
    }

    /** 限时加速：shift+右键 立起一个持续 30 秒的加速场。 */
    private void toggleTimedAcceleration() {
        Player player = Minecraft.getInstance().player;
        int current = player == null ? SwordTimeAcceleration.MODE_OFF
                : KunJinKaoSwordItem.getTimeAccelMode(player.getItemInHand(hand));
        applyTimeAccelMode(current == SwordTimeAcceleration.MODE_TIMED
                ? SwordTimeAcceleration.MODE_OFF : SwordTimeAcceleration.MODE_TIMED);
    }

    /** 无限加速：shift+右键 立起的加速场一直持续，直到再 shift+右键 同一位置停掉。 */
    private void toggleInfiniteAcceleration() {
        Player player = Minecraft.getInstance().player;
        int current = player == null ? SwordTimeAcceleration.MODE_OFF
                : KunJinKaoSwordItem.getTimeAccelMode(player.getItemInHand(hand));
        applyTimeAccelMode(current == SwordTimeAcceleration.MODE_INFINITE
                ? SwordTimeAcceleration.MODE_OFF : SwordTimeAcceleration.MODE_INFINITE);
    }

    private void setMiningSpeed(int speed) {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            onClose();
            return;
        }
        ItemStack stack = player.getItemInHand(hand);
        if (!(stack.getItem() instanceof KunJinKaoSwordItem)) {
            onClose();
            return;
        }
        KunJinKaoSwordItem.setMiningSpeed(stack, speed);
        // 该 settingId 走节流：拖动过程中同档碎变化合并，松手/窗口过后补发最后值。
        sendSettingValue(SwordSettingPayload.MINING_SPEED, speed);
    }

    private void setOreDropMultiplier(int multiplier) {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            onClose();
            return;
        }
        ItemStack stack = player.getItemInHand(hand);
        if (!(stack.getItem() instanceof KunJinKaoSwordItem)) {
            onClose();
            return;
        }
        KunJinKaoSwordItem.setOreDropMultiplier(stack, multiplier);
        sendSettingValue(SwordSettingPayload.ORE_DROP_MULTIPLIER, multiplier);
    }

    private void setAttackDamageLimit(int damageLimit) {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            onClose();
            return;
        }
        ItemStack stack = player.getItemInHand(hand);
        if (!(stack.getItem() instanceof KunJinKaoSwordItem)) {
            onClose();
            return;
        }
        KunJinKaoSwordItem.setAttackDamageLimit(stack, damageLimit);
        sendSettingValue(SwordSettingPayload.ATTACK_DAMAGE_LIMIT, damageLimit);
    }

    private void cycleAreaClearTargetMode() {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            onClose();
            return;
        }
        ItemStack stack = player.getItemInHand(hand);
        if (!(stack.getItem() instanceof KunJinKaoSwordItem)) {
            onClose();
            return;
        }
        KunJinKaoSwordItem.AreaClearTargetMode[] modes = KunJinKaoSwordItem.AreaClearTargetMode.values();
        KunJinKaoSwordItem.AreaClearTargetMode current = KunJinKaoSwordItem.getAreaClearTargetMode(stack);
        KunJinKaoSwordItem.AreaClearTargetMode next = modes[(current.ordinal() + 1) % modes.length];
        KunJinKaoSwordItem.setAreaClearTargetMode(stack, next);
        NetworkHandler.sendToServer(new SwordSettingPayload(hand, SwordSettingPayload.AREA_CLEAR_TARGET_MODE,
                next.id()));
        refreshLabels();
    }

    private void cycleLootingMode() {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            onClose();
            return;
        }
        ItemStack stack = player.getItemInHand(hand);
        if (!(stack.getItem() instanceof KunJinKaoSwordItem)) {
            onClose();
            return;
        }
        int nextMode = (KunJinKaoSwordItem.getLootingMode(stack) + 1) % 3;
        KunJinKaoSwordItem.setLootingMode(stack, nextMode);
        NetworkHandler.sendToServer(new SwordSettingPayload(hand, SwordSettingPayload.LOOTING_MODE, nextMode));
        refreshLabels();
    }

    /** 滑条专用：带节流地发送剑设置（离散开关不走这里）。 */
    private void sendSettingValue(int settingId, int value) {
        long now = System.currentTimeMillis();
        boolean withinInterval = now - lastSendMillis[settingId] < SEND_INTERVAL_MS;
        boolean crossedTier = Math.abs(value - lastSentValue[settingId]) >= tierWidthOf(settingId);
        if (withinInterval && !crossedTier) {
            hasPending[settingId] = true;
            pendingValue[settingId] = value;
            return;
        }
        hasPending[settingId] = false;
        lastSendMillis[settingId] = now;
        lastSentValue[settingId] = value;
        NetworkHandler.sendToServer(new SwordSettingPayload(hand, settingId, value));
    }

    /**
     * 补发被节流跳过的值。{@code force} 为 true 时立刻发（关屏），
     * 否则只在 100ms 窗口过去后发（render 每帧调用一次，拖完自然会收尾）。
     */
    private void flushPendingSettings(boolean force) {
        if (Minecraft.getInstance().player == null) {
            // 已经退图/断线：服务端不会再接受设置，丢弃待发值，别往空连接上发包。
            Arrays.fill(hasPending, false);
            return;
        }
        long now = System.currentTimeMillis();
        for (int settingId = 0; settingId < SETTING_COUNT; settingId++) {
            if (!hasPending[settingId]) {
                continue;
            }
            if (!force && now - lastSendMillis[settingId] < SEND_INTERVAL_MS) {
                continue;
            }
            hasPending[settingId] = false;
            lastSendMillis[settingId] = now;
            lastSentValue[settingId] = pendingValue[settingId];
            NetworkHandler.sendToServer(new SwordSettingPayload(hand, settingId, pendingValue[settingId]));
        }
    }

    /**
     * "一档"的宽度：把设置的取值范围等分成 {@link #TIER_COUNT} 档。
     * 数值相对上次发送跨过至少一档就立刻发送（拖得快时界面数字仍能跟上），
     * 同一档内的碎变化在 100ms 窗口内合并。
     */
    private static int tierWidthOf(int settingId) {
        int min = 0;
        int max = 1;
        if (settingId == SwordSettingPayload.MINING_SPEED) {
            min = KunJinKaoSwordItem.MIN_MINING_SPEED;
            max = KunJinKaoSwordItem.MAX_MINING_SPEED;
        } else if (settingId == SwordSettingPayload.ORE_DROP_MULTIPLIER) {
            min = KunJinKaoSwordItem.MIN_ORE_DROP_MULTIPLIER;
            max = KunJinKaoSwordItem.MAX_ORE_DROP_MULTIPLIER;
        } else if (settingId == SwordSettingPayload.ATTACK_DAMAGE_LIMIT) {
            min = 0;
            max = KunJinKaoSwordItem.MAX_ATTACK_DAMAGE_LIMIT;
        }
        return Math.max(1, (max - min) / TIER_COUNT);
    }

    // ===== 三个滑条（逻辑未变，只把文字改成「名称：值」）=====

    private static final class MiningSpeedSlider extends AbstractSliderButton {

        private final IntConsumer onSpeedChanged;
        private int currentSpeed;

        private MiningSpeedSlider(int x, int y, int width, int height, int speed, IntConsumer onSpeedChanged) {
            super(x, y, width, height, Component.empty(), toSliderValue(speed));
            this.currentSpeed = speed;
            this.onSpeedChanged = onSpeedChanged;
            updateMessage();
        }

        @Override
        protected void updateMessage() {
            setMessage(label("screen.kunjinkao.mining_speed", Component.literal(Integer.toString(currentSpeed))));
        }

        @Override
        protected void applyValue() {
            int speed = fromSliderValue(value);
            if (speed != currentSpeed) {
                currentSpeed = speed;
                onSpeedChanged.accept(speed);
            }
            updateMessage();
        }

        private static double toSliderValue(int speed) {
            int clamped = Math.max(KunJinKaoSwordItem.MIN_MINING_SPEED,
                    Math.min(KunJinKaoSwordItem.MAX_MINING_SPEED, speed));
            return (clamped - KunJinKaoSwordItem.MIN_MINING_SPEED)
                    / (double) (KunJinKaoSwordItem.MAX_MINING_SPEED - KunJinKaoSwordItem.MIN_MINING_SPEED);
        }

        private static int fromSliderValue(double value) {
            return KunJinKaoSwordItem.MIN_MINING_SPEED + (int) Math.round(value
                    * (KunJinKaoSwordItem.MAX_MINING_SPEED - KunJinKaoSwordItem.MIN_MINING_SPEED));
        }
    }

    private static final class OreDropMultiplierSlider extends AbstractSliderButton {

        private final IntConsumer onMultiplierChanged;
        private int currentMultiplier;

        private OreDropMultiplierSlider(int x, int y, int width, int height, int multiplier,
                                        IntConsumer onMultiplierChanged) {
            super(x, y, width, height, Component.empty(), toSliderValue(multiplier));
            this.currentMultiplier = multiplier;
            this.onMultiplierChanged = onMultiplierChanged;
            updateMessage();
        }

        @Override
        protected void updateMessage() {
            setMessage(label("screen.kunjinkao.ore_drop_multiplier",
                    Component.literal(Integer.toString(currentMultiplier))));
        }

        @Override
        protected void applyValue() {
            int multiplier = fromSliderValue(value);
            if (multiplier != currentMultiplier) {
                currentMultiplier = multiplier;
                onMultiplierChanged.accept(multiplier);
            }
            updateMessage();
        }

        private static double toSliderValue(int multiplier) {
            int clamped = Math.max(KunJinKaoSwordItem.MIN_ORE_DROP_MULTIPLIER,
                    Math.min(KunJinKaoSwordItem.MAX_ORE_DROP_MULTIPLIER, multiplier));
            return (clamped - KunJinKaoSwordItem.MIN_ORE_DROP_MULTIPLIER)
                    / (double) (KunJinKaoSwordItem.MAX_ORE_DROP_MULTIPLIER
                    - KunJinKaoSwordItem.MIN_ORE_DROP_MULTIPLIER);
        }

        private static int fromSliderValue(double value) {
            return KunJinKaoSwordItem.MIN_ORE_DROP_MULTIPLIER + (int) Math.round(value
                    * (KunJinKaoSwordItem.MAX_ORE_DROP_MULTIPLIER
                    - KunJinKaoSwordItem.MIN_ORE_DROP_MULTIPLIER));
        }
    }

    private static final class AttackDamageSlider extends AbstractSliderButton {
        private final IntConsumer onDamageChanged;
        private int currentDamage;

        private AttackDamageSlider(int x, int y, int width, int height, int damage, IntConsumer onDamageChanged) {
            super(x, y, width, height, Component.empty(), damage / (double) KunJinKaoSwordItem.MAX_ATTACK_DAMAGE_LIMIT);
            this.currentDamage = damage;
            this.onDamageChanged = onDamageChanged;
            updateMessage();
        }

        @Override
        protected void updateMessage() {
            Component valueText = currentDamage >= KunJinKaoSwordItem.MAX_ATTACK_DAMAGE_LIMIT
                    ? Component.translatable("screen.kunjinkao.unlimited")
                    : Component.literal(Integer.toString(currentDamage));
            setMessage(label("screen.kunjinkao.attack_damage_limit", valueText));
        }

        @Override
        protected void applyValue() {
            int damage = Math.max(0, Math.min(KunJinKaoSwordItem.MAX_ATTACK_DAMAGE_LIMIT,
                    (int) Math.round(value * KunJinKaoSwordItem.MAX_ATTACK_DAMAGE_LIMIT)));
            if (damage != currentDamage) {
                currentDamage = damage;
                onDamageChanged.accept(damage);
            }
            updateMessage();
        }
    }
}
