package org.navigationanalysis;

import java.net.ServerSocket;
import java.net.Socket;

class TCPServer {
	public static void main(String argv[]) throws Exception {
		ServerSocket socket = new ServerSocket(2000);
		Integer totalClients = Integer.valueOf(argv[0]);
		for(int numeroClientes = 0; numeroClientes < totalClients; numeroClientes++){
			Navigation nav = new Navigation();
			Socket connectionSocket = socket.accept();
			new Tracker(Side.SERVER,connectionSocket.getInputStream(),connectionSocket.getOutputStream(),nav).track(connectionSocket);
			connectionSocket.close();
		}
		socket.close();
	}
}
