package com.ielfgame.stupidGame.res;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.StyleRange;
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
import com.ielfgame.stupidGame.ColorFactory;
import com.ielfgame.stupidGame.IconCache;
import com.ielfgame.stupidGame.MainDesigner;
import com.ielfgame.stupidGame.PreviewPictureTab;
import com.ielfgame.stupidGame.ResJudge;
import com.ielfgame.stupidGame.data.ElfDataDisplay;
import com.ielfgame.stupidGame.dialog.MessageDialog;
import com.ielfgame.stupidGame.dialog.MultiLineDialog;
import com.ielfgame.stupidGame.newNodeMenu.AbstractMenu;
import com.ielfgame.stupidGame.newNodeMenu.AbstractMenuItem;
import com.ielfgame.stupidGame.platform.PlatformHelper;
import com.ielfgame.stupidGame.power.PowerMan;
import com.ielfgame.stupidGame.res.TpConfigPanel.TpConfig;
import com.ielfgame.stupidGame.res.TreeHelper.ISearchTreeItem;
import com.ielfgame.stupidGame.shellRunner.ConsoleHandle;
import com.ielfgame.stupidGame.shellRunner.ShellRunner;
import com.ielfgame.stupidGame.utils.FileHelper;

public class ResWorkSpaceTab extends AbstractWorkSpaceTab {
	protected static class Search extends ElfDataDisplay{
		public String key;
	}
	
	private static Search sSearch = new Search();
	
	/***
	 * update
	 * 
	 * copy path
	 * 
	 * already in plist
	 * 
	 * 
	 */
	
	private Tree mTree;
	private IconCache mIconCache;

	static void setItemData(TreeItem item, String[] names) {
		item.setData(names);
	}

	static String getItemData(TreeItem item, final int index) {
		return ((String[]) item.getData())[index];
	}

	public ResWorkSpaceTab() {
		super("Res");
		this.setInterval(8000);
	}
	
//	public void update() {
//	}
	
	public void update() {
//		ResManager.updateImageResource();
		final HashSet<String> changedMap = ResManager.getChangedDirSet();
		
		final HashMap<String, File[]> dir2listfile = ResManager.getDir2ListfileMap();
		
		final HashSet<String> pngMap = ResManager.getPngDirSet();
		
		final String resAsset = ResManager.getSingleton().getDesignerImageAsset();
		if(resAsset != null) {
			if(mTree.getItemCount() > 0) {
				updateItemChilds(mTree.getItem(0), resAsset, changedMap, dir2listfile, pngMap);
			} else { 
				updateItemChilds(new TreeItem(mTree, 0), resAsset, changedMap, dir2listfile, pngMap);
			} 
		} 
	} 
	
	private void updateItemChilds(final TreeItem item, final String dirPath, final HashSet<String> changeMap, final HashMap<String, File[]> filemap, final HashSet<String> pngDirMap) {
		if (item != null && !item.isDisposed()) {
			// 100 ms
			refreshItemImageText(item, changeMap, pngDirMap);

			final TreeItem[] childs = item.getItems();
			final File[] fromMapfs = filemap.get(dirPath);
			final File[] sortedFiles = fromMapfs == null ? new File[0] : fromMapfs;

			final HashSet<String> set = new HashSet<String>();
			for (final TreeItem ti : childs) {
				set.add(getItemData(ti, 1));
			}

			final HashSet<String> sameSet = new HashSet<String>();
			for (File file : sortedFiles) {
				final String shortName = file.getName();
				if (set.contains(shortName)) {
					sameSet.add(shortName);
				}
			}

			for (int i = 0; i < childs.length; i++) {
				final TreeItem childItem = childs[i];
				final String shortPath = getItemData(childItem, 1);
				if (sameSet.contains(shortPath)) {
					updateItemChilds(childItem, getItemData(childItem, 0), changeMap, filemap, pngDirMap);
				} else {
					childItem.dispose();
				}
			}

			for (int i = 0; i < sortedFiles.length; i++) {
				final File childFile = sortedFiles[i];
				final String shortPath = childFile.getName();
				if (!sameSet.contains(shortPath)) {
					final int ii = i;
					createItem(new TreeItem(item, 0, ii), childFile.getAbsolutePath(), changeMap);
				}
			}
		}
	}

