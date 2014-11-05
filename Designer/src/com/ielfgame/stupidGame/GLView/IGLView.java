package com.ielfgame.stupidGame.GLView;

import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public interface IGLView extends Runnable {
	public void setViewMode(ViewMode viewMode, float fovy, float aspect, float zNear, float zFar) ;
	public GLCanvas createWorkSpace(Composite parent) ;
	public void setGLRender(IGLRender render);
	public IGLRender getGLRender();
	public Shell getShell();
}
