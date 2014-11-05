package com.ielfgame.stupidGame.data;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringHelper {

	public static int readLastInt(final String string) {
		if (string != null) {
			Pattern p = Pattern.compile("[0-9]+");
			Matcher m = p.matcher(string);
			
			int ret = -1;
			while (m.find()) { 
				try {
					ret = Integer.valueOf(m.group());
				} catch (Exception e) {
					ret = -1;
					e.printStackTrace();
				} 
			} 
			
			return ret;
		} 

		return -1;
	}
	
	public static String replaceFirstInt(final String string, final int newInt) {
		if (string != null) {
			Pattern p = Pattern.compile("[0-9]+");
			Matcher m = p.matcher(string);
			
			while (m.find()) { 
				try {
					final String group = m.group();
					
					//with no error
					Integer.valueOf(group);
					
					final int index = string.lastIndexOf(group);
					if(index >= 0) {
						final StringBuffer sb = new StringBuffer();
						sb.append(string.substring(0, index));
						sb.append(newInt);
						sb.append(string.substring(index+group.length()));
						return sb.toString();
					}
				} catch (Exception e) { 
					e.printStackTrace();
				} 
			} 
			
			return null;
		} 

		return null;
	}
	
	public static void sortByLastInt(final String [] array) {
		if(array != null) {
			Arrays.sort(array, new Comparator<String>(){
				public int compare(String arg0, String arg1) {
					return readLastInt(arg0) - readLastInt(arg1);
				}
			});
		}
	}
	
	public static int compareStringByLastInt(String arg0, String arg1) {
		return readLastInt(arg0) - readLastInt(arg1);
	}
	
	public static void sortByLastInt(List<String> list) {
		if(list != null) {
			Collections.sort(list, new Comparator<String>(){
				public int compare(String arg0, String arg1) {
					return readLastInt(arg0) - readLastInt(arg1);
				}
			});
		}
	}

	public static void main(final String[] args) {
//		final String[] list = {
//				"a0",
//				"a1",
//				"a2",
//				"a3",
//				"a4",
//				"a5.pp",
//				"a10.34",
//				"a11.as",
//				"a20.tet",
//				"a100.png",
//		}; 
//		sortByLastInt(list);
//		
//		for (String s : list) {
//			System.err.println(s);
//		}
		
		System.err.println(replaceFirstInt("a10.34.png", 987));
	} 
}
