package nl.weeaboo.vn.test;

import org.junit.Assert;

import nl.weeaboo.common.Area2D;
import nl.weeaboo.common.Rect2D;

public final class RectAssert {

    private RectAssert() {
    }

    /**
     * Checks if two rects are equal, allowing up to a small {@code epsilon} difference.
     */
    public static void assertEquals(Rect2D expected, Rect2D actual, double epsilon) {
        assertEquals(expected.toArea2D(), actual.toArea2D(), epsilon);
    }

    /**
     * Checks if two areas are equal, allowing up to a small {@code epsilon} difference.
     */
    public static void assertEquals(Area2D expected, Area2D actual, double epsilon) {
        Assert.assertEquals("Invalid x: " + actual, expected.x, actual.x, epsilon);
        Assert.assertEquals("Invalid y: " + actual, expected.y, actual.y, epsilon);
        Assert.assertEquals("Invalid w: " + actual, expected.w, actual.w, epsilon);
        Assert.assertEquals("Invalid h: " + actual, expected.h, actual.h, epsilon);
    }

}
