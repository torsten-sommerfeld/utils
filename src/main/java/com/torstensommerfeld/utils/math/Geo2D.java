package com.torstensommerfeld.utils.math;

import com.torstensommerfeld.utils.math.matrix.EquationSolution;
import com.torstensommerfeld.utils.math.matrix.Matrix;
import com.torstensommerfeld.utils.math.matrix.MatrixUtil;
import com.torstensommerfeld.utils.math.model.Line;
import com.torstensommerfeld.utils.math.shapes.Circle;
import com.torstensommerfeld.utils.math.shapes.CubicFunctionParameters;
import com.torstensommerfeld.utils.math.shapes.Ellipse;
import com.torstensommerfeld.utils.math.shapes.QuadraticFunctionParameters;

/**
 * This class contains a set of methods to perform various calculations to solve problems within 2D domain
 * 
 * @author torsten
 *
 */
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
         * III'   ) x1*dy - y1*dx = -w*dy*dy - w*dx*dx + x*dy - y*dx
         * III''  ) x1*dy - y1*dx = -w*(dy*dy + dx*dx) + x*dy - y*dx
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
        final double rotationsAngle;
        if (B == 0) {
            rotationsAngle = A < C ? 0 : Math.PI;
        } else {
            rotationsAngle = Math.atan((C - A - acbb) / B);
        }

        target.setA(a);
        target.setB(b);
        target.setX(x);
        target.setY(y);
        target.setRotationsAngle(rotationsAngle);

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
    public static QuadraticFunctionParameters getQuadraticFunctionParameters(double x1, double y1, double x2, double y2, double x3, double y3, QuadraticFunctionParameters target) {
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

        target.setA(a);
        target.setB(b);
        target.setC(c);

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

    /**
     * The qubic equation has the form of ax^3 + bx^2 + cx + d = y
     * 
     * This method finds the parameter a, b, c and d fitting the 4 given points
     * 
     * 
     */
    public static CubicFunctionParameters getCubicFunctionParameters(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4, CubicFunctionParameters target) {
        /*-
         * I  ) ax1*x1*x1 + bx1*x1 + cx1 + d = y1
         * II ) ax2*x2*x2 + bx2*x2 + cx2 + d = y2
         * III) ax3*x3*x3 + bx3*x3 + cx3 + d = y3
         * IV ) ax4*x4*x4 + bx4*x4 + cx4 + d = y4
         */
        Matrix matrix = new Matrix(4, 4);
        double[][] m = matrix.getM();
        m[0][0] = x1 * x1 * x1;
        m[0][1] = x1 * x1;
        m[0][2] = x1;
        m[0][3] = 1;
        m[1][0] = x2 * x2 * x2;
        m[1][1] = x2 * x2;
        m[1][2] = x2;
        m[1][3] = 1;
        m[2][0] = x3 * x3 * x3;
        m[2][1] = x3 * x3;
        m[2][2] = x3;
        m[2][3] = 1;
        m[3][0] = x4 * x4 * x4;
        m[3][1] = x4 * x4;
        m[3][2] = x4;
        m[3][3] = 1;

        double[] result = MatrixUtil.solve(matrix, new double[] { y1, y2, y3, y4 }, new double[4]);

        target.setA(result[0]);
        target.setB(result[1]);
        target.setC(result[2]);
        target.setD(result[3]);

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

    /**
     * This method returns the angle between vector p11p12 and vector p21p22. The angle is guaranteed to be between 0 and 2PI
     */
    public static double getAngleBetweenVectors(double p1x1, double p1y1, double p1x2, double p1y2, double p2x1, double p2y1, double p2x2, double p2y2) {
        double p1dx = p1x2 - p1x1;
        double p1dy = p1y2 - p1y1;
        double p2dx = p2x2 - p2x1;
        double p2dy = p2y2 - p2y1;

        return normalizeAngle(Math.atan2(p2dy, p2dx) - Math.atan2(p1dy, p1dx));

    }

    /**
     * This methods returns an angle between 0 and 2PI which equivalent to the given angle angle
     * 
     * @param d
     * @return
     */
    public static double normalizeAngle(double angle) {
        double normalizedAngle = angle % NumberUtil.ANGLE_360;
        return normalizedAngle < 0 ? normalizedAngle + NumberUtil.ANGLE_360 : normalizedAngle;
    }

    /**
     * This method returns true if the vector p21p22 is bend clockwise when connected to the end to p11p12. This is only true in a screen / image coordinate system where the origin is in the left top corner.
     * 
     * NOTE: In an euclidean coordinate system (origin in the left bottom corner) this method would return true if both vectors are orientated anti-clockwise.
     */

    public static boolean isClockwise(double p1x1, double p1y1, double p1x2, double p1y2, double p2x1, double p2y1, double p2x2, double p2y2) {
        double p1dx = p1x2 - p1x1;
        double p1dy = p1y2 - p1y1;
        double p2dx = p2x2 - p2x1;
        double p2dy = p2y2 - p2y1;
        double c = p1dx * p2dy - p1dy * p2dx;

        return c >= 0;
    }

    /**
     * This method returns true if the 3 points are orientated clockwise (screen coordinate system)
     * 
     * (B - A) x (C - A) > 0
     * 
     * U x V = Ux*Vy-Uy*Vx
     */
    public static boolean isClockwise(double p1x, double p1y, double p2x, double p2y, double p3x, double p3y) {
        return (p2x - p1x) * (p3y - p1y) - (p2y - p1y) * (p3x - p1x) > 0;
    }

    /**
     * This method returns true if the 3 points are orientated clockwise (screen coordinate system)
     * 
     * (B - A) x (C - A) < 0
     * 
     * U x V = Ux*Vy-Uy*Vx
     */
    public static boolean isCounterClockwise(double p1x, double p1y, double p2x, double p2y, double p3x, double p3y) {
        return (p2x - p1x) * (p3y - p1y) - (p2y - p1y) * (p3x - p1x) < 0;
    }

    /**
     * This method returns true if the 3 points are colinear
     * 
     * (B - A) x (C - A) = 0
     * 
     * U x V = Ux*Vy-Uy*Vx
     */
    public static boolean isColinear(double p1x, double p1y, double p2x, double p2y, double p3x, double p3y, double epsilon) {
        return NumberUtil.isZero((p2x - p1x) * (p3y - p1y) - (p2y - p1y) * (p3x - p1x), epsilon);
    }

    public static boolean isColinear(double p1x, double p1y, double p2x, double p2y, double p3x, double p3y) {
        return isColinear(p1x, p1y, p2x, p2y, p3x, p3y, NumberUtil.DEFAULT_EPSILON);
    }

    /**
     * This method finds the center line of the given list of points for which the sum of the distance of all points to the line is minimal.
     * 
     * The base vector of the center line is always the center of the list of points.
     * 
     * The direction vector has always a positive x and the length of the vector is always 1.
     */
    /*-
     * We start with the equation to calculate the minimum distance of a point to a line:
     * 
     * ->  Math.abs((y2 - y1) * x - (x2 - x1) * y + x2 * y1 - y2 * x1) / getDistance(x1, y1, x2, y2);
     * 
     * getDistance(x1, y1, x2, y2) is linear factor -> drop it
     *   
     * ->  Math.abs((y2 - y1) * x - (x2 - x1) * y + x2 * y1 - y2 * x1);  
     * 
     * We need our distance to be positive which is insured with Math.abs(). Math.sqr does this as well. Substituting Math.abs with Math.sql allows us 
     * to transform the equation but in certain conditions leads to less optimal solutions (e.g. a square) 
     * 
     * ->  Math.sqr((y2 - y1) * x - (x2 - x1) * y + x2 * y1 - y2 * x1);  
     * 
     * The points (x1,y1) and (x2,y2) define the center line we are looking for and the point (x,y) stands for a point of our list of points
     * We can set (x2,y2) with the center list of points:
     * x2 -> mx
     * y2 -> my
     * x -> xi
     * y -> yi
     * 
     * We are now looking for values for (x1,y1)
     * 
     * x1 -> x
     * y1 -> y
     * 
     * ->  Math.sqr((y2 - y1) * x - (x2 - x1) * y + x2 * y1 - y2 * x1)
     * ->  Math.sqr((my - y1) * x - (mx - x1) * y + mx * y1 - my * x1)
     * ->  Math.sqr((my - y) * xi - (mx - x) * yi + mx * y - my * x)
     *  
     * We are looking for the values (x,y) which minimise the sum of our transformed distance/error function over all given points
     * 
     *  -> min = sum(Math.sqr((my - y) * xi - (mx - x) * yi + mx * y - my * x))
     * 
     *  we can simplify the equation
     * 
     *  -> min = sum(Math.sqr(my * xi - y * xi - mx * yi + x * yi + mx * y - my * x))
     *  -> min = sum(Math.sqr(-y * xi - mx * yi + x * yi + mx * y - my * x + my * xi))
     *  -> min = sum(Math.sqr(-y * xi + x * yi + mx * y - my * x + my * xi - mx * yi)) 
     *
     *  c = my * xi - mx * yi
     *
     *  -> min = sum(Math.sqr(-y * xi + x * yi + mx * y - my * x + c)) 
     *  -> min = sum(Math.sqr(y * (mx-xi) + x * (yi - my) + c))
     *
     *  substitude: 
     *  -> mxi = mx-xi
     *  -> myi = yi - my
     * 
     *  -> min = sum(Math.sqr(y * mxi + x * myi + c)) 
     *  -> min = sum(y*y*mxi*mxi + 2*y*mxi*x*myi + 2*y*mxi*c + x*x*myi*myi + 2*x*myi*c + c*c)
     *  -> min = y*y*sum(mxi*mxi) + x*x*sum(myi*myi) + x*y*sum(2*mxi*myi) + y*sum(2*mxi*c) + x*sum(2*myi*c) + sum(c*c)
     *  
     * sum(c*c) is a constant which does not affect the result so we can drop it (or we keep it and later it will be dropped by the derivative)
     *
     *  -> min = y*y*sum(mxi*mxi) + x*x*sum(myi*myi) + x*y*sum(2*mxi*myi) + y*sum(2*mxi*c) + x*sum(2*myi*c)
     * more substitutions:
     * 
     *  -> smximxi = sum(mxi*mxi)
     *  -> smyimyi = sum(myi*myi)
     *  -> mximyi2 = sum(2*mxi*myi)
     *  -> mxic2 = sum(2*mxi*c)
     *  -> myic2 = sum(2*myi*c)
     *  
     *  -> min = y*y*smximxi + x*x*smyimyi + x*y*mximyi2 + y*mxic2 + x*myic2
     *  
     * We have an optimising problem with 2 unknowns. However, y depends on x so we can just pick any x to calculate a matching y.
     * We could pick the simplest value 0 but mx might be 0 already so we pick mx+1. 
     * 
     * with x = mx+1 = x
     *  -> min = y*y*smximxi + y*mximyi2*x + y*mxic2 + x*x*smyimyi + x*myic2
     *  -> min = y*y*smximxi + y*(mximyi2*x + mxic2) + x*x*smyimyi + x*myic2
     *  
     * We have now an optimising problem with 1 unknown of a quadratic equation. The equation has its minimum where its derivative is 0
     * So we need to take the derivative and calculate where it is 0 (Nullstelle)
     * 
     *  -> 0 = 2*y*smximxi + mximyi2*x + mxic2 
     *  -> y = -(mximyi2*x + mxic2) / (2 * smximxi)
     * 
     */
    public static Line findCenterLine(double[] points, Line result) {
        int count = points.length / 2;

        // get median
        double mx = 0;
        double my = 0;
        for (int i = 0, end = points.length; i < end; i += 2) {
            mx += points[i];
            my += points[i + 1];
        }

        mx /= count;
        my /= count;

        // calculate factors
        double smximxi = 0;
        double smyimyi = 0;
        double mximyi2 = 0;
        double mxic2 = 0;
        double myic2 = 0;

        for (int i = 0, end = points.length; i < end; i += 2) {
            double xi = points[i];
            double yi = points[i + 1];
            double mxi = mx - xi;
            double myi = yi - my;
            double c = my * xi - mx * yi;
            smximxi += mxi * mxi;
            smyimyi += myi * myi;
            mximyi2 += 2 * mxi * myi;
            mxic2 += 2 * mxi * c;
            myic2 += 2 * myi * c;
        }

        result.setX0(mx);
        result.setY0(my);

        double xByX;
        double yByX;
        double xByY;
        double yByY;

        if (smximxi == 0 && smyimyi == 0) {
            // one point
            xByX = mx;
            yByX = my;
            xByY = mx;
            yByY = my;
        } else {
            xByX = mx + 1;
            yByX = -(mximyi2 * xByX + mxic2) / (2 * smximxi);
            yByY = my + 1;
            xByY = -(mximyi2 * yByY + myic2) / (2 * smyimyi);

            if (smximxi == 0) {
                yByX = yByY;
                xByX = xByY;
            } else if (smyimyi == 0) {
                yByY = yByX;
                xByY = xByX;
            }
        }

        // pick better one

        double errorByX = calcError(smximxi, smyimyi, mximyi2, mxic2, myic2, xByX, yByX);
        double errorByY = calcError(smximxi, smyimyi, mximyi2, mxic2, myic2, xByY, yByY);

        double x;
        double y;
        if (errorByX < errorByY) {
            x = xByX;
            y = yByX;
        } else {
            x = xByY;
            y = yByY;
        }

        // get result
        double dx = mx - x;
        double dy = my - y;
        if (dx < 0) {
            dx = -dx;
            dy = -dy;
        }
        double len = Math.sqrt(dx * dx + dy * dy);
        result.setDx(dx / len);
        result.setDy(dy / len);

        return result;
    }

    private static double calcError(double smximxi, double smyimyi, double mximyi2, double mxic2, double myic2, double x, double y) {
        return y * y * smximxi + x * x * smyimyi + x * y * mximyi2 + y * mxic2 + x * myic2;
    }
    /*-
    private static double calcError(double[] points, double x, double y) {
        double mx = 0;
        double my = 0;
        for (int i = 0, end = points.length; i < end; i += 2) {
            mx += points[i];
            my += points[i + 1];
        }
        double error = 0;
        double dist = getDistanceSqr(x, y, mx, my);
        for (int i = 0, end = points.length; i < end; i += 2) {
            double xi = points[i];
            double yi = points[i + 1];
            double e = MathUtil.sqr(Math.abs((my - y) * xi - (mx - x) * yi + mx * y - my * x) / getDistance(x, y, mx, my));
            error += e;
        }
        return error;
    } */
}
