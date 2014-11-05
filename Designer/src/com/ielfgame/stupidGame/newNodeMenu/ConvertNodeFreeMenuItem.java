package com.ielfgame.stupidGame.newNodeMenu;

import java.util.ArrayList;

import org.eclipse.swt.events.SelectionEvent;

import com.ielfgame.stupidGame.data.DataModel;
import com.ielfgame.stupidGame.data.ElfDataDisplay;
import com.ielfgame.stupidGame.dialog.MultiLineDialog;
import com.ielfgame.stupidGame.nodeMap.NodeMap;
import com.ielfgame.stupidGame.power.PowerMan;
import com.ielfgame.stupidGame.undo.UndoRedoManager;

import elfEngine.basic.node.ElfNode;

public class ConvertNodeFreeMenuItem  extends AbstractMenuItem {
	
	public static class NodeEnums extends ElfDataDisplay{
		public String NodeType = "ElfNode";
	}

	public ConvertNodeFreeMenuItem() {
		super("To Any Node");
	}
	
	public void onClick(SelectionEvent e) { 
		final MultiLineDialog multiLineDialog = new MultiLineDialog();
		final Object [] ret = multiLineDialog.open(new NodeEnums());
		if(ret != null && ret[0] != null) {
			try {
				UndoRedoManager.checkInUndo();
				
				final Class<? extends ElfNode> _class = NodeMap.getNodeClass((String)ret[0]);
				ConvertNodeMenuItem.convert(_class);
			} catch (Exception e2) { 
				e2.printStackTrace(); 
			} 
		} 
	} 
	
	public boolean isShow() { 
		final ArrayList<ElfNode> selectNodes = PowerMan.getSingleton(DataModel.class).getSelectNodeList(); 
		return !selectNodes.isEmpty();
	} 
}
