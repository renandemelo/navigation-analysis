package org.navigationanalysis.analyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.navigationanalysis.Pair;
import org.navigationanalysis.Pairs;
import org.navigationanalysis.RecordedPacket;
import org.navigationanalysis.navigation.Navigation;

public class ExperimentAnalyser {

	private Pairs pairs;
	private Navigation nav;
	
	public ExperimentAnalyser(File experimentDirectory, Navigation nav) {
		pairs = new Pairs(experimentDirectory);	
		this.nav = nav;
	}

	public Long getAverageUpDelay() throws IOException {
		return getAvgDelay(true);
	}

	private Long getAvgDelay(boolean upload) throws FileNotFoundException, IOException {
		long delayPerPair = 0L;
		for (Pair p : pairs) {
			BufferedReader[] fileReaders = p.getFileReaders();
			long delayPerPacket = 0;
			long packetLength = 0;
			while(true){
				String line1 = fileReaders[0].readLine();
				String line2 = fileReaders[1].readLine();
				if(line1 == null || line2 == null)
					break;
				RecordedPacket p1 = new RecordedPacket(line1);
				RecordedPacket p2 = new RecordedPacket(line2);
				long diff = Math.abs(p1.getTime() - p2.getTime());
				if(nav.isUpload(p1.getId()) == upload){
					delayPerPacket += diff;
					packetLength++;
				}else{
					continue;
				}
			}
			delayPerPair+= delayPerPacket/packetLength;		
			fileReaders[0].close();
			fileReaders[1].close();
		}
		long avgDelay = delayPerPair/pairs.size();
		return avgDelay;
	}
	
	public Long getAverageDownDelay() throws IOException {
		return getAvgDelay(false);
	}

	public Long getAverageNavigationTime() throws IOException {
		long navigationTimePerPair = 0L;
		for (Pair p : pairs) {
			BufferedReader[] fileReaders = p.getFileReaders();
			long initialTime = 0;
			long lastTime = 0;
			while(true){
				String line1 = fileReaders[0].readLine();
				if(line1 == null)
					break;
				RecordedPacket p1 = new RecordedPacket(line1);
				if(initialTime == 0)
					initialTime = p1.getTime();
				lastTime = p1.getTime();
			}
			navigationTimePerPair+= lastTime - initialTime;
			fileReaders[0].close();
			fileReaders[1].close();
		}
		long avgNavigationTime = navigationTimePerPair/pairs.size();
		return avgNavigationTime;
	}
	
}
