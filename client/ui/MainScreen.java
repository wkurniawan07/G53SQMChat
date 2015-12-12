package ui;

import static ui.Params.*;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import main.Client;
import main.Const;
import model.ChatMessage;
import datatransfer.Message;
import datatransfer.UIAction;

public class MainScreen extends ApplicationScreen {
	
	private final Client client;
	private final String username;
	
	private JScrollPane messagesPanel;
	private JScrollPane usersPanel;
	private ChatTextField privateChatTextField;
	private JTextField logMessageDisplay;
	
	private String currentChatPartner;
	
	MainScreen(final Client client, JPanel contentPanel, String username) {
		super();
		this.client = client;
		this.contentPanel = contentPanel;
		this.username = username;
		addUsernameDisplay(username);
		addRefreshButton();
		addMessagesPanel();
		addUsersPanel();
		addPrivateChatTextField();
		addBroadcastTextField();
		addQuitButton();
		addLogMessageDisplay();
	}
	
	private void addUsernameDisplay(String username) {
		JTextField usernameDisplay = new JTextField(String.format(USERNAME_DISPLAY_TEXT, username));
		usernameDisplay.setBounds(BOUNDS_USERNAME_DISPLAY);
		usernameDisplay.setFont(ClientUI.FONT_BOLD);
		usernameDisplay.setBorder(BorderFactory.createEmptyBorder());
		usernameDisplay.setEditable(false);
		contentPanel.add(usernameDisplay);
	}
	
	@SuppressWarnings("serial")
	private void addRefreshButton() {
		JButton refreshButton = new JButton(BUTTON_REFRESH);
		refreshButton.setBackground(Color.WHITE);
		refreshButton.setBounds(BOUNDS_REFRESH_BUTTON);
		refreshButton.setFont(ClientUI.FONT_BOLD);
		refreshButton.addActionListener(new AbstractAction() { 
			@Override
			public void actionPerformed(ActionEvent e) {
				client.sendCommand(Const.COMMAND_LIST);
			}
		});
		contentPanel.add(refreshButton);
	}
	
	private void addMessagesPanel() {
		messagesPanel = new JScrollPane();
		messagesPanel.setBounds(BOUNDS_MESSAGES_PANEL);
		messagesPanel.setVisible(false);
		contentPanel.add(messagesPanel);
	}
	
	private void addUsersPanel() {
		usersPanel = new JScrollPane();
		usersPanel.setBounds(BOUNDS_USERS_PANEL);
		contentPanel.add(usersPanel);
	}
	
	private void addPrivateChatTextField() {
		privateChatTextField = new ChatTextField("");
		privateChatTextField.setBackground(Color.WHITE);
		privateChatTextField.setBounds(BOUNDS_PRIVATE_CHAT_TEXT_FIELD);
		privateChatTextField.setFont(ClientUI.FONT_NORMAL);
		privateChatTextField.setVisible(false);
		contentPanel.add(privateChatTextField);
	}
	
	private void addEnterListenerToPrivateChatTextField(Action action) {
		privateChatTextField.getInputMap().put(ClientUI.ENTER, "chat");
		privateChatTextField.getActionMap().put("chat", action);
	}
	
