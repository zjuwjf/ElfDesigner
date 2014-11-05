package com.ielfgame.stupidGame.newNodeMenu;

import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import com.ielfgame.stupidGame.language.LanguageManager;

public abstract class AbstractMenuItem implements IMenuItem {
	private String mLabel;
	private MenuItem mMenuItem;

	public AbstractMenuItem(final String label) {
		setLable(label); 
	} 
	
	public void setLable(final String label) {
		if(label != null) {
			final int index = label.indexOf("\t");
			if(index != -1) {
				String s1 = label.substring(0, index);
				String s2 = label.substring(index);
				mLabel = LanguageManager.get(s1) + s2; 
			} else {
				mLabel = LanguageManager.get(label); 
			}
		}
	} 
	
	public MenuItem getMenuItem() {
		return mMenuItem;
	}
	
	public void setMenuItem(MenuItem menuItem) {
		mMenuItem = menuItem;
	}

	public String getLabel() {
		return mLabel;
	} 

	public Menu create(final Shell shell) {
		return null;
	}
	
	public boolean isShow() {
		return true;
	}
	
//	public void onClick(SelectionEvent e) {
//	} 
	
	public boolean isCheckItem() {
		return false;
	}
	
	public boolean initCheck() {
		return true;
	}
}
