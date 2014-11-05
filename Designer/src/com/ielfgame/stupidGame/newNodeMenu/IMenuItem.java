package com.ielfgame.stupidGame.newNodeMenu;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public interface IMenuItem { 
	public boolean isShow(); 
	public void onClick(SelectionEvent e); 
	public String getLabel(); 
	
	public boolean isCheckItem();
	public boolean initCheck();
	
	public Menu create(final Shell shell);
	
	public MenuItem getMenuItem() ;
	
	public void setMenuItem(MenuItem menuItem) ;
}