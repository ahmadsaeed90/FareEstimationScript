package com.beat.fareestimation.service;

import com.beat.fareestimation.model.Ride;
import com.beat.fareestimation.repository.writer.IFareWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RideProcessor implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RideProcessor.class);

    private Ride ride;
    private IFareWriter writer;

    public RideProcessor(Ride ride, IFareWriter writer) {
        this.ride = ride;
        this.writer = writer;
    }

    @Override
    public void run() {
        try {
            var fare = FareCalculator.calculateFare(this.ride);
            writer.write(this.ride.getRideId() + "," + fare +  System.lineSeparator());
        }
        catch (Exception e) {
            logger.error("Error in processing ride " + this.ride.getRideId(), e);
        }
    }
}
