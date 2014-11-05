package com.ielfgame.stupidGame.cocostudio;

import net.sf.json.JSONObject;
import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.nodeTouch.CheckBoxNode;

public class CocostudioCheckBoxNodeConverter extends CocostudioImageNodeConverter {
	@Override
	public ElfNode create(ElfNode parent, JSONObject jObj) {
		final CheckBoxNode node = new CheckBoxNode(parent, 0);
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
			final CheckBoxNode pNode= (CheckBoxNode)node;
			
			try {
				final String path = options.getString("backGroundBox");
				pNode.getNormalNode().setResid(path);
				pNode.getPressedNode().setResid(path);
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				final JSONObject backGroundBoxData = options.getJSONObject("backGroundBoxData");
				if(backGroundBoxData != null) {
					final String path = backGroundBoxData.getString("path");
					pNode.getNormalNode().setResid(path);
					pNode.getPressedNode().setResid(path);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			try {
				final String path = options.getString("backGroundBoxDisabled");
				pNode.getInvalidNode().setResid(path);
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				final JSONObject backGroundBoxData = options.getJSONObject("backGroundBoxDisabledData");
				if(backGroundBoxData != null) {
					final String path = backGroundBoxData.getString("path");
					pNode.getInvalidNode().setResid(path);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			final ElfNode invalidCross = new ElfNode(pNode.getInvalidNode(), 0);
			invalidCross.addToParent();
			try {
				final String path = options.getString("frontCrossDisabled");
				invalidCross.setResid(path);
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				final JSONObject backGroundBoxData = options.getJSONObject("frontCrossDisabledData");
				if(backGroundBoxData != null) {
					final String path = backGroundBoxData.getString("path");
					invalidCross.setResid(path);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				final String path = options.getString("backGroundBoxSelected");
				pNode.getSelectNode().setResid(path);
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				final JSONObject backGroundBoxSelectedData = options.getJSONObject("backGroundBoxSelectedData");
				if(backGroundBoxSelectedData != null) {
					final String path = backGroundBoxSelectedData.getString("path");
					pNode.getSelectNode().setResid(path);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			final ElfNode selectCross = new ElfNode(pNode.getSelectNode(), 0);
			selectCross.addToParent();
			try {
				final String path = options.getString("frontCross");
				selectCross.setResid(path);
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				final JSONObject backGroundBoxSelectedData = options.getJSONObject("frontCrossData");
				if(backGroundBoxSelectedData != null) {
					final String path = backGroundBoxSelectedData.getString("path");
					selectCross.setResid(path);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				final Boolean b = options.getBoolean("selectedState");
				pNode.setStateSelected(b);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			pNode.setSize(pNode.getNormalNode().getSize());
		}
	}

	@Override
	public boolean isMatch(JSONObject jObj) {
		final String classname = jObj.getString("classname");
		if (classname != null && classname.equals("CheckBox")) {
			return true;
		}
		
		return false;
	}
}
