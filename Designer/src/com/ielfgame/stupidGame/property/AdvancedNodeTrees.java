package com.ielfgame.stupidGame.property;

import java.util.Set;
import java.util.Stack;

import com.ielfgame.stupidGame.face.basic.EmptyTrees;
import com.ielfgame.stupidGame.face.basic.PropertyTree;
import com.ielfgame.stupidGame.nodeMap.NodeMap;

import elfEngine.basic.node.ElfNode;

public class AdvancedNodeTrees extends EmptyTrees { 
	
	public AdvancedNodeTrees(final boolean isGolbal, final int widthType, final int widthValue) {
		this(isGolbal, widthType, widthValue, false);
	} 
	
	public AdvancedNodeTrees(final boolean isGolbal, final int widthType, final int widthValue, boolean withElfNode) {
		final Set<String> set = NodeMap.getKeySet();
		for(final String key : set) {
			Class<?> _class = NodeMap.getNodeClass(key);
			final Stack<Class<?>> stack = new Stack<Class<?>>();
			
			while(_class != null) {
				if(withElfNode) {
					stack.add(_class);
					if(_class == ElfNode.class) {
						break;
					} 
					_class = _class.getSuperclass();
				} else {
					if(_class != ElfNode.class) { 
						stack.add(_class); 
						_class = _class.getSuperclass(); 
					} else { 
						break;
					} 
				}
			} 
			
			final PropertyTree nodeTree = new PropertyTree(widthType, widthValue); 
			while(!stack.isEmpty()) { 
				final Class<?> addClass = stack.pop(); 
				nodeTree.setSelectClass(addClass, isGolbal); 
			} 
			
			if(nodeTree.getSelectClass() != null) { 
				AdvancedNodeTrees.this.checkInTree(nodeTree); 
			} 
		} 
	}
} 
