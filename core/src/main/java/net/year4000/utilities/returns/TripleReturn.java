package net.year4000.utilities.returns;

import lombok.Value;

/** Allow returning three objects at a time */
@Value
public class TripleReturn<A, B, C> {
    private A a;
    private B b;
    private C c;
}
