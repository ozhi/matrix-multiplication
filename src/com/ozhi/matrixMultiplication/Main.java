package com.ozhi.matrixMultiplication;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import org.apache.commons.cli.ParseException;

public class Main {

	public static void main(String[] args) throws ParseException, FileNotFoundException, UnsupportedEncodingException {
		Config config = new Config(args);

		long timeBeforeMultiplication = Calendar.getInstance().getTimeInMillis();

		Matrix concurrentProduct = Matrix.concurrentProduct(
				config.getMatrix1(), config.getMatrix2(), config.getMaxThreads(), config.isQuietMode());

		long timeAfterMultiplication = Calendar.getInstance().getTimeInMillis();
		long timeTakenForMultiplication = timeAfterMultiplication - timeBeforeMultiplication;
		System.out.println(String.format("Total time of multiplication: %s", timeTakenForMultiplication));
		
		config.processOutputMatrix(concurrentProduct);
	}
}
