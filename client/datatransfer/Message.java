package datatransfer;

import main.Const;

public class Message {
	
	private final String content;
	private final MessageType type;
	
	public Message(String msg) {
		this.content = msg;
		MessageType type;
		if (msg.startsWith("PM from")) {
			type = MessageType.CHAT_PRIVATE;
		} else if (msg.startsWith("Broadcast from")) {
			type = MessageType.CHAT_BROADCAST;
		} else if (msg.startsWith(Const.OK)) {
			type = MessageType.OUTPUT_OK;
		} else if (msg.startsWith(Const.BAD)) {
			type = MessageType.OUTPUT_BAD;
		} else {
			type = MessageType.UNKNOWN;
		}
		this.type = type;
	}
	
	public String getMessageContent() {
		return content;
	}
	
	public MessageType getMessageType() {
		return type;
	}
	
	public boolean isChatMessage() {
		return type == MessageType.CHAT_BROADCAST || type == MessageType.CHAT_PRIVATE;
	}
	
	public boolean isListMessage() {
		return type == MessageType.OUTPUT_OK
				&& !(content.startsWith("OK There are currently ")
						|| content.startsWith("OK thank you for sending ") 
						|| content.startsWith("OK your message has been sent"));
	}
	
	public boolean isQuitMessage() {
		return type == MessageType.OUTPUT_OK && content.startsWith("OK thank you for sending ");
	}
	
	public boolean isMessageSentMessage() {
		return type == MessageType.OUTPUT_OK && content.startsWith("OK your message has been sent");
	}
	
	public static String[] getOnlineUsers(String msg) {
		try {
			return msg.split(Const.OK, 2)[1].split(", ");
		} catch (ArrayIndexOutOfBoundsException e) {
			String[] empty = {};
			return empty;
		}
	}
	
	public static String[] getMessageSenderAndContent(String msg) {
		try {
			return msg.split("from ", 2)[1].split(": ?", 2);
		} catch (ArrayIndexOutOfBoundsException e) {
			String[] empty = {};
			return empty;
		}
	}
	
}
