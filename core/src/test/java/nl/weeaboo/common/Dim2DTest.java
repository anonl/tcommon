package nl.weeaboo.common;

import org.junit.Assert;
import org.junit.Test;

public class Dim2DTest {

    @Test
    public void createDim() {
        assertDim(Dim2D.of(1, 2), 1, 2);
    }

    @Test
    public void emptyDim() {
        Assert.assertSame(Dim2D.EMPTY, Dim2D.of(0, 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeWidth() {
        Dim2D.of(-1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeHeight() {
        Dim2D.of(0, -1);
    }

    @Test
    public void equals() {
        Dim2D d12 = Dim2D.of(1, 2);
        Assert.assertEquals(Dim2D.of(1, 2).hashCode(), d12.hashCode());
        Assert.assertEquals(Dim2D.of(1, 2), d12);
        Assert.assertFalse(d12.equals(null));

        // Changing any attribute (w,h) causes the dims to no longer be equal
        Assert.assertNotEquals(d12, Dim2D.of(9, 2));
        Assert.assertNotEquals(d12, Dim2D.of(1, 9));

        // Dim and Dim2D are never equal
        Assert.assertNotEquals(d12, Dim.of(1, 2));
        Assert.assertNotEquals(Dim.of(1, 2), Dim.of(1, 2).toDim2D());
    }

    private static void assertDim(Dim2D dim, double expectedW, double expectedH) {
        Assert.assertEquals(expectedW, dim.w, 0.0);
        Assert.assertEquals(expectedH, dim.h, 0.0);
    }

}
