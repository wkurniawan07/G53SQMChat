package ui;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import datatransfer.UIAction;
import main.Client;

public class ClientUI {
	
	static final KeyStroke ENTER = KeyStroke.getKeyStroke("ENTER");

	static Font FONT_NORMAL;
	static Font FONT_BOLD;
	
	private final Client chatClient;
	
	private JFrame mainFrame;
	private JPanel contentPanel;
	
	private ApplicationScreen screen;
	
	public ClientUI(Client client) {
		this.chatClient = client;
		initMainFrame();
		initFont();
		addContentPanel();
		presentToUser();
	}
	
	private void initMainFrame() {
		mainFrame = new JFrame("ChatClient");
		mainFrame.setSize(Params.WIDTH_MAINFRAME, Params.HEIGHT_MAINFRAME);
		mainFrame.setResizable(false);
		mainFrame.setLocationRelativeTo(null);
	}
	
	private void initFont() {
		try {
			Font font = Font.createFont(Font.TRUETYPE_FONT, getClass()
					.getResourceAsStream("/fonts/Ubuntu.ttf"));
			FONT_NORMAL = font.deriveFont(Font.PLAIN, 15);
			FONT_BOLD = font.deriveFont(Font.BOLD, 15);
		} catch (FontFormatException|IOException e) {
			// will not happen
		}
	}
	
	private void addContentPanel() {
		contentPanel = new JPanel();
		contentPanel.setLayout(null);
		mainFrame.setContentPane(contentPanel);
	}
	
	private void presentToUser() {
		mainFrame.setVisible(true);
	}
	
	private void displayConnectionErrorScreen() {
		contentPanel.removeAll();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		screen = new ConnectionErrorScreen(contentPanel);
		contentPanel.revalidate();
		contentPanel.repaint();
	}

	private void displayLoginScreen(String welcomeMsg) {
		contentPanel.removeAll();
		mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		screen = new LoginScreen(chatClient, contentPanel, welcomeMsg);
		contentPanel.revalidate();
		contentPanel.repaint();
	}
	
	private void displayMainScreen(String username) {
		contentPanel.removeAll();
		mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		screen = new MainScreen(chatClient, contentPanel, username);
		contentPanel.revalidate();
		contentPanel.repaint();
	}
	
	private void displayGoodbyeScreen(String goodbyeMsg) {
		contentPanel.removeAll();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		screen = new GoodbyeScreen(contentPanel, goodbyeMsg);
		contentPanel.revalidate();
		contentPanel.repaint();
	}
	
	public void followUpAction(UIAction action) {
		switch (action.getType()) {
			case DISPLAY_LOGIN_SCREEN:
				displayLoginScreen(action.getMessage());
				break;
			case PRINT_BAD_USERNAME_RESPONSE:
				((LoginScreen) screen).printUsernameResponse(action.getMessage());
				break;
			case DISPLAY_ERROR_SCREEN:
				displayConnectionErrorScreen();
				break;
			case DISPLAY_MAIN_SCREEN:
				displayMainScreen(action.getMessage());
				break;
			case QUIT:
				displayGoodbyeScreen(action.getMessage());
				break;
			default:
				try {
					((MainScreen) screen).followUpAction(action);
				} catch (ClassCastException e) {
					// equivalent to not doing anything, i.e case UNKNOWN
				}
				break;
		}
	}
	
}
