/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.value;

import net.year4000.utilities.Conditions;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Supplier;

/** Internal use of Value for various value needs */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public interface Value<V> extends Supplier<V> {
    /** Creates an empty instance of Value */
    static <V> Value<V> empty() {
        return new ImmutableValue<>((V) null);
    }

    /** Creates an instance of Value for the given value */
    static <V> Value<V> of(V value) {
        return new ImmutableValue<>(value);
    }

    /** Creates an instance of Value for the given value */
    static <V> Value<V> of(Optional<V> value) {
        return new ImmutableValue<>(value.orElse(null));
    }

    /** Creates an instance of Value for the given value */
    static <V> Value<V> of(Value<V> value) {
        return new ImmutableValue<>(value.get());
    }

    /** Creates an instance of Value for the given String, treat empty strings as null */
    static Value<String> of(String value) {
        return new ImmutableValue<>((value == null || value.isEmpty()) ? null : value);
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

    /** Tries to parse the long from the String and returns the value if its found */
    static Value<Long> parseLong(String value) {
        try {
            if (value == null || value.isEmpty()) return empty();
            return of(Long.parseLong(value));
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

    /** Tries to parse the boolean from the String and returns the value if its found */
    static Value<Boolean> parseBoolean(String value) {
        try {
            if (value == null || value.isEmpty()) return empty();
            return of(Boolean.parseBoolean(value));
        } catch (NumberFormatException error) {
            return empty();
        }
    }

    /** Does the value instance contain a non null value */
    default boolean isPresent() {
        return get() != null;
    }

    /** Does the value instance contain a null value */
    default boolean isEmpty() {
        return get() == null;
    }

    /** Get the value of this instance if its present for return the argument could be null */
    default V getOrElse(V value) {
        return isEmpty() ? value : get();
    }

    /** Gets the value of this instance or throw NullPointerException with the provided message */
    default V getOrThrow(String message) {
        return Conditions.nonNull(get(), message);
    }

    /** Gets the value of this instance or throw NullPointerException */
    default V getOrThrow() {
        return getOrThrow("null");
    }

    /** Run a consumer instance on the value if it exists */
    default Value<V> ifPresent(Consumer<V> consumer) {
        if (isPresent()) {
            consumer.accept(get());
        }
        return this;
    }

    /** Run a runnable instance on the value if it exists */
    default Value<V> ifPresent(Runnable runnable) {
        if (isPresent()) {
            runnable.run();
        }
        return this;
    }

    /** Run a runnable instance on the value if it is empty */
    default Value<V> ifEmpty(Runnable runnable) {
        if (isEmpty()) {
            runnable.run();
        }
        return this;
    }
}
