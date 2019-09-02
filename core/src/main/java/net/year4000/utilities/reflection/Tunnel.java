/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.reflection;

import static net.year4000.utilities.reflection.MethodHandleHandlers.defaultHandle;
import static net.year4000.utilities.reflection.MethodHandleHandlers.methodHandle$getterHandle;
import static net.year4000.utilities.reflection.MethodHandleHandlers.methodHandle$invokeHandle;
import static net.year4000.utilities.reflection.MethodHandleHandlers.methodHandle$setterHandle;
import static net.year4000.utilities.reflection.ReflectionHandlers.reflection$getterHandle;
import static net.year4000.utilities.reflection.ReflectionHandlers.reflection$invokeHandle;
import static net.year4000.utilities.reflection.ReflectionHandlers.reflection$setterHandle;

import com.google.common.annotations.Beta;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableMap;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.ErrorReporter;
import net.year4000.utilities.reflection.annotations.*;
import net.year4000.utilities.reflection.lookups.SignatureLookup;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** Create a tunnel of the object and the under lying code */
class Tunnel<T> implements InvocationHandler {
    private final Cache<Method, MethodHandler> cache = CacheBuilder.newBuilder().softValues().build();
    private final Map<String, InvocationHandler> internalMethods;
    private final Object instance;
    private final Class<T> clazz;
    private final boolean useMethodHandle;

    // Normal proxy with an instance
    Tunnel(Class<T> clazz, Object instance) {
        this.clazz = clazz;
        this.useMethodHandle = clazz.getAnnotation(Proxied.class).methodHandle();
        this.instance = Conditions.nonNull(instance, "instance");
        this.internalMethods = populateInternalMethods();
    }

    // Static proxy that access statics only
    Tunnel(Class<T> clazz) {
        this.clazz = clazz;
        this.useMethodHandle = clazz.getAnnotation(Proxied.class).methodHandle();
        this.instance = null; // static proxy
        this.internalMethods = populateInternalMethods();
    }

    /** This is a prototype to have a decorated method of DuckType classes */
    @Beta
    private Map<String, InvocationHandler> createDecoratedMethods() {
        ImmutableMap.Builder<String, InvocationHandler> decorates = ImmutableMap.builder();

        if (this.clazz != null) {
            for (Method methods : this.clazz.getMethods()) {
                if (methods.isDefault() && methods.isAnnotationPresent(Decorator.class)) {
                    Decorator decorator = methods.getAnnotation(Decorator.class);
                    // todo place a different decorate depending on the signature type of the method
                    decorates.put(decorator.value(), ((proxy, method, args) -> {
                        // replace the reference of method with the decorated version
                        Method decoratedMethod = (Method) defaultHandle(methods, proxy).handle(new Object[]{ method });
                        return Reflections.invoke(decoratedMethod.getDeclaringClass(), this.instance, decoratedMethod.getName()).get();
                    }));

                }
            }
        }

        return decorates.build();
    }

    /** Create a set of internal methods that will run instead of the default behavior */
    private Map<String, InvocationHandler> populateInternalMethods() {
        return ImmutableMap.<String, InvocationHandler>builder()
            .putAll(createDecoratedMethods())
            // Allow getting the actual instance, bypassing proxy handles
            .put("$this", (proxy, method, args) -> this.instance)
            // Invalidate the method cache of the proxy
            .put("$invalidate", (proxy, method, args) -> {
                if (args.length == 1 && args[0] instanceof String) {
                    // Find the method from the signature of the method of the proxy instance
                    Iterator<Method> signature = SignatureLookup.methods((String) args[0], proxy.getClass()).find().iterator();
                    if (signature.hasNext()) {
                        this.cache.invalidate(signature.next());
                    }
                }
                return null;
            })
            .put("$invalidateAll", (proxy, method, args) -> {
                this.cache.invalidateAll();
                return null;
            })
            .put("$cacheSize", ((proxy, method, args) -> this.cache.size()))
            .build();
    }

    /** Handle the actual invocation of the method proxy system */
    private Object invokable(Object proxy, Method method, Object[] args) throws Throwable {
        // Caching
        MethodHandler handler = this.cache.getIfPresent(method);
        if (handler != null) {
            return handler.handle(args);
        }

        // Cache does not exist create one
        if (method.isAnnotationPresent(Invoke.class)) {
            Invoke invoke = method.getAnnotation(Invoke.class);
            MethodHandler handle = this.useMethodHandle || invoke.methodHandle()
                ? methodHandle$invokeHandle(invoke, method, this.instance)
                : reflection$invokeHandle(invoke, method, this.instance);
            this.cache.put(method, handle);
            return handle.handle(args);
        } else if (method.isAnnotationPresent(Setter.class)) {
            Conditions.inRange(args.length, 1, 1); // make sure there is only one argument
            Setter setter = method.getAnnotation(Setter.class);
            MethodHandler handle = this.useMethodHandle || setter.methodHandle()
                ? methodHandle$setterHandle(setter, method, this.instance)
                : reflection$setterHandle(setter, method, this.instance);
            this.cache.put(method, handle);
            return handle.handle(args);
        } else if (method.isAnnotationPresent(Getter.class)) {
            Getter getter = method.getAnnotation(Getter.class);
            MethodHandler handle = this.useMethodHandle || getter.methodHandle()
                ? methodHandle$getterHandle(getter, method, this.instance)
                : reflection$getterHandle(getter, method, this.instance);
            this.cache.put(method, handle);
            return handle.handle(args);
        } else if (method.isDefault()) {
            MethodHandler handle = defaultHandle(method, proxy);
            this.cache.put(method, handle);
            return handle.handle(args);
        }

        // Handle internal method that exist on the proxy
        if (this.internalMethods.containsKey(method.getName())) {
            MethodHandler handle = arg -> this.internalMethods.get(method.getName()).invoke(proxy, method, args);
            this.cache.put(method, handle);
            return handle.handle(this, args);
        }

        // Last if no others are found use the declaring classes method
        // Such as hashCode, toString, ect
        return MethodHandleHandlers.lookup.unreflect(method).bindTo(this.instance).invokeWithArguments(args);
    }

    /** Wraps the error from invokable and print out details */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws RuntimeException {
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
                .add("Failed at: ", method.getDeclaringClass() != null ? method.getDeclaringClass().getName() : "null")
                .add("Message: ", exception.getMessage())
                .add("Annotation(s): ", annotations)
                .add("Method: ", method.getName())
                .add("Return Type: ", method.getReturnType())
                .add("Arg(s): ", args)
                .buildAndReport(System.err);
        } catch (Throwable throwable) { // General errors
            throw ErrorReporter.builder(throwable)
                .add("Failed at: ", method.getDeclaringClass() != null ? method.getDeclaringClass().getName() : "null")
                .add("Method: ", method.getName())
                .add("Return Type: ", method.getReturnType())
                .add("Arg(s): ", args)
                .buildAndReport(System.err);
        }
    }
}
