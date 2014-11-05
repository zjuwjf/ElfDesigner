package com.ielfgame.stupidGame.res;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.ielfgame.stupidGame.AbstractWorkSpaceTab;
import com.ielfgame.stupidGame.IconCache;
import com.ielfgame.stupidGame.MainDesigner;
import com.ielfgame.stupidGame.data.ElfDataDisplay;
import com.ielfgame.stupidGame.dialog.MessageDialog;
import com.ielfgame.stupidGame.dialog.MultiLineDialog;
import com.ielfgame.stupidGame.newNodeMenu.AbstractMenu;
import com.ielfgame.stupidGame.newNodeMenu.AbstractMenuItem;
import com.ielfgame.stupidGame.platform.PlatformHelper;
import com.ielfgame.stupidGame.power.PowerMan;
import com.ielfgame.stupidGame.res.TreeHelper.ISearchTreeItem;
import com.ielfgame.stupidGame.utils.FileHelper;

public abstract class AbstractResWorkSpace extends AbstractWorkSpaceTab {
	protected Tree mTree;
	protected IconCache mIconCache;
	protected TreeItem mLastSelectItem = null;
	
	public AbstractResWorkSpace(String text) {
		super(text);
	}

	public void update() {
		final String assetPath = getAssertDir();
		if (assetPath != null) {
			if (mTree.getItemCount() > 0) {
				updateItemDeep(mTree.getItem(0), assetPath);
			} else {
				updateItemDeep(new TreeItem(mTree, 0), assetPath);
			}
		} else {
			final TreeItem[] items = mTree.getItems();
			for (TreeItem item : items) {
				if (!item.isDisposed()) {
					item.dispose();
				}
			}
		}
	}

