package net.year4000.utilities.reflection;

import net.year4000.utilities.value.Value;
import org.junit.Assert;
import org.junit.Test;

public class ReflectionsTest {
    private static class MyObject {
        private static String foo_bar = "foo bar";
        private String foo = "bar";
        private String bar = "foo";

        private MyObject() {}

        private MyObject(String foo) {}

        private static String bar() {
            return "foo";
        }

        private String foo() {
            return "bar";
        }
    }

    @Test
    public void instanceTest() {
        Assert.assertFalse(Reflections.instance(MyObject.class).isEmpty());
        Assert.assertFalse(Reflections.instance(MyObject.class, "bar").isEmpty());
        Assert.assertFalse(Reflections.instance("(Ljava/lang/String;)V", MyObject.class, "bar").isEmpty());
    }

    @Test
    public void fieldTest() {
        MyObject object = new MyObject();
        Assert.assertFalse(Reflections.getter(object, "foo").isEmpty());
        Assert.assertEquals("bar", Reflections.getter(object, "foo").get());
        Assert.assertTrue(Reflections.setter(object, "foo", "foo"));
        Assert.assertEquals("foo", Reflections.getter(object, "foo").get());
        Assert.assertEquals("foo bar", Reflections.getter(MyObject.class, "foo_bar").get());
        Assert.assertTrue(Reflections.setter(MyObject.class, "foo_bar", "bar foo"));
        Assert.assertNotEquals("foo bar", Reflections.getter(MyObject.class, "foo_bar").get());
    }

    @Test
    public void classTest() {
        final String clazz = "net.year4000.utilities.reflection.ReflectionsTest$MyObject";
        // Simple
        Value<Class<?>> simple = Reflections.clazz(clazz);
        Assert.assertTrue(simple.isPresent());
        Assert.assertEquals(MyObject.class, simple.get());
        // Complex
        Value<Class<?>> complex = Reflections.clazz(clazz, false, ReflectionsTest.class.getClassLoader());
        Assert.assertTrue(complex.isPresent());
        Assert.assertEquals(MyObject.class, complex.get());
    }

    @Test
    public void methodTest() {
        MyObject object = new MyObject();
        Value<Object> value = Reflections.invoke(object, "foo");
        Assert.assertTrue(value.isPresent());
        Assert.assertEquals(object.foo(), value.get());
        Assert.assertEquals("foo", Reflections.invoke(MyObject.class, "bar").get());
    }
}
