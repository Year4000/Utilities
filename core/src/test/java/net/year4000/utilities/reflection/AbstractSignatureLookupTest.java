package net.year4000.utilities.reflection;

import com.google.common.collect.ImmutableSet;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class AbstractSignatureLookupTest {
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

    @SuppressWarnings("unchecked")
    private AbstractSignatureLookup signType(String sig) {
        return new AbstractSignatureLookup(sig, Dumb.class, AbstractSignatureLookup.For.FIELD) {
            /** Do nothing just used for internal fields */
            @Override
            public ImmutableSet find() {
                return ImmutableSet.of();
            }
        };
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
        SignatureLookup<Field> lookup = SignatureLookup.fields("Ljava/lang/String;", Dumb.class);
        Assert.assertEquals(2, lookup.find().size());
    }

    @Test
    public void methodSignatureTest() throws Exception {
        SignatureLookup<Method> lookup = SignatureLookup.methods("()V", Dumb.class);
        Assert.assertEquals(4, lookup.find().size());
    }
}
