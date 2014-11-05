package com.ielfgame.stupidGame.swing;

import java.applet.Applet;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.TextAttribute;
import java.util.Hashtable;

/**
 * This class demonstrates how to use Text Attributes to style text.
 */
public class AttributedText extends Applet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void paint(Graphics g) {

		Font font = new Font(Font.SERIF, Font.PLAIN, 24);
		g.setFont(font);
		String text = "Too WAVY";
		g.drawString(text, 45, 30);

		Hashtable<TextAttribute, Object> map = new Hashtable<TextAttribute, Object>();

		/* Kerning makes the text spacing more natural */
		map.put(TextAttribute.KERNING, TextAttribute.KERNING_ON);
		font = font.deriveFont(map);
		g.setFont(font);
		g.drawString(text, 45, 60);

		/* Underlining is easy */
		map.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		font = font.deriveFont(map);
		g.setFont(font);
		g.drawString(text, 45, 90);

		/* Strikethrouh is easy */
		map.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
		font = font.deriveFont(map);
		g.setFont(font);
		g.drawString(text, 45, 120);

		/* This colour applies just to the font, not other rendering */
		map.put(TextAttribute.FOREGROUND, Color.BLUE);
		font = font.deriveFont(map);
		g.setFont(font);
		g.drawString(text, 45, 150);

		BasicStroke bs_3 = new BasicStroke(5f);
		Graphics2D g2 = (Graphics2D) g;
//		float[] dash1 = { 2.0f };
		// 设置打印线的属性。 //1.线宽 2、3、不知道，4、空白的宽度，5、虚线的宽度，6、偏移量
//		g2.setStroke(new BasicStroke(0.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2.0f, dash1, 0.0f));
		g2.setStroke(bs_3);// 设置线宽

//		map.put(TextAttribute., 20);
		font = font.deriveFont(map);
		g2.setFont(font);
		g2.drawString(text, 45, 180);

	}

	public static void main(String[] args) {

		Frame f = new Frame("Attributed Text Sample");

		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		f.add("Center", new AttributedText());
		f.setSize(new Dimension(250, 200));
		f.setVisible(true);
	}
}
