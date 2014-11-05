package com.ielfgame.stupidGame.face.basic;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import org.eclipse.swt.widgets.Composite;


public class EmptyTrees {
	protected final LinkedList<PropertyTree> mTrees = new LinkedList<PropertyTree>();
	
	public EmptyTrees() {
	}

	public void checkInTree(final PropertyTree tree) { 
		mTrees.add(tree); 
	} 

	public void createTrees(final Composite parent) { 
		for (PropertyTree tree : mTrees) {
			tree.createTree(parent);
		}
	}

	public PropertyTree findRightTree(final Class<?> select) { 
		
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
}
