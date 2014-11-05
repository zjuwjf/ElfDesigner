package com.ielfgame.stupidGame.platform;

import org.eclipse.swt.SWT;

import com.ielfgame.stupidGame.utils.FileHelper;

public class PlatformHelper {

	public static final String DECOLLATOR;
	public static final boolean IS_WINDOWS;
	public static final int CTRL;
	public static final int SHIFT;
	public static final int SPACE;
	public static final int DEL;

	static {
		final String sys = System.getProperty("os.name");
		System.err.println("System " + sys);
		IS_WINDOWS = sys.contains("Windows");
		if (IS_WINDOWS) {
			DECOLLATOR = "\\";
			CTRL = SWT.CTRL;
			DEL = SWT.DEL;
		} else {
			DECOLLATOR = "/";
			CTRL = SWT.COMMAND;
			DEL = SWT.BS;
		}
		SHIFT = SWT.SHIFT;
		SPACE = SWT.SPACE;
	}
	
	private static boolean sChangeUnderline = false;
	public static void setChangeUnderline(boolean set) {
		sChangeUnderline = set;
	}
	public static boolean getChangeUnderline() {
		return sChangeUnderline;
	}

	public static String toCurrentPath(String key) {
		final String simple = FileHelper.getSimpleName(key);
		if(sChangeUnderline) {
			if(simple != null) {
				return simple.replace("-", "_");
			}
		}
		return simple;
	}

	public static String getUserDir() {
		return System.getProperty("user.dir");
	}

}
