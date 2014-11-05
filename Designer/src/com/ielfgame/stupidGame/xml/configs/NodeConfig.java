package com.ielfgame.stupidGame.xml.configs;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.ielfgame.stupidGame.data.Stringified;
import com.ielfgame.stupidGame.reflect.ElfReflect;
import com.ielfgame.stupidGame.reflect.ElfReflect.ReflectUion;
import com.ielfgame.stupidGame.utils.FileHelper;
import com.ielfgame.stupidGame.utils.SystemHelper;
import com.ielfgame.stupidGame.xml.XMLManifest;

import elfEngine.basic.node.ElfNode;

public class NodeConfig { 
	
	private static final String Folder = FileHelper.DECOLLATOR+"node_configs"+FileHelper.DECOLLATOR;
	private static final String Suffix = ".xml";
	
	public static void initNode(ElfNode node) {
		if(node != null) { 
			final Class<?> _class = node.getClass();
			final String nodeLabel = _class.getSimpleName();
			
			final String path = SystemHelper.USER_DIR + Folder + nodeLabel + Suffix;
			final File file = new File(path);
			
			if( file.exists() ) {
				SAXReader saxReader = new SAXReader();
				Document document = null;
				try { 
					document = saxReader.read(file);
					final Element element = document.getRootElement();
					final String label = element.getName();
					
					if( label.equals(nodeLabel) ) {
						final ReflectUion [] rfs = ElfReflect.getReflects(_class);
						for(ReflectUion rf : rfs) {
							//����
							if(rf.funcName.equals("PositionInScreen")) { 
								continue;
							} 
							
							try {
								final String text = element.attributeValue(rf.funcName);
								final Object obj = Stringified.fromText(rf.type, text)[0];
								ElfReflect.set(rf, obj, node);
							} catch (Exception e) { 
							} 
						} 
					} 
				} catch (Exception e) { 
				} 
			} 
		} 
	} 
	
	public static void saveConfigs(final Class<?> _class, final Object instance) { 
		if(_class != null && instance != null && _class.isAssignableFrom(instance.getClass())) {
			final String nodeLabel = _class.getSimpleName();
			
			final String dirPath = SystemHelper.USER_DIR + Folder;
			final String path = dirPath + nodeLabel + Suffix;
			final File file = new File(path);
			
			if( file.exists() ) {
				//modifier
				SAXReader saxReader = new SAXReader();
				Document document = null;
				try { 
					document = saxReader.read(file);
					final Element element = document.getRootElement();
					element.setName(nodeLabel); 
					
					final ReflectUion [] rfs = ElfReflect.getReflects(_class);
					for(ReflectUion rf : rfs) {
						
						if(((rf.mask & ElfNode.XML_GET_SHIFT) != 0) || ((rf.mask & ElfNode.XML_SET_SHIFT) != 0) || ((rf.mask & ElfNode.XML_ALL_SHIFT) != 0)
								|| (rf.mask & ElfNode.FACE_SET_SHIFT) != 0|| (rf.mask & ElfNode.FACE_ALL_SHIFT) != 0
								) {
							
							//����
							if(rf.funcName.equals("PositionInScreen")) {
								continue;
							} 
							
							try {
								String text = "";
								try {
									text = element.attributeValue(rf.funcName);
								} catch (Exception e) {
								}
								Object obj = null;
								try {
									obj = Stringified.fromText(rf.type, text)[0];
								} catch (Exception e) {
								} 
								
								if(obj == null) { 
									element.addAttribute(rf.funcName, Stringified.toText(ElfReflect.get(rf, instance), false));
								} 
							} catch (Exception e) { 
							} 
						} 
					} 
					XMLManifest.writeDocument(document, path);
				} catch (Exception e) {
				} 
			} else { 
				//new 
				final File dir = new File(dirPath);
				if(!dir.exists()) {
					dir.mkdir();
				}
				
				try {
					final Document document = DocumentHelper.createDocument(); 
					final Element element = document.addElement(nodeLabel); 
					
					final ReflectUion [] rfs = ElfReflect.getReflects(_class);
					for(ReflectUion rf : rfs) {
						if(((rf.mask & ElfNode.XML_GET_SHIFT) != 0) || ((rf.mask & ElfNode.XML_SET_SHIFT) != 0) || ((rf.mask & ElfNode.XML_ALL_SHIFT) != 0)
								|| (rf.mask & ElfNode.FACE_SET_SHIFT) != 0|| (rf.mask & ElfNode.FACE_ALL_SHIFT) != 0
								) {
							
							//����
							if(rf.funcName.equals("PositionInScreen")) {
								continue;
							} 
							
							try { 
								element.addAttribute(rf.funcName, Stringified.toText(ElfReflect.get(rf, instance), false));
							} catch (Exception e) {  
								e.printStackTrace();
							} 
						}
					} 
					XMLManifest.writeDocument(document, path);
				} catch (Exception e) { 
				} 
			} 
		} 
	} 
} 

