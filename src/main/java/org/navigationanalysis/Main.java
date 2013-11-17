package org.navigationanalysis;

import org.navigationanalysis.analyzer.Analyzer;
import org.navigationanalysis.statistics.Statistics;

public class Main {
	
	public static void main(String[] args) throws NumberFormatException, Exception {
		if(args[0].equals("Server")){
			new Server(Integer.valueOf(args[1])).run();
		}else if(args[0].equals("Statistics")){
			new Statistics().run();
		}else if(args[0].equals("Client")){
			new Client(args[1]).run();
		}else{
			new Analyzer().run();
		}
			
	}

}
