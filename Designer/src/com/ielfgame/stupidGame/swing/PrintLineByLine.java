package com.ielfgame.stupidGame.swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class PrintLineByLine extends JPanel {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2d = (Graphics2D) g;

    RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);

    rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    
    rh.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);

    g2d.setRenderingHints(rh);
    
    g2d.setPaint(Color.RED);

    g2d.setFont(new Font("Purisa", Font.PLAIN, 13));

    g2d.drawString("Line 1", 20, 30);
    g2d.drawString("Line 2", 20, 60);
    g2d.drawString("Line 3", 20, 90);
    g2d.drawString("Line 4", 20, 120);
    g2d.drawString("Line 5", 20, 150);
    g2d.drawString("Line 6", 20, 180);
    
    
  }

  public static void main(String[] args) {
    JFrame frame = new JFrame();
    frame.add(new PrintLineByLine());
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(420, 250);
    frame.setVisible(true);
  }
}
