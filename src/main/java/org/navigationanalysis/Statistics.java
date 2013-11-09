package org.navigationanalysis;

import java.io.IOException;

public class Statistics {

	private Navigation navigation;

	public Statistics() {
		try {
			navigation = new Navigation();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void run() throws IOException {		
		long[] downloadPerMinute = navigation.getDownloadBytesPerMinute();
		long[] uploadPerMinute = navigation.getUploadBytesPerMinute();
		for (int i = 0; i < downloadPerMinute.length; i++) {
			System.out.println(downloadPerMinute[i] + " - " + uploadPerMinute[i]);
		}
	}
	
}
