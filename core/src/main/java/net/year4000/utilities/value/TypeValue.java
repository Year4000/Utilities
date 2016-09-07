/*
 * Copyright 2015 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.value;

import java.util.Optional;

/** This is a utility class that will try to cast to the required type */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class TypeValue extends ImmutableValue<Object> {

    /** Init this Value var with the specific value */
    public TypeValue(Optional<?> value) {
        super(value.orElse(null));
    }

    /** Init this Value var with the specific value */
    public TypeValue(Value<?> value) {
        super(value.get());
    }

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
    public boolean toBoolean(boolean defaultValue) {
        try {
            return isBoolean() ? castTo(Boolean.class) : Boolean.valueOf(toString());
        } catch (ClassCastException error) {
            return defaultValue;
        }
    }

    /** Try to parse the object to an int */
    public int toInt() {
        return isNumber() ? castTo(Number.class).intValue() : Integer.valueOf(toString());
    }

    /** Try to parse the object to an int */
    public int toInt(int defaultValue) {
        try {
            return isNumber() ? castTo(Number.class).intValue() : Integer.valueOf(toString());
        } catch (NumberFormatException | ClassCastException error) {
            return defaultValue;
        }
    }

    /** Try to parse the object to a double */
    public double toDouble() {
        return isNumber() ? castTo(Number.class).doubleValue() : Double.valueOf(toString());
    }

    /** Try to parse the object to a double */
    public double toDouble(double defaultValue) {
        try {
            return isNumber() ? castTo(Number.class).doubleValue() : Double.valueOf(toString());
        } catch (NumberFormatException | ClassCastException error) {
            return defaultValue;
        }
    }

    /** Try to parse the object to a long */
    public long toLong() {
        return isNumber() ? castTo(Number.class).longValue() : Long.valueOf(toString());
    }

    /** Try to parse the object to a long */
    public long toLong(long defaultValue) {
        try {
            return isNumber() ? castTo(Number.class).longValue() : Long.valueOf(toString());
        } catch (ClassCastException | NumberFormatException error) {
            return defaultValue;
        }
    }

    /** Try to parse the object to a float */
    public float toFloat() {
        return isNumber() ? castTo(Number.class).floatValue() : Float.valueOf(toString());
    }

    /** Try to parse the object to a float */
    public float toFloat(float defaultValue) {
        try {
            return isNumber() ? castTo(Number.class).floatValue() : Float.valueOf(toString());
        } catch (NumberFormatException | ClassCastException error) {
            return defaultValue;
        }
    }

    /** Try to parse the object to a short */
    public short toShort() {
        return isNumber() ? castTo(Number.class).shortValue() : Short.valueOf(toString());
    }

    /** Try to parse the object to a short */
    public short toShort(short defaultValue) {
        try {
            return isNumber() ? castTo(Number.class).shortValue() : Short.valueOf(toString());
        } catch (NumberFormatException | ClassCastException error) {
            return defaultValue;
        }
    }

    /** Try to parse the object to a byte */
    public byte toByte() {
        return isNumber() ? castTo(Number.class).byteValue() : Byte.valueOf(toString());
    }

    /** Try to parse the object to a byte */
    public byte toByte(byte defaultValue) {
        try {
            return isNumber() ? castTo(Number.class).byteValue() : Byte.valueOf(toString());
        } catch (NumberFormatException | ClassCastException error) {
            return defaultValue;
        }
    }

    /** Try to parse the object to a String */
    @Override
    public String toString() {
        return isString() ? castTo(String.class) : String.valueOf(value);
    }
}
