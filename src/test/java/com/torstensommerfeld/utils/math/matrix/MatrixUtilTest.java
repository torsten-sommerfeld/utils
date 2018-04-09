package com.torstensommerfeld.utils.math.matrix;

import org.junit.Assert;
import org.junit.Test;

import com.torstensommerfeld.utils.exceptions.InvalidMatrixOperation;
import com.torstensommerfeld.utils.math.NumberUtil;

public class MatrixUtilTest {

    @Test
    public void testTranspose() {
        // given
        Matrix m1 = new Matrix(2, 2, new double[][] { new double[] { 1, 2 }, new double[] { 3, 4 } });
        Matrix target = new Matrix(2, 2);

        // when
        Matrix result = MatrixUtil.transpose(m1, target);

        // then
        double[][] m = result.getM();
        Assert.assertEquals(1, m[0][0], 0);
        Assert.assertEquals(3, m[0][1], 0);
        Assert.assertEquals(2, m[1][0], 0);
        Assert.assertEquals(4, m[1][1], 0);
    }

    @Test(expected = InvalidMatrixOperation.class)
    public void testTranspose_invalid_target_size() {
        // given
        Matrix m1 = new Matrix(2, 2, new double[][] { new double[] { 1, 2 }, new double[] { 3, 4 } });
        Matrix target = new Matrix(1, 2);

        // when
        MatrixUtil.transpose(m1, target);
    }

    @Test
    public void testAdd() {
        // given
        Matrix m1 = new Matrix(2, 2, new double[][] { new double[] { 1, 2 }, new double[] { 3, 4 } });
        Matrix m2 = new Matrix(2, 2, new double[][] { new double[] { 4, 3 }, new double[] { 2, 1 } });
        Matrix target = new Matrix(2, 2);

        // when
        Matrix result = MatrixUtil.add(m1, m2, target);

        // then
        double[][] m = result.getM();
        Assert.assertEquals(5, m[0][0], 0);
        Assert.assertEquals(5, m[1][0], 0);
        Assert.assertEquals(5, m[0][1], 0);
        Assert.assertEquals(5, m[1][1], 0);
    }

    @Test(expected = InvalidMatrixOperation.class)
    public void testAdd_size_not_matching() {
        // given
        Matrix m1 = new Matrix(2, 2, new double[][] { new double[] { 1, 2 }, new double[] { 3, 4 } });
        Matrix m2 = new Matrix(1, 1, new double[][] { new double[] { 4 } });
        Matrix target = new Matrix(2, 2);

        // when
        MatrixUtil.add(m1, m2, target);
    }

    @Test(expected = InvalidMatrixOperation.class)
    public void testAdd_target_size_not_matching() {
        // given
        Matrix m1 = new Matrix(2, 2, new double[][] { new double[] { 1, 2 }, new double[] { 3, 4 } });
        Matrix m2 = new Matrix(2, 2, new double[][] { new double[] { 4, 3 }, new double[] { 2, 1 } });
        Matrix target = new Matrix(2, 1);

        // when
        MatrixUtil.add(m1, m2, target);
    }

    @Test
    public void testSub() {
        // given
        Matrix m1 = new Matrix(2, 2, new double[][] { new double[] { 1, 2 }, new double[] { 3, 4 } });
        Matrix m2 = new Matrix(2, 2, new double[][] { new double[] { 4, 3 }, new double[] { 2, 1 } });
        Matrix target = new Matrix(2, 2);

        // when
        Matrix result = MatrixUtil.sub(m1, m2, target);

        // then
        double[][] m = result.getM();
        Assert.assertEquals(-3, m[0][0], 0);
        Assert.assertEquals(-1, m[0][1], 0);
        Assert.assertEquals(1, m[1][0], 0);
        Assert.assertEquals(3, m[1][1], 0);
    }

    @Test(expected = InvalidMatrixOperation.class)
    public void testSub_size_not_matching() {
        // given
        Matrix m1 = new Matrix(2, 2, new double[][] { new double[] { 1, 2 }, new double[] { 3, 4 } });
        Matrix m2 = new Matrix(1, 1, new double[][] { new double[] { 4 } });
        Matrix target = new Matrix(2, 2);

        // when
        MatrixUtil.sub(m1, m2, target);
    }

