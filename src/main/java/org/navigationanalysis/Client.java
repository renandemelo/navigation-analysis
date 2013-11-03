package org.navigationanalysis;

import java.net.Socket;

class TCPClient {
	public static void main(String argv[]) throws Exception {
		Navigation navigation = new Navigation();
		Socket clientSocket = new Socket("localhost", 2000);
		new Tracker(Side.CLIENT,clientSocket.getInputStream(),clientSocket.getOutputStream(),navigation).track(clientSocket);
		clientSocket.close();
	}
}