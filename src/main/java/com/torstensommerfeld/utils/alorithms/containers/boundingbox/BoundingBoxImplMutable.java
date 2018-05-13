package com.torstensommerfeld.utils.alorithms.containers.boundingbox;

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
public class BoundingBoxImplMutable implements BoundingBox2D {

    private double left;
    private double top;
    private double right;
    private double bottom;

}
