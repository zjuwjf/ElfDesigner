//package com.ielfgame.stupidGame.design;
//
//import org.eclipse.swt.events.DisposeEvent;
//import org.eclipse.swt.events.DisposeListener;
//import org.eclipse.swt.layout.GridLayout;
//import org.eclipse.swt.widgets.Display;
//import org.eclipse.swt.widgets.Shell;
//
//import com.ielfgame.stupidGame.MainDesigner;
//import com.ielfgame.stupidGame.GLView.BasicGLView;
//import com.ielfgame.stupidGame.GLView.GLMainWindow;
//import com.ielfgame.stupidGame.GLView.IGLView;
//import com.ielfgame.stupidGame.config.RunState;
//import com.ielfgame.stupidGame.design.Surface.SurfaceGLRender;
//import com.ielfgame.stupidGame.power.PowerMan;
//
//import elfEngine.opengl.GLManage;
//
//public class GLWindow {
//
//	protected Shell mMyShell;
//	private final IGLView mGLView = new BasicGLView();
//	private final SurfaceGLRender mRender = new SurfaceGLRender();
//	
//	public GLWindow() { 
//		mGLView.setGLRender(mRender);
//	} 
//	
//	public void open() { 
//		
//		final GLMainWindow gLViewWorkSpace = PowerMan.getSingleton(GLMainWindow.class);
//		if(gLViewWorkSpace != null) {
//			gLViewWorkSpace.getGLView().getGLRender().setPauseGL(true);
//		}
//		
//		mMyShell = new Shell(PowerMan.getSingleton(MainDesigner.class).getShell()); 
//		RunState.initChildShell(mMyShell);
//		
//		final GridLayout gridLayout = new GridLayout();
//		gridLayout.numColumns = 1;
//		gridLayout.marginHeight = gridLayout.marginWidth = 0;
//		mMyShell.setLayout(gridLayout);
//		
//		mGLView.createWorkSpace(mMyShell);
//		
//		mMyShell.setMaximized(true); 
//		mMyShell.pack();
//		mMyShell.setLocation(mMyShell.getDisplay().getPrimaryMonitor().getClientArea().width / 2 - mMyShell.getSize().x/2, 
//				mMyShell.getDisplay().getPrimaryMonitor().getClientArea().height/2 - mMyShell.getSize().y/2);
//		
//		mMyShell.open(); 
//		
//		mMyShell.addDisposeListener(new DisposeListener() {
//			public void widgetDisposed(DisposeEvent e) {
//				mMyShell.getDisplay().syncExec(new Runnable() {
//					public void run() {
//						GLManage.clearByGLId(mRender.getRootNode().getGLId()); 
//						try {
//							PowerMan.getSingleton(GLMainWindow.class).getGLView().getGLRender().setPauseGL(false);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					} 
//				});
//			} 
//		});
//		
//		Display display = mMyShell.getDisplay(); 
//		while (!mMyShell.isDisposed()) { 
//			if (!display.readAndDispatch()) 
//				display.sleep(); 
//		} 
//		mMyShell = null;
//		System.gc(); 
//	} 
//}
