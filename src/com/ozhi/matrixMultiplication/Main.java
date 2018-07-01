package com.ozhi.matrixMultiplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.cli.ParseException;

public class Main {

	public static void main(String[] args) throws ParseException, FileNotFoundException, UnsupportedEncodingException {
		Config config = new Config(args);
		Logger logger = new Logger(config.isQuietMode());
		
		Map<String, Matrix> matrices = config.getInputFile() != null ?
				readMatricesFromFile(config.getInputFile(), logger) :
				createMatricesFromDimensions(config.getMatrix1Rows(), config.getMatricesCommonDimension(), config.getMatrix2Cols(), logger);
				
		long timeBeforeMultiplication = Calendar.getInstance().getTimeInMillis();

		Matrix product = Matrix.concurrentProduct(matrices.get("first"), matrices.get("second"), config.getMaxThreads(), logger);

		long timeAfterMultiplication = Calendar.getInstance().getTimeInMillis();
		
		logger.logImportant("Total time of multiplication: %s ms", timeAfterMultiplication - timeBeforeMultiplication);
		
		if (config.getOutputFile() != null) {
			outputMatrixToFile(product, config.getOutputFile(), logger);
		}
	}
	
	private static Map<String, Matrix> readMatricesFromFile(String inputFile, Logger logger) throws FileNotFoundException {
		logger.log("Reading matrices from input file %s", inputFile);
		
		Scanner scanner = new Scanner(new File(inputFile));

		int matrix1Rows = scanner.nextInt();
		int matricesCommonDimension = scanner.nextInt();
		int matrix2Cols = scanner.nextInt();

		Map<String, Matrix> matrices = new HashMap<String, Matrix>();
		
		matrices.put("first", readMatrixFromScanner(scanner, matrix1Rows, matricesCommonDimension));
		matrices.put("second", readMatrixFromScanner(scanner, matricesCommonDimension, matrix2Cols));
				
		scanner.close();
		
		return matrices;
	}
	
	private static Matrix readMatrixFromScanner(Scanner scanner, int rows, int cols) {
		Matrix matrix = new Matrix(rows, cols);
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				matrix.setCell(i, j, scanner.nextDouble());
			}
		}

		return matrix;
	}
	
	private static Map<String, Matrix> createMatricesFromDimensions(int matrix1Rows, int matricesCommonDimension, int matrix2Cols, Logger logger) {
		logger.log("Generating random matrices with dimensions %d %d %d", matrix1Rows, matricesCommonDimension, matrix2Cols);
		
		Matrix matrix1 = new Matrix(matrix1Rows, matricesCommonDimension);
		Matrix matrix2 = new Matrix(matricesCommonDimension, matrix2Cols);

		matrix1.fillRandomly();
		matrix2.fillRandomly();
		
		Map<String, Matrix> matrices = new HashMap<String, Matrix>();
		matrices.put("first", matrix1);
		matrices.put("second", matrix2);

		return matrices;
	}
	
	public static void outputMatrixToFile(Matrix outputMatrix, String outputFile, Logger logger) throws FileNotFoundException, UnsupportedEncodingException {
		logger.log("Writing matrix product to output file %s", outputFile);
		
		PrintWriter writer = new PrintWriter(outputFile, "UTF-8");
		writer.println(outputMatrix);
		writer.close();
	}
}
