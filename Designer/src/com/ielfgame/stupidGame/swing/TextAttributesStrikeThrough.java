package com.ielfgame.stupidGame.swing;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.TextAttribute;
import java.text.AttributedString;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class TextAttributesStrikeThrough extends JPanel {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public void paint(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    AttributedString as1 = new AttributedString("1234567890");
    as1.addAttribute(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON, 2, 8);
    g2d.drawString(as1.getIterator(), 15, 60);
  }

  public static void main(String[] args) {
    JFrame frame = new JFrame("Text attributes");
    frame.add(new TextAttributesStrikeThrough());
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    frame.setSize(620, 190);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }
}