    @Test(expected = InvalidMatrixOperation.class)
    public void testSub_target_size_not_matching() {
        // given
        Matrix m1 = new Matrix(2, 2, new double[][] { new double[] { 1, 2 }, new double[] { 3, 4 } });
        Matrix m2 = new Matrix(2, 2, new double[][] { new double[] { 4, 3 }, new double[] { 2, 1 } });
        Matrix target = new Matrix(2, 1);

        // when
        MatrixUtil.sub(m1, m2, target);
    }

    @Test
    public void testFactorMul() {
        // given
        Matrix m1 = new Matrix(2, 2, new double[][] { new double[] { 1, 2 }, new double[] { 3, 4 } });
        Matrix target = new Matrix(2, 2);

        // when
        Matrix result = MatrixUtil.mul(m1, 2, target);

        // then
        double[][] m = result.getM();
        Assert.assertEquals(2, m[0][0], 0);
        Assert.assertEquals(4, m[0][1], 0);
        Assert.assertEquals(6, m[1][0], 0);
        Assert.assertEquals(8, m[1][1], 0);
    }

    @Test(expected = InvalidMatrixOperation.class)
    public void testFactorMul_target_size_not_matching() {
        // given
        Matrix m1 = new Matrix(2, 2, new double[][] { new double[] { 1, 2 }, new double[] { 3, 4 } });
        Matrix target = new Matrix(1, 2);

        // when
        MatrixUtil.mul(m1, 2, target);
    }

    @Test
    public void testMatrixMul() {
        // given
        Matrix m1 = new Matrix(2, 2, new double[][] { new double[] { 1, 2 }, new double[] { 3, 4 } });
        Matrix m2 = new Matrix(2, 2, new double[][] { new double[] { 4, 3 }, new double[] { 2, 1 } });
        Matrix target = new Matrix(2, 2);

        // when
        Matrix result = MatrixUtil.mul(m1, m2, target);

        // then
        double[][] m = result.getM();
        Assert.assertEquals(8, m[0][0], 0);
        Assert.assertEquals(5, m[0][1], 0);
        Assert.assertEquals(20, m[1][0], 0);
        Assert.assertEquals(13, m[1][1], 0);
    }

    @Test(expected = InvalidMatrixOperation.class)
    public void testMatrixMul_incompatible_size() {
        // given
        Matrix m1 = new Matrix(2, 2, new double[][] { new double[] { 1, 2 }, new double[] { 3, 4 } });
        Matrix m2 = new Matrix(1, 1, new double[][] { new double[] { 4 } });
        Matrix target = new Matrix(2, 2);

        // when
        MatrixUtil.mul(m1, m2, target);
    }

    @Test(expected = InvalidMatrixOperation.class)
    public void testMatrixMul_incompatible_target_size_rows() {
        // given
        Matrix m1 = new Matrix(2, 2, new double[][] { new double[] { 1, 2 }, new double[] { 3, 4 } });
        Matrix m2 = new Matrix(2, 2, new double[][] { new double[] { 4, 3 }, new double[] { 2, 1 } });
        Matrix target = new Matrix(1, 2);

        // when
        MatrixUtil.mul(m1, m2, target);
    }

    @Test(expected = InvalidMatrixOperation.class)
    public void testMatrixMul_incompatible_target_size_cols() {
        // given
        Matrix m1 = new Matrix(2, 2, new double[][] { new double[] { 1, 2 }, new double[] { 3, 4 } });
        Matrix m2 = new Matrix(2, 2, new double[][] { new double[] { 4, 3 }, new double[] { 2, 1 } });
        Matrix target = new Matrix(2, 1);

        // when
        MatrixUtil.mul(m1, m2, target);
    }

    @Test
    public void testMatrixVectorMul() {
        // given
        Matrix m1 = new Matrix(2, 2, new double[][] { new double[] { 1, 2 }, new double[] { 3, 4 } });
        double[] v = new double[] { 1, 2 };
        double[] target = new double[2];

        // when
        double[] result = MatrixUtil.mul(m1, v, target);

        // then
        Assert.assertEquals(5, result[0], 0);
        Assert.assertEquals(11, result[1], 0);
    }

