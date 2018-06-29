package com.ozhi.matrixMultiplication;

public class ConcurrentMatrixMultiplier {
	private Matrix m1;
	private Matrix m2;
	private int maxThreads;
	private Matrix result;

	public ConcurrentMatrixMultiplier(Matrix m1, Matrix m2, int maxThreads) {
		if (maxThreads < 1 || maxThreads > 32) {
			throw new RuntimeException("invalid number of threads passed as arg to ConcurrentMatrixMultiplier");
		}

		this.m1 = m1;
		this.m2 = m2;
		this.maxThreads = maxThreads;
		this.result = null;
	}
	
	public Matrix getResult() { return result; }

	public void multiply() {
		/**
		 * Let N be the number of cells in the result matrix. N = result.rows * result.cols.
		 * They are numbered from 0 to N-1.
		 * Cell number n is actually cell(n / result.cols, n % result.cols).
		 * Each thread computes N/threads cells with consecutive numbers.  
		 */

		this.result = new Matrix(m1.getRows(), m2.getCols());
		
		int cells = result.getRows() * result.getCols();
		int threads = calculateActualThreads(cells, maxThreads - 1);
		
		int intervalLength = cells / threads;
		for (int t = 0; t < threads; t++) {
			int fromCellNumber = t * intervalLength;
			int toCellNumber = (t + 1) * intervalLength;
			if (t == threads - 1 ) {
				toCellNumber = cells;
			}
			
			Thread cellsCalculator = new Thread(new CellsCalculator(fromCellNumber, toCellNumber));
			cellsCalculator.start();

			try {
				cellsCalculator.join();
			} catch (InterruptedException e) {
				System.out.println("ConcurrentMatrixMultiplier main thread interrupted");
			}
		}
	}

	private class CellsCalculator implements Runnable {
		private int fromCellNumber;
		private int toCellNumber;		

		public CellsCalculator(int fromCellNumber, int toCellNumber) {
			this.fromCellNumber = fromCellNumber;	
			this.toCellNumber = toCellNumber;
		}

		// calculate cells with numbers [fromCellNumber; toCellNumber)
		public void run() {
			for (int cellNumber = fromCellNumber; cellNumber < toCellNumber; cellNumber++) {
				int row = cellNumber / result.getCols();
				int col = cellNumber % result.getCols();
				
				double cellValue = 0;

				int limit = m1.getCols();
				for (int i = 0; i < limit; i++) {
					cellValue += m1.getCell(row, i) * m2.getCell(i, col);
				}

				result.setCell(row, col, cellValue);
			}
		}
	}
	
	int calculateActualThreads(int cells, int maxThreads) {
		int threads = maxThreads;
		
		if (threads > cells / 100) {
			threads = cells / 100;
		}
		
		if (threads == 0) {
			threads = 1;
		}
		
		return threads;
	}
}
