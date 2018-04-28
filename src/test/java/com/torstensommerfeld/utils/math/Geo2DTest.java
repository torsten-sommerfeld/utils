package com.torstensommerfeld.utils.math;

import org.junit.Assert;
import org.junit.Test;

import com.torstensommerfeld.utils.math.model.Line;
import com.torstensommerfeld.utils.math.shapes.Circle;
import com.torstensommerfeld.utils.math.shapes.CubicFunctionParameters;
import com.torstensommerfeld.utils.math.shapes.Ellipse;
import com.torstensommerfeld.utils.math.shapes.QuadraticFunctionParameters;

public class Geo2DTest {

    @Test
    public void testGetDistance() {
        // when
        double distance = Geo2D.getDistance(0, 0, 1, 1);

        // then
        Assert.assertEquals(1.41, distance, 0.01);
    }

    @Test
    public void testGetDistanceSqr() {
        // when
        double distance = Geo2D.getDistanceSqr(0, 0, 1, 1);

        // then
        Assert.assertEquals(2, distance, 0.0001);
    }

    @Test
    public void testgetDistanceOfPointToLine() {
        // when
        double distanceStraight = Geo2D.distanceOfPointToLine(0, 0, 0, 10, 10, 10);
        double distanceDiagonal = Geo2D.distanceOfPointToLine(0, 0, 0, 10, 10, 0);

        // then
        Assert.assertEquals(10, distanceStraight, 0.0001);
        Assert.assertEquals(7.07, distanceDiagonal, 0.01);
    }

    @Test
    public void testgetDistanceOfPointToLineWithGivenDistance() {
        // when
        double distanceStraight = Geo2D.distanceOfPointToLine(0, 0, 0, 10, 10, 10, Geo2D.getDistance(0, 10, 10, 10));
        double distanceDiagonal = Geo2D.distanceOfPointToLine(0, 0, 0, 10, 10, 0, Geo2D.getDistance(0, 10, 10, 0));

        // then
        Assert.assertEquals(10, distanceStraight, 0.0001);
        Assert.assertEquals(7.07, distanceDiagonal, 0.01);
    }

    @Test
    public void testGetCircle_center_points_on_axis_and_center() {
        // given
        Circle target = new Circle();

        // when
        Geo2D.getCircle(0, -2, 2, 0, 0, 2, target);

        // then
        Assert.assertEquals(0, target.getX(), 0.001);
        Assert.assertEquals(0, target.getY(), 0.001);
        Assert.assertEquals(2, target.getR(), 0.001);
    }

    @Test
    public void testGetCircle_center_on_onetwo_and_points_on_axis_and_center() {
        // given
        Circle target = new Circle();

        // when
        Geo2D.getCircle(1, 0, 3, 2, 1, 4, target);

        // then
        Assert.assertEquals(1, target.getX(), 0.001);
        Assert.assertEquals(2, target.getY(), 0.001);
        Assert.assertEquals(2, target.getR(), 0.001);
    }

    @Test
    public void testGetCircle_center_on_onetwo_and_points_on_axis_and_center_swapped_points() {
        // given
        Circle target = new Circle();

        // when
        Geo2D.getCircle(1, 0, 1, 4, 3, 2, target);

        // then
        Assert.assertEquals(1, target.getX(), 0.001);
        Assert.assertEquals(2, target.getY(), 0.001);
        Assert.assertEquals(2, target.getR(), 0.001);
    }

    @Test
    public void testGetCircle_center_on_three_three_and_points_not_on_axis_and_center_swapped_points() {
        // given
        Circle target = new Circle();

        // when
        Geo2D.getCircle(1, 2, 2, 1, 4, 5, target);

        // then
        Assert.assertEquals(3, target.getX(), 0.001);
        Assert.assertEquals(3, target.getY(), 0.001);
        Assert.assertEquals(2, target.getR(), 0.3);
    }

    @Test
    public void testGetCircle_points_are_on_a_line() {
        // given
        Circle target = new Circle();

        // when
        Geo2D.getCircle(1, 0, 2, 0, 3, 0, target);

        // then
        Assert.assertTrue(Double.isNaN(target.getX()) || Double.isInfinite(target.getX()));
        Assert.assertTrue(Double.isNaN(target.getY()) || Double.isInfinite(target.getY()));
        Assert.assertTrue(Double.isNaN(target.getR()));
    }

