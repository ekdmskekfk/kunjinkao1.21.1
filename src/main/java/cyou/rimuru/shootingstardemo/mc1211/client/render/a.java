/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.render;

import cyou.rimuru.shootingstardemo.mc1211.ShootingStarDemo;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import javax.imageio.ImageIO;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

public final class a {
    private static final String a = "/assets/shooting_star_demo/textures/";
    private static final Map<String, Integer> b = new HashMap<String, Integer>();
    private static final Map<String, CompletableFuture<Entry>> c = new HashMap<String, CompletableFuture<Entry>>();

    private a() {
    }

    public static void a(String string) {
        if (!b.containsKey(string) && !c.containsKey(string)) {
            CompletableFuture completableFuture = new CompletableFuture();
            Thread thread = new Thread(() -> completableFuture.complete(cyou.rimuru.shootingstardemo.mc1211.client.render.a.c(string)), "Shooting Star texture " + string);
            thread.setDaemon(true);
            thread.start();
            c.put(string, completableFuture);
        }
    }

    public static int b(String string) {
        Integer n2 = b.get(string);
        if (n2 != null) {
            return n2;
        }
        cyou.rimuru.shootingstardemo.mc1211.client.render.a.a(string);
        CompletableFuture<Entry> completableFuture = c.get(string);
        if (!completableFuture.isDone()) {
            return 0;
        }
        c.remove(string);
        Entry a2 = completableFuture.getNow(null);
        int n3 = a2 == null ? 0 : cyou.rimuru.shootingstardemo.mc1211.client.render.a.a(a2);
        b.put(string, n3);
        return n3;
    }

    public static void a() {
        b.values().forEach(n2 -> {
            if (n2 != 0) {
                GL11.glDeleteTextures((int)n2);
            }
        });
        b.clear();
    }

