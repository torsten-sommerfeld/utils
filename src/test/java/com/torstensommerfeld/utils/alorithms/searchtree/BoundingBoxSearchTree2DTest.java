package com.torstensommerfeld.utils.alorithms.searchtree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.torstensommerfeld.utils.alorithms.containers.boundingbox.BoundingBox2D;
import com.torstensommerfeld.utils.alorithms.containers.boundingbox.BoundingBoxImpl;

public class BoundingBoxSearchTree2DTest {
    private BoundingBoxSearchTree2DBuilder<BoundingBox2D> builder = new BoundingBoxSearchTree2DBuilder<>(2);

    private BoundingBox2D bc = new BoundingBoxImpl(1.1, 1.1, 1.2, 1.2);
    private BoundingBox2D b0 = new BoundingBoxImpl(-10, -10, 10, 10);
    private BoundingBox2D b1 = new BoundingBoxImpl(0, 0, 1, 1);
    private BoundingBox2D b2 = new BoundingBoxImpl(-2, -2, 1, 1);
    private BoundingBox2D b3 = new BoundingBoxImpl(-0.5, -0.5, 0.5, 0.5);
    private BoundingBox2D b4 = new BoundingBoxImpl(0, 2, 1, 3);
    private BoundingBox2D b5 = new BoundingBoxImpl(0.5, 3, 1.5, 4);
    private BoundingBox2D b6 = new BoundingBoxImpl(0.6, 4, 1.6, 5);
    private BoundingBox2D b7 = new BoundingBoxImpl(-0.6, 2, 0.4, 3);
    private BoundingBox2D b8 = new BoundingBoxImpl(0.5, -2, 1.5, 1);
    private BoundingBox2D b9 = new BoundingBoxImpl(-4, -1, -3, 0);
    private BoundingBox2D b10 = new BoundingBoxImpl(-3.5, 1, -2.5, 2);
    private BoundingBox2D b11 = new BoundingBoxImpl(2, 0, 3, 1);

    @Test
    public void testClearHorizontalSplit() {
        // given
        List<BoundingBox2D> objects = Arrays.asList(b1, b11);
        BoundingBoxSearchTree2D<BoundingBox2D> tree = builder.build(objects);

        // when
        List<BoundingBox2D> lefts = tree.findCollisions(b1, new ArrayList<>());
        List<BoundingBox2D> rights = tree.findCollisions(b11, new ArrayList<>());
        List<BoundingBox2D> all = tree.findCollisions(b0, new ArrayList<>());
        List<BoundingBox2D> none = tree.findCollisions(bc, new ArrayList<>());

        // then
        Assert.assertEquals(1, lefts.size());
        Assert.assertSame(b1, lefts.get(0));
        Assert.assertEquals(1, rights.size());
        Assert.assertSame(b11, rights.get(0));

        Assert.assertEquals(2, all.size());
        Assert.assertEquals(0, none.size());
    }

    @Test
    public void testCenterHorizontalSplit() {
        // given
        List<BoundingBox2D> objects = Arrays.asList(b1, b11, b0);
        BoundingBoxSearchTree2D<BoundingBox2D> tree = builder.build(objects);

        // when
        List<BoundingBox2D> lefts = tree.findCollisions(b1, new ArrayList<>());
        List<BoundingBox2D> rights = tree.findCollisions(b11, new ArrayList<>());
        List<BoundingBox2D> all = tree.findCollisions(b0, new ArrayList<>());
        List<BoundingBox2D> none = tree.findCollisions(bc, new ArrayList<>());

        // then
        Assert.assertEquals(2, lefts.size());
        Assert.assertTrue(lefts.contains(b1));
        Assert.assertEquals(2, rights.size());
        Assert.assertTrue(rights.contains(b11));

        Assert.assertEquals(3, all.size());
        Assert.assertEquals(1, none.size());
    }

    @Test
    public void testComplex() {
        // given
        List<BoundingBox2D> objects = Arrays.asList(bc, b0, b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11);
        BoundingBoxSearchTree2D<BoundingBox2D> tree = builder.build(objects);

        // when
        List<BoundingBox2D> lefts = tree.findCollisions(b1, new ArrayList<>());
        List<BoundingBox2D> rights = tree.findCollisions(b11, new ArrayList<>());
        List<BoundingBox2D> all = tree.findCollisions(b0, new ArrayList<>());

        // then
        Assert.assertEquals(5, lefts.size());
        Assert.assertTrue(lefts.contains(b1));
        Assert.assertTrue(lefts.contains(b0));
        Assert.assertEquals(2, rights.size());
        Assert.assertTrue(rights.contains(b11));
        Assert.assertTrue(rights.contains(b0));

        Assert.assertEquals(13, all.size());

    }
}
