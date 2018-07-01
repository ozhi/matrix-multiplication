package com.ozhi.matrixMultiplication;

import static org.junit.Assert.*;

import org.apache.commons.cli.ParseException;
import org.junit.Test;

public class ConfigTest {

	@Test(expected = RuntimeException.class)
	public void eitherInputFileOrDimensionsShouldBeProvided() throws ParseException {
		String[] args = new String[]{"-t", "4"};
		Config config = new Config(args);
	}
	
	@Test(expected = RuntimeException.class)
	public void eitherInputFileOrDimensionsShouldNotBeProvided() throws ParseException {
		String[] args = new String[]{"-i", "4", "-m", "5", "-n", "6", "-k", "7"};
		Config config = new Config(args);
	}
	
	@Test(expected = RuntimeException.class)
	public void allThreeDimensionsShouldBeProvided() throws ParseException {
		String[] args = new String[]{"-m", "5", "-n", "6"};
		Config config = new Config(args);
	}
	
	@Test(expected = Test.None.class)
	public void outputFileCanBeProvided() throws ParseException {
		String[] args = new String[]{"-i", "input.txt", "-o", "output.txt"};
		Config config = new Config(args);
	}
	
	@Test(expected = Test.None.class)
	public void maxThreadsCanBeProvided() throws ParseException {
		String[] args = new String[]{"-i", "input.txt", "-t", "4"};
		Config config = new Config(args);
	}
	
	@Test(expected = Test.None.class)
	public void quietModeCanBeProvided() throws ParseException {
		String[] args = new String[]{"-i", "input.txt", "-q"};
		Config config = new Config(args);
	}
}
