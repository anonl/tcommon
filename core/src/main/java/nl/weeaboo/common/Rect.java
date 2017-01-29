package nl.weeaboo.common;

import java.io.Serializable;

public final class Rect implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final Rect EMPTY = new Rect(0, 0, 0, 0);

    public final int x;
    public final int y;
    public final int w;
    public final int h;

    private Rect(int x, int y, int w, int h) {
        Dim.checkDimensions(w, h);

        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    /**
     * Factory constructor for creating a new rect. For specific values of x/y/w/h a cached instance may be returned.
     */
    public static Rect of(int x, int y, int w, int h) {
        if (x == 0 && y == 0 && w == 0 && h == 0) {
            return EMPTY;
        }
        return new Rect(x, y, w, h);
    }

    /**
     * Returns a rect translated by {@code (dx, dy)}.
     */
    public Rect translatedCopy(int dx, int dy) {
        return Rect.of(x + dx, y + dy, w, h);
    }

    /**
     * Calculates the bounding rectangle for a collection of rectangles.
     */
    public static Rect combine(Rect... r) {
        if (r.length == 0) {
            return EMPTY;
        } else if (r.length == 1) {
            return r[0];
        }

        int minX = r[0].x;
        int minY = r[0].y;
        int maxX = r[0].x + r[0].w;
        int maxY = r[0].y + r[0].h;
        for (int n = 1; n < r.length; n++) {
            minX = Math.min(minX, r[n].x);
            minY = Math.min(minY, r[n].y);
            maxX = Math.max(maxX, r[n].x + r[n].w);
            maxY = Math.max(maxY, r[n].y + r[n].h);
        }

        return Rect.of(minX, minY, maxX - minX, maxY - minY);
    }

    @Override
    public int hashCode() {
        return ((x << 16) ^ y) ^ ((w << 16) ^ h);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Rect) {
            Rect r = (Rect) obj;
            return x == r.x && y == r.y && w == r.w && h == r.h;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Rect(" + x + ", " + y + ", " + w + ", " + h + ")";
    }

    /**
     * Returns an equivalent {@link Area} instance.
     */
    public Area toArea() {
        return Area.of(x, y, w, h);
    }

    /**
     * Returns an equivalent {@link Area2D} instance.
     */
    public Area2D toArea2D() {
        return Area2D.of(x, y, w, h);
    }

    /**
     * Returns an equivalent {@link Rect2D} instance.
     */
    public Rect2D toRect2D() {
        return Rect2D.of(x, y, w, h);
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
