package dev.modmind.kunjinkao.client.render;

import com.mojang.math.Axis;
import dev.modmind.kunjinkao.KunJinKaoSwordItem;
import dev.modmind.kunjinkao.KunJinKaoEntry;
import dev.modmind.kunjinkao.client.KunJinKaoClientSwordVisuals;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

/**
 * 第一人称手持时的冷启动待机动画：持续的微弱风扇震动，
 * 每三秒追加一次由上至下的数据刷新式轻微位移。
 */
@EventBusSubscriber(modid = KunJinKaoEntry.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public final class KunJinKaoIdleDataRefreshHandler {

    private static final int REFRESH_INTERVAL_TICKS = 60;
    // 剑的静止展示优先于待机抖动；保留旧代码便于以后改为非位移型特效。
    private static final boolean APPLY_POSE_ANIMATION = false;

    private KunJinKaoIdleDataRefreshHandler() {
    }

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        Player player = Minecraft.getInstance().player;
        if (player == null || player.level() == null) {
            return;
        }

        ItemStack stack = player.getItemInHand(event.getHand());
        if (!(stack.getItem() instanceof KunJinKaoSwordItem)
                || KunJinKaoSwordItem.isDisguised(stack)) {
            return;
        }

        if (KunJinKaoClientSwordVisuals.isDrawingInCenter(event.getHand())) {
            // 编译阶段隐藏原物品，但在后半段绘制玩家真实皮肤的手臂去抓中央剑柄。
            KunJinKaoClientSwordVisuals.noteCompileRenderObserved();
            renderReachingArm(event, player);
            event.setCanceled(true);
            return;
        }

        float grabProgress = KunJinKaoClientSwordVisuals.getGrabSwordProgress();
        if (grabProgress > 0.0F && !KunJinKaoClientSwordVisuals.isDrawingInCenter(event.getHand())) {
            // 接触剑柄后，“手+剑”整体从画面中央平滑拉回正常第一人称持剑位置。
            HumanoidArm arm = armForHand(player, event.getHand());
            float side = arm == HumanoidArm.RIGHT ? 1.0F : -1.0F;
            float eased = grabProgress * grabProgress * (3.0F - 2.0F * grabProgress);
            event.getPoseStack().translate(-side * 0.43F * eased, 0.22F * eased, -0.30F * eased);
            event.getPoseStack().mulPose(Axis.XP.rotationDegrees(-17.0F * eased));
            event.getPoseStack().mulPose(Axis.ZP.rotationDegrees(-side * 18.0F * eased));
        }

        if (!APPLY_POSE_ANIMATION) {
            return;
        }

        float gameTime = player.level().getGameTime() + event.getPartialTick();
        // 常态是几乎不可察觉的服务器风扇微震，不影响挥砍操作与瞄准。
        float vibration = (float) Math.sin(gameTime * 0.38F) * 0.0018F;
        event.getPoseStack().translate(0.0F, vibration, 0.0F);

        float refresh = gameTime % REFRESH_INTERVAL_TICKS;
        if (refresh < 4.0F) {
            // 刷新条从上向下扫过时，以非常短促的蓝绿数据重影式位移表现。
            float scanProgress = refresh / 4.0F;
            event.getPoseStack().translate(0.0025F * (1.0F - scanProgress),
                    0.010F * (0.5F - scanProgress), 0.0F);
            event.getPoseStack().mulPose(Axis.ZP.rotationDegrees((0.5F - scanProgress) * 1.2F));
        }
    }

    private static void renderReachingArm(RenderHandEvent event, Player player) {
        float progress = KunJinKaoClientSwordVisuals.getCompileReachProgress();
        if (progress <= 0.0F || !(player instanceof AbstractClientPlayer clientPlayer)) {
            return;
        }

        EntityRenderer<? super AbstractClientPlayer> renderer = Minecraft.getInstance()
                .getEntityRenderDispatcher().getRenderer(clientPlayer);
        if (!(renderer instanceof PlayerRenderer playerRenderer)) {
            return;
        }

        HumanoidArm arm = armForHand(player, event.getHand());
        float side = arm == HumanoidArm.RIGHT ? 1.0F : -1.0F;
        float start = 1.0F - progress;
        var pose = event.getPoseStack();
        pose.pushPose();

        // 从同侧屏幕下缘起步，随进度向准星下方的剑柄伸直。
        pose.translate(
                side * (0.64000005F + 0.45F * start - 0.52F * progress),
                -0.60F - 0.55F * start + 0.18F * progress,
                -0.72F + 0.25F * start - 0.28F * progress
        );
        pose.mulPose(Axis.YP.rotationDegrees(side * (45.0F - 18.0F * progress)));
        pose.mulPose(Axis.XP.rotationDegrees(-22.0F * progress));
        pose.mulPose(Axis.ZP.rotationDegrees(-side * 18.0F * progress));

        // 复用原版第一人称手臂末段变换与 PlayerRenderer，包含真实皮肤和外层袖子。
        pose.translate(side * -1.0F, 3.6F, 3.5F);
        pose.mulPose(Axis.ZP.rotationDegrees(side * 120.0F));
        pose.mulPose(Axis.XP.rotationDegrees(200.0F));
        pose.mulPose(Axis.YP.rotationDegrees(side * -135.0F));
        pose.translate(side * 5.6F, 0.0F, 0.0F);

        if (arm == HumanoidArm.RIGHT) {
            playerRenderer.renderRightHand(pose, event.getMultiBufferSource(), event.getPackedLight(), clientPlayer);
        } else {
            playerRenderer.renderLeftHand(pose, event.getMultiBufferSource(), event.getPackedLight(), clientPlayer);
        }
        pose.popPose();
    }

    private static HumanoidArm armForHand(Player player, InteractionHand hand) {
        if (hand == InteractionHand.MAIN_HAND) {
            return player.getMainArm();
        }
        return player.getMainArm() == HumanoidArm.RIGHT ? HumanoidArm.LEFT : HumanoidArm.RIGHT;
    }
}
