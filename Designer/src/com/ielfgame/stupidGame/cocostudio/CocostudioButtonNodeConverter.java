package com.ielfgame.stupidGame.cocostudio;

import net.sf.json.JSONObject;

import com.ielfgame.stupidGame.data.ElfColor;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.Scale9Node;
import elfEngine.basic.node.nodeText.richLabel.LabelNode;
import elfEngine.basic.node.nodeTouch.ClickNode;

public class CocostudioButtonNodeConverter extends CocostudioImageNodeConverter{

	@Override
	public ElfNode create(ElfNode parent, JSONObject jObj) {
		final ClickNode node = new ClickNode(parent, 0);
		node.onCreateRequiredNodes();
		node.setUseSettedSize(true);
		
		node.setDefaultWidth(0);
		node.setDefaultHeight(0);
		
		return node;
	}
	
	public void setData(final ElfNode node, JSONObject jObj) {
//		"disabledData": {
//        "path": null,
//        "plistFile": null,
//        "resourceType": 0
//      },
//      "fontName": "微软雅黑",
//      "fontSize": 14,
//      "fontType": 0,
//      "normal": null,
//      "normalData": {
//        "path": "button/btn_113.png",
//        "plistFile": "",
//        "resourceType": 0
//      },
//      "pressed": null,
//      "pressedData": {
//        "path": null,
//        "plistFile": null,
//        "resourceType": 0
//      },
//      "scale9Enable": false,
//      "scale9Height": 65,
//      "scale9Width": 74,
//      "text": "",
//      "textColorB": 255,
//      "textColorG": 255,
//      "textColorR": 255
		
		final JSONObject options = setDataBasic(node, jObj);
		
		final ClickNode click = (ClickNode)node;
		
		String resid = null;
		try {
			final JSONObject normalObj = options.getJSONObject("normalData");
			if(normalObj != null) {
				resid = normalObj.getString("path");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		final ElfNode normal = click.getNormalNode();
		final ElfNode pressed = click.getPressedNode();
		final ElfNode invalid = click.getInvalidNode();
		
		pressed.setScale(1.2f, 1.2f);
		invalid.setColor(new ElfColor(0.3f,0.3f,0.3f,1));
		
		if(isImageScale9Enabled(jObj)) {
			final ElfNode [] parents = new ElfNode[] {normal, pressed, invalid} ;
			for(ElfNode parent : parents) {
				final Scale9Node scale9Node = new Scale9Node(parent, 0);
				scale9Node.addToParent();
				scale9Node.setResid(resid);
				
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
				
				scale9Node.setSize(node.getSize());
				
				try {
					final int width = options.getInt("scale9Width");
					final int height = options.getInt("scale9Height");
					scale9Node.setSize(width, height);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			normal.setResid(resid);
			pressed.setResid(resid);
			invalid.setResid(resid);
		}
		
		int fontSize = 20;
		try {
			fontSize = options.getInt("fontSize");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String text = null;
		try {
			text = options.getString("text");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int r = 255;
		int g = 255;
		int b = 255;
		try {
			r = options.getInt("textColorR");
			g = options.getInt("textColorG");
			b = options.getInt("textColorB");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		final LabelNode nlabel = new LabelNode(normal, 0);
		nlabel.setStrokeSize(1);
		nlabel.setFontSize(fontSize);
		nlabel.setText(text);
		nlabel.setKey(text);
		nlabel.setTextFont("wenzi.ttf");
		nlabel.setFillColor(new ElfColor(r/255f,g/255f,b/255f,1));
		nlabel.addToParent();
		
		final LabelNode plabel = new LabelNode(pressed, 0);
		plabel.setStrokeSize(1);
		plabel.setFontSize(fontSize);
		plabel.setText(text);
		plabel.setKey(text);
		plabel.setTextFont("wenzi.ttf");
		plabel.setFillColor(new ElfColor(r/255f,g/255f,b/255f,1));
		plabel.addToParent();
		
		final LabelNode ilabel = new LabelNode(invalid, 0);
		ilabel.setStrokeSize(1);
		ilabel.setFontSize(fontSize);
		ilabel.setText(text);
		ilabel.setKey(text);
		ilabel.setTextFont("wenzi.ttf");
		ilabel.setFillColor(new ElfColor(r/255f,g/255f,b/255f,1));
		ilabel.addToParent();
		
		click.onHide(click);
		
		click.setUseSettedSize(true);
		click.setSize(normal.getSize());
	}
	
	@Override
	public boolean isMatch(JSONObject jObj) {
		final String classname = jObj.getString("classname");
		
		if (classname != null && classname.equals("Button")) {
			return true;
		}
		return false;
	}
}
