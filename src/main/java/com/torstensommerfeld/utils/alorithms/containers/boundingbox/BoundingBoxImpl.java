package com.torstensommerfeld.utils.alorithms.containers.boundingbox;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class BoundingBoxImpl implements BoundingBox2D {

    private double left;
    private double top;
    private double right;
    private double bottom;

}
