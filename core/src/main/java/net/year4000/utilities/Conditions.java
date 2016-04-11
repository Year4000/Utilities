package net.year4000.utilities;

import com.google.common.base.Joiner;
import net.year4000.utilities.utils.UtilityConstructError;
import net.year4000.utilities.value.Value;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** Help in the creation of object with helper util methods */
public final class Conditions {
    private Conditions() {
        UtilityConstructError.raise();
    }

    /** Check that the value is in the states */
    @SafeVarargs
    public static <T extends Enum<?>> T checkState(T value, T... states) {
        isLarger(states.length, 1);
        for (T state : states) {
            if (state == value) {
                return value;
            }
        }
        throw new IllegalStateException(String.format("Value %s is not in %s", value, Joiner.on(", ").join(states)));
    }

    private enum Ranges {RANGE, LARGER, SMALLER}

    /** Spits out the right message for range checks */
    private static String rangeMessage(Ranges range, Number value, Number state) {
        nonNull(range, "range");
        String first = String.valueOf(nonNull(value, "value"));
        String second = String.valueOf(nonNull(state, "state"));
        switch (range) {
            case RANGE:
                return String.format("Min %s must be smaller than Max %s", first, second);
            case LARGER:
                return String.format("Value %s must be larger than Min %s", first, second);
            case SMALLER:
                return String.format("Value %s must be smaller than Max %s", first, second);
        }
        return "null";
    }

    /** Checks if the value is smaller than the max */
    public static byte isSmaller(byte value, byte max) {
        return inRange(value, Byte.MIN_VALUE, max);
    }

    /** Checks if the value is larger than the min */
    public static byte isLarger(byte value, byte min) {
        return inRange(value, min, Byte.MAX_VALUE);
    }

    /** Checks if the value is in the range */
    public static byte inRange(byte value, byte min, byte max) {
        if (min > max) {
            throw new IllegalArgumentException(rangeMessage(Ranges.RANGE, min, max));
        }
        if (value < min) {
            throw new IllegalArgumentException(rangeMessage(Ranges.LARGER, value, min));
        }
        if (value > max) {
            throw new IllegalArgumentException(rangeMessage(Ranges.SMALLER, value, max));
        }
        return value;
    }

    /** Checks if the value is smaller than the max */
    public static short isSmaller(short value, short max) {
        return inRange(value, Short.MIN_VALUE, max);
    }

    /** Checks if the value is larger than the min */
    public static short isLarger(short value, short min) {
        return inRange(value, min, Short.MAX_VALUE);
    }

    /** Checks if the value is in the range */
    public static short inRange(short value, short min, short max) {
        if (min > max) {
            throw new IllegalArgumentException(rangeMessage(Ranges.RANGE, min, max));
        }
        if (value < min) {
            throw new IllegalArgumentException(rangeMessage(Ranges.LARGER, value, min));
        }
        if (value > max) {
            throw new IllegalArgumentException(rangeMessage(Ranges.SMALLER, value, max));
        }
        return value;
    }

    /** Checks if the value is smaller than the max */
    public static float isSmaller(float value, float max) {
        return inRange(value, Float.NEGATIVE_INFINITY, max);
    }

    /** Checks if the value is larger than the min */
    public static float isLarger(float value, float min) {
        return inRange(value, min, Float.POSITIVE_INFINITY);
    }

    /** Checks if the value is in the range */
    public static float inRange(float value, float min, float max) {
        if (min > max) {
            throw new IllegalArgumentException(rangeMessage(Ranges.RANGE, min, max));
        }
        if (value < min) {
            throw new IllegalArgumentException(rangeMessage(Ranges.LARGER, value, min));
        }
        if (value > max) {
            throw new IllegalArgumentException(rangeMessage(Ranges.SMALLER, value, max));
        }
        return value;
    }

    /** Checks if the value is smaller than the max */
    public static double isSmaller(double value, double max) {
        return inRange(value, Double.NEGATIVE_INFINITY, max);
    }

