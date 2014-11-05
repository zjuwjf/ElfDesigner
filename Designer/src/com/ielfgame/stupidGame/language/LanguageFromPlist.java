package com.ielfgame.stupidGame.language;

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import com.ielfgame.stupidGame.utils.FileHelper;

public class LanguageFromPlist {

	/** 
	 * 
	 * read content from project/resource/language
	 * megre the modify to content
	 * */
	public static void writePlist1(final String plistPathInP,final HashMap<String, String> map
			,final HashMap<String,String> key2oper) {
		
		try{
			HashMap<String,String> content = readKeyValueFromPlist(new File(plistPathInP));
			if(content != null && content.size() > 0 
					&& map.size() > 0 && key2oper.size() > 0){
				
				final Set<String> keyset = key2oper.keySet();
				
				final ArrayList<String> keyarray = new ArrayList<String>(keyset);
				for(String key : keyarray){
					String oper = key2oper.get(key);
					if(oper.equals("A") && map.containsKey(key)){
						content.put(key, map.get(key));
					}
					else if(oper.equals("R") && content.containsKey(key)){
						content.remove(key);
					}
				}
			}
			
			writePlist0(plistPathInP,content);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void writePlist(final String editmode,final String plistPathDes, final String plistPathInP,final HashMap<String, String> map
			,final HashMap<String,String> key2oper) {
		if(editmode != null && editmode.equals("Mode1")){
			writePlist1(plistPathInP,map,key2oper);
		}else{
			writePlist0(plistPathDes,map);
		}
	}
	
	public static void writePlist0(final String plistPath, final HashMap<String, String> map) {
		try {
			final LinkedList<String> lines = new LinkedList<String>();
			
			lines.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			lines.add("<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">");
			lines.add("<plist version=\"1.0\">");
			lines.add("<dict>");
			
			final Set<String> keyset = map.keySet();
			
			final ArrayList<String> keyarray = new ArrayList<String>(keyset);
			Collections.sort(keyarray);
			
			for(String key : keyarray) {
				lines.add(String.format("\t<key>%s</key>", key));
				lines.add(String.format("\t<string>%s</string>", map.get(key)));
			}
			
			lines.add("</dict>");
			lines.add("</plist>");
			
			FileHelper.writeUTF8(lines, new File(plistPath));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static HashMap<String, String> readKeyValueFromPlist(final File plsitfile) {
		final HashMap<String, String> map = new HashMap<String, String>();

		if (plsitfile != null && plsitfile.exists() && plsitfile.isFile()) {
			final BufferedReader reader = FileHelper.getUTF8Reader(plsitfile);
			String line = null;
			try {
				String key = null;
				String value = null;
				
				/**
				 * 
				 */
				String keyValue  = null;
				
				while ((line = reader.readLine()) != null) {
					
					if(keyValue != null) {
						final int index = line.indexOf("</string>");
						if(index >= 0) {
							keyValue = keyValue + "\n" + line.substring(0, index);
							
							value = keyValue;
							keyValue = null;
							
						} else {
							keyValue = keyValue + "\n" + line;
						} 
						
					} else if (key == null) {
						final int index = line.indexOf("</key>");
						if (index >= 0) {
							final int indexStart = line.indexOf("<key>");
							if (indexStart >= 0) {
								key = line.substring(indexStart + 5, index);
							}
						}
					} else if (value == null) {
						final int indexStart = line.indexOf("<string>");
						if (indexStart >= 0) {
							final int index = line.indexOf("</string>");
							if(index >= 0) {
								value = line.substring(indexStart + 8, index);
							} else {
								keyValue = line.substring(indexStart + 8);
							}
						}
					}
					
					if(key != null && value != null) {
						map.put(key, value);
						
						key = null;
						value = null;
					}
				}
				
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return map;
	}

}
