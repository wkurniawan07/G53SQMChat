package ui;

import static ui.Params.*;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.Client;

public class LoginScreen extends ApplicationScreen {
	
	private UsernameField usernameField;
	private JTextField errorMessage;
	
	@SuppressWarnings("serial")
	LoginScreen(final Client client, JPanel contentPanel, String welcomeMsg) {
		super();
		this.contentPanel = contentPanel;
		addWelcomeMessage(welcomeMsg);
		addUsernameField();
		addEnterListenerToUsernameField(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String input = usernameField.getText().trim();
				client.attemptToLogin(input);
				usernameField.setText("");
			}
		});
		addErrorMessagePlaceholder();
	}

	private void addWelcomeMessage(String welcomeMsg) {
		JTextField welcomeMessage = new JTextField(welcomeMsg);
		welcomeMessage.setBounds(BOUNDS_WELCOME_MESSAGE);
		welcomeMessage.setHorizontalAlignment(JTextField.CENTER);
		welcomeMessage.setBorder(BorderFactory.createEmptyBorder());
		welcomeMessage.setFont(ClientUI.FONT_BOLD);
		welcomeMessage.setEditable(false);
		contentPanel.add(welcomeMessage);
	}
	
	private void addUsernameField() {
		usernameField = new UsernameField();
		usernameField.setBounds(BOUNDS_USERNAME_FIELD);
		usernameField.setFont(ClientUI.FONT_NORMAL);
		contentPanel.add(usernameField);
	}
	
	private void addEnterListenerToUsernameField(Action action) {
		usernameField.getInputMap().put(ClientUI.ENTER, "login");
		usernameField.getActionMap().put("login", action);
	}
	
	private void addErrorMessagePlaceholder() {
		errorMessage = new JTextField();
		errorMessage.setForeground(COLOR_DANGER);
		errorMessage.setBounds(BOUNDS_LOGIN_ERROR_MESSAGE);
		errorMessage.setFont(ClientUI.FONT_NORMAL);
		errorMessage.setHorizontalAlignment(JTextField.CENTER);
		errorMessage.setBorder(BorderFactory.createEmptyBorder());
		errorMessage.setEditable(false);
		contentPanel.add(errorMessage);
	}
	
	public void printUsernameResponse(String response) {
		errorMessage.setText(response);
	}
	
}
