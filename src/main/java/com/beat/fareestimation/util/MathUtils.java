package com.beat.fareestimation.util;

public class MathUtils {

    private MathUtils() { /* No instantiation allowed */ }

    public static double distanceInKm(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = java.lang.Math.sin(deg2rad(lat1)) * java.lang.Math.sin(deg2rad(lat2)) + java.lang.Math.cos(deg2rad(lat1)) * java.lang.Math.cos(deg2rad(lat2)) * java.lang.Math.cos(deg2rad(theta));
        dist = java.lang.Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;
        return (dist);
    }

    public static double deg2rad(double deg) {
        return (deg * java.lang.Math.PI / 180.0);
    }

    public static double rad2deg(double rad) {
        return (rad * 180.0 / java.lang.Math.PI);
    }

    public static double timeDifferenceInHours(long t1, long t2) {
        int seconds = (int) (t2 - t1) / 1000;
        int hours = seconds / 3600;
        return hours;
    }
}
