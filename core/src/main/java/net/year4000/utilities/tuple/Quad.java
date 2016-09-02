/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.tuple;

/** Represents a Quad Tuple of objects */
public final class Quad<A, B, C, D> {
    public final TupleValue<A> a;
    public final TupleValue<B> b;
    public final TupleValue<C> c;
    public final TupleValue<D> d;

    public Quad(A a, B b, C c, D d) {
        this.a = new TupleValue<>(a);
        this.b = new TupleValue<>(b);
        this.c = new TupleValue<>(c);
        this.d = new TupleValue<>(d);
    }
}
