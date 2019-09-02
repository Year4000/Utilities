/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.reflection;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import net.year4000.utilities.reflection.annotations.Bridge;
import net.year4000.utilities.reflection.lookups.SignatureLookup;
import net.year4000.utilities.utils.UtilityConstructError;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

final class HandlerUtils {
    private HandlerUtils() {
        UtilityConstructError.raise();
    }

    /** Handle the bridge between objects and its proxy */
    static MethodHandler handleBridge(Method method, MethodHandler handler) {
        if (method.isAnnotationPresent(Bridge.class)) {
            final Class<?> bridge = method.getAnnotation(Bridge.class).value();
            return args -> {
                // null return types should just return null
                Object returnInst = handler.handle(args);
                return (returnInst == null) ? null : Gateways.proxy(bridge, returnInst);
            };
        }
        return handler;
    }

    /** Wrap index values, -1 gets the last value, -2 gets the second to last value and so on */
    private static int index(int index, int length) {
        return (index < 0) ? length + index : index;
    }

    /** Find the method */
    static Method findMethod(String signature, Class<?> clazz, String method, int index) throws NoSuchMethodException {
        ImmutableSortedSet<Method> methods = SignatureLookup.methods(signature, clazz).findSorted();
        // Only one just return it
        if (methods.size() == 1) {
            return methods.first();
        }
        // Filter by name
        Method[] result = methods.stream().filter(name -> name.getName().contains(method)).toArray(Method[]::new);
        if (result.length > 0) {
            return result[index(index, result.length)];
        }
        // Non found throw error
        throw new NoSuchMethodException("No method by this signature: " + signature);
    }

    /** Find the method */
    static Field findField(String signature, Class<?> clazz, String field, int index) throws NoSuchFieldException {
        ImmutableSet<Field> fields = SignatureLookup.fields(signature, clazz).find();
        // Only one just return it
        if (fields.size() == 1) {
            return fields.iterator().next();
        }
        // Filter by name
        Field[] result = fields.stream().filter(name -> name.getName().contains(field)).toArray(Field[]::new);
        if (result.length > 0) {
            return result[index(index, result.length)];
        }
        // Non found throw error
        throw new NoSuchFieldException("No field by this signature: " + signature);
    }
}
