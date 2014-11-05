package com.ielfgame.stupidGame.lua;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import org.dom4j.CDATA;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.VisitorSupport;

import com.ielfgame.stupidGame.batch.TpPlistScaner;
import com.ielfgame.stupidGame.data.DataModel;
import com.ielfgame.stupidGame.power.PowerMan;
import com.ielfgame.stupidGame.utils.FileHelper;

public class ConvertXMLToRes {
	static String FULL_NAME = "";
	static final String USER_DIR = System.getProperty("user.dir");
	
	
//	private static final String H_PATH = FileHelper.DECOLLATOR+"template/make_res_h";
	private static final String LUA_PATH = FileHelper.DECOLLATOR+"template/make_res_lua";
//	private static final String LUA_ALL_PATH = FileHelper.DECOLLATOR+"template/make_all_res_lua";
	
	private static String XML_SIMPLE_NAME; 
	
	public static boolean convertToLuaRes(final String outPut, final String [] ids, final String [] comments) {
		return convert(outPut, LUA_PATH, ids, comments);
	}
	
//	public static boolean convertToHRes(final String outPut) { 
//		final TransferConfig tc = PowerMan.getSingleton(TransferConfig.class); 
//		final TpPlistScaner scaner = new TpPlistScaner(tc.ResLocation_REF_DIR); 
//		
//		final HashMap<String, Set<String>> plistToIdMap = new HashMap<String, Set<String>>();
//		final HashMap<String, String> idToPlistMap = new HashMap<String, String>();
//		final HashMap<String, String> plistToPvr = new HashMap<String, String>();
//		scaner.scanPlist(plistToIdMap, idToPlistMap, plistToPvr, false);
//		
//		final LinkedList<String> plistIds = new LinkedList<String>();
//		final Set<String> set = idToPlistMap.keySet();
//		for(final String key : set) { 
//			final String plist = idToPlistMap.get(key);
//			plistIds.add(plist+"#"+key);
//		} 
//		
//		Collections.sort(plistIds); 
//		
//		final HashMap<String, String> pngToPath = new HashMap<String, String>();
//		scaner.scanPng(pngToPath);
//		
//		final LinkedList<String> pngs = new LinkedList<String>();
//		for(String key : pngToPath.keySet()) {
//			pngs.add(pngToPath.get(key));
//		}
//		Collections.sort(pngs);
//		
//		final LinkedList<String> allIds = new LinkedList<String>(plistIds);
//		allIds.addAll(pngs);
//		
////		for(String id : allIds) {
////			System.err.println(id);
////		} 
//		
//		final String [] ids = new String[allIds.size()];
//		allIds.toArray(ids);
//		
//		final String [] comments = null; 
//		return convert(outPut, H_PATH, ids, comments); 
//	}
//	
//	public static boolean convertToAllLuaRes(final String outPut) { 
//		final TransferConfig tc = PowerMan.getSingleton(TransferConfig.class); 
//		final TpPlistScaner scaner = new TpPlistScaner(tc.ResLocation_REF_DIR); 
//		
//		final HashMap<String, Set<String>> plistToIdMap = new HashMap<String, Set<String>>();
//		final HashMap<String, String> idToPlistMap = new HashMap<String, String>();
//		final HashMap<String, String> plistToPvr = new HashMap<String, String>();
//		scaner.scanPlist(plistToIdMap, idToPlistMap, plistToPvr, false);
//		
//		final LinkedList<String> plistIds = new LinkedList<String>();
//		final Set<String> set = idToPlistMap.keySet();
//		for(final String key : set) { 
//			final String plist = idToPlistMap.get(key);
//			plistIds.add(plist+"#"+key);
//		} 
//		
//		Collections.sort(plistIds); 
//		
//		final HashMap<String, String> pngToPath = new HashMap<String, String>();
//		scaner.scanPng(pngToPath);
//		
//		final LinkedList<String> pngs = new LinkedList<String>();
//		for(String key : pngToPath.keySet()) {
//			pngs.add(pngToPath.get(key));
//		}
//		Collections.sort(pngs);
//		
//		final LinkedList<String> allIds = new LinkedList<String>(plistIds);
//		allIds.addAll(pngs);
//		
////		for(String id : allIds) {
////			System.err.println(id);
////		} 
//		
//		final String [] ids = new String[allIds.size()];
//		allIds.toArray(ids);
//		
//		final String [] comments = null; 
//		return convert(outPut, LUA_ALL_PATH, ids, comments); 
//	}
	
	private static boolean convert(final String outPut, final String templateFileName, final String [] ids, final String [] comments) {
		final String path = PowerMan.getSingleton(DataModel.class).getLastImportPath(); 
		XML_SIMPLE_NAME = FileHelper.getSimpleNameNoSuffix(path); 
		
		final File file = new File(USER_DIR + FileHelper.DECOLLATOR + templateFileName);
		if(!file.exists()) { 
			System.err.println("not found:"+templateFileName);
			return false;
		} 

		BufferedReader reader = null;
		BufferedWriter output = null;
		
		try {
			reader = new BufferedReader(new FileReader(file));
			output = new BufferedWriter(new FileWriter(new File(outPut)));
			
			String line = null;
			
			String template = "";
			boolean findTemplate = false;
			
			while ((line = reader.readLine()) != null) {
				if(line.contains("<template")) {
					findTemplate = true;
					template = line;
				} else if(line.contains("</template>")) {
					findTemplate = false;
					template += line;
					final String ret = dealTemplate(template, ids, comments);
					
					if(ret != null && ret.length() > 0) {
						output.write(ret+"\n");
					}
				} else if(findTemplate){
					template += (line + "\n");				
				} else {
					final ArrayList<String> keys = getKeys(line); 
					for(String key : keys) { 
						line = line.replace(key, replaceKey(key)); 
					} 
					output.write(line+"\n"); 
				} 
			} 
			
			reader.close();
			output.close();
		} catch (IOException e) { 
			e.printStackTrace();
			return false;
		} 
		
		return true;
	} 
	
