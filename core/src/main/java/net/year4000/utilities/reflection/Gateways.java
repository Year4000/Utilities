package net.year4000.utilities.reflection;

import com.google.common.reflect.Reflection;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.reflection.annotations.Proxied;
import net.year4000.utilities.utils.UtilityConstructError;

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

    /** Grab the class instance of the reflective class */
    public static Class<?> reflectiveClass(Class<?> proxy) {
        Proxied proxied = Conditions.nonNull(proxy.getAnnotation(Proxied.class), "@Proxied");
        return Reflections.clazz(proxied.value(), proxied.init(), proxy.getClassLoader()).getOrThrow("value");
    }
}
