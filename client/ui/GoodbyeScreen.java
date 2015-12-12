package ui;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GoodbyeScreen extends ApplicationScreen {
	
	GoodbyeScreen(JPanel contentPanel, String goodbyeMsg) {
		super();
		this.contentPanel = contentPanel;
		addGoodbyeText(goodbyeMsg);
	}

	private void addGoodbyeText(String goodbyeMsg) {
		JTextField goodbyeMessageDisplay = new JTextField(goodbyeMsg);
		goodbyeMessageDisplay.setBounds(Params.BOUNDS_GOODBYE_TEXT);
		goodbyeMessageDisplay.setHorizontalAlignment(JTextField.CENTER);
		goodbyeMessageDisplay.setBorder(BorderFactory.createEmptyBorder());
		goodbyeMessageDisplay.setFont(ClientUI.FONT_BOLD);
		goodbyeMessageDisplay.setForeground(Params.COLOR_NORMAL);
		goodbyeMessageDisplay.setEditable(false);
		contentPanel.add(goodbyeMessageDisplay);
	}
	
}
