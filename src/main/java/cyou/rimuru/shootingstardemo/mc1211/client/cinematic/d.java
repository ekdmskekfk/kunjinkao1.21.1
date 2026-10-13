/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.cinematic;

import com.mojang.blaze3d.vertex.PoseStack;
import cyou.rimuru.shootingstardemo.mc1211.ShootingStarDemo;
import cyou.rimuru.shootingstardemo.mc1211.client.cinematic.a;
import cyou.rimuru.shootingstardemo.mc1211.client.cinematic.c;
import cyou.rimuru.shootingstardemo.mc1211.client.cinematic.i;
import cyou.rimuru.shootingstardemo.mc1211.client.magic.f;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.Properties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.neoforged.fml.loading.FMLPaths;

public final class d {
    public static final Path a = FMLPaths.CONFIGDIR.get().resolve("shooting_star_demo.properties");
    private static c b;
    private static c c;
    private static boolean d;
    private static boolean e;
    private static boolean f;
    private static float g;
    private static float h;
    private static float i;

    private d() {
    }

    public static void a() {
        if (!Files.exists(a, new LinkOption[0])) {
            return;
        }
        Properties properties = new Properties();
        try (InputStream inputStream = Files.newInputStream(a, new OpenOption[0]);){
            properties.load(inputStream);
            d = Boolean.parseBoolean(properties.getProperty("cutscenes", "true"));
        }
        catch (IOException iOException) {
            ShootingStarDemo.LOGGER.b("Could not read {}", a, iOException);
        }
    }

    private static void j() {
        Properties properties = new Properties();
        properties.setProperty("cutscenes", Boolean.toString(d));
        try (OutputStream outputStream = Files.newOutputStream(a, new OpenOption[0]);){
            properties.store(outputStream, "The Shooting Star (Demo)");
        }
        catch (IOException iOException) {
            ShootingStarDemo.LOGGER.b("Could not write {}", a, iOException);
        }
    }

    public static boolean b() {
        return d;
    }

    public static void a(boolean bl) {
        if (d != bl) {
            d = bl;
            cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.j();
        }
    }

    public static boolean c() {
        return d && e;
    }

    public static void b(boolean bl) {
        e = bl;
    }

    public static void c(boolean bl) {
        f = bl;
    }

    public static String d() {
        if (b != null && b.e() && !cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.b.p) {
            cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.b.p = true;
            cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.b.q = cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.b.m;
            return null;
        }
        d = !d;
        cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.j();
        return d ? "Spell cutscenes: ON" : "Spell cutscenes: OFF";
    }

    public static void a(c c2) {
        boolean bl;
        if (!d) {
            return;
        }
        boolean bl2 = bl = b != null && !b.f();
        if (bl && !c2.e()) {
            if (c == null || c2.k >= cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.c.k) {
                c = c2;
            }
            return;
        }
        if (bl && cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.b.k > c2.k) {
            if (b.e()) {
                return;
            }
            c = b;
        }
        b = c2;
    }

    public static void e() {
        b = null;
        c = null;
        g = 0.0f;
    }

    public static void f() {
        if (c != null) {
            c.d();
            if (b == null || c.e() && cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.c.k >= cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.b.k) {
                b = c;
                c = null;
                return;
            }
        }
        if (b != null) {
            b.d();
            if (b.f()) {
                b = c;
                c = null;
            }
        }
    }

    public static boolean g() {
        return b != null && g > 0.001f;
    }

    private static boolean b(i i2) {
        return b != null && cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.b.d == i2 && !cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.b.p;
    }

    public static float a(i i2) {
        return cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.b(i2) ? g : 0.0f;
    }

    public static float a(i i2, float f2) {
        return cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.b(i2) ? b.b(f2) : Float.NaN;
    }

    public static boolean h() {
        return f || b != null && g > 0.35f;
    }

    public static float i() {
        return h * g;
    }

    public static float a(float f2) {
        return b == null || g <= 0.001f ? f2 : Mth.lerp((float)g, (float)f2, (float)i);
    }

    public static a a(Vec3 vec3, float f2, float f3, float f4) {
        float f5;
        if (b == null) {
            g = 0.0f;
            return null;
        }
        g = f5 = b.c(f4);
        if (f5 <= 0.001f) {
            return null;
        }
        a a2 = b.e(f4);
        a2 = cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.b.r != null ? cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.b.r.a(b, a2, b.b(f4), f4) : cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.a(a2);
        h = a2.f();
        i = a2.e();
        double d2 = Math.toRadians(f2);
        double d3 = Math.toRadians(f3);
        Vec3 vec32 = vec3.add(-Math.sin(d2) * Math.cos(d3), -Math.sin(d3), Math.cos(d2) * Math.cos(d3));
        a a3 = new a(vec3, vec32, a2.e(), 0.0f);
        return a3.a(a2, f5);
    }

