package com.torstensommerfeld.utils.math.shapes;

import com.torstensommerfeld.utils.math.MathUtil;

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
public class Ellipse {
    private double x;
    private double y;
    private double a; // semi-major
    private double b; // semi-minor
    private double rotationsAngle;

    /**
     * This method returns a value specifying the distance of the point to the center of the ellipse. It returns 0 if the point is in the center. It returns 1 if the point is on the ellipse. If the value is larger than 1 then the point is outside the ellipse.
     * 
     * @param x
     * @param y
     * @return
     */
    public double getDistanceRatingToCenter(double x, double y) {
        return MathUtil.sqr(x - this.x) / MathUtil.sqr(a) + MathUtil.sqr(y - this.y) / MathUtil.sqr(b);
    }

}
