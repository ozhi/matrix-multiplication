package com.ozhi.matrixMultiplication;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Matrix {
	private final int rows, cols;
	private double cells[][];
	
	public Matrix(int rows, int cols) {
		if (rows <= 0 || cols <= 0) {
			throw new RuntimeException("Matrix rows and cols should be positive");
		}
		
		this.rows = rows;
		this.cols = cols;
		
		this.cells = new double[rows][cols];
	}
	
	public void fillRandomly() {
		ThreadLocalRandom random = ThreadLocalRandom.current();
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				cells[i][j] = random.nextDouble();
			}
		}
	}
	
	public static Matrix sequentialProduct(Matrix matrix1, Matrix matrix2) {
		if (matrix1.cols != matrix2.rows) {
			throw new IllegalArgumentException("Can not multiply matrices of incompatible sizes");
		}
		
		Matrix result = new Matrix(matrix1.rows, matrix2.cols);
		
		for (int i = 0; i < matrix1.rows; i++) { 
			for (int j = 0; j < matrix2.cols; j++) {
				double cellValue = 0;
				
				for (int k = 0; k < matrix1.cols; k++) {
					cellValue += matrix1.getCell(i, k) * matrix2.getCell(k, j);
				}
				
				result.setCell(i, j, cellValue);
			}
		}
		
		return result;
	}
	
	public static Matrix concurrentProduct(Matrix matrix1, Matrix matrix2, int maxThreads, Logger logger) {
		ConcurrentMatrixMultiplier multiplier = new ConcurrentMatrixMultiplier(matrix1, matrix2, maxThreads, logger);
		multiplier.multiply();
		return multiplier.getResult();
	}
		
	public double getCell(int row, int col) {
		if (!isValidIndexPair(row, col)) { 
			throw new IllegalArgumentException("Can not get cell with invalid indices");	
		}
		return cells[row][col];
	}
	
	public void setCell(int row, int col, double value) { 
		if (!isValidIndexPair(row, col)) { 
			throw new IllegalArgumentException("Can not set cell with invalid indices");	
		}
		cells[row][col] = value;
	}
	
	public int getRows() { return rows; }
	
	public int getCols() { return cols; }
	
	@Override
	public String toString() {
		String result = "";
		for (int i = 0; i < rows; i++) {
			result += Arrays.toString(cells[i]) + "\n";
		}
		return result;
	}
	
	@Override
	public boolean equals(Object object) {
		if (object == this) {
			return true;
		}
		
		if (!(object instanceof Matrix)) {
			return false;
		}
		
		return equals((Matrix) object);				
	}
	
	public boolean equals(Matrix other) {
		if (rows != other.rows || cols != other.cols) { 
			return false;
		}
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (getCell(i, j) != other.getCell(i, j)) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	// Note: If you override equals, you must also override hashCode, so we use a dummy (but valid!) implementation
	@Override
	public int hashCode() { return 0; }
	
	private boolean isValidIndexPair(int row, int col) {
		return 0 <= row && row < rows && 0 <= col && col < cols;
	}
}
