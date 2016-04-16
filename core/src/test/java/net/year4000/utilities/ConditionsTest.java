package net.year4000.utilities;

import org.junit.Test;

import static net.year4000.utilities.ConditionsTest.MyEnum.*;

public class ConditionsTest {

    enum MyEnum {ONE, TWO, THREE, FOUR}

    @Test
    public void rangeTests() {
        Conditions.checkState(ONE, ONE, TWO, THREE, FOUR);

        // Bytes
        Conditions.isLarger((byte) 0x1, (byte) 0x0);
        Conditions.isSmaller((byte) 0x0, (byte) 0x1);
        Conditions.inRange((byte) 0x1, (byte) 0x0, (byte) 0x2);

        // Shorts
        Conditions.isLarger((short) 1, (short) 0);
        Conditions.isSmaller((short) 0, (short) 1);
        Conditions.inRange((short) 1, 0, (short) 2);

        // Integers
        Conditions.isLarger(1, 0);
        Conditions.isSmaller(0, 1);
        Conditions.inRange(1, 0, 2);

        // Float
        Conditions.isLarger((float) 0.1, (float) 0.0);
        Conditions.isSmaller((float) 0.0, (float) 0.1);
        Conditions.inRange((float) 0.1, (float) 0.0, (float) 0.2);

        // Longs
        Conditions.isLarger((long) 0.1, (long) 0.0);
        Conditions.isSmaller((long) 0.0, (long) 0.1);
        Conditions.inRange((long) 0.1, (long) 0.0, (long) 0.2);

        // Doubles
        Conditions.isLarger(0.1, 0.0);
        Conditions.isSmaller(0.0, 0.1);
        Conditions.inRange(0.1, 0.0, 0.2);
    }

    @Test
    public void nullChecks() {
        Conditions.nonNull(new Object(), "object");
        Conditions.nonNullOrEmpty("String", "string");
    }
}
