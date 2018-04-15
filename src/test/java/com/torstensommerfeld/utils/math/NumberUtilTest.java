package com.torstensommerfeld.utils.math;

import static com.torstensommerfeld.utils.math.NumberUtil.ANGLE_0;
import static com.torstensommerfeld.utils.math.NumberUtil.ANGLE_180;
import static com.torstensommerfeld.utils.math.NumberUtil.ANGLE_270;
import static com.torstensommerfeld.utils.math.NumberUtil.ANGLE_315;
import static com.torstensommerfeld.utils.math.NumberUtil.ANGLE_45;
import static com.torstensommerfeld.utils.math.NumberUtil.ANGLE_90;
import static com.torstensommerfeld.utils.math.NumberUtil.isCounterClockWise;

import org.junit.Assert;
import org.junit.Test;

public class NumberUtilTest {
    @Test
    public void testIsOne() {
        // when and then
        Assert.assertFalse(NumberUtil.isOne(0));
        Assert.assertTrue(NumberUtil.isOne(0.999999999999));
        Assert.assertTrue(NumberUtil.isOne(1));
        Assert.assertTrue(NumberUtil.isOne(1.000000000001));
        Assert.assertFalse(NumberUtil.isOne(2));
    }

    @Test
    public void testIsOneWithEpsilon() {
        // given
        double epsilon = 0.1;
        // when and then
        Assert.assertFalse(NumberUtil.isOne(0, epsilon));
        Assert.assertFalse(NumberUtil.isOne(0.89, epsilon));
        Assert.assertTrue(NumberUtil.isOne(0.91, epsilon));
        Assert.assertTrue(NumberUtil.isOne(1, epsilon));
        Assert.assertTrue(NumberUtil.isOne(1.09, epsilon));
        Assert.assertFalse(NumberUtil.isOne(1.11, epsilon));
    }

    @Test
    public void testIsZero() {
        // when and then
        Assert.assertFalse(NumberUtil.isZero(-0.1));
        Assert.assertTrue(NumberUtil.isZero(-0.000000000001));
        Assert.assertTrue(NumberUtil.isZero(0));
        Assert.assertTrue(NumberUtil.isZero(0.000000000001));
        Assert.assertFalse(NumberUtil.isZero(0.1));
    }

    @Test
    public void testIsZeroWithEpsilon() {
        // given
        double epsilon = 0.1;
        // when and then
        Assert.assertFalse(NumberUtil.isZero(-1, epsilon));
        Assert.assertFalse(NumberUtil.isZero(-0.11, epsilon));
        Assert.assertTrue(NumberUtil.isZero(-0.09, epsilon));
        Assert.assertTrue(NumberUtil.isZero(0, epsilon));
        Assert.assertTrue(NumberUtil.isZero(0.09, epsilon));
        Assert.assertFalse(NumberUtil.isZero(0.11, epsilon));
    }

    @Test
    public void testIsSame() {
        // when and then
        Assert.assertFalse(NumberUtil.isSame(1, 1.2));
        Assert.assertFalse(NumberUtil.isSame(1, 1.11));
        Assert.assertTrue(NumberUtil.isSame(1, 0.999999999999));
        Assert.assertTrue(NumberUtil.isSame(1, 1));
        Assert.assertTrue(NumberUtil.isSame(1, 1.000000000001));
        Assert.assertFalse(NumberUtil.isSame(1, 0.89));
    }

    @Test
    public void testIsSameWithEpsilon() {
        // given
        double epsilon = 0.1;
        // when and then
        Assert.assertFalse(NumberUtil.isSame(1, 1.2, epsilon));
        Assert.assertFalse(NumberUtil.isSame(1, 1.11, epsilon));
        Assert.assertTrue(NumberUtil.isSame(1, 1.09, epsilon));
        Assert.assertTrue(NumberUtil.isSame(1, 1, epsilon));
        Assert.assertTrue(NumberUtil.isSame(1, 0.91, epsilon));
        Assert.assertFalse(NumberUtil.isSame(1, 0.89, epsilon));
    }

    @Test
    public void testNumberToSymbol_9() {
        // given
        int number = 9;

        // when
        char result = NumberUtil.numberToSymbol(number);

        // then
        Assert.assertEquals('9', result);

    }

    @Test
    public void testNumberToSymbol_10() {
        // given
        int number = 10;

        // when
        char result = NumberUtil.numberToSymbol(number);

        // then
        Assert.assertEquals('a', result);

    }

    @Test
    public void testIsCounterClockWise() {
        Assert.assertTrue(isCounterClockWise(ANGLE_90, ANGLE_315, ANGLE_270));
        Assert.assertTrue(isCounterClockWise(ANGLE_90, ANGLE_45, ANGLE_270));
        Assert.assertTrue(isCounterClockWise(ANGLE_180, ANGLE_90, ANGLE_0));
        Assert.assertFalse(isCounterClockWise(ANGLE_270, ANGLE_315, ANGLE_90));
        Assert.assertFalse(isCounterClockWise(ANGLE_270, ANGLE_45, ANGLE_90));
        Assert.assertFalse(isCounterClockWise(ANGLE_0, ANGLE_90, ANGLE_180));
    }
}
