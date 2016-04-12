/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

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
        return field(Conditions.nonNull(instance, "instance").getClass(), instance, name);
    }

    /** Get the value of the specific field if it exists and we can access it */
    @SuppressWarnings("unchecked")
    public static <T> Value<T> field(Class<?> clazz, Object instance, String name) {
        try {
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

    /** Get the class object from the name */
    public static Value<Class<?>> clazz(String name) {
        Conditions.nonNullOrEmpty(name, "name");
        try {
            Class<?> clazz = Class.forName(name);
            return Value.of(clazz);
        } catch (ReflectiveOperationException error) {
            return Value.empty();
        }
    }

    /** Get the class object from the name */
    public static Value<Class<?>> clazz(String name, boolean init, ClassLoader loader) {
        Conditions.nonNullOrEmpty(name, "name");
        Conditions.nonNull(loader, "loader");
        try {
            Class<?> clazz = Class.forName(name, init, loader);
            return Value.of(clazz);
        } catch (ReflectiveOperationException error) {
            return Value.empty();
        }
    }
}
