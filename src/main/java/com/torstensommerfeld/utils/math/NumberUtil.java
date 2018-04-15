package com.torstensommerfeld.utils.math;

public class NumberUtil {
    public static final double DEFAULT_EPSILON = 0.00000000001;
    public static final double HALF_PI = Math.PI / 2; // 90
    public static final double THREE_HALF_PI = 3 * Math.PI / 2; // 270
    public static final double TWO_PI = Math.PI * 2; // 360

    public static final double ANGLE_NEG_360 = -8 * Math.PI / 4;
    public static final double ANGLE_NEG_315 = -7 * Math.PI / 4;
    public static final double ANGLE_NEG_270 = -6 * Math.PI / 4;
    public static final double ANGLE_NEG_225 = -5 * Math.PI / 4;
    public static final double ANGLE_NEG_180 = -4 * Math.PI / 4;
    public static final double ANGLE_NEG_135 = -3 * Math.PI / 4;
    public static final double ANGLE_NEG_90 = -2 * Math.PI / 4;
    public static final double ANGLE_NEG_45 = -1 * Math.PI / 4;

    public static final double ANGLE_0 = 0 * Math.PI / 4;
    public static final double ANGLE_45 = 1 * Math.PI / 4;
    public static final double ANGLE_90 = 2 * Math.PI / 4;
    public static final double ANGLE_135 = 3 * Math.PI / 4;
    public static final double ANGLE_180 = 4 * Math.PI / 4;
    public static final double ANGLE_225 = 5 * Math.PI / 4;
    public static final double ANGLE_270 = 6 * Math.PI / 4;
    public static final double ANGLE_315 = 7 * Math.PI / 4;
    public static final double ANGLE_360 = 8 * Math.PI / 4;

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

    public static char numberToSymbol(int n) {
        return (char) (n < 10 ? '0' + n : 'a' + n - 10);
    }

    public static boolean isCounterClockWise(double startAngle, double middleAngle, double endAngle) {
        return (startAngle < middleAngle && startAngle < endAngle && middleAngle > endAngle) || //
                (startAngle > middleAngle && startAngle < endAngle && middleAngle < endAngle) || //
                (startAngle > middleAngle && startAngle > endAngle && middleAngle > endAngle) //
        ;
    }
}
