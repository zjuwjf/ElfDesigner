package com.ielfgame.stupidGame.res;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.ielfgame.stupidGame.MainDesigner;
import com.ielfgame.stupidGame.ResJudge;
import com.ielfgame.stupidGame.dialog.MessageDialog;
import com.ielfgame.stupidGame.power.PowerMan;
import com.ielfgame.stupidGame.utils.FileHelper;

public class ImageWindow {
	private Shell mShell;

	public synchronized void open(final String imagePath, Composite comp) {
		dispose(mShell);
		mShell = null;
		
		if (ResJudge.isLegalResNotExisted(imagePath)) {
			MessageDialog md = new MessageDialog();
			md.open("Warning!", "" + imagePath + " has not existed already!");
			
			return;
		}

		final Shell shell = new Shell(PowerMan.getSingleton(MainDesigner.class).getShell(), 0);
		shell.setLayout(new FillLayout());

		final Display display = shell.getDisplay();
		final Image imageView = new Image(display, imagePath);

		final Label label = new Label(shell, SWT.BORDER);
		label.setImage(imageView);

		shell.pack();
		shell.open();
		
		shell.setLocation(display.getPrimaryMonitor().getClientArea().width/2 - shell.getSize().x/2, display.getPrimaryMonitor().getClientArea().height - shell.getSize().y);
		
		shell.setText("" + imageView.getBounds().width + "*" + imageView.getBounds().height + " - " + FileHelper.getSimpleNameWithSuffix(imagePath));
		
		mShell = shell;
		
		comp.setFocus();
		comp.forceFocus();
		
		display.timerExec(1000, new Runnable() {
			public void run() {
				dispose(shell);
			}
		});

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	private void dispose(final Shell shell) {
		if (shell != null) {
			try {
				shell.dispose();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private final static ImageWindow sImageWindow = new ImageWindow();

	public static ImageWindow getSingleton() {
		return sImageWindow;
	}

}
