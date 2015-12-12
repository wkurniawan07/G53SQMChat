package storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import model.ChatMessage;

import org.junit.Test;

public class FileLoadTest {
	
	@Test
	public void testLoadJsonStandard() {
		
		String testUsername = "loadtest";
		File file = getFile(testUsername);
		assertTrue(file.exists());
		Map<String, List<ChatMessage>> loadedMap = new FileLoader(testUsername).load(file);
		assertEquals(2, loadedMap.size());
		
		List<ChatMessage> user1Messages = loadedMap.get("user1");
		assertEquals(2, user1Messages.size());
		ChatMessage user1Chat1 = new ChatMessage("user1", testUsername, "message 1", new Date(1449873189046L));
		ChatMessage user1Chat2 = new ChatMessage(testUsername, "user1", "message 2", new Date(1449873274509L));
		assertEquals(user1Chat1, user1Messages.get(0));
		assertEquals(user1Chat2, user1Messages.get(1));
		
		List<ChatMessage> user2Messages = loadedMap.get("user2");
		assertEquals(2, user2Messages.size());
		ChatMessage user2Chat1 = new ChatMessage(testUsername, "user2", "message 3", new Date(1449869094222L));
		ChatMessage user2Chat2 = new ChatMessage("user2", testUsername, "message 4", new Date(1449869360035L));
		assertEquals(user2Chat1, user2Messages.get(0));
		assertEquals(user2Chat2, user2Messages.get(1));
		
	}
	
	@Test
	public void testLoadJsonNonExistentFile() {
		
		String testUsername = "nonexistent";
		File file = getFile(testUsername);
		assertFalse(file.exists());
		Map<String, List<ChatMessage>> loadedMap = new FileLoader(testUsername).load(file);
		assertTrue(loadedMap.isEmpty());
		
	}

	@Test
	public void testLoadJsonMalformedFile() {
		
		String testUsername = "malformed";
		File file = getFile(testUsername);
		assertTrue(file.exists());
		Map<String, List<ChatMessage>> loadedMap = new FileLoader(testUsername).load(file);
		assertTrue(loadedMap.isEmpty());
		
	}

	@Test
	public void testLoadJsonNullFields() {
		
		String testUsername = "nullloadtest";
		File file = getFile(testUsername);
		assertTrue(file.exists());
		Map<String, List<ChatMessage>> loadedMap = new FileLoader(testUsername).load(file);
		assertEquals(1, loadedMap.size());
		
	}
	
	private File getFile(String username) {
		return new File("files" + File.separator + "chatclient-" + username + ".json");
	}

}
