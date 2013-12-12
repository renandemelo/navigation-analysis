package org.navigationanalysis;

import java.io.File;
import java.util.HashSet;

public class Pairs extends HashSet<Pair>{

	public Pairs(File experimentDirectory) {
		if(!experimentDirectory.exists())
			throw new RuntimeException("Experiment Directory does not exist! " + experimentDirectory);
		for (File side : experimentDirectory.listFiles()) {
			if(side.getAbsolutePath().contains("dump")){
				continue;
			}
			String[] pathSplit = side.getAbsolutePath().split(File.separator);
			String fileDesc = pathSplit[pathSplit.length-1].replace(".txt", "");
			String[] split = fileDesc.split("-");
			String first = split[0];
			String second = split[1];
			Pair pair = new Pair(first,second);
			if(contains(pair)){
				for (Pair p : this) {
					if(p.equals(pair)){
						pair = p;
						break;
					}
				}
			}else{
				add(pair);
			}
			pair.add(side);
		}
	}
}
