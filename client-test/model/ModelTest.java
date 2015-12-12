package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Date;
import java.util.List;

import org.junit.Test;

public class ModelTest {
	
	private final Model model = new Model("username");
	
	@Test
	public void testNullValues() {
		
		List<ChatMessage> messagesList;
		
		messagesList = model.getMessagesToAndFrom(null);
		assertNull(messagesList);
		
		messagesList = model.getMessagesToAndFrom("somebody");
		assertNull(messagesList);

		model.addMessageTo(null, null, null);
		model.addMessageTo(null, "a message", null);
		model.addMessageTo("somebody", null, null);
		
		messagesList = model.getMessagesToAndFrom("somebody");
		assertNull(messagesList);
		
		model.addMessageFrom(null, null, null);
		model.addMessageFrom(null, "a message", null);
		model.addMessageFrom("somebody", null, null);
		
		messagesList = model.getMessagesToAndFrom("somebody");
		assertNull(messagesList);

	}
	
	@Test
	public void testAddMessageTo() {
		
		List<ChatMessage> messagesList;
		
		model.addMessageTo("somebody", "a message", null);
		messagesList = model.getMessagesToAndFrom("somebody");
		assertNull(messagesList);
		
		model.updateUserList("somebody", "anotherperson");
		
		model.addMessageTo("somebody", "a message", null);
		messagesList = model.getMessagesToAndFrom("somebody");
		assertEquals(1, messagesList.size());
		
		Date now = new Date();
		model.addMessageTo("anotherperson", "a message", now);
		messagesList = model.getMessagesToAndFrom("anotherperson");
		ChatMessage expected = new ChatMessage("username", "anotherperson", "a message", now);
		assertEquals(expected, messagesList.get(0));
		
	}
	
	@Test
	public void testAddMessageFrom() {
		
		List<ChatMessage> messagesList;
		
		model.addMessageFrom("somebody", "a message", null);
		messagesList = model.getMessagesToAndFrom("somebody");
		assertEquals(1, messagesList.size());
		
		Date now = new Date();
		model.addMessageFrom("anotherperson", "a message", now);
		messagesList = model.getMessagesToAndFrom("anotherperson");
		ChatMessage expected = new ChatMessage("anotherperson", "username", "a message", now);
		assertEquals(expected, messagesList.get(0));
		
	}
	
	@Test
	public void testMixtureOfToAndFrom() {
		
		List<ChatMessage> messagesList;
		model.updateUserList("somebody", "anotherperson");
		
		model.addMessageFrom("somebody", "a message", null);
		messagesList = model.getMessagesToAndFrom("somebody");
		assertEquals(1, messagesList.size());
		
		model.addMessageTo("somebody", "a message", null);
		messagesList = model.getMessagesToAndFrom("somebody");
		assertEquals(2, messagesList.size());
		
		model.addMessageFrom("somebody", "a message", null);
		messagesList = model.getMessagesToAndFrom("somebody");
		assertEquals(3, messagesList.size());
		
		model.addMessageTo("somebody", "a message", null);
		messagesList = model.getMessagesToAndFrom("somebody");
		assertEquals(4, messagesList.size());
		
	}
	
}
