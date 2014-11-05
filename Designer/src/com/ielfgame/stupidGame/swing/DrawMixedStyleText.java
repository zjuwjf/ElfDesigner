package com.ielfgame.stupidGame.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedString;

public class DrawMixedStyleText {

	public static void main(String[] args) {

		// Create a frame

		Frame frame = new Frame();

		// Add a component with a custom paint method

		frame.add(new CustomPaintComponent());

		// Display the frame

		int frameWidth = 300;

		int frameHeight = 300;

		frame.setSize(frameWidth, frameHeight);

		frame.setVisible(true);

	}

	/**
	 * 
	 * To draw on the screen, it is first necessary to subclass a Component and
	 * 
	 * override its paint() method. The paint() method is automatically called
	 * 
	 * by the windowing system whenever component's area needs to be repainted.
	 */
	static class CustomPaintComponent extends Component {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void paint(Graphics g) {

			// Retrieve the graphics context; this object is used to paint
			// shapes

			Graphics2D g2d = (Graphics2D) g;

			/**
			 * 
			 * The coordinate system of a graphics context is such that the
			 * 
			 * origin is at the northwest corner and x-axis increases toward the
			 * 
			 * right while the y-axis increases toward the bottom.
			 */

			int x = 0;

			int y = 20;

			// Set the desired font if different from default font

			Font font = new Font("Serif", Font.PLAIN, 26);

			// Apply styles to text

			AttributedString astr = new AttributedString("This is a test string");

			astr.addAttribute(TextAttribute.FONT, font, 0, 6);

			astr.addAttribute(TextAttribute.FOREGROUND, Color.RED, 2, 9);

			astr.addAttribute(TextAttribute.BACKGROUND, Color.CYAN, 10, 21);

			astr.addAttribute(TextAttribute.FOREGROUND, Paint.BITMASK, 3, 9);
			
//			astr.addAttribute(TextAttribute., value, beginIndex, endIndex)

			// Draw mixed-style text such that its base line is at x, y

			TextLayout tl = new TextLayout(astr.getIterator(), g2d.getFontRenderContext());

			tl.draw(g2d, x, y);

		}

	}

}
