package com.ielfgame.stupidGame.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextHitInfo;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class TextLayoutPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	TextLayout layout;

	FontRenderContext frc;

	Font font;

	Rectangle2D rect;

	float rx, ry, rw, rh;

	TextHitInfo hitInfo;

	Color caretColor;

	int hit1, hit2;

	int w, h;

	public TextLayoutPanel() {
		setBackground(Color.white);
		setForeground(Color.black);
		setSize(400, 200);
		addMouseListener(new MouseHandler());
		addMouseMotionListener(new MouseMotionHandler());
		w = getWidth();
		h = getHeight();

		String text = "Java Source and Support";
		font = new Font("Arial", Font.PLAIN, 36);
		frc = new FontRenderContext(null, false, false);
		layout = new TextLayout(text, font, frc);

		rx = (float) (w / 2 - layout.getBounds().getWidth() / 2);
		ry = (float) 3 * h / 4;
		rw = (float) (layout.getBounds().getWidth());
		rh = (float) (layout.getBounds().getHeight());
		rect = new Rectangle2D.Float(rx, ry, rw, rh);

		caretColor = getForeground();
	}

	public void update(Graphics g) {
		g.clearRect(0, 0, getWidth(), getHeight());
		paintComponent(g);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		AffineTransform at = AffineTransform.getTranslateInstance(rx, ry);

		Shape hilight = layout.getLogicalHighlightShape(hit1, hit2);
		hilight = at.createTransformedShape(hilight);
		g2.setColor(Color.lightGray);
		g2.fill(hilight);

		g2.setColor(Color.black);
		layout.draw(g2, rx, ry);

		// Draw caret
		Shape[] caretShapes = layout.getCaretShapes(hit1);
		Shape caret = at.createTransformedShape(caretShapes[0]);
		g2.setColor(caretColor);
		g2.draw(caret);
	}

	public int getHitLocation(int mouseX, int mouseY) {
		hitInfo = layout.hitTestChar(mouseX, mouseY, rect);
		return hitInfo.getInsertionIndex();
	}

	class MouseHandler extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			caretColor = getForeground();
			hit1 = getHitLocation(e.getX(), e.getY());
			hit2 = hit1;
			repaint();
		}

		public void mousePressed(MouseEvent e) {
			caretColor = getForeground();
			hit1 = getHitLocation(e.getX(), e.getY());
			hit2 = hit1;
			repaint();
		}

		public void mouseReleased(MouseEvent e) {
			hit2 = getHitLocation(e.getX(), e.getY());
			repaint();
		}
	}

	class MouseMotionHandler extends MouseMotionAdapter {
		public void mouseDragged(MouseEvent e) {
			caretColor = getBackground();
			hit2 = getHitLocation(e.getX(), e.getY());
			repaint();
		}
	}

	public static void main(String arg[]) {
		JFrame frame = new JFrame();
		frame.setBackground(Color.white);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		frame.getContentPane().add("Center", new TextLayoutPanel());
		frame.pack();
		frame.setSize(new Dimension(400, 300));
		frame.setVisible(true);
	}
}