/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.tuple;

/** Represents a Single Tuple of an object */
public final class Single<A> {
    public final TupleValue<A> a;

    public Single(A a) {
        this.a = new TupleValue<>(a);
    }
}
