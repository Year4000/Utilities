package net.year4000.utilities;

import net.year4000.utilities.reflection.*;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class ReflectionTest {
    private static final String FOO = "foo";
    private static final String BAR = "bar";
    private static final String FOO_BAR = "foo bar";
    private static final MyObject OBJECT = new MyObject();

    private final static class MyObject {
        private String foo = FOO;
        private MyObject object = OBJECT;

        private String method() {
            return FOO_BAR;
        }
    }

    @Proxied("net.year4000.utilities.ReflectionTest$MyObject")
    public interface ProxyMyObject {
        @Setter void foo(String value);
        @Getter String foo();
        @Invoke String method();

        @Getter @Bridge(ProxyMyObject.class) ProxyMyObject object();
        @Setter void object(ProxyMyObject object);

        default String hello() {
            return "world";
        }
    }

    @Test
    public void invokeTest() {
        ProxyMyObject proxy = Gateways.proxy(ProxyMyObject.class, new MyObject());
        Assert.assertEquals(FOO_BAR, proxy.method());
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
    public void defaultTest() {
        ProxyMyObject proxy = Gateways.proxy(ProxyMyObject.class, new MyObject());
        Assert.assertEquals("world", proxy.hello());
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
        }
    }
}
