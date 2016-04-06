package net.year4000.utilities.value;

import java.util.Objects;

/** Simple implementation of the Value */
public class MutableValue<V> implements Value<V> {
    protected V value;

    /** Init this Value var with the specific value */
    protected MutableValue(V value) {
        this.value = value;
    }

    /** Get the value of this instance could be null */
    @Override
    public V get() {
        return value;
    }

    /** Set the value of this instance, could be null */
    public void set(V value) {
        this.value = value;
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