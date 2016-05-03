package net.year4000.utilities.reflection;

import static net.year4000.utilities.reflection.Gateways.reflectiveClass;

import net.year4000.utilities.reflection.annotations.Bridge;
import net.year4000.utilities.reflection.annotations.Getter;
import net.year4000.utilities.reflection.annotations.Invoke;
import net.year4000.utilities.reflection.annotations.Setter;
import net.year4000.utilities.utils.UtilityConstructError;
import net.year4000.utilities.value.Value;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/** Creates instances of the MethodHandler for the proxy tunnel */
public final class Handlers {
    private Handlers() {
        UtilityConstructError.raise();
    }

    /** Handle the bridge between objects and its proxy */
    private static MethodHandler handleBridge(Method method, MethodHandler handler) {
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
    static MethodHandler invokeHandle(Method method) {
        final String name = Value.of(method.getAnnotation(Invoke.class).value()).getOrElse(method.getName());
        Class<?> classInstance = reflectiveClass(method.getDeclaringClass());
        return handleBridge(method, (instance, args) -> Reflections.invoke(classInstance, instance, name, args).get());
    }

    /** Create the getter handler */
    static MethodHandler getterHandle(Method method) {
        final String name = Value.of(method.getAnnotation(Getter.class).value()).getOrElse(method.getName());
        Class<?> classInstance = reflectiveClass(method.getDeclaringClass());
        return handleBridge(method, (instance, args) -> Reflections.getter(classInstance, instance, name).get());
    }

    /** Create the setter handler */
    static MethodHandler setterHandle(Method method) {
        final String name = Value.of(method.getAnnotation(Setter.class).value()).getOrElse(method.getName());
        Class<?> classInstance = reflectiveClass(method.getDeclaringClass());
        return (instance, args) -> Reflections.setter(classInstance, instance, name, args[0]);
    }

    /** Create the default keyword method handle */
    static MethodHandler defaultHandle(Method method, Object proxy) throws Throwable {
        Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
        constructor.setAccessible(true);
        Class<?> proxyClass = method.getDeclaringClass();
        MethodHandle handle = constructor.newInstance(proxyClass, MethodHandles.Lookup.PRIVATE)
                .unreflectSpecial(method, proxyClass)
                .bindTo(proxy);
        return (instance, args) -> handle.invokeWithArguments(args);
    }
}
