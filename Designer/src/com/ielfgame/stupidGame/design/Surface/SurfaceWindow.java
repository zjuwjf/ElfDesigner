package com.ielfgame.stupidGame.design.Surface;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.ielfgame.stupidGame.GLView.BasicGLView;

import elfEngine.opengl.GLManage;

public class SurfaceWindow { 
	private Surface mSurface; 
	private Shell mMyShell;
	private final BasicGLView mGLView = new BasicGLView();
	private final SurfaceGLRender mRender = new SurfaceGLRender();
	
	public SurfaceWindow() { 
		mGLView.setGLRender(mRender);
	} 
	
	public void open(final Shell shell) { 
		mMyShell = new Shell(shell);
//		RunState.initChildShell(mMyShell);
		
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.marginHeight = gridLayout.marginWidth = 0;
		mMyShell.setLayout(gridLayout);
		
		mGLView.createWorkSpace(mMyShell);
		
		if(mSurface != null) {
			mMyShell.setText("Surface "+mSurface.getXMLPath());
		} else {
			mMyShell.setText("Surface Empty");
		}
		
		mMyShell.setMaximized(true); 
		mMyShell.pack();
//		mMyShell.setLocation(mMyShell.getDisplay().getClientArea().width / 2 - mMyShell.getSize().x/2, 
//				mMyShell.getDisplay().getClientArea().height/2 - mMyShell.getSize().y/2);
		
		mMyShell.open(); 
		
		mMyShell.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				mMyShell.getDisplay().syncExec(new Runnable() {
					public void run() {
						GLManage.clearByGLId(mRender.getRootNode().getGLId()); 
					} 
				});
			} 
		});
		
		Display display = mMyShell.getDisplay(); 
		while (!mMyShell.isDisposed()) { 
			if (!display.readAndDispatch()) 
				display.sleep(); 
		} 
		
		System.gc(); 
	} 
	
	
	public void setSurface(final Surface surface) {
		mSurface = surface;
		if(mMyShell != null) {
			mMyShell.setText("Surface "+mSurface.getXMLPath());
		} 
		
		mRender.setSurface(surface);
	}
}
