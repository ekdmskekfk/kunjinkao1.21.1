package dev.modmind.kunjinkao.overwrite;

import dev.modmind.kunjinkao.KunJinKaoSwordItem;
import dev.modmind.kunjinkao.event.KunJinKaoDeathEventHandler;
import dev.modmind.kunjinkao.event.KunJinKaoProtectionHandler;
import dev.modmind.kunjinkao.network.NetworkHandler;
import dev.modmind.kunjinkao.network.OverwriteEffectPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.bus.api.SubscribeEvent;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 锟斤拷之剑「覆写」流程处理器（服务端）：
 * 目标持有泥土时进入 2 秒（40 tick）覆写：护甲清零、移除正面效果、主手替换为损坏泥土、减速 60%、降跳 80%、
 * 每 4 tick 粒子，BossBar 进度仅对攻击者可见；倒计时结束时触发「断未」，
 * 写入掉落标记后 kill()，并在目标位置生成 3×3×2 屏障区块持续 30 秒，区块内玩家减速且无法破坏方块。
 */
public class KunJinKaoOverwriteHandler {

    private static final org.apache.logging.log4j.Logger LOGGER =
            org.apache.logging.log4j.LogManager.getLogger("KunJinKao");

    public static final int OVERWRITE_TICKS = 40;
    private static final int ZONE_TICKS = 600;

    private static final DustParticleOptions COLD_CORE =
            new DustParticleOptions(new Vector3f(0.00F, 0.90F, 1.00F), 0.95F);
    private static final DustParticleOptions COLD_EDGE =
            new DustParticleOptions(new Vector3f(0.72F, 0.96F, 1.00F), 0.68F);
    private static final DustParticleOptions COLD_PIXEL =
            new DustParticleOptions(new Vector3f(0.18F, 0.58F, 0.84F), 0.55F);

    private static final ResourceLocation SLOW_ID = ResourceLocation.fromNamespaceAndPath("kunjinkao", "overwrite_slow");
    private static final ResourceLocation JUMP_ID = ResourceLocation.fromNamespaceAndPath("kunjinkao", "overwrite_jump");
    private static final ResourceLocation ARMOR_ID = ResourceLocation.fromNamespaceAndPath("kunjinkao", "overwrite_armor");

    private static final Map<UUID, OverwriteState> STATES = new HashMap<>();
    private static final List<Zone> ZONES = new ArrayList<>();

    private static class OverwriteState {
        final LivingEntity target;
        final ServerPlayer attacker;
        final ServerBossEvent bossEvent;
        final ItemStack backupMainHand;
        final int lootingMode;
        final double armorValue;
        final int theme;
        final boolean freezeAi;
        final boolean wasNoAi;
        final boolean wasInvisible;
        boolean freezeApplied;
        int ticksLeft;
        int flickerCounter;
        boolean dirtApplied;
        boolean decisionSent;

        OverwriteState(LivingEntity target, ServerPlayer attacker, ServerBossEvent bossEvent,
                       ItemStack backupMainHand, int lootingMode, double armorValue, int theme) {
            this.target = target;
            this.attacker = attacker;
            this.bossEvent = bossEvent;
            this.backupMainHand = backupMainHand;
            this.lootingMode = lootingMode;
            this.armorValue = armorValue;
            this.theme = theme;
            this.freezeAi = theme == 1 || theme == 4;
            this.wasNoAi = target instanceof Mob mob && mob.isNoAi();
            this.wasInvisible = target.isInvisible();
            this.ticksLeft = OVERWRITE_TICKS;
        }
    }

    private static class Zone {
        final ServerLevel level;
        final BlockPos base;
        final long expireGameTime;
        final Map<BlockPos, BlockState> originals;

        Zone(ServerLevel level, BlockPos base, long expireGameTime, Map<BlockPos, BlockState> originals) {
            this.level = level;
            this.base = base;
            this.expireGameTime = expireGameTime;
            this.originals = originals;
        }

        boolean contains(BlockPos pos) {
            return pos.getX() >= base.getX() - 1 && pos.getX() <= base.getX() + 1
                    && pos.getZ() >= base.getZ() - 1 && pos.getZ() <= base.getZ() + 1
                    && pos.getY() >= base.getY() && pos.getY() <= base.getY() + 1;
        }
    }

