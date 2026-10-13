/*
 * Decompiled with CFR 0.152.
 */
package cyou.rimuru.shootingstardemo.mc1211.client.magic;

import com.mojang.blaze3d.platform.InputConstants;
import cyou.rimuru.shootingstardemo.mc1211.magic.d;
import cyou.rimuru.shootingstardemo.mc1211.magic.e;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public final class f {
    public static KeyMapping a;
    public static KeyMapping b;
    private static final Map<d, KeyMapping> c;
    private static final Map<KeyMapping, d> d;

    private f() {
    }

    public static void a(Consumer<KeyMapping> consumer) {
        String string = "key.category.shooting_star_demo.general";
        a = new KeyMapping("key.shooting_star_demo.skill_menu", InputConstants.Type.KEYSYM, 72, string);
        consumer.accept(a);
        b = new KeyMapping("key.shooting_star_demo.cutscenes", InputConstants.Type.KEYSYM, 74, string);
        consumer.accept(b);
        for (e e2 : e.a()) {
            String string2 = "key.category." + e2.a.getNamespace() + "." + e2.a.getPath();
            for (int i2 = 0; i2 < e2.c.size(); ++i2) {
                d d2 = e2.c.get(i2);
                int n2 = d2.g() == 0 ? InputConstants.UNKNOWN.getValue() : d2.g();
                KeyMapping keyMapping = new KeyMapping(e2.d(d2), InputConstants.Type.KEYSYM, n2, string2);
                consumer.accept(keyMapping);
                c.put(d2, keyMapping);
                d.put(keyMapping, d2);
            }
        }
    }

    public static KeyMapping a(d d2) {
        return c.get(d2);
    }

    public static String b(d d2) {
        return f.a(c.get(d2));
    }

    public static String a(KeyMapping keyMapping) {
        if (keyMapping.isUnbound()) {
            return "";
        }
        String string = f.b(keyMapping).getString();
        if (string.length() <= 3) {
            return string;
        }
        String[] stringArray = string.split(" ");
        if (stringArray.length == 1) {
            return string.substring(0, 3);
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String string2 : stringArray) {
            if (string2.isEmpty()) continue;
            stringBuilder.append(Character.isDigit(string2.charAt(0)) ? string2 : string2.substring(0, 1));
        }
        return stringBuilder.length() <= 4 ? stringBuilder.toString() : stringBuilder.substring(0, 4);
    }

    public static Component b(KeyMapping keyMapping) {
        String string;
        String string2 = keyMapping.saveString();
        if (string2.startsWith(string = "key.keyboard.") && string2.length() == string.length() + 1) {
            return Component.literal((String)string2.substring(string.length()).toUpperCase(Locale.ROOT));
        }
        return keyMapping.getTranslatedKeyMessage();
    }

    public static void a(d d2, InputConstants.Key key) {
        c.get(d2).setKey(key);
        KeyMapping.resetMapping();
        Minecraft.getInstance().options.save();
    }

    public static List<KeyMapping> c(d d2) {
        KeyMapping keyMapping = c.get(d2);
        ArrayList<KeyMapping> arrayList = new ArrayList<KeyMapping>();
        if (keyMapping.isUnbound()) {
            return arrayList;
        }
        e e2 = e.b(d2);
        for (KeyMapping keyMapping2 : Minecraft.getInstance().options.keyMappings) {
            d d3;
            if (keyMapping2 == keyMapping || !keyMapping2.same(keyMapping) || (d3 = d.get(keyMapping2)) != null && e.b(d3) != e2) continue;
            arrayList.add(keyMapping2);
        }
        return arrayList;
    }

    static {
        c = new IdentityHashMap<d, KeyMapping>();
        d = new IdentityHashMap<KeyMapping, d>();
    }
}

