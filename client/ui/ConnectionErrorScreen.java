package ui;

import static ui.Params.*;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ConnectionErrorScreen extends ApplicationScreen {
	
	ConnectionErrorScreen(JPanel contentPanel) {
		super();
		this.contentPanel = contentPanel;
		addConnectionUnavailableText();
	}

	private void addConnectionUnavailableText() {
		JTextField singleMessageDisplay = new JTextField(CONNECTION_UNAVAILABLE_TEXT);
		singleMessageDisplay.setBounds(BOUNDS_CONNECTION_UNAVAILABLE_TEXT);
		singleMessageDisplay.setHorizontalAlignment(JTextField.CENTER);
		singleMessageDisplay.setBorder(BorderFactory.createEmptyBorder());
		singleMessageDisplay.setFont(ClientUI.FONT_BOLD);
		singleMessageDisplay.setForeground(COLOR_DANGER);
		singleMessageDisplay.setEditable(false);
		contentPanel.add(singleMessageDisplay);
	}
	
}
