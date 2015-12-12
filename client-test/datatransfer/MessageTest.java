package datatransfer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MessageTest {
	
	@Test
	public void testParseMessage() {
		
		String content;
		Message msg;
		
		content = "OK this is an OK status";
		msg = new Message(content);
		assertEquals(MessageType.OUTPUT_OK, msg.getMessageType());
		assertEquals(content, msg.getMessageContent());
		
		content = "BAD this is a BAD status";
		msg = new Message(content);
		assertEquals(MessageType.OUTPUT_BAD, msg.getMessageType());
		assertEquals(content, msg.getMessageContent());
		
		content = "PM from somebody: this is a PM";
		msg = new Message(content);
		assertEquals(MessageType.CHAT_PRIVATE, msg.getMessageType());
		assertEquals(content, msg.getMessageContent());
		
		content = "Broadcast from somebody: this is a broadcast";
		msg = new Message(content);
		assertEquals(MessageType.CHAT_BROADCAST, msg.getMessageType());
		assertEquals(content, msg.getMessageContent());
		
		content = "Unknown message type";
		msg = new Message(content);
		assertEquals(MessageType.UNKNOWN, msg.getMessageType());
		assertEquals(content, msg.getMessageContent());
		
	}
	
	@Test
	public void testMessageTypes() {
		
		String content;
		Message msg;
		
		content = "Broadcast from somebody: a message";
		msg = new Message(content);
		assertTrue(msg.isChatMessage());
		assertFalse(msg.isListMessage());
		assertFalse(msg.isMessageSentMessage());
		assertFalse(msg.isQuitMessage());
		
		content = "PM from somebody: a message";
		msg = new Message(content);
		assertTrue(msg.isChatMessage());
		assertFalse(msg.isListMessage());
		assertFalse(msg.isMessageSentMessage());
		assertFalse(msg.isQuitMessage());
		
		content = "OK abc,def,ghi";
		msg = new Message(content);
		assertFalse(msg.isChatMessage());
		assertTrue(msg.isListMessage());
		assertFalse(msg.isMessageSentMessage());
		assertFalse(msg.isQuitMessage());
		
		content = "OK your message has been sent";
		msg = new Message(content);
		assertFalse(msg.isChatMessage());
		assertFalse(msg.isListMessage());
		assertTrue(msg.isMessageSentMessage());
		assertFalse(msg.isQuitMessage());
		
		content = "OK thank you for sending 2 message(s)";
		msg = new Message(content);
		assertFalse(msg.isChatMessage());
		assertFalse(msg.isListMessage());
		assertFalse(msg.isMessageSentMessage());
		assertTrue(msg.isQuitMessage());
		
		content = "OK There are currently 2 user(s) on the server";
		msg = new Message(content);
		assertFalse(msg.isChatMessage());
		assertFalse(msg.isListMessage());
		assertFalse(msg.isMessageSentMessage());
		assertFalse(msg.isQuitMessage());
		
	}
	
	@Test
	@SuppressWarnings("deprecation")
	public void testMessageExtraction() {
		
		String origin;
		String[] actual;
		
		origin = "OK abc, def, ghi";
		actual = Message.getOnlineUsers(origin);
		String[] expected = {"abc", "def", "ghi"};
		assertEquals(expected, actual);
		
		origin = "PM from somebody:a message";
		actual = Message.getMessageSenderAndContent(origin);
		String[] expected2 = {"somebody", "a message"};
		assertEquals(expected2, actual);
		
		origin = "Broadcast from somebody: a message";
		actual = Message.getMessageSenderAndContent(origin);
		assertEquals(expected2, actual);
		
		origin = "asdasdasd";
		actual = Message.getMessageSenderAndContent(origin);
		String[] expected3 = {};
		assertEquals(expected3, actual);
		
		actual = Message.getOnlineUsers(origin);
		assertEquals(expected3, actual);
		
	}

}
