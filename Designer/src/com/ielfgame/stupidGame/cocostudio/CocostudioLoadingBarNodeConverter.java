package com.ielfgame.stupidGame.cocostudio;

import net.sf.json.JSONObject;

import com.ielfgame.stupidGame.data.ElfPointf;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.Scale9Node;

public class CocostudioLoadingBarNodeConverter extends CocostudioImageNodeConverter {
	
	@Override
	public ElfNode create(ElfNode parent, JSONObject jObj) {
		final ElfNode node = new ElfNode(parent, 0);
		node.onCreateRequiredNodes();
		
		node.setUseSettedSize(true);
		
		node.setDefaultWidth(0);
		node.setDefaultHeight(0);
		
		return node;
	}
	
	@Override
	public void setData(ElfNode node, JSONObject jObj) {
		final JSONObject options = setDataBasic(node, jObj);
		if(options != null) {
			final Scale9Node scale9Node = new Scale9Node(node, 0);
			scale9Node.onCreateRequiredNodes();
			
			scale9Node.setName("loadingbar");
			scale9Node.addToParent();
			
			scale9Node.setResid(node.getResid());
			node.setResid(null);
			node.setUseSettedSize(true);
			
			int direction = 0;
			try {
				direction = options.getInt("direction");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			final ElfPointf size = node.getSize();
			
			try {
				final int percent = Math.round( (float)options.getDouble("percent") );
				if(direction == 0 || direction == 2) {
					scale9Node.setPositionX(-size.x/2);
					scale9Node.setAnchorPosition(0, 0.5f);
					scale9Node.setSize(size.x*percent/100f, size.y);
				} else {
					scale9Node.setPositionY(-size.y/2);
					scale9Node.setAnchorPosition(0.5f, 0);
					scale9Node.setSize(size.x, size.y*percent/100f);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(isImageScale9Enabled(jObj)) {
				try {
					final int capInsetsX = Math.round((float)options.getDouble("capInsetsX"));
					final int capInsetsY = Math.round((float)options.getDouble("capInsetsY"));
					final int capInsetsWidth = Math.round((float)options.getDouble("capInsetsWidth"));
					final int capInsetsHeight = Math.round((float)options.getDouble("capInsetsHeight"));
					
					scale9Node.setCapInsetsX(capInsetsX);
					scale9Node.setCapInsetsY(capInsetsY);
					scale9Node.setCapInsetsWidth(capInsetsWidth);
					scale9Node.setCapInsetsHeight(capInsetsHeight);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public boolean isMatch(JSONObject jObj) {
		final String classname = jObj.getString("classname");
		if (classname != null && classname.equals("LoadingBar")) {
			return true;
		}
		
		return false;
	}
	
}
