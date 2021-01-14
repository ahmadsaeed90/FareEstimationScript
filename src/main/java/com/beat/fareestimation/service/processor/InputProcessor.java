package com.beat.fareestimation.service.processor;

import com.beat.fareestimation.model.Position;
import com.beat.fareestimation.model.Ride;
import com.beat.fareestimation.service.FareCalculator;
import com.beat.fareestimation.service.IFareCalculator;
import com.beat.fareestimation.service.PositionsConsumer;
import com.beat.fareestimation.service.RideProcessor;
import com.beat.fareestimation.repository.writer.IFareWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.Queue;
import java.util.concurrent.*;

@Service
public class InputProcessor implements IInputProcessor {

    private static final Logger logger = LoggerFactory.getLogger(RideProcessor.class);
    private IFareWriter fareWriter;
    private IFareCalculator fareCalculator;

    @Autowired
    public InputProcessor(IFareWriter writer, IFareCalculator fareCalculator) {
        this.fareWriter = writer;
        this.fareCalculator = fareCalculator;
    }

    @Override
    public void process(InputStreamReader input, Writer writer) throws Exception {

        logger.info("processing input stream");
        //Queue<String> queue = new ConcurrentLinkedDeque<String>();
        //BlockingQueue<String> blockingQueue = new LinkedBlockingQueue(50000);

        BufferedReader reader = null;
        ExecutorService executorService = null;

        try {
            fareWriter.open(writer);
            fareWriter.write("test");
            reader = new BufferedReader(input);
            String line;

            executorService = Executors.newFixedThreadPool(6);

           // var rowConsumer = new Thread(new PositionsConsumer(blockingQueue, Executors.newFixedThreadPool(2), fareWriter));
           // rowConsumer.start();

            Ride ride = null;

            logger.info("Reading lines");
            while ((line = reader.readLine()) != null) {
                //blockingQueue.offer(line, 500, TimeUnit.MILLISECONDS);
                //logger.info(line);
                //var tokens = line.split(",");

                var tokens = line.split(",");
                //logger.info("Received ride " + tokens[0]);

                int rideId = Integer.parseInt(tokens[0]);

                if (ride == null) {
                    ride = new Ride(rideId);
                }
                else if (rideId != ride.getRideId()) {
                    // trigger batch calculation
                    executorService.execute(new RideProcessor(ride, fareWriter, fareCalculator));
                    //new RideProcessor(ride, fareWriter, new FareCalculator()).run();
                    //new FareCalculatorService(ride).run();
                    ride = new Ride(rideId);
                }

                ride.addPosition(new Position(Double.parseDouble(tokens[1]), Double.parseDouble(tokens[2]),
                        Long.parseLong(tokens[3])));













            }

            //blockingQueue.add("*");
            logger.info("Input processor completed");
            ///rowConsumer.join();

        } finally {
            logger.info("Shutting down executor");
             if (executorService != null)
                executorService.shutdown();
            //if (fareWriter != null)
             //   fareWriter.close();

            if (reader != null)
                reader.close();
            logger.info("Done everything");
        }
    }
}
