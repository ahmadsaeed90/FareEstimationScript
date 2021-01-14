package com.beat.fareestimation.util;

/**
 * Class for math utility functions
 */
public final class MathUtils {

    /**
     * Find distance in Km between 2 given positions
     */
    public static double distanceInKm(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = java.lang.Math.sin(deg2rad(lat1)) * java.lang.Math.sin(deg2rad(lat2)) + java.lang.Math.cos(deg2rad(lat1)) * java.lang.Math.cos(deg2rad(lat2)) * java.lang.Math.cos(deg2rad(theta));
        dist = java.lang.Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;
        return dist;
    }

    /**
     * Converts degree to radians
     */
    public static double deg2rad(double deg) {
        return (deg * java.lang.Math.PI / 180.0);
    }

    /**
     * Converts radians to degree
     */
    public static double rad2deg(double rad) {
        return (rad * 180.0 / java.lang.Math.PI);
    }

    /**
     * Find time difference in hours between 2 timestamps
     */
    public static double timeDifferenceInHours(long t1, long t2) {
        long seconds = t2 - t1;
        double hours = seconds / 3600.0;
        return hours;
    }

    /**
     * Calculates speed given 2 positions and timestamps
     */
    public static double calculateSpeed(double lat1, double lon1, long t1, double lat2, double lon2, long t2) {
        var dtHours = MathUtils.timeDifferenceInHours(t1, t2);
        var dsKms = MathUtils.distanceInKm(lat1, lon1, lat2, lon2);

        var speed = dsKms / dtHours;
        return speed;
    }
}
