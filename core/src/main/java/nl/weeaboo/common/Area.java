package nl.weeaboo.common;

import java.io.Serializable;

/**
 * Describes a rectangular area with integer coordinates. The area is allowed to be empty, or have negative dimensions.
 *
 * @see Rect
 * @see Area2D
 */
@javax.annotation.concurrent.Immutable
@com.google.errorprone.annotations.Immutable
public final class Area implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final Area EMPTY = new Area(0, 0, 0, 0);

    public final int x;
    public final int y;
    public final int w;
    public final int h;

    private Area(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    /**
     * Factory constructor for creating a new area. For specific values of x/y/w/h a cached instance may be returned.
     */
    public static Area of(int x, int y, int w, int h) {
        if (x == 0 && y == 0 && w == 0 && h == 0) {
            return EMPTY;
        }
        return new Area(x, y, w, h);
    }

    @Override
    public int hashCode() {
        return ((x << 16) ^ y) ^ ((w << 16) ^ h);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Area) {
            Area a = (Area)obj;
            return x == a.x && y == a.y && w == a.w && h == a.h;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Area(" + x + ", " + y + ", " + w + ", " + h + ")";
    }

    /**
     * Returns an equivalent {@link Area2D} instance.
     */
    public Area2D toArea2D() {
        return Area2D.of(x, y, w, h);
    }

    /**
     * Returns a new area that's mirrored in the x-axis and/or y-axis.
     */
    public Area flipped(boolean horizontal, boolean vertical) {
        int nx = x;
        int nw = w;
        int nh = h;
        int ny = y;
        if (horizontal) {
            nx += w;
            nw = -nw;
        }
        if (vertical) {
            ny += h;
            nh = -nh;
        }
        return Area.of(nx, ny, nw, nh);
    }

}
