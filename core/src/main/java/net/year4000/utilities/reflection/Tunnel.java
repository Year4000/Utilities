/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.reflection;

import static net.year4000.utilities.reflection.Handlers.defaultHandle;
import static net.year4000.utilities.reflection.Handlers.getterHandle;
import static net.year4000.utilities.reflection.Handlers.invokeHandle;
import static net.year4000.utilities.reflection.Handlers.setterHandle;

import com.google.common.annotations.Beta;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableMap;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.ErrorReporter;
import net.year4000.utilities.reflection.annotations.Decorator;
import net.year4000.utilities.reflection.annotations.Getter;
import net.year4000.utilities.reflection.annotations.Invoke;
import net.year4000.utilities.reflection.annotations.Setter;
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
    private final static Cache<Method, MethodHandler> cache = CacheBuilder.newBuilder().softValues().build();
    private final Map<String, InvocationHandler> internalMethods;
    private final Object instance;
    private final Class<T> clazz;

    // Normal proxy with an instance
    Tunnel(Class<T> clazz, Object instance) {
        this.clazz = clazz;
        this.instance = Conditions.nonNull(instance, "instance");
        this.internalMethods = populateInternalMethods(instance);
    }

    // Static proxy that access statics only
    Tunnel(Class<T> clazz) {
        this.clazz = clazz;
        this.instance = null; // static proxy
        this.internalMethods = populateInternalMethods(null);
    }

    /** This is a prototype to have a decorated method of DuckType classes */
    @Beta
    private Map<String, InvocationHandler> createDecoratedMethods() {
        ImmutableMap.Builder<String, InvocationHandler> decorates = ImmutableMap.builder();

        if (clazz != null) {
            for (Method methods : clazz.getMethods()) {
                if (methods.isDefault() && methods.isAnnotationPresent(Decorator.class)) {
                    Decorator decorator = methods.getAnnotation(Decorator.class);
                    // todo place a different decorate depending on the signature type of the method
                    decorates.put(decorator.value(), ((proxy, method, args) -> {
                        // replace the reference of method with the decorated version
                        Method decoratedMethod = (Method) defaultHandle(methods, proxy).handle(instance, new Object[]{ method });
                        return Reflections.invoke(decoratedMethod.getDeclaringClass(), instance, decoratedMethod.getName()).get();
                    }));

                }
            }
        }

        return decorates.build();
    }

    /** Create a set of internal methods that will run instead of the default behavior */
    private Map<String, InvocationHandler> populateInternalMethods(Object instance) {
        return ImmutableMap.<String, InvocationHandler>builder()
            .putAll(createDecoratedMethods())
            // Allow getting the actual instance, bypassing proxy handles
            .put("$this", (proxy, method, args) -> instance)
            // Invalidate the method cache of the proxy
            .put("$invalidate", (proxy, method, args) -> {
                if (args.length == 1 && args[0] instanceof String) {
                    // Find the method from the signature of the method of the proxy instance
                    Iterator<Method> signature = SignatureLookup.methods((String) args[0], proxy.getClass()).find().iterator();
                    if (signature.hasNext()) {
                        cache.invalidate(signature.next());
                    }
                }
                return null;
            })
            .put("$invalidateAll", (proxy, method, args) -> {
                cache.invalidateAll();
                return null;
            })
            .put("$cacheSize", ((proxy, method, args) -> cache.size()))
            .build();
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
            MethodHandler handle = invokeHandle(method);
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
        } else if (method.isDefault()) {
            MethodHandler handle = defaultHandle(method, proxy);
            cache.put(method, handle);
            return handle.handle(instance, args);
        }

        // Handle internal method that exist on the proxy
        if (internalMethods.containsKey(method.getName())) {
            MethodHandler handle = (Object instance, Object[] arg) -> internalMethods.get(method.getName()).invoke(proxy, method, args);
            cache.put(method, handle);
            return handle.handle(this, args);
        }

        // Last if no others are found use the declaring classes method
        // Such as hashCode, toString, ect
        return Reflections.invoke(method.getDeclaringClass(), instance, method.getName()).get();
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
