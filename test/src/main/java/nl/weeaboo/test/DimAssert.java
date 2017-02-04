package nl.weeaboo.test;

import org.junit.Assert;

import nl.weeaboo.common.Dim2D;

public final class DimAssert {

    private DimAssert() {
    }

    /**
     * Checks if two dimensions are equal, allowing up to a small {@code epsilon} difference.
     */
    public static void assertEquals(Dim2D expected, Dim2D actual, double epsilon) {
        Assert.assertEquals("Invalid w: " + actual, expected.w, actual.w, epsilon);
        Assert.assertEquals("Invalid h: " + actual, expected.h, actual.h, epsilon);
    }

}
