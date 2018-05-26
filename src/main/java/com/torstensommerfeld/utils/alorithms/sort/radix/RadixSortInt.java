package com.torstensommerfeld.utils.alorithms.sort.radix;

import com.torstensommerfeld.utils.alorithms.collections.IntList;

import lombok.Getter;

public class RadixSortInt {

    /**
     * Least significant digit, iterative, K = 32, non-negative, needs total buffer size (buckets) of size ~ n
     */
    public static void sortLsd(int[] data) {
        sortLsd(data, new Buckets());
    }

    public static void sortLsd(int[] data, Buckets buckets) {
        IntList[] bucketsArray = buckets.buckets;
        for (int k = 0; k < 4; ++k) {
            buckets.clear();
            handleKey(data, bucketsArray, k);
        }
    }

    private static void handleKey(int[] data, IntList[] bucketsArray, int k) {
        int shift = k * 8;
        // put all values in the appropriate buckets
        fillBuckets(data, bucketsArray, shift);
        // move values from the buckets back to the array
        fillArrayFromBuckets(data, bucketsArray);
    }

    private static void fillArrayFromBuckets(int[] data, IntList[] bucketsArray) {
        int pos = -1;
        for (int bucket = 0; bucket < 256; ++bucket) {
            IntList bucketData = bucketsArray[bucket];
            pos = copyBucketToArray(data, pos, bucketData);
        }
    }

    private static int copyBucketToArray(int[] data, int pos, IntList bucketData) {
        int[] bucketDataArray = bucketData.getBuffer();
        int n = 0, size = bucketData.size();
        int bucketPos = -1;
        for (int end = size / 8; n < end; n += 8) {
            data[pos + 1] = bucketDataArray[bucketPos + 1];
            data[pos + 2] = bucketDataArray[bucketPos + 2];
            data[pos + 3] = bucketDataArray[bucketPos + 3];
            data[pos + 4] = bucketDataArray[bucketPos + 4];
            data[pos + 5] = bucketDataArray[bucketPos + 5];
            data[pos + 6] = bucketDataArray[bucketPos + 6];
            data[pos + 7] = bucketDataArray[bucketPos + 7];
            data[pos + 8] = bucketDataArray[bucketPos + 8];
            pos += 8;
            bucketPos += 8;
        }
        for (; n < size; n += 1) {
            data[++pos] = bucketDataArray[n];
        }
        return pos;
    }

    private static void fillBuckets(int[] data, IntList[] bucketsArray, int shift) {
        for (int n = 0, end = data.length; n < end; ++n) {
            int value = data[n];
            int bucket = (value >> shift) & 255;
            bucketsArray[bucket].add(value);
        }
    }

    /**
     * Most significant digit, unstable, recursion, binary (K = 32), non-negative
     */
    public static void sortMsdBinaryInPlace(int[] data) {
        sortMsdBinaryInPlace(data, 0, data.length - 1);
    }

    public static void sortMsdBinaryInPlace(int[] data, int start, int end) {
        int max = 0;
        for (int i : data) {
            max = Math.max(max, i);
        }
        sortMsdBinaryInPlace(data, start, end, (int) Math.ceil(Math.log(max) / Math.log(2)));
    }

    public static void sortMsdBinaryInPlace(int[] data, int start, int end, int bitIndex) {
        if (end <= start) {
            return;
        }
        int oneBin = end + 1;
        for (int i = start; i < oneBin;) {
            int intValue = data[i];
            int bin = (intValue >> bitIndex) & 1;
            if (bin == 1) {
                data[i] = data[--oneBin];
                data[oneBin] = intValue;
            } else {
                i += 1;
            }

        }
        bitIndex -= 1;

        if (bitIndex < 0) {
            // termination
            return;
        }
        sortMsdBinaryInPlace(data, start, oneBin - 1, bitIndex);
        sortMsdBinaryInPlace(data, oneBin, end, bitIndex);

    }

    @Getter
    public static class Buckets {
        private IntList buckets[];

        public Buckets() {
            buckets = new IntList[256];
            for (int i = 0; i < 256; ++i) {
                buckets[i] = new IntList();
            }
        }

        public void clear() {
            for (int i = 0; i < 256; ++i) {
                buckets[i].clear();
            }

        }
    }

}
