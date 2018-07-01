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
	
	public void log(String format, Object...args) {
		if (!quiet) {
			System.out.println(String.format(format, args));
		}
	}
	
	public void logImportant(String format, Object...args) {
		System.out.println(String.format(format, args));
	}
}
