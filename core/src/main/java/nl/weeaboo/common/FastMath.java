package nl.weeaboo.common;

public final class FastMath {

	private FastMath() {
	}

	public static float PI = (float)Math.PI;
	public static float TWO_PI = PI + PI;
    public static float HALF_PI = PI * .5f;

    /** Lookup table based drop-in replacement for {@link Math#sin(double)} */
	public static float sin(float s) {
        return fastSin(s * SinLut.fastAngleScale);
	}

    /** Lookup table based drop-in replacement for {@link Math#cos(double)} */
	public static float cos(float s) {
        return fastCos(s * SinLut.fastAngleScale);
	}

	public static float acos(float s) {
        return fastArcCos(s) / SinLut.fastAngleScale;
	}
	public static float asin(float s) {
        return fastArcSin(s) / SinLut.fastAngleScale;
	}

	public static float fastArcTan2(float dy, float dx) {
        float c1 = SinLut.SIZE >> 3;
        float c2 = 3 * c1;

		float absDy = Math.abs(dy);
		if (absDy == 0.0) {
			absDy = 0.0001f;
		}

		float angle;
		if (dx >= 0) {
			float r = (dx - absDy) / (dx + absDy);
            angle = c1 - r * c1;
		} else {
			float r = (dx + absDy) / (absDy - dx);
            angle = c2 - r * c1;
		}

		if (dy < 0) {
            angle = -angle; // Negate if in quadrant 3 or 4
		}

        angle += (SinLut.SIZE >> 2);
		if (angle < 0) {
            angle += SinLut.SIZE;
		}
		return angle;
	}

	public static boolean isPowerOfTwo(int w, int h) {
		return isPowerOfTwo(w) && isPowerOfTwo(h);
	}
	public static boolean isPowerOfTwo(int sz) {
		return sz > 0 && (sz & (sz-1)) == 0;
	}
	public static int toPowerOfTwo(int target) {
        if (target <= 0) {
            throw new IllegalArgumentException("target must be positive");
        }
        if (target > 0x40000000) {
            throw new IllegalArgumentException("No greater power-of-two possible in 32 bits: " + target);
        }

		int cur = (target < 16 ? 1 : 16); //Start with a valid power of two lower than target
		while (cur < target) {
			cur <<= 1; //Double cur until at least as large as target
		}
		return cur;
	}
	public static Dim toPowerOfTwo(int w, int h) {
		return new Dim(toPowerOfTwo(w), toPowerOfTwo(h));
	}

	public static int align(int val, int align) {
		if (!isPowerOfTwo(align)) {
			throw new IllegalArgumentException("Alignment must be a power of two");
		}
		return (val+align-1) & ~(align-1);
	}

	//-------------------------------------------------------------------------
	//--- LUT implementations of trig functions -------------------------------
	//-------------------------------------------------------------------------

	public static float fastSin(int angle) {
        return SinLut.LUT[angle & SinLut.MASK];
	}
	public static float fastCos(int angle) {
        return SinLut.LUT[(angle + SinLut.COS_OFFSET) & SinLut.MASK];
	}

	public static float fastSin(float angle) {
        int a = (angle >= 0 ? (int)(angle) : (int)(angle - 1));
		float prev = fastSin(a);
		float next = fastSin(a + 1);

		float result = prev + (next-prev) * Math.abs(angle - a);
		return result;
	}
	public static float fastCos(float angle) {
        return fastSin(angle + (SinLut.SIZE >> 2));
	}

	private static float ASIN_LUT[];
	private static int ASIN_LUT_SIZE = 1024;

	protected synchronized static void initASinLUT() {
		if (ASIN_LUT != null) {
			return;
		}

		int halfLutSize = ASIN_LUT_SIZE>>1;

		ASIN_LUT = new float[ASIN_LUT_SIZE];
		for (int n = 0; n < ASIN_LUT_SIZE; n++) {
			double d = Math.asin((n - halfLutSize) / (float)halfLutSize);
            ASIN_LUT[n] = (float)(SinLut.fastAngleScale * d);
		}
	}

	public static float fastArcSin(float a) {
		if (ASIN_LUT == null) {
			initASinLUT();
		}

		if (Float.isNaN(a) || a < -1 || a > 1) return Float.NaN;
		if (a == -0f || a == 0f) return a;

		int halfLutSize = ASIN_LUT_SIZE>>1;
		int index = Math.abs(Math.round(halfLutSize + a * halfLutSize));
		return ASIN_LUT[Math.max(0, Math.min(ASIN_LUT_SIZE-1, index))];
	}

	public static float fastArcCos(float a) {
        return (SinLut.SIZE >> 1) - fastArcSin(a);
	}

    /** Inner class used to lazy-initialize the lookup table */
    private static final class SinLut {

        static final int SIZE = 512;
        static final int MASK = 511;
        static final int COS_OFFSET = SIZE >> 2;
        static final float fastAngleScale = SIZE / (TWO_PI);

        static final float LUT[];

        static {
            double s = Math.PI / (SIZE >> 1);

            LUT = new float[SIZE];
            for (int n = 0; n < SIZE; n++) {
                LUT[n] = (float)Math.sin(n * s);
            }
        }

    }
}
