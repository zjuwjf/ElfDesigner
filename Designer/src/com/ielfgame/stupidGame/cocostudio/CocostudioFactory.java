package com.ielfgame.stupidGame.cocostudio;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.LinkedList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.ielfgame.stupidGame.data.ElfPointf;
import com.ielfgame.stupidGame.utils.FileHelper;
import com.ielfgame.stupidGame.xml.XMLVersionManage;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.nodeList.ListNode;

public class CocostudioFactory {
	
	private static LinkedList<ICocostudioNodeConverter> sConverterList = new LinkedList<ICocostudioNodeConverter>();
	
	public static void check(ICocostudioNodeConverter converter) {
		sConverterList.add(converter);
	}
	
	static {
		check(new CocostudioLoadingBarNodeConverter());
		check(new CocostudioBMFontNodeConverter());
		check(new CocostudioCheckBoxNodeConverter());
		
		check(new CocostudioButtonNodeConverter());
		check(new CocostudioListNodeConverter());
		check(new CocostudioLabelNodeConverter());
		
		check(new CocostudioPanelNodeConverter());
		
		check(new CocostudioImageNodeConverter());
	}
	
	public static ICocostudioNodeConverter findConverter(JSONObject jObj) {
		for(ICocostudioNodeConverter converter : sConverterList) {
			if(converter.isMatch(jObj)) {
				return converter;
			}
		}
		return null;
	}
	
	public static boolean isIgnoreSize(JSONObject jObj) {
		boolean ignoreSize = false;
		if(jObj != null) {
			try {
				final JSONObject options = jObj.getJSONObject("options");
				if(options != null) {
					ignoreSize = options.getBoolean("ignoreSize");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return ignoreSize;
	}
	
	public static ElfNode json2node(final ElfNode parent, final JSONObject jObj) {
		final ICocostudioNodeConverter ic = findConverter(jObj);
		if(ic != null) {
			final ElfNode node = ic.create(parent, jObj);
			node.setData("JSONObject", jObj);
			ic.setData(node, jObj);
			
			//add child ???
			node.setParent(parent);
			node.addToParent();
			
			final JSONObject parentJson = (JSONObject)node.getData("JSONObject");
			boolean ignoreSize = isIgnoreSize(parentJson);
			
			final ElfPointf nodeSize = node.getSize();
			
			final JSONArray jArray = jObj.getJSONArray("children");
			if(jArray != null) {
				final int size = jArray.size();
				
				ElfNode chParent = node;
				if(node instanceof ListNode) {
					chParent = ((ListNode)node).getContainer();
				}
				
				final Object isPanel = chParent.getData("IsPanel");
				
				for(int i=0; i<size; i++) {
					final JSONObject chJObj = jArray.getJSONObject(i);
					if(chJObj != null) {
						final ElfNode child = json2node(chParent, chJObj);
						if(child != null) {
							if(isPanel != null) {
								child.translate(-nodeSize.x/2, -nodeSize.y/2);
							} 
//							else if(ignoreSize) {
//								child.translate(-nodeSize.x/2, -nodeSize.y/2);
//							} 
						}
//						if(chParent instanceof Scale9Node) {
//							child.translate(nodeSize.x/2, nodeSize.y/2);
//						}
					}
				}
			}
			
			return node;
		}
		return null;
	}
	
	public static LinkedList<Object> readFromJson(final String jsonPath) {
		try {
			final InputStream is = new FileInputStream(new File(jsonPath));
			final String data = FileHelper.readInputStream(is);
			
			try {
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			final JSONObject jObj = JSONObject.fromObject(data);
			
			final JSONObject rootJson = jObj.getJSONObject("widgetTree");
			
			final ElfNode root = json2node(null, rootJson);
			
			root.setPosition(-640/2, -1136/2);
			
			final LinkedList<Object> list = new LinkedList<Object>();
			list.add(root);
			
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void json2xml(final String jsonPath, final String xmlPath) {
		if(jsonPath != null && jsonPath.endsWith(".json")) {
			final LinkedList<Object> list = readFromJson(jsonPath);
			if(list != null) {
				XMLVersionManage.writeToXML(list, xmlPath);
				System.err.println("Convert "+jsonPath+" success!");
			} else {
				System.err.println("Convert "+jsonPath+" failed!");
			}
		}
	}
	
	
	public static void main(String [] args) {
		readFromJson("D:\\work\\gl_client\\develop\\editor\\Resources\\json\\activey-list.json");
	}

}
