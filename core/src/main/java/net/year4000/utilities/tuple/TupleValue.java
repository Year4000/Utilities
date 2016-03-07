package net.year4000.utilities.tuple;

/** Represents a element inside a tuple */
public final class TupleValue<V> {
    private V value;

    /** Init this Tuple var with the specific value */
    public TupleValue(V value) {
        this.value = value;
    }

    /** Get the element inside this value, could be null */
    public V get() {
        return value;
    }

    /** Tries to grab the value or allow setting an alt if the value is null */
    public V getOrElse(V alt) {
        return value == null ? alt : value;
    }

    /** Set the value inside this value, could be null */
    public void set(V value) {
        this.value = value;
    }
}
