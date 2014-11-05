package com.ielfgame.stupidGame;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import com.ielfgame.stupidGame.config.FrameInterval;
import com.ielfgame.stupidGame.language.LanguageManager;
import com.ielfgame.stupidGame.power.PowerMan;

public abstract class AbstractWorkSpaceTab extends AbstractWorkSpace{
	
	protected CTabFolder mCTabFolder;
	protected CTabItem mTabItem;
	private String mText;
	
	private static final String KEY = "key";
	private static final String LAST_ITEM_KEY = "last_key";
	
	protected static boolean sRunning = true;
	public static void pauseUI() {
		sRunning = false;
	}
	public static void resumeUI() {
		sRunning = true;
	}
		
	public AbstractWorkSpaceTab(String text){
		mText = LanguageManager.get(text);
	}
	
	public void setText(String text){
		mText = text;
		if(mTabItem != null){ 
			mTabItem.setText(""+mText+"");
		}
		
//		mCTabFolder
	} 
	
	public CTabFolder getCTabFolder() {
		return mCTabFolder;
	}
	
	public CTabItem getCTabItem() {
		return mTabItem;
	}
	
	public final Composite createWorkSpace(Composite parent) {
		if(!(parent instanceof CTabFolder)){
			Redirect.errPrintln("parent is not a CTabFolder!");
		} 
		mCTabFolder = (CTabFolder)parent;
		
		if(mCTabFolder.getItemCount() == 0){ 
			mCTabFolder.addListener(SWT.Selection, new Listener(){
				public void handleEvent(Event event) {
					final AbstractWorkSpaceTab last = (AbstractWorkSpaceTab)mCTabFolder.getData(LAST_ITEM_KEY);
					if(last != null){
						last.onDeselect();
					} 
					
					final CTabItem current = mCTabFolder.getSelection();
					if(current != null){
						final AbstractWorkSpaceTab now = (AbstractWorkSpaceTab)current.getData(KEY);
						now.onSelect();
						mCTabFolder.setData(LAST_ITEM_KEY, now);
					}
				}
			});
		}
			
		mTabItem = new CTabItem(mCTabFolder, SWT.NONE | SWT.BORDER); 
		mTabItem.setText(""+mText+""); 
		
		final Composite composite = createTab(mCTabFolder);
		mTabItem.setControl(composite);
		
		mTabItem.setData(KEY, this);
		
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
				
		return composite;
	}
	
	public void onSelect(){
	}
	
	public void onDeselect(){
	}
	
	public boolean isSelected(){ 
		return mCTabFolder.getSelection() == mTabItem;
	}
	
	public final void run(){
		final Shell shell = PowerMan.getSingleton(MainDesigner.class).mShell;
		if (shell.isDisposed()) { 
			return; 
		} 
		
		if(sRunning && isSelected()){ 
			update(); 
		} 
		
		shell.getDisplay().timerExec(mInterval, this);
	}
	
	private int mInterval = PowerMan.getSingleton(FrameInterval.class).UI_INTERVAL;
	
	public void setInterval(int interval) {
		mInterval = interval;
	}
	
	public int getInterval() {
		return mInterval;
	}
	
	public abstract void update();
	public abstract Composite createTab(CTabFolder parent);
	
}
