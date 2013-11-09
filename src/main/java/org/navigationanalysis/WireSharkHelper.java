package org.navigationanalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class WireSharkHelper {

	private String sourceFile;
	private File csv;

	public WireSharkHelper() {
		sourceFile = System.getProperty("navigation-file") != null? System.getProperty("navigation-file"): "navigation.pcapng";		
	}

	private void createCSV() {
		File destiny = new File("data/navigation.csv");
		String command = "tshark -r ${fileName} -R tcp -T fields -e frame.number -e tcp.hdr_len -e tcp.len -e frame.time_relative -e ip.src -e ip.dst -e frame.len -e tcp.srcport -e tcp.dstport -E header=n -E separator=,";
		command = command.replace("${fileName}", sourceFile);
		ProcessBuilder builder = new ProcessBuilder(command.split(" "));
		builder.inheritIO();
		builder.redirectOutput(destiny);
		Process p;
		try {
			p = builder.start();
			p.waitFor();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		csv = destiny;
	}
	
	public BufferedReader getCSVReader() {
		try {
			if(csv == null)
				createCSV();
			return new BufferedReader(new FileReader(csv));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

}
