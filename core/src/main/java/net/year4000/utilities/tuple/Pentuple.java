/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.tuple;

/** Represents a Pentuple Tuple of objects */
public class Pentuple<A, B, C, D, E> {
    public final TupleValue<A> a;
    public final TupleValue<B> b;
    public final TupleValue<C> c;
    public final TupleValue<D> d;
    public final TupleValue<E> e;

    public Pentuple(A a, B b, C c, D d, E e) {
        this.a = new TupleValue<>(a);
        this.b = new TupleValue<>(b);
        this.c = new TupleValue<>(c);
        this.d = new TupleValue<>(d);
        this.e = new TupleValue<>(e);
    }
}
