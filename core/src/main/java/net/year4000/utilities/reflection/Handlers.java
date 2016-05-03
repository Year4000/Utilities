package net.year4000.utilities.reflection;

import static net.year4000.utilities.reflection.Gateways.reflectiveClass;

import com.google.common.collect.ImmutableSet;
import net.year4000.utilities.reflection.annotations.Bridge;
import net.year4000.utilities.reflection.annotations.Getter;
import net.year4000.utilities.reflection.annotations.Invoke;
import net.year4000.utilities.reflection.annotations.Setter;
import net.year4000.utilities.utils.UtilityConstructError;
import net.year4000.utilities.value.Value;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/** Creates instances of the MethodHandler for the proxy tunnel */
final class Handlers {
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

    /** Find the method */
    private static Method findMethod(String signature, Class<?> clazz, String method) {
        SignatureLookup<Method> lookup = SignatureLookup.methods(signature, clazz);
        ImmutableSet<Method> methods = lookup.find();
        // Only one just return it
        if (methods.size() == 1) {
            return methods.iterator().next();
        }
        // Filter by name
        Method result = methods.stream().filter(name -> name.getName().contains(method)).findFirst().orElse(null);
        if (result != null) {
            return result;
        }
        // Non found throw error
        throw new RuntimeException("No method by this signature: " + signature);
    }

    /** Find the method */
    private static Field findField(String signature, Class<?> clazz, String method) {
        SignatureLookup<Field> lookup = SignatureLookup.fields(signature, clazz);
        ImmutableSet<Field> methods = lookup.find();
        // Only one just return it
        if (methods.size() == 1) {
            return methods.iterator().next();
        }
        // Filter by name
        Field result = methods.stream().filter(name -> name.getName().contains(method)).findFirst().orElse(null);
        if (result != null) {
            return result;
        }
        // Non found throw error
        throw new RuntimeException("No field by this signature: " + signature);
    }

    /** Create the invoke handler */
    static MethodHandler invokeHandle(Method method) {
        Invoke invoke = method.getAnnotation(Invoke.class);
        Class<?> classInstance = reflectiveClass(method.getDeclaringClass());
        if (!invoke.signature().isEmpty()) { // Search by signature
            Method signature = findMethod(invoke.signature(), classInstance, invoke.value());
            return handleBridge(method, (instance, args) -> Reflections.invoke(instance, signature, args).get());
        }
        // All else
        String name = Value.of(invoke.value()).getOrElse(method.getName());
        return handleBridge(method, (instance, args) -> Reflections.invoke(classInstance, instance, name, args).get());
    }

    /** Create the getter handler */
    static MethodHandler getterHandle(Method method) {
        Getter getter = method.getAnnotation(Getter.class);
        Class<?> classInstance = reflectiveClass(method.getDeclaringClass());
        if (!getter.signature().isEmpty()) {
            Field signature = findField(getter.signature(), classInstance, getter.value());
            return handleBridge(method, ((instance, args) -> Reflections.getter(instance, signature).get()));
        }
        String name = Value.of(getter.value()).getOrElse(method.getName());
        return handleBridge(method, (instance, args) -> Reflections.getter(classInstance, instance, name).get());
    }

    /** Create the setter handler */
    static MethodHandler setterHandle(Method method) {
        Setter setter = method.getAnnotation(Setter.class);
        Class<?> classInstance = reflectiveClass(method.getDeclaringClass());
        if (!setter.signature().isEmpty()) {
            Field signature = findField(setter.signature(), classInstance, setter.value());
            return (instance, args) -> Reflections.setter(instance, signature, args[0]);
        }
        String name = Value.of(method.getAnnotation(Setter.class).value()).getOrElse(method.getName());
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
