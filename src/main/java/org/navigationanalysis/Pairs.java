package org.navigationanalysis;

import java.io.File;
import java.util.ArrayList;

public class Pairs extends ArrayList<Pair>{

	public Pairs(File[] sides) {
		for (File side : sides) {
			String[] pathSplit = side.getAbsolutePath().split(File.separator);
			String fileDesc = pathSplit[pathSplit.length-1].split("\\.")[1];
			String[] split = fileDesc.split("-");
			String first = split[0];
			String second = split[1];
			add(new Pair(first,second));			
		}
	}

}
