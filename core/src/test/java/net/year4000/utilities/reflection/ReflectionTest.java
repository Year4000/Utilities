/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.reflection;

import net.year4000.utilities.reflection.annotations.*;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.function.Supplier;

public class ReflectionTest {
    private static final String FOO = "foo";
    private static final String BAR = "bar";
    private static final String FOO_BAR = "foo bar";
    private static final MyObject OBJECT = new MyObject();

    public abstract static class OtherObject {
        private static String other = "other";
    }

    private final static class MyObject extends OtherObject implements Supplier<String>, Runnable {
        private String foo = FOO;
        private MyObject object = OBJECT;
        private int findMe = 1;

        private String method() {
            return FOO_BAR;
        }

        private String findMe(int x, int y, int z) {
            return "found me";
        }

        @Override
        public String get() {
            return FOO;
        }

        @Override
        public void run() {
            System.out.println("I'm doing stuff in run");
        }
    }

    @Proxied("net.year4000.utilities.reflection.ReflectionTest$OtherObject")
    public interface ProxyOtherObject {
        @Getter @Static String other();
    }

    @Proxied("net.year4000.utilities.reflection.ReflectionTest$MyObject")
    @Implements({@Proxied("java.lang.Runnable"), @Proxied("java.util.function.Supplier")})
    public interface ProxyMyObject extends ProxyOtherObject {
        @Setter void foo(String value);
        @Getter String foo();
        @Invoke String method();

        @Getter @Bridge(ProxyMyObject.class) ProxyMyObject object();
        @Setter void object(ProxyMyObject object);

        @Invoke(signature = "(III)Ljava/lang/String;")
        String signatureInvoke(int x, int y, int z);

        @Getter(signature = "I")
        int signatureGetter();

        @Setter(signature = "I")
        void signatureSetter(int x);

        default String hello() {
            return "world";
        }

        Object $this();

        void $invalidateAll();

        long $cacheSize();

//        default void run() {
//            System.out.println("run test overwrite");
//            ((Runnable)$this()).run();
//        }

        // todo Replace the signature of this method with something more versatile
        @Decorator("run")
        default Method decorateRun(Method method) {
            // Use the python's decorator for now, my change it to get more access
            System.out.println("beforeOnRun");
            return method;
        }
    }

    @Test
    public void gatewaysTest() {
        Assert.assertEquals(MyObject.class, Gateways.reflectiveClass(ProxyMyObject.class));
        Assert.assertTrue(Gateways.proxy(ProxyMyObject.class) instanceof Runnable);
    }

    @Test
    public void extendTest() {
        ProxyMyObject proxy = Gateways.proxy(ProxyMyObject.class, new MyObject());
        Assert.assertEquals("other", proxy.other());
    }

    @Test
    public void invokeTest() {
        ProxyMyObject proxy = Gateways.proxy(ProxyMyObject.class, new MyObject());
        Assert.assertEquals(FOO_BAR, proxy.method());
        Supplier<String> supplier = (Supplier<String>) proxy;
        Assert.assertEquals(FOO, supplier.get());
    }

    @Test
    public void getterTest() {
        ProxyMyObject proxy = Gateways.proxy(ProxyMyObject.class, new MyObject());
        Assert.assertEquals(FOO, proxy.foo());
    }

    @Test
    public void setterTest() {
        ProxyMyObject proxy = Gateways.proxy(ProxyMyObject.class, new MyObject());
        Assert.assertNotEquals(BAR, proxy.foo());
        proxy.foo(BAR);
        Assert.assertEquals(BAR, proxy.foo());
    }

    @Test
    public void bridgeTest() {
        ProxyMyObject proxy = Gateways.proxy(ProxyMyObject.class, new MyObject());
        ProxyMyObject object = proxy.object();
        Assert.assertEquals(proxy.foo(), object.foo());
        object.object(null);
        Assert.assertNull(object.object());
    }

    @Test
    @Ignore
    public void defaultTest() {
        ProxyMyObject proxy = Gateways.proxy(ProxyMyObject.class, new MyObject());
        Assert.assertEquals("world", proxy.hello());
    }

    @Test
    public void signatureTest() {
        ProxyMyObject proxy = Gateways.proxy(ProxyMyObject.class, new MyObject());
        Assert.assertEquals("found me", proxy.signatureInvoke(1, 1, 1));
        Assert.assertEquals(1, proxy.signatureGetter());
        proxy.signatureSetter(0);
        Assert.assertNotEquals(1, proxy.signatureGetter());
    }

    @Test
    public void internalMethodsTest() {
        MyObject instance = new MyObject();
        ProxyMyObject proxy = Gateways.proxy(ProxyMyObject.class, instance);
        // Make sure that $this returns the instance that is needed
        Assert.assertEquals(proxy.$this().getClass(), instance.getClass());
        // At this point many methods been called
        proxy.$invalidateAll();
        // Invalidate the caches and will reset back to 1
        Assert.assertEquals(1, proxy.$cacheSize());
    }

    @Test
    public void decoratorTest() {
        MyObject instance = new MyObject();
        ProxyMyObject proxy = Gateways.proxy(ProxyMyObject.class, instance);
        Assert.assertTrue(proxy instanceof Runnable);
        ((Runnable) proxy).run();
    }

    @Test
    @Ignore
    public void timeTest() {
        for (int i = 0; i < Short.MAX_VALUE; i++) {
            bridgeTest();
            getterTest();
            defaultTest();
            setterTest();
            invokeTest();
            extendTest();
        }
    }
}
