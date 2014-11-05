package com.ielfgame.stupidGame.cocostudio;

import net.sf.json.JSONObject;
import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.Scale9Node;

public class CocostudioPanelNodeConverter extends CocostudioImageNodeConverter{
	
	@Override
	public ElfNode create(ElfNode parent, JSONObject jObj) {
		
		if(isPanelScale9Enabled(jObj)) {
			final Scale9Node node = new Scale9Node(parent, 0);
			
			node.setData("IsPanel", true);
			
			node.setDefaultWidth(0);
			node.setDefaultHeight(0);
			
			return node;
		} else {
			final ElfNode node = new ElfNode(parent, 0);
			
			node.setData("IsPanel", true);
			
			node.setDefaultWidth(0);
			node.setDefaultHeight(0);
			
			return node;
		}
	}
	
	@Override
	public void setData(ElfNode node, JSONObject jObj) {
		final JSONObject options = setDataBasic(node, jObj);
		if(options != null) {
			//backGroundImageData
			try {
				JSONObject backGroundImageData = options.getJSONObject("backGroundImageData");
				if(backGroundImageData != null) {
					final String resid = backGroundImageData.getString("path");
					node.setResid(resid);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(isPanelScale9Enabled(jObj)) {
				final Scale9Node scale9 = (Scale9Node)node;
				try {
					final int capInsetsX = Math.round((float)options.getDouble("capInsetsX"));
					final int capInsetsY = Math.round((float)options.getDouble("capInsetsY"));
					final int capInsetsWidth = Math.round((float)options.getDouble("capInsetsWidth"));
					final int capInsetsHeight = Math.round((float)options.getDouble("capInsetsHeight"));
					
					scale9.setCapInsetsX(capInsetsX);
					scale9.setCapInsetsY(capInsetsY);
					scale9.setCapInsetsWidth(capInsetsWidth);
					scale9.setCapInsetsHeight(capInsetsHeight);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				try {
					final int width = options.getInt("scale9Width");
					final int height = options.getInt("scale9Height");
					node.setSize(width, height);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public boolean isMatch(JSONObject jObj) {
		final String classname = jObj.getString("classname");
		
		if (classname != null && classname.equals("Panel")) {
			return true;
		}
		
		return false;
	}
}
