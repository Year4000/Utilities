/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.reflection;

import static net.year4000.utilities.reflection.Handlers.defaultHandle;
import static net.year4000.utilities.reflection.Handlers.getterHandle;
import static net.year4000.utilities.reflection.Handlers.invokeHandle;
import static net.year4000.utilities.reflection.Handlers.setterHandle;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.ErrorReporter;
import net.year4000.utilities.reflection.annotations.Getter;
import net.year4000.utilities.reflection.annotations.Invoke;
import net.year4000.utilities.reflection.annotations.Setter;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** Create a tunnel of the object and the under lying code */
class Tunnel implements InvocationHandler {
    private final static Cache<Method, MethodHandler> cache = CacheBuilder.newBuilder().softValues().build();
    private final Object instance;

    // Normal proxy with an instance
    Tunnel(Object instance) {
        this.instance = Conditions.nonNull(instance, "instance");
    }

    // Static proxy that access statics only
    Tunnel() {
        this.instance = null; // static proxy
    }

    /** Handle the actual invocation of the method proxy system */
    private Object invokable(Object proxy, Method method, Object[] args) throws Throwable {
        // Caching
        MethodHandler handler = cache.getIfPresent(method);
        if (handler != null) {
            return handler.handle(instance, args);
        }

        // Cache does not exist create one
        if (method.isAnnotationPresent(Invoke.class)) {
            MethodHandler handle = invokeHandle(method, args);
            cache.put(method, handle);
            return handle.handle(instance, args);
        } else if (method.isAnnotationPresent(Setter.class)) {
            Conditions.inRange(args.length, 1, 1); // make sure there is only one argument
            MethodHandler handle = setterHandle(method);
            cache.put(method, handle);
            return handle.handle(instance, args);
        } else if (method.isAnnotationPresent(Getter.class)) {
            MethodHandler handle = getterHandle(method);
            cache.put(method, handle);
            return handle.handle(instance, args);
        } else if (method.isDefault()) { // Can not be cached currently
            return defaultHandle(method, proxy).handle(instance, args);
        }

        // Last if no others are found use the declaring classes method
        return Reflections.invoke(method.getDeclaringClass(), instance, method.getName()).get();
    }

    /** Wraps the error from invokable and print out details */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Allow getting the actual instance, bypassing proxy handles
        if ("$this".equals(method.getName())) {
            return instance;
        }
        // Run the method handles to decided what to do
        try {
            return invokable(proxy, method, args);
        } catch (NoSuchFieldException | NoSuchMethodException exception) { // Show what went wrong
            String annotations = Stream.of(method.getAnnotations())
                .map(Annotation::annotationType)
                .map(clazz -> "@" + clazz.getSimpleName())
                .collect(Collectors.joining(", "));
            // The report
            throw ErrorReporter.builder(exception)
                .hideStackTrace()
                .add("Failed at: ", method.getDeclaringClass().getName())
                .add("Message: ", exception.getMessage())
                .add("Annotation(s): ", annotations)
                .add("Method: ", method.getName())
                .add("Arg(s): ", args)
                .buildAndReport(System.err);
        } catch (Throwable throwable) { // General errors
            throw ErrorReporter.builder(throwable)
                .add("Failed at: ", method.getDeclaringClass().getName())
                .add("Method: ", method.getName())
                .buildAndReport(System.err);
        }
    }
}
