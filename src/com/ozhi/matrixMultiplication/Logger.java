package com.ozhi.matrixMultiplication;

public class Logger {
	private boolean quiet;
	private final static boolean DEFAULT_QUIET = false;
	
	public Logger() {
		quiet = DEFAULT_QUIET;
	}
	
	public Logger(boolean quiet) {
		this.quiet = quiet;
	}
	
	public void log(String message) {
		if (!quiet) {
			System.out.println(message);
		}
	}
	
	public void importantLog(String message) {
		System.out.println(message);
	}
}
