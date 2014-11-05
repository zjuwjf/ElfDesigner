package com.ielfgame.stupidGame.config;

import java.util.LinkedList;

import com.ielfgame.stupidGame.data.ElfEnum;
import com.ielfgame.stupidGame.utils.FileHelper;

public class ProjectConfigEnum extends ElfEnum {

	public ProjectConfigEnum() { 
		this.setName("ProjectConfigEnum");
		
		refreshEnum();
	} 
	
	public void refreshEnum() { 
		final String dir = FileHelper.getUserDir() + FileHelper.DECOLLATOR + "config";
		
		final LinkedList<String> ids = FileHelper.getSimplePahIds(dir, new String[] { ".pcfg" }, false);
		final String[] array = new String[ids.size()];
		ids.toArray(array);
		
		this.setEnums(array);
	} 
	
//	public ProjectConfig getProjectConfig() {
//		final String fileName = FileHelper.getUserDir() + FileHelper.DECOLLATOR + "config"+FileHelper.DECOLLATOR+this.getCurrent();
//		final File f = new File(fileName);
//		if(f.exists() && f.isFile()) {
//			final LinkedList<Object> list = XMLVersionManage.getFactory().readFromXML(fileName);
//			for(final Object obj : list) {
//				if(obj instanceof ProjectConfig) {
//					return (ProjectConfig)obj;
//				}
//			}
//		}
//		return null;
//	}
	
	
	
}
