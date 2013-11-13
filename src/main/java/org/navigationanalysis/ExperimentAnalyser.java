package org.navigationanalysis;

import java.io.File;

public class ExperimentAnalyser {

	private Pairs pairs;
	private Navigation nav;
	
	public ExperimentAnalyser(File experimentDirectory, Navigation nav) {
		pairs = new Pairs(experimentDirectory.listFiles());	
		this.nav = nav;
	}

	public void analyze() {
//		nav.getPackets();
//		for (Pair p : pairs) {
//			
//		}
	}
	
}
