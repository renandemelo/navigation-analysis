package org.navigationanalysis;

import java.net.Socket;

public class Client {

	private String ip;

	public Client(String ip) {
		this.ip = ip;
	}
	
	public void run() throws Exception {
		Navigation navigation = new Navigation();
		Socket clientSocket = new Socket(ip, 2000);
		new Tracker(Side.CLIENT,clientSocket.getInputStream(),clientSocket.getOutputStream(),navigation).track(clientSocket);
		clientSocket.close();
	}
}