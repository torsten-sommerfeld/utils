package com.torstensommerfeld.utils.alorithms.collections;

import java.util.Arrays;
import java.util.RandomAccess;

public class IntList implements RandomAccess {

    private int[] buffer;
    private int size;

    public IntList() {
        this(10);
    }

    public IntList(int initialCapacity) {
        buffer = new int[initialCapacity];
    }

    public int[] getBuffer() {
        return buffer;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void add(int data) {
        if (buffer.length - size == 0) {
            buffer = Arrays.copyOf(buffer, buffer.length * 2 + 1);
        }
        buffer[size++] = data;
    }

    public void clear() {
        size = 0;
    }

    public void remove(int index) {
        for (int i = index, end = size - 1; i < end; ++i) {
            buffer[i] = buffer[i + 1];
        }
        size -= 1;
    }

    public int get(int index) {
        return buffer[index];
    }

}