    @Test(expected = InvalidMatrixOperation.class)
    public void testMatrixVectorMul_invalid_target_size() {
        // given
        Matrix m1 = new Matrix(2, 2, new double[][] { new double[] { 1, 2 }, new double[] { 3, 4 } });
        double[] v = new double[] { 1, 2 };
        double[] target = new double[3];

        // when
        MatrixUtil.mul(m1, v, target);
    }

    @Test(expected = InvalidMatrixOperation.class)
    public void testMatrixVectorMul_invalid_vector_size() {
        // given
        Matrix m1 = new Matrix(2, 2, new double[][] { new double[] { 1, 2 }, new double[] { 3, 4 } });
        double[] v = new double[] { 1, 2, 3 };
        double[] target = new double[3];

        // when
        MatrixUtil.mul(m1, v, target);
    }

    @Test
    public void testGetDeterminant0() {
        // given
        Matrix m1 = new Matrix(0, 0, new double[][] {});

        // when
        double result = MatrixUtil.getDeterminant(m1);

        // then
        Assert.assertEquals(1, result, 0);
    }

    @Test
    public void testGetDeterminant1() {
        // given
        Matrix m1 = new Matrix(1, 1, new double[][] { new double[] { 2 } });

        // when
        double result = MatrixUtil.getDeterminant(m1);

        // then
        Assert.assertEquals(2, result, 0);
    }

    @Test
    public void testGetDeterminant2() {
        // given
        Matrix m1 = new Matrix(2, 2, new double[][] { new double[] { 3, 8 }, new double[] { 4, 6 } });

        // when
        double result = MatrixUtil.getDeterminant(m1);

        // then
        Assert.assertEquals(-14, result, 0);
    }

    @Test
    public void testGetDeterminant3() {
        // given
        Matrix m1 = new Matrix(3, 3, new double[][] { new double[] { 6, 1, 1 }, new double[] { 4, -2, 5 }, new double[] { 2, 8, 7 } });

        // when
        double result = MatrixUtil.getDeterminant(m1);

        // then
        Assert.assertEquals(-306, result, 0);
    }

    @Test
    public void testGetDeterminant4() {
        // given
        Matrix m1 = new Matrix(4, 4, new double[][] { new double[] { 3, 2, -1, 4 }, new double[] { 2, 1, 5, 7 }, new double[] { 0, 5, 2, -6 }, new double[] { -1, 2, 1, 0 } });

        // when
        double result = MatrixUtil.getDeterminant(m1);

        // then
        Assert.assertEquals(-418, result, 0);
    }

    @Test
    public void testGetDeterminant4_2() {
        // given
        Matrix m1 = new Matrix(4, 4, new double[][] { new double[] { 1, 2, 3, 4 }, new double[] { 5, 6, 7, 8 }, new double[] { 2, 6, 4, 8 }, new double[] { 3, 1, 1, 2 } });

        // when
        double result = MatrixUtil.getDeterminant(m1);

        // then
        Assert.assertEquals(72, result, 0);
    }

    @Test
    public void testGetDeterminant5() {
        // given
        Matrix m1 = new Matrix(5, 5, new double[][] { new double[] { 0, 1, 0, -2, 1 }, new double[] { 1, 0, 3, 1, 1 }, new double[] { 1, -1, 1, 1, 1 }, new double[] { 2, 2, 1, 0, 1 }, new double[] { 3, 1, 1, 1, 2 } });

        // when
        double result = MatrixUtil.getDeterminant(m1);

        // then
        Assert.assertEquals(4, result, 0);
    }

    @Test
    public void testGetDeterminant5_2() {
        // given
        Matrix m1 = new Matrix(5, 5, new double[][] { new double[] { 1, 3, 1, 1, 8 }, new double[] { 2, 4, 7, 1, 20 }, new double[] { 4, 1, 2, 3, 22 }, new double[] { 1, 2, 1, 1, 5 }, new double[] { 3, 5, 2, 3, 25 } });

        // when
        double result = MatrixUtil.getDeterminant(m1);

        // then
        Assert.assertEquals(78, result, 0);
    }

    @Test
    public void testIdentity() {

        // when
        Matrix matrix = MatrixUtil.identity(2);

        // then
        double[][] m = matrix.getM();
        Assert.assertEquals(2, matrix.getRows());
        Assert.assertEquals(2, matrix.getCols());
        Assert.assertEquals(1, m[0][0], 0);
        Assert.assertEquals(0, m[0][1], 0);
        Assert.assertEquals(0, m[1][0], 0);
        Assert.assertEquals(1, m[1][1], 0);
    }

