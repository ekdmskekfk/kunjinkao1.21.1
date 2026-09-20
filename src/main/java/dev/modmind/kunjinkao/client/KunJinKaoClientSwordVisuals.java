package dev.modmind.kunjinkao.client;

import dev.modmind.kunjinkao.KunJinKaoSwordItem;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.CameraType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import org.joml.Vector3f;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 管理员剑的纯客户端视觉层。
 *
 * 不改变第一人称 PoseStack，因此扫描和拔出效果不会再造成视角或模型抖动。
 */
public final class KunJinKaoClientSwordVisuals {

    private static final Logger LOGGER = LogManager.getLogger("KunJinKao");
    private static final int DRAW_COMPtLE_TtCKS = 48;
    private static final int GRAB_SWORD_TtCKS = 14;

    private static final DustParticleOptions SCAN_BLUE =
            new DustParticleOptions(new Vector3f(0.00F, 0.90F, 1.00F), 0.72F);
    private static final DustParticleOptions SCAN_WHtTE =
            new DustParticleOptions(new Vector3f(0.82F, 0.97F, 1.00F), 0.55F);
    private static final DustParticleOptions DATA_GREEN =
            new DustParticleOptions(new Vector3f(0.35F, 1.00F, 0.82F), 0.52F);

    private static boolean wasHoldingSword;
    private static InteractionHand compileHand = InteractionHand.MAIN_HAND;
    private static int drawCompileTicks;
    private static int grabSwordTicks;
    private static int rightClickCompileTicks;
    private static int attackHudTicks;
    private static int attackHudPhase;
    private static boolean compileRenderObserved;
    private static boolean compileModelLogged;

    private KunJinKaoClientSwordVisuals() {
    }

    public static void tick() {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null || minecraft.level == null) {
            reset();
            return;
        }

        boolean holdingSword = isHoldingVisibleSword(player);
        if (holdingSword && !wasHoldingSword) {
            compileHand = findVisibleSwordHand(player);
            drawCompileTicks = DRAW_COMPtLE_TtCKS;
            compileRenderObserved = false;
            compileModelLogged = false;
            LOGGER.info("剑编译已开始，手：{}", compileHand);
            // 冰晶般的“咔哒”声，仅在本地切换到持剑状态时播放一次。
            minecraft.level.playSound(player, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.PLAYERS, 0.36F, 1.48F);
        }
        wasHoldingSword = holdingSword;

        if (!holdingSword) {
            drawCompileTicks = 0;
            grabSwordTicks = 0;
            rightClickCompileTicks = 0;
        }

