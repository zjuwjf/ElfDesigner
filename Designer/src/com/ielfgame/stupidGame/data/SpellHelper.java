package com.ielfgame.stupidGame.data;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

public class SpellHelper {
	@SuppressWarnings("deprecation")
	public static String getEname(String name) {
		HanyuPinyinOutputFormat pyFormat = new HanyuPinyinOutputFormat();
		pyFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		pyFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		pyFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
		
		try {
			return PinyinHelper.toHanyuPinyinString(name, pyFormat, "");
		} catch (Exception e) {
		}
		return name;
	}

	public static String getUpEname(String name) {
		if(name == null) {
			return "Null";
		} 
		char[] strs = name.toCharArray();
		String newname = "";
		
		for(int i=0; i<strs.length; i++) {
			if(IsChineseOrNot.isChinese(""+strs[i])) {
				newname += toUpCase(getEname(""+strs[i]));
			} else {
				newname += strs[i];
			}
		}
		
		return newname;
	}
	
	private static String toUpCase(String str) {
		StringBuffer newstr = new StringBuffer();
		newstr.append((str.substring(0, 1)).toUpperCase()).append(str.substring(1, str.length()));
		
		return newstr.toString();
	}

	public static void main(String[] args) {
		System.out.println(getUpEname("中文-a1.png"));

	}

}