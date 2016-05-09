package net.year4000.utilities.value;

import net.year4000.utilities.Utils;

import java.util.Objects;
import java.util.Optional;

/** Simple implementation of the Value */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class MutableValue<V> implements Value<V> {
    protected V value;

    /** Init this Value var with the specific value */
    public MutableValue(Optional<V> value) {
        this.value = value.orElse(null);
    }

    /** Init this Value var with the specific value */
    public MutableValue(Value<V> value) {
        this.value = value.get();
    }

    /** Init this Value var with the specific value */
    public MutableValue(V value) {
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
        return (other == null && value == null) || other != null && hashCode() == other.hashCode();
    }

    /** Generates the hashcode for the value */
    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return Utils.toString(this);
    }
}
