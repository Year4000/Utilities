package net.year4000.utilities;

import net.year4000.utilities.reflection.SignatureLookup;
import net.year4000.utilities.reflection.annotations.Getter;
import net.year4000.utilities.reflection.annotations.Signature;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class SignatureLookupTest {
    private abstract class Dumb {
        @Getter(signature = @Signature("Z"))
        abstract void bool();

        @Getter(signature = @Signature("I"))
        abstract void integer();
    }

    @Test
    @Ignore
    public void fieldSignatureTest() throws Exception {
        Signature sig = Dumb.class.getDeclaredMethod("bool").getAnnotation(Getter.class).signature()[0];
        Assert.assertEquals(boolean.class, new SignatureLookup(sig, Dumb.class, SignatureLookup.For.FIELD));
    }
}
