package org.navigationanalysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

public class PacketRecorder{

	private Queue<Registry> registries = new ArrayDeque<Registry>();
	private BufferedWriter writer;
	private FileWriter fileWriter;
	private boolean stop;
	private Timer timer;

	public void init(Socket socket) {
		try {
			new File("data").mkdirs();
			String filename = "data/"
					+ socket.getLocalAddress().getHostAddress() + "-"
					+ socket.getInetAddress().getHostAddress() + "-"
					+ System.currentTimeMillis() + ".txt";
			fileWriter = new FileWriter(filename);
			writer = new BufferedWriter(fileWriter);
			timer = new Timer();
			timer.schedule(new TimerTask() {				
				@Override
				public void run() {
					recordToFile();
				}
			}, new Date(), 5000L);
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public synchronized void record(Packet p) {
		registries.offer(new Registry(p));
	}
	
	private synchronized void recordToFile() {
		try {
			while(registries.peek() != null){
				Registry r = registries.poll();
				writer.append(r.getPacket().getId() + "," + r.getTime());
				writer.newLine();
			}
			if(stop){
				timer.cancel();
				close();
			}			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void close() throws IOException{
		writer.close();
		fileWriter.close();
	}

	public synchronized void finish() {
		stop = true;		
	}
}
