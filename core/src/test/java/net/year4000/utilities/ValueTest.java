package net.year4000.utilities;

import net.year4000.utilities.value.TypeValue;
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

    @Test
    public void miscTest() {
        Value.of("foo").getOrThrow();
        Value.of("bar").ifPresent(value -> {}).ifEmpty(value -> {});
    }

    @Test
    public void stringTest() {
        TypeValue value = new TypeValue("String");
        Assert.assertTrue(value.isPresent());
        Assert.assertTrue(value.isString());
        Assert.assertFalse(value.isNumber());
    }

    @Test
    public void integerTest() {
        TypeValue value = new TypeValue(3);
        Assert.assertTrue(value.isPresent());
        Assert.assertFalse(value.isString());
        Assert.assertEquals(3, value.toInt());
        Assert.assertEquals((short) 3, value.toShort());
        Assert.assertEquals((long) 3, value.toLong());
        Assert.assertEquals((byte) 3, value.toByte());
    }

    @Test
    public void integerStringTest() {
        TypeValue value = new TypeValue("3");
        Assert.assertTrue(value.isPresent());
        Assert.assertFalse(value.isNumber());
        Assert.assertEquals(3, value.toInt());
        Assert.assertEquals((short) 3, value.toShort());
        Assert.assertEquals((long) 3, value.toLong());
        Assert.assertEquals((byte) 3, value.toByte());
    }

    @Test
    public void decimalTest() {
        TypeValue value = new TypeValue(3.14);
        Assert.assertTrue(value.isPresent());
        Assert.assertFalse(value.isString());
        Assert.assertEquals(3.14, value.toDouble(), 0);
        Assert.assertEquals((float) 3.14, value.toFloat(), 0);
    }

    @Test
    public void decimalStringTest() {
        TypeValue value = new TypeValue("3.14");
        Assert.assertTrue(value.isPresent());
        Assert.assertFalse(value.isNumber());
        Assert.assertEquals(3.14, value.toDouble(), 0);
        Assert.assertEquals((float) 3.14, value.toFloat(), 0);
    }

    @Test
    public void booleanTest() {
        Assert.assertTrue(Value.parseBoolean("true").isPresent());
        Assert.assertTrue(Value.parseBoolean("true").get());
        TypeValue value = new TypeValue(true);
        Assert.assertTrue(value.isBoolean());
        Assert.assertTrue(value.toBoolean());
        Assert.assertFalse(!value.toBoolean());
    }

    @Test
    public void booleanStringTest() {
        TypeValue value = new TypeValue("true");
        Assert.assertTrue(value.isString());
        Assert.assertTrue(value.toBoolean());
        Assert.assertFalse(!value.toBoolean());
    }
}
