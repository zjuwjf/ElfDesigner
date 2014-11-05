package com.ielfgame.stupidGame.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.ielfgame.stupidGame.MainDesigner;
import com.ielfgame.stupidGame.power.PowerMan;

public class MessageDialog { 
	private final Shell mShell;
	public MessageDialog() {
		this(PowerMan.getSingleton(MainDesigner.class).getShell());
	}
	
	public MessageDialog(Shell shell) {
		mShell = shell;
	} 
	
	public boolean open(String tile, String text) {
		final Shell shell = mShell;
		final MessageBox box = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK | SWT.CANCEL );
		box.setText(""+tile);
		box.setMessage(""+text);
		
		return box.open() == SWT.OK;
	} 
	
	public boolean open(String tile, String text, int flag) {
		final Shell shell = mShell;
		final MessageBox box = new MessageBox(shell, flag);
		box.setText(""+tile);
		box.setMessage(""+text);
		
		return box.open() == SWT.OK;
	} 
	
	public boolean open(String tile, String text, String left, String right) {
		final Shell shell = PowerMan.getSingleton(MainDesigner.class).mShell;
		final MessageBox box = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK | SWT.CANCEL);
		box.setText(""+tile);
		box.setMessage(""+text);
		
		return box.open() == SWT.OK;
	}
}
