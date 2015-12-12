package ui;

import java.awt.Color;
import java.awt.Rectangle;

public class Params {
	
	static final int WIDTH_MAINFRAME = 750;
	static final int HEIGHT_MAINFRAME = 600;
	
	static final Rectangle BOUNDS_CONNECTION_UNAVAILABLE_TEXT = new Rectangle(25, 245, 700, 30);
	
	static final Rectangle BOUNDS_WELCOME_MESSAGE = new Rectangle(25, 175, 700, 30);
	static final Rectangle BOUNDS_USERNAME_FIELD = new Rectangle(250, 240, 250, 30);
	static final Rectangle BOUNDS_LOGIN_ERROR_MESSAGE = new Rectangle(25, 275, 700, 90);
	
	static final Rectangle BOUNDS_USERNAME_DISPLAY = new Rectangle(25, 25, 500, 30);
	static final Rectangle BOUNDS_REFRESH_BUTTON = new Rectangle(550, 460, 175, 30);
	static final Rectangle BOUNDS_MESSAGES_PANEL = new Rectangle(25, 80, 500, 300);
	static final Rectangle BOUNDS_USERS_PANEL = new Rectangle(550, 80, 175, 355);
	static final Rectangle BOUNDS_PRIVATE_CHAT_TEXT_FIELD = new Rectangle(25, 405, 500, 30);
	static final Rectangle BOUNDS_BROADCAST_TEXT_FIELD = new Rectangle(25, 460, 500, 30);
	static final Rectangle BOUNDS_QUIT_BUTTON = new Rectangle(550, 25, 175, 30);
	static final Rectangle BOUNDS_LOG_MESSAGE_DISPLAY = new Rectangle(25, 515, 700, 30);
	
	static final Rectangle BOUNDS_GOODBYE_TEXT = new Rectangle(25, 245, 700, 30);
	
	static final String BUTTON_REFRESH = "REFRESH";
	static final String BUTTON_QUIT = "QUIT";

	static final String CONNECTION_UNAVAILABLE_TEXT = "Connection to the server is unavailable :(";
	static final String USERNAME_DISPLAY_TEXT = "You are logged in as %s";
	static final String MESSAGE_DISPLAY = "%s (%s): %s";
	
	static final String PLACEHOLDER_LOGIN = "Enter your username here";
	static final String PLACEHOLDER_BROADCAST = "Enter a message for everyone currently online";
	static final String PLACEHOLDER_CHAT = "Enter a message for %s";
	
	static final Color COLOR_NORMAL = Color.BLACK;
	static final Color COLOR_DANGER = Color.RED;
	static final Color COLOR_SELECTED = Color.BLUE;
	static final Color COLOR_FADE = Color.GRAY;
	
}
