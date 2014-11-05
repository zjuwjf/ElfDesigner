package com.ielfgame.stupidGame.fileBar;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

import com.ielfgame.stupidGame.Constants;
import com.ielfgame.stupidGame.MainDesigner;
import com.ielfgame.stupidGame.power.PowerMan;
import com.ielfgame.stupidGame.res.ResManager;
import com.ielfgame.stupidGame.trans.TransferRes;
import com.ielfgame.stupidGame.xml.XMLVersionManage;

public class CopyPanel {
	
	public LinkedList<String> fakeOpen(final String path) {
		final HashMap<String, String> map = ResManager.getName2PathMap();
		
		final LinkedList<String> notFoundList = new LinkedList<String>();
		
		final Set<String> res = XMLVersionManage.getAllResids(path); 
		for(String resid : res) { 
			if(resid != null && resid.startsWith("@")) {
				resid = resid.substring(1);
			}
			
			final String fullPath = map.get(resid);
			if(fullPath != null) {
				final File file = new File(fullPath); 
				if(!file.exists()) { 
					notFoundList.add(resid); 
				} 
			} else {
				notFoundList.add(resid); 
			}
		} 
		
		return notFoundList;
	}
	
	public LinkedList<String> open(final String title, final Collection<String> set) {
		final LinkedList<String> notFoundList = new LinkedList<String>();
		final Shell shell = new Shell(PowerMan.getSingleton(MainDesigner.class).mShell, SWT.DIALOG_TRIM | SWT.PRIMARY_MODAL);
		final ArrayList<String> res = new ArrayList<String>(set);
		
//		RunState.initChildShell(shell);
		shell.setLayout(new GridLayout());
		shell.setText("Transfer Resource-" + title);
		
		final Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);
		
		final GridData gridData0 = new GridData(GridData.FILL_HORIZONTAL);
		gridData0.widthHint = 500;
		composite.setLayoutData(gridData0);
		
		final Label leftLabel = new Label(composite, SWT.LEFT);
		leftLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		leftLabel.setText("transfer...");
		
		final Label rightLabel = new Label(composite, SWT.RIGHT); 
		
		final String destPath = ResManager.getSingleton().getXCodeImageAsset();
		
		rightLabel.setText("to "+destPath); 
		
		final Composite composite1 = new Composite(shell, SWT.NONE);
		composite1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		layout = new GridLayout();
		layout.numColumns = 2;
		composite1.setLayout(layout);
		
		final ProgressBar pb1 = new ProgressBar(composite1, SWT.HORIZONTAL | SWT.SMOOTH);
		pb1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		pb1.setMinimum(0);
		pb1.setSelection(0);
		pb1.setMaximum(res.size());
		
		final Button cancelButton = new Button(composite1, SWT.PUSH);
		cancelButton.setText(Constants.POP_DIALOG_CANCEL);
		cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		
		
		final Thread thread = new Thread(new Runnable() {
			private int mLastCount = -1;
			public void run() {
				for(int i=0; i<res.size(); i++) {
					if(shell == null || shell.isDisposed() || leftLabel.isDisposed()) {
						break;
					} 
					if(mLastCount != i) {
						mLastCount = i;
						try { 
							Thread.sleep(5); 
						} catch (Exception e) { 
						} 
						
						TransferRes.copyRes(res.get(i), destPath, false, notFoundList); 
						
						shell.getDisplay().asyncExec(new Runnable() { 
							public void run() { 
								leftLabel.setText("Transfer "+ res.get(mLastCount));
								pb1.setSelection(mLastCount+1); 
							}
						});
					}
				}
				
				shell.getDisplay().asyncExec(new Runnable() {
					public void run() {
						shell.close();
					}
				});
			}
		});
		
		thread.start();
		
		shell.pack();
//		shell.setLocation(shell.getDisplay().getClientArea().width / 2 - shell.getSize().x/2, 
//				shell.getDisplay().getClientArea().height/2 - shell.getSize().y/2);
		
		shell.open();
		Display display = shell.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		} 
		
		//sort
		Collections.sort(notFoundList);
		
		return notFoundList;
	}
	
	public LinkedList<String> open(final String title) {
		final Set<String> set = XMLVersionManage.getAllResids(title);
		return open(title, set);
	} 
}