    /** Checks if the value is larger than the min */
    public static double isLarger(double value, double min) {
        return inRange(value, min, Double.POSITIVE_INFINITY);
    }

    /** Checks if the value is in the range */
    public static double inRange(double value, double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException(rangeMessage(Ranges.RANGE, min, max));
        }
        if (value < min) {
            throw new IllegalArgumentException(rangeMessage(Ranges.LARGER, value, min));
        }
        if (value > max) {
            throw new IllegalArgumentException(rangeMessage(Ranges.SMALLER, value, max));
        }
        return value;
    }

    /** Checks if the value is smaller than the max */
    public static int isSmaller(int value, int max) {
        return inRange(value, Integer.MIN_VALUE, max);
    }

    /** Checks if the value is larger than the min */
    public static int isLarger(int value, int min) {
        return inRange(value, min, Integer.MAX_VALUE);
    }

    /** Checks if the value is in the range */
    public static int inRange(int value, int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException(rangeMessage(Ranges.RANGE, min, max));
        }
        if (value < min) {
            throw new IllegalArgumentException(rangeMessage(Ranges.LARGER, value, min));
        }
        if (value > max) {
            throw new IllegalArgumentException(rangeMessage(Ranges.SMALLER, value, max));
        }
        return value;
    }

    /** Require the string to be non null or empty, or throw NullPointerException */
    public static String nonNullOrEmpty(String value, Object message) {
        if (value == null || value.isEmpty()) {
            throw new NullPointerException(String.valueOf(message));
        }
        return value;
    }

    /** Require the object is non null, or throw NullPointerException */
    public static <T> T nonNull(T value, Object message) {
        if (value == null) {
            throw new NullPointerException(String.valueOf(message));
        }
        return value;
    }

    /** Grab the field state value and name, used for toString */
    private static Optional<String> fieldState(Field field, Object instance) {
        Optional<Object> inst = fieldInstance(field, instance);
        if (inst.isPresent()) {
            return Optional.of(field.getName() + ": " + inst.get());
        }
        return Optional.empty();
    }

    /** Grab the field instance */
    private static Optional<Object> fieldInstance(Field field, Object instance) {
        boolean access = field.isAccessible();
        try {
            field.setAccessible(true);
            Object value = field.get(instance);
            field.setAccessible(access);
            return Optional.of(value == null ? "null" : value);
        } catch (IllegalAccessException error) {
            return Optional.empty();
        }
    }

    /** Use reflection to print the to string method */
    public static String toString(Object instance, String... vars) {
        String prefix = instance.getClass().getSimpleName();
        Field[] fields = instance.getClass().getDeclaredFields();
        List<String> variables = new ArrayList<>(fields.length);
        if (vars.length == 0) {
            for (Field field : fields) {
                fieldState(field, instance).ifPresent(variables::add);
            }
        } else {
            for (String name : vars) {
                try {
                    fieldState(instance.getClass().getDeclaredField(name), instance).ifPresent(variables::add);
                } catch (NoSuchFieldException error) {
                    error.printStackTrace();
                    variables.add(name + ": error");
                }
            }
        }
        return prefix + "(" + instance.hashCode()  +") {" + Joiner.on(", ").join(variables) + "}";
    }

    /** Check if the objects hash codes matches, thus the objects should be equal */
    public static boolean equals(Object a, Object b) {
        return Objects.hashCode(a) == Objects.hashCode(b);
    }

    /** Generate a hash code for the list of objects */
    public static int hashCode(Object instance, Object... objects) {
        int hash = Objects.hashCode(instance.getClass().getName());
        if (objects.length == 0) {
            Field[] fields = instance.getClass().getDeclaredFields();
            for (Field field : fields) {
                hash *= objects.length;
                hash += Objects.hashCode(fieldInstance(field, instance));
            }
        } else {
            for (Object object : objects) {
                hash *= objects.length;
                hash += Objects.hashCode(object);
            }
        }
        return hash;
    }
}
