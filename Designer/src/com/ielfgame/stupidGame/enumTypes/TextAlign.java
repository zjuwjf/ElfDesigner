package com.ielfgame.stupidGame.enumTypes;

public enum TextAlign {
	LEFT, CENTER, RIGHT;
	
	public static TextAlign fromString(String text){
		final TextAlign [] es = TextAlign.values();
		for(TextAlign e : es){
			if(e.toString().equalsIgnoreCase(text)){
				return e;
			}
		}
		return null;
	}
	
	public static String [] toStrings(){
		final String [] ret = new String[TextAlign.values().length];
		
		for(int i=0; i<ret.length; i++){
			ret[i] = TextAlign.values()[i].toString();
		}
		
		return ret;
	}
}
