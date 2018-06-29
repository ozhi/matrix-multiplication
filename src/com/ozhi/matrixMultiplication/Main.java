package com.ozhi.matrixMultiplication;

import java.io.FileNotFoundException;
import org.apache.commons.cli.ParseException;

public class Main {

	public static void main(String[] args) throws ParseException, FileNotFoundException {
		Config config = new Config(args);

//		System.out.println("Matrix1:\n" + config.matrix1 + "\n");
//		System.out.println("Matrix1:\n" + config.matrix2 + "\n");
		
		System.out.println("Non-concurrent product:\n");
		Matrix nonConcurrentProduct = config.matrix1.times(config.matrix2);
//		System.out.println(nonConcurrentProduct);
		
		System.out.println("Concurrent product:\n");
		Matrix concurrentProduct = Matrix.concurrentProduct(config.matrix1, config.matrix2, config.tasks);
//		System.out.println(concurrentProduct);
		
		System.out.println("Equal results: " + nonConcurrentProduct.isSameAs(concurrentProduct));
	}
}