    private static a a(a a2) {
        Entity entity;
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return a2;
        }
        Vec3 vec3 = a2.d().add(0.0, 0.6, 0.0);
        Vec3 vec32 = a2.c().subtract(vec3);
        double d2 = vec32.length();
        if (d2 < 0.001) {
            return a2;
        }
        Vec3 vec33 = vec32.scale(1.0 / d2);
        double d3 = 0.0;
        while (cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.a((Level)minecraft.level, vec3)) {
            if (d3 >= d2) {
                return a2;
            }
            vec3 = vec3.add(vec33);
            d3 += 1.0;
        }
        Object object = entity = minecraft.getCameraEntity() != null ? minecraft.getCameraEntity() : minecraft.player;
        if (entity == null) {
            return a2;
        }
        BlockHitResult blockHitResult = minecraft.level.clip(new ClipContext(vec3, a2.c(), ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, entity));
        if (blockHitResult.getType() != HitResult.Type.BLOCK) {
            return a2;
        }
        Vec3 vec34 = a2.c().subtract(vec3).normalize();
        return new a(blockHitResult.getLocation().subtract(vec34.scale(0.45)), a2.d(), a2.e(), a2.f());
    }

    private static boolean a(Level level, Vec3 vec3) {
        BlockPos blockPos = BlockPos.containing((Position)vec3);
        return blockPos.getY() < level.getMinBuildHeight() || !level.getBlockState(blockPos).getVisualShape((BlockGetter)level, blockPos, CollisionContext.empty()).isEmpty();
    }

    public static void a(GuiGraphics guiGraphics, float f2) {
        if (b == null || g <= 0.001f) {
            return;
        }
        cyou.rimuru.shootingstardemo.mc1211.client.fx.i.a(guiGraphics);
        cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.b(guiGraphics, f2);
        cyou.rimuru.shootingstardemo.mc1211.client.fx.i.b(guiGraphics);
    }

    private static void b(GuiGraphics guiGraphics, float f2) {
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        int n2 = cyou.rimuru.shootingstardemo.mc1211.client.fx.i.c(guiGraphics);
        int n3 = cyou.rimuru.shootingstardemo.mc1211.client.fx.i.d(guiGraphics);
        if (cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.b.l) {
            float f3 = b.b(f2);
            int n4 = (int)(Mth.clamp((float)((f3 - 4.0f) / 8.0f), (float)0.0f, (float)1.0f) * Mth.clamp((float)(g * 2.0f - 1.0f), (float)0.0f, (float)1.0f) * 110.0f);
            if (n4 > 4) {
                String string = "[" + cyou.rimuru.shootingstardemo.mc1211.client.magic.f.a(cyou.rimuru.shootingstardemo.mc1211.client.magic.f.b) + "] skip";
                guiGraphics.drawString(font, string, n2 - 18 - font.width(string), n3 - 38, n4 << 24 | 0xFFFFFF, false);
            }
            return;
        }
        int n5 = (int)((float)n3 * 0.12f * cyou.rimuru.shootingstardemo.mc1211.client.cinematic.c.f(g));
        guiGraphics.fill(0, 0, n2, n5, -16777216);
        guiGraphics.fill(0, n3 - n5, n2, n3, -16777216);
        float f4 = b.b(f2);
        float f5 = Mth.clamp((float)((f4 - 4.0f) / 8.0f), (float)0.0f, (float)1.0f) * Mth.clamp((float)(g * 2.0f - 1.0f), (float)0.0f, (float)1.0f);
        if (f5 > 0.02f && n5 > 18) {
            int n6 = (int)(f5 * 255.0f);
            String string = cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.b.g.toUpperCase();
            int n7 = n3 - n5 + Math.max(3, (n5 - 24) / 2);
            guiGraphics.drawString(font, string, 18, n7, n6 << 24 | 0xE8C66A, false);
            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();
            poseStack.translate(18.0f, (float)(n7 + 11), 0.0f);
            poseStack.scale(1.6f, 1.6f, 1.0f);
            guiGraphics.drawString(font, cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.b.f, 0, 0, n6 << 24 | cyou.rimuru.shootingstardemo.mc1211.client.cinematic.d.b.h & 0xFFFFFF, true);
            poseStack.popPose();
            String string2 = "[" + cyou.rimuru.shootingstardemo.mc1211.client.magic.f.a(cyou.rimuru.shootingstardemo.mc1211.client.magic.f.b) + "] skip";
            guiGraphics.drawString(font, string2, n2 - 18 - font.width(string2), n3 - n5 / 2 - 4, (int)((float)n6 * 0.45f) << 24 | 0xFFFFFF, false);
        }
    }

    static {
        d = true;
        e = true;
    }
}

