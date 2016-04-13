package nl.weeaboo.common;

import org.junit.Assert;
import org.junit.Test;

public class FastMathPerfTest {

    @Test
    public void sinCosPerformance() {
        long timeA = 0;
        long timeB = 0;

        final int warmUpRuns = 10;
        for (int run = 1; run < 100; run++) {
            long t0 = System.nanoTime();
            fastCosPerf();
            long t1 = System.nanoTime();
            cosPerf();
            long t2 = System.nanoTime();

            if (run > warmUpRuns) {
                timeA += (t1 - t0);
                timeB += (t2 - t1);
            }
        }

        // Check that 'fast' version is at least twice as fast
        System.out.println("fastCos: " + toMillis(timeA) + "ms");
        System.out.println("Math.cos: " + toMillis(timeB) + "ms");
        Assert.assertTrue("A=" + toMillis(timeA) + "ms, B=" + toMillis(timeB) + "ms", 2 * timeA <= timeB);
    }


    private static double toMillis(long nanos) {
        return nanos / 1000000.0;
    }

    private float fastCosPerf() {
        float total1 = 0f;
        for (float f = 0f; f < FastMath.PI; f += .0001f) {
            total1 += FastMath.cos(f);
        }
        return total1;
    }

    private double cosPerf() {
        double total2 = 0f;
        for (double f = 0f; f < Math.PI; f += .0001) {
            total2 += Math.cos(f);
        }
        return total2;
    }
}
