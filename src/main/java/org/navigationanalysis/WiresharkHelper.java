package org.navigationanalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class WiresharkHelper {

	private String sourceFile;
	private File csv;

	public WiresharkHelper(String sourceFile) {
		if(!new File(sourceFile).exists())
			throw new RuntimeException("Source file (pngcap) not found!");
		this.sourceFile = sourceFile;		
	}

	private void createCSV() {
		String tmpdir = System.getProperty("java.io.tmpdir");
		File destiny = new File(tmpdir + "/navigation-" + System.currentTimeMillis() + ".csv");
		String command = "tshark -r ${fileName} -R tcp -T fields -e frame.number -e tcp.hdr_len -e tcp.len -e frame.time_relative -e ip.src -e ip.dst -e frame.len -e tcp.srcport -e tcp.dstport -e frame.time -E header=n -E separator=,";
		String[] split = command.split(" ");
		split[2] = split[2].replace("${fileName}", sourceFile);
		ProcessBuilder builder = new ProcessBuilder(split);
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
