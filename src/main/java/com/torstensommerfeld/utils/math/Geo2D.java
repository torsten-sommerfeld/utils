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

    public static double[] getOthogonalVector(double dx, double dy, double[] target) {
        target[0] = -dy;
        target[1] = dx;
        return target;
    }

    /**
     * calculates the coordinates of a point which is part of the given line defined by (x1,y1) and (x2,y2) and is closest to the given point (x,y)
     */
    public static double[] getClosestPointOnLineToPoint(double x, double y, double x1, double y1, double x2, double y2, double[] target) {
        /*-
         *   x2-x1      x1    y1 - y2      x
         *  (y2-y1) v + y1 = (x2 - y1) w + y
         *  
         *  dx = x2 - x1
         *  dy = y2 - y1
         * 
         * I )   v*dx + x1 = -w*dy + x | * dy
         * II)   v*dy + y1 =  w*dx + y | * dx
         * 
         * I ')  v*dx*dy + x1*dy = -w*dy*dy + x*dy
         * II')  v*dy*dx + y1*dx =  w*dx*dx + y*dx
         * 
         * III: I' - II') x1*dy - y1*dx = -w*dy*dy + x*dy - w*dx*dx - y*dx
         * 
         * III'   )  x1*dy - y1*dx = -w*dy*dy - w*dx*dx + x*dy - y*dx
         * III''  )  x1*dy - y1*dx = -w*(dy*dy + dx*dx) + x*dy - y*dx
         * III''' ) w*(dy*dy + dx*dx) = x*dy -y*dx - x1*dy + y1*dx
         * III'''') w = (-y*dx - x1*dy + y1*dx) / (dy*dy + dx*dx)
         */

        double dx = x2 - x1;
        double dy = y2 - y1;
        double w = (x * dy - y * dx - x1 * dy + y1 * dx) / (dy * dy + dx * dx);

        target[0] = -w * dy + x;
        target[1] = w * dx + y;

        return target;
    }

    /**
     * This method calculates and returns f for the given equation:
     * 
     * x1 + (x2-x1)f = x2 - (x2-x1)(1-f) = x
     * 
     * y1 + (y2-y1)f = y2 - (y2-y1)(1-f) = y
     * 
     * f = (y - y1) / (y2-y1) and f = (x - x1) / (x2-x1)
     * 
     * The method assumes that point (x,y) is between (x1,y1) and (x2,y2)
     */

    public static double getRelativeLocationOfPointBetween2Points(double x, double y, double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;

        return dx == 0 //
                ? (y - y1) / dy //
                : (x - x1) / dx;

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

    /**
     * This method can be used if the center and 3 additional points are known
     * 
     * The regular ellipse equation degrates to (D and E are defined by the center):
     * 
     * B*xi*yi + C*yi*yi + (-2*xc - B*yc)*xi + (-B*xc - 2 * C * yc)*yi + F = -xi*xi
     * 
     * <=>
     * 
     * B*(xi*yi-yc*xi-xc*yi) + C*(yi*yi - 2*yc*yi) + F = -xi*xi + 2*xc*xi
     * 
     * @param target
     * @return
     */
    public static Ellipse getEllipseWithGivenCenter(double centerX, double centerY, double x1, double y1, double x2, double y2, double x3, double y3, Ellipse target) {
        Matrix equation = new Matrix(3, 3);
        double[][] m = equation.getM();

        m[0][0] = x1 * y1 - centerY * x1 - centerX * y1;
        m[0][1] = y1 * y1 - 2 * centerY * y1;
        m[0][2] = 1;
        m[1][0] = x2 * y2 - centerY * x2 - centerX * y2;
        m[1][1] = y2 * y2 - 2 * centerY * y2;
        m[1][2] = 1;
        m[2][0] = x3 * y3 - centerY * x3 - centerX * y3;
        m[2][1] = y3 * y3 - 2 * centerY * y3;
        m[2][2] = 1;

        double[] v1 = new double[] { -x1 * x1 + 2 * centerX * x1, -x2 * x2 + 2 * centerX * x2, -x3 * x3 + 2 * centerX * x3 };

        EquationSolution result = MatrixUtil.solveGaussianElimination(equation, v1);

        // copy over result
        double[] solution = result.getSolution();
        final double A = 1;
        final double B = solution[0];
        final double C = solution[1];
        final double D = -2 * A * centerX - B * centerY;
        final double E = -B * centerX - 2 * C * centerY;
        final double F = solution[2];

        double bb4ac = (B * B - 4 * A * C);
        double t = 2 * (A * E * E + C * D * D - B * D * E + bb4ac * F);
        double acbb = Math.sqrt(MathUtil.sqr(A - C) + B * B);
        double a = -Math.sqrt(t * (A + C + acbb)) / bb4ac;
        double b = -Math.sqrt(t * (A + C - acbb)) / bb4ac;
        final double rotationsAgnle;
        if (B == 0) {
            rotationsAgnle = A < C ? 0 : Math.PI;
        } else {
            rotationsAgnle = Math.atan((C - A - acbb) / B);
        }

        target.setA(a);
        target.setB(b);
        target.setX(centerX);
        target.setY(centerY);
        target.setRotationsAngle(rotationsAgnle);

        return target;

    }

    /**
     * The quadratic equation has the form of ax^2 + bx + c = y
     * 
     * This method finds the parameter a, b and c fitting the 3 given points
     * 
     * Note: This method calculates it manually. Alternatively we cold solve the linear equation system with 3 unknowns. It gives the same results but would be slower.
     */
    public static double[] getQuadraticEquationParameters(double x1, double y1, double x2, double y2, double x3, double y3, double[] target) {
        /*-
         * I  ) ax1*x1 + b*x1 + c = y1
         * II ) ax2*x2 + b*x2 + c = y2
         * III) ax3*x3 + b*x3 + c = y3
         * 
         * IV: I  - III) a(x1*x1 - x3*x3) + b(x1 - x3) = y1 - y3
         * V:  II - III) a(x2*x2 - x3*x3) + b(x2 - x3) = y2 - y3
         * 
         * xx13 = x1*x1 - x3*x3
         * x13 = x1 - x3
         * xx23 = x2*x2 - x3*x3
         * x23 = x2 - x3
         * y13 = y1 - y3
         * y23 = y2 - y3
         * 
         * IV' ) a*xx13 + b*x13 = y13  | * xx23
         * V'  ) a*xx23 + b*x23 = y23  | * xx13
         * 
         * IV'') a*xx13 * xx23 + b*x13 * xx23 = y13 * xx23
         * V'' ) a*xx23 * xx13 + b*x23 * xx13 = y23 * xx13
         * 
         * VI: IV'' - V'') b*x13 * xx23 - b*x23 * xx13 = y13 * xx23 - y23 * xx13
         * VI' ) b(x13 * xx23 - x23 * xx13) = y13 * xx23 - y23 * xx13
         * 
         * x' = x13 * xx23 - x23 * xx13
         * y' = y13 * xx23 - y23 * xx13
         * 
         * VI'' ) b * x' = y'
         * VI''') b = 'y / x'
         * 
         * VII: b in IV') a*xx13 + b*x13 = y13
         * VII') a*xx13 = y13 - b*x13
         *       a = (y13 - b*x13) / xx13;
         * 
         * VII: b in V') a*xx23 + b*x23 = y23
         * VII') a*xx23 = y13 - b*x23
         *       a = (y23 - b*x23) / xx23;
         * 
         */
        double xx13 = x1 * x1 - x3 * x3;
        double x13 = x1 - x3;
        double xx23 = x2 * x2 - x3 * x3;
        double x23 = x2 - x3;
        double y13 = y1 - y3;
        double y23 = y2 - y3;

        double dx = x13 * xx23 - x23 * xx13;
        double dy = y13 * xx23 - y23 * xx13;

        double b = dy / dx;
        double a = xx13 == 0 ? (y23 - b * x23) / xx23 : (y13 - b * x13) / xx13;
        double c = y1 - a * x1 * x1 - b * x1;

        target[0] = a;
        target[1] = b;
        target[2] = c;

        return target;

    }

    /**
     * returns the solution of a*x^2 + b*x + c = 0
     * 
     * returns [NaN, NaN] if there is no solution
     * 
     * returns [x, NaN] if there is only a double solution
     * 
     * returns [x1, x2] if there are two solutions
     * 
     */
    public static double[] solveQuadraticEquation(double a, double b, double c, double[] target) {
        double p = b / a;
        double q = c / a;

        double discriminant = p * p - 4 * q;
        if (NumberUtil.isZero(discriminant)) {
            target[0] = p / -2;
            target[1] = Double.NaN;
        } else if (discriminant < 0) {
            target[0] = Double.NaN;
            target[1] = Double.NaN;
        } else {
            double root = Math.sqrt(discriminant);
            target[0] = p / -2 + root;
            target[1] = p / -2 - root;
        }

        return target;

    }

    /**
     * Returns the vertex (apex / scheitelpunkt) [x, y] of the quadratic equation ax^2 + bx + c = y
     * 
     * @return
     */
    public static double[] getVertexPointForQuadraticEquation(double a, double b, double c, double[] target) {
        double x = -b / (2 * a);
        double y = a * x * x + b * x + c;
        target[0] = x;
        target[1] = y;
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
