package org.navigationanalysis;

import java.net.Socket;

import org.navigationanalysis.navigation.Navigation;

public class Client {

	private String ip;

	public Client(String ip) {
		this.ip = ip;
	}
	
	public void run() throws Exception {
		System.out.println("Parsing navigation...");
		Navigation navigation = new Navigation();
		System.out.println("Connecting to server...");
		Socket clientSocket = new Socket(ip, 2000);
		double navigationTimeInSeconds = navigation.getLast().getInterval(1);
		double waitingTime = (Math.random() * navigationTimeInSeconds * 0.3); //Random until 10% of navigation time
		System.out.println("Waiting ..." + waitingTime);
		Thread.sleep((long)(waitingTime) * 1000);
		System.out.println("Tracking navigation...");
		new Tracker(Side.CLIENT,clientSocket.getInputStream(),clientSocket.getOutputStream(),navigation).track(clientSocket);
	}
}