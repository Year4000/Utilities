package net.year4000.utilities;

import com.google.common.base.Joiner;
import net.year4000.utilities.utils.UtilityConstructError;
import net.year4000.utilities.value.Value;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Contains basic util methods that do not belong in a set category */
public final class Utils {
    private Utils() {
        UtilityConstructError.raise();
    }

    /** Grab the field state value and name, used for toString */
    private static Value<String> fieldState(Field field, Object instance) {
        Value<Object> inst = Reflections.field(instance, field.getName());
        if (inst.isPresent()) {
            return Value.of(field.getName() + ": " + inst.get());
        }
        return Value.of(field.getName() + ": null");
    }

    /** Use reflection to print the to string method */
    public static String toString(Object instance, String... vars) {
        String prefix = instance.getClass().getSimpleName();
        Field[] fields = instance.getClass().getDeclaredFields();
        List<String> variables = new ArrayList<>(fields.length);
        if (vars.length == 0) {
            for (Field field : fields) {
                fieldState(field, instance).ifPresent(variables::add);
            }
        } else {
            for (String name : vars) {
                try {
                    fieldState(instance.getClass().getDeclaredField(name), instance).ifPresent(variables::add);
                } catch (NoSuchFieldException error) {
                    error.printStackTrace();
                    variables.add(name + ": error");
                }
            }
        }
        return prefix + "(" + instance.hashCode()  +") {" + Joiner.on(", ").join(variables) + "}";
    }

    /** Check if the objects hash codes matches, thus the objects should be equal */
    public static boolean equals(Object a, Object b) {
        return Objects.hashCode(a) == Objects.hashCode(b);
    }

    /** Generate a hash code for the list of objects */
    public static int hashCode(Object instance, Object... objects) {
        int hash = Objects.hashCode(instance.getClass().getName());
        if (objects.length == 0) {
            Field[] fields = instance.getClass().getDeclaredFields();
            for (Field field : fields) {
                hash ^= objects.length;
                hash += Objects.hashCode(Reflections.field(instance, field.getName()));
            }
        } else {
            for (Object object : objects) {
                hash ^= objects.length;
                hash += Objects.hashCode(object);
            }
        }
        return hash;
    }
}
