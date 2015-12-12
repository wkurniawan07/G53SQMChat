package storage;

public abstract class AbstractConverter {
	
	protected static final String KEY_SENDER = "sender";
	protected static final String KEY_RECIPIENT = "recipient";
	protected static final String KEY_TIMESTAMP = "timestamp";
	protected static final String KEY_MESSAGE = "message";

	protected abstract Object convertMessagesMap(Object map);
	
	protected abstract Object convertMessagesList(Object list, String partner);
	
	protected abstract Object convertMessage(Object msg, String partner);
	
}
