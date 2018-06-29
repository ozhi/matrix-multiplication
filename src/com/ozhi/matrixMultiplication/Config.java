package com.ozhi.matrixMultiplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

class Config {
	public String inputFile;
	public int m, n, k;
	public String outputFile;
	public int tasks;
	public boolean quiet;

	public Matrix matrix1;
	public Matrix matrix2;

	public Config(String[] commandLineArgs) throws ParseException, FileNotFoundException{
		CommandLine cmd = createCommandLine(commandLineArgs);

		loadOptionValues(cmd);

		if (cmd.hasOption("i")) {
			readMatricesFromInputFile();
		} else {
			createMatricesFromDimensions();
		}
	}

	private static Options createCommandLineOptions() {
		Options options = new Options();
		
		// input file name
		options.addOption(Option.builder().argName("i").longOpt("i").hasArg().desc("input file").build());

		options.addOption(Option.builder().argName("m").longOpt("m").type(Integer.class).hasArg().desc("first matrix rows").build());

		options.addOption(Option.builder().argName("n").longOpt("n").type(Integer.class).hasArg().desc("first matrix cols == second matrix rows").build());

		options.addOption(Option.builder().argName("k").longOpt("k").type(Integer.class).hasArg().desc("second matrix cols").build());

		// output file name
		options.addOption(Option.builder().argName("o").longOpt("o").hasArg().desc("output file").build());

		// number of tasks to divide the task between
		options.addOption(Option.builder().argName("t").longOpt("t").type(Integer.class).hasArg().desc("maximum number of threads to use").build());

		// quiet - only output most important messages
		options.addOption(Option.builder().argName("q").longOpt("q").hasArg(false).desc("do not produce unnecessary output").build());

		return options;
	}

	private static CommandLine createCommandLine(String[] args) throws ParseException {
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
		inputFile = cmd.getOptionValue("i");

		m = Integer.parseInt(cmd.getOptionValue("m"));
		n = Integer.parseInt(cmd.getOptionValue("n"));
		k = Integer.parseInt(cmd.getOptionValue("k"));
		
		if (m <= 0 || n <= 0 || k <= 0) {
			throw new RuntimeException("Command args m, n and k should be positive");
		}

		outputFile = cmd.getOptionValue("o");

		tasks = Integer.parseInt(cmd.getOptionValue("t"));

		if (tasks < 1 || tasks > 32) {
			throw new RuntimeException("Command arg t should be in range [1, 32]");
		}
		
		quiet = cmd.hasOption("q");
	}

	private void readMatricesFromInputFile() throws FileNotFoundException {
		Scanner scanner = new Scanner(new File(inputFile));

		int m = scanner.nextInt();
		int n = scanner.nextInt();
		int k = scanner.nextInt();

		matrix1 = new Matrix(m, n);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				matrix1.setCell(i, j, scanner.nextDouble());
			}
		}

		Matrix matrix2 = new Matrix(n, k);
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < k; j++) {
				matrix2.setCell(i, j, scanner.nextDouble());
			}
		}
		
		scanner.close();
	}
	
	private void createMatricesFromDimensions() {
		matrix1 = new Matrix(m, n);
		matrix2 = new Matrix(n, k);

		matrix1.fillRandomly();
		matrix2.fillRandomly();
	}
}
