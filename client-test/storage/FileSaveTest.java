package storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.ChatMessage;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class FileSaveTest {
	
	private static final String TEST_USERNAME1 = "savetest";
	private static final String TEST_USERNAME2 = "nullsavetest";
	
	@BeforeClass
	public static void setUp() {
		deleteTestFiles();
	}
	
	private static void deleteTestFiles() {
		File file1 = getFile(TEST_USERNAME1);
		delete(file1);
		File file2 = getFile(TEST_USERNAME2);
		delete(file2);
	}
	
	private static File getFile(String username) {
		return new File(getFilePath(username));
	}
	
	private static String getFilePath(String username) {
		return "files" + File.separator + "chatclient-" + username + ".json";
	}
	
	private static void delete(File file) {
		if (file.exists()) {
			file.delete();
		}
	}
	
	@Test
	public void testSaveJsonStandard() throws IOException {
		
		Map<String, List<ChatMessage>> map = new HashMap<String, List<ChatMessage>>();
		
		List<ChatMessage> user1Messages = new ArrayList<ChatMessage>();
		user1Messages.add(new ChatMessage("user1", TEST_USERNAME1, "message 1", new Date(1449873189046L)));
		user1Messages.add(new ChatMessage("user1", "user2", "hacked message 1", new Date(1449873199046L)));
		user1Messages.add(new ChatMessage(TEST_USERNAME1, "user1", "message 2", new Date(1449873274509L)));
		map.put("user1", user1Messages);

		List<ChatMessage> user2Messages = new ArrayList<ChatMessage>();
		user2Messages.add(new ChatMessage("user2", TEST_USERNAME1, "message 4", new Date(1449869360035L)));
		user2Messages.add(new ChatMessage(TEST_USERNAME1, "user2", "message 3", new Date(1449869094222L)));
		user2Messages.add(new ChatMessage(TEST_USERNAME1, "user1", "hacked message 2", new Date(1449869094223L)));
		map.put("user2", user2Messages);

		List<ChatMessage> user3Messages = new ArrayList<ChatMessage>();
		user3Messages.add(new ChatMessage("user3", "user1", "hacked message 3", new Date(1449869360035L)));
		user3Messages.add(new ChatMessage("user2", TEST_USERNAME1, "hacked message 4", new Date(1449871871876L)));
		map.put("user3", user3Messages);
		
		FileSaver saver = new FileSaver(TEST_USERNAME1);
		saver.save(getFile(TEST_USERNAME1), map);
		
		File savedFile = getFile(TEST_USERNAME1);
		assertTrue(savedFile.exists());
		assertFileEquals(getFilePath("savetestexpected"), getFilePath(TEST_USERNAME1));
	}
	
	@Test
	public void testSaveJsonNull() throws IOException {
		
		Map<String, List<ChatMessage>> map = new HashMap<String, List<ChatMessage>>();

		List<ChatMessage> user1Messages = new ArrayList<ChatMessage>();
		user1Messages.add(new ChatMessage("user1", TEST_USERNAME2, "message 1", null));
		user1Messages.add(new ChatMessage("user1", TEST_USERNAME2, null, new Date(1449873199046L)));
		user1Messages.add(new ChatMessage(null, TEST_USERNAME2, "hacked message 1", new Date(1449873274509L)));
		user1Messages.add(new ChatMessage(TEST_USERNAME2, null, "message 2", new Date(1449873274509L)));
		user1Messages.add(new ChatMessage(TEST_USERNAME2, "user1", "a normal one", new Date(1449873274509L)));
		map.put("user1", user1Messages);

		FileSaver saver = new FileSaver(TEST_USERNAME2);
		saver.save(getFile(TEST_USERNAME2), map);
		
		File savedFile = getFile(TEST_USERNAME2);
		assertTrue(savedFile.exists());
		assertFileEquals(getFilePath("nullsavetestexpected"), getFilePath(TEST_USERNAME2));
	}
	
	private void assertFileEquals(String expected, String actual) throws IOException {
		String expectedContent = FileLoader.readFileContent(expected);
		String actualContent = FileLoader.readFileContent(actual);
		assertEquals(expectedContent, actualContent);
	}
	
	@AfterClass
	public static void tearDown() {
		deleteTestFiles();
	}
	
}
