package com.beat.fareestimation.service;

import com.beat.fareestimation.constant.Constants;
import com.beat.fareestimation.queue.IPositionsQueue;
import com.beat.fareestimation.PositionsProcessor;
import com.beat.fareestimation.queue.PositionsQueue;
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
import java.util.concurrent.TimeUnit;

/**
 * Service for processing fare estimation
 */
@Service
public class FareEstimationService implements IFareEstimationService {

    private static final Logger logger = LoggerFactory.getLogger(RideProcessingTask.class);
    private final IFareWriter fareWriter;
    private final IPositionsQueue positionsQueue;
    private final PositionsProcessor positionsProcessor;

    @Autowired
    public FareEstimationService(IFareWriter writer,
                                 PositionsQueue positionsQueue,
                                 PositionsProcessor positionsProcessor) {
        this.fareWriter = writer;
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

            int totalLines = 0;

            var lines = new ArrayList<String>(Constants.LinesBatchSize);
            String line;
            while ((line = inputReader.readLine()) != null) {

                lines.add(line);
                totalLines ++;

                if (lines.size() >= Constants.LinesBatchSize) {
                    if (!positionsQueue.offer(lines, 1, TimeUnit.MINUTES)) {
                        logger.info("cannot add lines batch to queue, stopping process, current size = " + positionsQueue.size());
                        throw new Exception("Queue was full for 1 minute, stopping process");
                    }

                    lines = new ArrayList<>(Constants.LinesBatchSize);
                }
            }

            if (!lines.isEmpty()) {
                // Send last batch
                positionsQueue.offer(lines, 1, TimeUnit.MINUTES);
            }

            // Now send termination signal
            positionsQueue.offer(Arrays.asList("*"), 1, TimeUnit.MINUTES);
            logger.info("Input processor completed, total lines read from file: " + totalLines);

            // Wait for consumer to finish
            consumer.join();

            logger.info("All Processing completed");
        } finally {
            inputReader.close();
            fareWriter.close();
        }
    }
}
