package datatransfer;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UIActionTest {
	
	@Test
	public void testGetActionForMessage() {
		
		Message msg;
		UIAction action;
		
		action = UIAction.getActionForMessage(null, "");
		assertEquals(UIActionType.UNKNOWN, action.getType());
		
		msg = new Message("OK abc, def, ghi");
		action = UIAction.getActionForMessage(msg, "");
		assertEquals(UIActionType.REFRESH_USER_LIST, action.getType());
		
		msg = new Message("OK your message has been sent");
		action = UIAction.getActionForMessage(msg, "");
		assertEquals(UIActionType.REFRESH_MESSAGE_PANEL, action.getType());
		assertEquals("PM from : ", action.getMessage());
		
		msg = new Message("OK thank you for sending 3 message(s)");
		action = UIAction.getActionForMessage(msg, "");
		assertEquals(UIActionType.QUIT, action.getType());
		
		msg = new Message("OK There are currently 3 user(s)");
		action = UIAction.getActionForMessage(msg, "");
		assertEquals(UIActionType.DISPLAY_OK_MESSAGE, action.getType());
		
		msg = new Message("BAD this is a bad message");
		action = UIAction.getActionForMessage(msg, "");
		assertEquals(UIActionType.DISPLAY_BAD_MESSAGE, action.getType());
		
		msg = new Message("PM from somebody: a message");
		action = UIAction.getActionForMessage(msg, "");
		assertEquals(UIActionType.REFRESH_MESSAGE_PANEL, action.getType());
		
		msg = new Message("Broadcast from somebody: a message");
		action = UIAction.getActionForMessage(msg, "");
		assertEquals(UIActionType.REFRESH_MESSAGE_PANEL, action.getType());
		
		msg = new Message("Unknown command");
		action = UIAction.getActionForMessage(msg, "");
		assertEquals(UIActionType.UNKNOWN, action.getType());
		
	}

}
