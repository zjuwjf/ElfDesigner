package com.ielfgame.stupidGame.xml;

import java.io.InputStream;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.Element;

import com.ielfgame.stupidGame.data.ElfPointf;
import com.ielfgame.stupidGame.data.ElfPointi;
import com.ielfgame.stupidGame.data.SpellHelper;
import com.ielfgame.stupidGame.enumTypes.HorizontalTextAlign;
import com.ielfgame.stupidGame.enumTypes.TextAlign;
import com.ielfgame.stupidGame.face.action.CCActionData;
import com.ielfgame.stupidGame.utils.FileHelper;

import elfEngine.basic.node.ElfNode;
import elfEngine.basic.node.ElfNode.IIterateChilds;
import elfEngine.basic.node.nodeText.TextNode;
import elfEngine.basic.node.nodeText.richLabel.LabelNode;

public class NewXMLFactory implements IXMLFactory {

	private final static LinkedList<IXMLItemConverter> sConverterList = new LinkedList<IXMLItemConverter>();
	private final static HashSet<Class<?>> sClassSet = new HashSet<Class<?>>();

	public NewXMLFactory() {
		NewXMLFactory.checkIn(new ActionXML());
		NewXMLFactory.checkIn(new DataModelConverter());
	}

	public static void checkIn(Class<?> selectClass) {
		if (!sClassSet.contains(selectClass)) {
			sClassSet.add(selectClass);
			sConverterList.add(new BasicXMLNode(selectClass));
		}
	}

	public static void checkIn(IXMLItemConverter converter) {
		if (!sClassSet.contains(converter.getClass())) {
			sClassSet.add(converter.getClass());
			sConverterList.add(converter);
		}
	}

	IXMLItemConverter getToXMLConverter(final Object obj) {
		for (IXMLItemConverter converter : sConverterList) {
			if (converter.isMyType(obj)) {
				return converter;
			}
		}
		return null;
	}

	IXMLItemConverter getFromXMLConverter(final Element obj) {
		for (IXMLItemConverter converter : sConverterList) {
			if (converter.isMyLabel(obj)) {
				return converter;
			}
		}
		return null;
	}

	private final static ElfNode labelnode2textnode(final LabelNode labelnode) {
		labelnode.removeFromParent();
		
		final TextNode textnode = new TextNode(labelnode.getParent(), labelnode.ordinal());
		try {
			ElfNode.copyFrom(textnode, labelnode);
		} catch (Exception e) {
		}

		/***
		 * Text Key Dimension TextFont FontSize FontStyle FillColor
		 * HorizontalTextAlign VerticalTextAlign EnableStroke StrokeSize
		 * StrokeColor EnableShadow ShadowOffset ShadowColor
		 * 
		 * 
		 * VerticalAlign TextAlign TextSize TextFont TextStyle Text
		 * Dimensions EnableStroke StrokeSize StrokeColor FillColor
		 */

		textnode.setVerticlaAlign(labelnode.getVerticalTextAlign());
		textnode.setTextAlign(TextAlign.values()[labelnode.getHorizontalTextAlign().ordinal()]);
//		textnode.setTextFont("fzcy.ttf");
		if (FileHelper.IS_WINDOWS) {
			textnode.setTextSize((int)(labelnode.getFontSize()*19/25));
		} else {
			textnode.setTextSize((int)(labelnode.getFontSize()));
		}

		// labelnode.setFontStyle(style);
		textnode.setEnableStroke(labelnode.getEnableStroke());
		textnode.setStrokeSize(labelnode.getStrokeSize());
		textnode.setStrokeColor(labelnode.getStrokeColor());

		final ElfPointi dim = labelnode.getDimension();
		textnode.setDimensions(new ElfPointf(dim.x, dim.y));

		textnode.addToParent();
		
		return textnode;
	}

