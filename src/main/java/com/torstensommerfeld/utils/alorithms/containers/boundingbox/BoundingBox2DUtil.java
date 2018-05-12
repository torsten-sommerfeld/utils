package com.torstensommerfeld.utils.alorithms.containers.boundingbox;

public class BoundingBox2DUtil {
    public static boolean touch(BoundingBox2D b1, BoundingBox2D b2) {
        if (b1.getRight() < b2.getLeft()) {
            // b1 is left of b2
            return false;
        }
        if (b1.getLeft() > b2.getRight()) {
            // b1 is right of b2
            return false;
        }

        if (b1.getBottom() < b2.getTop()) {
            // b1 is on top of b2
            return false;
        }
        if (b1.getTop() > b2.getBottom()) {
            // b1 is below b2
            return false;
        }

        return true;
    }
}
