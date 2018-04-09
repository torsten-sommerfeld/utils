package com.torstensommerfeld.utils.math.matrix;

import com.torstensommerfeld.utils.math.NumberUtil;

import lombok.Getter;

/**
 * This class contains the 'solution' of a linear equation; The solution might contain a unique result if the rank of the solution equals the rank of the equation matrix.
 *
 * @author ts
 *
 */
@Getter
public class EquationSolution {
    private int rank;
    private double[][] reducedEquation;
    private double[] solution;

    private Matrix equationMatrix;
    private double[] equationConstants;

    private double epsilon;

    public EquationSolution(int rank, double[][] reducedEquation, double[] solution, Matrix equationMatrix, double[] equationConstants, double epsilon) {
        this.rank = rank;
        this.reducedEquation = reducedEquation;
        this.solution = solution;
        this.equationMatrix = equationMatrix;
        this.equationConstants = equationConstants;
        this.epsilon = epsilon;
    }

    public boolean isValid() {
        int rows = equationMatrix.getRows();
        if (rows == 0) {
            return true;
        }

        int cols = equationMatrix.getCols();
        double[][] m = equationMatrix.getM();

        for (int row = 0; row < rows; ++row) {
            double v = 0;
            for (int col = 0; col < cols; ++col) {
                double sc = col >= solution.length ? 0 : solution[col];
                v += sc * m[row][col];
            }
            if (!NumberUtil.isSame(v, equationConstants[row], epsilon)) {
                return false;
            }
        }
        return true;
    }

    public int getRank() {
        return rank;
    }

    public boolean isUnique() {
        return rank == equationConstants.length;
    }

    public double[][] getNormalizedReducedEquationMatrix() {
        double[][] copy = new double[rank][];
        for (int row = 0; row < rank; ++row) {
            copy[row] = reducedEquation[row];
        }
        return copy;
    }

    @Override
    public String toString() {
        return toString("%5.2f");
    }

    public String toString(String template) {

        StringBuilder sb = new StringBuilder("\n");
        int opRow = equationConstants.length / 2;
        double[][] m = equationMatrix.getM();
        for (int row = 0; row < equationConstants.length; ++row) {
            int cols = equationMatrix.getCols();
            for (int col = 0; col < cols; ++col) {
                sb.append(String.format(template, m[row][col]));
                String prefix = col == cols - 1 ? "" : opRow == row ? " + " : "   ";
                sb.append(((row == opRow ? " " + (char) ('z' - cols + col + 1) : "  ") + prefix));
            }
            sb.append(row == opRow ? " = " : "   ");
            sb.append(String.format(template, equationConstants[row]));
            sb.append(row == opRow ? " -> " : "    ");

            double v = 0;
            for (int col = 0; col < cols; ++col) {
                double x = (col >= solution.length ? 0 : solution[col]);
                v += m[row][col] * x;
                sb.append(String.format(template, m[row][col]));
                sb.append(" *" + String.format(template, x));
                if (col != cols - 1) {
                    sb.append(" + ");
                }
            }
            sb.append(row == opRow ? " = " : "   ");
            sb.append(String.format(template, v));
            sb.append("\n");

        }
        return sb.toString();
    }

}
