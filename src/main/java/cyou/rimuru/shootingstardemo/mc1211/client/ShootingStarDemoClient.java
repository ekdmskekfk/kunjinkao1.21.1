/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import cyou.rimuru.shootingstardemo.mc1211.ShootingStarDemo;
import cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d;
import cyou.rimuru.shootingstardemo.mc1211.client.fx.e;
import cyou.rimuru.shootingstardemo.mc1211.client.item.a;
import cyou.rimuru.shootingstardemo.mc1211.client.magic.f;
import cyou.rimuru.shootingstardemo.mc1211.client.magic.g;
import cyou.rimuru.shootingstardemo.mc1211.client.render.b;
import cyou.rimuru.shootingstardemo.mc1211.client.render.c;
import cyou.rimuru.shootingstardemo.mc1211.client.render.h;
import cyou.rimuru.shootingstardemo.mc1211.client.render.i;
import java.io.IOException;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;

public final class ShootingStarDemoClient {
    public static void init(IEventBus iEventBus) {
        iEventBus.addListener((net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent registerKeyMappingsEvent) -> f.a(arg_0 -> ((RegisterKeyMappingsEvent)registerKeyMappingsEvent).register(arg_0)));
        d.a();
        a.a(iEventBus);
        iEventBus.addListener((net.neoforged.neoforge.client.event.RegisterShadersEvent registerShadersEvent) -> {
            try {
                registerShadersEvent.registerShader(new ShaderInstance(registerShadersEvent.getResourceProvider(), cyou.rimuru.shootingstardemo.mc1211.client.magic.c.B, DefaultVertexFormat.POSITION_TEX_COLOR), shaderInstance -> {
                    cyou.rimuru.shootingstardemo.mc1211.client.magic.c.A = shaderInstance;
                });
            }
            catch (IOException iOException) {
                throw new IllegalStateException("White Space's shader didn't load", iOException);
            }
        });
        cyou.rimuru.shootingstardemo.mc1211.net.d.a = e::a;
        cyou.rimuru.shootingstardemo.mc1211.net.d.b = cyou.rimuru.shootingstardemo.mc1211.client.magic.a::a;
        cyou.rimuru.shootingstardemo.mc1211.net.d.c = ShootingStarDemoClient::erase;
        IEventBus iEventBus2 = NeoForge.EVENT_BUS;
        iEventBus2.addListener((net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent.LoggingOut loggingOut) -> {
            e.a();
            cyou.rimuru.shootingstardemo.mc1211.client.magic.h.e();
        });
        iEventBus2.addListener((net.neoforged.neoforge.client.event.ClientTickEvent.Post post) -> ShootingStarDemoClient.tick(Minecraft.getInstance()));
        iEventBus2.addListener((net.neoforged.neoforge.client.event.ScreenEvent.Opening post) -> {
            Minecraft minecraft = Minecraft.getInstance();
            if (post.getScreen() instanceof TitleScreen && !b.a().a()) {
                minecraft.setScreen((Screen)new c(b.a()));
            } else {
                Screen screen = post.getScreen();
                if (screen instanceof TitleScreen) {
                    TitleScreen titleScreen = (TitleScreen)screen;
                    if (h.a()) {
                        minecraft.setScreen((Screen)new i((Screen)titleScreen));
                    }
                }
            }
        });
        cyou.rimuru.shootingstardemo.mc1211.magic.b.a = ShootingStarDemoClient::onUse;
        cyou.rimuru.shootingstardemo.mc1211.magic.b.b = f::b;
        cyou.rimuru.shootingstardemo.mc1211.magic.b.c = () -> f.b(f.a).getString();
        iEventBus2.addListener((net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.RightClickBlock entityInteract) -> {
            Player player = entityInteract.getEntity();
            if (!entityInteract.getLevel().isClientSide() || player.isSpectator()) {
                return;
            }
            Item item = player.getItemInHand(entityInteract.getHand()).getItem();
            if (item instanceof cyou.rimuru.shootingstardemo.mc1211.magic.b) {
                cyou.rimuru.shootingstardemo.mc1211.magic.b b2 = (cyou.rimuru.shootingstardemo.mc1211.magic.b)item;
                ShootingStarDemoClient.onUse(player, b2);
                player.swing(entityInteract.getHand());
                entityInteract.setCancellationResult(InteractionResult.FAIL);
                entityInteract.setCanceled(true);
            }
        });
        try {
            Class.forName(ShootingStarDemo.class.getPackageName() + ".devtest.DevAutoTest").getMethod("register", new Class[0]).invoke(null, new Object[0]);
        }
        catch (ClassNotFoundException classNotFoundException) {
        }
        catch (ReflectiveOperationException reflectiveOperationException) {
            throw new IllegalStateException("The dev auto test could not start", reflectiveOperationException);
        }
        ShootingStarDemo.LOGGER.a("The Stellar Remote answers to its wielder.", new Object[0]);
    }

