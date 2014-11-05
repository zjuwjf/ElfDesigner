package com.ielfgame.stupidGame;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class MakeSurePanel {
	
	boolean mReturn = false;
	private final Shell mShell;
	private final String mTips;
	public MakeSurePanel(Shell parent, String tips){
		mShell = new Shell(parent, SWT.DIALOG_TRIM | SWT.PRIMARY_MODAL);
//		RunState.initChildShell(mShell);
		mShell.setLayout(new GridLayout());
		
		mTips = tips;
	}
	
	public boolean open(){
		createTextWidgets();
		createControlButtons();
		
		mShell.pack();
		mShell.setLocation(mShell.getDisplay().getPrimaryMonitor().getClientArea().width / 2 - mShell.getSize().x/2, 
				mShell.getDisplay().getPrimaryMonitor().getClientArea().height/2 - mShell.getSize().y/2);
		
		mShell.open();
		Display display = mShell.getDisplay();
		while (!mShell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		
		return mReturn;
	}
	
	private void createControlButtons() {
		Composite composite = new Composite(mShell, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);
		
		Button okButton = new Button(composite, SWT.PUSH);
		okButton.setText(Constants.POP_DIALOG_OK);
		okButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				mReturn = true;
				mShell.close();
			}
		});

		Button cancelButton = new Button(composite, SWT.PUSH);
		cancelButton.setText(Constants.POP_DIALOG_CANCEL);
		cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) { 
				mReturn = false;
				mShell.close();
			} 
		});

		mShell.setDefaultButton(okButton);
	}
	
	private void createTextWidgets() {
		Composite composite = new Composite(mShell, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		composite.setLayout(layout);

		Label label = new Label(composite, SWT.RIGHT);
		label.setText(mTips);
	}
}
