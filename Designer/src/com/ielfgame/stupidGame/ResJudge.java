package com.ielfgame.stupidGame;

import java.io.File;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.ielfgame.stupidGame.utils.FileHelper;

public class ResJudge {
	private static final ResourceBundle sResourceBundle = ResourceBundle.getBundle("elfEditor");
	
	public static String getResourceString(String key) {
		try {
			return sResourceBundle.getString(key);
		} catch (MissingResourceException e) {
			return key;
		} catch (NullPointerException e) {
			return "!" + key + "!";
		}
	}
	
	public static String getResourceString(String key, Object[] args) {
		try {
			return MessageFormat.format(getResourceString(key), args);
		} catch (MissingResourceException e) {
			return key;
		} catch (NullPointerException e) {
			return "!" + key + "!";
		}
	}
	
	public static boolean isRes(String path){
		if(path == null){ 
			return false; 
		} else {
			if(path.endsWith(".png") || path.endsWith(".jpg")){
				return true;
			}
			return false;
		}
	}
	
	public static boolean isLegalResNotExisted(String path){
		if(path == null){
			return false;
		} else {
			if(path.endsWith(".png") || path.endsWith(".jpg")){
				final File file = new File(path);
				if(!file.exists() || file.isDirectory()) {
					return true;
				} 
				return false;
			}
			return false;
		}
	}
	
	public static boolean isLegalResAndExisted(String path){
		if(path == null){
			return false;
		} else {
			if(path.endsWith(".png") || path.endsWith(".jpg")){
				final File file = new File(path);
				if(file.exists() && file.isFile()) {
					return true;
				} 
				return false;
			}
			return false;
		}
	}
	
	public static boolean isXML(String path){
		if(path == null){
			return false;
		} else {
			if(path.endsWith(".xml")){
				return true;
			}
			return false;
		}
	}
	
	public static boolean isSWF(String path){
		if(path == null){
			return false;
		} else {
			if(path.endsWith(".swf")){
				return true;
			}
			return false;
		}
	}
	
	public static boolean isCocos(String path){
		if(path == null){
			return false;
		} else {
			if(path.endsWith(".cocos")){
				return true;
			}
			return false;
		}
	}
	
	public static boolean isPlist(String path){
		if(path == null){
			return false;
		} else {
			if(path.endsWith(".plist")){
				return true;
			}
			return false;
		}
	}
	
	public static boolean isLua(String path){
		if(path == null){
			return false;
		} else {
			if(path.endsWith(".lua")){
				return true;
			}
			return false;
		}
	}
	
	public static String getLittleName(String path){
		if(isRes(path)){ 
			int index = path.lastIndexOf(FileHelper.DECOLLATOR);
			
			if(index >= 0){
				return path.substring(index+1, path.length() - 4);
			}
		}
		return path;
	}
}
