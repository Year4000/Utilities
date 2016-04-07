package net.year4000.utilities;

import com.google.common.base.Joiner;
import com.google.common.base.Throwables;
import net.year4000.utilities.utils.UtilityConstructError;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class ObjectHelper {
    private ObjectHelper() {
        UtilityConstructError.raise();
    }

    public static String toString(Object instance, String... vars) {
        String prefix = instance.getClass().getSimpleName();
        Field[] fields = instance.getClass().getFields();
        List<String> variables = new ArrayList<>(fields.length);
        if (vars.length == 0) {
            for (Field field : fields) {
                fieldState(field, instance).ifPresent(variables::add);
            }
        } else {
            // todo
        }
        return prefix + "(" + instance.hashCode()  +") {" + Joiner.on(", ").join(variables) + "}";
    }

    private static Optional<String> fieldState(Field field, Object instance) {
        boolean access = field.isAccessible();
        try {
            field.setAccessible(true);
            String value = field.getName() + ": " + field.get(instance).toString();
            field.setAccessible(access);
            return Optional.of(value);
        } catch (IllegalAccessException error) {
            return Optional.empty();
        }
    }

    /** Generate a hash code for the list of objects */
    public static int hashCode(Object... objects) {
        int hash = objects.length;
        for (Object object : objects) {
            hash += Objects.hashCode(object);
        }
        return hash;
    }
}
