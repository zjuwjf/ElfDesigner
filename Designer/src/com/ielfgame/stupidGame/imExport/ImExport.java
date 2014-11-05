package com.ielfgame.stupidGame.imExport;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

import com.ielfgame.stupidGame.Constants;
import com.ielfgame.stupidGame.MainDesigner;
import com.ielfgame.stupidGame.StatusViewWorkSpace;
import com.ielfgame.stupidGame.NodeView.NodeViewWorkSpaceTab;
import com.ielfgame.stupidGame.animation.Animate;
import com.ielfgame.stupidGame.config.CurrentPlist;
import com.ielfgame.stupidGame.config.DefaultBatch;
import com.ielfgame.stupidGame.config.DoOnSave;
import com.ielfgame.stupidGame.config.LuaTempleConfig;
import com.ielfgame.stupidGame.data.DataModel;
import com.ielfgame.stupidGame.data.SpellHelper;
import com.ielfgame.stupidGame.design.hotSwap.flash.FlashManager;
import com.ielfgame.stupidGame.dialog.AnalysisDialog;
import com.ielfgame.stupidGame.dialog.MessageDialog;
import com.ielfgame.stupidGame.face.action.ActionTree;
import com.ielfgame.stupidGame.face.action.ActionWorkSpaceTab;
import com.ielfgame.stupidGame.face.action.CCActionData;
import com.ielfgame.stupidGame.fileBar.CopyPanel;
import com.ielfgame.stupidGame.lua.ConvertXMLToLua;
import com.ielfgame.stupidGame.power.PowerMan;
import com.ielfgame.stupidGame.res.ResManager;
import com.ielfgame.stupidGame.trans.TransferRes;
import com.ielfgame.stupidGame.undo.UndoRedoManager;
import com.ielfgame.stupidGame.utils.FileHelper;
import com.ielfgame.stupidGame.xml.XMLVersionManage;
import com.ielfgame.stupidGame.zip.ZipUtils;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.ElfNode.IIterateChilds;
import elfEngine.opengl.GLManage;

public class ImExport {

