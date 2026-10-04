package dev.modmind.kunjinkao.mixin;

import dev.modmind.kunjinkao.world.SwordAreaFields;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * 让暂停场里的<b>方块实体</b>也停下来。
 * <p>
 * 生物与玩家有 {@code EntityTickEvent} 可用，方块实体没有 —— NeoForge 1.21.1
 * 不提供任何方块实体 tick 事件。所以只能动原版的
 * {@code Level.tickBlockEntities}。
 * <p>
 * 用 {@code @Redirect} 而不是 {@code @Inject} + {@code @Local}：
 * {@code @Redirect} 会把被调用方的接收者（那个 {@code TickingBlockEntity}）
 * 直接作为第一个参数交给我，不需要去猜循环里的局部变量名，
 * 也不用担心局部变量表在别的映射下不一样。原方法里这一步是：
 * <pre>
 *   } else if (flag &amp;&amp; this.shouldTickBlocksAt(tickingblockentity.getPos())) {
 *       tickingblockentity.tick();          // ← 这里
 *   }
 * </pre>
 * 客户端不参与判定（{@link SwordAreaFields#isPaused(Level, net.minecraft.core.BlockPos)}
 * 在客户端直接返回 false），所以两边不会各停各的。
 */
@Mixin(Level.class)
public abstract class LevelPauseMixin {

    @Redirect(
            method = "tickBlockEntities",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/entity/TickingBlockEntity;tick()V"
            )
    )
    private void kunjinkao$skipPausedBlockEntities(TickingBlockEntity ticker) {
        Level self = (Level) (Object) this;
        if (SwordAreaFields.isPaused(self, ticker.getPos())) {
            return;
        }
        ticker.tick();
    }
}