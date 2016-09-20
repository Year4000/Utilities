/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.gradle

/** A class that helps with returning tuple vales with multiple types */
final class Tuples {
    /** A class for the use of pair */
    final static class TupleValue<V> {
        private V value

        /** Get the current value, could be null */
        V get() { value }

        /** Get or else the current value */
        V getOrElse(V value) { (V) this.value ?: value }

        /** Set the current value of this tuple */
        void set(V value) { this.value = value }

        @Override
        String toString() { "TupleValue(${value})" }
    }

    /** Represents a tuple pair */
    static class Pair<A, B> {
        final TupleValue<A> a;
        final TupleValue<B> b;

        Pair(A a, B b) {
            this.a = new TupleValue<>(value: a)
            this.b = new TupleValue<>(value: b)
        }

        @Override
        String toString() { "Pair(${a?.get()}, ${b?.get()})" }
    }
}
