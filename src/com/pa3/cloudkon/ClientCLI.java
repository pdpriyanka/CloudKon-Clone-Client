package com.pa3.cloudkon;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class ClientCLI {
	private String[] args;
	private static final Logger clientCLIlog = Logger.getLogger(ClientCLI.class.getName());
	private Options options;
	private boolean localWorkers = false;

	public ClientCLI() {
		super();
	}

	/**
	 * In the constructor command line options are created.
	 * 
	 * @param args
	 */
	public ClientCLI(String[] args) {
		options = new Options();
		this.args = args;
		
		options.addOption("s", "localRemote", true,
				"specify local or remote workers. If case of remote, it is queue name and in case of local,it is LOCAL");

		// condition check for local workers
		if (args != null && args.length > 1 && Constants.LOCAL.equalsIgnoreCase(args[2])) {
			localWorkers = true;
			options.addOption("t", "numberofWorkers", true, "Specify the number of workers");
		} else {
			localWorkers = false;
		}

		options.addOption("w", "workloadfile", true, "Specify the workload file");
	}

	/**
	 * This method will parse the command line options and validate it
	 */
	public void parse() {
		CommandLine commandLine = null;
		boolean missing = false;
		try {
			CommandLineParser parser = new DefaultParser();
			commandLine = parser.parse(options, args);

			// validate command line options
			if (commandLine.hasOption("s")) {
				clientCLIlog.log(Level.INFO, "Command line argument -s=" + commandLine.getOptionValue("s"));
			} else {
				clientCLIlog.log(Level.SEVERE, "missing s option from command line");
				missing = true;
			}

			if (localWorkers) {
				if (commandLine.hasOption("t")) {
					clientCLIlog.log(Level.INFO, "Command line argument -t=" + commandLine.getOptionValue("t"));
				} else {
					clientCLIlog.log(Level.SEVERE, "missing t option from command line");
					missing = true;
				}

			}
			if (commandLine.hasOption("w")) {
				clientCLIlog.log(Level.INFO, "Command line argument -w=" + commandLine.getOptionValue("w"));
			} else {
				clientCLIlog.log(Level.SEVERE, "missing w option from command line");
				missing = true;
			}

			// if any option is missing then show help

			if (missing) {
				help();
			}

		} catch (ParseException e) {
			System.out.println("Invalid command line option: " + e.getMessage());
		}
	}

	/**
	 * This method will show the help for command line
	 */
	public void help() {
		HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.printHelp("Client", options);
		System.exit(0);
	}

}
