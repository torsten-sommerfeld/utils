package com.torstensommerfeld.utils.math;

public class Geo2D {

    /**
     * @return distance of point (x1, y1) to point (x2, y2)
     */
    public static double getDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(MathUtil.sqr(x1 - x2) + MathUtil.sqr(y1 - y2));
    }

    /**
     * Compared to getDistance this method does not take the square root and hence it is faster
     * 
     * @return square of distance of point (x1, y1) to point (x2, y2)
     */
    public static double getDistanceSqr(int x1, int y1, int x2, int y2) {
        return MathUtil.sqr(x1 - x2) + MathUtil.sqr(y1 - y2);
    }

    /**
     * 
     * @return distance minimum distance of point (x, y) to line defined by the points (x1, y2) and (x2, y2)
     */
    public static double distanceOfPointToLine(int x, int y, int x1, int y1, int x2, int y2) {
        return Math.abs((y2 - y1) * x - (x2 - x1) * y + x2 * y1 - y2 * x1) / getDistance(x1, y1, x2, y2);
    }

    /**
     * 
     * @return distance minimum distance of point (x, y) to line defined by the points (x1, y2) and (x2, y2)
     */
    public static double distanceOfPointToLine(int x, int y, int x1, int y1, int x2, int y2, double distanceP1P2) {
        return Math.abs((y2 - y1) * x - (x2 - x1) * y + x2 * y1 - y2 * x1) / distanceP1P2;
    }
}
