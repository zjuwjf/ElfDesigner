package com.ielfgame.stupidGame.res;

import java.util.LinkedList;

import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class TreeHelper {
	public interface ISearchTreeItem {
		public boolean match(TreeItem item);
	}
	
	private static LinkedList<TreeItem> getSearchList(TreeItem item) {
		final LinkedList<TreeItem> list = new LinkedList<TreeItem>();
		if(item != null) {
			
			list.add(item);
			
			if(item.getItemCount() > 0) {
				final TreeItem[] its = item.getItems();
				for(TreeItem it:its) {
					list.addAll(getSearchList(it));
				}
			}
		}
		return list;
	}
	
	public static LinkedList<TreeItem> getSearchList(final Tree tree, final boolean restart) {
		final LinkedList<TreeItem> list = new LinkedList<TreeItem>();
		final TreeItem [] its = tree.getItems();
		if(its != null) {
			for(TreeItem it : its) {
				list.addAll(getSearchList(it));
			}
		}
		
		if(!restart) {
			final TreeItem [] sels = tree.getSelection();
			if(sels!=null && sels.length>0) {
				final TreeItem sel = sels[sels.length-1];
				
				int count = list.size();
				while(count>0) {
					TreeItem it = list.removeFirst();
					list.addLast(it);
					if(it == sel) {
						break;
					}
					count--;
				}
			}
		}
		
		return list;
	}
	
	public static LinkedList<TreeItem> search(final Tree tree, final boolean restart, ISearchTreeItem search) {
		final LinkedList<TreeItem> list = getSearchList(tree,restart);
		
		final LinkedList<TreeItem> result = new LinkedList<TreeItem>();
		for(TreeItem it : list) {
			if(search.match(it)) {
				result.add(it);
			}
		}
		
		return result;
	}
	
}
