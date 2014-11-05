package com.ielfgame.stupidGame.config;

import java.util.HashMap;
import java.util.Set;

import com.ielfgame.stupidGame.trans.TransferRes;

public class CurrentPlist {
	
	public final static HashMap<String, Set<String>> plistToIdMap = new HashMap<String, Set<String>>();
	public final static HashMap<String, String> idToPlistMap = new HashMap<String, String>();
	public final static HashMap<String, String> plistToPvr = new HashMap<String, String>();
	public final static HashMap<String, String> pngMap = new HashMap<String, String>();
	
	public static void clear() {
		plistToIdMap.clear();
		idToPlistMap.clear();
		plistToPvr.clear();
		pngMap.clear();
	}
	
	public static String toCocosResid(String resid) { 
		if(resid == null) { 
			return resid; 
		} 
		
		final String head = CurrentPlist.idToPlistMap.get(resid);
		if(head != null) { 
			return head+"#"+resid; 
		} else { 
			final String value = pngMap.get(resid); 
			if(value != null) {
				resid = value ;
			} else {
				System.err.println("not found " + resid + " in ResLocation!");
			} 
		} 
		
		return resid;
	}
	
	public static boolean isInPlist(final String resid) {
		return idToPlistMap.containsKey( TransferRes.exportCompressPath(resid, true) );
	}
	
//	public static 
}
