package com.ielfgame.stupidGame.swing;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;

import javax.swing.JFrame;

public class Talks extends Canvas {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		FontRenderContext frc = g2.getFontRenderContext();
		Font f = new Font("細明體", Font.BOLD, 50);
		String s = "文字描邊 \n hello world!";
		TextLayout tl = new TextLayout(s, f, frc);
		
//		tl.

		Shape sha = tl.getOutline(AffineTransform.getTranslateInstance(100, 100));
		g2.setColor(Color.black);
		g2.setStroke(new BasicStroke(2f));
		g2.draw(sha);

		g2.setColor(Color.white);
		g2.fill(sha);
	}

	public static void main(String[] args) {
		JFrame tmp = new JFrame("文字描邊");
		tmp.setSize(480, 416);
		Container ct = tmp.getContentPane();
		ct.add(new Talks());
		tmp.setVisible(true);
	}
}
