package com.torstensommerfeld.utils.alorithms.collections;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.torstensommerfeld.utils.alorithms.collections.UnOrderedArrayList;

public class UnOrderedArrayListTest {

    private UnOrderedArrayList<Object> classUnderTest;

    @Before
    public void setup() {
        classUnderTest = new UnOrderedArrayList<>();
    }

    @Test
    public void testAdd() {
        // given

        Object testObject = new Object();
        // when
        boolean result = classUnderTest.add(testObject);

        // then
        Assert.assertTrue(result);
        Assert.assertFalse(classUnderTest.isEmpty());
        Assert.assertEquals(1, classUnderTest.size());
        Assert.assertEquals(testObject, classUnderTest.get(0));
    }

    @Test
    public void testAddAtIndex() {
        // given

        Object testObject = new Object();
        // when
        classUnderTest.add(0, testObject);

        // then
        Assert.assertFalse(classUnderTest.isEmpty());
        Assert.assertEquals(1, classUnderTest.size());
        Assert.assertEquals(testObject, classUnderTest.get(0));
    }

    @Test
    public void testMultipleAdds() {
        // given
        int count = 10;
        Object testObject = new Object();

        // when
        for (int i = 0; i < count; ++i) {
            classUnderTest.add(testObject);
        }

        // then
        Assert.assertEquals(count, classUnderTest.size());
        for (int i = 0; i < count; ++i) {
            Assert.assertEquals(testObject, classUnderTest.get(i));
        }
    }

    @Test
    public void testForEach() {
        // given
        Object testObject = new Object();
        classUnderTest.add(testObject);
        classUnderTest.add(testObject);

        // when
        int count = 0;
        for (Object o : classUnderTest) {
            if (o == testObject) {
                count += 1;
            }
        }

        // then
        Assert.assertEquals(2, count);
    }

    @Test
    public void testIterator() {
        // given
        Integer testObject = 99;
        classUnderTest.add(testObject);
        classUnderTest.add(testObject);

        // when
        int count = 0;
        for (Iterator<Object> iter = classUnderTest.iterator(); iter.hasNext();) {
            Object o = iter.next();
            if (o == testObject) {
                count += 1;
            }
        }

        // then
        Assert.assertEquals(2, count);
    }

    @Test
    public void testListIterator() {
        // given
        Integer testObject = 99;
        classUnderTest.add(testObject);
        classUnderTest.add(testObject);

        // when
        int count = 0;
        for (Iterator<Object> iter = classUnderTest.listIterator(); iter.hasNext();) {
            Object o = iter.next();
            if (o == testObject) {
                count += 1;
            }
        }

        // then
        Assert.assertEquals(2, count);
    }

    @Test
    public void testListIteratorWithIndex() {
        // given
        Integer testObject = 99;
        classUnderTest.add(testObject);
        classUnderTest.add(testObject);

        // when
        int count = 0;
        for (Iterator<Object> iter = classUnderTest.listIterator(1); iter.hasNext();) {
            Object o = iter.next();
            if (o == testObject) {
                count += 1;
            }
        }

        // then
        Assert.assertEquals(1, count);
    }

    @Test
    public void testPop() {
        // given
        Object testObject1 = new Object();
        Object testObject2 = new Object();

        classUnderTest.add(testObject1);
        classUnderTest.add(testObject2);

        // when
        Object first = classUnderTest.remove(0);
        Object second = classUnderTest.remove(0);

        // then
        Assert.assertEquals(0, classUnderTest.size());
        Assert.assertTrue(classUnderTest.isEmpty());
        Assert.assertEquals(testObject1, first);
        Assert.assertEquals(testObject2, second);
    }

    @Test
    public void testPopAndAdd() {
        // given
        Object testObject1 = new Object();
        Object testObject2 = new Object();

        classUnderTest.add(testObject1);
        classUnderTest.add(testObject2);

        // when
        Object removed = classUnderTest.remove(0);
        classUnderTest.add(removed);
        removed = classUnderTest.remove(0);

        // then
        Assert.assertFalse(classUnderTest.isEmpty());
        Assert.assertEquals(1, classUnderTest.size());
        if (removed == testObject1) {
            Assert.assertEquals(testObject2, classUnderTest.get(0));
        }
        else {
            Assert.assertEquals(testObject1, classUnderTest.get(0));
        }
    }

