/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.reflection;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.primitives.Ints;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/** Find the constructor by the signature */
class ConstructorSignatureLookup<C> extends AbstractSignatureLookup<Constructor<C>> {
    ConstructorSignatureLookup(String signature, Class<?> from) {
        super(signature, from, For.CONSTRUCTOR);
    }

    /** Get all the constructors for the class */
    @SuppressWarnings("unchecked")
    private Constructor<C>[] constructors() {
        try {
            return (Constructor<C>[]) from.getDeclaredConstructors();
        } catch (SecurityException exception) {
            return (Constructor<C>[]) new Constructor<?>[0];
        }
    }

    /** Find the constructors by the args, should ony return 1 or 0 as constructors are born this way */
    @Override
    public ImmutableSet<Constructor<C>> find() {
        ImmutableSet.Builder<Constructor<C>> possibles = new ImmutableSet.Builder<>();
        for (Constructor<C> constructor : constructors()) {
            if (Modifier.isStatic(from.getModifiers())) {
                if (Arrays.equals(constructor.getParameterTypes(), argsType)) {
                    possibles.add(constructor);
                }
            } else { // Non Static Classes Constructors first arg is the parent class, so strip that off
                Class<?>[] paramTypes = Arrays.copyOfRange(constructor.getParameterTypes(), 1, constructor.getParameterCount());
                if (Arrays.equals(paramTypes, argsType)) {
                    possibles.add(constructor);
                }
            }
        }
        return possibles.build();
    }

    /** Sort by the parameter count, should should only return 1 or 0 as constructors are born this way */
    @Override
    public ImmutableSortedSet<Constructor<C>> findSorted() {
        return ImmutableSortedSet.copyOf((left, right) -> Ints.compare(left.getParameterCount(), right.getParameterCount()), find());
    }
}
