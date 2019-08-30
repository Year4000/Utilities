/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.reflection;

import net.year4000.utilities.value.Value;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;

public class ReflectionsTest {
    public static class MyObject implements Comparable<MyObject> {
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

        private String echo(String echo) {
            return echo;
        }

        @Override
        public int compareTo(MyObject other) {
            return 1;
        }
    }

    private interface Proxy {
        String foo();
    }

    /** This test just proves that setting accessible to a field do not change it at the class level */
    @Test
    public void accessibleTest() throws Throwable {
        Field field = ReflectionTest.MyObject.class.getDeclaredField("foo");
        boolean access = field.isAccessible();
        field.setAccessible(!access);
        Field field2 = ReflectionTest.MyObject.class.getDeclaredField("foo");
        boolean access2 = field2.isAccessible();
        Assert.assertEquals(access, access2);
    }

    @Test
    public void proxyTest() {
        // Simple handler that treated it correctly
        Proxy proxy = Reflections.proxy(Proxy.class, (proxy1, method, args) -> (args != null) ? 1 : "bar", Comparable.class);
        Assert.assertEquals("bar", proxy.foo());
        Comparable<MyObject> comparable = (Comparable<MyObject>) proxy;
        Assert.assertEquals(1, comparable.compareTo(null));
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

    @Test
    public void methodWithArgsTest() {
        MyObject object = new MyObject();
        Value<Object> value = Reflections.invoke(object, "echo", "echo");
        Assert.assertTrue(value.isPresent());
        Assert.assertEquals(object.echo("echo"), value.get());
    }
}
