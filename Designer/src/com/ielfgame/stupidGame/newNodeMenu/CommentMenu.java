package com.ielfgame.stupidGame.newNodeMenu;

import java.util.ArrayList;

import org.eclipse.swt.events.SelectionEvent;

import com.ielfgame.stupidGame.NodeView.NodeViewWorkSpaceTab;
import com.ielfgame.stupidGame.data.DataModel;
import com.ielfgame.stupidGame.power.PowerMan;

import elfEngine.basic.node.ElfNode;

public class CommentMenu extends AbstractMenu{
	private final NodeViewWorkSpaceTab mNodeViewWorkSpaceTab; 
	
	public CommentMenu() {
		super("Comment & Uncomment");
		
		mNodeViewWorkSpaceTab = PowerMan.getSingleton(NodeViewWorkSpaceTab.class); 
		
		this.checkInMenuItem(new AbstractMenuItem("Comment Node") {
			public void onClick(SelectionEvent e) {
				final ArrayList<ElfNode> nodes = PowerMan.getSingleton(DataModel.class).getSelectNodeList();
				for(ElfNode node: nodes) { 
					if(!mNodeViewWorkSpaceTab.isScreenOrRecycleNode(node)) {
						node.commentName();
					} 
				} 
			} 
			public boolean isShow() { 
				final ElfNode selectNode = PowerMan.getSingleton(DataModel.class).getSelectNode();
				return selectNode != null && !mNodeViewWorkSpaceTab.isScreenOrRecycleNode(selectNode);
			} 
		});
		
		this.checkInMenuItem(new AbstractMenuItem("Comment Deep") {
			public void onClick(SelectionEvent e) {
				final ArrayList<ElfNode> nodes = PowerMan.getSingleton(DataModel.class).getSelectNodeList();
				for(ElfNode node: nodes) {
					if(!mNodeViewWorkSpaceTab.isScreenOrRecycleNode(node)) {
						node.commentNameDeep();
					}
				} 
			} 
			public boolean isShow() { 
				final ElfNode selectNode = PowerMan.getSingleton(DataModel.class).getSelectNode();
				return selectNode != null && !mNodeViewWorkSpaceTab.isScreenOrRecycleNode(selectNode);
			} 
		});
		
		this.checkInMenuItem(new AbstractMenuItem("Uncomment Node") {
			public void onClick(SelectionEvent e) {
				final ArrayList<ElfNode> nodes = PowerMan.getSingleton(DataModel.class).getSelectNodeList();
				for(ElfNode node: nodes) {
					if(!mNodeViewWorkSpaceTab.isScreenOrRecycleNode(node)) {
						node.uncommentName();
					}
				} 
			} 
			public boolean isShow() { 
				final ElfNode selectNode = PowerMan.getSingleton(DataModel.class).getSelectNode();
				return selectNode != null && !mNodeViewWorkSpaceTab.isScreenOrRecycleNode(selectNode);
			} 
		});
		
		this.checkInMenuItem(new AbstractMenuItem("Uncomment Deep") {
			public void onClick(SelectionEvent e) {
				final ArrayList<ElfNode> nodes = PowerMan.getSingleton(DataModel.class).getSelectNodeList();
				for(ElfNode node: nodes) {
					if(!mNodeViewWorkSpaceTab.isScreenOrRecycleNode(node)) {
						node.uncommentNameDeep();
					} 
				} 
			} 
			public boolean isShow() { 
				final ElfNode selectNode = PowerMan.getSingleton(DataModel.class).getSelectNode();
				return selectNode != null && !mNodeViewWorkSpaceTab.isScreenOrRecycleNode(selectNode);
			} 
		});
	}

	@Override
	public void onClick(SelectionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
