package com.ielfgame.stupidGame.fileBar;

import java.io.BufferedReader;
import java.io.File;
import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.ielfgame.stupidGame.MainDesigner;
import com.ielfgame.stupidGame.ResJudge;
import com.ielfgame.stupidGame.batch.ElfBatch;
import com.ielfgame.stupidGame.cocostudio.CocostudioFactory;
import com.ielfgame.stupidGame.config.ElfConfig;
import com.ielfgame.stupidGame.config.MenuConfig;
import com.ielfgame.stupidGame.data.DataModel;
import com.ielfgame.stupidGame.data.ElfDataDisplay;
import com.ielfgame.stupidGame.design.AutoJava;
import com.ielfgame.stupidGame.design.ElfClassLoader;
import com.ielfgame.stupidGame.dialog.AnalysisDialog;
import com.ielfgame.stupidGame.dialog.MultiLineDialog;
import com.ielfgame.stupidGame.imExport.ImExport;
import com.ielfgame.stupidGame.language.LanguageManager;
import com.ielfgame.stupidGame.lua.ToLuaMain;
import com.ielfgame.stupidGame.power.PowerMan;
import com.ielfgame.stupidGame.res.ResManager;
import com.ielfgame.stupidGame.texturePacker.pvr.TPTest;
import com.ielfgame.stupidGame.utils.FileHelper;

public class FileBar {

	public static void onShortcutKeys() {
		final BufferedReader reader = FileHelper.getReader(FileBar.class, "ShortcutKeys");
		if (reader != null) {
			String line = null;

			StringBuilder sb = new StringBuilder();

			try {
				while ((line = reader.readLine()) != null) {
					sb.append(line).append('\n');
				}
				reader.close();
			} catch (Exception e) {
			}

			final AnalysisDialog<String> dailog = new AnalysisDialog<String>("Shortcut Keys", false);

			dailog.open(sb.toString(), String.class);
		}
	}

	private final Shell mShell = PowerMan.getSingleton(MainDesigner.class).mShell;
	private final MenuConfig mMenuConfig = PowerMan.getSingleton(MenuConfig.class);

	public void createMenu(Menu parent) {
		createFileMenu(parent);
		createSetMenu(parent);
		createRunMenu(parent);

		//
		final Menu menu = ElfBatch.createToolMenu(mShell, parent);
		// convertProjectIOS2Android
		final MenuItem item4 = new MenuItem(menu, SWT.PUSH);
		item4.setText(LanguageManager.get("Res IOS To Android"));
		item4.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				try {
					TPTest.convertProjectIOS2Android();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});

