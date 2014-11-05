package com.ielfgame.stupidGame.swf;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.TreeItem;

import com.ielfgame.stupidGame.dialog.MessageDialog;
import com.ielfgame.stupidGame.newNodeMenu.AbstractMenuItem;
import com.ielfgame.stupidGame.res.AbstractResWorkSpace;
import com.ielfgame.stupidGame.res.ResManager;

public class SwfResWorkSpace extends AbstractResWorkSpace  {

	public SwfResWorkSpace() {
		super("SWF");
	}

	public String getAssertDir() {
		return ResManager.getSingleton().getDesignerSWFAsset();
	}

	@Override
	public List<AbstractMenuItem> addMenu() {
		final AbstractMenuItem item = new AbstractMenuItem("Swf => Lua & Image") {
			public void onClick(SelectionEvent e) {
				final TreeItem[] items = mTree.getSelection();
				if (items != null && items.length > 0) {
					final TreeItem item = items[0];
					onDoubleClick(item);
				}
			}

			public boolean isShow() {
				final TreeItem[] items = mTree.getSelection();
				if (items != null && items.length > 0) {
					final TreeItem item = items[0];
					final String path = (String)item.getData();
					if(path != null && isMacthedFile(path)) {
						return true;
					}
				}
				return false;
			}
		};
		
		final LinkedList<AbstractMenuItem> list = new LinkedList<AbstractMenuItem>();
		
		list.add(getOpenMenu());
		list.add(null);
		list.add(item);
		
		return list;
	}

	public boolean isMacthedFile(String path) {
		if(path != null) {
			return path.endsWith(".swf");
		} else {
			return false;
		}
	}

	public void onDoubleClick(TreeItem item) {
		final String path = (String)item.getData();
		if(path != null && isMacthedFile(path)) {
			SwfSplit.split(path);
			
			final MessageDialog md = new MessageDialog();
			md.open("", "Swf 转化成功 ! ");
		}
	}
	
}
