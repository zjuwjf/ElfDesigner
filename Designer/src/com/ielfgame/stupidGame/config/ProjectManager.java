package com.ielfgame.stupidGame.config;

import java.util.ArrayList;

import com.ielfgame.stupidGame.utils.FileHelper;

public class ProjectManager {
	
	static String startUpProject;
	
	protected final static String HEAD_DIR = FileHelper.getUserDir() + FileHelper.DECOLLATOR;
	private final static ArrayList<String> sProjectArray = new ArrayList<String>();
	private static String sCurrentProject;

	private static void flush() {
		//
	}

	public static void addProject(final String name) {
		if (sProjectArray.contains(name)) {
			//
		} else {
			sProjectArray.add(name);
			sCurrentProject = name;
			flush();
			
			editProject(name);
		}
	}

	public static void removeProject(final String name) {
		if (sProjectArray.contains(name)) {
			sProjectArray.remove(name);
			flush();
		} else {
		}
	}

	public static void editProject(final String name) {
		if (sProjectArray.contains(name)) {
			// open dialog ?
			
		} else {
		}
	}

	public static void setCurrentProject(final String name) {
		if (sProjectArray.contains(name)) {
			sCurrentProject = name;
		} else {
		}
	}

}
