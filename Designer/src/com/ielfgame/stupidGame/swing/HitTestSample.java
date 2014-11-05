package com.ielfgame.stupidGame.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextHitInfo;
import java.awt.font.TextLayout;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

/**
 * This class demonstrates how to hit-test a TextLayout.  Hit-testing
 * is the mapping of a graphical location to a character position within
 * text.
 *
 * This class constructs a TextLayout from an AttributedCharacterIterator
 * and displays the TextLayout.  When the mouse is clicked inside this
 * Component, the mouse position is mapped to a character position, and the
 * carets for this character position are displayed.
 */
public class HitTestSample extends JPanel {
    // The TextLayout to draw and hit-test.
    private TextLayout textLayout;
    
    // The insertion index of the most recent mouse click.
    private int insertionIndex;
    
    RenderingHints rh;
    
    public HitTestSample() {
        rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
        // Initilize insertionIndex.
        insertionIndex = 0;
        addMouseListener(new HitTestMouseListener());
    }
    
    /**
     * Compute a location within this Component for textLayout's origin,
     * such that textLayout is centered horizontally and vertically.
     *
     * Note that this location is unknown to textLayout;  it is used only
     * by this Component for positioning.
     */
    private Point2D computeLayoutOrigin() {
        Dimension size = getSize();
        Point2D.Float origin = new Point2D.Float();
	
        origin.x = (float) (size.width - textLayout.getAdvance()) / 2;
        origin.y = (float) (size.height - textLayout.getDescent() + textLayout.getAscent()) / 2;
	
        return origin;
    }
    
    /**
     * Draw textLayout and the carets corresponding to the most recent
     * mouse click (if any).
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHints(rh);
	
        // Create a new TextLayout.
        textLayout = new TextLayout("Blott en dag, ett 枚gonblick...",
				    new Font("Times New Roman", Font.BOLD, 45),
				    g2.getFontRenderContext());
	
        Point2D origin = computeLayoutOrigin();
	
        // Since the caret Shapes are relative to the origin 
        // of textLayout, we'll translate the graphics so that 
        // the origin of the graphics is where we want textLayout
        // to appear.
	
        g2.translate(origin.getX(), origin.getY());
	
        // Draw textLayout.
        textLayout.draw(g2, 0, 0);
	
        // Retrieve caret Shapes for insertionIndex.
        Shape[] carets = textLayout.getCaretShapes(insertionIndex);
	
        // Draw the carets.  carets[0] is the strong caret, and
        // is never null.  carets[1], if it is not null, is the
        // weak caret.
        g2.setColor(Color.blue);
        //g2.draw(carets[0]);   //Draw the predefined caret.
        g2.fill(new Ellipse2D.Double(carets[0].getBounds().getX(), 
				     carets[0].getBounds().getY(), 
				     7, 50)); //Draw a costum caret.
	
        if (carets[1] != null) {
            g2.setColor(Color.red);
            g2.draw(carets[1]);
        }
    }
    
    private class HitTestMouseListener extends MouseAdapter {
	
        /**
         * Compute the character position of the mouse click.
         */
        public void mouseClicked(MouseEvent e) {
            Point2D origin = computeLayoutOrigin();
	    
            // Compute the mouse click location relative to
            // textLayout's origin.
            float clickX = (float) (e.getX() - origin.getX());
            float clickY = (float) (e.getY() - origin.getY());
	    
            // Get the character position of the mouse click.
            TextHitInfo currentHit = textLayout.hitTestChar(clickX, clickY);
            insertionIndex = currentHit.getInsertionIndex();
	    
            // Repaint the Component so the new caret(s) will be displayed.
            repaint();
        }
    }
    
    public static void main(String[] args) {
	JFrame f = new JFrame("HitTestSample");
	f.setContentPane(new HitTestSample());
	f.setSize(700, 300);
	f.setVisible(true);
	f.addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
		    System.exit(0);
		}
	    });
    }
    
}
