package com.ielfgame.stupidGame.enumTypes;

import org.eclipse.swt.SWT;

public enum TextStyle {
	Normal(SWT.NORMAL),
	Italic(SWT.ITALIC),
	Bold(SWT.BOLD),
	BoldItalic(SWT.BOLD  | SWT.ITALIC),
	;
	public final int styleValue;
	TextStyle(int value){
		styleValue = value;
	}
	
	public static TextStyle fromString(String text){
		final TextStyle [] es = TextStyle.values();
		for(TextStyle e : es){
			if(e.toString().equalsIgnoreCase(text)){
				return e;
			}
		}
		return null;
	}
	
	public static String [] toStrings(){
		final String [] ret = new String[TextStyle.values().length];
		
		for(int i=0; i<ret.length; i++){
			ret[i] = TextStyle.values()[i].toString();
		}
		
		return ret;
	}
}

