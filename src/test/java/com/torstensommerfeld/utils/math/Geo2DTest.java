package com.torstensommerfeld.utils.math;

import org.junit.Assert;
import org.junit.Test;

import com.torstensommerfeld.utils.math.shapes.Circle;

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

}
