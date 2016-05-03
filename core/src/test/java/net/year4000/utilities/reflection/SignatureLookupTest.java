package net.year4000.utilities.reflection;

import org.junit.Assert;
import org.junit.Test;

public class SignatureLookupTest {
    private abstract class Dumb {
        // Must be 2 for test bellow
        String foo = "foo";
        String bar = "bar";

        // Must be 4 for test bellow
        abstract void a();
        abstract void b();
        abstract void c();
        abstract void d();

        abstract int x();
    }

    private SignatureLookup signType(String sig) {
        return new SignatureLookup(sig, Dumb.class, SignatureLookup.For.FIELD);
    }

    @Test
    public void typeTest() {
        // Check types
        Assert.assertEquals(boolean.class, signType("Z").returnType);
        Assert.assertEquals(void.class, signType("V").returnType);
        Assert.assertEquals(long.class, signType("J").returnType);
        Assert.assertEquals(short.class, signType("S").returnType);
        Assert.assertEquals(int.class, signType("I").returnType);
        Assert.assertEquals(byte.class, signType("B").returnType);
        Assert.assertEquals(double[].class, signType("[D").returnType);
        Assert.assertEquals(Object[].class, signType("[Ljava/lang/Object;").returnType);
    }

    @Test
    public void fieldSignatureTest() {
        SignatureLookup lookup = new SignatureLookup("Ljava/lang/String;", Dumb.class, SignatureLookup.For.FIELD);
        Assert.assertEquals(2, lookup.findFields().size());
    }

    @Test
    public void methodSignatureTest() throws Exception {
        SignatureLookup lookup = new SignatureLookup("()V", Dumb.class, SignatureLookup.For.METHOD);
        Assert.assertEquals(4, lookup.findMethods().size());
    }
}
