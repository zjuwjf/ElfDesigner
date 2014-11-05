package com.ielfgame.stupidGame.GLView;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import com.ielfgame.stupidGame.MainDesigner;
import com.ielfgame.stupidGame.config.RunState;
import com.ielfgame.stupidGame.data.DataModel;
import com.ielfgame.stupidGame.power.APowerManSingleton;
import com.ielfgame.stupidGame.power.PowerMan;

import elfEngine.opengl.GLManage;

public class GLMainWindow extends APowerManSingleton{
	
	protected Shell mMyShell;
	private final IGLView mGLView;
	private final IGLRender mRender;
	
	public GLMainWindow() { 
		mGLView = new BasicGLView();
		
		mRender = new BasicGLViewRender();
		((BasicGLViewRender)mRender).setRootNode(PowerMan.getSingleton(DataModel.class).getScreenNode());
		
		mGLView.setGLRender(mRender);
	} 
	
	public void open() { 
		
		mMyShell = new Shell(PowerMan.getSingleton(MainDesigner.class).getShell(), SWT.TITLE|SWT.BORDER|SWT.MIN); 
		
		RunState.initGLShell(mMyShell, mGLView);
		
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.marginHeight = gridLayout.marginWidth = 0;
		mMyShell.setLayout(gridLayout);
		
		mGLView.createWorkSpace(mMyShell);
		
		mMyShell.setMaximized(true); 
		mMyShell.pack();
		mMyShell.setLocation(mMyShell.getDisplay().getPrimaryMonitor().getClientArea().width / 2 - mMyShell.getSize().x/2, 
				mMyShell.getDisplay().getPrimaryMonitor().getClientArea().height/2 - mMyShell.getSize().y/2);
		
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
		
		mMyShell.addListener(SWT.Close, new Listener() {
			public void handleEvent(Event event) { 
				event.doit = false;
			}
		}); 
		
		Display display = mMyShell.getDisplay(); 
		while (!mMyShell.isDisposed()) { 
			if (!display.readAndDispatch()) 
				display.sleep(); 
		} 
		mMyShell = null;
		System.gc(); 
	}

	public IGLView getGLView() {
		return mGLView;
	} 
}
