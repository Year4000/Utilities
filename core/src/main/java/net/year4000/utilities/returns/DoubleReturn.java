package net.year4000.utilities.returns;

import lombok.Value;

/** Allow returning two objects at a time */
@Value
public class DoubleReturn<A, B> {
    private A a;
    private B b;
}
