package com.torstensommerfeld.utils.alorithms.containers.boundingbox;

import org.junit.Assert;
import org.junit.Test;

public class BoundingBox2DUtilTest {
    @Test
    public void testLeftOf() {
        // given
        BoundingBox2D b1 = new BoundingBoxImpl(0, 0, 1, 1);
        BoundingBox2D b2 = new BoundingBoxImpl(2, 0, 3, 1);

        // when
        boolean result = BoundingBox2DUtil.touch(b1, b2);

        // then
        Assert.assertFalse(result);
    }

    @Test
    public void testTopOf() {
        // given
        BoundingBox2D b1 = new BoundingBoxImpl(0, 0, 1, 1);
        BoundingBox2D b2 = new BoundingBoxImpl(0, 2, 1, 3);

        // when
        boolean result = BoundingBox2DUtil.touch(b1, b2);

        // then
        Assert.assertFalse(result);
    }

    @Test
    public void testRightOf() {
        // given
        BoundingBox2D b1 = new BoundingBoxImpl(2, 0, 3, 1);
        BoundingBox2D b2 = new BoundingBoxImpl(0, 0, 1, 1);

        // when
        boolean result = BoundingBox2DUtil.touch(b1, b2);

        // then
        Assert.assertFalse(result);
    }

    @Test
    public void testBottomOf() {
        // given
        BoundingBox2D b1 = new BoundingBoxImpl(0, 2, 1, 3);
        BoundingBox2D b2 = new BoundingBoxImpl(0, 0, 1, 1);

        // when
        boolean result = BoundingBox2DUtil.touch(b1, b2);

        // then
        Assert.assertFalse(result);
    }

    @Test
    public void testIn() {
        // given
        BoundingBox2D b1 = new BoundingBoxImpl(1, 1, 2, 2);
        BoundingBox2D b2 = new BoundingBoxImpl(0, 0, 3, 3);

        // when
        boolean result = BoundingBox2DUtil.touch(b1, b2);

        // then
        Assert.assertTrue(result);
    }

    @Test
    public void testContains() {
        // given
        BoundingBox2D b1 = new BoundingBoxImpl(0, 0, 3, 3);
        BoundingBox2D b2 = new BoundingBoxImpl(1, 1, 2, 2);

        // when
        boolean result = BoundingBox2DUtil.touch(b1, b2);

        // then
        Assert.assertTrue(result);
    }

    @Test
    public void testTouchRight() {
        // given
        BoundingBox2D b1 = new BoundingBoxImpl(0, 0, 1, 1);
        BoundingBox2D b2 = new BoundingBoxImpl(0.9, 0, 3, 1);

        // when
        boolean result = BoundingBox2DUtil.touch(b1, b2);

        // then
        Assert.assertTrue(result);
    }

    @Test
    public void testTouchBottom() {
        // given
        BoundingBox2D b1 = new BoundingBoxImpl(0, 0, 1, 1);
        BoundingBox2D b2 = new BoundingBoxImpl(0, 0.9, 1, 3);

        // when
        boolean result = BoundingBox2DUtil.touch(b1, b2);

        // then
        Assert.assertTrue(result);
    }

    @Test
    public void testTouchLeft() {
        // given
        BoundingBox2D b1 = new BoundingBoxImpl(0.9, 0, 3, 1);
        BoundingBox2D b2 = new BoundingBoxImpl(0, 0, 1, 1);

        // when
        boolean result = BoundingBox2DUtil.touch(b1, b2);

        // then
        Assert.assertTrue(result);
    }

    @Test
    public void testTouchTop() {
        // given
        BoundingBox2D b1 = new BoundingBoxImpl(0, 0.9, 1, 3);
        BoundingBox2D b2 = new BoundingBoxImpl(0, 0, 1, 1);

        // when
        boolean result = BoundingBox2DUtil.touch(b1, b2);

        // then
        Assert.assertTrue(result);
    }

}
