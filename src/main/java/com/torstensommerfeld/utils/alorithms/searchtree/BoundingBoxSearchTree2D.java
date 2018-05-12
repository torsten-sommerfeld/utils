package com.torstensommerfeld.utils.alorithms.searchtree;

import java.util.List;

import com.torstensommerfeld.utils.alorithms.containers.boundingbox.BoundingBox2D;
import com.torstensommerfeld.utils.alorithms.containers.boundingbox.BoundingBox2DUtil;
import com.torstensommerfeld.utils.alorithms.searchtree.Splitter.Split;

import lombok.Getter;

@Getter
public class BoundingBoxSearchTree2D<T extends BoundingBox2D> {

    private BoundingBox2D boundingBox;
    private List<T> payload;
    private BoundingBoxSearchTree2D<T> smaller;
    private BoundingBoxSearchTree2D<T> larger;
    private double splitValue;
    private Split<T> split;

    public BoundingBoxSearchTree2D(BoundingBox2D boundingBox) {
        this.boundingBox = boundingBox;
    }

    public List<T> findCollisions(T object, List<T> targets) {

        for (T t : payload) {
            if (BoundingBox2DUtil.touch(t, object)) {
                targets.add(t);
            }
        }

        if (smaller != null && split.getSplitStartAccessor().get(object) < splitValue) {
            smaller.findCollisions(object, targets);
        }

        if (larger != null && split.getSplitEndAccessor().get(object) >= splitValue) {
            larger.findCollisions(object, targets);
        }

        return targets;

    }

    public void setPayLoad(List<T> payload) {
        this.payload = payload;
    }

    public void setSmaller(BoundingBoxSearchTree2D<T> smaller) {
        this.smaller = smaller;
    }

    public void setSplitValue(double splitValue) {
        this.splitValue = splitValue;
    }

    public void setLarger(BoundingBoxSearchTree2D<T> larger) {
        this.larger = larger;
    }

    public void setSplit(Split<T> split) {
        this.split = split;
    }

}
