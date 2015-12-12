package storage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import model.ChatMessage;

import org.json.JSONArray;
import org.json.JSONObject;

public class FileSaver extends AbstractConverter {
	
	private final String username;

	public FileSaver(String username) {
		super();
		this.username = username;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected JSONObject convertMessagesMap(Object map) {
		Map<String, List<ChatMessage>> messagesMap = (Map<String, List<ChatMessage>>) map;
		JSONObject messagesMapJson = new JSONObject();
		for (String key: messagesMap.keySet()) {
			JSONArray taskJson = (JSONArray) convertMessagesList(messagesMap.get(key), key);
			if (taskJson != null) {
				messagesMapJson.put(key, taskJson);
			}
		}
		return messagesMapJson;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected JSONArray convertMessagesList(Object list, String partner) {
		List<ChatMessage> messagesList = (List<ChatMessage>) list;
		JSONArray messagesListJson = new JSONArray();
		for (ChatMessage msg: messagesList) {
			Object messageJson = convertMessage(msg, partner);
			if (messageJson != null) {
				messagesListJson.put(messageJson);
			}
		}
		if (messagesListJson.length() == 0) {
			return null;
		} else {
			return messagesListJson;
		}
	}

	@Override
	protected Object convertMessage(Object msg, String partner) {
		ChatMessage message = (ChatMessage) msg;
		if (message.isInvalid(username, partner)) {
			return null;
		} else {
			JSONObject messageJson = new JSONObject();
			messageJson.put(KEY_SENDER, message.getSender());
			messageJson.put(KEY_RECIPIENT, message.getRecipient());
			messageJson.put(KEY_MESSAGE, message.getMessage());
			messageJson.put(KEY_TIMESTAMP, message.getTimestamp().getTime());
			return messageJson;
		}
	}
	
	public void save(File destin, Map<String, List<ChatMessage>> messagesMap) {
		try {
			JSONObject messagesMapJson = convertMessagesMap(messagesMap);
			writeToFile(destin, messagesMapJson);
		} catch (IOException e) {
			// fails silently
		}
	}
	
	private void writeToFile(File destin, JSONObject messagesJson) throws IOException {
		FileWriter fw = new FileWriter(destin, false);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(messagesJson.toString(4));
		bw.newLine();
		bw.close();
	}
	
}
