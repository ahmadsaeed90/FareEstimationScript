package com.beat.fareestimation;

import com.beat.fareestimation.model.Position;
import com.beat.fareestimation.model.Ride;
import com.beat.fareestimation.repository.writer.IFareWriter;
import com.beat.fareestimation.service.FareCalculator;
import com.beat.fareestimation.task.RideProcessingTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class PositionsProcessor implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(PositionsProcessor.class);

    private PositionsQueue positionsQueue;
    private ExecutorService executorService;
    private IFareWriter fareWriter;

    @Autowired
    public PositionsProcessor(PositionsQueue positionsQueue, ExecutorService executorService, IFareWriter writer) {
        this.positionsQueue = positionsQueue;
        this.executorService = executorService;
        this.fareWriter = writer;
    }

    @Override
    public void run() {
        try {
            Ride ride = null;
            var stop = false;
            int totalLines = 0;
            int totalRides = 0;
            //Pattern p = Pattern.compile("\\d+\\.\\d+,\\d+\\.\\d+,\\d+\\.\\d+");
            //Matcher m1 = p.matcher(s1);

            while (!stop) {
                var lines = positionsQueue.poll(1000, TimeUnit.MILLISECONDS);
                if (lines == null) {
                    logger.info("nothing to read from queue");
                    continue;
                }

                for (var line : lines) {
                    if (line.charAt(0) == '*') {
                        logger.info("Stop signal received");
                        stop = true;
                        break;
                    }
                    totalLines ++;

                    var tokens = line.split(",");

                    int rideId = Integer.parseInt(tokens[0]);

                    if (ride == null) {
                        ride = new Ride(rideId);
                    } else if (rideId != ride.getRideId()) {
                        // trigger batch calculation
                        executorService.execute(new RideProcessingTask(ride, fareWriter, new FareCalculator()));
                        ride = new Ride(rideId);
                        totalRides++;
                    }

                    var p = new Position(Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2]), Long.parseLong(tokens[3]));
                    ride.addPosition(p);
                }
            }
            logger.info("Consumer completed, total lines processed=" + totalLines + ", totalRides=" + totalRides);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            awaitTerminationAfterShutdown(executorService);
        }
    }

    public void awaitTerminationAfterShutdown(ExecutorService threadPool) {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}