	private final static String doBatch() {
		try {
			System.err.println("doBatch");
			return PowerMan.getSingleton(DefaultBatch.class).getBatch();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Batch failed! Check log.";
	}

	public static void exportsXMLOnly(final String path, final boolean batch) {
		final List<Object> exports = new LinkedList<Object>();
		final ElfNode screenNode = PowerMan.getSingleton(DataModel.class).getRootScreen();

		screenNode.iterateChilds(new IIterateChilds() {
			public boolean iterate(ElfNode node) {
				exports.add(node);
				return false;
			}
		});

		final ArrayList<Animate> animateList = PowerMan.getSingleton(DataModel.class).getAnimateList();
		for (Animate animate : animateList) {
			exports.add(animate);
		}

		final ActionTree ActionTree = PowerMan.getSingleton(ActionWorkSpaceTab.class).getActionTree();

		final Object[] actions = ((CCActionData) (ActionTree.getData(ActionTree.getRootItems()[0]))).getChildsForXML();
		for (int i = 0; i < actions.length; i++) {
			exports.add(actions[i]);
		}

		XMLVersionManage.writeToXML(exports, path);
		PowerMan.getSingleton(DataModel.class).setLastImportPath(path);

		final String lastPath = PowerMan.getSingleton(DataModel.class).getLastImportPath();
		final LinkedList<String> notFoundList = new CopyPanel().fakeOpen(lastPath);

		// final String trans =
		// path.substring(path.lastIndexOf(FileHelper.DECOLLATOR) + 1,
		// path.length());
		// String newPath =
		// PowerMan.getSingleton(DataModel.class).getTransferDir() +
		// FileHelper.DECOLLATOR + trans;
		// newPath = newPath.replace(".xml", ".cocos");
		// newPath = SpellHelper.getUpEname(newPath);
		// XMLVersionManage.writeToCocos(exports, newPath);
		// ConvertXMLToLua.convert(newPath, FileHelper.getDirPath(newPath));

		String batchRet = null;
		if (batch) {
			batchRet = doBatch();
		}

		//
		if (!notFoundList.isEmpty()) {
			final String[] notFoundArray = new String[notFoundList.size()];
			notFoundList.toArray(notFoundArray);
			final AnalysisDialog<String[]> dialog = new AnalysisDialog<String[]>("Not Existed Resids(" + notFoundArray.length + ")", false);
			dialog.open(notFoundArray, String[].class);
		}

		{
			MessageBox box = new MessageBox(PowerMan.getSingleton(MainDesigner.class).mShell, SWT.ICON_INFORMATION | SWT.OK);
			box.setText("Save");
			if (batchRet == null) {
				box.setMessage("Save " + path + " completed!");
			} else {
				box.setMessage("Save " + path + " completed!\n" + batchRet);
			}

			box.open();
		}
	}

	public static void newProject(final String path) {
		PowerMan.getSingleton(DataModel.class).getScreenNode().clearUpdateList();

		GLManage.clear();

		PowerMan.getSingleton(NodeViewWorkSpaceTab.class).rebuild();
		PowerMan.getSingleton(DataModel.class).getScreenNode().getTabNodeManage().clear();

		final ArrayList<Animate> animateList = PowerMan.getSingleton(DataModel.class).getAnimateList();
		animateList.clear();

		PowerMan.getSingleton(DataModel.class).clearCCActionDataList();
		final ActionTree actionTree = PowerMan.getSingleton(ActionWorkSpaceTab.class).getActionTree();
		actionTree.rebuild();

		PowerMan.getSingleton(DataModel.class).setLastImportPath(path);
	}

	public static void exports(final String path, final boolean batch) {
		final boolean isXCodeSupported = ResManager.getSingleton().getXCodeRootAssetSupported();

		final DoOnSave onSaveConfig = PowerMan.getSingleton(DoOnSave.class);

		final List<Object> exports = new LinkedList<Object>();
		final ElfNode screenNode = PowerMan.getSingleton(DataModel.class).getRootScreen();

		screenNode.iterateChilds(new IIterateChilds() {
			public boolean iterate(ElfNode node) {
				exports.add(node);
				return false;
			}
		});

		final ArrayList<Animate> animateList = PowerMan.getSingleton(DataModel.class).getAnimateList();
		for (Animate animate : animateList) {
			exports.add(animate);
		}

		final ActionTree ActionTree = PowerMan.getSingleton(ActionWorkSpaceTab.class).getActionTree();
		final Object[] actions = ((CCActionData) (ActionTree.getData(ActionTree.getRootItems()[0]))).getChildsForXML();
		for (int i = 0; i < actions.length; i++) {
			exports.add(actions[i]);
		}

		XMLVersionManage.writeToXML(exports, path);
		PowerMan.getSingleton(DataModel.class).setLastImportPath(path);

		final String lastPath = PowerMan.getSingleton(DataModel.class).getLastImportPath();

		final LinkedList<String> notFoundList = new CopyPanel().fakeOpen(lastPath);

		final String dir = FileHelper.getDirPath(path);
		final String simple = SpellHelper.getUpEname(FileHelper.getSimpleName(path));

		final String newPath = dir + FileHelper.DECOLLATOR + simple.replace(".xml", ".cocos");

		if (isXCodeSupported && (onSaveConfig.save_zip || onSaveConfig.save_template)) {
			XMLVersionManage.writeToCocos(exports, newPath);

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
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			// lua
			final LuaTempleConfig luaTempleConfig = PowerMan.getSingleton(LuaTempleConfig.class);
			if (onSaveConfig.save_template && luaTempleConfig != null && luaTempleConfig.LuaTemple != null) {

				final String outDirPath;
				final String[] subs;

				if (luaTempleConfig.LuaTemple.isForCPlusPlus()) {
					outDirPath = ResManager.getSingleton().getXCodeClassesAsset();
					subs = new String[] { ".h", ".cpp" };
				} else {
					outDirPath = ResManager.getSingleton().getXCodeScriptAsset();
					subs = new String[] { ".lua" };
				}

				// search
				if (outDirPath != null) {
					final LinkedList<String> allScripts = FileHelper.getFullPahIds(outDirPath, subs, true);

					String realOutDirPath = null;

					for (final String sub : subs) {
						final String templateSimpleName = FileHelper.getSimpleNameNoSuffix(newPath) + sub;

						for (String scriptpath : allScripts) {
							final String simpleName = FileHelper.getSimpleName(scriptpath);
							if (templateSimpleName.equals(simpleName)) {
								realOutDirPath = FileHelper.getDirPath(scriptpath);
								break;
							}
						}

						if (realOutDirPath == null) {
							realOutDirPath = outDirPath;
						}

						final String luaPath = realOutDirPath + FileHelper.DECOLLATOR + templateSimpleName;
						if (tmpDir != null) {
							final File tmpFile = new File(tmpDir);
							if (tmpFile.exists() && tmpFile.isDirectory()) {
								TransferRes.copyFileQuickly(new File(luaPath), new File(tmpDir), templateSimpleName);
							}
						}
					}

					final File luaDir = new File(realOutDirPath);
					if (luaDir.exists() && luaDir.isDirectory()) {
						ConvertXMLToLua.convert(newPath, realOutDirPath);
					} else {
						new MessageDialog().open("Warning", realOutDirPath + " Is Not A Template Directory !");
					}
				} else {
					new MessageDialog().open("Warning", "Check Your Template Directory !");
				}
			}

			new File(newPath).delete();
		}

		String batchRet = null;
		if (batch) {
			batchRet = doBatch();
		}

		if (isXCodeSupported && onSaveConfig.save_language) {
			ResManager.publishLanguage();
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

			final AnalysisDialog<String[]> dialog = new AnalysisDialog<String[]>("Not Existed Resids(" + notFoundArray.length + ")", false);
			dialog.open(notFoundArray, String[].class);
		}

		{
			MessageBox box = new MessageBox(PowerMan.getSingleton(MainDesigner.class).mShell, SWT.ICON_INFORMATION | SWT.OK);
			box.setText("Save");
			if (batchRet == null) {
				box.setMessage("Save " + path + " completed!");
			} else {
				box.setMessage("Save " + path + " completed!\n" + batchRet);
			}

			box.open();
		}
	}

	public static void imports(final String path, boolean cleanUp) {
		UndoRedoManager.clear();
		PowerMan.getSingleton(FlashManager.class).bindFlashMain(null);
		
		final LoadPanel loadPanel = new LoadPanel();
		final ImportRun importRun = new ImportRun(path, cleanUp, true);

		if (importRun.mAllResids == null) {
			return;
		}

		final File file = new File("" + path);

		if (file.exists()) {
			PowerMan.getSingleton(DataModel.class).getScreenNode().runWithDelay(new Runnable() {
				public void run() {
					importRun.sRunBefore.run();
				}
			}, 100);

			loadPanel.open(PowerMan.getSingleton(MainDesigner.class).mShell, importRun);
		}
	}

	public static void add(final String path) {
		final LoadPanel loadPanel = new LoadPanel();
		final ImportRun importRun = new ImportRun(path, false, false);

		if (importRun.mAllResids == null) {
			return;
		}

		final File file = new File("" + path);

		if (file.exists()) {
			PowerMan.getSingleton(DataModel.class).getScreenNode().runWithDelay(new Runnable() {
				public void run() {
					importRun.sRunLoad.run();
				}
			}, 100);

			loadPanel.open(PowerMan.getSingleton(MainDesigner.class).mShell, importRun);
		}
	}

	public static List<Object> readObjs(final String path) {
		final List<Object> list = XMLVersionManage.readFromXML(path);
		return list;
	}

	public static void writeObjs(List<Object> objs, final String path) {
		XMLVersionManage.writeToXML(objs, path);
	}
}

class ImportRun {
	public int mLoadCount = 0;
	public ArrayList<String> mAllResids;
	public final String mPath;
	final boolean mCleanUp;
	final boolean mResetPath;

