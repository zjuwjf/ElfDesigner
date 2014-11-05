package com.ielfgame.stupidGame;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;

import com.ielfgame.stupidGame.GLView.GLMainWindow;
import com.ielfgame.stupidGame.NodeView.NodeViewWorkSpaceTab;
import com.ielfgame.stupidGame.config.ElfConfig;
import com.ielfgame.stupidGame.config.MenuConfig;
import com.ielfgame.stupidGame.config.RunState;
import com.ielfgame.stupidGame.data.DataModel;
import com.ielfgame.stupidGame.design.hotSwap.flash.FlashManager;
import com.ielfgame.stupidGame.design.hotSwap.flash.FlashPropertyTab;
import com.ielfgame.stupidGame.design.hotSwap.flash.FlashWorkSpaceTab;
import com.ielfgame.stupidGame.dialog.QuitPanel;
import com.ielfgame.stupidGame.face.action.ActionWorkSpaceTab;
import com.ielfgame.stupidGame.fileBar.FileBar;
import com.ielfgame.stupidGame.imExport.ImExport;
import com.ielfgame.stupidGame.language.LanguageManager;
import com.ielfgame.stupidGame.platform.PlatformHelper;
import com.ielfgame.stupidGame.power.PowerMan;
import com.ielfgame.stupidGame.property.PropertyViewWorkSpaceTab;
import com.ielfgame.stupidGame.res.LanguageWorkSpaceTab;
import com.ielfgame.stupidGame.res.ResWorkSpaceTab;
import com.ielfgame.stupidGame.res.XMLWorkSpaceTab;
import com.ielfgame.stupidGame.swf.SwfResWorkSpace;
import com.ielfgame.stupidGame.undo.UndoRedoManager;

import elfEngine.basic.node.nodeAnimate.timeLine.MotionWorkSpaceTab;

public class MainDesigner extends AbstractWorkSpace {
	static {
		System.setProperty("org.lwjgl.librarypath", System.getProperty("user.dir"));
		System.setProperty("org.lwjgl.opengl.Display.enableHighDPI", "true");

		CheckRun.check();
	}

	static {
		// ElfConfig.init();
	}

	/* UI elements */
	protected Display mDdisplay;
	public Shell mShell;

	public Shell getShell() {
		return mShell;
	}

	public final IconCache mIconCache = new IconCache();

	public IconCache getIconCache() {
		return mIconCache;
	}

	public static void main(String[] args) {

		Redirect.redirect();
		new DataModel();
		FlashManager.init();

		ElfConfig.importElfConfig();
		ConfigImExport.importConfigs();

		MainDesigner application = new MainDesigner();

		final Shell shell = (Shell) application.createWorkSpace(null);
		shell.setMaximized(true);

		// final Composite swt4swingComp = new Composite(shell, SWT.EMBEDDED);
		// java.awt.Frame panel = SWT_AWT.new_Frame(swt4swingComp);
		// panel.setSize(1, 1);
		// swt4swingComp.dispose();

		shell.open();

		shell.addShellListener(new ShellListener() {
			public void shellIconified(ShellEvent e) {
				RunState.pauseOnIconify();
			}

			public void shellDeiconified(ShellEvent e) {
				RunState.resumeOnDeiconified();
			}

			public void shellDeactivated(ShellEvent e) {
				// RunState.pauseOnDeactivated();
			}

			public void shellClosed(ShellEvent e) {
				LanguageManager.createLanguageIni();
				ConfigImExport.exportConfigs();
				ElfConfig.exportElfConfig();
			}

			public void shellActivated(ShellEvent e) {
				// RunState.resumeOnActivated();
			}
		});

		shell.addListener(SWT.Close, new Listener() {
			public void handleEvent(Event event) {
				if (!new QuitPanel().open()) {
					event.doit = false;
				} else {
				}
			}
		});

		final Display display = shell.getDisplay();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		application.close();
		display.dispose();

		// System.err.println("exit done!");
		System.exit(1);

	}

	void close() {
		mIconCache.freeResources();
	}

