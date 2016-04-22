/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities;

import net.year4000.utilities.utils.UtilityConstructError;
import net.year4000.utilities.value.Value;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.stream.Stream;

/** A set of tools for handling reflections */
public final class Reflections {
    private Reflections() {
        UtilityConstructError.raise();
    }

    /** Get the method from the name */
    public static Value<Method> method(Class<?> clazz, String method, Object... args) {
        Conditions.nonNull(clazz, "clazz");
        Conditions.nonNullOrEmpty(method, "method");
        try {
            Method invoke = clazz.getDeclaredMethod(method);
            if (args != null && args.length > 0) {
                Class<?>[] types = Stream.of(args).map(Object::getClass).toArray(Class<?>[]::new);
                invoke = clazz.getDeclaredMethod(method, types);
            }
            return Value.of(invoke);
        } catch (ReflectiveOperationException error) {
            return Value.empty();
        }
    }

    /** Invoke the method from the instance, return a value if a return type exists and a non error */
    public static Value<Object> invoke(Object instance, String method, Object... args) {
        return invoke(Conditions.nonNull(instance, "instance").getClass(), instance, method, args);
    }

    /** Invoke the method from the instance, return a value if a return type exists and a non error */
    public static Value<Object> invoke(Class<?> clazz, Object instance, String method, Object... args) {
        return invoke(instance, method(clazz, method, args).get(), args);
    }

    /** Invoke the method statically */
    public static Value<Object> invoke(Class<?> clazz, String method, Object... args) {
        return invoke(method(clazz, method, args).get(), args);
    }

    /** Invoke the method statically */
    public static Value<Object> invoke(Method invoke, Object... args) {
        return invoke(null, invoke, args);
    }

    /** Invoke the method from the instance */
    public static Value<Object> invoke(Object instance, Method invoke, Object... args) {
        Conditions.nonNull(invoke, "invoke");
        try {
            boolean state = invoke.isAccessible();
            invoke.setAccessible(true);
            Object inst = invoke.invoke(instance, args);
            invoke.setAccessible(state);
            return Value.of(inst);
        } catch (ReflectiveOperationException error) {
            return Value.empty();
        }
    }

    /** Get the field from the class */
    public static Value<Field> field(Class<?> clazz, String name) {
        Conditions.nonNull(clazz, "clazz");
        try {
            return Value.of(clazz.getDeclaredField(Conditions.nonNullOrEmpty(name, "name")));
        } catch (ReflectiveOperationException error) {
            return Value.empty();
        }
    }

    /** Set the value of the specific field if it exists and we can access it */
    public static boolean setter(Object instance, String name, Object set) {
        return setter(Conditions.nonNull(instance, "instance").getClass(), instance, name, set);
    }

    /** Set the value of the specific field if it exists and we can access it */
    public static boolean setter(Class<?> clazz, Object instance, String name, Object set) {
        return setter(instance, field(clazz, name).get(), set);
    }

    /** Set the value of the field */
    public static boolean setter(Class<?> clazz, String name, Object set) {
        return setter(field(clazz, name).get(), set);
    }

    /** Set the field of the statically */
    public static boolean setter(Field field, Object set) {
        return setter(null, field, set);
    }

    /** Set the value of the field from the instance */
    public static boolean setter(Object instance, Field field, Object set) {
        Conditions.nonNull(field, "field");
        try {
            boolean state = field.isAccessible();
            field.setAccessible(true);
            field.set(instance, set);
            field.setAccessible(state);
            return true;
        } catch (ReflectiveOperationException error) {
            return false;
        }
    }

    /** Get the value of the specific field if it exists and we can access it */
    @SuppressWarnings("unchecked")
    public static <T> Value<T> getter(Object instance, String name) {
        return getter(Conditions.nonNull(instance, "instance").getClass(), instance, name);
    }

    /** Get the value of the specific field if it exists and we can access it */
    @SuppressWarnings("unchecked")
    public static <T> Value<T> getter(Class<?> clazz, Object instance, String name) {
        try {
            return getter(instance, clazz.getDeclaredField(Conditions.nonNullOrEmpty(name, "name")));
        } catch (ReflectiveOperationException error) {
            return Value.empty();
        }
    }

    /** Get the value of the field */
    public static <T> Value<T> getter(Class<?> clazz, String name) {
        return getter(field(clazz, name).get());
    }

    /** Get the value of the field statically */
    public static <T> Value<T> getter(Field field) {
        return getter(null, field);
    }

    /** Get the value of the field statically */
    @SuppressWarnings("unchecked")
    public static <T> Value<T> getter(Object instance, Field field) {
        Conditions.nonNull(field, "field");
        try {
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
            if (args != null && args.length > 0) {
                Class<?>[] params = Stream.of(args).map(Object::getClass).toArray(Class<?>[]::new);
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
