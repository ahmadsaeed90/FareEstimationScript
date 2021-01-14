package com.beat.fareestimation.task;

import com.beat.fareestimation.constant.Constants;
import com.beat.fareestimation.model.Position;
import com.beat.fareestimation.model.Ride;
import com.beat.fareestimation.repository.writer.IFareWriter;
import com.beat.fareestimation.service.IFareCalculator;
import com.beat.fareestimation.util.MathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

/**
 * Task to process a single ride
 */
public class RideProcessingTask implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RideProcessingTask.class);

    private final Ride ride;
    private final IFareWriter outputWriter;
    private final IFareCalculator fareCalculator;

    public RideProcessingTask(Ride ride, IFareWriter writer, IFareCalculator calculator) {
        this.ride = ride;
        this.outputWriter = writer;
        this.fareCalculator = calculator;
    }

    public void removeDuplicates(LinkedList<Position> positions) {
        var it  = positions.listIterator();
        Position prev = null;

        if (it.hasNext())
            prev = it.next();

        while (it.hasNext()) {
            var current = it.next();
            var speed = MathUtils.calculateSpeed(prev.getLatitude(), prev.getLongitude(), prev.getTimestamp(),
                    current.getLatitude(), current.getLongitude(), current.getTimestamp());
            if (speed > Constants.InvalidSpeedThreshold) {
                it.remove();
            }
            else {
                prev = current;
            }
        }
    }

    public double calculateFare(Ride ride) {
        var positions = ride.getPositions();
        removeDuplicates(positions);
        return fareCalculator.calculateRideFare(positions);
    }

    @Override
    public void run() {
        try {
            var fare = calculateFare(this.ride);
            outputWriter.write(this.ride.getRideId() + "," + fare +  System.lineSeparator());
        }
        catch (Exception e) {
            logger.error("Error in processing ride " + this.ride.getRideId(), e);
        }
    }
}
