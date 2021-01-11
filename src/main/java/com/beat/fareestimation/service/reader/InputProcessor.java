package com.beat.fareestimation.service.reader;

import com.beat.fareestimation.service.DataRowConsumer;
import com.beat.fareestimation.service.FareCalculatorService;
import com.beat.fareestimation.repository.writer.IFareWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executors;

@Service
public class InputProcessor implements IInputProcessor {

    private static final Logger logger = LoggerFactory.getLogger(FareCalculatorService.class);
    private IFareWriter fareWriter;

    @Autowired
    public InputProcessor(IFareWriter writer) {
        this.fareWriter = writer;
    }

    @Override
    public void process(InputStreamReader input, Writer writer) throws Exception {

        logger.info("processing input stream");
        Queue<String> queue = new ConcurrentLinkedDeque<String>();
        BufferedReader reader = null;

        try {
            fareWriter.open(writer);
            reader = new BufferedReader(input);
            String line;

            var rowConsumer = new Thread(new DataRowConsumer(queue, Executors.newFixedThreadPool(1), fareWriter));
            rowConsumer.start();

            while ((line = reader.readLine()) != null) {
                queue.add(line);
            }

            queue.add("*");
            logger.info("Input processor completed");
            rowConsumer.join();
        } finally {
            // if (executorService != null)
            //    executorService.shutdown();
            //if (writer != null)
            //    writer.close();

            if (reader != null)
                reader.close();
        }
    }
}
