package net.year4000.utilities;

import com.google.common.base.Stopwatch;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

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
        System.out.println(new MyObject().toString());
        System.out.println(new MyObject().hashCode());
        System.out.println(new MyObject().equals(new MyObject()));
        System.out.println(Utils.hostName());
    }

    @Test
    public void sleepTest() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Utils.quickSleep();
        System.out.println("Utils.quickSleep() lasted for: " + stopwatch.stop());
    }

    @Test
    public void checkersTest() {
        Assert.assertTrue(Utils.isIpAddress("127.0.0.1"));
        Assert.assertFalse(Utils.isIpAddress("a.b.c.d"));
    }

    @Test
    public void uuidTest() {
        final UUID uuid = UUID.randomUUID();
        final String stringUuid = uuid.toString();
        Assert.assertTrue(Utils.isUUID(stringUuid));
        Assert.assertEquals(uuid, Utils.toUUID(stringUuid));
    }
}