	private final static ElfNode textnode2labelnode(final TextNode textnode) {
		textnode.removeFromParent();

		final LabelNode labelnode = new LabelNode(textnode.getParent(), textnode.ordinal());

		try {
			ElfNode.copyFrom(labelnode, textnode);
		} catch (Exception e) {
		}

		/***
		 * Text Key Dimension TextFont FontSize FontStyle FillColor
		 * HorizontalTextAlign VerticalTextAlign EnableStroke StrokeSize
		 * StrokeColor EnableShadow ShadowOffset ShadowColor
		 * 
		 * 
		 * VerticalAlign TextAlign TextSize TextFont TextStyle Text
		 * Dimensions EnableStroke StrokeSize StrokeColor FillColor
		 */

		labelnode.setVerticalTextAlign(textnode.getVerticlaAlign());
		labelnode.setHorizontalTextAlign(HorizontalTextAlign.values()[textnode.getTextAlign().ordinal()]);
		labelnode.setTextFont("fzcy.ttf");
		if (FileHelper.IS_WINDOWS) {
			labelnode.setFontSize(textnode.getTextSize() * 25 / 19f);
		} else {
			labelnode.setFontSize(textnode.getTextSize());
		}

		// labelnode.setFontStyle(style);
		labelnode.setEnableStroke(textnode.getStrokeSize() > 0);

		final ElfPointf dim = textnode.getDimensions();
		labelnode.setDimension(new ElfPointi(Math.round(dim.x), Math.round(dim.y)));

		labelnode.addToParent();
		
		return labelnode;
	}
	
	private static boolean sConvertLabel2Text = false;
	private static boolean sConvertText2Label = true;

	public Object readFromElement(final Element element, final Object parent) {
		final IXMLItemConverter converter = getFromXMLConverter(element);
		if (converter != null) {
			Object newObject = converter.createData(parent);
			converter.setDataProperty(newObject, element);

			if (sConvertText2Label && newObject instanceof TextNode) {
				newObject = textnode2labelnode((TextNode)newObject);
			} else if (sConvertLabel2Text && newObject instanceof LabelNode) {
				newObject = labelnode2textnode((LabelNode)newObject);
			}

			// do with childs
			final LinkedList<Object> childList = new LinkedList<Object>();
			final Element[] childElements = converter.getElementChilds(element);
			for (Element childEle : childElements) {
				final Object child = readFromElement(childEle, newObject);
				childList.add(child);
				converter.onChildCreated(newObject, child);
			}

			final Object[] objs = new Object[childList.size()];
			childList.toArray(objs);
			converter.onAllChildsCreated(newObject, objs);

			return newObject;
		} else {
			// not check in!
			if (element == null) {
				System.err.println("Warning : readFromElement element = null!");
			} else {
				System.err.println("Warning : " + element.getName() + " has not been checked in for readFromElement!");
			}
		}

		return null;
	}

	public Element writeToElement(final Object obj, final Element parent) {
		final IXMLItemConverter converter = getToXMLConverter(obj);
		if (converter != null) {
			final Element element = converter.createElement(parent);
			converter.setElemenAttr(obj, element);

			// do with childs
			final Object[] childs = converter.getChilds(obj);
			if (childs != null) {
				for (Object o : childs) {
					writeToElement(o, element);
				}
			}

			return element;
		} else {
			// not check in!
			if (obj == null) {
				System.err.println("Warning : writeToElement obj = null!");
			} else {
				System.err.println("Warning : " + obj.getClass().getSimpleName() + " has not been checked in for writeToElement!");
			}
		}

		return null;
	}

	public String getVersion() {
		return "2.0";
	}

