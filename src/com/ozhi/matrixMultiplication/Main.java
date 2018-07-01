package com.ozhi.matrixMultiplication;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import org.apache.commons.cli.ParseException;

public class Main {

	public static void main(String[] args) throws ParseException, FileNotFoundException, UnsupportedEncodingException {
		Config config = new Config(args);
		Logger logger = new Logger(config.isQuietMode());

		long timeBeforeMultiplication = Calendar.getInstance().getTimeInMillis();

		Matrix product = Matrix.concurrentProduct(
				config.getMatrix1(), config.getMatrix2(), config.getMaxThreads(), logger);

		long timeAfterMultiplication = Calendar.getInstance().getTimeInMillis();
		
		if (!product.equals(Matrix.sequentialProduct(config.getMatrix1(), config.getMatrix2()))) {
			throw new Error("Concurrent and sequential product are not equal");
		}
		
		logger.importantLog(String.format("Total time of multiplication: %s", timeAfterMultiplication - timeBeforeMultiplication));
		
		config.processOutputMatrix(product);
	}
}
