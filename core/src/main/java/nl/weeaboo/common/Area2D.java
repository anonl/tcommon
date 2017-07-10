package nl.weeaboo.common;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Describes a rectangular area with {@code double} coordinates. The area is allowed to be empty, or have negative
 * dimensions.
 *
 * @see Area
 * @see Rect2D
 */
@javax.annotation.concurrent.Immutable
@com.google.errorprone.annotations.Immutable
public final class Area2D implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final Area2D EMPTY = new Area2D(0, 0, 0, 0);

    public final double x;
    public final double y;
    public final double w;
    public final double h;

    private Area2D(double x, double y, double w, double h) {
        Checks.checkRange(x, "x");
        Checks.checkRange(y, "y");
        Checks.checkRange(w, "w");
        Checks.checkRange(h, "h");

        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    /**
     * Factory constructor for creating a new area. For specific values of x/y/w/h a cached instance may be returned.
     */
    public static Area2D of(double x, double y, double w, double h) {
        return new Area2D(x, y, w, h);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new double[] {x, y, w, h});
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Area2D) {
            Area2D dim = (Area2D)obj;
            return x == dim.x && y == dim.y && w == dim.w && h == dim.h;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Area2D(" + x + "," + y + "," + w + "," + h + ")";
    }

    /**
     * Returns a new area that's mirrored in the x-axis and/or y-axis.
     */
    public Area2D flipped(boolean horizontal, boolean vertical) {
        double nx = x;
        double ny = y;
        double nw = w;
        double nh = h;
        if (horizontal) {
            nx += w;
            nw = -nw;
        }
        if (vertical) {
            ny += h;
            nh = -nh;
        }
        return Area2D.of(nx, ny, nw, nh);
    }

}
