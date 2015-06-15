package com.ielfgame.stupidGame.res;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import com.ielfgame.stupidGame.AbstractWorkSpaceTab;
import com.ielfgame.stupidGame.IconCache;
import com.ielfgame.stupidGame.MainDesigner;
import com.ielfgame.stupidGame.config.ProjectSetting;
import com.ielfgame.stupidGame.data.ElfDataDisplay;
import com.ielfgame.stupidGame.dialog.MessageDialog;
import com.ielfgame.stupidGame.dialog.MultiLineDialog;
import com.ielfgame.stupidGame.language.LanguageFromPlist;
import com.ielfgame.stupidGame.newNodeMenu.AbstractMenu;
import com.ielfgame.stupidGame.newNodeMenu.AbstractMenuItem;
import com.ielfgame.stupidGame.platform.PlatformHelper;
import com.ielfgame.stupidGame.power.PowerMan;
import com.ielfgame.stupidGame.res.TreeHelper.ISearchTreeItem;
import com.ielfgame.stupidGame.utils.FileHelper;

public class LanguageWorkSpaceTab extends AbstractWorkSpaceTab {
	
	protected static class Search extends ElfDataDisplay{
		public String value;
	}
	private static final Search sSearch = new Search();
	
	protected static class SearchKey extends ElfDataDisplay{
		public String key;
	}
	private static final SearchKey sSearchKey = new SearchKey();
	
	protected class KeyValue extends ElfDataDisplay{
		public String key;
		public String value;
		
		public KeyValue(TreeItem item) {
			if(item != null) {
				String key = item.getText();
				while((item = item.getParentItem()) != null) {
					key = item.getText()+"$" + key;
				}
				
				this.key = key;
				this.value = mKey2ValueMap.get(key);
			}
		}
	}

	private Tree mTree;
	private IconCache mIconCache;
	
	private final String mLanguagePlist;
	private final HashMap<String, String> mKey2ValueMap = new HashMap<String, String>();
	
	/*add by te */
	private final String mLanguagePlistInP;
	private final String mEditMode;
	private HashMap<String,String> mRecordKey2Oper;
	/**/
	
	public LanguageWorkSpaceTab() {
		super("Language");
		this.setInterval(2000);
		
		mLanguagePlistInP = ResManager.getSingleton().getXCodeLanguagePlistAsset();
		mEditMode = PowerMan.getSingleton(ProjectSetting.class).langEditMode;
		if(mEditMode.equals("Mode1")){
			mLanguagePlist = mLanguagePlistInP;
		}else{
			mLanguagePlist = ResManager.getSingleton().getDesignerLanguagePlistAsset();
		}
		
		
		mRecordKey2Oper = new HashMap<String, String>();
	}
	