    /**
     * 入口：由 KunJinKaoSwordItem.hurtEnemy 在有泥土分支调用。
     */
    public static void startOverwrite(LivingEntity attacker, LivingEntity target, ItemStack sword, ServerLevel level) {
        if (level.isClientSide()) {
            return;
        }
        UUID id = target.getUUID();
        OverwriteState existing = STATES.get(id);
        if (existing != null) {
            // 目标已在覆写中：重置倒计时，其余削弱继续生效
            existing.ticksLeft = OVERWRITE_TICKS;
            existing.flickerCounter = 0;
            if (existing.bossEvent != null) {
                existing.bossEvent.setProgress(1.0F);
            }
            sendStart(existing.attacker, target.getId(), existing.theme);
            return;
        }

        ServerPlayer playerAttacker = attacker instanceof ServerPlayer sp ? sp : null;
        ServerBossEvent bossEvent = null;
        if (playerAttacker != null) {
            bossEvent = new ServerBossEvent(
                    Component.literal("§f§l覆写中 · 断未倒计时"),
                    BossEvent.BossBarColor.WHITE,
                    BossEvent.BossBarOverlay.PROGRESS
            );
            bossEvent.setVisible(true);
            bossEvent.addPlayer(playerAttacker);
            sendStart(playerAttacker, target.getId(), KunJinKaoSwordItem.getTheme(sword));
        }

        OverwriteState state = new OverwriteState(
                target,
                playerAttacker,
                bossEvent,
                target.getMainHandItem().copy(),
                KunJinKaoSwordItem.getLootingMode(sword),
                target.getArmorValue(),
                KunJinKaoSwordItem.getTheme(sword)
        );
        STATES.put(id, state);
        applyDebuffs(state);
        spawnOverwriteParticles(state);
    }

