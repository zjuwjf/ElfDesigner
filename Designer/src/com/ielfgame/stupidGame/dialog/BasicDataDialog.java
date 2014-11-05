package com.ielfgame.stupidGame.dialog;

import java.lang.reflect.Array;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ST;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ExpandEvent;
import org.eclipse.swt.events.ExpandListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Shell;

import com.ielfgame.stupidGame.MainDesigner;
import com.ielfgame.stupidGame.data.Stringified;
import com.ielfgame.stupidGame.data.Stringified.ErrorStruct;
import com.ielfgame.stupidGame.platform.PlatformHelper;
import com.ielfgame.stupidGame.power.PowerMan;

/**
 * @author zju_wjf
 *
 * @param <T>
 */
public class BasicDataDialog <T> implements IDialog<T>{
	
	protected StyledText mStyledText;
	protected StyledText mError;
	protected T mReturn = null;
	protected Shell mMyShell;
	
	protected final String mTitle;
	
	protected final boolean mIsEditable;
	
	protected String mYesText = "OK";
	protected String mNoText = "Cancel";
	
	public String getYesText() {
		return mYesText;
	}
	public void setYesText(String mYesText) {
		this.mYesText = mYesText;
	}
	public String getNoText() {
		return mNoText;
	}
	public void setNoText(String mNoText) {
		this.mNoText = mNoText;
	}

	public BasicDataDialog(String title, boolean isEditable) {
		mTitle = title;
		mIsEditable = isEditable;
	}
	
	String valueToText(final T value, Class<?> _classType) { 
		return Stringified.toText(value, false); 
	} 
	
	public T open(T value, Class<?> _classType) { 
		createComposites(value, _classType);
		return mReturn; 
	} 
	
	void createComposites(final T value, final Class<?> _classType) {
		final Shell oldShell = PowerMan.getSingleton(MainDesigner.class).mShell;
		mMyShell = new Shell(oldShell);
//		RunState.initChildShell(mMyShell);
		
		mMyShell.setLayout(new GridLayout());
		
//		final String 
		if(value == null) {
			String append = " ";
			if(_classType.isArray()) {
				append = String.format(" (%d)", 0);
			}
			if(!mIsEditable) { 
				mMyShell.setText("View "+mTitle+" " + _classType.getSimpleName() + append);
			} else { 
				mMyShell.setText("Modifier "+mTitle+" " + _classType.getSimpleName() + append);
			} 
		} else {
			String append = " ";
			if(value.getClass().isArray()) {
				try {
					append = String.format(" (%d)", Array.getLength(value));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(!mIsEditable) { 
				mMyShell.setText("View "+mTitle+" "+value.getClass().getSimpleName() + append);
			} else { 
				mMyShell.setText("Modifier "+mTitle+" "+value.getClass().getSimpleName() + append);
			} 
		}
		
		//text input
		Composite composite = new Composite(mMyShell, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		mStyledText = new StyledText(composite, SWT.V_SCROLL | SWT.MULTI | SWT.BORDER | SWT.WRAP);
		mStyledText.setSize(600, 200);
		mStyledText.setEditable(mIsEditable);
		mStyledText.setKeyBinding('A' | PlatformHelper.CTRL, ST.SELECT_ALL);
		
		mStyledText.setText( valueToText(value, _classType) );
		
		mStyledText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) { 
				mStyledText.setStyleRange(null); 
			} 
		}); 
		
		if(mIsEditable) {
			final ExpandBar expandBar = new ExpandBar(mMyShell, SWT.NONE);
			expandBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_CENTER));
			
			final ExpandItem expandItem = new ExpandItem(expandBar, SWT.NONE, 0);
			expandItem.setText("Check Error");
			
			expandBar.addExpandListener(new ExpandListener() {
				public void itemExpanded(ExpandEvent e) {
					expandItem.setExpanded(true);
					mMyShell.pack();
					mMyShell.layout();
					
					expandItem.setExpanded(false);
					expandBar.layout();
				} 
				public void itemCollapsed(ExpandEvent e) {
					expandItem.setExpanded(false);
					mMyShell.pack();
					mMyShell.layout();
					
					expandItem.setExpanded(true);
					expandBar.layout();
				}
			});
			
			mError = new StyledText(expandBar, SWT.BORDER | SWT.MULTI  | SWT.WRAP | SWT.V_SCROLL );
			mError.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			mError.setEditable(false); 
			mError.setForeground(new Color(null, 127, 0, 85));
			expandItem.setControl(mError);
			expandItem.setHeight(80);
			expandItem.setExpanded(true);
		} 
		
		final Composite composite2 = new Composite(mMyShell, SWT.NONE);
		composite2.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		GridLayout layout = new GridLayout();
		layout.numColumns = mIsEditable ? 2 : 1;
		composite2.setLayout(layout);
		
		Button okButton = new Button(composite2, SWT.PUSH);
		okButton.setText(mYesText);
		okButton.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("unchecked")
			public void widgetSelected(SelectionEvent e) { 
				try {
					if(mIsEditable) {
						final String text = mStyledText.getText(); 
						final Object [] ret = Stringified.fromText(_classType, text);
						final Object ObjRet = ret[0];
						final ErrorStruct errorRet = (ErrorStruct)ret[1];
						
						if(errorRet != null) {
							try {
								final StyleRange styleRange = new StyleRange(errorRet.beginPos, errorRet.endPos-errorRet.beginPos, new Color(null, 255,0,0), null, SWT.BOLD);
								mStyledText.setStyleRange(styleRange);
							} catch (Exception exception) {
								exception.printStackTrace();
							} 
							
							mError.setText("");
							mError.append("Error info: "+errorRet.errorInfo+"\n");
							mError.append("Error type: "+errorRet.errorType+"\n");
							mError.append("Error pos : ["+errorRet.beginPos+","+errorRet.endPos+")\n");
						} else {
							mReturn = (T)ObjRet;
							mMyShell.close();
						}
					} else {
						mMyShell.close();
					} 
				} catch (Exception exception) {
				} 
			} 
		}); 

		if(mIsEditable) { 
			Button cancelButton = new Button(composite2, SWT.PUSH);
			cancelButton.setText(mNoText);
			cancelButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) { 
					mReturn = null;
					mMyShell.close();
				} 
			});
			mMyShell.setDefaultButton(okButton);
		} 
		
		mMyShell.pack();
		mMyShell.setLocation(mMyShell.getDisplay().getPrimaryMonitor().getClientArea().width / 2 - mMyShell.getSize().x/2, 
				mMyShell.getDisplay().getPrimaryMonitor().getClientArea().height/2 - mMyShell.getSize().y/2);
		
		mMyShell.open();
		Display display = mMyShell.getDisplay();
		while (!mMyShell.isDisposed()) { 
			if (!display.readAndDispatch())
				display.sleep();
		} 
		
		try {
			if(mMyShell != null && !mMyShell.isDisposed()) {
				mMyShell.dispose();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mMyShell = null;
	} 
	
	public boolean isValid() {
		return mMyShell != null && !mMyShell.isDisposed();
	}
	
	public StyledText getStyledText() {
		return mStyledText;
	} 
} 
