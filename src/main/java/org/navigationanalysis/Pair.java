package org.navigationanalysis;

public class Pair {
	
	private String ip1;
	private String ip2;

	public Pair(String first, String second) {
		first = ip1;
		second = ip2;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Pair){
			Pair other = (Pair) obj;
			return (ip1.equals(other.ip1) && ip2.equals(ip2)) || (ip1.equals(other.ip2) && ip2.equals(ip1));
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return 1;
	}

}