	@SuppressWarnings("serial")
	private void addBroadcastTextField() {
		final ChatTextField broadcastTextField = new ChatTextField(PLACEHOLDER_BROADCAST);
		broadcastTextField.setBackground(Color.WHITE);
		broadcastTextField.setBounds(BOUNDS_BROADCAST_TEXT_FIELD);
		broadcastTextField.setFont(ClientUI.FONT_NORMAL);
		broadcastTextField.getInputMap().put(ClientUI.ENTER, "broadcast");
		broadcastTextField.getActionMap().put("broadcast", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String message = broadcastTextField.getText().trim();
				client.sendCommand(String.format(Const.COMMAND_HAIL_FORMAT, message));
				broadcastTextField.setText("");
			}
		});
		contentPanel.add(broadcastTextField);
	}

	@SuppressWarnings("serial")
	private void addQuitButton() {
		JButton quitButton = new JButton(BUTTON_QUIT);
		quitButton.setBackground(Color.WHITE);
		quitButton.setBounds(BOUNDS_QUIT_BUTTON);
		quitButton.setFont(ClientUI.FONT_BOLD);
		quitButton.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				client.sendCommand(Const.COMMAND_QUIT);
			}
		});
		contentPanel.add(quitButton);
	}
	
	private void addLogMessageDisplay() {
		logMessageDisplay = new JTextField();
		logMessageDisplay.setBounds(BOUNDS_LOG_MESSAGE_DISPLAY);
		logMessageDisplay.setFont(ClientUI.FONT_NORMAL);
		logMessageDisplay.setBorder(BorderFactory.createEmptyBorder());
		logMessageDisplay.setHorizontalAlignment(JTextField.CENTER);
		logMessageDisplay.setEditable(false);
		contentPanel.add(logMessageDisplay);
	}
	
	@SuppressWarnings("serial")
	private void changeChatPartner() {
		if (currentChatPartner == null) {
			messagesPanel.setVisible(false);
			
			privateChatTextField.setPlaceholderMsg("");
			privateChatTextField.setVisible(false);
		} else {
			refreshMessagePanel();
			messagesPanel.setVisible(true);
			
			privateChatTextField.setPlaceholderMsg(String.format(PLACEHOLDER_CHAT, currentChatPartner));
			addEnterListenerToPrivateChatTextField(new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String message = privateChatTextField.getText().trim();
					client.sendCommand(String.format(Const.COMMAND_MESG_FORMAT, currentChatPartner, message));
					privateChatTextField.setText("");
				}
			});
			privateChatTextField.setVisible(true);
			privateChatTextField.revalidate();
			privateChatTextField.repaint();
		}
	}
	
	private void refreshMessagePanel() {
		List<ChatMessage> messages = client.getMessagesToAndFrom(currentChatPartner);
		Box box = Box.createVerticalBox();
		if (messages != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault());
			for (ChatMessage msg: messages) {
				String sender = msg.getSender().equals(currentChatPartner) ? currentChatPartner : "You";
				String label = String.format(MESSAGE_DISPLAY, sender, sdf.format(msg.getTimestamp()), msg.getMessage());
				JLabel message = new JLabel(label);
				message.setFont(ClientUI.FONT_NORMAL);
				box.add(message);
			}
		}
	    messagesPanel.getViewport().add(box);				
	    messagesPanel.revalidate();
	    messagesPanel.repaint();
		JScrollBar vertical = messagesPanel.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
	}
	
	private void refreshUserList(String... onlineUsers) {
		Box box = Box.createVerticalBox();
		boolean isCurrentChatPartnerStillOnline = false;
		for (final String user : onlineUsers) {
			if (!user.equals(username)) {
				if (user.equals(currentChatPartner)) {
					isCurrentChatPartnerStillOnline = true;
				}
				final JLabel usernameDisplay = new JLabel(user);
				usernameDisplay.setText(user);
				usernameDisplay.setFont(ClientUI.FONT_BOLD);
				usernameDisplay.setBorder(new EmptyBorder(10, 10, 10, 10));
				usernameDisplay.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if (currentChatPartner == null || !currentChatPartner.equals(user)) {
							currentChatPartner = user;
							changeChatPartner();
						}
					}
				});
				box.add(usernameDisplay);
			}
		}
	    usersPanel.getViewport().add(box);				
		usersPanel.revalidate();
		usersPanel.repaint();
		if (!isCurrentChatPartnerStillOnline) {
			currentChatPartner = null;
			changeChatPartner();
		}
	}
	
	public void followUpAction(UIAction action) {
		switch (action.getType()) {
			case DISPLAY_OK_MESSAGE:
				logMessageDisplay.setForeground(COLOR_NORMAL);
				logMessageDisplay.setText(action.getMessage());
				break;
			case DISPLAY_BAD_MESSAGE:
				logMessageDisplay.setForeground(COLOR_DANGER);
				logMessageDisplay.setText(action.getMessage());
				break;
			case REFRESH_USER_LIST:
				logMessageDisplay.setText("");
				String[] onlineUsers = Message.getOnlineUsers(action.getMessage());
				refreshUserList(onlineUsers);
				break;
			case REFRESH_MESSAGE_PANEL:
				logMessageDisplay.setText("");
				String from = Message.getMessageSenderAndContent(action.getMessage())[0];
				if (from.equals(currentChatPartner) || from.equals(username)) {
					refreshMessagePanel();
				}
				break;
			default:
				break;
		}
	}
	
}
