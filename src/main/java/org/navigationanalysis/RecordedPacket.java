package org.navigationanalysis;

public class RecordedPacket {

	private String id;
	private Long time;

	public RecordedPacket(String line) {
		String[] split = line.split(",");
		this.id = split[0];
		this.time = Long.valueOf(split[1]);
	}
	
	public String getId() {
		return id;
	}

	public Long getTime() {
		return time;
	}

}
