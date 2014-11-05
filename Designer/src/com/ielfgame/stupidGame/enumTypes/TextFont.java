package com.ielfgame.stupidGame.enumTypes;

import java.util.ArrayList;

import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;

import com.ielfgame.stupidGame.data.SystemType;

public class TextFont {
	private final static ArrayList<String> FontNameList = new ArrayList<String>();
	private final static String DEFUALT_FONT; 
	private final static float FONT_SIZE_RATE; 
	
	private final static float CURRENT_FONT_TO_MAC_FONT_RATE;

	static {
		FontData[] fontDatas = Display.getCurrent().getFontList(null, true);
		for (int i = 0; i < fontDatas.length; i++) {
			// remove duplicates and sort
			String nextName = fontDatas[i].getName();
			if (!FontNameList.contains(nextName)) {
				int j = 0;
				while (j < FontNameList.size() && nextName.compareTo( FontNameList.get(j)) > 0) {
					j++;
				}
				FontNameList.add(j, nextName); 
			} 
		} 
		fontDatas = Display.getCurrent().getFontList(null, false);
		for (int i = 0; i < fontDatas.length; i++) {
			// remove duplicates and sort
			String nextName = fontDatas[i].getName();
			if (!FontNameList.contains(nextName)) {
				int j = 0;
				while (j < FontNameList.size() && nextName.compareTo(FontNameList.get(j)) > 0) {
					j++;
				} 
				FontNameList.add(j, nextName);
			} 
		} 
		
		if(SystemType.CURRENT_SYSTEM == SystemType.Mac) {
			DEFUALT_FONT = "Helvetica";
			FONT_SIZE_RATE = 19f/25;
			CURRENT_FONT_TO_MAC_FONT_RATE = 1;
		} else { 
			DEFUALT_FONT = "@Arial Unicode MS";
			FONT_SIZE_RATE = 1.0f;
//			CURRENT_FONT_TO_MAC_FONT_RATE = 19f/25;
			CURRENT_FONT_TO_MAC_FONT_RATE = 1;
		} 
	} 
	
	public static String getDefaultFont() {
		return DEFUALT_FONT;
	}
	
	public static float getDefaultFontSizeRate() {
		return FONT_SIZE_RATE;
	}
	
	public static float getCurrentFont2MacFontRate() {
		return CURRENT_FONT_TO_MAC_FONT_RATE;
	}
	
//	public static float getMacFont2CurrentFontRate() {
//		return 1f/CURRENT_FONT_TO_MAC_FONT_RATE;
//	}
	
	public static int getFontIndex(String font){
		return FontNameList.indexOf(font);
	}
	
	public static String getFont(int index){
		return FontNameList.get(index);
	}
	
	public static String [] toStrings(){
		
		final String [] ret = new String[FontNameList.size()];
		
		FontNameList.toArray(ret);
		
		return ret;
	}
	
	
}
