package net.year4000.utilities;

import net.year4000.utilities.value.Value;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;

public class ValueTest {
    /** Test the value creations of for numbers */
    @Test
    public void parseNumberNotNull() {
        Assert.assertNotNull(Value.parseByte("0").get());
        Assert.assertNotNull(Value.parseDouble("0.0").get());
        Assert.assertNotNull(Value.parseFloat("0.0").get());
        Assert.assertNotNull(Value.parseInteger("0").get());
        Assert.assertNotNull(Value.parseShort("0").get());
    }

    /** Test the value creations of for numbers */
    @Test
    public void parseNumbersNull() {
        Assert.assertNull(Value.parseByte("").get());
        Assert.assertNull(Value.parseDouble("").get());
        Assert.assertNull(Value.parseFloat("").get());
        Assert.assertNull(Value.parseInteger("").get());
        Assert.assertNull(Value.parseShort("").get());
    }

    /** Test the values of getOrElse */
    @Test
    public void getOrElse() {
        Assert.assertEquals(Value.of("").getOrElse("Joshua"), "Joshua");
        Assert.assertEquals(Value.of("Joshua").getOrElse(""), "Joshua");
    }
}
