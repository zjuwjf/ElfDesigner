package com.ielfgame.stupidGame.xml;

import java.util.HashMap;
import java.util.LinkedList;

public class XMLStringManage { 
	private final static HashMap<String, LinkedList<String>> sResidMap = new  HashMap<String, LinkedList<String>>();
	private final static HashMap<String, LinkedList<String>> sResidArrayMap = new  HashMap<String, LinkedList<String>>();
	
	public static void checkInString(final String label, final LinkedList<String> list) { 
		sResidMap.put(label, list); 
	} 
	
	public static void checkInStringArray(final String label, final LinkedList<String> list) { 
		sResidArrayMap.put(label, list); 
	} 
	
	public static boolean isString(final String label, final String attribute) { 
		final LinkedList<String> list = sResidMap.get(label);
		if(list != null) { 
			final boolean ret = list.contains(attribute);
			return ret;
		} 
		return false; 
	} 
	
	public static boolean isStringArray(final String label, final String attribute) { 
		final LinkedList<String> list = sResidArrayMap.get(label);
		if(list != null) { 
			final boolean ret = list.contains(attribute); 
			return ret;  
		} 
		return false; 
	} 
	
//	public static boolean checkIsChinese(final String string, boolean isResid) {
//		if(isResid) { 
//			if(ResJudge.isLegalRes(string)) {
//				
//			} 
//		} else { 
//			
//		} 
//		return false;
//	}
} 
