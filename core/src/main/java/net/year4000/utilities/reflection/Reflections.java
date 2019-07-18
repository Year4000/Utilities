/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.reflection;

import net.year4000.utilities.Conditions;
import net.year4000.utilities.reflection.lookups.SignatureLookup;
import net.year4000.utilities.utils.UtilityConstructError;
import net.year4000.utilities.value.Value;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

/** A set of tools for handling reflections */
public final class Reflections {
    private Reflections() {
        UtilityConstructError.raise();
    }

    /** Create a proxy interface from the proxy interface with the handler and other interfaces the proxy must implement */
    public static Object proxy(InvocationHandler handler, Collection<Class<?>> classes) {
        return proxy(handler, Conditions.nonNull(classes, "classes must not be null but can be empty").toArray(new Class<?>[] {}));
    }

    /** Create a proxy interface from the proxy interface with the handler and other interfaces the proxy must implement */
    public static Object proxy(InvocationHandler handler, Class<?>... classes) {
        Conditions.nonNull(handler, "handler");
        // Proxy that implements other interfaces
        if (classes.length > 0) {
            for (Class<?> clazz : classes) {
                Conditions.condition(clazz.isInterface(), clazz.getSimpleName() + " must be an interface");
            }
            return Proxy.newProxyInstance(classes[0].getClassLoader(), classes, handler);
        }
        // Simple proxy
        return Proxy.newProxyInstance(handler.getClass().getClassLoader(), new Class<?>[] {}, handler);
    }

    /** Create a proxy interface from the proxy interface with the handler and other interfaces the proxy must implement */
    public static <T> T proxy(Class<T> proxy, InvocationHandler handler, Collection<Class<?>> classes) {
        return proxy(proxy, handler, Conditions.nonNull(classes, "classes must not be null but can be empty").toArray(new Class<?>[] {}));
    }

    /** Create a proxy interface from the proxy interface with the handler and other interfaces the proxy must implement */
    public static <T> T proxy(Class<T> proxy, InvocationHandler handler, Class<?>... classes) {
        Conditions.nonNull(proxy, "proxy");
        Conditions.nonNull(handler, "handler");
        Conditions.condition(proxy.isInterface(), "Proxy class must be an interface");
        // Proxy that implements other interfaces
        if (classes.length > 0) {
            for (Class<?> clazz : classes) {
                Conditions.condition(clazz.isInterface(), clazz.getSimpleName() + " must be an interface");
            }
            Class<?>[] interfaces = new Class<?>[classes.length + 1];
            interfaces[0] = proxy;
            System.arraycopy(classes, 0, interfaces, 1, classes.length);
            Object proxied = Proxy.newProxyInstance(proxy.getClassLoader(), interfaces, handler);
            return proxy.cast(proxied);
        }
        // Simple proxy
        return proxy.cast(Proxy.newProxyInstance(proxy.getClassLoader(), new Class<?>[] {proxy}, handler));
    }

    /** Get the method from the name */
    public static Value<Method> method(Class<?> clazz, String method, Object... args) {
        Conditions.nonNull(clazz, "clazz");
        Conditions.nonNullOrEmpty(method, "method");
        try {
            if (args != null && args.length > 0) {
                Class<?>[] types = Stream.of(args).map(Object::getClass).toArray(Class<?>[]::new);
                return Value.of(clazz.getDeclaredMethod(method, types));
            }
            return Value.of(clazz.getDeclaredMethod(method));
        } catch (ReflectiveOperationException error) {
            return Value.empty();
        }
    }

    /** Get the method from the name but give elevated access, this will avoid setting access to the method */
    public static Value<Method> methodElevated(Class<?> clazz, String method, Object... args) throws SecurityException {
        Value<Method> methodValue = method(clazz, method, args);
        Method nullableMethod = methodValue.get();
        if (nullableMethod != null) {
            nullableMethod.setAccessible(true);
        }
        return methodValue;
    }

    /** Invoke the method from the instance, return a value if a return type exists and a non error */
    public static Value<Object> invoke(Object instance, String method, Object... args) throws SecurityException {
        return invoke(Conditions.nonNull(instance, "instance").getClass(), instance, method, args);
    }

    /** Invoke the method statically */
    public static Value<Object> invoke(Class<?> clazz, String method, Object... args) throws SecurityException {
        return invoke(clazz, null, method, args);
    }

    /** Invoke the method from the instance, return a value if a return type exists and a non error */
    public static Value<Object> invoke(Class<?> clazz, Object instance, String method, Object... args) throws SecurityException {
        try {
            // Find the method using the parameters with elevated access
            Method methodElevated = methodElevated(clazz, method, args).getOrThrow();
            Object inst = methodElevated.invoke(instance, args);
            return Value.of(inst);
        } catch (ReflectiveOperationException | NullPointerException error) {
            return Value.empty();
        }
    }

