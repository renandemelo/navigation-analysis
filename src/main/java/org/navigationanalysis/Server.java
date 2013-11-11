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
		System.out.println("Parsing navigation...");
		Navigation nav = new Navigation();
		for(int numeroClientes = 0; numeroClientes < totalClients; numeroClientes++){
			System.out.println("Accepting new connection...");
			Socket connectionSocket = socket.accept();
			new Tracker(Side.SERVER,connectionSocket.getInputStream(),connectionSocket.getOutputStream(),nav).track(connectionSocket);
		}
		socket.close();
	}
}
