package org.navigationanalysis;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class Analyzer {

	private String analysisDir;
	private HashMap<String, String> siteNavigations;
	private int[] numClientsArray;

	public Analyzer() {
		analysisDir = System.getProperty("analysis-dir") != null? System.getProperty("analysis-dir"): "experiments";
		///home/renan/Dropbox/usp/disciplinas/Redes/mininet/scripts/1
		
		numClientsArray = new int[]{1,5,10,15};		
		siteNavigations = new HashMap<String, String>();
		siteNavigations.put("Facebook", "entrega-aps/eduardo/Facebook/facebook1.pcapng");
		siteNavigations.put("Kernel", "entrega-aps/eduardo/Kernel/kernel3.pcapng");
		siteNavigations.put("Terra", "entrega-aps/daniel/WireShark/Terra250913-0004/Terra250913-0004.pcapng");
		siteNavigations.put("YouTube", "entrega-aps/willian/wireshark/dia25.09/Youtube25.09/Youtube25.09.pcapng");
				
	}
	
	public void run() {
		try {
			Set<Entry<String, String>> entrySet = siteNavigations.entrySet();
			for (Entry<String, String> siteNavigation : entrySet) {
				Navigation nav = new Navigation(siteNavigation.getValue());
				for (int numClients : numClientsArray) {
					String experimentId = siteNavigation.getKey() + "-" + numClients;
					System.out.println(experimentId);
					File experimentDirectory = new File(analysisDir + "/" + experimentId);
					new ExperimentAnalyser(experimentDirectory,nav).analyze();
				}								
			}
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}