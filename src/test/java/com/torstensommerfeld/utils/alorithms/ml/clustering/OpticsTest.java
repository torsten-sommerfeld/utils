package com.torstensommerfeld.utils.alorithms.ml.clustering;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.torstensommerfeld.utils.math.MathUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class OpticsTest {
    private Optics<GeoData> classUnderTest;

    @Before
    public void setup() {
        classUnderTest = new Optics<>(GeoData::getDistance);
    }

    @Test
    public void broadTestOnMockGeoData() {
        run(createGeoData(10, 2000), 1000, 6, 0.1);
    }

    private void run(List<GeoData> data, double maxDistance, int minPoints, double epsilonPercent) {
        long start = System.currentTimeMillis();
        ClusterResponse<GeoData> result = classUnderTest.cluster(data, maxDistance, minPoints, epsilonPercent);
        // TODO Auto-generated method stub
        long end = System.currentTimeMillis();

        System.out.println(String.format("%s: %s", data.size(), end - start));
        System.out.println(result.getClusters().size() + " " + result.getNotClustered().size());

    }

    @Test
    public void testClusteringOnDataWithoutSuitableClusters() {
        // given
        Optics<Colors> optics = new Optics<Colors>(Colors::getDistance);
        List<Colors> data = createColorData();

        // when
        ClusterResponse<Colors> cluster = optics.cluster(data, 128, 2, 0.1);

        // then
        Assert.assertEquals(data.size(), cluster.getNotClustered().size());

    }

    public List<GeoData> createGeoData(int numberOfClusters, int maxNumberOfElementsPerCluster) {
        Random rnd = new Random(0);
        List<GeoData> data = new ArrayList<>();
        for (int clusterId = 0; clusterId < numberOfClusters; ++clusterId) {
            double centerLatitude = rnd.nextDouble() * 380 - 180;
            double centerLongitude = rnd.nextDouble() * 380 - 180;
            int numbers = rnd.nextInt(maxNumberOfElementsPerCluster) + 1;
            for (int i = 0; i < numbers; ++i) {
                double latitude = centerLatitude + rnd.nextDouble() * 40 - 20;
                double longitude = centerLongitude + rnd.nextDouble() * 40 - 20;
                data.add(new GeoData(longitude, latitude));
            }
        }

        return data;
    }

    public List<Colors> createColorData() {
        List<Colors> result = new ArrayList<>();

        result.add(new Colors(0, 0, 0));
        result.add(new Colors(255, 0, 0));
        result.add(new Colors(0, 255, 0));
        // result.add(new Colors(0, 0, 255));

        return result;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class GeoData {
        double longitude;
        double latitude;

        public static double getDistance(GeoData d1, GeoData d2) {
            double lng = d1.longitude - d2.longitude;
            double lat = d1.latitude - d2.latitude;

            return lng * lng + lat * lat;
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Colors {
        private int r;
        private int g;
        private int b;

        public static double getDistance(Colors color, Colors color2) {
            return MathUtil.sqr(color.r - color2.r) + MathUtil.sqr(color.g - color2.g) + MathUtil.sqr(color.b - color2.b);
        }
    }

}
