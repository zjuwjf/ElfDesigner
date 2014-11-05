package com.ielfgame.stupidGame.swing;

/*
 This program is a part of the companion code for Core Java 8th ed.
 (http://horstmann.com/corejava)

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * This program demonstrates different stroke types.
 * 
 * @version 1.03 2007-08-16
 * @author Cay Horstmann
 */
public class StrokeTest {
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new StrokeTestFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}
}

/**
 * This frame lets the user choose the cap, join, and line style, and shows the
 * resulting stroke.
 */
class StrokeTestFrame extends JFrame {
	public StrokeTestFrame() {
		setTitle("StrokeTest");
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

		canvas = new StrokeComponent();
		add(canvas, BorderLayout.CENTER);

		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(3, 3));
		add(buttonPanel, BorderLayout.NORTH);

		ButtonGroup group1 = new ButtonGroup();
		makeCapButton("Butt Cap", BasicStroke.CAP_BUTT, group1);
		makeCapButton("Round Cap", BasicStroke.CAP_ROUND, group1);
		makeCapButton("Square Cap", BasicStroke.CAP_SQUARE, group1);

		ButtonGroup group2 = new ButtonGroup();
		makeJoinButton("Miter Join", BasicStroke.JOIN_MITER, group2);
		makeJoinButton("Bevel Join", BasicStroke.JOIN_BEVEL, group2);
		makeJoinButton("Round Join", BasicStroke.JOIN_ROUND, group2);

		ButtonGroup group3 = new ButtonGroup();
		makeDashButton("Solid Line", false, group3);
		makeDashButton("Dashed Line", true, group3);
	}

	/**
	 * Makes a radio button to change the cap style.
	 * 
	 * @param label
	 *            the button label
	 * @param style
	 *            the cap style
	 * @param group
	 *            the radio button group
	 */
	private void makeCapButton(String label, final int style, ButtonGroup group) {
		// select first button in group
		boolean selected = group.getButtonCount() == 0;
		JRadioButton button = new JRadioButton(label, selected);
		buttonPanel.add(button);
		group.add(button);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				canvas.setCap(style);
			}
		});
	}

	/**
	 * Makes a radio button to change the join style.
	 * 
	 * @param label
	 *            the button label
	 * @param style
	 *            the join style
	 * @param group
	 *            the radio button group
	 */
	private void makeJoinButton(String label, final int style, ButtonGroup group) {
		// select first button in group
		boolean selected = group.getButtonCount() == 0;
		JRadioButton button = new JRadioButton(label, selected);
		buttonPanel.add(button);
		group.add(button);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				canvas.setJoin(style);
			}
		});
	}

	/**
	 * Makes a radio button to set solid or dashed lines
	 * 
	 * @param label
	 *            the button label
	 * @param style
	 *            false for solid, true for dashed lines
	 * @param group
	 *            the radio button group
	 */
	private void makeDashButton(String label, final boolean style, ButtonGroup group) {
		// select first button in group
		boolean selected = group.getButtonCount() == 0;
		JRadioButton button = new JRadioButton(label, selected);
		buttonPanel.add(button);
		group.add(button);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				canvas.setDash(style);
			}
		});
	}

	private StrokeComponent canvas;
	private JPanel buttonPanel;

	private static final int DEFAULT_WIDTH = 400;
	private static final int DEFAULT_HEIGHT = 400;
}

/**
 * This component draws two joined lines, using different stroke objects, and
 * allows the user to drag the three points defining the lines.
 */
class StrokeComponent extends JComponent {
	public StrokeComponent() {
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent event) {
				Point p = event.getPoint();
				for (int i = 0; i < points.length; i++) {
					double x = points[i].getX() - SIZE / 2;
					double y = points[i].getY() - SIZE / 2;
					Rectangle2D r = new Rectangle2D.Double(x, y, SIZE, SIZE);
					if (r.contains(p)) {
						current = i;
						return;
					}
				}
			}

			public void mouseReleased(MouseEvent event) {
				current = -1;
			}
		});

		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent event) {
				if (current == -1)
					return;
				points[current] = event.getPoint();
				repaint();
			}
		});

		points = new Point2D[3];
		points[0] = new Point2D.Double(200, 100);
		points[1] = new Point2D.Double(100, 200);
		points[2] = new Point2D.Double(200, 200);
		current = -1;
		width = 8.0F;
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		GeneralPath path = new GeneralPath();
		path.moveTo((float) points[0].getX(), (float) points[0].getY());
		for (int i = 1; i < points.length; i++)
			path.lineTo((float) points[i].getX(), (float) points[i].getY());
		BasicStroke stroke;
		if (dash) {
			float miterLimit = 10.0F;
			float[] dashPattern = { 10F, 10F, 10F, 10F, 10F, 10F, 30F, 10F, 30F, 10F, 30F, 10F, 10F, 10F, 10F, 10F, 10F, 30F };
			float dashPhase = 0;
			stroke = new BasicStroke(width, cap, join, miterLimit, dashPattern, dashPhase);
		} else
			stroke = new BasicStroke(width, cap, join);
		g2.setStroke(stroke);
		g2.draw(path);
	}

	/**
	 * Sets the join style.
	 * 
	 * @param j
	 *            the join style
	 */
	public void setJoin(int j) {
		join = j;
		repaint();
	}

	/**
	 * Sets the cap style.
	 * 
	 * @param c
	 *            the cap style
	 */
	public void setCap(int c) {
		cap = c;
		repaint();
	}

	/**
	 * Sets solid or dashed lines
	 * 
	 * @param d
	 *            false for solid, true for dashed lines
	 */
	public void setDash(boolean d) {
		dash = d;
		repaint();
	}

	private Point2D[] points;
	private static int SIZE = 10;
	private int current;
	private float width;
	private int cap;
	private int join;
	private boolean dash;
}
