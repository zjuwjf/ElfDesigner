package com.ielfgame.stupidGame.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

import com.ielfgame.stupidGame.Constants;
import com.ielfgame.stupidGame.MainDesigner;
import com.ielfgame.stupidGame.power.PowerMan;

public class ProgressDialog {
	
	public static interface IProgress {
		public void onStart();
		public void onClose();
	}
	
	private Shell mShell;
	private Label mLeftLabel, mRightLabel;
	private ProgressBar mProgressBar;
	private IProgress mListener;
	
//	public Shell getShell() {
//		return mShell;
//	}
	
	public void open(Shell shell, String title) { 
		mShell = shell;
		
		mShell.setLayout(new GridLayout());
		mShell.setText(title);
		
		final Composite composite = new Composite(mShell, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		composite.setLayout(layout);
		
		final GridData gridData0 = new GridData(GridData.FILL_HORIZONTAL);
		gridData0.widthHint = 500;
		composite.setLayoutData(gridData0);
		
		mLeftLabel = new Label(composite, SWT.LEFT);
		mLeftLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		mRightLabel = new Label(composite, SWT.RIGHT); 
		mRightLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		final Composite composite1 = new Composite(mShell, SWT.NONE);
		composite1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		layout = new GridLayout();
		layout.numColumns = 2;
		composite1.setLayout(layout);
		
		mProgressBar = new ProgressBar(composite1, SWT.HORIZONTAL | SWT.SMOOTH);
		mProgressBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		mProgressBar.setMinimum(0);
		mProgressBar.setSelection(0);
		mProgressBar.setMaximum(100);
		
		final Button cancelButton = new Button(composite1, SWT.PUSH);
		cancelButton.setText(Constants.POP_DIALOG_CANCEL);
		cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if(mListener != null) {
					mListener.onClose();
				}
				
				mShell.close(); 
				mShell = null;
			} 
		});
		
		mShell.pack();
		mShell.setLocation(mShell.getDisplay().getPrimaryMonitor().getClientArea().width / 2 - mShell.getSize().x/2, 
				mShell.getDisplay().getPrimaryMonitor().getClientArea().height/2 - mShell.getSize().y/2);
		
		mShell.addListener(SWT.Close, new Listener() {
			public void handleEvent(Event event) {
				if(mListener != null) { 
					mListener.onClose();
				} 
			}
		});
		
		if(mListener != null) { 
			mListener.onStart();
		} 
		
		mShell.open();
		
		Display display = mShell.getDisplay();
		while (mShell != null && !mShell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		} 
	}
	
	public void open(String title) { 
		final Shell shell = new Shell(PowerMan.getSingleton(MainDesigner.class).getShell(), SWT.DIALOG_TRIM | SWT.PRIMARY_MODAL);
		this.open(shell, title);
	} 
	
	public void setListener(IProgress listener) {
		mListener = listener;
	}
	
	public void setProgress(final String contextLeft, final String contextRight, final int index, final int size) {
		if(mShell != null) {
			mShell.getDisplay().asyncExec(new Runnable() { 
				public void run() { 
					setProgressDirectly(contextLeft, contextRight, index, size);
				}
			});
		}
	}
	
	public void setProgressDirectly(final String contextLeft, final String contextRight, final int index, final int size) {
		try {
			mLeftLabel.setText(contextLeft);
			mRightLabel.setText(contextRight);
			mProgressBar.setSelection(index);
			mProgressBar.setMaximum(size);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void close() { 
		if(mShell != null) { 
			mShell.getDisplay().asyncExec(new Runnable() { 
				public void run() { 
					mShell.close(); 
					mShell = null;
				} 
			});
		} 
	} 
} 
