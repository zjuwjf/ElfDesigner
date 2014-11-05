package com.ielfgame.stupidGame.xmlTest;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import org.dom4j.Element;
import org.dom4j.VisitorSupport;

import com.ielfgame.stupidGame.xml.XMLManifest;

public class ReadFrameDelays {
	
	public static void main(final String [] args) {
		final String xmlPath = "D:\\pic\\test\\xml\\AllStars.xml";
		final Element element = XMLManifest.readManifest(xmlPath);
		
		final String nameKey = "@heroaction";
		
		final LinkedList<String> frameDelays = new LinkedList<String>();
		
		if(element != null) {
			element.accept(new VisitorSupport() {
				public void visit(final Element e) {
					final String name = e.attributeValue("Name");
					if(name != null && name.startsWith(nameKey)) {
						final String delay = e.attributeValue("FrameDelay");
						frameDelays.add(delay);
						
						System.err.println(name + " delay : " + delay);
					} 
				}
			}); 
		}
		
		final String dist = "D:\\pic\\gyx\\Ó¢ÐÛ\\";
		
		remove(dist, "HuXi", false);
		
		final String [] list = new File(dist).list(); 
		for(int i=0; i<list.length; i++) { 
			final String delay = frameDelays.getFirst(); 
			frameDelays.removeFirst(); 
			final String newFile = dist + list[i] + "\\" + "hx-"+delay; 
			System.err.println(newFile);
			final File file = new File(newFile);
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
		
//		final String xmlPath2 = "D:\\pic\\test\\Elf Designer\\xml\\Ó¢ÐÛ×Ü.xml";
//		final Element element2 = XMLManifest.readManifest(xmlPath2);
//		
//		final String nameKey2 = "ºôÎü";
//		
//		if(element2 != null) {
//			element2.accept(new VisitorSupport() {
//				public void visit(final Element e) {
//					final String name = e.attributeValue("Name");
//					if(name != null && name.startsWith(nameKey2)) { 
//						if(!frameDelays.isEmpty()) {
//							final String value = frameDelays.getFirst();
//							frameDelays.removeFirst();
//							e.attribute("FrameDelay").setValue(value);
//							System.err.println(name + " delay : " + value);
//						}
//					} 
//				}
//			}); 
//		}
//		
//		XMLManifest.writeDocument(element2.getDocument(), xmlPath2);
		
		System.err.println("ReadFrameDelays completed!");
	} 
	
	
	static void remove(final String name, final String key, boolean equalsOrContainer) {
		final File file = new File(name);
		if(file.exists() && file.isFile()) {
			if(equalsOrContainer) {
				if(file.getName().equals(key)) {
					file.delete();
				}
			} else {
				if(file.getName().contains(key)) {
					file.delete();
				}
			}
		} else if(file.exists() && file.isDirectory()) {
			final String [] list = file.list();
			for(String l : list) {
				remove(name+"\\"+l, key, equalsOrContainer);
			}
		}
	}
} 
