package com.ielfgame.stupidGame.GLView;

import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;

import elfEngine.basic.node.extend.ElfScreenNode;

public interface IGLRender extends  KeyListener, MouseListener, MouseMoveListener, MouseWheelListener{
	public void render();
	public void setDND(GLCanvas gLCanvas);
	
	//for size
	public int getPhysicWidth();
	public int getPhysicHeight();
	public int getLogicWidth();
	public int getLogicHeight();
	
	public boolean getFixedSize();
	
	public int getGLID();
	public void setGLID(int id);
	
	public ElfScreenNode getRootNode();
	
	public Menu getMenu(Shell shell);
	
	public void resetView();
	
	public void setPauseGL(boolean pause) ; 
	public boolean getPauseGL() ;
	
	public boolean getCharKeyPressed(final int keycode);
	
	public IGLView getGLView();
	public void setGLView(IGLView view);
	
}
