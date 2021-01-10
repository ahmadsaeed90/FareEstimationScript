package com.beat.fareestimation.service;

import com.beat.fareestimation.constant.Constants;
import com.beat.fareestimation.constant.RideState;
import com.beat.fareestimation.model.Position;
import com.beat.fareestimation.model.Ride;
import com.beat.fareestimation.util.MathUtils;
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

        var dtHours = MathUtils.timeDifferenceInHours(p1.getTimestamp(), p2.getTimestamp());
        var dsKms = MathUtils.distanceInKm(p1.getLatitude(), p1.getLongitude(), p2.getLatitude(), p2.getLongitude());

        var speed = dsKms / dtHours;

        if (speed > 10) {
            //todo: fix logic
            return 1.30 * dsKms;
        }
        else {
            return Constants.IdlePerHourRate * dtHours;
        }
    }

    public double calculateFare(LinkedList<Position> positions) {

        // todo: remove duplicates

        double fare = Constants.BaseFare;
        Position prev = null;
        var it = positions.listIterator();

        // take first node as previous
        if (it.hasNext())
            prev = it.next();

        while (it.hasNext()) {
            var current = it.next();
            fare += calculateFare(prev, current);
            prev = current;
        }

        /*
        for (int i = 1; i < positions.size(); i++) {
            fare += calculateFare(positions.get(i-1), positions.get(i));
        }
         */
        return Math.max(fare, Constants.MinRideFare);
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
