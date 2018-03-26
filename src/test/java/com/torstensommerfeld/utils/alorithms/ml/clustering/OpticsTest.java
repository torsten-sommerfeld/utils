package com.torstensommerfeld.utils.alorithms.ml.clustering;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class OpticsTest {
    private Optics<Data> classUnderTest;

    @Before
    public void setup() {
        classUnderTest = new Optics<>(Data::getDistance);
    }

    @Test
    public void test() {
        run(createData(10, 2000), 1000, 6, 0.1);
    }

    private void run(List<Data> data, double maxDistance, int minPoints, double epsilonPercent) {
        long start = System.currentTimeMillis();
        ClusterResponse<Data> result = classUnderTest.cluster(data, maxDistance, minPoints, epsilonPercent);
        // TODO Auto-generated method stub
        long end = System.currentTimeMillis();

        System.out.println(String.format("%s: %s", data.size(), end - start));
        System.out.println(result.getClusters().size() + " " + result.getNotClustered().size());

    }

    public List<Data> createData(int numberOfClusters, int maxNumberOfElementsPerCluster) {
        Random rnd = new Random(0);
        List<Data> data = new ArrayList<>();
        for (int clusterId = 0; clusterId < numberOfClusters; ++clusterId) {
            double centerLatitude = rnd.nextDouble() * 380 - 180;
            double centerLongitude = rnd.nextDouble() * 380 - 180;
            int numbers = rnd.nextInt(maxNumberOfElementsPerCluster) + 1;
            for (int i = 0; i < numbers; ++i) {
                double latitude = centerLatitude + rnd.nextDouble() * 40 - 20;
                double longitude = centerLongitude + rnd.nextDouble() * 40 - 20;
                data.add(new Data(longitude, latitude));
            }
        }

        return data;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Data {
        double longitude;
        double latitude;

        public static double getDistance(Data d1, Data d2) {
            double lng = d1.longitude - d2.longitude;
            double lat = d1.latitude - d2.latitude;

            return lng * lng + lat * lat;
        }
    }
}
