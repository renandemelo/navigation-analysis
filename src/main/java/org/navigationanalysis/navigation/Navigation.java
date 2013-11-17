package org.navigationanalysis.navigation;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.navigationanalysis.Packet;
import org.navigationanalysis.WiresharkHelper;

public class Navigation {

	private ArrayList<Packet> packets = new ArrayList<Packet>();
	
	private HashMap<String,Long> candidates = new HashMap<String, Long>();
	
	public Navigation() throws IOException {
		this(System.getProperty("navigation-file") != null? System.getProperty("navigation-file"): "navigation.pcapng");
	}
	public Navigation(String sourceFile) throws IOException{
		BufferedReader reader = new WiresharkHelper(sourceFile).getCSVReader();	
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

	public long[] getUploadBytesPerInterval(){
		return getBytesPerInterval(true);
	}

	public long[] getDownloadBytesPerInterval(){
		return getBytesPerInterval(false);
	}
	
	public long[] getBytesPerInterval(Boolean fromClient) {
		long[] upload = new long[(int) Math.ceil(getLast().getInterval(20))];		
		for (Packet packet : packets) {
			if(packet.isFromClient() == fromClient){
				int index = (int) packet.getInterval(20);
				upload[index] += packet.getSize();	
			}
		}
		return upload;
	}

	public Packet getLast() {
		Packet last = packets.get(packets.size()-1);
		return last;
	}
	public Date getDate() {
		Packet first = packets.get(0);
		return first.getDate();		
	}
	public boolean isUpload(String id) {
		for (Packet p : packets) {
			if(p.getId().equals(id))
				return p.isFromClient();
		}
		throw new RuntimeException("Unrecognized packet!!!");
	}

}