	private void createShellContents() {
		mShell.setText(ResJudge.getResourceString("Title", new Object[] { "" }));
		mShell.setImage(mIconCache.stockImages[mIconCache.shellIcon]);
		Menu bar = new Menu(mShell, SWT.BAR);
		mShell.setMenuBar(bar);
		final FileBar fileBar = new FileBar();
		fileBar.createMenu(bar);

		// if(true) {
		// return;
		// }

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		gridLayout.marginHeight = gridLayout.marginWidth = 0;
		mShell.setLayout(gridLayout);

		// Main View
		// final ProjectSetting screen =
		// PowerMan.getSingleton(ProjectSetting.class);
		// if( screen.Orientation.isHorizontal() || true) {
		// old
		final SashForm mainSashForm = new SashForm(mShell, SWT.NONE);
		mainSashForm.setOrientation(SWT.VERTICAL);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
		gridData.horizontalSpan = 3;
		mainSashForm.setLayoutData(gridData);

		createUpView(mainSashForm);
		createDownView(mainSashForm);

		mainSashForm.setWeights(new int[] { 7, 3 });
		// } else {
		// //new
		// final SashForm mainSashForm = new SashForm(mShell, SWT.NONE);
		// mainSashForm.setOrientation(SWT.HORIZONTAL);
		// GridData gridData = new GridData(GridData.FILL_HORIZONTAL |
		// GridData.FILL_VERTICAL);
		// gridData.horizontalSpan = 2;
		// mainSashForm.setLayoutData(gridData);
		//
		// createLeftView(mainSashForm);
		// createRightView(mainSashForm);
		//
		// int minwidth = 2000;
		// for(final Monitor mon : this.mDdisplay.getMonitors()) {
		// minwidth = Math.min(minwidth, mon.getBounds().width);
		// }
		//
		// mainSashForm.setWeights(new int[] { screen.getPhysicWidth()*11/10,
		// minwidth-screen.getPhysicWidth()*11/10 });
		// }

		final StatusViewWorkSpace statusViewWorkSpace = new StatusViewWorkSpace();
		statusViewWorkSpace.createWorkSpace(mShell);
	}