    @Test
    public void testInverse0() {
        // given
        Matrix m1 = new Matrix(0, 0, new double[][] {});
        Matrix target = new Matrix(m1);

        // when
        Matrix result = MatrixUtil.getInverse(m1, target);

        // then
        Assert.assertSame(m1, result);
    }

    @Test
    public void testInverse1() {
        // given
        Matrix m1 = new Matrix(1, 1, new double[][] { new double[] { 2 } });
        Matrix target = new Matrix(m1);

        // when
        Matrix result = MatrixUtil.getInverse(m1, target);

        // then
        double[][] m = result.getM();
        Assert.assertEquals(0.5, m[0][0], 0);
    }

    @Test
    public void testInverse2() {
        // given
        Matrix m1 = new Matrix(2, 2, new double[][] { new double[] { 5, 2 }, new double[] { -7, -3 } });
        Matrix target = new Matrix(m1);

        // when
        Matrix result = MatrixUtil.getInverse(m1, target);

        // then
        double[][] m = result.getM();
        Assert.assertEquals(3, m[0][0], 0);
        Assert.assertEquals(2, m[0][1], 0);
        Assert.assertEquals(-7, m[1][0], 0);
        Assert.assertEquals(-5, m[1][1], 0);
    }

    @Test
    public void testInverse3() {
        // given
        Matrix m1 = new Matrix(3, 3, new double[][] { new double[] { 2, 4, 1 }, new double[] { -1, 1, -1 }, new double[] { 1, 4, 0 } });
        Matrix target = new Matrix(m1);

        // when
        Matrix result = MatrixUtil.getInverse(m1, target);

        // then
        double[][] m = result.getM();
        Assert.assertEquals(-4, m[0][0], 0);
        Assert.assertEquals(-4, m[0][1], 0);
        Assert.assertEquals(5, m[0][2], 0);
        Assert.assertEquals(1, m[1][0], 0);
        Assert.assertEquals(1, m[1][1], 0);
        Assert.assertEquals(-1, m[1][2], 0);
        Assert.assertEquals(5, m[2][0], 0);
        Assert.assertEquals(4, m[2][1], 0);
        Assert.assertEquals(-6, m[2][2], 0);
    }

    @Test
    public void testInverse3_2() {
        // given
        Matrix m1 = new Matrix(3, 3, new double[][] { new double[] { 2, 1, 1 }, new double[] { -5, -3, 0 }, new double[] { 1, 1, -1 } });
        Matrix target = new Matrix(m1);

        // when
        Matrix result = MatrixUtil.getInverse(m1, target);

        // then
        double[][] m = result.getM();
        Assert.assertEquals(-3, m[0][0], 0);
        Assert.assertEquals(-2, m[0][1], 0);
        Assert.assertEquals(-3, m[0][2], 0);
        Assert.assertEquals(5, m[1][0], 0);
        Assert.assertEquals(3, m[1][1], 0);
        Assert.assertEquals(5, m[1][2], 0);
        Assert.assertEquals(2, m[2][0], 0);
        Assert.assertEquals(1, m[2][1], 0);
        Assert.assertEquals(1, m[2][2], 0);
    }

    @Test
    public void testInverse4() {
        // given
        Matrix m1 = new Matrix(4, 4, new double[][] { new double[] { 1, 2, 3, 4 }, new double[] { 0, 2, 1, 3 }, new double[] { -1, 1, 1, -2 }, new double[] { 2, 4, 3, 6 } });
        Matrix target = new Matrix(m1);

        // when
        Matrix result = MatrixUtil.getInverse(m1, target);

        // then
        double[][] m = result.getM();
        Assert.assertEquals(-7 / 25D, m[0][0], 0);
        Assert.assertEquals(-22 / 25D, m[0][1], 0);
        Assert.assertEquals(-2 / 25D, m[0][2], 0);
        Assert.assertEquals(3 / 5D, m[0][3], 0);
        Assert.assertEquals(-13 / 25D, m[1][0], 0);
        Assert.assertEquals(2 / 25D, m[1][1], 0);
        Assert.assertEquals(7 / 25D, m[1][2], 0);
        Assert.assertEquals(2 / 5D, m[1][3], 0);
        Assert.assertEquals(14 / 25D, m[2][0], 0);
        Assert.assertEquals(-6 / 25D, m[2][1], 0);
        Assert.assertEquals(4 / 25D, m[2][2], 0);
        Assert.assertEquals(-1 / 5D, m[2][3], 0);
        Assert.assertEquals(4 / 25D, m[3][0], 0);
        Assert.assertEquals(9 / 25D, m[3][1], 0);
        Assert.assertEquals(-6 / 25D, m[3][2], 0);
        Assert.assertEquals(-1 / 5D, m[3][3], 0);
    }

