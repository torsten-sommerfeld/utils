package com.torstensommerfeld.utils.alorithms.searchtree;

import java.util.ArrayList;
import java.util.List;

import com.torstensommerfeld.utils.alorithms.containers.boundingbox.BoundingBox2D;
import com.torstensommerfeld.utils.alorithms.containers.boundingbox.BoundingBoxImpl;
import com.torstensommerfeld.utils.alorithms.searchtree.Splitter.Split;

public class BoundingBoxSearchTree2DBuilder<T extends BoundingBox2D> {
    private Splitter splitter = new Splitter();
    private int maxNumberOfNotSplittedPayload;

    public BoundingBoxSearchTree2DBuilder(int maxNumberOfNotSplittedPayload) {
        this.maxNumberOfNotSplittedPayload = maxNumberOfNotSplittedPayload;
    }

    public BoundingBoxSearchTree2D<T> build(List<T> objects) {

        return build(objects, splitter.get());
    }

    private BoundingBoxSearchTree2D<T> build(List<T> objects, Split split) {
        BoundingBox2D boundingBox = getBoundingBox(objects);
        BoundingBoxSearchTree2D<T> node = new BoundingBoxSearchTree2D<>(boundingBox);
        // termination condition
        if (objects.size() < maxNumberOfNotSplittedPayload) {
            node.setPayLoad(objects);
            return node;
        }
        // split objects
        objects.sort(split.getComparator());
        double splitValue = (split.getSplitEndAccessor().get(objects.get(objects.size() / 2 - 1)) + split.getSplitStartAccessor().get(objects.get(objects.size() / 2))) / 2;
        List<T> smaller = new ArrayList<T>();
        List<T> payload = new ArrayList<T>();
        List<T> larger = new ArrayList<T>();

        for (T t : objects) {
            double start = split.getSplitStartAccessor().get(t);
            double end = split.getSplitEndAccessor().get(t);
            if (end <= splitValue) {
                smaller.add(t);
            } else if (start >= splitValue) {
                larger.add(t);
            } else {
                payload.add(t);
            }
        }

        node.setPayLoad(payload);
        node.setSplitValue(splitValue);
        node.setSplit(split);

        if (smaller.isEmpty() && larger.isEmpty()) {
            return node;
        }

        Split next = split.getNext();

        if (!smaller.isEmpty()) {
            node.setSmaller(build(smaller, next));
        }

        if (!larger.isEmpty()) {
            node.setLarger(build(larger, next));
        }

        return node;
    }

    private BoundingBox2D getBoundingBox(List<T> objects) {
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;

        for (T t : objects) {
            minX = Math.min(minX, t.getLeft());
            minY = Math.min(minY, t.getTop());
            maxX = Math.max(maxX, t.getRight());
            maxY = Math.max(maxY, t.getBottom());
        }
        return new BoundingBoxImpl(minX, minY, maxX, maxY);
    }

}
