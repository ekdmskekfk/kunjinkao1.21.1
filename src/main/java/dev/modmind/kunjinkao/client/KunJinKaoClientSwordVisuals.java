package dev.modmind.kunjinkao.client;

import dev.modmind.kunjinkao.KunJinKaoSwordItem;
import dev.modmind.kunjinkao.KunJinKaoEntry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.CameraType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemDisplayContext;
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

    /**
     * 抓取阶段里，屏幕中央那把剑的淡出时长（tick）。
     * <p>
     * 只占抓取阶段的<b>开头</b>一小段：编译一结束就把剑交给手。
     * 早先这里铺满整个 {@link #GRAB_SWORD_TtCKS}，结果编译动画早就结束、
     * 手臂也把剑收回去了，屏幕中央却还挂着一把半透明的剑将近一秒。
     */
    private static final int CENTER_FADE_TICKS = 3;

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

    /**
     * 是否让自己的手持剑在编译期间切换到 kun_jin_kao_compile_* 模型。
     * <p>
     * 默认 false，原因是素材形态不匹配：剑本体（kun_jin_kao_3d 经 ItemModelGenerator 生成）
     * 是一张 1254×1254 平面贴图、占满 16 单位；而那 8 个编译阶段是 3.4~13 单位的 3D 方块几何。
     * 一切换，玩家看到的就是「手里的大剑消失、剩一个小方块」。
     * <p>
     * 关闭后：手持物品始终是完整剑；逐级成形由屏幕中央的裁剪揭示动画呈现
     * （见 {@link #renderCompileModel}，它用完整模型自上而下揭示）。
     * 远端编译玩家那条分支不受影响 —— 第三人称仍会隐藏其手持剑，由抓取层代画。
     */
    private static final boolean SWITCH_LOCAL_HAND_MODEL = false;

    /** 排查编译动画时打开：每次动画开始时打印一行诊断，排查完请关掉以免刷日志。 */
    private static final boolean DEBUG_COMPILE_LOG = false;

    private static boolean centerDrawLogged;

    /**
     * 编译阶段模型是否真的有几何体。
     * <p>
     * 若为 false，物品模型谓词一律返回 -1（完整剑），避免切换到空模型导致"手里的剑消失"。
     * 每次动画开始时重新探测一次。
     */
    private static boolean compileStageModelsUsable = true;

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
            compileStageModelsUsable = areCompileStageModelsUsable(player);
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
     * 屏幕中央的编译展示。
     * <p>
     * 这里用最原始的 2D 贴图绘制（{@code GuiGraphics.blit}），而不是绘制 3D 物品模型。
     * 理由：战术 HUD 证明本模组的 GUI 图层渲染正常，而
     * {@code GuiGraphics.renderItem} 与 {@code ItemRenderer.renderStatic} 在这套阶段模型上
     * 始终画不出任何东西（GUI 图层与世界渲染两条路都试过）。
     * <p>
     * {@code kun_jin_kao_stage_0..7.png} 是同一张剑图的递进揭示版本，
     * 逐帧切换就得到"剑在屏幕中央一块块拼起来"的效果，且完全不依赖模型烘焙。
     */
    public static void renderCompileModel(GuiGraphics graphics, int screenWidth, int screenHeight, float partialTick) {
        boolean compiling = drawCompileTicks > 0;
        boolean grabbing = grabSwordTicks > 0;
        if (!compiling && !grabbing) {
            return;
        }

        float reveal;   // 0..1 —— 剑的揭示进度（连续，不再离散跳级）
        float alpha;    // 0..1 —— 整体透明度
        if (compiling) {
            float elapsed = (DRAW_COMPtLE_TtCKS - drawCompileTicks) + partialTick;
            reveal = Mth.clamp(elapsed / DRAW_COMPtLE_TtCKS, 0.0F, 1.0F);
            // smoothstep：起步与收尾都放缓，中段推进，避免匀速的机械感
            reveal = reveal * reveal * (3.0F - 2.0F * reveal);
            // 开场 5 tick 淡入，避免第一帧突然冒出来
            alpha = Mth.clamp(elapsed / 5.0F, 0.0F, 1.0F);
        } else {
            reveal = 1.0F;
            // 抓取阶段：中央这一把在开头 CENTER_FADE_TICKS tick 内就交给手，不陪着整段淡出。
            float elapsedInGrab = GRAB_SWORD_TtCKS - grabSwordTicks;
            alpha = Mth.clamp(1.0F - elapsedInGrab / (float) CENTER_FADE_TICKS, 0.0F, 1.0F);
        }

        int size = Math.max(48, Math.round(Math.min(screenWidth, screenHeight) * 0.42F));
        int x = screenWidth / 2 - size / 2;
        int y = screenHeight / 2 - size / 2 + 6;

        // 用【整张剑图 + 逐帧连续的裁剪线】揭示，而不是在 8 张阶段图之间切换：
        // 8 级切换每级占整高 12.5%，即使做交叉淡入也仍读得出台阶感；
        // 而裁剪线的位置是逐帧连续的，无论 alpha 混合是否生效，推进本身就是平滑的。
        // 贴图内容在整张 1254x1254 里的纵向范围是 y 37..1196（约 2.95% ~ 95.45%）。
        float contentTop = y + size * 0.0295F;
        float contentBottom = y + size * 0.9545F;
        int cut = Math.round(contentBottom - (contentBottom - contentTop) * reveal);

        graphics.setColor(1.0F, 1.0F, 1.0F, alpha);
        graphics.enableScissor(x, cut, x + size, Math.round(contentBottom) + 1);
        graphics.blit(ResourceLocation.fromNamespaceAndPath(
                        KunJinKaoEntry.MOD_ID, "textures/item/kun_jin_kao.png"),
                x, y, 0, 0, size, size, size, size);
        graphics.disableScissor();
        // 切口处的扫描亮线：让推进方向一眼可读，也让整条边不像"被切掉"
        int scanAlpha = (int) (alpha * 190.0F);
        if (scanAlpha > 4) {
            graphics.fill(x, cut - 1, x + size, cut + 1, (scanAlpha << 24) | 0xBFF6FF);
        }
        // 复位着色器颜色，避免影响同一图层里后续的绘制
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    /** 当前编译动画绑定的手；{@code RenderHandEvent} 用它保证一帧只画一次中央展示。 */
    public static InteractionHand getCompileHand() {
        return compileHand;
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
        if (!SWITCH_LOCAL_HAND_MODEL || !compileStageModelsUsable) {
            // 手持物品保持完整剑：编译阶段的 3D 方块与剑的平面贴图形态差异过大，
            // 切换只会让玩家以为剑消失了。
            return -1.0F;
        }
        int elapsed = DRAW_COMPtLE_TtCKS - drawCompileTicks;
        int stage = Math.min(7, elapsed / 6);
        return stage / 8.0F;
    }

    /**
     * 当前手是否正在显示位于屏幕中央的逐段编译模型。
     * <p>
     * 手持物品在编译期间【保持显示】（见 {@code KunJinKaoIdleDataRefreshHandler}），
     * 本方法只用于决定是否额外绘制"伸手抓剑"的手臂。
     * 带上 {@code compileStageModelsUsable} 是必要的：阶段模型不可用时中央什么都画不出来，
     * 那就没有剑可抓，手臂也不该出现。
     */
    public static boolean isDrawingInCenter(InteractionHand hand) {
        return drawCompileTicks > 0 && compileHand == hand && compileStageModelsUsable;
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

    /**
     * 逐个探测 8 个编译阶段模型是否真的有面。
     * <p>
     * 用 {@code KunJinKaoCompileStage} 标记的副本来求值，因此不依赖当前动画状态。
     * 历史上这些模型曾因父模型挂在 builtin/generated 下且缺少 layer0 而被
     * ItemModelGenerator 生成为空模型 —— 那时本方法返回 false，谓词退回完整剑，
     * 至少不会出现"选中剑后手里空空如也"。
     */
    private static boolean areCompileStageModelsUsable(Player player) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return false;
        }
        ItemStack probe = player.getMainHandItem().copy();
        for (int stage = 0; stage < 8; stage++) {
            CompoundTag tag = customData(probe);
            tag.putInt("KunJinKaoCompileStage", stage);
            probe.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
            BakedModel model = minecraft.getItemRenderer().getModel(probe, minecraft.level, player, 0);
            if (model == minecraft.getModelManager().getMissingModel() || countQuads(model) <= 0) {
                LOGGER.warn("剑编译阶段 {} 的模型没有几何体，本次动画退回完整剑模型", stage);
                return false;
            }
        }
        return true;
    }

    /** 统计烘焙模型的面数，用于诊断（完整剑约 3145 面，compile_0 只有个位数）。 */
    private static int countQuads(BakedModel model) {
        RandomSource random = RandomSource.create(0L);
        int quads = model.getQuads(null, null, random).size();
        for (Direction direction : Direction.values()) {
            random.setSeed(0L);
            quads += model.getQuads(null, direction, random).size();
        }
        return quads;
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

    /**
     * 断线/退出世界时的清理入口：复用既有私有 reset()，不对外暴露内部计时字段。
     * 注意 tick() 在 player/level 为空时本来就会调用 reset()，因此该入口只保证
     * "退出世界立刻清空"，不改变进世界时的表现。
     */
    public static void clear() {
        reset();
    }

    private static void reset() {
        wasHoldingSword = false;
        compileHand = InteractionHand.MAIN_HAND;
        drawCompileTicks = 0;
        grabSwordTicks = 0;
        compileRenderObserved = false;
        compileModelLogged = false;
        centerDrawLogged = false;
        compileStageModelsUsable = true;
        rightClickCompileTicks = 0;
        attackHudTicks = 0;
    }

    private static InteractionHand findVisibleSwordHand(Player player) {
        return isVisibleSword(player.getMainHandItem()) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
    }
}
