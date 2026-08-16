package dev.modmind.kunjinkao.block;

import dev.modmind.kunjinkao.network.NetworkHandler;
import dev.modmind.kunjinkao.network.TimeAcceleratorConfigPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntFunction;

/**
 * 时间加速器配置 GUI：两个滑轮（滚轮滚动选择）。
 * 左轮：加速倍率 4x~1024x；右轮：加速范围 3x3x3~9x9x9。
 */
public class TimeAcceleratorScreen extends Screen {

    private final BlockPos pos;
    private int multiplierIndex;
    private int sizeIndex;

    public TimeAcceleratorScreen(BlockPos pos) {
        super(Component.translatable("block.kunjinkao.time_accelerator"));
        this.pos = pos;
        if (Minecraft.getInstance().level != null
                && Minecraft.getInstance().level.getBlockEntity(pos) instanceof TimeAcceleratorBlockEntity be) {
            this.multiplierIndex = be.getMultiplierIndex();
            this.sizeIndex = be.getSizeIndex();
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        int delta = (int) -Math.signum(verticalAmount);
        if (delta == 0) {
            return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
        }
        // 左轮：倍率
        if (mouseX >= this.width / 2.0 - 130 && mouseX <= this.width / 2.0 - 20) {
            multiplierIndex = Math.floorMod(multiplierIndex + delta, TimeAcceleratorBlockEntity.MULTIPLIERS.length);
            sendConfig();
            return true;
        }
        // 右轮：范围
        if (mouseX >= this.width / 2.0 + 20 && mouseX <= this.width / 2.0 + 130) {
            sizeIndex = Math.floorMod(sizeIndex + delta, TimeAcceleratorBlockEntity.SIZES.length);
            sendConfig();
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    private void sendConfig() {
        NetworkHandler.sendToServer(new TimeAcceleratorConfigPayload(pos, multiplierIndex, sizeIndex));
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 14, 0xFFFFFF);
        // 说明
        guiGraphics.drawCenteredString(this.font,
                Component.literal("滚轮切换 · 点击生效"),
                this.width / 2, 26, 0xAAAAAA);

        drawWheel(guiGraphics, this.width / 2 - 130, 44, 110, 150, "加速倍率",
                TimeAcceleratorBlockEntity.MULTIPLIERS, multiplierIndex, m -> m + " 倍");
        drawWheel(guiGraphics, this.width / 2 + 20, 44, 110, 150, "加速范围",
                TimeAcceleratorBlockEntity.SIZES, sizeIndex, s -> s + "×" + s + "×" + s);

        // 底部提示
        guiGraphics.drawCenteredString(this.font,
                Component.literal("当前：")
                        .append(Component.literal(TimeAcceleratorBlockEntity.MULTIPLIERS[Math.floorMod(multiplierIndex, TimeAcceleratorBlockEntity.MULTIPLIERS.length)] + " 倍 · "))
                        .append(Component.literal(TimeAcceleratorBlockEntity.SIZES[Math.floorMod(sizeIndex, TimeAcceleratorBlockEntity.SIZES.length)] + "×" + TimeAcceleratorBlockEntity.SIZES[Math.floorMod(sizeIndex, TimeAcceleratorBlockEntity.SIZES.length)] + "×" + TimeAcceleratorBlockEntity.SIZES[Math.floorMod(sizeIndex, TimeAcceleratorBlockEntity.SIZES.length)])),
                this.width / 2, this.height - 26, 0xFFFF55);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    private void drawWheel(GuiGraphics gg, int x, int y, int w, int h, String label,
                           int[] options, int selected, IntFunction<String> fmt) {
        // 背景
        gg.fill(x, y, x + w, y + h, 0xAA000000);
        // 标签
        gg.drawCenteredString(this.font, label, x + w / 2, y - 10, 0xFFFFFF);
        // 上箭头
        gg.drawCenteredString(this.font, "^", x + w / 2, y + 4, 0x777777);
        // 显示 5 个选项（选中项居中，循环滚动）
        int visible = Math.min(5, options.length);
        int start = selected - 2;
        int rowH = (h - 24) / visible;
        for (int i = 0; i < visible; i++) {
            int idx = Math.floorMod(start + i, options.length);
            boolean isSelected = idx == selected;
            int oy = y + 16 + i * rowH;
            if (isSelected) {
                gg.fill(x + 4, oy - 2, x + w - 4, oy + 10, 0x55FFAA00);
            }
            gg.drawCenteredString(this.font, fmt.apply(options[idx]), x + w / 2, oy, isSelected ? 0xFFFF55 : 0x9A9A9A);
        }
        // 下箭头
        gg.drawCenteredString(this.font, "v", x + w / 2, y + h - 12, 0x777777);
    }
}