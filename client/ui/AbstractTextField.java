package ui;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JTextField;

@SuppressWarnings("serial")
public abstract class AbstractTextField extends JTextField {

	protected String placeholderMsg;

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if (getText().trim().isEmpty()) {
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setFont(ClientUI.FONT_NORMAL);
			g2.setColor(Params.COLOR_FADE);
			g2.drawString(placeholderMsg, 5, 20);
			g2.dispose();
		}
	}
	
	AbstractTextField(String placeholderMsg) {
		super();
		this.placeholderMsg = placeholderMsg;
	}
	
}
