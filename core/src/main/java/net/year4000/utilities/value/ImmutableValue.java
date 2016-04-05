package net.year4000.utilities.value;

import java.util.Objects;

/** Simple implementation of the Value */
public class ImmutableValue<V> implements Value<V> {
    protected final V value;

    /** Init this Value var with the specific value */
    protected ImmutableValue(V value) {
        this.value = value;
    }

    /** Get the value of this instance could be null */
    @Override
    public V get() {
        return value;
    }

    /** Checks if the value is equal to the provided object, could be the raw value or the wrapped value */
    @Override
    public boolean equals(Object other) {
        if (other == null && value == null) return true;
        return other != null && hashCode() == other.hashCode();
    }

    /** Generates the hashcode for the value */
    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
