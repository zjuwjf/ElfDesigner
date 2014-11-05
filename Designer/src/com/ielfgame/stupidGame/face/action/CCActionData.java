package com.ielfgame.stupidGame.face.action;

import java.util.ArrayList;

import org.cocos2d.actions.base.CCAction;
import org.eclipse.swt.widgets.TreeItem;

import com.ielfgame.stupidGame.data.DataModel;
import com.ielfgame.stupidGame.face.action.CCActionHelper.AbstractNewAction;
import com.ielfgame.stupidGame.face.basic.BasicTree.ITreeObject;
import com.ielfgame.stupidGame.power.PowerMan;
import com.ielfgame.stupidGame.xml.IXMLChilds;

import elfEngine.basic.node.ElfNode;

public class CCActionData implements ITreeObject, Cloneable, IXMLChilds{
	
	private AbstractNewAction mAbstractNewAction; 
	
	public AbstractNewAction getAbstractNewAction() { 
		return mAbstractNewAction;
	} 
	
	public void setAbstractNewAction(AbstractNewAction mAbstractNewAction) { 
		this.mAbstractNewAction = mAbstractNewAction; 
	} 
	
	private final ArrayList<ElfNode> mTargets = new ArrayList<ElfNode>();
	public void setTargets(ArrayList<ElfNode> list) {
		try {
			mTargets.clear();
			mTargets.addAll(list);
		} catch (Exception e) {
		}
	} 
	public ArrayList<ElfNode> getTargets() {
		return mTargets;
	}
	
	private boolean mIsRoot = false;
	public void setIsRoot(boolean isRoot) {
		mIsRoot = isRoot;
	} 
	public boolean getIsRoot() {
		return mIsRoot;
	}
	
//	private ElfNode mTarget;
//	public void setTarget(ElfNode node) {
//		mTarget = node;
//	}
//	public ElfNode getTarget() { 
//		return mTarget;
//	}
	
	public String [] getTargetFullNames() {
		final String [] ret = new String[mTargets.size()];
		for(int i=0; i<ret.length; i++) { 
			ret[i] = mTargets.get(i).getFullName(); 
		} 
		return ret;
	} 
	
	private String [] mTargetFullNames;
	
	public void setTargetFullNames(String [] fullNames) {
		mTargetFullNames = fullNames;
//		PowerMan.getSingleton(DataModel.class).
	}
	
	public void setTargetByFullNames(){
		if(mTargetFullNames != null) {
			mTargets.clear();
			final ElfNode node = PowerMan.getSingleton(DataModel.class).getScreenNode();
			for(String name : mTargetFullNames) {
				final ElfNode target = node.findByName(name);
				if(target != null) {
					mTargets.add(target);
				}
			}
		}
	}
	
	public CCActionData copy() {
		try {
			CCActionData ret = new CCActionData();
			ret.mAbstractNewAction = this.mAbstractNewAction.copy();
			ret.mTreeItem = null;
//			ret.mTarget = null; 
			
			for(final CCActionData data: this.mChildsList) {
				ret.mChildsList.add( data.copy() );
			} 
			
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String toString() { 
		String ret =  "";
		//
		if(mAbstractNewAction == null) {
			ret += "null";
		} else { 
			ret += mAbstractNewAction.mActoinClass.getSimpleName()+"("; 
			final String [] values = mAbstractNewAction.toValues();
			for(int i=0; i<values.length-1; i++) {
				ret += values[i] + ", ";
			}
			if(ret.endsWith(", ")) {
				ret = ret.substring(0, ret.length() - 2);
			}
			ret += ")";
		}
		
		ret += ", ";
		
		if(mAbstractNewAction != null) { 
			final ArrayList<CCActionData> actions = new ArrayList<CCActionData>(); 
			for(CCActionData data : mChildsList) { 
				if(data.newAction() != null) { 
					actions.add(data); 
				} 
			} 
			
			final CCActionData [] actionArray = new CCActionData[actions.size()];
			actions.toArray(actionArray);
			
			for(CCActionData data : actionArray) {
				ret += "{" + data.toString() +"}, ";
			} 
		} 
		
		return ret;
	}
	
	public final ArrayList<CCActionData> mChildsList = new ArrayList<CCActionData>();
	
	public CCAction newAction() { 
		if(mAbstractNewAction != null) { 
			final ArrayList<CCAction> actions = new ArrayList<CCAction>(); 
			for(CCActionData data : mChildsList) { 
				final CCAction action = data.newAction(); 
				if(action != null) { 
					actions.add(action); 
				} 
			} 
			
			final CCAction [] actionArray = new CCAction[actions.size()];
			actions.toArray(actionArray);
			
			return mAbstractNewAction.newAction(actionArray);
		} 
		
		return null;
	}

	public String getName() {
		if(mAbstractNewAction != null) {
			return mAbstractNewAction.name;
		} 
		return null;
	}

	public void setName(String name) {
		if(mAbstractNewAction != null) { 
			mAbstractNewAction.name = name;
		} 
	}

	private TreeItem mTreeItem;
	public void setTreeItem(TreeItem item) {
		mTreeItem = item;
	}

	public TreeItem getTreeItem() { 
		return mTreeItem;
	}

	public Object[] getChildsForFace() {
		return getChilds();
	}

	public Object[] getChildsForXML() {
		return getChilds();
	} 
	
	private final Object[] getChilds() {
		final CCActionData [] ret = new CCActionData[mChildsList.size()];
		mChildsList.toArray(ret);
		return ret;
	} 
} 
