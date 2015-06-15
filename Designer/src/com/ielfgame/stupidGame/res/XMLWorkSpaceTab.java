package com.ielfgame.stupidGame.res;

import java.io.BufferedWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.ielfgame.stupidGame.AbstractWorkSpaceTab;
import com.ielfgame.stupidGame.IconCache;
import com.ielfgame.stupidGame.MainDesigner;
import com.ielfgame.stupidGame.ResJudge;
import com.ielfgame.stupidGame.config.CurrentPlist;
import com.ielfgame.stupidGame.config.DoOnSave;
import com.ielfgame.stupidGame.data.ElfDataDisplay;
import com.ielfgame.stupidGame.data.SpellHelper;
import com.ielfgame.stupidGame.design.hotSwap.flash.FlashMainNode;
import com.ielfgame.stupidGame.design.hotSwap.flash.KeyFrameArrayNode;
import com.ielfgame.stupidGame.design.hotSwap.flash.KeyFrameNode;
import com.ielfgame.stupidGame.design.hotSwap.flash.KeyStorageNode;
import com.ielfgame.stupidGame.dialog.MessageDialog;
import com.ielfgame.stupidGame.dialog.MultiLineDialog;
import com.ielfgame.stupidGame.dialog.PopDialog;
import com.ielfgame.stupidGame.fileBar.CopyPanel;
import com.ielfgame.stupidGame.imExport.ImExport;
import com.ielfgame.stupidGame.newNodeMenu.AbstractMenu;
import com.ielfgame.stupidGame.newNodeMenu.AbstractMenuItem;
import com.ielfgame.stupidGame.platform.PlatformHelper;
import com.ielfgame.stupidGame.power.PowerMan;
import com.ielfgame.stupidGame.res.TreeHelper.ISearchTreeItem;
import com.ielfgame.stupidGame.trans.TransferRes;
import com.ielfgame.stupidGame.utils.FileHelper;
import com.ielfgame.stupidGame.xml.XMLVersionManage;
import com.ielfgame.stupidGame.zip.ZipUtils;

public class XMLWorkSpaceTab extends AbstractWorkSpaceTab {
	protected static class Search extends ElfDataDisplay {
		public String key;
	}

	private static Search sSearch = new Search();

	private Tree mTree;
	private IconCache mIconCache;
	private TreeItem mLastSelectItem = null;

	public static class New_XML extends ElfDataDisplay {
		public String name = "null";
	};

	public XMLWorkSpaceTab() {
		super("XML");
		this.setInterval(2000);
	}

