package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Test;

public class ChatMessageTest {
	
	@Test
	public void testEquality() throws InterruptedException {
		Date now = new Date();
		Date later = new Date(now.getTime() + 1000);
		ChatMessage msg1 = new ChatMessage(null, "recipient", "message", now);
		ChatMessage msg2 = new ChatMessage("sender", "recipient", "message", now);
		ChatMessage msg3 = new ChatMessage(null, null, "message", now);
		ChatMessage msg4 = new ChatMessage(null, "recipient", "different message", now);
		ChatMessage msg5 = new ChatMessage(null, "recipient", "message", later);
		assertEquals(msg1, msg1);
		assertNotEquals(msg1, msg2);
		assertNotEquals(msg1, msg3);
		assertNotEquals(msg1, msg4);
		assertNotEquals(msg1, msg5);
		assertNotEquals(msg1, "a string");
	}
	
	@Test
	public void testComparison() throws InterruptedException {
		Date now = new Date();
		Date before = new Date(now.getTime() - 1000);
		Date later = new Date(now.getTime() + 1000);
		Date evenLater = new Date(now.getTime() + 2000);
		ChatMessage msg1 = new ChatMessage("sender", "recipient", "message", before);
		ChatMessage msg2 = new ChatMessage("sender", "recipient", "message", now);
		ChatMessage msg3 = new ChatMessage("sender", "recipient", "message", later);
		ChatMessage msg4 = new ChatMessage("sender", "recipient", "message", evenLater);
		List<ChatMessage> msgs = new ArrayList<ChatMessage>();
		msgs.add(msg4);
		msgs.add(msg1);
		msgs.add(msg3);
		msgs.add(msg2);
		Collections.sort(msgs);
		assertEquals(msg1, msgs.get(0));
		assertEquals(msg2, msgs.get(1));
		assertEquals(msg3, msgs.get(2));
		assertEquals(msg4, msgs.get(3));
	}
	
	@Test
	public void testInvalidity() {
		Date now = new Date();
		ChatMessage msg1 = new ChatMessage("sender", "recipient", "message", now);
		ChatMessage msg2 = new ChatMessage(null, "recipient", "message", now);
		ChatMessage msg3 = new ChatMessage("sender", null, "different message", now);
		ChatMessage msg4 = new ChatMessage("sender", "recipient", null, now);
		ChatMessage msg5 = new ChatMessage("sender", "recipient", "message", null);
		assertFalse(msg1.isInvalid("sender",  "recipient"));
		assertTrue(msg1.isInvalid("notsender",  "recipient"));
		assertTrue(msg1.isInvalid("sender",  "notrecipient"));
		assertTrue(msg2.isInvalid("sender",  "recipient"));
		assertTrue(msg3.isInvalid("sender",  "recipient"));
		assertTrue(msg4.isInvalid("sender",  "recipient"));
		assertTrue(msg5.isInvalid("sender",  "recipient"));
	}

}