        // 拿出剑时由物品模型的几何切片逐行出现，不使用粒子模拟。
        if (drawCompileTicks > 0) {
            emitCentralCompileParticles(player, drawCompileTicks);
            drawCompileTicks--;
            if (drawCompileTicks == 0) {
                grabSwordTicks = GRAB_SWORD_TtCKS;
            }
        } else if (grabSwordTicks > 0) {
            grabSwordTicks--;
        }
        // 右键编译仍保留独立的粒子与音效反馈。
        if (rightClickCompileTicks > 0) {
            emitCompileParticles(player, rightClickCompileTicks);
            rightClickCompileTicks--;
        }
        if (attackHudTicks > 0) {
            attackHudTicks--;
        }
    }

    public static void onLocalAttack(AttackEntityEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (event.getEntity() != minecraft.player || minecraft.level == null
                || !isHoldingVisibleSword(minecraft.player)) {
            return;
        }

        Entity target = event.getTarget();
        emitStructuredAfterimage(minecraft.player, target);
        attackHudTicks = 18;
        attackHudPhase = (attackHudPhase + 1) % 3;
    }

    /** 右键使用时重新播放一次短编译序列；不干预原有的右键清怪功能。 */
    public static void onLocalRightClick(Player player, InteractionHand hand) {
        if (player != Minecraft.getInstance().player || !isVisibleSword(player.getItemInHand(hand))) {
            return;
        }
        compileHand = hand;
        rightClickCompileTicks = 14;
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level != null) {
            minecraft.level.playSound(player, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.PLAYERS, 0.85F, 1.58F);
        }
    }

    /** 在准星右侧显示小而规整的命令数据字样，避免传统红色故障文字。 */
    public static void renderAttackData(GuiGraphics graphics, int screenWidth, int screenHeight) {
        if (attackHudTicks <= 0) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        String text = switch (attackHudPhase) {
            case 0 -> "/execute";
            case 1 -> "/data";
            default -> "[SUCCESS]";
        };
        int alpha = Math.min(220, attackHudTicks * 14);
        int color = (alpha << 24) | (attackHudPhase == 2 ? 0x68FFD2 : 0xA9F6FF);
        int x = screenWidth / 2 + 12;
        int y = screenHeight / 2 + 8;
        graphics.drawString(minecraft.font, text, x, y, color, false);
    }

    /**
     * 在 HUD 最上层直接绘制当前编译阶段的烘焙物品模型。
     * 这与背包物品使用同一条稳定渲染管线，不再依赖本地临时展示实体。
     */
    public static void renderCompileModel(GuiGraphics graphics, int screenWidth, int screenHeight) {
        if (drawCompileTicks <= 0) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.level == null) {
            return;
        }

        ItemStack stageStack = minecraft.player.getItemInHand(compileHand).copy();
        // 让模型谓词明确回退到已经在正常持剑状态验证过的完整 3D 剑模型。
        CompoundTag modelTag = customData(stageStack);
        modelTag.putBoolean("KunJinKaoCompileHudModel", true);
        stageStack.set(DataComponents.CUSTOM_DATA, CustomData.of(modelTag));
        if (!compileModelLogged) {
            BakedModel model = minecraft.getItemRenderer().getModel(
                    stageStack, minecraft.level, minecraft.player, 0);
            boolean missing = model == minecraft.getModelManager().getMissingModel();
            RandomSource random = RandomSource.create(0L);
            int quadCount = model.getQuads(null, null, random).size();
            for (Direction direction : Direction.values()) {
                random.setSeed(0L);
                quadCount += model.getQuads(null, direction, random).size();
            }
            LOGGER.info("剑编译 HUD 完整模型已取得：阶段={}，物品={}，缺失模型={}，几何面={}",
                    getDrawCompileStage(), stageStack.getItem(), missing, quadCount);
            compileModelLogged = true;
        }

        boolean firstPerson = minecraft.options.getCameraType() == CameraType.FIRST_PERSON;
        int centerX = screenWidth / 2;
        int centerY = screenHeight / 2 + 6;
        int elapsedTicks = DRAW_COMPtLE_TtCKS - drawCompileTicks + 1;
        float progress = Math.min(1.0F, elapsedTicks / (float) DRAW_COMPtLE_TtCKS);
        int modelTop = centerY - 72;
        int modelBottom = centerY + 72;
        int visibleTop = modelBottom - Math.round((modelBottom - modelTop) * progress);

        // 使用屏幕裁剪线逐像素揭示同一把完整 3D 模型，视觉上就是剑身逐行编译成形。
        graphics.enableScissor(centerX - 72, visibleTop, centerX + 72, modelBottom);
        var pose = graphics.pose();
        pose.pushPose();
        pose.translate(centerX, centerY, 500.0F);
        // 抵消完整剑 GUt 模型的投影倾角，让编译中的剑刃竖直向上。
        pose.mulPose(Axis.ZP.rotationDegrees(22.5F));
        // 第三人称能看见玩家模型，编译剑缩小以免遮住脸；第一人称保持原尺寸。
        float displayScale = firstPerson ? 5.2F : 2.75F;
        pose.scale(displayScale, displayScale, displayScale);
        pose.translate(-8.0F, -8.0F, 0.0F);
        graphics.renderItem(stageStack, 0, 0);
        graphics.flush();
        pose.popPose();
        graphics.disableScissor();
    }

    /**
     * 供物品模型谓词读取。返回 -1 时使用完整剑模型；0..0.875 对应八个逐行编译阶段。
     */
    public static float getDrawCompileModelStage(ItemStack stack, LivingEntity renderEntity) {
        CompoundTag tag = customData(stack);
        if (tag.getBoolean("KunJinKaoCompileHudModel")) {
            return -1.0F;
        }
        if (tag.contains("KunJinKaoCompileStage")) {
            return Math.max(0, Math.min(7, tag.getInt("KunJinKaoCompileStage"))) / 8.0F;
        }
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (renderEntity instanceof Player renderedPlayer
                && renderedPlayer != player
                && RemoteSwordDrawVisualState.isCompiling(renderedPlayer.getUUID())
                && isVisibleSword(stack)
                && ItemStack.isSameItemSameComponents(stack,
                renderedPlayer.getItemInHand(RemoteSwordDrawVisualState.getHand(renderedPlayer.getUUID())))) {
            // 远端持剑玩家的原始手持剑在编译期间隐藏，由第三人称渲染层绘制独立动画剑。
            return 0.0F;
        }
        // 第一人称物品模型求值时 renderEntity 可能为 null；不能把它当作玩家身份依据，
        // 否则会直接回退完整模型，表现为数秒后整把剑突然出现。
        if (player == null || drawCompileTicks <= 0 || !isVisibleSword(stack)
                || !ItemStack.isSameItemSameComponents(stack, player.getItemInHand(compileHand))) {
            return -1.0F;
        }
        int elapsed = DRAW_COMPtLE_TtCKS - drawCompileTicks;
        int stage = Math.min(7, elapsed / 6);
        return stage / 8.0F;
    }

    /** 当前手是否正在显示位于屏幕中央的逐段编译模型。 */
    public static boolean isDrawingInCenter(InteractionHand hand) {
        return drawCompileTicks > 0 && compileHand == hand;
    }

    /** 当前应直接绘制的 3D 编译模型阶段。 */
    public static int getDrawCompileStage() {
        int elapsed = DRAW_COMPtLE_TtCKS - drawCompileTicks;
        return Math.max(0, Math.min(7, elapsed / 6));
    }

    /** 仅用于确认客户端的第一人称模型渲染入口确实被调用。 */
    public static void noteCompileRenderObserved() {
        if (!compileRenderObserved) {
            compileRenderObserved = true;
            LOGGER.info("剑编译 3D 渲染入口已进入");
        }
    }

    /** 编译完成后取剑动作的剩余进度，供第一人称手部渲染使用。 */
    public static float getGrabSwordProgress() {
        return grabSwordTicks / (float) GRAB_SWORD_TtCKS;
    }

    /** 编译后半段真实手臂伸向剑柄的平滑进度。 */
    public static float getCompileReachProgress() {
        int elapsed = DRAW_COMPtLE_TtCKS - drawCompileTicks;
        float linear = Math.max(0.0F, Math.min(1.0F, (elapsed - 25.0F) / 21.0F));
        return linear * linear * (3.0F - 2.0F * linear);
    }

    public static boolean isThirdPersonReachActive(Player player) {
        Minecraft minecraft = Minecraft.getInstance();
        return player == minecraft.player
                && drawCompileTicks > 0
                && getCompileReachProgress() > 0.0F
                && minecraft.options.getCameraType() != CameraType.FIRST_PERSON;
    }

    public static HumanoidArm getCompileArm(Player player) {
        if (compileHand == InteractionHand.MAIN_HAND) {
            return player.getMainArm();
        }
        return player.getMainArm() == HumanoidArm.RIGHT ? HumanoidArm.LEFT : HumanoidArm.RIGHT;
    }

    private static boolean isHoldingVisibleSword(Player player) {
        return isVisibleSword(player.getMainHandItem()) || isVisibleSword(player.getOffhandItem());
    }

    private static boolean isVisibleSword(ItemStack stack) {
        return stack.getItem() instanceof KunJinKaoSwordItem && !KunJinKaoSwordItem.isDisguised(stack);
    }

    private static void emitCompileParticles(Player player, int ticksLeft) {
        Minecraft minecraft = Minecraft.getInstance();
        Vec3 look = player.getLookAngle().normalize();
        Vec3 side = look.cross(new Vec3(0.0D, 1.0D, 0.0D));
        if (side.lengthSqr() < 1.0E-4D) {
            side = new Vec3(1.0D, 0.0D, 0.0D);
        } else {
            side = side.normalize();
        }
        double progress = 1.0D - ticksLeft / 14.0D;
        // 第一人称没有可直接读取的“物品骨骼坐标”，因此按持剑手、主手方向和视线
        // 推导出稳定的剑位锚点。它位于准星侧下方，而不是相机/准星正中心。
        boolean rightSide = (compileHand == InteractionHand.MAIN_HAND)
                == (player.getMainArm() == HumanoidArm.RIGHT);
        // look × up 在当前相机坐标中为屏幕右侧。
        double handOffset = rightSide ? 0.38D : -0.38D;
        Vec3 swordAnchor = player.getEyePosition()
                .add(side.scale(handOffset))
                // 再向屏幕右侧推进到边缘附近。
                .add(look.scale(0.26D))
                .add(0.0D, -0.54D, 0.0D);
        // 粒子从剑前方的“虚空”向剑位收束，模拟编译拼合。
        Vec3 origin = swordAnchor.add(look.scale(0.58D - progress * 0.40D));
        for (int index = -1; index <= 1; index++) {
            Vec3 point = origin.add(side.scale(index * 0.075D)).add(0.0D, (index & 1) * 0.035D, 0.0D);
            minecraft.level.addParticle(index == 0 ? SCAN_WHtTE : SCAN_BLUE,
                    point.x, point.y, point.z, -look.x * 0.018D, -look.y * 0.018D, -look.z * 0.018D);
        }
        if ((ticksLeft & 1) == 0) {
            minecraft.level.addParticle(ParticleTypes.END_ROD, origin.x, origin.y, origin.z,
                    0.0D, 0.012D, 0.0D);
        }
    }

    private static void emitCentralCompileParticles(Player player, int ticksLeft) {
        Minecraft minecraft = Minecraft.getInstance();
        Vec3 look = player.getLookAngle().normalize();
        Vec3 side = look.cross(new Vec3(0.0D, 1.0D, 0.0D));
        if (side.lengthSqr() < 1.0E-4D) {
            side = new Vec3(1.0D, 0.0D, 0.0D);
        } else {
            side = side.normalize();
        }
        Vec3 up = side.cross(look).normalize();
        double elapsed = DRAW_COMPtLE_TtCKS - ticksLeft;
        Vec3 center = player.getEyePosition().add(look.scale(1.18D));

        // 两条相反方向的淡蓝数据环围绕屏幕中央的剑旋转，并在完成时向剑身收束。
        double radius = 0.31D - elapsed / DRAW_COMPtLE_TtCKS * 0.10D;
        for (int index = 0; index < 4; index++) {
            double angle = elapsed * 0.68D + index * (Math.PI / 2.0D);
            double vertical = Math.sin(angle * 1.7D) * 0.11D;
            Vec3 radial = side.scale(Math.cos(angle) * radius)
                    .add(up.scale(Math.sin(angle) * radius + vertical));
            Vec3 point = center.add(radial);
            minecraft.level.addParticle((index & 1) == 0 ? SCAN_BLUE : SCAN_WHtTE,
                    point.x, point.y, point.z,
                    radial.x * -0.055D, radial.y * -0.055D, radial.z * -0.055D);
        }
    }

    private static void emitStructuredAfterimage(Player player, Entity target) {
        Minecraft minecraft = Minecraft.getInstance();
        Vec3 origin = player.getEyePosition();
        Vec3 towardTarget = target.position().add(0.0D, target.getBbHeight() * 0.5D, 0.0D).subtract(origin);
        if (towardTarget.lengthSqr() < 0.01D) {
            return;
        }
        Vec3 forward = towardTarget.normalize();
        Vec3 side = forward.cross(new Vec3(0.0D, 1.0D, 0.0D));
        if (side.lengthSqr() < 1.0E-4D) {
            side = new Vec3(1.0D, 0.0D, 0.0D);
        } else {
            side = side.normalize();
        }
        Vec3 up = side.cross(forward).normalize();
        double length = Math.min(2.8D, towardTarget.length());
        // 规整的 3x4 矩形数据块拖尾。
        for (int row = 0; row < 4; row++) {
            Vec3 rowCenter = origin.add(forward.scale(0.35D + length * row / 5.0D));
            for (int column = -1; column <= 1; column++) {
                Vec3 point = rowCenter.add(side.scale(column * 0.12D)).add(up.scale((row & 1) * 0.055D));
                minecraft.level.addParticle((row + column) % 2 == 0 ? SCAN_BLUE : DATA_GREEN,
                        point.x, point.y, point.z, forward.x * 0.012D, forward.y * 0.012D, forward.z * 0.012D);
            }
        }
    }

    private static CompoundTag customData(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        return data == null ? new CompoundTag() : data.copyTag();
    }

    private static void reset() {
        wasHoldingSword = false;
        compileHand = InteractionHand.MAIN_HAND;
        drawCompileTicks = 0;
        grabSwordTicks = 0;
        compileRenderObserved = false;
        compileModelLogged = false;
        rightClickCompileTicks = 0;
        attackHudTicks = 0;
    }

    private static InteractionHand findVisibleSwordHand(Player player) {
        return isVisibleSword(player.getMainHandItem()) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
    }
}