	static String dealTemplate(final String template, final String [] ids, final String [] comments) {
		//<FULL_NAME>
		//<TYPE>
		//<List_NAME>
		//<ITEM_NUM>
		//<ACTION_NAME>
		//<ACTION_EXPRESSION>
		
		//<BUTTONS>
		//<MAPS>
		//<TEXTS>
		//<ELFNODES>
		//<LISTS>
		//<LAYOUTS>
		
		StringBuilder sb = new StringBuilder();
		
//		String ret = "";
		
		Document document = null;
		try {
			document = DocumentHelper.parseText(template);
		} catch (Exception e) { 
			e.printStackTrace(); 
		} 
		
		if(document != null) { 
			final Element element = document.getRootElement(); 
			final String name = element.attributeValue("name");
			final String [] templateIn = new String[10];
			
			element.accept(new VisitorSupport() { 
				public void visit(CDATA arg0) { 
					
					final String text = arg0.getText(); 
					
					final String [] lines = text.split("\n");
					for(int i=0; i<lines.length; i++) {
						templateIn[i] = lines[i] + "\n";
					} 
				} 
			});
			
			if(templateIn[0] != null) {
				if(name.equals("fields")) {
					
					for(int i=0; i<ids.length; i++) {
						String newTemple = templateIn[0];
//						newTemple = newTemple.replace("<XML_DATE>", new Date().toString()+" By "+FileHelper.getUserName());
						newTemple = newTemple.replace("<XML_DATE>", "");
						newTemple = newTemple.replace("<KEY>", ""+(i+1));
						newTemple = newTemple.replace("<RES_ID>", ids[i]);
						
						newTemple = newTemple.replace("<SIMPLE_RES_ID>", getResidVarName( ids[i] )); 
						
						if(comments != null && comments.length > i && comments[i] != null) {
							newTemple = newTemple.replace("<COMMENT>", comments[i]);
						} else {
							newTemple = newTemple.replace("<COMMENT>", "");
						}
						
						sb.append(newTemple);
					} 
				} else { 
					System.err.println(name);
				} 
			} 
		} 
		
		final String ret = sb.toString();
		if(ret == null || ret.equals("")) {
//			System.out.println(template);
		}
		
		return ret;
	} 
	
	static String getResidVarName(String resid) {
		if(resid == null) {
			return "null";
		} else {
			final int index = resid.lastIndexOf("#");
			if(index != -1) { 
				try {
					return resid.substring(index+1, resid.lastIndexOf("."));
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else { 
				return FileHelper.getSimpleNameNoSuffix(resid);
			}
		}
		
		return "null";
	}
	
	static String getNames(ArrayList<String> list) {
		String ret = "";
		for(String name: list) {
			ret += "_"+name+",";
		}
		return ret;
	}
	
	private static String replaceKey(String key) {
		if(key.equals("<XML_NAME>")) { 
			return XML_SIMPLE_NAME;
		} else { 
			System.err.println("not found key:"+key);
		} 
		
		return key;
	}
	
	static ArrayList<String> getKeys(final String line) {
		final ArrayList<String> keys = new ArrayList<String>();
		if(line != null) {
			final int length = line.length();
			int beg = -1;
			for(int i=0; i<length; i++) {
				if(line.charAt(i) == '<') {
					beg = i;
				} else if(line.charAt(i) == '>') {
					if(beg < 0) {
						System.err.println("no match <> in "+line);
					} else {
						keys.add(line.substring(beg, i+1));
						beg = -1;
					}
				}
			}
		}
		
		return keys;
	} 
	
	public static void main(String [] args) {
		final TpPlistScaner scaner = new TpPlistScaner("D:\\AndroidRes\\landofhero\\debug\\GameRes\\p\\");
		
		final HashMap<String, Set<String>> plistToIdMap = new HashMap<String, Set<String>>();
		final HashMap<String, String> idToPlistMap = new HashMap<String, String>();
		final HashMap<String, String> plistToPvr = new HashMap<String, String>();
		scaner.scanPlist(plistToIdMap, idToPlistMap, plistToPvr, false);
		
		final LinkedList<String> plistIds = new LinkedList<String>();
		final Set<String> set = idToPlistMap.keySet();
		for(final String key : set) { 
			final String plist = idToPlistMap.get(key);
			plistIds.add(plist+"#"+key);
		} 
		
		Collections.sort(plistIds); 
		
		final HashMap<String, String> pngToPath = new HashMap<String, String>();
		scaner.scanPng(pngToPath);
		
		final LinkedList<String> pngs = new LinkedList<String>();
		for(String key : pngToPath.keySet()) {
			pngs.add(pngToPath.get(key));
		}
		Collections.sort(pngs);
		
		final LinkedList<String> allIds = new LinkedList<String>(plistIds);
		allIds.addAll(pngs);
		
		for(String id : allIds) {
			System.err.println(id);
		} 
	} 
} 
