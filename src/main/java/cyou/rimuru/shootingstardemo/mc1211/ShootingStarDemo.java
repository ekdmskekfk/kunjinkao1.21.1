/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211;

import cyou.rimuru.shootingstardemo.mc1211.client.ShootingStarDemoClient;
import cyou.rimuru.shootingstardemo.mc1211.magic.a;
import cyou.rimuru.shootingstardemo.mc1211.net.d;
import cyou.rimuru.shootingstardemo.mc1211.registry.b;
import cyou.rimuru.shootingstardemo.mc1211.registry.e;
import cyou.rimuru.shootingstardemo.mc1211.registry.f;
import cyou.rimuru.shootingstardemo.mc1211.registry.g;
import cyou.rimuru.shootingstardemo.mc1211.spell.c;
import cyou.rimuru.shootingstardemo.mc1211.star.i;
import cyou.rimuru.shootingstardemo.mc1211.star.j;
import cyou.rimuru.shootingstardemo.mc1211.star.m;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;

/**
 * 原 mod 的入口类。
 * <p>
 * 它的 @Mod 构造函数已删除：注册表、事件与客户端初始化现在由 kunjinkao 的入口
 * （KunJinKaoEntry）统一接线，这个类只留常量与 logger 给其它类用。
 * 反编译产物里那个构造函数本身也编不过（方法引用推断不出类型变量、事件类型对不上）。
 */
public final class ShootingStarDemo {
    public static final String MOD_ID = "shooting_star_demo";
    public static final cyou.rimuru.shootingstardemo.mc1211.a LOGGER = new cyou.rimuru.shootingstardemo.mc1211.a();
    public static final String DEMO_NOTICE = ShootingStarDemo.decoded("RGVtbyBtYWRlIGJ5IHRpa3Rvay5jb20vQHJpbXVydS5kZXYgLSBBbGwgcmlnaHRzIHJlc2VydmVk");
    public static final String DEMO_HANDLE = ShootingStarDemo.decoded("dGlrdG9rLmNvbS9AcmltdXJ1LmRldg==");


    private static String decoded(String string) {
        return new String(Base64.getDecoder().decode(string), StandardCharsets.UTF_8);
    }
}

