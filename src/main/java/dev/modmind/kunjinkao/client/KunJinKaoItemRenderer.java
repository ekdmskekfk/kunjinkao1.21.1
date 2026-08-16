package dev.modmind.kunjinkao.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.modmind.kunjinkao.KunJinKaoSwordItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class KunJinKaoItemRenderer implements IClientItemExtensions {

    private static final KunJinKaoItemRenderer INSTANCE = new KunJinKaoItemRenderer();

    public static KunJinKaoItemRenderer instance() {
        return INSTANCE;
    }

    @Override
    public net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer getCustomRenderer() {
        // 强制使用原版模型渲染；不再提供自定义 BEWLR（此前 renderByItem 递归调用 renderStatic 会导致物品被放弃渲染而透明）
        return null;
    }
}
