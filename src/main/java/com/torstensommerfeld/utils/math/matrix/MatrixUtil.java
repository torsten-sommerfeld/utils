package com.torstensommerfeld.utils.math.matrix;

import java.util.Arrays;

import com.torstensommerfeld.utils.exceptions.InvalidMatrixOperation;
import com.torstensommerfeld.utils.math.NumberUtil;

public class MatrixUtil {

    private static final Matrix COFACTOR_MATRIX_OF_DEGREE_1 = new Matrix(1, 1, new double[][] { new double[] { 1 } });;

    public static Matrix transpose(Matrix source, Matrix target) {
        checkDimensions("source", source, target);

        int rows = source.getRows();
        int cols = source.getCols();
        double[][] m = source.getM();
        double[][] t = target.getM();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                t[col][row] = m[row][col];
            }
        }
        return target;
    }

    public static Matrix add(Matrix source1, Matrix source2, Matrix target) {
        checkDimensions("source1", source1, target);
        checkDimensions("source2", source2, target);

        double[][] m1 = source1.getM();
        double[][] m2 = source2.getM();
        double[][] t = target.getM();

        for (int row = target.getRows() - 1; row >= 0; --row) {
            for (int col = target.getCols() - 1; col >= 0; --col) {
                t[row][col] = m1[row][col] + m2[row][col];
            }
        }
        return target;
    }

    public static Matrix sub(Matrix source1, Matrix source2, Matrix target) {
        checkDimensions("source1", source1, target);
        checkDimensions("source2", source2, target);

        double[][] m1 = source1.getM();
        double[][] m2 = source2.getM();
        double[][] t = target.getM();

        for (int row = target.getRows() - 1; row >= 0; --row) {
            for (int col = target.getCols() - 1; col >= 0; --col) {
                t[row][col] = m1[row][col] - m2[row][col];
            }
        }
        return target;
    }

    public static Matrix mul(Matrix source, double factor, Matrix target) {
        checkDimensions("source", source, target);

        double[][] m = source.getM();
        double[][] t = target.getM();

        for (int row = target.getRows() - 1; row >= 0; --row) {
            for (int col = target.getCols() - 1; col >= 0; --col) {
                t[row][col] = m[row][col] * factor;
            }
        }
        return target;
    }

    public static Matrix mul(Matrix source1, Matrix source2, Matrix target) {
        int m1Rows = source1.getRows();
        int m1Cols = source1.getCols();

        int m2Rows = source2.getRows();
        int m2Cols = source2.getRows();

        if (m1Cols != m2Rows) {
            throw new InvalidMatrixOperation("Cannot multiply %s with %s because their sizes are not compatible", source1, source2);
        }
        if (target.getRows() != m1Rows || target.getCols() != m2Cols) {
            throw new InvalidMatrixOperation("Provided target matrix cannot store the multiplication of %s * %s", source1, source2);
        }

        double[][] m1 = source1.getM();
        double[][] m2 = source2.getM();

        double[][] t = target.getM();
        for (int row = 0; row < m1Rows; row++) {
            for (int col = 0; col < m2Cols; col++) {
                double d = 0;
                for (int i = 0; i < m1Cols; ++i) {
                    d += m1[row][i] * m2[i][col];
                }
                t[row][col] = d;
            }
        }
        return target;
    }

    /**
     * Special version of the matrix multiplication returning a vector instead of a matrix
     *
     * @param m
     *            the matrix
     * @param v
     *            the vector
     * @return m*v
     */
    public static double[] mul(Matrix source, double[] v, double[] target) {
        if (v.length != target.length) {
            throw new InvalidMatrixOperation("Target vector and factor vector need to have the same size but their sizes are %d and %d", v.length, target.length);
        }

        int m1Rows = source.getRows();
        int m1Cols = source.getCols();

        if (m1Cols != v.length) {
            throw new InvalidMatrixOperation("Cannot multiply %s with [%s] because their sizes are not compatible", source, Arrays.toString(v));
        }

        double[][] m = source.getM();

        for (int row = 0; row < m1Rows; row++) {
            double d = 0;
            for (int i = 0; i < m1Cols; ++i) {
                d += m[row][i] * v[i];
            }
            target[row] = d;
        }
        return target;
    }

    /**
     * this only works for square matrices
     */
    public static double getDeterminant(Matrix source) {
        int size = source.getRows();
        double[][] m = source.getM();
        switch (size) {
        case 0:
            return 1;
        case 1:
            return m[0][0];
        case 2:
            return m[0][0] * m[1][1] - m[1][0] * m[0][1];
        case 3:
            double a11 = m[0][0];
            double a21 = m[1][0];
            double a31 = m[2][0];
            double a12 = m[0][1];
            double a22 = m[1][1];
            double a32 = m[2][1];
            double a13 = m[0][2];
            double a23 = m[1][2];
            double a33 = m[2][2];
            return a11 * a22 * a33 + a12 * a23 * a31 + a13 * a21 * a32 - a13 * a22 * a31 - a12 * a21 * a33 - a11 * a23 * a32;

        default:
            return getDeterminantUsingLaplacesFormula(source);
        }
    }

    /**
     * this only works for square matrices
     */
    public static double getDeterminantUsingLaplacesFormula(Matrix source) {
        int size = source.getRows();
        double[][] m = source.getM();
        // this is slow
        double d = 0;
        Matrix minor = new Matrix(size - 1, size - 1);
        // sum sub determinants
        double sign = 1;
        for (int i = 0; i < size; ++i) {
            getMinor(source, 0, i, minor);
            d += m[0][i] * getDeterminant(minor) * sign;
            sign *= -1;
        }
        return d;
    }

    public static Matrix identity(int size) {
        Matrix matrix = new Matrix(size, size);
        double[][] m = matrix.getM();
        for (int i = 0; i < size; ++i) {
            m[i][i] = 1;
        }
        return matrix;
    }

    /*-
     * returns the cofactor matrix "cof" of the given matrix "source"
     * each cofactor a_ij can be calculated by a_ij = (-1)^(i+j) * det(A_ij)
     * where i is the row index
     *       j is the col index
     *       det(x) is the determinant of x
     *       and A_ij is the minor matrix created by removing the row i and the column j from the given matrix
     * @param matrix
     * @return
     */
    public static Matrix getCofactorMatrix(Matrix source, Matrix target) {
        checkDimensions("source", source, target);

        int rows = source.getRows();

        if (rows < 1) {
            return source;
        }

        if (rows == 1) {
            return COFACTOR_MATRIX_OF_DEGREE_1;
        }

        int cols = source.getCols();
        double[][] t = target.getM();

        Matrix minor = new Matrix(rows - 1, cols - 1);
        for (int row = 0; row < rows; ++row) {
            for (int col = 0; col < cols; ++col) {
                getMinor(source, row, col, minor);
                int sign = 1 - ((col + row) % 2) * 2;
                t[row][col] = sign * getDeterminant(minor);
            }
        }
        return target;

    }

    /**
     * Writes the minor matrix of given matrix to the buffer "minorMatrix" by removing the the col'th column and the row'th row from the given matrix "matrix"
     *
     * @param matrix
     * @param col
     * @param row
     * @param minorMatrixBuffer
     */
    public static void getMinor(Matrix source, int row, int col, Matrix minorMatrix) {

        int rows = minorMatrix.getRows();
        if (rows < 1) {
            return;
        }
        int cols = minorMatrix.getCols();

        double[][] mirrorMatrixM = minorMatrix.getM();
        double[][] m = source.getM();
        for (int c = 0; c < cols; ++c) {
            int mappedC = c < col ? c : c + 1;
            for (int r = 0; r < rows; ++r) {
                int mappedR = r < row ? r : r + 1;
                mirrorMatrixM[r][c] = m[mappedR][mappedC];
            }
        }
    }

    /**
     * this only works for square matrices
     */
    public static Matrix getInverse(Matrix source, Matrix target) {
        checkDimensions("source", source, target);

        int size = source.getRows();
        double[][] m = source.getM();
        double[][] t = target.getM();
        switch (size) {
        case 0:
            return source;
        case 1: {
            t[0][0] = 1 / m[0][0];
            return target;
        }
        case 2: {
            double a = m[0][0];
            double b = m[1][0];
            double c = m[0][1];
            double d = m[1][1];
            double det = a * d - b * c;
            t[0][0] = d / det;
            t[1][0] = -b / det;
            t[0][1] = -c / det;
            t[1][1] = a / det;
            return target;
        }
        case 3:
            double det = getDeterminant(source);
            double a = m[0][0];
            double b = m[0][1];
            double c = m[0][2];
            double d = m[1][0];
            double e = m[1][1];
            double f = m[1][2];
            double g = m[2][0];
            double h = m[2][1];
            double i = m[2][2];
            t[0][0] = (e * i - f * h) / det;
            t[1][0] = (f * g - d * i) / det;
            t[2][0] = (d * h - e * g) / det;
            t[0][1] = (c * h - b * i) / det;
            t[1][1] = (a * i - c * g) / det;
            t[2][1] = (b * g - a * h) / det;
            t[0][2] = (b * f - c * e) / det;
            t[1][2] = (c * d - a * f) / det;
            t[2][2] = (a * e - b * d) / det;
            return target;
        default:
            return getInverseUsingItsAdjugate(source, target);
        }
    }

    /*-
     * Calculates the inverse of matrix "matrix": A^-1 = 1 / det(A) * adj(A)
     * where A^-1 is the inverse matrix
     *       det(x) is the determinante of x
     *       adj(A) is the transposed cofactor matrix of A and called Adjugate
     * @param matrix
     * @return
     */
    public static Matrix getInverseUsingItsAdjugate(Matrix source, Matrix target) {
        double det = getDeterminant(source);
        Matrix cofactorMatrix = getCofactorMatrix(source, new Matrix(source));
        Matrix adjugae = transpose(cofactorMatrix, target);
        return mul(adjugae, 1d / det);
    }

    /**
     * This calculates the transpose of a square matrix by mutating it.
     *
     * @param source
     * @return source (for chaining)
     */
    public static Matrix transpose(Matrix matrix) {
        int size = matrix.getRows();
        double old;
        double[][] m = matrix.getM();
        for (int i = 1; i < size; ++i) {
            for (int j = 1; j <= i; ++j) {
                old = m[i - j][i];
                m[i - j][j] = m[i][i - j];
                m[i][i - j] = old;
            }
        }
        return matrix;
    }

    /**
     * This multiplies each value if the matrix "source" with the factor "factor"
     *
     * @param source
     * @param factor
     * @return source (for chaining)
     */
    public static Matrix mul(Matrix source, double factor) {
        int rows = source.getRows();
        int cols = source.getCols();
        double[][] m = source.getM();
        for (int row = 0; row < rows; ++row) {
            for (int col = 0; col < cols; ++col) {
                m[row][col] = m[row][col] * factor;
            }
        }
        return source;
    }

    public static boolean isIdentity(Matrix source) {
        int rows = source.getRows();
        int cols = source.getCols();
        double[][] m = source.getM();
        for (int row = 0; row < rows; ++row) {
            for (int col = 0; col < cols; ++col) {
                double v = m[row][col];
                if (row == col) {
                    if (!NumberUtil.isOne(v)) {
                        return false;
                    }
                } else if (!NumberUtil.isZero(v)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Solves the linear equation A*x = b It calculates the solution by: x = A^-1 * b For now this method may fail if the solution is ambiguous meaning that the inverse of a does not exist
     *
     * @param m
     * @return
     */
    public static double[] solve(Matrix A, double[] b, double[] target) {
        Matrix inverse = getInverse(A, new Matrix(A));
        return mul(inverse, b, target);
    }

    public static EquationSolution solveGaussianElimination(Matrix a, double[] b) {
        return solveGaussianElimination(a, b, NumberUtil.DEFAULT_EPSILON);
    }

    public static EquationSolution solveGaussianElimination(Matrix a, double[] b, double epsilon) {
        int rows = a.getRows();
        if (rows == 0) {
            // empty solution
            return null;
        }
        int cols = a.getCols();
        int maxRank = Math.min(rows, cols);

        // first we need a mapping vector which maps the row index of the solution to the original order
        // the order gets changed if the diagonal element of the current row is 0
        int[] rowMapping = new int[rows];
        // initial row mapping is the identity mapping
        for (int i = 0; i < rows; i++) {
            rowMapping[i] = i;
        }
        // same goes for column mapping
        int[] colMapping = new int[cols];
        // initial row mapping is the identity mapping
        for (int i = 0; i < cols; i++) {
            colMapping[i] = i;
        }

        // now create a copy of the matrix because we need to change its values
        Matrix copy = copy(a);
        double[][] m = copy.getM();
        // we need to copy b as well
        double[] c = Arrays.copyOf(b, b.length);

        // now it is time to start the forward elimination to reshape the matrix
        // into its equivalent row echelon form,
        int rank = 0;
        for (int i = 0; i < maxRank - 1; ++i) {
            // first we need to check if the i'th diagonal element is non-null;
            // if so we need to swap rows (or cols)
            boolean gotPivo = selectSuitablePivoElement(copy, c, i, rowMapping, colMapping, epsilon);
            if (!gotPivo) {
                // the matrix is rank deficient
                // this means we have potentially infinite number of solutions
                break;
            }
            // secondly update add a multiplication of the pivo row to all rows
            // be low the pivo row in a way that all elements below the pivo
            // element become 0; this will eventually lead to the
            for (int j = i + 1; j < rows; ++j) {
                eliminateRow(copy, c, i, j);
            }
            // keep track of the rank of the matrix
            rank = i + 1;
        }
        // update rank with the last row
        double lastPivo = m[maxRank - 1][maxRank - 1];
        if (!NumberUtil.isZero(lastPivo, epsilon)) {
            ++rank;
        } else if (rank < rows - 1) {
            // we might just have a bad row so swap it
            boolean gotPivo = selectSuitablePivoElement(copy, c, rank, rowMapping, colMapping, epsilon);
            if (gotPivo) {
                ++rank;
            }
        }
        // now its time for the back substitution
        for (int i = rank - 1; i > 0; --i) {
            // no need to re-evaluate the pivo elements, they must be suitable
            // at this stage
            for (int j = 0; j < i; ++j) {
                eliminateRow(copy, c, i, j);
            }
        }
        // third we need to multiply the rows in a way that they become 1;
        for (int i = 0; i < rank; ++i) {
            // note again, the pivo element m[i][i] cannot be null anymore at
            // this stage
            double pivo = m[i][i];
            c[i] = c[i] / pivo;
            // normalize row
            for (int col = i; col < cols; ++col) {
                m[i][col] = m[i][col] / pivo;
            }
        }
        // the result is now stored in c
        // we need to order c using the row mapping
        for (int i = 0; i < rows; ++i) {
            int mapping = rowMapping[i];
            if (mapping != i) {
                swap(rowMapping, i, mapping);
            }
        }
        return new EquationSolution(rank, m, c, a, b, epsilon);
    }

    private static void eliminateRow(Matrix matrix, double[] b, int pivoRow, int targetRow) {
        int cols = matrix.getCols();
        double[][] m = matrix.getM();
        double pivoElement = m[pivoRow][pivoRow];
        double factor = -m[targetRow][pivoRow] / pivoElement;
        // System.out.println("pivo: " + pivoElement + " factor: " + factor);
        for (int col = pivoRow; col < cols; ++col) {
            m[targetRow][col] = m[targetRow][col] + factor * m[pivoRow][col];
        }
        // update b as well
        b[targetRow] = b[targetRow] + factor * b[pivoRow];
    }

    private static boolean selectSuitablePivoElement(Matrix matrix, double[] b, int diagonalIndex, int[] rowMapping, int[] colMapping, double epsilon) {
        int rows = matrix.getRows();
        if (rows == 0) {
            return true;
        }
        int cols = matrix.getCols();

        double[][] m = matrix.getM();

        // check present pivo element
        double pivo = m[diagonalIndex][diagonalIndex];
        if (!NumberUtil.isZero(pivo, epsilon)) {
            // perfect, the default pivo element is suitable
            return true;
        }

        // we need to find a new pivo element
        // first try to find one by replacing the current row with a suitable
        // different row with a larger index.
        for (int i = diagonalIndex + 1; i < rows; ++i) {
            double newPivo = m[i][diagonalIndex];
            if (!NumberUtil.isZero(newPivo, epsilon)) {
                // found new pivo -> swap row
                swapRows(m, diagonalIndex, i);
                // swap values in b as well
                swap(b, diagonalIndex, i);
                // update row mapping
                swap(rowMapping, diagonalIndex, i);
                return true;
            }
        }
        // now try to swap columns to find a pivo element
        for (int i = diagonalIndex + 1; i < cols; ++i) {
            double newPivo = m[diagonalIndex][i];
            if (!NumberUtil.isZero(newPivo, epsilon)) {
                // found new pivo -> swap row
                swapCols(m, diagonalIndex, i);
                // update row mapping
                swap(rowMapping, diagonalIndex, i);
                return true;
            }
        }
        // no pivo element found
        return false;

    }

    private static void swap(double[] v, int i, int j) {
        double t = v[i];
        v[i] = v[j];
        v[j] = t;
    }

    private static void swap(int[] v, int i, int j) {
        int t = v[i];
        v[i] = v[j];
        v[j] = t;
    }

    private static void swapCols(double[][] m, int colIndex1, int colIndex2) {
        double[] tmp = m[colIndex1];
        m[colIndex1] = m[colIndex2];
        m[colIndex2] = tmp;

    }

    private static void swapRows(double[][] m, int rowIndex1, int rowIndex2) {
        int cols = m[rowIndex1].length;
        for (int col = 0; col < cols; ++col) {
            double tmp = m[rowIndex1][col];
            m[rowIndex1][col] = m[rowIndex2][col];
            m[rowIndex2][col] = tmp;
        }
    }

    public static Matrix copy(Matrix matrix) {
        int rows = matrix.getRows();
        if (rows == 0) {
            // empty solution
            return new Matrix(0, 0);
        }
        int cols = matrix.getCols();

        Matrix target = new Matrix(matrix);
        double[][] m = matrix.getM();
        double[][] t = target.getM();
        for (int col = 0; col < cols; ++col) {
            for (int row = 0; row < rows; ++row) {
                t[row][col] = m[row][col];
            }
        }
        return target;
    }

    public static boolean equals(double[][] m1, double[][] m2, double epsilon) {
        if (m1.length != m2.length) {
            return false;
        }
        int rows = m1.length;
        if (rows == 0) {
            return true;
        }
        if (m1[0].length != m2[0].length) {
            return false;
        }
        int cols = m1[0].length;

        for (int col = 0; col < cols; ++col) {
            for (int row = 0; row < rows; ++row) {
                if (!NumberUtil.isSame(m1[row][col], m2[row][col], epsilon)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void checkDimensions(String name, Matrix namedMatrix, Matrix target) {
        if (namedMatrix.getRows() != target.getRows() || namedMatrix.getCols() != target.getCols()) {
            throw new InvalidMatrixOperation("Dimensions of %s ([%d][%d]) does not match target matrix ([%d][%d])", name, namedMatrix.getRows(), namedMatrix.getCols(), target.getRows(), target.getCols());
        }
    }

}
