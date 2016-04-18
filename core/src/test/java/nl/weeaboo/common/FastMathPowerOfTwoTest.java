package nl.weeaboo.common;

import org.junit.Assert;
import org.junit.Test;

public class FastMathPowerOfTwoTest {

    @Test
    public void isPowerOfTwo() {
        assertIsPoT(false, 0); // Zero is not a power of two

        assertIsPoT(true, 1);
        assertIsPoT(true, 2);
        assertIsPoT(false, 3);
        assertIsPoT(true, 4);

        assertIsPoT(false, -2); // Negative values always return false
        assertIsPoT(false, Integer.MIN_VALUE);

        // Two-arg version
        Assert.assertEquals(false, FastMath.isPowerOfTwo(0, 0));
        Assert.assertEquals(false, FastMath.isPowerOfTwo(2, 0));
        Assert.assertEquals(false, FastMath.isPowerOfTwo(0, 4));
        Assert.assertEquals(true, FastMath.isPowerOfTwo(2, 4)); // Only true if both are PoT
    }

    private static void assertIsPoT(boolean expected, int input) {
        Assert.assertEquals(expected, FastMath.isPowerOfTwo(input));
    }

    @Test
    public void toPowerOfTwo() {
        assertToPoT(null, Integer.MIN_VALUE); // Negative values are invalid
        assertToPoT(null, -1); // Invalid
        assertToPoT(null, 0); // Zero is treated as invalid
        assertToPoT(1, 1);
        assertToPoT(2, 2);
        assertToPoT(4, 3);
        assertToPoT(1024, 1000);
        assertToPoT(0x40000000, 0x40000000); // Largest possible power-of-two
        assertToPoT(null, Integer.MAX_VALUE); // Too large
    }

    private static void assertToPoT(Integer expected, int input) {
        if (expected != null) {
            Assert.assertEquals(expected.intValue(), FastMath.toPowerOfTwo(input));
        } else {
            try {
                FastMath.toPowerOfTwo(input);
                Assert.fail("Expected an exception");
            } catch (IllegalArgumentException iae) {
                // Expected
            }
        }
    }

}
