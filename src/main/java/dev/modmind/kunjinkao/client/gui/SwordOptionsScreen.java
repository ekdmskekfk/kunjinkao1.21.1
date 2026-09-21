package dev.modmind.kunjinkao.client.gui;

import dev.modmind.kunjinkao.KunJinKaoSwordItem;
import dev.modmind.kunjinkao.client.KunJinKaoKeyBindings;
import dev.modmind.kunjinkao.network.NetworkHandler;
// 9 个剑设置包已合并为 SwordSettingPayload，导入与发包点一起收敛。
import dev.modmind.kunjinkao.network.SwordSettingPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.function.IntConsumer;

/**
 * Small in-game settings screen opened while holding the administrator sword.
 * The blue-screen option is an item flag only; it never launches a local program.
 */
public final class SwordOptionsScreen extends Screen {

    private static final int PANEL_WIDTH = 260;
    private static final int PANEL_HEIGHT = 170;

    private final InteractionHand hand;
    private Button blueScreenToggle;
    private Button unbreakableBlockToggle;
    private Button ultimateDeathToggle;
    private Button quitStrikeToggle;
    private Button areaClearModeButton;
    private Button lootingModeButton;
    private MiningSpeedSlider miningSpeedSlider;
    private OreDropMultiplierSlider oreDropMultiplierSlider;
    private AttackDamageSlider attackDamageSlider;
    private SettingOption selectedOption = SettingOption.BLUE_SCREEN_ATTACK;

    // ===== 滑条发包节流（本次新增）=====
    // 三个滑条原来每变化一格就发一个包，拖一次最坏约 1000 个包（掉落倍数 1..1000）。
    // 规则：同一 settingId 距上次真正发送 < 100ms，且数值相对上次发送没有跨过一"档"时跳过；
    // 被跳过的数值记在 pending 里，render 每帧检查（窗口一过就补发）并在关屏时强制补发一次，
    // 保证玩家最终选定的值一定到达服务端，不会因为节流丢设置。
    // 开关/循环按钮不走节流：它们是离散操作，连点两下必须发两次。
    private static final int SETTING_COUNT = 9;
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

    @Override
    protected void init() {
        int panelX = (width - PANEL_WIDTH) / 2;
        int panelY = (height - PANEL_HEIGHT) / 2;
        addRenderableWidget(Button.builder(Component.literal("<"), button -> cycleOption(-1))
                .bounds(panelX + 16, panelY + 12, 20, 20)
                .build());
        addRenderableWidget(Button.builder(Component.literal(">"), button -> cycleOption(1))
                .bounds(panelX + PANEL_WIDTH - 36, panelY + 12, 20, 20)
                .build());
        blueScreenToggle = Button.builder(Component.empty(), button -> toggleBlueScreenAttack())
                .bounds(panelX + 80, panelY + 105, 100, 20)
                .build();
        addRenderableWidget(blueScreenToggle);
        unbreakableBlockToggle = Button.builder(Component.empty(), button -> toggleUnbreakableBlockBreaking())
                .bounds(panelX + 80, panelY + 105, 100, 20)
                .build();
        addRenderableWidget(unbreakableBlockToggle);
        ultimateDeathToggle = Button.builder(Component.empty(), button -> toggleUltimateDeath())
                .bounds(panelX + 80, panelY + 105, 100, 20)
                .build();
        addRenderableWidget(ultimateDeathToggle);
        quitStrikeToggle = Button.builder(Component.empty(), button -> toggleQuitStrike())
                .bounds(panelX + 80, panelY + 105, 100, 20)
                .build();
        addRenderableWidget(quitStrikeToggle);
        areaClearModeButton = Button.builder(Component.empty(), button -> cycleAreaClearTargetMode())
                .bounds(panelX + 55, panelY + 105, 150, 20)
                .build();
        addRenderableWidget(areaClearModeButton);
        Player player = Minecraft.getInstance().player;
        int miningSpeed = player == null ? KunJinKaoSwordItem.DEFAULT_MINING_SPEED
                : KunJinKaoSwordItem.getMiningSpeed(player.getItemInHand(hand));
        miningSpeedSlider = addRenderableWidget(new MiningSpeedSlider(panelX + 30, panelY + 105, 200, 20,
                miningSpeed, this::setMiningSpeed));
        int oreDropMultiplier = player == null ? KunJinKaoSwordItem.DEFAULT_ORE_DROP_MULTIPLIER
                : KunJinKaoSwordItem.getOreDropMultiplier(player.getItemInHand(hand));
        oreDropMultiplierSlider = addRenderableWidget(new OreDropMultiplierSlider(panelX + 30, panelY + 105, 200, 20,
                oreDropMultiplier, this::setOreDropMultiplier));
        int attackDamage = player == null ? KunJinKaoSwordItem.DEFAULT_ATTACK_DAMAGE_LIMIT
                : KunJinKaoSwordItem.getAttackDamageLimit(player.getItemInHand(hand));
        attackDamageSlider = addRenderableWidget(new AttackDamageSlider(panelX + 30, panelY + 105, 200, 20,
                attackDamage, this::setAttackDamageLimit));
        lootingModeButton = Button.builder(Component.empty(), button -> cycleLootingMode())
                .bounds(panelX + 55, panelY + 105, 150, 20)
                .build();
        addRenderableWidget(lootingModeButton);
        refreshToggleLabel();
        refreshOptionVisibility();
    }

