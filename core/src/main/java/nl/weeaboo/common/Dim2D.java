package nl.weeaboo.common;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Holds a non-negative floating point width/height pair representing a dimension.
 */
@javax.annotation.concurrent.Immutable
@com.google.errorprone.annotations.Immutable
public final class Dim2D implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final Dim2D EMPTY = new Dim2D(0, 0);

    public final double w;
    public final double h;

    private Dim2D(double w, double h) {
        Dim.checkDimensions(w, h);

        this.w = w;
        this.h = h;
    }

    /**
     * Factory constructor for creating a new dimension. For specific values of w/h a cached instance may be returned.
     */
    public static Dim2D of(double w, double h) {
        if (w == 0 && h == 0) {
            return Dim2D.EMPTY;
        }
        return new Dim2D(w, h);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new double[] { w, h });
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Dim2D) {
            Dim2D dim = (Dim2D) obj;
            return w == dim.w && h == dim.h;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Dim2D(" + w + "x" + h + ")";
    }

}
