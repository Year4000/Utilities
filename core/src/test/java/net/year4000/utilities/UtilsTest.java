package net.year4000.utilities;

import org.junit.Test;

public class UtilsTest {
    private static class MyObject {
        private String bar = "bar";
        private String foo = "foo";
        private Object empty;

        @Override
        public String toString() {
            return Utils.toString(this);
        }

        @Override
        public boolean equals(Object other) {
            return Utils.equals(this, other);
        }

        @Override
        public int hashCode() {
            return Utils.hashCode(this);
        }
    }

    @Test
    public void test() {
        System.err.println(new MyObject().toString());
        System.err.println(new MyObject().hashCode());
        System.err.println(new MyObject().equals(new MyObject()));
    }
}
