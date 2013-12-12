package org.navigationanalysis.analyzer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.navigationanalysis.navigation.Navigation;

public class Analyzer {

	private String analysisDir;
	private HashMap<String, String> siteNavigations;
	private int[] numClientsArray;

	public Analyzer() {
		analysisDir = System.getProperty("analysis-dir") != null? System.getProperty("analysis-dir"): "experiments";
		String[] numClientsString = System.getProperty("num_clients").split(",");
		String[] navigations = System.getProperty("navigations").split(",");
		
		numClientsArray = new int[numClientsString.length];
		for (int i = 0; i < numClientsString.length; i++) {
			numClientsArray[i] = Integer.valueOf(numClientsString[i]);
		}
		
		siteNavigations = new HashMap<String, String>();
		for (String nav : navigations) {
			String[] navSplit = nav.split(":");
			siteNavigations.put(navSplit[0],navSplit[1]);
		}
		
		///home/renan/Dropbox/usp/disciplinas/Redes/mininet/scripts/1
	}
	
	public void run() {
		try {
			Set<Entry<String, String>> entrySet = siteNavigations.entrySet();
			
			System.out.println("Site\tNumber of Clients\tUpload Delay\tDownload Delay");
			for (Entry<String, String> siteNavigation : entrySet) {
				Navigation nav = new Navigation(siteNavigation.getValue());
				for (int numClients : numClientsArray) {
					String experimentId = siteNavigation.getKey() + "-" + numClients;
					File experimentDirectory = new File(analysisDir + "/" + experimentId);
					ExperimentAnalyser experimentAnalyser = new ExperimentAnalyser(experimentDirectory,nav);
					Long upDelay = experimentAnalyser.getAverageUpDelay();
					Long downDelay = experimentAnalyser.getAverageDownDelay();
					Long navigationTime = experimentAnalyser.getAverageNavigationTime();
					System.out.println(siteNavigation.getKey() + "\t"+ numClients + "\t" + upDelay + "\t" + downDelay);
				}								
			}
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
