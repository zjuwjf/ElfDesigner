package com.ielfgame.stupidGame.auto2C;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;

import com.ielfgame.stupidGame.utils.FileHelper;

public class MakeCPlusClass {
	
	final static String DIR = "D:\\svn-code\\NewWorld\\MyTest\\Extends";
	
	public static void main(final String [] args) { 
		make("LinearLayour2D",DIR); 
	} 
	
	static void make(final String className, final String dir) { 
		final File fh = new File(dir + "\\" + className + ".h"); 
		final File fcpp =  new File(dir + "\\" + className + ".cpp"); 
		
		if(fh.exists() || fcpp.exists()) { 
			System.err.println(className + " exists");
			return ;
		}
		
		final BufferedReader hReader = FileHelper.getReader(MakeCPlusClass.class, "class_h");
		final BufferedWriter hWrite = FileHelper.getWriter(dir+"\\" + className + ".h");
		final String upClassName = className.toUpperCase();
		
		String line = null;
		try {
			while((line = hReader.readLine()) != null) {
				line = line.replace("Up_Name", upClassName);
				line = line.replace("Name", className);
				
				hWrite.write(line+"\n");
			}
			
			hWrite.flush();
			hWrite.close();
			
			hReader.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		final BufferedReader cppReader = FileHelper.getReader(MakeCPlusClass.class, "class_cpp");
		final BufferedWriter cppWrite = FileHelper.getWriter(dir+"\\" + className + ".cpp");
		
		line = null;
		try {
			while((line = cppReader.readLine()) != null) { 
				line = line.replace("Up_Name", upClassName); 
				line = line.replace("Name", className); 
				cppWrite.write(line+"\n"); 
			} 
			
			cppWrite.flush();
			cppWrite.close();
			
			cppReader.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		System.err.println("make " + className + " completed!");
	}
}
