package com.ielfgame.stupidGame.power;

import java.util.HashMap;

import org.eclipse.swt.widgets.Shell;

import com.ielfgame.stupidGame.MainDesigner;


public class PowerMan {
	
	static int LOAD_COUNT = 0;
	static {
		System.err.println("load PowerMan " + LOAD_COUNT++);
	} 
	
	private final static HashMap<Class<? extends ISingleton>, ISingleton> sMap = new HashMap<Class<? extends ISingleton>, ISingleton>();
	public static void register(Class<? extends ISingleton> _class, ISingleton workSpace){
		System.err.println(_class.getCanonicalName() + " registers!"); 
		sMap.put(_class, workSpace); 
	} 
	
	@SuppressWarnings("unchecked")
	public static <T extends ISingleton> T getSingleton(Class<T> _class){
		final T ret = (T)sMap.get(_class);
		return ret;
	} 
	
	public static void runOnUI(Runnable run) {
		final Shell shell = getSingleton(MainDesigner.class).mShell;
		shell.getDisplay().asyncExec(run);
	}
}
