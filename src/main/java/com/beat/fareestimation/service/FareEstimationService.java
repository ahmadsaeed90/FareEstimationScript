package com.beat.fareestimation.service;

import com.beat.fareestimation.model.Position;
import com.beat.fareestimation.model.Ride;
import com.beat.fareestimation.repository.writer.IFareWriter;
import com.beat.fareestimation.task.RideProcessingTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.concurrent.*;

/**
 * Service for processing fare estimation
 */
@Service
public class FareEstimationService implements IFareEstimationService {

    private static final Logger logger = LoggerFactory.getLogger(RideProcessingTask.class);
    private final IFareWriter fareWriter;
    private final IFareCalculator fareCalculator;
    private final ExecutorService taskExecutorService;

    @Autowired
    public FareEstimationService(IFareWriter writer, IFareCalculator fareCalculator, ExecutorService taskExecutorService) {
        this.fareWriter = writer;
        this.fareCalculator = fareCalculator;
        this.taskExecutorService = taskExecutorService;
    }

    @Override
    public void process(BufferedReader inputReader, Writer writer) throws Exception {

        logger.info("processing input stream");

        try {
            fareWriter.open(writer);
            Ride ride = null;
            String line;

            logger.info("Reading lines");
            while ((line = inputReader.readLine()) != null) {

                var tokens = line.split(",");
                int rideId = Integer.parseInt(tokens[0]);

                if (ride == null) {
                    ride = new Ride(rideId);
                }
                else if (rideId != ride.getRideId()) {
                    // Submit a ride task to pool for execution
                    taskExecutorService.execute(new RideProcessingTask(ride, fareWriter, fareCalculator));
                    ride = new Ride(rideId);
                }

                ride.addPosition(new Position(Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2]),
                        Long.parseLong(tokens[3])));
            }

            if (ride != null) {
                taskExecutorService.execute(new RideProcessingTask(ride, fareWriter, fareCalculator));
            }

            logger.info("Input processor completed");
        } finally {
            logger.info("Shutting down executor");
             if (taskExecutorService != null)
                taskExecutorService.shutdown();

            if (inputReader != null)
                inputReader.close();
            logger.info("Processing completed");
        }
    }
}
