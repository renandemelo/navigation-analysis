package org.navigationanalysis;

public class Main {
	
	public static void main(String[] args) throws NumberFormatException, Exception {
		String param = args[1];
		if(args[0].equals("Server")){
			new Server(Integer.valueOf(param)).run();
		}else{
			new Client(param).run();
		}
			
	}

}
