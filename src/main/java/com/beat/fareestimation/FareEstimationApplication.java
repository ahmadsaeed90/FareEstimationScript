package com.beat.fareestimation;

import com.beat.fareestimation.service.IFareEstimationService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

@SpringBootApplication
public class FareEstimationApplication {

	/**
	 * Main Method - Execution starts here
	 */
	public static void main(String[] args) throws Exception {

		// Setup spring context for auto-wiring
		var applicationContext = SpringApplication.run(FareEstimationApplication.class, args);

		//new BufferedReader(new InputStreamReader(System.in));

		// Run the fare calculation process
		var fareEstimationService = applicationContext.getBean(IFareEstimationService.class);
		fareEstimationService.process(new BufferedReader(new FileReader("paths.csv")), new FileWriter("output.csv"));
	}
}