    private static Entry c(String string) {
        String[] stringArray = string.split("\\+");
        try {
            int n2;
            if (stringArray.length == 1) {
                return cyou.rimuru.shootingstardemo.mc1211.client.render.a.a(stringArray[0], 0);
            }
            Entry[] aArray = new Entry[stringArray.length];
            for (n2 = 0; n2 < stringArray.length; ++n2) {
                aArray[n2] = cyou.rimuru.shootingstardemo.mc1211.client.render.a.a(stringArray[n2], 1);
            }
            n2 = aArray[0].b;
            int n3 = aArray[0].c;
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(n2 * n3 * 4);
            for (int i2 = 0; i2 < n2 * n3; ++i2) {
                for (int i3 = 0; i3 < 4; ++i3) {
                    boolean bl;
                    Entry a2 = i3 < aArray.length ? aArray[i3] : null;
                    boolean bl2 = bl = a2 != null && a2.b == n2 && a2.c == n3;
                    byteBuffer.put(i2 * 4 + i3, bl ? a2.a.get(i2) : (byte)(i3 == 3 ? 255 : 0));
                }
            }
            return new Entry(byteBuffer, n2, n3, 4);
        }
        catch (IOException | RuntimeException exception) {
            ShootingStarDemo.LOGGER.c("Could not load spell texture {}: {}", string, exception.getMessage());
            cyou.rimuru.shootingstardemo.mc1211.client.a.a("image " + string, String.valueOf(exception.getMessage()));
            return null;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static Entry a(String string, int n2) throws IOException {
        Object object;
        ByteBuffer byteBuffer;
        int n3;
        int n4;
        int n5;
        BufferedImage bufferedImage;
        block18: {
            boolean bl;
            block17: {
                block16: {
                    try (InputStream inputStream = a.class.getResourceAsStream(a + string);){
                        if (inputStream == null) {
                            throw new IOException("missing " + string);
                        }
                        bufferedImage = ImageIO.read(inputStream);
                    }
                    if (bufferedImage == null) {
                        throw new IOException(string + ": unreadable image");
                    }
                    n5 = bufferedImage.getWidth();
                    n4 = bufferedImage.getHeight();
                    boolean bl2 = bl = bufferedImage.getRaster().getNumBands() == 1;
                    n3 = n2 != 0 ? n2 : (bl ? 1 : 4);
                    byteBuffer = ByteBuffer.allocateDirect(n5 * n4 * n3);
                    object = bufferedImage.getRaster().getDataBuffer();
                    if (!(object instanceof DataBufferByte)) break block16;
                    DataBufferByte dataBufferByte = (DataBufferByte)object;
                    if (bufferedImage.getType() != 10 && bufferedImage.getType() != 5) break block16;
                    object = dataBufferByte.getData();
                    break block17;
                }
                object = new int[n5];
                break block18;
            }
            for (int i2 = 0; i2 < n5 * n4; ++i2) {
                if (bl) {
                    byte object2 = ((byte[])object)[i2];
                    if (n3 == 1) {
                        byteBuffer.put(i2, (byte)object2);
                        continue;
                    }
                    byteBuffer.put(i2 * 4, (byte)object2).put(i2 * 4 + 1, (byte)object2).put(i2 * 4 + 2, (byte)object2).put(i2 * 4 + 3, (byte)-1);
                    continue;
                }
                if (n3 == 1) {
                    byteBuffer.put(i2, (byte)(((((byte[])object)[i2 * 3] & 0xFF) + (((byte[])object)[i2 * 3 + 1] & 0xFF) + (((byte[])object)[i2 * 3 + 2] & 0xFF)) / 3));
                    continue;
                }
                byteBuffer.put(i2 * 4, ((byte[])object)[i2 * 3 + 2]).put(i2 * 4 + 1, ((byte[])object)[i2 * 3 + 1]).put(i2 * 4 + 2, ((byte[])object)[i2 * 3]).put(i2 * 4 + 3, (byte)-1);
            }
            return new Entry(byteBuffer, n5, n4, n3);
        }
        for (int i3 = 0; i3 < n4; ++i3) {
            bufferedImage.getRGB(0, i3, n5, 1, (int[])object, 0, n5);
            for (int i4 = 0; i4 < n5; ++i4) {
                int object3 = ((int[])object)[i4];
                int n6 = i3 * n5 + i4;
                if (n3 == 1) {
                    byteBuffer.put(n6, (byte)(((object3 >> 16 & 0xFF) + (object3 >> 8 & 0xFF) + (object3 & 0xFF)) / 3));
                    continue;
                }
                byteBuffer.put(n6 * 4, (byte)(object3 >> 16)).put(n6 * 4 + 1, (byte)(object3 >> 8)).put(n6 * 4 + 2, (byte)object3).put(n6 * 4 + 3, (byte)-1);
            }
        }
        return new Entry(byteBuffer, n5, n4, n3);
    }

    private static int a(Entry a2) {
        int n2;
        int n3 = GL11.glGetInteger((int)32873);
        int n4 = GL11.glGetInteger((int)35055);
        int[] nArray = new int[]{3317, 3314, 3315, 3316};
        int[] nArray2 = new int[nArray.length];
        for (n2 = 0; n2 < nArray.length; ++n2) {
            nArray2[n2] = GL11.glGetInteger((int)nArray[n2]);
        }
        GL15.glBindBuffer((int)35052, (int)0);
        GL11.glPixelStorei((int)3317, (int)1);
        GL11.glPixelStorei((int)3314, (int)0);
        GL11.glPixelStorei((int)3315, (int)0);
        GL11.glPixelStorei((int)3316, (int)0);
        n2 = GL11.glGenTextures();
        GL11.glBindTexture((int)3553, (int)n2);
        GL11.glTexParameteri((int)3553, (int)10241, (int)9987);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9729);
        GL11.glTexParameteri((int)3553, (int)10242, (int)10497);
        GL11.glTexParameteri((int)3553, (int)10243, (int)33071);
        if (GL.getCapabilities().GL_EXT_texture_filter_anisotropic) {
            GL11.glTexParameterf((int)3553, (int)34046, (float)8.0f);
        }
        if (a2.d == 1) {
            GL11.glTexImage2D((int)3553, (int)0, (int)33321, (int)a2.b, (int)a2.c, (int)0, (int)6403, (int)5121, (ByteBuffer)a2.a);
            GL11.glTexParameteri((int)3553, (int)36419, (int)6403);
            GL11.glTexParameteri((int)3553, (int)36420, (int)6403);
        } else {
            GL11.glTexImage2D((int)3553, (int)0, (int)32856, (int)a2.b, (int)a2.c, (int)0, (int)6408, (int)5121, (ByteBuffer)a2.a);
        }
        GL30.glGenerateMipmap((int)3553);
        for (int i2 = 0; i2 < nArray.length; ++i2) {
            GL11.glPixelStorei((int)nArray[i2], (int)nArray2[i2]);
        }
        GL15.glBindBuffer((int)35052, (int)n4);
        GL11.glBindTexture((int)3553, (int)n3);
        return n2;
    }

    private static final class Entry
{
        private final ByteBuffer a;
        private final int b;
        private final int c;
        private final int d;

        private Entry(ByteBuffer pixels, int width, int height, int channels) {
            this.a = pixels;
            this.b = width;
            this.c = height;
            this.d = channels;
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

        public ByteBuffer a() {
            return this.a;
        }

        public int b() {
            return this.b;
        }

        public int c() {
            return this.c;
        }

        public int d() {
            return this.d;
        }
    }
}

