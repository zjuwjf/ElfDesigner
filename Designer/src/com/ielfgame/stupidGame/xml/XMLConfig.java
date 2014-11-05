package com.ielfgame.stupidGame.xml;

public class XMLConfig {
	public static final String ENTER = "&#x0D;";
	
	public static String toXML(String text) {
		if(text == null) {
			return "";
		} 
		return text.replace("\n", ENTER);
	} 
	
	public static String fromXML(String text) {
		if(text == null) {
			return "";
		} 
		return text.replace(ENTER, "\n");
	} 
}
