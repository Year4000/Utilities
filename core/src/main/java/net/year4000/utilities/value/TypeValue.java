/*
 * Copyright 2015 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.value;

/** This is a utility class that will try to cast to the required type */
public class TypeValue extends ImmutableValue<Object> {

    /** Init this Value var with the specific value */
    public TypeValue(Object value) {
        super(value);
    }

    /** Try to cast to the specific type */
    public <T> T castTo(Class<T> clazz) {
        try {
            return clazz.cast(value);
        } catch (ClassCastException error) {
            throw new RuntimeException(error);
        }
    }

    /** Is this TypeValue a Number */
    public boolean isNumber() {
        return value instanceof Number;
    }

    /** Is this TypeValue a String */
    public boolean isString() {
        return value instanceof String;
    }

    /** Is this TypeValue a Boolean */
    public boolean isBoolean() {
        return value instanceof Boolean;
    }

    /** Try to parse the object to an int */
    public boolean toBoolean() {
        return isBoolean() ? castTo(Boolean.class) : Boolean.valueOf(toString());
    }

    /** Try to parse the object to an int */
    public int toInt() {
        return isNumber() ? castTo(Number.class).intValue() : Integer.valueOf(toString());
    }

    /** Try to parse the object to a double */
    public double toDouble() {
        return isNumber() ? castTo(Number.class).doubleValue() : Double.valueOf(toString());
    }

    /** Try to parse the object to a long */
    public long toLong() {
        return isNumber() ? castTo(Number.class).longValue() : Long.valueOf(toString());
    }

    /** Try to parse the object to a float */
    public float toFloat() {
        return isNumber() ? castTo(Number.class).floatValue() : Float.valueOf(toString());
    }

    /** Try to parse the object to a short */
    public short toShort() {
        return isNumber() ? castTo(Number.class).shortValue() : Short.valueOf(toString());
    }

    /** Try to parse the object to a byte */
    public byte toByte() {
        return isNumber() ? castTo(Number.class).byteValue() : Byte.valueOf(toString());
    }

    /** Try to parse the object to a String */
    @Override
    public String toString() {
        return isString() ? castTo(String.class) : String.valueOf(value);
    }
}
