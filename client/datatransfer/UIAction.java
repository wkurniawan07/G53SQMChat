package datatransfer;

public class UIAction {
	
	private final UIActionType type;
	private final String message;
	
	public UIAction(UIActionType type, String message) {
		this.type = type;
		this.message = message;
	}
	
	public UIActionType getType() {
		return type;
	}
	
	public String getMessage() {
		return message;
	}
	
	public static UIAction getActionForMessage(Message msg, String username) {
		if (msg == null) {
			return new UIAction(UIActionType.UNKNOWN, "");
		}
		switch (msg.getMessageType()) {
			case OUTPUT_OK:
				if (msg.isListMessage()) {
					return new UIAction(UIActionType.REFRESH_USER_LIST, msg.getMessageContent());
				} else if (msg.isMessageSentMessage()) {
					return new UIAction(UIActionType.REFRESH_MESSAGE_PANEL, "PM from " + username + ": ");
				} else if (msg.isQuitMessage()) {
					return new UIAction(UIActionType.QUIT, msg.getMessageContent());
				} else {
					return new UIAction(UIActionType.DISPLAY_OK_MESSAGE, msg.getMessageContent());
				}
			case OUTPUT_BAD:
				return new UIAction(UIActionType.DISPLAY_BAD_MESSAGE, msg.getMessageContent());
			case CHAT_PRIVATE:
			case CHAT_BROADCAST:
				return new UIAction(UIActionType.REFRESH_MESSAGE_PANEL, msg.getMessageContent());
			default:
				return new UIAction(UIActionType.UNKNOWN, "");
		}
	}
	
}
