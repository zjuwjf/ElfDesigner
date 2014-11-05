package com.ielfgame.stupidGame.swing;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class TextLayoutStrikeout {

	public static void main(String[] args) {
		Display display = new Display();
		final Shell shell = new Shell(display, SWT.SHELL_TRIM | SWT.DOUBLE_BUFFERED);
		shell.setText("Underline, Strike Out");
		Font font = shell.getFont();
		String text = "text.text.text.";
		final TextLayout layout = new TextLayout(display);
		layout.setText(text);
		TextStyle style1 = new TextStyle(font, null, null);
		style1.strikeout = true;
		style1.strikeoutColor = new Color(null, 255,0,0);
		layout.setStyle(style1, 3, 5);

		shell.addListener(SWT.Paint, new Listener() {
			public void handleEvent(Event event) {
				Point point = new Point(10, 10);
				int width = shell.getClientArea().width - 2 * point.x;
				layout.setWidth(width);
				layout.draw(event.gc, point.x, point.y);
			}
		});

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		layout.dispose();
		display.dispose();
	}
}
