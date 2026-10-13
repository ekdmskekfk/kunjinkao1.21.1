/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.render;

import cyou.rimuru.shootingstardemo.mc1211.ShootingStarDemo;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

public final class b {
    static final int a = 16;
    static final int b = 4096;
    private static final String[] c = new String[]{"llvmpipe", "softpipe", "swiftshader", "microsoft basic render", "gdi generic", "software rasterizer", "lavapipe"};
    private static a d;

    private b() {
    }

    public static a a() {
        if (d == null) {
            d = cyou.rimuru.shootingstardemo.mc1211.client.render.b.b();
            if (d.a()) {
                ShootingStarDemo.LOGGER.a("GPU check passed: {} ({}, {})", cyou.rimuru.shootingstardemo.mc1211.client.render.b.d.a, cyou.rimuru.shootingstardemo.mc1211.client.render.b.d.b, cyou.rimuru.shootingstardemo.mc1211.client.render.b.d.c);
            } else {
                ShootingStarDemo.LOGGER.b("GPU check failed on {} ({}, {}): {}", cyou.rimuru.shootingstardemo.mc1211.client.render.b.d.a, cyou.rimuru.shootingstardemo.mc1211.client.render.b.d.b, cyou.rimuru.shootingstardemo.mc1211.client.render.b.d.c, cyou.rimuru.shootingstardemo.mc1211.client.render.b.d.d.stream().map(Component::getString).toList());
            }
        }
        return d;
    }

    public static a a(List<String> list) {
        a a2 = cyou.rimuru.shootingstardemo.mc1211.client.render.b.a();
        ArrayList<Component> arrayList = new ArrayList<Component>(a2.d);
        arrayList.add((Component)Component.translatable((String)"gui.shooting_star_demo.gpu.shaders", (Object[])new Object[]{list.size(), String.join((CharSequence)", ", list)}));
        return new a(a2.a, a2.b, a2.c, List.copyOf(arrayList));
    }

    private static a b() {
        int n2;
        String string = cyou.rimuru.shootingstardemo.mc1211.client.render.b.a(7937);
        String string2 = cyou.rimuru.shootingstardemo.mc1211.client.render.b.a(7936);
        String string3 = cyou.rimuru.shootingstardemo.mc1211.client.render.b.a(7938);
        ArrayList<MutableComponent> arrayList = new ArrayList<MutableComponent>();
        String string4 = string.toLowerCase(Locale.ROOT);
        String[] stringArray = c;
        int n3 = stringArray.length;
        for (int i2 = 0; i2 < n3; ++i2) {
            String string5 = stringArray[i2];
            if (!string4.contains(string5)) continue;
            arrayList.add(Component.translatable((String)"gui.shooting_star_demo.gpu.software", (Object[])new Object[]{string}));
            break;
        }
        if ((n2 = GL11.glGetInteger((int)34930)) < 16) {
            arrayList.add(Component.translatable((String)"gui.shooting_star_demo.gpu.units", (Object[])new Object[]{n2, 16}));
        }
        if ((n3 = GL11.glGetInteger((int)3379)) < 4096) {
            arrayList.add(Component.translatable((String)"gui.shooting_star_demo.gpu.texture", (Object[])new Object[]{n3, 4096}));
        }
        if (!cyou.rimuru.shootingstardemo.mc1211.client.render.b.c()) {
            arrayList.add(Component.translatable((String)"gui.shooting_star_demo.gpu.float"));
        }
        if ("fail".equals(System.getProperty("shooting_star_demo.gpucheck"))) {
            arrayList.add(Component.translatable((String)"gui.shooting_star_demo.gpu.forced"));
        }
        return new a(string, string2, string3, List.copyOf(arrayList));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static boolean c() {
        int n2 = GL11.glGetInteger((int)34016);
        GL13.glActiveTexture((int)33984);
        int n3 = GL11.glGetInteger((int)32873);
        int n4 = GL11.glGetInteger((int)36006);
        int n5 = GL11.glGetInteger((int)36010);
        int n6 = GL11.glGenTextures();
        int n7 = GL30.glGenFramebuffers();
        try {
            GL11.glBindTexture((int)3553, (int)n6);
            GL11.glTexParameteri((int)3553, (int)10241, (int)9729);
            GL11.glTexParameteri((int)3553, (int)10240, (int)9729);
            GL11.glTexImage2D((int)3553, (int)0, (int)34842, (int)4, (int)4, (int)0, (int)6408, (int)5126, (long)0L);
            GL30.glBindFramebuffer((int)36160, (int)n7);
            GL30.glFramebufferTexture2D((int)36160, (int)36064, (int)3553, (int)n6, (int)0);
            boolean bl = GL30.glCheckFramebufferStatus((int)36160) == 36053;
            return bl;
        }
        finally {
            GL30.glBindFramebuffer((int)36009, (int)n4);
            GL30.glBindFramebuffer((int)36008, (int)n5);
            GL30.glDeleteFramebuffers((int)n7);
            GL11.glBindTexture((int)3553, (int)n3);
            GL11.glDeleteTextures((int)n6);
            GL13.glActiveTexture((int)n2);
            while (GL11.glGetError() != 0) {
            }
        }
    }

    private static String a(int n2) {
        String string = GL11.glGetString((int)n2);
        return string == null ? "?" : string;
    }

    public static final class a
{
        private final String a;
        private final String b;
        private final String c;
        private final List<Component> d;

        public a(String renderer, String vendor, String version, List<Component> problems) {
            this.a = renderer;
            this.b = vendor;
            this.c = version;
            this.d = problems;
        }

        public boolean a() {
            return this.d.isEmpty();
        }

        @Override
        public final String toString() {
            return cyou.rimuru.shootingstardemo.mc1211.RecordBridge.toString(this);
        }

        @Override
        public final int hashCode() {
            return (int)cyou.rimuru.shootingstardemo.mc1211.RecordBridge.hashCode(this);
        }

        @Override
        public final boolean equals(Object object) {
            return (boolean)cyou.rimuru.shootingstardemo.mc1211.RecordBridge.equals(this, object);
        }

        public String b() {
            return this.a;
        }

        public String c() {
            return this.b;
        }

        public String d() {
            return this.c;
        }

        public List<Component> e() {
            return this.d;
        }
    }
}

