package com.torstensommerfeld.utils.math;

import org.junit.Assert;
import org.junit.Test;

public class MathUtilTest {

    @Test
    public void testSqr() {
        Assert.assertEquals(4, MathUtil.sqr(2));
        Assert.assertEquals(4l, MathUtil.sqr(2l));
        Assert.assertEquals(4.0, MathUtil.sqr(2.0), 0);
        Assert.assertEquals(4F, MathUtil.sqr(2F), 0);
    }
}