		final MenuItem item5 = new MenuItem(menu, SWT.PUSH);
		item5.setText(LanguageManager.get("Copy To Android Asset"));
		item5.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				try {
					TPTest.copyToAsset();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});

		final MenuItem item6 = new MenuItem(menu, SWT.PUSH);
		item6.setEnabled(false);
		item6.setText(LanguageManager.get("Cocos2d To Lua"));
		item6.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				try {
					ToLuaMain.convert();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});
		
		
		//json2xml
		final MenuItem item7 = new MenuItem(menu, SWT.PUSH);
		item7.setText(LanguageManager.get("Cocostudio To XML"));
		
		item7.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				
				if(sCocostudioData.json_REF_DIR == null) {
					sCocostudioData.json_REF_DIR = ResManager.getSingleton().getDesignerRootAsset()+FileHelper.DECOLLATOR+"json";
				}
				if(sCocostudioData.xml_REF_DIR == null) {
					sCocostudioData.xml_REF_DIR = ResManager.getSingleton().getDesignerRootAsset()+FileHelper.DECOLLATOR+"xml";
				}
				
				final MultiLineDialog md = new MultiLineDialog();
				final Object [] objs = md.open(sCocostudioData);
				if(objs != null) {
					sCocostudioData.setValues(objs);
					final LinkedList<String> paths = FileHelper.getSimplePahIds(sCocostudioData.json_REF_DIR, new String[]{".json"}, false);
					for(String path:paths) {
						final String json = sCocostudioData.json_REF_DIR+FileHelper.DECOLLATOR+path;
						final String xml = sCocostudioData.xml_REF_DIR+FileHelper.DECOLLATOR+(path.replace(".json", ".xml"));
						CocostudioFactory.json2xml(json, xml);
					}
				}
			}
		});

		createHelpMenu(parent);
	}
	
	
	static final CocostudioData sCocostudioData = new CocostudioData();
	static class CocostudioData extends ElfDataDisplay {
		public String json_REF_DIR;
		public String xml_REF_DIR;
	}

	private void createFileMenu(Menu parent) {
		Menu menu = new Menu(parent);
		MenuItem header = new MenuItem(parent, SWT.CASCADE);
		header.setText(LanguageManager.get("File"));
		header.setMenu(menu);
		
//		final MenuItem newProject = new MenuItem(menu, SWT.PUSH);
//		newProject.setText(LanguageManager.get("New Project"));
//		newProject.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent e) {
//			}
//		});

		new MenuItem(menu, SWT.SEPARATOR);
		
//		final MenuItem cleanUp = new MenuItem(menu, SWT.CHECK);
//		cleanUp.setText(LanguageManager.get("CleanUp"));
//		cleanUp.setSelection(mMenuConfig.AutoCleanUpOnImport);
//		cleanUp.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent e) {
//				mMenuConfig.AutoCleanUpOnImport = cleanUp.getSelection();
//			}
//		});

		// import export
		final MenuItem item0 = new MenuItem(menu, SWT.PUSH);
		item0.setText(LanguageManager.get("Import"));
		item0.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(mShell, SWT.OPEN);
				fileDialog.setText("Import");
				
				fileDialog.setFilterExtensions(new String[] { "*.xml;", "*.*" });
				String path = fileDialog.open();

				if (path == null)
					return;
				File file = new File(path);
				if (!file.exists()) {
					return;
				}

				ImExport.imports(path, mMenuConfig.AutoCleanOnImport);
			}
		});

		// import export
		final MenuItem itemAdd = new MenuItem(menu, SWT.PUSH);
		itemAdd.setText(LanguageManager.get("Add"));
		itemAdd.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(mShell, SWT.OPEN);
				fileDialog.setText("Add");
				
				fileDialog.setFileName( ResManager.getSingleton().getDesignerXMLAsset() );
				fileDialog.setFilterExtensions(new String[] { "*.xml;", "*.*" });
				String path = fileDialog.open();

				if (path == null)
					return;
				File file = new File(path);
				if (!file.exists()) {
					return;
				}

				final String old = PowerMan.getSingleton(DataModel.class).getLastImportPath();

				ImExport.add(path);

				if (old != null) {
					PowerMan.getSingleton(DataModel.class).setLastImportPath(old);
				}
			}
		});

		new MenuItem(menu, SWT.SEPARATOR);

		final MenuItem item1 = new MenuItem(menu, SWT.PUSH);
		item1.setText(LanguageManager.get("Export"));
		item1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(mShell, SWT.SAVE);
				fileDialog.setText("Export");

				fileDialog.setFilterExtensions(new String[] { "*.xml;" });
				String path = fileDialog.open();
				if (path == null)
					return;

				if (!path.endsWith(".xml")) {
					path += ".xml";
				}

				ImExport.exports(path, mMenuConfig.AutoBatch);
			}
		});

		final MenuItem item2 = new MenuItem(menu, SWT.PUSH);
		item2.setText(LanguageManager.get("Save"));
		item2.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				final String path = PowerMan.getSingleton(DataModel.class).getLastImportPath();
				if (path != null) {
					ImExport.exports(path, mMenuConfig.AutoBatch);
				}
			}
		});

		new MenuItem(menu, SWT.SEPARATOR);

		final MenuItem item3 = new MenuItem(menu, SWT.PUSH);
		item3.setText(LanguageManager.get("Publish All"));
		item3.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				try {
					ResManager.getSingleton().publish();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});
		
		final MenuItem item4 = new MenuItem(menu, SWT.PUSH);
		item4.setText(LanguageManager.get("Publish All(encrypt-png)"));
		item4.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				try {
					ResManager.getSingleton().publishWithXXTea();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});
	}

	private void createSetMenu(Menu parent) {
		Menu menu = new Menu(parent);
		MenuItem header = new MenuItem(parent, SWT.CASCADE);
		header.setText(LanguageManager.get("Set"));
		header.setMenu(menu);
		
//		header.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent e) {
//				ElfConfig.openSetConfig();
//			}
//		});

		final MenuItem setConfig = new MenuItem(menu, SWT.PUSH);
		setConfig.setText(LanguageManager.get("Set Config"));
		setConfig.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				ElfConfig.openSetConfig();
			}
		});
	}

	private void createRunMenu(Menu parent) {
		Menu menu = new Menu(parent);
		MenuItem header = new MenuItem(parent, SWT.CASCADE);
		header.setText(LanguageManager.get("Run"));
		header.setMenu(menu);

		final MenuItem autoJava = new MenuItem(menu, SWT.PUSH);
		autoJava.setText("Refresh Code");
		autoJava.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				try {
					final String xmlPath = PowerMan.getSingleton(DataModel.class).getLastImportPath();
					AutoJava.autoJava(xmlPath);
				} catch (Exception e0) {
					e0.printStackTrace();
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});

		final MenuItem test = new MenuItem(menu, SWT.PUSH);
		test.setText("Run Surface");
		test.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("unchecked")
			public void widgetSelected(SelectionEvent e) {
				try {
					final ElfClassLoader ucl = ElfClassLoader.getClassLoader();
					System.gc();

					final Class<?> _class = ucl.loadClass("com.ielfgame.stupidGame.design.hotSwap.RunClassDelegate");

					if (Runnable.class.isAssignableFrom(_class)) {
						try {
							((Class<? extends Runnable>) _class).newInstance().run();
						} catch (Exception e1) {
						}
					}
				} catch (ClassNotFoundException e3) {
					e3.printStackTrace();
				}
			}
		});
		
		final MenuItem window = new MenuItem(menu, SWT.PUSH);
		window.setText("Run Window");
		window.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
//				final GLWindow gLWindow = new GLWindow();
//				gLWindow.open();
			}
		});
	}

	private void createHelpMenu(Menu parent) {
		Menu menu = new Menu(parent);
		MenuItem header = new MenuItem(parent, SWT.CASCADE);
		header.setText(LanguageManager.get("Help"));
		header.setMenu(menu);

		MenuItem item = new MenuItem(menu, SWT.PUSH);
		item.setText(LanguageManager.get("About"));
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {

				MessageBox box = new MessageBox(mShell, SWT.ICON_INFORMATION | SWT.OK);
				box.setText(ResJudge.getResourceString("dialog.About.title"));
				box.setMessage(ResJudge.getResourceString("dialog.About.description", new Object[] { System.getProperty("os.name") }));
				box.open();
			}
		});

		item = new MenuItem(menu, SWT.PUSH);
		item.setText(LanguageManager.get("Shortcut Keys"));
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				onShortcutKeys();
			}
		});
	}
}

