/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.tuple;

/** Represents a Pair Tuple of objects */
public final class Pair<A, B> {
    public final TupleValue<A> a;
    public final TupleValue<B> b;

    public Pair(A a, B b) {
        this.a = new TupleValue<>(a);
        this.b = new TupleValue<>(b);
    }
}
