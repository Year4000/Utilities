package net.year4000.utilities.reflection;

import com.google.common.collect.Maps;
import com.google.common.reflect.Reflection;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.Reflections;
import net.year4000.utilities.utils.UtilityConstructError;
import net.year4000.utilities.value.Value;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * The gateway between Proxied instances and the real instances,
 * this allows for proxied instances access fields and method of
 * the bridged that we should not have access to.
 */
public final class Gateways {
    private Gateways() {
        UtilityConstructError.raise();
    }

    /** Creates a proxy instance between the interface class {@code proxy} with bridged class {@code instance} */
    public static <T> T proxy(Class<T> proxy, Object instance) {
        Conditions.nonNull(proxy, "proxy");
        if (instance == null) { // static proxy
            return Reflection.newProxy(proxy, new Tunnel());
        }
        return Reflection.newProxy(proxy, new Tunnel(instance));
    }

    /** Creates a static proxy instance between the interface class {@code proxy} */
    public static <T> T proxy(Class<T> proxy) {
        return proxy(proxy, null);
    }

    /** Create a tunnel of the object and the under lying code */
    private static class Tunnel implements InvocationHandler {
        private final static Map<Method, MethodHandler> cache = Maps.newConcurrentMap();
        private final Object instance;

        // Normal proxy with an instance
        Tunnel(Object instance) {
            this.instance = Conditions.nonNull(instance, "instance");
        }

        // Static proxy that access statics only
        Tunnel() {
            this.instance = null; // static proxy
        }

        /** Grab the class instance of the reflective class */
        private Class<?> reflectiveClass(Class<?> proxy) {
            Proxied proxied = Conditions.nonNull(proxy.getAnnotation(Proxied.class), "@Proxied");
            return Reflections.clazz(proxied.value(), proxied.init(), proxy.getClassLoader()).getOrThrow("value");
        }

        /** Handle the bridge between objects and its proxy */
        private MethodHandler handleBridge(Method method, MethodHandler handler) {
            if (method.isAnnotationPresent(Bridge.class)) {
                final Class<?> bridge = method.getAnnotation(Bridge.class).value();
                return (instance, args) -> {
                    // null return types should just return null
                    Object returnInst = handler.handle(instance, args);
                    return (returnInst == null) ? null : Gateways.proxy(bridge, returnInst);
                };
            }
            return handler;
        }

        /** Create the invoke handler */
        private MethodHandler invokeHandle(Method method) {
            final String name = Value.of(method.getAnnotation(Invoke.class).value()).getOrElse(method.getName());
            Class<?> classInstance = reflectiveClass(method.getDeclaringClass());
            return handleBridge(method, (instance, args) -> Reflections.invoke(classInstance, instance, name, args).get());
        }

        /** Create the getter handler */
        private MethodHandler getterHandle(Method method) {
            final String name = Value.of(method.getAnnotation(Getter.class).value()).getOrElse(method.getName());
            Class<?> classInstance = reflectiveClass(method.getDeclaringClass());
            return handleBridge(method, (instance, args) -> Reflections.getter(classInstance, instance, name).get());
        }

        /** Create the setter handler */
        private MethodHandler setterHandle(Method method) {
            final String name = Value.of(method.getAnnotation(Setter.class).value()).getOrElse(method.getName());
            Class<?> classInstance = reflectiveClass(method.getDeclaringClass());
            return (instance, args) -> Reflections.setter(classInstance, instance, name, args[0]);
        }

        /** Create the default keyword method handle */
        private MethodHandler defaultHandle(Method method, Object proxy) throws Throwable {
            Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
            constructor.setAccessible(true);
            Class<?> proxyClass = method.getDeclaringClass();
            MethodHandle handle = constructor.newInstance(proxyClass, MethodHandles.Lookup.PRIVATE)
                .unreflectSpecial(method, proxyClass)
                .bindTo(proxy);
            return (instance, args) -> handle.invokeWithArguments(args);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            // Caching
            MethodHandler handler = cache.get(method);
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

            // Special cases
            return Reflections.invoke(Object.class, instance, method.getName()).get();
        }

        /** A interface to cache the method and avoid reflection look up calls */
        @FunctionalInterface
        private interface MethodHandler {
            /** Handle the method args */
            Object handle(Object instance, Object[] args) throws Throwable;
        }
    }
}
