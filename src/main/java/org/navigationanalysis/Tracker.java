package org.navigationanalysis;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Tracker {

	private Side side;
	private InputStream inputStream;
	private OutputStream outputStream;
	private Navigation navigation;
	private PacketRecorder packetRecorder;

	public Tracker(Side side, InputStream inputStream,
			OutputStream outputStream, Navigation nav) {
		this.side = side;
		this.inputStream = inputStream;
		this.outputStream = outputStream;
		this.navigation = nav;
		packetRecorder = new PacketRecorder();		
	}

	public void track(Socket socket) {
		try {
			packetRecorder.init(socket);
			ArrayList<Packet> packets = navigation.getPackets();
			for (Packet p : packets) {
				boolean needToSend = ((side == Side.CLIENT && p.isFromClient()) || (side == Side.SERVER && !p
						.isFromClient()));
				if (needToSend) {
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
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
