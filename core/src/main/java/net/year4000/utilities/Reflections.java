package net.year4000.utilities;

import net.year4000.utilities.utils.UtilityConstructError;
import net.year4000.utilities.value.Value;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

/** A set of tools for handling reflections */
public final class Reflections {
    private Reflections() {
        UtilityConstructError.raise();
    }

    /** Get the value of the specific field if it exists and we can access it */
    @SuppressWarnings("unchecked")
    public static <T> Value<T> field(Object instance, String name) {
        try {
            Class<?> clazz = Conditions.nonNull(instance, "instance").getClass();
            Field field = clazz.getDeclaredField(Conditions.nonNullOrEmpty(name, "name"));
            boolean state = field.isAccessible();
            field.setAccessible(true);
            T value = (T) field.get(instance);
            field.setAccessible(state);
            return Value.of(value);
        } catch (ReflectiveOperationException error) {
            return Value.empty();
        }
    }

    /** Create an instance of the provided object */
    public static <T> Value<T> instance(Class<T> clazz, Object... args) {
        try {
            Constructor<T> constructor = Conditions.nonNull(clazz, "clazz").getDeclaredConstructor();
            if (args.length > 0) {
                Class<?>[] params = Arrays.asList(args).stream()
                    .map(Object::getClass)
                    .collect(Collectors.toList())
                    .toArray(new Class[args.length]);
                constructor = clazz.getDeclaredConstructor(params);
            }
            boolean state = constructor.isAccessible();
            constructor.setAccessible(true);
            T value = constructor.newInstance(args);
            constructor.setAccessible(state);
            return Value.of(value);
        } catch (ReflectiveOperationException error) {
            return Value.empty();
        }
    }
}
