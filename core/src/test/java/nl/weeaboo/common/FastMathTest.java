package nl.weeaboo.common;

import java.util.Random;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FastMathTest {

    private static final float ACCURACY = 0.0001f;

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

}
