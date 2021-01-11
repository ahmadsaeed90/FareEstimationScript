package com.beat.fareestimation.service;

import com.beat.fareestimation.model.Position;
import com.beat.fareestimation.model.Ride;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;

public class DataRowConsumer implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(DataRowConsumer.class);

    private BlockingQueue<String> queue;

    public DataRowConsumer(BlockingQueue<String> blockingQueue) {
        this.queue = blockingQueue;
    }

    @Override
    public void run() {
        try {
            Ride ride = null;

            while (true) {
                var line = queue.take();
                if (line.charAt(0) == '*') break;

                //var tokens = line.split(",");
                //logger.info("Received ride " + tokens[0]);
                /*
                int rideId = Integer.parseInt(tokens[0]);

                if (ride == null) {
                    ride = new Ride(rideId);
                }
                else if (rideId != ride.getRideId()) {
                   // trigger batch calculation
                    //executorService.submit(new FareCalculatorService(ride));
                    ride = new Ride(rideId);
                }

                ride.addPosition(new Position(Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2]),
                        Long.parseLong(tokens[3])));
                        */

            }
            logger.info("Consumer completed");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
