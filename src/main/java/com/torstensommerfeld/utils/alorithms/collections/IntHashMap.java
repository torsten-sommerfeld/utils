/*
 * Copyright (c) 1997, 2013, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package com.torstensommerfeld.utils.alorithms.collections;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import com.torstensommerfeld.utils.exceptions.NotSupportedException;

/**
 * This is a copy of java.util.hashmap but simplified to use int as key to avoid excessive integer autoboxing.
 * 
 * Not all features of the original class are kept, e.g. the tree concept for the buckets has been removed.
 * 
 * @param <V>
 */
public class IntHashMap<V> {
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16

    static final int MAXIMUM_CAPACITY = 1 << 30;

    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int UNTREEIFY_THRESHOLD = 6;
    static final int MIN_TREEIFY_CAPACITY = 64;

    private Set<Integer> keySet;
    private Collection<V> values;

    static class Node<V> {
        final int key;
        V value;
        Node<V> next;

        Node(int key, V value, Node<V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final int getKey() {
            return key;
        }

        public final V getValue() {
            return value;
        }

        public final String toString() {
            return key + "=" + value;
        }

        public final int hashCode() {
            return key ^ Objects.hashCode(value);
        }

        public final V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        public final boolean equals(Object o) {
            if (o == this)
                return true;
            if (o instanceof Node) {
                @SuppressWarnings("unchecked")
                Node<V> e = (Node<V>) o;
                if (key == e.getKey() && Objects.equals(value, e.getValue()))
                    return true;
            }
            return false;
        }
    }

    /* ---------------- Static utilities -------------- */

    /**
     * Returns k.compareTo(x) if x matches kc (k's screened comparable class), else 0.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" }) // for cast to Comparable
    static int compareComparables(Class<?> kc, Object k, Object x) {
        return (x == null || x.getClass() != kc ? 0 : ((Comparable) k).compareTo(x));
    }

    /**
     * Returns a power of two size for the given target capacity.
     */
    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    /* ---------------- Fields -------------- */

    transient Node<V>[] table;

    transient Set<Node<V>> entrySet;

    transient int size;

    transient int modCount;

    int threshold;

    final float loadFactor;

    /* ---------------- Public operations -------------- */

    /**
     * Constructs an empty <tt>HashMap</tt> with the specified initial capacity and load factor.
     *
     * @param initialCapacity
     *            the initial capacity
     * @param loadFactor
     *            the load factor
     * @throws IllegalArgumentException
     *             if the initial capacity is negative or the load factor is nonpositive
     */
    public IntHashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        if (initialCapacity > MAXIMUM_CAPACITY)
            initialCapacity = MAXIMUM_CAPACITY;
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
        this.loadFactor = loadFactor;
        this.threshold = tableSizeFor(initialCapacity);
    }

