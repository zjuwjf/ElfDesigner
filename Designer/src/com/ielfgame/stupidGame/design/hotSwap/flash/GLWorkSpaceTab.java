package com.ielfgame.stupidGame.design.hotSwap.flash;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.widgets.Composite;

import com.ielfgame.stupidGame.AbstractWorkSpaceTab;
import com.ielfgame.stupidGame.GLView.BasicGLView;
import com.ielfgame.stupidGame.GLView.IGLRender;
import com.ielfgame.stupidGame.config.FrameInterval;
import com.ielfgame.stupidGame.design.Surface.Surface;
import com.ielfgame.stupidGame.design.Surface.SurfaceGLRender;
import com.ielfgame.stupidGame.power.PowerMan;

public class GLWorkSpaceTab extends AbstractWorkSpaceTab{
	private final BasicGLView mBasicGLView = new BasicGLView();
	
	public GLWorkSpaceTab(String text) {
		super(text);
		this.setInterval(PowerMan.getSingleton(FrameInterval.class).GL_INTERVAL);
	}
	
	public void setSurface(final Surface surface) {
		final SurfaceGLRender sr = new SurfaceGLRender();
		sr.setSurface(surface);
		mBasicGLView.setGLRender(sr);
	}

	public void update() {
		
	}

	public final Composite createTab(CTabFolder parent) {
		ScrolledComposite sc1 = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		sc1.setAlwaysShowScrollBars(true);
		
		final GLCanvas glCanvas = mBasicGLView.createWorkSpace(sc1);
		
		final IGLRender render = mBasicGLView.getGLRender();
		glCanvas.setSize(render.getPhysicWidth(), render.getPhysicHeight());
		glCanvas.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
		
		sc1.setContent(glCanvas);
		
		return sc1;
	}
	
	public BasicGLView getGLView() {
		return mBasicGLView;
	}
}
