package com.ielfgame.stupidGame.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.TextAttribute;
import java.text.AttributedString;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class IteratorTest extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		String s = "Java Source and Support";
		Dimension d = getSize();

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Font serifFont = new Font("Serif", Font.PLAIN, 48);
		Font sansSerifFont = new Font("Monospaced", Font.PLAIN, 48);

		AttributedString as = new AttributedString(s);
		as.addAttribute(TextAttribute.FONT, serifFont);
		as.addAttribute(TextAttribute.FONT, sansSerifFont, 2, 5);
		as.addAttribute(TextAttribute.FOREGROUND, Color.red, 5, 6);
		as.addAttribute(TextAttribute.FOREGROUND, Color.red, 16, 17);

		g2.drawString(as.getIterator(), 40, 80);
	}

	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.getContentPane().add(new IteratorTest());
		f.setSize(850, 250);
		f.show();

	}
}
