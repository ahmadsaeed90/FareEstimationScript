package com.beat.fareestimation.service;

import com.beat.fareestimation.constant.Constants;
import com.beat.fareestimation.model.Position;
import com.beat.fareestimation.model.Ride;
import com.beat.fareestimation.repository.writer.IFareWriter;
import com.beat.fareestimation.util.MathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import java.util.LinkedList;

public class FareCalculatorService implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(FareCalculatorService.class);

    private Ride ride;
    private IFareWriter writer;

    public FareCalculatorService(Ride ride, IFareWriter writer) {
        this.ride = ride;
        this.writer = writer;
    }

    @Override
    public void run() {
        try {
            var fare = new FareCalculator().calculateFare(this.ride);
            writer.write(this.ride.getRideId() + "," + fare +  System.lineSeparator());
        }
        catch (Exception e) {
            logger.error("Error in processing ride " + this.ride.getRideId(), e);
        }
    }
}
