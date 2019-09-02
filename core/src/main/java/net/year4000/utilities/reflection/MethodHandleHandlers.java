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

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/** Creates instances of the MethodHandler for the proxy tunnel */
final class MethodHandleHandlers {
    /** The custom Lookup instance that allows invoking of private methods and fields, see BlackMagicTest for more */
    static final MethodHandles.Lookup lookup = Reflections.<MethodHandles.Lookup>getter(MethodHandles.Lookup.class, MethodHandles.publicLookup(), "IMPL_LOOKUP").getOrThrow();

    private MethodHandleHandlers() {
        UtilityConstructError.raise();
    }

    /** Create the invoke handler */
    static MethodHandler methodHandle$invokeHandle(Invoke invoke, Method method, @Nullable Object instance) throws NoSuchMethodException, IllegalAccessException {
        Class<?> classInstance = reflectiveClass(method.getDeclaringClass());
        if (!invoke.signature().isEmpty()) { // Search by signature
            Method signature = findMethod(invoke.signature(), classInstance, invoke.value(), invoke.index());
            MethodHandle handle = method.isAnnotationPresent(Static.class)
                ? MethodHandleHandlers.lookup.unreflect(signature)
                : MethodHandleHandlers.lookup.unreflect(signature).bindTo(instance);
            return handleBridge(method, handle::invokeWithArguments);
        }
        // No signature lookup
        String name = Value.of(invoke.value()).getOrElse(method.getName());
        // When the bridge is detected use the reflectiveClass not the class of the return type
        Class<?> returnType = method.isAnnotationPresent(Bridge.class)
            ? reflectiveClass(method.getReturnType())
            : method.getReturnType();
        MethodType methodType = MethodType.methodType(returnType, method.getParameterTypes());
        // When static use the static method handle lookup
        MethodHandle handle = method.isAnnotationPresent(Static.class)
            ? MethodHandleHandlers.lookup.findStatic(classInstance, name, methodType)
            : MethodHandleHandlers.lookup.findVirtual(classInstance, name, methodType).bindTo(instance);
        return handleBridge(method, handle::invokeWithArguments);
    }

    /** Create the getter handler */
    static MethodHandler methodHandle$getterHandle(Getter getter, Method method, @Nullable Object instance) throws NoSuchFieldException, IllegalAccessException {
        Class<?> classInstance = reflectiveClass(method.getDeclaringClass());
        if (!getter.signature().isEmpty()) { // Search by signature
            Field signature = findField(getter.signature(), classInstance, getter.value(), getter.index());
            MethodHandle handle = method.isAnnotationPresent(Static.class)
                ? MethodHandleHandlers.lookup.unreflectGetter(signature)
                : MethodHandleHandlers.lookup.unreflectGetter(signature).bindTo(instance);
            return handleBridge(method, handle::invokeWithArguments);
        }
        // No signature lookup
        String name = Value.of(getter.value()).getOrElse(method.getName());
        // When the bridge is detected use the reflectiveClass not the class of the return type
        Class<?> returnType = method.isAnnotationPresent(Bridge.class)
            ? reflectiveClass(method.getReturnType())
            : method.getReturnType();
        // When static use the static method handle lookup
        MethodHandle handle = method.isAnnotationPresent(Static.class)
            ? MethodHandleHandlers.lookup.findStaticGetter(classInstance, name, returnType)
            : MethodHandleHandlers.lookup.findGetter(classInstance, name, returnType).bindTo(instance);
        return handleBridge(method, handle::invokeWithArguments);
    }

    /** Create the setter handler */
    static MethodHandler methodHandle$setterHandle(Setter setter, Method method, @Nullable Object instance) throws NoSuchFieldException, IllegalAccessException {
        Class<?> classInstance = reflectiveClass(method.getDeclaringClass());
        if (!setter.signature().isEmpty()) { // Search by signature
            Field signature = findField(setter.signature(), classInstance, setter.value(), setter.index());
            MethodHandle handle = method.isAnnotationPresent(Static.class)
                ? MethodHandleHandlers.lookup.unreflectSetter(signature)
                : MethodHandleHandlers.lookup.unreflectSetter(signature).bindTo(instance);
            return handle::invokeWithArguments;
        }
        // No signature lookup
        String name = Value.of(setter.value()).getOrElse(method.getName());
        Class<?> setterParameter = method.getParameterTypes()[0];
        // When a proxy is detected as the parameter use the reflectiveClass not the class of the parameter
        Class<?> setterArgument = setterParameter.isAnnotationPresent(Proxied.class)
            ? reflectiveClass(setterParameter)
            : setterParameter;
        // When static use the static method handle lookup
        MethodHandle handle = setterArgument.isAnnotationPresent(Static.class)
            ? MethodHandleHandlers.lookup.findStaticSetter(classInstance, name, setterArgument)
            : MethodHandleHandlers.lookup.findSetter(classInstance, name, setterArgument).bindTo(instance);
        return handle::invokeWithArguments;
    }

    /** Create the default keyword method handle */
    static MethodHandler defaultHandle(Method method, Object instance) throws Throwable {
        Class<?> proxyClass = method.getDeclaringClass();
        MethodHandle handle = MethodHandleHandlers.lookup.unreflectSpecial(method, proxyClass).bindTo(instance);
        return handle::invokeWithArguments;
    }
}
