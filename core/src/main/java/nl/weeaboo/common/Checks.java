package nl.weeaboo.common;

/**
 * Runtime assertions.
 */
public final class Checks {

    private Checks() {
    }

    private static String nameString(String name) {
        return (name != null ? name : "<undefined>");
    }

    /**
     * @throws IllegalArgumentException if the supplied value is {@code null}.
     * @see #checkNotNull(Object, String)
     */
    public static <T> T checkNotNull(T val) throws IllegalArgumentException {
        return checkNotNull(val, null);
    }

    /**
     * @throws IllegalArgumentException if the supplied value is {@code null}.
     */
    public static <T> T checkNotNull(T val, String name) throws IllegalArgumentException {
        if (val == null) {
            throw new IllegalArgumentException("Invalid value for " + nameString(name) + ": null");
        }
        return val;
    }

    /**
     * @throws IllegalArgumentException if the supplied boolean value is {@code false}.
     */
    public static void checkArgument(boolean condition, String errorMessage) throws IllegalArgumentException {
        if (!condition) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    /**
     * @throws IllegalStateException if the supplied boolean value is {@code false}.
     */
    public static void checkState(boolean condition, String errorMessage) throws IllegalStateException {
        if (!condition) {
            throw new IllegalStateException(errorMessage);
        }
    }

    /**
     * Checks if {@code val >= min}.
     *
     * @throws IllegalArgumentException if the supplied value is less than the specified miniumum value
     */
    public static int checkRange(int val, String name, int min) throws IllegalArgumentException {
        return checkRange(val, name, min, Integer.MAX_VALUE);
    }

    /**
     * Checks if {@code val >= min && val <= max}.
     *
     * @throws IllegalArgumentException if the supplied value is less than the specified miniumum value or greater than
     *         the specified maximum value.
     */
    public static int checkRange(int val, String name, int min, int max) throws IllegalArgumentException {
        if (val < min || val > max) {
            throw new IllegalArgumentException("Invalid value for " + nameString(name) + ": " + val);
        }
        return val;
    }

    /**
     * Checks if {@code val} is finite.
     *
     * @throws IllegalArgumentException if the supplied value is not finite.
     */
    public static double checkRange(double val, String name) throws IllegalArgumentException {
        return checkRange(val, name, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    /**
     * Checks if {@code val >= min && isFinite(val)}.
     *
     * @throws IllegalArgumentException if the supplied value is less than the specified minimum value or not finite.
     */
    public static double checkRange(double val, String name, double min) throws IllegalArgumentException {
        return checkRange(val, name, min, Double.POSITIVE_INFINITY);
    }

    /**
     * Checks if {@code val >= min && val <= max && isFinite(val)}.
     *
     * @throws IllegalArgumentException if the supplied value is:
     *         <ul>
     *           <li>less than the specified minimum value
     *           <li>greater than the specified maximum value
     *           <li>not finite
     *         </ul>
     */
    public static double checkRange(double val, String name, double min, double max) throws IllegalArgumentException {
        if (Double.isNaN(val) || Double.isInfinite(val) || !(val >= min && val <= max)) {
            throw new IllegalArgumentException("Invalid value for " + nameString(name) + ": " + val);
        }
        return val;
    }
}

