package com.beat.fareestimation;

import com.beat.fareestimation.service.IFareEstimationService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;

@SpringBootApplication
public class FareEstimationApplication {

	/**
	 * Main Method - Execution starts here
	 */
	public static void main(String[] args) throws Exception {

		// Setup spring context for auto-wiring
		var applicationContext = SpringApplication.run(FareEstimationApplication.class, args);

		// Run the fare calculation process
		var fareEstimationService = applicationContext.getBean(IFareEstimationService.class);

		// For reading from file uncomment below line and comment out second line
		//fareEstimationService.process(new BufferedReader(new FileReader("paths.csv")), new FileWriter("output.csv"));
		fareEstimationService.process(new BufferedReader(new InputStreamReader(System.in)), new FileWriter("output.csv"));
	}
}

