package com.ozhi.matrixMultiplication;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import org.apache.commons.cli.ParseException;

public class Main {

	public static void main(String[] args) throws ParseException, FileNotFoundException, UnsupportedEncodingException {
		Config config = new Config(args);

		long timeBeforeMultiplication = Calendar.getInstance().getTimeInMillis();

		Matrix concurrentProduct = Matrix.concurrentProduct(config.matrix1, config.matrix2, config.tasks, config.quiet);

		long timeAfterMultiplication = Calendar.getInstance().getTimeInMillis();
		long timeTakenForMultiplication = timeAfterMultiplication - timeBeforeMultiplication;
		System.out.println(String.format("Total time of multiplication: %s", timeTakenForMultiplication));
		
		if (config.outputFile != null) {
			if (!config.quiet) {
				System.out.println("Writing result to output file " + config.outputFile);
			}
			
			config.writeMatrixToOutputFile(concurrentProduct);
		}
	}
}
