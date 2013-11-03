package org.navigationanalysis;

public class Registry {
	
	private long time;
	private Packet packet;

	public Registry(Packet packet) {
		this.time = System.currentTimeMillis();
		this.packet = packet;
	}

	public long getTime() {
		return time;
	}

	public Packet getPacket() {
		return packet;
	}
}