    /**
     * 1.21.1 的 Screen.render 第一步就会调用 renderBackground，随后才画控件。
     * 面板与选项名必须画在这里（即 super.renderBackground 之后），才能得到
     * "背景只画一遍 → 面板 → 控件"的正确顺序：画到 super.render 之后会被 0xE0 的面板
     * 盖住滑动条与开关按钮，画到 super.render 之前又会被背景纹理压暗（原 bug）。
     */
    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // 不再手动调用 renderBackground：背景由 super.render 内部负责，只保留这一次绘制
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        int panelX = (width - PANEL_WIDTH) / 2;
        int panelY = (height - PANEL_HEIGHT) / 2;
        graphics.fill(panelX, panelY, panelX + PANEL_WIDTH, panelY + PANEL_HEIGHT, 0xE0122030);
        graphics.fill(panelX, panelY, panelX + PANEL_WIDTH, panelY + 1, 0xFF57CFFF);
        graphics.fill(panelX, panelY + PANEL_HEIGHT - 1, panelX + PANEL_WIDTH, panelY + PANEL_HEIGHT, 0xFF57CFFF);
        graphics.fill(panelX, panelY, panelX + 1, panelY + PANEL_HEIGHT, 0xFF57CFFF);
        graphics.fill(panelX + PANEL_WIDTH - 1, panelY, panelX + PANEL_WIDTH, panelY + PANEL_HEIGHT, 0xFF57CFFF);
        graphics.drawCenteredString(font, title, width / 2, panelY + 18, 0xFFE9FBFF);
        Component optionName = switch (selectedOption) {
            case BLUE_SCREEN_ATTACK -> Component.translatable("screen.kunjinkao.blue_screen_attack");
            case MINING_SPEED -> Component.translatable("screen.kunjinkao.mining_speed");
            case ORE_DROP_MULTIPLIER -> Component.literal("矿石掉落倍数");
            case LOOTING_MODE -> Component.literal("抢夺模式");
            case BREAK_UNBREAKABLE_BLOCKS -> Component.translatable("screen.kunjinkao.break_unbreakable_blocks");
            case AREA_CLEAR_TARGETS -> Component.translatable("screen.kunjinkao.area_clear_targets");
            // Keep the newly added control readable even in older installations
            // that still carry the project's malformed legacy zh_cn resource.
            case ATTACK_DAMAGE_LIMIT -> Component.literal("单次伤害上限");
            case ULTIMATE_DEATH -> Component.translatable("screen.kunjinkao.ultimate_death");
            case QUIT_STRIKE -> Component.translatable("screen.kunjinkao.quit_strike");
        };
        graphics.drawCenteredString(font, optionName, width / 2, panelY + 73, 0xFF8FEAFF);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // 背景与控件都由 super.render 负责；面板与选项名见上面的 renderBackground
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
        refreshToggleLabel();
    }

    private void refreshToggleLabel() {
        if (blueScreenToggle == null) {
            return;
        }
        Player player = Minecraft.getInstance().player;
        boolean enabled = player != null
                && KunJinKaoSwordItem.isBlueScreenAttackEnabled(player.getItemInHand(hand));
        blueScreenToggle.setMessage(Component.translatable(
                enabled ? "screen.kunjinkao.switch_on" : "screen.kunjinkao.switch_off"));
        boolean canBreak = player != null
                && KunJinKaoSwordItem.canBreakUnbreakableBlocks(player.getItemInHand(hand));
        if (unbreakableBlockToggle != null) {
            unbreakableBlockToggle.setMessage(Component.translatable(
                    canBreak ? "screen.kunjinkao.switch_on" : "screen.kunjinkao.switch_off"));
        }
        if (ultimateDeathToggle != null) {
            boolean ultimateDeath = player != null
                    && KunJinKaoSwordItem.isUltimateDeathEnabled(player.getItemInHand(hand));
            ultimateDeathToggle.setMessage(Component.translatable(
                    ultimateDeath ? "screen.kunjinkao.switch_on" : "screen.kunjinkao.switch_off"));
        }
        if (quitStrikeToggle != null) {
            boolean quitStrike = player != null
                    && KunJinKaoSwordItem.isQuitStrikeEnabled(player.getItemInHand(hand));
            quitStrikeToggle.setMessage(Component.translatable(
                    quitStrike ? "screen.kunjinkao.switch_on" : "screen.kunjinkao.switch_off"));
        }
        if (areaClearModeButton != null) {
            KunJinKaoSwordItem.AreaClearTargetMode mode = player == null
                    ? KunJinKaoSwordItem.AreaClearTargetMode.HOSTILE
                    : KunJinKaoSwordItem.getAreaClearTargetMode(player.getItemInHand(hand));
            areaClearModeButton.setMessage(Component.translatable("screen.kunjinkao.area_clear_target_value",
                    Component.translatable(mode.translationKey())));
        }
        if (lootingModeButton != null) {
            int mode = player == null ? 0 : KunJinKaoSwordItem.getLootingMode(player.getItemInHand(hand));
            String modeText = switch (mode) {
                case 1 -> "抢夺 25 级";
                case 2 -> "抢夺 50 级";
                default -> "无抢夺";
            };
            lootingModeButton.setMessage(Component.literal(modeText));
        }
    }

    private void cycleOption(int direction) {
        SettingOption[] options = SettingOption.values();
        selectedOption = options[Math.floorMod(selectedOption.ordinal() + direction, options.length)];
        refreshOptionVisibility();
    }

    private void refreshOptionVisibility() {
        boolean blueScreenSelected = selectedOption == SettingOption.BLUE_SCREEN_ATTACK;
        boolean unbreakableBlocksSelected = selectedOption == SettingOption.BREAK_UNBREAKABLE_BLOCKS;
        boolean areaClearTargetsSelected = selectedOption == SettingOption.AREA_CLEAR_TARGETS;
        boolean lootingModeSelected = selectedOption == SettingOption.LOOTING_MODE;
        boolean ultimateDeathSelected = selectedOption == SettingOption.ULTIMATE_DEATH;
        boolean quitStrikeSelected = selectedOption == SettingOption.QUIT_STRIKE;
        if (blueScreenToggle != null) {
            blueScreenToggle.visible = blueScreenSelected;
        }
        if (unbreakableBlockToggle != null) {
            unbreakableBlockToggle.visible = unbreakableBlocksSelected;
        }
        if (ultimateDeathToggle != null) {
            ultimateDeathToggle.visible = ultimateDeathSelected;
        }
        if (quitStrikeToggle != null) {
            quitStrikeToggle.visible = quitStrikeSelected;
        }
        if (miningSpeedSlider != null) {
            miningSpeedSlider.visible = selectedOption == SettingOption.MINING_SPEED;
        }
        if (oreDropMultiplierSlider != null) {
            oreDropMultiplierSlider.visible = selectedOption == SettingOption.ORE_DROP_MULTIPLIER;
        }
        if (attackDamageSlider != null) {
            attackDamageSlider.visible = selectedOption == SettingOption.ATTACK_DAMAGE_LIMIT;
        }
        if (areaClearModeButton != null) {
            areaClearModeButton.visible = areaClearTargetsSelected;
        }
        if (lootingModeButton != null) {
            lootingModeButton.visible = lootingModeSelected;
        }
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
        refreshToggleLabel();
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
        refreshToggleLabel();
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
        refreshToggleLabel();
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
        refreshToggleLabel();
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
        refreshToggleLabel();
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
            setMessage(Component.translatable("screen.kunjinkao.mining_speed_value", currentSpeed));
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
            setMessage(Component.literal("掉落倍数：" + currentMultiplier + " 倍"));
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
            String valueText = currentDamage >= KunJinKaoSwordItem.MAX_ATTACK_DAMAGE_LIMIT
                    ? "无限" : Integer.toString(currentDamage);
            setMessage(Component.literal("伤害：" + valueText));
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

    private enum SettingOption {
        BLUE_SCREEN_ATTACK,
        MINING_SPEED,
        ORE_DROP_MULTIPLIER,
        LOOTING_MODE,
        BREAK_UNBREAKABLE_BLOCKS,
        AREA_CLEAR_TARGETS,
        ATTACK_DAMAGE_LIMIT,
        ULTIMATE_DEATH,
        QUIT_STRIKE
    }
}
