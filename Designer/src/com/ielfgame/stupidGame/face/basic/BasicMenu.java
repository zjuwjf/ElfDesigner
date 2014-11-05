package com.ielfgame.stupidGame.face.basic;

import com.ielfgame.stupidGame.newNodeMenu.AbstractMenu;

public abstract class BasicMenu extends AbstractMenu{
	
	protected final BasicTree mBasicTree;
	
	public BasicMenu(BasicTree tree) { 
		super(null);
		
		mBasicTree = tree; 
		assert(mBasicTree != null); 
		
		tree.setBasicMenu(this);
	} 
} 
