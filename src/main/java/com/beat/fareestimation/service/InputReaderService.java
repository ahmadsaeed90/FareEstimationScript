package com.beat.fareestimation.service;

import com.beat.fareestimation.model.Position;
import com.beat.fareestimation.model.Ride;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

@Service
public class InputReaderService {

    private static final Logger logger = LoggerFactory.getLogger(FareCalculatorService.class);

    public void Read(String fileName) {

        logger.info("Reading file = " + fileName);

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        //BlockingQueue<String> blockingQueue = new LinkedBlockingDeque<>();

        //executorService.submit(new DataRowConsumer(blockingQueue));


        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;

            Ride ride = null;
            //logger.info("Producer started");
            while ((line = reader.readLine()) != null) {

                //blockingQueue.put(line);
                //System.out.println("added to queue");

                var tokens = line.split(",");
                int rideId = Integer.parseInt(tokens[0]);

                if (ride == null) {
                    ride = new Ride(rideId);
                }
                else if (rideId != ride.getRideId()) {
                   // trigger batch calculation
                    executorService.submit(new FareCalculatorService(ride));
                    //new FareCalculatorService(ride).run();
                    ride = new Ride(rideId);
                }

                ride.addPosition(new Position(Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2]),
                        Long.parseLong(tokens[3])));
            }
            //blockingQueue.add("*");
            logger.info("Producer done");

            executorService.submit(new FareCalculatorService(ride));
           // System.out.println("Reader completed");

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (executorService != null)
               executorService.shutdown();
        }
    }


}
