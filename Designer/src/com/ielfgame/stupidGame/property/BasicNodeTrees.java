package com.ielfgame.stupidGame.property;

import com.ielfgame.stupidGame.face.basic.EmptyTrees;

public class BasicNodeTrees extends EmptyTrees{
	public BasicNodeTrees(boolean isGlobal, int widthType, int widthValue) {
		this.checkInTree(new ElfNodeTree(isGlobal, widthType, widthValue)); 
	} 
} 
