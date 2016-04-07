package net.year4000.utilities;

import org.junit.Test;

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
}
