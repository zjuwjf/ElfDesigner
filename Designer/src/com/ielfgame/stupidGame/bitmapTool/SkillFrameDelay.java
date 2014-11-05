//package com.ielfgame.stupidGame.bitmapTool;
//
//import java.util.LinkedList;
//
//import com.ielfgame.stupidGame.ConfigImExport;
//import com.ielfgame.stupidGame.config.ElfConfig;
//import com.ielfgame.stupidGame.data.DataModel;
//import com.ielfgame.stupidGame.newNodeMenu.MainNodeMenu;
//import com.ielfgame.stupidGame.xml.XMLVersionManage;
//
//import elfEngine.basic.node.ElfNode;
//import elfEngine.basic.node.ElfNode.IIterateChilds;
//import elfEngine.basic.node.nodeAnimate.SimpleAnimateNode;
//
//public class SkillFrameDelay {
//	
//	public static void main(String [] args) {
//		
//		new DataModel();
//		
//		ConfigImExport.importConfigs();
//		ElfConfig.importElfConfig();
//		
//		new MainNodeMenu();
//		
//		final String old0 = "C:\\Users\\zju_wjf\\Desktop\\SkillAnimate0.xml";
//		final String old1 = "C:\\Users\\zju_wjf\\Desktop\\SkillAnimate2.xml";
//		final String new0 = "C:\\Users\\zju_wjf\\Desktop\\SkillAnimate3.xml";
//		
//		final LinkedList<Object> list0 = XMLVersionManage.readFromXML(old0);
//		final LinkedList<Object> list1 = XMLVersionManage.readFromXML(old1);
//		
//		ElfNode skillanimate0 = null;
//		final int size0 = list0.size();
//		for(int i=0; i<size0; i++) {
//			final Object obj0 = list0.get(i);
//			if(obj0 instanceof ElfNode) {
//				final ElfNode node0 = (ElfNode)obj0;
//				if(node0.getName().equals("skillanimate")) {
//					System.err.println("found0 skillanimate");
//					skillanimate0 = node0;
//				} 
//			} 
//		} 
//		
//		ElfNode skillanimate1 = null;
//		final int size1 = list1.size();
//		for(int i=0; i<size1; i++) {
//			final Object obj1 = list1.get(i);
//			if(obj1 instanceof ElfNode) {
//				final ElfNode node1 = (ElfNode)obj1;
//				if(node1.getName().equals("skillanimate")) {
//					System.err.println("found1 skillanimate");
//					skillanimate1 = node1;
//				} 
//			} 
//		} 
//		
//		if(skillanimate0 != null && skillanimate1 != null) {
//			final ElfNode s1 = skillanimate1;
//			
//			skillanimate0.iterateChildsDeep(new IIterateChilds() {
//				public boolean iterate(ElfNode node) {
//					if(node instanceof SimpleAnimateNode) {
//						final String name = node.getFullName();
//						final ElfNode other = s1.findByName(name);
//						if(other != null && other instanceof SimpleAnimateNode) {
//							System.err.println("found on " + name);
//							((SimpleAnimateNode) node).setFrameDelay((((SimpleAnimateNode)other).getFrameDelay()));
//						} else {
//							System.err.println("error on " + name);
//						}
//					}
//					return false;
//				}
//			});
//		}
//		
//		XMLVersionManage.writeToXML(list0, new0);
//	}
//}
