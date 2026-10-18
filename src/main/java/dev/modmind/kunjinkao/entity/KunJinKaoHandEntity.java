package dev.modmind.kunjinkao.entity;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

/**
 * 锟斤拷之手：一只贴地蠕动的手。
 * <p>
 * 造型来自实拍：手背朝上、四指并拢朝前、指甲又长又白、腕部是断口。
 * 它没有腿，所以"走"这件事全靠模型的手指波浪式蠕动（见 {@code KunJinKaoHandModel}）——
 * 实体这一层只用最普通的步行寻路，不为它写任何位移技巧：
 * <b>蠕动必须是动画，不能是物理</b>。用脉冲式位移去"假装爬行"会在斜坡、台阶和冰面上
 * 立刻露馅，而且一拍一拍的位移会和寻路打架。
 * <p>
 * 强度定位：血量 30、单次 4 点伤害、移速 0.27 —— 比僵尸耐打一点、比僵尸慢，
 * 属于"管理员放几只出来当氛围/靶子"的档位，不是 boss。
 */
public class KunJinKaoHandEntity extends Monster {

    public KunJinKaoHandEntity(EntityType<? extends KunJinKaoHandEntity> type, Level level) {
        super(type, level);
        this.xpReward = 6;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.27D)
                .add(Attributes.FOLLOW_RANGE, 24.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.35D)
                // 1.21 里步高是属性（不再是 setMaxUpStep）。0.6 是原版默认，
                // 显式写出来是为了说明"贴地爬行的手能翻上台阶"这件事是设计好的。
                .add(Attributes.STEP_HEIGHT, 0.6D);
    }

    @Override
    protected void registerGoals() {
        // 近战追击。第二个参数是速度倍率，第三个 true = 边追边重新规划路径。
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
        // 没有目标时在地上慢慢挪。
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    /**
     * 贴地的东西不该吃摔落伤害：它能从两格高的地方掉下去，但"啪"一下摔掉血很出戏。
     * 其余伤害照常。
     */
    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, source, recentlyHit);
        // 指骨。用原版的骨头而不是新加一个物品：这东西的价值在"长得吓人"，
        // 不在掉落物上；真要给它做专属掉落，应该连配方一起设计，而不是先扔一个用不上的东西。
        this.spawnAtLocation(new ItemStack(Items.BONE, 1 + this.random.nextInt(2)));
        if (this.random.nextFloat() < 0.25F) {
            // 皮 —— 照片里那只手背上的薄皮与血管，掉落上给一个"皮"的对应物。
            this.spawnAtLocation(new ItemStack(Items.LEATHER));
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        // 借蜘蛛的音效：都是"细碎的爬行系"声音，比给新生物造一套音效省事且不出戏。
        return SoundEvents.SPIDER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.SPIDER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SPIDER_DEATH;
    }

    /**
     * 攻击时朝目标扑一下。
     * <p>
     * 只给一点点水平速度（0.25），够让"拍"这个动作有冲量，又不会把它自己甩出去 ——
     * 这个实体没有腿，用力过猛会表现为"飞出去然后滑行"，比不动更难看。
     */
    @Override
    public boolean doHurtTarget(net.minecraft.world.entity.Entity target) {
        boolean hit = super.doHurtTarget(target);
        if (hit && target instanceof LivingEntity living) {
            double dx = living.getX() - this.getX();
            double dz = living.getZ() - this.getZ();
            double length = Math.sqrt(dx * dx + dz * dz);
            if (length > 1.0E-4D) {
                this.setDeltaMovement(this.getDeltaMovement().add(dx / length * 0.25D, 0.05D, dz / length * 0.25D));
                this.hasImpulse = true;
            }
        }
        return hit;
    }

    /** 它不会被日光点着：一只手站在太阳下起火没有道理。原版 1.21 的日光点燃是僵尸/骷髅各自内联的，没有可重写的钩子，所以这里什么都不用做。 */

    /** 让模型知道"现在该蠕动得多用力"：追击时用走路的摆幅，站着时留一点轻微蠕动。 */
    public float squirmAmplitude(float limbSwingAmount) {
        return 0.25F + 0.75F * Math.min(1.0F, limbSwingAmount);
    }

    /** 调试/工具用：这个实体是不是我们自己的手（避免 instanceof 散落各处）。 */
    public static boolean isHand(net.minecraft.world.entity.Entity entity) {
        return entity instanceof KunJinKaoHandEntity;
    }

    /** 让它别和同类打起来（同类之间不抢目标）。 */
    @Override
    public boolean canAttackType(EntityType<?> type) {
        return type != KunJinKaoEntities.HAND.get() && super.canAttackType(type);
    }
}