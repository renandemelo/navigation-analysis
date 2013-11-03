package org.navigationanalysis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class Navigation {
	
	public ArrayList<Packet> getPackets() {
		return packets;
	}

	private ArrayList<Packet> packets = new ArrayList<Packet>();
	
	private HashMap<String,Long> candidates = new HashMap<String, Long>();
	
	public Navigation() throws IOException {	
		String fileName = System.getProperty("navigation-file") != null? System.getProperty("navigation-file"): "navigation.csv";
		BufferedReader reader = new BufferedReader(new FileReader(fileName));	
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

}
