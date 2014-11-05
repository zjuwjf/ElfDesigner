package com.ielfgame.stupidGame.cocostudio;

import net.sf.json.JSONObject;
import elfEngine.basic.node.ElfNode;

public interface ICocostudioNodeConverter {
	
	public ElfNode create(ElfNode parent, JSONObject jObj);
	public void setData(ElfNode node, JSONObject jObj);
	public boolean isMatch(JSONObject jObj);
}
