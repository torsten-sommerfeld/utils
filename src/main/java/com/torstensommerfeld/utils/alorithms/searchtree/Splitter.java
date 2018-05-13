package com.torstensommerfeld.utils.alorithms.searchtree;

import java.util.Comparator;

import com.torstensommerfeld.utils.alorithms.containers.boundingbox.BoundingBox2D;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class Splitter {
    private Comparator<BoundingBox2D> horizontalComparator = (o1, o2) -> Double.compare(o1.getLeft(), o2.getLeft());
    private Comparator<BoundingBox2D> verticalComparator = (o1, o2) -> Double.compare(o1.getTop(), o2.getTop());
    private SplitVaueAccessor leftAccessor = o -> o.getLeft();
    private SplitVaueAccessor topAccessor = o -> o.getTop();
    private SplitVaueAccessor rightAccessor = o -> o.getRight();
    private SplitVaueAccessor bottomAccessor = o -> o.getBottom();

    private SplitImpl horizontal;

    public Splitter() {
        SplitImpl horizontal = new SplitImpl(leftAccessor, rightAccessor, horizontalComparator, null);
        SplitImpl vertical = new SplitImpl(topAccessor, bottomAccessor, verticalComparator, null);
        horizontal.setNext(vertical);
        vertical.setNext(horizontal);

        this.horizontal = horizontal;
    }

    public Split get() {
        return horizontal;
    }

    public interface SplitVaueAccessor {
        double get(BoundingBox2D object);
    }

    public interface Split {
        SplitVaueAccessor getSplitStartAccessor();

        SplitVaueAccessor getSplitEndAccessor();

        Comparator<BoundingBox2D> getComparator();

        Split getNext();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static class SplitImpl implements Split {
        SplitVaueAccessor splitStartAccessor;
        SplitVaueAccessor splitEndAccessor;
        Comparator<BoundingBox2D> comparator;
        Split next;
    }

}
