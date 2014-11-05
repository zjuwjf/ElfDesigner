package com.ielfgame.stupidGame.cocostudio;

import net.sf.json.JSONObject;
import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.nodeText.BMFontNode;

public class CocostudioBMFontNodeConverter extends CocostudioImageNodeConverter {
//LabelBMFont
	
	@Override
	public ElfNode create(ElfNode parent, JSONObject jObj) {
		final BMFontNode node = new BMFontNode(parent, 0);
		node.onCreateRequiredNodes();
		return node;
	}

	@Override
	public void setData(ElfNode node, JSONObject jObj) {
		final JSONObject options = setDataBasic(node, jObj);
		node.setResid(null);
		
		if(options != null) {
			final BMFontNode pNode= (BMFontNode)node;
			
//			"fileNameData": {
//                "path": "bmtfont.proj/vip.fnt",
//                "plistFile": "",
//                "resourceType": 0
//              },
//              "text": "V16"
			
			try {
				final JSONObject fileData = options.getJSONObject("fileNameData");
				if(fileData != null) {
					final String path = fileData.getString("path");
					pNode.setFontPath(path);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				final String text = options.getString("text");
				pNode.setText(text);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean isMatch(JSONObject jObj) {
		final String classname = jObj.getString("classname");
		if (classname != null && classname.equals("LabelBMFont")) {
			return true;
		}
		
		return false;
	}
	
}
