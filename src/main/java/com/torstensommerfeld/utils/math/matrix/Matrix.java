package com.torstensommerfeld.utils.math.matrix;

import lombok.Getter;

@Getter
public class Matrix {

    private double m[][];
    private int rows;
    private int cols;

    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.m = new double[rows][cols];
    }

    public Matrix(int rows, int cols, double[][] m) {
        this.rows = rows;
        this.cols = cols;
        this.m = m;
    }

    public Matrix(Matrix template) {
        this(template.getRows(), template.getCols());
    }

    @Override
    public String toString() {
        return toString("%.3f");
    }

    public String toString(String template) {
        int len = 0;
        String[][] ss = new String[rows][cols];
        for (int row = 0; row < rows; ++row) {
            for (int col = 0; col < cols; ++col) {
                double v = m[row][col];
                String s = String.format(template, v);
                len = Math.max(s.length(), len);
                ss[row][col] = s;
            }
        }
        StringBuilder sb = new StringBuilder("\n");
        template = "%" + len + "s";
        for (int row = 0; row < rows; ++row) {
            for (int col = 0; col < cols; ++col) {
                String s = ss[row][col];
                if (col > 0) {
                    sb.append(" ");
                }
                sb.append(String.format(template, s));
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}