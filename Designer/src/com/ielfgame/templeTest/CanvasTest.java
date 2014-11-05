package com.ielfgame.templeTest;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;

public class CanvasTest {
	
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		final Point origin = new Point(0, 0);
		final Canvas canvas = new Canvas(shell, SWT.V_SCROLL | SWT.H_SCROLL);
		final int width = 500;
		final int height = 100;
		
//		canvas.a
		
		final ScrollBar hBar = canvas.getHorizontalBar();
		final Color red = new Color(null, 255, 0, 0);
		hBar.setMaximum(width);
		hBar.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				System.out.println("HBar listener");
				int hSelection = hBar.getSelection();
				int destX = -hSelection - origin.x;
				canvas.scroll(destX, 0, 0, 0, width, height, true);
				origin.x = -hSelection;
				System.out.println("HBar listener exit");
			}
		});
		final ScrollBar vBar = canvas.getVerticalBar();
		vBar.setMaximum(height);
		vBar.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				int vSelection = vBar.getSelection();
				int destY = -vSelection - origin.y;
				canvas.scroll(0, destY, 0, 0, width, height, true);
				origin.y = -vSelection;
			}
		});
		canvas.addListener(SWT.Paint, new Listener() {
			public void handleEvent(Event e) {
				System.out.println("Paint listener");
				GC gc = e.gc;
				gc.setBackground(red);
				gc.fillRectangle(origin.x, origin.y, width, 100);
				gc.drawText("test", origin.x, origin.y);
			}
		});
		shell.setSize(500, 150);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
