package com.torstensommerfeld.utils.math;

import org.junit.Assert;
import org.junit.Test;

import com.torstensommerfeld.utils.math.shapes.Circle;
import com.torstensommerfeld.utils.math.shapes.Ellipse;

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
        assertPointOnEllipse(ellipse, x1, y1);
        assertPointOnEllipse(ellipse, x2, y2);
        assertPointOnEllipse(ellipse, x3, y3);
        assertPointOnEllipse(ellipse, x4, y4);
        assertPointOnEllipse(ellipse, x5, y5);
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
        assertPointOnEllipse(ellipse, x1, y1);
        assertPointOnEllipse(ellipse, x2, y2);
        assertPointOnEllipse(ellipse, x3, y3);
        assertPointOnEllipse(ellipse, x4, y4);
        assertPointOnEllipse(ellipse, x5, y5);
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
}
