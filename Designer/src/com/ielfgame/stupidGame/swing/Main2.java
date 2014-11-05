package com.ielfgame.stupidGame.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main2 extends JPanel {
	String text = "this is a test. This is a test. This is a test. This is a test";

	AttributedString attribString = new AttributedString(text);

	AttributedCharacterIterator attribCharIterator;

	public Main2() {
		setSize(350, 400);
		attribString.addAttribute(TextAttribute.FOREGROUND, Color.blue, 0, text.length());
		attribString.addAttribute(TextAttribute.FONT, new Font("sanserif", Font.ITALIC, 20), 0, text.length());
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		attribCharIterator = attribString.getIterator();

		FontRenderContext frc = new FontRenderContext(null, false, false);
		LineBreakMeasurer lbm = new LineBreakMeasurer(attribCharIterator, frc);

		int x = 10, y = 20;
		int w = getWidth();

		float wrappingWidth = w - 15;

		while (lbm.getPosition() < text.length()) {
			TextLayout layout = lbm.nextLayout(wrappingWidth);
			y += layout.getAscent();
			layout.draw(g2, x, y);
			y += layout.getDescent() + layout.getLeading();
		}
	}

	public static void main(String arg[]) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add("Center", new Main2());
		frame.pack();
		frame.setSize(new Dimension(350, 400));
		frame.setVisible(true);
	}
}