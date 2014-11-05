package com.ielfgame.stupidGame.face.action;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Composite;

import com.ielfgame.stupidGame.AbstractWorkSpaceTab;

public class ActionWorkSpaceTab extends AbstractWorkSpaceTab{

	final ActionTree mActionTree = new ActionTree();
	final ActionMenu mActionMenu = new ActionMenu(mActionTree);
	
	public ActionWorkSpaceTab() { 
		super("Action");
	} 

	public void update() {
	}

	public Composite createTab(CTabFolder parent) { 
		return mActionTree.createTree(mCTabFolder,  SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI);
	}
 
	public ActionTree getActionTree() {
		return mActionTree;
	}
}
