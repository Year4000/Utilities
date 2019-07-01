/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.reflection.lookups;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import net.year4000.utilities.Conditions;

import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;

/** Look for the fields of the class */
class MethodSignatureLookup implements SignatureLookup<Method> {
    private final Class<?> from;
    private final Class<?> returnType;
    private final Class<?>[] parameterTypes;

    MethodSignatureLookup(String signature, Class<?> clazz) {
        this.from = Conditions.nonNull(clazz, "clazz");
        MethodType methodType = MethodType.fromMethodDescriptorString(Conditions.nonNullOrEmpty(signature, "signature"), clazz.getClassLoader());
        this.returnType = methodType.returnType();
        this.parameterTypes = methodType.parameterArray();
    }

    /** Get the methods for the class */
    private Method[] methods() {
        try {
            return this.from.getDeclaredMethods();
        } catch (SecurityException exception) {
            return new Method[0];
        }
    }

    /** Find all methods, there is no order to how they appear */
    @Override
    public ImmutableSet<Method> find() {
        ImmutableSet.Builder<Method> possibles = new ImmutableSet.Builder<>();
        for (Method method : methods()) {
            if (method.getReturnType() == this.returnType && Arrays.equals(method.getParameterTypes(), this.parameterTypes)) {
                possibles.add(method);
            }
        }
        return possibles.build();
    }

    /** Sort by field name as that is the only valid choice */
    @Override
    public ImmutableSortedSet<Method> findSorted() {
        return ImmutableSortedSet.copyOf(Comparator.comparing(Method::getName), find());
    }
}
