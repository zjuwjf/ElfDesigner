package com.ielfgame.stupidGame.face.action;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;

import org.cocos2d.actions.base.CCAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;

import com.ielfgame.stupidGame.data.DataModel;
import com.ielfgame.stupidGame.dialog.MultiLineDialog;
import com.ielfgame.stupidGame.face.basic.BasicMenu;
import com.ielfgame.stupidGame.face.basic.BasicTree;
import com.ielfgame.stupidGame.face.basic.BasicTree.IName;
import com.ielfgame.stupidGame.newNodeMenu.AbstractMenuItem;
import com.ielfgame.stupidGame.platform.PlatformHelper;
import com.ielfgame.stupidGame.power.PowerMan;

import elfEngine.basic.node.ElfNode;

public class ActionMenu extends BasicMenu{

	public ActionMenu(final BasicTree tree) {
		super(tree);
		
		final Set<String> set = CCActionHelper.CategoryMap.keySet();
		for(String key : set) {
			final LinkedList<Class<?>> list = CCActionHelper.CategoryMap.get(key);
			this.checkInMenuItem(new NewActionMenu(tree, key, list)); 
		} 
		
		this.checkInMenuItem(null); 
		
		this.checkInMenuItem(new AbstractMenuItem("Rename\tF2") { 
			public void onClick(SelectionEvent e) { 
				rename();
			} 
			public boolean isShow() { 
				return !mBasicTree.isRootItem( mBasicTree.getSelectItem() );
			} 
		}); 
		
		this.checkInMenuItem(new AbstractMenuItem("Delete\tDel") { 
			public void onClick(SelectionEvent e) { 
				delete();
			} 
			public boolean isShow() { 
				return !mBasicTree.isRootItem( mBasicTree.getSelectItem() );
			} 
		}); 
		
		this.checkInMenuItem(new AbstractMenuItem("Copy\tF4") {  
			public void onClick(SelectionEvent e) { 
				copy();
			} 
			public boolean isShow() { 
				return !mBasicTree.isRootItem( mBasicTree.getSelectItem() );
			} 
		}); 
		
		this.checkInMenuItem(null) ;
		
		this.checkInMenuItem(new AbstractMenuItem("Set Target As Select Node\tF7") {
			public void onClick(SelectionEvent e) { 
				setTarget();
			} 
			public boolean isShow() { 
				final TreeItem item = mBasicTree.getSelectItem();
				if(mBasicTree.isValid(item)) { 
					return !mBasicTree.isRootItem( mBasicTree.getSelectItem() ) && mBasicTree.isRootItem( item.getParentItem() );
				} 
				return false;
			} 
		});
		
		this.checkInMenuItem(new AbstractMenuItem("Run Action\tF8") {
			public void onClick(SelectionEvent e) { 
				run();
			} 
			public boolean isShow() { 
				final TreeItem item = mBasicTree.getSelectItem();
				if(mBasicTree.isValid(item)) {
					return !mBasicTree.isRootItem( mBasicTree.getSelectItem() ) && mBasicTree.isRootItem( item.getParentItem() );
				}
				return false;
			} 
		}); 
		
		this.checkInMenuItem(new AbstractMenuItem("Stop Action\tF9") {
			public void onClick(SelectionEvent e) { 
				stop();
			} 
			public boolean isShow() { 
				final TreeItem item = mBasicTree.getSelectItem();
				if(mBasicTree.isValid(item)) { 
					return !mBasicTree.isRootItem( mBasicTree.getSelectItem() ) && mBasicTree.isRootItem( item.getParentItem() );
				}
				return false;
			} 
		});
	} 
	
	public Menu create(final Shell shell) {
		final Menu ret = super.create(shell);
		this.mBasicTree.getTree().addKeyListener(new KeyListener() {
			public void keyReleased(KeyEvent e) {
			}
			public void keyPressed(KeyEvent e) {
				switch(e.keyCode) { 
				case SWT.F2:
					rename();
					break;
				case SWT.F4:
					copy();
					break;
				case SWT.F7:
					setTarget();
					break;
				case SWT.F8:
					run();
					break;
				case SWT.F9:
					stop();
					break;
				}
				if( PlatformHelper.DEL == e.keyCode) {
					delete();
				}
			}
			
		});
		return ret;
	}
	
	void rename() { 
		final IName obj = (IName)mBasicTree.getSelectObject();
		if(obj != null) {
			final MultiLineDialog multiLineDialog = new MultiLineDialog();
			
			multiLineDialog.setTitle("Rename");
			multiLineDialog.setLabels(new String[]{"NewName"});
			multiLineDialog.setValues(new String[]{obj.getName()});
			multiLineDialog.setValueTypes(new Class<?>[]{String.class});
			
			final Object [] ret = multiLineDialog.open(); 
			if(ret != null) { 
				try { 
					obj.setName((String)ret[0]); 
				} catch (Exception e) { 
				} 
			} 
		} 
	} 
	
	void copy() {
		final CCActionData data = (CCActionData) mBasicTree.getSelectObject();
		if(data != null) { 
			final CCActionData newData = data.copy();
			
			final CCActionData newDataFather = (CCActionData)mBasicTree.getData( mBasicTree.getSelectItem().getParentItem() );
			mBasicTree.addToTree(newDataFather, newData, true);
		} 
	}
	
	void run() {
		final Object [] objs = mBasicTree.getSelectObjects();
		
		if(objs != null) { 
			for(int i=0; i<objs.length; i++) {
				final CCActionData data = (CCActionData)objs[i];
				
				ArrayList<ElfNode> list = data.getTargets(); 
				if(list.size() == 0) { 
					list = PowerMan.getSingleton(DataModel.class).getSelectNodeList();
					data.setTargets(list); 
				} 
				
				for(ElfNode node : list) {
					if(node != null) { 
						node.popActionTmp();
						node.pushActionTmp();
						final CCAction action = data.newAction();
						if(action != null) { 
							node.stopActions();
							node.runAction(action);
						} 
					}
				} 
			}
		} 
	}
	
	void stop() {
		final CCActionData data = (CCActionData)mBasicTree.getSelectObject();
		if(data != null) { 
			ArrayList<ElfNode> list = data.getTargets(); 
			for(ElfNode node : list) { 
				if(node != null) { 
					node.popActionTmp();
					node.stopActions();
				} 
			} 
		} 
	} 
	
	void delete() {
		mBasicTree.disposeBranch( mBasicTree.getSelectItem(), true);
	}
	
	void setTarget() {
		final CCActionData data = (CCActionData)mBasicTree.getSelectObject();
		if(data != null) {
			ArrayList<ElfNode> list = PowerMan.getSingleton(DataModel.class).getSelectNodeList();
			data.setTargets(list); 
			
			for(ElfNode node : list) { 
				if(node != null) { 
					node.pushActionTmp();
				} 
			} 
		} 
	}

	@Override
	public void onClick(SelectionEvent e) {
	}
} 
