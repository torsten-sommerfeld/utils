package com.torstensommerfeld.utils.math;

import org.junit.Assert;
import org.junit.Test;

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

}
