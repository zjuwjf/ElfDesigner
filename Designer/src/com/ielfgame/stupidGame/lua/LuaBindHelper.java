package com.ielfgame.stupidGame.lua;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

import com.ielfgame.stupidGame.utils.FileHelper;

public class LuaBindHelper {
	static int MODE = 2;
	public static void main(String[] args) {
		if(MODE == 0) {
			try {
				final Class<?>[] _classes = getClasses("org.cocos2d.actions");
				for (Class<?> _class : _classes) {
					System.err.println("CC"+_class.getSimpleName()+" = luajava.bindClass(\""+_class.getName()+"\")");
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else if(MODE == 1) {
			//for tipTexts
			final ArrayList<String> constructors = getStaticConstructors("C:\\Users\\zju_wjf\\workspace\\Study\\src\\org\\cocos2d\\actions");
			for(String construct : constructors) {
				System.err.println(construct);
			}
		} else if(MODE == 2) {
			//for key words
			try {
				final Class<?>[] _classes = getClasses("org.cocos2d.actions");
				for (Class<?> _class : _classes) {
					//set1.add("CCRotateBy");
					System.err.println("set1.add(\"CC"+_class.getSimpleName()+"\");");
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} 
	} 
	
	
	
	//
	public static ArrayList<String> getStaticConstructors(final String path) {
		final ArrayList<String> ret = new ArrayList<String>();
		if(path != null) {
			
			final File file = new File(path);
			if(file.exists()) {
				if(file.isDirectory()) {
					final String [] paths = file.list();
					for(String childPath : paths) {
						final ArrayList<String> childRet = getStaticConstructors(path+FileHelper.DECOLLATOR+childPath);
						ret.addAll(childRet);
					}
				} else if(path.endsWith(".java")){
					final int index = path.lastIndexOf(FileHelper.DECOLLATOR);
					final String simpleName = path.substring(index+1, path.length()-5);
					
					final String key1 = "public static ";
					final String key2 = "(";
					final String key3 = ")";
					final String key4 = "//";
					
					try {
						BufferedReader br = new BufferedReader(new FileReader(file));
						String line = br.readLine();
						while(line != null) {
							if(line.contains(key1) && line.contains(key2) && line.contains(key3) && !line.contains(key4)) {
								final int startIndex = line.indexOf("(");
								final int endIndex = line.lastIndexOf(")");
								final String parameter = line.substring(startIndex, endIndex+1);
								
								final String before = line.substring(0, startIndex);
								StringTokenizer strT1 = new StringTokenizer(before," \t");
								String method = null;
								while(strT1.hasMoreElements()){
									method = strT1.nextToken();
								}
								
								ret.add("sTipTexts.add(\""+"CC"+simpleName+":"+method+parameter+"\");");
								return ret;
							}
							line = br.readLine();
						}
						
					} catch (Exception e) {
					}
				}
			}
		}
		System.err.println(path + " failed to getStaticConstructor!");
		return ret;
	}
	
	public static Class<?>[] getClasses(String pckgname) throws ClassNotFoundException {
		ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
		File directory = null;
		try {
			ClassLoader cld = Thread.currentThread().getContextClassLoader();
			if (cld == null)
				throw new ClassNotFoundException("Can't get class loader.");
			String path = pckgname.replace('.', '/');
			URL resource = cld.getResource(path);
			if (resource == null)
				throw new ClassNotFoundException("No resource for " + path);
			directory = new File(resource.getFile());
		} catch (NullPointerException x) {
			throw new ClassNotFoundException(pckgname + " (" + directory + ") does not appear to be a valid package a");
		}
		if (directory.exists()) {
			final File[] files = directory.listFiles();

			for (int i = 0; i < files.length; i++) {
				final File file = files[i];
				final String name = file.getName();
				if (name.endsWith(".class")) {
					classes.add(Class.forName(pckgname + '.' + name.substring(0, name.length() - 6)));
				} else if (file.isDirectory()) {
					final Class<?>[] rets = getClasses(pckgname + "." + file.getName());
					for (Class<?> ret : rets) {
						classes.add(ret);
					}
				} else {
					System.err.println("unnormal " + files[i]);
				}
			}
		} else
			throw new ClassNotFoundException(pckgname + " does not appear to be a valid package b");

		Class<?>[] classesA = new Class[classes.size()];
		classes.toArray(classesA);
		return classesA;
	}
}
