package cyou.rimuru.shootingstardemo.mc1211;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * record 的 equals / hashCode / toString 桥接。
 * <p>
 * 原 mod 被 ProGuard 混淆过：这些类在字节码里确实 extends Record，但 Record 属性里保留的是
 * <b>混淆前</b>的组件名（position/look/fov/roll 这种），而字段被改名成了 a/b/c/d，
 * 于是反编译出来的 record 头与类体互相矛盾、源码编不过。
 * <p>
 * 这里按真实字节码把它们还原成普通类（去掉非法的 extends Record），
 * 原来由 invokedynamic 引导方法提供的三个方法改由本类用反射按字段实现 ——
 * 语义与原版一致：逐字段比较、逐字段求哈希、按字段顺序打印。
 */
public final class RecordBridge {

    private RecordBridge() {
    }

    private static List<Field> fieldsOf(Object self) {
        List<Field> result = new ArrayList<>();
        for (Field field : self.getClass().getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) {
                result.add(field);
            }
        }
        result.sort((left, right) -> left.getName().compareTo(right.getName()));
        return result;
    }

    public static boolean equals(Object self, Object other) {
        if (self == other) {
            return true;
        }
        if (other == null || self.getClass() != other.getClass()) {
            return false;
        }
        try {
            for (Field field : fieldsOf(self)) {
                field.setAccessible(true);
                Object left = field.get(self);
                Object right = field.get(other);
                boolean same = field.getType().isArray()
                        ? deepEquals(left, right)
                        : java.util.Objects.equals(left, right);
                if (!same) {
                    return false;
                }
            }
            return true;
        } catch (ReflectiveOperationException failed) {
            throw new IllegalStateException("record equals failed", failed);
        }
    }

    private static boolean deepEquals(Object left, Object right) {
        if (left instanceof Object[] leftArray && right instanceof Object[] rightArray) {
            return Arrays.deepEquals(leftArray, rightArray);
        }
        if (left instanceof int[] leftArray && right instanceof int[] rightArray) {
            return Arrays.equals(leftArray, rightArray);
        }
        if (left instanceof long[] leftArray && right instanceof long[] rightArray) {
            return Arrays.equals(leftArray, rightArray);
        }
        if (left instanceof double[] leftArray && right instanceof double[] rightArray) {
            return Arrays.equals(leftArray, rightArray);
        }
        if (left instanceof float[] leftArray && right instanceof float[] rightArray) {
            return Arrays.equals(leftArray, rightArray);
        }
        if (left instanceof byte[] leftArray && right instanceof byte[] rightArray) {
            return Arrays.equals(leftArray, rightArray);
        }
        if (left instanceof short[] leftArray && right instanceof short[] rightArray) {
            return Arrays.equals(leftArray, rightArray);
        }
        if (left instanceof char[] leftArray && right instanceof char[] rightArray) {
            return Arrays.equals(leftArray, rightArray);
        }
        if (left instanceof boolean[] leftArray && right instanceof boolean[] rightArray) {
            return Arrays.equals(leftArray, rightArray);
        }
        return java.util.Objects.equals(left, right);
    }

    public static int hashCode(Object self) {
        int result = 1;
        try {
            for (Field field : fieldsOf(self)) {
                field.setAccessible(true);
                Object value = field.get(self);
                result = 31 * result + (value == null ? 0 : (field.getType().isArray() ? arrayHash(value) : value.hashCode()));
            }
            return result;
        } catch (ReflectiveOperationException failed) {
            throw new IllegalStateException("record hashCode failed", failed);
        }
    }

    private static int arrayHash(Object value) {
        if (value instanceof Object[] array) {
            return Arrays.deepHashCode(array);
        }
        if (value instanceof int[] array) {
            return Arrays.hashCode(array);
        }
        if (value instanceof long[] array) {
            return Arrays.hashCode(array);
        }
        if (value instanceof double[] array) {
            return Arrays.hashCode(array);
        }
        if (value instanceof float[] array) {
            return Arrays.hashCode(array);
        }
        if (value instanceof byte[] array) {
            return Arrays.hashCode(array);
        }
        if (value instanceof short[] array) {
            return Arrays.hashCode(array);
        }
        if (value instanceof char[] array) {
            return Arrays.hashCode(array);
        }
        if (value instanceof boolean[] array) {
            return Arrays.hashCode(array);
        }
        return value.hashCode();
    }

    public static String toString(Object self) {
        StringBuilder text = new StringBuilder(self.getClass().getSimpleName()).append('[');
        try {
            boolean first = true;
            for (Field field : fieldsOf(self)) {
                field.setAccessible(true);
                if (!first) {
                    text.append(", ");
                }
                first = false;
                text.append(field.getName()).append('=').append(field.get(self));
            }
        } catch (ReflectiveOperationException failed) {
            throw new IllegalStateException("record toString failed", failed);
        }
        return text.append(']').toString();
    }
}