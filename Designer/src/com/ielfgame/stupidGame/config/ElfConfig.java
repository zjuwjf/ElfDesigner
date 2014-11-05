package com.ielfgame.stupidGame.config;

import java.io.File;
import java.util.LinkedList;

import com.ielfgame.stupidGame.MainDesigner;
import com.ielfgame.stupidGame.data.ElfDataXML;
import com.ielfgame.stupidGame.dialog.MultiButtonDialog;
import com.ielfgame.stupidGame.power.PowerMan;
import com.ielfgame.stupidGame.texturePacker.pvr.TPTest.ResIOS2AndroidData;
import com.ielfgame.stupidGame.utils.FileHelper;
import com.ielfgame.stupidGame.xml.XMLVersionManage;

public class ElfConfig {
	
	private final static String StartUpPath = FileHelper.getUserDir() + FileHelper.DECOLLATOR + "StartUp.xml";
	static {
		final LinkedList<String> lines = FileHelper.read(new File(StartUpPath));
		final int size = lines.size();
		if(size > 0) {
			final String line = lines.get(0);
			if(line != null) {
				//make sure startup existed
				final String startup = FileHelper.getUserDir() + FileHelper.DECOLLATOR + line;
				FileHelper.isFile(startup);
				
				//
				
			}
		}
	}
	
	//except autoSave.xml a.xml
	//project
	
	private final static LinkedList<ElfDataXML> sConfigList = new LinkedList<ElfDataXML>();
	private final static String CONFIG_PATH = FileHelper.getUserDir() + FileHelper.DECOLLATOR + "ElfConfig.xml";
	
	static { 
		sConfigList.add(new ProjectSetting());
		sConfigList.add(new DoOnSave());
		sConfigList.add(new DefaultBatch()); 
		sConfigList.add(new FrameInterval()); 
		sConfigList.add(new MenuConfig()); 
		sConfigList.add(new AutoBackground()); 
//		sConfigList.add(new OtherConfig()); 
		sConfigList.add(new LuaTempleConfig()); 
		sConfigList.add(new ResIOS2AndroidData());
//		sConfigList.add(new ToLuaCppData());
	}
	
	public static void importElfConfig() { 
		XMLVersionManage.readFromXML(CONFIG_PATH);
	} 
	
	public static void exportElfConfig() {
		final LinkedList<Object> list = new LinkedList<Object>(sConfigList);
		XMLVersionManage.writeToXML(list, CONFIG_PATH);
	}
	
	public static void openSetConfig() { 
		final ElfDataXML [] datas = new ElfDataXML[sConfigList.size()];
		sConfigList.toArray(datas);
		new MultiButtonDialog().open("ElfConfig", PowerMan.getSingleton(MainDesigner.class).mShell, datas);
		//save
		exportElfConfig();
	} 
}
