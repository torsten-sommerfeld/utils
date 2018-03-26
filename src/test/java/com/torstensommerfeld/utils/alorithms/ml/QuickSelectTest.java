package com.torstensommerfeld.utils.alorithms.ml;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class QuickSelectTest {
    private QuickSelect<Integer> classUnderTest;

    @Before
    public void setup() {
        classUnderTest = new QuickSelect<>(Integer::compareTo);
    }

    @Test
    public void test_selectFirstFromOrderedList4() {
        // given
        List<Integer> items = Arrays.asList(0, 1, 2, 3);
        int k = 0;

        // when
        int kthValueIndex = classUnderTest.select(items, k);

        // then
        int value = items.get(kthValueIndex);
        Assert.assertEquals(0, value);
    }

    @Test
    public void test_selectLastFromOrderedList4() {
        // given
        List<Integer> items = Arrays.asList(0, 1, 2, 3);
        int k = 3;

        // when
        int kthValueIndex = classUnderTest.select(items, k);

        // then
        int value = items.get(kthValueIndex);
        Assert.assertEquals(3, value);
    }

    @Test
    public void test_selectSecondFromOrderedList4() {
        // given
        List<Integer> items = Arrays.asList(0, 1, 2, 3);
        int k = 1;

        // when
        int kthValueIndex = classUnderTest.select(items, k);

        // then
        int value = items.get(kthValueIndex);
        Assert.assertEquals(1, value);
    }

    @Test
    public void test_selectSecondFromUnOrderedList4() {
        // given
        List<Integer> items = Arrays.asList(3, 1, 2, 4);
        int k = 1;

        // when
        int kthValueIndex = classUnderTest.select(items, k);

        // then
        int value = items.get(kthValueIndex);
        Assert.assertEquals(2, value);
    }

    @Test
    public void test_selectSecondFromUnOrderedList11() {
        // given
        List<Integer> items = Arrays.asList(9, 4, 5, 1, 2, 7, 6, 8, 10, 11, 3);
        int k = 1;

        // when
        int kthValueIndex = classUnderTest.select(items, k);

        // then
        int value = items.get(kthValueIndex);
        Assert.assertEquals(2, value);
    }

    @Test
    public void test_select3rdFromUnOrderedList13() {
        // given
        List<Integer> items = Arrays.asList(9, 4, 13, 12, 5, 1, 2, 7, 6, 8, 10, 11, 3);
        int k = 2;

        // when
        int kthValueIndex = classUnderTest.select(items, k);

        // then
        int value = items.get(kthValueIndex);
        Assert.assertEquals(3, value);
    }

    @Test
    public void test_selectMultipleFromUnOrderedLargeList() {
        // given
        Random rnd = new Random(0);
        int max = 1000;
        List<Integer> items = IntStream.range(0, max).mapToObj(Integer::valueOf).collect(Collectors.toList());
        Collections.shuffle(items, rnd);

        for (int k = 0; k < max; ++k) {
            // when
            int kthValueIndex = classUnderTest.select(items, k);

            // then
            int value = items.get(kthValueIndex);
            Assert.assertEquals(k, value);
        }

    }
}