	public ImportRun(final String path, boolean cleanUp, boolean reset) {
		mPath = path;
		mCleanUp = cleanUp;
		mResetPath = reset;
		try {
			mAllResids = new ArrayList<String>();
			mAllResids.addAll(XMLVersionManage.getAllResids(path));
		} catch (Exception e) {
		}
	}

	public final Runnable sRunBefore = new Runnable() {
		public void run() {
			if (mAllResids != null) {
				PowerMan.getSingleton(DataModel.class).getScreenNode().setVisible(false);
				PowerMan.getSingleton(DataModel.class).getScreenNode().clearUpdateList();

				if (mCleanUp) {
					GLManage.clear();
				}

				PowerMan.getSingleton(NodeViewWorkSpaceTab.class).rebuild();
				PowerMan.getSingleton(DataModel.class).getScreenNode().getTabNodeManage().clear();

				final ArrayList<Animate> animateList = PowerMan.getSingleton(DataModel.class).getAnimateList();
				animateList.clear();

				if (mResetPath) {
					PowerMan.getSingleton(DataModel.class).clearCCActionDataList();
					PowerMan.getSingleton(ActionTree.class).rebuild();
				}

				final ElfNode node = PowerMan.getSingleton(DataModel.class).getScreenNode();
				node.runWithDelay(sRunLoad, 100);
			}
		}
	};

	final int mStep = 2;

	public final Runnable sRunLoad = new Runnable() {
		public void run() {
			if (mAllResids != null) {
				final int size = mAllResids.size();
				final ElfNode node = PowerMan.getSingleton(DataModel.class).getScreenNode();
				node.runWithDelay(new Runnable() {
					public void run() {
						for (int i = 0; i < mStep && mLoadCount < size; i++, mLoadCount++) {
							final String resid = mAllResids.get(mLoadCount);
							GLManage.loadTextureRegion(resid, node.getGLId());
						}
					}
				}, 0);

				if (mLoadCount < size) {
					node.runWithDelay(sRunLoad, 1);
				} else {
					node.runWithDelay(sRunAfter, 1);
				}
			}
		}
	};

