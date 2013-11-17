package org.navigationanalysis.statistics;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class Statistics {
	
	private List<String> sourceList = new ArrayList<String>();;

	public Statistics() {
		File baseDir = new File("/home/renan/Dropbox/usp/disciplinas/Redes/mininet/scripts/entrega-aps");
		ArrayDeque<File> deque = new ArrayDeque<File>();
		deque.add(baseDir);
		while (!deque.isEmpty()) {
			File dir = deque.poll();
			File[] files = dir.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					deque.add(file);
				} else {
					String name = file.getAbsolutePath();
					if (name.endsWith("pcapng"))
						sourceList.add(name);
				}
			}
		}
	}
	
	public void run() throws IOException {
		recordStatistics();
	}

	private void recordStatistics() {		
		//System.out.println("Site\tStudent\tNavigation time\tUpload\tUpload per minute\tDownload\tDownload per minute");
		System.out.println("Site\tUp C. of Variation\tUp Pearson\tDown C. of Variation\tDown Pearson");
		for (String sourceFile : sourceList) {
			try {
				StatisticsRecord statisticsRecord = new StatisticsRecord(sourceFile);
				System.out.println(statisticsRecord.basicStatistics());
				
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
		
}
