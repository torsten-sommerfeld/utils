package com.torstensommerfeld.utils.alorithms.collections;

import org.junit.Assert;
import org.junit.Test;

public class IntListTest {
    @Test
    public void testAdding() {
        // when
        IntList list = createList();

        // then
        Assert.assertEquals(100, list.size());
        for (int i = 0; i < 100; ++i) {
            Assert.assertEquals(i, list.get(i));
        }
    }

    @Test
    public void testRemoving() {
        // given
        IntList list = createList();

        // when
        for (int i = 0; i < 100; ++i) {
            list.remove(i);
        }

        // then
        Assert.assertTrue(list.isEmpty());
    }

    @Test
    public void testClear() {
        // given
        IntList list = createList();

        // when
        list.clear();

        // then
        Assert.assertEquals(0, list.size());
        Assert.assertTrue(list.isEmpty());
    }

    private IntList createList() {
        IntList list = new IntList();
        for (int i = 0; i < 100; ++i) {
            list.add(i);
        }
        Assert.assertFalse(list.isEmpty());
        return list;
    }
}
