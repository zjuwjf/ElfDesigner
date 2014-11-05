package com.ielfgame.stupidGame.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

import com.ielfgame.stupidGame.MainDesigner;
import com.ielfgame.stupidGame.power.PowerMan;

public class QuitPanel {
	public boolean open() {
		MessageBox box = new MessageBox(PowerMan.getSingleton(MainDesigner.class).mShell, SWT.ICON_INFORMATION
				| SWT.OK | SWT.CANCEL);
		box.setText("Quit");
		box.setMessage("Are you sure?");
		return SWT.OK == box.open();
	} 
} 
