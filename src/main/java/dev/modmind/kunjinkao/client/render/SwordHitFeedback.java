package dev.modmind.kunjinkao.client.render;

import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.KunJinKaoPalette;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.joml.Vector3f;

import java.util.Random;

/**
 * 命中那一刻的反馈：冲击波 + 屏幕故障。
 * <p>
 * 这是"打起来不爽"的主要来源 —— 原来打中一个生物，只有一段 HUD 文字和一个残影，
 * 没有任何<b>爆点</b>。而爽感恰恰全在这一瞬：<b>打出去要有回应</b>。
 * <p>
 * 两层，一近一远：
 * <ul>
 *   <li><b>冲击波</b>：目标身上炸开一圈品红粒子并向外飞散。给速度而不是画固定的环，
 *       让它自己扩出去 —— 比定时逐帧画一个环简单，观感一样。</li>
 *   <li><b>屏幕故障</b>：全屏几条水平撕裂带，青与品红各偏一点 ——
 *       这是用廉价办法假装 RGB 分离（真做需要后期着色器）。配上乱码符号，
 *       读作"这一下把显示打乱了"，正好是模组主题。</li>
 * </ul>
 * <p>
 * 颜色全部取自 {@link KunJinKaoPalette}：品红 = 损坏，青 = 常态。
 * 不再各处写死字面量，否则加一处特效就要再挑一次颜色，最后又回到"全是青"。
 * <p>
 * 屏幕撕裂刻意每 tick 重掷一次位置而<b>不是</b>连续动画：故障应当是跳变的，
 * 平滑移动就变成"扫描线"了，那是另一种东西。
 */
@EventBusSubscriber(modid = KunJinKaoEntry.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public final class SwordHitFeedback {

    /** 屏幕故障持续多少 tick。18 刻 = 0.9 秒，比挥砍动画略长一点就够。 */
    private static final int GLITCH_TICKS = 18;
    /** 撕裂带条数。多了变花，少了看不出来。 */
    private static final int BANDS = 7;

    private static final DustParticleOptions CORRUPT_DUST =
            new DustParticleOptions(new Vector3f(
                    KunJinKaoPalette.red(KunJinKaoPalette.CORRUPT),
                    KunJinKaoPalette.green(KunJinKaoPalette.CORRUPT),
                    KunJinKaoPalette.blue(KunJinKaoPalette.CORRUPT)), 0.85F);
    private static final DustParticleOptions NORMAL_DUST =
            new DustParticleOptions(new Vector3f(
                    KunJinKaoPalette.red(KunJinKaoPalette.NORMAL),
                    KunJinKaoPalette.green(KunJinKaoPalette.NORMAL),
                    KunJinKaoPalette.blue(KunJinKaoPalette.NORMAL)), 0.7F);

    private static final Random RANDOM = new Random();
    private static int glitchTicks;

    private SwordHitFeedback() {
    }

    /** 由 {@code KunJinKaoClientSwordVisuals.onLocalAttack} 调用。 */
    public static void trigger(Player player, Entity target) {
        glitchTicks = GLITCH_TICKS;
        if (player != null && target != null) {
            spawnShockwave(player.level(), target);
        }
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        if (glitchTicks > 0) {
            glitchTicks--;
        }
    }

    /**
     * 在目标身上炸开一圈并向外飞散。
     * <p>
     * 给粒子一个向外的初速度，环就自己扩出去 —— 不需要像区域场那样维护一张
     * "正在扩散的环"的表逐帧重画。省一份状态，观感一样。
     */
    private static void spawnShockwave(Level level, Entity target) {
        double cx = target.getX();
        double cy = target.getY() + target.getBbHeight() * 0.5D;
        double cz = target.getZ();

        int points = 20;
        for (int i = 0; i < points; i++) {
            double angle = Math.PI * 2.0D / points * i;
            double radius = 0.5D + RANDOM.nextDouble() * 0.18D;
            level.addParticle(CORRUPT_DUST,
                    cx + Math.cos(angle) * radius,
                    cy + (RANDOM.nextDouble() - 0.5D) * 0.25D,
                    cz + Math.sin(angle) * radius,
                    Math.cos(angle) * 0.16D, 0.02D, Math.sin(angle) * 0.16D);
        }
        // 中心迸溅：几颗青色向上窜，给环一个"芯"
        for (int i = 0; i < 8; i++) {
            level.addParticle(NORMAL_DUST, cx, cy, cz,
                    (RANDOM.nextDouble() - 0.5D) * 0.3D,
                    0.12D + RANDOM.nextDouble() * 0.12D,
                    (RANDOM.nextDouble() - 0.5D) * 0.3D);
        }
    }

    /** 由 {@code ClientModEvents.registerOverlays} 注册的 GUI 层调用。 */
    public static void renderOverlay(GuiGraphics graphics, int screenWidth, int screenHeight) {
        if (glitchTicks <= 0 || screenWidth <= 0 || screenHeight <= 0) {
            return;
        }
        float life = glitchTicks / (float) GLITCH_TICKS;
        float intensity = life * life;   // 平方衰减：起手重、收尾快

        // 全屏一层极淡的品红，把整个画面"染脏"一点
        graphics.fill(0, 0, screenWidth, screenHeight,
                KunJinKaoPalette.alpha(KunJinKaoPalette.CORRUPT, 0.05F * intensity));

        for (int i = 0; i < BANDS; i++) {
            // 每 tick 重掷：故障要跳变，平滑移动就成扫描线了
            int seed = glitchTicks * 1103515245 + i * 12345;
            int y = Math.floorMod(seed, screenHeight);
            int bandHeight = 2 + Math.floorMod(seed >> 7, 7);
            int shift = Math.floorMod(seed >> 13, 23) - 11;
            int color = (i % 2 == 0) ? KunJinKaoPalette.CORRUPT : KunJinKaoPalette.NORMAL;
            int argb = KunJinKaoPalette.alpha(color, 0.20F * intensity);
            // 左右错开画，制造"画面被横向扯开"的错觉
            graphics.fill(Math.max(0, shift), y, screenWidth, y + bandHeight, argb);
            if (shift > 0) {
                graphics.fill(0, y, shift, y + bandHeight, argb);
            }
        }

        // 乱码符号：主题自带的记号，散在撕裂带上
        var font = Minecraft.getInstance().font;
        if (font != null) {
            int glyphs = 3;
            for (int i = 0; i < glyphs; i++) {
                int seed = glitchTicks * 7919 + i * 104729;
                int y = Math.floorMod(seed, screenHeight);
                int x = Math.floorMod(seed >> 11, Math.max(1, screenWidth - 40));
                String text = (i % 2 == 0) ? "锟斤拷" : KunJinKaoPalette.MOJIBAKE;
                graphics.drawString(font, text, x, y,
                        KunJinKaoPalette.alpha(KunJinKaoPalette.CORRUPT, 0.75F * intensity), false);
            }
        }
    }
}