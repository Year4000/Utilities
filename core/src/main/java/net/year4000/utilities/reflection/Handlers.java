/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.reflection;

import static net.year4000.utilities.reflection.Gateways.reflectiveClass;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import net.year4000.utilities.annotations.Nullable;
import net.year4000.utilities.reflection.annotations.*;
import net.year4000.utilities.reflection.lookups.SignatureLookup;
import net.year4000.utilities.utils.UtilityConstructError;
import net.year4000.utilities.value.Value;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/** Creates instances of the MethodHandler for the proxy tunnel */
final class Handlers {
    /** The custom Lookup instance that allows invoking of private methods and fields, see BlackMagicTest for more */
    private static final MethodHandles.Lookup lookup = Reflections.<MethodHandles.Lookup>getter(MethodHandles.Lookup.class, MethodHandles.publicLookup(), "IMPL_LOOKUP").getOrThrow();

    private Handlers() {
        UtilityConstructError.raise();
    }

    /** Handle the bridge between objects and its proxy */
    private static MethodHandler handleBridge(Method method, MethodHandler handler) {
        if (method.isAnnotationPresent(Bridge.class)) {
            final Class<?> bridge = method.getAnnotation(Bridge.class).value();
            return args -> {
                // null return types should just return null
                Object returnInst = handler.handle(args);
                return (returnInst == null) ? null : Gateways.proxy(bridge, returnInst);
            };
        }
        return handler;
    }

    /** Wrap index values, -1 gets the last value, -2 gets the second to last value and so on */
    private static int index(int index, int length) {
        return (index < 0) ? length + index : index;
    }

    /** Find the method */
    private static Method findMethod(String signature, Class<?> clazz, String method, int index) throws NoSuchMethodException {
        ImmutableSortedSet<Method> methods = SignatureLookup.methods(signature, clazz).findSorted();
        // Only one just return it
        if (methods.size() == 1) {
            return methods.first();
        }
        // Filter by name
        Method[] result = methods.stream().filter(name -> name.getName().contains(method)).toArray(Method[]::new);
        if (result.length > 0) {
            return result[index(index, result.length)];
        }
        // Non found throw error
        throw new NoSuchMethodException("No method by this signature: " + signature);
    }

    /** Find the method */
    private static Field findField(String signature, Class<?> clazz, String field, int index) throws NoSuchFieldException {
        ImmutableSet<Field> fields = SignatureLookup.fields(signature, clazz).find();
        // Only one just return it
        if (fields.size() == 1) {
            return fields.iterator().next();
        }
        // Filter by name
        Field[] result = fields.stream().filter(name -> name.getName().contains(field)).toArray(Field[]::new);
        if (result.length > 0) {
            return result[index(index, result.length)];
        }
        // Non found throw error
        throw new NoSuchFieldException("No field by this signature: " + signature);
    }

    /** Create the invoke handler */
    static MethodHandler invokeHandle(Method method, @Nullable Object instance) throws NoSuchMethodException, IllegalAccessException {
        Invoke invoke = method.getAnnotation(Invoke.class);
        Class<?> classInstance = reflectiveClass(method.getDeclaringClass());
        if (!invoke.signature().isEmpty()) { // Search by signature
            Method signature = findMethod(invoke.signature(), classInstance, invoke.value(), invoke.index());
            MethodHandle handle = Handlers.lookup.unreflect(signature);
            if (instance != null) {
                handle = handle.bindTo(instance);
            }
            return handleBridge(method, handle::invokeWithArguments);
        }
        // All else
        String name = Value.of(invoke.value()).getOrElse(method.getName());
        if (method.isAnnotationPresent(Bridge.class)) {
            if (method.isAnnotationPresent(Static.class)) {
                MethodHandle handle = Handlers.lookup.findStatic(classInstance, name, MethodType.methodType(reflectiveClass(method.getReturnType()), method.getParameterTypes()));
                return handleBridge(method, handle::invokeWithArguments);
            }
            MethodHandle handle = Handlers.lookup.findVirtual(classInstance, name, MethodType.methodType(reflectiveClass(method.getReturnType()), method.getParameterTypes())).bindTo(instance);
            return handleBridge(method, handle::invokeWithArguments);
        }
        if (method.isAnnotationPresent(Static.class)) {
            MethodHandle handle = Handlers.lookup.findStatic(classInstance, name, MethodType.methodType(method.getReturnType(), method.getParameterTypes()));
            return handleBridge(method, handle::invokeWithArguments);
        }
        MethodHandle handle = Handlers.lookup.findVirtual(classInstance, name, MethodType.methodType(method.getReturnType(), method.getParameterTypes())).bindTo(instance);
        return handleBridge(method, handle::invokeWithArguments);
    }

