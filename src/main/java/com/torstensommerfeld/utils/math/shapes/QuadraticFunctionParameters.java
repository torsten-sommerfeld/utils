package com.torstensommerfeld.utils.math.shapes;

import com.torstensommerfeld.utils.math.Geo2D;
import com.torstensommerfeld.utils.math.NumberUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QuadraticFunctionParameters {
    private double a;
    private double b;
    private double c;

    public double calculateY(double x) {
        return a * x * x + b * x + c;
    }

    public double[] getVertex(double[] target) {
        return Geo2D.getVertexPointForQuadraticEquation(a, b, c, target);
    }

    public boolean isValid() {
        return !Double.isNaN(a) && !NumberUtil.isZero(a);
    }
}
