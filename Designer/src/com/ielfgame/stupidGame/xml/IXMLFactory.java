package com.ielfgame.stupidGame.xml;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public interface IXMLFactory {
	
	public String getVersion();
	
	public void writeToCocos(List<Object> exports, final String path);
	
	
	public void writeToXML(List<Object> exports, final String path);
	public LinkedList<Object> readFromXML(final String path);
	public LinkedList<Object> readFromXML(final InputStream is);
	
	public Set<String> getAllResids(final String xmlPath);
	public Set<String> getAllResids(final InputStream is);
	
}
