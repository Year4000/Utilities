package net.year4000.utilities.returns;

import lombok.Value;

/** Allow returning four objects at a time */
@Value
public class QuadReturn<A, B, C, D> {
    private A a;
    private B b;
    private C c;
    private D d;
}
