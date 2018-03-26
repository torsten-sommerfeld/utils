package com.torstensommerfeld.utils.alorithms.collections;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.collections4.iterators.ArrayIterator;
import org.apache.commons.collections4.iterators.ArrayListIterator;

import com.google.common.base.Objects;

import lombok.NoArgsConstructor;

/**
 * This class is a special version of an array list and its purpose is to allow quick inserts and removals in O(1) while still allowing random access in O(1). This comes with the price that the location of an item is not guaranteed - an insert or remove operation may (and does if the affected location is not the last) change the order.
 * 
 * It is still possible to sort the list and it maintains the order as long as the list is not modified.
 * 
 * Implementation details:
 * 
 * (1) New elements are always put at the end independently of the requested location. (2) The place of a removed element is always re-occupied by the last element. (3) Some methods are not implemented in the most efficient way and could use improvements. (4) No boundary checks, no concurrent modification checks nor any validations are performed to improve performance.
 * 
 * @author torsten
 *
 * @param <T>
 */
@NoArgsConstructor
@SuppressWarnings("unchecked")
public class UnOrderedArrayList<T> extends AbstractList<T> {
    private static final Object[] INIT_ARRAY = new Object[0];

    private Object[] objects = INIT_ARRAY;
    private int end = 0;

    public UnOrderedArrayList(int initialSize) {
        objects = new Object[initialSize];
    }

    @Override
    public int size() {
        return end;
    }

    @Override
    public boolean isEmpty() {
        return end == 0;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < end; ++i) {
            if (Objects.equal(o, objects[i])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayIterator<>(objects, 0, end);
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOfRange(objects, 0, end);
    }

    @Override
    public <T2> T2[] toArray(T2[] a) {
        return (T2[]) Arrays.copyOfRange(objects, 0, end, a.getClass());
    }

    @Override
    public boolean add(T e) {

        if (end < objects.length) {
            objects[end++] = e;
            return true;
        }
        Object[] copy = new Object[objects.length * 2 + 2];
        System.arraycopy(objects, 0, copy, 0, end);
        copy[end++] = e;
        objects = copy;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index >= 0) {
            remove(index);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) {
            changed |= remove(o);
        }
        return changed;
    }

    @Override
    public void clear() {
        end = 0;
    }

    @Override
    public T get(int index) {
        return (T) objects[index];
    }

    @Override
    public T set(int index, T element) {
        Object old = objects[index];
        objects[index] = element;
        return (T) old;
    }

    @Override
    public void add(int index, T element) {
        // insertion ordering is not guaranteed
        add(element);
    }

    @Override
    public T remove(int index) {
        Object old = objects[index];
        objects[index] = objects[--end];
        return (T) old;
    }

    @Override
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < end; i++) {
                if (objects[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < end; i++) {
                if (o.equals(objects[i])) {
                    return i;
                }
            }
        }

        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = end - 1; i >= 0; i--) {
                if (objects[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = end - 1; i >= 0; i--) {
                if (o.equals(objects[i])) {
                    return i;
                }
            }
        }

        return -1;
    }

    @Override
    public ListIterator<T> listIterator() {
        return new ArrayListIterator<>(objects, 0, end);
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new ArrayListIterator<>(objects, index, end);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        List<T> result = new ArrayList<>(toIndex - fromIndex);
        for (int i = fromIndex; i < toIndex; ++i) {
            result.add((T) objects[i]);
        }
        return result;
    }

    @Override
    public void sort(Comparator<? super T> c) {
        Arrays.sort((T[]) objects, 0, end, c);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