    private static void applyDebuffs(OverwriteState state) {
        LivingEntity target = state.target;
        AttributeInstance speed = target.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speed != null && speed.getModifier(SLOW_ID) == null) {
            speed.addTransientModifier(new AttributeModifier(SLOW_ID, -0.6D, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        }
        AttributeInstance jump = target.getAttribute(Attributes.JUMP_STRENGTH);
        if (jump != null && jump.getModifier(JUMP_ID) == null) {
            jump.addTransientModifier(new AttributeModifier(JUMP_ID, -0.8D, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        }
        if (state.freezeAi && !state.freezeApplied) {
            state.freezeApplied = true;
            if (target instanceof Mob mob) {
                mob.setNoAi(true);
            }
            target.setDeltaMovement(0.0D, 0.0D, 0.0D);
        }
        AttributeInstance armor = target.getAttribute(Attributes.ARMOR);
        if (armor != null && armor.getModifier(ARMOR_ID) == null) {
            armor.addTransientModifier(new AttributeModifier(ARMOR_ID, -state.armorValue, AttributeModifier.Operation.ADD_VALUE));
        }
        List<MobEffectInstance> beneficial = new ArrayList<>();
        for (MobEffectInstance effect : target.getActiveEffects()) {
            if (effect.getEffect().value().isBeneficial()) {
                beneficial.add(effect);
            }
        }
        for (MobEffectInstance effect : beneficial) {
            target.removeEffect(effect.getEffect());
        }
        if (!state.dirtApplied) {
            ItemStack dirt = new ItemStack(Items.DIRT);
            dirt.set(net.minecraft.core.component.DataComponents.CUSTOM_DATA,
                    CustomData.of(new net.minecraft.nbt.CompoundTag()));
            dirt.get(net.minecraft.core.component.DataComponents.CUSTOM_DATA).copyTag().putBoolean("KunJinKaoBrokenDirt", true);
            target.setItemInHand(InteractionHand.MAIN_HAND, dirt);
            state.dirtApplied = true;
        }
    }

    private static void cleanupDebuffs(OverwriteState state) {
        LivingEntity target = state.target;
        removeModifier(target, Attributes.MOVEMENT_SPEED, SLOW_ID);
        removeModifier(target, Attributes.JUMP_STRENGTH, JUMP_ID);
        removeModifier(target, Attributes.ARMOR, ARMOR_ID);
        if (state.freezeApplied && target instanceof Mob mob) {
            mob.setNoAi(state.wasNoAi);
        }
        target.setInvisible(state.wasInvisible);
        if (state.dirtApplied && target.getMainHandItem().is(Items.DIRT)) {
            target.setItemInHand(InteractionHand.MAIN_HAND, state.backupMainHand);
        }
    }

    private static void removeModifier(LivingEntity target, Holder<Attribute> attribute, ResourceLocation id) {
        AttributeInstance instance = target.getAttribute(attribute);
        if (instance != null && instance.getModifier(id) != null) {
            instance.removeModifier(id);
        }
    }

    private static void tickOverwrite(OverwriteState state) {
        LivingEntity target = state.target;
        if (!target.isAlive() || target.isRemoved()) {
            STATES.remove(target.getUUID());
            return;
        }
        state.ticksLeft--;
        state.flickerCounter++;
        if (state.ticksLeft <= 0) {
            finishOverwrite(state);
            return;
        }
        applyDebuffs(state);

        // 覆写期间目标隐身闪烁：每 8 tick 切换一次可见性，强化「执行中」反馈
        // 不再切换实体隐身状态。旧的可见性闪烁会造成“抖动”观感，
        // 现在由冷色六边形网格和由上至下的扫描删除线表现覆写过程。

        // 距断未裁决还有 4 tick：通知客户端进入阶段三（目标头顶红色裁决文字）
        if (!state.decisionSent && state.ticksLeft <= 4) {
            state.decisionSent = true;
            sendToAttacker(state.attacker, target.getId(), state.ticksLeft, OverwriteEffectPayload.PHASE_DECISION);
        }

        // 每 4 tick：粒子与 BossBar 进度、进度包
        if (state.flickerCounter % 2 == 0) {
            spawnOverwriteParticles(state);
        }
        if (state.flickerCounter % 4 == 0) {
            if (state.bossEvent != null) {
                state.bossEvent.setProgress(state.ticksLeft / (float) OVERWRITE_TICKS);
                sendToAttacker(state.attacker, target.getId(), state.ticksLeft, OverwriteEffectPayload.PHASE_PROGRESS);
            }
        }
    }

    private static void finishOverwrite(OverwriteState state) {
        LivingEntity target = state.target;
        UUID id = target.getUUID();
        if (!target.isAlive() || target.isRemoved()) {
            STATES.remove(id);
            return;
        }
        cleanupDebuffs(state);

        // 写入掉落/经验增强标记与断未赐死标记（绕过持剑保护）
        target.getPersistentData().putBoolean(KunJinKaoDeathEventHandler.MARK_KEY, true);
        target.getPersistentData().putInt(KunJinKaoDeathEventHandler.LOOTING_MODE_ENTITY_KEY, state.lootingMode);
        target.getPersistentData().putBoolean(KunJinKaoProtectionHandler.KILL_BY_OVERWRITE_KEY, true);

        if (state.bossEvent != null) {
            state.bossEvent.removePlayer(state.attacker);
            sendToAttacker(state.attacker, target.getId(), target.blockPosition(), OverwriteEffectPayload.PHASE_END);
        }

        if (target.level() instanceof ServerLevel serverLevel) {
            spawnFinalResidue(serverLevel, target);
            spawnUndefinedZone(serverLevel, target.blockPosition());
        }

        // 先移除状态再 kill，避免死亡事件再次进入清理路径
        STATES.remove(id);
        LOGGER.info("[OVERWRITE-FINISH] killing target={}", target.getType());
        target.kill();
    }

    private static void spawnUndefinedZone(ServerLevel level, BlockPos center) {
        Map<BlockPos, BlockState> originals = new HashMap<>();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                for (int dy = 0; dy <= 1; dy++) {
                    BlockPos pos = center.offset(dx, dy, dz);
                    BlockState original = level.getBlockState(pos);
                    if (original.isAir() || original.canBeReplaced() || original.getBlock() == Blocks.BARRIER) {
                        originals.put(pos, original);
                        level.setBlock(pos, Blocks.BARRIER.defaultBlockState(), 3);
                    }
                }
            }
        }
        ZONES.add(new Zone(level, center, level.getGameTime() + ZONE_TICKS, originals));
        level.sendParticles(COLD_PIXEL,
                center.getX() + 0.5D, center.getY() + 1.0D, center.getZ() + 0.5D,
                72, 1.15D, 0.85D, 1.15D, 0.06D);
        level.sendParticles(ParticleTypes.END_ROD,
                center.getX() + 0.5D, center.getY() + 1.0D, center.getZ() + 0.5D,
                20, 0.7D, 0.7D, 0.7D, 0.025D);
    }

    private static void spawnOverwriteParticles(OverwriteState state) {
        LivingEntity target = state.target;
        if (!(target.level() instanceof ServerLevel server)) {
            return;
        }
        double progress = 1.0D - state.ticksLeft / (double) OVERWRITE_TICKS;
        double centerX = target.getX();
        double centerY = target.getY() + target.getBbHeight() * 0.5D;
        double centerZ = target.getZ();
        double baseRadius = Math.max(0.42D, Math.max(target.getBbWidth(), target.getBbHeight() * 0.45D));
        double radius = baseRadius * (1.0D - progress * 0.62D);

        // 两层六边形网格逐步向中心收缩。
        for (int ring = 0; ring < 2; ring++) {
            double ringRadius = radius + ring * 0.13D;
            for (int point = 0; point < 6; point++) {
                double angle = Math.toRadians(point * 60.0D + ring * 30.0D);
                double yOffset = ((point & 1) == 0 ? 0.11D : -0.11D) * (1.0D - progress * 0.5D);
                sendCold(server, ring == 0 ? COLD_CORE : COLD_EDGE,
                        centerX + Math.cos(angle) * ringRadius,
                        centerY + yOffset,
                        centerZ + Math.sin(angle) * ringRadius);
            }
        }

        // 文本被退格般的逐行删除：扫描线始终由上向下移动。
        double scanY = target.getY() + target.getBbHeight() * (1.0D - progress);
        double halfWidth = Math.max(0.28D, target.getBbWidth() * 0.58D);
        for (int column = -3; column <= 3; column++) {
            double offset = column * halfWidth / 3.0D;
            sendCold(server, column == 0 ? COLD_CORE : COLD_EDGE, centerX + offset, scanY, centerZ);
            if ((column & 1) == 0) {
                sendCold(server, COLD_PIXEL, centerX + offset, scanY - 0.045D, centerZ + 0.035D);
            }
        }
    }

    private static void spawnFinalResidue(ServerLevel level, LivingEntity target) {
        double x = target.getX();
        double y = target.getY() + target.getBbHeight() * 0.45D;
        double z = target.getZ();
        level.sendParticles(COLD_PIXEL, x, y, z, 42,
                Math.max(0.35D, target.getBbWidth() * 0.7D), target.getBbHeight() * 0.45D,
                Math.max(0.35D, target.getBbWidth() * 0.7D), 0.055D);
        level.sendParticles(COLD_EDGE, x, y, z, 18,
                0.32D, 0.42D, 0.32D, 0.025D);
    }

    private static void sendCold(ServerLevel level, DustParticleOptions particle, double x, double y, double z) {
        level.sendParticles(particle, x, y, z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
    }

    /**
     * 从攻击者主手/副手查找覆写·断未剑；未找到返回 EMPTY。
     */
    private static ItemStack findSword(LivingEntity holder) {
        ItemStack main = holder.getMainHandItem();
        if (!main.isEmpty() && main.getItem() instanceof KunJinKaoSwordItem) {
            return main;
        }
        ItemStack off = holder.getOffhandItem();
        if (!off.isEmpty() && off.getItem() instanceof KunJinKaoSwordItem) {
            return off;
        }
        return ItemStack.EMPTY;
    }

    /**
     * 执行覆写或瞬杀。成功处理返回 true（调用方应取消原伤害事件，避免免疫机制干扰）。
     */
    private static boolean handleSwordAttack(LivingEntity attacker, LivingEntity target, ItemStack sword, Level level) {
        if (level.isClientSide() || target == null || attacker == null || sword.isEmpty()) {
            return false;
        }
        if (KunJinKaoSwordItem.isDisguised(sword)) {
            LOGGER.info("[SWORD-ATTACK] sword disguised -> vanilla");
            return false;
        }
        if (!(level instanceof ServerLevel serverLevel)) {
            return false;
        }
        if (KunJinKaoSwordItem.isOverwriteEnabled(sword)) {
            LOGGER.info("[SWORD-ATTACK] overwriteEnabled=true -> startOverwrite target={}", target.getType());
            startOverwrite(attacker, target, sword, serverLevel);
        } else {
            LOGGER.info("[SWORD-ATTACK] overwriteEnabled=false -> INSTANT KILL target={}", target.getType());
            KunJinKaoSwordItem.applyKunJinKaoMark(target, sword);
            target.kill();
        }
        return true;
    }

    private static void sendStart(ServerPlayer player, int entityId, int theme) {
        if (player != null) {
            NetworkHandler.sendToPlayer(player, OverwriteEffectPayload.start(entityId, OVERWRITE_TICKS, theme));
        }
    }

    private static void sendToAttacker(ServerPlayer player, int entityId, int remainingTicks, int phase) {
        if (player != null) {
            OverwriteEffectPayload payload = switch (phase) {
                case OverwriteEffectPayload.PHASE_PROGRESS -> OverwriteEffectPayload.progress(entityId, remainingTicks);
                case OverwriteEffectPayload.PHASE_CANCEL -> OverwriteEffectPayload.cancel(entityId);
                case OverwriteEffectPayload.PHASE_DECISION -> OverwriteEffectPayload.decision(entityId, remainingTicks);
                default -> new OverwriteEffectPayload(entityId, remainingTicks, phase, 0, false, 0, 0, 0, "", 0);
            };
            NetworkHandler.sendToPlayer(player, payload);
        }
    }

    private static void sendToAttacker(ServerPlayer player, int entityId, BlockPos pos, int phase) {
        if (player != null) {
            NetworkHandler.sendToPlayer(player, OverwriteEffectPayload.end(entityId, pos));
        }
    }

    private static boolean isInsideAnyZone(Level level, BlockPos pos) {
        for (Zone zone : ZONES) {
            if (zone.level == level && zone.contains(pos)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 玩家攻击兜底：在 Player.attack 流程最开头触发，早于伤害结算与 isInvulnerableTo 免疫判定，
     * 因此即使目标对剑类伤害免疫（例如 BOSS 在 hurt/isInvulnerableTo 中拦截），
     * 只要攻击者手持未伪装的覆写·断未，就会强制接管本次攻击。
     */
    @SubscribeEvent
    public void onPlayerAttack(AttackEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();
        if (player.level().isClientSide()) {
            return;
        }
        if (!(event.getTarget() instanceof LivingEntity target) || target instanceof Player) {
            LOGGER.info("[ATTACK-EVENT] skipped target={} (non-living or player)", event.getTarget());
            return;
        }
        ItemStack sword = findSword(player);
        if (sword.isEmpty()) {
            LOGGER.info("[ATTACK-EVENT] no sword held -> vanilla");
            return;
        }
        LOGGER.info("[ATTACK-EVENT] target={} overwriteEnabled={}",
                target.getType(), KunJinKaoSwordItem.isOverwriteEnabled(sword));
        if (handleSwordAttack(player, target, sword, player.level())) {
            event.setCanceled(true);
        }
    }

    /**
     * 非玩家攻击者兜底：当任意活体实体手持覆写·断未发起伤害时，同样接管攻击。
     * 注意 LivingIncomingDamageEvent 在 isInvulnerableTo 判定之后触发，纯免疫目标不经过此处；
     * 玩家攻击场景已由 onPlayerAttack 完整覆盖。
     */
    @SubscribeEvent
    public void onLivingAttack(LivingIncomingDamageEvent event) {
        LivingEntity target = event.getEntity();
        if (target.level().isClientSide() || target instanceof Player) {
            return;
        }
        if (!(event.getSource().getEntity() instanceof LivingEntity attacker) || attacker instanceof Player) {
            return;
        }
        ItemStack sword = findSword(attacker);
        if (sword.isEmpty()) {
            return;
        }
        if (handleSwordAttack(attacker, target, sword, target.level())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onLivingTick(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof LivingEntity entity)) {
            return;
        }
        if (entity.level().isClientSide()) {
            return;
        }
        OverwriteState state = STATES.get(entity.getUUID());
        if (state != null) {
            tickOverwrite(state);
        }
    }

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        // 目标在覆写期间因其他原因死亡：清理削弱状态并恢复主手，避免原物品被泥土覆盖丢失
        LivingEntity entity = event.getEntity();
        OverwriteState state = STATES.remove(entity.getUUID());
        if (state == null) {
            return;
        }
        cleanupDebuffs(state);
        if (state.bossEvent != null) {
            state.bossEvent.removePlayer(state.attacker);
            sendToAttacker(state.attacker, entity.getId(), 0, OverwriteEffectPayload.PHASE_CANCEL);
        }
    }

    @SubscribeEvent
    public void onServerTick(ServerTickEvent.Post event) {
        Iterator<Zone> iterator = ZONES.iterator();
        while (iterator.hasNext()) {
            Zone zone = iterator.next();
            if (zone.level.getGameTime() >= zone.expireGameTime) {
                for (Map.Entry<BlockPos, BlockState> entry : zone.originals.entrySet()) {
                    zone.level.setBlock(entry.getKey(), entry.getValue(), 3);
                }
                iterator.remove();
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) {
            return;
        }
        if (isInsideAnyZone(player.level(), player.blockPosition())) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 30, 1, false, false, false));
        }
    }

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getLevel() instanceof Level level && isInsideAnyZone(level, event.getPos())) {
            event.setCanceled(true);
            Player player = event.getPlayer();
            player.displayClientMessage(Component.literal("§7§o未定义区块阻断了你的破坏……"), true);
        }
    }
}
