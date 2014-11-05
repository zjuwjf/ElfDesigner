package com.ielfgame.stupidGame.cocostudio;

import net.sf.json.JSONObject;

import com.ielfgame.stupidGame.data.ElfColor;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.Scale9Node;

public class CocostudioImageNodeConverter implements ICocostudioNodeConverter {

	public boolean isPanelScale9Enabled(JSONObject jObj) {
		final JSONObject options = jObj.getJSONObject("options");
		if (options != null) {
			try {
				if (options.has("backGroundScale9Enable")) {
					final boolean enabled = options.getBoolean("backGroundScale9Enable");
					return enabled;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean isImageScale9Enabled(JSONObject jObj) {
		final JSONObject options = jObj.getJSONObject("options");
		if (options != null) {
			try {
				if (options.has("scale9Enable")) {
					final boolean enabled = options.getBoolean("scale9Enable");
					return enabled;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public JSONObject setDataBasic(ElfNode node, JSONObject jObj) {
		final String name = jObj.getString("name");
		node.setName("" + name);

		final JSONObject options = jObj.getJSONObject("options");
		if (options != null) {
			// "__type":
			// "ImageViewSurrogate:#EditorCommon.JsonModel.Component.GUI",
			// "classname": "ImageView",
			// "name": "Image_4",
			// "anchorPointX": 0.5,
			// "anchorPointY": 0.5,
			// "classType": "CocoStudio.EngineAdapterWrap.CSImageView",
			// "colorB": 255,
			// "colorG": 255,
			// "colorR": 255,
			// "height": 88,
			// "ignoreSize": true,
			// "layoutParameter": null,
			// "opacity": 255,
			// "positionPercentX": 0.5125,
			// "positionPercentY": 0.9923913,
			// "positionType": 0,
			// "rotation": 0,
			// "scaleX": 1,
			// "scaleY": 1,
			// "sizePercentX": 0.59375,
			// "sizePercentY": 0.09565217,
			// "sizeType": 0,
			// "visible": true,
			// "width": 380,
			// "x": 328,
			// "y": 913,
			// "capInsetsHeight": 1,
			// "capInsetsWidth": 1,
			// "capInsetsX": 0,
			// "capInsetsY": 0,
			// "fileName": null,
			// "fileNameData": {
			// "path": "images/img_143.png",
			// "plistFile": "",
			// "resourceType": 0
			// },
			// "scale9Enable": false,
			// "scale9Height": 88,
			// "scale9Width": 380

			try {
				final String name2 = options.getString("name");
				node.setName("" + name2);
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				final float x = (float) (options.getDouble("x"));
				final float y = (float) (options.getDouble("y"));
				node.setPosition(x, y);
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				final float scaleX = (float) (options.getDouble("scaleX"));
				final float scaleY = (float) (options.getDouble("scaleY"));
				node.setScale(scaleX, scaleY);
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				final boolean visible = options.getBoolean("visible");
				node.setVisible(visible);
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				final float rotate = (float) options.getDouble("rotation");
				node.setRotate(rotate);
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				final float anchorX = (float) options.getDouble("anchorPointX");
				final float anchorY = (float) options.getDouble("anchorPointY");
				node.setAnchorPosition(anchorX, anchorY);
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				final int r = options.getInt("colorR");
				final int g = options.getInt("colorG");
				final int b = options.getInt("colorB");
				final int a = options.getInt("opacity");
				final ElfColor color = new ElfColor(r / 255f, g / 255f, b / 255f, a / 255f);
				node.setColor(color);
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				final JSONObject fileData = options.getJSONObject("fileNameData");
				if (fileData != null) {
					final String resid = fileData.getString("path");
					node.setResid(resid);
				}
			} catch (Exception e) {
				// e.printStackTrace();
			}

			try {
				final JSONObject fileData = options.getJSONObject("textureData");
				if (fileData != null) {
					final String resid = fileData.getString("path");
					node.setResid(resid);
				}
			} catch (Exception e) {
				// e.printStackTrace();
			}

			try {
				final int width = options.getInt("width");
				final int height = options.getInt("height");
				node.setSize(width, height);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return options;
	}

	@Override
	public ElfNode create(ElfNode parent, JSONObject jObj) {
		final boolean scale9Enable = isImageScale9Enabled(jObj);
		if (scale9Enable) {
			final Scale9Node node = new Scale9Node(parent, 0);
			
			node.setDefaultWidth(0);
			node.setDefaultHeight(0);
			
			return node;
		} else {
			final ElfNode node = new ElfNode(parent, 0);
			
			node.setDefaultWidth(0);
			node.setDefaultHeight(0);
			
			return node;
		}
	}

	@Override
	public void setData(ElfNode node, JSONObject jObj) {
		final JSONObject options = setDataBasic(node, jObj);

		if (isImageScale9Enabled(jObj)) {
			final Scale9Node scale9 = (Scale9Node) node;

			try {
				final int capInsetsX = Math.round((float) options.getDouble("capInsetsX"));
				final int capInsetsY = Math.round((float) options.getDouble("capInsetsY"));
				final int capInsetsWidth = Math.round((float) options.getDouble("capInsetsWidth"));
				final int capInsetsHeight = Math.round((float) options.getDouble("capInsetsHeight"));

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

	@Override
	public boolean isMatch(JSONObject jObj) {
		final String classname = jObj.getString("classname");

		if (classname != null && classname.equals("ImageView")) {
			return true;
		}

		System.err.println("class = " + classname + " 可能并不正确");

		return true;
	}

}
