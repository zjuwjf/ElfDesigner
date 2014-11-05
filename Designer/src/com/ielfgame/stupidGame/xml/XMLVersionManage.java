package com.ielfgame.stupidGame.xml;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class XMLVersionManage{
	
	private static final LinkedList<IXMLFactory> sFactorys = new LinkedList<IXMLFactory>();
	
	static {
		System.err.println("load XMLVersionManage");
	}
	
	static { 
		sFactorys.add(new NewXMLFactory());//2
	} 
	static IXMLFactory sCurrentFactory = null;
	
	public static IXMLFactory getFactory() {
		if(sCurrentFactory != null) {
			return sCurrentFactory;
		} else {
			return sFactorys.getLast();
		}
	}

	public static String getVersion() { 
		return getFactory().getVersion(); 
	} 

	public static void writeToCocos(List<Object> exports, String path) { 
		getFactory().writeToCocos(exports, path);
	} 

	public static void writeToXML(List<Object> exports, String path) {
		getFactory().writeToXML(exports, path);
	} 

	public static LinkedList<Object> readFromXML(String path) { 
		
		final String version = XMLManifest.getVersion(path);
		IXMLFactory readFactory = getFactory();
		
		if(readFactory != null) {
			System.err.println("readFromXML " + path + " Yes"); 
			return readFactory.readFromXML(path); 
		} else {
			System.err.println("readFromXML : "+path);
			System.err.println("Not A IXMLFactory Could Accept Version "+version);
		} 
		return null;
	}
	
	
	
	public static LinkedList<Object> readFromXML(final InputStream is) { 
		
//		final String version = XMLManifest.getVersion(path);
		IXMLFactory readFactory = getFactory();
		
		if(readFactory != null) {
//			System.err.println("readFromXML " + path + " Yes"); 
			return readFactory.readFromXML(is); 
		} else {
			System.err.println("readFromXML InputStream Error");
//			System.err.println("Not A IXMLFactory Could Accept Version "+version);
		} 
		return null;
	}
	
	public static Set<String> getAllResids(InputStream is) { 
		IXMLFactory readFactory = getFactory(); 
		if(readFactory != null) { 
			return readFactory.getAllResids(is); 
		} 
		
		return null;
	} 
	
	public static Set<String> getAllResids(String xmlPath) { 
		final String version = XMLManifest.getVersion(xmlPath); 
		IXMLFactory readFactory = getFactory(); 
		
		if(readFactory != null) { 
			return readFactory.getAllResids(xmlPath); 
		} else { 
			System.err.println("getAllResids : "+xmlPath); 
			System.err.println("Not A IXMLFactory Could Accept Version "+version);
		} 
		
		return null;
	} 
} 
