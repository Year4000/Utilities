package net.year4000.utilities;

import org.junit.Assert;
import org.junit.Test;

public class ReflectionsTest {
    public static class MyObject {
        private String foo = "bar";

        private MyObject() {}

        private MyObject(String foo) {}
    }

    @Test
    public void instanceTest() {
        Assert.assertFalse(Reflections.instance(MyObject.class).isEmpty());
        Assert.assertFalse(Reflections.instance(MyObject.class, "bar").isEmpty());
    }

    @Test
    public void fieldTest() {
        MyObject object = new MyObject();
        Assert.assertFalse(Reflections.field(object, "foo").isEmpty());
        Assert.assertEquals("bar", Reflections.field(object, "foo").get());
    }
}
