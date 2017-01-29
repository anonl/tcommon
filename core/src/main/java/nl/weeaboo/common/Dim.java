package nl.weeaboo.common;

import java.io.Serializable;

/**
 * Holds a non-negative integral width/height pair.
 */
public final class Dim implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final Dim EMPTY = new Dim(0, 0);

    public final int w;
    public final int h;

    private Dim(int w, int h) {
        if (w < 0 || h < 0) {
            throw new IllegalArgumentException("Dimensions must be >= 0, w=" + w + ", h=" + h);
        }

        this.w = w;
        this.h = h;
    }

    public static Dim of(int w, int h) {
        if (w == 0 && h == 0) {
            return Dim.EMPTY;
        }
        return new Dim(w, h);
    }

    @Override
    public int hashCode() {
        return (w << 16) ^ h;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Dim) {
            Dim dim = (Dim) obj;
            return w == dim.w && h == dim.h;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Dim(" + w + "x" + h + ")";
    }

    public Dim2D toDim2D() {
        return Dim2D.of(w, h);
    }

    static void checkDimensions(int w, int h) {
        if (w < 0 || h < 0) {
            throw new IllegalArgumentException("Dimensions must be >= 0, w=" + w + ", h=" + h);
        }
    }

    static void checkDimensions(double w, double h) {
        if (w < 0 || Double.isInfinite(w) || Double.isNaN(w) || h < 0 || Double.isInfinite(h)
                || Double.isNaN(h)) {
            throw new IllegalArgumentException("Dimensions must be >= 0 and finite, w=" + w + ", h=" + h);
        }
    }

}