	@Override
	public Composite createTab(CTabFolder parent) {

		// final Shell shell =
		// PowerMan.getSingleton(MainDesigner.class).getShell();
		mIconCache = PowerMan.getSingleton(MainDesigner.class).getIconCache();

		mTree = new Tree(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI);

		mTree.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				final TreeItem[] selection = mTree.getSelection();
				if (selection != null && selection.length != 0) {
					TreeItem item = selection[0];
//					item.setExpanded(!item.getExpanded());
					
					final String path = getItemData(item, 0);
					if (ResJudge.isLegalResAndExisted(path)) {
						PreviewPictureTab prev = PowerMan.getSingleton(PreviewPictureTab.class);
//						ImageWindow.getSingleton().open(path, mTree);
						prev.setResid(path);
//						mResTree.forceFocus();
					}
				}
			}
			
			public void widgetDefaultSelected(SelectionEvent event) {
				final TreeItem[] selection = mTree.getSelection();
				if (selection != null && selection.length != 0) {
					TreeItem itemInit = selection[0];
					if (itemInit != null) {
						final String pathInit = getItemData(itemInit, 0);
						TpConfig tpc = ResPackerHelper.readPackerConfig(pathInit);
						if (tpc == null) {
							tpc = new TpConfig();
						}

						final TpConfigPanel tpcp = new TpConfigPanel();
						final TpConfig newTpc = tpcp.open(tpc, PowerMan.getSingleton(MainDesigner.class).getShell());
						
						if (newTpc != null) {
							for (TreeItem item : selection) {
								final String path = getItemData(item, 0);
								if (FileHelper.isDir(path)) {
									if (newTpc != null) {
										ResPackerHelper.writePackerConfig(path, newTpc);
										item.setData("tpc", newTpc);
									}
								}
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
				}
			}
		});
		

		final String[] columnTitles = new String[] { "file", "pack" };

		for (int i = 0; i < columnTitles.length; i++) {
			TreeColumn treeColumn = new TreeColumn(mTree, SWT.NONE);
			treeColumn.setText(columnTitles[i]);
			treeColumn.setMoveable(true);
		}
		mTree.setHeaderVisible(true);
		
		buildTree();

		final int columnCount = mTree.getColumnCount();
		for (int i = 0; i < columnCount; i++) {
			TreeColumn treeColumn = mTree.getColumn(i);
			treeColumn.pack();
			
			treeColumn.setWidth(200);
		}

		setDND();
		setMenu();
		
		run();
		
		return mTree;
	}
	
