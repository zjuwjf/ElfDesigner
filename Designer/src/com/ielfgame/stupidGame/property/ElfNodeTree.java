package com.ielfgame.stupidGame.property;


import com.ielfgame.stupidGame.face.basic.PropertyTree;

import elfEngine.basic.node.ElfNode;

public class ElfNodeTree extends PropertyTree{
	
	public ElfNodeTree(final boolean isGlobal, int widthType, int widthValue) {
		super(widthType, widthValue);
		setSelectClass(ElfNode.class, isGlobal);
	} 
}
