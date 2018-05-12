package com.torstensommerfeld.utils.alorithms.searchtree;

import java.util.Comparator;

import com.torstensommerfeld.utils.alorithms.containers.boundingbox.BoundingBox2D;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class Splitter<T extends BoundingBox2D> {
    private Comparator<T> horizontalComparator = (o1, o2) -> Double.compare(o1.getLeft(), o2.getLeft());
    private Comparator<T> verticalComparator = (o1, o2) -> Double.compare(o1.getTop(), o2.getTop());
    private SplitVaueAccessor<T> leftAccessor = o -> o.getLeft();
    private SplitVaueAccessor<T> topAccessor = o -> o.getTop();
    private SplitVaueAccessor<T> rightAccessor = o -> o.getRight();
    private SplitVaueAccessor<T> bottomAccessor = o -> o.getBottom();

    private SplitImpl<T> horizontal;

    public Splitter() {
        SplitImpl<T> horizontal = new SplitImpl<>(leftAccessor, rightAccessor, horizontalComparator, null);
        SplitImpl<T> vertical = new SplitImpl<>(topAccessor, bottomAccessor, verticalComparator, null);
        horizontal.setNext(vertical);
        vertical.setNext(horizontal);

        this.horizontal = horizontal;
    }

    public Split<T> get() {
        return horizontal;
    }

    public interface SplitVaueAccessor<T> {
        double get(T object);
    }

    public interface Split<T> {
        SplitVaueAccessor<T> getSplitStartAccessor();

        SplitVaueAccessor<T> getSplitEndAccessor();

        Comparator<T> getComparator();

        Split<T> getNext();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static class SplitImpl<T> implements Split<T> {
        SplitVaueAccessor<T> splitStartAccessor;
        SplitVaueAccessor<T> splitEndAccessor;
        Comparator<T> comparator;
        Split<T> next;
    }

}
