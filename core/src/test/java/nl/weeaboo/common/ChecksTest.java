package nl.weeaboo.common;

import org.junit.Assert;
import org.junit.Test;

public class ChecksTest {

    @Test
    public void testCheckNotNull() {
        // Returns the input argument if non-null
        Integer ival = Integer.valueOf(987);
        Assert.assertSame(ival, Checks.checkNotNull(ival));

        // Throws a NullPointerException if value is null
        try {
            Checks.checkNotNull(null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid value for <undefined>: null", e.getMessage());
        }

        // Throws a NullPointerException if value is null
        try {
            Checks.checkNotNull(null, "myvar");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid value for myvar: null", e.getMessage());
        }
    }

    @Test
    public void testCheckArgument() {
        Checks.checkArgument(true, "No error");

        // If the condition is false, throws an exception with the given error message
        try {
            Checks.checkArgument(false, "An error");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("An error", e.getMessage());
        }
    }

    @Test
    public void testCheckState() {
        Checks.checkState(true, "No error");

        // If the condition is false, throws an exception with the given error message
        try {
            Checks.checkState(false, "An error");
            Assert.fail();
        } catch (IllegalStateException e) {
            Assert.assertEquals("An error", e.getMessage());
        }
    }

    @Test
    public void checkIntRange() {
        assertIntRangeValid(true, Integer.MIN_VALUE, Integer.MIN_VALUE);
        assertIntRangeValid(true, 0, 0);
        assertIntRangeValid(true, Integer.MAX_VALUE, Integer.MAX_VALUE);
        assertIntRangeValid(false, 0, 1);
        assertIntRangeValid(true, 1, 0);

        assertIntRangeValid(true, 1, 0, 1);
        assertIntRangeValid(true, 1, 1, 1);
        assertIntRangeValid(false, 1, 1, -1);
    }

    @Test
    public void checkDoubleRange() {
        // Infinity/NaN values are never valid
        assertDoubleRangeValid(false, Double.NEGATIVE_INFINITY);
        assertDoubleRangeValid(false, Double.POSITIVE_INFINITY);
        assertDoubleRangeValid(false, Double.NaN);
        assertDoubleRangeValid(true, -1);
        assertDoubleRangeValid(true, 0);
        assertDoubleRangeValid(true, 1);

        assertDoubleRangeValid(true, Double.MIN_VALUE, 0.0);
        assertDoubleRangeValid(true, 0, 0);
        assertDoubleRangeValid(true, Double.MAX_VALUE, Double.MAX_VALUE);
        assertDoubleRangeValid(false, 0, 1);
        assertDoubleRangeValid(true, 1, 0);

        assertDoubleRangeValid(true, 1, 0, 1);
        assertDoubleRangeValid(true, 1, 1, 1);
        assertDoubleRangeValid(false, 1, 1, -1);
    }

    private void assertIntRangeValid(boolean expected, int val, int min) {
        try {
            Checks.checkRange(val, "myvar", min);
            Assert.assertEquals(expected, true);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(expected, false);
            Assert.assertEquals("Invalid value for myvar: " + val, e.getMessage());
        }
    }

    private void assertIntRangeValid(boolean expected, int val, int min, int max) {
        try {
            Checks.checkRange(val, "myvar", min, max);
            Assert.assertEquals(expected, true);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(expected, false);
            Assert.assertEquals("Invalid value for myvar: " + val, e.getMessage());
        }
    }

    private void assertDoubleRangeValid(boolean expected, double val) {
        try {
            Checks.checkRange(val, "myvar");
            Assert.assertEquals(expected, true);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(expected, false);
            Assert.assertEquals("Invalid value for myvar: " + val, e.getMessage());
        }
    }

    private void assertDoubleRangeValid(boolean expected, double val, double min) {
        try {
            Checks.checkRange(val, "myvar", min);
            Assert.assertEquals(expected, true);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(expected, false);
            Assert.assertEquals("Invalid value for myvar: " + val, e.getMessage());
        }
    }

    private void assertDoubleRangeValid(boolean expected, double val, double min, double max) {
        try {
            Checks.checkRange(val, "myvar", min, max);
            Assert.assertEquals(expected, true);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals(expected, false);
            Assert.assertEquals("Invalid value for myvar: " + val, e.getMessage());
        }
    }

}
