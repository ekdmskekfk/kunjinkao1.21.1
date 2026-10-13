/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import net.neoforged.fml.loading.FMLPaths;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

public final class a {
    public static final String a = "shooting_star.log";
    private static final DateTimeFormatter b = DateTimeFormatter.ofPattern("HH:mm:ss");
    private BufferedWriter c;
    private boolean d;

    a() {
    }

    public void a(String string, Object ... objectArray) {
        this.a("INFO", string, objectArray);
    }

    public void b(String string, Object ... objectArray) {
        this.a("WARN", string, objectArray);
    }

    public void c(String string, Object ... objectArray) {
        this.a("ERROR", string, objectArray);
    }

    public void d(String string, Object ... objectArray) {
        this.a("DEBUG", string, objectArray);
    }

    public static Path a() {
        return cyou.rimuru.shootingstardemo.mc1211.a.b().resolve(a);
    }

    private static Path b() {
        return FMLPaths.GAMEDIR.get();
    }

    private synchronized void a(String string, String string2, Object[] objectArray) {
        FormattingTuple formattingTuple = MessageFormatter.arrayFormat((String)string2, (Object[])objectArray);
        StringBuilder stringBuilder = new StringBuilder().append('[').append(LocalTime.now().format(b)).append("] [").append(Thread.currentThread().getName()).append('/').append(string).append("] ").append(formattingTuple.getMessage());
        Throwable throwable = formattingTuple.getThrowable();
        if (throwable != null) {
            StringWriter stringWriter = new StringWriter();
            throwable.printStackTrace(new PrintWriter(stringWriter));
            stringBuilder.append(System.lineSeparator()).append(stringWriter.toString().stripTrailing());
        }
        if (!this.d) {
            this.d = true;
            try {
                this.c = Files.newBufferedWriter(cyou.rimuru.shootingstardemo.mc1211.a.a(), StandardCharsets.UTF_8, new OpenOption[0]);
            }
            catch (IOException | RuntimeException exception) {
                this.c = null;
                System.err.println("[shooting_star_demo] could not open shooting_star.log: " + String.valueOf(exception));
            }
        }
        if (this.c != null) {
            try {
                this.c.write(stringBuilder.toString());
                this.c.newLine();
                this.c.flush();
                return;
            }
            catch (IOException iOException) {
                this.c = null;
            }
        }
        System.out.println("[shooting_star_demo] " + String.valueOf(stringBuilder));
    }
}

