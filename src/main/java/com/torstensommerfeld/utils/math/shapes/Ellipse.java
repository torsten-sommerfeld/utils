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
public class Ellipse extends Center {

    public static final Ellipse INVALID = new Ellipse(Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN);
    private double a; // semi-major
    private double b; // semi-minor
    private double rotationsAngle;

    public Ellipse(double x, double y, double a, double b, double rotationsAngle) {
        super(x, y);
        this.a = a;
        this.b = b;
        this.rotationsAngle = rotationsAngle;
    }

    /**
     * This method returns a value specifying the distance of the point to the center of the ellipse. It returns 0 if the point is in the center. It returns 1 if the point is on the ellipse. If the value is larger than 1 then the point is outside the ellipse.
     * 
     * @param x
     * @param y
     * @return
     */
    public double getDistanceRatingToCenter(double x, double y) {
        return MathUtil.sqr(x - this.getX()) / MathUtil.sqr(a) + MathUtil.sqr(y - this.getY()) / MathUtil.sqr(b);
    }

    public boolean isValid() {
        return !Double.isNaN(a) && !Double.isNaN(b) && !Double.isNaN(getX()) && !Double.isNaN(getY()) && !Double.isNaN(rotationsAngle);
    }

    public boolean isInvalid() {
        return Double.isNaN(a) || Double.isNaN(b) || Double.isNaN(getY()) || Double.isNaN(getX()) || Double.isNaN(rotationsAngle);
    }

}
