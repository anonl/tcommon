package nl.weeaboo.common;

/**
 * Represents a version number in (numeric-only) x.y.z format.
 */
@javax.annotation.concurrent.Immutable
// @com.google.errorprone.annotations.Immutable // Requires an immutable int collection type
public final class VersionNumber implements Comparable<VersionNumber> {

    private final int[] versionParts;

    /**
     * @param major First version part.
     * @param minor Second version part.
     */
    public VersionNumber(int major, int minor) {
        versionParts = new int[] {major, minor};
    }

    private VersionNumber(int... parts) {
        versionParts = parts.clone();
    }

    /**
     * @throws NumberFormatException If the string isn't parseable as a version number.
     */
    public static VersionNumber parse(String str) throws NumberFormatException {
        String[] stringParts = str.split("\\.", -1);
        int[] intParts = new int[stringParts.length];
        for (int n = 0; n < stringParts.length; n++) {
            intParts[n] = Integer.parseInt(stringParts[n]);
        }
        return new VersionNumber(intParts);
    }

    /** Visible for testing */
    int[] getParts() {
        return versionParts.clone();
    }

    /**
     * @throws NumberFormatException If the string isn't parseable as a version number.
     */
    public int compareTo(String str) throws NumberFormatException {
        return compareTo(VersionNumber.parse(str));
    }

    @Override
    public int compareTo(VersionNumber other) {
        final int aLen = versionParts.length;
        final int bLen = other.versionParts.length;
        for (int n = 0; n < Math.max(aLen, bLen); n++) {
            // Assume 'missing' parts are zeroes
            int a = (n < aLen ? versionParts[n] : 0);
            int b = (n < bLen ? other.versionParts[n] : 0);
            if (a != b) {
                return (a < b ? -1 : 1);
            }
        }
        return 0;
    }

    @Override
    public int hashCode() {
        /*
         * Hash code of 1.0.0 and 1.0 must be the same for hashCode() to match the implementation of equals().
         * To achieve this, hash the parts in reverse.
         */
        int hash = 0;
        for (int n = versionParts.length - 1; n >= 0; n--) {
            hash = 31 * hash + versionParts[n];
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof VersionNumber) {
            return compareTo((VersionNumber)obj) == 0;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int part : versionParts) {
            if (sb.length() > 0) {
                sb.append('.');
            }
            sb.append(part);
        }
        return sb.toString();
    }

}
