/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client;

import cyou.rimuru.shootingstardemo.mc1211.ShootingStarDemo;
import cyou.rimuru.shootingstardemo.mc1211.client.render.b;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public final class a {
    private static final Set<String> a = new LinkedHashSet<String>();
    private static final List<String> b = new ArrayList<String>();

    private a() {
    }

    public static void a(String string, String string2) {
        Minecraft.getInstance().execute(() -> {
            if (!a.add(string)) {
                return;
            }
            ShootingStarDemo.LOGGER.b("Spell effect problem: {} ({})", string, string2);
            b.add(string);
        });
    }

    public static void a(Minecraft minecraft) {
        if (b.isEmpty() || minecraft.player == null) {
            return;
        }
        String string = cyou.rimuru.shootingstardemo.mc1211.client.render.b.a().b();
        minecraft.player.displayClientMessage((Component)Component.translatable((String)"gui.shooting_star_demo.effects_failed", (Object[])new Object[]{string, String.join((CharSequence)", ", b)}).withStyle(ChatFormatting.GOLD), false);
        b.clear();
    }
}