    /** Create the getter handler */
    static MethodHandler getterHandle(Method method, @Nullable Object instance) throws NoSuchFieldException, IllegalAccessException {
        Getter getter = method.getAnnotation(Getter.class);
        Class<?> classInstance = reflectiveClass(method.getDeclaringClass());
        if (!getter.signature().isEmpty()) {
            Field signature = findField(getter.signature(), classInstance, getter.value(), getter.index());
            MethodHandle handle = instance != null ? Handlers.lookup.unreflectGetter(signature).bindTo(instance) : Handlers.lookup.unreflectGetter(signature);
            return handleBridge(method, handle::invokeWithArguments);
        }
        String name = Value.of(getter.value()).getOrElse(method.getName());
        if (method.isAnnotationPresent(Bridge.class)) {
            if (method.isAnnotationPresent(Static.class)) {
                MethodHandle handle = Handlers.lookup.findStaticGetter(classInstance, name, reflectiveClass(method.getReturnType()));
                return handleBridge(method, handle::invokeWithArguments);
            }
            MethodHandle handle = Handlers.lookup.findGetter(classInstance, name, reflectiveClass(method.getReturnType())).bindTo(instance);
            return handleBridge(method, handle::invokeWithArguments);
        }
        if (method.isAnnotationPresent(Static.class)) {
            MethodHandle handle = Handlers.lookup.findStaticGetter(classInstance, name, method.getReturnType());
            return handleBridge(method, handle::invokeWithArguments);
        }
        MethodHandle handle = Handlers.lookup.findGetter(classInstance, name, method.getReturnType()).bindTo(instance);
        return handleBridge(method, handle::invokeWithArguments);
    }

    /** Create the setter handler */
    static MethodHandler setterHandle(Method method, @Nullable Object instance) throws NoSuchFieldException, IllegalAccessException {
        Setter setter = method.getAnnotation(Setter.class);
        Class<?> classInstance = reflectiveClass(method.getDeclaringClass());
        if (!setter.signature().isEmpty()) {
            Field signature = findField(setter.signature(), classInstance, setter.value(), setter.index());
            MethodHandle handle = instance != null ? Handlers.lookup.unreflectSetter(signature).bindTo(instance) : Handlers.lookup.unreflectSetter(signature);
            return handle::invokeWithArguments;
        }
        String name = Value.of(method.getAnnotation(Setter.class).value()).getOrElse(method.getName());
        if (method.isAnnotationPresent(Static.class)) {
            Class<?> setterArgument = method.getParameterTypes()[0];
            if (setterArgument.isAnnotationPresent(Proxied.class)) {
                MethodHandle handle = Handlers.lookup.findStaticSetter(classInstance, name, reflectiveClass(setterArgument));
                return handle::invokeWithArguments;
            }
            MethodHandle handle = Handlers.lookup.findStaticSetter(classInstance, name, method.getParameterTypes()[0]);
            return handle::invokeWithArguments;
        }
        Class<?> setterArgument = method.getParameterTypes()[0];
        if (setterArgument.isAnnotationPresent(Proxied.class)) {
            MethodHandle handle = Handlers.lookup.findSetter(classInstance, name, reflectiveClass(setterArgument)).bindTo(instance);
            return handle::invokeWithArguments;
        }
        MethodHandle handle = Handlers.lookup.findSetter(classInstance, name, method.getParameterTypes()[0]).bindTo(instance);
        return handle::invokeWithArguments;
    }

    /** Create the default keyword method handle */
    static MethodHandler defaultHandle(Method method, @Nullable Object instance) throws Throwable {
        Class<?> proxyClass = method.getDeclaringClass();
        MethodHandle handle = Handlers.lookup.unreflectSpecial(method, proxyClass).bindTo(instance);
        return handle::invokeWithArguments;
    }
}
