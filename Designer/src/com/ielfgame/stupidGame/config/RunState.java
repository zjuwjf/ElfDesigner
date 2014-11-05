package com.ielfgame.stupidGame.config;

import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.widgets.Shell;

import com.ielfgame.stupidGame.AbstractWorkSpaceTab;
import com.ielfgame.stupidGame.GLView.GLMainWindow;
import com.ielfgame.stupidGame.GLView.IGLRender;
import com.ielfgame.stupidGame.GLView.IGLView;
import com.ielfgame.stupidGame.power.PowerMan;

public class RunState {
	private final static int SLEEP_GL_FD = 500;
	private final static int SLEEP_UI_FD = 500;
	private static int OLD_GL_FD;
	private static int OLD_UI_FD;
	
	public static void pauseOnIconify() {
		final AutoBackground autoBackground = PowerMan.getSingleton(AutoBackground.class);
		if(autoBackground.onIconified) { 
			System.err.println("Pause On Iconify"); 
			RunState.pause(); 
		} 
	} 
	
	public static void resumeOnDeiconified() {
		final AutoBackground autoBackground = PowerMan.getSingleton(AutoBackground.class);
		if(autoBackground.onIconified) {
			System.err.println("Resume On Iconify");
			RunState.resume();
		} 
	}

	public static void pauseOnDeactivated() {
		final AutoBackground autoBackground = PowerMan.getSingleton(AutoBackground.class);
		if(autoBackground.onDeactivated) {
			System.err.println("Deactivate");
			RunState.pause();
		} 
	}

	public static void resumeOnActivated() {
		final AutoBackground autoBackground = PowerMan.getSingleton(AutoBackground.class);
		if(autoBackground.onDeactivated) {
			System.err.println("Activated");
			RunState.resume();
		} 
	}
	
	private static void pause() {
		AbstractWorkSpaceTab.pauseUI(); 
		try {
			PowerMan.getSingleton(GLMainWindow.class).getGLView().getGLRender().setPauseGL(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		final FrameInterval fd = PowerMan.getSingleton(FrameInterval.class); 
		if(fd.GL_INTERVAL != SLEEP_GL_FD || fd.UI_INTERVAL != SLEEP_UI_FD) {
			OLD_GL_FD = fd.GL_INTERVAL;
			OLD_UI_FD = fd.UI_INTERVAL;
			fd.GL_INTERVAL = SLEEP_GL_FD;
			fd.UI_INTERVAL = SLEEP_UI_FD;
		}
	}
	
	private static void resume() {
		AbstractWorkSpaceTab.resumeUI();
		try {
			PowerMan.getSingleton(GLMainWindow.class).getGLView().getGLRender().setPauseGL(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		final FrameInterval fd = PowerMan.getSingleton(FrameInterval.class); 
		if(fd.GL_INTERVAL != OLD_GL_FD || fd.UI_INTERVAL != OLD_UI_FD) {
			fd.GL_INTERVAL = OLD_GL_FD;
			fd.UI_INTERVAL = OLD_UI_FD;
		}
	}
	
//	public static void initChildShell(Shell shell) {
//		shell.addShellListener(new ShellListener() { 
//			public void shellIconified(ShellEvent e) { 
////				RunState.pauseOnIconify();
//			} 
//			public void shellDeiconified(ShellEvent e) {
////				RunState.resumeOnDeiconified();
//			}
//			public void shellDeactivated(ShellEvent e) {
////				RunState.pauseOnDeactivated();
//			}
//			public void shellActivated(ShellEvent e) {
////				RunState.resumeOnActivated();
//			} 
//			public void shellClosed(ShellEvent e) { 
//			} 
//		});
//	}
	
	public static void initGLShell(final Shell shell, final IGLView GlView) {
		final IGLRender render = GlView.getGLRender();
		shell.addShellListener(new ShellListener() { 
			public void shellIconified(ShellEvent e) { 
				render.setPauseGL(false);
			} 
			public void shellDeiconified(ShellEvent e) {
				render.setPauseGL(false);
			}
			public void shellDeactivated(ShellEvent e) {
			}
			public void shellActivated(ShellEvent e) {
			} 
			public void shellClosed(ShellEvent e) { 
			} 
		});
	}
}