    @Test
    public void testContains() {
        // given
        Object testObject1 = new Object();
        Object testObject2 = new Object();
        Object testObject3 = new Object();

        classUnderTest.add(testObject1);
        classUnderTest.add(testObject2);

        // when
        boolean containsFirst = classUnderTest.contains(testObject1);
        boolean containsSecond = classUnderTest.contains(testObject2);
        boolean containsThird = classUnderTest.contains(testObject3);

        // then
        Assert.assertTrue(containsFirst);
        Assert.assertTrue(containsSecond);
        Assert.assertFalse(containsThird);
    }

    @Test
    public void testRemove() {
        // given
        Object testObject1 = new Object();
        Object testObject2 = new Object();
        Object testObject3 = new Object();

        classUnderTest.add(testObject1);
        classUnderTest.add(testObject2);

        // when
        boolean first = classUnderTest.remove(testObject1);
        boolean third = classUnderTest.remove(testObject3);
        boolean second = classUnderTest.remove(testObject2);

        // then
        Assert.assertTrue(first);
        Assert.assertTrue(second);
        Assert.assertFalse(third);
        Assert.assertTrue(classUnderTest.isEmpty());
    }

    @Test
    public void testRemoveAll() {
        // given
        Object testObject1 = new Object();
        Object testObject2 = new Object();
        Object testObject3 = new Object();

        classUnderTest.add(testObject1);
        classUnderTest.add(testObject2);
        classUnderTest.add(testObject3);

        // when
        boolean removed = classUnderTest.removeAll(Arrays.asList(testObject1, testObject3));

        // then
        Assert.assertTrue(removed);
        Assert.assertEquals(1, classUnderTest.size());
    }

    @Test
    public void testClear() {
        // given
        Object testObject1 = new Object();
        Object testObject2 = new Object();

        classUnderTest.add(testObject1);
        classUnderTest.add(testObject2);

        // when
        classUnderTest.clear();

        // then
        Assert.assertTrue(classUnderTest.isEmpty());
    }

    @Test
    public void testSet() {
        // given
        Object testObject1 = new Object();
        Object testObject2 = new Object();
        Object testObject3 = new Object();

        classUnderTest.add(testObject1);
        classUnderTest.add(testObject2);

        // when
        classUnderTest.set(0, testObject3);

        // then
        Assert.assertEquals(2, classUnderTest.size());
        Assert.assertEquals(testObject3, classUnderTest.get(0));
        Assert.assertEquals(testObject2, classUnderTest.get(1));
    }

    @Test
    public void testRemoveViaIndex() {
        // given
        Object testObject1 = 1;
        Object testObject2 = 2;
        Object testObject3 = 3;
        Object testObject4 = 4;

        classUnderTest.add(testObject1);
        classUnderTest.add(testObject2);
        classUnderTest.add(testObject3);
        classUnderTest.add(testObject4);

        // when
        Object first = classUnderTest.remove(0);
        Object third = classUnderTest.remove(1);
        Object forth = classUnderTest.remove(1);
        Object second = classUnderTest.remove(0);

        // then
        Assert.assertEquals(testObject1, first);
        Assert.assertNotEquals(first, second);
        Assert.assertNotEquals(first, third);
        Assert.assertNotEquals(first, forth);
        Assert.assertNotEquals(second, third);
        Assert.assertNotEquals(second, forth);
        Assert.assertNotEquals(third, forth);
        Assert.assertTrue(classUnderTest.isEmpty());
    }

