package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import logic.Logic;
import model.ChatMessage;
import ui.ClientUI;
import datatransfer.UIAction;
import datatransfer.UIActionType;
import exception.InvalidCommandException;

public class Client {
	
	private ClientUI ui;
	private BufferedReader in;
	private PrintWriter out;
	
	private Logic logic;
	
	public void attemptToLogin(String username) {
		String response;
		if (username.isEmpty()) {
			response = Const.BAD_USERNAME_EMPTY;
		} else if (isUsernameInWrongFormat(username)) {
			response = Const.BAD_USERNAME_INVALID_CHAR;
		} else {
			out.println(String.format(Const.COMMAND_IDEN_FORMAT, username));
			try {
				response = in.readLine();
				if (response.startsWith(Const.BAD)) {
					ui.followUpAction(new UIAction(UIActionType.PRINT_BAD_USERNAME_RESPONSE, response));
				} else {
					continueToMainActivity(username);
				}
			} catch (IOException e) {
				ui.followUpAction(new UIAction(UIActionType.DISPLAY_ERROR_SCREEN, ""));
			}
		}
	}
	
	private boolean isUsernameInWrongFormat(String username) {
		return username.contains(",") || username.contains(":") || username.contains(" ");
	}
	
	private void continueToMainActivity(String username) {

		ui.followUpAction(new UIAction(UIActionType.DISPLAY_MAIN_SCREEN, username));
		
		logic = new Logic(out, in, username);
		
		sendCommand(Const.COMMAND_LIST);

		Timer timer = new Timer();
		TimerTask getMessage = new TimerTask() {
			@Override
			public void run() {
				try {
					// schedule a message receiver every half a second
					UIAction action = logic.getActionNeeded();
					ui.followUpAction(action);
				} catch (IOException e) {
					ui.followUpAction(new UIAction(UIActionType.DISPLAY_ERROR_SCREEN, ""));
				}
			}
		};
		timer.schedule(getMessage, 500, 500);

	}

	public void sendCommand(String cmd) {
		try {
			logic.parseAndSendCommand(cmd);
		} catch (InvalidCommandException ice) {
			// this should not happen because user cannot enter command words by him/herself
			ui.followUpAction(new UIAction(UIActionType.DISPLAY_BAD_MESSAGE, Const.BAD_INVALID_COMMAND));
		}
	}
	
	public List<ChatMessage> getMessagesToAndFrom(String username) {
		return logic.getMessagesToAndFrom(username);
	}
	
	public Client() throws UnknownHostException, IOException {
		
		try {
			
			@SuppressWarnings("resource")
			Socket clientSocket = new Socket(Const.HOST_NAME, Const.PORT_NUMBER);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			
			String welcomeMsg = in.readLine();
			welcomeMsg = welcomeMsg.split(Const.OK)[1];
			ui = new ClientUI(this);
			ui.followUpAction(new UIAction(UIActionType.DISPLAY_LOGIN_SCREEN, welcomeMsg));
			
		} catch (ConnectException ce) {
			
			ui = new ClientUI(this);
			ui.followUpAction(new UIAction(UIActionType.DISPLAY_ERROR_SCREEN, ""));
			
		}
		
	}
	
}
