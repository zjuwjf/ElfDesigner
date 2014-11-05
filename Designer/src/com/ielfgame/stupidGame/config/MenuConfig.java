package com.ielfgame.stupidGame.config;

import java.io.File;

import com.ielfgame.stupidGame.data.ElfDataXML;

public class MenuConfig extends ElfDataXML {
	public boolean AutoCleanOnImport = true;
	
//	public boolean SaveCocos;
//	public boolean SaveZip;
//	public boolean SaveLua;
	
	public boolean AutoBatch;
	public boolean AutoNameWhenCopy;
//	public boolean AutoNameWhenCopy;
	public static void main(final String[] args) {
		final File f = new File("G/");
		System.err.println(f.getAbsolutePath());
		System.err.println(f.isAbsolute());
	}
}
