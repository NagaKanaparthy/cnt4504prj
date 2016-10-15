//package Naga.networks;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class iterativeServer{
	private static LinkedBlockingQueue<Socket> clientsToServe;
	private static ServerSocket listeningSocket;
	private static final int serverPortNumber = 3515;

	public static void main(String[] args){
/*
listening thread (parent)
				when request came add into handling q
processing thread (child)
				process command from q and respond to approiate client
*/
		try{
			listeningSocket = new ServerSocket(3515);
			while(true){
				Socket newClient = listeningSocket.accept();
				if(newClient != null){
					handleClient(newClient);
				}
			}
		}
		catch(IOException e){
			System.err.println("Failed to bind on port : "+serverPortNumber);
			System.exit(-1);
		}
	}
	public void handleClient(Socket socket){
		PrintWriter out;
		BufferedReader in;
		String inputLine;
		try{
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
			//return message to client and print
			System.out.println("Client connected on port: " + socket.getPort());
			out.println("Connection Accepted");
			//loop
			while(true){
				//print out menu
				out.println(CommandCaller.menu);
				//let client know server is done messaging
				out.println("Select Menu Option");
				//wait for response
				inputLine = in.readLine();
				//respond with correct response
				out.println(CommandCaller.respondData(inputLine));
				//checkes if cmd was the exit Option
				if(Integer.parseInt(inputLine) == 7)
					{ break; }
				//end of response fix
				out.println("---Response-Compelete---");
			}
			socket.close();
			System.out.println("Client on port: " + socket.getPort()+" Exited");
		}
		catch (IOException e) {
			e.printStackTrace();
								return;
		}
	}
}
class ProcessingThread extends HandlingThread{
	private LinkedBlockingQueue<Socket> clientQueue;
	private final boolean DEBUG_MODE = true;
	ProcessingThread(LinkedBlockingQueue<Socket> clientsToServe){
		this.clientQueue = clientsToServe;
				}

				private static void listen(){
					//add newClient
					try{
						Socket newClient = listeningSocket.accept();
						if(clientsToServe.offer(newClient, 1L,TimeUnit.SECONDS) != true){
							System.out.println(":L - Listing");
						}
						System.out.println(":D - Connected");
					}
					catch(Exception e){
															System.err.println("Client Addition ERROR: "+serverPortNumber+e.toString());
											}
				}
	public void run(){
		try{
			while(true){
				//take element from q
				//if(this.DEBUG_MODE)
				//	System.out.println(":O - got Here");
				Socket client = this.clientQueue.take();
				if(client != null){
					handleClient(client);
					if(this.DEBUG_MODE)
						System.out.println(":) -  Client Served");
				}
				else
					Thread.sleep(10);
				System.out.println("loop");
			}
		}
		catch(Exception e){
			System.out.println("Failed to extract item from q\n" + e.toString());
		}
	}

}
class CommandCaller{
	public static final String menu = "1. Host current Date and Time\n2. Host uptime\n3. Host memory use\n4. Host Netstat\n5. Host current users\n6. Host running processes\n7. Quit";
	/*
			Method Name: respondData
			Purpose: This will take input and returns to the data from the
				user option.
			Parameters: takes an int in form of a string. This int will be the option
				from the user.
			Return: string with the data from the user option.
		*/
		public static String respondData(String input){
				int value = Integer.parseInt(input);
				String response = null;
				switch(value){
						case 1:
								response = getCommandResults("date");
								break;
						case 2:
								response = getCommandResults("uptime");
								break;
						case 3:
								response = getCommandResults("free -m");
								break;
						case 4:
								response = getCommandResults("netstat");
								break;
						case 5:
								response = getCommandResults("who");
								break;
						case 6:
								response = getCommandResults("ps aux");
								break;
						case 7:
								response = "Exit";
								break;
						default:
								response = "Not a vaild selection, please choose";
								break;
				}
				return response;
		}
		/*
			Method Name: getCommandResults
			Purpose: This will handle take a command and returns to the data from the
					command ran on commandline.
			Parameters: takes an int in form of a string. This int will be the option
				from the user.
			Return: string with the data from command result.
		*/
		public static String getCommandResults(String cmd){
				String data = "";
				try {
						Process process = Runtime.getRuntime().exec(cmd);
						//attempt to initializing buffers
						BufferedReader stdInput = new BufferedReader(
								new InputStreamReader(process.getInputStream()));
						//read the output from the command
						String s = null;
						while ((s = stdInput.readLine()) != null) {
								data += "\n" + s;
						}
				}
				catch(Exception e){
						data = "error";
				}
				return data;
		}
}
