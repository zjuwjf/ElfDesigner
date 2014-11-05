package com.ielfgame.stupidGame.xml;

import com.ielfgame.stupidGame.data.DataModel;
import com.ielfgame.stupidGame.power.PowerMan;

import elfEngine.basic.node.ElfNode;

public class ConverterHelper { 
	
	public static class Mainfest implements IXMLChilds{
		private String mVersion = "2.0";
		public String getVersion() {
			return mVersion;
		} 
		
		public void setVersion(String mVersion) { 
			this.mVersion = mVersion; 
		} 
		
		public Object[] getChildsForXML() { 
			return new Object[] { new NodeTree() };
		} 
	} 
	
	public static class MainfestConverter extends AbstractXMLItemConverter{
		public MainfestConverter() { 
			super(Mainfest.class);
			
			this.addXMLAttr(Mainfest.class, "Version");
		} 
		
		public Object createData(Object parent) { 
			return new Mainfest(); 
		} 
	} 
	
	public static class NodeTree implements IXMLChilds{ 
		public Object[] getChildsForXML() {
			final ElfNode node = PowerMan.getSingleton(DataModel.class).getScreenNode().getBindNode();
			if(node == null) {
				return null;
			} else {
				return node.getChilds();
			} 
		}
	} 
	
	public static class NodeTreeConverter extends AbstractXMLItemConverter {

		public NodeTreeConverter() { 
			super(NodeTree.class);
		} 
		
		public Object createData(Object parent) { 
			return new NodeTree(); 
		} 
	} 
}
