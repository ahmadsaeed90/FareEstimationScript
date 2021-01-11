package com.beat.fareestimation;

import com.beat.fareestimation.service.reader.IInputProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileReader;
import java.io.FileWriter;

@SpringBootApplication
public class FareEstimationApplication {

	private static final Logger logger = LoggerFactory.getLogger(FareEstimationApplication.class);

	public static void main(String[] args) {

		boolean isTest = false;

		if (isTest) {
			try {
				generateTestFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		var applicationContext = SpringApplication.run(FareEstimationApplication.class, args);

		if (!isTest) {
			try {
				var readerService = applicationContext.getBean(IInputProcessor.class);
				readerService.process(new FileReader("test-medium.csv"), new FileWriter("output.csv"));
			}
			catch (Exception e) {
				logger.error("Error in processing", e);
			}
		}
		System.out.println("main completed");
	}

	public static void generateTestFile() throws Exception {

		FileWriter writer = new FileWriter("test.csv");

		for (int i = 0; i < Integer.MAX_VALUE / 2; i++) {
			writer.write(i + ",37.966625,23.728263,1405594974\n");
		}

		writer.close();
	}

}

