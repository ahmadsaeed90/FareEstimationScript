package com.beat.fareestimation.service;

import com.beat.fareestimation.constant.Constants;
import com.beat.fareestimation.model.Position;
import com.beat.fareestimation.util.MathUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.LinkedList;

@Service
public class FareCalculator implements IFareCalculator {

    public double calculateFare(Position p1, Position p2) {

        var dtHours = MathUtils.timeDifferenceInHours(p1.getTimestamp(), p2.getTimestamp());
        var dsKms = MathUtils.distanceInKm(p1.getLatitude(), p1.getLongitude(), p2.getLatitude(), p2.getLongitude());

        var speed = dsKms / dtHours;

        if (speed > 10) {
            var dt2 = LocalDateTime.ofEpochSecond(p2.getTimestamp(), 0, ZoneOffset.UTC);

            // Time of day (05:00, 00:00], 0.74 per km
            // Time of day (00:00, 05:00] 1.30 per km
            var rate  = dt2.getHour() >= 0 && dt2.getHour() <= 5? 1.30 : 0.74;
            return rate * dsKms;
        }
        else { // Idle state
            return Constants.IdlePerHourRate * dtHours;
        }
    }

    public double calculateRideFare(LinkedList<Position> positions) {

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
}
