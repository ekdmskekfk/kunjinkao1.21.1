package dev.modmind.kunjinkao.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * 本模组所有界面的基类：负责"壳"，子类只负责内容。
 *
 * <p>壳包含：面板底、双层描边、扫描线、四角括号、标题栏（乱码记号 + 标题 + 渐隐线）。
 * 子类实现 {@link #renderPanelBody} 在标题栏下方画自己的东西，用
 * {@link #contentX()} / {@link #contentY()} / {@link #contentWidth()} 取内容区坐标，
 * 这样四个界面的边距、标题位置、装饰完全一致。</p>
 *
 * <p>面板尺寸默认占屏幕的 {@link #WIDTH_RATIO} × {@link #HEIGHT_RATIO}，
 * 子类可以覆写 {@link #panelWidth()} / {@link #panelHeight()} 指定固定尺寸
 * （小窗口用固定尺寸更好看，大界面用比例更合适）。</p>
 */
public abstract class KunJinKaoScreen extends Screen {

    /** 面板宽占屏幕宽的比例。 */
    protected static final float WIDTH_RATIO = 0.72F;
    /** 面板高占屏幕高的比例。 */
    protected static final float HEIGHT_RATIO = 0.78F;

    /** 面板矩形（在 {@link #init()} 里算好）。 */
    private int panelX;
    private int panelY;
    private int panelW;
    private int panelH;

    protected KunJinKaoScreen(Component title) {
        super(title);
    }

    // ===== 子类可覆写的尺寸 =====

    /** 返回 -1 表示按屏幕比例算。 */
    protected int panelWidth() {
        return -1;
    }

    /** 返回 -1 表示按屏幕比例算。 */
    protected int panelHeight() {
        return -1;
    }

    @Override
    protected void init() {
        int w = panelWidth() > 0 ? panelWidth() : Math.round(this.width * WIDTH_RATIO);
        int h = panelHeight() > 0 ? panelHeight() : Math.round(this.height * HEIGHT_RATIO);
        // 别超出屏幕
        w = Math.min(w, this.width - 8);
        h = Math.min(h, this.height - 8);
        this.panelW = w;
        this.panelH = h;
        this.panelX = (this.width - w) / 2;
        this.panelY = (this.height - h) / 2;
        initPanel();
    }

    /** 子类在这里建控件。此时 {@link #panelX()} 等已经可用。 */
    protected abstract void initPanel();

    /**
     * 背景与面板壳。1.21.1 的 {@code Screen.render} 会先调它、再画控件，
     * 所以壳与内容都画在这里，控件天然盖在最上层。
     */
    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        KunJinKaoUi.panel(graphics, panelX, panelY, panelW, panelH);
        KunJinKaoUi.titleBar(graphics, font, title, panelX, panelY, panelW);
        renderPanelBody(graphics, mouseX, mouseY, partialTick);
    }

    /** 子类在这里画内容（标题栏以下）。 */
    protected abstract void renderPanelBody(GuiGraphics graphics, int mouseX, int mouseY, float partialTick);

    // ===== 坐标访问 =====

    public int panelX() {
        return panelX;
    }

    public int panelY() {
        return panelY;
    }

    public int panelW() {
        return panelW;
    }

    public int panelH() {
        return panelH;
    }

    /** 内容区左边界。 */
    public int contentX() {
        return panelX + KunJinKaoUi.PAD;
    }

    /** 内容区上边界（标题栏之下）。 */
    public int contentY() {
        return panelY + KunJinKaoUi.TITLE_BAR + KunJinKaoUi.PAD;
    }

    /** 内容区宽度。 */
    public int contentWidth() {
        return panelW - KunJinKaoUi.PAD * 2;
    }

    /** 内容区底边界。 */
    public int contentBottom() {
        return panelY + panelH - KunJinKaoUi.PAD;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
