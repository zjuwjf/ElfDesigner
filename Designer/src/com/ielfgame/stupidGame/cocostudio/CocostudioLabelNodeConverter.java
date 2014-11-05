package com.ielfgame.stupidGame.cocostudio;

import com.ielfgame.stupidGame.data.ElfColor;

import net.sf.json.JSONObject;
import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.nodeText.richLabel.LabelNode;

public class CocostudioLabelNodeConverter extends CocostudioImageNodeConverter{
	@Override
	public ElfNode create(ElfNode parent, JSONObject jObj) {
		final LabelNode node = new LabelNode(parent, 0);
		node.onCreateRequiredNodes();
		return node;
	}

	@Override
	public void setData(ElfNode node, JSONObject jObj) {
		final JSONObject options = setDataBasic(node, jObj);
		
		if(options != null) {
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
			
			final LabelNode plabel = (LabelNode)node;
			plabel.setStrokeSize(1);
			plabel.setFontSize(fontSize);
			plabel.setText(text);
			plabel.setKey(text);
			plabel.setTextFont("wenzi.ttf");
			
			final float r = plabel.getColor().red;
			final float g = plabel.getColor().green;
			final float b = plabel.getColor().blue;
			final float a = plabel.getColor().alpha;
			
			plabel.setFillColor(new ElfColor(r,g,b,1));
			plabel.setColor(new ElfColor(1,1,1,a));
		}
		
	}

	@Override
	public boolean isMatch(JSONObject jObj) {
		final String classname = jObj.getString("classname");
		
		if (classname != null && classname.equals("Label")) {
			return true;
		}
		
		return false;
	}
}
