package com.ielfgame.stupidGame.property;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import com.ielfgame.stupidGame.face.basic.EmptyTrees;
import com.ielfgame.stupidGame.face.basic.ICurrentSelect;
import com.ielfgame.stupidGame.face.basic.PropertyTree;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.nodeAnimate.timeLine.BoneNode;
import elfEngine.basic.node.nodeAnimate.timeLine.ElfMotionKeyNode;
import elfEngine.basic.node.nodeAnimate.timeLine.ElfMotionNode;
import elfEngine.basic.node.nodeAnimate.timeLine.TimeData;

public class CustomizedNodeTrees extends EmptyTrees { 
	//for 
	public CustomizedNodeTrees(final boolean isGolbal, final int widthType, final int widthValue) {
		
		final LinkedList<String> elfList = new LinkedList<String>(); 
		elfList.add("Resid");
		elfList.add("Position");
		elfList.add("Rotate"); 
		elfList.add("AnchorPosition");
		elfList.add("Scale");
		elfList.add("Color"); 
		elfList.add("Visible"); 
		elfList.add("Name"); 
		
		final LinkedList<String> motionKeyList = new LinkedList<String>();
		motionKeyList.add("Time");
		motionKeyList.add("InterType"); 
		motionKeyList.add("LoopMode");
		//PauseMotion
		
		final LinkedList<String> motionList = new LinkedList<String>();
		motionList.add("KeyStorage"); 
		
		final LinkedList<String> boneList = new LinkedList<String>();
		boneList.add("Length");
		
		final PropertyTree elfNodeTree = new PropertyTree(widthType, widthValue); 
		elfNodeTree.setCurrentSelect(motionICurrentSelect); 
		elfNodeTree.setSelectClass(ElfNode.class, false, elfList); 
		this.checkInTree(elfNodeTree); 
		
		final PropertyTree motionKeyNodeTree = new PropertyTree(widthType, widthValue); 
		motionKeyNodeTree.setCurrentSelect(motionICurrentSelect); 
		
		motionKeyNodeTree.setSelectClass(ElfNode.class, false, elfList); 
		motionKeyNodeTree.setSelectClass(ElfMotionKeyNode.class, false, motionKeyList); 
		this.checkInTree(motionKeyNodeTree); 
		
		final PropertyTree boneNodeTree = new PropertyTree(widthType, widthValue); 
		boneNodeTree.setCurrentSelect(motionICurrentSelect); 
		
		boneNodeTree.setSelectClass(ElfNode.class, false, elfList); 
		boneNodeTree.setSelectClass(BoneNode.class, false, boneList); 
		this.checkInTree(boneNodeTree); 
		
		final PropertyTree motionNodeTree = new PropertyTree(widthType, widthValue); 
		motionNodeTree.setCurrentSelect(motionICurrentSelect); 
		
		motionNodeTree.setSelectClass(ElfNode.class, false, elfList); 
		motionNodeTree.setSelectClass(ElfMotionNode.class, false, motionList); 
		this.checkInTree(motionNodeTree); 
	} 
	
	final ICurrentSelect motionICurrentSelect = new ICurrentSelect() {
		public Object[] getSelectObjs(boolean gloabl) {
			//
			final TimeData timeData = TimeData.getSelectTimeData();
			if(timeData != null) {
				final ElfMotionKeyNode key = timeData.getBindKeyNode();
				if(key != null) { 
					return new Object[]{key}; 
				} 
			} 
			
			return ICurrentSelect.CurrentSelect_Default.getSelectObjs(gloabl) ; 
		}
	};
	
	public PropertyTree findRightTree(final Class<?> select) { 
		if(motionICurrentSelect.getSelectObjs(false) != null) { 
			if (select != null) { 
				final LinkedList<PropertyTree> results = new LinkedList<PropertyTree>();
				for (PropertyTree tree : mTrees) {
					final Class<?> treeClass = tree.getSelectClass();
					if (treeClass != null && treeClass.isAssignableFrom(select)) {
						results.add(tree);
					}
				}

				Collections.sort(results, new Comparator<PropertyTree>() {
					public int compare(PropertyTree arg0, PropertyTree arg1) {
						if (arg0.getSelectClass() == arg1.getSelectClass()) {
							return 0;
						} else if (arg0.getSelectClass().isAssignableFrom(arg1.getSelectClass())) {
							return 1;
						} else {
							return -1;
						}
					}
				});

				if (results.size() > 0) {
					return results.getFirst();
				}
			}
			return null;
		} 
		return null;
	}
}
