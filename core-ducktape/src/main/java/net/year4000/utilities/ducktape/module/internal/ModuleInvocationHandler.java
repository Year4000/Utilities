/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.ducktape.module.internal;

import com.google.common.collect.ImmutableMap;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.ErrorReporter;
import net.year4000.utilities.ducktape.module.*;
import net.year4000.utilities.reflection.MethodHandler;
import net.year4000.utilities.reflection.Reflections;
import net.year4000.utilities.tuple.Pair;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** This is the custom invocation handler that handles the module without directly invoking its methods */
public class ModuleInvocationHandler implements InvocationHandler {
    /** The custom Lookup instance that allows invoking of private methods and fields, see BlackMagicTest for more */
    private static final MethodHandles.Lookup LOOKUP = Reflections.<MethodHandles.Lookup>getter(MethodHandles.Lookup.class, MethodHandles.publicLookup(), "IMPL_LOOKUP").getOrThrow();
    private static final ImmutableMap<Class<? extends Annotation>, Pair<String, Class<?>>> METHOD_MAP = ImmutableMap.of(
        Load.class, new Pair<>("load", Loader.class),
        Enable.class, new Pair<>("enable", Enabler.class)
    );
    private final Map<String, MethodHandler> methodLookup = new HashMap<>();
    private final Object moduleInstance;

    private ModuleInvocationHandler(Object moduleInstance) {
        this.moduleInstance = Conditions.nonNull(moduleInstance, "Cant not proxy a null module");
    }

    /** This will create the proxy instance */
    public static Object createProxy(Class<?> moduleClass, Object moduleInstance) {
        final ModuleInvocationHandler invocationHandler = new ModuleInvocationHandler(moduleInstance);
        final List<Class<?>> interfaces = new ArrayList<>();
        // Add our custom method into the proxy
        invocationHandler.methodLookup.put("$this", args -> moduleInstance);
        // Add the methods that we want into the proxy class with the cached method handler
        for (Method method : moduleClass.getMethods()) {
            METHOD_MAP.forEach((annotation, pair) -> {
                if (method.isAnnotationPresent(annotation)) {
                    try {
                        MethodHandle handle = ModuleInvocationHandler.LOOKUP.unreflect(method).bindTo(moduleInstance);
                        interfaces.add(pair.b.get());
                        invocationHandler.methodLookup.put(pair.a.get(), handle::invokeWithArguments);
                    } catch (IllegalAccessException error) {
                        throw ErrorReporter.builder(error).buildAndReport();
                    }
                }
            });
        }
        return Reflections.proxy(ModuleHandle.class, invocationHandler, interfaces);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        try {
            MethodHandler proxyMethod = this.methodLookup.get(method.getName());
            if (proxyMethod != null) {
                return proxyMethod.handle(args);
            }
            return Reflections.invoke(method.getDeclaringClass(), this.moduleInstance, method.getName()).get();
        } catch (Throwable throwable) { // General errors
            throw ErrorReporter.builder(throwable)
                .add("Failed at: ", method.getDeclaringClass() != null ? method.getDeclaringClass().getName() : "null")
                .add("Method: ", method.getName())
                .buildAndReport(System.err);
        }
    }
}