	// NodeTree, Animate,
	public void writeToCocos(List<Object> exports, String path) {
		final Element manifest = XMLManifest.writeManifest(getVersion());
		final Document document = manifest.getDocument();

		for (Object obj : exports) {
			if (obj instanceof CCActionData) {
				CCActionData data = (CCActionData) obj;
				final String oldName = data.getName();
				data.setName(SpellHelper.getUpEname(oldName));
				this.writeToElement(obj, manifest);
				data.setName(oldName);
			} else {
				this.writeToElement(obj, manifest);
			}
		}

		// no need to change

		// document.accept(new VisitorSupport() {
		// public void visit(Element element) {
		// final String label = element.getName();
		// List<?> list = element.attributes();
		// for(Object o : list) {
		// if(o instanceof Attribute) {
		// final Attribute attr = ((Attribute)o);
		// final String func = attr.getName();
		// if( XMLStringManage.isString(label, func) ) {
		// String res = attr.getValue();
		// res = XMLConfig.fromXML(res);
		//
		// if(ResJudge.isRes(res)) {
		// final String newRes = CurrentPlist.toCocosResid(
		// TransferRes.exportCompressPath(res,
		// PowerMan.getSingleton(DataModel.class).isConvertJpg2Png()));
		// attr.setValue(newRes);
		// }
		// } else if( XMLStringManage.isStringArray(label, func) ) {
		// String resArray = attr.getValue();
		//
		// resArray = XMLConfig.fromXML(resArray);
		//
		// final String[] residArray =
		// (String[])Stringified.fromText(String[].class, resArray)[0];
		//
		// if(residArray != null) {
		// for(int i=0; i<residArray.length; i++) {
		// final String res = residArray[i];
		// if(ResJudge.isRes(res)) {
		// final String newRes =
		// CurrentPlist.toCocosResid(TransferRes.exportCompressPath(res,
		// PowerMan.getSingleton(DataModel.class).isConvertJpg2Png()));
		// residArray[i] = newRes;
		// }
		// }
		//
		// final String output = XMLConfig.toXML(Stringified.toText(residArray,
		// false));
		// attr.setValue(output);
		// }
		// }
		// }
		// }
		// }
		//
		// public void visit(Attribute arg0) {
		// if (arg0.getName().equals("TextFont")) {
		// //no change
		// // arg0.setValue("HelveticaNeue");
		// } else if (arg0.getName().equals("ParticlePList")) {
		// final String res = arg0.getValue();
		// arg0.setValue(TransferRes.exportCompressPath(res, false));
		// }
		// }
		// });

		XMLManifest.writeDocument(document, path);
	}

	public void writeToXML(List<Object> exports, String path) {
		final Element manifest = XMLManifest.writeManifest(getVersion());
		final Document document = manifest.getDocument();

		for (Object obj : exports) {
			this.writeToElement(obj, manifest);
		}

		XMLManifest.writeDocument(document, path);
	}

	public LinkedList<Object> readFromXML(String path) {
		final LinkedList<Object> ret = new LinkedList<Object>();
		final Element manifest = XMLManifest.readManifest(path);

		if (manifest != null) {
			final Element[] eles = XMLManifest.getElements(manifest);
			for (int i = 0; i < eles.length; i++) {
				final Object obj = this.readFromElement(eles[i], null);
				ret.add(obj);
			}
		}

		return ret;
	}

	public LinkedList<Object> readFromXML(final InputStream is) {
		final LinkedList<Object> ret = new LinkedList<Object>();
		final Element manifest = XMLManifest.readManifest(is);

		if (manifest != null) {
			final Element[] eles = XMLManifest.getElements(manifest);
			for (int i = 0; i < eles.length; i++) {
				final Object obj = this.readFromElement(eles[i], null);
				ret.add(obj);
			}
		}

		return ret;
	}

	public Set<String> getAllResids(String xmlPath) {
		LinkedList<Object> objs = readFromXML(xmlPath);

		final Set<String> set = new HashSet<String>();
		for (Object obj : objs) {
			if (obj instanceof ElfNode) {
				final ElfNode node = (ElfNode) obj;
				node.iterateChildsDeepWithSelf(new IIterateChilds() {
					public boolean iterate(ElfNode node) {
						set.addAll(node.getResidsSet());
						return false;
					}
				});
			}
		}

		return set;
	}

	public Set<String> getAllResids(InputStream is) {
		LinkedList<Object> objs = readFromXML(is);

		final Set<String> set = new HashSet<String>();
		for (Object obj : objs) {
			if (obj instanceof ElfNode) {
				final ElfNode node = (ElfNode) obj;
				node.iterateChildsDeepWithSelf(new IIterateChilds() {
					public boolean iterate(ElfNode node) {
						set.addAll(node.getResidsSet());
						return false;
					}
				});
			}
		}

		return set;
	}
}
