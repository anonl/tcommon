package nl.weeaboo.common;

import org.junit.Assert;
import org.junit.Test;

public class Insets2DTest {

    // TODO: Test convenience constructors

    @Test
    public void equals() {
        Insets2D i1234 = Insets2D.of(1, 2, 3, 4);
        Assert.assertEquals(Insets2D.of(1, 2, 3, 4).hashCode(), i1234.hashCode());
        Assert.assertEquals(Insets2D.of(1, 2, 3, 4), i1234);

        // Changing any attribute (x,y,w,h) causes the other to no longer be equal
        Assert.assertNotEquals(i1234, Insets2D.of(9, 2, 3, 4));
        Assert.assertNotEquals(i1234, Insets2D.of(1, 9, 3, 4));
        Assert.assertNotEquals(i1234, Insets2D.of(1, 2, 9, 4));
        Assert.assertNotEquals(i1234, Insets2D.of(1, 2, 3, 9));

        // Not equal to null
        Assert.assertNotEquals(i1234, null);
    }

    @Test
    public void invalidRange() {
        // Empty insets are legal
        assertLegal(true, 0, 0, 0, 0);

        // Negative values aren't allowed
        assertLegal(false, -1, 0, 0, 0);
        assertLegal(false, 0, -1, 0, 0);
        assertLegal(false, 0, 0, -1, 0);
        assertLegal(false, 0, 0, 0, -1);

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

    private void assertLegal(boolean expected, double top, double right, double bottom, double left) {
        try {
            Insets2D.of(top, right, bottom, left);
            Assert.assertEquals(expected, true);
        } catch (IllegalArgumentException iae) {
            Assert.assertEquals("Exception: " + iae, expected, false);
        }
    }

}
