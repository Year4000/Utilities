package net.year4000.utilities;

import org.junit.Test;

import static net.year4000.utilities.ObjectHelperTest.MyEnum.*;

public class ObjectHelperTest {
    private static class MyObject {
        private String bar = "bar";
        private String foo = "foo";

        @Override
        public String toString() {
            return ObjectHelper.toString(this);
        }

        @Override
        public boolean equals(Object other) {
            return ObjectHelper.equals(this, other);
        }

        @Override
        public int hashCode() {
            return ObjectHelper.hashCode(this);
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
        ObjectHelper.checkState(ONE, ONE, TWO, THREE, FOUR);

        // Bytes
        ObjectHelper.isLarger((byte) 0x1, (byte) 0x0);
        ObjectHelper.isSmaller((byte) 0x0, (byte) 0x1);
        ObjectHelper.inRange((byte) 0x1, (byte) 0x0, (byte) 0x2);

        // Shorts
        ObjectHelper.isLarger((short) 1, (short) 0);
        ObjectHelper.isSmaller((short) 0, (short) 1);
        ObjectHelper.inRange((short) 1, 0, (short) 2);

        // Integers
        ObjectHelper.isLarger(1, 0);
        ObjectHelper.isSmaller(0, 1);
        ObjectHelper.inRange(1, 0, 2);

        // Float
        ObjectHelper.isLarger((float) 0.1, (float) 0.0);
        ObjectHelper.isSmaller((float) 0.0, (float) 0.1);
        ObjectHelper.inRange((float) 0.1, (float) 0.0, (float) 0.2);

        // Longs
        ObjectHelper.isLarger((long) 0.1, (long) 0.0);
        ObjectHelper.isSmaller((long) 0.0, (long) 0.1);
        ObjectHelper.inRange((long) 0.1, (long) 0.0, (long) 0.2);

        // Doubles
        ObjectHelper.isLarger(0.1, 0.0);
        ObjectHelper.isSmaller(0.0, 0.1);
        ObjectHelper.inRange(0.1, 0.0, 0.2);
    }

    @Test
    public void nullChecks() {
        ObjectHelper.nonNull(new Object(), "object");
        ObjectHelper.nonNullOrEmpty("String", "string");
    }
}