    /**
     * Constructs an empty <tt>HashMap</tt> with the specified initial capacity and the default load factor (0.75).
     *
     * @param initialCapacity
     *            the initial capacity.
     * @throws IllegalArgumentException
     *             if the initial capacity is negative.
     */
    public IntHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Constructs an empty <tt>HashMap</tt> with the default initial capacity (16) and the default load factor (0.75).
     */
    public IntHashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR; // all other fields defaulted
    }

    /**
     * Constructs a new <tt>HashMap</tt> with the same mappings as the specified <tt>Map</tt>. The <tt>HashMap</tt> is created with default load factor (0.75) and an initial capacity sufficient to hold the mappings in the specified <tt>Map</tt>.
     *
     * @param m
     *            the map whose mappings are to be placed in this map
     * @throws NullPointerException
     *             if the specified map is null
     */
    public IntHashMap(IntHashMap<? extends V> m) {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        putMapEntries(m, false);
    }

    /**
     * Implements Map.putAll and Map constructor
     *
     * @param m
     *            the map
     * @param evict
     *            false when initially constructing this map, else true (relayed to method afterNodeInsertion).
     */
    final void putMapEntries(IntHashMap<? extends V> m, boolean evict) {
        int s = m.size();
        if (s > 0) {
            if (table == null) { // pre-size
                float ft = ((float) s / loadFactor) + 1.0F;
                int t = ((ft < (float) MAXIMUM_CAPACITY) ? (int) ft : MAXIMUM_CAPACITY);
                if (t > threshold)
                    threshold = tableSizeFor(t);
            } else if (s > threshold)
                resize();
            for (Node<? extends V> e : m.entrySet()) {
                int key = e.getKey();
                V value = e.getValue();
                putVal(key, value, false, evict);
            }
        }
    }

    /**
     * Returns the number of key-value mappings in this map.
     *
     * @return the number of key-value mappings in this map
     */
    public int size() {
        return size;
    }

    /**
     * Returns <tt>true</tt> if this map contains no key-value mappings.
     *
     * @return <tt>true</tt> if this map contains no key-value mappings
     */
    public boolean isEmpty() {
        return size == 0;
    }

    public V get(int key) {
        Node<V> e;
        return (e = getNode(key)) == null ? null : e.value;
    }

    /**
     * Implements Map.get and related methods
     *
     * @param hash
     *            hash for key
     * @param key
     *            the key
     * @return the node, or null if none
     */
    final Node<V> getNode(int key) {
        Node<V>[] tab;
        Node<V> first, e;
        int n;
        if ((tab = table) != null && (n = tab.length) > 0 && (first = tab[(n - 1) & key]) != null) {
            if (first.key == key)
                return first;
            if ((e = first.next) != null) {
                do {
                    if (key == e.key)
                        return e;
                } while ((e = e.next) != null);
            }
        }
        return null;
    }

    /**
     * Returns <tt>true</tt> if this map contains a mapping for the specified key.
     *
     * @param key
     *            The key whose presence in this map is to be tested
     * @return <tt>true</tt> if this map contains a mapping for the specified key.
     */
    public boolean containsKey(int key) {
        return getNode(key) != null;
    }

    /**
     * Associates the specified value with the specified key in this map. If the map previously contained a mapping for the key, the old value is replaced.
     *
     * @param key
     *            key with which the specified value is to be associated
     * @param value
     *            value to be associated with the specified key
     * @return the previous value associated with <tt>key</tt>, or <tt>null</tt> if there was no mapping for <tt>key</tt>. (A <tt>null</tt> return can also indicate that the map previously associated <tt>null</tt> with <tt>key</tt>.)
     */
    public V put(int key, V value) {
        return putVal(key, value, false, true);
    }

    /**
     * Implements Map.put and related methods
     *
     * @param hash
     *            hash for key
     * @param key
     *            the key
     * @param value
     *            the value to put
     * @param onlyIfAbsent
     *            if true, don't change existing value
     * @param evict
     *            if false, the table is in creation mode.
     * @return previous value, or null if none
     */
    final V putVal(int key, V value, boolean onlyIfAbsent, boolean evict) {
        Node<V>[] tab;
        Node<V> p;
        int n, i;
        if ((tab = table) == null || (n = tab.length) == 0)
            n = (tab = resize()).length;
        if ((p = tab[i = (n - 1) & key]) == null)
            tab[i] = newNode(key, value, null);
        else {
            Node<V> e;
            if (p.key == key)
                e = p;
            else {
                for (;;) {
                    if ((e = p.next) == null) {
                        p.next = newNode(key, value, null);
                    }
                    if (e.key == key)
                        break;
                    p = e;
                }
            }
            if (e != null) { // existing mapping for key
                V oldValue = e.value;
                if (!onlyIfAbsent || oldValue == null)
                    e.value = value;
                afterNodeAccess(e);
                return oldValue;
            }
        }
        ++modCount;
        if (++size > threshold)
            resize();
        afterNodeInsertion(evict);
        return null;
    }

    /**
     * Initializes or doubles table size. If null, allocates in accord with initial capacity target held in field threshold. Otherwise, because we are using power-of-two expansion, the elements from each bin must either stay at same index, or move with a power of two offset in the new table.
     *
     * @return the table
     */
    final Node<V>[] resize() {
        Node<V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap, newThr = 0;
        if (oldCap > 0) {
            if (oldCap >= MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTab;
            } else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY && oldCap >= DEFAULT_INITIAL_CAPACITY)
                newThr = oldThr << 1; // double threshold
        } else if (oldThr > 0) // initial capacity was placed in threshold
            newCap = oldThr;
        else { // zero initial threshold signifies using defaults
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }
        if (newThr == 0) {
            float ft = (float) newCap * loadFactor;
            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float) MAXIMUM_CAPACITY ? (int) ft : Integer.MAX_VALUE);
        }
        threshold = newThr;
        @SuppressWarnings({ "unchecked" })
        Node<V>[] newTab = (Node<V>[]) new Node[newCap];
        table = newTab;
        if (oldTab != null) {
            for (int j = 0; j < oldCap; ++j) {
                Node<V> e;
                if ((e = oldTab[j]) != null) {
                    oldTab[j] = null;
                    if (e.next == null)
                        newTab[e.key & (newCap - 1)] = e;
                    else { // preserve order
                        Node<V> loHead = null, loTail = null;
                        Node<V> hiHead = null, hiTail = null;
                        Node<V> next;
                        do {
                            next = e.next;
                            if ((e.key & oldCap) == 0) {
                                if (loTail == null)
                                    loHead = e;
                                else
                                    loTail.next = e;
                                loTail = e;
                            } else {
                                if (hiTail == null)
                                    hiHead = e;
                                else
                                    hiTail.next = e;
                                hiTail = e;
                            }
                        } while ((e = next) != null);
                        if (loTail != null) {
                            loTail.next = null;
                            newTab[j] = loHead;
                        }
                        if (hiTail != null) {
                            hiTail.next = null;
                            newTab[j + oldCap] = hiHead;
                        }
                    }
                }
            }
        }
        return newTab;
    }

    /**
     * Copies all of the mappings from the specified map to this map. These mappings will replace any mappings that this map had for any of the keys currently in the specified map.
     *
     * @param m
     *            mappings to be stored in this map
     * @throws NullPointerException
     *             if the specified map is null
     */
    public void putAll(IntHashMap<? extends V> m) {
        putMapEntries(m, true);
    }

    public V remove(int key) {
        Node<V> e;
        return (e = removeNode(key, null, false, true)) == null ? null : e.value;
    }

    final Node<V> removeNode(int key, Object value, boolean matchValue, boolean movable) {
        Node<V>[] tab;
        Node<V> p;
        int n, index;
        if ((tab = table) != null && (n = tab.length) > 0 && (p = tab[index = (n - 1) & key]) != null) {
            Node<V> node = null, e;
            V v;
            if (p.key == key)
                node = p;
            else if ((e = p.next) != null) {
                do {
                    if (e.key == key) {
                        node = e;
                        break;
                    }
                    p = e;
                } while ((e = e.next) != null);
            }
            if (node != null && (!matchValue || (v = node.value) == value || (value != null && value.equals(v)))) {
                if (node == p)
                    tab[index] = node.next;
                else
                    p.next = node.next;
                ++modCount;
                --size;
                afterNodeRemoval(node);
                return node;
            }
        }
        return null;
    }

    /**
     * Removes all of the mappings from this map. The map will be empty after this call returns.
     */
    public void clear() {
        Node<V>[] tab;
        modCount++;
        if ((tab = table) != null && size > 0) {
            size = 0;
            for (int i = 0; i < tab.length; ++i)
                tab[i] = null;
        }
    }

    /**
     * Returns <tt>true</tt> if this map maps one or more keys to the specified value.
     *
     * @param value
     *            value whose presence in this map is to be tested
     * @return <tt>true</tt> if this map maps one or more keys to the specified value
     */
    public boolean containsValue(Object value) {
        Node<V>[] tab;
        V v;
        if ((tab = table) != null && size > 0) {
            for (int i = 0; i < tab.length; ++i) {
                for (Node<V> e = tab[i]; e != null; e = e.next) {
                    if ((v = e.value) == value || (value != null && value.equals(v)))
                        return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns a {@link Set} view of the keys contained in this map. The set is backed by the map, so changes to the map are reflected in the set, and vice-versa. If the map is modified while an iteration over the set is in progress (except through the iterator's own <tt>remove</tt> operation), the results of the iteration are undefined. The set supports element removal, which removes the corresponding mapping from the map, via the <tt>Iterator.remove</tt>, <tt>Set.remove</tt>,
     * <tt>removeAll</tt>, <tt>retainAll</tt>, and <tt>clear</tt> operations. It does not support the <tt>add</tt> or <tt>addAll</tt> operations.
     *
     * @return a set view of the keys contained in this map
     */
    public Set<Integer> keySet() {
        Set<Integer> ks = keySet;
        if (ks == null) {
            ks = new KeySet();
            keySet = ks;
        }
        return ks;
    }

    final class KeySet implements Set<Integer> {
        public final int size() {
            return size;
        }

        public final void clear() {
            IntHashMap.this.clear();
        }

        public final Iterator<Integer> iterator() {
            return new KeyIterator();
        }

        public final boolean contains(Integer o) {
            return containsKey(o);
        }

        public final boolean remove(Integer key) {
            return removeNode(key, null, false, true) != null;
        }

        @Override
        public boolean isEmpty() {
            return IntHashMap.this.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            if (o instanceof Integer) {
                return IntHashMap.this.containsKey((Integer) o);
            }
            return false;
        }

        @Override
        public Object[] toArray() {
            throw new NotSupportedException();
        }

        @Override
        public <T> T[] toArray(T[] a) {
            throw new NotSupportedException();
        }

        @Override
        public boolean add(Integer e) {
            throw new NotSupportedException();
        }

        @Override
        public boolean remove(Object o) {
            if (o instanceof Integer) {
                IntHashMap.this.remove((Integer) o);
            }
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            throw new NotSupportedException();
        }

        @Override
        public boolean addAll(Collection<? extends Integer> c) {
            throw new NotSupportedException();
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            throw new NotSupportedException();
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            c.forEach(this::remove);
            return true;
        }
    }

    /**
     * Returns a {@link Collection} view of the values contained in this map. The collection is backed by the map, so changes to the map are reflected in the collection, and vice-versa. If the map is modified while an iteration over the collection is in progress (except through the iterator's own <tt>remove</tt> operation), the results of the iteration are undefined. The collection supports element removal, which removes the corresponding mapping from the map, via the
     * <tt>Iterator.remove</tt>, <tt>Collection.remove</tt>, <tt>removeAll</tt>, <tt>retainAll</tt> and <tt>clear</tt> operations. It does not support the <tt>add</tt> or <tt>addAll</tt> operations.
     *
     * @return a view of the values contained in this map
     */
    public Collection<V> values() {
        Collection<V> vs = values;
        if (vs == null) {
            vs = new Values();
            values = vs;
        }
        return vs;
    }

    final class Values extends AbstractCollection<V> {
        public final int size() {
            return size;
        }

        public final void clear() {
            IntHashMap.this.clear();
        }

        public final Iterator<V> iterator() {
            return new ValueIterator();
        }

        public final boolean contains(Object o) {
            return containsValue(o);
        }

        public final Spliterator<V> spliterator() {
            throw new NotSupportedException();
        }

        public final void forEach(Consumer<? super V> action) {
            Node<V>[] tab;
            if (action == null)
                throw new NullPointerException();
            if (size > 0 && (tab = table) != null) {
                int mc = modCount;
                for (int i = 0; i < tab.length; ++i) {
                    for (Node<V> e = tab[i]; e != null; e = e.next)
                        action.accept(e.value);
                }
                if (modCount != mc)
                    throw new ConcurrentModificationException();
            }
        }
    }

    /**
     * Returns a {@link Set} view of the mappings contained in this map. The set is backed by the map, so changes to the map are reflected in the set, and vice-versa. If the map is modified while an iteration over the set is in progress (except through the iterator's own <tt>remove</tt> operation, or through the <tt>setValue</tt> operation on a map entry returned by the iterator) the results of the iteration are undefined. The set supports element removal, which removes the
     * corresponding mapping from the map, via the <tt>Iterator.remove</tt>, <tt>Set.remove</tt>, <tt>removeAll</tt>, <tt>retainAll</tt> and <tt>clear</tt> operations. It does not support the <tt>add</tt> or <tt>addAll</tt> operations.
     *
     * @return a set view of the mappings contained in this map
     */
    public Set<Node<V>> entrySet() {
        Set<Node<V>> es;
        return (es = entrySet) == null ? (entrySet = new EntrySet()) : es;
    }

    final class EntrySet implements Set<Node<V>> {
        public final int size() {
            return size;
        }

        public final void clear() {
            IntHashMap.this.clear();
        }

        public final Iterator<Node<V>> iterator() {
            return new EntryIterator();
        }

        public final boolean contains(Object o) {
            if (!(o instanceof Node))
                return false;
            Node<?> e = (Node<?>) o;
            int key = e.getKey();
            Node<V> candidate = getNode(key);
            return candidate != null && candidate.equals(e);
        }

        public final boolean remove(Object o) {
            if (o instanceof Node) {
                @SuppressWarnings("unchecked")
                Node<V> e = (Node<V>) o;
                int key = e.getKey();
                Object value = e.getValue();
                return removeNode(key, value, true, true) != null;
            }
            return false;
        }

        public final Spliterator<Node<V>> spliterator() {
            throw new NotSupportedException();
        }

        public final void forEach(Consumer<? super Node<V>> action) {
            Node<V>[] tab;
            if (action == null)
                throw new NullPointerException();
            if (size > 0 && (tab = table) != null) {
                int mc = modCount;
                for (int i = 0; i < tab.length; ++i) {
                    for (Node<V> e = tab[i]; e != null; e = e.next)
                        action.accept(e);
                }
                if (modCount != mc)
                    throw new ConcurrentModificationException();
            }
        }

        @Override
        public boolean isEmpty() {
            return IntHashMap.this.isEmpty();
        }

        @Override
        public Object[] toArray() {
            throw new NotSupportedException();
        }

        @Override
        public <T> T[] toArray(T[] a) {
            throw new NotSupportedException();
        }

        @Override
        public boolean add(Node<V> e) {
            throw new NotSupportedException();
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            throw new NotSupportedException();
        }

        @Override
        public boolean addAll(Collection<? extends Node<V>> c) {
            throw new NotSupportedException();
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            throw new NotSupportedException();
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            throw new NotSupportedException();
        }
    }

    // Overrides of JDK8 Map extension methods

    public V getOrDefault(int key, V defaultValue) {
        Node<V> e;
        return (e = getNode(key)) == null ? defaultValue : e.value;
    }

    public V putIfAbsent(int key, V value) {
        return putVal(key, value, true, true);
    }

    public boolean remove(int key, Object value) {
        return removeNode(key, value, true, true) != null;
    }

    public boolean replace(int key, V oldValue, V newValue) {
        Node<V> e;
        V v;
        if ((e = getNode(key)) != null && ((v = e.value) == oldValue || (v != null && v.equals(oldValue)))) {
            e.value = newValue;
            afterNodeAccess(e);
            return true;
        }
        return false;
    }

    public V replace(int key, V value) {
        Node<V> e;
        if ((e = getNode(key)) != null) {
            V oldValue = e.value;
            e.value = value;
            afterNodeAccess(e);
            return oldValue;
        }
        return null;
    }

    public V computeIfAbsent(int key, Function<Integer, ? extends V> mappingFunction) {
        if (mappingFunction == null)
            throw new NullPointerException();
        Node<V>[] tab;
        Node<V> first;
        int n, i;
        Node<V> old = null;
        if (size > threshold || (tab = table) == null || (n = tab.length) == 0)
            n = (tab = resize()).length;
        if ((first = tab[i = (n - 1) & key]) != null) {
            Node<V> e = first;
            do {
                if (e.key == key) {
                    old = e;
                    break;
                }
            } while ((e = e.next) != null);

            V oldValue;
            if (old != null && (oldValue = old.value) != null) {
                afterNodeAccess(old);
                return oldValue;
            }
        }
        V v = mappingFunction.apply(key);
        if (v == null) {
            return null;
        } else if (old != null) {
            old.value = v;
            afterNodeAccess(old);
            return v;
        } else {
            tab[i] = newNode(key, v, first);
        }
        ++modCount;
        ++size;
        afterNodeInsertion(true);
        return v;
    }

    public V computeIfPresent(int key, BiFunction<Integer, ? super V, ? extends V> remappingFunction) {
        if (remappingFunction == null)
            throw new NullPointerException();
        Node<V> e;
        V oldValue;
        if ((e = getNode(key)) != null && (oldValue = e.value) != null) {
            V v = remappingFunction.apply(key, oldValue);
            if (v != null) {
                e.value = v;
                afterNodeAccess(e);
                return v;
            } else
                removeNode(key, null, false, true);
        }
        return null;
    }

    public V compute(int key, BiFunction<Integer, ? super V, ? extends V> remappingFunction) {
        if (remappingFunction == null)
            throw new NullPointerException();
        Node<V>[] tab;
        Node<V> first;
        int n, i;
        Node<V> old = null;
        if (size > threshold || (tab = table) == null || (n = tab.length) == 0)
            n = (tab = resize()).length;
        if ((first = tab[i = (n - 1) & key]) != null) {
            Node<V> e = first;
            do {
                if (e.key == key) {
                    old = e;
                    break;
                }
            } while ((e = e.next) != null);
        }
        V oldValue = (old == null) ? null : old.value;
        V v = remappingFunction.apply(key, oldValue);
        if (old != null) {
            if (v != null) {
                old.value = v;
                afterNodeAccess(old);
            } else
                removeNode(key, null, false, true);
        } else if (v != null) {
            tab[i] = newNode(key, v, first);
            ++modCount;
            ++size;
            afterNodeInsertion(true);
        }
        return v;
    }

    public V merge(int key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        if (value == null)
            throw new NullPointerException();
        if (remappingFunction == null)
            throw new NullPointerException();
        Node<V>[] tab;
        Node<V> first;
        int n, i;
        Node<V> old = null;
        if (size > threshold || (tab = table) == null || (n = tab.length) == 0)
            n = (tab = resize()).length;
        if ((first = tab[i = (n - 1) & key]) != null) {
            Node<V> e = first;
            do {
                if (e.key == key) {
                    old = e;
                    break;
                }
            } while ((e = e.next) != null);
        }
        if (old != null) {
            V v;
            if (old.value != null)
                v = remappingFunction.apply(old.value, value);
            else
                v = value;
            if (v != null) {
                old.value = v;
                afterNodeAccess(old);
            } else
                removeNode(key, null, false, true);
            return v;
        }
        if (value != null) {
            tab[i] = newNode(key, value, first);
            ++modCount;
            ++size;
            afterNodeInsertion(true);
        }
        return value;
    }

    public void forEach(BiConsumer<Integer, ? super V> action) {
        Node<V>[] tab;
        if (action == null)
            throw new NullPointerException();
        if (size > 0 && (tab = table) != null) {
            int mc = modCount;
            for (int i = 0; i < tab.length; ++i) {
                for (Node<V> e = tab[i]; e != null; e = e.next)
                    action.accept(e.key, e.value);
            }
            if (modCount != mc)
                throw new ConcurrentModificationException();
        }
    }

    public void replaceAll(BiFunction<Integer, ? super V, ? extends V> function) {
        Node<V>[] tab;
        if (function == null)
            throw new NullPointerException();
        if (size > 0 && (tab = table) != null) {
            int mc = modCount;
            for (int i = 0; i < tab.length; ++i) {
                for (Node<V> e = tab[i]; e != null; e = e.next) {
                    e.value = function.apply(e.key, e.value);
                }
            }
            if (modCount != mc)
                throw new ConcurrentModificationException();
        }
    }

    /* ------------------------------------------------------------ */
    // Cloning and serialization

    /**
     * Returns a shallow copy of this <tt>HashMap</tt> instance: the keys and values themselves are not cloned.
     *
     * @return a shallow copy of this map
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object clone() {
        IntHashMap<V> result;
        try {
            result = (IntHashMap<V>) super.clone();
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError(e);
        }
        result.reinitialize();
        result.putMapEntries(this, false);
        return result;
    }

    // These methods are also used when serializing HashSets
    final float loadFactor() {
        return loadFactor;
    }

    final int capacity() {
        return (table != null) ? table.length : (threshold > 0) ? threshold : DEFAULT_INITIAL_CAPACITY;
    }

    /**
     * Save the state of the <tt>HashMap</tt> instance to a stream (i.e., serialize it).
     *
     * @serialData The <i>capacity</i> of the HashMap (the length of the bucket array) is emitted (int), followed by the <i>size</i> (an int, the number of key-value mappings), followed by the key (Object) and value (Object) for each key-value mapping. The key-value mappings are emitted in no particular order.
     */
    private void writeObject(java.io.ObjectOutputStream s) throws IOException {
        int buckets = capacity();
        // Write out the threshold, loadfactor, and any hidden stuff
        s.defaultWriteObject();
        s.writeInt(buckets);
        s.writeInt(size);
        internalWriteEntries(s);
    }

    /**
     * Reconstitute the {@code HashMap} instance from a stream (i.e., deserialize it).
     */
    private void readObject(java.io.ObjectInputStream s) throws IOException, ClassNotFoundException {
        // Read in the threshold (ignored), loadfactor, and any hidden stuff
        s.defaultReadObject();
        reinitialize();
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new InvalidObjectException("Illegal load factor: " + loadFactor);
        s.readInt(); // Read and ignore number of buckets
        int mappings = s.readInt(); // Read number of mappings (size)
        if (mappings < 0)
            throw new InvalidObjectException("Illegal mappings count: " + mappings);
        else if (mappings > 0) { // (if zero, use defaults)
            // Size the table using given load factor only if within
            // range of 0.25...4.0
            float lf = Math.min(Math.max(0.25f, loadFactor), 4.0f);
            float fc = (float) mappings / lf + 1.0f;
            int cap = ((fc < DEFAULT_INITIAL_CAPACITY) ? DEFAULT_INITIAL_CAPACITY : (fc >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : tableSizeFor((int) fc));
            float ft = (float) cap * lf;
            threshold = ((cap < MAXIMUM_CAPACITY && ft < MAXIMUM_CAPACITY) ? (int) ft : Integer.MAX_VALUE);
            @SuppressWarnings({ "unchecked" })
            Node<V>[] tab = (Node<V>[]) new Node[cap];
            table = tab;

            // Read the keys and values, and put the mappings in the HashMap
            for (int i = 0; i < mappings; i++) {
                int key = s.readInt();
                @SuppressWarnings("unchecked")
                V value = (V) s.readObject();
                putVal(key, value, false, false);
            }
        }
    }

    /* ------------------------------------------------------------ */
    // iterators

    abstract class HashIterator {
        Node<V> next; // next entry to return
        Node<V> current; // current entry
        int expectedModCount; // for fast-fail
        int index; // current slot

        HashIterator() {
            expectedModCount = modCount;
            Node<V>[] t = table;
            current = next = null;
            index = 0;
            if (t != null && size > 0) { // advance to first entry
                do {
                } while (index < t.length && (next = t[index++]) == null);
            }
        }

        public final boolean hasNext() {
            return next != null;
        }

        final Node<V> nextNode() {
            Node<V>[] t;
            Node<V> e = next;
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            if (e == null)
                throw new NoSuchElementException();
            if ((next = (current = e).next) == null && (t = table) != null) {
                do {
                } while (index < t.length && (next = t[index++]) == null);
            }
            return e;
        }

        public final void remove() {
            Node<V> p = current;
            if (p == null)
                throw new IllegalStateException();
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            current = null;
            int key = p.key;
            removeNode(key, null, false, false);
            expectedModCount = modCount;
        }
    }

    final class KeyIterator extends HashIterator implements Iterator<Integer> {
        public final Integer next() {
            return nextNode().key;
        }
    }

    final class ValueIterator extends HashIterator implements Iterator<V> {
        public final V next() {
            return nextNode().value;
        }
    }

    final class EntryIterator extends HashIterator implements Iterator<Node<V>> {
        public final Node<V> next() {
            return nextNode();
        }
    }

    /* ------------------------------------------------------------ */
    // LinkedHashMap support

    /*
     * The following package-protected methods are designed to be overridden by LinkedHashMap, but not by any other subclass. Nearly all other internal methods are also package-protected but are declared final, so can be used by LinkedHashMap, view classes, and HashSet.
     */

    // Create a regular (non-tree) node
    Node<V> newNode(int key, V value, Node<V> next) {
        return new Node<>(key, value, next);
    }

    // For conversion from TreeNodes to plain nodes
    Node<V> replacementNode(Node<V> p, Node<V> next) {
        return new Node<>(p.key, p.value, next);
    }

    /**
     * Reset to initial default state. Called by clone and readObject.
     */
    void reinitialize() {
        table = null;
        entrySet = null;
        modCount = 0;
        threshold = 0;
        size = 0;
    }

    // Callbacks to allow LinkedHashMap post-actions
    void afterNodeAccess(Node<V> p) {
    }

    void afterNodeInsertion(boolean evict) {
    }

    void afterNodeRemoval(Node<V> p) {
    }

    // Called only from writeObject, to ensure compatible ordering.
    void internalWriteEntries(java.io.ObjectOutputStream s) throws IOException {
        Node<V>[] tab;
        if (size > 0 && (tab = table) != null) {
            for (int i = 0; i < tab.length; ++i) {
                for (Node<V> e = tab[i]; e != null; e = e.next) {
                    s.writeObject(e.key);
                    s.writeObject(e.value);
                }
            }
        }
    }

}
