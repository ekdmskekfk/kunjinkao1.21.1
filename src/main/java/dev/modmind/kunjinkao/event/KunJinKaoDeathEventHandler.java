package dev.modmind.kunjinkao.event;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;

import java.util.ArrayList;
import java.util.List;

public class KunJinKaoDeathEventHandler {

    public static final String MARK_KEY = "KunJinKaoMark";
    public static final String LOOTING_MODE_ENTITY_KEY = "KunJinKaoLootingMode";
    public static final String KILLER_UUID_KEY = "KunJinKaoKiller";

    @SubscribeEvent
    public void onLivingDrops(LivingDropsEvent event) {
        CompoundTag targetData = event.getEntity().getPersistentData();
        if (!targetData.getBoolean(MARK_KEY)) return;
        int mode = targetData.getInt(LOOTING_MODE_ENTITY_KEY);
        targetData.remove(MARK_KEY);
        targetData.remove(LOOTING_MODE_ENTITY_KEY);
        if (mode == 0) return;
        LivingEntity entity = event.getEntity();
        int lootingLevel = (mode == 1) ? 25 : 50;
        List<ItemEntity> additionalDrops = new ArrayList<>();
        for (ItemEntity drop : event.getDrops()) {
            ItemStack dropStack = drop.getItem();
            int maxSize = dropStack.getMaxStackSize();
            int added = 0;
            while (added < lootingLevel) {
                int chunk = Math.min(lootingLevel - added, maxSize);
                ItemStack extra = dropStack.copy();
                extra.setCount(chunk);
                additionalDrops.add(new ItemEntity(entity.level(), drop.getX(), drop.getY(), drop.getZ(), extra));
                added += chunk;
            }
        }
        if (entity.level() instanceof ServerLevel serverLevel) {
            List<ItemStack> rerolled = rollEntityLootTable(serverLevel, entity, lootingLevel, event.getSource());
            for (ItemStack stack : rerolled) {
                if (!stack.isEmpty()) additionalDrops.add(new ItemEntity(entity.level(), entity.getX(), entity.getY() + 0.2D, entity.getZ(), stack));
            }
        }
        if (entity instanceof Slime) {
            ItemStack slimeBall = new ItemStack(Items.SLIME_BALL, lootingLevel);
            additionalDrops.add(new ItemEntity(entity.level(), entity.getX(), entity.getY() + 0.2D, entity.getZ(), slimeBall));
        }
        event.getDrops().addAll(additionalDrops);
    }

    private List<ItemStack> rollEntityLootTable(ServerLevel serverLevel, LivingEntity entity, int lootingLevel, DamageSource damageSource) {
        ResourceKey<LootTable> lootTableKey = entity.getType().getDefaultLootTable();
        LootTable lootTable = serverLevel.getServer().getServerResources().managers().fullRegistries().getLootTable(lootTableKey);
        if (lootTable == null || lootTable == LootTable.EMPTY) return List.of();
        LootParams params = new LootParams.Builder(serverLevel)
            .withParameter(LootContextParams.THIS_ENTITY, entity)
            .withParameter(LootContextParams.ORIGIN, entity.position())
            .withParameter(LootContextParams.DAMAGE_SOURCE, damageSource)
            .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, damageSource.getEntity())
            .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, damageSource.getDirectEntity())
            .withLuck(lootingLevel)
            .create(LootContextParamSets.ENTITY);
        return lootTable.getRandomItems(params);
    }
}
