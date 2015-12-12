package storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import model.ChatMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FileLoader extends AbstractConverter {

	private final String username;

	public FileLoader(String username) {
		super();
		this.username = username;
	}
	
	@Override
	public Map<String, List<ChatMessage>> convertMessagesMap(Object map) {
		JSONObject messagesMapJson = (JSONObject) map;
		Map<String, List<ChatMessage>> messagesMap = new HashMap<String, List<ChatMessage>>();
		Iterator<String> keys = messagesMapJson.keys();
		while (keys.hasNext()) {
			String key = keys.next();
			List<ChatMessage> messagesList = convertMessagesList(messagesMapJson.get(key), key);
			if (!messagesList.isEmpty()) {
				messagesMap.put(key, messagesList);
			}
		}
		return messagesMap;
	}
	
	@Override
	public List<ChatMessage> convertMessagesList(Object list, String partner) {
		JSONArray messagesListJson = (JSONArray) list;
		List<ChatMessage> messagesList = new ArrayList<ChatMessage>();
		for (int i = 0; i < messagesListJson.length(); i++) {
			ChatMessage msg = convertMessage(messagesListJson.get(i), partner);
			if (msg != null) {
				messagesList.add(msg);
			}
		}
		Collections.sort(messagesList);
		return messagesList;
	}
	
	@Override
	public ChatMessage convertMessage(Object msg, String partner) {
		JSONObject messageJson = (JSONObject) msg;
		String sender = getString(messageJson, KEY_SENDER);
		String recipient = getString(messageJson, KEY_RECIPIENT);
		String messageContent = getString(messageJson, KEY_MESSAGE);
		Date date = null;
		try {
			date = new Date(messageJson.getLong(KEY_TIMESTAMP));
		} catch (JSONException e) {
			// let the date be null
		}
		ChatMessage message = new ChatMessage(sender, recipient, messageContent, date);
		return message.isInvalid(username, partner) ? null : message;
	}
	
	private String getString(JSONObject obj, String key) {
		try {
			return obj.getString(key);
		} catch (JSONException e) {
			return null;
		}
	}
	
	public Map<String, List<ChatMessage>> load(File file) {
		try {
			JSONObject messagesJson = readFromFile(file);
			return convertMessagesMap(messagesJson);
		} catch (IOException e) {
			return new HashMap<String, List<ChatMessage>>();
		}
	}
	
	private JSONObject readFromFile(File file) throws IOException {
		String str = readFileContent(file.getPath());
		try {
			return new JSONObject(str);
		} catch (JSONException e) {
			return new JSONObject("{}");
		}
	}
	
	static String readFileContent(String fileName) throws IOException {
        Scanner scanner = new Scanner(new BufferedReader(new FileReader(fileName)));
        String str = scanner.useDelimiter("\\Z").next();
        scanner.close();
        return str.replace("\r", "");
	}

}
