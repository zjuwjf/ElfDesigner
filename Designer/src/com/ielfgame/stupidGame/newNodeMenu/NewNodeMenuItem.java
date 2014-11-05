package com.ielfgame.stupidGame.newNodeMenu;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Shell;

import com.ielfgame.stupidGame.MainDesigner;
import com.ielfgame.stupidGame.NodeView.NodeViewWorkSpaceTab;
import com.ielfgame.stupidGame.data.DataModel;
import com.ielfgame.stupidGame.dialog.PopDialog;
import com.ielfgame.stupidGame.dialog.PopDialog.PopType;
import com.ielfgame.stupidGame.nodeMap.NodeMap;
import com.ielfgame.stupidGame.power.PowerMan;
import com.ielfgame.stupidGame.undo.UndoRedoManager;
import com.ielfgame.stupidGame.xml.NewXMLFactory;

import elfEngine.basic.node.ElfNode;

public class NewNodeMenuItem extends AbstractMenuItem { 
	protected final Class<? extends ElfNode> mClass;
	private final boolean mEnable;
	
	public NewNodeMenuItem(Class<? extends ElfNode> _class) {
		this(_class, true);
	} 
	
	public NewNodeMenuItem(Class<? extends ElfNode> _class, boolean enable) {
		super(_class.getSimpleName());
		mClass = _class;
		
		NodeMap.checkIn(_class);
		NewXMLFactory.checkIn(_class); 
		
		mEnable = enable;
	} 
	
	
	public void onClick(SelectionEvent e2) {
		
		final Class<? extends ElfNode> _class = mClass;
		final ArrayList<ElfNode> selectNodes = PowerMan.getSingleton(DataModel.class).getSelectNodeList();
		if(_class != null && selectNodes.size() > 0) { 
			try {
				ArrayList<ElfNode> newNodes = new ArrayList<ElfNode>();
				
				final Constructor<? extends ElfNode> constructor = _class.getConstructor(ElfNode.class, int.class);
				
				for(ElfNode selectNode : selectNodes) {
					final ElfNode newNode = constructor.newInstance(selectNode, -1);
					
					try {
//						NodeConfig.initNode(newNode);
					} catch (Exception e) {
					}
					
					newNode.onCreateRequiredNodes();
					newNodes.add( newNode );
				} 
				
				if(newNodes.size() > 0) {
					final Shell shell = PowerMan.getSingleton(MainDesigner.class).mShell;
					PopDialog dialog = new PopDialog(shell, newNodes.get(0), PopType.NEW_NODE);
					dialog.setLabels("New "+_class.getSimpleName());
					dialog.setValues(newNodes.get(0).getName());
					final String[] values = dialog.open();
					if (values == null) {
						return;
					} else {
						UndoRedoManager.checkInUndo();
						
						for(int i=0; i<newNodes.size(); i++) {
							final ElfNode newNode = newNodes.get(i);
							final ElfNode selectNode = selectNodes.get(i);
							
							newNode.setName(values[0]);
							final NodeViewWorkSpaceTab nodeViewWorkSpaceTab = PowerMan.getSingleton(NodeViewWorkSpaceTab.class);
							nodeViewWorkSpaceTab.sysName(selectNode);
							nodeViewWorkSpaceTab.addNode(selectNode, newNode, Integer.MAX_VALUE, false);
							NodeViewWorkSpaceTab.setExpand(selectNode, true);
							
//							if(newNode instanceof TextNode) {
//								((TextNode)newNode).setText(values[0]);
//							} 
						} 
					} 
				} 
				
			} catch (Exception exception) { 
				exception.printStackTrace();
			} 
		} 
	}
	
	public boolean isShow() {
		if(!mEnable) {
			return false;
		}
		
		final int modifier = mClass.getModifiers();
		boolean ret = (!mClass.isMemberClass() || (modifier & 8) != 0)
				&& (modifier & 1024) == 0; 
		
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
	/*
	 * 
	 * PUBLIC: 1
PRIVATE: 2
PROTECTED: 4
STATIC: 8
FINAL: 16
SYNCHRONIZED: 32
VOLATILE: 64
TRANSIENT: 128
NATIVE: 256
INTERFACE: 512
ABSTRACT: 1024
STRICT: 2048
	 */
}
