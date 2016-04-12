package net.year4000.utilities.reflection;

import com.google.common.reflect.Reflection;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.Reflections;
import net.year4000.utilities.utils.UtilityConstructError;
import net.year4000.utilities.value.Value;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

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
        if (instance == null) {
            return null;
        }
        return Reflection.newProxy(proxy, new Tunnel(proxy, instance));
    }

    /** Create a tunnel of the object and the under lying code */
    private static class Tunnel implements InvocationHandler {
        private final Class<?> proxyClass;
        private final Class<?> classInstance;
        private final Object instance;

        Tunnel(Class<?> clazz, Object instance) {
            this.instance = Conditions.nonNull(instance, "instance");
            this.classInstance = reflectiveClass(clazz);
            this.proxyClass = clazz;
        }

        /** Grab the class instance of the reflective class */
        private Class<?> reflectiveClass(Class<?> proxy) {
            Proxied proxied = Conditions.nonNull(proxy.getAnnotation(Proxied.class), "@Proxied");
            Value<Class<?>> value = Reflections.clazz(proxied.value(), proxied.init(), proxy.getClassLoader());
            return Conditions.nonNull(value.get(), "value");
        }

        /** Handle the bridge between objects and its proxy */
        private Object handleBridge(Bridge bridge, Object instance) {
            if (bridge == null || instance == null) {
                return instance;
            }
            return Gateways.proxy(bridge.value(), instance);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.isAnnotationPresent(Invoke.class)) {
                String name = Value.of(method.getAnnotation(Invoke.class).value()).getOrElse(method.getName());
                Object object = Reflections.invoke(classInstance, instance, name, args).getOrThrow();
                return handleBridge(method.getAnnotation(Bridge.class), object);
            } else if (method.isAnnotationPresent(Setter.class)) {
                Conditions.inRange(args.length, 1, 1); // make sure there is only one argument
                String name = Value.of(method.getAnnotation(Setter.class).value()).getOrElse(method.getName());
                return Reflections.field(classInstance, instance, name, args[0]);
            } else if (method.isAnnotationPresent(Getter.class)) {
                String name = Value.of(method.getAnnotation(Getter.class).value()).getOrElse(method.getName());
                Object object = Reflections.field(classInstance, instance, name).getOrThrow();
                return handleBridge(method.getAnnotation(Bridge.class), object);
            } else if (method.isDefault()) {
                Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
                constructor.setAccessible(true);
                return constructor.newInstance(proxyClass, MethodHandles.Lookup.PRIVATE)
                    .unreflectSpecial(method, proxyClass)
                    .bindTo(proxy)
                    .invokeWithArguments(args);
            }

            // Special cases
            return Reflections.invoke(Object.class, instance, method.getName()).getOrThrow();
        }
    }
}
