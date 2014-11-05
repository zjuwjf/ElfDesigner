package com.ielfgame.stupidGame.newNodeMenu;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import org.eclipse.swt.events.SelectionEvent;

import com.ielfgame.stupidGame.NodeView.NodeViewWorkSpaceTab;
import com.ielfgame.stupidGame.data.DataModel;
import com.ielfgame.stupidGame.power.PowerMan;
import com.ielfgame.stupidGame.undo.UndoRedoManager;

import elfEngine.basic.list.ElfList;
import elfEngine.basic.node.ElfNode;

public class ConvertNodeMenuItem extends AbstractMenuItem {
	protected final Class<? extends ElfNode> mClass;
	
	public ConvertNodeMenuItem(Class<? extends ElfNode> _class) {
		super("To "+_class.getSimpleName());
		mClass = _class;
	} 
	
	public void onClick(SelectionEvent e) { 
		convert(mClass);
	} 
	
	public final static boolean convert(final Class<? extends ElfNode> _class) {
		try {
			UndoRedoManager.checkInUndo();
			
			final Constructor<? extends ElfNode> constructor = _class.getConstructor(ElfNode.class, int.class);
			
			final ArrayList<ElfNode> selectNodes = PowerMan.getSingleton(DataModel.class).getSelectNodeList(); 
			for(ElfNode node : selectNodes) { 
				final ElfNode newNode = constructor.newInstance(node.getParent(), node.ordinal()); 
				ElfNode.copyFrom(newNode, node);
				
				final ElfList<ElfNode> list = node.getChildList(); 
				final ElfList<ElfNode>.Iterator it = list.iterator(true); 
				int index = 0;
				while (it.hasNext()) {
					final ElfNode child = it.next();
					final ElfNode newChild = child.copyDeep(newNode);
					newChild.setParent(newNode);
					newChild.addToParent(index);
					index++;
				} 
				
				final NodeViewWorkSpaceTab nodeViewWorkSpaceTab = PowerMan.getSingleton(NodeViewWorkSpaceTab.class);
				
				if(ConvertNodeMenu.getAutoREmove()) {
					nodeViewWorkSpaceTab.removeNode(node);
				} 
				
				newNode.onCreateRequiredNodes();
				
				nodeViewWorkSpaceTab.addNode(newNode.getParent(), newNode, Integer.MAX_VALUE, false);
				NodeViewWorkSpaceTab.setExpand(newNode, true); 
			} 
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean isShow() {
		final int modifier = mClass.getModifiers();
		boolean ret = !mClass.isMemberClass() && (modifier & 1024) == 0; 
		
		if(mClass.isMemberClass() && (modifier & 1024) == 0) { 
			final int size = PowerMan.getSingleton(DataModel.class).getSelectNodeList().size();
			if(size == 1) { 
				final ElfNode node = PowerMan.getSingleton(DataModel.class).getSelectNode();
				if(node != null) {
					Class<?> selectClass = node.getClass();
					final Class<?> [] declares = selectClass.getDeclaredClasses(); 
					for(Class<?> c : declares) { 
						if(c == mClass) { 
							return true; 
						} 
					} 
				} 
			} 
		} 
		
		return ret;
	} 
}
