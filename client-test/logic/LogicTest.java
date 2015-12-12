package logic;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Collections;
import java.util.List;

import model.ChatMessage;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import datatransfer.Message;
import datatransfer.MessageType;
import exception.InvalidCommandException;

public class LogicTest {
	
	private static final String TEST_USERNAME = "logictest";
	private final Logic logic = new Logic(null, null, TEST_USERNAME);
	private static final File TEST_FILE = new File("files" + File.separator + "chatclient-" + TEST_USERNAME + ".json");
	
	@BeforeClass
	public static void setUp() {
		deleteFileIfExists(TEST_FILE);
	}
	
	private static void deleteFileIfExists(File file) {
		if (file.exists()) {
			file.delete();
		}
	}
	
	@Test
	public void testGetMessage() {
		
		String content;
		Message msg;
		
		msg = logic.getMessage("", TEST_FILE);
		assertNull(msg);
		
		msg = logic.getMessage(null, TEST_FILE);
		assertNull(msg);
		
		content = "OK There are currently 3 users";
		msg = logic.getMessage(content, TEST_FILE);
		assertEquals(MessageType.OUTPUT_OK, msg.getMessageType());
		assertEquals(content, msg.getMessageContent());
		
		content = "OK abc, def, ghi";
		msg = logic.getMessage(content, TEST_FILE);
		assertTrue(msg.isListMessage());
		List<String> usersList = logic.getUsersList();
		Collections.sort(usersList);
		assertEquals(3, usersList.size());
		assertEquals("abc", usersList.get(0));
		assertEquals("def", usersList.get(1));
		assertEquals("ghi", usersList.get(2));
		
		assertFalse(TEST_FILE.exists());
		
		content = "PM from somebody: a message";
		msg = logic.getMessage(content, TEST_FILE);
		assertTrue(msg.isChatMessage());
		List<ChatMessage> messagesList = logic.getMessagesToAndFrom("somebody");
		assertEquals(1, messagesList.size());
		ChatMessage message = messagesList.get(0);
		assertEquals("somebody", message.getSender());
		assertEquals("logictest", message.getRecipient());
		assertEquals("a message", message.getMessage());
		
		assertTrue(TEST_FILE.exists());
		
		/* 
		 * The rest are unit tested in MessageTest
		 */
	}
	
	@Test
	public void testParseCommand() {
		
		String cmd;
		
		cmd = "IDEN somebody";
		parseAndValidateCommandSuccessfully(cmd);

		cmd = "IDEN";
		parseAndValidateCommandWithoutSuccess(cmd);

		cmd = "HAIL message";
		parseAndValidateCommandSuccessfully(cmd);

		cmd = "HAIL ";
		parseAndValidateCommandWithoutSuccess(cmd);

		cmd = "MESG somebody a message";
		parseAndValidateCommandSuccessfully(cmd);

		cmd = "MESG somebody";
		parseAndValidateCommandWithoutSuccess(cmd);

		cmd = "MESG";
		parseAndValidateCommandWithoutSuccess(cmd);

		cmd = "UNKNOWN command";
		parseAndValidateCommandWithoutSuccess(cmd);

		cmd = "QUIT";
		parseAndValidateCommandSuccessfully(cmd);

	}
	
	private void parseAndValidateCommandSuccessfully(String cmd) {
		try {
			logic.parseAndValidateCommand(cmd);
		} catch (InvalidCommandException e) {
			fail();
		}
	}
	
	private void parseAndValidateCommandWithoutSuccess(String cmd) {
		try {
			logic.parseAndValidateCommand(cmd);
			fail();
		} catch (InvalidCommandException e) {
			// exception expected to be thrown: do nothing
		}
	}
	
	@AfterClass
	public static void tearDown() {
		deleteFileIfExists(TEST_FILE);
	}

}
