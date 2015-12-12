package ui;

@SuppressWarnings("serial")
public class ChatTextField extends AbstractTextField {
	
	ChatTextField(String placeholderMsg) {
		super(placeholderMsg);
	}
	
	void setPlaceholderMsg(String placeholderMsg) {
		this.placeholderMsg = placeholderMsg;
	}
	
}
