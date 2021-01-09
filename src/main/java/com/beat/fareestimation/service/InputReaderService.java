package com.beat.fareestimation.service;

import com.beat.fareestimation.model.Position;
import com.beat.fareestimation.model.Ride;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class InputReaderService {

    public void Read(String fileName) {

        ExecutorService executorService = Executors.newFixedThreadPool(1);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;

            Ride ride = null;

            while ((line = reader.readLine()) != null) {
                var tokens = line.split(",");
                int rideId = Integer.parseInt(tokens[0]);

                if (ride == null) {
                    ride = new Ride(rideId);
                }
                else if (rideId != ride.getRideId()) {
                   // trigger batch calculation
                    executorService.submit(new FareCalculatorService(ride));
                    ride = new Ride(rideId);
                }

                ride.addPosition(new Position(Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2]),
                        Long.parseLong(tokens[3])));
            }

            executorService.submit(new FareCalculatorService(ride));
            System.out.println("Reader completed");

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (executorService != null)
                executorService.shutdown();
        }
    }


}
