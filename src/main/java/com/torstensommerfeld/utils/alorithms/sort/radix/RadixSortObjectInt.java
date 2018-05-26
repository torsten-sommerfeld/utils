package com.torstensommerfeld.utils.alorithms.sort.radix;

import java.util.List;

import com.torstensommerfeld.utils.alorithms.collections.UnOrderedArrayList;

import lombok.Getter;

public class RadixSortObjectInt {

    /**
     * Least significant digit, iterative, K = 32, non-negative, needs total buffer size (buckets) of size ~ n
     */
    public static <T> void sortLsd(T[] data, KeyAccessor<T> keyAccessor) {
        sortLsd(data, keyAccessor, new Buckets<T>());
    }

    public static <T> void sortLsd(T[] data, KeyAccessor<T> keyAccessor, Buckets<T> buckets) {
        List<T>[] bucketsArray = buckets.buckets;
        for (int k = 0; k < 4; ++k) {
            buckets.clear();
            handleKey(data, keyAccessor, bucketsArray, k);
        }
    }

    private static <T> void handleKey(T[] data, KeyAccessor<T> keyAccessor, List<T>[] bucketsArray, int k) {
        int shift = k * 8;
        // put all values in the appropriate buckets
        fillBuckets(data, keyAccessor, bucketsArray, shift);
        // move values from the buckets back to the array
        fillArrayFromBuckets(data, bucketsArray);
    }

    private static <T> void fillArrayFromBuckets(T[] data, List<T>[] bucketsArray) {
        int pos = -1;
        for (int bucket = 0; bucket < 256; ++bucket) {
            List<T> bucketData = bucketsArray[bucket];
            pos = copyBucketToArray(data, pos, bucketData);
        }
    }

    private static <T> int copyBucketToArray(T[] data, int pos, List<T> bucketData) {
        int n = 0, size = bucketData.size();
        int bucketPos = -1;
        for (int end = size / 8; n < end; n += 8) {
            data[pos + 1] = bucketData.get(bucketPos + 1);
            data[pos + 2] = bucketData.get(bucketPos + 2);
            data[pos + 3] = bucketData.get(bucketPos + 3);
            data[pos + 4] = bucketData.get(bucketPos + 4);
            data[pos + 5] = bucketData.get(bucketPos + 5);
            data[pos + 6] = bucketData.get(bucketPos + 6);
            data[pos + 7] = bucketData.get(bucketPos + 7);
            data[pos + 8] = bucketData.get(bucketPos + 8);
            pos += 8;
            bucketPos += 8;
        }
        for (; n < size; n += 1) {
            data[++pos] = bucketData.get(n);
        }
        return pos;
    }

    private static <T> void fillBuckets(T[] data, KeyAccessor<T> keyAccessor, List<T>[] bucketsArray, int shift) {
        for (int n = 0, end = data.length; n < end; ++n) {
            T value = data[n];
            int bucket = (keyAccessor.getKey(value) >> shift) & 255;
            bucketsArray[bucket].add(value);
        }
    }

    @Getter
    public static class Buckets<T> {
        private List<T> buckets[];

        @SuppressWarnings("unchecked")
        public Buckets() {
            buckets = new List[256];
            for (int i = 0; i < 256; ++i) {
                buckets[i] = new UnOrderedArrayList<>(); // slightly faster than ArrayList; Linked list is terrible slow
            }
        }

        public void clear() {
            for (int i = 0; i < 256; ++i) {
                buckets[i].clear();
            }

        }
    }

    public interface KeyAccessor<T> {
        int getKey(T object);
    }

}