	private void openSearch() {
		final MultiLineDialog md = new MultiLineDialog();
		final Object [] objs = md.open(sSearch);
		if(objs!=null && objs.length==1) {
			sSearch.key = (String)objs[0];
			
			final LinkedList<TreeItem> result = search(sSearch.key);
			if(result.isEmpty()) {
				final MessageDialog warning = new MessageDialog();
				warning.open("warning", "No XML Found By Key:"+sSearch.key);
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
				if(text.contains(key)) {
					return true;
				}
				return false;
			}
		});
	}

	private void setDND() {
		DragSource dragSource = new DragSource(mTree, DND.DROP_MOVE | DND.DROP_COPY);
		dragSource.setTransfer(new Transfer[] { FileTransfer.getInstance() });
		dragSource.addDragListener(new DragSourceAdapter() {
			public void dragSetData(DragSourceEvent event) {
				final TreeItem[] items = mTree.getSelection();
				if (items != null && items.length > 0) {
					event.data = new String[] { getItemData(items[0], 0) };
				} else {
					event.detail = DND.DROP_NONE;
				}
			}
		});
	}

	private final void setMenu() {
		final AbstractMenu rootMenu = new AbstractMenu(null) {
			public void onClick(SelectionEvent e) {
			}
		};

//		final AbstractMenuItem refreshMenu = new AbstractMenuItem("刷新") {
//			public void onClick(SelectionEvent e) {
//				refresh();
//			}
//		};
		
//		final AbstractMenuItem hideMenu = new AbstractMenuItem("隐藏") {
//			public void onClick(SelectionEvent e) {
//				final TreeItem[] items = mTree.getItems();
//				for (TreeItem item : items) {
//					item.dispose();
//				}
//			}
//		};
		
		final AbstractMenuItem cleanMenu = new AbstractMenuItem("清理") {
			public void onClick(SelectionEvent e) {
				ResManager.setForceUpdate(true);
				
				final TreeItem[] items = mTree.getSelection();
				if (items != null && items.length > 0) {
					final TreeItem item = items[0];
					ResPackerHelper.cleanDirForPlist(new File(getItemData(item, 0)), false);
				}
			}
		};

		final AbstractMenuItem cleanDeepMenu = new AbstractMenuItem("深度清理") {
			public void onClick(SelectionEvent e) {
				ResManager.setForceUpdate(true);
				
				final TreeItem[] items = mTree.getSelection();
				if (items != null && items.length > 0) {
					final TreeItem item = items[0];
					ResPackerHelper.cleanDirForPlist(new File(getItemData(item, 0)), true);
				}
			}
		};

		final AbstractMenuItem runTpMenu = new AbstractMenuItem("资源打包") {
			public void onClick(SelectionEvent e) {
				
				ResManager.setForceUpdate(true);
				
				final TreeItem[] items = mTree.getSelection();
				if (items != null && items.length > 0) {
					final TreeItem item = items[0];
					final String dir = getItemData(item, 0);
					
					final Thread thread = new Thread() {
						public void run() {
							final ShellRunner sr = new ShellRunner();
							final ViewInfoDialog pid = ViewInfoDialog.getSingleton();
							sr.setConsoleHandle(new ConsoleHandle() {
								public void onOutRead(final String consoleMessage, final boolean isErrorOut) {
									if (isErrorOut) {
										final StyleRange sr = new StyleRange();
										sr.foreground = ColorFactory.RED;
										pid.appendInfo(consoleMessage, sr);
									} else {
										pid.appendInfo(consoleMessage, null);
									}
								}
							});

							final HashSet<String> changeMap = ResManager.getChangedDirSet();
							ResPackerHelper.runTexturePackerInDeep(sr, new File(dir), changeMap);

							final StyleRange styleRange = new StyleRange();
							styleRange.foreground = ColorFactory.GREEN;
							pid.appendInfo(String.format("\n\nResource %s\n Packer Completed!!!!", dir), styleRange);
						}
					};

					thread.start();
				}
			}
			
			public boolean isShow() {
				final TreeItem[] items = mTree.getSelection();
				if (items != null && items.length > 0) {
					final String path = getItemData(items[0], 0);
					if(FileHelper.isDir(path)) {
						return true;
					}
				}
				return false;
			}
		};
		
		final AbstractMenuItem etcAlpha = new AbstractMenuItem("ETC-ALPHA") {
			public void onClick(SelectionEvent e) {
				final TreeItem[] items = mTree.getSelection();
				if (items != null && items.length > 0) {
					final TreeItem item = items[0];
					ResPackerHelper.visitETCAlpha(getItemData(item, 0));
					
					MessageDialog md = new MessageDialog();
					md.open("", "ETC-Alpha Convert Successed!");
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

		final AbstractMenuItem openMenu = new AbstractMenuItem(FileHelper.IS_WINDOWS ? "Show In Explorer" : "Show In Finder") {
			public void onClick(SelectionEvent e) {
				ResManager.setForceUpdate(true);
				
				final TreeItem[] items = mTree.getSelection();
				if (items != null && items.length > 0) {
					final TreeItem item = items[0];
					ResPackerHelper.openExplorer(getItemData(item, 0));
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
		
		final AbstractMenuItem findMenu = new AbstractMenuItem("Find\tF") {
			public void onClick(SelectionEvent e) {
				openSearch();
			}

			public boolean isShow() {
				return true;
			}
		};
		
//		final AbstractMenuItem adjustMenu = new AbstractMenuItem("Adjust \"-\" to \"_\"") {
//			public void onClick(SelectionEvent e) {
//				final String resPath = ResManager.getSingleton().getDesignerImageAsset();
//				final String xmlPath = ResManager.getSingleton().getDesignerXMLAsset();
//				
//				final LinkedList<String> imageList = FileHelper.getFullPahIds(resPath, new String[]{".png",".jpg"}, true);
//				final LinkedList<String> xmlList = FileHelper.getFullPahIds(xmlPath, new String[]{".xml"}, true);
//				
//				final ViewInfoDialog vid = ViewInfoDialog.getSingleton();
//				
//				for(String fullname : imageList) { 
//					final String shortName = FileHelper.getSimpleName(fullname);
//					if(shortName.contains("-")) {
//						vid.appendInfo("rename "+shortName, null);
//						new File(fullname).renameTo(new File(new File(FileHelper.getDirPath(fullname)), shortName.replace("-", "_")));
//					}
//				} 
//				
//				final boolean old = PlatformHelper.getChangeUnderline();
//				PlatformHelper.setChangeUnderline(true);
//				
//				for(String xml : xmlList) {
//					final LinkedList<Object> objs = XMLVersionManage.readFromXML(xml);
//					XMLVersionManage.writeToXML(objs, xml);
//					vid.appendInfo("rename for "+xml, null);
//				}
//				
//				PlatformHelper.setChangeUnderline(old);
//				
//				vid.appendInfo("\nCompleted!", null);
//			}
//		};
		
//		rootMenu.checkInMenuItem(refreshMenu);
//		rootMenu.checkInMenuItem(hideMenu);
//		rootMenu.checkInMenuItem(null);
		rootMenu.checkInMenuItem(openMenu);
		rootMenu.checkInMenuItem(null);
		rootMenu.checkInMenuItem(runTpMenu);
		rootMenu.checkInMenuItem(null);
		rootMenu.checkInMenuItem(cleanMenu);
		rootMenu.checkInMenuItem(cleanDeepMenu);
		rootMenu.checkInMenuItem(null);
		rootMenu.checkInMenuItem(etcAlpha);
		rootMenu.checkInMenuItem(null);
		rootMenu.checkInMenuItem(findMenu);
		
		final Menu menu = rootMenu.create(mTree.getShell());

		mTree.setMenu(menu);
	}

	private void buildTree() {
		// clean
//		ResManager.updateImageResource();
		
		final TreeItem[] items = mTree.getItems();
		for (TreeItem item : items) {
			item.dispose();
		}
		
		final String resAsset = ResManager.getSingleton().getDesignerImageAsset();
		if(resAsset != null) {
			createItem(new TreeItem(mTree, 0), resAsset, null);
		}
		
		System.err.println("Res buildTree completed!");
	}

	private void createItem(final TreeItem myItem, final String path, final HashSet<String> changeMap) {
		final String[] names = { path, FileHelper.getSimpleName(path) };
		setItemData(myItem, names);

		myItem.setData("tpc", ResPackerHelper.readPackerConfig(path));
		
		refreshItemImageText(myItem, changeMap, null);

		final File f = new File(path);
		if (f.exists() && f.isDirectory() && !f.getName().startsWith(".")) {
			final File[] sortedFiles = ResPackerHelper.sortFile(path);
			for (final File cf : sortedFiles) {
				final String sub = cf.getAbsolutePath();
				if (cf.isDirectory()) {
					createItem(new TreeItem(myItem, 0), sub, changeMap);
				} else {
					createItem(new TreeItem(myItem, 0), sub, changeMap);
				}
			}
		}
	}
	
	private final void refreshItemImageText(final TreeItem item, final HashSet<String> changeMap, final HashSet<String> pngDirMap) {
		if (item != null && !item.isDisposed()) {
			final String path = getItemData(item, 0);
			final String name = getItemData(item, 1);

			if (ResJudge.isRes(path)) { 
				item.setImage(mIconCache.stockImages[mIconCache.iconPic]);
				item.setText(new String[] { name, "" });
			} else {
				boolean isPngDir = (pngDirMap == null ? ResPackerHelper.isDirHasimages(path) : pngDirMap.contains(path));
				if (isPngDir) {
					final TpConfig tpc = (TpConfig) item.getData("tpc");
					if (tpc != null) {
						item.setText(new String[] { name, tpc.toString() });
					} else {
						item.setText(new String[] { name, "default" });
					}

					final Boolean flag = ((changeMap == null) ? false : changeMap.contains(path));

					if (flag) {
						item.setImage(mIconCache.stockImages[mIconCache.iconWarning]);

						TreeItem parent = item.getParentItem();
						while (parent != null) {
							parent.setImage(mIconCache.stockImages[mIconCache.iconWarning]);
							parent = parent.getParentItem();
						}
					} else {
						item.setImage(mIconCache.stockImages[mIconCache.iconClosedTpFolder]);
					}
				} else {
					item.setImage(mIconCache.stockImages[mIconCache.iconClosedFolder]);
					item.setText(new String[] { name, "" });
				}
			}
		}
	}

}