    @Test
    public void testIndexOf() {
        // given
        Object testObject1 = new Object();
        Object testObject2 = new Object();
        classUnderTest.add(testObject1);
        classUnderTest.add(testObject1);
        classUnderTest.add(testObject2);
        classUnderTest.add(testObject2);

        // when
        Object first = classUnderTest.indexOf(testObject1);
        Object second = classUnderTest.indexOf(testObject2);
        Object miss = classUnderTest.indexOf(classUnderTest);

        // then
        Assert.assertEquals(0, first);
        Assert.assertEquals(2, second);
        Assert.assertEquals(-1, miss);
    }

    @Test
    public void testIndexOf_null() {
        // given
        Object testObject1 = new Object();
        Object testObject2 = new Object();
        classUnderTest.add(testObject1);
        classUnderTest.add(testObject1);
        classUnderTest.add(null);
        classUnderTest.add(testObject2);

        // when
        int index1 = classUnderTest.indexOf(null);
        classUnderTest.remove(null);
        int index2 = classUnderTest.indexOf(null);

        // then
        Assert.assertEquals(2, index1);
        Assert.assertEquals(-1, index2);
    }

    @Test
    public void testLastIndexOf() {
        // given
        Object testObject1 = new Object();
        Object testObject2 = new Object();
        classUnderTest.add(testObject1);
        classUnderTest.add(testObject1);
        classUnderTest.add(testObject2);
        classUnderTest.add(testObject2);

        // when
        int first = classUnderTest.lastIndexOf(testObject1);
        int second = classUnderTest.lastIndexOf(testObject2);
        int miss = classUnderTest.lastIndexOf(classUnderTest);

        // then
        Assert.assertEquals(1, first);
        Assert.assertEquals(3, second);
        Assert.assertEquals(-1, miss);
    }

    @Test
    public void testLastIndexOf_null() {
        // given
        Object testObject1 = new Object();
        Object testObject2 = new Object();
        classUnderTest.add(testObject1);
        classUnderTest.add(testObject1);
        classUnderTest.add(null);
        classUnderTest.add(testObject2);

        // when
        int index1 = classUnderTest.lastIndexOf(null);
        classUnderTest.remove(null);
        int index2 = classUnderTest.lastIndexOf(null);

        // then
        Assert.assertEquals(2, index1);
        Assert.assertEquals(-1, index2);
    }

    @Test
    public void testSubString() {
        // given
        Object testObject1 = new Object();
        Object testObject2 = new Object();
        classUnderTest.add(testObject1);
        classUnderTest.add(testObject2);

        // when
        List<Object> sublist = classUnderTest.subList(0, 1);

        // then
        Assert.assertEquals(1, sublist.size());
        Assert.assertEquals(testObject1, sublist.get(0));
    }

    @Test
    public void testToArray() {
        // given
        Integer testObject1 = 1;
        Integer testObject2 = 2;
        classUnderTest.add(testObject1);
        classUnderTest.add(testObject2);
        classUnderTest.remove(testObject1);

        // when
        Object[] array = classUnderTest.toArray();

        // then
        Assert.assertEquals(1, array.length);
        Assert.assertEquals(testObject2, array[0]);
    }

    @Test
    public void testToTypedArray() {
        // given
        Integer testObject1 = 1;
        Integer testObject2 = 2;
        classUnderTest.add(testObject1);
        classUnderTest.add(testObject2);
        classUnderTest.remove(testObject1);

        // when
        Integer[] array = classUnderTest.toArray(new Integer[0]);

        // then
        Assert.assertEquals(1, array.length);
        Assert.assertEquals(testObject2, array[0]);
    }
    
    @Test
    public void testSort() {
        // given
        Integer testObject1 = 1;
        Integer testObject2 = 2;
        Integer testObject3 = 0;
        classUnderTest.add(testObject3);
        classUnderTest.add(testObject2);
        classUnderTest.add(testObject1);
        classUnderTest.remove(testObject3);

        // when
        classUnderTest.sort((o1, o2) -> (Integer) o1 - (Integer)(o2));

        // then
        Assert.assertEquals(testObject1, classUnderTest.get(0));
        Assert.assertEquals(testObject2, classUnderTest.get(1));
    }
    
    @Test
    public void testHashcode() {
        // when
        classUnderTest.hashCode();
    }

}
