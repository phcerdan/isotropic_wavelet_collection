/*
 * Decompiled with CFR 0_114.
 */
package monogenicwavelettoolbox;

import java.awt.Point;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class PointComparable
extends Point
implements Comparable<PointComparable> {
    private static final long serialVersionUID = -5815049019312579150L;

    public PointComparable(int x, int y) {
        super(x, y);
    }

    @Override
    public int compareTo(PointComparable o) {
        if ((double)this.x > o.getX()) {
            return 1;
        }
        if ((double)this.x < o.getX()) {
            return -1;
        }
        if ((double)this.y > o.getY()) {
            return 1;
        }
        if ((double)this.y < o.getY()) {
            return -1;
        }
        return 0;
    }
}

