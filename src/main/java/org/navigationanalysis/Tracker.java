package org.navigationanalysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Tracker {

	private Side side;
	private InputStream inputStream;
	private OutputStream outputStream;
	private List<Registry> registry = new ArrayList<Registry>();
	private Navigation navigation;

	public Tracker(Side side, InputStream inputStream,
			OutputStream outputStream, Navigation nav) {
		this.side = side;
		this.inputStream = inputStream;
		this.outputStream = outputStream;
		this.navigation = nav;
	}

	public void track(Socket socket) {
		try {
			ArrayList<Packet> packets = navigation.getPackets();
			for (Packet p : packets) {
				boolean needToSend = ((side == Side.CLIENT && p.isFromClient()) || (side == Side.SERVER && !p
						.isFromClient()));
				if (needToSend) {
					System.out.println("Sending packet " + p.getId() + " with "+ p.getSize() + " bytes");
					registry.add(new Registry(p));
					outputStream.write(new byte[p.getSize()]);
					System.out.println(p.getSize() + " bytes sent!");
				} else {
					System.out.println("Receiving packet " + p.getId() + " with "+ p.getSize() + " bytes");
					for (int i = 0; i < p.getSize(); i++) {
						if(inputStream.read() == -1) throw new RuntimeException("End of stream not expected!!");
					}
					registry.add(new Registry(p));
					System.out.println(p.getSize() + " bytes received!");
				}
			}
			System.out.println("Agora vou guardar os tempos!");
			guardarTempos(socket);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void guardarTempos(Socket socket) {

		try {
			new File("data").mkdirs();
			String filename = "data/" + socket.getLocalAddress().getHostAddress() + "-"
					+ socket.getInetAddress().getHostAddress() + "-"
					+ System.currentTimeMillis() + ".txt";
			FileWriter fileWriter = new FileWriter(filename);
			BufferedWriter writer = new BufferedWriter(fileWriter);
			for (Registry r : registry) {
				writer.append(r.getPacket().getId() + "," + r.getTime());
				writer.newLine();
			}
			writer.close();
			fileWriter.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
