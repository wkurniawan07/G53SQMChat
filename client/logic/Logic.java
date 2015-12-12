package logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;

import model.ChatMessage;
import model.Model;
import storage.FileLoader;
import storage.FileSaver;
import datatransfer.Command;
import datatransfer.CommandType;
import datatransfer.Message;
import datatransfer.UIAction;
import exception.InvalidCommandException;

public final class Logic {
	
	private final PrintWriter out;
	private final BufferedReader in;
	private final File savedFile;
	private final FileSaver saver;
	private final Model model;
	private final String username;
	
	public Logic(PrintWriter out, BufferedReader in, String username) {
		this.out = out;
		this.in = in;
		this.savedFile = new File("chatclient-" + username + ".json");
		FileLoader loader = new FileLoader(username);
		Map<String, List<ChatMessage>> partnerToMessagesMap = loader.load(savedFile);
		this.saver = new FileSaver(username);
		this.model = new Model(username, partnerToMessagesMap);
		this.username = username;
	}
	
	public void parseAndSendCommand(String cmd) throws InvalidCommandException {
		Command parsedCmd = parseAndValidateCommand(cmd);
		out.println(parsedCmd.getCommandFullContent());
		if (parsedCmd.getCommandType() == CommandType.MESG || parsedCmd.getCommandType() == CommandType.HAIL) {
			Date now = new Date();
			if (parsedCmd.getCommandType() == CommandType.MESG) {
				String[] splitCmd = parsedCmd.getCommandContent().split(" ", 2);
				model.addMessageTo(splitCmd[0], splitCmd[1], now);
			} else {
				List<String> currentOnlineUsers = model.getUsersList();
				for (String user: currentOnlineUsers) {
					if (!user.equals(username)) {
						model.addMessageTo(user, parsedCmd.getCommandContent(), now);
					}
				}
			}
			saver.save(savedFile, model.getPartnerToMessagesMap());
		}
	}
	
	Command parseAndValidateCommand(String cmd) throws InvalidCommandException {
		Command parsedCmd = new Command(cmd);
		String cmdContent = parsedCmd.getCommandContent();
		switch (parsedCmd.getCommandType()) {
			case IDEN:
				validateIdenCommandFormat(cmdContent);
				break;
			case HAIL:
				validateHailCommandFormat(cmdContent);
				break;
			case MESG:
				validateMesgCommandFormat(cmdContent);
				break;
			case UNKNOWN:
				throw new InvalidCommandException();
			default:
				break;
		}
		return parsedCmd;
	}

	private void validateIdenCommandFormat(String cmdContent) throws InvalidCommandException {
		if (cmdContent.isEmpty()) {
			throw new InvalidCommandException();
		}
	}
	
	private void validateHailCommandFormat(String cmdContent) throws InvalidCommandException {
		if (cmdContent.isEmpty()) {
			throw new InvalidCommandException();
		}
	}
	
	private void validateMesgCommandFormat(String cmdContent) throws InvalidCommandException {
		if (!cmdContent.contains(" ")) {
			throw new InvalidCommandException();
		}
	}
	
	public Message getMessage() throws IOException {
		return getMessage(in.readLine(), savedFile);
	}
	
	Message getMessage(String msg, File savedFile) {
		if (msg == null || msg.isEmpty()) {
			return null;
		} else {
			Message message = new Message(msg);
			String[] splitMsg;
			if (message.isChatMessage()) {
				splitMsg = Message.getMessageSenderAndContent(msg);
				model.addMessageFrom(splitMsg[0], splitMsg[1], new Date());
				saver.save(savedFile, model.getPartnerToMessagesMap());
			} else if (message.isListMessage()) {
				splitMsg = Message.getOnlineUsers(msg);
				model.updateUserList(splitMsg);
			}
			return message;
		}
	}
	
	public UIAction getActionNeeded() throws IOException {
		Message message = getMessage();
		return UIAction.getActionForMessage(message, username);
	}
	
	List<String> getUsersList() {
		return model.getUsersList();
	}
	
	public List<ChatMessage> getMessagesToAndFrom(String username) {
		return model.getMessagesToAndFrom(username);
	}
	
}
