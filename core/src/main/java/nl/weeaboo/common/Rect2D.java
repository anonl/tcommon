package nl.weeaboo.common;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Rectangle with floating point coordinates and non-negative width/height.
 */
@javax.annotation.concurrent.Immutable
@com.google.errorprone.annotations.Immutable
public final class Rect2D implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final Rect2D EMPTY = new Rect2D(0, 0, 0, 0);

    public final double x;
    public final double y;
    public final double w;
    public final double h;

    private Rect2D(double x, double y, double w, double h) {
        Checks.checkRange(x, "x");
        Checks.checkRange(y, "y");
        Dim.checkDimensions(w, h);

        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    /**
     * Factory constructor for creating a new rect. For specific values of x/y/w/h a cached instance may be returned.
     */
    public static Rect2D of(double x, double y, double w, double h) {
        return new Rect2D(x, y, w, h);
    }

    /**
     * Returns a rect translated by {@code (dx, dy)}.
     */
    public Rect2D translatedCopy(double dx, double dy) {
        return Rect2D.of(x + dx, y + dy, w, h);
    }

    /**
     * Returns the bounding rect for the set of input rectangles.
     *
     * @return The bounding rect, or {@code Rect2D#EMPTY} if the input array is empty.
     */
    public static Rect2D combine(Rect2D... r) {
        if (r.length == 0) {
            return EMPTY;
        }

        double minX = r[0].x;
        double minY = r[0].y;
        double maxX = r[0].x + r[0].w;
        double maxY = r[0].y + r[0].h;
        for (int n = 1; n < r.length; n++) {
            minX = Math.min(minX, r[n].x);
            minY = Math.min(minY, r[n].y);
            maxX = Math.max(maxX, r[n].x + r[n].w);
            maxY = Math.max(maxY, r[n].y + r[n].h);
        }

        return Rect2D.of(minX, minY, maxX - minX, maxY - minY);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new double[] {x, y, w, h});
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Rect2D) {
            Rect2D dim = (Rect2D)obj;
            return x == dim.x && y == dim.y && w == dim.w && h == dim.h;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Rect2D(" + x + ", " + y + ", " + w + ", " + h + ")";
    }

    /**
     * Returns an equivalent {@link Area2D} instance.
     */
    public Area2D toArea2D() {
        return Area2D.of(x, y, w, h);
    }

    /**
     * @return {@code true} if the given point lies inside this rectangle or on its boundary.
     */
    public boolean contains(double px, double py) {
        if (w <= 0 || h <= 0) {
            return false; // Special case: empty rect contains nothing
        }
        return px >= x && px <= x + w && py >= y && py <= y + h;
    }

    /**
     * Checks if this rect completely contains the rectangle described by {@code (ax, ay, aw, ah)}.
     */
    public boolean contains(double ax, double ay, double aw, double ah) {
        return contains(ax, ay) && contains(ax + aw, ay + ah);
    }

    /**
     * Checks if this rect is intersected by the rectangle described by {@code (ax, ay, aw, ah)}.
     */
    public boolean intersects(double ax, double ay, double aw, double ah) {
        if (w <= 0 || h <= 0) {
            return false; // Special case: empty rects intersect nothing
        }

        double x0 = Math.min(ax, ax + aw);
        double x1 = Math.max(ax, ax + aw);
        double y0 = Math.min(ay, ay + ah);
        double y1 = Math.max(ay, ay + ah);

        return x1 > x && y1 > y && x0 < x + w && y0 < y + h;
    }

}
