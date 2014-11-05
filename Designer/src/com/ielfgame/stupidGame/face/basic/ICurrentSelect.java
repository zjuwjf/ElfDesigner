package com.ielfgame.stupidGame.face.basic;

import java.util.ArrayList;

import com.ielfgame.stupidGame.data.DataModel;
import com.ielfgame.stupidGame.power.PowerMan;

import elfEngine.basic.node.ElfNode;

public interface ICurrentSelect { 
	
	public Object[] getSelectObjs(boolean gloabl); 
	
	public static final class CurrentSelectAdapter implements ICurrentSelect{
		public Object[] getSelectObjs(boolean gloabl) { 
			if(gloabl) { 
				final ArrayList<ElfNode> list = PowerMan.getSingleton(DataModel.class).getSelectNodeList(); 
				final Object [] ret = new Object[list.size()];
				list.toArray(ret); 
				return ret; 
			} else { 
				final Object obj = PowerMan.getSingleton(DataModel.class).getSelectNode();
				if(obj != null) { 
					return new Object[]{obj};
				} else {
					return new Object[0];
				} 
			} 
		}
	} 
	
	public static final ICurrentSelect CurrentSelect_Default = new CurrentSelectAdapter();
} 
