package nl.weeaboo.common;

import org.junit.Assert;
import org.junit.Test;

public class DimTest {

    @Test
    public void createDim() {
        assertDim(Dim.of(1, 2), 1, 2);
    }

    @Test
    public void emptyDim() {
        Assert.assertSame(Dim.EMPTY, Dim.of(0, 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeWidth() {
        Dim.of(-1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeHeight() {
        Dim.of(0, -1);
    }

    @Test
    public void equals() {
        Dim d12 = Dim.of(1, 2);
        Assert.assertEquals(Dim.of(1, 2).hashCode(), d12.hashCode());
        Assert.assertEquals(Dim.of(1, 2), d12);
        Assert.assertFalse(d12.equals(null));

        // Changing any attribute (w,h) causes the dims to no longer be equal
        Assert.assertNotEquals(d12, Dim.of(9, 2));
        Assert.assertNotEquals(d12, Dim.of(1, 9));
    }

    private static void assertDim(Dim dim, int expectedW, int expectedH) {
        Assert.assertEquals(expectedW, dim.w);
        Assert.assertEquals(expectedH, dim.h);
    }

}
