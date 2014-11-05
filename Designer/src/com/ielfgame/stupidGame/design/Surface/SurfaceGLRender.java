package com.ielfgame.stupidGame.design.Surface;

import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import com.ielfgame.stupidGame.GLView.BasicGLViewRender;
import com.ielfgame.stupidGame.GLView.GLID;
import com.ielfgame.stupidGame.data.ElfPointf;

public class SurfaceGLRender extends BasicGLViewRender {
	private Surface mSurface;

	public SurfaceGLRender() {
		this.setFixedSize(true);
		this.setUseControl(false);
		this.setGLID(GLID.GL_WINDOW_ID);
	}

	public void setSurface(Surface surface) {
		mSurface = surface;
		mSurface.setRender(this);

		this.setRootNode(surface.getRootNode());
	}

	protected ElfPointf getFixedScale() {
		return new ElfPointf(1,1);
	}
	
	public int getPhysicWidth() {
		if (mSurface == null) {
			return 1;
		}
		return mSurface.getPhysicWidth();
	}

	public int getPhysicHeight() {
		if (mSurface == null) {
			return 1;
		}
		return mSurface.getPhysicHeight();
	}

	public Menu getMenu(final Shell shell) {
		final Menu menu = new Menu(shell, SWT.POP_UP);

		menu.addMenuListener(new MenuListener() {
			public void menuShown(final MenuEvent e) {
				final MenuItem[] items = menu.getItems();
				for (MenuItem item : items) {
					item.dispose();
				}
				/***
				 * do something
				 */
				if (mSurface != null) {
					final ElfPointf pos = getLastMousePos();
					mSurface.onMenuShow(menu, pos.x, pos.y);
				} else {
					System.err.println("No Surface On Menu Show");
				}
			}

			public void menuHidden(MenuEvent e) {
			}
		});

		return menu;
	}

	public void mouseMove(MouseEvent event) {
		if (mSurface == null || !mSurface.mouseMove(event)) {
			super.mouseMove(event);
		}
	}

	public void mouseDown(MouseEvent event) {
		if (mSurface == null || !mSurface.mouseDown(event)) {
			super.mouseDown(event);
		}
	}

	public void mouseUp(MouseEvent event) {
		if (mSurface == null || !mSurface.mouseUp(event)) {
			super.mouseUp(event);
		}
	}
	
	public void mouseDoubleClick(MouseEvent event) {
		if (mSurface == null || !mSurface.mouseDoubleClick(event)) {
			super.mouseDown(event);
		}
	}
	
	private final LinkedList<KeyListener> mKeyListenerList = new LinkedList<KeyListener>();
	
	public void addKeyListener(KeyListener k) {
		mKeyListenerList.add(k);
	}
	
	public boolean removeKeyListener(KeyListener k) {
		return mKeyListenerList.remove(k);
	}
	
	public void keyPressed(KeyEvent e) {
		for(KeyListener kl : mKeyListenerList) {
			kl.keyPressed(e);
		}
	}
	
	public void keyReleased(KeyEvent e) {
		for(KeyListener kl : mKeyListenerList) {
			kl.keyReleased(e);
		}
	}
	
	public void mouseScrolled (MouseEvent event) {
		if (mSurface == null || !mSurface.mouseScrolled(event)) {
			super.mouseScrolled(event);
		}
	}
}
