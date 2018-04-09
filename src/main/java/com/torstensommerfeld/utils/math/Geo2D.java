package com.torstensommerfeld.utils.math;

import com.torstensommerfeld.utils.math.shapes.Circle;

public class Geo2D {

    /**
     * @return distance of point (x1, y1) to point (x2, y2)
     */
    public static double getDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(MathUtil.sqr(x1 - x2) + MathUtil.sqr(y1 - y2));
    }

    /**
     * @return distance of point (x1, y1) to point (x2, y2)
     */
    public static double getDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(MathUtil.sqr(x1 - x2) + MathUtil.sqr(y1 - y2));
    }

    /**
     * Compared to getDistance this method does not take the square root and hence it is faster
     * 
     * @return square of distance of point (x1, y1) to point (x2, y2)
     */
    public static double getDistanceSqr(double x1, double y1, double x2, double y2) {
        return MathUtil.sqr(x1 - x2) + MathUtil.sqr(y1 - y2);
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

    /**
     * This method calculates the parameters of a circle (center, radius) based on 3 points.
     * 
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     * @param target
     * @return
     */
    public static Circle getCircle(int x1, int y1, int x2, int y2, int x3, int y3, Circle target) {
        /*-
         * x^2 + y^2 + D * x + E * y + F = 0
         * 
         * I   ) x1*D + y1*E + F + x1^2 + y1^2 = 0
         * II  ) x2*D + y2*E + F + x2^2 + y2^2 = 0
         * III ) x3*D + y3*E + F + x3^2 + y3^2 = 0
         * 
         * c1 = x1^2 + y1^2
         * c2 = x2^2 + y2^2
         * c3 = x3^2 + y3^2
         * 
         * I   ') x1*D + y1*E + F + c1 = 0
         * II  ') x2*D + y2*E + F + c2 = 0
         * III ') x3*D + y3*E + F + c3 = 0
         * 
         * IV = I - II ) (x1 - x2)D + (y1 - y2)E + c1 - c2 = 0
         *  V = I - III) (x1 - x3)D + (y1 - y3)E + c1 - c3 = 0
         * 
         * d1 = x1 - x2
         * e1 = y1 - y2
         * f1 = c1 - c2
         * 
         * d2 = x1 - x3
         * e2 = y1 - y3
         * f2 = c1 - c3
         * 
         * IV ') d1D + e1E + f1 = 0
         *  V ') d2D + e2E + f2 = 0
         *  
         * VI =  IV * d2 - V * d1:
         *    =  e1*d2*E - e2*d1*E + f1 * d2 - f2 * d1
         *   <=> e1*d2*E - e2*d1*E = f2 * d1 - f1 * d2
         *  E = (f1 * d2 - f2 * d1) / (e2*d1 - e1 * d2)
         *       
         * E in IV')
         *   d1D + e1E + f1 = 0
         *   d1D = -e1E - f1
         *   D   = ()-e1E - f1) / d1;
         *   
         * E and D in I)
         *  
         *  x1*D + y1*E + F + x1^2 + y1^2 = 0
         *  -F = x1*D + y1*E + x1^2 + y1^2
         */
        int c1 = x1 * x1 + y1 * y1;
        int c2 = x2 * x2 + y2 * y2;
        int c3 = x3 * x3 + y3 * y3;

        int d1 = x1 - x2;
        int e1 = y1 - y2;
        int f1 = c1 - c2;

        int d2 = x1 - x3;
        int e2 = y1 - y3;
        int f2 = c1 - c3;

        double E = (f1 * d2 - f2 * d1) / (double) (e2 * d1 - e1 * d2);
        double D = d1 == 0 //
                ? (-e2 * E - f2) / d2 //
                : (-e1 * E - f1) / d1; //

        double F = -(x1 * D + y1 * E + x1 * x1 + y1 * y1);

        double centerX = D / -2;
        double centerY = E / -2;
        target.setX(centerX);
        target.setY(centerY);
        target.setR(Math.sqrt(centerX * centerX + centerY * centerY - F));

        return target;
    }
}
