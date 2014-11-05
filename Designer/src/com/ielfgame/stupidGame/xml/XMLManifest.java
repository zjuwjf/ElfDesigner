package com.ielfgame.stupidGame.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * @author zju_wjf
 *
 */
public final class XMLManifest { 
	
	public static Element readManifest(final String xmlPath) {
		try {
			return readManifest(new FileInputStream(new File(xmlPath)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		return null;
	} 
	
	public static Element readManifest(final InputStream is) {
		SAXReader saxReader = new SAXReader();
		Document document = null;
		try { 
			document = saxReader.read(is);
			final Element element = document.getRootElement(); 
			final String label = element.getName();
			if( label.equals("manifest") ) { 
				return element; 
			} 
		} catch (Exception e) { 
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	} 
	
	public static String getVersion(final String xmlPath) {
		try {
			final Element element = readManifest(xmlPath);
			return element.attributeValue("version");
		} catch (Exception e) {
		}
		return "";
	} 
	
	public static Element [] getElements(final Element element) { 
		@SuppressWarnings("rawtypes")
		final List eles = element.elements();
		final Element[] ret = new Element[eles.size()]; 
		for (int i = 0; i < ret.length; i++) { 
			ret[i] = (Element) eles.get(i); 
		} 
		return ret; 
	} 
	
	/**
	 * @param version
	 * @return check inside
	 */
	public static Element writeManifest(final String version) { 
		final Document document = DocumentHelper.createDocument(); 
		final Element root = document.addElement("manifest"); 
		root.addAttribute("version", version); 
		
		return root; 
	}
	
	public static void writeDocument(final Document document, final String path) {
		OutputFormat format = OutputFormat.createPrettyPrint();
		XMLWriter writer;
		try {
			writer = new XMLWriter(new FileOutputStream(path), format);
			writer.write(document);
			writer.flush();
			writer.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		} 
	} 
	
} 