	@Override
	public Composite createTab(CTabFolder parent) {
		mIconCache = PowerMan.getSingleton(MainDesigner.class).getIconCache();
		
		mTree = new Tree(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		mTree.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				final TreeItem[] selection = mTree.getSelection();
				if (selection != null && selection.length != 0) {
					TreeItem item = selection[0];
					item.setExpanded(!item.getExpanded());
				}
			}
			
			public void widgetDefaultSelected(SelectionEvent event) {
				final TreeItem[] selection = mTree.getSelection();
				if (selection != null && selection.length != 0) {
					TreeItem item = selection[0];
					final String key = (String)item.getData("key");
					final String value = (String)item.getData("value");
					if(key != null && value != null) {
						final KeyValue kv = new KeyValue(item);
						
						final MultiLineDialog md = new MultiLineDialog();
						final Object [] objs = md.open(kv);
						if(objs != null && objs.length == 2) {
							if( !edit(kv.key, (String)objs[0], (String)objs[1]) ) {
								final MessageDialog message = new MessageDialog();
								message.open("Warning!", "Key:"+objs[0]+" has existed!");
							}
						}
					}
				}

				event.doit = false;
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
				case 'K':
				case 'k':
					if ((e.stateMask & PlatformHelper.CTRL) != 0) {
						openSearchByKey();
					}
					break;
				}
			}
		});
		
		final String[] columnTitles = new String[] { "key", "value" };

		for (int i = 0; i < columnTitles.length; i++) {
			TreeColumn treeColumn = new TreeColumn(mTree, SWT.NONE);
			treeColumn.setText(columnTitles[i]);
			treeColumn.setMoveable(true);
//			treeColumn.setWidth(200);
		}
		mTree.setHeaderVisible(true);
		
		try {
			buildTree();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		final int columnCount = mTree.getColumnCount();
		for (int i = 0; i < columnCount; i++) {
			TreeColumn treeColumn = mTree.getColumn(i);
			treeColumn.pack();
		}
		
		setMenu();
		setDND();
		run();

		return mTree;
	}
	
	private TreeItem getItemByName(Tree tree, TreeItem parent, String name) {
		final TreeItem itemRet;
		final TreeItem [] items = ((parent == null)?tree.getItems():parent.getItems());
		
		int index = 0;
		for(TreeItem item : items) {
			final String lastName = item.getText();
			if(lastName != null && lastName.equals(name)) {
				return item;
			} else {
				if(lastName.compareTo(name) < 0) {
					index++;
				}
			}
		}
		
		itemRet = ((parent == null) ? new TreeItem(tree, 0, index):new TreeItem(parent, 0, index));
		itemRet.setText(new String[]{name, ""});
		
		return itemRet;
	}
	
	private void addItemByKeyValue(Tree tree, final String key, final String value, boolean show) {
		final String [] sp = key.split("\\$");
		TreeItem root = null;
		for(int i=0; i<sp.length; i++) {
			final String name = sp[i];
			root = getItemByName(tree, root, name);
			if(i==sp.length-1) {
				root.setData("key", key);
				root.setData("value", mKey2ValueMap.get(key));
				root.setText(new String[]{name, mKey2ValueMap.get(key)});
				root.setImage(mIconCache.stockImages[mIconCache.iconFile]);
				
				if(show) {
					tree.setSelection(root);
					tree.showItem(root);
				}

			} else {
				root.setImage(mIconCache.stockImages[mIconCache.iconClosedFolder]);
			}
		}
	}
	
	private void removeItemByKeyValue(Tree tree, final String key) {
		final String [] sp = key.split("\\$");
		TreeItem root = null;
		
		final LinkedList<TreeItem> list = new LinkedList<TreeItem>();
		
		for(int i=0; i<sp.length; i++) {
			final String name = sp[i];
			root = getItemByName(tree, root, name);
			list.add(root);
		}
		
		while(!list.isEmpty()) {
			final TreeItem item = list.pop();
			if(item.getData("key") != null) {
				item.dispose();
			} else if(item.getItemCount() <= 0) {
				item.dispose();
			}
		}
	}
	
	private void buildTree() {
		try {
			final Tree tree = mTree;
			// clean
			final TreeItem[] items = tree.getItems();
			for (TreeItem item : items) {
				item.dispose();
			}
			
			final HashMap<String, String> map = LanguageFromPlist.readKeyValueFromPlist(new File(mLanguagePlist));
			mKey2ValueMap.clear();
			mKey2ValueMap.putAll(map);
			recordClear();
			
			final Set<String> keySet = map.keySet();
			final ArrayList<String> array = new ArrayList<String>(keySet);
			Collections.sort(array);
			
			plistChanged();
			
			for(final String key : array) {
				addItemByKeyValue(tree,key,mKey2ValueMap.get(key), false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void update() {
	}
	
	public String key2value(String key) {
		return mKey2ValueMap.get(key);
	}
	
	private final void setMenu() {
		final AbstractMenu rootMenu = new AbstractMenu(null) {
			public void onClick(SelectionEvent e) {
			}
		};

		final AbstractMenuItem addMenu = new AbstractMenuItem("New") {
			public void onClick(SelectionEvent e) {
				final TreeItem item = mTree.getSelection()[0];
				final KeyValue kv = new KeyValue(item);
				
				final MultiLineDialog md = new MultiLineDialog();
				final Object [] objs = md.open(kv);
				if(objs != null && objs.length == 2) {
					if( !addKey((String)objs[0], (String)objs[1]) ) {
						final MessageDialog message = new MessageDialog();
						message.open("Warning!", "Key:"+objs[0]+" has existed!");
					}
				}
			}
			public boolean isShow() {
				final TreeItem [] items = mTree.getSelection();
				return items != null && items.length > 0;
			}
		};
		
		final AbstractMenuItem removeMenu = new AbstractMenuItem("Delete") {
			public void onClick(SelectionEvent e) {
				final TreeItem item = mTree.getSelection()[0];
				final KeyValue kv = new KeyValue(item);
				remove(kv.key);
			}
			public boolean isShow() {
				final TreeItem [] items = mTree.getSelection();
				return items != null && items.length > 0;
			}
		};
		
		final AbstractMenuItem editMenu = new AbstractMenuItem("Edit") {
			public void onClick(SelectionEvent e) {
				final TreeItem item = mTree.getSelection()[0];
				final KeyValue kv = new KeyValue(item);
				
				final MultiLineDialog md = new MultiLineDialog();
				final Object [] objs = md.open(kv);
				if(objs != null && objs.length == 2) {
					if( !edit(kv.key, (String)objs[0], (String)objs[1]) ) {
						final MessageDialog message = new MessageDialog();
						message.open("Warning!", "Key:"+objs[0]+" has existed!");
					}
				}
			}
			public boolean isShow() {
				final TreeItem [] items = mTree.getSelection();
				return items != null && items.length > 0;
			}
		};
		
		final AbstractMenuItem searchMenu = new AbstractMenuItem("Find\tF") {
			public void onClick(SelectionEvent e) {
				openSearch();
			}
			public boolean isShow() {
				return true;
			}
		};
		
		final AbstractMenuItem searchKeyMenu = new AbstractMenuItem("Find By Key\tK") {
			public void onClick(SelectionEvent e) {
				openSearchByKey();
			}
			public boolean isShow() {
				return true;
			}
		};
		
		final AbstractMenuItem rebuildMenu = new AbstractMenuItem("Rebuild") {
			public void onClick(SelectionEvent e) {
				buildTree();
			}
			public boolean isShow() {
				return true;
			}
		};

		rootMenu.checkInMenuItem(addMenu);
		rootMenu.checkInMenuItem(removeMenu);
		rootMenu.checkInMenuItem(editMenu);
		rootMenu.checkInMenuItem(null);
		rootMenu.checkInMenuItem(searchMenu);
		rootMenu.checkInMenuItem(searchKeyMenu);
		rootMenu.checkInMenuItem(null);
		rootMenu.checkInMenuItem(rebuildMenu);
		
		final Menu menu = rootMenu.create(mTree.getShell());
		mTree.setMenu(menu);
	}
	
	private LinkedList<TreeItem> search(final String key) {
		return TreeHelper.search(mTree, false, new ISearchTreeItem() {
			public boolean match(TreeItem item) {
				final String value = (String)item.getData("value");
				if(value != null && value.contains(key)) {
					return true;
				}
				return false;
			}
		});
	}
	
	private LinkedList<TreeItem> searchKey(final String key) {
		return TreeHelper.search(mTree, false, new ISearchTreeItem() {
			public boolean match(TreeItem item) {
				final String value = item.getText();
				if(value != null && value.contains(key)) {
					return true;
				}
				return false;
			}
		});
	}
	
	private void openSearch() {
		final MultiLineDialog md = new MultiLineDialog();
		final Object [] objs = md.open(sSearch);
		if(objs!=null && objs.length==1) {
			sSearch.value = (String)objs[0];
			
			final LinkedList<TreeItem> result = search(sSearch.value);
			if(result.isEmpty()) {
				final MessageDialog warning = new MessageDialog();
				warning.open("warning", "No Key Found By Value:"+sSearch.value);
			} else {
				final TreeItem item = result.getFirst();
				mTree.select(item);
				mTree.showItem(item);
			}
		}
	}
	
	private void openSearchByKey() {
		final MultiLineDialog md = new MultiLineDialog();
		final Object [] objs = md.open(sSearchKey);
		if(objs!=null && objs.length==1) {
			sSearchKey.key = (String)objs[0];
			
			final LinkedList<TreeItem> result = searchKey(sSearchKey.key);
			if(result.isEmpty()) {
				final MessageDialog warning = new MessageDialog();
				warning.open("warning", "No Item Found By Key:"+sSearchKey.key);
			} else {
				final TreeItem item = result.getFirst();
				mTree.select(item);
				mTree.showItem(item);
			}
		}
	}
	
	private void setDND() {
		DragSource dragSource = new DragSource(mTree, DND.DROP_MOVE | DND.DROP_COPY);
		dragSource.setTransfer(new Transfer[] { FileTransfer.getInstance() });
		dragSource.addDragListener(new DragSourceAdapter() {
			public void dragSetData(DragSourceEvent event) {
				final TreeItem[] items = mTree.getSelection();
				if (items != null && items.length > 0) {
					final String key = (String)items[0].getData("key");
					final String value = (String)items[0].getData("value");
					if(key != null && value != null) {
						event.data = new String[] { key };
					} else {
						event.detail = DND.DROP_NONE;
					}
				} else {
					event.detail = DND.DROP_NONE;
				}
			}
		});
	}
	
	private void plistChanged() {
		LanguageFromPlist.writePlist(mEditMode,mLanguagePlist, mLanguagePlistInP,mKey2ValueMap,mRecordKey2Oper);
		recordClear();
	}
	
	private boolean addKey(String key, String value) {
		final HashMap<String, String> map = mKey2ValueMap;
		
		if(!map.containsKey(key)) {
			if(key.contains("$")) {
				map.put(key, value);
				addItemByKeyValue(mTree, key, value, true);
				recordAddKey(key);
				plistChanged();
			} else {
				final MessageDialog md = new MessageDialog();
				md.open("warning", key+" must contains $!");
			}
			return true;
		}
		
		return false;
	}
	
	private boolean edit(final String oldKey, final String newKey, final String newValue) {
		final HashMap<String, String> map = mKey2ValueMap;
		
		if(oldKey.equals(newKey)) {
			map.remove(oldKey);
			removeItemByKeyValue(mTree, oldKey);
			recordRemoveKey(oldKey);
			
			map.put(newKey, newValue);
			addItemByKeyValue(mTree, newKey, newValue, true);
			recordAddKey(newKey);
			
			plistChanged();
			
			return true;
		} else if(!map.containsKey(newKey)) {
			if(newKey.contains("$")) {
				map.put(newKey, newValue);
				addItemByKeyValue(mTree, newKey, newValue, true);
				recordAddKey(newKey);
				
				map.remove(oldKey);
				removeItemByKeyValue(mTree, oldKey);
				recordRemoveKey(oldKey);
				
				plistChanged();
			} else {
				final MessageDialog md = new MessageDialog();
				md.open("Warning", newKey+" must contains $!");
			}
			
			return true;
		} 
		
		return false;
	}
	
	private void remove(String key) {
		mKey2ValueMap.remove(key);
		removeItemByKeyValue(mTree, key);
		recordRemoveKey(key);
		
		plistChanged();
	}

	private void recordAddKey(String key){
		mRecordKey2Oper.put(key, "A");
	}
	private void recordRemoveKey(String key){
		mRecordKey2Oper.put(key,"R");
	}
	private void recordClear(){
		mRecordKey2Oper.clear();
	}
	
	public static void main(String [] args) {
		String path = "D:\\opencv\\build\\x86\\vc10\\lib";
		LinkedList<String> list = FileHelper.getSimplePahIds(path, new String[]{".lib"}, false);
		for(String s : list) {
			System.err.println(s);
		}
	}
}
