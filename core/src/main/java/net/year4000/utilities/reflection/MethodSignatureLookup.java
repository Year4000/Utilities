package net.year4000.utilities.reflection;

import com.google.common.collect.ImmutableSet;

import java.lang.reflect.Method;
import java.util.Arrays;

/** Look for the fields of the class */
class MethodSignatureLookup extends AbstractSignatureLookup<Method> {
    MethodSignatureLookup(String signature, Class<?> clazz) {
        super(signature, clazz, For.METHOD);
    }

    /** Get the methods for the class */
    private Method[] methods() {
        try {
            return from.getDeclaredMethods();
        } catch (SecurityException exception) {
            return new Method[0];
        }
    }

    /** Find all methods, there is no order to how they appear */
    @Override
    public ImmutableSet<Method> find() {
        ImmutableSet.Builder<Method> possibles = new ImmutableSet.Builder<>();
        for (Method method : methods()) {
            if (method.getReturnType() == returnType && Arrays.equals(method.getParameterTypes(), argsType)) {
                possibles.add(method);
            }
        }
        return possibles.build();
    }
}
