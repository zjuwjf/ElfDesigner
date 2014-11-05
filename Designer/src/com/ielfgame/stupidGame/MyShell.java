//package com.ielfgame.stupidGame;
//
//import org.eclipse.swt.events.ShellEvent;
//import org.eclipse.swt.events.ShellListener;
//import org.eclipse.swt.widgets.Shell;
//
//import com.ielfgame.stupidGame.config.RunState;
//
//public class MyShell{
//	public final Shell shell;
//	public MyShell(Shell shell, final int type) {
//		this.shell = new Shell(shell, type);
//		this.shell.addShellListener(new ShellListener() { 
//			public void shellIconified(ShellEvent e) { 
//				RunState.pauseOnIconify();
//			} 
//			public void shellDeiconified(ShellEvent e) {
//				RunState.resumeOnDeiconified();
//			}
//			public void shellDeactivated(ShellEvent e) {
//				RunState.pauseOnDeactivated();
//			}
//			public void shellActivated(ShellEvent e) {
//				RunState.resumeOnActivated();
//			} 
//			public void shellClosed(ShellEvent e) {
//			}
//		});
//	}
//}
