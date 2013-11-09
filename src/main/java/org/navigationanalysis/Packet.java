package org.navigationanalysis;

import java.math.BigDecimal;

public class Packet {

	String id;
	Boolean fromClient;
	Integer size;
	private String localIP;
	private String remoteIP;
	private Long time;
	
	public Packet(String line) {
		String[] split = line.split(",");
		id = split[0];
		localIP = split[4];
		remoteIP = split[5];
		size = Integer.valueOf(split[2]);
		time = new BigDecimal(split[3]).multiply(new BigDecimal(1000)).longValue();
	}	

	public void setFromClient(String clientIP){
		fromClient = clientIP.equals(localIP);
		clientIP = null;
		remoteIP = null;
	}
	public Long getTime() {
		return time;
	}

	public String getId() {
		return id;
	}

	public String getLocalIP() {
		return localIP;
	}

	public String getRemoteIP() {
		return remoteIP;
	}

	public Boolean isFromClient() {
		return fromClient;
	}


	public Integer getSize() {
		return size;
	}

	public double getMinute() {
		return getSecond()/60;
	}

	private double getSecond() {
		return time/1000.0;
	}

	
}
