package com.ozhi.matrixMultiplication;

//import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

public class ConcurrentMatrixMultiplierTest {

	@Test
	public void concurrnetProducShouldEqualSequentialProduct() {
		Matrix matrix1 = new Matrix(100, 500);
		Matrix matrix2 = new Matrix(500, 700);
		Matrix sequentialProduct = Matrix.sequentialProduct(matrix1, matrix2);
		int maxThreads = 4;
		ConcurrentMatrixMultiplier cmm = new ConcurrentMatrixMultiplier(matrix1, matrix2, maxThreads, new Logger(false));
		
		cmm.multiply();
		
		Assert.assertTrue(sequentialProduct.equals(cmm.getResult()));
	}
	
	@Test(expected = RuntimeException.class)
	public void multipliedMatricesShouldBeOfCompatibleSizes() {
		ConcurrentMatrixMultiplier cmm = new ConcurrentMatrixMultiplier(
				new Matrix(1,1), new Matrix(100, 100), 1, new Logger(false));
	}

	@Test(expected = RuntimeException.class)
	public void maxThreadsShouldBePositive() {
		ConcurrentMatrixMultiplier cmm = new ConcurrentMatrixMultiplier(
				new Matrix(1,2), new Matrix(2, 1), 0, new Logger(false));
	}
	
	@Test(expected = RuntimeException.class)
	public void maxThreadsShouldBeLTE32() {
		ConcurrentMatrixMultiplier cmm = new ConcurrentMatrixMultiplier(
				new Matrix(1,2), new Matrix(2, 1), 33, new Logger(false));
	}
	

}