	// private void createLeftView(final Composite parent) {
	// final Composite parent2 = new Composite(parent, SWT.NONE);
	//
	// final ProjectSetting screenConfig =
	// PowerMan.getSingleton(ProjectSetting.class);
	// parent2.setSize(screenConfig.getPhysicWidth(),
	// screenConfig.getPhysicWidth());
	// parent2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL |
	// GridData.FILL_VERTICAL));
	//
	// final GridLayout gridLayout1 = new GridLayout();
	// gridLayout1.numColumns = 1;
	// gridLayout1.marginHeight = gridLayout1.marginWidth = 2;
	// gridLayout1.horizontalSpacing = gridLayout1.verticalSpacing = 0;
	// parent2.setLayout(gridLayout1);
	//
	// final GLViewWorkSpace gLViewWorkSpace = new GLViewWorkSpace();
	// gLViewWorkSpace.createWorkSpace(parent2);
	//
	// }
	//
	// private void createRightView(final Composite mainParent) {
	// final SashForm parent = new SashForm(mainParent, SWT.NONE);
	// parent.setOrientation(SWT.VERTICAL);
	//
	// //up
	// final SashForm upSashForm = new SashForm(parent, SWT.NONE | SWT.BORDER);
	// upSashForm.setOrientation(SWT.HORIZONTAL);
	//
	// SashForm leftSashForm = new SashForm(upSashForm, SWT.NONE);
	// leftSashForm.setOrientation(SWT.VERTICAL);
	// leftSashForm.setLayoutData(new GridData(GridData.FILL_HORIZONTAL |
	// GridData.FILL_VERTICAL));
	//
	// final MyTabFolder leftTopTabFolder = new MyTabFolder(leftSashForm);
	// GridLayout gridLayout1 = new GridLayout();
	// gridLayout1.numColumns = 1;
	// gridLayout1.marginHeight = gridLayout1.marginWidth = 2;
	// gridLayout1.horizontalSpacing = gridLayout1.verticalSpacing = 0;
	// leftTopTabFolder.setLayout(gridLayout1);
	//
	// final NodeViewWorkSpaceTab nodeViewWorkSpace = new
	// NodeViewWorkSpaceTab();
	// nodeViewWorkSpace.createWorkSpace(leftTopTabFolder);
	//
	// final ActionWorkSpaceTab actionWorkSpaceTab = new ActionWorkSpaceTab();
	// actionWorkSpaceTab.createWorkSpace(leftTopTabFolder);
	//
	// final SurfaceTab surfaceTab = new SurfaceTab();
	// surfaceTab.createWorkSpace(leftTopTabFolder);
	//
	// final ResWorkSpaceTab resWorkSpaceTab = new ResWorkSpaceTab();
	// resWorkSpaceTab.createWorkSpace(leftTopTabFolder);
	//
	// leftTopTabFolder.setSelection(0);
	//
	// Composite composite1 = new Composite(upSashForm, SWT.NONE);
	// gridLayout1 = new GridLayout();
	// gridLayout1.numColumns = 2;
	// gridLayout1.marginHeight = gridLayout1.marginWidth = 2;
	// gridLayout1.horizontalSpacing = gridLayout1.verticalSpacing = 0;
	// composite1.setLayout(gridLayout1);
	//
	// // final GLViewWorkSpace gLViewWorkSpace = new GLViewWorkSpace();
	// // gLViewWorkSpace.createWorkSpace(composite1);
	//
	// Composite composite2 = new Composite(composite1, SWT.NONE);
	// GridLayout gridLayout2 = new GridLayout();
	// gridLayout2.numColumns = 1;
	// gridLayout2.marginHeight = gridLayout2.marginWidth = 2;
	// gridLayout2.horizontalSpacing = gridLayout2.verticalSpacing = 0;
	// composite2.setLayout(gridLayout2);
	// composite2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL |
	// GridData.FILL_VERTICAL | GridData.VERTICAL_ALIGN_BEGINNING));
	//
	// final MyTabFolder setTabFolder = new MyTabFolder(composite2);
	// setTabFolder.setLayoutData(new GridData(GridData.FILL_HORIZONTAL |
	// GridData.FILL_VERTICAL | GridData.VERTICAL_ALIGN_BEGINNING));
	//
	// final MotionPropertyWorkSpaceTab simplePropertyTab = new
	// MotionPropertyWorkSpaceTab();
	// simplePropertyTab.createWorkSpace(setTabFolder);
	//
	// // final DebugTab setScreenTab = new DebugTab();
	// // setScreenTab.createWorkSpace(mSetTabFolder);
	// //
	// // final SetPreviewScreenTab SetPreviewScreenTab = new
	// SetPreviewScreenTab();
	// // SetPreviewScreenTab.createWorkSpace(mSetTabFolder);
	// //
	// // final OutputWorkSpaceTab outputWorkSpaceTab = new
	// OutputWorkSpaceTab();
	// // outputWorkSpaceTab.createWorkSpace(mSetTabFolder);
	//
	// setTabFolder.setSelection(0);
	// // upSashForm.setWeights(new int[] { 22, 78 });
	//
	// upSashForm.setWeights(new int[]{6,4});
	//
	// //down
	// final MyTabFolder dowmTabFolder = new MyTabFolder(parent);
	//
	// final PropertyViewWorkSpaceTab propertyViewWorkSpace = new
	// PropertyViewWorkSpaceTab("Node Property", false);
	// propertyViewWorkSpace.createWorkSpace(dowmTabFolder);
	//
	// final PropertyViewWorkSpaceTab propertyViewWorkSpace2 = new
	// PropertyViewWorkSpaceTab("Global Property", true);
	// propertyViewWorkSpace2.createWorkSpace(dowmTabFolder);
	//
	// final MotionWorkSpaceTab motionWorkSpace = new MotionWorkSpaceTab();
	// motionWorkSpace.createWorkSpace(dowmTabFolder);
	//
	// // final FlashWorkSpaceTab flashTab = new FlashWorkSpaceTab();
	// // flashTab.createWorkSpace(dowmTabFolder);
	//
	// dowmTabFolder.setSelection(0);
	//
	// parent.setWeights(new int[]{7,3});
	// }

