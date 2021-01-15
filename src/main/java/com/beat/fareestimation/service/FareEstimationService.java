package com.beat.fareestimation.service;

import com.beat.fareestimation.IPositionsQueue;
import com.beat.fareestimation.PositionsProcessor;
import com.beat.fareestimation.PositionsQueue;
import com.beat.fareestimation.repository.writer.IFareWriter;
import com.beat.fareestimation.task.RideProcessingTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Service for processing fare estimation
 */
@Service
public class FareEstimationService implements IFareEstimationService {

    private static final Logger logger = LoggerFactory.getLogger(RideProcessingTask.class);
    private final IFareWriter fareWriter;
    //private final ExecutorService taskExecutorService;
    private final IPositionsQueue positionsQueue;
    private final PositionsProcessor positionsProcessor;

    @Autowired
    public FareEstimationService(IFareWriter writer,
                                 //ExecutorService taskExecutorService,
                                 PositionsQueue positionsQueue,
                                 PositionsProcessor positionsProcessor) {
        this.fareWriter = writer;
        //this.taskExecutorService = taskExecutorService;
        this.positionsQueue = positionsQueue;
        this.positionsProcessor = positionsProcessor;
    }

    @Override
    public void process(BufferedReader inputReader, Writer writer) throws Exception {

        logger.info("processing input stream");

        try {
            fareWriter.open(writer);

            // Start consumer thread
            var consumer = new Thread(positionsProcessor);
            consumer.start();
            logger.info("Reading lines");

            int linesGroupSize = 500;
            int totalLines = 0;

            var lines = new ArrayList<String>(linesGroupSize);
            String line;
            while ((line = inputReader.readLine()) != null) {

                lines.add(line);
                totalLines ++;

                if (lines.size() >= linesGroupSize) {
                    if (!positionsQueue.offer(lines, 5, TimeUnit.SECONDS))
                        logger.info("cannot add lines group to queue, current size = " + positionsQueue.size());

                    lines = new ArrayList<>(linesGroupSize);
                }
            }

            if (!lines.isEmpty()) {
                // Send last batch
                positionsQueue.offer(lines, 5, TimeUnit.SECONDS);
            }

            // Now send termination signal
            positionsQueue.offer(Arrays.asList("*"), 5, TimeUnit.SECONDS);
            logger.info("Input processor completed, total lines read from file: " + totalLines);

            // Wait for consumer to finish
            consumer.join();

        } finally {
            //awaitTerminationAfterShutdown(taskExecutorService);

            inputReader.close();
            fareWriter.close();
            logger.info("All Processing completed");
        }
    }
}
