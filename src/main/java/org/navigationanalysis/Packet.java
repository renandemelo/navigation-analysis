package org.navigationanalysis;

public class Packet {

	String id;
	Boolean fromClient;
	Integer size;
	private String localIP;
	private String remoteIP;
	
	public Packet(String line) {
		String[] split = line.split(",");
		id = split[0];
		localIP = split[4];
		remoteIP = split[5];
		size = Integer.valueOf(split[2]);
	}	

	public void setFromClient(String clientIP){
		fromClient = clientIP.equals(localIP);
		clientIP = null;
		remoteIP = null;
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

	
}
