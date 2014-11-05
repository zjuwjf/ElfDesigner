package com.ielfgame.stupidGame.splash;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class SwtDraw {
	private static Display display;
	private static Shell s;
	private Canvas c;
	private int n = 300;
	private boolean run = true;

	public void start() {
		display = Display.getDefault();
		Image img = new Image(display, "icon/splash.jpg");
		s = new Shell(display, SWT.SHELL_TRIM);
		s.setSize(430, 305);
		c = new Canvas(s, SWT.NO_REDRAW_RESIZE);
		s.setLayout(new FillLayout());
		c.setBackgroundImage(img);
		c.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent pe) {
				GC g = pe.gc;
				g.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
				Font t = new Font(display, "55", 10, 50);
				g.setFont(t);
				g.drawString(Integer.toString(n), 20, 260, true);
				// g.drawString("正在刷新显示", 20, 420, true);
			}
		});
		s.open();
		while (!s.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	public void flush() {
		Runnable runnable = new Runnable() {
			public void run() {
				while (run) {
					try {
						Thread.sleep(100);
						if (n > 0) {
							n--;
						} else {
							run = false;
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					display.asyncExec(new Runnable() {
						public void run() {
							c.redraw();
						}
					});
				}
			}
		};
		// 建立线程
		Thread t = new Thread(runnable);
		t.setDaemon(true);
		t.start();
	}

	public static void main(String args[]) {
		SwtDraw t = new SwtDraw();
		t.flush();
		t.start();
	}
}
