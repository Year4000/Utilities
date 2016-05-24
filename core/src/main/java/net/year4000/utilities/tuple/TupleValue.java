/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.tuple;

import net.year4000.utilities.value.MutableValue;

/** Represents a element inside a tuple */
public final class TupleValue<V> extends MutableValue<V> {
    /** Init this Tuple var with the specific value */
    TupleValue(V value) {
        super(value);
    }
}
