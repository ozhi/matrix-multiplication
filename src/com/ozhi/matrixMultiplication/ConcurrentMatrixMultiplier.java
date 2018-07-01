package com.ozhi.matrixMultiplication;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class ConcurrentMatrixMultiplier {
	private Matrix matrix1;
	private Matrix matrix2;
	private int maxThreads;
	private Logger logger;
	
	private static final int MIN_CELLS_PER_THREAD = 1000;
	
	private Matrix result;

	public ConcurrentMatrixMultiplier(Matrix matrix1, Matrix matrix2, int maxThreads, Logger logger) {
		if (matrix1.getCols() != matrix2.getRows()) {
			throw new RuntimeException("Can not multiply matrices of incompatible sizes");
		}
		
		if (maxThreads < 1 || maxThreads > 32) {
			throw new RuntimeException("Invalid number of threads passed as arg to ConcurrentMatrixMultiplier");
		}

		this.matrix1 = matrix1;
		this.matrix2 = matrix2;
		this.maxThreads = maxThreads;
		this.logger = logger;
		this.result = null;
	}

	public void multiply() {
		/**
		 * Let N be the number of cells in the result matrix. N = result.rows * result.cols.
		 * They are numbered from 0 to N-1.
		 * Cell number n is actually cell(n / result.cols, n % result.cols).
		 * Each thread computes N/threads cells with consecutive numbers.
		 * The leftover threads are calculated by the last thread
		 */
		
		result = new Matrix(matrix1.getRows(), matrix2.getCols());

		int cellsCount = result.getRows() * result.getCols();
		int threadsCount = calculateThreadsToBeUsed(cellsCount, maxThreads);
		List<Thread> threads = createThreads(cellsCount, threadsCount);
		
		logger.log("Max threads to be used: %d", maxThreads);
		logger.log("Actual threads to be used: %d", threadsCount);		

		ListIterator<Thread> iterator = threads.listIterator();
		while (iterator.hasNext()) {
			iterator.next().start();
		}
		
		iterator = threads.listIterator();
		while (iterator.hasNext()) {
			try {
				iterator.next().join();
			} catch (InterruptedException e) {
				System.out.println("ConcurrentMatrixMultiplier main thread interrupted");
				return;
			}
		}
	}
	
	public Matrix getResult() {
		return result;
	}
	
	private int calculateThreadsToBeUsed(int cells, int maxThreads) {
		int threads = maxThreads;
		
		if (cells / threads < MIN_CELLS_PER_THREAD) {
			threads = cells / MIN_CELLS_PER_THREAD;
		}
		
		if (threads == 0) {
			threads = 1;
		}
		
		return threads;
	}
	
	private List<Thread> createThreads(int cellsCount, int threadsCount) {
		LinkedList<Thread> threads = new LinkedList<Thread>(); 
		int intervalLength = cellsCount / threadsCount;

		for (int threadIndex = 0; threadIndex < threadsCount; threadIndex++) {
			int intervalStart = threadIndex * intervalLength;
			int intervalEnd = (threadIndex + 1) * intervalLength;
			
			if (threadIndex == threadsCount - 1 ) {
				intervalLength = cellsCount;
			}
			
			Thread thread = new Thread(new CellsCalculator(threadIndex, intervalStart, intervalEnd));
			threads.add(thread);
		}
		
		return threads;
	}

	private class CellsCalculator implements Runnable {
		private int threadIndex;
		private int fromCellNumber;
		private int toCellNumber;

		public CellsCalculator(int threadIndex, int fromCellNumber, int toCellNumber) {
			this.threadIndex = threadIndex;
			this.fromCellNumber = fromCellNumber;
			this.toCellNumber = toCellNumber;
		}

		public void run() {
			long timeBeforeCalculation = Calendar.getInstance().getTimeInMillis();
			logger.log("Thread %d started", threadIndex);
			
			for (int cellNumber = fromCellNumber; cellNumber < toCellNumber; cellNumber++) {
				int row = cellNumber / result.getCols();
				int col = cellNumber % result.getCols();

				double cellValue = 0;

				int matricesCommonDimension = matrix1.getCols();
				for (int i = 0; i < matricesCommonDimension; i++) {
					cellValue += matrix1.getCell(row, i) * matrix2.getCell(i, col);
				}

				result.setCell(row, col, cellValue);
			}

			long timeAfterCalculation = Calendar.getInstance().getTimeInMillis();
			logger.log("Thread %d finished (execution time %d ms)", threadIndex, timeAfterCalculation - timeBeforeCalculation);
		}
	}
}