    @Test
    public void testMutableTranspose() {
        // given
        Matrix m1 = new Matrix(2, 2, new double[][] { new double[] { 1, 2 }, new double[] { 3, 4 } });

        // when
        Matrix result = MatrixUtil.transpose(m1);

        // then
        double[][] m = result.getM();
        Assert.assertEquals(1, m[0][0], 0);
        Assert.assertEquals(3, m[0][1], 0);
        Assert.assertEquals(2, m[1][0], 0);
        Assert.assertEquals(4, m[1][1], 0);
    }

    @Test
    public void testIdentity_yes() {

        // given
        Matrix matrix = MatrixUtil.identity(2);

        // when
        boolean result = MatrixUtil.isIdentity(matrix);

        // then
        Assert.assertTrue(result);
    }

    @Test
    public void testIdentity_no1() {

        // given
        Matrix matrix = MatrixUtil.identity(2);
        matrix.getM()[0][0] = 1.1;

        // when
        boolean result = MatrixUtil.isIdentity(matrix);

        // then
        Assert.assertFalse(result);
    }

    @Test
    public void testIdentity_no2() {

        // given
        Matrix matrix = MatrixUtil.identity(2);
        matrix.getM()[1][0] = 1.1;

        // when
        boolean result = MatrixUtil.isIdentity(matrix);

        // then
        Assert.assertFalse(result);
    }

    @Test
    public void testSolve1() {

        // given
        Matrix m1 = new Matrix(1, 1, new double[][] { new double[] { 2 } });
        double v1[] = new double[] { 2 };
        double t[] = new double[1];

        // when
        double[] result = MatrixUtil.solve(m1, v1, t);

        // then
        Assert.assertEquals(1, result[0], NumberUtil.DEFAULT_EPSILON);
    }

