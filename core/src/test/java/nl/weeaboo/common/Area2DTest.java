package nl.weeaboo.common;

import org.junit.Assert;
import org.junit.Test;

public class Area2DTest {

    @Test
    public void createArea() {
        assertArea(Area2D.of(1, 2, 3, 4), 1, 2, 3, 4);
    }

    @Test
    public void equals() {
        Area2D a1234 = Area2D.of(1, 2, 3, 4);
        Assert.assertEquals(Area2D.of(1, 2, 3, 4).hashCode(), a1234.hashCode());
        Assert.assertEquals(Area2D.of(1, 2, 3, 4), a1234);

        // Changing any attribute (x,y,w,h) causes the other to no longer be equal
        Assert.assertNotEquals(a1234, Area2D.of(9, 2, 3, 4));
        Assert.assertNotEquals(a1234, Area2D.of(1, 9, 3, 4));
        Assert.assertNotEquals(a1234, Area2D.of(1, 2, 9, 4));
        Assert.assertNotEquals(a1234, Area2D.of(1, 2, 3, 9));

        // Never equal to null
        Assert.assertNotEquals(a1234, null);

        // Area and Area2D are never equal
        Assert.assertNotEquals(a1234, Area.of(1, 2, 3, 4));
    }

    @Test
    public void flip() {
        Area2D a1234 = Area2D.of(1, 2, 3, 4);
        assertArea(a1234.flipped(false, false), 1, 2, 3, 4);
        assertArea(a1234.flipped(true, false), 4, 2, -3, 4);
        assertArea(a1234.flipped(false, true), 1, 6, 3, -4);
        assertArea(a1234.flipped(true, true), 4, 6, -3, -4);
    }

    @Test
    public void invalidRange() {
        // Empty area is legal
        assertLegal(true, 0, 0, 0, 0);

        // Negative values are allowed
        assertLegal(true, -1, 0, 0, 0);
        assertLegal(true, 0, -1, 0, 0);
        assertLegal(true, 0, 0, -1, 0);
        assertLegal(true, 0, 0, 0, -1);

        // NaN values aren't allowed
        assertLegal(false, Double.NaN, 0, 0, 0);
        assertLegal(false, 0, Double.NaN, 0, 0);
        assertLegal(false, 0, 0, Double.NaN, 0);
        assertLegal(false, 0, 0, 0, Double.NaN);

        // Infinity values aren't allowed (both positive and negative)
        assertLegal(false, Double.NEGATIVE_INFINITY, 0, 0, 0);
        assertLegal(false, 0, Double.NEGATIVE_INFINITY, 0, 0);
        assertLegal(false, 0, 0, Double.NEGATIVE_INFINITY, 0);
        assertLegal(false, 0, 0, 0, Double.NEGATIVE_INFINITY);
        assertLegal(false, Double.POSITIVE_INFINITY, 0, 0, 0);
        assertLegal(false, 0, Double.POSITIVE_INFINITY, 0, 0);
        assertLegal(false, 0, 0, Double.POSITIVE_INFINITY, 0);
        assertLegal(false, 0, 0, 0, Double.POSITIVE_INFINITY);
    }

    private void assertLegal(boolean expected, double x, double y, double w, double h) {
        try {
            Area2D.of(x, y, w, h);
            Assert.assertEquals(expected, true);
        } catch (IllegalArgumentException iae) {
            Assert.assertEquals("Exception: " + iae, expected, false);
        }
    }

    private static void assertArea(Area2D area, double expectedX, double expectedY, double expectedW,
            double expectedH) {

        Assert.assertEquals(expectedX, area.x, 0.0);
        Assert.assertEquals(expectedY, area.y, 0.0);
        Assert.assertEquals(expectedW, area.w, 0.0);
        Assert.assertEquals(expectedH, area.h, 0.0);
    }

}
