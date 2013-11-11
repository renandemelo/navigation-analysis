package org.navigationanalysis;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Tracker implements Runnable{

	private Side mySide;
	private InputStream inputStream;
	private OutputStream outputStream;
	private Navigation navigation;
	private PacketRecorder packetRecorder;
	private Long initial;
	private boolean realTime;
	private Socket socket;

	public Tracker(Side side, InputStream inputStream,
			OutputStream outputStream, Navigation nav) {
		this.mySide = side;
		this.inputStream = inputStream;
		this.outputStream = outputStream;
		this.navigation = nav;
		packetRecorder = new PacketRecorder();	
		realTime = System.getProperty("realTime") != null?Boolean.getBoolean(System.getProperty("realTime")): true;
	}

	
	
	public void track(Socket socket) {
		this.socket = socket;
		new Thread(this).start();
	}

	private boolean iAm(Side side) {
		return mySide == side;
	}



	public void run() {
		initial = System.currentTimeMillis();
		try {
			packetRecorder.init(socket);
			ArrayList<Packet> packets = navigation.getPackets();
			for (Packet p : packets) {
				boolean needToSend = ((iAm(Side.CLIENT) && p.isFromClient()) || (iAm(Side.SERVER) && !p.isFromClient()));
				if (needToSend) {
					Long elapsedTime = System.currentTimeMillis() - initial;
					Long timeDiff = p.getTime() - elapsedTime;
					if(realTime &&  timeDiff > 0){
						try {Thread.sleep(timeDiff);} catch (InterruptedException e) {}
					}
					System.out.println("Sending packet " + p.getId() + " with "+ p.getSize() + " bytes");
					packetRecorder.record(p);
					outputStream.write(new byte[p.getSize()]);
					System.out.println(p.getSize() + " bytes sent!");
				} else {
					System.out.println("Receiving packet " + p.getId() + " with "+ p.getSize() + " bytes");
					for (int i = 0; i < p.getSize(); i++) {
						if(inputStream.read() == -1) throw new RuntimeException("End of stream not expected!!");
					}
					packetRecorder.record(p);
					System.out.println(p.getSize() + " bytes received!");
				}
			}
			packetRecorder.finish();
			socket.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}

}
