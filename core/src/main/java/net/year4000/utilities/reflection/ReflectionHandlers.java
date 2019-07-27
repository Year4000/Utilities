/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.reflection;

import static net.year4000.utilities.reflection.Gateways.reflectiveClass;
import static net.year4000.utilities.reflection.HandlerUtils.handleBridge;
import static net.year4000.utilities.reflection.HandlerUtils.findField;
import static net.year4000.utilities.reflection.HandlerUtils.findMethod;

import net.year4000.utilities.annotations.Nullable;
import net.year4000.utilities.reflection.annotations.*;
import net.year4000.utilities.utils.UtilityConstructError;
import net.year4000.utilities.value.Value;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/** Creates instances of the MethodHandler for the proxy tunnel */
final class ReflectionHandlers {

    private ReflectionHandlers() {
        UtilityConstructError.raise();
    }

    /** Create the invoke handler */
    static MethodHandler reflection$invokeHandle(Invoke invoke, Method method, @Nullable Object instance) throws NoSuchMethodException {
        Class<?> classInstance = reflectiveClass(method.getDeclaringClass());
        if (!invoke.signature().isEmpty()) { // Search by signature
            Method signature = findMethod(invoke.signature(), classInstance, invoke.value(), invoke.index());
            return args -> Reflections.invoke(instance, signature, args).get();
        }
        // No signature lookup
        String name = Value.of(invoke.value()).getOrElse(method.getName());
        return handleBridge(method, args -> Reflections.invoke(classInstance, instance, name, args).get());
    }

    /** Create the getter handler */
    static MethodHandler reflection$getterHandle(Getter getter, Method method, @Nullable Object instance) throws NoSuchFieldException {
        Class<?> classInstance = reflectiveClass(method.getDeclaringClass());
        if (!getter.signature().isEmpty()) { // Search by signature
            Field signature = findField(getter.signature(), classInstance, getter.value(), getter.index());
            return args -> Reflections.getter(instance, signature).get();
        }
        // No signature lookup
        String name = Value.of(getter.value()).getOrElse(method.getName());
        return handleBridge(method, args -> Reflections.getter(classInstance, instance, name).get());
    }

    /** Create the setter handler */
    static MethodHandler reflection$setterHandle(Setter setter, Method method, @Nullable Object instance) throws NoSuchFieldException {
        Class<?> classInstance = reflectiveClass(method.getDeclaringClass());
        if (!setter.signature().isEmpty()) { // Search by signature
            Field signature = findField(setter.signature(), classInstance, setter.value(), setter.index());
            return args -> Reflections.setter(classInstance, instance, signature.getName(), args[0]);
        }
        // No signature lookup
        String name = Value.of(setter.value()).getOrElse(method.getName());
        return args -> Reflections.setter(classInstance, instance, name, args[0]);
    }
}
