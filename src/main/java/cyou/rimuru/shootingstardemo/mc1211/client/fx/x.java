/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.fx;

import com.mojang.blaze3d.audio.SoundBuffer;
import cyou.rimuru.shootingstardemo.mc1211.ShootingStarDemo;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.Locale;
import javax.sound.sampled.AudioFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.JOrbisAudioStream;
import net.minecraft.client.sounds.SoundBufferLibrary;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.lwjgl.BufferUtils;

public final class x {
    private static final String b = "stretch/";
    private static final double c = 0.04;
    private static final double d = 0.012;
    private static final int e = 4;
    public static SoundBufferLibrary a;

    private x() {
    }

    public static boolean a(float f2) {
        return Math.abs(f2 - 1.0f) > 0.004f;
    }

    public static ResourceLocation a(ResourceLocation resourceLocation, float f2) {
        return ResourceLocation.fromNamespaceAndPath((String)resourceLocation.getNamespace(), (String)(b + Math.round(f2 * 100.0f) + "/" + resourceLocation.getPath()));
    }

    public static boolean a(ResourceLocation resourceLocation) {
        return resourceLocation.getNamespace().equals("shooting_star_demo") && resourceLocation.getPath().startsWith("sounds/stretch/");
    }

    public static void a(String string, float f2) {
        if (a == null || !x.a(f2)) {
            return;
        }
        for (ResourceLocation resourceLocation2 : Minecraft.getInstance().getResourceManager().listResources("sounds", resourceLocation -> resourceLocation.getNamespace().equals("shooting_star_demo") && resourceLocation.getPath().startsWith("sounds/" + string) && resourceLocation.getPath().endsWith(".ogg")).keySet()) {
            String string2 = resourceLocation2.getPath().substring("sounds/".length());
            a.getCompleteBuffer(ResourceLocation.fromNamespaceAndPath((String)resourceLocation2.getNamespace(), (String)("sounds/stretch/" + Math.round(f2 * 100.0f) + "/" + string2)));
        }
    }

    /*
     * Enabled aggressive exception aggregation
     */
    public static SoundBuffer a(ResourceProvider resourceProvider, ResourceLocation resourceLocation) {
        String string = resourceLocation.getPath().substring("sounds/stretch/".length());
        int n2 = string.indexOf(47);
        float f2 = (float)Integer.parseInt(string.substring(0, n2)) / 100.0f;
        ResourceLocation resourceLocation2 = ResourceLocation.fromNamespaceAndPath((String)resourceLocation.getNamespace(), (String)("sounds/" + string.substring(n2 + 1)));
        long l2 = System.nanoTime();
        try (InputStream inputStream = resourceProvider.open(resourceLocation2);){
            SoundBuffer soundBuffer;
            try (JOrbisAudioStream jOrbisAudioStream = new JOrbisAudioStream(inputStream);){
                ByteBuffer byteBuffer = jOrbisAudioStream.readAll();
                AudioFormat audioFormat = jOrbisAudioStream.getFormat();
                ByteBuffer byteBuffer2 = x.a(byteBuffer, audioFormat, f2);
                ShootingStarDemo.LOGGER.d("Stretched {} to {}x in {} ms", resourceLocation2, Float.valueOf(f2), (System.nanoTime() - l2) / 1000000L);
                soundBuffer = new SoundBuffer(byteBuffer2, audioFormat);
            }
            return soundBuffer;
        }
        catch (IOException | RuntimeException exception) {
            throw new IllegalStateException(String.format(Locale.ROOT, "Could not stretch %s to %sx", resourceLocation2, Float.valueOf(f2)), exception);
        }
    }

    static ByteBuffer a(ByteBuffer byteBuffer, AudioFormat audioFormat, float f2) {
        int n2;
        int n3 = audioFormat.getChannels();
        if (audioFormat.getSampleSizeInBits() != 16 || n3 < 1) {
            return byteBuffer;
        }
        ShortBuffer shortBuffer = byteBuffer.duplicate().order(ByteOrder.nativeOrder()).asShortBuffer();
        int n4 = shortBuffer.remaining() / n3;
        float[][] fArray = new float[n3][n4];
        for (int i2 = 0; i2 < n4; ++i2) {
            for (n2 = 0; n2 < n3; ++n2) {
                fArray[n2][i2] = shortBuffer.get(i2 * n3 + n2);
            }
        }
        float[][] fArray2 = x.a(fArray, f2, audioFormat.getSampleRate());
        n2 = fArray2[0].length;
        ByteBuffer byteBuffer2 = BufferUtils.createByteBuffer((int)(n2 * n3 * 2));
        ShortBuffer shortBuffer2 = byteBuffer2.asShortBuffer();
        for (int i3 = 0; i3 < n2; ++i3) {
            for (int i4 = 0; i4 < n3; ++i4) {
                shortBuffer2.put((short)Math.max(Short.MIN_VALUE, Math.min(Short.MAX_VALUE, Math.round(fArray2[i4][i3]))));
            }
        }
        return byteBuffer2;
    }

