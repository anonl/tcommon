package nl.weeaboo.gdx.test;

import org.junit.Assert;

import nl.weeaboo.common.Insets2D;

public final class InsetsAssert {

    private InsetsAssert() {
    }

    /**
     * Checks if two insets are equal, allowing up to a small {@code epsilon} difference.
     */
    public static void assertEquals(Insets2D expected, Insets2D actual, double epsilon) {
        Assert.assertEquals("Invalid top: " + actual, expected.top, actual.top, epsilon);
        Assert.assertEquals("Invalid right: " + actual, expected.right, actual.right, epsilon);
        Assert.assertEquals("Invalid bottom: " + actual, expected.bottom, actual.bottom, epsilon);
        Assert.assertEquals("Invalid left: " + actual, expected.left, actual.left, epsilon);
    }

}
