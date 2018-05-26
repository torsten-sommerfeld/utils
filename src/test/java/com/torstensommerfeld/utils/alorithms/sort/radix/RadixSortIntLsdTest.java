package com.torstensommerfeld.utils.alorithms.sort.radix;

import java.util.Arrays;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class RadixSortIntLsdTest {
    @Test
    public void testSmallArray() {
        // given
        // int[] is = { 2, 1 };
        Wrapper[] is = wrap(new int[] { 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0, 11, 12, 13, 14, 15, 16, 17, 18, 19 });

        // when
        RadixSortObjectInt.sortLsd(is, o -> o.key);

        // then
        for (int i = 0; i < 20; ++i) {
            Assert.assertEquals(i, is[i].getKey());
        }
    }

    @Test
    public void testSingleton() {
        // given
        Wrapper[] is = wrap(new int[] { 1 });

        // when
        RadixSortObjectInt.sortLsd(is, o -> o.key);

        // then
        Assert.assertEquals(1, is[0].getKey());
    }

    @Test
    public void testDupplicates() {
        // given
        Wrapper[] is = wrap(new int[] { 1, 2, 1, 2 });

        // when
        RadixSortObjectInt.sortLsd(is, o -> o.key);

        // then
        Assert.assertEquals(1, is[0].getKey());
        Assert.assertEquals(1, is[1].getKey());
        Assert.assertEquals(2, is[2].getKey());
        Assert.assertEquals(2, is[3].getKey());
    }

    @Test
    public void bigTest() {
        // given
        int[] data = new int[200000];
        Random rnd = new Random(0);
        for (int i = 0; i < data.length; ++i) {
            data[i] = rnd.nextInt(1000000);
        }
        Wrapper[] is = wrap(data);

        // when

        // Wrapper[] copy = Arrays.copyOf(is, is.length);
        // long start = System.currentTimeMillis();
        RadixSortObjectInt.sortLsd(is, o -> o.key);

        // System.out.println(System.currentTimeMillis() - start);

        // then
        for (int i = 0; i < data.length - 1; ++i) {
            Assert.assertTrue(is[i].getKey() <= is[i + 1].getKey());
        }

        // start = System.currentTimeMillis();
        // Arrays.sort(copy, (o1, o2) -> o1.key - o2.key);
        // System.out.println(System.currentTimeMillis() - start);

    }

    private static Wrapper[] wrap(int[] array) {
        return Arrays.stream(array).mapToObj(Wrapper::new).toArray(count -> new Wrapper[count]);
    }

    @Getter
    @AllArgsConstructor
    private static class Wrapper {
        private int key;
    }

}
