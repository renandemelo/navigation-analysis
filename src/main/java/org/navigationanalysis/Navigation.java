package org.navigationanalysis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class Navigation {

	private ArrayList<Packet> packets = new ArrayList<Packet>();
	
	private HashMap<String,Long> candidates = new HashMap<String, Long>();
	
	public Navigation() throws IOException {
		BufferedReader reader = new WireSharkHelper().getCSVReader();	
		ArrayList<Packet> tempPackets = new ArrayList<Packet>();
		while(true){
			String line = reader.readLine();
			if(line == null) break;
			Packet packet = new Packet(line);
			count(packet.getLocalIP(),packet.getRemoteIP());
			if(packet.getSize() > 0)
				tempPackets.add(packet);
		}		
		boolean passedInitialClientRequest = false;
		String clientIP = findClientIP();
		for (Packet p : tempPackets) {
			p.setFromClient(clientIP);
			if(!passedInitialClientRequest)
				passedInitialClientRequest = p.isFromClient();
			if(passedInitialClientRequest)
				packets.add(p);
		}
		reader.close();
	}
	
	public ArrayList<Packet> getPackets() {
		return packets;
	}

	private void count(String... ips) {
		for (String ip : ips) {
			Long c = candidates.get(ip);
			if(c == null)
				c = 0L;
			c++;
			candidates.put(ip, c);
		}
	}

	private String findClientIP() {
		Set<Entry<String, Long>> entrySet = candidates.entrySet();
		Entry<String,Long> chosen = null;		
		for (Entry<String, Long> entry : entrySet) {
			if(chosen == null || chosen.getValue() < entry.getValue()){
					chosen = entry;
			}
		}
		return chosen.getKey();
	}

	public long[] getUploadBytesPerMinute(){
		return getBytesPerSecond(true);
	}

	public long[] getDownloadBytesPerMinute(){
		return getBytesPerSecond(false);
	}
	
	public long[] getBytesPerSecond(Boolean fromClient) {
		Packet last = packets.get(packets.size()-1);
		long[] upload = new long[(int) Math.ceil(last.getMinute())];		
		for (Packet packet : packets) {
			if(packet.isFromClient() == fromClient){
				int index = (int) packet.getMinute();
				upload[index] += packet.getSize();	
			}
		}
		return upload;
	}

}
