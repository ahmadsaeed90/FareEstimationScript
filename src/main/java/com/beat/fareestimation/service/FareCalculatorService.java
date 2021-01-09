package com.beat.fareestimation.service;

import com.beat.fareestimation.constant.Constants;
import com.beat.fareestimation.constant.RideState;
import com.beat.fareestimation.model.Position;
import com.beat.fareestimation.model.Ride;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FareCalculatorService implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(FareCalculatorService.class);

    private Ride ride;

    public FareCalculatorService(Ride ride) {
        this.ride = ride;
    }

    public void removeDuplicates(LinkedList<Position> positions) {
    }

    public double calculateFare(Position p1, Position p2) {

        var dtHours = timeDifferenceInHours(p1.getTimestamp(), p2.getTimestamp());
        var dsKms = distanceInKm(p1.getLatitude(), p1.getLongitude(), p2.getLatitude(), p2.getLongitude());

        var speed = dsKms / dtHours;

        if (speed > 10) {
            //todo:
        }
        else {
            return 11.90 * dtHours;
        }
        return 0;
    }

    public double calculateFare(List<Position> positions) {

        // todo: remove duplicates

        double fare = 1.30;

        for (int i = 1; i < positions.size(); i++) {
            fare += calculateFare(positions.get(i-1), positions.get(i));
        }
        return Math.max(fare, Constants.MinRideFare);
    }

    public RideState findState(Position p1, Position p2) {
        return RideState.Idle;
    }

    public static double distanceInKm(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;
        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public static double timeDifferenceInHours(long t1, long t2) {
        int seconds = (int) (t2 - t1) / 1000;
        int hours = seconds / 3600;
        return hours;
    }

    @Override
    public void run() {
        /*for (int i = 0; i < 1000000; i++) {
            System.out.println("Thread" + Thread.currentThread().getId() + "::" + i);
        }*/

        var fare = calculateFare(this.ride.getPositions());

        System.out.println("Fare for ride " + ride.getRideId() + " = " + fare);
    }
}
