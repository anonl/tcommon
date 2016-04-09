package nl.weeaboo.common;

import org.junit.Assert;
import org.junit.Test;

public class RectTest {

    @Test
    public void createRect() {
        assertRect(Rect.of(1, 2, 3, 4), 1, 2, 3, 4);
    }

    /** Attempting to create an empty rect just returns the shared {@link Rect#EMPTY} instance */
    @Test
    public void createEmptyRect() {
        Assert.assertSame(Rect.EMPTY, Rect.of(0, 0, 0, 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeWidth() {
        Rect.of(0, 0, -10, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeHeight() {
        Rect.of(0, 0, 0, -10);
    }

    @Test
    public void equals() {
        Rect r1234 = Rect.of(1, 2, 3, 4);
        Assert.assertEquals(Rect.of(1, 2, 3, 4).hashCode(), r1234.hashCode());
        Assert.assertEquals(Rect.of(1, 2, 3, 4), r1234);

        // Changing any attribute (x,y,w,h) causes the rects to no longer be equal
        Assert.assertNotEquals(r1234, Rect.of(9, 2, 3, 4));
        Assert.assertNotEquals(r1234, Rect.of(1, 9, 3, 4));
        Assert.assertNotEquals(r1234, Rect.of(1, 2, 9, 4));
        Assert.assertNotEquals(r1234, Rect.of(1, 2, 3, 9));

        // Rect and Rect2D are never equal
        Assert.assertNotEquals(r1234, Rect2D.of(1, 2, 3, 4));
    }

    @Test
    public void translatedCopy() {
        Rect translated = Rect.of(1, 2, 3, 4).translatedCopy(-2, -4);
        assertRect(translated, -1, -2, 3, 4);
    }

    @Test
    public void conversionFunctions() {
        Rect rect1234 = Rect.of(1, 2, 3, 4);
        Assert.assertEquals(Rect2D.of(1, 2, 3, 4), rect1234.toRect2D());
        Assert.assertEquals(Area.of(1, 2, 3, 4), rect1234.toArea());
        Assert.assertEquals(Area2D.of(1, 2, 3, 4), rect1234.toArea2D());
    }

    @Test
    public void combine() {
        // Empty rect if there are no rects to combine
        Assert.assertEquals(Rect.EMPTY, Rect.combine());

        // A single rect combines to itself
        Rect combined = Rect.combine(Rect.of(1, 2, 3, 4));
        Assert.assertEquals(Rect.of(1, 2, 3, 4), combined);

        // Check that the expected bounding rect is returned
        combined = Rect.combine(Rect.of(-1, -2, 11, 2), Rect.of(0, 1, 1, 3));
        Assert.assertEquals(Rect.of(-1, -2, 11, 6), combined);
    }

    @Test
    public void containsPoint() {
        Rect r = Rect.of(1, 2, 3, 4);

        // Zero width or zero height rects contains nothing
        Assert.assertEquals(false, Rect.of(1, 2, 0, 4).contains(1, 2));
        Assert.assertEquals(false, Rect.of(1, 2, 3, 0).contains(1, 2));

        // Bounds are inclusive
        Assert.assertEquals(true, r.contains(1, 2));
        Assert.assertEquals(true, r.contains(4, 2));
        Assert.assertEquals(true, r.contains(1, 6));
        Assert.assertEquals(true, r.contains(4, 6));
    }

    @Test
    public void containsRect() {
        Rect r = Rect.of(1, 2, 3, 4);

        // Zero width or zero height rects contains nothing (not even itself)
        Assert.assertEquals(false, Rect.of(1, 2, 0, 4).contains(1, 2, 0, 4));
        Assert.assertEquals(false, Rect.of(1, 2, 3, 0).contains(1, 2, 3, 0));

        // Empty rects are treated as points
        Assert.assertEquals(true, r.contains(1, 2, 0, 0));
        Assert.assertEquals(false, r.contains(0, 1, 0, 0));

        // Rects that touch the inside boundary (completely inside)
        Assert.assertEquals(true, r.contains(1, 2, 1, 1));
        Assert.assertEquals(true, r.contains(3, 2, 1, 1));
        Assert.assertEquals(true, r.contains(1, 5, 1, 1));
        Assert.assertEquals(true, r.contains(3, 5, 1, 1));

        // Rects that touch the outside boundary (completely outside)
        Assert.assertEquals(false, r.contains(0, 2, 1, 1));
        Assert.assertEquals(false, r.contains(4, 2, 1, 1));
        Assert.assertEquals(false, r.contains(1, 1, 1, 1));
        Assert.assertEquals(false, r.contains(1, 6, 1, 1));

        // Partial overlap
        Assert.assertEquals(false, r.contains(1, 3, 4, 1));
        Assert.assertEquals(false, r.contains(0, 3, 4, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void containsRectNegativeWidth() {
        Rect.EMPTY.contains(0, 0, -1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void containsRectNegativeHeight() {
        Rect.EMPTY.contains(0, 0, 0, -1);
    }

    @Test
    public void intersectsRect() {
        Rect r = Rect.of(1, 2, 3, 4);

        // Zero width or zero height rects intersect nothing
        Assert.assertEquals(false, Rect.of(1, 2, 0, 4).intersects(1, 2, 0, 4));
        Assert.assertEquals(false, Rect.of(1, 2, 3, 0).intersects(1, 2, 3, 0));

        // Empty rects
        Assert.assertEquals(false, r.intersects(1, 2, 0, 0));
        Assert.assertEquals(false, r.intersects(0, 1, 0, 0));

        // Rects that touch the inside boundary (completely inside)
        Assert.assertEquals(true, r.intersects(1, 2, 1, 1));
        Assert.assertEquals(true, r.intersects(3, 2, 1, 1));
        Assert.assertEquals(true, r.intersects(1, 5, 1, 1));
        Assert.assertEquals(true, r.intersects(3, 5, 1, 1));

        // Rects that touch the outside boundary (completely outside)
        Assert.assertEquals(false, r.intersects(0, 2, 1, 1));
        Assert.assertEquals(false, r.intersects(4, 2, 1, 1));
        Assert.assertEquals(false, r.intersects(1, 1, 1, 1));
        Assert.assertEquals(false, r.intersects(1, 6, 1, 1));

        // Partial overlap
        Assert.assertEquals(true, r.intersects(1, 3, 4, 1));
        Assert.assertEquals(true, r.intersects(0, 3, 4, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void intersectsRectNegativeWidth() {
        Rect.EMPTY.intersects(0, 0, -1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void intersectsRectNegativeHeight() {
        Rect.EMPTY.intersects(0, 0, 0, -1);
    }

    private static void assertRect(Rect rect, int expectedX, int expectedY, int expectedW, int expectedH) {
        Assert.assertEquals(expectedX, rect.x);
        Assert.assertEquals(expectedY, rect.y);
        Assert.assertEquals(expectedW, rect.w);
        Assert.assertEquals(expectedH, rect.h);
    }

}
