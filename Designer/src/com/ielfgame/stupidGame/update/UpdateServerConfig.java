package com.ielfgame.stupidGame.update;

import com.ielfgame.stupidGame.data.ElfDataXML;

public class UpdateServerConfig extends ElfDataXML{
	/***
	 * ftp:
	 * url
	 * port
	 * user
	 * password
	 * 
	 * remotePath -> windows or mac
	 * max version 
	 * 
	 * old 
	 * 
	 */
	
	public enum UrlType {
		ftp, http
	} ;
	
	public UrlType type = UrlType.ftp;
	public String url = "";
	public int port;
	public String user;
	public String password;
	public String remotePath; 
	public String fileName;
//	public 
	
}
