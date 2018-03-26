package com.torstensommerfeld.utils.alorithms.ml;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class QuickSelect<T> {

    private Comparator<T> comparator;

    public int select(List<T> items, int k) {
        return select(items, 0, items.size() - 1, k);
    }

    public int select(List<T> items, int left, int right, int k) {
        while (true) {
            if (left == right) {
                return left;
            }
            int pivotIndex = pivot(items, left, right);
            pivotIndex = partition(items, left, right, pivotIndex);

            if (k == pivotIndex) {
                return k;
            }

            if (k < pivotIndex) {
                right = pivotIndex - 1;
            }
            else {
                left = pivotIndex + 1;
            }
        }
    }

    private int pivot(List<T> items, int left, int right) {
        // for 5 or less elements just get median
        if (right - left < 5) {
            return partition5(items, left, right);
        }
        // otherwise move the medians of five-element subgroups to the first n/5 positions
        for (int i = left; i <= right; i += 5) {
            // get the median of the i'th five-element subgroup
            int subRight = i + 4;
            if (subRight > right) {
                subRight = right;
            }

            int median5 = partition5(items, i, subRight);
            int otherIndex = left + (int)Math.floor((i - left) / 5F);
            Collections.swap(items, median5, otherIndex);
        }

        // compute the median of the n/5 medians-of-five
        return select(items, left, left + (int)Math.floor((right - left) / 5F), left + (right - left) / 10);
    }

    private int partition5(List<T> items, int left, int right) {
        int size = right - left;
        switch (size) {
            case 0:
            case 1:
                return left;
            case 2:
                /*-
                 * 0 1 2  mid
                 * 2 1 0  mid
                 *
                 * 0 2 1  right
                 * 2 0 1  right
                 *
                 * 1 0 2  left
                 * 1 2 0  left
                 */
                T i1 = items.get(left);
                T i2 = items.get(left + 1);
                T i3 = items.get(right);
                int compare01 = comparator.compare(i1, i2);
                int compare12 = comparator.compare(i2, i3);
                if (compare01 <= 0 && compare12 <= 0) {
                    return left + 1;
                }
                if (compare01 >= 0 && compare12 >= 0) {
                    return left + 1;
                }
                int compare02 = comparator.compare(i1, i3);
                if (compare02 <= 0 && compare12 >= 0) {
                    return right;
                }
                if (compare12 <= 0 && compare02 >= 0) {
                    return right;
                }

                return left;
            default:
                i1 = items.get(left);
                i2 = items.get(left + 1);
                i3 = items.get(left + 2);
                T i4 = items.get(right);
                compare01 = comparator.compare(i1, i2);
                compare02 = comparator.compare(i1, i3);
                int compare03 = comparator.compare(i1, i4);

                if (compare01 >= 0 && compare02 <= 0 && compare03 <= 0) {
                    return left;
                }

                if (compare01 <= 0 && compare02 >= 0 && compare03 <= 0) {
                    return left;
                }

                if (compare01 <= 0 && compare02 <= 0 && compare03 >= 0) {
                    return left;
                }

                compare12 = comparator.compare(i2, i3);
                int compare13 = comparator.compare(i2, i4);

                if (compare01 <= 0 && compare12 <= 0 && compare13 <= 0) {
                    return left + 1;
                }

                if (compare01 >= 0 && compare12 >= 0 && compare13 <= 0) {
                    return left + 1;
                }

                if (compare01 >= 0 && compare12 <= 0 && compare13 >= 0) {
                    return left + 1;
                }

                int compare23 = comparator.compare(i3, i4);

                if (compare02 <= 0 && compare12 >= 0 && compare23 <= 0) {
                    return left + 2;
                }

                if (compare02 >= 0 && compare12 <= 0 && compare23 >= 0) {
                    return left + 2;
                }

                if (compare02 >= 0 && compare12 >= 0 && compare23 <= 0) {
                    return left + 2;
                }

                return right;
        }
    }

    private int partition(List<T> items, int left, int right, int pivotIndex) {
        T piviot = items.get(pivotIndex);
        Collections.swap(items, pivotIndex, right); // Move pivot to end
        int storeIndex = left;
        for (int i = left, end = right; i < end; ++i) {
            if (comparator.compare(items.get(i), piviot) < 0) {
                Collections.swap(items, storeIndex++, i);
            }
        }
        Collections.swap(items, right, storeIndex); // Move pivot to its final place
        return storeIndex;
    }

}
