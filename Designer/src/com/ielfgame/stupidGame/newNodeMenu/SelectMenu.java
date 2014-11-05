package com.ielfgame.stupidGame.newNodeMenu;

import java.util.ArrayList;

import org.eclipse.swt.events.SelectionEvent;

import com.ielfgame.stupidGame.NodeView.NodeViewWorkSpaceTab;
import com.ielfgame.stupidGame.data.DataModel;
import com.ielfgame.stupidGame.power.PowerMan;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.ElfNode.IIterateChilds;

public class SelectMenu extends AbstractMenu{

	private final NodeViewWorkSpaceTab mNodeViewWorkSpaceTab; 
	
	public SelectMenu() {
		super("Selects");
		
		mNodeViewWorkSpaceTab = PowerMan.getSingleton(NodeViewWorkSpaceTab.class); 
		
		this.checkInMenuItem(new AbstractMenuItem("Select Same Nodes") {
			public void onClick(SelectionEvent e) {
				final ElfNode select = PowerMan.getSingleton(DataModel.class).getSelectNode();
				if(select != null) {
					final ArrayList<ElfNode> nodes = select.searchSameNameAndTypeNodes();
					final ElfNode [] arry_nodes = new ElfNode[nodes.size()];
					nodes.toArray(arry_nodes);
					
					mNodeViewWorkSpaceTab.setSelectNodes(arry_nodes);
				} 
			} 
			public boolean isShow() { 
				final ElfNode selectNode = PowerMan.getSingleton(DataModel.class).getSelectNode();
				return selectNode != null && !mNodeViewWorkSpaceTab.isScreenOrRecycleNode(selectNode);
			} 
		});
		
		this.checkInMenuItem(new AbstractMenuItem("Select Parent-Son Nodes") {
			public void onClick(SelectionEvent e) {
				final ElfNode select = PowerMan.getSingleton(DataModel.class).getSelectNode();
				if(select != null) {
					final ArrayList<ElfNode> nodes = select.searchSameNameAndTypeParentSonNodes();
					
					final ElfNode [] arry_nodes = new ElfNode[nodes.size()];
					nodes.toArray(arry_nodes);
					
					mNodeViewWorkSpaceTab.setSelectNodes(arry_nodes);
				} 
			} 
			public boolean isShow() { 
				final ElfNode selectNode = PowerMan.getSingleton(DataModel.class).getSelectNode();
				return selectNode != null && !mNodeViewWorkSpaceTab.isScreenOrRecycleNode(selectNode);
			} 
		});
		
		this.checkInMenuItem(new AbstractMenuItem("Select Deep") {
			public void onClick(SelectionEvent e) {
				final ArrayList<ElfNode> list = PowerMan.getSingleton(DataModel.class).getSelectNodeList();
				for(final ElfNode node : list) {
					node.iterateChildsDeepWithSelf(new IIterateChilds(){
						public boolean iterate(final ElfNode node) {
							mNodeViewWorkSpaceTab.addSelectNodes(node);
							return false;
						}
					});
				} 
			} 
		});
		
		this.checkInMenuItem(new AbstractMenuItem("Expand Selected Nodes") {
			public void onClick(SelectionEvent e) {
				NodeViewWorkSpaceTab.expandedNodes();
			} 
		});
	}

	@Override
	public void onClick(SelectionEvent e) {
		
	}

}
