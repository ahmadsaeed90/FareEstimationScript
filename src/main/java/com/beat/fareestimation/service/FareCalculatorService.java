package com.beat.fareestimation.service;

import com.beat.fareestimation.constant.Constants;
import com.beat.fareestimation.model.Position;
import com.beat.fareestimation.model.Ride;
import com.beat.fareestimation.util.MathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

public class FareCalculatorService implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(FareCalculatorService.class);

    private Ride ride;

    public FareCalculatorService(Ride ride) {
        this.ride = ride;
    }

    public void removeDuplicates(LinkedList<Position> positions) {
        var it  = positions.listIterator();
        Position prev = null;

        if (it.hasNext())
            prev = it.next();

        while (it.hasNext()) {
            var current = it.next();
            var speed = findSpeed(prev, current);
            if (speed > Constants.InvalidSpeedThreshold) {
                it.remove();
            }
            else {
                prev = current;
            }
        }
    }

    public double findSpeed(Position p1, Position p2) {
        var dtHours = MathUtils.timeDifferenceInHours(p1.getTimestamp(), p2.getTimestamp());
        var dsKms = MathUtils.distanceInKm(p1.getLatitude(), p1.getLongitude(), p2.getLatitude(), p2.getLongitude());

        var speed = dsKms / dtHours;
        return speed;
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

        return Math.max(fare, Constants.MinRideFare);
    }

    public double processRide(Ride ride) {
        removeDuplicates(ride.getPositions());
        return calculateFare(ride.getPositions());
    }

    @Override
    public void run() {
        var fare = processRide(this.ride);
        logger.info("Fare for ride " + ride.getRideId() + " = " + fare);
    }
}
