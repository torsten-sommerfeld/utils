package com.torstensommerfeld.utils.math;

public class NumberUtil {
    public static final double DEFAULT_EPSILON = 0.00000000001;
    private static final double ONE_UPPER_BOUND = 1 + DEFAULT_EPSILON;
    private static final double ONE_LOWER_BOUND = 1 - DEFAULT_EPSILON;

    private static final double ZERO_UPPER_BOUND = +DEFAULT_EPSILON;
    private static final double ZERO_LOWER_BOUND = -DEFAULT_EPSILON;

    public static boolean isOne(double v) {
        return v > ONE_LOWER_BOUND && v < ONE_UPPER_BOUND;
    }

    public static boolean isZero(double v) {
        return v > ZERO_LOWER_BOUND && v < ZERO_UPPER_BOUND;
    }

    public static boolean isOne(double v, double epsilon) {
        return v > (1 - epsilon) && v < (1 + epsilon);
    }

    public static boolean isZero(double v, double epsilon) {
        return v > -epsilon && v < epsilon;
    }

    public static boolean isSame(double v1, double v2, double epsilon) {
        return isZero(v1 - v2, epsilon);
    }

    public static boolean isSame(double v1, double v2) {
        return isZero(v1 - v2, DEFAULT_EPSILON);
    }
}