	@Override
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
					if (ResJudge.isXML(path)) {
						ImExport.imports(path, true);
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

	private void openSearch() {
		final MultiLineDialog md = new MultiLineDialog();
		final Object[] objs = md.open(sSearch);
		if (objs != null && objs.length == 1) {
			sSearch.key = (String) objs[0];

			final LinkedList<TreeItem> result = search(sSearch.key);
			if (result.isEmpty()) {
				final MessageDialog warning = new MessageDialog();
				warning.open("warning", "No XML Found By Key:" + sSearch.key);
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

	private void buildTree(final Tree tree) {
		// clean
		final TreeItem[] items = tree.getItems();
		for (TreeItem item : items) {
			item.dispose();
		}

		final String path = ResManager.getSingleton().getDesignerXMLAsset();
		if (path != null) {
			createItem(new TreeItem(mTree, 0), path);
		}
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
				if (ResJudge.isXML(fname) || (f.isDirectory() && !fname.startsWith("."))) {
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
					System.err.println("TreeItem :" + childFile.getAbsolutePath());
					createItem(new TreeItem(myItem, 0, ii), childFile.getAbsolutePath());
				}
			}
		}
	}

	public void update() {
		final String assetPath = ResManager.getSingleton().getDesignerXMLAsset();
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
					if (ResJudge.isXML(sub)) {
						createItem(new TreeItem(myItem, 0), sub);
					}
				}
			}
		}
	}

	private final void setItemImage(final TreeItem item) {
		if (item != null && !item.isDisposed()) {

			final String path = (String) item.getData();

			if (ResJudge.isXML(path)) {
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

	private final void setMenu() {
		final AbstractMenu rootMenu = new AbstractMenu(null) {
			public void onClick(SelectionEvent e) {
			}
		};

		final AbstractMenuItem openMenu = new AbstractMenuItem(FileHelper.IS_WINDOWS ? "Show In Explorer" : "Show In Finder") {
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

		final AbstractMenuItem newXMLMenu = new AbstractMenuItem("New XML") {
			public void onClick(SelectionEvent e) {
				final TreeItem[] items = mTree.getSelection();
				if (items != null && items.length > 0) {
					final TreeItem item = items[0];

					final PopDialog pd = new PopDialog(new New_XML());
					final String[] ret = pd.open();
					if (ret != null && ret.length > 0) {
						String name = ret[0];
						if (name != null) {
							if (!name.endsWith(".xml")) {
								name = name + ".xml";
							}

							final String dir = ResManager.getSingleton().getDesignerXMLAsset();
							final LinkedList<String> xmlList = FileHelper.getSimplePahIds(dir, new String[] { ".xml" }, true);

							final HashSet<String> xmlSet = new HashSet<String>(xmlList);
							if (xmlSet.contains(name)) {
								MessageDialog dm = new MessageDialog();
								dm.open("Warnings", name + " Has Already Existed!");
								onClick(e);
							} else {
								final String path = (String) item.getData();
								XMLVersionManage.writeToXML(new LinkedList<Object>(), path + FileHelper.DECOLLATOR + name);
							}
						}
					}

				}

			}

			public boolean isShow() {
				final String dir = ResManager.getSingleton().getDesignerXMLAsset();
				final File f = new File(dir);
				if (f.exists() && f.isDirectory()) {
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

		// final AbstractMenuItem saveMenu = new AbstractMenuItem("Save All") {
		// public void onClick(SelectionEvent e) {
		//
		// final MessageDialog makesure = new MessageDialog();
		// boolean ret = makesure.open("warnings!",
		// "Are You Sure To Save All XMLs?\n(It will discard your modifier to current XML!)");
		//
		// if(!ret) {
		// return ;
		// }
		//
		// final boolean isXCodeSupported =
		// ResManager.getSingleton().getXCodeRootAssetSupported();
		//
		// final LinkedList<TreeItem> items = TreeHelper.getSearchList(mTree,
		// false);
		// for (TreeItem item : items) {
		// // open ?
		// final String path = (String) item.getData();
		// if (ResJudge.isXML(path)) {
		// final DoOnSave onSaveConfig = PowerMan.getSingleton(DoOnSave.class);
		//
		// final List<Object> objs = XMLVersionManage.readFromXML(path);
		// final LinkedList<String> notFoundList = new
		// CopyPanel().fakeOpen(path);
		//
		// final String dir = FileHelper.getDirPath(path);
		// final String simple =
		// SpellHelper.getUpEname(FileHelper.getSimpleName(path));
		//
		// final String newPath = dir + FileHelper.DECOLLATOR +
		// simple.replace(".xml", ".cocos");
		//
		// if (isXCodeSupported && onSaveConfig.save_zip) {
		// XMLVersionManage.writeToCocos(objs, newPath);
		//
		// final String tmpDir =
		// ResManager.getSingleton().getDesignerTmpAsset();
		//
		// if (onSaveConfig.save_zip) {
		// final String zipDirPath =
		// ResManager.getSingleton().getXCodeZipAsset();
		// final String zipPath = zipDirPath + FileHelper.DECOLLATOR +
		// FileHelper.getSimpleNameNoSuffix(newPath) + ".cocos.zip";
		//
		// if (tmpDir != null) {
		// final File tmpFile = new File(tmpDir);
		// if (tmpFile.exists() && tmpFile.isDirectory()) {
		// TransferRes.copyFileQuickly(new File(zipPath), new File(tmpDir),
		// FileHelper.getSimpleNameNoSuffix(newPath) + ".cocos.zip");
		// }
		// }
		//
		// final File zipDir = new File("" + zipDirPath);
		// if (zipDir.exists() && zipDir.isDirectory()) {
		// try {
		// ZipUtils.zip(newPath, zipPath);
		// } catch (Exception e2) {
		// e2.printStackTrace();
		// }
		// }
		// }
		//
		// new File(newPath).delete();
		// }
		//
		// //
		// if (!notFoundList.isEmpty()) {
		// final String[] notFoundArray = new String[notFoundList.size()];
		// notFoundList.toArray(notFoundArray);
		//
		// for (int i = 0; i < notFoundArray.length; i++) {
		// String resid = notFoundArray[i];
		// if (CurrentPlist.isInPlist(resid)) {
		// resid = "No Worries:" + resid + " is in plist!";
		// }
		// notFoundArray[i] = resid;
		// }
		//
		// Arrays.sort(notFoundArray);
		//
		// final AnalysisDialog<String[]> dialog = new
		// AnalysisDialog<String[]>(""+FileHelper.getSimpleName(path)+":Not Existed Resids("
		// + notFoundArray.length + ")", false);
		// dialog.open(notFoundArray, String[].class);
		// }
		//
		// XMLVersionManage.writeToXML(objs, path);
		// }
		// }
		//
		// {
		// MessageBox box = new
		// MessageBox(PowerMan.getSingleton(MainDesigner.class).mShell,
		// SWT.ICON_INFORMATION | SWT.OK);
		// box.setText("Save");
		// box.setMessage("Save All" + " completed!");
		//
		// box.open();
		// }
		// }
		//
		// public boolean isShow() {
		// return true;
		// }
		// };

		final AbstractMenuItem saveMenuFolder = new AbstractMenuItem("Save Folder Zips") {
			public void onClick(SelectionEvent e) {

				final TreeItem[] myitems = mTree.getSelection();
				if (myitems == null || myitems.length == 0) {
					return;
				}

				final boolean isXCodeSupported = ResManager.getSingleton().getXCodeRootAssetSupported();

				TreeItem[] items = myitems[0].getItems();

				for (TreeItem item : items) {
					// open ?
					final String path = (String) item.getData();
					if (ResJudge.isXML(path)) {
						final DoOnSave onSaveConfig = PowerMan.getSingleton(DoOnSave.class);

						final List<Object> objs = XMLVersionManage.readFromXML(path);
						final LinkedList<String> notFoundList = new CopyPanel().fakeOpen(path);

						final String dir = FileHelper.getDirPath(path);
						final String simple = SpellHelper.getUpEname(FileHelper.getSimpleName(path));

						final String newPath = dir + FileHelper.DECOLLATOR + simple.replace(".xml", ".cocos");

						if (isXCodeSupported && onSaveConfig.save_zip) {
							XMLVersionManage.writeToCocos(objs, newPath);

							final String tmpDir = ResManager.getSingleton().getDesignerTmpAsset();

							if (onSaveConfig.save_zip) {
								final String zipDirPath = ResManager.getSingleton().getXCodeZipAsset();
								final String zipPath = zipDirPath + FileHelper.DECOLLATOR + FileHelper.getSimpleNameNoSuffix(newPath) + ".cocos.zip";

								if (tmpDir != null) {
									final File tmpFile = new File(tmpDir);
									if (tmpFile.exists() && tmpFile.isDirectory()) {
										TransferRes.copyFileQuickly(new File(zipPath), new File(tmpDir), FileHelper.getSimpleNameNoSuffix(newPath) + ".cocos.zip");
									}
								}

								final File zipDir = new File("" + zipDirPath);
								if (zipDir.exists() && zipDir.isDirectory()) {
									try {
										ZipUtils.zip(newPath, zipPath);
									} catch (Exception e2) {
										e2.printStackTrace();
									}
								}
							}

							new File(newPath).delete();
						}

						//
						if (!notFoundList.isEmpty()) {
							final String[] notFoundArray = new String[notFoundList.size()];
							notFoundList.toArray(notFoundArray);

							for (int i = 0; i < notFoundArray.length; i++) {
								String resid = notFoundArray[i];
								if (CurrentPlist.isInPlist(resid)) {
									resid = "No Worries:" + resid + " is in plist!";
								}
								notFoundArray[i] = resid;
							}

							Arrays.sort(notFoundArray);
							
							System.err.println("Not Found Res:");
							for(final String resid : notFoundArray) {
								System.err.println(resid);
							}
//							final AnalysisDialog<String[]> dialog = new AnalysisDialog<String[]>("" + FileHelper.getSimpleName(path) + ":Not Existed Resids(" + notFoundArray.length + ")", false);
//							dialog.open(notFoundArray, String[].class);
						}
						// XMLVersionManage.writeToXML(objs, path);
					}
				}

				{
					MessageBox box = new MessageBox(PowerMan.getSingleton(MainDesigner.class).mShell, SWT.ICON_INFORMATION | SWT.OK);
					box.setText("Save");
					box.setMessage("Save All" + " completed!");

					box.open();
				}
			}

			public boolean isShow() {
				final TreeItem[] myitems = mTree.getSelection();
				if (myitems == null || myitems.length == 0) {
					return false;
				}
				return true;
			}
		};

		final AbstractMenuItem boneMenuFolder = new AbstractMenuItem("生成骨骼动作时间表") {
			public void onClick(SelectionEvent e) {
				final TreeItem[] myitems = mTree.getSelection();
				if (myitems == null || myitems.length == 0) {
					return;
				}

				final LinkedList<String> lines = new LinkedList<String>();

				TreeItem[] items = myitems[0].getItems();
				for (TreeItem item : items) {
					// open ?
					final String path = (String) item.getData();
					if (ResJudge.isXML(path)) {
						final List<Object> objs = XMLVersionManage.readFromXML(path);
						if (objs != null && !objs.isEmpty()) {
							for (Object obj : objs) {
								if (obj instanceof FlashMainNode) {
									final FlashMainNode fmn = (FlashMainNode) obj;
									lines.add(fmn.toLuaString(FileHelper.getSimpleNameNoSuffix(path)));
									break;
								}
							}
						}
					}
				}

				if (!lines.isEmpty()) {
					try {
						final BufferedWriter bWriter = FileHelper.getUTF8Writer(ResManager.getSingleton().getXCodeScriptAsset(), "BonesData.lua");

						bWriter.write("local _table = {");
						bWriter.newLine();

						for (int i = 0; i < lines.size(); i++) {
							String line = lines.get(i);
							bWriter.write("\t");
							bWriter.write(String.format("[%d] = ", (i + 1)));
							bWriter.write(line);
							bWriter.write(",");
							bWriter.newLine();
						}

						bWriter.write("}");
						bWriter.newLine();
						bWriter.write("return _table");
						bWriter.newLine();

						bWriter.flush();

						bWriter.close();

						MessageDialog md = new MessageDialog();
						md.open("", "生成骨骼数据成功!");

					} catch (Exception e2) {
						e2.printStackTrace();
					}
				} else {
					MessageDialog md = new MessageDialog();
					md.open("", "没有找到骨骼数据!");
				}
			}

			public boolean isShow() {
				final boolean isXCodeSupported = ResManager.getSingleton().getXCodeRootAssetSupported();
				final TreeItem[] myitems = mTree.getSelection();
				if (myitems == null || myitems.length == 0 || !isXCodeSupported) {
					return false;
				}
				return true;
			}
		};

		final AbstractMenuItem fixMenuFolder = new AbstractMenuItem("修复死亡动作") {
			public void onClick(SelectionEvent e) {
				final TreeItem[] myitems = mTree.getSelection();
				if (myitems == null || myitems.length == 0) {
					return;
				}

				TreeItem[] items = myitems[0].getItems();
				for (TreeItem item : items) {
					// open ?
					final String path = (String) item.getData();
					if (ResJudge.isXML(path)) {
						final List<Object> objs = XMLVersionManage.readFromXML(path);
						if (objs != null && !objs.isEmpty()) {
							for (Object obj : objs) {
								boolean changed = false;
								
								if (obj instanceof FlashMainNode) {
									final FlashMainNode fmn = (FlashMainNode) obj;
									final KeyStorageNode ksn = fmn.findKeyStorageByName("死亡");
									if(ksn != null) {
										final KeyFrameArrayNode[] kfans = ksn.getKeyFrameArrayNodes();
										for(KeyFrameArrayNode kfan : kfans) {
											final KeyFrameNode[] kfns = kfan.getFlashKeys();
											if(kfns != null && kfns.length > 0) {
												KeyFrameNode kfn = kfns[kfns.length-1];
												if(kfn.getVisible() || kfn.getAlpha() > 0) {
													kfn.setAlpha(0);
													kfn.setVisible(false);
													changed = true;
												}
											}
										}
									}
									
									if(changed) {
										XMLVersionManage.writeToXML(objs, path);
									}
									
									break;
								}
							}
						}
					}
				}
				
				MessageDialog md = new MessageDialog();
				md.open("", "修复死亡动作成功!");
			}

			public boolean isShow() {
				final boolean isXCodeSupported = ResManager.getSingleton().getXCodeRootAssetSupported();
				final TreeItem[] myitems = mTree.getSelection();
				if (myitems == null || myitems.length == 0 || !isXCodeSupported) {
					return false;
				}
				return true;
			}
		};

		rootMenu.checkInMenuItem(openMenu);
		rootMenu.checkInMenuItem(null);
		rootMenu.checkInMenuItem(newXMLMenu);
		rootMenu.checkInMenuItem(null);
		rootMenu.checkInMenuItem(findMenu);
		rootMenu.checkInMenuItem(null);
		// rootMenu.checkInMenuItem(saveMenu);
		rootMenu.checkInMenuItem(saveMenuFolder);
		rootMenu.checkInMenuItem(null);
		rootMenu.checkInMenuItem(boneMenuFolder);
		rootMenu.checkInMenuItem(null);
		rootMenu.checkInMenuItem(fixMenuFolder);
		

		final Menu menu = rootMenu.create(mTree.getShell());

		mTree.setMenu(menu);
	}
}
