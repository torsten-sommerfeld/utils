package com.torstensommerfeld.utils.math.shapes;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class Circle extends Center {
    private double r;

    public Circle(double x, double y, double r) {
        super(x, y);
        this.r = r;
    }

}
