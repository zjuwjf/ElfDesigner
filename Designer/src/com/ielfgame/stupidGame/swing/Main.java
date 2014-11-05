package com.ielfgame.stupidGame.swing;

import java.awt.Graphics;

import javax.swing.JPanel;

public class Main extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String s = "Underlined text";

	int x = 10;

	int y = 10;

	public void init() {
	}

	public void paint(Graphics g) {
		g.drawString(s, x, y);
		g.drawLine(x, y + 2, x + getFontMetrics(getFont()).stringWidth(s), y + 2);
	}
}
