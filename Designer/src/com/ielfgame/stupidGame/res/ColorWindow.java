package com.ielfgame.stupidGame.res;

import java.awt.Robot;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.colorspy.main.MainWindow;
import com.colorspy.mouse.LowLevelMouseProc;
import com.colorspy.mouse.MOUSEHOOKSTRUCT;
import com.ielfgame.stupidGame.MainDesigner;
import com.ielfgame.stupidGame.data.ElfColor;
import com.ielfgame.stupidGame.power.PowerMan;
import com.sun.jna.examples.win32.Kernel32;
import com.sun.jna.examples.win32.User32;
import com.sun.jna.examples.win32.User32.HHOOK;
import com.sun.jna.examples.win32.W32API.HMODULE;
import com.sun.jna.examples.win32.W32API.LRESULT;
import com.sun.jna.examples.win32.W32API.WPARAM;

public class ColorWindow {
	
	public static final int WM_MOUSEMOVE = 512;
	public static final int WM_LBUTTONDOWN = 513;
	public static final int WM_LBUTTONUP = 514;
	public static final int WM_RBUTTONDOWN = 516;
	public static final int WM_RBUTTONUP = 517;
	public static final int WM_MBUTTONDOWN = 519;
	public static final int WM_MBUTTONUP = 520;
	private static HHOOK hhk;
	private static LowLevelMouseProc mouseHook;
	private final User32 lib = User32.INSTANCE;
	
	private Label mLabel;
	
	private ElfColor mPickColor = null;

	public ElfColor open() {

		final Shell shell = new Shell(PowerMan.getSingleton(MainDesigner.class).getShell(), 0);
		final GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		shell.setLayout(layout);
		
		final Display display = shell.getDisplay();
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		mLabel = new Label(composite, SWT.BORDER);
		mLabel.setSize(50, 50);
		
		shell.pack();
		shell.open();
		
		try {
			final Robot robot = new Robot();

			final HMODULE hMod = Kernel32.INSTANCE.GetModuleHandle(null);
			mouseHook = new LowLevelMouseProc() {
				public LRESULT callback(int nCode, WPARAM wParam, MOUSEHOOKSTRUCT info) {
					if (nCode >= 0) {
						int x = info.pt.x;
						int y = info.pt.y;
						java.awt.Color color = robot.getPixelColor(x, y);
						
						final int r = color.getRed();
						final int g = color.getGreen();
						final int b = color.getBlue();
						
//						System.err.println("MouseHook");
						
						switch (wParam.intValue()) {
						case MainWindow.WM_LBUTTONDOWN:
							mPickColor = new ElfColor(r/255f, g/255f, b/255f, 1);
							
							synchronized (this) {
								if(hhk != null) { 
									lib.UnhookWindowsHookEx(hhk);
									hhk = null;
								} 
							}
							
							dispose();
							return null;
							
						case MainWindow.WM_MOUSEMOVE:
							break;
						case MainWindow.WM_MBUTTONDOWN:
							break;
						case MainWindow.WM_MBUTTONUP:
							break;
						}
						
						try {
							mLabel.getShell().setLocation(x-25, y-100);
							mLabel.setBackground(new Color(mLabel.getDisplay(),r,g,b));
//							mLabel.redraw();
						} catch (Exception e) {
							e.printStackTrace();
						} 
					} 
					return lib.CallNextHookEx(hhk, nCode, wParam, info.getPointer());
				}
			};
			hhk = lib.SetWindowsHookEx(User32.WH_MOUSE_LL, mouseHook, hMod, 0);
//			int result;
//			MSG msg = new MSG();
//			result = 
//			lib.GetMessage(msg, null, 512, 513);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		shell.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				synchronized (this) {
					if(hhk != null) { 
						lib.UnhookWindowsHookEx(hhk);
						hhk = null;
					} 
				}
			}
		});
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		
		return mPickColor;
	}
	
	public void setColor(final int r, final int g, final int b) {
		try {
			if(!mLabel.isDisposed()) {
				final Display display = mLabel.getDisplay();
				display.asyncExec(new Runnable() {
					public void run() {
						mLabel.setBackground(new Color(mLabel.getDisplay(),r,g,b));
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setPosition(final int x, final int y) {
		try {
			if(!mLabel.isDisposed()) {
				final Display display = mLabel.getDisplay();
				display.asyncExec(new Runnable() {
					public void run() {
						mLabel.getShell().setLocation(x, y);
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void dispose() {
		if(!mLabel.isDisposed()) {
			try {
				final Display display = mLabel.getDisplay();
				display.asyncExec(new Runnable() {
					public void run() {
						final Shell shell = mLabel.getShell();
						shell.dispose();
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
