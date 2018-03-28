package com.torstensommerfeld.utils.alorithms.ml.clustering;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.torstensommerfeld.utils.alorithms.collections.UnOrderedArrayList;
import com.torstensommerfeld.utils.alorithms.ml.QuickSelect;

import lombok.Getter;
import lombok.ToString;
import progress.DummyProgressImpl;
import progress.Progress;
import progress.ProgressImpl;

public class Optics<T> {

    private DistanceProvider<T> distanceProvider;
    private QuickSelect<ClusterItem<T>> quickSelect = new QuickSelect<>(ClusterItem::compateDistanceToCurrent);

    public Optics(DistanceProvider<T> distanceProvider) {
        this.distanceProvider = distanceProvider;
    }

    public ClusterResponse<T> cluster(T[] items, double maxDistance, int minPoints, double epsilonPercent) {
        return cluster(items, maxDistance, minPoints, epsilonPercent, new DummyProgressImpl());
    }

    public ClusterResponse<T> cluster(T[] items, double maxDistance, int minPoints, double epsilonPercent, Progress progress) {
        @SuppressWarnings("unchecked")
        ClusterItem<T>[] clusterItems = Arrays.stream(items).map(ClusterItem<T>::new).toArray(count -> new ClusterItem[count]);
        return cluster(clusterItems, maxDistance, minPoints, epsilonPercent, progress);
    }

    public ClusterResponse<T> cluster(List<T> items, double maxDistance, int minPoints, double epsilonPercent) {
        return cluster(items, maxDistance, minPoints, epsilonPercent, new DummyProgressImpl());
    }

    public ClusterResponse<T> cluster(List<T> items, double maxDistance, int minPoints, double epsilonPercent, Progress progress) {
        @SuppressWarnings("unchecked")
        ClusterItem<T>[] clusterItems = items.stream().map(ClusterItem<T>::new).toArray(count -> new ClusterItem[count]);
        return cluster(clusterItems, maxDistance, minPoints, epsilonPercent, progress);
    }

    private ClusterResponse<T> cluster(ClusterItem<T>[] clusterItems, double maxDistance, int minPoints, double epsilonPercent, Progress progress) {

        // create sub processes to reflect the 2 stages
        Progress sortProgress = new ProgressImpl("Order by reachable distance");
        Progress detectProgress = new ProgressImpl("Detect cluster");
        progress.addChild(sortProgress, 5);
        progress.addChild(detectProgress, 5);
        double progressIncrement = 1.0 / clusterItems.length;

        // prepare work buffers
        List<ClusterItem<T>> neighbors = new UnOrderedArrayList<>(clusterItems.length - 1);
        List<ClusterItem<T>> result = new ArrayList<>(clusterItems.length + 1);
        List<ClusterItem<T>> seeds = new UnOrderedArrayList<>();

        // for each unprocessed point p of DB
        for (int i = 0, end = clusterItems.length; i < end; ++i) {
            sortProgress.addProgress(progressIncrement);
            ClusterItem<T> p = clusterItems[i];
            if (p.isProcessed()) {
                // p is already processed
                continue;
            }

            // mark p as processed
            p.processed = true;

            // N = getNeighbors(p, eps)
            getNeighbors(clusterItems, p, maxDistance, neighbors);

            // output p to the ordered list
            result.add(p);
            p.coreDistance = getCoreDistance(neighbors, minPoints);

            // if (core-distance(p, eps, Minpts) != UNDEFINED)
            if (p.coreDistance > 0) {

                // Seeds = empty priority queue
                seeds.clear();

                // update(N, p, Seeds, eps, Minpts)
                update(neighbors, p, seeds);
                // for each next q in Seeds
                while (!seeds.isEmpty()) {
                    handleBestSeed(maxDistance, minPoints, clusterItems, neighbors, result, seeds, sortProgress);
                }
            }
        }
        sortProgress.finish();
        return detect(result, epsilonPercent, minPoints, detectProgress);
    }

    private void handleBestSeed(double maxDistance, int minPoints, ClusterItem<T>[] clusterItems, List<ClusterItem<T>> neighbors, List<ClusterItem<T>> result, List<ClusterItem<T>> seeds, Progress sortProgress) {
        // find next seed with the lowest reachable distance
        int bestReachableDistanceIndex = getBestReachableDistanceIndex(seeds);
        ClusterItem<T> q = seeds.remove(bestReachableDistanceIndex);
        // N' = getNeighbors(q, eps)
        getNeighbors(clusterItems, q, maxDistance, neighbors);
        q.coreDistance = getCoreDistance(neighbors, minPoints);
        // mark q as processed
        q.processed = true;
        // output q to the ordered list
        result.add(q);
        sortProgress.addProgress(1.0 / clusterItems.length);
        // if (core-distance(q, eps, Minpts) != UNDEFINED)
        if (q.coreDistance > 0) {
            // update(N', q, Seeds, eps, Minpts)
            update(neighbors, q, seeds);
        }
    }