	private void createDownView(final SashForm parent) {

		final SashForm downSashForm = new SashForm(parent, SWT.NONE);
		downSashForm.setOrientation(SWT.HORIZONTAL);

		{
			SashForm leftSashForm = new SashForm(downSashForm, SWT.NONE);
			leftSashForm.setOrientation(SWT.VERTICAL);
			leftSashForm.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));

			final MyTabFolder dowmTabFolder = new MyTabFolder(leftSashForm);

			final MotionWorkSpaceTab motionWorkSpace = new MotionWorkSpaceTab();
			motionWorkSpace.createWorkSpace(dowmTabFolder);

			// FlashWorkSpaceTab
			final FlashWorkSpaceTab flashWorkSpace = new FlashWorkSpaceTab();
			flashWorkSpace.createWorkSpace(dowmTabFolder);

			dowmTabFolder.setSelection(0);
		}

		{
			SashForm rightSashForm = new SashForm(downSashForm, SWT.NONE);
			rightSashForm.setOrientation(SWT.VERTICAL);
			rightSashForm.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));

			final MyTabFolder dowmTabFolder = new MyTabFolder(rightSashForm);

			// FlashWorkSpaceTab
			final PreviewPictureTab previewScreenTab = new PreviewPictureTab();
			previewScreenTab.createWorkSpace(dowmTabFolder);

			final FlashPropertyTab keyPropertyTab = new FlashPropertyTab();
			keyPropertyTab.createWorkSpace(dowmTabFolder);

			dowmTabFolder.setSelection(0);
		}

		downSashForm.setWeights(new int[] { 8, 2 });
	}

	private void createUpView(final SashForm parent) {
		final SashForm upSashForm = new SashForm(parent, SWT.NONE);
		upSashForm.setOrientation(SWT.HORIZONTAL);

		{
			SashForm leftSashForm = new SashForm(upSashForm, SWT.NONE);
			leftSashForm.setOrientation(SWT.VERTICAL);
			leftSashForm.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));

			final MyTabFolder leftTopTabFolder = new MyTabFolder(leftSashForm);
			GridLayout gridLayout1 = new GridLayout();
			gridLayout1.numColumns = 1;
			gridLayout1.marginHeight = gridLayout1.marginWidth = 2;
			gridLayout1.horizontalSpacing = gridLayout1.verticalSpacing = 0;
			leftTopTabFolder.setLayout(gridLayout1);

			final NodeViewWorkSpaceTab nodeViewWorkSpace = new NodeViewWorkSpaceTab();
			nodeViewWorkSpace.createWorkSpace(leftTopTabFolder);

			final ActionWorkSpaceTab actionWorkSpaceTab = new ActionWorkSpaceTab();
			actionWorkSpaceTab.createWorkSpace(leftTopTabFolder);

			final SurfaceTab surfaceTab = new SurfaceTab();
			surfaceTab.createWorkSpace(leftTopTabFolder);

		}

		{
			SashForm centerSashForm = new SashForm(upSashForm, SWT.NONE);
			centerSashForm.setOrientation(SWT.VERTICAL);
			centerSashForm.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));

			final MyTabFolder centerTopTabFolder = new MyTabFolder(centerSashForm);
			GridLayout gridLayout1 = new GridLayout();
			gridLayout1.numColumns = 1;
			gridLayout1.marginHeight = gridLayout1.marginWidth = 2;
			gridLayout1.horizontalSpacing = gridLayout1.verticalSpacing = 0;
			centerTopTabFolder.setLayout(gridLayout1);

