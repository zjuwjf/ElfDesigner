package com.ielfgame.stupidGame.design.hotSwap;

import java.util.LinkedList;

import com.ielfgame.stupidGame.MainDesigner;
import com.ielfgame.stupidGame.data.DataModel;
import com.ielfgame.stupidGame.data.SpellHelper;
import com.ielfgame.stupidGame.design.ElfClassLoader;
import com.ielfgame.stupidGame.design.Surface.Surface;
import com.ielfgame.stupidGame.design.Surface.SurfaceWindow;
import com.ielfgame.stupidGame.power.PowerMan;
import com.ielfgame.stupidGame.utils.FileHelper;


public class RunClassDelegate implements Runnable { 
	
	final static String DEFAULT_DIR = FileHelper.getUserDir()+"\\src\\com\\ielfgame\\stupidGame\\design\\hotSwap"; 
	
	
	final static LinkedList<String> sDynamicClasses = new LinkedList<String>();
	static { 
		sDynamicClasses.clear(); 
	} 
	
	@Override
	public void run() { 
		System.err.println("RunClassDelegate run..."); 
		
		try { 
			final SurfaceWindow glView = new SurfaceWindow(); 
			final String xmlPath = PowerMan.getSingleton(DataModel.class).getLastImportPath();
			
			final String simpleName = SpellHelper.getUpEname(xmlPath.substring(xmlPath.lastIndexOf(FileHelper.DECOLLATOR)+1, xmlPath.lastIndexOf(".")));
			final ElfClassLoader ucl = ElfClassLoader.getClassLoader(); 
			
			final Class<?> _class = ucl.loadClass("com.ielfgame.stupidGame.design.hotSwap.Surface"+simpleName);
			final Object surface = _class.newInstance(); 
			glView.setSurface((Surface)surface); 
			glView.open(PowerMan.getSingleton(MainDesigner.class).getShell());
		} catch (Exception e) { 
			e.printStackTrace();
		} 
	} 
} 


