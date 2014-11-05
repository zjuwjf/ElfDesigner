package com.ielfgame.stupidGame.face.action;

import java.util.ArrayList;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.ielfgame.stupidGame.data.DataModel;
import com.ielfgame.stupidGame.dialog.MultiLineDialog;
import com.ielfgame.stupidGame.face.action.CCActionHelper.AbstractNewAction;
import com.ielfgame.stupidGame.face.basic.BasicTree;
import com.ielfgame.stupidGame.power.PowerMan;

import elfEngine.basic.node.ElfNode;

public class ActionTree extends BasicTree{ 
	
	public ActionTree() {
		this.setTreeColumns(new String[]{"Name", "Text", "Target"}, new int [] {150, 900, 150});
	} 

	public void initRootItem(Tree tree, TreeItem root, TreeItem recycle) { 
		root.setText("Action List");
		recycle.setText("Recycle List");
		
		this.setData(root, PowerMan.getSingleton(DataModel.class).getRootCCActionData());
		this.setData(recycle, PowerMan.getSingleton(DataModel.class).getRecycleCCActionData());
	} 
	
	public void rebuild() {
		super.rebuild();
		
		final CCActionData rootData = PowerMan.getSingleton(DataModel.class).getRootCCActionData();
		for(CCActionData data : rootData.mChildsList) {
			this.addToTree(rootData, data, false);
		}
	}

	public void addObject(ITreeObject father, ITreeObject child, int index) { 
		final CCActionData fatherData = (CCActionData)father;
		final CCActionData childData = (CCActionData)child;
		
		try { 
			fatherData.mChildsList.remove(childData);
			index = Math.min(index, fatherData.mChildsList.size());
			index = Math.max(index, 0);
			fatherData.mChildsList.add(index, childData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 

	public void removeObject(ITreeObject father, ITreeObject child, int index) { 
		final CCActionData fatherData = (CCActionData)father;
		final CCActionData childData = (CCActionData)child;
		
		try { 
			fatherData.mChildsList.remove(childData);
		} catch (Exception e) { 
			e.printStackTrace();
		} 
	} 
	
	public void onClick(TreeItem item, ITreeObject data) { 
	} 
	
	public void onDoubleClick(TreeItem item, ITreeObject data) { 
		if(data != null) { 
			final CCActionData ccData = (CCActionData)data;
			final AbstractNewAction abstractNewAction = ccData.getAbstractNewAction();
			if(abstractNewAction != null && ccData != null) { 
				
				try { 
					final MultiLineDialog multiLineDialog = new MultiLineDialog();
					
					multiLineDialog.setTitle(abstractNewAction.toTitle());
					multiLineDialog.setLabels(abstractNewAction.toLabels());
					multiLineDialog.setValues(abstractNewAction.getValues());
					multiLineDialog.setValueTypes(abstractNewAction.getTypes());
					
					final Object [] ret = multiLineDialog.open();
					if(ret != null) {
						abstractNewAction.setValues( ret );
					}
				} catch (Exception e) { 
					e.printStackTrace(); 
				} 
			} 
		} 
	} 

	public void update(TreeItem item, ITreeObject data) { 
		try {
			item.setText(data.getName());
			item.setText(1, data.toString());
			
			String text = "";
			final ArrayList<ElfNode> list = ((CCActionData)data).getTargets();
			for(ElfNode node : list) {
				if(node != null) {
					text += node.getName() + ",";
				}
			}
			item.setText(2, text);
		} catch (Exception e) {
		} 
	} 

	public Image[] getImage(ITreeObject object) { 
		return null;
	} 
} 

