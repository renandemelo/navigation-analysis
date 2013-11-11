package org.navigationanalysis;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

public class Packet {

	String id;
	Boolean fromClient;
	Integer size;
	private String localIP;
	private String remoteIP;
	private Long time;
	private Date date;
	
	public Packet(String line) {
		String[] split = line.split(",");
		try {
			id = split[0];
			localIP = split[4];
			remoteIP = split[5];
			size = Integer.valueOf(split[2].trim().equals("")? "0":split[2]);
			time = new BigDecimal(split[3]).multiply(new BigDecimal(1000)).longValue();	
			date = new java.text.SimpleDateFormat("MMM dd yyyy HH:mm:ss").parse((split[split.length-2] + split[split.length-1]).split("\\.")[0]);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
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

	public double getInterval(int seconds) {
		return getSecond()/seconds;
	}

	private double getSecond() {
		return time/1000.0;
	}

	public Date getDate() {
		return date;
	}

	
}