	public Composite createTab(CTabFolder parent) {
		mIconCache = PowerMan.getSingleton(MainDesigner.class).getIconCache();

		mTree = new Tree(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		mTree.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
			}

			public void widgetDefaultSelected(SelectionEvent event) {
				
				final TreeItem[] selection = mTree.getSelection();
				if (selection != null && selection.length != 0) {
					final TreeItem item = selection[0];
					// item.setExpanded(!item.getExpanded());
					// open ?
					final String path = (String) item.getData();
					if (isMacthedFile(path)) {
						onDoubleClick(item);
						mLastSelectItem = item;
					}
				}
			}
		});

		mTree.addKeyListener(new KeyListener() {
			public void keyReleased(KeyEvent e) {
			}

			public void keyPressed(KeyEvent e) {
				switch (e.keyCode) {
				case 'F':
				case 'f':
					if ((e.stateMask & PlatformHelper.CTRL) != 0) {
						openSearch();
					}
					break;
				}
			}
		});

		buildTree(mTree);
		setMenu();
		run();

		return mTree;
	}
	
	private void updateItemDeep(final TreeItem myItem, final String path) {
		myItem.setData(path);
		myItem.setText(FileHelper.getSimpleNameWithSuffix(path));
		setItemImage(myItem);

		final TreeItem[] childs = myItem.getItems();

		File[] sortedFiles = new File(path).listFiles();

		if (sortedFiles == null) {
			sortedFiles = new File[0];
		} else {
			final ArrayList<File> array = new ArrayList<File>();
			for (File f : sortedFiles) {
				final String fname = f.getName();
				if (isMacthedFile(fname) || (f.isDirectory() && !fname.startsWith(".") )) {
					array.add(f);
				}
			}
			sortedFiles = new File[array.size()];
			array.toArray(sortedFiles);
		}

		final HashSet<String> set = new HashSet<String>();
		for (final TreeItem ti : childs) {
			set.add((String) ti.getData());
		}

		final HashSet<String> sameSet = new HashSet<String>();
		for (File file : sortedFiles) {
			final String fullName = file.getAbsolutePath();
			if (set.contains(fullName)) {
				sameSet.add(fullName);
			}
		}

		for (int i = 0; i < childs.length; i++) {
			final TreeItem childItem = childs[i];
			final String childPath = (String) childItem.getData();
			if (sameSet.contains(childPath)) {
				updateItemDeep(childItem, childPath);
			} else {
				childItem.dispose();
			}
		}

		for (int i = 0; i < sortedFiles.length; i++) {
			final File childFile = sortedFiles[i];
			final String fullPath = childFile.getAbsolutePath();
			if (!sameSet.contains(fullPath)) {
				final String shortName = childFile.getName();
				if (!shortName.startsWith(".")) {
					final int ii = i;
					createItem(new TreeItem(myItem, 0, ii), childFile.getAbsolutePath());
				}
			}
		}
	}
	
	protected void setItemImage(final TreeItem item) {
		if (item != null && !item.isDisposed()) {

			final String path = (String) item.getData();

			if (isMacthedFile(path)) {
				if (mLastSelectItem == item) {
					item.setImage(mIconCache.stockImages[mIconCache.iconSelect]);
				} else {
					item.setImage(mIconCache.stockImages[mIconCache.iconFile]);
				}
			} else {
				item.setImage(mIconCache.stockImages[mIconCache.iconOpenFolder]);
			}
		}
	}
	
	private void buildTree(final Tree tree) {
		// clean
		final TreeItem[] items = tree.getItems();
		for (TreeItem item : items) {
			item.dispose();
		}

		final String path = getAssertDir();
		if (path != null) {
			createItem(new TreeItem(mTree, 0), path);
		}
	}
	
	private void createItem(final TreeItem myItem, final String path) {
		myItem.setData(path);
		myItem.setText(FileHelper.getSimpleNameWithSuffix(path));
		setItemImage(myItem);

		final File f = new File(path);

		if (f.exists() && f.isDirectory()) {
			final File[] fs = f.listFiles();
			for (File cf : fs) {
				final String sub = cf.getAbsolutePath();
				if (cf.isDirectory()) {
					if (!cf.getName().startsWith(".")) {
						createItem(new TreeItem(myItem, 0), sub);
					}
				} else {
					if (isMacthedFile(sub)) {
						createItem(new TreeItem(myItem, 0), sub);
					}
				}
			}
		}
	}
	
	private final void setMenu() {
		final AbstractMenu rootMenu = new AbstractMenu(null) {
			public void onClick(SelectionEvent e) {
			}
		};
		
		List<AbstractMenuItem> list = addMenu();
		if(list != null) {
			for(AbstractMenuItem m : list) {
				rootMenu.checkInMenuItem(m);
			}
		}

		final Menu menu = rootMenu.create(mTree.getShell());

		mTree.setMenu(menu);
	}
	
	protected static class Search extends ElfDataDisplay {
		public String key;
	}

	private static Search sSearch = new Search();
	
	private void openSearch() {
		final MultiLineDialog md = new MultiLineDialog();
		final Object[] objs = md.open(sSearch);
		if (objs != null && objs.length == 1) {
			sSearch.key = (String) objs[0];

			final LinkedList<TreeItem> result = search(sSearch.key);
			if (result.isEmpty()) {
				final MessageDialog warning = new MessageDialog();
				warning.open("warning", "No Found By Key:" + sSearch.key);
			} else {
				final TreeItem item = result.getFirst();
				mTree.select(item);
				mTree.showItem(item);
			}
		}
	}
	
	private LinkedList<TreeItem> search(final String key) {
		return TreeHelper.search(mTree, false, new ISearchTreeItem() {
			public boolean match(TreeItem item) {
				final String text = item.getText();
				if (text.contains(key)) {
					return true;
				}
				return false;
			}
		});
	}
	
	protected final AbstractMenuItem getOpenMenu() {
		return new AbstractMenuItem(FileHelper.IS_WINDOWS ? "Show In Explorer" : "Show In Finder") {
			public void onClick(SelectionEvent e) {
				final TreeItem[] items = mTree.getSelection();
				if (items != null && items.length > 0) {
					final TreeItem item = items[0];
					ResPackerHelper.openExplorer((String) item.getData());
				}
			}

			public boolean isShow() {
				final TreeItem[] items = mTree.getSelection();
				if (items != null && items.length > 0) {
					return true;
				}
				return false;
			}
		};
	}
	
	public abstract String getAssertDir();
	
	public abstract List<AbstractMenuItem> addMenu() ;
	
	public abstract boolean isMacthedFile(String path);
	
	public abstract void onDoubleClick(TreeItem item);
	

}
