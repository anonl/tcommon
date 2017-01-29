package nl.weeaboo.common;

import org.junit.Assert;
import org.junit.Test;

public class RectTest {

    @Test
    public void createRect() {
        assertRect(Rect.of(1, 2, 3, 4), 1, 2, 3, 4);
    }

    /**
     * Attempting to create an empty rect just returns the shared {@link Rect#EMPTY} instance.
     */
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
        assertRectContains(false, Rect.of(1, 2, 0, 4), 1, 2, 0, 4);
        assertRectContains(false, Rect.of(1, 2, 3, 0), 1, 2, 3, 0);

        // Empty rects are treated as points
        assertRectContains(true, r, 1, 2, 0, 0);
        assertRectContains(false, r, 0, 1, 0, 0);

        // Rects that touch the inside boundary (completely inside)
        assertRectContains(true, r, 1, 2, 1, 1);
        assertRectContains(true, r, 3, 2, 1, 1);
        assertRectContains(true, r, 1, 5, 1, 1);
        assertRectContains(true, r, 3, 5, 1, 1);

        // Rects that touch the outside boundary (completely outside)
        assertRectContains(false, r, 0, 2, 1, 1);
        assertRectContains(false, r, 4, 2, 1, 1);
        assertRectContains(false, r, 1, 1, 1, 1);
        assertRectContains(false, r, 1, 6, 1, 1);

        // Partial overlap
        assertRectContains(false, r, 1, 3, 4, 1);
        assertRectContains(false, r, 0, 3, 4, 1);
    }

    private void assertRectContains(boolean expected, Rect rect, int x, int y, int w, int h) {
        Assert.assertEquals(expected, rect.contains(x, y, w, h));

        // Mirrored in the x-axis
        Assert.assertEquals(expected, rect.contains(x + w, y, -w, h));

        // Mirrored in the y-axis
        Assert.assertEquals(expected, rect.contains(x, y + h, w, -h));

        // Mirrored both the x-axis and y-axis
        Assert.assertEquals(expected, rect.contains(x + w, y + h, -w, -h));
    }

    @Test
    public void intersectsRect() {
        Rect r = Rect.of(1, 2, 3, 4);

        // Zero width or zero height rects intersect nothing
        assertRectIntersects(false, Rect.of(1, 2, 0, 4), 1, 2, 0, 4);
        assertRectIntersects(false, Rect.of(1, 2, 3, 0), 1, 2, 3, 0);

        // Empty rects
        assertRectIntersects(false, r, 1, 2, 0, 0);
        assertRectIntersects(false, r, 0, 1, 0, 0);

        // Rects that touch the inside boundary (completely inside)
        assertRectIntersects(true, r, 1, 2, 1, 1);
        assertRectIntersects(true, r, 3, 2, 1, 1);
        assertRectIntersects(true, r, 1, 5, 1, 1);
        assertRectIntersects(true, r, 3, 5, 1, 1);

        // Rects that touch the outside boundary (completely outside)
        assertRectIntersects(false, r, 0, 2, 1, 1);
        assertRectIntersects(false, r, 4, 2, 1, 1);
        assertRectIntersects(false, r, 1, 1, 1, 1);
        assertRectIntersects(false, r, 1, 6, 1, 1);

        // Partial overlap
        assertRectIntersects(true, r, 1, 3, 4, 1);
        assertRectIntersects(true, r, 0, 3, 4, 1);
    }

    private void assertRectIntersects(boolean expected, Rect rect, int x, int y, int w, int h) {
        Assert.assertEquals(expected, rect.intersects(x, y, w, h));

        // Mirrored in the x-axis
        Assert.assertEquals(expected, rect.intersects(x + w, y, -w, h));

        // Mirrored in the y-axis
        Assert.assertEquals(expected, rect.intersects(x, y + h, w, -h));

        // Mirrored both the x-axis and y-axis
        Assert.assertEquals(expected, rect.intersects(x + w, y + h, -w, -h));
    }

    @Test
    public void testToString() {
        Assert.assertEquals("Rect(1, 2, 3, 4)", Rect.of(1, 2, 3, 4).toString());
    }

    private static void assertRect(Rect rect, int expectedX, int expectedY, int expectedW, int expectedH) {
        Assert.assertEquals(expectedX, rect.x);
        Assert.assertEquals(expectedY, rect.y);
        Assert.assertEquals(expectedW, rect.w);
        Assert.assertEquals(expectedH, rect.h);
    }

}