    @Test
    public void testGetEllipse_circle_centered_in_origin() {
        // given
        Ellipse ellipse = new Ellipse();
        double sq = Math.sqrt(2) / 2;

        // when
        ellipse = Geo2D.getEllipse(//
                0, -1, // 0
                sq, -sq, // 45
                1, 0, // 90
                sq, sq, // 135
                0, 1, // 180
                ellipse);

        // then
        Assert.assertEquals(0, ellipse.getX(), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(0, ellipse.getY(), NumberUtil.DEFAULT_EPSILON);

    }

    @Test
    public void testGetEllipse_circle_centered_in_1x2() {
        // given
        Ellipse ellipse = new Ellipse();
        double sq = Math.sqrt(2) / 2;
        double x1 = 1 + 0, y1 = 2 - 1; // 0
        double x2 = 1 + sq, y2 = 2 - sq; // 45
        double x3 = 1 + 1, y3 = 2 + 0; // 90
        double x4 = 1 + sq, y4 = 2 + sq; // 135
        double x5 = 1 + 0, y5 = 2 + 1;
        // when
        ellipse = Geo2D.getEllipse(x1, y1, x2, y2, x3, y3, x4, y4, x5, y5, ellipse);

        // then
        Assert.assertEquals(1, ellipse.getX(), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(2, ellipse.getY(), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(1, ellipse.getA(), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(1, ellipse.getB(), NumberUtil.DEFAULT_EPSILON);
        assertPointOnEllipse(ellipse, x1, y1);
        assertPointOnEllipse(ellipse, x2, y2);
        assertPointOnEllipse(ellipse, x3, y3);
        assertPointOnEllipse(ellipse, x4, y4);
        assertPointOnEllipse(ellipse, x5, y5);

    }

    @Test
    public void testGetEllipse_x_axis_centered_in_3x0() {
        // given
        Ellipse ellipse = new Ellipse();
        double x1 = 0, y1 = 0;
        double x2 = 1, y2 = 1;
        double x3 = 5, y3 = 1;
        double x4 = 6, y4 = 0;
        double x5 = 5, y5 = -1;
        // when
        ellipse = Geo2D.getEllipse(x1, y1, x2, y2, x3, y3, x4, y4, x5, y5, ellipse);

        // then
        Assert.assertEquals(3, ellipse.getX(), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(0, ellipse.getY(), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(0, ellipse.getRotationsAngle(), NumberUtil.DEFAULT_EPSILON);
        assertPointOnEllipse(ellipse, x1, y1);
        assertPointOnEllipse(ellipse, x2, y2);
        assertPointOnEllipse(ellipse, x3, y3);
        assertPointOnEllipse(ellipse, x4, y4);
        assertPointOnEllipse(ellipse, x5, y5);

    }

    @Test
    public void testGetEllipse_x_axis_centered_in_3x0_45degree_angle() {
        // given
        Ellipse ellipse = new Ellipse();
        double x1 = 0, y1 = 0;
        double x2 = 1, y2 = 1;
        double x3 = 5, y3 = 1;
        double x4 = 6, y4 = 0;
        double x5 = 5, y5 = -1;
        double[] rotationCenter = { 3, 0 };
        double angle = Math.PI / 4;
        double[] p1 = Geo2D.rotate(new double[] { x1, y1 }, rotationCenter, angle, new double[2]);
        double[] p2 = Geo2D.rotate(new double[] { x2, y2 }, rotationCenter, angle, new double[2]);
        double[] p3 = Geo2D.rotate(new double[] { x3, y3 }, rotationCenter, angle, new double[2]);
        double[] p4 = Geo2D.rotate(new double[] { x4, y4 }, rotationCenter, angle, new double[2]);
        double[] p5 = Geo2D.rotate(new double[] { x5, y5 }, rotationCenter, angle, new double[2]);
        // when
        ellipse = Geo2D.getEllipse(p1, p2, p3, p4, p5, ellipse);

        // then
        Assert.assertEquals(3, ellipse.getX(), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(0, ellipse.getY(), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(angle, ellipse.getRotationsAngle(), NumberUtil.DEFAULT_EPSILON);
        assertPointOnEllipse(ellipse, p1[0], p1[1]);
        assertPointOnEllipse(ellipse, p2[0], p2[1]);
        assertPointOnEllipse(ellipse, p3[0], p3[1]);
        assertPointOnEllipse(ellipse, p4[0], p4[1]);
        assertPointOnEllipse(ellipse, p5[0], p5[1]);
    }

    @Test
    public void testGetEllipse_with_given_center() {
        // given
        Ellipse ellipse = new Ellipse();
        double x1 = 0, y1 = 0;
        double x2 = 1, y2 = 1;
        double x3 = 5, y3 = 1;
        // when
        ellipse = Geo2D.getEllipseWithGivenCenter(3, 0, x1, y1, x2, y2, x3, y3, ellipse);

        // then
        Assert.assertEquals(3, ellipse.getX(), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(0, ellipse.getY(), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(0, ellipse.getRotationsAngle(), NumberUtil.DEFAULT_EPSILON);
        assertPointOnEllipse(ellipse, x1, y1);
        assertPointOnEllipse(ellipse, x2, y2);
        assertPointOnEllipse(ellipse, x3, y3);
    }

    @Test
    public void testGetEllipse_with_given_center2() {
        // given
        /*-
         *     12345
         *     
         * 1    123
         * 2   4   5
         * 3    678
         *     
         */
        Ellipse ellipse = new Ellipse();
        double x1 = 1, y1 = 2;
        double x2 = 2, y2 = 1;
        double x3 = 2, y3 = 3;
        // when
        ellipse = Geo2D.getEllipseWithGivenCenter(3, 2, x1, y1, x2, y2, x3, y3, ellipse);
        // then
        Assert.assertEquals(3, ellipse.getX(), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(2, ellipse.getY(), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(0, ellipse.getRotationsAngle(), NumberUtil.DEFAULT_EPSILON);
        assertPointOnEllipse(ellipse, x1, y1);
        assertPointOnEllipse(ellipse, x2, y2);
        assertPointOnEllipse(ellipse, x3, y3);
    }

    @Test
    public void testGetEllipse_x_axis_centered_in_3x0_30degree_angle() {
        // given
        Ellipse ellipse = new Ellipse();
        double x1 = 0, y1 = 0;
        double x2 = 1, y2 = 1;
        double x3 = 5, y3 = 1;
        double x4 = 6, y4 = 0;
        double x5 = 5, y5 = -1;
        double[] rotationCenter = { 3, 0 };
        double angle = Math.PI * 30 / 180;
        double[] p1 = Geo2D.rotate(new double[] { x1, y1 }, rotationCenter, angle, new double[2]);
        double[] p2 = Geo2D.rotate(new double[] { x2, y2 }, rotationCenter, angle, new double[2]);
        double[] p3 = Geo2D.rotate(new double[] { x3, y3 }, rotationCenter, angle, new double[2]);
        double[] p4 = Geo2D.rotate(new double[] { x4, y4 }, rotationCenter, angle, new double[2]);
        double[] p5 = Geo2D.rotate(new double[] { x5, y5 }, rotationCenter, angle, new double[2]);
        // when
        ellipse = Geo2D.getEllipse(p1, p2, p3, p4, p5, ellipse);

        // then
        Assert.assertEquals(3, ellipse.getX(), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(0, ellipse.getY(), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(angle, ellipse.getRotationsAngle(), NumberUtil.DEFAULT_EPSILON);
        assertPointOnEllipse(ellipse, p1[0], p1[1]);
        assertPointOnEllipse(ellipse, p2[0], p2[1]);
        assertPointOnEllipse(ellipse, p3[0], p3[1]);
        assertPointOnEllipse(ellipse, p4[0], p4[1]);
        assertPointOnEllipse(ellipse, p5[0], p5[1]);
    }

    private static void assertPointOnEllipse(Ellipse ellipse, double x, double y) {
        Assert.assertEquals(1, ellipse.getDistanceRatingToCenter(x, y), 0.0001);
    }

    @Test
    public void testRotatePointAroundOrigin() {
        // given
        double[] point = { 1, 0 };
        double[] target = new double[2];
        double deltaAngle = Math.PI / 4;

        // when
        for (int i = 0; i < 8; ++i) {
            double angle = deltaAngle * i;
            double[] result = Geo2D.rotate(point, angle, target);

            // then
            Assert.assertEquals(Math.cos(angle), result[0], NumberUtil.DEFAULT_EPSILON);
            Assert.assertEquals(Math.sin(angle), result[1], NumberUtil.DEFAULT_EPSILON);
        }
    }

    @Test
    public void testRotatePoint_array() {
        // given
        double[] point = { 2, 1 };
        double[] center = { 1, 1 };
        double[] target = new double[2];
        double deltaAngle = Math.PI / 4;

        // when
        for (int i = 0; i < 8; ++i) {
            double angle = deltaAngle * i;
            double[] result = Geo2D.rotate(point, center, angle, target);

            // then
            Assert.assertEquals(center[0] + Math.cos(angle), result[0], NumberUtil.DEFAULT_EPSILON);
            Assert.assertEquals(center[1] + Math.sin(angle), result[1], NumberUtil.DEFAULT_EPSILON);
        }
    }

    @Test
    public void testRotatePoint() {
        // given
        double[] point = { 2, 1 };
        double[] center = { 1, 1 };
        double[] target = new double[2];
        double deltaAngle = Math.PI / 4;

        // when
        for (int i = 0; i < 8; ++i) {
            double angle = deltaAngle * i;
            double[] result = Geo2D.rotate(point[0], point[1], center, angle, target);

            // then
            Assert.assertEquals(center[0] + Math.cos(angle), result[0], NumberUtil.DEFAULT_EPSILON);
            Assert.assertEquals(center[1] + Math.sin(angle), result[1], NumberUtil.DEFAULT_EPSILON);
        }
    }

    @Test
    public void testQuadraticFunction_center_0x0_upwards_no_scale() {
        // given
        double x1 = -1, y1 = 1;
        double x2 = 0, y2 = 0;
        double x3 = 1, y3 = 1;

        // when
        QuadraticFunctionParameters result = Geo2D.getQuadraticFunctionParameters(x1, y1, x2, y2, x3, y3, new QuadraticFunctionParameters());

        // then
        double a = result.getA();
        double b = result.getB();
        double c = result.getC();
        double vertex[] = Geo2D.getVertexPointForQuadraticEquation(a, b, c, new double[2]);
        Assert.assertEquals(1, a, NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(0, vertex[0], NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(0, vertex[1], NumberUtil.DEFAULT_EPSILON);
    }

    @Test
    public void testQuadraticFunction_center_1x2_upwards_no_scale() {
        // given
        double x1 = 0, y1 = 3;
        double x2 = 1, y2 = 2;
        double x3 = 2, y3 = 3;

        // when
        QuadraticFunctionParameters result = Geo2D.getQuadraticFunctionParameters(x1, y1, x2, y2, x3, y3, new QuadraticFunctionParameters());

        // then
        double a = result.getA();
        double b = result.getB();
        double c = result.getC();
        double vertex[] = Geo2D.getVertexPointForQuadraticEquation(a, b, c, new double[2]);
        Assert.assertEquals(1, vertex[0], NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(2, vertex[1], NumberUtil.DEFAULT_EPSILON);
    }

    @Test
    public void testCubicFunction() {
        // given
        double x1 = -4, y1 = 0;
        double x2 = -1, y2 = 0;
        double x3 = 0, y3 = -2;
        double x4 = 2, y4 = 0;

        // when
        CubicFunctionParameters result = Geo2D.getCubicFunctionParameters(x1, y1, x2, y2, x3, y3, x4, y4, new CubicFunctionParameters());

        // then
        Assert.assertEquals(y1, result.calculateY(x1), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(y2, result.calculateY(x2), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(y3, result.calculateY(x3), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(y4, result.calculateY(x4), NumberUtil.DEFAULT_EPSILON);
    }

    @Test
    public void testGetClosestPointOnLineToPoint_xAxis_to_point() {
        // given
        double x1 = 1;
        double y1 = 0;
        double x2 = 10;
        double y2 = 0;
        double x = 5;
        double y = 5;

        // when
        double[] result = Geo2D.getClosestPointOnLineToPoint(x, y, x1, y1, x2, y2, new double[2]);

        // then
        Assert.assertEquals(5, result[0], NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(0, result[1], NumberUtil.DEFAULT_EPSILON);
    }

    @Test
    public void testGetClosestPointOnLineToPoint_parallelToAxis_to_point() {
        // given
        double x1 = 1;
        double y1 = 3;
        double x2 = 10;
        double y2 = 3;
        double x = 5;
        double y = 3;

        // when
        double[] result = Geo2D.getClosestPointOnLineToPoint(x, y, x1, y1, x2, y2, new double[2]);

        // then
        Assert.assertEquals(5, result[0], NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(3, result[1], NumberUtil.DEFAULT_EPSILON);
    }

    @Test
    public void testGetClosestPointOnLineToPoint_line_through_origin_to_point() {
        // given
        double x1 = -5;
        double y1 = -10;
        double x2 = 5;
        double y2 = 10;
        double x = y1 - y2;
        double y = x2 - x1;

        // when
        double[] result = Geo2D.getClosestPointOnLineToPoint(x, y, x1, y1, x2, y2, new double[2]);

        // then
        Assert.assertEquals(0, result[0], NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(0, result[1], NumberUtil.DEFAULT_EPSILON);
    }

    @Test
    public void testGetRelativeLocationOfPointBetween2Points_horizontal() {
        // given
        double x1 = 10;
        double y1 = 2;
        double x2 = 18;
        double y2 = 2;
        double x = 12;
        double y = 2;

        // when
        double result = Geo2D.getRelativeLocationOfPointBetween2Points(x, y, x1, y1, x2, y2);

        // then
        Assert.assertEquals(0.25, result, NumberUtil.DEFAULT_EPSILON);
    }

    @Test
    public void testGetRelativeLocationOfPointBetween2Points_vertically() {
        // given
        double x1 = 2;
        double y1 = 18;
        double x2 = 2;
        double y2 = 10;
        double x = 2;
        double y = 12;

        // when
        double result = Geo2D.getRelativeLocationOfPointBetween2Points(x, y, x1, y1, x2, y2);

        // then
        Assert.assertEquals(0.75, result, NumberUtil.DEFAULT_EPSILON);
    }

    @Test
    public void testNormalizeAngle() {
        Assert.assertEquals(NumberUtil.ANGLE_135, Geo2D.normalizeAngle(NumberUtil.ANGLE_135), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(NumberUtil.ANGLE_135, Geo2D.normalizeAngle(NumberUtil.ANGLE_135 + NumberUtil.ANGLE_360), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(NumberUtil.ANGLE_135, Geo2D.normalizeAngle(NumberUtil.ANGLE_135 - NumberUtil.ANGLE_360), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(NumberUtil.ANGLE_135, Geo2D.normalizeAngle(NumberUtil.ANGLE_135 + NumberUtil.ANGLE_360 + NumberUtil.ANGLE_360), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(NumberUtil.ANGLE_135, Geo2D.normalizeAngle(NumberUtil.ANGLE_135 - NumberUtil.ANGLE_360 - NumberUtil.ANGLE_360), NumberUtil.DEFAULT_EPSILON);
    }

    @Test
    public void testGetAngleBetweenVectors() {
        Assert.assertEquals(0, Geo2D.getAngleBetweenVectors(0, 0, 1, 0, 0, 1, 1, 1), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(NumberUtil.ANGLE_90, Geo2D.getAngleBetweenVectors(0, 0, 1, 0, 0, 0, 0, 1), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(NumberUtil.ANGLE_270, Geo2D.getAngleBetweenVectors(0, 0, 0, 1, 0, 0, 1, 0), NumberUtil.DEFAULT_EPSILON);
    }

    @Test
    public void testIsClockWise() {
        Assert.assertTrue(Geo2D.isClockwise(0, 0, 10, 0, 0, 0, 10, 0)); // 0 degrees
        Assert.assertTrue(Geo2D.isClockwise(0, 0, 10, 0, 0, 0, 10, 1)); // 5.7 degrees
        Assert.assertFalse(Geo2D.isClockwise(0, 0, 10, 0, 0, 0, 10, -1)); // 354 degrees
        Assert.assertFalse(Geo2D.isClockwise(0, 0, 0, 10, 0, 0, 10, 0)); // 270 degrees
        Assert.assertFalse(Geo2D.isClockwise(0, 0, 0, 10, 0, 0, 10, -100)); // 185 degrees
        Assert.assertTrue(Geo2D.isClockwise(0, 0, 0, 10, 0, 0, -10, -100)); // 174 degrees
    }

    @Test
    public void testFindCenterLine_vertical() {

        // given
        double points[] = { 0, 0, 6, 0, 6, 2, 4, 2, 4, 6, 6, 6, 6, 8, 0, 8, 0, 6, 2, 6, 2, 2, 0, 2 };

        // when
        Line result = Geo2D.findCenterLine(points, new Line());

        // then
        Assert.assertEquals(0, result.getDx(), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(1, Math.abs(result.getDy()), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(3, result.getX0(), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(4, result.getY0(), NumberUtil.DEFAULT_EPSILON);
    }

    @Test
    public void testFindCenterLine_horizontal() {

        // given
        double points[] = { 0, 0, 0, 6, 2, 6, 2, 4, 6, 4, 6, 6, 8, 6, 8, 0, 6, 0, 6, 2, 2, 2, 2, 0 };

        // when
        Line result = Geo2D.findCenterLine(points, new Line());

        // then
        Assert.assertEquals(1, Math.abs(result.getDx()), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(0, result.getDy(), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(4, result.getX0(), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(3, result.getY0(), NumberUtil.DEFAULT_EPSILON);
    }

    @Test
    public void testFindCenterLine_horizontal_2_points() {
        // given
        double points[] = { 1, 1, 2, 1 };

        // when
        Line result = Geo2D.findCenterLine(points, new Line());

        // then
        Assert.assertEquals(1, result.getDx(), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(0, result.getDy(), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(1.5, result.getX0(), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(1, result.getY0(), NumberUtil.DEFAULT_EPSILON);
    }

    @Test
    public void testFindCenterLine_vertical_2_points() {
        // given
        double points[] = { 1, 1, 1, 2 };

        // when
        Line result = Geo2D.findCenterLine(points, new Line());

        // then
        Assert.assertEquals(0, result.getDx(), NumberUtil.DEFAULT_EPSILON);
        Assert.assertNotEquals(1, result.getDy(), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(1, result.getX0(), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(1.5, result.getY0(), NumberUtil.DEFAULT_EPSILON);
    }

    @Test
    public void testFindCenterLine_diagonal_2_points() {
        // given
        double points[] = { 1, 1, 2, 2 };

        // when
        Line result = Geo2D.findCenterLine(points, new Line());

        // then
        Assert.assertEquals(Math.sqrt(2) / 2, Math.abs(result.getDx()), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(Math.sqrt(2) / 2, Math.abs(result.getDy()), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(1.5, result.getX0(), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(1.5, result.getY0(), NumberUtil.DEFAULT_EPSILON);
    }

    @Test
    public void testFindCenterLine_triangle_horizontal() {
        // given
        double points[] = { 0, 0, 10, 0, 5, 1 };

        // when
        Line result = Geo2D.findCenterLine(points, new Line());

        // then
        Assert.assertNotEquals(0, result.getDx(), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(0, result.getDy(), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(5, result.getX0(), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(1.0 / 3.0, result.getY0(), NumberUtil.DEFAULT_EPSILON);
    }

    @Test
    public void testFindCenterLine_triangle_vertical() {
        // given
        double points[] = { 0, 0, 10, 0, 5, 100 };

        // when
        Line result = Geo2D.findCenterLine(points, new Line());

        // then
        Assert.assertEquals(0, result.getDx(), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(1, Math.abs(result.getDy()), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(5, result.getX0(), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(100 / 3.0, result.getY0(), NumberUtil.DEFAULT_EPSILON);
    }

    @Test
    public void testFindCenterLine_rotated_rectangle_45() {
        // given
        double points[] = { 0, 0, 20, 0, 20, 10, 0, 10 };

        // rotate points
        for (int i = 0; i < points.length; i += 2) {
            double[] p = { points[i], points[i + 1] };
            double[] target = Geo2D.rotate(p, new double[] { 10, 5 }, NumberUtil.ANGLE_45, new double[2]);
            points[i] = target[0];
            points[i + 1] = target[1];
        }

        // when
        Line result = Geo2D.findCenterLine(points, new Line());

        // then
        Assert.assertNotEquals(0, result.getDx(), NumberUtil.DEFAULT_EPSILON);
        Assert.assertNotEquals(0, Math.abs(result.getDy()), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(10, result.getX0(), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(5, result.getY0(), NumberUtil.DEFAULT_EPSILON);
    }

    @Test
    public void testFindCenterLine_diamond() {
        // given
        double points[] = { -10, 0, 0, -10, 10, 0, 0, 10 };

        // when
        Line result = Geo2D.findCenterLine(points, new Line());

        // then
        Assert.assertEquals(0, result.getDx(), NumberUtil.DEFAULT_EPSILON);
        Assert.assertNotEquals(0, Math.abs(result.getDy()), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(0, result.getX0(), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(0, result.getY0(), NumberUtil.DEFAULT_EPSILON);
    }

    @Test
    public void testFindCenterLine_square() {
        // given
        double points[] = { -5, -5, 5, -5, 5, 5, -5, 5 };

        // when
        Line result = Geo2D.findCenterLine(points, new Line());

        // then
        Assert.assertEquals(0, result.getDx(), NumberUtil.DEFAULT_EPSILON);
        Assert.assertNotEquals(0, Math.abs(result.getDy()), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(0, result.getX0(), NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(0, result.getY0(), NumberUtil.DEFAULT_EPSILON);
    }
}
