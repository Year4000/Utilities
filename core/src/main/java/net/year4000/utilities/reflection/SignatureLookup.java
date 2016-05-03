package net.year4000.utilities.reflection;

import com.google.common.collect.ImmutableSet;

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

    /** Return all the possible matches */
    ImmutableSet<T> find();
}
