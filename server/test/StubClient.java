package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class StubClient implements Runnable {

	private final String serverAddress;
	private final int serverPort;
	private final Command[] commands;
	private final String clientName;
	private final List<String> outputMsgs;
	private final List<String> chatMsgs;
	
	private BufferedReader in;
	private PrintWriter out;
	private Socket clientSocket;

	public StubClient(int serverPort, String serverAddress, Command[] commands, String clientName) {
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
		this.commands = commands;
		this.clientName = clientName;
		this.outputMsgs = new ArrayList<String>();
		this.chatMsgs = new ArrayList<String>();
	}

	public void run() {

		initialize();
		
		printWelcomeMessage();

		for (Command command : commands) {
			executeCommand(command);
		}

		try {
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void initialize() {
		try {
			clientSocket = new Socket(this.serverAddress, this.serverPort);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			out = new PrintWriter(clientSocket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void printWelcomeMessage() {
		try {
			System.out.println(this.clientName + ": " + in.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void executeCommand(Command command) {
		if (command instanceof Message) {
			out.println(((Message) command).getCommand());
			try {
				String line;
				do {
					line = in.readLine();
					System.out.println(this.clientName + ": " + line);
					if (!line.isEmpty()) {
						if (isChatMessage(line)) {
							chatMsgs.add(line);
						} else {
							outputMsgs.add(line);
						}
					}
				} while (!isOutputMessage(line));
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else if (command instanceof Wait) {
			try {
				Thread.sleep(((Wait) command).getDur());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}				
		}
	}

	private boolean isChatMessage(String msg) {
		return msg.startsWith("PM from") || msg.startsWith("Broadcast from ");
	}
	
	private boolean isOutputMessage(String msg) {
		return !msg.startsWith("PM") && !msg.isEmpty() 
				&& !(msg.startsWith("Broadcast from ") && !msg.startsWith("Broadcast from " + clientName));
	}
	
	public List<String> getOutputMsgs() {
		return outputMsgs;
	}
	
	public List<String> getChatMsgs() {
		return chatMsgs;
	}
	
}