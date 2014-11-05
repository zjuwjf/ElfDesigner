package com.ielfgame.stupidGame.newNodeMenu;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public abstract class AbstractMenu extends AbstractMenuItem{ 
	
	private final ArrayList<IMenuItem> mFuncList = new ArrayList<IMenuItem>();
	public AbstractMenu(final String label) { 
		super(label); 
	} 

	public void checkInMenuItem(final IMenuItem aMenuItem) { 
		mFuncList.add(aMenuItem);
	} 
	
	public Menu create(final Shell shell) { 
		final Menu menu = new Menu(shell, getLabel()==null? SWT.POP_UP: SWT.DROP_DOWN); 
		
		for(IMenuItem aMenuItem : mFuncList) { 
			if(aMenuItem != null) { 
				Menu newMenu = aMenuItem.create(shell); 
				if(newMenu != null) { 
					final MenuItem menuItem = new MenuItem(menu, SWT.CASCADE); // 0
					menuItem.setText( aMenuItem.getLabel() ); 
					menuItem.setMenu(newMenu); 
					menuItem.setData( aMenuItem ); 
					
					aMenuItem.setMenuItem(menuItem); 
				} else { 
					final MenuItem menuItem = new MenuItem(menu, aMenuItem.isCheckItem()? SWT.CHECK:SWT.PUSH); 
					menuItem.setText( aMenuItem.getLabel() ); 
					menuItem.setData( aMenuItem ); 
					menuItem.addSelectionListener(mSelectionListener); 
					
					aMenuItem.setMenuItem(menuItem);
					
					if(aMenuItem.isCheckItem()) { 
						menuItem.setSelection(aMenuItem.initCheck()); 
					} 
				} 
			} else { 
				new MenuItem(menu, SWT.SEPARATOR); 
			} 
		}
		
		menu.addMenuListener(new MenuListener() {
			public void menuShown(MenuEvent e) {
				if(isShow()) {
					Menu menu = (Menu) e.widget;
					MenuItem[] items = menu.getItems();
					for(MenuItem item : items) {
						final IMenuItem iMenuItem = (IMenuItem)item.getData();
						if(iMenuItem != null) { 
							item.setEnabled(iMenuItem.isShow()); 
						} 
					} 
					onShown(menu);
				} 
			}
			public void menuHidden(MenuEvent e) {
			} 
		});
		
		return menu; 
	} 
	
	public final void onShown(Menu menu) { 
		
	} 
	
	private final SelectionListener mSelectionListener = new SelectionListener() {
		public void widgetSelected(SelectionEvent e) {
			final MenuItem item = (MenuItem)e.widget;
			if( item != null ) {
				final IMenuItem iMenuItem = (IMenuItem)item.getData();
				if(iMenuItem != null) { 
					try {
						iMenuItem.onClick(e); 
					} catch (Exception e2) {
						e2.printStackTrace();
					}
					
				} 
			}
		}

		public void widgetDefaultSelected(SelectionEvent e) {
		}
	};

	public boolean isCheckItem() {
		return false;
	}
	
	public boolean initCheck() {
		return true;
	}
} 
