package com.ielfgame.stupidGame.swing;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class AWTGraphicsDemo extends Frame {

	public AWTGraphicsDemo() {
		super("Java AWT Examples");
		prepareGUI();
	}

	public static void main(String[] args) {
		AWTGraphicsDemo awtGraphicsDemo = new AWTGraphicsDemo();
		awtGraphicsDemo.setVisible(true);
	}

	private void prepareGUI() {
		setSize(400, 400);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(3.0f));
		g2.setPaint(Color.blue);

		Rectangle2D shape = new Rectangle2D.Float();
		shape.setFrame(100, 150, 200, 100);
		g2.draw(shape);

		Rectangle2D shape1 = new Rectangle2D.Float();
		shape1.setFrame(110, 160, 180, 80);
		g2.setStroke(new BasicStroke(1.0f));

		g2.draw(shape1);
		Font plainFont = new Font("Serif", Font.PLAIN, 24);
		g2.setFont(plainFont);
		g2.setColor(Color.DARK_GRAY);
		g2.drawString("TutorialsPoint", 130, 200);
	}
}