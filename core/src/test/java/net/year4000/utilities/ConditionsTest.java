package net.year4000.utilities;

import org.junit.Test;

import static net.year4000.utilities.ConditionsTest.MyEnum.*;

public class ConditionsTest {
    private static class MyObject {
        private String bar = "bar";
        private String foo = "foo";
        private Object empty;

        @Override
        public String toString() {
            return Conditions.toString(this);
        }

        @Override
        public boolean equals(Object other) {
            return Conditions.equals(this, other);
        }

        @Override
        public int hashCode() {
            return Conditions.hashCode(this);
        }
    }

    @Test
    public void test() {
        System.err.println(new MyObject().toString());
        System.err.println(new MyObject().hashCode());
        System.err.println(new MyObject().equals(new MyObject()));
    }

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
