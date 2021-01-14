package com.beat.fareestimation.service;

import com.beat.fareestimation.model.Position;
import com.beat.fareestimation.model.Ride;
import com.beat.fareestimation.repository.writer.IFareWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Time;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class PositionsConsumer implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(PositionsConsumer.class);

    //private Queue<String> queue;
    private BlockingQueue<String> blockingQueue;
    private ExecutorService executorService;
    private IFareWriter fareWriter;

    public PositionsConsumer(BlockingQueue<String> sourceQueue, ExecutorService executorService, IFareWriter writer) {
       // this.queue = sourceQueue;
        this.blockingQueue = sourceQueue;
        this.executorService = executorService;
        this.fareWriter = writer;
    }

    @Override
    public void run() {
        try {
            Ride ride = null;

            while (true) {
                var line = blockingQueue.poll(1000, TimeUnit.MILLISECONDS);
                if (line == null) {
                    logger.info("nothing to read");
                    continue;
                }
                if (line.charAt(0) == '*') break;
                //logger.info("msg received");

                var tokens = line.split(",");
                //logger.info("Received ride " + tokens[0]);

                int rideId = Integer.parseInt(tokens[0]);

                if (ride == null) {
                    ride = new Ride(rideId);
                }
                else if (rideId != ride.getRideId()) {
                   // trigger batch calculation
                    executorService.submit(new RideProcessor(ride, fareWriter, new FareCalculator()));
                    //new FareCalculatorService(ride).run();
                    ride = new Ride(rideId);
                }

                ride.addPosition(new Position(Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2]),
                        Long.parseLong(tokens[3])));
            }
            logger.info("Consumer completed");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            executorService.shutdown();
        }
    }
}
