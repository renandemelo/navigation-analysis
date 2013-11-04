package org.navigationanalysis;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private Integer totalClients;

	public Server(Integer totalClients) {
		this.totalClients = totalClients;
	}
	
	public void run() throws Exception {
		ServerSocket socket = new ServerSocket(2000);
		for(int numeroClientes = 0; numeroClientes < totalClients; numeroClientes++){
			Navigation nav = new Navigation();
			Socket connectionSocket = socket.accept();
			new Tracker(Side.SERVER,connectionSocket.getInputStream(),connectionSocket.getOutputStream(),nav).track(connectionSocket);
			connectionSocket.close();
		}
		socket.close();
	}
}
