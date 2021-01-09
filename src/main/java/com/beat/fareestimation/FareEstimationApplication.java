package com.beat.fareestimation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

@SpringBootApplication
public class FareEstimationApplication {

	public static void main(String[] args) {

		/*try {
			generateTestFile();
		} catch (Exception e) {
			e.printStackTrace();
		}*/

		int count = 0;

		try {
			BufferedReader reader = new BufferedReader(new FileReader("test.csv"));
			String line;

			while ((line = reader.readLine()) != null) {
				count++;
			}

			System.out.println("count = " + count);

		} catch (Exception e) {
			e.printStackTrace();
		}



		SpringApplication.run(FareEstimationApplication.class, args);
	}

	public static void generateTestFile() throws Exception {

		FileWriter writer = new FileWriter("test.csv");

		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			writer.write(i + ",1,37.966625,23.728263,1405594974\n");
		}

		writer.close();
	}

}

