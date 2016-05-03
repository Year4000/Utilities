package net.year4000.utilities.reflection;


import static net.year4000.utilities.reflection.Handlers.defaultHandle;
import static net.year4000.utilities.reflection.Handlers.getterHandle;
import static net.year4000.utilities.reflection.Handlers.invokeHandle;
import static net.year4000.utilities.reflection.Handlers.setterHandle;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.reflection.annotations.Getter;
import net.year4000.utilities.reflection.annotations.Invoke;
import net.year4000.utilities.reflection.annotations.Setter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

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

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
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

        // Special cases
        return Reflections.invoke(Object.class, instance, method.getName()).get();
    }
}