    private int getBestReachableDistanceIndex(List<ClusterItem<T>> seeds) {
        int bestReachableDistanceIndex = 0;
        double bestReachableDistance = seeds.get(0).reachableDistance;
        for (int j = seeds.size() - 1; j > 0; --j) {
            double currentBestReachableDistance = seeds.get(j).reachableDistance;
            if (bestReachableDistance > currentBestReachableDistance) {
                bestReachableDistance = currentBestReachableDistance;
                bestReachableDistanceIndex = j;
            }
        }
        return bestReachableDistanceIndex;
    }

    private ClusterResponse<T> detect(List<ClusterItem<T>> result, double epsilonPercent, int minPoints, Progress detectProgress) {
        List<T> notClustered = new ArrayList<>();
        List<List<T>> clusters = new ArrayList<>();
        List<Integer> steepDownList = new ArrayList<>();

        ensureReachableDistance(result, epsilonPercent);
        // result.forEach(r -> System.out.println(r.reachableDistance));

        int index = 0;
        int end = result.size() - 1;

        // start going through the results and try to find clusters
        while (index < end) {
            detectProgress.setProgress(index / (double) end);
            // find downward start (needs to be steep)
            int startDownward = findStartDownward(result, epsilonPercent, index, end);
            steepDownList.add(startDownward);

            // find upward start and collect all potential downward starts
            int endDownward = findEndDownward(result, epsilonPercent, steepDownList, end, startDownward);

            // find largest cluster possible
            int endUpward;
            int endUpwardBestCluster = -1;
            int startDownwardBestCluster = -1;
            for (endUpward = endDownward; endUpward < end; ++endUpward) {
                ClusterItem<T> current = result.get(endUpward);
                ClusterItem<T> next = result.get(endUpward + 1);
                if (isDownward(current, next)) {
                    // termination condition: this could be a new cluster
                    break;
                }
                if (isSteepUpward(current, next, epsilonPercent)) {
                    // check if we can make a cluster out of all start downwards and this upward
                    for (int potentialDownwardsStart : steepDownList) {
                        if (isCluster(potentialDownwardsStart, endUpward, minPoints)) {
                            int size = endUpward - potentialDownwardsStart;
                            if (startDownwardBestCluster < 0 || // first potential cluster found
                                    size > endUpwardBestCluster - startDownwardBestCluster // current potential cluster
                                                                                           // is larger then the
                                                                                           // previous best cluster
                                                                                           // found
                            ) {
                                endUpwardBestCluster = endUpward + 1;
                                startDownwardBestCluster = potentialDownwardsStart;
                            }
                        }
                    }
                }
                // keep going
            }

            addToCluster(result, notClustered, clusters, index, endUpward, endUpwardBestCluster, startDownwardBestCluster);

            steepDownList.clear();
            index = endUpward;
        }

        detectProgress.finish();
        return new ClusterResponse<>(notClustered, clusters);
    }

    private void addToCluster(List<ClusterItem<T>> result, List<T> notClustered, List<List<T>> clusters, int index, int endUpward, int endUpwardBestCluster, int startDownwardBestCluster) {
        if (startDownwardBestCluster >= 0) {
            // cluster found
            List<T> cluster = new ArrayList<>();
            clusters.add(cluster);
            for (int i = startDownwardBestCluster; i < endUpwardBestCluster; ++i) {
                cluster.add(result.get(i).item);

            }
            for (int i = index; i < startDownwardBestCluster; ++i) {
                notClustered.add(result.get(i).item);
            }
        } else {
            // add not clustered items
            for (int i = index; i < endUpward; ++i) {
                notClustered.add(result.get(i).item);
            }
        }
    }

    private int findEndDownward(List<ClusterItem<T>> result, double epsilonPercent, List<Integer> steepDownList, int end, int startDownward) {
        int endDownward;
        for (endDownward = startDownward + 1; endDownward < end; ++endDownward) {
            ClusterItem<T> current = result.get(endDownward);
            ClusterItem<T> next = result.get(endDownward + 1);
            if (isUpward(current, next)) {
                // one upwards signals that it should only go upwards until the cluster has finished
                break;
            }
            if (isSteepDownward(current, next, epsilonPercent)) {
                // this could be an alternative (downward) start as well
                steepDownList.add(endDownward);
            }
            // we are still in downwards land -> keep going
        }
        return endDownward;
    }

