package net.year4000.utilities.value;

import com.google.common.base.Strings;

import java.util.Objects;

/** Internal use of Value for various value needs */
public interface Value<V> {
    /** Creates an empty instance of Value */
    static <V> Value<V> empty() {
        return new ImmutableValue<>(null);
    }

    /** Creates an instance of Value for the given value */
    static <V> Value<V> of(V value) {
        return new ImmutableValue<>(value);
    }

    /** Creates an instance of Value for the given String, treat empty strings as null */
    static Value<String> of(String value) {
        return new ImmutableValue<>(Strings.emptyToNull(value));
    }

    /** Tries to parse the integer from the String and returns the value if its found */
    static Value<Integer> parseInteger(String value) {
        try {
            if (value == null || value.isEmpty()) return empty();
            return of(Integer.parseInt(value));
        } catch (NumberFormatException error) {
            return empty();
        }
    }

    /** Tries to parse the double from the String and returns the value if its found */
    static Value<Double> parseDouble(String value) {
        try {
            if (value == null || value.isEmpty()) return empty();
            return of(Double.parseDouble(value));
        } catch (NumberFormatException error) {
            return empty();
        }
    }

    /** Tries to parse the float from the String and returns the value if its found */
    static Value<Float> parseFloat(String value) {
        try {
            if (value == null || value.isEmpty()) return empty();
            return of(Float.parseFloat(value));
        } catch (NumberFormatException error) {
            return empty();
        }
    }

    /** Tries to parse the short from the String and returns the value if its found */
    static Value<Short> parseShort(String value) {
        try {
            if (value == null || value.isEmpty()) return empty();
            return of(Short.parseShort(value));
        } catch (NumberFormatException error) {
            return empty();
        }
    }

    /** Tries to parse the byte from the String and returns the value if its found */
    static Value<Byte> parseByte(String value) {
        try {
            if (value == null || value.isEmpty()) return empty();
            return of(Byte.parseByte(value));
        } catch (NumberFormatException error) {
            return empty();
        }
    }

    /** Does the value instance contain a non null value */
    boolean isEmpty();

    /** Get the value of this instance could be null */
    V get();

    /** Get the value of this instance if its present for return the argument could be null */
    default V getOrElse(V value) {
        return isEmpty() ? value : get();
    }

    /** Gets the value of this instance or throw NullPointerException with the provided message */
    default V getOrThrow(String message) {
        return Objects.requireNonNull(get(), message);
    }

    /** Gets the value of this instance or throw NullPointerException */
    default V getOrThrow() {
        return Objects.requireNonNull(get());
    }
}