    static float[][] a(float[][] fArray, float f2, float f3) {
        int n2;
        int n3;
        int n4 = fArray.length;
        int n5 = fArray[0].length;
        int n6 = Math.max(64, (int)Math.round((double)f3 * 0.04) & 0xFFFFFFFE);
        int n7 = n6 / 2;
        int n8 = Math.max(4, (int)Math.round((double)f3 * 0.012));
        int n9 = Math.max(1, (int)Math.round((double)n5 / (double)f2));
        float[] fArray2 = new float[n6];
        for (int i2 = 0; i2 < n6; ++i2) {
            fArray2[i2] = (float)(0.5 - 0.5 * Math.cos(Math.PI * 2 * (double)i2 / (double)n6));
        }
        float[] fArray3 = new float[n5 + n6 + 2 * n8];
        for (int i3 = 0; i3 < n4; ++i3) {
            for (n3 = 0; n3 < n5; ++n3) {
                int n10 = n3;
                fArray3[n10] = fArray3[n10] + fArray[i3][n3] / (float)n4;
            }
        }
        float[][] fArray4 = new float[n4][n9 + n6];
        n3 = 0;
        int n11 = 0;
        while ((long)n11 * (long)n7 < (long)n9) {
            int n12;
            int n13;
            n2 = n11 * n7;
            int n14 = (int)Math.round((double)n11 * (double)n7 * (double)f2);
            if (n11 == 0) {
                n13 = 0;
            } else {
                n12 = n3 + n7;
                n13 = x.a(fArray3, n12, n14, n8, n6, n5);
            }
            for (n12 = 0; n12 < n4; ++n12) {
                int n15;
                float[] fArray5 = fArray[n12];
                float[] fArray6 = fArray4[n12];
                for (int i4 = 0; i4 < n6 && (n15 = n13 + i4) < n5; ++i4) {
                    float f4 = n11 == 0 && i4 < n7 ? 1.0f : fArray2[i4];
                    int n16 = n2 + i4;
                    fArray6[n16] = fArray6[n16] + f4 * fArray5[n15];
                }
            }
            n3 = n13;
            ++n11;
        }
        float[][] fArray7 = new float[n4][n9];
        for (n2 = 0; n2 < n4; ++n2) {
            System.arraycopy(fArray4[n2], 0, fArray7[n2], 0, n9);
        }
        return fArray7;
    }

    private static int a(float[] fArray, int n2, int n3, int n4, int n5, int n6) {
        double d2;
        int n7;
        int n8 = Math.max(0, n3 - n4);
        int n9 = Math.min(Math.max(0, n6 - 1), n3 + n4);
        if (n2 >= n6 || n8 >= n9) {
            return Math.max(0, Math.min(n3, Math.max(0, n6 - 1)));
        }
        int n10 = Math.max(n8, Math.min(n3, n9));
        double d3 = Double.NEGATIVE_INFINITY;
        for (n7 = n8; n7 <= n9; n7 += 4) {
            d2 = x.a(fArray, n2, n7, n5, 4);
            if (!(d2 > d3)) continue;
            d3 = d2;
            n10 = n7;
        }
        n7 = n10;
        d2 = Double.NEGATIVE_INFINITY;
        for (int i2 = Math.max(n8, n7 - 4 + 1); i2 <= Math.min(n9, n7 + 4 - 1); ++i2) {
            double d4 = x.a(fArray, n2, i2, n5, 2);
            if (!(d4 > d2)) continue;
            d2 = d4;
            n10 = i2;
        }
        return n10;
    }

    private static double a(float[] fArray, int n2, int n3, int n4, int n5) {
        double d2 = 0.0;
        int n6 = Math.min(n4, fArray.length - Math.max(n2, n3));
        for (int i2 = 0; i2 < n6; i2 += n5) {
            d2 += (double)(fArray[n2 + i2] * fArray[n3 + i2]);
        }
        return d2;
    }
}