	private final Runnable sRunAfter = new Runnable() {
		public void run() {
			PowerMan.getSingleton(DataModel.class).getScreenNode().setVisible(true);
			final ArrayList<Animate> animateList = PowerMan.getSingleton(DataModel.class).getAnimateList();

			final String path = mPath;

			final List<Object> list = XMLVersionManage.readFromXML(path);

			for (Object o : list) {
				if (o instanceof ElfNode) {
					PowerMan.getSingleton(NodeViewWorkSpaceTab.class).addToScreenNode((ElfNode) o, Integer.MAX_VALUE);
				} else if (o instanceof Animate) {
					animateList.add((Animate) o);
				} else if (o instanceof CCActionData) {
					final CCActionData data = (CCActionData) o;
					PowerMan.getSingleton(DataModel.class).addCCActionData(data);
					data.setTargetByFullNames();
				}
			}

			final ActionTree actionTree = PowerMan.getSingleton(ActionWorkSpaceTab.class).getActionTree();
			actionTree.rebuild();

			// PowerMan.getSingleton(AnimationWorkSpaceTab.class).rebuild();

			PowerMan.getSingleton(StatusViewWorkSpace.class).getLable(0).setText("Import:" + path + " succeed!");

			if (mResetPath) {
				System.err.println("setLastImportPath " + path);
				PowerMan.getSingleton(DataModel.class).setLastImportPath(path);
			}
			
			/***
			 * auto bind flashnode
			 */
			final ElfNode elfnode = PowerMan.getSingleton(DataModel.class).getScreenNode().getBindNode();
			elfnode.autoBindFlash();
			
			// add heros
			// createHeroList();
			System.gc();
		}
	};
}

class LoadPanel {

	public void open(final Shell parent, final ImportRun importRun) {
		final Shell shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.PRIMARY_MODAL);
		// RunState.initChildShell(shell);
		// final Shell shell = new Shell(parent);
		shell.setLayout(new GridLayout());
		shell.setText("Load Resource " + importRun.mPath);

		final Composite composite0 = new Composite(shell, SWT.NONE);
		final GridData gridData0 = new GridData(GridData.FILL_HORIZONTAL);
		gridData0.widthHint = 500;
		composite0.setLayoutData(gridData0);

		final Label leftLabel = new Label(composite0, SWT.LEFT | SWT.WRAP);
		leftLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		leftLabel.setSize(500, 20);

		leftLabel.setText("  loading...");

		final Composite composite1 = new Composite(shell, SWT.NONE);
		final GridData gridData1 = new GridData(GridData.FILL_HORIZONTAL);
		gridData1.widthHint = 500;
		composite1.setLayoutData(gridData1);
		final GridLayout layout1 = new GridLayout();
		layout1.numColumns = 2;
		composite1.setLayout(layout1);

		final ProgressBar pb1 = new ProgressBar(composite1, SWT.HORIZONTAL | SWT.SMOOTH);
		pb1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		final ArrayList<String> res = importRun.mAllResids;
		final int size = importRun.mAllResids.size();

		pb1.setMinimum(0);
		pb1.setSelection(0);
		pb1.setMaximum(size);

		final Button cancelButton = new Button(composite1, SWT.PUSH);
		cancelButton.setEnabled(false);
		cancelButton.setText(Constants.POP_DIALOG_CANCEL);
		cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});

		if (size > 0) {
			leftLabel.setText("  loading " + res.get(0));
		}

		final Thread thread = new Thread(new Runnable() {
			private int mLastCount = 0;

			public void run() {
				while (importRun.mLoadCount < size) {
					if (shell == null || shell.isDisposed()) {
						break;
					}

					try {
						Thread.sleep(5);
					} catch (Exception e) {
						e.printStackTrace();
					}

					if (mLastCount != importRun.mLoadCount) {
						mLastCount = importRun.mLoadCount;
						shell.getDisplay().asyncExec(new Runnable() {
							public void run() {
								try {
									leftLabel.setText("  loading " + res.get(mLastCount));
									pb1.setSelection(mLastCount + 1);
								} catch (Exception e) {
//									e.printStackTrace();
								}
							}
						});
					}
				}

				shell.getDisplay().asyncExec(new Runnable() {
					public void run() {
						if (shell != null && !shell.isDisposed()) {
							shell.close();
						}
					}
				});
			}
		});

		thread.start();

		shell.pack();
		shell.setLocation(shell.getDisplay().getPrimaryMonitor().getClientArea().width / 2 - shell.getSize().x / 2, shell.getDisplay().getPrimaryMonitor().getClientArea().height / 2 - shell.getSize().y / 2);

		shell.open();
		final Display display = shell.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}
	
}
