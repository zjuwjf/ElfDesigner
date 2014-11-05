package com.ielfgame.stupidGame.lua;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.ielfgame.stupidGame.config.ElfConfig;
import com.ielfgame.stupidGame.data.ElfDataXML;
import com.ielfgame.stupidGame.dialog.MultiLineDialog;
import com.ielfgame.stupidGame.power.PowerMan;

public class ToLuaMain {
	
	public static void main(String [] args) {
		//
		final String SCR_PATH = "D:\\svn-code\\NewWorld\\MyTest\\proj.win32\\tolua_elf_tmp.cpp";
		final String DST_PATH = "D:\\svn-code\\NewWorld\\MyTest\\Extends\\tolua_elf.cpp";
		
		final File file = new File(SCR_PATH);
		if(!file.exists()) {
			System.err.println("not found:"+SCR_PATH);
		} 

		BufferedReader reader = null;
		BufferedWriter output = null;
		
		try {
			reader = new BufferedReader(new FileReader(file));
			output = new BufferedWriter(new FileWriter(new File(DST_PATH)));
			
			String line = null;
//			String lastLine = null;
			while ((line = reader.readLine()) != null) {
				if(line.contains("LUA_FUNCTION")) {
					final String key1 = "*((LUA_FUNCTION*)  tolua_tousertype";
					final String key2 = "\"LUA_FUNCTION\"";
					final String key3 = "tolua_isusertype";
					
					if(line.contains(key1)) {
						line = line.replace(key1, "(tolua_ref_function");
						line = line.replace("LUA_FUNCTION", "int");
						
					} else if(line.contains(key2)) {
						if(line.contains(key3)) {
							line = "     !tolua_isfunction(tolua_S,2,&tolua_err) ||";
						} else {
							line = "//" + line;
						} 
					} 
				} else if( line.contains("tolua_pushusertype(tolua_S,(void*)tolua_ret,") ) { 
//				    tolua_pushusertype(tolua_S,(void*)tolua_ret,"TouchNode");
					//
//					int nID = (tolua_ret) ? tolua_ret->m_uID : -1; 
//				    int* pLuaID = (tolua_ret) ? &tolua_ret->m_nLuaID : NULL; 
//				    tolua_pushusertype_ccobject(tolua_S, nID, pLuaID, (void*)tolua_ret,"CCObject"); 
					//except 
					//
					
					if(!line.contains("Interpolator") &&
							!line.contains("XMLFactory") && !line.contains("TiXml")) {
						System.err.println("catch : " + line);
						
						line = line.replace("tolua_pushusertype(tolua_S,(void*)tolua_ret,", 
								"int nID = (tolua_ret) ? tolua_ret->m_uID : -1;\n"+
										"\tint* pLuaID = (tolua_ret) ? &tolua_ret->m_nLuaID : NULL;\n"+
										"\ttolua_pushusertype_ccobject(tolua_S, nID, pLuaID, (void*)tolua_ret,"
								); 
					} 
				} 
				
				output.write(line+"\n"); 
//				lastLine = line; 
			} 
			
			output.close(); 
		} catch (Exception e) {
		} 
		
		System.err.println("ToLuaMain Done!");
	}
	
	public static class ToLuaCppData extends ElfDataXML {
		public String tmp_cpp_REF_FILE_cpp = "D:\\svn-code\\NewWorld\\MyTest\\proj.win32\\tolua_elf_tmp.cpp";
		public String tolua_cpp_REF_FILE_cpp = "D:\\svn-code\\NewWorld\\MyTest\\Extends\\tolua_elf.cpp";
	}
	
	public final static void convert() { 
		
		final ToLuaCppData data = PowerMan.getSingleton(ToLuaCppData.class);
		final MultiLineDialog dialog = new MultiLineDialog();
		final Object[] ret = dialog.open(data);
		if (ret != null) {
			data.setValues(ret);
			// save
			ElfConfig.exportElfConfig();
			
			final String SCR_PATH = data.tmp_cpp_REF_FILE_cpp;
			final String DST_PATH = data.tolua_cpp_REF_FILE_cpp;
			
			try {
				final File file = new File(SCR_PATH);
				if(!file.exists()) { 
					System.err.println("not found:"+SCR_PATH); 
				} 

				BufferedReader reader = null;
				BufferedWriter output = null;
				
				try {
					reader = new BufferedReader(new FileReader(file));
					output = new BufferedWriter(new FileWriter(new File(DST_PATH)));
					
					String line = null;
					while ((line = reader.readLine()) != null) {
						if(line.contains("LUA_FUNCTION")) {
							final String key1 = "*((LUA_FUNCTION*)  tolua_tousertype";
							final String key2 = "\"LUA_FUNCTION\"";
							final String key3 = "tolua_isusertype";
							
							if(line.contains(key1)) {
								line = line.replace(key1, "(tolua_ref_function");
								line = line.replace("LUA_FUNCTION", "int");
								
							} else if(line.contains(key2)) {
								if(line.contains(key3)) {
									line = "     !tolua_isfunction(tolua_S,2,&tolua_err) ||";
								} else {
									line = "//" + line;
								} 
							} 
						} else if( line.contains("tolua_pushusertype(tolua_S,(void*)tolua_ret,") ) { 
//						    tolua_pushusertype(tolua_S,(void*)tolua_ret,"TouchNode");
							//
//							int nID = (tolua_ret) ? tolua_ret->m_uID : -1; 
//						    int* pLuaID = (tolua_ret) ? &tolua_ret->m_nLuaID : NULL; 
//						    tolua_pushusertype_ccobject(tolua_S, nID, pLuaID, (void*)tolua_ret,"CCObject"); 
							//except 
							//
							
							if(!line.contains("Interpolator") &&
									!line.contains("XMLFactory") && !line.contains("TiXml")) {
								System.err.println("catch : " + line);
								
								line = line.replace("tolua_pushusertype(tolua_S,(void*)tolua_ret,", 
										"int nID = (tolua_ret) ? tolua_ret->m_uID : -1;\n"+
												"\tint* pLuaID = (tolua_ret) ? &tolua_ret->m_nLuaID : NULL;\n"+
												"\ttolua_pushusertype_ccobject(tolua_S, nID, pLuaID, (void*)tolua_ret,"
										); 
							} 
						} 
						
						output.write(line+"\n"); 
//						lastLine = line; 
					} 
					
					output.close(); 
				} catch (Exception e) {
				} 
				
				System.err.println("ToLuaMain Done!");
			} catch (Exception e) {
			}
		}
	}
}
