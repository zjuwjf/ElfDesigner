package com.ielfgame.stupidGame.newNodeMenu;

import org.eclipse.swt.events.SelectionEvent;

import com.ielfgame.stupidGame.data.DataModel;
import com.ielfgame.stupidGame.power.PowerMan;

import elfEngine.basic.node.ElfNode;


public class ConvertNodeMenu extends AbstractMenu {
	
	private static boolean sAutoRemove = true; 
	
	public static boolean getAutoREmove() { 
		return sAutoRemove; 
	} 
	
	public ConvertNodeMenu() {
		super("Convert Node");
		
		init();
	}
	
	public void init() { 
		this.checkInMenuItem(new AbstractMenuItem("Auto Remove") {
			public void onClick(SelectionEvent e) {
				sAutoRemove = getMenuItem().getSelection();
			}
			public boolean isCheckItem() { 
				return true;
			} 
		});
		
		this.checkInMenuItem(null);
		this.checkInMenuItem(new ConvertNodeMenuItem(ElfNode.class));
		this.checkInMenuItem(new ConvertNodeFreeMenuItem());
	} 
	
	public boolean isShow() {
		final ElfNode selectNode = PowerMan.getSingleton(DataModel.class).getSelectNode();
		return selectNode != null;
	}

	@Override
	public void onClick(SelectionEvent e) {
	} 
} 
