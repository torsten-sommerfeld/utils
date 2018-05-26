package com.torstensommerfeld.utils.alorithms.sort.radix;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

public class RadixSortIntLsdTest {
    @Test
    public void testSmallArray() {
        // given
        // int[] is = { 2, 1 };
        int[] is = { 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0, 11, 12, 13, 14, 15, 16, 17, 18, 19 };

        // when
        RadixSortInt.sortLsd(is);

        // then
        for (int i = 0; i < 20; ++i) {
            Assert.assertEquals(i, is[i]);
        }
    }

    @Test
    public void testSingleton() {
        // given
        int[] is = { 1 };

        // when
        RadixSortInt.sortLsd(is);

        // then
        Assert.assertEquals(1, is[0]);
    }

    @Test
    public void testDupplicates() {
        // given
        int[] is = { 1, 2, 1, 2 };

        // when
        RadixSortInt.sortLsd(is);

        // then
        Assert.assertEquals(1, is[0]);
        Assert.assertEquals(1, is[1]);
        Assert.assertEquals(2, is[2]);
        Assert.assertEquals(2, is[3]);
    }

    @Test
    public void bigTest() {
        // given
        int[] data = new int[10000000];
        Random rnd = new Random(0);
        for (int i = 0; i < data.length; ++i) {
            data[i] = rnd.nextInt(1000000);
        }

        // when
        RadixSortInt.sortLsd(data);

        // then
        for (int i = 0; i < data.length - 1; ++i) {
            Assert.assertTrue(data[i] <= data[i + 1]);
        }

    }

}
