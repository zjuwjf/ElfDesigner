package com.ielfgame.stupidGame.cocostudio;

import com.ielfgame.stupidGame.enumTypes.Orientation;

import net.sf.json.JSONObject;
import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.nodeList.ListNode;

public class CocostudioListNodeConverter extends CocostudioImageNodeConverter{
	
	@Override
	public ElfNode create(ElfNode parent, JSONObject jObj) {
		final ListNode node = new ListNode(parent, 0);
		node.onCreateRequiredNodes();
		return node;
	}

	@Override
	public void setData(ElfNode node, JSONObject jObj) {
		final JSONObject options = setDataBasic(node, jObj);
		
		if(options != null) {
			final ListNode list = (ListNode)node;
			
			try {
				final int direction = options.getInt("direction");
				if(direction == 0) {
					list.getContainer().setOrientation(Orientation.Horizontal);
				} else if(direction == 1) {
					list.getContainer().setOrientation(Orientation.Vertical);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean isMatch(JSONObject jObj) {
		final String classname = jObj.getString("classname");
		
		if (classname != null && classname.equals("ListView")) {
			return true;
		}
		
		return false;
	}
}
