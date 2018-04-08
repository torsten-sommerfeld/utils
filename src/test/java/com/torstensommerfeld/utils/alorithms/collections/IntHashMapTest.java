package com.torstensommerfeld.utils.alorithms.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class IntHashMapTest {

    private IntHashMap<Integer> classUnderTest;

    @Before
    public void setup() {
        classUnderTest = new IntHashMap<>();
    }

    @Test
    public void testSingleAddition() {
        // when
        classUnderTest.put(1, 1);

        // then
        Assert.assertEquals(1, classUnderTest.size);
        Assert.assertEquals(new Integer(1), classUnderTest.get(1));
    }

    @Test
    public void testMultipeAddition() {
        // given
        int count = 10000;
        // when
        for (int i = 0; i < count; ++i) {
            classUnderTest.put(i, i * 2);
        }

        // then
        Assert.assertEquals(count, classUnderTest.size);
        for (int i = 0; i < count; ++i) {
            Assert.assertEquals(new Integer(i * 2), classUnderTest.get(i));
        }
    }

    @Test
    public void testRemove() {
        // given
        int count = 10000;
        for (int i = 0; i < count; ++i) {
            classUnderTest.put(i, i * 2);
        }
        int candidate = 1000;

        // when
        Integer result = classUnderTest.remove(candidate);

        // then
        Assert.assertEquals(new Integer(candidate * 2), result);
        Assert.assertEquals(count - 1, classUnderTest.size);
        Assert.assertFalse(classUnderTest.containsKey(candidate));
        Assert.assertNull(classUnderTest.get(candidate));

    }

    @Test
    public void testKeySet() {
        // given
        classUnderTest.put(1, 2);
        classUnderTest.put(2, 4);

        // when
        Set<Integer> keySet = classUnderTest.keySet();
        Iterator<Integer> iterator = keySet.iterator();

        // then
        Assert.assertEquals(2, keySet.size());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(new Integer(1), iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(new Integer(2), iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testValueSet() {
        // given
        classUnderTest.put(1, 2);
        classUnderTest.put(2, 4);

        // when
        Collection<Integer> values = classUnderTest.values();
        Iterator<Integer> iterator = values.iterator();

        // then
        Assert.assertEquals(2, values.size());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(new Integer(2), iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(new Integer(4), iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }
}
