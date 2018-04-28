package com.torstensommerfeld.utils.math.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/*-
 * 
 * This class represents a line with the support vector (x0, y0) and the slow (dx, dy) 
 * 
 * The line l has the form:
 *     dx     x0
 * l = dy t + y0
 * 
 *
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Line {
    double dx;
    double dy;
    double x0;
    double y0;
}
