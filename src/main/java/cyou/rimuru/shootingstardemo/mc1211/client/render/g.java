/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.render;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

final class g {
    static final String a = "#version 150\n\nout vec2 texCoord;\n\nvoid main() {\n    vec2 positions[3] = vec2[](vec2(-1.0, -1.0), vec2(3.0, -1.0), vec2(-1.0, 3.0));\n    vec2 position = positions[gl_VertexID];\n    gl_Position = vec4(position, 0.0, 1.0);\n    texCoord = position * 0.5 + 0.5;\n}\n";
    private static final String c = "/assets/shooting_star_demo/shaders/fx/";
    final int b;
    private final Map<String, Integer> d = new HashMap<String, Integer>();

    private g(int n2) {
        this.b = n2;
    }

    int a(String string2) {
        return this.d.computeIfAbsent(string2, string -> GL20.glGetUniformLocation((int)this.b, (CharSequence)string));
    }

    void a() {
        GL20.glDeleteProgram((int)this.b);
    }

    static String b(String string) throws IOException {
        String string2 = g.d("common.glsl");
        String string3 = g.d(string + ".fsh");
        return string2 + "\n#line 1 1\n" + g.c(string3);
    }

    private static String c(String string) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String[] stringArray = string.split("\n", -1);
        for (int i2 = 0; i2 < stringArray.length; ++i2) {
            String string2 = stringArray[i2].trim();
            if (string2.startsWith("#include \"") && string2.endsWith("\"") && string2.length() > 11) {
                stringBuilder.append(g.d(string2.substring(10, string2.length() - 1))).append("\n#line ").append(i2 + 2).append(" 1\n");
                continue;
            }
            stringBuilder.append(stringArray[i2]).append('\n');
        }
        return stringBuilder.toString();
    }

    private static String d(String string) throws IOException {
        try (InputStream inputStream = g.class.getResourceAsStream(c + string);){
            if (inputStream == null) {
                throw new IOException("missing shader " + string);
            }
            String string2 = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            return string2;
        }
    }

    static g a(String string, String string2) {
        int n2;
        int n3 = g.a(35633, a, string + ".vsh");
        try {
            n2 = g.a(35632, string2, string + ".fsh");
        }
        catch (IllegalStateException illegalStateException) {
            GL20.glDeleteShader((int)n3);
            throw illegalStateException;
        }
        int n4 = GL20.glCreateProgram();
        GL20.glAttachShader((int)n4, (int)n3);
        GL20.glAttachShader((int)n4, (int)n2);
        GL30.glBindFragDataLocation((int)n4, (int)0, (CharSequence)"fragColor");
        GL20.glLinkProgram((int)n4);
        int n5 = GL20.glGetProgrami((int)n4, (int)35714);
        String string3 = GL20.glGetProgramInfoLog((int)n4, (int)32768);
        GL20.glDetachShader((int)n4, (int)n3);
        GL20.glDetachShader((int)n4, (int)n2);
        GL20.glDeleteShader((int)n3);
        GL20.glDeleteShader((int)n2);
        if (n5 == 0) {
            GL20.glDeleteProgram((int)n4);
            throw new IllegalStateException(string + " link failed: " + string3);
        }
        return new g(n4);
    }

    private static int a(int n2, String string, String string2) {
        int n3 = GL20.glCreateShader((int)n2);
        GL20.glShaderSource((int)n3, (CharSequence)string);
        GL20.glCompileShader((int)n3);
        if (GL20.glGetShaderi((int)n3, (int)35713) == 0) {
            String string3 = GL20.glGetShaderInfoLog((int)n3, (int)32768);
            GL20.glDeleteShader((int)n3);
            throw new IllegalStateException(string2 + " compile failed: " + string3);
        }
        return n3;
    }
}

