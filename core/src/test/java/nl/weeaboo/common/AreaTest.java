package nl.weeaboo.common;

import org.junit.Assert;
import org.junit.Test;

public class AreaTest {

    @Test
    public void createArea() {
        assertArea(Area.of(1, 2, 3, 4), 1, 2, 3, 4);
    }

    /**
     * Attempting to create an empty area just returns the shared {@link Area#EMPTY} instance.
     */
    @Test
    public void createEmptyRect() {
        Assert.assertSame(Area.EMPTY, Area.of(0, 0, 0, 0));
    }

    @Test
    public void equals() {
        Area a1234 = Area.of(1, 2, 3, 4);
        Assert.assertEquals(Area.of(1, 2, 3, 4).hashCode(), a1234.hashCode());
        Assert.assertEquals(Area.of(1, 2, 3, 4), a1234);

        // Changing any attribute (x,y,w,h) causes the others to no longer be equal
        Assert.assertNotEquals(a1234, Area.of(9, 2, 3, 4));
        Assert.assertNotEquals(a1234, Area.of(1, 9, 3, 4));
        Assert.assertNotEquals(a1234, Area.of(1, 2, 9, 4));
        Assert.assertNotEquals(a1234, Area.of(1, 2, 3, 9));

        // Area and Area2D are never equal
        Assert.assertNotEquals(a1234, Area2D.of(1, 2, 3, 4));
    }

    @Test
    public void conversionFunctions() {
        Area a1234 = Area.of(1, 2, 3, 4);
        Assert.assertEquals(Area2D.of(1, 2, 3, 4), a1234.toArea2D());
    }

    @Test
    public void flip() {
        Area a1234 = Area.of(1, 2, 3, 4);
        assertArea(a1234.flipped(false, false), 1, 2, 3, 4);
        assertArea(a1234.flipped(true, false), 4, 2, -3, 4);
        assertArea(a1234.flipped(false, true), 1, 6, 3, -4);
        assertArea(a1234.flipped(true, true), 4, 6, -3, -4);
    }

    private static void assertArea(Area area, int expectedX, int expectedY, int expectedW, int expectedH) {
        Assert.assertEquals(expectedX, area.x);
        Assert.assertEquals(expectedY, area.y);
        Assert.assertEquals(expectedW, area.w);
        Assert.assertEquals(expectedH, area.h);
    }

}
