package nl.weeaboo.common;

import java.util.Random;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FastMathTrigTest {

    private static final float ACCURACY = 0.0001f;
    private static final float ASIN_ACCURACY = 0.01f;

    private Random random;

    @Before
    public void before() {
        random = new Random(12345);
    }

    /** Test LUT-based sin and cos functions */
    @Test
    public void sinCos() {
        // Test some interesting values
        assertSinCos(0);
        assertSinCos(0.001f);
        assertSinCos(0.5f * FastMath.PI);
        assertSinCos(1.0f * FastMath.PI);
        assertSinCos(2.0f * FastMath.PI);
        assertSinCos(3.0f * FastMath.PI);
        assertSinCos(-1.0f * FastMath.PI);
        assertSinCos(-2.0f * FastMath.PI);

        // Test random values
        for (int n = 0; n < 1000; n++) {
            assertSinCos(random.nextFloat() * FastMath.TWO_PI);
        }
    }

    private static void assertSinCos(float x) {
        Assert.assertEquals("sin(" + x + ")", Math.sin(x), FastMath.sin(x), ACCURACY);
        Assert.assertEquals("cos(" + x + ")", Math.cos(x), FastMath.cos(x), ACCURACY);
    }

    @Test
    public void arcSinCos() {
        // Test some interesting values
        assertArcSinCos(0);
        assertArcSinCos(0.001f);
        assertArcSinCos(0.5f);
        assertArcSinCos(1.0f);
        assertArcSinCos(2.0f);
        assertArcSinCos(-0.5f);
        assertArcSinCos(-1.0f);
        assertArcSinCos(-2.0f);

        // Test random values
        for (int n = 0; n < 1000; n++) {
            assertArcSinCos(random.nextFloat() * 2f - 1f);
        }
    }

    private void assertArcSinCos(float x) {
        Assert.assertEquals("asin(" + x + ")", Math.asin(x), FastMath.asin(x), ASIN_ACCURACY);
        Assert.assertEquals("acos(" + x + ")", Math.acos(x), FastMath.acos(x), ASIN_ACCURACY);
    }

}