    private static void tick(Minecraft minecraft) {
        String string;
        cyou.rimuru.shootingstardemo.mc1211.client.magic.a.b();
        cyou.rimuru.shootingstardemo.mc1211.client.magic.h.b(minecraft);
        cyou.rimuru.shootingstardemo.mc1211.client.a.a(minecraft);
        boolean bl = minecraft.isPaused();
        if (!bl) {
            d.f();
        }
        while (f.b.consumeClick()) {
            string = d.d();
            if (string == null || minecraft.player == null) continue;
            minecraft.player.displayClientMessage((Component)Component.literal((String)string).withStyle(ChatFormatting.GOLD), true);
        }
        if (!bl) {
            e.a(minecraft);
        }
        Player player = minecraft.player;
        while (f.a.consumeClick()) {
            if (player == null || minecraft.screen != null) continue;
            ShootingStarDemoClient.openMenu(minecraft, player);
        }
        for (cyou.rimuru.shootingstardemo.mc1211.magic.e e2 : cyou.rimuru.shootingstardemo.mc1211.magic.e.a()) {
            for (cyou.rimuru.shootingstardemo.mc1211.magic.d d2 : e2.c) {
                KeyMapping keyMapping = f.a(d2);
                while (keyMapping.consumeClick()) {
                    if (minecraft.screen != null || !cyou.rimuru.shootingstardemo.mc1211.magic.b.a(player, e2)) continue;
                    cyou.rimuru.shootingstardemo.mc1211.client.magic.a.a(d2);
                }
            }
        }
    }

    private static void openMenu(Minecraft minecraft, Player player) {
        cyou.rimuru.shootingstardemo.mc1211.client.magic.h.a(minecraft);
    }

    private static void onUse(Player player, cyou.rimuru.shootingstardemo.mc1211.magic.b b2) {
        cyou.rimuru.shootingstardemo.mc1211.magic.d d2 = cyou.rimuru.shootingstardemo.mc1211.client.magic.a.a(b2.d);
        if (player.isShiftKeyDown()) {
            cyou.rimuru.shootingstardemo.mc1211.client.magic.a.a(cyou.rimuru.shootingstardemo.mc1211.client.magic.a.b(b2.d));
            return;
        }
        int n2 = cyou.rimuru.shootingstardemo.mc1211.client.magic.a.b(d2);
        if (n2 > 0) {
            player.displayClientMessage((Component)Component.literal((String)(d2.c() + " is recharging \u2014 " + (n2 + 19) / 20 + "s")).withStyle(ChatFormatting.DARK_PURPLE), true);
            return;
        }
        cyou.rimuru.shootingstardemo.mc1211.net.d.a(new cyou.rimuru.shootingstardemo.mc1211.net.a(cyou.rimuru.shootingstardemo.mc1211.magic.e.a(d2), g.b(d2), g.d(d2), g.e(d2)));
    }

    private static void erase(cyou.rimuru.shootingstardemo.mc1211.net.c c2) {
        ClientLevel clientLevel = Minecraft.getInstance().level;
        if (clientLevel == null) {
            return;
        }
        for (int n2 : c2.a()) {
            Entity entity = clientLevel.getEntity(n2);
            if (entity == null) continue;
            cyou.rimuru.shootingstardemo.mc1211.star.d.a(entity);
        }
    }
}

