package model;

import java.util.Date;

public class ChatMessage implements Comparable<ChatMessage> {
	
	private final String sender;
	private final String recipient;
	private final String message;
	private final Date timestamp;
	
	public ChatMessage(String sender, String recipient, String message, Date timestamp) {
		this.sender = sender;
		this.recipient = recipient;
		this.message = message;
		this.timestamp = timestamp;
	}
	
	public String getSender() {
		return sender;
	}
	
	public String getRecipient() {
		return recipient;
	}
	
	public String getMessage() {
		return message;
	}
	
	public Date getTimestamp() {
		return timestamp;
	}
	
	public boolean isInvalid(String username1, String username2) {
		return sender == null || recipient == null || message == null || timestamp == null
				|| !sender.equals(username1) && !recipient.equals(username1)
				|| !sender.equals(username2) && !recipient.equals(username2);
	}

	@Override
	public int compareTo(ChatMessage other) {
		return this.timestamp.compareTo(other.timestamp);
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof ChatMessage) {
			ChatMessage msg = (ChatMessage) other;
			return equalsNullable(this.sender, msg.sender)
					&& equalsNullable(this.recipient, msg.recipient)
					&& equalsNullable(this.message, msg.message)
					&& equalsNullable(String.valueOf(this.timestamp), String.valueOf(msg.timestamp));
		} else {
			return false;
		}
	}
	
	private boolean equalsNullable(String prop1, String prop2) {
		if (prop1 == null && prop2 == null) {
			return true;
		} else if (prop1 == null || prop2 == null) {
			return false;
		} else {
			return prop1.equals(prop2);
		}
	}
	
}
