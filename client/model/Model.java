package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Model {

	private List<String> usersList = new ArrayList<String>();
	private final String username;
	private final Map<String, List<ChatMessage>> partnerToMessagesMap;
	
	public Model(String username) {
		this(username, new HashMap<String, List<ChatMessage>>());
	}
	
	public Model(String username, Map<String, List<ChatMessage>> partnerToMessagesMap) {
		this.username = username;
		this.partnerToMessagesMap = partnerToMessagesMap;
	}
	
	public List<String> getUsersList() {
		return usersList;
	}

	public Map<String, List<ChatMessage>> getPartnerToMessagesMap() {
		return partnerToMessagesMap;
	}

	public List<ChatMessage> getMessagesToAndFrom(String partner) {
		if (partner == null) {
			return null;
		} else {
			return partnerToMessagesMap.get(partner);
		}
	}
	
	public void addMessageTo(String recipient, String message, Date date) {
		if (recipient != null && message != null && usersList.contains(recipient)) {
			if (!partnerToMessagesMap.containsKey(recipient)) {
				partnerToMessagesMap.put(recipient, new ArrayList<ChatMessage>());
			}
			partnerToMessagesMap.get(recipient).add(
					new ChatMessage(username, recipient, message, date == null ? new Date() : date));
		}
	}

	public void addMessageFrom(String sender, String message, Date date) {
		if (sender != null && message != null) {
			if (!partnerToMessagesMap.containsKey(sender)) {
				partnerToMessagesMap.put(sender, new ArrayList<ChatMessage>());
			}
			partnerToMessagesMap.get(sender).add(
					new ChatMessage(sender, username, message, date == null ? new Date() : date));
		}
	}
	
	public void updateUserList(String... splitMsg) {
		usersList = Arrays.asList(splitMsg);
	}
	
}