    @Test
    public void testSolve2() {

        // given
        Matrix m1 = new Matrix(2, 2, new double[][] { new double[] { 1, 1 }, new double[] { 1, 7 } });
        double v1[] = new double[] { 2, 8 };
        double t[] = new double[2];

        // when
        double[] result = MatrixUtil.solve(m1, v1, t);

        // then
        Assert.assertEquals(1, result[0], NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(1, result[1], NumberUtil.DEFAULT_EPSILON);
    }

    @Test
    public void testSolve3() {

        // given
        Matrix m1 = new Matrix(3, 3, new double[][] { new double[] { 1, 1, 1 }, new double[] { 1, 7, 7 }, new double[] { 4, 2, 6 } });
        double v1[] = new double[] { 2, 8, 2 };
        double t[] = new double[3];

        // when
        double[] result = MatrixUtil.solve(m1, v1, t);

        // then
        Assert.assertEquals(1, result[0], NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(2, result[1], NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(-1, result[2], NumberUtil.DEFAULT_EPSILON);
    }

    @Test
    public void testSolve4() {

        // given
        Matrix m1 = new Matrix(4, 4, new double[][] { new double[] { 1, 1, 1, 1 }, new double[] { 1, 7, 7, 1 }, new double[] { 4, 2, 6, 8 }, new double[] { 2, 4, 4, 0 } });
        double v1[] = new double[] { 2, 8, 2, 4 };
        double t[] = new double[4];

        // when
        double[] result = MatrixUtil.solve(m1, v1, t);

        // then
        Assert.assertEquals(0, result[0], NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(3, result[1], NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(-2, result[2], NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(1, result[3], NumberUtil.DEFAULT_EPSILON);
    }

    @Test
    public void testSolve4_2() {

        // given
        Matrix m1 = new Matrix(4, 4, new double[][] { new double[] { 0, 1, 1, 0 }, new double[] { 1, 7, 7, 1 }, new double[] { 4, 2, 6, 8 }, new double[] { 0, 3, 4, 0 } });
        double v1[] = new double[] { 2, 8, 2, 4 };
        double t[] = new double[4];

        // when
        double[] result = MatrixUtil.solve(m1, v1, t);

        // then
        Assert.assertEquals(-13.5, result[0], NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(4, result[1], NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(-2, result[2], NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(7.5, result[3], NumberUtil.DEFAULT_EPSILON);
    }

    @Test
    public void testSolve4_3() {

        // given
        Matrix m1 = new Matrix(4, 4, new double[][] { new double[] { 0, 1, 1, 0 }, new double[] { 0, 0, 6, 1 }, new double[] { 4, 0, 6, 0 }, new double[] { 0, 3, 4, 0 } });
        double v1[] = new double[] { 0, 0, 2, 4 };
        double t[] = new double[4];

        // when
        double[] result = MatrixUtil.solve(m1, v1, t);

        // then
        Assert.assertEquals(-5.5, result[0], NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(-4, result[1], NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(4, result[2], NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(-24, result[3], NumberUtil.DEFAULT_EPSILON);
    }

    @Test
    public void testSolve4_4() {

        // given
        Matrix m1 = new Matrix(4, 4, new double[][] { new double[] { 0, 0, 1, 0 }, new double[] { 0, 0, 0, 1 }, new double[] { 1, 0, 0, 0 }, new double[] { 0, 1, 0, 0 } });
        double v1[] = new double[] { 0, 0, 1, 0 };
        double t[] = new double[4];

        // when
        double[] result = MatrixUtil.solve(m1, v1, t);

        // then
        Assert.assertEquals(1, result[0], NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(0, result[1], NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(0, result[2], NumberUtil.DEFAULT_EPSILON);
        Assert.assertEquals(0, result[3], NumberUtil.DEFAULT_EPSILON);
    }

    @Test
    public void solveGaussianElimination1x1() {
        // given
        Matrix m1 = new Matrix(1, 1, new double[][] { new double[] { 2 } });
        double v1[] = new double[] { 2 };
        double t[] = new double[v1.length];
        double[] expected = MatrixUtil.solve(m1, v1, t);

        // when
        EquationSolution result = MatrixUtil.solveGaussianElimination(m1, v1);

        // then
        Assert.assertTrue(result.isValid());
        Assert.assertTrue(result.isUnique());
        for (int i = 0; i < expected.length; ++i) {
            Assert.assertEquals(expected[i], result.getSolution()[i], NumberUtil.DEFAULT_EPSILON);
        }
    }

    @Test
    public void solveGaussianElimination2x2() {
        // given
        Matrix m1 = new Matrix(2, 2, new double[][] { new double[] { 1, 1 }, new double[] { 1, 7 } });
        double v1[] = new double[] { 2, 8 };
        double t[] = new double[v1.length];
        double[] expected = MatrixUtil.solve(m1, v1, t);

        // when
        EquationSolution result = MatrixUtil.solveGaussianElimination(m1, v1);

        // then
        Assert.assertTrue(result.isValid());
        Assert.assertTrue(result.isUnique());
        for (int i = 0; i < expected.length; ++i) {
            Assert.assertEquals(expected[i], result.getSolution()[i], NumberUtil.DEFAULT_EPSILON);
        }
    }

    @Test
    public void solveGaussianElimination3x3() {
        // given
        Matrix m1 = new Matrix(3, 3, new double[][] { new double[] { 1, 1, 1 }, new double[] { 1, 7, 7 }, new double[] { 4, 2, 6 } });
        double v1[] = new double[] { 2, 8, 2 };
        double t[] = new double[v1.length];
        double[] expected = MatrixUtil.solve(m1, v1, t);

        // when
        EquationSolution result = MatrixUtil.solveGaussianElimination(m1, v1);

        // then
        Assert.assertTrue(result.isValid());
        Assert.assertTrue(result.isUnique());
        for (int i = 0; i < expected.length; ++i) {
            Assert.assertEquals(expected[i], result.getSolution()[i], NumberUtil.DEFAULT_EPSILON);
        }
    }

    @Test
    public void solveGaussianElimination4x4() {
        // given
        Matrix m1 = new Matrix(4, 4, new double[][] { new double[] { 1, 1, 1, 1 }, new double[] { 1, 7, 7, 1 }, new double[] { 4, 2, 6, 8 }, new double[] { 2, 4, 4, 0 } });
        double v1[] = new double[] { 2, 8, 2, 4 };
        double t[] = new double[v1.length];
        double[] expected = MatrixUtil.solve(m1, v1, t);

        // when
        EquationSolution result = MatrixUtil.solveGaussianElimination(m1, v1);

        // then
        Assert.assertTrue(result.isValid());
        Assert.assertTrue(result.isUnique());
        for (int i = 0; i < expected.length; ++i) {
            Assert.assertEquals(expected[i], result.getSolution()[i], NumberUtil.DEFAULT_EPSILON);
        }
    }

    @Test
    public void solveGaussianElimination4x4_2() {
        // given
        Matrix m1 = new Matrix(4, 4, new double[][] { new double[] { 0, 1, 1, 0 }, new double[] { 1, 7, 7, 1 }, new double[] { 4, 2, 6, 8 }, new double[] { 0, 3, 4, 0 } });
        double v1[] = new double[] { 2, 8, 2, 4 };
        double t[] = new double[v1.length];
        double[] expected = MatrixUtil.solve(m1, v1, t);

        // when
        EquationSolution result = MatrixUtil.solveGaussianElimination(m1, v1);

        // then
        Assert.assertTrue(result.isValid());
        Assert.assertTrue(result.isUnique());
        for (int i = 0; i < expected.length; ++i) {
            Assert.assertEquals(expected[i], result.getSolution()[i], NumberUtil.DEFAULT_EPSILON);
        }
    }

    @Test
    public void solveGaussianElimination4x4_3() {
        // given
        Matrix m1 = new Matrix(4, 4, new double[][] { new double[] { 0, 1, 1, 0 }, new double[] { 0, 0, 6, 1 }, new double[] { 4, 0, 6, 0 }, new double[] { 0, 3, 4, 0 } });
        double v1[] = new double[] { 0, 0, 2, 4 };
        double t[] = new double[v1.length];
        double[] expected = MatrixUtil.solve(m1, v1, t);

        // when
        EquationSolution result = MatrixUtil.solveGaussianElimination(m1, v1);

        // then
        Assert.assertTrue(result.isValid());
        Assert.assertTrue(result.isUnique());
        for (int i = 0; i < expected.length; ++i) {
            Assert.assertEquals(expected[i], result.getSolution()[i], NumberUtil.DEFAULT_EPSILON);
        }
    }

    @Test
    public void solveGaussianElimination4x4_4() {
        // given
        Matrix m1 = new Matrix(4, 4, new double[][] { new double[] { 0, 0, 1, 0 }, new double[] { 0, 0, 0, 1 }, new double[] { 1, 0, 0, 0 }, new double[] { 0, 1, 0, 0 } });
        double v1[] = new double[] { 0, 0, 1, 0 };
        double t[] = new double[v1.length];
        double[] expected = MatrixUtil.solve(m1, v1, t);

        // when
        EquationSolution result = MatrixUtil.solveGaussianElimination(m1, v1);

        // then
        Assert.assertTrue(result.isValid());
        Assert.assertTrue(result.isUnique());
        for (int i = 0; i < expected.length; ++i) {
            Assert.assertEquals(expected[i], result.getSolution()[i], NumberUtil.DEFAULT_EPSILON);
        }
    }

    @Test
    public void solveGaussianElimination2x2_degenerated() {
        // given
        Matrix m1 = new Matrix(2, 2, new double[][] { new double[] { 1, 1 }, new double[] { 1, 1 } });
        double v1[] = new double[] { 2, 8 };

        // when
        EquationSolution result = MatrixUtil.solveGaussianElimination(m1, v1);

        // then
        Assert.assertFalse(result.isValid());
        Assert.assertFalse(result.isUnique());
        Assert.assertEquals(1, result.getRank());
    }

    @Test
    public void solveGaussianElimination2x3_multiple_solutions() {
        // given
        Matrix m1 = new Matrix(2, 3, new double[][] { new double[] { 1, 0, 1 }, new double[] { 1, 3, 2 } });
        double v1[] = new double[] { 3, 2 };

        // when
        EquationSolution result = MatrixUtil.solveGaussianElimination(m1, v1);

        // then
        Assert.assertTrue(result.isValid());
        Assert.assertTrue(result.isUnique());
        Assert.assertEquals(2, result.getRank());
    }

    @Test
    public void solveGaussianElimination3x2_multiple_solutions() {
        // given
        Matrix m1 = new Matrix(3, 2, new double[][] { new double[] { 1, 0 }, new double[] { 1, 3 }, new double[] { 2, 6 } });
        double v1[] = new double[] { 3, 2, 4 };

        // when
        EquationSolution result = MatrixUtil.solveGaussianElimination(m1, v1);

        // then
        Assert.assertTrue(result.isValid());
        Assert.assertFalse(result.isUnique());
        Assert.assertEquals(2, result.getRank());
    }

    @Test
    public void solveGaussianElimination3x2_multiple_solutions_invalid() {
        // given
        Matrix m1 = new Matrix(3, 2, new double[][] { new double[] { 1, 0 }, new double[] { 1, 3 }, new double[] { 2, 6 } });
        double v1[] = new double[] { 3, 2, 5 };

        // when
        EquationSolution result = MatrixUtil.solveGaussianElimination(m1, v1);

        // then
        Assert.assertFalse(result.isValid());
        Assert.assertFalse(result.isUnique());
        Assert.assertEquals(2, result.getRank());
    }

    @Test
    public void testToString_equationSolution() {
        // given
        Matrix m1 = new Matrix(3, 2, new double[][] { new double[] { 1, 0 }, new double[] { 1, 3 }, new double[] { 2, 6 } });
        double v1[] = new double[] { 3, 2, 5 };
        EquationSolution equationSolution = MatrixUtil.solveGaussianElimination(m1, v1);

        // when
        String result = equationSolution.toString();

        // then
        Assert.assertNotNull(result);
    }

    @Test
    public void testEquals_yes() {
        // given
        Matrix m1 = new Matrix(3, 2, new double[][] { new double[] { 1, 0 }, new double[] { 1, 3 }, new double[] { 2, 6 } });
        Matrix m2 = new Matrix(3, 2, new double[][] { new double[] { 1, 0 }, new double[] { 1, 3 }, new double[] { 2, 6 } });

        // when
        boolean result = MatrixUtil.equals(m1.getM(), m2.getM(), NumberUtil.DEFAULT_EPSILON);

        // then
        Assert.assertTrue(result);
    }

    @Test
    public void testEquals_no_value() {
        // given
        Matrix m1 = new Matrix(3, 2, new double[][] { new double[] { 1, 1 }, new double[] { 1, 3 }, new double[] { 2, 6 } });
        Matrix m2 = new Matrix(3, 2, new double[][] { new double[] { 1, 0 }, new double[] { 1, 3 }, new double[] { 2, 6 } });

        // when
        boolean result = MatrixUtil.equals(m1.getM(), m2.getM(), NumberUtil.DEFAULT_EPSILON);

        // then
        Assert.assertFalse(result);
    }

    @Test
    public void testEquals_no_rows() {
        // given
        Matrix m1 = new Matrix(3, 2, new double[][] { new double[] { 1, 1 }, new double[] { 1, 3 }, new double[] { 2, 6 } });
        Matrix m2 = new Matrix(2, 2, new double[][] { new double[] { 1, 0 }, new double[] { 1, 3 } });

        // when
        boolean result = MatrixUtil.equals(m1.getM(), m2.getM(), NumberUtil.DEFAULT_EPSILON);

        // then
        Assert.assertFalse(result);
    }

    @Test
    public void testEquals_no_cols() {
        // given
        Matrix m1 = new Matrix(3, 2, new double[][] { new double[] { 1, 1 }, new double[] { 1, 3 }, new double[] { 2, 6 } });
        Matrix m2 = new Matrix(3, 1, new double[][] { new double[] { 1 }, new double[] { 1 }, new double[] { 2 } });

        // when
        boolean result = MatrixUtil.equals(m1.getM(), m2.getM(), NumberUtil.DEFAULT_EPSILON);

        // then
        Assert.assertFalse(result);
    }

}
