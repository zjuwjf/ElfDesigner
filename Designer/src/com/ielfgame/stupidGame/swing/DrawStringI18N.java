package com.ielfgame.stupidGame.swing;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;

public class DrawStringI18N extends JFrame {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public void paint(Graphics g) {
    Graphics2D graphics2D = (Graphics2D) g;
    GraphicsEnvironment.getLocalGraphicsEnvironment();
    graphics2D.setFont(new Font("LucidaSans", Font.PLAIN, 40));
    graphics2D.drawString("\u05E9\u05DC\u05D5\u05DD \u05E2\u05D5\u05DC\u05DD", 50, 75);
  }

  public static void main(String[] args) {
    JFrame frame = new DrawStringI18N();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    frame.setSize(400,400);
    frame.setVisible(true);
  }
}