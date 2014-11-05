package com.ielfgame.stupidGame.versionConverter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.LinkedList;

import com.ielfgame.stupidGame.utils.FileHelper;
import com.ielfgame.stupidGame.xml.XMLManifest;
import com.ielfgame.stupidGame.xml.XMLVersionManage;


public class VersionConverter {
	//
	public static void main(final String [] args) {
		
		final String USER_DIR = System.getProperty("user.dir");
		final File [] files = new File(USER_DIR).listFiles();
		
		try {
			PrintStream out = new PrintStream(new BufferedOutputStream(new FileOutputStream(new File(USER_DIR + FileHelper.DECOLLATOR+"VersionConvert.log"))),true);
			System.setErr(out);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		System.err.println("VersionConverter : "+new Date().toString()); 
		
		try {
			for(final File f : files) { 
				if(!f.isDirectory()) { 
					final String path = f.getAbsolutePath();
					if(path.endsWith(".xml")) {
						final String ret = doFile(path);  
						System.err.println(ret); 
					} 
				} 
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
	
	static String doFile(final String path) {
		final String version = XMLManifest.getVersion(path);
		final String newVersion = XMLVersionManage.getVersion();
		
		final LinkedList<Object> exports = XMLVersionManage.readFromXML(path);
		
		if(exports != null) { 
			XMLVersionManage.writeToXML(exports, path);
			XMLVersionManage.writeToCocos(exports, path.replace(".xml", ".cocos"));
			
			return "convert " + path + "\tvesion " + version + "\n" + "create " + path + ", " + path.replace(".xml", ".cocos") + "\tvesion " + newVersion;
		} else { 
			return "convert " + path+ "\tvesion failed!" ;
		} 
	} 
} 
