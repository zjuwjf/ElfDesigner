package com.ielfgame.stupidGame.packer;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import com.ielfgame.stupidGame.ConfigImExport;
import com.ielfgame.stupidGame.batch.TpPlistScaner;
import com.ielfgame.stupidGame.config.ElfConfig;
import com.ielfgame.stupidGame.data.DataModel;
import com.ielfgame.stupidGame.texturePacker.TexturePackerHelper;
import com.ielfgame.stupidGame.utils.FileHelper;

public class AndroidPacker {
	
	static boolean rename = false;
	
	public static void main(final String [] args) { 
		final String dir = "D:\\svn-code\\Games\\LordRise\\GameRes\\";
		
//		final LinkedList<String> plists = FileHelper.getFullPahIds(dir, new String[]{".plist"}, true);
		final TpPlistScaner scaner = new TpPlistScaner(dir);
		final HashMap<String, Set<String>> plistToIdMap = new HashMap<String, Set<String>>();
		final HashMap<String, String> idToPlistMap = new HashMap<String, String>();
		final HashMap<String, String> plistToPvr = new HashMap<String, String>();
		scaner.scanPlist(plistToIdMap, idToPlistMap, plistToPvr, false);
		
		final Set<String> idSet = idToPlistMap.keySet();
		final LinkedList<String> idList = new LinkedList<String>(idSet);
		Collections.sort(idList);
		
		new DataModel();
		
		ElfConfig.importElfConfig();
		ConfigImExport.importConfigs();
		
		if(rename) {
			final String [] pngs = FileHelper.getSimplePaths("D:\\transfer\\pic\\");
			for(String png : pngs) {
				if(png.startsWith("_")) {
					new File("D:\\transfer\\pic\\"+png).renameTo(new File("D:\\transfer\\pic\\"+png.substring(1)));
				}
			}
			return ;
		}
		
//		for(String id : idList) { 
//			if(id.startsWith("lkx_") || id.startsWith("gyx_")) {
//				id = "_" + id;
//			} 
//			final String path = TransferRes.importCompressPath(id); 
//			
//			TransferRes.copyRes(path, "D:\\transfer\\pic", true, null); 
//		} 
		
		final String head = "D:\\svn-code\\Games\\LordRise\\proj.android\\GameRes\\";
		
		final Set<String> plistToIdMapSet = plistToIdMap.keySet(); 
		for(String plist : plistToIdMapSet) { 
//			System.err.println(plist); 
			
			final Set<String> ids = plistToIdMap.get(plist);
			plist = plist.replace("/", "\\");
//			System.err.println();
			final String newPlistPath = plist;
			
			final HashSet<String> newIdsSet = new HashSet<String>();
			for(String id : ids) {
				if(id.startsWith("_")) {
					newIdsSet.add(id.substring(1));
				} else {
					newIdsSet.add(id);
				}
			}
//			String inputs = "";
			
			TexturePackerHelper.pack("D:\\transfer\\pic", head, newPlistPath, newIdsSet, newIdsSet.size(), "RGBA4444", "--dither-fs-alpha", 0.5f);
			
		} 
		// scale 0.5
		
		// 
		
		
	} 
} 
