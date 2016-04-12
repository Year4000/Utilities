package net.year4000.utilities;

import net.year4000.utilities.value.Value;
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

    @Test
    public void classTest() {
        final String clazz = "net.year4000.utilities.ReflectionsTest$MyObject";
        // Simple
        Value<Class<?>> simple = Reflections.clazz(clazz);
        Assert.assertTrue(simple.isPresent());
        Assert.assertEquals(MyObject.class, simple.get());
        // Complex
        Value<Class<?>> complex = Reflections.clazz(clazz, false, ReflectionsTest.class.getClassLoader());
        Assert.assertTrue(complex.isPresent());
        Assert.assertEquals(MyObject.class, complex.get());
    }
}
