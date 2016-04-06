package net.year4000.utilities.tuple;

/** Represents a Triad Tuple of objects */
public final class Triad<A, B, C> {
    public final TupleValue<A> a;
    public final TupleValue<B> b;
    public final TupleValue<C> c;

    public Triad(A a, B b, C c) {
        this.a = new TupleValue<>(a);
        this.b = new TupleValue<>(b);
        this.c = new TupleValue<>(c);
    }
}
