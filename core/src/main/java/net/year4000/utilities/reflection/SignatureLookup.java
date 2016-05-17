/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.reflection;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/** Find internal things by their signature */
public interface SignatureLookup<T> {
    /** Get a field signature lookup */
    static SignatureLookup<Field> fields(String signature, Class<?> clazz) {
        return new FieldSignatureLookup(signature, clazz);
    }

    /** Get a method signature lookup */
    static SignatureLookup<Method> methods(String signature, Class<?> clazz) {
        return new MethodSignatureLookup(signature, clazz);
    }

    /** Get a method signature lookup */
    static <C> SignatureLookup<Constructor<C>> constructors(String signature, Class<C> clazz) {
        return new ConstructorSignatureLookup<>(signature, clazz);
    }

    /** Return all the possible matches */
    ImmutableSet<T> find();

    /** Return all the possible matches and then sort it */
    ImmutableSortedSet<T> findSorted();
}
