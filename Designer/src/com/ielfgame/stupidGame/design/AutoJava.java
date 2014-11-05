package com.ielfgame.stupidGame.design;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

import com.ielfgame.stupidGame.data.SpellHelper;
import com.ielfgame.stupidGame.lua.ConvertXMLToLua;
import com.ielfgame.stupidGame.lua.ConvertXMLToLua.Union;
import com.ielfgame.stupidGame.utils.FileHelper;

public class AutoJava { 
	
	final static String DEFAULT_DIR = FileHelper.getUserDir()+"\\src\\com\\ielfgame\\stupidGame\\design\\hotSwap"; 
	
	public static void autoJava(final String xmlPath) { 
		autoJava(xmlPath, DEFAULT_DIR); 
	} 
	
	public static void autoJava(final String xmlPath, final String dir) { 
		final String simpleName = SpellHelper.getUpEname(xmlPath.substring(xmlPath.lastIndexOf("\\")+1, xmlPath.lastIndexOf(".")));
		
		System.err.println("simpleName" + simpleName); 
		
		final LinkedList<String> outlines = new LinkedList<String>();
		
		final String ouputPath = dir+"\\Surface" + simpleName + ".java";
		
		int freeCodeStartLine = -1; 
		int freeCodeEndLine = -1; 
		
		int initCodeStartLine = -1;
		int definesCodeStartLine = -1;
		
		if( !new File(ouputPath).exists() ) { 
			try { 
				final BufferedReader reader = FileHelper.getReader(AutoJava.class, "SurfaceTemple_java"); 
				String line = null;
				int count = 0;
				try { 
					while((line = reader.readLine()) != null) { 
						if(freeCodeStartLine >= 0 && freeCodeEndLine < 0) {
							//ignore
							if(line.contains("@@auto-code-end")) { 
								freeCodeEndLine = count; 
								line = line.replace("Temple", simpleName).replace("XML-PATH", xmlPath.replace("\\", "\\\\"));
								outlines.add(line); 
								count++;
								freeCodeStartLine = freeCodeEndLine = -1;
							} 
						} else {
							if(line.contains("@@auto-code-start")) {
								freeCodeStartLine = count;
								if(line.contains("initialize")) {
									initCodeStartLine = freeCodeStartLine;
								} else if(line.contains("defines")) {
									definesCodeStartLine = freeCodeStartLine;
								} 
							} 
							line = line.replace("Temple", simpleName).replace("XML-PATH", xmlPath.replace("\\", "\\\\"));
							outlines.add(line); 
							count++;
						} 
					} 
				} catch (Exception e) {
					e.printStackTrace();
				} 
				
				reader.close();
			} catch (Exception e) { 
				e.printStackTrace(); 
			} 
		} else { 
			try { 
				final BufferedReader reader = FileHelper.getReader(ouputPath); 
				String line = null;
				int count = 0;
				try { 
					while((line = reader.readLine()) != null) { 
						System.err.println(line);
						
						if(freeCodeStartLine >= 0 && freeCodeEndLine < 0) {
							//ignore
							if(line.contains("@@auto-code-end")) { 
								freeCodeEndLine = count; 
								line = line.replace("Temple", simpleName).replace("XML-PATH", xmlPath.replace("\\", "\\\\"));
								outlines.add(line); 
								count++;
								freeCodeStartLine = freeCodeEndLine = -1;
							} 
						} else {
							if(line.contains("@@auto-code-start")) {
								freeCodeStartLine = count;
								if(line.contains("initialize")) {
									initCodeStartLine = freeCodeStartLine;
								} else if(line.contains("defines")) {
									definesCodeStartLine = freeCodeStartLine;
								} 
							} 
							line = line.replace("Temple", simpleName).replace("XML-PATH", xmlPath.replace("\\", "\\\\"));
							outlines.add(line); 
							count++;
						} 
					} 
				} catch (Exception e) { 
					e.printStackTrace();
				} 
				
				reader.close();
			} catch (Exception e) { 
				e.printStackTrace(); 
			} 
		} 
		
		if(initCodeStartLine<0 || definesCodeStartLine <0 || freeCodeEndLine != -1 || freeCodeStartLine != -1) {
			System.err.println("ERROR: check auto-code-start&auto-code-end "+freeCodeStartLine+","+freeCodeEndLine); 
			return ;
		} 
		
		ArrayList<ArrayList<Union>> nodesAndActions = ConvertXMLToLua.convertToJava(xmlPath); 
		final ArrayList<Union> allFullNames = nodesAndActions.get(0); 
		
		final LinkedList<String> insertLines = new LinkedList<String>();
		for(Union u : allFullNames) { 
			if(u.type.equals("ElfAction")) { 
				insertLines.add(getDefineActionLine(u)); 
			} else { 
				insertLines.add(getDefineNodeLine(u)); 
			} 
		} 
		outlines.addAll(definesCodeStartLine+1, insertLines);
		
		insertLines.clear(); 
		for(Union u : allFullNames) { 
			if(u.type.equals("ElfAction")) { 
				insertLines.add(getInitActionLine(u)); 
			} else { 
				insertLines.add(getInitNodeLine(u)); 
			} 
		} 
		outlines.addAll(initCodeStartLine+1, insertLines);
		
		
		
		try {
			final BufferedWriter writer = FileHelper.getWriter(ouputPath); 
			for(String line : outlines) { 
				writer.write(line+"\n"); 
			} 
			
			writer.flush();
			writer.close();
		} catch (Exception e) {
		}
	} 
	
	static String getDefineNodeLine(Union u) {
		final String line = "\tprotected NODE-TYPE _NODE-NAME;";
		return line.replace("NODE-TYPE", u.type).replace("NODE-NAME", SpellHelper.getUpEname(u.name));
	} 
	
	static String getDefineActionLine(Union u) { 
		final String line = "\tprotected CCActionData _ACTION-NAME;";
		return line.replace("ACTION-NAME", SpellHelper.getUpEname(u.name)); 
	} 
	
	static String getInitNodeLine(Union u) { 
		final String line = "\t\t_NODE-NAME0 = (NODE-TYPE)findNodeByName(\"NODE-NAME1\");";
		return line.replace("NODE-TYPE", u.type).replace("NODE-NAME0", SpellHelper.getUpEname(u.name)).replace("NODE-NAME1", u.name);
	} 
	
	static String getInitActionLine(Union u) { 
		final String line = "\t\t_ACTION-NAME0 = findActionByName(\"ACTION-NAME1\");";
		return line.replace("ACTION-NAME0", SpellHelper.getUpEname(u.name)).replace("ACTION-NAME1", u.name); 
	} 
	
	public static void main(String [] args) { 
		autoJava("D:\\pic\\elf-xml\\菜园.xml");
	} 
} 