    /** Invoke the method statically */
    public static Value<Object> invoke(Method invoke, Object... args) throws SecurityException {
        return invoke(null, invoke, args);
    }

    /** Invoke the method from the instance */
    public static Value<Object> invoke(Object instance, Method invoke, Object... args) throws SecurityException {
        Conditions.nonNull(invoke, "invoke");
        try {
            invoke.setAccessible(true);
            Object inst = invoke.invoke(instance, args);
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

    /** Get the field from the class but give elevated access, this will avoid setting access to the field */
    public static Value<Field> fieldElevated(Class<?> clazz, String name) throws SecurityException {
        Value<Field> fieldValue = field(clazz, name);
        Field nullableField = fieldValue.get();
        if (nullableField != null) {
            nullableField.setAccessible(true);
        }
        return fieldValue;
    }

    /** Set the value of the specific field if it exists and we can access it */
    public static boolean setter(Object instance, String name, Object set) throws SecurityException {
        return setter(Conditions.nonNull(instance, "instance").getClass(), instance, name, set);
    }

    /** Set the value of the field */
    public static boolean setter(Class<?> clazz, String name, Object set) throws SecurityException {
        return setter(clazz, null, name, set);
    }

    /** Set the value of the specific field if it exists and we can access it */
    public static boolean setter(Class<?> clazz, Object instance, String name, Object set) throws SecurityException {
        try {
            // Find the field using the parameters with elevated access
            Field field = fieldElevated(clazz, name).getOrThrow();
            if (field != null) {
                field.set(instance, set);
            }
            return true;
        } catch (ReflectiveOperationException | NullPointerException error) {
            return false;
        }
    }

    /** Set the field of the statically */
    public static boolean setter(Field field, Object set) throws SecurityException {
        return setter(null, field, set);
    }

    /** Set the value of the field from the instance */
    public static boolean setter(Object instance, Field field, Object set) throws SecurityException {
        Conditions.nonNull(field, "field");
        try {
            field.setAccessible(true);
            field.set(instance, set);
            return true;
        } catch (ReflectiveOperationException error) {
            return false;
        }
    }

    /** Get the value of the specific field if it exists and we can access it */
    @SuppressWarnings("unchecked")
    public static <T> Value<T> getter(Object instance, String name) throws SecurityException {
        return getter(Conditions.nonNull(instance, "instance").getClass(), instance, name);
    }

    /** Get the value of the field */
    public static <T> Value<T> getter(Class<?> clazz, String name) throws SecurityException {
        return getter(clazz, null, name);
    }

    /** Get the value of the specific field if it exists and we can access it */
    @SuppressWarnings("unchecked")
    public static <T> Value<T> getter(Class<?> clazz, Object instance, String name) throws SecurityException {
        try {
            // Find the field using the parameters with elevated access
            Field field = fieldElevated(clazz, name).getOrThrow();
            if (field != null) {
                return Value.of((T) field.get(instance));
            }
            return Value.empty();
        } catch (ReflectiveOperationException | NullPointerException error) {
            return Value.empty();
        }
    }

    /** Get the value of the field statically */
    public static <T> Value<T> getter(Field field) throws SecurityException {
        return getter(null, field);
    }

    /** Get the value of the field statically */
    @SuppressWarnings("unchecked")
    public static <T> Value<T> getter(Object instance, Field field) throws SecurityException {
        Conditions.nonNull(field, "field");
        try {
            field.setAccessible(true);
            T value = (T) field.get(instance);
            return Value.of(value);
        } catch (ReflectiveOperationException error) {
            return Value.empty();
        }
    }

    /** Create an instance of the provided object with the specific signature */
    public static <T> Value<T> instance(String signature, Class<T> clazz, Object... args) throws SecurityException {
        try {
            SignatureLookup<Constructor<T>> lookup = SignatureLookup.constructors(signature, clazz);
            Constructor<T> constructor = lookup.find().iterator().next();
            Conditions.condition(constructor.getParameterCount() == args.length, "Args must match signature");
            constructor.setAccessible(true);
            T value = constructor.newInstance(args);
            return Value.of(value);
        } catch (ReflectiveOperationException | NoSuchElementException error) {
            error.printStackTrace();
            return Value.empty();
        }
    }

    /** Create an instance of the provided object */
    public static <T> Value<T> instance(Class<T> clazz, Object... args) throws SecurityException {
        try {
            Constructor<T> constructor = Conditions.nonNull(clazz, "clazz").getDeclaredConstructor();
            if (args != null && args.length > 0) {
                Class<?>[] params = Stream.of(args).map(Object::getClass).toArray(Class<?>[]::new);
                constructor = clazz.getDeclaredConstructor(params);
            }
            constructor.setAccessible(true);
            T value = constructor.newInstance(args);
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
