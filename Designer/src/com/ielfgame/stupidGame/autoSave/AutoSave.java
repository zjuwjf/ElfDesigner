package com.ielfgame.stupidGame.autoSave;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.ielfgame.stupidGame.animation.Animate;
import com.ielfgame.stupidGame.data.DataModel;
import com.ielfgame.stupidGame.face.action.ActionTree;
import com.ielfgame.stupidGame.face.action.ActionWorkSpaceTab;
import com.ielfgame.stupidGame.face.action.CCActionData;
import com.ielfgame.stupidGame.power.PowerMan;
import com.ielfgame.stupidGame.utils.FileHelper;
import com.ielfgame.stupidGame.xml.XMLVersionManage;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.ElfNode.IIterateChilds;

public class AutoSave implements Runnable{
	
	static final String USER_DIR = System.getProperty("user.dir"); 
	static final String AUTO_SAVE = FileHelper.DECOLLATOR+"autoSave.xml"; 
	
	public AutoSave() { 
	} 
	
	private long mTimeSpace;
	private long mLastSaveTime = System.currentTimeMillis();
	
	public void run() {
		mTimeSpace = (long)(PowerMan.getSingleton(DataModel.class).getAutoSaveTime() * 1000 * 60);
		
		final long now = System.currentTimeMillis();
		
		if(now - mLastSaveTime > mTimeSpace) {
			mLastSaveTime = now;
			
			final String path = USER_DIR + AUTO_SAVE;
			
			final List<Object> exports = new LinkedList<Object>();
			final ElfNode screenNode = PowerMan.getSingleton(DataModel.class).getRootScreen();
			
			screenNode.iterateChilds(new IIterateChilds() { 
				public boolean iterate(ElfNode node) { 
					exports.add(node); 
					return false; 
				} 
			}); 
			
			final ArrayList<Animate> animateList = PowerMan.getSingleton(DataModel.class).getAnimateList();
			for(Animate animate : animateList){ 
				exports.add(animate); 
			} 
			
			final ActionTree ActionTree = PowerMan.getSingleton(ActionWorkSpaceTab.class).getActionTree();
			
			final Object[] actions = ((CCActionData)(ActionTree.getData( ActionTree.getRootItems()[0] ))).getChildsForXML();
			for(int i=0; i<actions.length; i++) { 
				exports.add(actions[i]);
			}
			
			XMLVersionManage.writeToXML(exports, path);
		} 
	} 
} 
