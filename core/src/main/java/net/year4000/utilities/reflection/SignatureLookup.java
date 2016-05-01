package net.year4000.utilities.reflection;

import net.year4000.utilities.Conditions;
import net.year4000.utilities.reflection.annotations.Signature;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/** Tries and finds the field for method based on the signatures value */
public class SignatureLookup {
    /** Should it look up fields for methods */
    public enum For {FIELD, METHOD}

    // Constructor fields
    private final For type;
    private final Class<?> from;
    private final String signature;
    private final int index;

    // Caching

    public SignatureLookup(Signature signature, Class<?> from, For type) {
        Conditions.nonNull(signature, "signature");
        this.from = Conditions.nonNull(from, "from");
        this.type = Conditions.nonNull(type, "type");
        this.signature = Conditions.nonNull(signature.value(), "signature.value()");
        this.index = signature.index();
    }

    /** Are we looking for fields */
    public boolean isForField() {
        return type == For.FIELD;
    }

    /** Are we looing for methods */
    public boolean isForMethod() {
        return type == For.METHOD;
    }

    /** Get the fields for the class */
    public Field[] fields() {
        try {
            return from.getDeclaredFields();
        } catch (SecurityException exception) {
            return new Field[0];
        }
    }

    /** Get the methods for the class */
    public Method[] methods() {
        try {
            return from.getDeclaredMethods();
        } catch (SecurityException exception) {
            return new Method[0];
        }
    }

    public Class<?> classType() {
        Conditions.condition(isForField(), "Only for fields");
        String type = signature;
        if (type.contains(")")) { // Trim off args
            type = type.substring(type.indexOf(")"));
        }

        return void.class;
    }
}
