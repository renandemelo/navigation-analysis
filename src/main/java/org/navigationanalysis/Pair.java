package org.navigationanalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class Pair {
	
	private String ip1;
	private String ip2;

	private ArrayList<File> sides = new ArrayList<File>();
	
	public Pair(String first, String second) {
		ip1 = first;
		ip2 = second;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Pair){
			Pair other = (Pair) obj;
			return (ip1.equals(other.ip1) && ip2.equals(other.ip2)) || (ip1.equals(other.ip2) && ip2.equals(other.ip1));
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return 1;
	}

	public BufferedReader[] getFileReaders() throws FileNotFoundException {
		BufferedReader[] bufferedReaders = new BufferedReader[2];
		bufferedReaders[0] = new BufferedReader(new FileReader(sides.get(0)));
		bufferedReaders[1] = new BufferedReader(new FileReader(sides.get(1)));
		return bufferedReaders;
	}

	public void add(File side) {
		sides.add(side);
	}

}
