package com.torstensommerfeld.utils.math;

import com.torstensommerfeld.utils.math.matrix.EquationSolution;
import com.torstensommerfeld.utils.math.matrix.Matrix;
import com.torstensommerfeld.utils.math.matrix.MatrixUtil;
import com.torstensommerfeld.utils.math.shapes.Circle;
import com.torstensommerfeld.utils.math.shapes.Ellipse;

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

    /**
     * convenience method to provide the points as arrays
     */
    public static Ellipse getEllipse(double[] p1, double[] p2, double[] p3, double[] p4, double[] p5, Ellipse target) {
        return getEllipse(p1[0], p1[1], p2[0], p2[1], p3[0], p3[1], p4[0], p4[1], p5[0], p5[1], target);
    }

    /*-
     * Solve equation with 6 unknowns using 5 points
     * AX^2 + BXY + CY^2 + DX + EY + F = 0
     * where X and Y are coordinates of the 5 points
     * and A,B,C,D,E,F are the the general equation's coefficients
     * 
     * Because any multiple of the equation describes the same ellipse we can divide the equation by A and still describe the same ellipse
     * X^2 + B'XY + C'Y^2 + D'X + E'Y + F' = 0
     * B'XY + C'Y^2 + D'X + E'Y + F' = -X^2 (*A/A)
     * 
     * This makes A=1 and reduced the number of unknowns to 5
     */
    public static Ellipse getEllipse(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4, double x5, double y5, Ellipse target) {
        Matrix equation = new Matrix(5, 5);
        double[][] m = equation.getM();

        m[0][0] = x1 * y1;
        m[0][1] = y1 * y1;
        m[0][2] = x1;
        m[0][3] = y1;
        m[0][4] = 1;
        m[1][0] = x2 * y2;
        m[1][1] = y2 * y2;
        m[1][2] = x2;
        m[1][3] = y2;
        m[1][4] = 1;
        m[2][0] = x3 * y3;
        m[2][1] = y3 * y3;
        m[2][2] = x3;
        m[2][3] = y3;
        m[2][4] = 1;
        m[3][0] = x4 * y4;
        m[3][1] = y4 * y4;
        m[3][2] = x4;
        m[3][3] = y4;
        m[3][4] = 1;
        m[4][0] = x5 * y5;
        m[4][1] = y5 * y5;
        m[4][2] = x5;
        m[4][3] = y5;
        m[4][4] = 1;

        double[] v1 = new double[] { -x1 * x1, -x2 * x2, -x3 * x3, -x4 * x4, -x5 * x5 };

        EquationSolution result = MatrixUtil.solveGaussianElimination(equation, v1);

        // copy over result
        double[] solution = result.getSolution();
        double A = 1;
        double B = solution[0];
        double C = solution[1];
        double D = solution[2];
        double E = solution[3];
        double F = solution[4];

        // calculate ellipse parameters
        /*-
         * calculating (center) x and (center) y from A,B,C,E and F 
         * 
         * I)  D = -2Ax - 1By
         * II) E = -1Bx - 2Cy
         *
         * I ') I  * 1B <=> 1BD = -2ABx - 1BBy
         * II') II * 2A <=> 2AE = -2ABX - 4BCy
         * 
         * III ) I' - II' <=> 1BD - 2AE = -1BBy + 4BCy
         * III' )             1BD - 2AE = (-1BB + 4BC)y
         * III'')              y = (1BD - 2AE) / (-1BB + 4AC)
         * 
         *  y in I:
         *  I') D = -2Ax - 1By
         *  I'') 2Ax = -1By - D
         *  I''') x = (-1By - D) / (2A)
         */
        double bb4ac = (B * B - 4 * A * C);
        double t = 2 * (A * E * E + C * D * D - B * D * E + bb4ac * F);
        double acbb = Math.sqrt(MathUtil.sqr(A - C) + B * B);
        double a = -Math.sqrt(t * (A + C + acbb)) / bb4ac;
        double b = -Math.sqrt(t * (A + C - acbb)) / bb4ac;
        double y = (B * D - 2 * A * E) / (-B * B + 4 * A * C);
        double x = (-B * y - D) / (2 * A);
        final double rotationsAgnle;
        if (B == 0) {
            rotationsAgnle = A < C ? 0 : Math.PI;
        } else {
            rotationsAgnle = Math.atan((C - A - acbb) / B);
        }

        target.setA(a);
        target.setB(b);
        target.setX(x);
        target.setY(y);
        target.setRotationsAngle(rotationsAgnle);

        return target;

    }

    public static double[] rotate(double[] point, double angle, double[] target) {
        double x = point[0];
        double y = point[1];
        target[0] = x * Math.cos(angle) - y * Math.sin(angle);
        target[1] = y * Math.cos(angle) + x * Math.sin(angle);
        return target;
    }

    public static double[] rotate(double[] point, double[] rotationCenter, double angle, double[] target) {
        double x = point[0] - rotationCenter[0];
        double y = point[1] - rotationCenter[1];
        target[0] = x * Math.cos(angle) - y * Math.sin(angle) + rotationCenter[0];
        target[1] = y * Math.cos(angle) + x * Math.sin(angle) + rotationCenter[1];
        return target;
    }

    public static double[] rotate(double px, double py, double[] rotationCenter, double angle, double[] target) {
        double x = px - rotationCenter[0];
        double y = py - rotationCenter[1];
        target[0] = x * Math.cos(angle) - y * Math.sin(angle) + rotationCenter[0];
        target[1] = y * Math.cos(angle) + x * Math.sin(angle) + rotationCenter[1];
        return target;
    }
}
