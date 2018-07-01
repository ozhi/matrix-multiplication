package com.ozhi.matrixMultiplication;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

class Config {
	private String inputFile;
	private Integer matrix1Rows, matricesCommonDimension, matrix2Cols;
	private String outputFile;
	private int maxThreads;
	private boolean quiet;
	
	private final static int DEFAULT_MAX_THREADS = 1;

	public Config(String[] commandLineArgs) throws ParseException {
		CommandLine cmd = parseCommandLineArgs(commandLineArgs);
		loadOptionValues(cmd);
	}

	public String getInputFile() {
		return inputFile;
	}
	
	public Integer getMatrix1Rows() {
		return matrix1Rows;
	}
	
	public Integer getMatricesCommonDimension() {
		return matricesCommonDimension;
	}

	public Integer getMatrix2Cols() {
		return matrix2Cols;
	}

	public int getMaxThreads() {
		return maxThreads;
	}
	
	public String getOutputFile() {
		return outputFile;
	}
	
	public boolean isQuietMode() {
		return quiet;
	}	
	
	private static Options createCommandLineOptions() {
		Options options = new Options();
		
		options.addOption(Option.builder().argName("i").longOpt("i").hasArg().desc("input file name").build());

		options.addOption(Option.builder().argName("m").longOpt("m").type(Integer.class).hasArg().desc("first matrix rows").build());

		options.addOption(Option.builder().argName("n").longOpt("n").type(Integer.class).hasArg().desc("first matrix cols == second matrix rows").build());

		options.addOption(Option.builder().argName("k").longOpt("k").type(Integer.class).hasArg().desc("second matrix cols").build());

		options.addOption(Option.builder().argName("o").longOpt("o").hasArg().desc("output file name").build());

		options.addOption(Option.builder().argName("t").longOpt("t").type(Integer.class).hasArg().desc("maximum number of threads to use").build());

		options.addOption(Option.builder().argName("q").longOpt("q").hasArg(false).desc("only output time of multiplication").build());

		return options;
	}
	

	private static CommandLine parseCommandLineArgs(String[] args) throws ParseException {
		Options options = createCommandLineOptions();
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse(options, args);

		String optionsErrorMessage = "Either -i or (-m, -n and -k) options should be provided";
		if (cmd.hasOption("i")) {
			if (cmd.hasOption("m") || cmd.hasOption("n") || cmd.hasOption("k")) {
				throw new RuntimeException(optionsErrorMessage);
			}
		} else {
			if (!cmd.hasOption("m") || !cmd.hasOption("n") || !cmd.hasOption("k")) {
				throw new RuntimeException(optionsErrorMessage);
			}
		}

		return cmd;
	}
	

	private void loadOptionValues(CommandLine cmd) {
		if (cmd.hasOption("i")) {
			inputFile = cmd.getOptionValue("i");
		} else {
			matrix1Rows = Integer.parseInt(cmd.getOptionValue("m"));
			matricesCommonDimension = Integer.parseInt(cmd.getOptionValue("n"));
			matrix2Cols = Integer.parseInt(cmd.getOptionValue("k"));
			
			if (matrix1Rows <= 0 || matricesCommonDimension <= 0 || matrix2Cols <= 0) {
				throw new RuntimeException("Command args m, n and k should be positive");
			}
		}

		if (cmd.hasOption("o")) {
			outputFile = cmd.getOptionValue("o");
		}
		
		maxThreads = cmd.hasOption("t") ? Integer.parseInt(cmd.getOptionValue("t")) : DEFAULT_MAX_THREADS;

		if (maxThreads < 1 || maxThreads > 32) {
			throw new RuntimeException("Max threads should be in range [1, 32]");
		}
		
		quiet = cmd.hasOption("q");
	}
}