    private int findStartDownward(List<ClusterItem<T>> result, double epsilonPercent, int index, int end) {
        int startDownward = 0;
        for (startDownward = index; startDownward < end; ++startDownward) {
            ClusterItem<T> current = result.get(startDownward);
            ClusterItem<T> next = result.get(startDownward + 1);
            if (isSteepDownward(current, next, epsilonPercent)) {
                // found a start downwards
                break;
            }
            // current index is not part of any cluster
        }
        return startDownward;
    }

    private void ensureReachableDistance(List<ClusterItem<T>> result, double epsilonPercent) {
        // update unknown reachableDistance with max reachableDistance
        double maxReachableDistance = getMaxReachableDistance(result, epsilonPercent);
        result.forEach(r -> {
            if (r.reachableDistance < 0)
                r.reachableDistance = maxReachableDistance;
        });

        // add termination cluster item
        ClusterItem<T> end = new ClusterItem<>(null);
        end.reachableDistance = maxReachableDistance;
        result.add(end);
    }

    private double getMaxReachableDistance(List<ClusterItem<T>> result, double epsilonPercent) {
        return result.stream().mapToDouble(ClusterItem::getReachableDistance).max().getAsDouble() / (0.999 - epsilonPercent);
    }

    private boolean isCluster(int start, int end, int minPoints) {
        int size = 1 + end - start;
        return (size >= minPoints);
    }

    private static final <T> boolean isSteepUpward(ClusterItem<T> p1, ClusterItem<T> p2, double epsilonPercent) {
        return p1.reachableDistance <= p2.reachableDistance * (1D - epsilonPercent);
    }

    private static final <T> boolean isUpward(ClusterItem<T> p1, ClusterItem<T> p2) {
        return p1.reachableDistance < p2.reachableDistance;
    }

    private static final <T> boolean isDownward(ClusterItem<T> p1, ClusterItem<T> p2) {
        return p1.reachableDistance > p2.reachableDistance;
    }

    private static final <T> boolean isSteepDownward(ClusterItem<T> p1, ClusterItem<T> p2, double epsilonPercent) {
        return p1.reachableDistance * (1D - epsilonPercent) >= p2.reachableDistance;
    }

    private static final <T> void update(List<ClusterItem<T>> neighbors, ClusterItem<T> p, List<ClusterItem<T>> seeds) {
        // for each o in N
        for (int i = 0, end = neighbors.size(); i < end; ++i) {
            ClusterItem<T> o = neighbors.get(i);
            // if (o is not processed)
            if (!o.isProcessed()) {
                // new-reach-dist = max(coredist, dist(p,o))
                double distance = o.distanceToCurrent;
                double newReachDistance = Math.max(p.coreDistance, distance);
                // if (o.reachability-distance == UNDEFINED) // o is not in Seeds
                if (o.reachableDistance < 0) {
                    // o.reachability-distance = new-reach-dist
                    o.reachableDistance = newReachDistance;
                    // Seeds.insert(o, new-reach-dist)
                    seeds.add(o);
                } else {
                    // else // o in Seeds, check for improvement
                    if (newReachDistance < o.reachableDistance) {
                        o.reachableDistance = newReachDistance;
                        // Seeds.move-up(o, new-reach-dist)
                    }
                }
            }
        }

    }

    private final double getCoreDistance(List<ClusterItem<T>> db, int minPoints) {
        if (db.size() < minPoints - 1) {
            return -1;
        }
        int index1 = quickSelect.select(db, minPoints - 1);
        return db.get(index1).distanceToCurrent;
    }

    private final void getNeighbors(ClusterItem<T>[] clusterItems, ClusterItem<T> p, double maxDistance, List<ClusterItem<T>> neighbors) {
        neighbors.clear();
        for (int i = 0, end = clusterItems.length; i < end; ++i) {
            ClusterItem<T> p2 = clusterItems[i];
            double distance = distanceProvider.getDistance(p.item, p2.item);
            if (distance < maxDistance && p2 != p) {
                neighbors.add(p2);
                p2.distanceToCurrent = distance;
            }
        }
    }

    @Getter
    @ToString
    private static final class ClusterItem<T> {
        private T item;
        private boolean processed;
        private double coreDistance;
        private double reachableDistance;
        private double distanceToCurrent;

        public ClusterItem(T item) {
            this.item = item;
            this.processed = false;
            this.coreDistance = -1;
            this.reachableDistance = -1;
        }

        private static <T> int compateDistanceToCurrent(ClusterItem<T> i1, ClusterItem<T> i2) {
            return Double.compare(i1.distanceToCurrent, i2.distanceToCurrent);
        }

    }

    public interface DistanceProvider<T2> {
        double getDistance(T2 p1, T2 p2);
    }

}
