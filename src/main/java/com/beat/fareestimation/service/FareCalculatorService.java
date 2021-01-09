package com.beat.fareestimation.service;

import com.beat.fareestimation.model.Ride;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

public class FareCalculatorService implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(FareCalculatorService.class);

    private Ride ride;

    public FareCalculatorService(Ride ride) {
        this.ride = ride;
    }

    @Override
    public void run() {
        /*for (int i = 0; i < 1000000; i++) {
            System.out.println("Thread" + Thread.currentThread().getId() + "::" + i);
        }*/
        System.out.println("Thread" + Thread.currentThread().getId()  + " completed");
    }
}
