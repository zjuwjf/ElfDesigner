package com.ielfgame.stupidGame.nodeMap;

import java.util.HashMap;
import java.util.Set;

import elfEngine.basic.node.ElfNode;

public class NodeMap {
	private final static HashMap<String, Class<? extends ElfNode>> sMap = new HashMap<String, Class<? extends ElfNode>>();
	
	static {
		sMap.clear();
	}
	
	public static void checkIn(Class<? extends ElfNode> _class) {
		if(_class != null) {
			sMap.put(_class.getSimpleName(), _class); 
		} 
	} 

	public static Set<String> getKeySet() {
		return sMap.keySet();
	}
	
	public static Class<? extends ElfNode> getNodeClass(final String key) {
		return sMap.get(key);
	} 
} 
