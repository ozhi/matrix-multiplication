package com.ozhi.matrixMultiplication;

import org.junit.Test;

import junit.framework.Assert;

public class MatrixTest {	
	@Test()
	public void shouldGetRows() {
		Matrix matrix = new Matrix(4, 5);
		Assert.assertEquals(4, matrix.getRows());
	}
	
	@Test()
	public void shouldGetCols() {
		Matrix matrix = new Matrix(4, 5);
		Assert.assertEquals(5, matrix.getCols());
	}	
	
	@Test(expected = RuntimeException.class)
	public void arrayDimensionsShouldBePositive() {
		new Matrix(-1, 2);
	}
	
	@Test
	public void newMatrixShouldBeFilledWithZeros() {
		Matrix matrix = new Matrix(1, 2);
		Assert.assertEquals(0.0, matrix.getCell(0, 0));
		Assert.assertEquals(0.0, matrix.getCell(0, 1));
	}
	
	@Test
	public void shouldSetCell() {
		Matrix matrix = new Matrix(2, 2);
		matrix.setCell(1, 0, 42.0);
		
		Assert.assertEquals(42.0, matrix.getCell(1, 0));
	}
	
	@Test
	public void shouldMultiplySequentially() {
		Matrix matrix1 = new Matrix(1, 2);
		Matrix matrix2 = new Matrix(2, 1);
		matrix1.fillRandomly();
		matrix2.fillRandomly();
		Matrix product = Matrix.sequentialProduct(matrix1, matrix2);
		
		double expectedValue =
				matrix1.getCell(0, 0) * matrix2.getCell(0, 0) +
				matrix1.getCell(0, 1) * matrix2.getCell(1, 0);
		double actualValue = product.getCell(0, 0);
		
		Assert.assertEquals(expectedValue, actualValue);
	}
}

