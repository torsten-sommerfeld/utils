package com.torstensommerfeld.utils.math.shapes;

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
public class CubicFunctionParameters {
    private double a;
    private double b;
    private double c;
    private double d;

    public double calculateY(double x) {
        return a * x * x * x + b * x * x + c * x + d;
    }

}
