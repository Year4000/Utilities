package net.year4000.utilities;

import net.year4000.utilities.value.Value;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ValueTest {
    private static final List<Value<? extends Number>> FULL_NUMBER_VALUES = Arrays.asList(
        Value.parseByte("0"),
        Value.parseDouble("0.0"),
        Value.parseFloat("0.0"),
        Value.parseInteger("0"),
        Value.parseShort("0")
    );
    private static final List<Value<? extends Number>> EMPTY_NUMBER_VALUES = Arrays.asList(
        Value.parseByte(""),
        Value.parseDouble(""),
        Value.parseFloat(""),
        Value.parseInteger(""),
        Value.parseShort("")
    );

    /** Test the value creations of for numbers */
    @Test
    public void parseNumberNotNullTest() {
        FULL_NUMBER_VALUES.forEach(value -> Assert.assertNotNull(value.get()));
    }

    /** Test the value creations of for numbers */
    @Test
    public void parseNumbersNullTest() {
        EMPTY_NUMBER_VALUES.forEach(value -> Assert.assertNull(value.get()));
    }

    /** Test the values of getOrElse */
    @Test
    public void getOrElseTest() {
        Assert.assertEquals(Value.of("").getOrElse("Joshua"), "Joshua");
        Assert.assertEquals(Value.of("Joshua").getOrElse(""), "Joshua");
    }

    @Test
    public void isEmptyTest() {
        Assert.assertTrue(Value.empty().isEmpty());
        Assert.assertTrue(Value.of("").isEmpty());
        Assert.assertFalse(Value.of(new Object()).isEmpty());
        Assert.assertFalse(Value.of("String").isEmpty());
        FULL_NUMBER_VALUES.forEach(value -> Assert.assertFalse(value.isEmpty()));
        EMPTY_NUMBER_VALUES.forEach(value -> Assert.assertTrue(value.isEmpty()));
    }

    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    @Test
    public void areEqualTest() {
        Assert.assertTrue(Value.of("Equal").equals("Equal"));
        Assert.assertTrue(Value.of("Equal").equals(Value.of("Equal")));
    }
}
