package com.ielfgame.stupidGame.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.Hashtable;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class LineBreakSample extends JPanel {

	private LineBreakMeasurer lineMeasurer;

	// the first character in the paragraph.
	private int paragraphStart;

	// the first character after the end of the paragraph.
	private int paragraphEnd;

	private static final Hashtable map = new Hashtable();
	static {
		map.put(TextAttribute.SIZE, new Float(18.0));
	}

	private static final String key = "中文.\n恭喜获得.Many people believe that Vincent van Gogh painted his best works " + "during the two-year period he spent in Provence. Here is where he " + "painted The Starry Night--which some consider to be his greatest "
			+ "work of all. \n\n However, as his artistic brilliance reached new heights " + "in Provence, his physical and mental health plummeted. ";
	
	private static AttributedString vanGogh = new AttributedString(key, map);
	
	public LineBreakSample() {
		AttributedCharacterIterator paragraph = vanGogh.getIterator();
		paragraphStart = paragraph.getBeginIndex();
		paragraphEnd = paragraph.getEndIndex();

		// Create a new LineBreakMeasurer from the paragraph.
		lineMeasurer = new LineBreakMeasurer(paragraph, new FontRenderContext(null, false, false));
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(Color.white);
		
		Graphics2D graphics2D = (Graphics2D) g;
		// Set formatting width to width of Component.
		Dimension size = getSize();
		float formatWidth = (float) size.width;

		float drawPosY = 0;

		lineMeasurer.setPosition(paragraphStart);

		// Get lines from lineMeasurer until the entire
		// paragraph has been displayed.
		while (lineMeasurer.getPosition() < paragraphEnd) {
			int next = lineMeasurer.nextOffset(formatWidth);
			int limit = next;
			if (limit < key.length()) {
			   for (int i = lineMeasurer.getPosition(); i < next; ++i) {
			      char c = key.charAt(i);
			      if (c == '\n') {
			         limit = i;
			         break;
			      }
			   }
			}

			final TextLayout layout = lineMeasurer.nextLayout(formatWidth, limit+1, false);

			// Retrieve next layout.
//			TextLayout layout = lineMeasurer.nextLayout(formatWidth, 1000, false);
			// Move y-coordinate by the ascent of the layout.
			drawPosY += layout.getAscent();

			// Compute pen x position. If the paragraph is
			// right-to-left, we want to align the TextLayouts
			// to the right edge of the panel.
			float drawPosX;
			if (layout.isLeftToRight()) {
				drawPosX = 0;
			} else {
				drawPosX = formatWidth - layout.getAdvance();
			}

			// Draw the TextLayout at (drawPosX, drawPosY).
			layout.draw(graphics2D, drawPosX, drawPosY);

			// Move y-coordinate in preparation for next layout.
			drawPosY += layout.getDescent() + layout.getLeading();
		}
	}

	public static void main(String[] args) {
		JFrame f = new JFrame("");

		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		LineBreakSample controller = new LineBreakSample();
		f.getContentPane().add(controller, "Center");
		f.setSize(new Dimension(400, 250));
		f.setVisible(true);
	}
}