//			final PropertyViewWorkSpaceTab propertyViewWorkSpace = new PropertyViewWorkSpaceTab("Node Property", false);
//			propertyViewWorkSpace.createWorkSpace(centerTopTabFolder);

			final PropertyViewWorkSpaceTab propertyViewWorkSpace2 = new PropertyViewWorkSpaceTab("Global Property", true);
			propertyViewWorkSpace2.createWorkSpace(centerTopTabFolder);

			centerTopTabFolder.setSelection(0);
		}

		{
			SashForm rightSashForm = new SashForm(upSashForm, SWT.NONE);
			rightSashForm.setOrientation(SWT.VERTICAL);
			rightSashForm.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));

			final MyTabFolder rightTopTabFolder = new MyTabFolder(rightSashForm);
			GridLayout gridLayout1 = new GridLayout();
			gridLayout1.numColumns = 1;
			gridLayout1.marginHeight = gridLayout1.marginWidth = 2;
			gridLayout1.horizontalSpacing = gridLayout1.verticalSpacing = 0;
			rightTopTabFolder.setLayout(gridLayout1);

			final ResWorkSpaceTab resWorkSpaceTab = new ResWorkSpaceTab();
			resWorkSpaceTab.createWorkSpace(rightTopTabFolder);

			final LanguageWorkSpaceTab languageWorkSpaceTab = new LanguageWorkSpaceTab();
			languageWorkSpaceTab.createWorkSpace(rightTopTabFolder);

			final XMLWorkSpaceTab xMLWorkSpaceTab = new XMLWorkSpaceTab();
			xMLWorkSpaceTab.createWorkSpace(rightTopTabFolder);
			
			final SwfResWorkSpace swfResWorkSpace = new SwfResWorkSpace();
			swfResWorkSpace.createWorkSpace(rightTopTabFolder);
			
			rightTopTabFolder.setSelection(0);
		}

		upSashForm.setWeights(new int[] { 20, 60, 20 });

		mDdisplay.timerExec(1000, new Runnable() {
			public void run() {
				new GLMainWindow().open();
			}
		});
	}

	public Composite createWorkSpace(Composite parent) {
		mShell = new Shell();
		// mShell.open();
		mIconCache.initResources(mShell.getDisplay());
		this.mDdisplay = mShell.getDisplay();

		Display.getDefault().addFilter(SWT.KeyDown, new Listener() {
			public void handleEvent(Event e) {
				if (e.stateMask == PlatformHelper.CTRL && (e.keyCode == 'z' || e.keyCode == 'Z')) {
					UndoRedoManager.onUndo();
				} else if (e.stateMask == PlatformHelper.CTRL && (e.keyCode == 'y' || e.keyCode == 'Y')) {
					UndoRedoManager.onRedo();
				} else if (e.stateMask == PlatformHelper.CTRL && (e.keyCode == 's' || e.keyCode == 'S')) {
					final String path = PowerMan.getSingleton(DataModel.class).getLastImportPath();
					final MenuConfig menuConfig = PowerMan.getSingleton(MenuConfig.class);
					if (path != null) {
						ImExport.exports(path, menuConfig.AutoBatch);
					}
				} else if(e.keyCode == SWT.SPACE) {
					PowerMan.getSingleton(FlashManager.class).toggle();
				} else if(e.keyCode == 'a' || e.keyCode == 'A') {
					PowerMan.getSingleton(FlashManager.class).stop();
					int nextFrame = PowerMan.getSingleton(FlashManager.class).getCurrentFrame()-1;
					if(nextFrame < 0) {
						nextFrame = PowerMan.getSingleton(FlashManager.class).getMaxF();
					}
					PowerMan.getSingleton(FlashManager.class).setFrame(nextFrame);
				} else if(e.keyCode == 'd' || e.keyCode == 'D') {
					PowerMan.getSingleton(FlashManager.class).stop();
					int nextFrame = PowerMan.getSingleton(FlashManager.class).getCurrentFrame()+1;
					if(nextFrame > PowerMan.getSingleton(FlashManager.class).getMaxF()) {
						nextFrame = 0;
					}
					PowerMan.getSingleton(FlashManager.class).setFrame(nextFrame);
				} 
			}
		});

		createShellContents();

		return mShell;
	}

}
