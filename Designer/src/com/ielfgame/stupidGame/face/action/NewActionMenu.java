package com.ielfgame.stupidGame.face.action;

import java.util.List;

import org.eclipse.swt.events.SelectionEvent;

import com.ielfgame.stupidGame.dialog.MultiLineDialog;
import com.ielfgame.stupidGame.face.action.CCActionHelper.AbstractNewAction;
import com.ielfgame.stupidGame.face.basic.BasicTree;
import com.ielfgame.stupidGame.newNodeMenu.AbstractMenu;
import com.ielfgame.stupidGame.newNodeMenu.AbstractMenuItem;

public class NewActionMenu extends AbstractMenu{
	private final BasicTree mTree;
	public NewActionMenu(final BasicTree tree, final String label, final List<Class<?>> list) { 
		super(label); 
		mTree = tree;
		
		for(final Class<?> _class : list) {
			this.checkInMenuItem(new AbstractMenuItem(_class.getSimpleName().substring(1)) {
				public void onClick(SelectionEvent e) { 
					
					final CCActionData data = (CCActionData)mTree.getSelectObject();
					
					final CCActionData newData = new CCActionData();
					
					try { 
						final AbstractNewAction abstractNewAction = (AbstractNewAction)_class.newInstance();
						
						final MultiLineDialog multiLineDialog = new MultiLineDialog();
						
						multiLineDialog.setTitle(abstractNewAction.toTitle());
						multiLineDialog.setLabels(abstractNewAction.toLabels());
						multiLineDialog.setValues(abstractNewAction.getValues());
						multiLineDialog.setValueTypes(abstractNewAction.getTypes());
						
						final Object [] ret = multiLineDialog.open(); 
						if(ret != null) { 
							abstractNewAction.setValues( ret );
							newData.setAbstractNewAction(abstractNewAction);
							
							mTree.addToTree(data, newData, true);
						} 
						
					} catch (Exception e2) { 
						e2.printStackTrace(); 
					} 
				} 
				
				public boolean isShow() {
					return true;
				}
			});
		}
	}
	@Override
	public void onClick(SelectionEvent e) {
	} 
} 
