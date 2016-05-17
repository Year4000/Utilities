/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.reflection;

import net.year4000.utilities.Conditions;
import net.year4000.utilities.reflection.annotations.Implements;
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
            return Reflections.proxy(proxy, new Tunnel(), reflectiveImplements(proxy));
        }
        return Reflections.proxy(proxy, new Tunnel(instance), reflectiveImplements(proxy));
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

    /** Get the classes that the proxy should implement, must match the actual class implemented interfaces */
    public static Class<?>[] reflectiveImplements(Class<?> proxy) {
        Implements implementing = proxy.getAnnotation(Implements.class);
        if (implementing != null) {
            int size = implementing.value().length;
            Class<?>[] classes = new Class<?>[size];
            for (int i = 0 ; i < size ; i++) {
                Proxied proxied = implementing.value()[i];
                classes[i] = Reflections.clazz(proxied.value(), proxied.init(), proxy.getClassLoader()).getOrThrow();
            }
            return classes;
        }
        return new Class<?>[0];
    }
}
