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
	
	public Matrix times(Matrix other) {
		if (true) {
			throw new Error("Only use the concurrent multiplication method");
		}
		
		if (cols != other.rows) {
			throw new IllegalArgumentException("Can not multiply matrices of incompatible sizes");
		}
		
		Matrix result = new Matrix(rows, other.cols);
		
		for (int i = 0; i < rows; i++) { 
			for (int j = 0; j < other.cols; j++) {
				double sum = 0;
				
				for (int k = 0; k < cols; k++) {
					sum += getCell(i, k) * other.getCell(k, j);
				}
				
				result.setCell(i, j, sum);
			}
		}
		
		return result;
	}
	
	public static Matrix concurrentProduct(Matrix m1, Matrix m2, int threads, boolean quiet) {
		ConcurrentMatrixMultiplier cmm = new ConcurrentMatrixMultiplier(m1, m2, threads, quiet);
		cmm.multiply();
		return cmm.getResult();
	}
		
	public double getCell(int row, int col) {
		if (!isValidIndexPair(row, col)) { 
			throw new IllegalArgumentException();	
		}
		return cells[row][col];
	}
	
	public void setCell(int row, int col, double value) { 
		if (!isValidIndexPair(row, col)) { 
			throw new IllegalArgumentException();	
		}
		cells[row][col] = value;
	}
	
	public int getRows() { return this.rows; }
	
	public int getCols() { return this.cols; }
	
	@Override
	public String toString() {
		String result = "";
		for (int i = 0; i < rows; i++) {
			result += Arrays.toString(cells[i]) + "\n";
		}
		return result;
	}
	
	// if one overrides equals, one must also override hashCode, so we use a custom function name
	public boolean isSameAs(Matrix other) {
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
	
	private boolean isValidIndexPair(int row, int col) {
		return 0 <= row && row < rows && 0 <= col && col < cols;
	}
}
